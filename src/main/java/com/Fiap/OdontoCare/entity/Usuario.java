package com.Fiap.OdontoCare.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "TB_USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "LOGIN_USUARIO", unique = true, nullable = false, length = 50)
    private String username; // Nome da propriedade permanece o mesmo

    @Column(name = "SENHA_USUARIO", nullable = false, length = 255)
    private String password;

    @Column(name = "NOME_USUARIO", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL_USUARIO", nullable = false, length = 100)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TB_USUARIOS_PERFIS",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERFIL")
    )
    private Set<Perfil> perfis = new HashSet<>();
}
