package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.ProdutoDTO;
import br.uniesp.si.techback.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    public Produto toEntity(ProdutoDTO dto) {
        return Produto.builder().id(dto.getId()).nome(dto.getNome())
                .descricao(dto.getDescricao()).preco(dto.getPreco()).estoque(dto.getEstoque()).build();
    }
    public ProdutoDTO toDTO(Produto p) {
        return ProdutoDTO.builder().id(p.getId()).nome(p.getNome())
                .descricao(p.getDescricao()).preco(p.getPreco()).estoque(p.getEstoque()).build();
    }
}
