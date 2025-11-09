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
 * TutorialProgressEntity - РїСЂРѕРіСЂРµСЃСЃ С‚СѓС‚РѕСЂРёР°Р»Р°.
 * 
 * РћС‚СЃР»РµР¶РёРІР°РµС‚ РїСЂРѕРіСЂРµСЃСЃ РїСЂРѕС…РѕР¶РґРµРЅРёСЏ С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РєР°Р¶РґРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°.
 */
@Entity
@Table(name = "tutorial_progress", indexes = {
    @Index(name = "idx_tutorial_progress_character_id", columnList = "character_id"),
    @Index(name = "idx_tutorial_progress_completed", columnList = "completed")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorialProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false, unique = true)
    private UUID characterId;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 0;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps = 4;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "skipped", nullable = false)
    private Boolean skipped = false;

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
}

