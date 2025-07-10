package com.example.pessoa.service;

import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.service.kafka.KafkaSincronoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;
import static com.example.pessoa.constants.serasa.TopicSerasa.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class SerasaService {

    private final KafkaSincronoService kafkaSincronoService;

    public Optional<Boolean> consultarSituacaoFinanceira(PessoaDto pessoaDto) {
        Boolean resultado = kafkaSincronoService.sendAndReceive(
                TOPIC_VERIFICAR_SERASA_REQUEST,
                pessoaDto.cpf(),
                Boolean.class
        );
        return Optional.ofNullable(resultado);
    }
}
