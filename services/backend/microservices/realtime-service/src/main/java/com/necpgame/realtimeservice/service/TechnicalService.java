package com.necpgame.realtimeservice.service;

import com.necpgame.realtimeservice.model.Error;
import org.springframework.lang.Nullable;
import com.necpgame.realtimeservice.model.PlayerRelocationRequest;
import com.necpgame.realtimeservice.model.RealtimeAlertRequest;
import com.necpgame.realtimeservice.model.RealtimeInstance;
import com.necpgame.realtimeservice.model.RealtimeInstanceHeartbeatRequest;
import com.necpgame.realtimeservice.model.RealtimeInstanceMetricsRequest;
import com.necpgame.realtimeservice.model.RealtimeInstanceRegistrationRequest;
import com.necpgame.realtimeservice.model.RealtimeInstanceUpdateRequest;
import com.necpgame.realtimeservice.model.TechnicalRealtimeZonesZoneIdCellsGet200Response;
import com.necpgame.realtimeservice.model.Zone;
import com.necpgame.realtimeservice.model.ZoneCellSnapshot;
import com.necpgame.realtimeservice.model.ZoneEvacuationPlan;
import com.necpgame.realtimeservice.model.ZoneTransferRequest;
import com.necpgame.realtimeservice.model.ZoneUpdateRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TechnicalService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TechnicalService {

    /**
     * POST /technical/realtime/alerts : Зафиксировать SLA тревогу
     * Ops endpoint публикует событие в incident-service и Event Bus &#x60;realtime.alerts&#x60;.
     *
     * @param realtimeAlertRequest  (required)
     * @return Void
     */
    Void technicalRealtimeAlertsPost(RealtimeAlertRequest realtimeAlertRequest);

    /**
     * GET /technical/realtime/instances : Список realtime-инстансов
     *
     * @param status  (optional)
     * @param region  (optional)
     * @param tickRate  (optional)
     * @return RealtimeInstance
     */
    RealtimeInstance technicalRealtimeInstancesGet(String status, String region, Integer tickRate);

    /**
     * GET /technical/realtime/instances/{instanceId} : Получить информацию об инстансе
     *
     * @param instanceId UUID realtime-инстанса (required)
     * @return RealtimeInstance
     */
    RealtimeInstance technicalRealtimeInstancesInstanceIdGet(String instanceId);

    /**
     * POST /technical/realtime/instances/{instanceId}/heartbeat : Heartbeat realtime-инстанса
     * Каждые 5 секунд сообщает активность, tickDuration, предупреждения (&#x60;tickDurationMs&gt;50&#x60;).
     *
     * @param instanceId UUID realtime-инстанса (required)
     * @param realtimeInstanceHeartbeatRequest  (required)
     * @return Void
     */
    Void technicalRealtimeInstancesInstanceIdHeartbeatPost(String instanceId, RealtimeInstanceHeartbeatRequest realtimeInstanceHeartbeatRequest);

    /**
     * POST /technical/realtime/instances/{instanceId}/metrics : Push метрик инстанса
     *
     * @param instanceId UUID realtime-инстанса (required)
     * @param realtimeInstanceMetricsRequest  (required)
     * @return Void
     */
    Void technicalRealtimeInstancesInstanceIdMetricsPost(String instanceId, RealtimeInstanceMetricsRequest realtimeInstanceMetricsRequest);

    /**
     * PATCH /technical/realtime/instances/{instanceId} : Обновить параметры инстанса
     *
     * @param instanceId UUID realtime-инстанса (required)
     * @param realtimeInstanceUpdateRequest  (required)
     * @return RealtimeInstance
     */
    RealtimeInstance technicalRealtimeInstancesInstanceIdPatch(String instanceId, RealtimeInstanceUpdateRequest realtimeInstanceUpdateRequest);

    /**
     * POST /technical/realtime/instances : Зарегистрировать realtime-инстанс
     * Регистрация нового realtime-сервера (tickRate 20/30/60, maxPlayers ≤ 2000).
     *
     * @param realtimeInstanceRegistrationRequest  (required)
     * @return RealtimeInstance
     */
    RealtimeInstance technicalRealtimeInstancesPost(RealtimeInstanceRegistrationRequest realtimeInstanceRegistrationRequest);

    /**
     * POST /technical/realtime/players/{playerId}/relocate : Принудительно перенести игрока
     *
     * @param playerId UUID игрока (required)
     * @param playerRelocationRequest  (required)
     * @return Void
     */
    Void technicalRealtimePlayersPlayerIdRelocatePost(String playerId, PlayerRelocationRequest playerRelocationRequest);

    /**
     * GET /technical/realtime/zones : Список зон
     *
     * @param status  (optional)
     * @param assignedServerId  (optional)
     * @param isPvpEnabled  (optional)
     * @return Zone
     */
    Zone technicalRealtimeZonesGet(String status, String assignedServerId, Boolean isPvpEnabled);

    /**
     * POST /technical/realtime/zones/{zoneId}/cells/{cellKey}/snapshot : Получить snapshot клетки
     * Возвращает &#x60;CellPlayerState[]&#x60; для отладки interest management. Redis ключ &#x60;zone_cell:{zoneId}:{x}:{y}&#x60;.
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @param cellKey Координаты cell (x:y, шаг 100м) (required)
     * @return ZoneCellSnapshot
     */
    ZoneCellSnapshot technicalRealtimeZonesZoneIdCellsCellKeySnapshotPost(String zoneId, String cellKey);

    /**
     * GET /technical/realtime/zones/{zoneId}/cells : Сводка по cell в зоне
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @param limit  (optional, default to 100)
     * @param sort  (optional)
     * @return TechnicalRealtimeZonesZoneIdCellsGet200Response
     */
    TechnicalRealtimeZonesZoneIdCellsGet200Response technicalRealtimeZonesZoneIdCellsGet(String zoneId, Integer limit, String sort);

    /**
     * POST /technical/realtime/zones/{zoneId}/evacuate : Эвакуировать игроков из зоны
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @param zoneEvacuationPlan  (required)
     * @return Void
     */
    Void technicalRealtimeZonesZoneIdEvacuatePost(String zoneId, ZoneEvacuationPlan zoneEvacuationPlan);

    /**
     * GET /technical/realtime/zones/{zoneId} : Информация о зоне
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @return Zone
     */
    Zone technicalRealtimeZonesZoneIdGet(String zoneId);

    /**
     * PATCH /technical/realtime/zones/{zoneId} : Обновить параметры зоны
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @param zoneUpdateRequest  (required)
     * @return Zone
     */
    Zone technicalRealtimeZonesZoneIdPatch(String zoneId, ZoneUpdateRequest zoneUpdateRequest);

    /**
     * POST /technical/realtime/zones/{zoneId}/transfer : Запланировать перенос зоны
     * Запускает drain стратегию (batch evac + reassignment) с ETA.
     *
     * @param zoneId Идентификатор зоны (например &#x60;night-city.watson&#x60;) (required)
     * @param zoneTransferRequest  (required)
     * @return Void
     */
    Void technicalRealtimeZonesZoneIdTransferPost(String zoneId, ZoneTransferRequest zoneTransferRequest);
}

