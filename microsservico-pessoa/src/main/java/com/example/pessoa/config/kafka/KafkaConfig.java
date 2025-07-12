package com.example.pessoa.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import java.util.Map;
import static com.example.pessoa.constants.serasa.TopicSerasa.*;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.producer.retries}")
    private String retriesConfig;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    // Assincrona

    // Producer para comunicação assíncrona
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.RETRIES_CONFIG, retriesConfig,
                ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true
        ));
    }

    // Template para comunicação assíncrona
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    // Sincrona
    // Consumer para comunicação síncrona
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true
        ));
    }

    // Container para reply topics
    @Bean
    public ConcurrentMessageListenerContainer<String, String> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_VERIFICAR_SERASA_RESPONSE);
        return new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
    }

    // Template para comunicação síncrona
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(producerFactory(), replyContainer());
    }
}
