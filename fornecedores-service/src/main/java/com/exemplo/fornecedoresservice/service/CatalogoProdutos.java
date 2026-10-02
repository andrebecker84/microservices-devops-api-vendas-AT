package com.exemplo.fornecedoresservice.service;

import com.exemplo.fornecedoresservice.client.ProdutoClient;
import com.exemplo.fornecedoresservice.dto.ProdutoDTO;
import com.exemplo.fornecedoresservice.exception.ProdutosIndisponiveisException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Acesso ao catalogo do produtos-service. Isola o resto do servico do Feign: quem chama
 * recebe produtos ou uma excecao do proprio dominio, nunca um erro HTTP de outro servico.
 */
@Service
public class CatalogoProdutos {

    private static final Logger log = LoggerFactory.getLogger(CatalogoProdutos.class);

    private final ProdutoClient produtoClient;

    public CatalogoProdutos(ProdutoClient produtoClient) {
        this.produtoClient = produtoClient;
    }

    public List<ProdutoDTO> listarTodos() {
        try {
            return produtoClient.listarTodos();
        } catch (FeignException e) {
            log.warn("Falha ao consultar o produtos-service: status={} {}", e.status(), e.getMessage());
            throw new ProdutosIndisponiveisException(e);
        }
    }
}
