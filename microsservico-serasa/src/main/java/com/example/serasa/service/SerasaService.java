package com.example.serasa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class SerasaService {

    public boolean consultarCpfSerasa(String cpf) {
        boolean resultado = CPFS_NEGATIVADOS.contains(cpf);
        log.info("Resultado da consulta para CPF {}: {}", cpf, resultado);
        return resultado;
    }

    private static final Set<String> CPFS_NEGATIVADOS = Set.of(
            "18142226006",
            "16470435068"
    );

}