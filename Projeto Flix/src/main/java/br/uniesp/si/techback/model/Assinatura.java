package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assinatura")
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "plano_id", nullable = false)
    private Long planoId;

    @Column(nullable = false, length = 20)
    private String status; // ATIVA, EM_ATRASO ou CANCELADA

    @Column(name = "iniciada_em", nullable = false)
    private LocalDateTime iniciadaEm;

    @Column(name = "cancelada_em")
    private LocalDateTime canceladaEm;
}
