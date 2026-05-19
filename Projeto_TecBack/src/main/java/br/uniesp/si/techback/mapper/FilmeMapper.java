package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.model.Filme;
import org.springframework.stereotype.Component;

@Component
public class FilmeMapper {

    public Filme toEntity(FilmeDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Filme filme = new Filme();
        filme.setId(dto.getId());
        filme.setTitulo(dto.getTitulo());
        filme.setSinopse(dto.getSinopse());
        filme.setDataLancamento(dto.getDataLancamento());
        filme.setGenero(dto.getGenero());
        filme.setDuracaoMinutos(dto.getDuracaoMinutos());
        filme.setClassificacaoIndicativa(dto.getClassificacaoIndicativa());
        
        return filme;
    }

    public FilmeDTO toDTO(Filme entity) {
        if (entity == null) {
            return null;
        }
        
        FilmeDTO dto = new FilmeDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setSinopse(entity.getSinopse());
        dto.setDataLancamento(entity.getDataLancamento());
        dto.setGenero(entity.getGenero());
        dto.setDuracaoMinutos(entity.getDuracaoMinutos());
        dto.setClassificacaoIndicativa(entity.getClassificacaoIndicativa());
        
        return dto;
    }
}
