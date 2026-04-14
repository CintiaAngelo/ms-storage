package com.javer.ms_storage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Column(nullable = false)
    private Long telefone;

    @NotNull
    @Column(nullable = false)
    private Boolean correntista;

    @NotNull
    @Column(name = "score_credito", nullable = false)
    private Float scoreCredito;

    @NotNull
    @Column(name = "saldo_cc", nullable = false)
    private Float saldoCc;
}