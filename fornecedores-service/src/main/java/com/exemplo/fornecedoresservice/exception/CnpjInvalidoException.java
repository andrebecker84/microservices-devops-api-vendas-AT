package com.exemplo.fornecedoresservice.exception;

public class CnpjInvalidoException extends RuntimeException {

    public CnpjInvalidoException(String informado) {
        super("CNPJ invalido: " + informado);
    }
}
