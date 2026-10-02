package com.exemplo.fornecedoresservice.model;

import com.exemplo.fornecedoresservice.exception.CnpjInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CnpjTest {

    @Test
    void mesmaNumeracaoComESemMascaraEOMesmoCnpj() {
        assertThat(Cnpj.of("11.222.333/0001-81")).isEqualTo(Cnpj.of("11222333000181"));
    }

    @Test
    void formataNoPadraoDaReceita() {
        assertThat(Cnpj.of("11222333000181").formatado()).isEqualTo("11.222.333/0001-81");
    }

    // Exemplo publicado pela Receita Federal para o CNPJ alfanumerico.
    @Test
    void aceitaCnpjAlfanumerico() {
        assertThat(Cnpj.of("12.abc.345/01de-35").formatado()).isEqualTo("12.ABC.345/01DE-35");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "11.222.333/0001-82",   // segundo digito verificador errado
            "11.222.333/0001-91",   // primeiro digito verificador errado
            "11.222.333/0001-8",    // curto demais
            "11.222.333/0001-811",  // longo demais
            "00.000.000/0000-00",   // todos iguais: passa no modulo 11, mas nao existe
            "12.ABC.345/01DE-3A",   // digito verificador nunca e letra
            "12.AB#.345/01DE-35"    // caractere fora do alfabeto permitido
    })
    void recusaCnpjInvalido(String informado) {
        assertThatThrownBy(() -> Cnpj.of(informado)).isInstanceOf(CnpjInvalidoException.class);
    }
}
