package com.necpgame.backjava.service;

import org.springframework.format.annotation.DateTimeFormat;
import com.necpgame.backjava.model.DistributionResult;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GuaranteedDistributionRequest;
import com.necpgame.backjava.model.LootConfigResponse;
import com.necpgame.backjava.model.LootEntryBatchRequest;
import com.necpgame.backjava.model.LootEventNotificationRequest;
import com.necpgame.backjava.model.LootGenerationError;
import com.necpgame.backjava.model.LootGenerationRequest;
import com.necpgame.backjava.model.LootGenerationResult;
import com.necpgame.backjava.model.LootHistoryResponse;
import com.necpgame.backjava.model.LootReleaseRequest;
import com.necpgame.backjava.model.LootReservationResponse;
import com.necpgame.backjava.model.LootReserveRequest;
import com.necpgame.backjava.model.LootRoll;
import com.necpgame.backjava.model.LootStatsResponse;
import com.necpgame.backjava.model.LootTableListResponse;
import com.necpgame.backjava.model.LootTableSummary;
import com.necpgame.backjava.model.LootTableUpsertRequest;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import com.necpgame.backjava.model.PersonalDistributionRequest;
import com.necpgame.backjava.model.PityTimerState;
import com.necpgame.backjava.model.PityTimerUpdateRequest;
import com.necpgame.backjava.model.RaidDistributionRequest;
import com.necpgame.backjava.model.RollStartRequest;
import com.necpgame.backjava.model.SharedDistributionRequest;
import com.necpgame.backjava.model.SimulationRequest;
import com.necpgame.backjava.model.SimulationResponse;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for LootService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface LootService {

    /**
     * GET /loot/config : Получить конфигурацию модификаторов лута
     *
     * @return LootConfigResponse
     */
    LootConfigResponse lootConfigGet();

    /**
     * POST /loot/distribution/guaranteed : Выдать гарантированную награду
     *
     * @param guaranteedDistributionRequest  (required)
     * @return Void
     */
    Void lootDistributionGuaranteedPost(GuaranteedDistributionRequest guaranteedDistributionRequest);

    /**
     * POST /loot/distribution/personal : Выдать персональный лут
     *
     * @param personalDistributionRequest  (required)
     * @return DistributionResult
     */
    DistributionResult lootDistributionPersonalPost(PersonalDistributionRequest personalDistributionRequest);

    /**
     * POST /loot/distribution/raid : Распределить рейдовый лут с гарантиями
     *
     * @param raidDistributionRequest  (required)
     * @return DistributionResult
     */
    DistributionResult lootDistributionRaidPost(RaidDistributionRequest raidDistributionRequest);

    /**
     * POST /loot/distribution/shared : Распределить общий лут
     *
     * @param sharedDistributionRequest  (required)
     * @return DistributionResult
     */
    DistributionResult lootDistributionSharedPost(SharedDistributionRequest sharedDistributionRequest);

    /**
     * POST /loot/events/notify : Отправить уведомление о событии лута
     *
     * @param lootEventNotificationRequest  (required)
     * @return Void
     */
    Void lootEventsNotifyPost(LootEventNotificationRequest lootEventNotificationRequest);

    /**
     * GET /loot/history : История полученного лута
     *
     * @param playerId  (optional)
     * @param sourceId  (optional)
     * @param distributionMode  (optional)
     * @param rarity  (optional)
     * @param from  (optional)
     * @param to  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return LootHistoryResponse
     */
    LootHistoryResponse lootHistoryGet(String playerId, String sourceId, String distributionMode, String rarity, OffsetDateTime from, OffsetDateTime to, Integer page, Integer pageSize);

    /**
     * POST /loot/pity : Управление счетчиками гарантированного дропа
     *
     * @param pityTimerUpdateRequest  (required)
     * @return PityTimerState
     */
    PityTimerState lootPityPost(PityTimerUpdateRequest pityTimerUpdateRequest);

    /**
     * POST /loot/release : Снять резерв с предмета
     *
     * @param lootReleaseRequest  (required)
     * @return Void
     */
    Void lootReleasePost(LootReleaseRequest lootReleaseRequest);

    /**
     * POST /loot/reserve : Зарезервировать предмет до выдачи
     *
     * @param lootReserveRequest  (required)
     * @return LootReservationResponse
     */
    LootReservationResponse lootReservePost(LootReserveRequest lootReserveRequest);

    /**
     * POST /loot/rolls : Создать сессию Need/Greed
     *
     * @param rollStartRequest  (required)
     * @return Void
     */
    Void lootRollsPost(RollStartRequest rollStartRequest);

    /**
     * GET /loot/rolls/{sessionId} : Получить состояние ролла
     *
     * @param sessionId Идентификатор сессии Need/Greed. (required)
     * @return LootRoll
     */
    LootRoll lootRollsSessionIdGet(UUID sessionId);

    /**
     * POST /loot/sources/{sourceId}/generate : Сгенерировать лут для источника
     *
     * @param sourceId Идентификатор источника лута. (required)
     * @param lootGenerationRequest  (required)
     * @return LootGenerationResult
     */
    LootGenerationResult lootSourcesSourceIdGeneratePost(String sourceId, LootGenerationRequest lootGenerationRequest);

    /**
     * POST /loot/sources/{sourceId}/simulate : Провести симуляцию таблицы дропа
     *
     * @param sourceId Идентификатор источника лута. (required)
     * @param simulationRequest  (required)
     * @return SimulationResponse
     */
    SimulationResponse lootSourcesSourceIdSimulatePost(String sourceId, SimulationRequest simulationRequest);

    /**
     * GET /loot/stats : Агрегированные метрики дропа
     *
     * @param tableId  (optional)
     * @param timeRange  (optional)
     * @return LootStatsResponse
     */
    LootStatsResponse lootStatsGet(String tableId, String timeRange);

    /**
     * GET /loot/tables : Получить список таблиц лута
     *
     * @param sourceType  (optional)
     * @param active  (optional)
     * @param rarityCurve  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return LootTableListResponse
     */
    LootTableListResponse lootTablesGet(String sourceType, Boolean active, String rarityCurve, Integer page, Integer pageSize);

    /**
     * POST /loot/tables : Создать или обновить таблицу лута
     *
     * @param lootTableUpsertRequest  (required)
     * @return LootTableSummary
     */
    LootTableSummary lootTablesPost(LootTableUpsertRequest lootTableUpsertRequest);

    /**
     * POST /loot/tables/{tableId}/entries : Массовые операции над записями таблицы
     *
     * @param tableId Идентификатор таблицы лута. (required)
     * @param lootEntryBatchRequest  (required)
     * @return Void
     */
    Void lootTablesTableIdEntriesPost(String tableId, LootEntryBatchRequest lootEntryBatchRequest);

    /**
     * GET /loot/tables/{tableId} : Получить конфигурацию таблицы
     *
     * @param tableId Идентификатор таблицы лута. (required)
     * @return LootTableUpsertRequest
     */
    LootTableUpsertRequest lootTablesTableIdGet(String tableId);
}

