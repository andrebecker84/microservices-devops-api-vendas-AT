package com.exemplo.produtosservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProdutosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdutosServiceApplication.class, args);
    }

}
