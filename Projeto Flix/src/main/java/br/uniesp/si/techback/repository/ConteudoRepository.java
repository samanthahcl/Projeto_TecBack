package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {

    // JPQL 1 - listar todos ordenados por título
    @Query("SELECT c FROM Conteudo c ORDER BY c.titulo ASC")
    List<Conteudo> listarOrdenadosPorTitulo();

    // JPQL 2 - filtrar por gênero (não diferencia maiúscula/minúscula)
    @Query("SELECT c FROM Conteudo c WHERE LOWER(c.genero) = LOWER(:genero) ORDER BY c.titulo ASC")
    List<Conteudo> filtrarPorGenero(@Param("genero") String genero);

    // JPQL 3 - top conteúdos por relevância
    @Query("SELECT c FROM Conteudo c ORDER BY c.relevancia DESC")
    List<Conteudo> listarPorRelevancia();

    // JPQL 4 - conteúdos lançados após um determinado ano
    @Query("SELECT c FROM Conteudo c WHERE c.ano > :ano ORDER BY c.ano DESC")
    List<Conteudo> listarLancadosAposAno(@Param("ano") Integer ano);

    // buscar por palavra-chave no título ou sinopse
    @Query("SELECT c FROM Conteudo c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :q, '%')) ORDER BY c.titulo ASC")
    List<Conteudo> buscarPorTitulo(@Param("q") String q);

    // filtrar por tipo (FILME ou SERIE)
    List<Conteudo> findAllByTipoOrderByTituloAsc(String tipo);
}
