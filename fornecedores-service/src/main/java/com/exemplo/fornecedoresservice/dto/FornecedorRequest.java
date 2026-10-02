package com.exemplo.fornecedoresservice.dto;

import com.exemplo.fornecedoresservice.model.Fornecedor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Corpo do POST /fornecedores. Nao tem id: um id enviado no JSON e ignorado,
 * e o POST nunca vira atualizacao de um registro existente.
 */
public record FornecedorRequest(
        @NotBlank @Size(max = Fornecedor.TAMANHO_MAXIMO_NOME) String nome,
        @NotBlank String cnpj) {
}
