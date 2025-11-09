package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faction_quests", indexes = {
    @Index(name = "idx_faction_quests_faction", columnList = "faction"),
    @Index(name = "idx_faction_quests_difficulty", columnList = "difficulty")
})
public class FactionQuestEntity {

    @Id
    @Column(name = "quest_id", length = 120, nullable = false)
    private String questId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "faction", length = 40, nullable = false)
    private Faction faction;

    @Column(name = "category", length = 120)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20)
    private Difficulty difficulty;

    @Column(name = "min_level_requirement")
    private Integer minLevelRequirement;

    @Column(name = "min_reputation_required")
    private Integer minReputationRequired;

    @Column(name = "branches_count")
    private Integer branchesCount;

    @Column(name = "endings_count")
    private Integer endingsCount;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "requirements_json", columnDefinition = "jsonb")
    private String requirementsJson;

    @Column(name = "rewards_json", columnDefinition = "jsonb")
    private String rewardsJson;

    @Column(name = "storyline", columnDefinition = "TEXT")
    private String storyline;

    @Column(name = "key_npcs_json", columnDefinition = "jsonb")
    private String keyNpcsJson;

    @Column(name = "locations_json", columnDefinition = "jsonb")
    private String locationsJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum Faction {
        NCPD,
        MAXTAC,
        ARASAKA,
        SIXTH_STREET,
        VOODOO_BOYS,
        ALDECALDOS,
        MILITECH,
        BIOTECHNICA,
        VALENTINOS,
        MAELSTROM,
        FIXERS,
        RIPPERS,
        TRAUMA_TEAM,
        NETRUNNERS,
        MEDIA,
        POLITICS;

        public static Faction fromCode(String value) {
            for (Faction faction : values()) {
                if (faction.name().equalsIgnoreCase(value)) {
                    return faction;
                }
            }
            throw new IllegalArgumentException("Unknown faction: " + value);
        }
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        VERY_HARD,
        EXTREME;

        public static Difficulty fromCode(String value) {
            for (Difficulty difficulty : values()) {
                if (difficulty.name().equalsIgnoreCase(value)) {
                    return difficulty;
                }
            }
            throw new IllegalArgumentException("Unknown difficulty: " + value);
        }
    }
}


