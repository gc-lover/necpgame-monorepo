package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.TechnologyCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
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
@Table(name = "technology_access", indexes = {
        @Index(name = "idx_technology_access_available", columnList = "available"),
        @Index(name = "idx_technology_access_required_era", columnList = "required_era")
})
public class TechnologyAccessEntity {

    @Id
    @Column(name = "technology_id", nullable = false)
    private UUID technologyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 32)
    private TechnologyCategory category;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "required_era", length = 32)
    private String requiredEra;

    @Column(name = "restricted_factions_json", columnDefinition = "jsonb")
    private String restrictedFactionsJson;
}

