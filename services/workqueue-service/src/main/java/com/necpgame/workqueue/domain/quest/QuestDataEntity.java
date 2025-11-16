package com.necpgame.workqueue.domain.quest;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "quest_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestDataEntity {
    @Id
    @Column(name = "content_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "content_entity_id")
    private ContentEntryEntity entity;

    @Column(name = "segment", length = 64)
    private String segment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_category_value_id")
    private EnumValueEntity category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_value_id")
    private EnumValueEntity difficulty;

    @Column(name = "level_min")
    private Integer levelMin;

    @Column(name = "level_max")
    private Integer levelMax;

    @Column(name = "estimated_duration_min")
    private Integer estimatedDurationMin;

    @Column(name = "estimated_duration_max")
    private Integer estimatedDurationMax;

    @Column(name = "repeatable", nullable = false)
    private boolean repeatable;

    @Column(name = "recommended_players")
    private Integer recommendedPlayers;

    @Column(name = "start_npc_entity_id")
    private UUID startNpcId;

    @Column(name = "end_npc_entity_id")
    private UUID endNpcId;

    @Column(name = "start_location_entity_id")
    private UUID startLocationId;

    @Column(name = "end_location_entity_id")
    private UUID endLocationId;

    @Column(name = "prerequisites", columnDefinition = "JSONB")
    private String prerequisitesJson;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}


