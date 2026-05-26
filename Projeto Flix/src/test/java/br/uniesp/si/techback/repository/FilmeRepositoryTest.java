package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Filme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do FilmeRepository")
class FilmeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilmeRepository filmeRepository;

    private Filme filmeTeste;

    @BeforeEach
    void setUp() {
        filmeTeste = Filme.builder()
                .titulo("Filme de Teste")
                .sinopse("Sinopse do filme de teste")
                .dataLancamento(LocalDate.of(2023, 1, 1))
                .genero("Ação")
                .duracaoMinutos(120)
                .classificacaoIndicativa("12 anos")
                .build();
    }

    @Test
    @DisplayName("Deve salvar um filme com sucesso")
    void deveSalvarFilme() {
        Filme filmeSalvo = filmeRepository.save(filmeTeste);

        assertThat(filmeSalvo).isNotNull();
        assertThat(filmeSalvo.getId()).isNotNull();
        assertThat(filmeSalvo.getTitulo()).isEqualTo(filmeTeste.getTitulo());
        assertThat(filmeSalvo.getSinopse()).isEqualTo(filmeTeste.getSinopse());
        assertThat(filmeSalvo.getDataLancamento()).isEqualTo(filmeTeste.getDataLancamento());
        assertThat(filmeSalvo.getGenero()).isEqualTo(filmeTeste.getGenero());
        assertThat(filmeSalvo.getDuracaoMinutos()).isEqualTo(filmeTeste.getDuracaoMinutos());
        assertThat(filmeSalvo.getClassificacaoIndicativa()).isEqualTo(filmeTeste.getClassificacaoIndicativa());
    }

    @Test
    @DisplayName("Deve encontrar filme por ID quando existir")
    void deveEncontrarFilmePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        Optional<Filme> filmeEncontrado = filmeRepository.findById(filmeSalvo.getId());

        assertThat(filmeEncontrado).isPresent();
        assertThat(filmeEncontrado.get().getId()).isEqualTo(filmeSalvo.getId());
        assertThat(filmeEncontrado.get().getTitulo()).isEqualTo(filmeTeste.getTitulo());
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar por ID inexistente")
    void deveRetornarVazioQuandoBuscarPorIdInexistente() {
        Optional<Filme> filmeEncontrado = filmeRepository.findById(999L);

        assertThat(filmeEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todos os filmes")
    void deveListarTodosOsFilmes() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filme2 = Filme.builder()
                .titulo("Filme de Teste 2")
                .sinopse("Outra sinopse")
                .dataLancamento(LocalDate.of(2023, 2, 1))
                .genero("Comédia")
                .duracaoMinutos(90)
                .classificacaoIndicativa("Livre")
                .build();
        entityManager.persistAndFlush(filme2);

        List<Filme> filmes = filmeRepository.findAll();

        assertThat(filmes).hasSize(2);
        assertThat(filmes).extracting(Filme::getTitulo)
                .containsExactlyInAnyOrder(filmeTeste.getTitulo(), filme2.getTitulo());
    }

    @Test
    @DisplayName("Deve verificar se filme existe por ID")
    void deveVerificarSeFilmeExistePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        boolean existe = filmeRepository.existsById(filmeSalvo.getId());
        boolean naoExiste = filmeRepository.existsById(999L);

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    @DisplayName("Deve deletar filme por ID")
    void deveDeletarFilmePorId() {
        Filme filmeSalvo = entityManager.persistAndFlush(filmeTeste);

        filmeRepository.deleteById(filmeSalvo.getId());

        Optional<Filme> filmeDeletado = filmeRepository.findById(filmeSalvo.getId());
        assertThat(filmeDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve contar total de filmes")
    void deveContarTotalDeFilmes() {
        entityManager.persistAndFlush(filmeTeste);

        Filme filme2 = Filme.builder()
                .titulo("Filme de Teste 2")
                .sinopse("Outra sinopse")
                .dataLancamento(LocalDate.of(2023, 2, 1))
                .genero("Comédia")
                .duracaoMinutos(90)
                .classificacaoIndicativa("Livre")
                .build();
        entityManager.persistAndFlush(filme2);

        long totalFilmes = filmeRepository.count();

        assertThat(totalFilmes).isEqualTo(2);
    }
}
