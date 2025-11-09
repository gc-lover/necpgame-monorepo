package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.ActionXpBatchRequest;
import com.necpgame.worldservice.model.ActionXpBatchResponse;
import com.necpgame.worldservice.model.ActionXpMetrics;
import com.necpgame.worldservice.model.ActionXpSummary;
import com.necpgame.worldservice.model.Error;
import com.necpgame.worldservice.model.FatigueResetRequest;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.SkillFatigue;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for ProgressionService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface ProgressionService {

    /**
     * GET /progression/action-xp/metrics : Получить метрики Action XP
     * Предоставляет агрегированные показатели Action XP по навыкам, типам активностей и усталости.
     *
     * @param window Окно агрегации метрик. (optional, default to 24h)
     * @return ActionXpMetrics
     */
    ActionXpMetrics getActionXpMetrics(String window);

    /**
     * GET /progression/action-xp/summary : Получить сводку Action XP
     * Возвращает дневные лимиты, fatigue modifier и прогресс soft cap для персонажа и навыка.
     *
     * @param characterId Идентификатор персонажа. (required)
     * @param skillId Идентификатор навыка (например, strength, stealth). (optional)
     * @return ActionXpSummary
     */
    ActionXpSummary getActionXpSummary(UUID characterId, String skillId);

    /**
     * POST /progression/action-xp : Батчевое начисление Action XP
     * Принимает пакет начислений Action XP с учётом множителей активности и индекса усталости.
     *
     * @param idempotencyKey Уникальный ключ идемпотентности для защиты от повторной обработки. (required)
     * @param xAuditId Идентификатор аудита для трассировки изменений. (required)
     * @param actionXpBatchRequest  (required)
     * @return ActionXpBatchResponse
     */
    ActionXpBatchResponse postActionXpBatch(String idempotencyKey, UUID xAuditId, ActionXpBatchRequest actionXpBatchRequest);

    /**
     * POST /progression/fatigue/reset : Сброс усталости по навыку
     * Сбрасывает усталость навыка с применением экономических предметов или административных полномочий.
     *
     * @param idempotencyKey Уникальный ключ идемпотентности для защиты от повторной обработки. (required)
     * @param xAuditId Идентификатор аудита для трассировки изменений. (required)
     * @param fatigueResetRequest  (required)
     * @return SkillFatigue
     */
    SkillFatigue resetFatigue(String idempotencyKey, UUID xAuditId, FatigueResetRequest fatigueResetRequest);
}

