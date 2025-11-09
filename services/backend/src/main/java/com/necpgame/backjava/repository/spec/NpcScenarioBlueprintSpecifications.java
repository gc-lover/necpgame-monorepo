package com.necpgame.backjava.repository.spec;

import com.necpgame.backjava.entity.NpcScenarioBlueprintEntity;
import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import com.necpgame.backjava.model.ScenarioCategory;
import com.necpgame.backjava.model.ScenarioInstanceStatus;
import java.util.UUID;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public final class NpcScenarioBlueprintSpecifications {

    private NpcScenarioBlueprintSpecifications() {
    }

    public static Specification<NpcScenarioBlueprintEntity> hasOwner(UUID ownerId) {
        return (root, query, cb) -> cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<NpcScenarioBlueprintEntity> hasCategory(ScenarioCategory category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<NpcScenarioBlueprintEntity> hasPublicFlag(boolean isPublic) {
        return (root, query, cb) -> cb.equal(root.get("isPublic"), isPublic);
    }

    public static Specification<NpcScenarioBlueprintEntity> hasLicenseTier(NpcScenarioBlueprintEntity.LicenseTier tier) {
        return (root, query, cb) -> cb.equal(root.get("licenseTier"), tier);
    }

    public static Specification<NpcScenarioBlueprintEntity> hasInstanceWithStatus(ScenarioInstanceStatus status) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<NpcScenarioBlueprintEntity, NpcScenarioInstanceEntity> join = root.join("instances", JoinType.LEFT);
            return cb.equal(join.get("status"), status);
        };
    }
}


