package com.necpgame.workqueue.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContentCommandControllerTest {

    private static final UUID CONCEPT_DIRECTOR_ID = UUID.fromString("00000000-0000-0000-0000-00000000010E");
    private static final UUID VISION_MANAGER_ID = UUID.fromString("00000000-0000-0000-0000-00000000010F");
    private static final UUID BACKEND_AGENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        cleanupDatabase();
        seedAgents();
    }

    @Test
    void conceptDirectorCanCreateContentEntity() throws Exception {
        Map<String, Object> payload = buildRequest("quest::cd-season3", "quest");

        mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "concept-director")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summary.code").value("quest::cd-season3"))
                .andExpect(jsonPath("$.summary.entityType.code").value("quest"))
                .andExpect(jsonPath("$.summary.status.code").value("draft"));
    }

    @Test
    void visionManagerCanUpdateExistingContent() throws Exception {
        Map<String, Object> payload = buildRequest("quest::vm-update", "quest");
        UUID entityId = createContent(payload);

        Map<String, Object> updatePayload = new HashMap<>(payload);
        updatePayload.put("title", "Updated Vision Quest");
        updatePayload.put("statusCode", "in_review");

        mockMvc.perform(put("/api/content/entities/{id}", entityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(updatePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary.title").value("Updated Vision Quest"))
                .andExpect(jsonPath("$.summary.status.code").value("in_review"));
    }

    @Test
    void backendAgentCannotAccessContentCommands() throws Exception {
        Map<String, Object> payload = buildRequest("quest::forbidden", "quest");

        mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    private UUID createContent(Map<String, Object> payload) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "concept-director")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        String id = node.path("summary").path("id").asText();
        assertThat(id).isNotBlank();
        return UUID.fromString(id);
    }

    private Map<String, Object> buildRequest(String code, String type) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", code);
        payload.put("title", "Season 3 High Level Quest");
        payload.put("summary", "Bootstrap Season 3 north arc");
        payload.put("typeCode", type);
        payload.put("statusCode", "draft");
        payload.put("visibilityCode", "public");
        payload.put("riskLevelCode", "low");
        payload.put("version", "2025.11");
        payload.put("lastUpdated", OffsetDateTime.now());
        payload.put("sourceDocument", "/api/reference/templates/knowledge-entry-template");
        payload.put("tags", List.of("season3", "quest"));
        payload.put("topics", List.of("lore", "roadmap"));
        payload.put("metadata", Map.of("pillar", "north-expansion"));
        return payload;
    }

    private void seedAgents() {
        OffsetDateTime now = OffsetDateTime.now();
        agentRepository.save(AgentEntity.builder()
                .id(CONCEPT_DIRECTOR_ID)
                .roleKey("concept-director")
                .displayName("Concept Director")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
        agentRepository.save(AgentEntity.builder()
                .id(VISION_MANAGER_ID)
                .roleKey("vision-manager")
                .displayName("Vision Manager")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
        agentRepository.save(AgentEntity.builder()
                .id(BACKEND_AGENT_ID)
                .roleKey("backend-implementer")
                .displayName("Backend Implementer")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : List.of(
                "entity_links",
                "entity_history",
                "entity_notes",
                "entity_localizations",
                "entity_attributes",
                "entity_sections",
                "content_entities",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}


