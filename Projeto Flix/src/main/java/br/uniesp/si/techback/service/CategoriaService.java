package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.CategoriaDTO;
import br.uniesp.si.techback.mapper.CategoriaMapper;
import br.uniesp.si.techback.model.Categoria;
import br.uniesp.si.techback.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaDTO incluir(CategoriaDTO dto) {
        return categoriaMapper.toDTO(categoriaRepository.save(categoriaMapper.toEntity(dto)));
    }
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream().map(categoriaMapper::toDTO).toList();
    }
    public CategoriaDTO buscarPorId(Long id) {
        return categoriaMapper.toDTO(categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada: " + id)));
    }
    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada: " + id));
        c.setNome(dto.getNome()); c.setDescricao(dto.getDescricao());
        return categoriaMapper.toDTO(categoriaRepository.save(c));
    }
    public void deletar(Long id) {
        categoriaRepository.delete(categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada: " + id)));
    }
}
