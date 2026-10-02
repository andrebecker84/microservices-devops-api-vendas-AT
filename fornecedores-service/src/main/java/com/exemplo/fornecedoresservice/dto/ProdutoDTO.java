package com.exemplo.fornecedoresservice.dto;

import java.math.BigDecimal;

/**
 * Produto como o produtos-service devolve em GET /produtos. So os campos que
 * este servico usa; o Feign converte o JSON da resposta nesta classe.
 */
public record ProdutoDTO(Long id, String nome, BigDecimal preco) {
}
