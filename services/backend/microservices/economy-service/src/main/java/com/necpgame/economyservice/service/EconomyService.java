package com.necpgame.economyservice.service;

import com.necpgame.economyservice.model.Error;
import com.necpgame.economyservice.model.EscrowStatus;
import com.necpgame.economyservice.model.GetPlayerRiskProfile200Response;
import com.necpgame.economyservice.model.InsurancePlan;
import com.necpgame.economyservice.model.InsuranceQuoteRequest;
import com.necpgame.economyservice.model.InsuranceQuoteResponse;
import org.springframework.lang.Nullable;
import com.necpgame.economyservice.model.RiskAlertSubscription;
import com.necpgame.economyservice.model.RiskEvaluationJobRequest;
import com.necpgame.economyservice.model.RiskEvaluationJobResponse;
import com.necpgame.economyservice.model.RiskEvaluationRequest;
import com.necpgame.economyservice.model.RiskEvaluationResponse;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for EconomyService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface EconomyService {

    /**
     * DELETE /economy/player-orders/risk/alerts/{subscriptionId} : Отменить подписку на алерты
     * Удаляет подписку на оповещения по рискам.
     *
     * @param subscriptionId Идентификатор подписки на алерты риска. (required)
     * @return Void
     */
    Void deletePlayerOrderRiskAlert(UUID subscriptionId);

    /**
     * POST /economy/player-orders/risk/jobs : Запустить batch пересчёт
     * Ставит в очередь пересчёт риска для множества заказов, игроков или регионов.
     *
     * @param riskEvaluationJobRequest  (required)
     * @return RiskEvaluationJobResponse
     */
    RiskEvaluationJobResponse enqueuePlayerOrderRiskJob(RiskEvaluationJobRequest riskEvaluationJobRequest);

    /**
     * POST /economy/player-orders/risk/evaluate : Рассчитать риск заказа
     * Выполняет синхронный расчёт риск-профиля заказа и возвращает модификаторы.
     *
     * @param riskEvaluationRequest  (required)
     * @return RiskEvaluationResponse
     */
    RiskEvaluationResponse evaluatePlayerOrderRisk(RiskEvaluationRequest riskEvaluationRequest);

    /**
     * GET /economy/player-orders/risk/escrow/{orderId} : Статус escrow заказа
     * Возвращает статус escrow, суммы и условия освобождения.
     *
     * @param orderId Идентификатор заказа игрока. (required)
     * @return EscrowStatus
     */
    EscrowStatus getPlayerOrderEscrowStatus(UUID orderId);

    /**
     * GET /economy/player-orders/risk/{orderId} : Получить риск заказа
     * Возвращает последний рассчитанный риск заказа и связанные рекомендации.
     *
     * @param orderId Идентификатор заказа игрока. (required)
     * @return RiskEvaluationResponse
     */
    RiskEvaluationResponse getPlayerOrderRisk(UUID orderId);

    /**
     * GET /economy/player-orders/risk/jobs/{jobId} : Получить статус пересчёта
     * Возвращает состояние batch-задачи пересчёта риска.
     *
     * @param jobId Идентификатор batch-задачи пересчёта риска. (required)
     * @return RiskEvaluationJobResponse
     */
    RiskEvaluationJobResponse getPlayerOrderRiskJob(UUID jobId);

    /**
     * GET /economy/player-orders/risk/players/{playerId} : Risk profile участника
     * Возвращает агрегированную информацию о риске для игрока.
     *
     * @param playerId Идентификатор игрока (заказчика или исполнителя). (required)
     * @param period Период агрегации данных риска или страхования. (optional)
     * @param orderType Тип заказа для фильтрации риск-профиля. (optional)
     * @return GetPlayerRiskProfile200Response
     */
    GetPlayerRiskProfile200Response getPlayerRiskProfile(UUID playerId, String period, String orderType);

    /**
     * POST /economy/player-orders/risk/insurance/quote : Получить котировку страховки
     * Рассчитывает страховую премию и условия покрытия для заказа.
     *
     * @param insuranceQuoteRequest  (required)
     * @return InsuranceQuoteResponse
     */
    InsuranceQuoteResponse quotePlayerOrderInsurance(InsuranceQuoteRequest insuranceQuoteRequest);

    /**
     * POST /economy/player-orders/risk/alerts : Подписаться на алерты риска
     * Создаёт подписку на уведомления о достижении порогов риска.
     *
     * @param riskAlertSubscription  (required)
     * @return RiskAlertSubscription
     */
    RiskAlertSubscription subscribePlayerOrderRiskAlerts(RiskAlertSubscription riskAlertSubscription);

    /**
     * POST /economy/player-orders/risk/insurance/plans : Создать или обновить страховой план
     * Управляет страховыми продуктами для заказов.
     *
     * @param insurancePlan  (required)
     * @return InsurancePlan
     */
    InsurancePlan upsertPlayerOrderInsurancePlan(InsurancePlan insurancePlan);
}

