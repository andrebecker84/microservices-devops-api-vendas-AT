package com.exemplo.fornecedoresservice.model;

import com.exemplo.fornecedoresservice.exception.CnpjInvalidoException;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * CNPJ como objeto de valor: so existe instancia valida, e duas grafias do mesmo numero
 * ("11.222.333/0001-81" e "11222333000181") sao o mesmo CNPJ.
 *
 * Aceita o formato alfanumerico que a Receita Federal emite desde julho de 2026: os 12
 * primeiros caracteres podem ser letras maiusculas ou digitos, e os 2 ultimos sao sempre
 * os digitos verificadores.
 */
public record Cnpj(String valor) {

    private static final Pattern FORMATO = Pattern.compile("[0-9A-Z]{12}[0-9]{2}");
    private static final Pattern MASCARA = Pattern.compile("[.\\-/\\s]");
    private static final int[] PESOS = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public Cnpj {
        if (valor == null || !FORMATO.matcher(valor).matches()
                || todosIguais(valor) || !digitosConferem(valor)) {
            throw new CnpjInvalidoException(valor);
        }
    }

    /** Aceita o CNPJ com ou sem mascara, em maiusculas ou minusculas. */
    public static Cnpj of(String informado) {
        if (informado == null) {
            throw new CnpjInvalidoException(null);
        }
        return new Cnpj(MASCARA.matcher(informado).replaceAll("").toUpperCase(Locale.ROOT));
    }

    public String formatado() {
        return valor.substring(0, 2) + "." + valor.substring(2, 5) + "." + valor.substring(5, 8)
                + "/" + valor.substring(8, 12) + "-" + valor.substring(12);
    }

    @Override
    public String toString() {
        return formatado();
    }

    private static boolean todosIguais(String valor) {
        return valor.chars().distinct().count() == 1;
    }

    private static boolean digitosConferem(String valor) {
        return digitoVerificador(valor, 12) == valor.charAt(12) - '0'
                && digitoVerificador(valor, 13) == valor.charAt(13) - '0';
    }

    // Modulo 11 da Receita: o primeiro digito usa os 12 primeiros caracteres (pesos 5..2, 9..2),
    // o segundo usa 13 (pesos 6..2, 9..2). Cada caractere vale seu codigo ASCII menos 48, o que
    // mantem 0-9 com o proprio valor e da 17 para 'A', 18 para 'B' e assim por diante.
    private static int digitoVerificador(String valor, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (valor.charAt(i) - '0') * PESOS[PESOS.length - tamanho + i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
