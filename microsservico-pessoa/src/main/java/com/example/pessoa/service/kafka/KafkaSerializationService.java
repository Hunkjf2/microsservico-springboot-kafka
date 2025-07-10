package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.KafkaMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSerializationService {

    protected final ObjectMapper objectMapper;

    public String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new KafkaMessageException("Falha na serialização", e);
        }
    }

    public <T> T deserialize(Object payload, Class<T> targetType) {
        try {
            return objectMapper.convertValue(payload, targetType);
        } catch (Exception e) {
            throw new KafkaMessageException("Falha na deserialização", e);
        }
    }

}
