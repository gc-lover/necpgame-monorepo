package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QueueItemStateRepository extends JpaRepository<QueueItemStateEntity, UUID> {
    @EntityGraph(attributePaths = {"actor"})
    List<QueueItemStateEntity> findByItemOrderByCreatedAtAsc(QueueItemEntity item);
}