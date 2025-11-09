package com.necpgame.backjava.api;

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface NpcScenariosApi {

    String BASE_PATH = "/gameplay/social/personal-npc-scenarios";
    String EXECUTE_PATH = "/gameplay/social/personal-npcs/{npc_id}/execute-scenario";

    @GetMapping(BASE_PATH)
    ResponseEntity<ScenarioBlueprintListResponse> listBlueprints(
        @RequestParam(name = "owner_id", required = false) UUID ownerId,
        @RequestParam(name = "category", required = false) ScenarioCategory category,
        @RequestParam(name = "scenario_status", required = false) ScenarioInstanceStatus scenarioStatus,
        @RequestParam(name = "is_public", required = false) Boolean isPublic,
        @RequestParam(name = "license_tier", required = false) String licenseTier,
        @RequestParam(name = "page", required = false) @Min(1) Integer page,
        @RequestParam(name = "page_size", required = false) @Min(1) Integer pageSize
    );

    @PostMapping(BASE_PATH)
    ResponseEntity<ScenarioBlueprintDetailResponse> createBlueprint(
        @Valid @RequestBody ScenarioBlueprintCreateRequest request
    );

    @GetMapping(BASE_PATH + "/{blueprint_id}")
    ResponseEntity<ScenarioBlueprintDetailResponse> getBlueprint(
        @PathVariable("blueprint_id") @NotNull UUID blueprintId
    );

    @PutMapping(BASE_PATH + "/{blueprint_id}")
    ResponseEntity<ScenarioBlueprintDetailResponse> updateBlueprint(
        @PathVariable("blueprint_id") @NotNull UUID blueprintId,
        @Valid @RequestBody ScenarioBlueprintUpdateRequest request
    );

    @DeleteMapping(BASE_PATH + "/{blueprint_id}")
    ResponseEntity<Void> deleteBlueprint(
        @PathVariable("blueprint_id") @NotNull UUID blueprintId
    );

    @PostMapping(BASE_PATH + "/{blueprint_id}/publish")
    ResponseEntity<ScenarioBlueprintDetailResponse> publishBlueprint(
        @PathVariable("blueprint_id") @NotNull UUID blueprintId,
        @Valid @RequestBody ScenarioBlueprintPublishRequest request
    );

    @GetMapping(BASE_PATH + "/{blueprint_id}/instances")
    ResponseEntity<ScenarioInstanceListResponse> listInstances(
        @PathVariable("blueprint_id") @NotNull UUID blueprintId,
        @RequestParam(name = "scenario_status", required = false) ScenarioInstanceStatus scenarioStatus,
        @RequestParam(name = "page", required = false) @Min(1) Integer page,
        @RequestParam(name = "page_size", required = false) @Min(1) Integer pageSize
    );

    @PostMapping(EXECUTE_PATH)
    ResponseEntity<ScenarioExecutionResponse> executeScenario(
        @PathVariable("npc_id") @NotNull UUID npcId,
        @Valid @RequestBody ExecuteScenarioRequest request
    );
}


