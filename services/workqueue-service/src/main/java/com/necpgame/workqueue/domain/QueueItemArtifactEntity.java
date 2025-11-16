package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "queue_item_artifacts")
public class QueueItemArtifactEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private QueueItemEntity item;

    @Enumerated(EnumType.STRING)
    @Column(name = "artifact_type", nullable = false, length = 16)
    private ArtifactType artifactType;

    @Column(length = 256)
    private String title;

    @Column(length = 1024)
    private String url;

    @Column(name = "storage_path", length = 512)
    private String storagePath;

    @Column(name = "media_type", length = 128)
    private String mediaType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public enum ArtifactType {
        FILE,
        LINK
    }
}

