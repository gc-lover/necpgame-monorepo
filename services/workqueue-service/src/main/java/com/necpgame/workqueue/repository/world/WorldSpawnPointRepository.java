package com.necpgame.workqueue.repository.world;

import com.necpgame.workqueue.domain.world.WorldSpawnPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorldSpawnPointRepository extends JpaRepository<WorldSpawnPointEntity, UUID> {
    List<WorldSpawnPointEntity> findByLocation_Id(UUID locationId);

    void deleteByLocation_Id(UUID locationId);
}

