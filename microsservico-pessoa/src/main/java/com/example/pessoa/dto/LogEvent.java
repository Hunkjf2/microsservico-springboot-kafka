package com.example.pessoa.dto;

public record LogEvent(
        PessoaDto pessoaDto,
        String operacao,
        String microservico,
        Long idUsuario,
        String nomeUsuario
) {
}
