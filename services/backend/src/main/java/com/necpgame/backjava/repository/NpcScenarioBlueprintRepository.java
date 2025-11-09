package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NpcScenarioBlueprintEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NpcScenarioBlueprintRepository extends JpaRepository<NpcScenarioBlueprintEntity, UUID>, JpaSpecificationExecutor<NpcScenarioBlueprintEntity> {
}


