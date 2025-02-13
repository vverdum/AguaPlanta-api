package com.farm.AguaPlanta_api.controllers;

import com.farm.AguaPlanta_api.models.Bancada;
import com.farm.AguaPlanta_api.services.BancadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> adicionarBancada(@RequestBody Bancada bancada) {
        List<Bancada> bancadas = bancadaService.listarTodas();
        if (bancadas.size() >= 6) {
            return ResponseEntity.badRequest().body("Número máximo de bancadas atingido (6).");
        }
        int numeroBancada = bancadaService.obterProximaBancadaDisponivel();
        bancada.setId((long) numeroBancada);
        bancadaService.cadastrarBancada(bancada);

        return ResponseEntity.ok("Bancada " + numeroBancada + " adicionada com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirBancada(@PathVariable Long id) {
        boolean removida = bancadaService.excluirBancada(id);
        if (removida) {
            return ResponseEntity.ok("Bancada removida com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/ultima-imagem")
    public ResponseEntity<String> obterUltimaImagem(@PathVariable Long id) {
        String imagem = bancadaService.obterUltimaImagem(id);
        if (imagem != null) {
            return ResponseEntity.ok(imagem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{id}/imagem")
    public ResponseEntity<String> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            bancadaService.uploadImagem(id, file);
            return ResponseEntity.ok("Imagem salva com sucesso para a bancada " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem: " + e.getMessage());
        }
    }


}
