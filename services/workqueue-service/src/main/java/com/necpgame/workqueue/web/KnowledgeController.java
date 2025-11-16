package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.KnowledgeCatalogService;
import com.necpgame.workqueue.web.dto.knowledge.KnowledgeDocumentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge/docs")
@RequiredArgsConstructor
public class KnowledgeController {
    private final KnowledgeCatalogService knowledgeCatalogService;

    @GetMapping
    public ResponseEntity<List<KnowledgeDocumentDto>> list(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "documentType", required = false) String documentType,
            @RequestParam(value = "prefix", required = false) String prefix
    ) {
        if (prefix != null && !prefix.isBlank()) {
            return ResponseEntity.ok(knowledgeCatalogService.listByPrefix(prefix));
        }
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(knowledgeCatalogService.listByCategory(category));
        }
        if (documentType != null && !documentType.isBlank()) {
            return ResponseEntity.ok(knowledgeCatalogService.listByType(documentType));
        }
        return ResponseEntity.ok(knowledgeCatalogService.listAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<KnowledgeDocumentDto> get(@PathVariable String code) {
        return ResponseEntity.ok(knowledgeCatalogService.getByCode(code));
    }
}

