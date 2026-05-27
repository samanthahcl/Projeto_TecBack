package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, Long> {
    List<MetodoPagamento> findAllByUsuarioId(Long usuarioId);
}
