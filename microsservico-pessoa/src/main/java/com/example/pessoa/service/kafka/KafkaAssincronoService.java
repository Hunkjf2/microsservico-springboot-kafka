package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.KafkaMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaAssincronoService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, Object payload) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, mensagemJson);
            log.debug("Mensagem enviada para tópico: {}", topic);
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem para tópico {}: {}", topic, e.getMessage());
            throw new KafkaMessageException("Falha no envio da mensagem", e);
        }
    }
}