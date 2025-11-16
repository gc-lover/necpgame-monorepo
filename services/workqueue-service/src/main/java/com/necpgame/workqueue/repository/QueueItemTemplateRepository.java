package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QueueItemTemplateRepository extends JpaRepository<QueueItemTemplateEntity, UUID> {
    @EntityGraph(attributePaths = {"item"})
    List<QueueItemTemplateEntity> findByItem(QueueItemEntity item);
}

