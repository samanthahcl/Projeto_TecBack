package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conteudo")
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 10)
    private String tipo; // FILME ou SERIE

    @Column(nullable = false)
    private Integer ano;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal relevancia;

    @Column(columnDefinition = "TEXT")
    private String sinopse;

    @Column(name = "trailer_url", length = 500)
    private String trailerUrl;

    @Column(length = 50)
    private String genero;
}
