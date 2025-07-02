package com.example.pessoa.controller;

import com.example.pessoa.service.SearaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/serasa")
public class SerasaController {

    private final SearaService searaService;

    @GetMapping("/{cpf}")
    public ResponseEntity<Boolean> consultar(@PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.CREATED).body(searaService.consultarCpfSerasa(cpf));
    }

}
