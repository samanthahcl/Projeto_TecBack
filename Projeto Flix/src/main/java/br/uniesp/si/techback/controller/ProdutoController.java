package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.ProdutoDTO;
import br.uniesp.si.techback.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/produtos") @RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDTO incluir(@RequestBody @Valid ProdutoDTO dto) { return produtoService.incluir(dto); }

    @GetMapping
    public List<ProdutoDTO> listar() { return produtoService.listar(); }

    @GetMapping("/{id}")
    public ProdutoDTO buscarPorId(@PathVariable Long id) { return produtoService.buscarPorId(id); }

    @PutMapping("/{id}")
    public ProdutoDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoDTO dto) { return produtoService.atualizar(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) { produtoService.deletar(id); }
}
