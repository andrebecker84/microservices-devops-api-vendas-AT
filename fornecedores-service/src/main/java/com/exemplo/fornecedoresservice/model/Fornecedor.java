package com.exemplo.fornecedoresservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    public static final int TAMANHO_MAXIMO_NOME = 150;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = TAMANHO_MAXIMO_NOME)
    private String nome;

    @Convert(converter = CnpjConverter.class)
    @Column(nullable = false, unique = true, length = 18)
    private Cnpj cnpj;

    /** Exigido pelo JPA. Fora dele, use {@link #novo}. */
    protected Fornecedor() {
    }

    private Fornecedor(String nome, Cnpj cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    /** Unica forma de criar um fornecedor: o id e sempre gerado pelo banco. */
    public static Fornecedor novo(String nome, Cnpj cnpj) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome do fornecedor e obrigatorio");
        }
        return new Fornecedor(nome.strip(), Objects.requireNonNull(cnpj, "cnpj e obrigatorio"));
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }
}
