package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.DuplicateOrderHint;
import com.necpgame.worldservice.model.Error;
import com.necpgame.worldservice.model.FactionValidationResult;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.PlayerOrderValidationReport;
import com.necpgame.worldservice.model.PlayerOrderValidationRequest;
import com.necpgame.worldservice.model.TerritoryRestriction;
import com.necpgame.worldservice.model.ToxicityCheckResult;
import java.util.UUID;
import com.necpgame.worldservice.model.ValidatePlayerOrderContentRequest;
import com.necpgame.worldservice.model.ValidatePlayerOrderDuplicatesRequest;
import com.necpgame.worldservice.model.ValidatePlayerOrderFactionsRequest;
import com.necpgame.worldservice.model.ValidationHistoryItem;
import com.necpgame.worldservice.model.ZonePoliciesResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for WorldService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface WorldService {

    /**
     * GET /world/player-orders/validation/history/{orderId} : Получить историю проверок заказа
     *
     * @param orderId Уникальный идентификатор заказа игрока. (required)
     * @return ValidationHistoryItem
     */
    ValidationHistoryItem getPlayerOrderValidationHistory(UUID orderId);

    /**
     * GET /world/player-orders/zones/{zoneId}/restrictions : Получить ограничения для зоны
     *
     * @param zoneId Идентификатор зоны мира. (required)
     * @return TerritoryRestriction
     */
    TerritoryRestriction getZoneRestrictions(String zoneId);

    /**
     * GET /world/player-orders/validation/policies : Получить актуальные политики проверки
     *
     * @param locale Код локали для локализованных сообщений (RFC 5646). (optional)
     * @return ZonePoliciesResponse
     */
    ZonePoliciesResponse listPlayerOrderPolicies(String locale);

    /**
     * POST /world/player-orders/validation : Провести полную валидацию заказа
     * Выполняет все проверки и возвращает чеклист нарушений с рекомендациями.
     *
     * @param playerOrderValidationRequest  (required)
     * @param locale Код локали для локализованных сообщений (RFC 5646). (optional)
     * @return PlayerOrderValidationReport
     */
    PlayerOrderValidationReport validatePlayerOrder(PlayerOrderValidationRequest playerOrderValidationRequest, String locale);

    /**
     * POST /world/player-orders/validation/content : Проверить текст на токсичность
     *
     * @param validatePlayerOrderContentRequest  (required)
     * @return ToxicityCheckResult
     */
    ToxicityCheckResult validatePlayerOrderContent(ValidatePlayerOrderContentRequest validatePlayerOrderContentRequest);

    /**
     * POST /world/player-orders/validation/duplicates : Проверить дубликаты заказов
     *
     * @param validatePlayerOrderDuplicatesRequest  (required)
     * @return DuplicateOrderHint
     */
    DuplicateOrderHint validatePlayerOrderDuplicates(ValidatePlayerOrderDuplicatesRequest validatePlayerOrderDuplicatesRequest);

    /**
     * POST /world/player-orders/validation/factions : Проверить санкции и репутационные ограничения
     *
     * @param validatePlayerOrderFactionsRequest  (required)
     * @return FactionValidationResult
     */
    FactionValidationResult validatePlayerOrderFactions(ValidatePlayerOrderFactionsRequest validatePlayerOrderFactionsRequest);
}

