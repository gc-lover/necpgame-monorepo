package com.necpgame.workqueue.web.mapper;

import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.web.dto.QueueItemDetailDto;
import com.necpgame.workqueue.web.dto.QueueItemStateDto;
import com.necpgame.workqueue.web.dto.QueueItemSummaryDto;
import com.necpgame.workqueue.web.dto.QueueListItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QueueMapper {
    public QueueListItemDto toQueueDto(QueueEntity entity) {
        return new QueueListItemDto(entity.getId(), entity.getSegment(), entity.getStatusCode(), entity.getTitle(), entity.getUpdatedAt());
    }

    public QueueItemSummaryDto toSummary(QueueItemEntity entity) {
        String status = entity.getCurrentState() != null ? entity.getCurrentState().getStatusCode() : null;
        return new QueueItemSummaryDto(
                entity.getId(),
                entity.getQueue().getId(),
                entity.getTitle(),
                entity.getStatusValueId(),
                status,
                entity.getPriority(),
                entity.getAssignedTo() != null ? entity.getAssignedTo().getId() : null,
                entity.getDueAt(),
                entity.getUpdatedAt(),
                entity.getLockedUntil(),
                entity.getVersion() != null ? entity.getVersion() : 0
        );
    }

    public QueueItemDetailDto toDetail(QueueItemEntity entity, List<QueueItemStateEntity> history) {
        List<QueueItemStateDto> states = history.stream()
                .map(this::toStateDto)
                .collect(Collectors.toList());
        return new QueueItemDetailDto(toSummary(entity), entity.getPayload(), states);
    }

    private QueueItemStateDto toStateDto(QueueItemStateEntity state) {
        return new QueueItemStateDto(
                state.getId(),
                state.getStatusValueId(),
                state.getStatusCode(),
                state.getNote(),
                state.getActor() != null ? state.getActor().getId() : null,
                state.getMetadata(),
                state.getCreatedAt()
        );
    }
}


