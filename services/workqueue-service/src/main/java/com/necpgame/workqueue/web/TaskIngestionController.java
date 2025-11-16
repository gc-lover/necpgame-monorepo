package com.necpgame.workqueue.web;

import com.necpgame.workqueue.config.WorkqueueIngestionProperties;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.TaskIngestionService;
import com.necpgame.workqueue.service.model.TaskIngestionRequest;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.HandoffCondition;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.HandoffPlan;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.TemplateReference;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.Templates;
import com.necpgame.workqueue.service.model.TaskIngestionResult;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.ingest.TaskIngestionRequestDto;
import com.necpgame.workqueue.web.dto.ingest.TaskIngestionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingest/tasks")
public class TaskIngestionController {
    private final TaskIngestionService taskIngestionService;
    private final WorkqueueIngestionProperties ingestionProperties;
    private final String ingestionSchema;

    public TaskIngestionController(TaskIngestionService taskIngestionService,
                                   WorkqueueIngestionProperties ingestionProperties,
                                   @Value("classpath:contracts/task-ingestion-request.schema.json") Resource schemaResource) {
        this.taskIngestionService = taskIngestionService;
        this.ingestionProperties = ingestionProperties;
        this.ingestionSchema = loadSchema(schemaResource);
    }

    @PostMapping
    public ResponseEntity<TaskIngestionResponseDto> ingest(@AuthenticationPrincipal AgentPrincipal principal,
                                                          @RequestBody @Valid TaskIngestionRequestDto dto) {
        assertConceptAgent(principal);
        TaskIngestionResult result = taskIngestionService.ingest(toRequest(dto));
        TaskIngestionResponseDto response = new TaskIngestionResponseDto(
                result.itemId(),
                result.queueId(),
                result.segment(),
                result.status(),
                result.createdAt()
        );
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/queue-items/{itemId}")
                .buildAndExpand(result.itemId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(value = "/schema", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> schema() {
        return ResponseEntity.ok(ingestionSchema);
    }

    private TaskIngestionRequest toRequest(TaskIngestionRequestDto dto) {
        List<String> refs = dto.knowledgeRefs().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
        return new TaskIngestionRequest(
                dto.sourceId(),
                dto.segment(),
                dto.initialStatus(),
                dto.priority(),
                dto.title(),
                dto.summary(),
                refs,
                toTemplates(dto.templates()),
                dto.payload() == null ? Map.of() : dto.payload(),
                toHandoff(dto.handoffPlan())
        );
    }

    private Templates toTemplates(TaskIngestionRequestDto.TemplatesDto dto) {
        if (dto == null) {
            return Templates.empty();
        }
        List<String> primary = sanitizeList(dto.primary());
        List<String> checklists = sanitizeList(dto.checklists());
        List<TemplateReference> references = dto.references() == null ? List.of() : dto.references().stream()
                .filter(Objects::nonNull)
                .map(ref -> new TemplateReference(ref.code(), ref.version(), ref.path()))
                .toList();
        return new Templates(primary, checklists, references);
    }

    private List<String> sanitizeList(List<String> source) {
        if (source == null) {
            return List.of();
        }
        return source.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private HandoffPlan toHandoff(TaskIngestionRequestDto.HandoffPlanDto dto) {
        List<HandoffCondition> conditions = dto.conditions() == null ? List.of() : dto.conditions().stream()
                .filter(Objects::nonNull)
                .map(condition -> new HandoffCondition(condition.status(), condition.targetSegment()))
                .toList();
        return new HandoffPlan(dto.nextSegment(), conditions, dto.notes());
    }

    private String loadSchema(Resource resource) {
        try {
            return StreamUtils.copyToString(resource.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Schema unavailable", e);
        }
    }

    private void assertConceptAgent(AgentPrincipal principal) {
        if (principal == null || principal.roleKey() == null || !principal.roleKey().equals(ingestionProperties.getSystemRole())) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "ingest.forbidden.agent",
                    "Только Concept Director может создавать задачи",
                    List.of("agent-brief:concept"),
                    List.of(new ApiErrorDetail("X-Agent-Role", "Укажите роль Concept Director"))
            );
        }
    }
}

