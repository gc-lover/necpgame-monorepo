package com.necpgame.adminservice.service;

import com.necpgame.adminservice.model.AlertAck;
import com.necpgame.adminservice.model.AlertsResponse;
import com.necpgame.adminservice.model.AnalyticsError;
import com.necpgame.adminservice.model.AutotuneRequest;
import com.necpgame.adminservice.model.AutotuneResult;
import com.necpgame.adminservice.model.Error;
import com.necpgame.adminservice.model.MetricDetail;
import com.necpgame.adminservice.model.MetricsResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for AnalyticsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface AnalyticsService {

    /**
     * POST /analytics/factions/alerts/ack : Подтвердить или закрыть алерт
     *
     * @param alertAck  (required)
     * @param xSandboxMode Укажите &#x60;true&#x60;, чтобы задействовать sandbox-профиль для auto-tune/алертов. (optional, default to false)
     * @return Void
     */
    Void acknowledgeFactionAlert(AlertAck alertAck, String xSandboxMode);

    /**
     * POST /analytics/factions/autotune : Применить авто-тюнинг параметров
     *
     * @param autotuneRequest  (required)
     * @param xSandboxMode Укажите &#x60;true&#x60;, чтобы задействовать sandbox-профиль для auto-tune/алертов. (optional, default to false)
     * @return AutotuneResult
     */
    AutotuneResult applyFactionAutotune(AutotuneRequest autotuneRequest, String xSandboxMode);

    /**
     * GET /analytics/factions/metrics/{metricId} : Получить детализацию метрики
     *
     * @param metricId Идентификатор метрики или дэшбордного индикатора. (required)
     * @param factionId Идентификатор фракции или агрегирующей группы. (optional)
     * @param period Период агрегации метрик. (optional)
     * @param sandbox Укажите &#x60;true&#x60;, чтобы выполнить запрос в sandbox-режиме. (optional, default to false)
     * @return MetricDetail
     */
    MetricDetail getFactionMetricDetail(String metricId, String factionId, String period, Boolean sandbox);

    /**
     * GET /analytics/factions/alerts : Получить активные алерты
     *
     * @param alertId  (optional)
     * @param factionId Идентификатор фракции или агрегирующей группы. (optional)
     * @param sandbox Укажите &#x60;true&#x60;, чтобы выполнить запрос в sandbox-режиме. (optional, default to false)
     * @return AlertsResponse
     */
    AlertsResponse listFactionAlerts(String alertId, String factionId, Boolean sandbox);

    /**
     * GET /analytics/factions/metrics : Получить агрегированные метрики фракций
     *
     * @param factionId Идентификатор фракции или агрегирующей группы. (optional)
     * @param metric Фильтр по ключу метрики. (optional)
     * @param period Период агрегации метрик. (optional)
     * @param sandbox Укажите &#x60;true&#x60;, чтобы выполнить запрос в sandbox-режиме. (optional, default to false)
     * @param xSandboxMode Укажите &#x60;true&#x60;, чтобы задействовать sandbox-профиль для auto-tune/алертов. (optional, default to false)
     * @return MetricsResponse
     */
    MetricsResponse listFactionMetrics(String factionId, String metric, String period, Boolean sandbox, String xSandboxMode);
}

