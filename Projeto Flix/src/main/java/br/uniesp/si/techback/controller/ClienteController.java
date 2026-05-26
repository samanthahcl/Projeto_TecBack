package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.ClienteDTO;
import br.uniesp.si.techback.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/clientes") @RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO incluir(@RequestBody @Valid ClienteDTO dto) { return clienteService.incluir(dto); }

    @GetMapping
    public List<ClienteDTO> listar() { return clienteService.listar(); }

    @GetMapping("/{id}")
    public ClienteDTO buscarPorId(@PathVariable Long id) { return clienteService.buscarPorId(id); }

    @PutMapping("/{id}")
    public ClienteDTO atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO dto) { return clienteService.atualizar(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) { clienteService.deletar(id); }
}
