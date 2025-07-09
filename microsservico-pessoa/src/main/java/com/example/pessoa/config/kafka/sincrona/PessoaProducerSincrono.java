package com.example.pessoa.config.kafka.sincrona;

import com.example.pessoa.config.exception.PessoaProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static com.example.pessoa.utils.ConverterMenssagem.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PessoaProducerSincrono {

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private static final Integer TIMEOUT_SECONDS = 3;

    @CircuitBreaker(name = "serasa-service", fallbackMethod = "fallbackEnviarMensagem")
    public <T> T enviarMensagem(String topic, String replyTopic, Object mensagem, Class<T> responseType) {
        try {
            ProducerRecord<String, String> request = criarProducerRecord(topic, replyTopic, serializar(mensagem));
            ConsumerRecord<String, String> response = enviarEvento(request);

            return desserializar(response.value(), responseType);
        } catch (Exception e) {
            throw new PessoaProcessingException("Erro ao processar mensagem síncrona", e);
        }
    }

    private ProducerRecord<String, String> criarProducerRecord(String topic, String replyTopic, String mensagemJson) {
        ProducerRecord<String, String> request = new ProducerRecord<>(topic, mensagemJson);
        request.headers().add("kafka_replyTopic", replyTopic.getBytes());
        return request;
    }

    private ConsumerRecord<String, String> enviarEvento(ProducerRecord<String, String> request)
            throws InterruptedException, ExecutionException, TimeoutException {
        RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(request);
        return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unused")
    public <T> T fallbackEnviarMensagem(String topic, String replyTopic, Object mensagem, Class<T> responseType, Throwable throwable) {
        log.warn("Serviço Serasa indisponível para tópico '{}'. Fallback ativado. Erro: {}", topic, throwable.getMessage());
        return null;
    }

}