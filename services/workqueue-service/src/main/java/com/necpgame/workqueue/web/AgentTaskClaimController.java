package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.AgentTaskService;
import com.necpgame.workqueue.web.dto.agent.AgentTaskClaimRequestDto;
import com.necpgame.workqueue.web.dto.agent.AgentTaskClaimResponseDto;
import com.necpgame.workqueue.web.mapper.TaskClaimResponseFactory;
import com.necpgame.workqueue.security.AgentPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents/tasks/claim")
@RequiredArgsConstructor
public class AgentTaskClaimController {
    private final AgentTaskService agentTaskService;
    private final TaskClaimResponseFactory taskClaimResponseFactory;

    @PostMapping
    public ResponseEntity<?> claim(@AuthenticationPrincipal AgentPrincipal principal,
                                   @RequestBody(required = false) @Valid AgentTaskClaimRequestDto request) {
        UUID agentId = principal == null ? null : principal.id();
        List<String> segments = request != null ? request.segments() : List.of();
        Integer priorityFloor = request != null ? request.priorityFloor() : null;

        var claimed = agentTaskService.claimTask(agentId, segments, priorityFloor);
        if (claimed.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ClaimNoTaskResponse("no_tasks", "Нет доступных задач"));
        }
        AgentTaskClaimResponseDto response = taskClaimResponseFactory.build(claimed.get());
        return ResponseEntity.ok(response);
    }

    public record ClaimNoTaskResponse(String code, String message) {
    }
}

