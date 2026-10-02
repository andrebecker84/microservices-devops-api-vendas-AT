package com.example.vendas_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Corpo do POST /vendas. Nao tem id nem preco: o id e gerado pelo banco e o preco
 * vem do produtos-service, entao o cliente nao consegue escolher quanto paga.
 */
public record VendaRequest(@NotNull Long idProduto, @NotNull @Positive Integer quantidade) {
}
