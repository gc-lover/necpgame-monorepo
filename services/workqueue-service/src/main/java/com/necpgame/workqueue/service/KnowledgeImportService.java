package com.necpgame.workqueue.service;

import com.necpgame.workqueue.config.KnowledgeImportProperties;
import com.necpgame.workqueue.web.dto.knowledge.KnowledgeDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@Slf4j
public class KnowledgeImportService {
    private static final HexFormat HEX = HexFormat.of();
    private static final List<Charset> CHARSETS = List.of(
            StandardCharsets.UTF_8,
            Charset.forName("windows-1251"),
            StandardCharsets.ISO_8859_1
    );
    private final KnowledgeCatalogService catalogService;
    private final KnowledgeImportProperties properties;
    private final String repoRoot;

    public KnowledgeImportService(KnowledgeCatalogService catalogService,
                                  KnowledgeImportProperties properties,
                                  @Value("${workqueue.repo-root:..}") String repoRoot) {
        this.catalogService = catalogService;
        this.properties = properties;
        this.repoRoot = repoRoot;
    }

    public void importAll() {
        Path base = Paths.get(repoRoot == null ? ".." : repoRoot)
                .resolve(properties.rootPath())
                .normalize();
        if (!Files.exists(base)) {
            log.warn("Knowledge root {} not found, skipping import", base);
            return;
        }
        AtomicInteger processed = new AtomicInteger();
        try (Stream<Path> stream = Files.walk(base)) {
            stream.filter(Files::isRegularFile)
                    .filter(this::isSupported)
                    .forEach(path -> {
                        try {
                            importFile(base, path);
                            processed.incrementAndGet();
                        } catch (IOException e) {
                            log.warn("Failed to import knowledge file {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Failed to walk knowledge root {}", base, e);
        }
        log.info("Knowledge import completed: {} documents processed", processed.get());
    }

    private boolean isSupported(Path path) {
        String filename = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return filename.endsWith(".yaml") || filename.endsWith(".yml") || filename.endsWith(".md");
    }

    private void importFile(Path base, Path file) throws IOException {
        Path relative = base.relativize(file);
        String body = readBody(file);
        KnowledgeDocumentDto dto = new KnowledgeDocumentDto(
                null,
                buildCode(relative),
                normalizeSeparators(relative.toString()),
                detectCategory(relative),
                detectDocumentType(relative),
                detectFormat(relative),
                buildTitle(relative),
                checksum(body),
                body,
                buildTags(relative),
                OffsetDateTime.now()
        );
        catalogService.upsert(dto);
    }

    private String buildCode(Path relative) {
        String normalized = normalizeSeparators(relative.toString());
        int idx = normalized.lastIndexOf('.');
        String withoutExt = idx > 0 ? normalized.substring(0, idx) : normalized;
        return withoutExt.replace('/', '.');
    }

    private String detectCategory(Path relative) {
        if (relative.getNameCount() > 0) {
            return relative.getName(0).toString();
        }
        return properties.categoryDefault();
    }

    private String detectDocumentType(Path relative) {
        if (relative.getNameCount() > 1) {
            return relative.getName(1).toString();
        }
        return properties.documentTypeDefault();
    }

    private String detectFormat(Path relative) {
        String name = relative.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".md")) {
            return "md";
        }
        if (name.endsWith(".yml")) {
            return "yml";
        }
        return "yaml";
    }

    private String buildTitle(Path relative) {
        String filename = relative.getFileName().toString();
        int idx = filename.lastIndexOf('.');
        String base = idx > 0 ? filename.substring(0, idx) : filename;
        String spaced = base.replace('_', ' ').replace('-', ' ');
        return spaced.substring(0, 1).toUpperCase(Locale.ROOT) + spaced.substring(1);
    }

    private List<String> buildTags(Path relative) {
        int nameCount = relative.getNameCount();
        if (nameCount <= 1) {
            return List.of();
        }
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < nameCount - 1; i++) {
            tags.add(normalizeTagSegment(relative.getName(i).toString()));
        }
        return tags;
    }

    private String normalizeTagSegment(String value) {
        return value.replace('_', '-');
    }

    private String normalizeSeparators(String path) {
        return path.replace('\\', '/');
    }

    private String checksum(String body) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(body.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String readBody(Path file) throws IOException {
        IOException last = null;
        for (Charset charset : CHARSETS) {
            try {
                return Files.readString(file, charset);
            } catch (MalformedInputException ex) {
                last = ex;
                log.warn("Failed to read {} with charset {}, trying fallback", file, charset.name());
            }
        }
        if (last != null) {
            throw last;
        }
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}

