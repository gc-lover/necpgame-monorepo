package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueLockEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.service.command.QueueItemUpdateCommand;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.exception.VersionConflictException;
import com.necpgame.workqueue.service.model.AgentPreference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentTaskService {
    private static final String REQUIREMENT_GLOBAL = "policy:workqueue";
    private static final String REQUIREMENT_AGENT_PREFIX = "agent-role:";
    private static final Comparator<QueueItemEntity> PRIORITY_COMPARATOR = Comparator
            .comparingInt(QueueItemEntity::getPriority).reversed()
            .thenComparing(QueueItemEntity::getUpdatedAt);

    private final AgentPreferenceService agentPreferenceService;
    private final AgentDirectoryService agentDirectoryService;
    private final QueueItemRepository queueItemRepository;
    private final QueueCommandService queueCommandService;
    private final QueueLockService queueLockService;
    private final QueueLockRepository queueLockRepository;
    private final EnumCatalogService enumCatalogService;

    public AgentTaskService(AgentPreferenceService agentPreferenceService,
                            AgentDirectoryService agentDirectoryService,
                            QueueItemRepository queueItemRepository,
                            QueueCommandService queueCommandService,
                            QueueLockService queueLockService,
                            QueueLockRepository queueLockRepository,
                            EnumCatalogService enumCatalogService) {
        this.agentPreferenceService = agentPreferenceService;
        this.agentDirectoryService = agentDirectoryService;
        this.queueItemRepository = queueItemRepository;
        this.queueCommandService = queueCommandService;
        this.queueLockService = queueLockService;
        this.queueLockRepository = queueLockRepository;
        this.enumCatalogService = enumCatalogService;
    }

    @Transactional(readOnly = true)
    public Optional<NextTask> findNextTask(UUID agentId) {
        AgentPreference preference = agentPreferenceService.get(agentId);

        Optional<QueueItemEntity> active = findActiveTask(agentId, preference);
        if (active.isPresent()) {
            QueueItemEntity item = active.get();
            return Optional.of(new NextTask(item, item.getCurrentState() != null ? item.getCurrentState().getStatusCode() : preference.acceptStatus(), false, preference.maxInProgressMinutes()));
        }

        return findFromSegments(agentId, preference.primarySegments(), preference, null)
                .or(() -> findFromSegments(agentId, preference.fallbackSegments(), preference, null));
    }

    @Transactional
    public AcceptedTask acceptTask(UUID agentId, UUID itemId, long expectedVersion, String statusOverride, String note, String payload, String metadata) {
        AgentPreference preference = agentPreferenceService.get(agentId);
        AgentEntity actor = agentDirectoryService.requireAgent(agentId);
        QueueItemEntity item = queueItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
        validateVersion(item, expectedVersion);

        String status = (statusOverride == null || statusOverride.isBlank()) ? preference.acceptStatus() : statusOverride;

        QueueItemUpdateCommand command = new QueueItemUpdateCommand(
                item.getId(),
                actor,
                status,
                expectedVersion,
                note,
                payload,
                actor,
                metadata,
                OffsetDateTime.now(),
                false
        );
        queueCommandService.updateItem(command);
        queueLockService.acquireItemLock(item.getId(), actor, preference.maxInProgressMinutes() * 60L);
        QueueItemEntity refreshed = queueItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
        return new AcceptedTask(refreshed, preference.maxInProgressMinutes());
    }

    @Transactional
    public QueueItemEntity releaseTask(UUID agentId, UUID itemId, long expectedVersion, String note, String statusOverride) {
        AgentPreference preference = agentPreferenceService.get(agentId);
        AgentEntity actor = agentDirectoryService.requireAgent(agentId);
        QueueItemEntity item = queueItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
        validateVersion(item, expectedVersion);
        String status = (statusOverride == null || statusOverride.isBlank()) ? preference.returnStatus() : statusOverride;

        QueueItemUpdateCommand command = new QueueItemUpdateCommand(
                item.getId(),
                actor,
                status,
                expectedVersion,
                note,
                null,
                null,
                null,
                OffsetDateTime.now(),
                true
        );
        queueCommandService.updateItem(command);
        queueLockRepository.lockByItem(itemId).ifPresent(lock -> {
            if (lock.getOwner().getId().equals(agentId)) {
                queueLockRepository.delete(lock);
            }
        });
        return queueItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
    }

    @Transactional
    public void releaseExpiredLock(QueueLockEntity lock) {
        if (lock.getItem() == null) {
            queueLockRepository.delete(lock);
            return;
        }
        UUID agentId = lock.getOwner().getId();
        AgentPreference preference = agentPreferenceService.get(agentId);
        AgentEntity actor = agentDirectoryService.requireAgent(agentId);
        QueueItemEntity item = queueItemRepository.findById(lock.getItem().getId())
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));

        QueueItemUpdateCommand command = new QueueItemUpdateCommand(
                item.getId(),
                actor,
                preference.returnStatus(),
                item.getVersion(),
                "Auto-release after timeout",
                null,
                null,
                null,
                OffsetDateTime.now(),
                true
        );
        queueCommandService.updateItem(command);
        queueLockRepository.delete(lock);
    }

    @Transactional
    public Optional<ClaimedTask> claimTask(UUID agentId, List<String> segmentsOverride, Integer priorityFloor) {
        AgentPreference preference = agentPreferenceService.get(agentId);
        Optional<QueueItemEntity> active = findActiveTask(agentId, preference);
        if (active.isPresent()) {
            QueueItemEntity item = active.get();
            throw new ApiErrorException(
                    HttpStatus.CONFLICT,
                    "active_task_exists",
                    "У агента уже есть активная задача",
                    buildRequirements(preference),
                    List.of(new ApiErrorDetail("activeItemId", item.getId().toString()))
            );
        }

        Optional<NextTask> candidate;
        List<String> overrideSegments = normalizeSegments(segmentsOverride);
        if (!overrideSegments.isEmpty()) {
            candidate = findFromSegments(agentId, overrideSegments, preference, priorityFloor);
        } else {
            candidate = findFromSegments(agentId, preference.primarySegments(), preference, priorityFloor)
                    .or(() -> findFromSegments(agentId, preference.fallbackSegments(), preference, priorityFloor));
        }

        if (candidate.isEmpty()) {
            return Optional.empty();
        }

        NextTask next = candidate.get();
        AcceptedTask accepted = acceptTask(agentId, next.item().getId(), next.item().getVersion(), null, "Auto-claim", null, null);
        return Optional.of(new ClaimedTask(accepted.item(), preference.acceptStatus(), accepted.ttlMinutes(), false));
    }

    private Optional<NextTask> findFromSegments(UUID agentId, List<String> segments, AgentPreference preference, Integer priorityFloor) {
        if (segments.isEmpty()) {
            return Optional.empty();
        }
        List<UUID> statusIds = enumCatalogService.requireTaskStatuses(preference.pickupStatuses());
        if (statusIds.isEmpty()) {
            return Optional.empty();
        }
        List<QueueItemEntity> candidates = queueItemRepository.findNextTaskCandidate(segments, statusIds, priorityFloor);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        QueueItemEntity selected = candidates.stream()
                .sorted(PRIORITY_COMPARATOR)
                .findFirst()
                .orElse(null);
        if (selected == null) {
            return Optional.empty();
        }
        return Optional.of(new NextTask(selected, preference.acceptStatus(), true, preference.maxInProgressMinutes()));
    }

    private Optional<QueueItemEntity> findActiveTask(UUID agentId, AgentPreference preference) {
        if (preference.activeStatuses().isEmpty()) {
            return Optional.empty();
        }
        List<UUID> statusIds = enumCatalogService.requireTaskStatuses(preference.activeStatuses());
        if (statusIds.isEmpty()) {
            return Optional.empty();
        }
        List<QueueItemEntity> active = queueItemRepository.findByAssignedToIdAndStatusValueIdIn(agentId, statusIds);
        return active.stream()
                .sorted(Comparator.comparing(QueueItemEntity::getUpdatedAt))
                .findFirst();
    }

    private void validateVersion(QueueItemEntity item, long expectedVersion) {
        if (!item.getVersion().equals(expectedVersion)) {
            throw new VersionConflictException("Queue item version mismatch");
        }
    }

    private List<String> normalizeSegments(List<String> segments) {
        if (segments == null) {
            return List.of();
        }
        return segments.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.trim().toLowerCase())
                .toList();
    }

    private List<String> buildRequirements(AgentPreference preference) {
        List<String> requirements = new ArrayList<>();
        requirements.add(REQUIREMENT_GLOBAL);
        if (preference.roleKey() != null && !preference.roleKey().isBlank()) {
            requirements.add(REQUIREMENT_AGENT_PREFIX + preference.roleKey());
        }
        return requirements;
    }

    public record NextTask(QueueItemEntity item, String recommendedStatus, boolean requiresAcceptance, int ttlMinutes) {
    }

    public record AcceptedTask(QueueItemEntity item, int ttlMinutes) {
    }

    public record ClaimedTask(QueueItemEntity item, String recommendedStatus, int ttlMinutes, boolean existingAssignment) {
    }
}

