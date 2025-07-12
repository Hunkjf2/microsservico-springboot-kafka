package com.example.pessoa.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaAssincronoService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaSerializationService serializationService;

    public void enviar(String topic, Object payload) {
        String stringJson = serializationService.serialize(payload);
        kafkaTemplate.send(topic, stringJson);
        log.info("Mensagem enviada para tópico: {}", topic);
    }
}