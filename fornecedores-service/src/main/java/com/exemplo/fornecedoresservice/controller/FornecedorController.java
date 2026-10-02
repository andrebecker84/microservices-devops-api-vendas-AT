package com.exemplo.fornecedoresservice.controller;

import com.exemplo.fornecedoresservice.dto.FornecedorRequest;
import com.exemplo.fornecedoresservice.dto.FornecedorResponse;
import com.exemplo.fornecedoresservice.dto.ProdutoDTO;
import com.exemplo.fornecedoresservice.service.CatalogoProdutos;
import com.exemplo.fornecedoresservice.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;
    private final CatalogoProdutos catalogoProdutos;

    public FornecedorController(FornecedorService fornecedorService, CatalogoProdutos catalogoProdutos) {
        this.fornecedorService = fornecedorService;
        this.catalogoProdutos = catalogoProdutos;
    }

    @GetMapping
    public List<FornecedorResponse> listarTodos() {
        return fornecedorService.listarTodos().stream()
                .map(FornecedorResponse::de)
                .toList();
    }

    // Mesmo tratamento do ProdutoController: o Optional vazio vira 404.
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id)
                .map(FornecedorResponse::de)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FornecedorResponse> cadastrar(@Valid @RequestBody FornecedorRequest request) {
        FornecedorResponse criado = FornecedorResponse.de(
                fornecedorService.cadastrar(request.nome(), request.cnpj()));
        return ResponseEntity.created(URI.create("/fornecedores/" + criado.id())).body(criado);
    }

    // Produtos obtidos do produtos-service via Feign. O caminho literal "/produtos"
    // tem precedencia sobre "/{id}", entao as duas rotas convivem.
    @GetMapping("/produtos")
    public List<ProdutoDTO> listarProdutos() {
        return catalogoProdutos.listarTodos();
    }
}
