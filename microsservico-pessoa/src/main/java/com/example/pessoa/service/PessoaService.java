package com.example.pessoa.service;

import static com.example.pessoa.constants.log.Operacao.*;
import static com.example.pessoa.constants.serasa.TopicSerasa.*;
import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.config.exception.PessoaNaoEncontradaException;
import com.example.pessoa.mapper.PessoaMapper;
import com.example.pessoa.model.Pessoa;
import com.example.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.pessoa.constants.global.MenssagemSistema.*;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;
    private final LogService logService;

    public Pessoa consultar(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(REGISTRO_NAO_ENCONTRADO));
    }

    private Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa cadastrar(PessoaDto pessoaDto) {
        Pessoa pessoa = this.salvar(pessoaMapper.toEntity(pessoaDto));

        // Consulta síncrona ao Serasa
        Boolean negativado = consultarSituacaoFinanceira(pessoaDto);
        pessoa.setNegativado(negativado);

        logService.enviarDadosLog(pessoaMapper.toDto(pessoa), CADASTRO);
        return pessoa;
    }

    private Boolean consultarSituacaoFinanceira(PessoaDto pessoaDto) {
        Boolean negativado = logService.enviarDadosSincrono(
                TOPIC_CONSULTAR_SERASA_REQUEST,
                TOPIC_CONSULTAR_SERASA_RESPONSE,
                pessoaDto.cpf(),
                Boolean.class
        );
        return negativado;
    }

    public Pessoa editar(Long id, PessoaDto pessoaDto) {
        Pessoa pessoa = this.consultar(id);
        Pessoa pessoaAtualizada = this.salvar(atualizaDados(pessoaDto, pessoa));
        logService.enviarDadosLog(pessoaMapper.toDto(pessoaAtualizada), ATUALIZACAO);
        return pessoaAtualizada;
    }

    private Pessoa atualizaDados(PessoaDto pessoaDto, Pessoa pessoa) {
        pessoa.setNome(pessoaDto.nome());
        pessoa.setDataNascimento(pessoaDto.dataNascimento());
        return pessoa;
    }

    public void deletarPessoa(Long id) {
        Pessoa pessoa = this.consultar(id);
        PessoaDto pessoaDto = pessoaMapper.toDto(pessoa);
        pessoaRepository.deleteById(id);
        logService.enviarDadosLog(pessoaDto, EXCLUSAO);
    }

}