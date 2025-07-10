package com.example.pessoa.service;

import com.example.pessoa.dto.PessoaDto;
import com.example.pessoa.config.exception.PessoaNaoEncontradaException;
import com.example.pessoa.mapper.PessoaMapper;
import com.example.pessoa.model.Pessoa;
import com.example.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.example.pessoa.constants.global.MenssagemSistema.*;
import static com.example.pessoa.constants.log.Operacao.*;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;
    private final LogService logService;
    private final SerasaService serasaService;

    public Pessoa consultar(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNaoEncontradaException(REGISTRO_NAO_ENCONTRADO));
    }

    @Transactional
    public Pessoa cadastrar(PessoaDto pessoaDto) {
        Optional<Boolean> negativado = serasaService.consultarSituacaoFinanceira(pessoaDto);

        Pessoa pessoa = pessoaMapper.toEntity(pessoaDto);
        pessoa.setNegativado(negativado.orElse(null));

        Pessoa pessoaSalva = salvar(pessoa);
        logService.enviarDadosLog(pessoaSalva, CADASTRO);
        return pessoaSalva;
    }

    @Transactional
    public Pessoa editar(Long id, PessoaDto pessoaDto) {
        Pessoa pessoaAtualizada = this.salvar(atualizaDados(pessoaDto, this.consultar(id)));
        logService.enviarDadosLog(pessoaAtualizada, ATUALIZACAO);
        return pessoaAtualizada;
    }

    @Transactional
    public void deletarPessoa(Long id) {
        Pessoa pessoa = this.consultar(id);
        pessoaRepository.deleteById(id);
        logService.enviarDadosLog(pessoa, EXCLUSAO);
    }

    private Pessoa atualizaDados(PessoaDto pessoaDto, Pessoa pessoa) {
        pessoa.setNome(pessoaDto.nome());
        pessoa.setDataNascimento(pessoaDto.dataNascimento());
        return pessoa;
    }

    private Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

}