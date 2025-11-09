package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.NpcScenariosApi;
import com.necpgame.backjava.model.ExecuteScenarioRequest;
import com.necpgame.backjava.model.ScenarioBlueprintCreateRequest;
import com.necpgame.backjava.model.ScenarioBlueprintDetailResponse;
import com.necpgame.backjava.model.ScenarioBlueprintListResponse;
import com.necpgame.backjava.model.ScenarioBlueprintPublishRequest;
import com.necpgame.backjava.model.ScenarioBlueprintUpdateRequest;
import com.necpgame.backjava.model.ScenarioCategory;
import com.necpgame.backjava.model.ScenarioExecutionResponse;
import com.necpgame.backjava.model.ScenarioInstanceListResponse;
import com.necpgame.backjava.model.ScenarioInstanceStatus;
import com.necpgame.backjava.service.NpcScenariosService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NpcScenariosController implements NpcScenariosApi {

    private final NpcScenariosService service;

    @Override
    public ResponseEntity<ScenarioBlueprintListResponse> listBlueprints(UUID ownerId,
                                                                        ScenarioCategory category,
                                                                        ScenarioInstanceStatus scenarioStatus,
                                                                        Boolean isPublic,
                                                                        String licenseTier,
                                                                        Integer page,
                                                                        Integer pageSize) {
        log.info("Listing NPC scenario blueprints");
        ScenarioBlueprintListResponse response = service.listBlueprints(ownerId, category, scenarioStatus, isPublic, licenseTier, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScenarioBlueprintDetailResponse> createBlueprint(@Valid ScenarioBlueprintCreateRequest request) {
        log.info("Creating NPC scenario blueprint");
        ScenarioBlueprintDetailResponse response = service.createBlueprint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ScenarioBlueprintDetailResponse> getBlueprint(UUID blueprintId) {
        log.info("Fetching NPC scenario blueprint {}", blueprintId);
        ScenarioBlueprintDetailResponse response = service.getBlueprint(blueprintId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScenarioBlueprintDetailResponse> updateBlueprint(UUID blueprintId, @Valid ScenarioBlueprintUpdateRequest request) {
        log.info("Updating NPC scenario blueprint {}", blueprintId);
        ScenarioBlueprintDetailResponse response = service.updateBlueprint(blueprintId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteBlueprint(UUID blueprintId) {
        log.info("Deleting NPC scenario blueprint {}", blueprintId);
        service.deleteBlueprint(blueprintId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ScenarioBlueprintDetailResponse> publishBlueprint(UUID blueprintId, @Valid ScenarioBlueprintPublishRequest request) {
        log.info("Updating publication status for NPC scenario blueprint {}", blueprintId);
        ScenarioBlueprintDetailResponse response = service.publishBlueprint(blueprintId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScenarioInstanceListResponse> listInstances(UUID blueprintId,
                                                                      ScenarioInstanceStatus scenarioStatus,
                                                                      Integer page,
                                                                      Integer pageSize) {
        log.info("Listing NPC scenario instances for blueprint {}", blueprintId);
        ScenarioInstanceListResponse response = service.listInstances(blueprintId, scenarioStatus, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ScenarioExecutionResponse> executeScenario(UUID npcId, @Valid ExecuteScenarioRequest request) {
        log.info("Executing NPC scenario {} for npc {}", request.getBlueprintId(), npcId);
        ScenarioExecutionResponse response = service.executeScenario(npcId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}


