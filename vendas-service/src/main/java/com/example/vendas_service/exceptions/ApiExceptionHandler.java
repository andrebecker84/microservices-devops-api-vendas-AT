package com.example.vendas_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Traduz as falhas previsiveis em respostas HTTP no formato Problem Details (RFC 9457).
 * A classe base cobre os erros do proprio Spring MVC, como JSON malformado e @Valid (400).
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // 422, e nao 404: a rota /vendas existe; o que nao existe e o produto citado no corpo.
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    ProblemDetail produtoNaoEncontrado(ProdutoNaoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
    }

    @ExceptionHandler(ProdutosIndisponiveisException.class)
    ProblemDetail produtosIndisponiveis(ProdutosIndisponiveisException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }
}
