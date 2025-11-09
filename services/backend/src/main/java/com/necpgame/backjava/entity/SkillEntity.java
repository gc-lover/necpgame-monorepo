package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * SkillEntity - СЃРїСЂР°РІРѕС‡РЅРёРє РЅР°РІС‹РєРѕРІ РІ РёРіСЂРµ.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml (Skill schema)
 */
@Entity
@Table(name = "skills", indexes = {
    @Index(name = "idx_skills_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "category", length = 50)
    private String category; // combat, technical, social, stealth

    @Column(name = "max_level", nullable = false)
    private Integer maxLevel = 20;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

