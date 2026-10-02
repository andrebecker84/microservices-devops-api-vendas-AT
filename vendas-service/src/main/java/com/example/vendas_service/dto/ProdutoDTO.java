package com.example.vendas_service.dto;

import java.math.BigDecimal;

/** Produto como o produtos-service devolve em GET /produtos/{id}. */
public record ProdutoDTO(Long id, String nome, BigDecimal preco) {
}
