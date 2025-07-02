package com.example.pessoa.config.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
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

    public SerasaProducer criarSerasaProducer() {
        return new SerasaProducer(kafkaTemplate);
    }

}