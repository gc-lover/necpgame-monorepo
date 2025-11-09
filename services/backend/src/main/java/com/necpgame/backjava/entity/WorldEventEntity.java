package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.WorldEventSeverity;
import com.necpgame.backjava.entity.enums.WorldEventType;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "world_events", indexes = {
        @Index(name = "idx_world_events_active", columnList = "is_active"),
        @Index(name = "idx_world_events_era", columnList = "era"),
        @Index(name = "idx_world_events_type", columnList = "event_type")
})
public class WorldEventEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 32)
    private WorldEventType type;

    @Column(name = "era", nullable = false, length = 32)
    private String era;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 32)
    private WorldEventSeverity severity;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "affected_regions_json", columnDefinition = "jsonb")
    private String affectedRegionsJson;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "lore_background", columnDefinition = "text")
    private String loreBackground;

    @Column(name = "quest_hooks_json", columnDefinition = "jsonb")
    private String questHooksJson;

    @Column(name = "faction_involvement_json", columnDefinition = "jsonb")
    private String factionInvolvementJson;
}

