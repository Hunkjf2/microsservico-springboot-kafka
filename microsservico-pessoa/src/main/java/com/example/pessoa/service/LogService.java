package com.example.pessoa.service;

import com.example.pessoa.dto.LogEvent;
import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.config.kafka.KafkaProducerFactory;
import com.example.pessoa.mapper.PessoaMapper;
import com.example.pessoa.model.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.pessoa.constants.log.TopicLog.*;

@RequiredArgsConstructor
@Service
public class LogService {

    private final KafkaProducerFactory producerFactory;
    private final PessoaMapper pessoaMapper;

    public void enviarDadosLog(Pessoa pessoa, String operacao) {
        PessoaDto pessoaDto = pessoaMapper.toDto(pessoa);
        LogEvent logEvent = informacaoLog(pessoaDto, operacao);
        producerFactory.getAssincronoProducer().enviarMensagem(
                TOPIC_ENVIAR_LOG, logEvent
        );
    }

    private static LogEvent informacaoLog(PessoaDto pessoaDto, String operacao) {
        return new LogEvent(
                pessoaDto, operacao, "microservico-pessoa", 1L, "Jhon Doe"
        );
    }


}
