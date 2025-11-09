package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.NpcScenarioBlueprintEntity;
import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.mapper.NpcScenarioMapper;
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
import com.necpgame.backjava.repository.NpcScenarioBlueprintRepository;
import com.necpgame.backjava.repository.NpcScenarioInstanceRepository;
import com.necpgame.backjava.repository.spec.NpcScenarioBlueprintSpecifications;
import com.necpgame.backjava.repository.spec.NpcScenarioInstanceSpecifications;
import com.necpgame.backjava.service.NpcScenariosService;
import java.time.ZoneOffset;
import java.util.EnumSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor

@Transactional
public class NpcScenariosServiceImpl implements NpcScenariosService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final NpcScenarioBlueprintRepository blueprintRepository;
    private final NpcScenarioInstanceRepository instanceRepository;
    private final NpcScenarioMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ScenarioBlueprintListResponse listBlueprints(UUID ownerId,
                                                        ScenarioCategory category,
                                                        ScenarioInstanceStatus scenarioStatus,
                                                        Boolean isPublic,
                                                        String licenseTier,
                                                        Integer page,
                                                        Integer pageSize) {
        Pageable pageable = buildPageable(page, pageSize);
        Specification<NpcScenarioBlueprintEntity> specification = Specification.<NpcScenarioBlueprintEntity>where(null);

        if (ownerId != null) {
            specification = specification.and(NpcScenarioBlueprintSpecifications.hasOwner(ownerId));
        }
        if (category != null) {
            specification = specification.and(NpcScenarioBlueprintSpecifications.hasCategory(category));
        }
        if (isPublic != null) {
            specification = specification.and(NpcScenarioBlueprintSpecifications.hasPublicFlag(isPublic));
        }
        if (licenseTier != null && !licenseTier.isBlank()) {
            specification = specification.and(
                NpcScenarioBlueprintSpecifications.hasLicenseTier(parseLicenseTier(licenseTier))
            );
        }
        if (scenarioStatus != null) {
            specification = specification.and(NpcScenarioBlueprintSpecifications.hasInstanceWithStatus(scenarioStatus));
        }

        Page<NpcScenarioBlueprintEntity> result = blueprintRepository.findAll(specification, pageable);
        return mapper.toListResponse(result);
    }

    @Override
    public ScenarioBlueprintDetailResponse createBlueprint(ScenarioBlueprintCreateRequest request) {
        log.info("Creating NPC scenario blueprint: {}", request.getName());
        validateStepsCount(request.getSteps().size());

        NpcScenarioBlueprintEntity entity = new NpcScenarioBlueprintEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setAuthorId(request.getAuthorId());
        entity.setOwnerId(request.getAuthorId());
        entity.setVersion(request.getVersion());
        entity.setCategory(request.getCategory());
        entity.setRequiredRolesJson(mapper.writeValue(request.getRequiredRoles()));
        entity.setParametersJson(mapper.writeValue(request.getParameters()));
        entity.setConditionsJson(mapper.writeValue(request.getConditions()));
        entity.setStepsJson(mapper.writeValue(request.getSteps()));
        entity.setRewardsJson(mapper.writeValue(request.getRewards()));
        entity.setCostsJson(mapper.writeValue(request.getCosts()));
        entity.setAutomationHintsJson(mapper.writeValue(null));
        entity.setVerificationNotes(null);
        entity.setPublic(Boolean.TRUE.equals(request.getIsPublic()));
        entity.setVerified(false);
        entity.setPrice(request.getPrice());

        NpcScenarioBlueprintEntity saved = blueprintRepository.save(entity);
        return mapper.toDetailResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioBlueprintDetailResponse getBlueprint(UUID blueprintId) {
        NpcScenarioBlueprintEntity entity = fetchBlueprint(blueprintId);
        return mapper.toDetailResponse(entity);
    }

    @Override
    public ScenarioBlueprintDetailResponse updateBlueprint(UUID blueprintId, ScenarioBlueprintUpdateRequest request) {
        log.info("Updating NPC scenario blueprint {}", blueprintId);
        NpcScenarioBlueprintEntity entity = fetchBlueprint(blueprintId);

        apply(request.getName(), entity::setName);
        apply(request.getDescription(), entity::setDescription);
        apply(request.getVersion(), entity::setVersion);

        if (request.getRequiredRoles().isPresent()) {
            var roles = request.getRequiredRoles().get();
            if (roles == null || roles.isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_INPUT, "requiredRoles must contain at least one role");
            }
            entity.setRequiredRolesJson(mapper.writeValue(roles));
        }
        if (request.getParameters().isPresent()) {
            entity.setParametersJson(mapper.writeValue(request.getParameters().get()));
        }
        if (request.getConditions().isPresent()) {
            entity.setConditionsJson(mapper.writeValue(request.getConditions().get()));
        }
        if (request.getSteps().isPresent()) {
            var steps = request.getSteps().get();
            validateStepsCount(steps == null ? 0 : steps.size());
            entity.setStepsJson(mapper.writeValue(steps));
        }
        if (request.getRewards().isPresent()) {
            entity.setRewardsJson(mapper.writeValue(request.getRewards().get()));
        }
        if (request.getCosts().isPresent()) {
            entity.setCostsJson(mapper.writeValue(request.getCosts().get()));
        }
        if (request.getPrice().isPresent()) {
            entity.setPrice(request.getPrice().get());
        }
        if (request.getIsPublic().isPresent()) {
            entity.setPublic(Boolean.TRUE.equals(request.getIsPublic().get()));
        }
        if (request.getIsVerified().isPresent()) {
            entity.setVerified(Boolean.TRUE.equals(request.getIsVerified().get()));
        }

        return mapper.toDetailResponse(entity);
    }

    @Override
    public void deleteBlueprint(UUID blueprintId) {
        log.info("Deleting NPC scenario blueprint {}", blueprintId);
        NpcScenarioBlueprintEntity entity = fetchBlueprint(blueprintId);
        boolean hasActiveInstances = instanceRepository.existsByBlueprintIdAndStatusIn(
            entity.getId(),
            EnumSet.of(ScenarioInstanceStatus.PENDING, ScenarioInstanceStatus.RUNNING, ScenarioInstanceStatus.PAUSED)
        );
        if (hasActiveInstances) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "Blueprint has active instances");
        }
        blueprintRepository.delete(entity);
    }

    @Override
    public ScenarioBlueprintDetailResponse publishBlueprint(UUID blueprintId, ScenarioBlueprintPublishRequest request) {
        log.info("Updating publication state for NPC scenario blueprint {}", blueprintId);
        NpcScenarioBlueprintEntity entity = fetchBlueprint(blueprintId);
        boolean publish = Boolean.TRUE.equals(request.getPublish());
        entity.setPublic(publish);

        if (request.getPrice() != null) {
            entity.setPrice(request.getPrice());
        }

        if (request.getVisibilityScope() != null) {
            entity.setVisibilityScope(NpcScenarioBlueprintEntity.VisibilityScope.valueOf(request.getVisibilityScope().name()));
        } else if (publish && entity.getVisibilityScope() == null) {
            entity.setVisibilityScope(NpcScenarioBlueprintEntity.VisibilityScope.MARKETPLACE);
        }

        return mapper.toDetailResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioInstanceListResponse listInstances(UUID blueprintId,
                                                      ScenarioInstanceStatus scenarioStatus,
                                                      Integer page,
                                                      Integer pageSize) {
        fetchBlueprint(blueprintId);
        Pageable pageable = buildPageable(page, pageSize);
        Specification<NpcScenarioInstanceEntity> specification = Specification.<NpcScenarioInstanceEntity>where(
            NpcScenarioInstanceSpecifications.belongsToBlueprint(blueprintId)
        );
        if (scenarioStatus != null) {
            specification = specification.and(NpcScenarioInstanceSpecifications.hasStatus(scenarioStatus));
        }
        Page<NpcScenarioInstanceEntity> result = instanceRepository.findAll(specification, pageable);
        return mapper.toInstanceListResponse(result);
    }

    @Override
    public ScenarioExecutionResponse executeScenario(UUID npcId, ExecuteScenarioRequest request) {
        if (request.getBlueprintId() == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "blueprintId is required");
        }

        NpcScenarioBlueprintEntity blueprint = fetchBlueprint(request.getBlueprintId());
        NpcScenarioInstanceEntity instance = new NpcScenarioInstanceEntity();
        instance.setBlueprintId(blueprint.getId());
        instance.setNpcId(npcId);
        instance.setOwnerId(blueprint.getOwnerId());
        instance.setStatus(ScenarioInstanceStatus.PENDING);
        instance.setCurrentStep(0);
        instance.setParametersJson(mapper.writeValue(request.getParameters()));
        if (request.getScheduledAt() != null) {
            instance.setScheduledAt(request.getScheduledAt().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
            instance.setStatus(ScenarioInstanceStatus.PENDING);
        }
        instance.setPriority(request.getPriority());
        instance.setAutomationRuleId(request.getAutomationRuleId());

        NpcScenarioInstanceEntity saved = instanceRepository.save(instance);

        ScenarioExecutionResponse response = new ScenarioExecutionResponse();
        response.setInstanceId(saved.getId());
        response.setStatus(request.getScheduledAt() != null ? ScenarioExecutionResponse.ExecutionStatus.SCHEDULED : ScenarioExecutionResponse.ExecutionStatus.PENDING);
        if (saved.getScheduledAt() != null) {
            response.setScheduledAt(saved.getScheduledAt().atOffset(ZoneOffset.UTC));
        }
        response.setQueuePosition(0);
        return response;
    }

    private Pageable buildPageable(Integer page, Integer pageSize) {
        int pageNumber = page == null || page < 1 ? DEFAULT_PAGE : page;
        int size = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        return PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private NpcScenarioBlueprintEntity fetchBlueprint(UUID blueprintId) {
        return blueprintRepository.findById(blueprintId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "NPC scenario blueprint not found: " + blueprintId));
    }

    private void validateStepsCount(int count) {
        if (count < 1) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Scenario blueprint requires at least one step");
        }
    }

    private NpcScenarioBlueprintEntity.LicenseTier parseLicenseTier(String raw) {
        try {
            return NpcScenarioBlueprintEntity.LicenseTier.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ErrorCode.INVALID_FORMAT, "Unsupported license tier: " + raw);
        }
    }

    private <T> void apply(JsonNullable<T> source, java.util.function.Consumer<T> consumer) {
        if (source != null && source.isPresent()) {
            consumer.accept(source.orElse(null));
        }
    }
}


