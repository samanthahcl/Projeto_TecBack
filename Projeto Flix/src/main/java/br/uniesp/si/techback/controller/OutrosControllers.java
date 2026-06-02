package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.AssinaturaDTO;
import br.uniesp.si.techback.dto.AtualizacaoCartaoDTO;
import br.uniesp.si.techback.dto.MetodoPagamentoDTO;
import br.uniesp.si.techback.dto.PlanoDTO;
import br.uniesp.si.techback.service.AssinaturaService;
import br.uniesp.si.techback.service.MetodoPagamentoService;
import br.uniesp.si.techback.service.PlanoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ============================================================
//  PlanoController  /api/v1/planos
// ============================================================
@RestController
@RequestMapping("/api/v1/planos")
@RequiredArgsConstructor
class PlanoController {

    private final PlanoService planoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoDTO criar(@Valid @RequestBody PlanoDTO dto) {
        return planoService.criar(dto);
    }

    @GetMapping
    public List<PlanoDTO> listar() {
        return planoService.listar();
    }

    @GetMapping("/{codigo}")
    public PlanoDTO buscarPorCodigo(@PathVariable String codigo) {
        return planoService.buscarPorCodigo(codigo);
    }
}

// ============================================================
//  AssinaturaController  /api/v1/assinaturas
// ============================================================
@RestController
@RequestMapping("/api/v1/assinaturas")
@RequiredArgsConstructor
class AssinaturaController {

    private final AssinaturaService assinaturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssinaturaDTO criar(@Valid @RequestBody AssinaturaDTO dto) {
        return assinaturaService.criar(dto);
    }

    // cancelar assinatura
    @PatchMapping("/{id}/cancelar")
    public AssinaturaDTO cancelar(@PathVariable Long id) {
        return assinaturaService.cancelar(id);
    }

    // listar por status: ATIVA, EM_ATRASO ou CANCELADA
    @GetMapping
    public List<AssinaturaDTO> listarPorStatus(@RequestParam(defaultValue = "ATIVA") String status) {
        return assinaturaService.listarPorStatus(status);
    }

    // listar assinaturas de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public List<AssinaturaDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return assinaturaService.listarPorUsuario(usuarioId);
    }
}

// ============================================================
//  MetodoPagamentoController  /api/v1/metodos-pagamento
// ============================================================
@RestController
@RequestMapping("/api/v1/metodos-pagamento")
@RequiredArgsConstructor
class MetodoPagamentoController {

    private final MetodoPagamentoService metodoPagamentoService;

    // RF7 - cadastrar cartão
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetodoPagamentoDTO cadastrar(@Valid @RequestBody MetodoPagamentoDTO dto, Authentication authentication) {
        return metodoPagamentoService.cadastrar(dto, authentication.getName());
    }

    // RF7 - alterar dados do cartão
    @PutMapping("/{id}")
    public MetodoPagamentoDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizacaoCartaoDTO dto,
            Authentication authentication
    ) {
        return metodoPagamentoService.atualizar(id, dto, authentication.getName());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<MetodoPagamentoDTO> listarPorUsuario(@PathVariable Long usuarioId, Authentication authentication) {
        return metodoPagamentoService.listarPorUsuario(usuarioId, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id, Authentication authentication) {
        metodoPagamentoService.remover(id, authentication.getName());
    }
}
