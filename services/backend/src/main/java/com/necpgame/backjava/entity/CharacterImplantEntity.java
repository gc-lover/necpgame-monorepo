package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity РґР»СЏ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹С… РёРјРїР»Р°РЅС‚РѕРІ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: character_implants
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "character_implants",
    indexes = {
        @Index(name = "idx_character_implants_character", columnList = "character_id"),
        @Index(name = "idx_character_implants_implant", columnList = "implant_id"),
        @Index(name = "idx_character_implants_active", columnList = "is_active")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_character_implant", columnNames = {"character_id", "implant_id"})
    }
)
public class CharacterImplantEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false, foreignKey = @ForeignKey(name = "fk_character_implants_character"))
    private CharacterEntity character;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "implant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_character_implants_implant"))
    private ImplantEntity implant;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * РљР°С‡РµСЃС‚РІРѕ СѓСЃС‚Р°РЅРѕРІРєРё (РІР»РёСЏРµС‚ РЅР° С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєРё): poor, normal, excellent
     */
    @Column(name = "quality", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private InstallQuality quality = InstallQuality.normal;
    
    @CreationTimestamp
    @Column(name = "installed_at", nullable = false, updatable = false)
    private LocalDateTime installedAt;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
    
    public enum InstallQuality {
        poor, normal, excellent
    }
}

