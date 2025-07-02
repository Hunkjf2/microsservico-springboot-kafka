package com.example.pessoa.config.kafka;

import com.example.pessoa.config.exception.PessoaProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SerasaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SerasaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public void enviarMenssagem(String topic, Object mensagem) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(mensagem);
            kafkaTemplate.send(topic, mensagemJson);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar ", e);
        }
    }

}
