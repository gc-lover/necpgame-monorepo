package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * NPCEntity - NPC РїРµСЂСЃРѕРЅР°Р¶ РІ РёРіСЂРµ.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ NPC (С‚РѕСЂРіРѕРІС†С‹, РєРІРµСЃС‚РѕРґР°С‚РµР»Рё, РѕР±С‹С‡РЅС‹Рµ Р¶РёС‚РµР»Рё).
 */
@Entity
@Table(name = "npcs", indexes = {
    @Index(name = "idx_npcs_location_id", columnList = "location_id"),
    @Index(name = "idx_npcs_type", columnList = "type"),
    @Index(name = "idx_npcs_faction", columnList = "faction")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NPCEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NPCType type;

    @Column(name = "faction", length = 100)
    private String faction;

    @Column(name = "greeting", nullable = false, length = 500)
    private String greeting;

    @Column(name = "location_id", nullable = false, length = 100)
    private String locationId;

    @Column(name = "available_quests", length = 1000)
    private String availableQuests; // JSON array of quest IDs

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * РўРёРї NPC
     */
    public enum NPCType {
        TRADER,
        QUEST_GIVER,
        CITIZEN,
        ENEMY
    }
}

