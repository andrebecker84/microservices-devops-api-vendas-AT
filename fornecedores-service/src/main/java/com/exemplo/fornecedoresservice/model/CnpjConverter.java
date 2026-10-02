package com.exemplo.fornecedoresservice.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Grava o CNPJ sempre na forma formatada. Como toda entrada passa por {@link Cnpj#of},
 * a constraint UNIQUE da coluna passa a valer para o numero, e nao para a grafia.
 */
@Converter
public class CnpjConverter implements AttributeConverter<Cnpj, String> {

    @Override
    public String convertToDatabaseColumn(Cnpj cnpj) {
        return cnpj == null ? null : cnpj.formatado();
    }

    @Override
    public Cnpj convertToEntityAttribute(String coluna) {
        return coluna == null ? null : Cnpj.of(coluna);
    }
}
