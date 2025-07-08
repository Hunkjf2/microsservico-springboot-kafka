package com.example.pessoa.config.kafka.assincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaProducerAssincrono {

    private final KafkaTemplate<String, String> asyncKafkaTemplate;
    private final ObjectMapper objectMapper;

    public void enviarMensagem(String topic, Object mensagem) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(mensagem);
            asyncKafkaTemplate.send(topic, mensagemJson);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem assíncrona", e);
        }
    }

}
