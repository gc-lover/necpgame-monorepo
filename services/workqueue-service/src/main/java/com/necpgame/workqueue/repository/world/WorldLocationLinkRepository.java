package com.necpgame.workqueue.repository.world;

import com.necpgame.workqueue.domain.world.WorldLocationLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorldLocationLinkRepository extends JpaRepository<WorldLocationLinkEntity, UUID> {
    List<WorldLocationLinkEntity> findByFromLocation_Id(UUID locationId);

    void deleteByFromLocation_Id(UUID locationId);
}

