package com.example.serasa.service.serasa;

import com.example.serasa.config.exception.SerasaMessageException;
import com.example.serasa.service.kafka.KafkaSerializacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import static com.example.serasa.constants.TopicSerasa.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class SerasaConsumerService {

    private final SerasaService serasaService;
    private final KafkaSerializacaoService kafkaSerializacaoService;

    @KafkaListener(topics = TOPIC_VERIFICAR_SERASA_REQUEST)
    @SendTo(TOPIC_VERIFICAR_SERASA_RESPONSE)
    public String consultarCpf(String mensagem, Acknowledgment acknowledgment) {
        try {
            String cpf = kafkaSerializacaoService.deserialize(mensagem, String.class);

            boolean resultado = serasaService.consultarCpfSerasa(cpf);

            log.info("Resultado da consulta para CPF {}: {}", cpf, resultado);
            acknowledgment.acknowledge();
            return String.valueOf(resultado);
        } catch (Exception e) {
            throw new SerasaMessageException("Falha no processamento:", e);
        }
    }

}