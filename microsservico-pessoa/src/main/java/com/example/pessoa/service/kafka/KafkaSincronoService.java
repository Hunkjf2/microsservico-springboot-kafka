package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.KafkaMessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSincronoService {

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    @CircuitBreaker(name = "microsservico-serasa", fallbackMethod = "fallbackSend")
    public <T> T sendAndReceive(String topic, Object payload, Class<T> responseType) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(payload);

            Message<String> message = MessageBuilder
                    .withPayload(mensagemJson)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();

            var future = replyingKafkaTemplate.sendAndReceive(message, TIMEOUT);
            var response = future.get();

            return objectMapper.convertValue(response.getPayload(), responseType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrompida durante comunicação com tópico {}", topic);
            throw new KafkaMessageException("Operação foi interrompida", e);
        } catch (Exception e) {
            log.error("Erro na comunicação síncrona com tópico {}: {}", topic, e.getMessage());
            throw new KafkaMessageException("Falha na comunicação síncrona", e);
        }
    }

    @SuppressWarnings("unused")
    private <T> T fallbackSend(String topic, Object payload, Class<T> responseType, Throwable ex) {
        log.warn("Fallback ativado para tópico '{}'. Erro: {}", topic, ex.getMessage());
        return null;
    }
}
