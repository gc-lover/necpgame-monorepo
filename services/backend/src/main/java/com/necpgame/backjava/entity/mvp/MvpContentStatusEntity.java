package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_content_status")
public class MvpContentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "mvp_content_ready", nullable = false)
    private boolean mvpContentReady;

    @Column(name = "total_quests_available", nullable = false)
    private Integer totalQuestsAvailable;

    @Column(name = "total_locations_available", nullable = false)
    private Integer totalLocationsAvailable;

    @Column(name = "total_npcs_available", nullable = false)
    private Integer totalNpcsAvailable;

    @Column(name = "quest_engine_ready", nullable = false)
    private boolean questEngineReady;

    @Column(name = "combat_ready", nullable = false)
    private boolean combatReady;

    @Column(name = "progression_ready", nullable = false)
    private boolean progressionReady;

    @Column(name = "social_ready", nullable = false)
    private boolean socialReady;

    @Column(name = "economy_ready", nullable = false)
    private boolean economyReady;
}


