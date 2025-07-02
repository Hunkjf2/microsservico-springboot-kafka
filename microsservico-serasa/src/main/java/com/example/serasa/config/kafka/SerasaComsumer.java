package com.example.serasa.config.kafka;

import static com.example.serasa.constants.TopicSerasa.*;
import com.example.serasa.service.SerasaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SerasaComsumer {

    private final SerasaService serasaService;

    @KafkaListener(topics = TOPIC_CONSULTAR_SERASA_REQUEST, groupId = "${spring.kafka.consumer.group-id}")
    @SendTo(TOPIC_CONSULTAR_SERASA_RESPONSE)
    public String consultarCpf(String cpf) {
        log.info("Recebida consulta para CPF: {}", cpf);
        boolean resultado = serasaService.consultarCpfSerasa(cpf);
        log.info("Resultado da consulta para CPF {}: {}", cpf, resultado);
        return String.valueOf(resultado);
    }

}


