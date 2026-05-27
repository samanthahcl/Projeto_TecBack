package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.ConteudoDTO;
import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.repository.ConteudoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;

    public ConteudoDTO criar(ConteudoDTO dto) {
        Conteudo conteudo = toEntity(dto);
        Conteudo salvo = conteudoRepository.save(conteudo);
        return toDTO(salvo);
    }

    public List<ConteudoDTO> listar() {
        return conteudoRepository.listarOrdenadosPorTitulo()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ConteudoDTO buscarPorId(Long id) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não encontrado: " + id));
        return toDTO(conteudo);
    }

    public ConteudoDTO atualizar(Long id, ConteudoDTO dto) {
        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não encontrado: " + id));

        conteudo.setTitulo(dto.getTitulo());
        conteudo.setTipo(dto.getTipo());
        conteudo.setAno(dto.getAno());
        conteudo.setDuracaoMinutos(dto.getDuracaoMinutos());
        conteudo.setRelevancia(dto.getRelevancia());
        conteudo.setSinopse(dto.getSinopse());
        conteudo.setTrailerUrl(dto.getTrailerUrl());
        conteudo.setGenero(dto.getGenero());

        return toDTO(conteudoRepository.save(conteudo));
    }

    public void deletar(Long id) {
        if (!conteudoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conteúdo não encontrado: " + id);
        }
        conteudoRepository.deleteById(id);
    }

    // métodos que usam as consultas JPQL
    public List<ConteudoDTO> listarPorGenero(String genero) {
        return conteudoRepository.filtrarPorGenero(genero)
                .stream().map(this::toDTO).toList();
    }

    public List<ConteudoDTO> listarPorRelevancia() {
        return conteudoRepository.listarPorRelevancia()
                .stream().map(this::toDTO).toList();
    }

    public List<ConteudoDTO> listarLancadosApos(Integer ano) {
        return conteudoRepository.listarLancadosAposAno(ano)
                .stream().map(this::toDTO).toList();
    }

    public List<ConteudoDTO> buscarPorTitulo(String titulo) {
        return conteudoRepository.buscarPorTitulo(titulo)
                .stream().map(this::toDTO).toList();
    }

    public List<ConteudoDTO> listarPorTipo(String tipo) {
        return conteudoRepository.findAllByTipoOrderByTituloAsc(tipo.toUpperCase())
                .stream().map(this::toDTO).toList();
    }

    // conversão de Entity para DTO
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

    // conversão de DTO para Entity
    private Conteudo toEntity(ConteudoDTO dto) {
        return Conteudo.builder()
                .titulo(dto.getTitulo())
                .tipo(dto.getTipo().toUpperCase())
                .ano(dto.getAno())
                .duracaoMinutos(dto.getDuracaoMinutos())
                .relevancia(dto.getRelevancia())
                .sinopse(dto.getSinopse())
                .trailerUrl(dto.getTrailerUrl())
                .genero(dto.getGenero())
                .build();
    }
}
