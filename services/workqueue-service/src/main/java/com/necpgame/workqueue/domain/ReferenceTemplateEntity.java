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

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reference_templates")
public class ReferenceTemplateEntity {
    @Id
    @Column(length = 128)
    private String code;

    @Column(length = 256)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(length = 32)
    private String type;

    @Column(name = "source_path", length = 256)
    private String sourcePath;

    @Column(length = 64)
    private String version;

    @Column(name = "content_hash", length = 128)
    private String contentHash;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

