package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.ProcessingException;
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

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final KafkaSerializationService serializationService;
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    @CircuitBreaker(name = "microsservico-serasa", fallbackMethod = "fallbackEnvio")
    public <T> T enviarEReceber(String topic, Object payload, Class<T> responseType) {
        try {
            String stringJson = serializationService.serialize(payload);

            Message<String> message = MessageBuilder
                    .withPayload(stringJson)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();

            var future = replyingKafkaTemplate.sendAndReceive(message, TIMEOUT);
            var response = future.get();

            return serializationService.deserialize(response.getPayload(), responseType);
        } catch (Exception e) {
            throw new ProcessingException("Falha na comunicação", e);
        }
    }

    @SuppressWarnings("unused")
    private <T> T fallbackEnvio(String topic, Object payload, Class<T> responseType, Throwable ex) {
        log.warn("Fallback ativado para tópico '{}'. Erro: {}", topic, ex.getMessage());
        return null;
    }
}
