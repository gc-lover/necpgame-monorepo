package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.TimelineEventType;
import com.necpgame.backjava.entity.enums.TimelineImpactLevel;
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
@Table(name = "lore_timeline_events", indexes = {
        @Index(name = "idx_lore_timeline_events_era", columnList = "era"),
        @Index(name = "idx_lore_timeline_events_year", columnList = "year"),
        @Index(name = "idx_lore_timeline_events_type", columnList = "event_type")
})
public class LoreTimelineEventEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true, length = 64)
    private String eventId;

    @Column(name = "era", length = 64)
    private String era;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 32)
    private TimelineEventType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "impact_level", nullable = false, length = 16)
    private TimelineImpactLevel impactLevel;

    @Column(name = "related_factions_json", columnDefinition = "JSONB")
    private String relatedFactionsJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
}


