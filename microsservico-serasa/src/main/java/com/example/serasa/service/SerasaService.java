package com.example.serasa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SerasaService {

    public boolean consultarCpfSerasa(String cpf) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        return CPFS_MOCADOS.contains(cpfLimpo);
    }

    private static final List<String> CPFS_MOCADOS = Arrays.asList(
            "18142226006",
            "16470435068"
    );

}