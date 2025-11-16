package com.necpgame.workqueue.domain.world;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "world_event_data")
@Getter
@Setter
@NoArgsConstructor
public class WorldEventEntity {
    @Id
    @Column(name = "content_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "content_entity_id")
    private ContentEntryEntity entity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_value_id")
    private EnumValueEntity eventType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_value_id")
    private EnumValueEntity region;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_entity_id")
    private ContentEntryEntity location;

    @Column(name = "difficulty_tier")
    private Integer difficultyTier;

    @Column(name = "recurrence_pattern", columnDefinition = "JSONB")
    private String recurrencePatternJson;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_entity_id")
    private ContentEntryEntity rewardEntity;

    @Column(name = "reward_description", columnDefinition = "TEXT")
    private String rewardDescription;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}

