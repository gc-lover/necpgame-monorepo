package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "lore_universe")
public class LoreUniverseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "version", nullable = false, length = 32)
    private String version;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "setting", columnDefinition = "TEXT")
    private String setting;

    @Column(name = "time_period", length = 64)
    private String timePeriod;

    @Column(name = "key_events_json", columnDefinition = "JSONB")
    private String keyEventsJson;

    @Column(name = "simulation_lore_json", columnDefinition = "JSONB")
    private String simulationLoreJson;

    @Column(name = "major_factions_count")
    private Integer majorFactionsCount;

    @Column(name = "locations_count")
    private Integer locationsCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
}


