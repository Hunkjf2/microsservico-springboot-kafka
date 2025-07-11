package com.example.pessoa.config.exception;

import com.example.pessoa.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String message) {
        super(message);
    }
}
