package com.example.vendas_service.controllers;

import com.example.vendas_service.dto.VendaDTO;
import com.example.vendas_service.dto.VendaRequest;
import com.example.vendas_service.services.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<VendaDTO> listarTodas() {
        return service.listarTodas().stream()
                .map(VendaDTO::de)
                .toList();
    }

    @PostMapping
    public ResponseEntity<VendaDTO> salvarVenda(@Valid @RequestBody VendaRequest request) {
        VendaDTO criada = VendaDTO.de(service.salvarVenda(request.idProduto(), request.quantidade()));
        return ResponseEntity.created(URI.create("/vendas/" + criada.id())).body(criada);
    }
}
