package com.necpgame.workqueue.repository.world;

import com.necpgame.workqueue.domain.world.WorldLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorldLocationRepository extends JpaRepository<WorldLocationEntity, UUID> {
}

