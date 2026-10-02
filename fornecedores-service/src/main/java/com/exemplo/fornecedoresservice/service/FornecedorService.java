package com.exemplo.fornecedoresservice.service;

import com.exemplo.fornecedoresservice.exception.CnpjJaCadastradoException;
import com.exemplo.fornecedoresservice.model.Cnpj;
import com.exemplo.fornecedoresservice.model.Fornecedor;
import com.exemplo.fornecedoresservice.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Regra de negocio de Fornecedor. O controller nao fala direto com o repository,
 * fala com este service.
 */
@Service
@Transactional(readOnly = true)
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    // O existsByCnpj da a mensagem clara no caso comum. Dois cadastros simultaneos com o mesmo
    // CNPJ passam juntos por ele; nesse caso quem decide e a constraint UNIQUE do banco.
    @Transactional
    public Fornecedor cadastrar(String nome, String cnpjInformado) {
        Cnpj cnpj = Cnpj.of(cnpjInformado);
        if (fornecedorRepository.existsByCnpj(cnpj)) {
            throw new CnpjJaCadastradoException(cnpj);
        }
        return fornecedorRepository.save(Fornecedor.novo(nome, cnpj));
    }
}
