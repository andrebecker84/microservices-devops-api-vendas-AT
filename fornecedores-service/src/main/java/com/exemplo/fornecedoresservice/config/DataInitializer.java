package com.exemplo.fornecedoresservice.config;

import com.exemplo.fornecedoresservice.model.Cnpj;
import com.exemplo.fornecedoresservice.model.Fornecedor;
import com.exemplo.fornecedoresservice.repository.FornecedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Popula o banco H2 em memoria com cinco fornecedores assim que a aplicacao sobe.
 * Os CNPJs sao ficticios, mas com digitos verificadores validos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final FornecedorRepository fornecedorRepository;

    public DataInitializer(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public void run(String... args) {
        if (fornecedorRepository.count() > 0) {
            return;
        }
        fornecedorRepository.saveAll(List.of(
                Fornecedor.novo("Alfa Distribuidora de Informatica Ltda", Cnpj.of("11.222.333/0001-81")),
                Fornecedor.novo("Beta Perifericos e Acessorios Ltda", Cnpj.of("24.567.890/0001-86")),
                Fornecedor.novo("Gama Monitores e Video S.A.", Cnpj.of("31.827.364/0001-73")),
                Fornecedor.novo("Delta Moveis para Escritorio Ltda", Cnpj.of("42.738.495/0001-09")),
                Fornecedor.novo("Epsilon Eletronicos Importadora Ltda", Cnpj.of("53.948.506/0001-93"))));
    }
}
