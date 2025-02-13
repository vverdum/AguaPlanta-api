package com.farm.AguaPlanta_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    private static final String BASE_DIR = "uploads/";

    @GetMapping("/{bancadaId}")
    public ResponseEntity<Resource> getUltimaImagem(@PathVariable int bancadaId) throws IOException {
        if (bancadaId < 1 || bancadaId > 6) {
            return ResponseEntity.badRequest().body(null);
        }

        Path bancadaDir = Paths.get(BASE_DIR, "bancada_" + bancadaId);
        if (!Files.exists(bancadaDir) || !Files.isDirectory(bancadaDir)) {
            return ResponseEntity.notFound().build();
        }

        // Obtendo a última imagem da pasta
        Optional<Path> ultimaImagem = obterUltimaImagem(bancadaDir);

        if (ultimaImagem.isPresent()) {
            Path imagePath = ultimaImagem.get();
            Resource resource = new UrlResource(imagePath.toUri());

            // Determina o tipo de arquivo dinamicamente
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Tipo padrão
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Optional<Path> obterUltimaImagem(Path bancadaDir) throws IOException {
        try (Stream<Path> files = Files.list(bancadaDir)) {
            return files.filter(Files::isRegularFile)
                    .max(Comparator.comparingLong(f -> f.toFile().lastModified())); // Última imagem pela data de modificação
        }
    }
}
