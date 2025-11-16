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
@Table(name = "quest_world_effects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestWorldEffectEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quest_entity_id", nullable = false)
    private ContentEntryEntity questEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "effect_type_value_id", nullable = false)
    private EnumValueEntity effectType;

    @Column(name = "target_entity_id")
    private UUID targetEntityId;

    @Column(name = "payload", columnDefinition = "JSONB", nullable = false)
    private String payloadJson;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}


