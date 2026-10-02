package com.example.vendas_service.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(Long idProduto) {
        super("Produto " + idProduto + " nao encontrado");
    }
}
