package com.necpgame.sessionservice.service;

import com.necpgame.sessionservice.model.DiagnosticsRequest;
import com.necpgame.sessionservice.model.DiagnosticsResponse;
import com.necpgame.sessionservice.model.DisconnectEvent;
import com.necpgame.sessionservice.model.DisconnectRateMetrics;
import com.necpgame.sessionservice.model.Error;
import com.necpgame.sessionservice.model.ForceReconnectRequest;
import com.necpgame.sessionservice.model.IncidentAlertRequest;
import org.springframework.lang.Nullable;
import com.necpgame.sessionservice.model.ReconnectError;
import com.necpgame.sessionservice.model.ReconnectRequest;
import com.necpgame.sessionservice.model.ReconnectResponse;
import com.necpgame.sessionservice.model.ReconnectTokenRequest;
import com.necpgame.sessionservice.model.ReconnectTokenResponse;
import com.necpgame.sessionservice.model.SessionInstabilityRecord;
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
     * POST /technical/session-management/alerts/instability : Зарегистрировать массовый disconnect
     * Формирует alert для incident-service и realtime/voice сервисов.
     *
     * @param incidentAlertRequest  (required)
     * @return Void
     */
    Void technicalSessionManagementAlertsInstabilityPost(IncidentAlertRequest incidentAlertRequest);

    /**
     * GET /technical/session-management/events/realtime : SSE поток событий стабильности
     * Поставляет live события (&#x60;session.disconnect&#x60;, &#x60;session.reconnect&#x60;, &#x60;session.instability&#x60;).
     *
     * @return String
     */
    String technicalSessionManagementEventsRealtimeGet();

    /**
     * GET /technical/session-management/metrics/disconnect-rate : Метрики disconnect rate
     *
     * @param range  (optional, default to 1h)
     * @param region  (optional)
     * @param isp  (optional)
     * @return DisconnectRateMetrics
     */
    DisconnectRateMetrics technicalSessionManagementMetricsDisconnectRateGet(String range, String region, String isp);

    /**
     * GET /technical/session-management/monitoring/unstable : Нестабильные игроки/сессии
     *
     * @param range  (optional, default to 24h)
     * @param minDisconnects  (optional, default to 3)
     * @return SessionInstabilityRecord
     */
    SessionInstabilityRecord technicalSessionManagementMonitoringUnstableGet(String range, Integer minDisconnects);

    /**
     * POST /technical/session-management/reconnect : Выполнить reconnect
     * Восстанавливает состояние сессии при reconnect-токене (включая party/realtime binding).
     *
     * @param reconnectRequest  (required)
     * @return ReconnectResponse
     */
    ReconnectResponse technicalSessionManagementReconnectPost(ReconnectRequest reconnectRequest);

    /**
     * POST /technical/session-management/reconnect/token : Выдать reconnect-токен
     * Формирует токен для быстрого переподключения (окно 5 минут, максимум 3 попытки).
     *
     * @param reconnectTokenRequest  (required)
     * @return ReconnectTokenResponse
     */
    ReconnectTokenResponse technicalSessionManagementReconnectTokenPost(ReconnectTokenRequest reconnectTokenRequest);

    /**
     * POST /technical/session-management/sessions/{sessionId}/diagnostics : Запрос диагностики сессии
     *
     * @param sessionId UUID сессии (required)
     * @param diagnosticsRequest  (required)
     * @return DiagnosticsResponse
     */
    DiagnosticsResponse technicalSessionManagementSessionsSessionIdDiagnosticsPost(String sessionId, DiagnosticsRequest diagnosticsRequest);

    /**
     * GET /technical/session-management/sessions/{sessionId}/disconnects : История disconnect событий
     *
     * @param sessionId UUID сессии (required)
     * @return DisconnectEvent
     */
    DisconnectEvent technicalSessionManagementSessionsSessionIdDisconnectsGet(String sessionId);

    /**
     * POST /technical/session-management/sessions/{sessionId}/force-reconnect : Принудительное переподключение игрока
     *
     * @param sessionId UUID сессии (required)
     * @param forceReconnectRequest  (required)
     * @return Void
     */
    Void technicalSessionManagementSessionsSessionIdForceReconnectPost(String sessionId, ForceReconnectRequest forceReconnectRequest);
}

