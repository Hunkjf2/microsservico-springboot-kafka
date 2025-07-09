package com.example.log.service;

import com.example.log.dto.LogEventDto;
import com.example.log.dto.PessoaDto;
import com.example.log.model.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static com.example.log.constants.TopicLog.*;
import static com.example.log.utils.ConverterMenssagem.*;

/**
 * Consumer Kafka para processar mensagens relacionadas a Autor
 * Escuta os tópicos específicos e delega o processamento para o LogService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogConsumerService {
    
    private final LogService logService;

    @KafkaListener(
        topics = TOPIC_ENVIAR_LOG,
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void processarEnvioLog(String mensagem) {
        try {

            LogEventDto logEventDto = desserializar(mensagem, LogEventDto.class);
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
            log.error("Erro ao processar mensagem de cadastro de log: {}", e.getMessage(), e);
        }

    }
    
}