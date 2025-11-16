package com.necpgame.workqueue.web;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.security.AgentContext;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.AgentDirectoryService;
import com.necpgame.workqueue.service.QueueCommandService;
import com.necpgame.workqueue.service.QueueQueryService;
import com.necpgame.workqueue.service.command.QueueItemUpdateCommand;
import com.necpgame.workqueue.web.dto.QueueItemDetailDto;
import com.necpgame.workqueue.web.dto.QueueItemUpdateRequestDto;
import com.necpgame.workqueue.web.mapper.QueueMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue-items")
@RequiredArgsConstructor
public class QueueItemController {
    private final QueueQueryService queueQueryService;
    private final QueueCommandService queueCommandService;
    private final AgentDirectoryService agentDirectoryService;
    private final AgentContext agentContext;
    private final QueueMapper queueMapper;

    @GetMapping("/{id}")
    public QueueItemDetailDto getItem(@PathVariable UUID id) {
        QueueItemEntity item = queueQueryService.requireItem(id);
        var history = queueQueryService.loadHistory(item);
        return queueMapper.toDetail(item, history);
    }

    @PatchMapping("/{id}")
    public QueueItemDetailDto updateItem(@PathVariable UUID id, @RequestBody @Valid QueueItemUpdateRequestDto request) {
        AgentPrincipal principal = java.util.Objects.requireNonNull(agentContext.currentPrincipal(), "Unauthenticated");
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        AgentEntity assigned = request.assignedTo() != null ? agentDirectoryService.requireAgent(request.assignedTo()) : null;
        QueueItemUpdateCommand command = new QueueItemUpdateCommand(
                id,
                actor,
                request.statusCode(),
                request.expectedVersion(),
                request.note(),
                request.payload(),
                assigned,
                request.metadata(),
                OffsetDateTime.now(),
                false
        );
        QueueItemEntity updated = queueCommandService.updateItem(command);
        var history = queueQueryService.loadHistory(updated);
        return queueMapper.toDetail(updated, history);
    }
}

