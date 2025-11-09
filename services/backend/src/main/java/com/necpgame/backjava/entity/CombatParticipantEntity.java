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
 * CombatParticipantEntity - СѓС‡Р°СЃС‚РЅРёРє Р±РѕСЏ (РёРіСЂРѕРє, РІСЂР°Рі, NPC).
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ РѕР± СѓС‡Р°СЃС‚РЅРёРєР°С… Р±РѕРµРІРѕР№ СЃРµСЃСЃРёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/combat/combat.yaml (CombatParticipant schema)
 */
@Entity
@Table(name = "combat_participants", indexes = {
    @Index(name = "idx_combat_participants_session", columnList = "combat_session_id"),
    @Index(name = "idx_combat_participants_type", columnList = "participant_type"),
    @Index(name = "idx_combat_participants_alive", columnList = "is_alive")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombatParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "combat_session_id", nullable = false)
    private UUID combatSessionId;

    @Column(name = "participant_id", nullable = false, length = 100)
    private String participantId; // character_id РёР»Рё npc_id РёР»Рё enemy_id

    @Column(name = "participant_name", nullable = false, length = 200)
    private String participantName;

    @Column(name = "participant_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ParticipantType participantType;

    @Column(name = "health", nullable = false)
    private Integer health;

    @Column(name = "max_health", nullable = false)
    private Integer maxHealth;

    @Column(name = "energy")
    private Integer energy;

    @Column(name = "armor", nullable = false)
    private Integer armor = 0;

    @Column(name = "is_alive", nullable = false)
    private Boolean isAlive = true;

    @Column(name = "initiative", nullable = false)
    private Integer initiative = 0; // Р”Р»СЏ РїРѕСЂСЏРґРєР° С…РѕРґРѕРІ

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combat_session_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CombatSessionEntity combatSession;

    /**
     * РўРёРї СѓС‡Р°СЃС‚РЅРёРєР° Р±РѕСЏ (РёР· OpenAPI - CombatParticipant.type enum)
     */
    public enum ParticipantType {
        PLAYER,
        ENEMY,
        NPC
    }
}

