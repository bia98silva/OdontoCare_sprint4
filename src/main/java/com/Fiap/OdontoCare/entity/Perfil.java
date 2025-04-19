package com.Fiap.OdontoCare.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_PERFIS")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "SEQ_PERFIL", sequenceName = "SEQ_PERFIL", allocationSize = 1)
    @Column(name = "ID_PERFIL")
    private Long id;

    @Column(name = "NOME_PERFIL", unique = true, nullable = false, length = 50)
    private String nome;
}