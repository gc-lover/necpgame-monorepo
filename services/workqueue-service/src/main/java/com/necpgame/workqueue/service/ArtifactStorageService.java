package com.necpgame.workqueue.service;

import com.necpgame.workqueue.service.model.StoredArtifactFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ArtifactStorageService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private final Path root;

    public ArtifactStorageService(@Value("${workqueue.storage.artifacts-path:.data/artifacts}") String artifactsPath) {
        this.root = Paths.get(artifactsPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize artifacts directory: " + artifactsPath, e);
        }
    }

    public StoredArtifactFile store(UUID itemId, MultipartFile file) {
        try {
            Path itemDir = root.resolve(itemId.toString());
            Files.createDirectories(itemDir);
            String sanitized = sanitizeFilename(file.getOriginalFilename());
            String storedName = FORMATTER.format(OffsetDateTime.now()) + "-" + sanitized;
            Path destination = itemDir.resolve(storedName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            String relativePath = root.relativize(destination).toString().replace("\\", "/");
            return new StoredArtifactFile(
                    sanitized,
                    relativePath,
                    file.getContentType(),
                    file.getSize()
            );
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to store artifact", ex);
        }
    }

    private String sanitizeFilename(String original) {
        if (original == null || original.isBlank()) {
            return "artifact.bin";
        }
        return original.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

