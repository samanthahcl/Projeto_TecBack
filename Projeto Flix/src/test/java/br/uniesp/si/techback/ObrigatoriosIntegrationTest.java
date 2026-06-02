package br.uniesp.si.techback;

import br.uniesp.si.techback.model.MetodoPagamento;
import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.ConteudoRepository;
import br.uniesp.si.techback.repository.MetodoPagamentoRepository;
import br.uniesp.si.techback.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ObrigatoriosIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @Autowired
    private ConteudoRepository conteudoRepository;

    @Test
    void deveExecutarFluxosObrigatoriosComAutenticacao() throws Exception {
        String email = "maria.integration@example.com";
        String senha = "Senha@123";

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomeCompleto": "Maria da Silva",
                                  "dataNascimento": "1995-05-20",
                                  "email": "maria.integration@example.com",
                                  "senha": "Senha@123",
                                  "confirmarSenha": "Senha@123",
                                  "cpfCnpj": "52998224725",
                                  "numeroCartao": "4111111111111111",
                                  "validadeCartao": "12/2099",
                                  "codigoSegurancaCartao": "123",
                                  "nomeTitularCartao": "Maria da Silva"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.perfil").value("USER"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.numeroCartao").doesNotExist())
                .andExpect(jsonPath("$.codigoSegurancaCartao").doesNotExist());

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        MetodoPagamento metodoPagamento = metodoPagamentoRepository.findAllByUsuarioId(usuario.getId()).get(0);
        assertThat(usuario.getSenhaHash()).startsWith("$2");
        assertThat(metodoPagamento.getUltimos4()).isEqualTo("1111");
        assertThat(metodoPagamento.getTokenGateway()).isNotBlank();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "maria.integration@example.com",
                                  "senha": "Senha@123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        String atualizacaoCartao = """
                {
                  "numeroCartao": "5555555555554444",
                  "validadeCartao": "12/2099",
                  "codigoSegurancaCartao": "321",
                  "nomeTitularCartao": "Maria da Silva"
                }
                """;

        mockMvc.perform(put("/api/v1/metodos-pagamento/{id}", metodoPagamento.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoCartao))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/v1/metodos-pagamento/{id}", metodoPagamento.getId())
                        .with(httpBasic(email, senha))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoCartao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ultimos4").value("4444"))
                .andExpect(jsonPath("$.tokenGateway").doesNotExist());

        String conteudo = """
                {
                  "titulo": "Filme de Integração",
                  "tipo": "FILME",
                  "ano": 2024,
                  "duracaoMinutos": 120,
                  "relevancia": 9.0,
                  "sinopse": "Teste integrado",
                  "trailerUrl": "https://example.com/trailer",
                  "genero": "Drama"
                }
                """;

        mockMvc.perform(post("/api/v1/conteudos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conteudo))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/conteudos")
                        .with(httpBasic(email, senha))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conteudo))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/conteudos")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conteudo))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/conteudos")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conteudo.replace("Filme de Integração", "Segundo Filme de Integração")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/conteudos")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conteudo.replace("\"FILME\"", "\"STRING\"")))
                .andExpect(status().isBadRequest());

        Conteudo conteudoCadastrado = conteudoRepository.buscarPorTitulo("Filme de Integração").get(0);

        mockMvc.perform(get("/api/v1/conteudos/{id}", conteudoCadastrado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Filme de Integração"))
                .andExpect(jsonPath("$.tipo").value("FILME"))
                .andExpect(jsonPath("$.genero").value("Drama"))
                .andExpect(jsonPath("$.ano").value(2024))
                .andExpect(jsonPath("$.duracaoMinutos").value(120))
                .andExpect(jsonPath("$.relevancia").value(9.0))
                .andExpect(jsonPath("$.sinopse").value("Teste integrado"))
                .andExpect(jsonPath("$.trailerUrl").value("https://example.com/trailer"));

        mockMvc.perform(post("/api/v1/favoritos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": %d,
                                  "conteudoId": %d
                                }
                                """.formatted(usuario.getId(), conteudoCadastrado.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/favoritos/usuario/{id}/filmes", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Filme de Integração"));

        mockMvc.perform(get("/api/v1/favoritos/usuario/{id}/series", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private RequestPostProcessor httpBasic(String username, String password) {
        String credentials = username + ":" + password;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return request -> {
            request.addHeader("Authorization", "Basic " + encoded);
            return request;
        };
    }
}
