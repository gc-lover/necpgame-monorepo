package com.necpgame.backjava.entity.narrative;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "narrative_quest_blueprints",
    indexes = {
        @Index(name = "idx_narrative_quest_blueprints_code", columnList = "blueprint_code", unique = true),
        @Index(name = "idx_narrative_quest_blueprints_filters", columnList = "quest_type, difficulty, region")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NarrativeQuestBlueprintEntity implements NarrativeToolsWeightedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "blueprint_code", nullable = false, length = 120)
    private String blueprintCode;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "quest_type", nullable = false, length = 120)
    private String questType;

    @Column(name = "difficulty", length = 80)
    private String difficulty;

    @Column(name = "region", length = 120)
    private String region;

    @Column(name = "summary", columnDefinition = "text", nullable = false)
    private String summary;

    @Column(name = "recommended_level")
    private Integer recommendedLevel;

    @Column(name = "expiry_days")
    private Integer expiryDays;

    @Column(name = "objectives_json", columnDefinition = "jsonb", nullable = false)
    private String objectivesJson;

    @Column(name = "rewards_json", columnDefinition = "jsonb", nullable = false)
    private String rewardsJson;

    @Column(name = "hooks_json", columnDefinition = "jsonb", nullable = false)
    private String hooksJson;

    @Column(name = "weight", nullable = false)
    private int weight;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @Override
    public int getWeight() {
        return weight;
    }
}



