package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.NpcScenarioBlueprintEntity;
import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.mapper.NpcScenarioMapper;
import com.necpgame.backjava.model.ExecuteScenarioRequest;
import com.necpgame.backjava.model.ScenarioBlueprintCreateRequest;
import com.necpgame.backjava.model.ScenarioBlueprintDetailResponse;
import com.necpgame.backjava.model.ScenarioCategory;
import com.necpgame.backjava.model.ScenarioExecutionResponse;
import com.necpgame.backjava.model.ScenarioStep;
import com.necpgame.backjava.repository.NpcScenarioBlueprintRepository;
import com.necpgame.backjava.repository.NpcScenarioInstanceRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NpcScenariosServiceImplTest {

    @Mock
    private NpcScenarioBlueprintRepository blueprintRepository;

    @Mock
    private NpcScenarioInstanceRepository instanceRepository;

    private NpcScenariosServiceImpl service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        NpcScenarioMapper mapper = new NpcScenarioMapper(objectMapper);
        service = new NpcScenariosServiceImpl(blueprintRepository, instanceRepository, mapper);
    }

    @Test
    void createBlueprint_shouldPersistEntityAndReturnDetail() throws Exception {
        ScenarioBlueprintCreateRequest request = new ScenarioBlueprintCreateRequest();
        request.setName("Courier Duty");
        request.setDescription("Deliver a package across the district");
        request.setAuthorId(UUID.randomUUID());
        request.setCategory(ScenarioCategory.SOCIAL);
        request.setRequiredRoles(List.of("courier"));
        request.setParameters(Map.of("difficulty", "medium"));
        request.setConditions(Collections.singletonMap("nightOnly", Boolean.TRUE));
        request.setSteps(List.of(step("step-1", 1)));
        request.setIsPublic(Boolean.TRUE);
        request.setPrice(null);

        when(blueprintRepository.save(any(NpcScenarioBlueprintEntity.class)))
            .thenAnswer(invocation -> {
                NpcScenarioBlueprintEntity entity = invocation.getArgument(0);
                entity.setId(UUID.randomUUID());
                return entity;
            });

        ScenarioBlueprintDetailResponse response = service.createBlueprint(request);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals("Courier Duty", response.getData().getSummary().getName());
        assertTrue(response.getData().getSummary().getIsPublic());
        assertEquals(1, response.getData().getSteps().size());

        ArgumentCaptor<NpcScenarioBlueprintEntity> captor = ArgumentCaptor.forClass(NpcScenarioBlueprintEntity.class);
        verify(blueprintRepository).save(captor.capture());
        NpcScenarioBlueprintEntity persisted = captor.getValue();
        assertEquals(request.getAuthorId(), persisted.getAuthorId());
        assertEquals(request.getName(), persisted.getName());
        Map<String, Object> storedParams = objectMapper.readValue(persisted.getParametersJson(), new TypeReference<>() {});
        assertEquals("medium", storedParams.get("difficulty"));
    }

    @Test
    void getBlueprint_shouldThrowWhenNotFound() {
        UUID blueprintId = UUID.randomUUID();
        when(blueprintRepository.findById(blueprintId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> service.getBlueprint(blueprintId));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void deleteBlueprint_shouldFailWhenActiveInstancesExist() {
        UUID blueprintId = UUID.randomUUID();
        NpcScenarioBlueprintEntity entity = new NpcScenarioBlueprintEntity();
        entity.setId(blueprintId);
        entity.setRequiredRolesJson("[]");
        entity.setStepsJson("[]");
        entity.setAutomationHintsJson("[]");
        when(blueprintRepository.findById(blueprintId)).thenReturn(Optional.of(entity));
        when(instanceRepository.existsByBlueprintIdAndStatusIn(eq(blueprintId), any())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.deleteBlueprint(blueprintId));
        assertEquals(ErrorCode.OPERATION_NOT_ALLOWED, ex.getErrorCode());
        verify(blueprintRepository, never()).delete(any(NpcScenarioBlueprintEntity.class));
    }

    @Test
    void executeScenario_shouldCreateInstanceAndReturnResponse() {
        UUID blueprintId = UUID.randomUUID();
        UUID npcId = UUID.randomUUID();
        NpcScenarioBlueprintEntity blueprint = new NpcScenarioBlueprintEntity();
        blueprint.setId(blueprintId);
        blueprint.setOwnerId(UUID.randomUUID());
        blueprint.setRequiredRolesJson("[]");
        blueprint.setStepsJson("[]");
        blueprint.setAutomationHintsJson("[]");
        when(blueprintRepository.findById(blueprintId)).thenReturn(Optional.of(blueprint));

        when(instanceRepository.save(any(NpcScenarioInstanceEntity.class)))
            .thenAnswer(invocation -> {
                NpcScenarioInstanceEntity entity = invocation.getArgument(0);
                entity.setId(UUID.randomUUID());
                entity.setScheduledAt(entity.getScheduledAt());
                return entity;
            });

        ExecuteScenarioRequest request = new ExecuteScenarioRequest();
        request.setBlueprintId(blueprintId);
        request.setParameters(Map.of("priority", "high"));
        request.setPriority(5);
        request.setScheduledAt(OffsetDateTime.now(ZoneOffset.UTC));
        request.setAutomationRuleId("rule-1");

        ScenarioExecutionResponse response = service.executeScenario(npcId, request);

        assertNotNull(response.getInstanceId());
        assertEquals(ScenarioExecutionResponse.ExecutionStatus.SCHEDULED, response.getStatus());
        assertEquals(0, response.getQueuePosition());

        ArgumentCaptor<NpcScenarioInstanceEntity> captor = ArgumentCaptor.forClass(NpcScenarioInstanceEntity.class);
        verify(instanceRepository).save(captor.capture());
        NpcScenarioInstanceEntity persisted = captor.getValue();
        assertEquals(npcId, persisted.getNpcId());
        assertEquals(blueprint.getOwnerId(), persisted.getOwnerId());
        assertEquals("rule-1", persisted.getAutomationRuleId());
    }

    private ScenarioStep step(String id, int order) {
        ScenarioStep step = new ScenarioStep();
        step.setId(id);
        step.setOrder(order);
        step.setType(ScenarioStep.StepType.ACTION);
        step.setAction("action-" + id);
        return step;
    }
}


