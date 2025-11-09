package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "character_progression")
public class CharacterProgressionEntity {

    @Id
    @Column(name = "character_id", nullable = false, updatable = false)
    private UUID characterId;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "experience", nullable = false)
    private Long experience = 0L;

    @Column(name = "experience_to_next_level", nullable = false)
    private Long experienceToNextLevel = 1000L;

    @Column(name = "unspent_attribute_points", nullable = false)
    private Integer unspentAttributePoints = 0;

    @Column(name = "unspent_skill_points", nullable = false)
    private Integer unspentSkillPoints = 0;

    @Column(name = "total_experience_earned", nullable = false)
    private Long totalExperienceEarned = 0L;

    @Column(name = "total_attribute_points_spent", nullable = false)
    private Integer totalAttributePointsSpent = 0;

    @Column(name = "total_skill_points_spent", nullable = false)
    private Integer totalSkillPointsSpent = 0;

    @Column(name = "level_cap", nullable = false)
    private Integer levelCap = 100;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;
}



