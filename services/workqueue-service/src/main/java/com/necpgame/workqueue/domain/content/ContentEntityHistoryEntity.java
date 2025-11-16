package com.necpgame.workqueue.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "entity_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentEntityHistoryEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private ContentEntryEntity entity;

    @Column(nullable = false, length = 32)
    private String version;

    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime changedAt;

    @Column(name = "changed_by", length = 128)
    private String changedBy;

    @Column(name = "changes_summary", columnDefinition = "TEXT")
    private String changesSummary;

    @Column(name = "diff_blob", columnDefinition = "TEXT")
    private String diffBlob;
}


