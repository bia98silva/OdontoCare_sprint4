package com.Fiap.OdontoCare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Dentista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Dentista")
    private Long idDentista;
    @Column(name = "Nome", length = 100)
    private String nome;
    @Column(name = "CRO", unique = true, length = 20)
    private String cro;
    @Column(name = "Especialidade", length = 50)
    private String especialidade;
    @Column(name = "Telefone", length = 20)
    private String telefone;
    @Column(name = "Email")
    private String email;

    @OneToMany(mappedBy = "dentista", cascade = CascadeType.ALL)
    private List<Consulta> consultas;


}
