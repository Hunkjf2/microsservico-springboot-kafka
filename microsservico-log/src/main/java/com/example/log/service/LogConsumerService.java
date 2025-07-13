package com.example.log.service;

import com.example.log.config.exception.ProcessingException;
import com.example.log.dto.LogEventDto;
import com.example.log.dto.PessoaDto;
import com.example.log.model.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static com.example.log.constants.TopicLog.*;

@Service
@RequiredArgsConstructor
public class LogConsumerService {
    
    private final LogService logService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = TOPIC_ENVIAR_LOG
    )
    public void processarEnvioLog(String mensagem) {
        try {

            LogEventDto logEventDto = objectMapper.readValue(mensagem, LogEventDto.class);
            PessoaDto pessoaDto = logEventDto.pessoaDto();

            Log log = Log.builder()
                    .operacao(logEventDto.operacao())
                    .dados(mensagem)
                    .dataHoraCriacao(pessoaDto.dataHoraCriacao())
                    .nomeUsuario(logEventDto.nomeUsuario())
                    .nomeMicroSservico(logEventDto.microservico())
                    .idUsuario(pessoaDto.id())
                    .build();

            logService.cadastrarLog(log);
        } catch (Exception e) {
            throw new ProcessingException("Erro ao processar mensagem de cadastro de log: {}", e);
        }

    }
    
}