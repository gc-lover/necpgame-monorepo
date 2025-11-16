package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity, UUID> {
}


