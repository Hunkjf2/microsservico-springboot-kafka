package com.example.pessoa.config.kafka.assincrona;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ProcuderConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.retries}")
    private String retriesConfig;

    @Bean
    public ProducerFactory<String, String> criarProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retriesConfig); // Número de tentativas de reenvio em caso de falha
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Garante que reenvios não criem duplicatas no tópico
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Cria o template Kafka para envio de mensagens.
     * Utiliza a factory configurada para criar um template
     * que facilita o envio de mensagens para tópicos Kafka.
     *
     * @return KafkaTemplate configurado para operações de envio
     */
    @Bean
    public KafkaTemplate<String, String> criarKafkaTemplate() {
        return new KafkaTemplate<>(criarProducerFactory());
    }

}
