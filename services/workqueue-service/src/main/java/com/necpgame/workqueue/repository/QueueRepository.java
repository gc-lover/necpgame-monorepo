package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueRepository extends JpaRepository<QueueEntity, UUID> {
    List<QueueEntity> findBySegment(String segment);

    Optional<QueueEntity> findBySegmentAndStatusCode(String segment, String statusCode);
}


