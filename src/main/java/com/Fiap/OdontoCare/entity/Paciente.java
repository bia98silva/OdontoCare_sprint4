package com.Fiap.OdontoCare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "Paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Paciente")
    private Long id;

    @Column(name = "Nome",  length = 30)
    private String nome;

    @Column(name = "Data_Nascimento")
    private LocalDateTime dataNascimento;

    @Column(name = "CPF", unique = true,  length = 14)
    private String cpf;

    @Column(name = "Carteirinha", unique = true )
    private Long carteirinha;

    @Column(name = "Telefone", length = 20)
    private String telefone;

    @Column(name = "Endereco",  length = 200)
    private String endereco;

    @Column(name = "Email" )
    @Email(message = "Email inválido")
    private String email;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

}
