package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.QueueQueryService;
import com.necpgame.workqueue.web.dto.agent.AgentTaskClaimResponseDto;
import com.necpgame.workqueue.web.mapper.TaskClaimResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/agents/tasks")
@RequiredArgsConstructor
public class AgentTaskQueryController {
    private final QueueQueryService queueQueryService;
    private final TaskClaimResponseFactory taskClaimResponseFactory;

    @GetMapping("/items/{itemId}")
    public ResponseEntity<AgentTaskClaimResponseDto> getItem(@AuthenticationPrincipal AgentPrincipal principal,
                                                             @PathVariable UUID itemId) {
        var item = queueQueryService.requireItem(itemId);
        var dto = taskClaimResponseFactory.buildForItem(item);
        return ResponseEntity.ok(dto);
    }
}


