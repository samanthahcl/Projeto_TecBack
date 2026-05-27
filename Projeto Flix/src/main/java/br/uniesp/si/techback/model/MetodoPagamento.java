package br.uniesp.si.techback.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metodo_pagamento")
public class MetodoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 20)
    private String bandeira;

    @Column(nullable = false, length = 4)
    private String ultimos4;

    @Column(name = "mes_exp", nullable = false)
    private Integer mesExp;

    @Column(name = "ano_exp", nullable = false)
    private Integer anoExp;

    @Column(name = "nome_portador", nullable = false, length = 150)
    private String nomePortador;

    @Column(name = "token_gateway", nullable = false, length = 120)
    private String tokenGateway;
}
