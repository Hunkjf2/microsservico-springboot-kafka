package com.example.pessoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearaService {

//    private final KafkaProducerFactory producerFactory;

    public boolean consultarCpfSerasa(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return CPFS_MOCADOS.contains(cpfLimpo);
    }

    private static final List<String> CPFS_MOCADOS = Arrays.asList(
            "12345678901",
            "98765432100",
            "11122233344",
            "55566677788",
            "99988877766"
    );



}
