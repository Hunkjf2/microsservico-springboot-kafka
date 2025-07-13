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
    public Boolean consultarSituacaoFinanceira(PessoaDto pessoaDto) {
        Boolean resultado = kafkaSincronoService.enviarEReceber(
                 TOPIC_VERIFICAR_SERASA_REQUEST,
                 pessoaDto.cpf(),
                 Boolean.class);
        log.info("Situação financeira consultada para CPF {}: {}", pessoaDto.cpf(), resultado);
        return resultado;
    }

    @SuppressWarnings("unused")
    public Boolean fallbackConsultaSerasa(PessoaDto pessoaDto, Exception ex) {
        log.warn("Fallback ativado para consulta Serasa. CPF: {}. Erro: {}",
                pessoaDto.cpf(), ex.getMessage());

        return false;
    }

}
