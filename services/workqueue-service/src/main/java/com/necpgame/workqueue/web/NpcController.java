package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.NpcCommandService;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.npc.NpcCommandRequestDto;
import com.necpgame.workqueue.web.dto.npc.NpcDetailDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/npcs")
@RequiredArgsConstructor
public class NpcController {
    private static final String REQUIREMENT = "policy:content";
    private final NpcCommandService npcCommandService;

    @PostMapping
    public ResponseEntity<NpcDetailDto> create(@AuthenticationPrincipal AgentPrincipal principal,
                                               @Valid @RequestBody NpcCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager"));
        NpcDetailDto detail = npcCommandService.create(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(detail);
    }

    @PutMapping
    public ResponseEntity<NpcDetailDto> update(@AuthenticationPrincipal AgentPrincipal principal,
                                               @Valid @RequestBody NpcCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager", "backend-implementer"));
        return ResponseEntity.ok(npcCommandService.update(principal, request));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<NpcDetailDto> get(@PathVariable UUID contentId) {
        return ResponseEntity.ok(npcCommandService.detail(contentId));
    }

    private void assertRole(AgentPrincipal principal, List<String> allowed) {
        if (principal == null || principal.roleKey() == null || allowed.stream().noneMatch(role -> role.equals(principal.roleKey()))) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "npc.forbidden",
                    "Недостаточно прав для выполнения операции",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("X-Agent-Role", "Доступ разрешён для: " + String.join(", ", allowed)))
            );
        }
    }
}

