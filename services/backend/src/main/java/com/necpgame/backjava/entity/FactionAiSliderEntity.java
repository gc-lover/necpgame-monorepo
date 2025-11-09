package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faction_ai_sliders", indexes = {
        @Index(name = "idx_faction_ai_sliders_faction", columnList = "faction")
})
public class FactionAiSliderEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "faction", nullable = false)
    private String faction;

    @Column(name = "influence", precision = 10, scale = 2)
    private BigDecimal influence;

    @Column(name = "aggression", precision = 5, scale = 2)
    private BigDecimal aggression;

    @Column(name = "wealth", precision = 10, scale = 2)
    private BigDecimal wealth;

    @Column(name = "technology", precision = 5, scale = 2)
    private BigDecimal technology;

    @Column(name = "relations_json", columnDefinition = "jsonb")
    private String relationsJson;
}

