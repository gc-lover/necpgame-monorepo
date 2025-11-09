---
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 12:20  
**Приоритет:** high  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** Метрики аналитики и авто-тюнинг фракционных систем. WebSocket через gateway `wss://api.necp.game/v1/analytics/factions`.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 01:40

# Faction Analytics & Balance — Метрики и авто-тюнинг

**target-domain:** technical/analytics  
**target-microservice:** analytics-service (8090), world-service (8086)  
**target-frontend-module:** modules/analytics/dashboard  
**интеграции:** raids, contracts, economy, social-service

## 1. Цели
- Определить метрики для новых фракционных систем (очистка зон, трибуналы, климатические события, рейды, диалоги).
- Настроить параметры авто-тюнинга для сложности, наград и world flags.
- Обеспечить REST/WS контуры для dashboard и оркестрации изменений.

## 2. Ключевые метрики
| Метрика | Описание | Источник | Использование |
| --- | --- | --- | --- |
| contractSuccessRate | % успешных этапов цепочек | world-service contracts | Тюнинг порогов репутации |
| ranchPreferenceIndex | Выбор веток (escort/sabotage и т.д.) | social-service decisions | Планирование ивентов |
| 
aidClearTime | Среднее время завершения рейдов по сложности | raids telemetry | Авто-тюнинг HP/урона |
| coAssetVelocity | Скорость оборота фракционных активов | economy-service | Баланс цен и налогов |
| legacyImpactScore | Влияние исторических событий на current world flags | history subsystem | Адаптация seasonal rotation |
| ffinityGrowthRate | Рост привязанности в социальном дереве | social-service dialogues | Настройка требований |
| climateStabilityIndex | Степень контролируемости погоды Solar Covenant | world climate | Управление окнами Badlands |
| metanetComplianceRate | Решения трибуналов Echo Dominion | analytics-service logs | Допуск ИИ в инфраструктуру |

## 3. Авто-тюнинг
- **Контракты:** при contractSuccessRate > 0.8 повышать требования репутации на 5%; при <0.4 снижать.
- **Рейды:** 
aidClearTime сравнивается с целевым диапазоном; при отклонении корректируются HP/урон через /world/raids/{id}/balance.
- **Экономика:** coAssetVelocity > 1.5 → увеличивать налог, <0.7 → снижать.
- **История:** legacyImpactScore низок → активировать дополнительные historical events.
- **Социальные линии:** ffinityGrowthRate слишком высок → повышать стоимость ключевых выборов.
- **Климат:** climateStabilityIndex < 0.5 → усиливать песчаные бури; >0.8 → снижать награды.
- **Metanet:** metanetComplianceRate > 0.75 → разрешать больше ИИ-сервисов, <0.4 → вводить ограничения.

## 4. REST/WS Контуры
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| /analytics/factions/metrics | GET | Агрегированные метрики по фракциям |
| /analytics/factions/metrics/{metricId} | GET | Детализация с фильтрами по времени |
| /analytics/factions/autotune | POST | Применение авто-тюнинга (payload с изменениями) |
| /analytics/factions/alerts | GET | Пороговые события для ГМ |

**WebSocket:** wss://api.necp.game/v1/analytics/factions — MetricUpdate, AutotuneApplied, AlertRaised.

## 5. Схемы данных
`sql
CREATE TABLE faction_metrics (
    metric_id VARCHAR(64) PRIMARY KEY,
    faction_id VARCHAR(64) NOT NULL,
    metric_name VARCHAR(64) NOT NULL,
    value NUMERIC(12,4) NOT NULL,
    time_bucket TIMESTAMPTZ NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE faction_autotune_actions (
    action_id UUID PRIMARY KEY,
    metric_id VARCHAR(64) NOT NULL,
    previous_value NUMERIC(12,4) NOT NULL,
    new_value NUMERIC(12,4) NOT NULL,
    applied_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    applied_by VARCHAR(64) NOT NULL,
    notes TEXT
);
`

## 6. Готовность
- Метрики и правила авто-тюнинга описаны, интеграция с сервисами определена.
- Документ готов для реализации analytics-service и world-service.