package com.example.pessoa.config.kafka.sincrona;

import static com.example.pessoa.constants.serasa.TopicSerasa.*;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaSincronaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private final ProducerFactory<String, String> assincronoProducerFactory;

    @Bean
    public ConsumerFactory<String, String> syncConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ProducerFactory<String, String> syncProducerFactory() {
        return assincronoProducerFactory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_CONSULTAR_SERASA_RESPONSE);
        return new ConcurrentMessageListenerContainer<>(syncConsumerFactory(), containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(syncProducerFactory(), replyContainer());
    }

}