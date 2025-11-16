package com.necpgame.workqueue.web;

import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.WorldEventCommandService;
import com.necpgame.workqueue.service.WorldLocationCommandService;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.world.WorldEventCommandRequestDto;
import com.necpgame.workqueue.web.dto.world.WorldEventDetailDto;
import com.necpgame.workqueue.web.dto.world.WorldLocationCommandRequestDto;
import com.necpgame.workqueue.web.dto.world.WorldLocationDetailDto;
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
@RequestMapping("/api/world")
@RequiredArgsConstructor
public class WorldController {
    private static final String REQUIREMENT = "policy:content";
    private final WorldLocationCommandService worldLocationCommandService;
    private final WorldEventCommandService worldEventCommandService;

    @PostMapping("/locations")
    public ResponseEntity<WorldLocationDetailDto> createLocation(@AuthenticationPrincipal AgentPrincipal principal,
                                                                 @Valid @RequestBody WorldLocationCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager"));
        return ResponseEntity.status(HttpStatus.CREATED).body(worldLocationCommandService.create(principal, request));
    }

    @PutMapping("/locations")
    public ResponseEntity<WorldLocationDetailDto> updateLocation(@AuthenticationPrincipal AgentPrincipal principal,
                                                                 @Valid @RequestBody WorldLocationCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager", "backend-implementer"));
        return ResponseEntity.ok(worldLocationCommandService.update(principal, request));
    }

    @GetMapping("/locations/{contentId}")
    public ResponseEntity<WorldLocationDetailDto> getLocation(@PathVariable UUID contentId) {
        return ResponseEntity.ok(worldLocationCommandService.detail(contentId));
    }

    @PostMapping("/events")
    public ResponseEntity<WorldEventDetailDto> createEvent(@AuthenticationPrincipal AgentPrincipal principal,
                                                           @Valid @RequestBody WorldEventCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager"));
        return ResponseEntity.status(HttpStatus.CREATED).body(worldEventCommandService.create(principal, request));
    }

    @PutMapping("/events")
    public ResponseEntity<WorldEventDetailDto> updateEvent(@AuthenticationPrincipal AgentPrincipal principal,
                                                           @Valid @RequestBody WorldEventCommandRequestDto request) {
        assertRole(principal, List.of("vision-manager", "backend-implementer"));
        return ResponseEntity.ok(worldEventCommandService.update(principal, request));
    }

    @GetMapping("/events/{contentId}")
    public ResponseEntity<WorldEventDetailDto> getEvent(@PathVariable UUID contentId) {
        return ResponseEntity.ok(worldEventCommandService.detail(contentId));
    }

    private void assertRole(AgentPrincipal principal, List<String> allowed) {
        if (principal == null || principal.roleKey() == null || allowed.stream().noneMatch(role -> role.equals(principal.roleKey()))) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "world.forbidden",
                    "Недостаточно прав для выполнения операции",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("X-Agent-Role", "Доступ разрешён для: " + String.join(", ", allowed)))
            );
        }
    }
}

