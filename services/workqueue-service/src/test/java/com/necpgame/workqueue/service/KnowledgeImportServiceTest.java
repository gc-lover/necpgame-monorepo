package com.necpgame.workqueue.service;

import com.necpgame.workqueue.config.KnowledgeImportProperties;
import com.necpgame.workqueue.web.dto.knowledge.KnowledgeDocumentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnowledgeImportServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void importAllReadsYamlAndMd() throws IOException {
        Path knowledgeRoot = tempDir.resolve("knowledge");
        Files.createDirectories(knowledgeRoot.resolve("canon/lore"));
        Files.createDirectories(knowledgeRoot.resolve("mechanics"));

        Files.writeString(knowledgeRoot.resolve("canon/lore/universe.yaml"), "title: Universe\nsummary: test");
        Files.writeString(knowledgeRoot.resolve("mechanics/overview.md"), "# Mechanics");

        CapturingCatalogService catalogService = new CapturingCatalogService();
        KnowledgeImportProperties props = new KnowledgeImportProperties(true, "knowledge", "knowledge", "reference");
        KnowledgeImportService service = new KnowledgeImportService(catalogService, props, tempDir.toString());

        service.importAll();

        assertThat(catalogService.documents).hasSize(2);
        KnowledgeDocumentDto yamlDoc = catalogService.documents.stream()
                .filter(dto -> dto.code().contains("canon"))
                .findFirst()
                .orElseThrow();
        assertThat(yamlDoc.category()).isEqualTo("canon");
        assertThat(yamlDoc.documentType()).isEqualTo("lore");
        assertThat(yamlDoc.format()).isEqualTo("yaml");
        assertThat(yamlDoc.tags()).containsExactly("canon", "lore");
    }

    private static class CapturingCatalogService extends KnowledgeCatalogService {
        private final List<KnowledgeDocumentDto> documents = new ArrayList<>();

        CapturingCatalogService() {
            super(null);
        }

        @Override
        public KnowledgeDocumentDto upsert(KnowledgeDocumentDto dto) {
            documents.add(dto);
            return dto;
        }
    }
}

