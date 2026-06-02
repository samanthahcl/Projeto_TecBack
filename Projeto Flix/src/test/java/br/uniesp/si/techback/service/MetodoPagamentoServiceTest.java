package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.AtualizacaoCartaoDTO;
import br.uniesp.si.techback.dto.MetodoPagamentoDTO;
import br.uniesp.si.techback.model.MetodoPagamento;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.MetodoPagamentoRepository;
import br.uniesp.si.techback.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetodoPagamentoServiceTest {

    @Mock
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MetodoPagamentoService metodoPagamentoService;

    @Test
    void deveAtualizarCartaoDoUsuarioAutenticadoSemPersistirNumeroCompleto() {
        MetodoPagamento metodo = metodoPagamento();
        when(metodoPagamentoRepository.findById(1L)).thenReturn(Optional.of(metodo));
        when(usuarioRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(usuario(10L)));
        when(metodoPagamentoRepository.save(any(MetodoPagamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MetodoPagamentoDTO resposta = metodoPagamentoService.atualizar(
                1L,
                atualizacaoValida(),
                "maria@example.com"
        );

        assertThat(resposta.getBandeira()).isEqualTo("MASTERCARD");
        assertThat(resposta.getUltimos4()).isEqualTo("4444");
        assertThat(resposta.getTokenGateway()).isNotBlank();
        assertThat(metodo.getUltimos4()).isEqualTo("4444");
    }

    @Test
    void deveNegarAtualizacaoDoCartaoDeOutroUsuario() {
        MetodoPagamento metodo = metodoPagamento();
        when(metodoPagamentoRepository.findById(1L)).thenReturn(Optional.of(metodo));
        when(usuarioRepository.findByEmail("outra@example.com")).thenReturn(Optional.of(usuario(99L)));

        assertThatThrownBy(() -> metodoPagamentoService.atualizar(1L, atualizacaoValida(), "outra@example.com"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Acesso negado");

        verify(metodoPagamentoRepository, never()).save(any());
    }

    private MetodoPagamento metodoPagamento() {
        return MetodoPagamento.builder()
                .id(1L)
                .usuarioId(10L)
                .bandeira("VISA")
                .ultimos4("1111")
                .mesExp(12)
                .anoExp(2030)
                .nomePortador("Maria da Silva")
                .tokenGateway("token-antigo")
                .build();
    }

    private Usuario usuario(Long id) {
        return Usuario.builder()
                .id(id)
                .email("maria@example.com")
                .perfil("USER")
                .build();
    }

    private AtualizacaoCartaoDTO atualizacaoValida() {
        AtualizacaoCartaoDTO dto = new AtualizacaoCartaoDTO();
        dto.setNumeroCartao("5555555555554444");
        dto.setValidadeCartao("12/2031");
        dto.setCodigoSegurancaCartao("321");
        dto.setNomeTitularCartao("Maria da Silva");
        return dto;
    }
}
