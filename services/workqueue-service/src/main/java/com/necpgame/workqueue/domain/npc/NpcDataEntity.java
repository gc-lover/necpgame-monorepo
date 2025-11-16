package com.necpgame.workqueue.domain.npc;

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

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "npc_data")
@Getter
@Setter
@NoArgsConstructor
public class NpcDataEntity {
    @Id
    @Column(name = "content_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "content_entity_id")
    private ContentEntryEntity entity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alignment_value_id")
    private EnumValueEntity alignment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "behavior_value_id")
    private EnumValueEntity behavior;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faction_entity_id")
    private ContentEntryEntity faction;

    @Column(name = "role_title", length = 128)
    private String roleTitle;

    @Column(name = "level")
    private Integer level;

    @Column(name = "power_score")
    private BigDecimal powerScore;

    @Column(name = "vendor_catalog", columnDefinition = "JSONB")
    private String vendorCatalogJson;

    @Column(name = "schedule_metadata", columnDefinition = "JSONB")
    private String scheduleMetadataJson;

    @Column(name = "dialogue_profile", columnDefinition = "JSONB")
    private String dialogueProfileJson;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}

