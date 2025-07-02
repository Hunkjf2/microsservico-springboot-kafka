package com.example.pessoa.controller;

import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pessoa")
@Tag(name = "Pessoa", description = "Operações relacionadas a pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    @Operation(summary = "Cadastrar pessoa")
    public ResponseEntity<Object> cadastrar(@RequestBody PessoaDto pessoaDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                pessoaService.cadastrar(pessoaDto)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa")
    public ResponseEntity<Object> editar(@PathVariable Long id, @RequestBody PessoaDto pessoaDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                pessoaService.editar(id, pessoaDto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pessoa")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pessoaService.deletarPessoa(id);
        return ResponseEntity.noContent().build();
    }

}
