package com.example.vendas_service.services;

import com.example.vendas_service.dto.ProdutoDTO;
import com.example.vendas_service.exceptions.ProdutoNaoEncontradoException;
import com.example.vendas_service.exceptions.ProdutosIndisponiveisException;
import com.example.vendas_service.interfaces.ProdutoInterface;
import com.example.vendas_service.models.Venda;
import com.example.vendas_service.repository.VendasRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VendaService {

    private static final Logger log = LoggerFactory.getLogger(VendaService.class);

    private final VendasRepository vendaRepository;
    private final ProdutoInterface produtoInterface;

    public VendaService(VendasRepository vendaRepository, ProdutoInterface produtoInterface) {
        this.vendaRepository = vendaRepository;
        this.produtoInterface = produtoInterface;
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    @Transactional
    public Venda salvarVenda(Long idProduto, int quantidade) {
        ProdutoDTO produto = buscarProduto(idProduto);
        return vendaRepository.save(Venda.registrar(produto.id(), quantidade, produto.preco()));
    }

    // O Feign nao devolve null para produto inexistente: o 404 do produtos-service chega
    // como FeignException.NotFound. Qualquer outra falha HTTP ou de rede e indisponibilidade.
    private ProdutoDTO buscarProduto(Long idProduto) {
        try {
            return produtoInterface.buscarPorId(idProduto);
        } catch (FeignException.NotFound e) {
            throw new ProdutoNaoEncontradoException(idProduto);
        } catch (FeignException e) {
            log.warn("Falha ao consultar o produtos-service: status={} {}", e.status(), e.getMessage());
            throw new ProdutosIndisponiveisException(e);
        }
    }
}
