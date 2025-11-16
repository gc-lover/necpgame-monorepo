package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.ContentCommandService;
import com.necpgame.workqueue.service.ContentQueryService;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.content.ContentDetailDto;
import com.necpgame.workqueue.web.dto.content.command.ContentCommandRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentCommandController {
    private static final String REQUIREMENT = "policy:content";
    private final ContentCommandService contentCommandService;
    private final ContentQueryService contentQueryService;

    @PostMapping("/entities")
    public ResponseEntity<ContentDetailDto> create(@AuthenticationPrincipal AgentPrincipal principal,
                                                   @RequestBody @Valid ContentCommandRequestDto request) {
        assertRole(principal, List.of("concept-director"));
        var entity = contentCommandService.create(principal, request);
        var detail = contentQueryService.getDetail(entity.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(location).body(detail);
    }

    @PutMapping("/entities/{id}")
    public ResponseEntity<ContentDetailDto> update(@AuthenticationPrincipal AgentPrincipal principal,
                                                   @PathVariable UUID id,
                                                   @RequestBody @Valid ContentCommandRequestDto request) {
        assertRole(principal, List.of("concept-director", "vision-manager"));
        var entity = contentCommandService.update(principal, id, request);
        var detail = contentQueryService.getDetail(entity.getId());
        return ResponseEntity.ok(detail);
    }

    private void assertRole(AgentPrincipal principal, List<String> allowedRoles) {
        if (principal == null || principal.roleKey() == null || allowedRoles.stream().noneMatch(role -> role.equals(principal.roleKey()))) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "content.forbidden",
                    "Недостаточно прав для выполнения операции",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("X-Agent-Role", "Доступ разрешён только для: " + String.join(", ", allowedRoles)))
            );
        }
    }
}


