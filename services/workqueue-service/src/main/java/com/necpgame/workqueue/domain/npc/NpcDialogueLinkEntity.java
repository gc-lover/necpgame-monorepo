package com.necpgame.workqueue.domain.npc;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "npc_dialogue_links")
@Getter
@Setter
@NoArgsConstructor
public class NpcDialogueLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_entity_id")
    private ContentEntryEntity npc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dialogue_entity_id")
    private ContentEntryEntity dialogue;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "conditions", columnDefinition = "JSONB")
    private String conditionsJson;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadataJson;
}

