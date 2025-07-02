package com.example.log.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    
    /**
     * Configura a factory do consumer Kafka
     * Define as propriedades de conexão e deserialização
     * @return ConsumerFactory configurada
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);   // Configuração do servidor Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);  // Configuração do grupo do consumer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Configuração dos deserializers
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Configuração do offset - lê desde o início se não houver offset commitado
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Configuração de confirmação automática de offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(props);
    }
    
//    /**
//     * Bean para ObjectMapper usado na deserialização JSON
//     * @return ObjectMapper configurado
//     */
//    @Bean
//    public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
//        return new com.fasterxml.jackson.databind.ObjectMapper();
//    }
}