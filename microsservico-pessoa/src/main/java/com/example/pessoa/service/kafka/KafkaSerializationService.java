package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.KafkaMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSerializationService {

    protected final ObjectMapper objectMapper;

    public String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Erro ao serializar objeto: {}", e.getMessage());
            throw new KafkaMessageException("Falha na serialização", e);
        }
    }

    public <T> T deserialize(Object payload, Class<T> targetType) {
        try {
            return objectMapper.convertValue(payload, targetType);
        } catch (Exception e) {
            log.error("Erro ao deserializar para {}: {}", targetType.getSimpleName(), e.getMessage());
            throw new KafkaMessageException("Falha na deserialização", e);
        }
    }

}
