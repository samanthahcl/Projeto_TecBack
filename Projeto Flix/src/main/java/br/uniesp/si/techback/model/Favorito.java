package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "favoritos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_id", "conteudo_id"})
        }
)
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "conteudo_id", nullable = false)
    private Long conteudoId;
}