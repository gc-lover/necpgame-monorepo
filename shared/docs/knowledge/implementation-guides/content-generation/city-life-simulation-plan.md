---
**Статус:** draft  
**Версия:** 0.1.0  
**Дата:** 2025-11-07 20:45  
**Автор:** AI Brain Manager
---

# City Life Simulation Plan

## Цели

- Провести 24-часовую симуляцию оживления городов для первой волны (Watson, Westbrook, Shinjuku, Kreuzberg).
- Выполнить стресс-тесты и собрать метрики SLA живости, инфраструктурной нагрузки, реакции на события игроков.
- Настроить мониторинг (Grafana) и алерты для live-ops команды.

## Подготовка

1. Импорт baseline (`content-generation/baseline/*.json`) в world-service и social-service.
2. Генерация тестовых NPC и расписаний с использованием `npc-profile-generator`.
3. Настройка kafka topics (`world.city.lifecycle.v1`, `social.npc.schedule.v1`, `economy.infrastructure.alerts`, `gameplay.player.impact`).
4. Развёртывание тестовой среды Grafana + Prometheus с дашбордами:
   - Living Metrics Dashboard
   - Infrastructure Load Dashboard
   - Player Impact SLA Dashboard

## Сценарии

### 1. Суточная симуляция (24h)

- Тайм-слайсы: `weekday`, `weekend`, `global_event` (по 8 часов).
- Игроки-боты: 5000 сценариев (квесты, криминал, экономика, социальные активности).
- Метрики:
  - `npcDensityDeviation` (по районам, целевой диапазон ±0.15).
  - `infrastructureLoad` (среднее и пиковое, целевой 0.6–0.85).
  - `dynamicNpcShare` (не менее 0.25).
  - `playerImpactSla` (<3 мин локальные, <15 мин глобальные).

### 2. Стресс-тест (Combined Event)

- События: `corporate_raid`, `street_protest`, `weather_storm` запускаются одновременно.
- Оцениваем:
  - Пропускную способность Kafka (сообщений/сек > 10k без потерь).
  - Latency REST эндпоинтов (`/world/cities/{id}/snapshot`, `/economy/districts/{id}/infrastructure`) < 500 мс P95.
  - SLA пересчёта (`recompute job` завершение < 5 мин).

### 3. QA маршрутов

- Визуализация 1000 случайных NPC (timeline, heatmaps).
- Автоматические проверки: `routeLatency < 5 мин`, `collisionProbability < 0.02`, отсутствие тупиковых маршрутов.

## Мониторинг и алерты

- **Grafana Panels:**
  - NPC Density Heatmap.
  - Infrastructure Load by Category.
  - Player Impact Response Time.
  - Kafka Topic Throughput.
- **Алерты:**
  - `npcDensityDeviation > 0.20` (Warning), `> 0.30` (Critical).
  - `queueLag > 120s` для `world.city.lifecycle.v1`.
  - `eventProcessingError > 0` (Critical).
  - `recomputeJobDuration > PT10M`.

## Отчётность

1. Собрать JSON лог симуляции (`simulation-run-{timestamp}.json`).
2. Сформировать сводный отчёт (Living metrics, SLA, алерты) → `reports/simulation/city-life-24h-{date}.md`.
3. Подготовить рекомендации по корректировке алгоритма (если метрики вне диапазона).

## Дальнейшие шаги

- После первой симуляции скорректировать baseline и расписания.
- Уточнить алерты и показатели для live-ops.
- Подготовить автоматизацию запуска (CI job или cron в тестовой среде).

