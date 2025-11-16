package com.necpgame.workqueue.web.dto.quest;

import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record QuestDetailDto(
        ContentSummaryDto summary,
        String segment,
        EnumValueDto category,
        EnumValueDto difficulty,
        Integer levelMin,
        Integer levelMax,
        Integer estimatedDurationMin,
        Integer estimatedDurationMax,
        boolean repeatable,
        Integer recommendedPlayers,
        UUID startNpcId,
        UUID endNpcId,
        UUID startLocationId,
        UUID endLocationId,
        Map<String, Object> prerequisites,
        Map<String, Object> metadata,
        List<QuestStageDetailDto> stages,
        List<QuestRewardDetailDto> rewards,
        List<QuestBranchDetailDto> branches,
        List<QuestWorldEffectDetailDto> worldEffects
) {
    public record QuestStageDetailDto(
            UUID id,
            Integer index,
            String title,
            String description,
            EnumValueDto objectiveType,
            UUID targetEntityId,
            UUID targetLocationEntityId,
            boolean optional,
            Map<String, Object> successConditions,
            Map<String, Object> failureConditions,
            Map<String, Object> metadata
    ) {
    }

    public record QuestRewardDetailDto(
            UUID id,
            EnumValueDto rewardType,
            UUID rewardEntityId,
            BigDecimal amount,
            Map<String, Object> metadata
    ) {
    }

    public record QuestBranchDetailDto(
            UUID id,
            String branchKey,
            Integer fromStageIndex,
            Integer leadsToStageIndex,
            Map<String, Object> triggerConditions,
            String notes
    ) {
    }

    public record QuestWorldEffectDetailDto(
            UUID id,
            EnumValueDto effectType,
            UUID targetEntityId,
            Map<String, Object> payload,
            String notes
    ) {
    }
}


