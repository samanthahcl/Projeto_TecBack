package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Conteudo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do ConteudoRepository - consultas JPQL")
class ConteudoRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ConteudoRepository conteudoRepository;

    @BeforeEach
    void setUp() {
        salvar("Interestelar", "FILME", 2014, 9.5, "Ficção Científica");
        salvar("Breaking Bad", "SERIE", 2008, 9.9, "Crime");
        salvar("Coringa",      "FILME", 2019, 8.5, "Drama");
        salvar("Dark",         "SERIE", 2017, 9.3, "Ficção Científica");
    }

    @Test
    @DisplayName("JPQL 1 - deve listar ordenado por título A-Z")
    void deveListarOrdenadoPorTitulo() {
        List<Conteudo> lista = conteudoRepository.listarOrdenadosPorTitulo();
        assertThat(lista).hasSize(4);
        assertThat(lista.get(0).getTitulo()).isEqualTo("Breaking Bad");
        assertThat(lista.get(3).getTitulo()).isEqualTo("Interestelar");
    }

    @Test
    @DisplayName("JPQL 2 - deve filtrar por gênero sem diferenciar maiúscula/minúscula")
    void deveFiltrarPorGenero() {
        List<Conteudo> lista = conteudoRepository.filtrarPorGenero("ficção científica");
        assertThat(lista).hasSize(2);
    }

    @Test
    @DisplayName("JPQL 3 - deve listar do mais relevante para o menos")
    void deveListarPorRelevancia() {
        List<Conteudo> lista = conteudoRepository.listarPorRelevancia();
        assertThat(lista.get(0).getTitulo()).isEqualTo("Breaking Bad"); // 9.9
        assertThat(lista.get(3).getTitulo()).isEqualTo("Coringa");      // 8.5
    }

    @Test
    @DisplayName("JPQL 4 - deve listar conteúdos lançados após 2015")
    void deveListarLancadosApos2015() {
        List<Conteudo> lista = conteudoRepository.listarLancadosAposAno(2015);
        assertThat(lista).hasSize(2);
        assertThat(lista)
                .extracting(Conteudo::getTitulo)
                .containsExactlyInAnyOrder("Coringa", "Dark");
    }

    private void salvar(String titulo, String tipo, int ano, double relevancia, String genero) {
        em.persistAndFlush(Conteudo.builder()
                .titulo(titulo)
                .tipo(tipo)
                .ano(ano)
                .duracaoMinutos(100)
                .relevancia(BigDecimal.valueOf(relevancia))
                .genero(genero)
                .build());
    }
}
