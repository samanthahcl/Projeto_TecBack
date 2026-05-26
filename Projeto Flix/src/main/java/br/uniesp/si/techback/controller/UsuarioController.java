package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.UsuarioDTO;
import br.uniesp.si.techback.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/usuarios") @RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO incluir(@RequestBody @Valid UsuarioDTO dto) { return usuarioService.incluir(dto); }

    @GetMapping
    public List<UsuarioDTO> listar() { return usuarioService.listar(); }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) { return usuarioService.buscarPorId(id); }

    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO dto) { return usuarioService.atualizar(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) { usuarioService.deletar(id); }
}
