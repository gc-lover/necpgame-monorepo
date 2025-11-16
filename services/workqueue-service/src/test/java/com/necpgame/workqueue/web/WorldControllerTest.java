package com.necpgame.workqueue.web;

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

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class WorldControllerTest {

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
    void visionManagerCreatesLocationAndEvent() throws Exception {
        UUID baseLocationId = createContent("location::alpha", "location");
        UUID linkTargetId = createContent("location::beta", "location");
        UUID spawnTarget = createContent("npc::spawn", "npc");

        Map<String, Object> locationPayload = buildLocationPayload("location::alpha", linkTargetId, spawnTarget);
        mockMvc.perform(post("/api/world/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(locationPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.spawnPoints[0].targetEntityId").value(spawnTarget.toString()));

        UUID rewardId = createContent("item::reward", "item");
        createContent("event::raid", "world_event");
        Map<String, Object> eventPayload = buildEventPayload("event::raid", baseLocationId, rewardId);
        mockMvc.perform(post("/api/world/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(eventPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requirements", hasSize(1)));
    }

    @Test
    void backendUpdatesLocation() throws Exception {
        UUID locationId = createContent("location::gamma", "location");
        UUID linkTarget = createContent("location::delta", "location");
        Map<String, Object> payload = buildLocationPayload("location::gamma", linkTarget, null);

        mockMvc.perform(post("/api/world/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        payload.put("dangerLevel", 7);
        mockMvc.perform(put("/api/world/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dangerLevel").value(7));

        mockMvc.perform(get("/api/world/locations/{id}", locationId)
                        .header("X-Agent-Role", "vision-manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links", hasSize(1)));
    }

    @Test
    void otherRoleCannotCreateEvent() throws Exception {
        createContent("event::denied", "world_event");
        Map<String, Object> payload = buildEventPayload("event::denied", null, null);

        mockMvc.perform(post("/api/world/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    private Map<String, Object> buildLocationPayload(String code, UUID linkTarget, UUID spawnTarget) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", code);
        payload.put("dangerLevel", 3);
        payload.put("recommendedLevelMin", 5);
        payload.put("recommendedLevelMax", 15);
        payload.put("populationEstimate", 2000);
        payload.put("coordinates", Map.of("x", 10, "y", 24));
        payload.put("metadata", Map.of("climate", "dry"));

        if (linkTarget != null) {
            Map<String, Object> link = new HashMap<>();
            link.put("toLocationId", linkTarget);
            link.put("linkType", "road");
            link.put("travelTimeMinutes", 30);
            link.put("metadata", Map.of("speed", "normal"));
            payload.put("links", List.of(link));
        } else {
            payload.put("links", List.of());
        }

        if (spawnTarget != null) {
            Map<String, Object> spawn = new HashMap<>();
            spawn.put("spawnType", "npc");
            spawn.put("targetEntityId", spawnTarget);
            spawn.put("respawnSeconds", 120);
            spawn.put("conditions", Map.of("time", "night"));
            spawn.put("metadata", Map.of("note", "rare"));
            payload.put("spawnPoints", List.of(spawn));
        } else {
            payload.put("spawnPoints", List.of());
        }
        return payload;
    }

    private Map<String, Object> buildEventPayload(String code, UUID locationId, UUID rewardId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", code);
        payload.put("difficultyTier", 2);
        payload.put("locationEntityId", locationId);
        payload.put("rewardEntityId", rewardId);
        payload.put("rewardDescription", "Epic loot");
        payload.put("recurrencePattern", Map.of("intervalHours", 6));
        payload.put("metadata", Map.of("threat", "medium"));
        payload.put("requirements", List.of(Map.of("payload", Map.of("powerScore", 300))));
        return payload;
    }

    private UUID createContent(String code, String typeCode) throws Exception {
        Map<String, Object> content = new HashMap<>();
        content.put("code", code);
        content.put("title", "Content " + code);
        content.put("summary", "Summary " + code);
        content.put("typeCode", typeCode);
        content.put("statusCode", "draft");
        content.put("visibilityCode", "internal");
        content.put("version", "2025.11");
        content.put("lastUpdated", OffsetDateTime.now());
        content.put("tags", List.of("tag"));
        content.put("topics", List.of("topic"));
        content.put("metadata", Map.of("scope", "test"));

        mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "concept-director")
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isCreated());
        return fetchContentId(code);
    }

    private UUID fetchContentId(String code) {
        return jdbcTemplate.queryForObject("select id from content_entities where code = ?", UUID.class, code);
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
                "world_event_requirements",
                "world_spawn_points",
                "world_location_links",
                "world_event_data",
                "world_location_data",
                "content_entities",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

