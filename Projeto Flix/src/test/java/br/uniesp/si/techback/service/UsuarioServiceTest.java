package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.CadastroUsuarioDTO;
import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private MetodoPagamentoService metodoPagamentoService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuarioComHashPerfilUserECartaoTokenizado() {
        CadastroUsuarioDTO dto = cadastroValido();
        when(passwordEncoder.encode("Senha@123")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(10L);
            return usuario;
        });

        UsuarioDTO resposta = usuarioService.criar(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        verify(metodoPagamentoService).cadastrarCartaoInicial(10L, dto);
        assertThat(captor.getValue().getSenhaHash()).isEqualTo("hash-bcrypt");
        assertThat(captor.getValue().getPerfil()).isEqualTo("USER");
        assertThat(resposta.getId()).isEqualTo(10L);
        assertThat(resposta.getSenha()).isNull();
    }

    @Test
    void deveRejeitarConfirmacaoDeSenhaDiferente() {
        CadastroUsuarioDTO dto = cadastroValido();
        dto.setConfirmarSenha("Outra@123");

        assertThatThrownBy(() -> usuarioService.criar(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Senha e confirmação de senha não conferem");

        verify(usuarioRepository, never()).save(any());
        verify(metodoPagamentoService, never()).cadastrarCartaoInicial(any(), any());
    }

    private CadastroUsuarioDTO cadastroValido() {
        CadastroUsuarioDTO dto = new CadastroUsuarioDTO();
        dto.setNomeCompleto("Maria da Silva");
        dto.setDataNascimento(LocalDate.of(1995, 5, 20));
        dto.setEmail("maria@example.com");
        dto.setSenha("Senha@123");
        dto.setConfirmarSenha("Senha@123");
        dto.setCpfCnpj("52998224725");
        dto.setNumeroCartao("4111111111111111");
        dto.setValidadeCartao("12/2030");
        dto.setCodigoSegurancaCartao("123");
        dto.setNomeTitularCartao("Maria da Silva");
        return dto;
    }
}
