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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class ItemControllerTest {
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
    void visionManagerCreatesItem() throws Exception {
        createContent("item::alpha");
        Map<String, Object> payload = buildItemPayload("item::alpha");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summary.code").value("item::alpha"))
                .andExpect(jsonPath("$.rarity.code").value("common"))
                .andExpect(jsonPath("$.weapon.damageMax").value(155.5));

        Integer count = jdbcTemplate.queryForObject("select count(*) from item_data", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void backendUpdatesItem() throws Exception {
        createContent("item::beta");
        Map<String, Object> payload = buildItemPayload("item::beta");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "vision-manager")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        payload.put("powerScore", new BigDecimal("512.0"));
        payload.put("tradeable", true);

        mockMvc.perform(put("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.powerScore").value(512.0))
                .andExpect(jsonPath("$.tradeable").value(true));

        mockMvc.perform(get("/api/items/{id}", fetchContentId("item::beta"))
                        .header("X-Agent-Role", "vision-manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modSlots[0].slotCode").value("scope"));
    }

    @Test
    void otherRoleCannotCreate() throws Exception {
        createContent("item::gamma");
        Map<String, Object> payload = buildItemPayload("item::gamma");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "backend-implementer")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    private void createContent(String code) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("code", code);
        request.put("title", "Item " + code);
        request.put("summary", "Summary " + code);
        request.put("typeCode", "item");
        request.put("statusCode", "draft");
        request.put("visibilityCode", "internal");
        request.put("version", "2025.11");
        request.put("lastUpdated", OffsetDateTime.now());
        request.put("tags", List.of("gear"));
        request.put("topics", List.of("combat"));
        request.put("metadata", Map.of("pipeline", "items"));

        mockMvc.perform(post("/api/content/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Agent-Role", "concept-director")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    private Map<String, Object> buildItemPayload(String contentCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", contentCode);
        payload.put("rarityCode", "common");
        payload.put("bindTypeCode", "bind_on_pickup");
        payload.put("categoryCode", "weapon");
        payload.put("slotCode", "hands");
        payload.put("weight", new BigDecimal("4.75"));
        payload.put("levelRequirement", 12);
        payload.put("stackSize", 1);
        payload.put("vendorPrice", new BigDecimal("1500"));
        payload.put("durabilityMax", 140);
        payload.put("powerScore", new BigDecimal("320.5"));
        payload.put("tradeable", false);
        payload.put("metadata", Map.of("family", "prototype"));

        Map<String, Object> weapon = new HashMap<>();
        weapon.put("weaponClassCode", "rifle");
        weapon.put("damageTypeCode", "kinetic");
        weapon.put("damageMin", new BigDecimal("120.0"));
        weapon.put("damageMax", new BigDecimal("155.5"));
        weapon.put("fireRate", new BigDecimal("4.5"));
        weapon.put("magazineSize", 30);
        weapon.put("reloadTimeSeconds", new BigDecimal("2.4"));
        weapon.put("rangeMin", new BigDecimal("10"));
        weapon.put("rangeMax", new BigDecimal("70"));
        weapon.put("criticalChance", new BigDecimal("0.25"));
        weapon.put("criticalMultiplier", new BigDecimal("1.75"));
        weapon.put("accuracy", new BigDecimal("0.82"));
        weapon.put("recoil", new BigDecimal("0.35"));
        weapon.put("metadata", Map.of("burst", true));
        payload.put("weapon", weapon);

        payload.put("armor", null);
        payload.put("consumableEffects", List.of());

        Map<String, Object> slot = new HashMap<>();
        slot.put("slotCode", "scope");
        slot.put("capacity", 1);
        slot.put("metadata", Map.of("type", "optic"));
        payload.put("modSlots", List.of(slot));
        payload.put("componentRequirements", List.of());
        return payload;
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
                .displayName("Backend Agent")
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build());
    }

    private void cleanupDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : List.of(
                "item_component_requirements",
                "item_mod_slots",
                "consumable_effects",
                "armor_stats",
                "weapon_stats",
                "item_data",
                "content_entities",
                "agents"
        )) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

