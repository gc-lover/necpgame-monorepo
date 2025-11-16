package com.necpgame.workqueue.web;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueLockEntity;
import com.necpgame.workqueue.security.AgentContext;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.AgentDirectoryService;
import com.necpgame.workqueue.service.QueueLockService;
import com.necpgame.workqueue.web.dto.LockRequestDto;
import com.necpgame.workqueue.web.dto.LockResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/locks")
@RequiredArgsConstructor
public class LockController {
    private final QueueLockService queueLockService;
    private final AgentDirectoryService agentDirectoryService;
    private final AgentContext agentContext;

    @PostMapping
    public LockResponseDto acquireLock(@RequestBody @Valid LockRequestDto request) {
        AgentPrincipal principal = java.util.Objects.requireNonNull(agentContext.currentPrincipal(), "Unauthenticated");
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        long ttl = request.ttlSeconds() > 0 ? request.ttlSeconds() : 60;
        String scope = request.scope().toUpperCase(Locale.ROOT);
        QueueLockEntity lock;
        if ("QUEUE".equals(scope)) {
            if (request.queueId() == null) {
                throw new IllegalArgumentException("queueId is required for queue scope");
            }
            lock = queueLockService.acquireQueueLock(request.queueId(), actor, ttl);
        } else if ("ITEM".equals(scope)) {
            UUID itemId = request.itemId();
            if (itemId == null) {
                throw new IllegalArgumentException("itemId is required for item scope");
            }
            lock = queueLockService.acquireItemLock(itemId, actor, ttl);
        } else {
            throw new IllegalArgumentException("Unknown lock scope");
        }
        return new LockResponseDto(lock.getId(), lock.getToken(), lock.getExpiresAt());
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> releaseLock(@PathVariable String token) {
        AgentPrincipal principal = java.util.Objects.requireNonNull(agentContext.currentPrincipal(), "Unauthenticated");
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        queueLockService.release(token, actor);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanup() {
        queueLockService.cleanupExpired();
        return ResponseEntity.accepted().build();
    }
}

