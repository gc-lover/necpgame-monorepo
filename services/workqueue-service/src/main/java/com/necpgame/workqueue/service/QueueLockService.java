package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueLockEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.exception.LockUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueLockService {
    private final QueueRepository queueRepository;
    private final QueueItemRepository queueItemRepository;
    private final QueueLockRepository queueLockRepository;

    @Transactional
    public QueueLockEntity acquireQueueLock(UUID queueId, AgentEntity owner, long ttlSeconds) {
        java.util.Objects.requireNonNull(queueId, "queueId required");
        java.util.Objects.requireNonNull(owner, "owner required");
        QueueEntity queue = queueRepository.findById(queueId).orElseThrow(() -> new EntityNotFoundException("Queue not found"));
        OffsetDateTime now = OffsetDateTime.now();
        var existingOpt = queueLockRepository.lockByQueue(queueId);
        if (existingOpt.isPresent()) {
            QueueLockEntity existing = existingOpt.get();
            if (existing.getExpiresAt().isAfter(now) && !existing.getOwner().getId().equals(owner.getId())) {
                throw new LockUnavailableException("Queue lock active");
            }
            queueLockRepository.delete(existing);
        }
        QueueLockEntity lock = QueueLockEntity.builder()
                .id(UUID.randomUUID())
                .scope(QueueLockEntity.Scope.QUEUE)
                .queue(queue)
                .owner(owner)
                .token(UUID.randomUUID().toString())
                .expiresAt(now.plusSeconds(ttlSeconds))
                .createdAt(now)
                .build();
        return queueLockRepository.save(lock);
    }

    @Transactional
    public QueueLockEntity acquireItemLock(UUID itemId, AgentEntity owner, long ttlSeconds) {
        java.util.Objects.requireNonNull(itemId, "itemId required");
        java.util.Objects.requireNonNull(owner, "owner required");
        QueueItemEntity item = queueItemRepository.lockById(itemId).orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
        OffsetDateTime now = OffsetDateTime.now();
        var existingOpt = queueLockRepository.lockByItem(itemId);
        if (existingOpt.isPresent()) {
            QueueLockEntity existing = existingOpt.get();
            if (existing.getExpiresAt().isAfter(now) && !existing.getOwner().getId().equals(owner.getId())) {
                throw new LockUnavailableException("Queue item lock active");
            }
            queueLockRepository.delete(existing);
        }
        QueueLockEntity lock = QueueLockEntity.builder()
                .id(UUID.randomUUID())
                .scope(QueueLockEntity.Scope.ITEM)
                .queue(item.getQueue())
                .item(item)
                .owner(owner)
                .token(UUID.randomUUID().toString())
                .expiresAt(now.plusSeconds(ttlSeconds))
                .createdAt(now)
                .build();
        QueueLockEntity saved = queueLockRepository.save(lock);
        item.setLockedUntil(saved.getExpiresAt());
        queueItemRepository.save(item);
        return saved;
    }

    @Transactional
    public void release(String token, AgentEntity actor) {
        java.util.Objects.requireNonNull(token, "token required");
        java.util.Objects.requireNonNull(actor, "actor required");
        QueueLockEntity lock = queueLockRepository.findByToken(token).orElseThrow(() -> new EntityNotFoundException("Lock not found"));
        if (!lock.getOwner().getId().equals(actor.getId())) {
            throw new LockUnavailableException("Lock owned by another agent");
        }
        queueLockRepository.delete(lock);
        if (lock.getItem() != null) {
            QueueItemEntity item = lock.getItem();
            item.setLockedUntil(null);
            queueItemRepository.save(item);
        }
    }

    @Transactional
    public int cleanupExpired() {
        OffsetDateTime now = OffsetDateTime.now();
        var locks = queueLockRepository.findByExpiresAtBefore(now);
        int count = locks.size();
        locks.forEach(lock -> {
            queueLockRepository.delete(lock);
            if (lock.getItem() != null) {
                QueueItemEntity item = lock.getItem();
                item.setLockedUntil(null);
                queueItemRepository.save(item);
            }
        });
        return count;
    }
}

