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
 * CharacterLocationEntity - С‚РµРєСѓС‰Р°СЏ Р»РѕРєР°С†РёСЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ С‚РµРєСѓС‰РµРј РјРµСЃС‚РѕРїРѕР»РѕР¶РµРЅРёРё РїРµСЂСЃРѕРЅР°Р¶Р° РІ РёРіСЂРѕРІРѕРј РјРёСЂРµ.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/locations/locations.yaml
 */
@Entity
@Table(name = "character_locations", indexes = {
    @Index(name = "idx_character_locations_character", columnList = "character_id", unique = true),
    @Index(name = "idx_character_locations_location", columnList = "current_location_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false, unique = true)
    private UUID characterId;

    @Column(name = "current_location_id", nullable = false, length = 100)
    private String currentLocationId;

    @Column(name = "previous_location_id", length = 100)
    private String previousLocationId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_location_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GameLocationEntity currentLocation;
}

