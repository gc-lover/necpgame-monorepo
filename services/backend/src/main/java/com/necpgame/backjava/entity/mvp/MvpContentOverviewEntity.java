package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_content_overview", indexes = {
    @Index(name = "idx_mvp_content_overview_period", columnList = "period", unique = true)
})
public class MvpContentOverviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "period", nullable = false, length = 32, unique = true)
    private String period;

    @Column(name = "total_quests", nullable = false)
    private Integer totalQuests;

    @Column(name = "main_quests", nullable = false)
    private Integer mainQuests;

    @Column(name = "side_quests", nullable = false)
    private Integer sideQuests;

    @Column(name = "faction_quests", nullable = false)
    private Integer factionQuests;

    @Column(name = "total_locations", nullable = false)
    private Integer totalLocations;

    @Column(name = "total_npcs", nullable = false)
    private Integer totalNpcs;

    @Column(name = "implemented_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal implementedPercentage;

    @OneToMany(mappedBy = "overview", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MvpContentOverviewEventEntity> keyEvents = new ArrayList<>();
}
