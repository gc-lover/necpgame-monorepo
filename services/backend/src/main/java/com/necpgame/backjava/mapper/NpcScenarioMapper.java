package com.necpgame.backjava.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.NpcScenarioBlueprintEntity;
import com.necpgame.backjava.entity.NpcScenarioInstanceEntity;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.ScenarioBlueprintDetail;
import com.necpgame.backjava.model.ScenarioBlueprintDetailResponse;
import com.necpgame.backjava.model.ScenarioBlueprintListResponse;
import com.necpgame.backjava.model.ScenarioBlueprintSummary;
import com.necpgame.backjava.model.ScenarioCost;
import com.necpgame.backjava.model.ScenarioInstance;
import com.necpgame.backjava.model.ScenarioInstanceListResponse;
import com.necpgame.backjava.model.ScenarioReward;
import com.necpgame.backjava.model.ScenarioStep;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NpcScenarioMapper {

    private final ObjectMapper objectMapper;

    public ScenarioBlueprintSummary toSummary(NpcScenarioBlueprintEntity entity) {
        ScenarioBlueprintSummary summary = new ScenarioBlueprintSummary();
        summary.setId(entity.getId());
        summary.setName(entity.getName());
        summary.setDescription(entity.getDescription());
        summary.setAuthorId(entity.getAuthorId());
        summary.setVersion(entity.getVersion());
        summary.setCategory(entity.getCategory());
        summary.setRequiredRoles(readValue(entity.getRequiredRolesJson(), new TypeReference<List<String>>() {}, Collections::emptyList));
        summary.setParameters(readValue(entity.getParametersJson(), new TypeReference<Map<String, Object>>() {}, LinkedHashMap::new));
        summary.setIsPublic(entity.isPublic());
        summary.setIsVerified(entity.isVerified());
        summary.setPrice(entity.getPrice());
        if (entity.getCreatedAt() != null) {
            summary.setCreatedAt(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        if (entity.getUpdatedAt() != null) {
            summary.setUpdatedAt(entity.getUpdatedAt().atOffset(ZoneOffset.UTC));
        }
        return summary;
    }

    public ScenarioBlueprintDetailResponse toDetailResponse(NpcScenarioBlueprintEntity entity) {
        ScenarioBlueprintDetail detail = new ScenarioBlueprintDetail();
        detail.setSummary(toSummary(entity));
        detail.setSteps(readValue(entity.getStepsJson(), new TypeReference<List<ScenarioStep>>() {}, Collections::emptyList));
        detail.setConditions(readValue(entity.getConditionsJson(), new TypeReference<Map<String, Object>>() {}, LinkedHashMap::new));
        detail.setRewards(readValue(entity.getRewardsJson(), ScenarioReward.class, () -> null));
        detail.setCosts(readValue(entity.getCostsJson(), ScenarioCost.class, () -> null));
        detail.setAutomationHints(readValue(entity.getAutomationHintsJson(), new TypeReference<List<String>>() {}, Collections::emptyList));
        detail.setVerificationNotes(entity.getVerificationNotes());

        ScenarioBlueprintDetailResponse response = new ScenarioBlueprintDetailResponse();
        response.setData(detail);
        return response;
    }

    public ScenarioBlueprintListResponse toListResponse(Page<NpcScenarioBlueprintEntity> page) {
        ScenarioBlueprintListResponse response = new ScenarioBlueprintListResponse();
        response.setData(page.getContent().stream().map(this::toSummary).toList());
        PaginationMeta meta = new PaginationMeta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setTotal(Math.toIntExact(page.getTotalElements()));
        meta.setTotalPages(page.getTotalPages());
        meta.setHasNext(page.hasNext());
        meta.setHasPrev(page.hasPrevious());
        response.setMeta(meta);
        return response;
    }

    public ScenarioInstanceListResponse toInstanceListResponse(Page<NpcScenarioInstanceEntity> page) {
        ScenarioInstanceListResponse response = new ScenarioInstanceListResponse();
        response.setData(page.getContent().stream().map(this::toInstance).toList());
        PaginationMeta meta = new PaginationMeta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setTotal(Math.toIntExact(page.getTotalElements()));
        meta.setTotalPages(page.getTotalPages());
        meta.setHasNext(page.hasNext());
        meta.setHasPrev(page.hasPrevious());
        response.setMeta(meta);
        return response;
    }

    public ScenarioInstance toInstance(NpcScenarioInstanceEntity entity) {
        ScenarioInstance instance = new ScenarioInstance();
        instance.setId(entity.getId());
        instance.setBlueprintId(entity.getBlueprintId());
        instance.setNpcId(entity.getNpcId());
        instance.setOwnerId(entity.getOwnerId());
        instance.setStatus(entity.getStatus());
        instance.setCurrentStep(entity.getCurrentStep());
        instance.setParameters(readValue(entity.getParametersJson(), new TypeReference<Map<String, Object>>() {}, LinkedHashMap::new));
        instance.setKpi(readValue(entity.getKpiJson(), com.necpgame.backjava.model.ScenarioKPI.class, () -> null));
        instance.setResult(readValue(entity.getResultJson(), new TypeReference<Map<String, Object>>() {}, LinkedHashMap::new));
        if (entity.getStartedAt() != null) {
            instance.setStartedAt(entity.getStartedAt().atOffset(ZoneOffset.UTC));
        }
        if (entity.getCompletedAt() != null) {
            instance.setCompletedAt(entity.getCompletedAt().atOffset(ZoneOffset.UTC));
        }
        if (entity.getScheduledAt() != null) {
            instance.setScheduledAt(entity.getScheduledAt().atOffset(ZoneOffset.UTC));
        }
        instance.setDuration(entity.getDuration());
        return instance;
    }

    public String writeValue(Object source) {
        if (source == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize NPC scenario payload", ex);
        }
    }

    private <T> T readValue(String source, Class<T> type, Supplier<T> fallback) {
        if (source == null || source.isBlank()) {
            return fallback.get();
        }
        try {
            return objectMapper.readValue(source, type);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse NPC scenario payload", ex);
        }
    }

    private <T> T readValue(String source, TypeReference<T> type, Supplier<T> fallback) {
        if (source == null || source.isBlank()) {
            return fallback.get();
        }
        try {
            return objectMapper.readValue(source, type);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse NPC scenario payload", ex);
        }
    }
}


