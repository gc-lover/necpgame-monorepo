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

import java.math.BigDecimal;
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
class NpcControllerTest {

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
    void visionManagerCreatesNpc() throws Exception {
        createContent("npc::alpha", "npc");
        UUID weaponId = createContent("item::vendor", "item");
        UUID dialogueId = createContent("dialogue::intro", "dialogue");
        UUID locationId = createContent("location::plaza", "location");

        Map<String, Object> payload = buildNpcPayload("npc::alpha", weaponId, dialogueId, locationId);

        mockMvc.perform(post("/api/npcs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventory", hasSize(1)))
                .andExpect(jsonPath("$.dialogueLinks[0].dialogueId").value(dialogueId.toString()));
    }

    @Test
    void backendUpdatesNpc() throws Exception {
        createContent("npc::bravo", "npc");
        UUID itemId = createContent("item::rare", "item");
        UUID dialogueId = createContent("dialogue::branch", "dialogue");
        UUID locationId = createContent("location::hub", "location");

        Map<String, Object> payload = buildNpcPayload("npc::bravo", itemId, dialogueId, locationId);
        mockMvc.perform(post("/api/npcs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        payload.put("roleTitle", "Commander");
        mockMvc.perform(put("/api/npcs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleTitle").value("Commander"));

        UUID npcId = fetchContentId("npc::bravo");
        mockMvc.perform(get("/api/npcs/{id}", npcId)
                        .header("X-Agent-Role", "vision-manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventory", hasSize(1)));
    }

    @Test
    void otherRoleCannotCreate() throws Exception {
        createContent("npc::gamma", "npc");
        UUID itemId = createContent("item::basic", "item");
        Map<String, Object> payload = buildNpcPayload("npc::gamma", itemId, null, null);

        mockMvc.perform(post("/api/npcs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    private Map<String, Object> buildNpcPayload(String contentCode, UUID itemId, UUID dialogueId, UUID locationId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", contentCode);
        payload.put("roleTitle", "Vendor");
        payload.put("level", 15);
        payload.put("powerScore", new BigDecimal("125.5"));
        payload.put("metadata", Map.of("fame", "high"));
        payload.put("vendorCatalog", Map.of("items", List.of("ammo")));
        payload.put("scheduleMetadata", Map.of("cycles", 2));
        payload.put("dialogueProfile", Map.of("tone", "friendly"));

        Map<String, Object> schedule = new HashMap<>();
        schedule.put("dayTimeRange", "08:00-16:00");
        schedule.put("locationEntityId", locationId);
        schedule.put("payload", Map.of("activity", "trade"));
        payload.put("schedule", List.of(schedule));

        Map<String, Object> inventory = new HashMap<>();
        inventory.put("itemEntityId", itemId);
        inventory.put("quantity", 4);
        inventory.put("restockIntervalMinutes", 120);
        inventory.put("metadata", Map.of("quality", "A"));
        payload.put("inventory", List.of(inventory));

        if (dialogueId != null) {
            Map<String, Object> dialogue = new HashMap<>();
            dialogue.put("dialogueEntityId", dialogueId);
            dialogue.put("priority", 10);
            dialogue.put("metadata", Map.of("branch", "intro"));
            dialogue.put("conditions", Map.of("reputation", "ally"));
            payload.put("dialogueLinks", List.of(dialogue));
        } else {
            payload.put("dialogueLinks", List.of());
        }
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
                "npc_dialogue_links",
                "npc_inventory_items",
                "npc_schedule_entries",
                "npc_data",
                "content_entities",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

