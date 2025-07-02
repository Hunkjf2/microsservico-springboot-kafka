package com.example.pessoa.config.kafka;

import com.example.pessoa.config.kafka.assincrona.PessoaProducerAssincrono;
import com.example.pessoa.config.kafka.sincrona.PessoaProducerSincrono;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Factory para criar e gerenciar producers do Kafka
 * Implementa o padrão Factory para centralizar a criação de producers
 * Facilita a manutenção e permite diferentes configurações por tipo de producer
 */
@Component
@RequiredArgsConstructor
public class KafkaProducerFactory {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    public PessoaProducerAssincrono criarPessoaProducer() {
        return new PessoaProducerAssincrono(kafkaTemplate);
    }

    public PessoaProducerSincrono criarProducerSincrono() {
        return new PessoaProducerSincrono(replyingKafkaTemplate);
    }

}