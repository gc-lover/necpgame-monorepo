package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.KnowledgeDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocumentEntity, UUID> {
    Optional<KnowledgeDocumentEntity> findByCode(String code);
    List<KnowledgeDocumentEntity> findByCategoryOrderByCode(String category);
    List<KnowledgeDocumentEntity> findByDocumentTypeOrderByCode(String documentType);
    List<KnowledgeDocumentEntity> findAllByOrderByCodeAsc();
    List<KnowledgeDocumentEntity> findByCodeStartingWithIgnoreCaseOrderByCode(String prefix);
}

