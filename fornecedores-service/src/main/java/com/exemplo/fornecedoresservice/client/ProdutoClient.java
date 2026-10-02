package com.exemplo.fornecedoresservice.client;

import com.exemplo.fornecedoresservice.dto.ProdutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Cliente Feign do produtos-service. O "name" e o nome registrado no Eureka:
 * o LoadBalancer troca esse nome por uma instancia real, sem IP nem porta no codigo.
 * E o mesmo caminho do ProdutoInterface do vendas-service, mudando so o metodo chamado.
 */
@FeignClient(name = "produtos-service")
public interface ProdutoClient {

    @GetMapping("/produtos")
    List<ProdutoDTO> listarTodos();
}
