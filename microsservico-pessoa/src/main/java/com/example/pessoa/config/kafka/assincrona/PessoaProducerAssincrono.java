package com.example.pessoa.config.kafka.assincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import static com.example.pessoa.utils.ConverterMenssagem.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaProducerAssincrono {

    private final KafkaTemplate<String, String> criarKafkaTemplate;

    public void enviarParaTopico(String topic, Object mensagem) {
        try {
            criarKafkaTemplate.send(topic, serializar(mensagem));
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem assíncrona", e);
        }
    }

}
