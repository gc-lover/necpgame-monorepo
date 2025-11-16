package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.service.command.QueueItemUpdateCommand;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.exception.VersionConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueCommandService {
    private final QueueItemRepository queueItemRepository;
    private final QueueItemStateRepository queueItemStateRepository;
    private final ActivityLogService activityLogService;
    private final EnumCatalogService enumCatalogService;

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    public QueueItemEntity updateItem(QueueItemUpdateCommand command) {
        QueueItemEntity item = queueItemRepository.lockById(command.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));

        if (!item.getVersion().equals(command.expectedVersion())) {
            throw new VersionConflictException("Queue item version mismatch");
        }

        if (command.clearAssignee()) {
            item.assignTo(null);
        } else if (command.assignedTo() != null) {
            item.assignTo(command.assignedTo());
        }
        if (command.payload() != null) {
            item.updatePayload(command.payload());
        }
        item.clearLock();
        item.updateTimestamp(command.occurredAt());

        UUID statusValueId = enumCatalogService.requireTaskStatus(command.statusCode());

        QueueItemStateEntity state = QueueItemStateEntity.create(
                item,
                command.actor(),
                command.statusCode(),
                statusValueId,
                command.note(),
                command.metadata(),
                command.occurredAt()
        );

        QueueItemStateEntity persistedState = queueItemStateRepository.save(state);
        item.applyState(persistedState);
        item.setStatusValueId(statusValueId);
        QueueItemEntity savedItem = queueItemRepository.save(item);
        activityLogService.recordItemEvent(command.actor(), savedItem, command.statusCode(), command.metadata());
        return savedItem;
    }
}
