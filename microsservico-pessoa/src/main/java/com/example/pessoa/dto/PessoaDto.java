package com.example.pessoa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PessoaDto(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
        String cpf,

        @NotBlank(message = "Data de Nascimento é obrigatório")
        @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
        LocalDate dataNascimento,

        Boolean negativado,

        LocalDateTime dataHoraCriacao
) {}