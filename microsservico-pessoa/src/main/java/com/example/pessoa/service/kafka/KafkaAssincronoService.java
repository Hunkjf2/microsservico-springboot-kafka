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

    public void publicarMensagem(String topic, Object payload) {
        String mensagemJson = serializationService.serialize(payload);
        kafkaTemplate.send(topic, mensagemJson);
        log.debug("Mensagem enviada para tópico: {}", topic);
    }
}