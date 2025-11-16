package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.QuestCommandService;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.quest.QuestCommandRequestDto;
import com.necpgame.workqueue.web.dto.quest.QuestDetailDto;
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
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {
    private static final String REQUIREMENT = "policy:content";
    private final QuestCommandService questCommandService;

    @PostMapping
    public ResponseEntity<QuestDetailDto> create(@AuthenticationPrincipal AgentPrincipal principal,
                                                 @Valid @RequestBody QuestCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager"));
        QuestDetailDto detail = questCommandService.create(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(detail);
    }

    @PutMapping
    public ResponseEntity<QuestDetailDto> update(@AuthenticationPrincipal AgentPrincipal principal,
                                                 @Valid @RequestBody QuestCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager", "api-task-architect"));
        QuestDetailDto detail = questCommandService.update(principal, request);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<QuestDetailDto> get(@PathVariable UUID contentId) {
        return ResponseEntity.ok(questCommandService.detail(contentId));
    }

    private void assertRole(AgentPrincipal principal, List<String> roles) {
        if (principal == null || principal.roleKey() == null || roles.stream().noneMatch(r -> r.equals(principal.roleKey()))) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "quest.forbidden",
                    "Недостаточно прав для выполнения операции",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("X-Agent-Role", "Доступ разрешён для: " + String.join(", ", roles)))
            );
        }
    }
}


