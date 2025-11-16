package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueLockRepository extends JpaRepository<QueueLockEntity, UUID> {
    Optional<QueueLockEntity> findByToken(String token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from QueueLockEntity l where l.item.id = :itemId")
    Optional<QueueLockEntity> lockByItem(@Param("itemId") UUID itemId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from QueueLockEntity l where l.queue.id = :queueId")
    Optional<QueueLockEntity> lockByQueue(@Param("queueId") UUID queueId);

    List<QueueLockEntity> findByExpiresAtBefore(OffsetDateTime cutoff);
}


