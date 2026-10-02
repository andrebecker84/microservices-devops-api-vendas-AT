package com.exemplo.fornecedoresservice.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testa o Feign de verdade, sem mock da interface: o WireMock sobe um servidor HTTP no papel do
 * produtos-service, e o nome "produtos-service" e resolvido pelo Spring Cloud LoadBalancer, como
 * em producao. So o Eureka e trocado pela lista fixa do SimpleDiscoveryClient.
 */
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.cloud.discovery.client.simple.instances.produtos-service[0].uri=${wiremock.server.baseUrl}",
        // Timeout curto para o teste do servico lento nao esperar os 5 s do config-repo.
        "spring.cloud.openfeign.client.config.produtos-service.read-timeout=500"
})
@AutoConfigureMockMvc
@EnableWireMock
class ProdutoClientFeignTest {

    @InjectWireMock
    private WireMockServer produtosService;

    @Autowired
    private MockMvc mvc;

    @Test
    void buscaOsProdutosNoProdutosServicePeloNomeRegistrado() throws Exception {
        produtosService.stubFor(get("/produtos").willReturn(okJson("""
                [
                  {"id": 1, "nome": "Notebook", "preco": 3500.00},
                  {"id": 2, "nome": "Mouse sem fio", "preco": 79.90}
                ]
                """)));

        consultarProdutos()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Notebook"))
                .andExpect(jsonPath("$[1].preco").value(79.90));

        produtosService.verify(1, getRequestedFor(urlEqualTo("/produtos")));
    }

    @Test
    void erroNoProdutosServiceViraServicoIndisponivel() throws Exception {
        produtosService.stubFor(get("/produtos").willReturn(aResponse().withStatus(500)));

        consultarProdutos()
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.detail").value("produtos-service indisponivel"));
    }

    @Test
    void produtosServiceLentoEstouraOTimeoutEmVezDeTravarARequisicao() throws Exception {
        produtosService.stubFor(get("/produtos").willReturn(okJson("[]").withFixedDelay(2_000)));

        consultarProdutos()
                .andExpect(status().isServiceUnavailable());
    }

    // O get(...) do WireMock ja ocupa o nome; aqui fica explicito que e a requisicao ao fornecedores-service.
    private ResultActions consultarProdutos() throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/fornecedores/produtos"));
    }
}
