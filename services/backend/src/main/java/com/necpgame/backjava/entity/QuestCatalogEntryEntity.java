package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quest_catalog_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestCatalogEntryEntity {

    @Id
    @Column(name = "quest_id", nullable = false, length = 150)
    private String questId;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "type", length = 40)
    private String type;

    @Column(name = "period", length = 40)
    private String period;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "level_requirement")
    private Integer levelRequirement;

    @Column(name = "level_cap")
    private Integer levelCap;

    @Column(name = "faction", length = 120)
    private String faction;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "tags_json", columnDefinition = "jsonb")
    private String tagsJson;

    @Column(name = "rewards_summary_json", columnDefinition = "jsonb")
    private String rewardsSummaryJson;

    @Column(name = "completion_rate", precision = 6, scale = 4)
    private BigDecimal completionRate;

    @Column(name = "average_rating", precision = 4, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "full_description")
    private String fullDescription;

    @Column(name = "storyline", length = 200)
    private String storyline;

    @Column(name = "objectives_json", columnDefinition = "jsonb")
    private String objectivesJson;

    @Column(name = "key_npcs_json", columnDefinition = "jsonb")
    private String keyNpcsJson;

    @Column(name = "locations_json", columnDefinition = "jsonb")
    private String locationsJson;

    @Column(name = "prerequisites_json", columnDefinition = "jsonb")
    private String prerequisitesJson;

    @Column(name = "unlocks_json", columnDefinition = "jsonb")
    private String unlocksJson;

    @Column(name = "branches_count")
    private Integer branchesCount;

    @Column(name = "endings_count")
    private Integer endingsCount;

    @Column(name = "has_dialogue_tree")
    private Boolean hasDialogueTree;

    @Column(name = "has_skill_checks")
    private Boolean hasSkillChecks;

    @Column(name = "has_combat")
    private Boolean hasCombat;

    @Column(name = "has_romance")
    private Boolean hasRomance;

    @Column(name = "rewards_detailed_json", columnDefinition = "jsonb")
    private String rewardsDetailedJson;

    @Column(name = "dialogue_tree_json", columnDefinition = "jsonb")
    private String dialogueTreeJson;

    @Column(name = "loot_table_json", columnDefinition = "jsonb")
    private String lootTableJson;

    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private String metadataJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}


