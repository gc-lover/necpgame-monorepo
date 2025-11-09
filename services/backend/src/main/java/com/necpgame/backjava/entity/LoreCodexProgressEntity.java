package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lore_codex_progress", indexes = {
        @Index(name = "idx_lore_codex_progress_character", columnList = "character_id"),
        @Index(name = "idx_lore_codex_progress_entry", columnList = "entry_id"),
        @Index(name = "uq_lore_codex_progress_character_entry", columnList = "character_id,entry_id", unique = true)
})
public class LoreCodexProgressEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", nullable = false)
    private LoreCodexEntryEntity entry;

    @Column(name = "is_unlocked", nullable = false)
    private boolean unlocked;

    @Column(name = "unlocked_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime unlockedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;
}


