package com.example.vendas_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idProduto;

    @Column(nullable = false)
    private Integer quantidade;

    // Dinheiro em BigDecimal: com Double, 0.1 + 0.2 da 0.30000000000000004.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorProduto;

    /** Exigido pelo JPA. Fora dele, use {@link #registrar}. */
    protected Venda() {
    }

    private Venda(Long idProduto, Integer quantidade, BigDecimal valorProduto) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.valorProduto = valorProduto;
    }

    /** O preco vem do produtos-service no momento da venda, nunca do cliente HTTP. */
    public static Venda registrar(Long idProduto, int quantidade, BigDecimal valorProduto) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("quantidade deve ser positiva");
        }
        return new Venda(Objects.requireNonNull(idProduto, "idProduto e obrigatorio"), quantidade,
                Objects.requireNonNull(valorProduto, "valorProduto e obrigatorio"));
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorProduto() {
        return valorProduto;
    }
}
