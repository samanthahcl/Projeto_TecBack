package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.ConteudoDTO;
import br.uniesp.si.techback.service.ConteudoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conteudos")
@RequiredArgsConstructor
public class ConteudoController {

    private final ConteudoService conteudoService;

    // RF8 - listar filmes e séries com filtros opcionais
    @GetMapping
    public List<ConteudoDTO> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String q) {

        if (q != null && !q.isBlank()) {
            return conteudoService.buscarPorTitulo(q);
        }
        if (genero != null && !genero.isBlank()) {
            return conteudoService.listarPorGenero(genero);
        }
        if (tipo != null && !tipo.isBlank()) {
            return conteudoService.listarPorTipo(tipo);
        }
        return conteudoService.listar();
    }

    // RF8 - detalhar conteúdo
    @GetMapping("/{id}")
    public ConteudoDTO buscarPorId(@PathVariable Long id) {
        return conteudoService.buscarPorId(id);
    }

    // JPQL 3 - top por relevância
    @GetMapping("/top-relevancia")
    public List<ConteudoDTO> topRelevancia() {
        return conteudoService.listarPorRelevancia();
    }

    // JPQL 4 - lançados após um ano
    @GetMapping("/lancados-apos")
    public List<ConteudoDTO> lancadosApos(@RequestParam Integer ano) {
        return conteudoService.listarLancadosApos(ano);
    }

    // RF11 - cadastrar conteúdo (admin)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConteudoDTO criar(@Valid @RequestBody ConteudoDTO dto) {
        return conteudoService.criar(dto);
    }

    @PutMapping("/{id}")
    public ConteudoDTO atualizar(@PathVariable Long id, @Valid @RequestBody ConteudoDTO dto) {
        return conteudoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        conteudoService.deletar(id);
    }
}
