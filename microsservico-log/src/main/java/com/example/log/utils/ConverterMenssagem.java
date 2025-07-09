package com.example.log.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConverterMenssagem {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public <T> T desserializar(String responseValue, Class<T> responseType) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(responseValue, responseType);
    }

}