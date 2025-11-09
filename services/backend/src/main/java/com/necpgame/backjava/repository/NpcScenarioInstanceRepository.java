package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.necpgame.backjava.model.ScenarioInstanceStatus;
import java.util.Collection;

public interface NpcScenarioInstanceRepository extends JpaRepository<NpcScenarioInstanceEntity, UUID>, JpaSpecificationExecutor<NpcScenarioInstanceEntity> {

    boolean existsByBlueprintIdAndStatusIn(UUID blueprintId, Collection<ScenarioInstanceStatus> statuses);

    long countByBlueprintId(UUID blueprintId);
}


