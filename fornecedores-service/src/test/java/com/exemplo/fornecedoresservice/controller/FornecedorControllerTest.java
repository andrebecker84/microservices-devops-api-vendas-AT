package com.exemplo.fornecedoresservice.controller;

import com.exemplo.fornecedoresservice.client.ProdutoClient;
import com.exemplo.fornecedoresservice.dto.ProdutoDTO;
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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sobe a aplicacao inteira, com o H2 em memoria e os cinco fornecedores do DataInitializer.
 * Config Server e Eureka ficam desligados: o teste roda sem nenhum outro servico no ar,
 * inclusive no GitHub Actions. O produtos-service e substituido por um mock do cliente Feign.
 *
 * Cada teste roda numa transacao desfeita no final, entao um POST nao altera a contagem
 * vista pelo teste seguinte.
 */
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
@Transactional
class FornecedorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProdutoClient produtoClient;

    @Test
    void listaOsCincoFornecedoresCadastradosNaSubida() throws Exception {
        mvc.perform(get("/fornecedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cnpj").value("11.222.333/0001-81"));
    }

    @Test
    void buscaFornecedorPorId() throws Exception {
        mvc.perform(get("/fornecedores/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nome").value("Beta Perifericos e Acessorios Ltda"));
    }

    @Test
    void idInexistenteDevolve404() throws Exception {
        mvc.perform(get("/fornecedores/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postCriaFornecedorEDevolve201ComOObjetoCriado() throws Exception {
        cadastrar("""
                {"nome": "Zeta Componentes Ltda", "cnpj": "65.123.478/0001-54"}
                """)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("/fornecedores/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Zeta Componentes Ltda"));

        mvc.perform(get("/fornecedores"))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[5].cnpj").value("65.123.478/0001-54"));
    }

    @Test
    void postGuardaOCnpjFormatadoMesmoQuandoEnviadoSemMascara() throws Exception {
        cadastrar("""
                {"nome": "Zeta Componentes Ltda", "cnpj": "65123478000154"}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cnpj").value("65.123.478/0001-54"));
    }

    @Test
    void postIgnoraIdEnviadoNoCorpoENaoSobrescreveRegistroExistente() throws Exception {
        cadastrar("""
                {"id": 1, "nome": "Zeta Componentes Ltda", "cnpj": "65.123.478/0001-54"}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(1)));

        mvc.perform(get("/fornecedores/1"))
                .andExpect(jsonPath("$.nome").value("Alfa Distribuidora de Informatica Ltda"));
    }

    @Test
    void postComCnpjRepetidoDevolve409() throws Exception {
        cadastrar("""
                {"nome": "Outra Empresa Ltda", "cnpj": "11.222.333/0001-81"}
                """)
                .andExpect(status().isConflict());
    }

    // Sem o objeto de valor, a grafia sem mascara passaria pela constraint UNIQUE.
    @Test
    void cnpjRepetidoEmOutraGrafiaTambemDevolve409() throws Exception {
        cadastrar("""
                {"nome": "Outra Empresa Ltda", "cnpj": "11222333000181"}
                """)
                .andExpect(status().isConflict());
    }

    @Test
    void postComCnpjInvalidoDevolve400() throws Exception {
        cadastrar("""
                {"nome": "Empresa Ltda", "cnpj": "11.222.333/0001-82"}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("CNPJ invalido: 11222333000182"));
    }

    @Test
    void postSemCnpjDevolve400() throws Exception {
        cadastrar("""
                {"nome": "Sem CNPJ Ltda"}
                """)
                .andExpect(status().isBadRequest());
    }

    @Test
    void postComJsonMalformadoDevolve400() throws Exception {
        cadastrar("{\"nome\": ")
                .andExpect(status().isBadRequest());
    }

    @Test
    void listaProdutosObtidosDoProdutosServiceViaFeign() throws Exception {
        given(produtoClient.listarTodos()).willReturn(List.of(
                new ProdutoDTO(1L, "Notebook", new BigDecimal("3500.00")),
                new ProdutoDTO(2L, "Mouse sem fio", new BigDecimal("79.90"))));

        mvc.perform(get("/fornecedores/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Notebook"))
                .andExpect(jsonPath("$[1].preco").value(79.90));
    }

    @Test
    void produtosServiceForaDoArDevolve503() throws Exception {
        Request requisicao = Request.create(Request.HttpMethod.GET, "http://produtos-service/produtos",
                Map.of(), null, StandardCharsets.UTF_8, null);
        given(produtoClient.listarTodos()).willThrow(new FeignException.ServiceUnavailable(
                "Load balancer does not contain an instance for the service produtos-service",
                requisicao, null, null));

        mvc.perform(get("/fornecedores/produtos"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.detail").value("produtos-service indisponivel"));
    }

    private ResultActions cadastrar(String json) throws Exception {
        return mvc.perform(post("/fornecedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }
}
