package com.example.log.mapper;

import com.example.log.dto.LogDto;
import com.example.log.model.Log;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {

    public Log toEntity(LogDto dto) {
        return Log.builder()
                .idUsuario(dto.idUsuario())
                .nomeUsuario(dto.nomeUsuario())
                .operacao(dto.operacao())
                .nomeMicroSservico(dto.nomeMicroSservico())
                .dataHoraCriacao(dto.dataHoraCriacao())
                .build();
    }

}