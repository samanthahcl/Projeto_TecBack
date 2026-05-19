package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FilmeDTO;
import br.uniesp.si.techback.mapper.FilmeMapper;
import br.uniesp.si.techback.model.Filme;
import br.uniesp.si.techback.repository.FilmeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do FilmeService")
class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private FilmeMapper filmeMapper;

    @InjectMocks
    private FilmeService filmeService;

    private Filme filme;
    private FilmeDTO filmeDTO;

    @BeforeEach
    void setUp() {
        filme = Filme.builder()
                .id(1L)
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Ação")
                .duracaoMinutos(120)
                .classificacaoIndicativa("12 anos")
                .build();

        filmeDTO = FilmeDTO.builder()
                .id(1L)
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Ação")
                .duracaoMinutos(120)
                .classificacaoIndicativa("12 anos")
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    void deveListarTodosOsFilmes() {
        List<Filme> filmes = Arrays.asList(filme);
        List<FilmeDTO> filmesDTO = Arrays.asList(filmeDTO);

        when(filmeRepository.findAll()).thenReturn(filmes);
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        List<FilmeDTO> resultado = filmeService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo(filmeDTO.getTitulo());
        verify(filmeRepository).findAll();
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve buscar filme por ID quando existir")
    void deveBuscarFilmePorIdQuandoExistir() {
        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        FilmeDTO resultado = filmeService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(filmeDTO.getId());
        assertThat(resultado.getTitulo()).isEqualTo(filmeDTO.getTitulo());
        verify(filmeRepository).findById(1L);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar filme por ID inexistente")
    void deveLancarExcecaoQuandoBuscarFilmePorIdInexistente() {
        when(filmeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmeService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Filme não encontrado com o ID: 999");

        verify(filmeRepository).findById(999L);
        verifyNoInteractions(filmeMapper);
    }

    @Test
    @DisplayName("Deve salvar um novo filme")
    void deveSalvarNovoFilme() {
        FilmeDTO filmeDTOSemId = FilmeDTO.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Ação")
                .duracaoMinutos(120)
                .classificacaoIndicativa("12 anos")
                .build();

        Filme filmeSemId = Filme.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Ação")
                .duracaoMinutos(120)
                .classificacaoIndicativa("12 anos")
                .build();

        when(filmeMapper.toEntity(filmeDTOSemId)).thenReturn(filmeSemId);
        when(filmeRepository.save(filmeSemId)).thenReturn(filme);
        when(filmeMapper.toDTO(filme)).thenReturn(filmeDTO);

        FilmeDTO resultado = filmeService.salvar(filmeDTOSemId);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(filmeDTO.getId());
        assertThat(resultado.getTitulo()).isEqualTo(filmeDTO.getTitulo());
        verify(filmeMapper).toEntity(filmeDTOSemId);
        verify(filmeRepository).save(filmeSemId);
        verify(filmeMapper).toDTO(filme);
    }

    @Test
    @DisplayName("Deve atualizar um filme existente")
    void deveAtualizarFilmeExistente() {
        FilmeDTO filmeDTOAtualizado = FilmeDTO.builder()
                .id(1L)
                .titulo("Filme Atualizado")
                .sinopse("Sinopse atualizada")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Drama")
                .duracaoMinutos(150)
                .classificacaoIndicativa("16 anos")
                .build();

        Filme filmeParaAtualizar = Filme.builder()
                .id(1L)
                .titulo("Filme Atualizado")
                .sinopse("Sinopse atualizada")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Drama")
                .duracaoMinutos(150)
                .classificacaoIndicativa("16 anos")
                .build();

        when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        when(filmeMapper.toEntity(filmeDTOAtualizado)).thenReturn(filmeParaAtualizar);
        when(filmeRepository.save(filmeParaAtualizar)).thenReturn(filmeParaAtualizar);
        when(filmeMapper.toDTO(filmeParaAtualizar)).thenReturn(filmeDTOAtualizado);

        FilmeDTO resultado = filmeService.atualizar(1L, filmeDTOAtualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Filme Atualizado");
        assertThat(resultado.getGenero()).isEqualTo("Drama");
        verify(filmeRepository).findById(1L);
        verify(filmeMapper).toEntity(filmeDTOAtualizado);
        verify(filmeRepository).save(filmeParaAtualizar);
        verify(filmeMapper).toDTO(filmeParaAtualizar);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar atualizar filme inexistente")
    void deveLancarExcecaoQuandoTentarAtualizarFilmeInexistente() {
        when(filmeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> filmeService.atualizar(999L, filmeDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Falha ao atualizar: filme não encontrado com o ID: 999");

        verify(filmeRepository).findById(999L);
        verifyNoInteractions(filmeMapper);
    }

    @Test
    @DisplayName("Deve excluir um filme existente")
    void deveExcluirFilmeExistente() {
        when(filmeRepository.existsById(1L)).thenReturn(true);

        filmeService.excluir(1L);

        verify(filmeRepository).existsById(1L);
        verify(filmeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar excluir filme inexistente")
    void deveLancarExcecaoQuandoTentarExcluirFilmeInexistente() {
        when(filmeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> filmeService.excluir(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Falha ao excluir: filme não encontrado com o ID: 999");

        verify(filmeRepository).existsById(999L);
        verify(filmeRepository, never()).deleteById(any());
    }
}
