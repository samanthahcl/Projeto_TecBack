package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.PlanoDTO;
import br.uniesp.si.techback.model.Plano;
import br.uniesp.si.techback.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoDTO criar(PlanoDTO dto) {
        Plano plano = new Plano();
        plano.setCodigo(dto.getCodigo().toUpperCase());
        plano.setLimiteDiario(dto.getLimiteDiario());
        plano.setPreco(dto.getPreco());
        return toDTO(planoRepository.save(plano));
    }

    public List<PlanoDTO> listar() {
        return planoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public PlanoDTO buscarPorCodigo(String codigo) {
        Plano plano = planoRepository.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado: " + codigo));
        return toDTO(plano);
    }

    private PlanoDTO toDTO(Plano p) {
        return PlanoDTO.builder()
                .id(p.getId())
                .codigo(p.getCodigo())
                .limiteDiario(p.getLimiteDiario())
                .preco(p.getPreco())
                .build();
    }
}
