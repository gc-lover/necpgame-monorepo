package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.AgentPreferenceService;
import com.necpgame.workqueue.web.dto.agent.AgentPreferenceDto;
import com.necpgame.workqueue.web.dto.agent.AgentPreferenceUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/agents/preferences")
@RequiredArgsConstructor
public class AgentPreferenceController {
    private final AgentPreferenceService agentPreferenceService;

    @GetMapping("/me")
    public ResponseEntity<AgentPreferenceDto> getMe(@AuthenticationPrincipal AgentPrincipal principal) {
        var pref = agentPreferenceService.get(principal.id());
        return ResponseEntity.ok(new AgentPreferenceDto(
                pref.roleKey(),
                pref.primarySegments(),
                pref.fallbackSegments(),
                pref.pickupStatuses(),
                pref.activeStatuses(),
                pref.acceptStatus(),
                pref.returnStatus(),
                pref.maxInProgressMinutes()
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<AgentPreferenceDto> upsertMe(@AuthenticationPrincipal AgentPrincipal principal,
                                                       @RequestBody @Valid AgentPreferenceUpdateRequestDto request) {
        var updated = agentPreferenceService.upsert(
                principal.id(),
                principal.roleKey(),
                request.primarySegments(),
                request.fallbackSegments(),
                request.pickupStatuses(),
                request.activeStatuses(),
                request.acceptStatus(),
                request.returnStatus(),
                request.maxInProgressMinutes()
        );
        return ResponseEntity.ok(new AgentPreferenceDto(
                updated.roleKey(),
                updated.primarySegments(),
                updated.fallbackSegments(),
                updated.pickupStatuses(),
                updated.activeStatuses(),
                updated.acceptStatus(),
                updated.returnStatus(),
                updated.maxInProgressMinutes()
        ));
    }
}


