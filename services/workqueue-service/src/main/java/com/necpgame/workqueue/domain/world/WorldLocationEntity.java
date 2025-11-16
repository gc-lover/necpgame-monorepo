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
@Table(name = "world_location_data")
@Getter
@Setter
@NoArgsConstructor
public class WorldLocationEntity {
    @Id
    @Column(name = "content_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "content_entity_id")
    private ContentEntryEntity entity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_value_id")
    private EnumValueEntity region;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biome_value_id")
    private EnumValueEntity biome;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_location_id")
    private ContentEntryEntity parentLocation;

    @Column(name = "danger_level")
    private Integer dangerLevel;

    @Column(name = "recommended_level_min")
    private Integer recommendedLevelMin;

    @Column(name = "recommended_level_max")
    private Integer recommendedLevelMax;

    @Column(name = "population_estimate")
    private Integer populationEstimate;

    @Column(name = "coordinates", columnDefinition = "JSONB")
    private String coordinatesJson;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}

