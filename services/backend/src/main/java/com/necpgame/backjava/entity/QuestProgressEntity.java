package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * QuestProgressEntity - РїСЂРѕРіСЂРµСЃСЃ РІС‹РїРѕР»РЅРµРЅРёСЏ РєРІРµСЃС‚Р°.
 * 
 * РћС‚СЃР»РµР¶РёРІР°РµС‚ РїСЂРѕРіСЂРµСЃСЃ РІС‹РїРѕР»РЅРµРЅРёСЏ РєРІРµСЃС‚РѕРІ РґР»СЏ РєР°Р¶РґРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°.
 */
@Entity
@Table(name = "quest_progress", indexes = {
    @Index(name = "idx_quest_progress_character_id", columnList = "character_id"),
    @Index(name = "idx_quest_progress_quest_id", columnList = "quest_id"),
    @Index(name = "idx_quest_progress_status", columnList = "status"),
    @Index(name = "idx_quest_progress_character_quest", columnList = "character_id, quest_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "quest_id", nullable = false, length = 100)
    private String questId;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private QuestStatus status = QuestStatus.ACTIVE;

    @Column(name = "progress", nullable = false)
    private Integer progress = 0; // Progress percentage (0-100)

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    /**
     * РЎС‚Р°С‚СѓСЃ РєРІРµСЃС‚Р°
     */
    public enum QuestStatus {
        ACTIVE,
        COMPLETED,
        FAILED,
        ABANDONED
    }
}

