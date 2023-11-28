package br.com.lucascosta.lcmoneyapi.service;

import br.com.lucascosta.lcmoneyapi.model.Pessoa;
import br.com.lucascosta.lcmoneyapi.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        var pessoaSalva = buscarPessoaPeloId(id);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivoPessoa(Long id, Boolean ativo) {
        var pessoaSalva = buscarPessoaPeloId(id);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public Pessoa buscarPessoaPeloId(Long id) {
        var pessoaSalva = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        return pessoaSalva;
    }
}
