package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.PedidoDTO;
import br.uniesp.si.techback.mapper.PedidoMapper;
import br.uniesp.si.techback.model.Pedido;
import br.uniesp.si.techback.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoDTO incluir(PedidoDTO dto) {
        return pedidoMapper.toDTO(pedidoRepository.save(pedidoMapper.toEntity(dto)));
    }
    public List<PedidoDTO> listar() {
        return pedidoRepository.findAll().stream().map(pedidoMapper::toDTO).toList();
    }
    public PedidoDTO buscarPorId(Long id) {
        return pedidoMapper.toDTO(pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id)));
    }
    public PedidoDTO atualizar(Long id, PedidoDTO dto) {
        Pedido p = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id));
        p.setDataPedido(dto.getDataPedido()); p.setStatus(dto.getStatus());
        p.setValorTotal(dto.getValorTotal()); p.setClienteId(dto.getClienteId());
        return pedidoMapper.toDTO(pedidoRepository.save(p));
    }
    public void deletar(Long id) {
        pedidoRepository.delete(pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id)));
    }
}
