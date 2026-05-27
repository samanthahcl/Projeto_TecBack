package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.ConteudoDTO;
import br.uniesp.si.techback.dto.FavoritoDTO;
import br.uniesp.si.techback.service.FavoritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoDTO adicionar(@Valid @RequestBody FavoritoDTO dto) {
        return favoritoService.adicionar(dto);
    }

    // RF10 - listar filmes favoritos separadamente
    @GetMapping("/usuario/{usuarioId}/filmes")
    public List<ConteudoDTO> listarFilmesFavoritos(@PathVariable Long usuarioId) {
        return favoritoService.listarFilmesFavoritos(usuarioId);
    }

    // RF10 - listar séries favoritas separadamente
    @GetMapping("/usuario/{usuarioId}/series")
    public List<ConteudoDTO> listarSeriesFavoritas(@PathVariable Long usuarioId) {
        return favoritoService.listarSeriesFavoritas(usuarioId);
    }
}