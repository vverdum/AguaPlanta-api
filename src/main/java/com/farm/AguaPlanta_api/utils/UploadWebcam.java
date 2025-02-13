package com.farm.AguaPlanta_api.utils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.*;

public class UploadWebcam {
    private static final String BASE_DIRECTORY = "C:/Users/vverd/OneDrive/Imagens/Webcam/"; // Diretório base
    private static final String UPLOAD_URL = "http://localhost:8080/bancadas/{id}/imagem"; // API para upload

    public static void main(String[] args) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        // Processa imagens já existentes antes de iniciar o monitoramento
        System.out.println("Processando imagens existentes...");
        processarImagensExistentes();

        // Agora, inicia o monitoramento das pastas
        System.out.println("Iniciando monitoramento das bancadas...");
        for (int i = 1; i <= 6; i++) {
            Path path = Paths.get(BASE_DIRECTORY + i);
            if (Files.exists(path)) {
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                System.out.println("Monitorando: " + path);
            }
        }

        while (true) {
            WatchKey key;
            try {
                key = watchService.take(); // Aguarda eventos de novo arquivo
            } catch (InterruptedException e) {
                return;
            }

            // Verifica em qual pasta a imagem foi salva
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path dir = (Path) key.watchable();
                    String fileName = event.context().toString();
                    Path filePath = dir.resolve(fileName);

                    // Obtém o número da bancada com base no diretório
                    String folderName = dir.getFileName().toString();
                    try {
                        int bancadaId = Integer.parseInt(folderName); // Nome da pasta é o número da bancada
                        System.out.println("Nova imagem detectada na bancada " + bancadaId + ": " + filePath);
                        uploadFile(filePath.toString(), bancadaId);
                    } catch (NumberFormatException e) {
                        System.err.println("Pasta inválida detectada: " + folderName);
                    }
                }
            }
            key.reset();
        }
    }

    /**
     * Processa imagens já existentes antes de iniciar o monitoramento.
     */
    private static void processarImagensExistentes() {
        for (int i = 1; i <= 6; i++) {
            final int bancadaId = i; // Torna a variável final para ser usada dentro da lambda
            Path bancadaDir = Paths.get(BASE_DIRECTORY + bancadaId);

            if (Files.exists(bancadaDir) && Files.isDirectory(bancadaDir)) {
                try {
                    Files.list(bancadaDir)
                            .filter(Files::isRegularFile)
                            .forEach(filePath -> {
                                System.out.println("Encontrada imagem na bancada " + bancadaId + ": " + filePath);
                                uploadFile(filePath.toString(), bancadaId);
                            });
                } catch (IOException e) {
                    System.err.println("Erro ao processar imagens na bancada " + bancadaId + ": " + e.getMessage());
                }
            }
        }
    }


    /**
     * Realiza o upload do arquivo para a API.
     */
    private static void uploadFile(String filePath, int bancadaId) {
        RestTemplate restTemplate = new RestTemplate();
        FileSystemResource fileResource = new FileSystemResource(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String finalUrl = UPLOAD_URL.replace("{id}", String.valueOf(bancadaId)); // Substitui o ID na URL

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(finalUrl, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Upload concluído para a bancada " + bancadaId + ": " + response.getBody());
            } else {
                System.out.println("Falha no upload para a bancada " + bancadaId + ": " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar imagem para bancada " + bancadaId + ": " + e.getMessage());
        }
    }
}
