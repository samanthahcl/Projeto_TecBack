package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.CategoriaDTO;
import br.uniesp.si.techback.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/categorias") @RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO incluir(@RequestBody @Valid CategoriaDTO dto) { return categoriaService.incluir(dto); }

    @GetMapping
    public List<CategoriaDTO> listar() { return categoriaService.listar(); }

    @GetMapping("/{id}")
    public CategoriaDTO buscarPorId(@PathVariable Long id) { return categoriaService.buscarPorId(id); }

    @PutMapping("/{id}")
    public CategoriaDTO atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaDTO dto) { return categoriaService.atualizar(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) { categoriaService.deletar(id); }
}
