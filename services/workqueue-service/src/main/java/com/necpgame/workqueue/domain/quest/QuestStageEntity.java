package com.necpgame.workqueue.domain.quest;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "quest_stages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestStageEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quest_entity_id", nullable = false)
    private ContentEntryEntity questEntity;

    @Column(name = "stage_index", nullable = false)
    private Integer stageIndex;

    @Column(length = 256)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_type_value_id")
    private EnumValueEntity objectiveType;

    @Column(name = "target_entity_id")
    private UUID targetEntityId;

    @Column(name = "target_location_entity_id")
    private UUID targetLocationEntityId;

    @Column(name = "is_optional", nullable = false)
    private boolean optional;

    @Column(name = "success_conditions", columnDefinition = "JSONB")
    private String successConditionsJson;

    @Column(name = "failure_conditions", columnDefinition = "JSONB")
    private String failureConditionsJson;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}


