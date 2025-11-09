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
 * GameSessionEntity - РёРіСЂРѕРІР°СЏ СЃРµСЃСЃРёСЏ.
 * 
 * РћС‚СЃР»РµР¶РёРІР°РµС‚ РєР°Р¶РґС‹Р№ РІС…РѕРґ РёРіСЂРѕРєР° РІ РёРіСЂСѓ.
 */
@Entity
@Table(name = "game_sessions", indexes = {
    @Index(name = "idx_game_sessions_character_id", columnList = "character_id"),
    @Index(name = "idx_game_sessions_account_id", columnList = "account_id"),
    @Index(name = "idx_game_sessions_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "location_id", nullable = false, length = 100)
    private String locationId;

    @Column(name = "tutorial_enabled", nullable = false)
    private Boolean tutorialEnabled = false;

    @Column(name = "session_start", nullable = false)
    private LocalDateTime sessionStart;

    @Column(name = "session_end")
    private LocalDateTime sessionEnd;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;
}

