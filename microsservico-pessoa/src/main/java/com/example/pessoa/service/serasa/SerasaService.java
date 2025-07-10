package com.example.pessoa.service.serasa;

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

    public Boolean consultarSituacaoFinanceira(PessoaDto pessoaDto) {
         return kafkaSincronoService.sendAndReceive(
                TOPIC_VERIFICAR_SERASA_REQUEST,
                pessoaDto.cpf(),
                Boolean.class
        );
    }
}
