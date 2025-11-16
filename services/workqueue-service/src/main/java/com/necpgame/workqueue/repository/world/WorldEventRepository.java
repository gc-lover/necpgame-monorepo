package com.necpgame.workqueue.repository.world;

import com.necpgame.workqueue.domain.world.WorldEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorldEventRepository extends JpaRepository<WorldEventEntity, UUID> {
}

