package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ character_appearances - РІРЅРµС€РЅРѕСЃС‚СЊ РїРµСЂСЃРѕРЅР°Р¶РµР№
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ CharacterAppearance DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "character_appearances")
@NoArgsConstructor
@AllArgsConstructor
public class CharacterAppearanceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "height", nullable = false)
    private Integer height; // 150-220 cm
    
    @Column(name = "body_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    
    @Column(name = "hair_color", nullable = false, length = 100)
    private String hairColor;

    @Column(name = "hair_style", length = 100)
    private String hairStyle;
    
    @Column(name = "eye_color", nullable = false, length = 100)
    private String eyeColor;
    
    @Column(name = "skin_color", nullable = false, length = 100)
    private String skinColor;
    
    @Column(name = "distinctive_features", length = 500)
    private String distinctiveFeatures;

    @Column(name = "tattoos_json")
    private String tattoosJson;

    @Column(name = "scars_json")
    private String scarsJson;

    @Column(name = "implants_visible_json")
    private String implantsVisibleJson;

    @Column(name = "makeup_preset", length = 100)
    private String makeupPreset;

    @Column(name = "seed", length = 32)
    private String seed;
    
    // Enum для типа телосложения
    public enum BodyType {
        thin,
        normal,
        muscular,
        large,
        slim,
        athletic,
        heavy,
        cybernetic
    }
}

