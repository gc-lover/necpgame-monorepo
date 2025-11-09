package com.necpgame.backjava.service;

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
import java.util.UUID;

public interface NpcScenariosService {

    ScenarioBlueprintListResponse listBlueprints(UUID ownerId,
                                                 ScenarioCategory category,
                                                 ScenarioInstanceStatus scenarioStatus,
                                                 Boolean isPublic,
                                                 String licenseTier,
                                                 Integer page,
                                                 Integer pageSize);

    ScenarioBlueprintDetailResponse createBlueprint(ScenarioBlueprintCreateRequest request);

    ScenarioBlueprintDetailResponse getBlueprint(UUID blueprintId);

    ScenarioBlueprintDetailResponse updateBlueprint(UUID blueprintId, ScenarioBlueprintUpdateRequest request);

    void deleteBlueprint(UUID blueprintId);

    ScenarioBlueprintDetailResponse publishBlueprint(UUID blueprintId, ScenarioBlueprintPublishRequest request);

    ScenarioInstanceListResponse listInstances(UUID blueprintId,
                                               ScenarioInstanceStatus scenarioStatus,
                                               Integer page,
                                               Integer pageSize);

    ScenarioExecutionResponse executeScenario(UUID npcId, ExecuteScenarioRequest request);
}


