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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuestControllerTest {

    private static final UUID CONCEPT_ID = UUID.fromString("00000000-0000-0000-0000-00000000010E");
    private static final UUID VISION_ID = UUID.fromString("00000000-0000-0000-0000-00000000010F");
    private static final UUID BACKEND_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

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
    void visionManagerCreatesQuest() throws Exception {
        createContent("quest::alpha");

        Map<String, Object> questPayload = buildQuestPayload("quest::alpha");

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(questPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summary.code").value("quest::alpha"))
                .andExpect(jsonPath("$.repeatable").value(false))
                .andExpect(jsonPath("$.stages[0].title").value("Первый этап"));

        Integer count = jdbcTemplate.queryForObject("select count(*) from quest_data", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void updateQuestReplacesStages() throws Exception {
        createContent("quest::beta");
        Map<String, Object> questPayload = buildQuestPayload("quest::beta");

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(questPayload)))
                .andExpect(status().isCreated());

        Map<String, Object> update = buildQuestPayload("quest::beta");
        List<Map<String, Object>> stages = (List<Map<String, Object>>) update.get("stages");
        stages.get(0).put("title", "Обновлённый этап");
        update.put("repeatable", true);

        mockMvc.perform(put("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repeatable").value(true))
                .andExpect(jsonPath("$.stages[0].title").value("Обновлённый этап"));
    }

    @Test
    void otherAgentRejected() throws Exception {
        createContent("quest::gamma");
        Map<String, Object> questPayload = buildQuestPayload("quest::gamma");

        mockMvc.perform(post("/api/quests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(questPayload)))
                .andExpect(status().isForbidden());
    }

    private void createContent(String code) throws Exception {
        Map<String, Object> content = new HashMap<>();
        content.put("code", code);
        content.put("title", "Quest Content " + code);
        content.put("summary", "Summary for " + code);
        content.put("typeCode", "quest");
        content.put("statusCode", "draft");
        content.put("visibilityCode", "public");
        content.put("version", "2025.11");
        content.put("lastUpdated", OffsetDateTime.now());
        content.put("repeatable", false);

        mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "concept-director")
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isCreated());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildQuestPayload(String contentCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", contentCode);
        payload.put("segment", "vision");
        payload.put("repeatable", false);
        payload.put("metadata", Map.of("pillar", "north"));
        payload.put("prerequisites", Map.of("minLevel", 10));

        Map<String, Object> stage = new HashMap<>();
        stage.put("index", 1);
        stage.put("title", "Первый этап");
        stage.put("description", "Подготовка");
        stage.put("optional", false);
        stage.put("metadata", Map.of());

        payload.put("stages", List.of(stage));
        payload.put("rewards", List.of());
        payload.put("branches", List.of());
        payload.put("worldEffects", List.of());
        return payload;
    }

    private void seedAgents() {
        OffsetDateTime now = OffsetDateTime.now();
        agentRepository.save(AgentEntity.builder()
                .id(CONCEPT_ID)
                .roleKey("concept-director")
                .displayName("Concept Director")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
        agentRepository.save(AgentEntity.builder()
                .id(VISION_ID)
                .roleKey("vision-manager")
                .displayName("Vision Manager")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
        agentRepository.save(AgentEntity.builder()
                .id(BACKEND_ID)
                .roleKey("backend-implementer")
                .displayName("Backend")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : List.of(
                "quest_world_effects",
                "quest_branches",
                "quest_rewards",
                "quest_stages",
                "quest_data",
                "content_entities",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}


