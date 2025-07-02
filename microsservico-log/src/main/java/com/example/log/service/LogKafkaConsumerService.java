package com.example.log.service;

import static com.example.log.constants.TopicLog.*;

import com.example.log.dto.LogEvent;
import com.example.log.dto.PessoaDto;
import com.example.log.model.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Consumer Kafka para processar mensagens relacionadas a Autor
 * Escuta os tópicos específicos e delega o processamento para o LogService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogKafkaConsumerService {
    
    private final LogService logService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = TOPIC_ENVIAR_LOG,
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void processarEnvioLog(@Payload String mensagem) {
        
        try {
            LogEvent logEvent = objectMapper.readValue(mensagem, LogEvent.class);
            PessoaDto pessoaDto = logEvent.pessoaDto();
            String dadosJson = objectMapper.writeValueAsString(pessoaDto);

            Log log = Log.builder()
                    .operacao(logEvent.operacao())
                    .dados(dadosJson)
                    .dataHoraCriacao(pessoaDto.dataHoraCriacao())
                    .nomeUsuario(logEvent.nomeUsuario())
                    .nomeMicroSservico(logEvent.microservico())
                    .idUsuario(pessoaDto.id())
                    .build();

            logService.cadastrarLog(log);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem de cadastro de log: {}", e.getMessage(), e);
        }

    }
    
}