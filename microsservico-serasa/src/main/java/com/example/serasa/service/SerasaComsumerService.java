package com.example.serasa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import static com.example.serasa.constants.TopicSerasa.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SerasaComsumerService {

    private final SerasaService serasaService;

    @KafkaListener(topics = TOPIC_VERIFICAR_SERASA_REQUEST)
    @SendTo(TOPIC_VERIFICAR_SERASA_RESPONSE)
    public String consultarCpf(String cpf) {
        log.info("Recebida consulta para CPF: {}", cpf);
        boolean resultado = serasaService.consultarCpfSerasa(cpf);
        log.info("Resultado da consulta para CPF {}: {}", cpf, resultado);
        return String.valueOf(resultado);
    }

}