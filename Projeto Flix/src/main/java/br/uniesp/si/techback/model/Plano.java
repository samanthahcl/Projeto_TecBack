package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plano")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo; // BASICO, PADRAO ou PREMIUM

    @Column(name = "limite_diario", nullable = false)
    private Integer limiteDiario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
}
