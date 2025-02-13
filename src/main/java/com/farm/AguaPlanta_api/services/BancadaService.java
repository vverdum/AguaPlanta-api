package com.farm.AguaPlanta_api.services;

import com.farm.AguaPlanta_api.models.Bancada;
import com.farm.AguaPlanta_api.repository.BancadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class BancadaService {
    private static final String IMAGES_DIR = "uploads/"; // Pasta onde as imagens serão salvas

    @Autowired
    private BancadaRepository bancadaRepository;

    public Bancada cadastrarBacada(Bancada bancada) {
        return bancadaRepository.save(bancada);
    }

    public Bancada uploadImagem(Long bancadaId, MultipartFile file) throws IOException {
        Bancada bancada = bancadaRepository.findById(bancadaId)
                .orElseThrow(() -> new RuntimeException("Bancada não encontrada"));

        Files.createDirectories(Paths.get("uploads/"));

        String fileName = "bancada_" + bancadaId + getFileExtension(file.getOriginalFilename());
        Path filePath = Paths.get("uploads/", fileName);

        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        bancada.setImagePath(filePath.toString());
        return bancadaRepository.save(bancada);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public List<Bancada> listarTodas() {
        return bancadaRepository.findAll();
    }
}