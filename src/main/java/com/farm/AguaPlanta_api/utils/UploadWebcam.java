package com.farm.AguaPlanta_api.utils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.*;

public class UploadWebcam {
    private static final String DIRECTORY_TO_WATCH = "C:/Users/vverd/OneDrive/Imagens/Webcam"; // Altere para seu caminho
    private static final String UPLOAD_URL = "http://localhost:8080/bancadas/{id}/imagem"; // URL da API

    public static void main(String[] args) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(DIRECTORY_TO_WATCH);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("Monitorando diretório: " + DIRECTORY_TO_WATCH);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take(); // Espera até um arquivo novo ser criado
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    String fileName = event.context().toString();
                    Path filePath = path.resolve(fileName);

                    System.out.println("Nova imagem detectada: " + filePath);
                    uploadFile(filePath.toString());
                }
            }
            key.reset();
        }
    }

    private static void uploadFile(String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        FileSystemResource fileResource = new FileSystemResource(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(UPLOAD_URL, requestEntity, String.class);
            System.out.println("Upload concluído: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Erro ao enviar imagem: " + e.getMessage());
        }
    }
}
