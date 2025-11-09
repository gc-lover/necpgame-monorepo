package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "world_era_mechanics")
public class WorldEraMechanicsEntity {

    @Id
    @Column(name = "era", nullable = false, length = 32)
    private String era;

    @MapsId
    @OneToOne
    @JoinColumn(name = "era", referencedColumnName = "era")
    private WorldEraEntity eraEntity;

    @Column(name = "base_dc")
    private Integer baseDc;

    @Column(name = "combat_modifier")
    private Integer combatModifier;

    @Column(name = "social_modifier")
    private Integer socialModifier;

    @Column(name = "hacking_modifier")
    private Integer hackingModifier;

    @Column(name = "crafting_modifier")
    private Integer craftingModifier;

    @Column(name = "example_challenges_json", columnDefinition = "jsonb")
    private String exampleChallengesJson;

    @Column(name = "economic_state_json", columnDefinition = "jsonb")
    private String economicStateJson;

    @Column(name = "technology_restrictions_json", columnDefinition = "jsonb")
    private String technologyRestrictionsJson;

    @Column(name = "social_mechanics_json", columnDefinition = "jsonb")
    private String socialMechanicsJson;
}

