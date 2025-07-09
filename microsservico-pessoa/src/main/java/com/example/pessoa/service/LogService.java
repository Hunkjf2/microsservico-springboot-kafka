package com.example.pessoa.service;

import com.example.pessoa.dto.LogEventDto;
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
        LogEventDto logEventDto = informacaoLog(pessoaMapper.toDto(pessoa), operacao);

        producerFactory.getAssincronoProducer().enviarParaTopico(
                TOPIC_ENVIAR_LOG, logEventDto
        );
    }

    private static LogEventDto informacaoLog(PessoaDto pessoaDto, String operacao) {
        return new LogEventDto(
                pessoaDto, operacao, "microservico-pessoa", 1L, "Jhon Doe"
        );
    }


}
