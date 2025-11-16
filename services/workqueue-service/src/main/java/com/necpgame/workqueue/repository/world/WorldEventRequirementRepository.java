package com.necpgame.workqueue.repository.world;

import com.necpgame.workqueue.domain.world.WorldEventRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorldEventRequirementRepository extends JpaRepository<WorldEventRequirementEntity, UUID> {
    List<WorldEventRequirementEntity> findByWorldEvent_Id(UUID eventId);

    void deleteByWorldEvent_Id(UUID eventId);
}

