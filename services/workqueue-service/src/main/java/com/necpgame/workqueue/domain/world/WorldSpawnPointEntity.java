package com.necpgame.workqueue.domain.world;

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
@Table(name = "world_spawn_points")
@Getter
@Setter
@NoArgsConstructor
public class WorldSpawnPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_entity_id")
    private ContentEntryEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_entity_id")
    private ContentEntryEntity target;

    @Column(name = "spawn_type", length = 64)
    private String spawnType;

    @Column(name = "respawn_seconds")
    private Integer respawnSeconds;

    @Column(name = "conditions", columnDefinition = "JSONB")
    private String conditionsJson;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadataJson;
}

