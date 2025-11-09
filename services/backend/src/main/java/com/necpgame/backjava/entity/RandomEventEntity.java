package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * RandomEventEntity - справочник расширенных случайных событий.
 *
 * Источник: API-SWAGGER/api/v1/gameplay/world/random-events-extended/random-events.yaml
 */
@Entity
@Table(name = "random_events", indexes = {
    @Index(name = "idx_random_events_category", columnList = "category"),
    @Index(name = "idx_random_events_period", columnList = "period")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RandomEventEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 40)
    private String category;

    @Column(name = "period", length = 20)
    private String period;

    @Column(name = "base_trigger_chance", precision = 6, scale = 4)
    private Double baseTriggerChance;

    @Column(name = "possible_outcomes_count")
    private Integer possibleOutcomesCount;

    @Column(name = "location_types_json", columnDefinition = "jsonb")
    private String locationTypesJson;

    @Column(name = "trigger_conditions_json", columnDefinition = "jsonb")
    private String triggerConditionsJson;

    @Column(name = "trigger_locations_json", columnDefinition = "jsonb")
    private String triggerLocationsJson;

    @Column(name = "time_restrictions_json", columnDefinition = "jsonb")
    private String timeRestrictionsJson;

    @Column(name = "npcs_involved_json", columnDefinition = "jsonb")
    private String npcsInvolvedJson;

    @Column(name = "choices_json", columnDefinition = "jsonb")
    private String choicesJson;

    @Column(name = "outcomes_json", columnDefinition = "jsonb")
    private String outcomesJson;

    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

