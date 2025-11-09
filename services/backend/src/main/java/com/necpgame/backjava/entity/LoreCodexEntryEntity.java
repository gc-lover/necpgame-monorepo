package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LoreCodexCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lore_codex_entries", indexes = {
        @Index(name = "idx_lore_codex_entries_category", columnList = "category")
})
public class LoreCodexEntryEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "entry_id", nullable = false, unique = true, length = 64)
    private String entryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 32)
    private LoreCodexCategory category;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "unlock_condition", columnDefinition = "TEXT")
    private String unlockCondition;

    @Column(name = "related_entries_json", columnDefinition = "JSONB")
    private String relatedEntriesJson;

    @Column(name = "default_unlocked", nullable = false)
    private boolean defaultUnlocked;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
}


