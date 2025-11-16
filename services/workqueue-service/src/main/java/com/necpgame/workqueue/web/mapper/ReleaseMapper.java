package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.release.ReleaseRunEntity;
import com.necpgame.workqueue.domain.release.ReleaseRunStepEntity;
import com.necpgame.workqueue.domain.release.ReleaseRunValidationEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.release.ReleaseRunDto;
import com.necpgame.workqueue.web.dto.release.ReleaseRunListResponseDto;
import com.necpgame.workqueue.web.dto.release.ReleaseRunStepDto;
import com.necpgame.workqueue.web.dto.release.ReleaseRunValidationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReleaseMapper {
    private final ObjectMapper objectMapper;
    private final ContentMapper contentMapper;

    public ReleaseRunListResponseDto toListResponse(List<ReleaseRunEntity> runs) {
        List<ReleaseRunDto> dto = runs.stream().map(this::toDto).collect(Collectors.toList());
        return new ReleaseRunListResponseDto(dto);
    }

    public ReleaseRunDto toDto(ReleaseRunEntity run) {
        List<ReleaseRunStepDto> steps = run.getSteps().stream()
                .map(this::toStep)
                .collect(Collectors.toList());
        List<ReleaseRunValidationDto> validations = run.getValidations().stream()
                .map(this::toValidation)
                .collect(Collectors.toList());
        return new ReleaseRunDto(
                run.getId(),
                run.getChangeId(),
                run.getTitle(),
                run.getAuthor() != null ? run.getAuthor().getId() : null,
                run.getAuthor() != null ? run.getAuthor().getDisplayName() : null,
                run.getReleaseDate(),
                toEnum(run.getImpactLevel()),
                run.getSummary(),
                parseJson(run.getScopeDescriptionJson()),
                run.getRollbackPlan(),
                toEnum(run.getStatus()),
                run.getCreatedAt(),
                run.getUpdatedAt(),
                steps,
                validations
        );
    }

    private ReleaseRunStepDto toStep(ReleaseRunStepEntity step) {
        return new ReleaseRunStepDto(
                step.getId(),
                step.getSortOrder(),
                step.getOwnerRole(),
                step.getActionDescription(),
                step.getDueDate(),
                toEnum(step.getStatus()),
                step.getCompletedAt(),
                parseJson(step.getMetadataJson())
        );
    }

    private ReleaseRunValidationDto toValidation(ReleaseRunValidationEntity validation) {
        return new ReleaseRunValidationDto(
                validation.getId(),
                validation.getValidationType(),
                validation.getDescription(),
                toEnum(validation.getStatus()),
                validation.getValidatedBy() != null ? validation.getValidatedBy().getId() : null,
                validation.getValidatedBy() != null ? validation.getValidatedBy().getDisplayName() : null,
                validation.getValidatedAt(),
                parseJson(validation.getResultsJson())
        );
    }

    private EnumValueDto toEnum(EnumValueEntity value) {
        return contentMapper.toEnum(value);
    }

    private Object parseJson(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            Object parsed = objectMapper.readValue(json, Object.class);
            if (parsed instanceof Map<?, ?> map) {
                Map<String, Object> ordered = new LinkedHashMap<>();
                map.forEach((key, value) -> {
                    if (key instanceof String stringKey) {
                        ordered.put(stringKey, value);
                    }
                });
                return ordered;
            }
            return parsed;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


