package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * QuestEntity - РєРІРµСЃС‚ РІ РёРіСЂРµ.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ РєРІРµСЃС‚Р°С… (РѕСЃРЅРѕРІРЅС‹Рµ, РїРѕР±РѕС‡РЅС‹Рµ, РєРѕРЅС‚СЂР°РєС‚С‹).
 */
@Entity
@Table(name = "quests", indexes = {
    @Index(name = "idx_quests_type", columnList = "type"),
    @Index(name = "idx_quests_level", columnList = "level"),
    @Index(name = "idx_quests_giver_npc_id", columnList = "giver_npc_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "type", length = 20)
    @Enumerated(EnumType.STRING)
    private QuestType type;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "giver_npc_id", nullable = false, length = 100)
    private String giverNpcId;

    @Column(name = "reward_experience")
    private Integer rewardExperience;

    @Column(name = "reward_money")
    private Integer rewardMoney;

    @Column(name = "reward_items", length = 1000)
    private String rewardItems; // JSON array of item IDs

    @Column(name = "reward_reputation_faction", length = 100)
    private String rewardReputationFaction;

    @Column(name = "reward_reputation_amount")
    private Integer rewardReputationAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * РўРёРї РєРІРµСЃС‚Р°
     */
    public enum QuestType {
        MAIN,
        SIDE,
        CONTRACT
    }
}

