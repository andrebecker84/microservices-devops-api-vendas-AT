package com.exemplo.fornecedoresservice.exception;

public class ProdutosIndisponiveisException extends RuntimeException {

    public ProdutosIndisponiveisException(Throwable causa) {
        super("produtos-service indisponivel", causa);
    }
}
