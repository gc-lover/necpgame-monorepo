package com.necpgame.backjava.entity;

import com.necpgame.backjava.model.ScenarioCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(
    name = "npc_scenario_blueprints",
    indexes = {
        @Index(name = "idx_npc_scenario_blueprints_owner", columnList = "owner_id"),
        @Index(name = "idx_npc_scenario_blueprints_category", columnList = "category"),
        @Index(name = "idx_npc_scenario_blueprints_public", columnList = "is_public"),
        @Index(name = "idx_npc_scenario_blueprints_license", columnList = "license_tier")
    }
)
public class NpcScenarioBlueprintEntity {

    public enum VisibilityScope {
        PRIVATE,
        FACTION,
        MARKETPLACE
    }

    public enum LicenseTier {
        L1,
        L2,
        L3
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "version", length = 32)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 32)
    private ScenarioCategory category;

    @Column(name = "required_roles_json", columnDefinition = "TEXT", nullable = false)
    private String requiredRolesJson;

    @Column(name = "parameters_json", columnDefinition = "TEXT")
    private String parametersJson;

    @Column(name = "conditions_json", columnDefinition = "TEXT")
    private String conditionsJson;

    @Column(name = "steps_json", columnDefinition = "TEXT", nullable = false)
    private String stepsJson;

    @Column(name = "rewards_json", columnDefinition = "TEXT")
    private String rewardsJson;

    @Column(name = "costs_json", columnDefinition = "TEXT")
    private String costsJson;

    @Column(name = "automation_hints_json", columnDefinition = "TEXT")
    private String automationHintsJson;

    @Column(name = "verification_notes", length = 2000)
    private String verificationNotes;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "price", precision = 19, scale = 4)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_tier", nullable = false, length = 10)
    private LicenseTier licenseTier = LicenseTier.L1;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_scope", nullable = false, length = 20)
    private VisibilityScope visibilityScope = VisibilityScope.PRIVATE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "blueprint", fetch = FetchType.LAZY)
    private Set<NpcScenarioInstanceEntity> instances = new HashSet<>();
}


