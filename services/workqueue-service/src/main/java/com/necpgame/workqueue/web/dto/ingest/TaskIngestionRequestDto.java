package com.necpgame.workqueue.web.dto.ingest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record TaskIngestionRequestDto(
        @NotBlank String sourceId,
        @NotBlank String segment,
        @NotBlank String initialStatus,
        @NotNull @Min(0) @Max(5) Integer priority,
        @NotBlank String title,
        @NotBlank String summary,
        @NotEmpty List<@NotBlank String> knowledgeRefs,
        @Valid TemplatesDto templates,
        Map<String, Object> payload,
        @NotNull @Valid HandoffPlanDto handoffPlan
) {

    public record TemplatesDto(
            List<@NotBlank String> primary,
            List<@NotBlank String> checklists,
            List<@Valid TemplateReferenceDto> references
    ) {
    }

    public record TemplateReferenceDto(
            @NotBlank String code,
            String version,
            @NotBlank String path
    ) {
    }

    public record HandoffPlanDto(
            @NotBlank String nextSegment,
            List<@Valid HandoffConditionDto> conditions,
            String notes
    ) {
    }

    public record HandoffConditionDto(
            @NotBlank String status,
            @NotBlank String targetSegment
    ) {
    }
}

