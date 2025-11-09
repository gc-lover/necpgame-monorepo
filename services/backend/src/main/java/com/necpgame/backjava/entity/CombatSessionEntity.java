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
 * CombatSessionEntity - СЃРµСЃСЃРёСЏ Р±РѕСЏ.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ Р±РѕРµРІРѕР№ СЃРµСЃСЃРёРё (Р°РєС‚РёРІРЅС‹Р№ Р±РѕР№).
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/combat/combat.yaml (CombatState schema)
 */
@Entity
@Table(name = "combat_sessions", indexes = {
    @Index(name = "idx_combat_sessions_character", columnList = "character_id"),
    @Index(name = "idx_combat_sessions_status", columnList = "status"),
    @Index(name = "idx_combat_sessions_location", columnList = "location_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombatSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "location_id", length = 100)
    private String locationId;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CombatStatus status = CombatStatus.ACTIVE;

    @Column(name = "current_turn", length = 100)
    private String currentTurn; // ID СѓС‡Р°СЃС‚РЅРёРєР°, С‡РµР№ С…РѕРґ

    @Column(name = "round", nullable = false)
    private Integer round = 1;

    @Column(name = "combat_log", columnDefinition = "TEXT")
    private String combatLog; // JSON array of log entries

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    /**
     * РЎС‚Р°С‚СѓСЃ Р±РѕСЏ (РёР· OpenAPI - CombatState.status enum)
     */
    public enum CombatStatus {
        ACTIVE,
        ENDED,
        FLED
    }
}

