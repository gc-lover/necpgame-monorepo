package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueItemArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QueueItemArtifactRepository extends JpaRepository<QueueItemArtifactEntity, UUID> {
}

