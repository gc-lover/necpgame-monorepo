package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.EraDangerLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "world_eras", indexes = {
        @Index(name = "idx_world_eras_current", columnList = "is_current")
})
public class WorldEraEntity {

    @Id
    @Column(name = "era", nullable = false, length = 32)
    private String era;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "key_features_json", columnDefinition = "jsonb")
    private String keyFeaturesJson;

    @Column(name = "major_factions_json", columnDefinition = "jsonb")
    private String majorFactionsJson;

    @Column(name = "technology_level")
    private Integer technologyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "danger_level", length = 16)
    private EraDangerLevel dangerLevel;

    @Column(name = "active_events_count")
    private Integer activeEventsCount;

    @Column(name = "is_current", nullable = false)
    private boolean current;
}

