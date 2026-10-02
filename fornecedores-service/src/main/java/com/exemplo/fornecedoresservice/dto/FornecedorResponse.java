package com.exemplo.fornecedoresservice.dto;

import com.exemplo.fornecedoresservice.model.Fornecedor;

/** Representacao do fornecedor na API, desacoplada da entidade JPA. */
public record FornecedorResponse(Long id, String nome, String cnpj) {

    public static FornecedorResponse de(Fornecedor fornecedor) {
        return new FornecedorResponse(fornecedor.getId(), fornecedor.getNome(),
                fornecedor.getCnpj().formatado());
    }
}
