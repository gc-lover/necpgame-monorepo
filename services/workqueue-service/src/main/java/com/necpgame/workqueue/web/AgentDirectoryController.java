package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.AgentDirectoryService;
import com.necpgame.workqueue.web.dto.agent.AgentListResponseDto;
import com.necpgame.workqueue.web.dto.agent.AgentSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentDirectoryController {
    private final AgentDirectoryService agentDirectoryService;

    @GetMapping
    public ResponseEntity<AgentListResponseDto> listAgents() {
        var agents = agentDirectoryService.listActive().stream()
                .map(agent -> new AgentSummaryDto(
                        agent.getId(),
                        agent.getRoleKey(),
                        agent.getDisplayName(),
                        agent.getContact(),
                        agent.getCreatedAt(),
                        agent.getUpdatedAt()
                ))
                .toList();
        return ResponseEntity.ok(new AgentListResponseDto(agents));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentSummaryDto> getAgent(@PathVariable UUID id) {
        var agent = agentDirectoryService.requireAgent(id);
        return ResponseEntity.ok(new AgentSummaryDto(
                agent.getId(),
                agent.getRoleKey(),
                agent.getDisplayName(),
                agent.getContact(),
                agent.getCreatedAt(),
                agent.getUpdatedAt()
        ));
    }

    @GetMapping("/role/{roleKey}")
    public ResponseEntity<AgentSummaryDto> getAgentByRole(@PathVariable String roleKey) {
        var agent = agentDirectoryService.requireActiveByRole(roleKey);
        return ResponseEntity.ok(new AgentSummaryDto(
                agent.getId(),
                agent.getRoleKey(),
                agent.getDisplayName(),
                agent.getContact(),
                agent.getCreatedAt(),
                agent.getUpdatedAt()
        ));
    }
}


