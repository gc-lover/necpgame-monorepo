package com.necpgame.workqueue.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.AgentPreferenceEntity;
import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.ReferenceTemplateEntity;
import com.necpgame.workqueue.repository.ActivityLogRepository;
import com.necpgame.workqueue.repository.AgentPreferenceRepository;
import com.necpgame.workqueue.repository.AgentRepository;
import com.necpgame.workqueue.repository.HandoffRuleRepository;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.repository.ReferenceTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentPreferenceRepository agentPreferenceRepository;

    @Autowired
    private QueueItemRepository queueItemRepository;

    @Autowired
    private HandoffRuleRepository handoffRuleRepository;

    @Autowired
    private ReferenceTemplateRepository referenceTemplateRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueRepository queueRepository;

    private UUID backendAgentId;
    private UUID qaAgentId;
    private UUID conceptAgentId;

    private static final String SEGMENT_CONCEPT = "concept";

    @BeforeEach
    void setup() {
        cleanupDatabase();
        seedAgents();
    }

    @Test
    void ingestionEndpointCreatesQueueItem() throws Exception {
        UUID itemId = ingestTask("INGEST-INT-1", "concept");

        assertThat(queueItemRepository.findById(itemId)).isPresent();
    }

    @Test
    void claimSubmitFlowCreatesNextSegmentTask() throws Exception {
        seedHandoffRule();
        UUID itemId = ingestTask("BACK-INT-1", "backend");

        UUID claimedItemId = claimTask(backendAgentId, List.of("backend"));
        assertThat(claimedItemId).isEqualTo(itemId);

        submitAsAgent(backendAgentId, claimedItemId, false);

        assertThat(queueItemRepository.findByExternalRef("BACK-INT-1::frontend")).isPresent();
        assertThat(activityLogRepository.count()).isGreaterThan(0);
    }

    @Test
    void submitRejectedForForeignAgent() throws Exception {
        UUID itemId = ingestTask("BACK-INT-2", "backend");
        claimTask(backendAgentId, List.of("backend"));

        submitAsAgent(qaAgentId, itemId, true);
    }

    @Test
    void ingestionLoadSmokeTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            ingestTask("LOAD-" + i, "concept");
        }

        var items = queueItemRepository.findByQueueSegment("concept");
        assertThat(items).hasSize(10);
    }

    private UUID ingestTask(String sourceId, String targetSegment) throws Exception {
        Map<String, Object> payload = Map.of(
                "sourceId", sourceId,
                "segment", SEGMENT_CONCEPT,
                "initialStatus", "queued",
                "priority", 3,
                "title", "Integration Task " + sourceId,
                "summary", "Summary for " + sourceId,
                    "knowledgeRefs", List.of("/api/reference/templates/knowledge-entry-template"),
                "templates", Map.of(
                        "primary", List.of("concept-director-checklist"),
                        "checklists", List.of(),
                        "references", List.of(
                                Map.of(
                                        "code", "concept-canon",
                                        "version", "2025.11",
                                        "path", "pipeline/templates/concept-canon.yaml"
                                )
                        )
                ),
                "payload", Map.of("feature", "integration"),
                "handoffPlan", Map.of(
                        "nextSegment", "frontend",
                        "conditions", List.of(),
                        "notes", ""
                )
        );

        MvcResult result = mockMvc.perform(post("/api/ingest/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .header("X-Agent-Role", agentRole(conceptAgentId))
                        )
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID itemId = UUID.fromString(node.get("itemId").asText());

        if (!SEGMENT_CONCEPT.equals(targetSegment)) {
            queueItemRepository.findById(itemId).ifPresent(queueItem -> {
                QueueEntity queue = queueItem.getQueue();
                queue.setSegment(targetSegment);
                queue.setTitle(targetSegment.toUpperCase(Locale.ROOT) + " :: queued");
                queueRepository.save(queue);
            });
        }

        return itemId;
    }

    private UUID claimTask(UUID agentId, List<String> segments) throws Exception {
        Map<String, Object> request = Map.of(
                "segments", segments,
                "priorityFloor", 1
        );
        MvcResult result = mockMvc.perform(post("/api/agents/tasks/claim")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Agent-Role", agentRole(agentId))
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.summary.id").exists())
                .andReturn();
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return UUID.fromString(node.path("item").path("summary").path("id").asText());
    }

    private void submitAsAgent(UUID agentId, UUID itemId, boolean expectForbidden) throws Exception {
        Map<String, Object> payload = Map.of(
                "notes", "All checks passed",
                "artifacts", List.of(
                        Map.of("title", "CI build", "url", "https://ci.necp.game/job/123")
                ),
                "metadata", "{\"tests\":\"green\"}"
        );

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(
                "/api/agents/tasks/{itemId}/submit", itemId
        );
        MockMultipartFile payloadFile = new MockMultipartFile(
                "payload",
                "payload",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(payload)
        );
        builder.file(payloadFile);
        builder.contentType(MediaType.MULTIPART_FORM_DATA);

        if (expectForbidden) {
            mockMvc.perform(builder
                            .header("X-Agent-Role", agentRole(agentId))
                            )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.code").value("submission.not_owner"));
        } else {
            mockMvc.perform(builder
                            .header("X-Agent-Role", agentRole(agentId))
                            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nextSegment").value("frontend"));
        }
    }

    private void seedHandoffRule() {
        referenceTemplateRepository.save(ReferenceTemplateEntity.builder()
                .code("frontend-brief")
                .title("Frontend Brief")
                .body("## Instructions")
                .type("primary")
                .sourcePath("pipeline/templates/frontend-brief.yaml")
                .version("2025.11")
                .contentHash("hash")
                .updatedAt(OffsetDateTime.now())
                .build());

        handoffRuleRepository.save(HandoffRuleEntity.builder()
                .id(UUID.randomUUID())
                .currentSegment("backend")
                .statusCode("completed")
                .nextSegment("frontend")
                .templateCodes("frontend-brief")
                .createdAt(OffsetDateTime.now())
                .build());
    }

    private void seedAgents() {
        OffsetDateTime now = OffsetDateTime.now();
        conceptAgentId = UUID.randomUUID();
        AgentEntity ingestion = AgentEntity.builder()
                .id(conceptAgentId)
                .roleKey("concept-director")
                .displayName("System Ingestion")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        agentRepository.save(ingestion);

        backendAgentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        AgentEntity backend = AgentEntity.builder()
                .id(backendAgentId)
                .roleKey("backend-implementer")
                .displayName("Backend Agent")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        agentRepository.save(backend);

        agentPreferenceRepository.save(AgentPreferenceEntity.builder()
                .agentId(backendAgentId)
                .roleKey("backend-implementer")
                .primarySegments("backend")
                .fallbackSegments("qa")
                .pickupStatuses("queued")
                .activeStatuses("in_progress")
                .acceptStatus("in_progress")
                .returnStatus("queued")
                .maxInProgressMinutes(30)
                .build());

        qaAgentId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        AgentEntity qa = AgentEntity.builder()
                .id(qaAgentId)
                .roleKey("qa-agent")
                .displayName("QA Agent")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        agentRepository.save(qa);
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : List.of(
                "activity_log",
                "queue_item_artifacts",
                "queue_item_templates",
                "queue_item_states",
                "queue_locks",
                "queue_items",
                "queues",
                "handoff_rules",
                "reference_templates",
                "agent_preferences",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private String agentRole(UUID agentId) {
        return agentRepository.findById(agentId)
                .map(AgentEntity::getRoleKey)
                .orElseThrow(() -> new IllegalStateException("Agent not found"));
    }
}

