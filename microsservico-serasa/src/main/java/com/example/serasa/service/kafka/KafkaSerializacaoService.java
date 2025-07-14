package com.example.serasa.service.kafka;

import com.example.serasa.config.exception.SerasaMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSerializacaoService {

    private final ObjectMapper objectMapper;

    public <T> T deserialize(String payload, Class<T> targetType) {
        try {
            return objectMapper.readValue(payload, targetType);
        } catch (Exception e) {
            throw new SerasaMessageException("Falha na deserialização:", e);
        }
    }

}