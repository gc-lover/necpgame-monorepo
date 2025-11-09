package com.necpgame.backjava.entity;

import com.necpgame.backjava.model.ScenarioInstanceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(
    name = "npc_scenario_instances",
    indexes = {
        @Index(name = "idx_npc_scenario_instances_blueprint", columnList = "blueprint_id"),
        @Index(name = "idx_npc_scenario_instances_status", columnList = "status"),
        @Index(name = "idx_npc_scenario_instances_npc", columnList = "npc_id")
    }
)
public class NpcScenarioInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "blueprint_id", nullable = false)
    private UUID blueprintId;

    @Column(name = "npc_id", nullable = false)
    private UUID npcId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ScenarioInstanceStatus status = ScenarioInstanceStatus.PENDING;

    @Column(name = "current_step")
    private Integer currentStep;

    @Column(name = "parameters_json", columnDefinition = "TEXT")
    private String parametersJson;

    @Column(name = "kpi_json", columnDefinition = "TEXT")
    private String kpiJson;

    @Column(name = "result_json", columnDefinition = "TEXT")
    private String resultJson;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration", precision = 19, scale = 4)
    private BigDecimal duration;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "automation_rule_id", length = 255)
    private String automationRuleId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NpcScenarioBlueprintEntity blueprint;
}


