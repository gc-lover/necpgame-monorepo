package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.service.AgentBriefService;
import com.necpgame.workqueue.service.AgentTaskService;
import com.necpgame.workqueue.service.QueueQueryService;
import com.necpgame.workqueue.service.ReferenceTemplateService;
import com.necpgame.workqueue.web.dto.ClaimInstructionsDto;
import com.necpgame.workqueue.web.dto.ClaimTemplateDto;
import com.necpgame.workqueue.web.dto.QueueItemDetailDto;
import com.necpgame.workqueue.web.dto.SubmitContractDto;
import com.necpgame.workqueue.web.dto.reference.ReferenceTemplateDto;
import com.necpgame.workqueue.web.dto.agent.AgentTaskClaimResponseDto;
import com.necpgame.workqueue.web.dto.agent.AgentBriefDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskClaimResponseFactory {
    private static final Logger log = LoggerFactory.getLogger(TaskClaimResponseFactory.class);
    private final QueueQueryService queueQueryService;
    private final QueueItemTemplateRepository queueItemTemplateRepository;
    private final AgentBriefService agentBriefService;
    private final ReferenceTemplateService referenceTemplateService;
    private final QueueMapper queueMapper;
    private final ObjectMapper objectMapper;

    public AgentTaskClaimResponseDto build(AgentTaskService.ClaimedTask claimedTask) {
        QueueItemEntity item = claimedTask.item();
        QueueItemDetailDto detail = queueMapper.toDetail(item, queueQueryService.loadHistory(item));
        ClaimInstructionsDto instructions = buildInstructions(item);
        return new AgentTaskClaimResponseDto(
                detail,
                claimedTask.recommendedStatus(),
                claimedTask.ttlMinutes(),
                claimedTask.existingAssignment(),
                instructions
        );
    }

    public AgentTaskClaimResponseDto buildForItem(QueueItemEntity item) {
        QueueItemDetailDto detail = queueMapper.toDetail(item, queueQueryService.loadHistory(item));
        String currentStatus = item.getCurrentState() != null ? item.getCurrentState().getStatusCode() : null;
        ClaimInstructionsDto instructions = buildInstructions(item);
        return new AgentTaskClaimResponseDto(
                detail,
                currentStatus,
                0,
                true,
                instructions
        );
    }

    private ClaimInstructionsDto buildInstructions(QueueItemEntity item) {
        JsonNode payload = readPayload(item.getPayload());
        AgentBriefDto brief = agentBriefService.findBySegment(item.getQueue() == null ? null : item.getQueue().getSegment())
                .orElse(null);
        List<String> knowledgeRefs = extractKnowledgeRefs(payload);
        JsonNode handoffPlan = payload.has("handoffPlan") ? payload.get("handoffPlan") : NullNode.getInstance();

        List<ClaimTemplateDto> templateDtos = queueItemTemplateRepository.findByItem(item).stream()
                .map(this::toTemplateDto)
                .toList();

        return new ClaimInstructionsDto(
                brief,
                knowledgeRefs,
                templateDtos,
                handoffPlan,
                buildSubmitContract(item)
        );
    }

    private SubmitContractDto buildSubmitContract(QueueItemEntity item) {
        String method = "POST";
        String path = "/api/agents/tasks/{itemId}/submit";
        String contentType = "multipart/form-data";
        String segment = item.getQueue() == null ? null : item.getQueue().getSegment();
        List<String> required = switch (segment == null ? "" : segment.toLowerCase()) {
            case "vision" -> List.of(
                    "payload:application/json (notes, artifacts[], metadata)",
                    "metadata:application/json (handoff.nextSegment='api')"
            );
            case "api" -> List.of(
                    "payload:application/json (notes, artifacts[], metadata)",
                    "spec-file: file (.yaml/.yml/.json) ИЛИ artifacts.url → .yaml/.yml/.json",
                    "metadata:application/json (openapiVersion, specPath)"
            );
            case "backend" -> List.of(
                    "payload:application/json (notes, artifacts[], metadata)",
                    "metadata:application/json (buildSuccess=true, commit)"
            );
            case "frontend" -> List.of(
                    "payload:application/json (notes, artifacts[], metadata)",
                    "metadata:application/json (buildSuccess=true, artifactUrl=.zip)"
            );
            default -> List.of(
                    "payload:application/json (notes, artifacts[], metadata)"
            );
        };
        java.util.Map<String, String> encoding = java.util.Map.of(
                "knowledgeRefs", "repeat field per value",
                "templates.primary", "repeat field per value",
                "artifacts.url", "repeat field per value"
        );
        java.util.Map<String, String> headers = java.util.Map.of(
                "X-Agent-Role", segment == null ? "<role>" : segment
        );
        String exampleCurl = buildSegmentCurl(segment);
        String response = "{ \"itemId\":\"" + item.getId() + "\", \"status\":\"completed\", \"nextItemId\":\"<uuid>\", \"nextSegment\":\"<segment>\" }";
        String payloadExample = buildPayloadExample(segment);
        return new SubmitContractDto(method, path, contentType, required, encoding, headers, exampleCurl, response, payloadExample);
    }

    private String buildSegmentCurl(String segment) {
        String base = "curl -s -X POST \"/api/agents/tasks/$ITEM/submit\" "
                + "-H \"X-Agent-Role: $ROLE\" "
                + "-H \"Content-Type: multipart/form-data\" ";
        String visionForm = "-F \"payload={\\\"notes\\\":\\\"brief\\\",\\\"artifacts\\\":[],\\\"metadata\\\":\\\"{\\\\\\\"handoff\\\\\\\":{\\\\\\\"nextSegment\\\\\\\":\\\\\\\"api\\\\\\\"}}\\\"};type=application/json\"";
        String apiForm = "-F \"payload={\\\"notes\\\":\\\"api-contract\\\",\\\"artifacts\\\":[],\\\"metadata\\\":\\\"{\\\\\\\"openapiVersion\\\\\\\":\\\\\\\"3.0.3\\\\\\\",\\\\\\\"specPath\\\\\\\":\\\\\\\"services/openapi/api/workqueue.yaml\\\\\\\"}\\\"};type=application/json\" "
                + "-F \"spec-file=@workqueue.yaml;type=application/yaml\"";
        String backendForm = "-F \"payload={\\\"notes\\\":\\\"build done\\\",\\\"artifacts\\\":[],\\\"metadata\\\":\\\"{\\\\\\\"buildSuccess\\\\\\\":true,\\\\\\\"commit\\\\\\\":\\\\\\\"<sha>\\\\\\\"}\\\"};type=application/json\"";
        String frontendForm = "-F \"payload={\\\"notes\\\":\\\"build done\\\",\\\"artifacts\\\":[],\\\"metadata\\\":\\\"{\\\\\\\"buildSuccess\\\\\\\":true,\\\\\\\"artifactUrl\\\\\\\":\\\\\\\"https://example.com/build.zip\\\\\\\"}\\\"};type=application/json\"";
        String defForm = "-F \"payload={\\\"notes\\\":\\\"brief\\\",\\\"artifacts\\\":[],\\\"metadata\\\":\\\"{}\\\"};type=application/json\"";
        return switch (segment == null ? "" : segment.toLowerCase()) {
            case "vision" -> base + visionForm;
            case "api" -> base + apiForm;
            case "backend" -> base + backendForm;
            case "frontend" -> base + frontendForm;
            default -> base + defForm;
        };
    }

    private String buildPayloadExample(String segment) {
        return switch (segment == null ? "" : segment.toLowerCase()) {
            case "vision" -> """
{
  "notes": "Сформирован vision-brief и roadmap",
  "artifacts": [],
  "metadata": "{\\"handoff\\":{\\"nextSegment\\":\\"api\\"}}"
}
""";
            case "api" -> """
{
  "notes": "OpenAPI спецификация подготовлена",
  "artifacts": [],
  "metadata": "{\\"openapiVersion\\":\\"3.0.3\\",\\"specPath\\":\\"services/openapi/api/workqueue.yaml\\"}"
}
""";
            case "backend" -> """
{
  "notes": "Сервис собран и протестирован",
  "artifacts": [],
  "metadata": "{\\"buildSuccess\\":true,\\"commit\\":\\"<sha>\\"}"
}
""";
            case "frontend" -> """
{
  "notes": "Фронтенд собран",
  "artifacts": [],
  "metadata": "{\\"buildSuccess\\":true,\\"artifactUrl\\":\\"https://example.com/build.zip\\"}"
}
""";
            default -> """
{
  "notes": "brief",
  "artifacts": [],
  "metadata": "{}"
}
""";
        };
    }

    private ClaimTemplateDto toTemplateDto(QueueItemTemplateEntity entity) {
        var template = referenceTemplateService.find(entity.getTemplateCode());
        return new ClaimTemplateDto(
                entity.getTemplateCode(),
                entity.getTemplateType().name().toLowerCase(),
                template.map(ReferenceTemplateDto::title).orElse(null),
                template.map(ReferenceTemplateDto::version).orElse(entity.getTemplateVersion()),
                template.map(ReferenceTemplateDto::sourcePath).orElse(entity.getSourcePath()),
                template.map(ReferenceTemplateDto::body).orElse(null)
        );
    }

    private JsonNode readPayload(String json) {
        if (json == null || json.isBlank()) {
            return NullNode.getInstance();
        }
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            log.warn("Failed to parse queue item payload for {}", json, e);
            return NullNode.getInstance();
        }
    }

    private List<String> extractKnowledgeRefs(JsonNode payload) {
        if (payload == null || !payload.has("knowledgeRefs")) {
            return List.of();
        }
        List<String> refs = new ArrayList<>();
        payload.get("knowledgeRefs").forEach(node -> {
            if (node.isTextual()) {
                refs.add(node.asText());
            }
        });
        return refs;
    }
}

