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
 * CharacterSkillEntity - РЅР°РІС‹РєРё РїРµСЂСЃРѕРЅР°Р¶Р° СЃ РїСЂРѕРіСЂРµСЃСЃРѕРј.
 * 
 * РћС‚СЃР»РµР¶РёРІР°РµС‚ СѓСЂРѕРІРµРЅСЊ Рё РѕРїС‹С‚ РЅР°РІС‹РєРѕРІ РґР»СЏ РєР°Р¶РґРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml (Skill schema)
 */
@Entity
@Table(name = "character_skills", indexes = {
    @Index(name = "idx_character_skills_character", columnList = "character_id"),
    @Index(name = "idx_character_skills_skill", columnList = "skill_id"),
    @Index(name = "idx_character_skills_character_skill", columnList = "character_id, skill_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "skill_id", nullable = false, length = 100)
    private String skillId;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SkillEntity skill;
}

