package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "skill_experience", indexes = {
    @Index(name = "idx_skill_experience_character", columnList = "character_id"),
    @Index(name = "idx_skill_experience_skill", columnList = "skill_id")
})
public class SkillExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "skill_id", nullable = false, length = 100)
    private String skillId;

    @Column(name = "current_level", nullable = false)
    private Integer currentLevel = 0;

    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "experience_to_next_level", nullable = false)
    private Integer experienceToNextLevel = 100;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SkillEntity skill;
}



