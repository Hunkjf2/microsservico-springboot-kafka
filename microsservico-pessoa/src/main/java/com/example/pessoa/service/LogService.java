package com.example.pessoa.service;

import com.example.pessoa.dto.LogEvent;
import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.config.kafka.KafkaProducerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.pessoa.constants.log.TopicLog.*;

@RequiredArgsConstructor
@Service
public class LogService {

    private final KafkaProducerFactory producerFactory;

    // Para comunicação assíncrona
    public void enviarDadosLog(PessoaDto pessoaDto, String operacao) {
        LogEvent logEvent = informacaoLog(pessoaDto, operacao);
        producerFactory.criarPessoaProducer().enviarMenssagem(
                TOPIC_ENVIAR_LOG, logEvent
        );
    }


    // Para comunicação sincrona
    public <T> T enviarDadosSincrono(String topic, String replyTopic, Object dados, Class<T> responseType) {
        return producerFactory.criarProducerSincrono()
                .enviarMensagemSincrona(topic, replyTopic, dados, responseType);
    }

    private static LogEvent informacaoLog(PessoaDto pessoaDto, String operacao) {
        return new LogEvent(
                pessoaDto, operacao, "microservico-pessoa", 1L, "Jhon Doe"
        );
    }

}
