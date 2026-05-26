package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.CategoriaDTO;
import br.uniesp.si.techback.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaDTO dto) {
        return Categoria.builder().id(dto.getId()).nome(dto.getNome()).descricao(dto.getDescricao()).build();
    }
    public CategoriaDTO toDTO(Categoria c) {
        return CategoriaDTO.builder().id(c.getId()).nome(c.getNome()).descricao(c.getDescricao()).build();
    }
}
