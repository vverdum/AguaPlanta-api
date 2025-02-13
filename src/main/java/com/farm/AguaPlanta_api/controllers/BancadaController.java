package com.farm.AguaPlanta_api.controllers;

import com.farm.AguaPlanta_api.models.Bancada;
import com.farm.AguaPlanta_api.services.BancadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bancadas")

public class BancadaController {
    @Autowired
    private BancadaService bancadaService;
    @GetMapping
    public ResponseEntity<List<Bancada>> listarBancadas() {
        List<Bancada> bancadas = bancadaService.listarTodas();
        return ResponseEntity.ok(bancadas);
    }

    @PostMapping
    public ResponseEntity<Bancada> cadastrarBancada(@RequestBody Bancada bancada) {
        Bancada novaBancada = bancadaService.cadastrarBacada(bancada);
        return ResponseEntity.ok(novaBancada);
    }

    @PostMapping("/{id}/imagem")
    public ResponseEntity<String> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Bancada bancada = bancadaService.uploadImagem(id, file);
            return ResponseEntity.ok("Imagem enviada com sucesso! Caminho: " + bancada.getImagePath());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar a imagem.");
        }
    }
}
