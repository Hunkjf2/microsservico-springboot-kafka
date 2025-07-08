package com.example.pessoa.config.kafka.sincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PessoaProducerSincrono {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final ObjectMapper objectMapper;
    private static Integer timeout = 10;

    public <T> T enviarMensagem(String topic, String replyTopic, Object mensagem, Class<T> responseType) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(mensagem);

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, mensagemJson);
            producerRecord.headers().add("kafka_replyTopic", replyTopic.getBytes());

            RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(
                    producerRecord, Duration.ofSeconds(timeout)
            );
            ConsumerRecord<String, String> response = future.get();

            return objectMapper.readValue(response.value(), responseType);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem síncrona", e);
        }
    }

}
