package com.example.pessoa.service;

import com.example.pessoa.config.kafka.KafkaProducerFactory;
import com.example.pessoa.dto.PessoaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.pessoa.constants.serasa.TopicSerasa.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class SerasaService {

    private final KafkaProducerFactory producerFactory;

    public Optional<Boolean> consultarSituacaoFinanceira(PessoaDto pessoaDto) {
        try {
            Boolean resultado = this.enviarDadosSincrono(
                    TOPIC_CONSULTAR_SERASA_REQUEST,
                    TOPIC_CONSULTAR_SERASA_RESPONSE,
                    pessoaDto.cpf(),
                    Boolean.class
            );
            return Optional.ofNullable(resultado);
        } catch (Exception e){
            log.error("Erro ao consultar situacao financeira", e);
            return Optional.empty();
        }
    }

    public <T> T enviarDadosSincrono(String topic, String replyTopic, Object dados, Class<T> responseType) {
        return producerFactory.getSincronoProducer()
                .enviarMensagem(topic, replyTopic, dados, responseType);
    }


}
