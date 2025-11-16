package com.necpgame.workqueue.web.dto.quest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record QuestCommandRequestDto(
        @NotBlank String contentCode,
        String categoryCode,
        String segment,
        String difficultyCode,
        @Min(0) Integer levelMin,
        @Min(0) Integer levelMax,
        @Min(0) Integer estimatedDurationMin,
        @Min(0) Integer estimatedDurationMax,
        @NotNull Boolean repeatable,
        @Min(1) Integer recommendedPlayers,
        UUID startNpcId,
        UUID endNpcId,
        UUID startLocationId,
        UUID endLocationId,
        Map<String, Object> prerequisites,
        Map<String, Object> metadata,
        @Valid List<StageDto> stages,
        @Valid List<RewardDto> rewards,
        @Valid List<BranchDto> branches,
        @Valid List<WorldEffectDto> worldEffects
) {
    public record StageDto(
            @NotNull Integer index,
            @NotBlank String title,
            String description,
            String objectiveTypeCode,
            UUID targetEntityId,
            UUID targetLocationEntityId,
            @NotNull Boolean optional,
            Map<String, Object> successConditions,
            Map<String, Object> failureConditions,
            Map<String, Object> metadata
    ) {
    }

    public record RewardDto(
            @NotBlank String typeCode,
            UUID rewardEntityId,
            Double amount,
            Map<String, Object> metadata
    ) {
    }

    public record BranchDto(
            @NotBlank String branchKey,
            @NotNull Integer fromStageIndex,
            Integer leadsToStageIndex,
            Map<String, Object> triggerConditions,
            String notes
    ) {
    }

    public record WorldEffectDto(
            @NotBlank String effectTypeCode,
            UUID targetEntityId,
            @NotNull Map<String, Object> payload,
            String notes
    ) {
    }
}


