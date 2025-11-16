package com.necpgame.workqueue.scheduler;

import com.necpgame.workqueue.domain.QueueLockEntity;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.service.AgentTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AgentTaskTimeoutJob {
    private final QueueLockRepository queueLockRepository;
    private final AgentTaskService agentTaskService;

    @Scheduled(fixedDelayString = "${workqueue.timeout.poll-interval-ms:60000}")
    public void releaseExpired() {
        List<QueueLockEntity> expiredLocks = queueLockRepository.findByExpiresAtBefore(OffsetDateTime.now());
        if (expiredLocks.isEmpty()) {
            return;
        }
        expiredLocks.forEach(lock -> {
            try {
                agentTaskService.releaseExpiredLock(lock);
            } catch (Exception ex) {
                log.warn("Failed to auto-release lock {}: {}", lock.getId(), ex.getMessage());
            }
        });
    }
}

