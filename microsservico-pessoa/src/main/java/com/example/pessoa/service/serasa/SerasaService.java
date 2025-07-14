package com.example.pessoa.service.serasa;

import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.service.kafka.KafkaSincronoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.example.pessoa.constants.serasa.TopicSerasa.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class SerasaService {

    private final KafkaSincronoService kafkaSincronoService;

    @CircuitBreaker(name = "microsservico-serasa", fallbackMethod = "fallbackEnvio")
    public Boolean consultarSituacaoFinanceira(String cpf) {
        String resultado = kafkaSincronoService.enviarEReceber(
                 TOPIC_VERIFICAR_SERASA_REQUEST,
                 cpf);
        log.info("Situação financeira consultada para CPF {}: {}", cpf, resultado);

        return Boolean.parseBoolean(resultado);
    }

    @SuppressWarnings("unused")
    public Boolean fallbackEnvio(PessoaDto pessoaDto, Exception ex) {
        return null;
    }

}
