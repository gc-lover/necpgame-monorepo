package com.necpgame.backjava.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ characters - РїРµСЂСЃРѕРЅР°Р¶Рё РёРіСЂРѕРєРѕРІ
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ Character DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "characters", indexes = {
    @Index(name = "idx_characters_account_id", columnList = "account_id"),
    @Index(name = "idx_characters_name", columnList = "name"),
    @Index(name = "idx_characters_class", columnList = "class_code"),
    @Index(name = "idx_characters_origin", columnList = "origin_code"),
    @Index(name = "idx_characters_faction_id", columnList = "faction_id"),
    @Index(name = "idx_characters_city_id", columnList = "city_id")
})
@NoArgsConstructor
@AllArgsConstructor
public class CharacterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;
    
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    
    @Column(name = "class_code", nullable = false, length = 50)
    private String classCode; // Solo, Netrunner, etc. - СЃСЃС‹Р»РєР° РЅР° CharacterClass
    
    @Column(name = "subclass_code", length = 50)
    private String subclassCode; // solo_assassin, etc. - СЃСЃС‹Р»РєР° РЅР° CharacterSubclass
    
    @Column(name = "gender", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "origin_code", nullable = false, length = 50)
    private String originCode; // street_kid, corpo, nomad - СЃСЃС‹Р»РєР° РЅР° CharacterOrigin
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faction_id")
    private FactionEntity faction;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "appearance_id", nullable = false)
    private CharacterAppearanceEntity appearance;
    
    @Column(name = "level", nullable = false)
    private Integer level = 1;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private LifecycleStatus status = LifecycleStatus.ACTIVE;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "restore_until")
    private OffsetDateTime restoreUntil;

    @Column(name = "last_active_at")
    private OffsetDateTime lastActiveAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "restore_deadline")
    private OffsetDateTime restoreDeadline;
    
    // Enum для пола
    public enum Gender {
        male,
        female,
        other
    }

    public enum LifecycleStatus {
        ACTIVE,
        IN_COMBAT,
        AFK,
        DEAD,
        DELETED
    }
}

