package com.example.pessoa.service;

import static com.example.pessoa.constants.log.Operacao.*;
import static com.example.pessoa.constants.serasa.TopicSerasa.*;
import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.config.exception.PessoaNaoEncontradaException;
import com.example.pessoa.mapper.PessoaMapper;
import com.example.pessoa.model.Pessoa;
import com.example.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.pessoa.constants.global.MenssagemSistema.*;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public Pessoa cadastrar(PessoaDto pessoaDto) {
        Boolean negativado = consultarSituacaoFinanceira(pessoaDto);

        Pessoa pessoa = pessoaMapper.toEntity(pessoaDto);
        pessoa.setNegativado(negativado);

        Pessoa pessoaSalva = salvar(pessoa);
        enviarLog(pessoaSalva);

        return pessoaSalva;
    }

    private void enviarLog(Pessoa pessoa) {
        logService.enviarDadosLog(pessoaMapper.toDto(
                pessoa
        ), CADASTRO);
    }

    private Boolean consultarSituacaoFinanceira(PessoaDto pessoaDto) {
        try {
            return logService.enviarDadosSincrono(
                    TOPIC_CONSULTAR_SERASA_REQUEST,
                    TOPIC_CONSULTAR_SERASA_RESPONSE,
                    pessoaDto.cpf(),
                    Boolean.class
            );
        } catch (Exception e){
            log.error("Erro ao consultar situacao financeira", e);
            return null;
        }
    }

    @Transactional
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

    @Transactional
    public void deletarPessoa(Long id) {
        Pessoa pessoa = this.consultar(id);
        PessoaDto pessoaDto = pessoaMapper.toDto(pessoa);
        pessoaRepository.deleteById(id);
        logService.enviarDadosLog(pessoaDto, EXCLUSAO);
    }

}