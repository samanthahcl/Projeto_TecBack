package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.AssinaturaDTO;
import br.uniesp.si.techback.model.Assinatura;
import br.uniesp.si.techback.repository.AssinaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;

    public AssinaturaDTO criar(AssinaturaDTO dto) {
        Assinatura assinatura = new Assinatura();
        assinatura.setUsuarioId(dto.getUsuarioId());
        assinatura.setPlanoId(dto.getPlanoId());
        assinatura.setStatus("ATIVA");
        assinatura.setIniciadaEm(LocalDateTime.now());
        return toDTO(assinaturaRepository.save(assinatura));
    }

    public AssinaturaDTO cancelar(Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada: " + id));
        assinatura.setStatus("CANCELADA");
        assinatura.setCanceladaEm(LocalDateTime.now());
        return toDTO(assinaturaRepository.save(assinatura));
    }

    public List<AssinaturaDTO> listarPorStatus(String status) {
        return assinaturaRepository.findAllByStatus(status.toUpperCase())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AssinaturaDTO> listarPorUsuario(Long usuarioId) {
        return assinaturaRepository.findAllByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private AssinaturaDTO toDTO(Assinatura a) {
        return AssinaturaDTO.builder()
                .id(a.getId())
                .usuarioId(a.getUsuarioId())
                .planoId(a.getPlanoId())
                .status(a.getStatus())
                .iniciadaEm(a.getIniciadaEm())
                .canceladaEm(a.getCanceladaEm())
                .build();
    }
}
