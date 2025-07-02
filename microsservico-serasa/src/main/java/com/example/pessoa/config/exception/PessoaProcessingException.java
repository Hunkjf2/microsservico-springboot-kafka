package com.example.pessoa.config.exception;

public class PessoaProcessingException extends RuntimeException {
    public PessoaProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}