package com.example.pessoa.config.kafka.assincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaProducerAssincrono {

    private final KafkaTemplate<String, String> criarKafkaTemplate;
    private final ObjectMapper objectMapper;

    public void enviarParaTopico(String topic, Object mensagem) {
        try {
            String mensagemSerializada = objectMapper.writeValueAsString(mensagem);
            criarKafkaTemplate.send(topic, mensagemSerializada);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem assíncrona", e);
        }
    }

}
