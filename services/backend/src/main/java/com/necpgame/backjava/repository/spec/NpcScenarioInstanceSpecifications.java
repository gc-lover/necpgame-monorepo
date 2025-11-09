package com.necpgame.backjava.repository.spec;

import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import com.necpgame.backjava.model.ScenarioInstanceStatus;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class NpcScenarioInstanceSpecifications {

    private NpcScenarioInstanceSpecifications() {
    }

    public static Specification<NpcScenarioInstanceEntity> belongsToBlueprint(UUID blueprintId) {
        return (root, query, cb) -> cb.equal(root.get("blueprintId"), blueprintId);
    }

    public static Specification<NpcScenarioInstanceEntity> hasStatus(ScenarioInstanceStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}


