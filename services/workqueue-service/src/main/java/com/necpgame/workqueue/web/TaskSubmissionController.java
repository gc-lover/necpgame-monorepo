package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.TaskSubmissionService;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents/tasks/{itemId}/submit")
@RequiredArgsConstructor
public class TaskSubmissionController {
    private final TaskSubmissionService taskSubmissionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskSubmissionResponseDto> submit(@AuthenticationPrincipal AgentPrincipal principal,
                                                            @PathVariable UUID itemId,
                                                            @RequestPart("payload") @Valid TaskSubmissionRequestDto payload,
                                                            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        UUID agentId = principal == null ? null : principal.id();
        TaskSubmissionResponseDto response = taskSubmissionService.submit(agentId, itemId, payload, files);
        return ResponseEntity.ok(response);
    }
}

