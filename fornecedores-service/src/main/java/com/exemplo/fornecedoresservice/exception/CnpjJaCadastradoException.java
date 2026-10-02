package com.exemplo.fornecedoresservice.exception;

import com.exemplo.fornecedoresservice.model.Cnpj;

public class CnpjJaCadastradoException extends RuntimeException {

    public CnpjJaCadastradoException(Cnpj cnpj) {
        super("Ja existe fornecedor com o CNPJ " + cnpj.formatado());
    }
}
