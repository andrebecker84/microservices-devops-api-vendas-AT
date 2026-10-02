package com.example.vendas_service.controllers;

import com.example.vendas_service.dto.ProdutoDTO;
import com.example.vendas_service.interfaces.ProdutoInterface;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Aplicacao inteira com H2, sem Config Server nem Eureka. O produtos-service e substituido
 * por um mock do cliente Feign.
 */
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
@Transactional
class VendaControllerTest {

    private static final Request REQUISICAO = Request.create(Request.HttpMethod.GET,
            "http://produtos-service/produtos/2", Map.of(), null, StandardCharsets.UTF_8, null);

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProdutoInterface produtoInterface;

    @Test
    void registraVendaComOPrecoDoProdutosServiceEDevolve201() throws Exception {
        given(produtoInterface.buscarPorId(2L))
                .willReturn(new ProdutoDTO(2L, "Mouse sem fio", new BigDecimal("79.90")));

        vender("""
                {"idProduto": 2, "quantidade": 3}
                """)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/vendas/\\d+")))
                .andExpect(jsonPath("$.idProduto").value(2))
                .andExpect(jsonPath("$.quantidade").value(3))
                .andExpect(jsonPath("$.preco").value(79.90));

        mvc.perform(get("/vendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void produtoInexistenteDevolve422() throws Exception {
        given(produtoInterface.buscarPorId(99L))
                .willThrow(new FeignException.NotFound("nao encontrado", REQUISICAO, null, null));

        vender("""
                {"idProduto": 99, "quantidade": 1}
                """)
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("Produto 99 nao encontrado"));
    }

    @Test
    void produtosServiceForaDoArDevolve503() throws Exception {
        given(produtoInterface.buscarPorId(2L))
                .willThrow(new FeignException.ServiceUnavailable("fora do ar", REQUISICAO, null, null));

        vender("""
                {"idProduto": 2, "quantidade": 1}
                """)
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void quantidadeZeroOuNegativaDevolve400() throws Exception {
        vender("""
                {"idProduto": 2, "quantidade": 0}
                """)
                .andExpect(status().isBadRequest());
        vender("""
                {"idProduto": 2, "quantidade": -5}
                """)
                .andExpect(status().isBadRequest());
    }

    @Test
    void semProdutoDevolve400() throws Exception {
        vender("""
                {"quantidade": 1}
                """)
                .andExpect(status().isBadRequest());
    }

    private ResultActions vender(String json) throws Exception {
        return mvc.perform(post("/vendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }
}
