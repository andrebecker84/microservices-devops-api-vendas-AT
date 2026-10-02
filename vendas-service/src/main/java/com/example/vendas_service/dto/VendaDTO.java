package com.example.vendas_service.dto;

import com.example.vendas_service.models.Venda;

import java.math.BigDecimal;

/** Representacao da venda na API, desacoplada da entidade JPA. */
public record VendaDTO(Long id, Long idProduto, Integer quantidade, BigDecimal preco) {

    public static VendaDTO de(Venda venda) {
        return new VendaDTO(venda.getId(), venda.getIdProduto(), venda.getQuantidade(),
                venda.getValorProduto());
    }
}
