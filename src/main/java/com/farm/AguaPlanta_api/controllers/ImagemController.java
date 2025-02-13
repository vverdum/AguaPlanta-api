package com.farm.AguaPlanta_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    private static final String IMAGES_DIR = "uploads/";

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImagem(@PathVariable String fileName) throws IOException {
        Path imagePath = Paths.get(IMAGES_DIR, fileName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "jpg") // Ajustar conforme o tipo da imagem
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
