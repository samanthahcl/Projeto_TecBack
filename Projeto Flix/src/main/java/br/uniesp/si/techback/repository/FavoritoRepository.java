package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Conteudo;
import br.uniesp.si.techback.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    boolean existsByUsuarioIdAndConteudoId(Long usuarioId, Long conteudoId);

    @Query("""
           SELECT c
           FROM Favorito f
           JOIN Conteudo c ON c.id = f.conteudoId
           WHERE f.usuarioId = :usuarioId
           AND UPPER(c.tipo) = UPPER(:tipo)
           ORDER BY c.titulo ASC
           """)
    List<Conteudo> listarConteudosFavoritosPorTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") String tipo
    );
}