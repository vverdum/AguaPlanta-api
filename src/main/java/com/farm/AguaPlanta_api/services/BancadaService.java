package com.farm.AguaPlanta_api.services;

import com.farm.AguaPlanta_api.models.Bancada;
import com.farm.AguaPlanta_api.repository.BancadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class BancadaService {
    private static final String BASE_DIR = "uploads/";
    private static final int MAX_BANCADAS = 6;

    @Autowired
    private BancadaRepository bancadaRepository;

    public Bancada cadastrarBancada(Bancada bancada) {
        return bancadaRepository.save(bancada);
    }

    public boolean excluirBancada(Long id) {
        Optional<Bancada> bancada = bancadaRepository.findById(id);
        if (bancada.isPresent()) {
            bancadaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Bancada> listarTodas() {
        return bancadaRepository.findAll();
    }

    public int obterProximaBancadaDisponivel() {
        List<Bancada> bancadas = listarTodas();
        return IntStream.rangeClosed(1, MAX_BANCADAS)
                .filter(i -> bancadas.stream().noneMatch(b -> b.getId().equals((long) i)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todas as bancadas estão ocupadas."));
    }

    public void salvarImagem(Long bancadaId, MultipartFile file) throws IOException {
        Path dirPath = Paths.get("uploads/bancada_" + bancadaId);
        Files.createDirectories(dirPath); // Cria a pasta se não existir

        String fileName = "imagem_" + System.currentTimeMillis() + getFileExtension(file.getOriginalFilename());
        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("✅ Imagem salva em: " + filePath.toString());
    }


    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex);
    }


    public String obterUltimaImagem(Long id) {
        if (id < 1 || id > MAX_BANCADAS) {
            throw new IllegalArgumentException("Bancada inválida. Deve ser entre 1 e 6.");
        }

        Path bancadaDir = Paths.get(BASE_DIR + "bancada_" + id);

        if (!Files.exists(bancadaDir) || !Files.isDirectory(bancadaDir)) {
            return null; // Nenhuma imagem encontrada
        }

        try {
            return Files.list(bancadaDir)
                    .filter(Files::isRegularFile)
                    .sorted((p1, p2) -> {
                        try {
                            return Files.getLastModifiedTime(p2).compareTo(Files.getLastModifiedTime(p1)); // Ordem decrescente
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .map(Path::toString)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    public void uploadImagem(Long id, MultipartFile file) {
    }
}

