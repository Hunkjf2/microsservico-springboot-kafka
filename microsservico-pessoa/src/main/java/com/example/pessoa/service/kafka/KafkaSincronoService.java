package com.example.pessoa.service.kafka;

import com.example.pessoa.config.exception.ProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class KafkaSincronoService {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final KafkaSerializacaoService kafkaSerializacaoService;
    private static final Duration TIMEOUT = Duration.ofSeconds(3);


    public <T> T enviarEReceber(String topic, Object payload, Class<T> responseType) {
        try {
            String stringJson = kafkaSerializacaoService.serialize(payload);

            Message<String> message = MessageBuilder
                    .withPayload(stringJson)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();

            var future = replyingKafkaTemplate.sendAndReceive(message, TIMEOUT);
            var response = future.get();

            return kafkaSerializacaoService.deserialize(response.getPayload(), responseType);
        } catch (Exception e) {
            throw new ProcessingException("Falha no envio e rebimento da mensagem:", e);
        }
    }

}
