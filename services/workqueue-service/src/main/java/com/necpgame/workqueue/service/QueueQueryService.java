package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueQueryService {
    private final QueueRepository queueRepository;
    private final QueueItemRepository queueItemRepository;
    private final QueueItemStateRepository queueItemStateRepository;
    private final EnumCatalogService enumCatalogService;

    @Transactional(readOnly = true)
    public List<QueueEntity> findQueuesBySegment(String segment) {
        java.util.Objects.requireNonNull(segment, "segment required");
        return queueRepository.findBySegment(segment);
    }

    @Transactional(readOnly = true)
    public QueueItemEntity requireItem(UUID id) {
        return queueItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
    }

    @Transactional(readOnly = true)
    public List<QueueItemEntity> findItems(String segment, Collection<String> statuses, UUID assignedTo) {
        java.util.Objects.requireNonNull(segment, "segment required");
        if (statuses == null || statuses.isEmpty()) {
            if (assignedTo == null) {
                return queueItemRepository.findByQueueSegment(segment);
            }
            return queueItemRepository.findByQueueSegmentAndAssignedToId(segment, assignedTo);
        }
        List<java.util.UUID> statusIds = enumCatalogService.requireTaskStatuses(statuses);
        if (statusIds.isEmpty()) {
            return java.util.List.of();
        }
        if (assignedTo == null) {
            return queueItemRepository.findByQueueSegmentAndStatusValueIdIn(segment, statusIds);
        }
        return queueItemRepository.findByQueueSegmentAndAssignedToIdAndStatusValueIdIn(segment, assignedTo, statusIds);
    }

    @Transactional(readOnly = true)
    public List<QueueItemStateEntity> loadHistory(QueueItemEntity item) {
        return new ArrayList<>(queueItemStateRepository.findByItemOrderByCreatedAtAsc(item));
    }
}

