package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.PedidoDTO;
import br.uniesp.si.techback.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {
    public Pedido toEntity(PedidoDTO dto) {
        return Pedido.builder().id(dto.getId()).dataPedido(dto.getDataPedido())
                .status(dto.getStatus()).valorTotal(dto.getValorTotal()).clienteId(dto.getClienteId()).build();
    }
    public PedidoDTO toDTO(Pedido p) {
        return PedidoDTO.builder().id(p.getId()).dataPedido(p.getDataPedido())
                .status(p.getStatus()).valorTotal(p.getValorTotal()).clienteId(p.getClienteId()).build();
    }
}
