package com.necpgame.workqueue.web;

import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.service.AgentTaskService;
import com.necpgame.workqueue.web.dto.AgentNextTaskResponseDto;
import com.necpgame.workqueue.web.dto.AgentTaskAcceptRequestDto;
import com.necpgame.workqueue.web.dto.AgentTaskReleaseRequestDto;
import com.necpgame.workqueue.web.mapper.QueueMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentTaskController {
    private final AgentTaskService agentTaskService;
    private final QueueMapper queueMapper;

    @GetMapping("/next-task")
    public ResponseEntity<AgentNextTaskResponseDto> nextTask(@AuthenticationPrincipal com.necpgame.workqueue.security.AgentPrincipal principal) {
        UUID agentId = principal == null ? null : principal.id();
        return agentTaskService.findNextTask(agentId)
                .map(result -> ResponseEntity.ok(new AgentNextTaskResponseDto(
                        queueMapper.toSummary(result.item()),
                        result.recommendedStatus(),
                        result.ttlMinutes(),
                        result.requiresAcceptance()
                )))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/next-task/accept")
    public AgentNextTaskResponseDto acceptTask(@AuthenticationPrincipal com.necpgame.workqueue.security.AgentPrincipal principal, @RequestBody @Valid AgentTaskAcceptRequestDto request) {
        UUID agentId = principal == null ? null : principal.id();
        AgentTaskService.AcceptedTask result = agentTaskService.acceptTask(
                agentId,
                request.itemId(),
                request.expectedVersion(),
                request.statusCode(),
                request.note(),
                request.payload(),
                request.metadata()
        );
        QueueItemEntity updated = result.item();
        String currentStatus = updated.getCurrentState() != null ? updated.getCurrentState().getStatusCode() : null;
        return new AgentNextTaskResponseDto(queueMapper.toSummary(updated), currentStatus, result.ttlMinutes(), false);
    }

    @PostMapping("/next-task/release")
    public AgentNextTaskResponseDto releaseTask(@AuthenticationPrincipal com.necpgame.workqueue.security.AgentPrincipal principal, @RequestBody @Valid AgentTaskReleaseRequestDto request) {
        UUID agentId = principal == null ? null : principal.id();
        QueueItemEntity updated = agentTaskService.releaseTask(
                agentId,
                request.itemId(),
                request.expectedVersion(),
                request.note(),
                request.statusCode()
        );
        return new AgentNextTaskResponseDto(
                queueMapper.toSummary(updated),
                updated.getCurrentState() != null ? updated.getCurrentState().getStatusCode() : null,
                0,
                false
        );
    }
}

