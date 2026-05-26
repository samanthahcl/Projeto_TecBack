package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.PedidoDTO;
import br.uniesp.si.techback.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/pedidos") @RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO incluir(@RequestBody @Valid PedidoDTO dto) { return pedidoService.incluir(dto); }

    @GetMapping
    public List<PedidoDTO> listar() { return pedidoService.listar(); }

    @GetMapping("/{id}")
    public PedidoDTO buscarPorId(@PathVariable Long id) { return pedidoService.buscarPorId(id); }

    @PutMapping("/{id}")
    public PedidoDTO atualizar(@PathVariable Long id, @RequestBody @Valid PedidoDTO dto) { return pedidoService.atualizar(id, dto); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) { pedidoService.deletar(id); }
}
