package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LoreFactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lore_factions", indexes = {
        @Index(name = "idx_lore_factions_name", columnList = "name"),
        @Index(name = "idx_lore_factions_type", columnList = "faction_type"),
        @Index(name = "idx_lore_factions_region", columnList = "region")
})
public class LoreFactionEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "external_id", nullable = false, unique = true, length = 64)
    private String externalId;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "faction_type", nullable = false, length = 32)
    private LoreFactionType type;

    @Column(name = "region", length = 64)
    private String region;

    @Column(name = "power_level")
    private Integer powerLevel;

    @Column(name = "description_short", columnDefinition = "TEXT")
    private String descriptionShort;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Column(name = "history", columnDefinition = "TEXT")
    private String history;

    @Column(name = "goals_json", columnDefinition = "JSONB")
    private String goalsJson;

    @Column(name = "leadership_json", columnDefinition = "JSONB")
    private String leadershipJson;

    @Column(name = "territories_json", columnDefinition = "JSONB")
    private String territoriesJson;

    @Column(name = "allies_json", columnDefinition = "JSONB")
    private String alliesJson;

    @Column(name = "enemies_json", columnDefinition = "JSONB")
    private String enemiesJson;

    @Column(name = "resources_json", columnDefinition = "JSONB")
    private String resourcesJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
}

