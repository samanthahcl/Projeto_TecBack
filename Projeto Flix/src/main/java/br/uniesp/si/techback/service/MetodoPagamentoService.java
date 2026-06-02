package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.AtualizacaoCartaoDTO;
import br.uniesp.si.techback.dto.CadastroUsuarioDTO;
import br.uniesp.si.techback.dto.MetodoPagamentoDTO;
import br.uniesp.si.techback.model.MetodoPagamento;
import br.uniesp.si.techback.model.Usuario;
import br.uniesp.si.techback.repository.MetodoPagamentoRepository;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetodoPagamentoService {

    private final MetodoPagamentoRepository metodoPagamentoRepository;
    private final UsuarioRepository usuarioRepository;

    // Cadastro adicional com dados previamente tokenizados.
    public MetodoPagamentoDTO cadastrar(MetodoPagamentoDTO dto, String emailAutenticado) {
        autorizarUsuario(dto.getUsuarioId(), emailAutenticado);

        MetodoPagamento mp = new MetodoPagamento();
        mp.setUsuarioId(dto.getUsuarioId());
        mp.setBandeira(dto.getBandeira());
        mp.setUltimos4(dto.getUltimos4());
        mp.setMesExp(dto.getMesExp());
        mp.setAnoExp(dto.getAnoExp());
        mp.setNomePortador(dto.getNomePortador());
        mp.setTokenGateway(dto.getTokenGateway());
        return toDTO(metodoPagamentoRepository.save(mp));
    }

    // RF1 - o número completo e o CVV são recebidos, mas nunca persistidos.
    public void cadastrarCartaoInicial(Long usuarioId, CadastroUsuarioDTO dto) {
        MetodoPagamento mp = new MetodoPagamento();
        mp.setUsuarioId(usuarioId);
        preencherDadosTokenizados(
                mp,
                dto.getNumeroCartao(),
                dto.getValidadeCartao(),
                dto.getNomeTitularCartao()
        );
        metodoPagamentoRepository.save(mp);
    }

    public List<MetodoPagamentoDTO> listarPorUsuario(Long usuarioId, String emailAutenticado) {
        autorizarUsuario(usuarioId, emailAutenticado);
        return metodoPagamentoRepository.findAllByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // RF7 - alterar dados do cartão após autenticação.
    public MetodoPagamentoDTO atualizar(Long id, AtualizacaoCartaoDTO dto, String emailAutenticado) {
        MetodoPagamento mp = metodoPagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Método de pagamento não encontrado: " + id
                ));

        autorizarUsuario(mp.getUsuarioId(), emailAutenticado);
        preencherDadosTokenizados(
                mp,
                dto.getNumeroCartao(),
                dto.getValidadeCartao(),
                dto.getNomeTitularCartao()
        );

        return toDTO(metodoPagamentoRepository.save(mp));
    }

    public void remover(Long id, String emailAutenticado) {
        MetodoPagamento mp = metodoPagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Método de pagamento não encontrado: " + id
                ));
        autorizarUsuario(mp.getUsuarioId(), emailAutenticado);
        metodoPagamentoRepository.delete(mp);
    }

    private void autorizarUsuario(Long usuarioId, String emailAutenticado) {
        if ("admin".equals(emailAutenticado)) {
            return;
        }

        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));

        if (!usuario.getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado ao método de pagamento");
        }
    }

    private void preencherDadosTokenizados(
            MetodoPagamento mp,
            String numeroCartao,
            String validadeCartao,
            String nomeTitularCartao
    ) {
        YearMonth validade = YearMonth.parse(validadeCartao, DateTimeFormatter.ofPattern("MM/uuuu"));
        if (validade.isBefore(YearMonth.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cartão vencido");
        }

        mp.setBandeira(identificarBandeira(numeroCartao));
        mp.setUltimos4(numeroCartao.substring(numeroCartao.length() - 4));
        mp.setMesExp(validade.getMonthValue());
        mp.setAnoExp(validade.getYear());
        mp.setNomePortador(nomeTitularCartao);
        mp.setTokenGateway(UUID.randomUUID().toString());
    }

    private String identificarBandeira(String numeroCartao) {
        if (numeroCartao.startsWith("4")) {
            return "VISA";
        }
        if (numeroCartao.matches("5[1-5].*")) {
            return "MASTERCARD";
        }
        return "OUTRA";
    }

    private MetodoPagamentoDTO toDTO(MetodoPagamento mp) {
        return MetodoPagamentoDTO.builder()
                .id(mp.getId())
                .usuarioId(mp.getUsuarioId())
                .bandeira(mp.getBandeira())
                .ultimos4(mp.getUltimos4())
                .mesExp(mp.getMesExp())
                .anoExp(mp.getAnoExp())
                .nomePortador(mp.getNomePortador())
                .tokenGateway(mp.getTokenGateway())
                .build();
    }
}
