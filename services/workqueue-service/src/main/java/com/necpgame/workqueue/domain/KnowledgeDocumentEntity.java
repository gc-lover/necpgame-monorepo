package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "knowledge_documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeDocumentEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 512, unique = true)
    private String code;

    @Column(name = "source_path", nullable = false, length = 1024)
    private String sourcePath;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(name = "document_type", nullable = false, length = 64)
    private String documentType;

    @Column(nullable = false, length = 16)
    private String format;

    @Column(length = 256)
    private String title;

    @Column(length = 64)
    private String checksum;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSONB", nullable = false)
    private List<String> tags;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

