package com.Fiap.OdontoCare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String nome;


    private LocalDateTime dataNascimento;

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = LocalDate.parse(dataNascimento, DateTimeFormatter.ISO_LOCAL_DATE)
                .atStartOfDay();
    }

    @NotBlank
    @Size(max = 14)
    private String cpf;

    @NotNull
    private Long carteirinha;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @NotBlank
    @Email(message = "Email inválido")
    private String email;

    @NotBlank
    @Size(max = 200)
    private String endereco;
}
