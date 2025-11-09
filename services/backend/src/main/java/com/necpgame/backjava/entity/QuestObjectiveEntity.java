package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * QuestObjectiveEntity - С†РµР»Рё РєРІРµСЃС‚Р°.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ С†РµР»СЏС… (objectives) РєРІРµСЃС‚РѕРІ.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/quests/quests.yaml (QuestObjective schema)
 */
@Entity
@Table(name = "quest_objectives", indexes = {
    @Index(name = "idx_quest_objectives_quest_id", columnList = "quest_id"),
    @Index(name = "idx_quest_objectives_type", columnList = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestObjectiveEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "quest_id", nullable = false, length = 100)
    private String questId;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ObjectiveType type;

    @Column(name = "target_progress", nullable = false)
    private Integer targetProgress = 1;

    @Column(name = "optional", nullable = false)
    private Boolean optional = false;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", referencedColumnName = "id", insertable = false, updatable = false)
    private QuestEntity quest;

    /**
     * РўРёРї С†РµР»Рё РєРІРµСЃС‚Р° (РёР· OpenAPI)
     */
    public enum ObjectiveType {
        LOCATION,   // Р”РѕСЃС‚РёС‡СЊ Р»РѕРєР°С†РёРё
        KILL,       // РЈР±РёС‚СЊ РІСЂР°РіРѕРІ
        COLLECT,    // РЎРѕР±СЂР°С‚СЊ РїСЂРµРґРјРµС‚С‹
        TALK,       // РџРѕРіРѕРІРѕСЂРёС‚СЊ СЃ NPC
        INTERACT    // Р’Р·Р°РёРјРѕРґРµР№СЃС‚РІРѕРІР°С‚СЊ СЃ РѕР±СЉРµРєС‚РѕРј
    }
}

