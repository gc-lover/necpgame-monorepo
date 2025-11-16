package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.KnowledgeDocumentEntity;
import com.necpgame.workqueue.repository.KnowledgeDocumentRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.web.dto.knowledge.KnowledgeDocumentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KnowledgeCatalogService {
    private final KnowledgeDocumentRepository repository;

    @Transactional
    public KnowledgeDocumentDto upsert(KnowledgeDocumentDto dto) {
        KnowledgeDocumentEntity entity = repository.findByCode(dto.code())
                .orElseGet(() -> KnowledgeDocumentEntity.builder()
                        .id(UUID.randomUUID())
                        .code(dto.code())
                        .createdAt(OffsetDateTime.now())
                        .build());
        entity.setSourcePath(dto.sourcePath());
        entity.setCategory(dto.category());
        entity.setDocumentType(dto.documentType());
        entity.setFormat(dto.format());
        entity.setTitle(dto.title());
        entity.setChecksum(dto.checksum());
        entity.setBody(dto.body());
        entity.setTags(dto.tags() == null ? List.of() : dto.tags());
        entity.setUpdatedAt(OffsetDateTime.now());
        return toDto(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public KnowledgeDocumentDto getByCode(String code) {
        return repository.findByCode(code)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Knowledge document not found: " + code));
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentDto> listByCategory(String category) {
        return repository.findByCategoryOrderByCode(category).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentDto> listByType(String type) {
        return repository.findByDocumentTypeOrderByCode(type).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentDto> listAll() {
        return repository.findAllByOrderByCodeAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<KnowledgeDocumentDto> listByPrefix(String prefix) {
        return repository.findByCodeStartingWithIgnoreCaseOrderByCode(prefix).stream()
                .map(this::toDto)
                .toList();
    }

    private KnowledgeDocumentDto toDto(KnowledgeDocumentEntity entity) {
        return new KnowledgeDocumentDto(
                entity.getId(),
                entity.getCode(),
                entity.getSourcePath(),
                entity.getCategory(),
                entity.getDocumentType(),
                entity.getFormat(),
                entity.getTitle(),
                entity.getChecksum(),
                entity.getBody(),
                entity.getTags(),
                entity.getUpdatedAt()
        );
    }
}

