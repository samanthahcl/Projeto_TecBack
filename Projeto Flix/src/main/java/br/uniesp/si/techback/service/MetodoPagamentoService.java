package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.MetodoPagamentoDTO;
import br.uniesp.si.techback.model.MetodoPagamento;
import br.uniesp.si.techback.repository.MetodoPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagamentoService {

    private final MetodoPagamentoRepository metodoPagamentoRepository;

    // RF7 - cadastrar cartão de crédito
    public MetodoPagamentoDTO cadastrar(MetodoPagamentoDTO dto) {
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

    public List<MetodoPagamentoDTO> listarPorUsuario(Long usuarioId) {
        return metodoPagamentoRepository.findAllByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // RF7 - alterar dados do cartão após autenticação
    public MetodoPagamentoDTO atualizar(Long id, MetodoPagamentoDTO dto) {
        MetodoPagamento mp = metodoPagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Método de pagamento não encontrado: " + id
                ));

        mp.setUsuarioId(dto.getUsuarioId());
        mp.setBandeira(dto.getBandeira());
        mp.setUltimos4(dto.getUltimos4());
        mp.setMesExp(dto.getMesExp());
        mp.setAnoExp(dto.getAnoExp());
        mp.setNomePortador(dto.getNomePortador());
        mp.setTokenGateway(dto.getTokenGateway());

        return toDTO(metodoPagamentoRepository.save(mp));
    }

    public void remover(Long id) {
        if (!metodoPagamentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pagamento não encontrado: " + id);
        }
        metodoPagamentoRepository.deleteById(id);
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