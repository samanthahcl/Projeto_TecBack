package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.ConteudoDTO;
import br.uniesp.si.techback.dto.FavoritoDTO;
import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.model.Favorito;
import br.uniesp.si.techback.repository.ConteudoRepository;
import br.uniesp.si.techback.repository.FavoritoRepository;
import br.uniesp.si.techback.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConteudoRepository conteudoRepository;

    public FavoritoDTO adicionar(FavoritoDTO dto) {
        if (!usuarioRepository.existsById(dto.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        if (!conteudoRepository.existsById(dto.getConteudoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não encontrado");
        }

        if (favoritoRepository.existsByUsuarioIdAndConteudoId(dto.getUsuarioId(), dto.getConteudoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conteúdo já está nos favoritos");
        }

        Favorito favorito = Favorito.builder()
                .usuarioId(dto.getUsuarioId())
                .conteudoId(dto.getConteudoId())
                .build();

        Favorito salvo = favoritoRepository.save(favorito);

        return FavoritoDTO.builder()
                .id(salvo.getId())
                .usuarioId(salvo.getUsuarioId())
                .conteudoId(salvo.getConteudoId())
                .build();
    }

    // RF10 - listar filmes favoritos separadamente
    public List<ConteudoDTO> listarFilmesFavoritos(Long usuarioId) {
        return favoritoRepository.listarConteudosFavoritosPorTipo(usuarioId, "FILME")
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // RF10 - listar séries favoritas separadamente
    public List<ConteudoDTO> listarSeriesFavoritas(Long usuarioId) {
        return favoritoRepository.listarConteudosFavoritosPorTipo(usuarioId, "SERIE")
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ConteudoDTO toDTO(Conteudo c) {
        return ConteudoDTO.builder()
                .id(c.getId())
                .titulo(c.getTitulo())
                .tipo(c.getTipo())
                .ano(c.getAno())
                .duracaoMinutos(c.getDuracaoMinutos())
                .relevancia(c.getRelevancia())
                .sinopse(c.getSinopse())
                .trailerUrl(c.getTrailerUrl())
                .genero(c.getGenero())
                .build();
    }
}