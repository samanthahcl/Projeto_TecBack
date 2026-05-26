package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.ProdutoDTO;
import br.uniesp.si.techback.mapper.ProdutoMapper;
import br.uniesp.si.techback.model.Produto;
import br.uniesp.si.techback.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoDTO incluir(ProdutoDTO dto) {
        return produtoMapper.toDTO(produtoRepository.save(produtoMapper.toEntity(dto)));
    }
    public List<ProdutoDTO> listar() {
        return produtoRepository.findAll().stream().map(produtoMapper::toDTO).toList();
    }
    public ProdutoDTO buscarPorId(Long id) {
        return produtoMapper.toDTO(produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + id)));
    }
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + id));
        p.setNome(dto.getNome()); p.setDescricao(dto.getDescricao());
        p.setPreco(dto.getPreco()); p.setEstoque(dto.getEstoque());
        return produtoMapper.toDTO(produtoRepository.save(p));
    }
    public void deletar(Long id) {
        produtoRepository.delete(produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + id)));
    }
}
