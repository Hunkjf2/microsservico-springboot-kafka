package com.example.pessoa.config.kafka.sincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PessoaProducerSincrono {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final ObjectMapper objectMapper;

    public PessoaProducerSincrono(ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public <T> T enviarMensagemSincrona(String topic, String replyTopic, Object mensagem, Class<T> responseType) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(mensagem);

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, mensagemJson);
            record.headers().add("kafka_replyTopic", replyTopic.getBytes());

            RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(record, Duration.ofSeconds(10));
            ConsumerRecord<String, String> response = future.get();

            return objectMapper.readValue(response.value(), responseType);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem síncrona", e);
        }
    }

}
