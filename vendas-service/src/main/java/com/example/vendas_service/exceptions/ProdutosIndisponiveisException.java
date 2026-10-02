package com.example.vendas_service.exceptions;

public class ProdutosIndisponiveisException extends RuntimeException {

    public ProdutosIndisponiveisException(Throwable causa) {
        super("produtos-service indisponivel", causa);
    }
}
