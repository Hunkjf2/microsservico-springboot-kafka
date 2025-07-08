package com.example.pessoa.config.kafka;

import com.example.pessoa.config.kafka.assincrona.PessoaProducerAssincrono;
import com.example.pessoa.config.kafka.sincrona.PessoaProducerSincrono;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerFactory {

    private final PessoaProducerAssincrono pessoaProducerAssincrono;
    private final PessoaProducerSincrono pessoaProducerSincrono;

    public PessoaProducerAssincrono getAssincronoProducer() {
        return pessoaProducerAssincrono;
    }

    public PessoaProducerSincrono getSincronoProducer() {
        return pessoaProducerSincrono;
    }

}