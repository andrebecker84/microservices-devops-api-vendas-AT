package com.exemplo.fornecedoresservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(CnpjInvalidoException.class)
    ProblemDetail cnpjInvalido(CnpjInvalidoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CnpjJaCadastradoException.class)
    ProblemDetail cnpjJaCadastrado(CnpjJaCadastradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    // Corrida entre dois cadastros com o mesmo CNPJ: a constraint UNIQUE recusa o segundo.
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail violacaoDeIntegridade(DataIntegrityViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "O registro conflita com um fornecedor ja cadastrado");
    }

    @ExceptionHandler(ProdutosIndisponiveisException.class)
    ProblemDetail produtosIndisponiveis(ProdutosIndisponiveisException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }
}
