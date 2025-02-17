package com.farm.AguaPlanta_api.controllers;

import com.farm.AguaPlanta_api.models.Bancada;
import com.farm.AguaPlanta_api.models.Imagem;
import com.farm.AguaPlanta_api.repository.ImagemRepository;
import com.farm.AguaPlanta_api.services.BancadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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


        @Autowired
        private ImagemRepository imagemRepository;

        @PostMapping("/{id}/imagem")
        public ResponseEntity<String> uploadImagem(@PathVariable int id, @RequestParam("file") MultipartFile file) {
            try {
                Imagem imagem = new Imagem();
                imagem.setBancadaId(id);
                imagem.setNomeArquivo(file.getOriginalFilename());
                imagem.setDados(file.getBytes());

                imagemRepository.save(imagem);

                return ResponseEntity.ok("Imagem salva no banco de dados!");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem: " + e.getMessage());
            }
        }


    @GetMapping("/bancada/{id}")
    public ResponseEntity<Resource> getUltimaImagem(@PathVariable Long id) throws IOException {
        String ultimaImagem = bancadaService.obterUltimaImagem(id);

        if (ultimaImagem != null) {
            Path imagePath = Paths.get(ultimaImagem);
            Resource resource = new UrlResource(imagePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
