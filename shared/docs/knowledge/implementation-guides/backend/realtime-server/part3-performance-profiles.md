# Realtime Server - Part 3: Performance Profiles

---

- **Status:** not_created
  - N/A
- **Last Updated:** 2025-11-08 09:26
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-08 09:26  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 09:26
**api-readiness-notes:** "Performance-профили финализированы: edge-конфигурация, live-migration и инфраструктурные пороги утверждены, блокеров для API нет."

[← Part 2](./part2-protocol-optimization.md) | [Навигация](./README.md)

---

## Назначение документа

Документ описывает кастомные профили производительности для realtime-серверов NECPGAME. Каждый профиль соответствует типу контента и определяет базовые значения тикрейта, сетевых и вычислительных бюджетов, требования к оборудованию и дополнительные параметры, влияющие на качество сервиса. Матрица профилей позволяет заранее планировать горизонтальное и вертикальное масштабирование, а также согласовывать ожидания команд PvP, PvE и инфраструктуры.

---

## Матрица профилей контента

| Профиль | Контент | Tick Rate (Hz) | Snapshot Interval (мс) | Input Buffer (мс) | Max Players / Instance | AI Budget / Tick (мс) | Примечания |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **Esports Arena** | Ranked PvP 5v5, 3v3 Duels | 120 | 8 | 12 | 12 | 1 | Максимальный приоритет QoS, детальная телеметрия на каждое действие, повышенный античит (frame-by-frame).
| **Competitive Siege** | GvG 20v20, PvPvP King of the Hill | 90 | 11 | 18 | 50 | 2 | Упор на консистентность попаданий, адаптивная динамическая интерполяция, двуступенчатая валидация урона.
| **Extraction Ops** | PvPvE Extraction, Heist, Rogue Missions | 75 | 13 | 22 | 36 | 4 | Баланс real-time PvP и сложных AI; приоритет на репликацию целей и лута, гибридная система интересов (AOI + задачные группы).
| **Open World Conflict** | PvPvE на открытых картах, события 64+ игроков | 60 | 17 | 28 | 96 | 6 | Динамический тикрейт (60→45 при перегрузке), агрессивное AOI, зональный античит.
| **Narrative Raids** | PvE рейды 12-24 игроков, World Boss | 45 | 22 | 32 | 24 | 8 | Основной бюджет на AI и скрипты босса, расщепление рассылок по ролям (Tank/Heal/DPS), relaxed QoS.
| **Massive Warfront** | GvGvG и сезонные войны 100+ игроков | 40 | 25 | 35 | 120 | 5 | Tick decimation (каждый третий тик full state), делегированное моделирование отрядов, обязательный серверный предиктор траекторий.
| **Casual Coop** | PvE миссии, данджи 4-8 игроков | 30 | 33 | 40 | 16 | 10 | Низкий тикрейт, приоритет стабильности, batching событий и prefetching данных окружения.
| **Social Hub** | Городские зоны без боёв, мероприятия | 20 | 50 | 60 | 150 | 2 | Поддержка сотен игроков, упор на стриминг косметики и чата, отключение детальной физики.

### Дополнительные параметры по профилям

- **Tick Elasticity:** для профилей Open World Conflict и Massive Warfront используется ступенчатое снижение до 45/30 Hz при загрузке CPU > 75% в течение 3 секунд.
- **Snapshot Strategy:** Esports Arena и Competitive Siege отправляют полные снимки каждые 200 мс, остальные профили используют delta-компрессию с приоритетом критических сущностей.
- **Priority Channels:** отдельные очереди сообщений для боевых событий, команд управления и вторичных данных (анимации, косметика). В Esports Arena и Competitive Siege критические каналы ограничены максимум 2мс задержки на обработку.
- **Interest Management:** гибридная модель (квадродерево + task groups). Для Extraction Ops акцент на количестве активных задач, в Massive Warfront — на принадлежности к взводам.
- **Reliability Policies:** PvP-профили повторяют critical пакеты до 3 раз, PvE-профили ограничиваются 2 попытками и fallback на предикцию.

---

## Параметры и их назначение

| Параметр | Описание | Ответственный сервис |
| --- | --- | --- |
| Tick Rate | Частота обновления геймплейного цикла. Влияет на точность боевых взаимодействий и плавность движения. | gameplay-service / world-service |
| Snapshot Interval | Частота рассылки авторитетных снимков состояния. | realtime dispatcher в world-service |
| Input Buffer | Скользящее окно, допускающее задержку ввода для сглаживания скачков пинга. | gameplay-service (input reconciliation) |
| AI Budget | Максимальный бюджет вычислений AI на тик, предотвращает starvation сетевых задач. | gameplay-service (AI cluster) |
| Max Players / Instance | Жёсткий лимит соединений на инстанс. Используется впервые при матчмейкинге. | orchestrator (matchmaking + deployment) |
| Priority Channels | Количество и тип очередей сообщений с QoS. | api-gateway + world-service |
| Telemetry Density | Частота отправки метрик и логов (per tick, per second, per event). | monitoring-service |
| Anti-Cheat Level | Набор проверок (basic, advanced, forensic). | anti-cheat-service |

---

## Серверные профили оборудования

| Профиль | vCPU | RAM | Network (Gbps) | Storage | Примечания |
| --- | --- | --- | --- | --- | --- |
| Esports Arena | 16 | 32 GB | 10 | NVMe 1 TB | Один инстанс = один матч, CPU pinning, NUMA awareness.
| Competitive Siege | 24 | 48 GB | 10 | NVMe 1 TB | Поддержка горячей репликации состояния на standby-инстанс.
| Extraction Ops | 20 | 48 GB | 10 | NVMe 1 TB | GPU-ускорение pathfinding (опционально) через CUDA воркеры.
| Open World Conflict | 32 | 64 GB | 25 | NVMe 2 TB | Высокая пропускная способность сети, обязательный dedicated Redis shard.
| Massive Warfront | 48 | 128 GB | 40 | NVMe 2 TB | Шардирование по батальонам, требуется колокация с Kafka.
| PvE / Social | 12 | 24 GB | 5 | SSD 512 GB | Возможен мульти-инстанс на одном bare-metal хосте.

### Автоскейлинг

- **Горизонтальное масштабирование:** на основе метрик времени обработки тика (TargetTickDuration) и активных соединений. Порог масштабирования задаётся как 0.85 * Max Players.
- **Вертикальное масштабирование:** шаблоны Terraform готовят отдельные пуллы для профилей Esports Arena, Open World Conflict и Massive Warfront; переключение осуществляется через blue-green запуск.
- **Warm Pools:** поддерживаются прогретые инстансы (5 минут readiness) для Esports Arena и Extraction Ops, что обеспечивает время старта < 30 секунд.

---

## QoS и управление трафиком

- **API Gateway:** применяет приоритизацию на основе профиля матча; каналы PvP получают класс обслуживания `premium-low-latency`.
- **Network Shaping:** использование gRPC + WebSocket multiplexing с ограничением MTU 1200 байт; в PvP профилях включена FEC (Forward Error Correction) 5%.
- **Packet Batching:** в PvE профилях отправка пакетов происходит каждые 2 тика (для экономии пропускной способности).
- **Compression:** MessagePack + Zstd (уровень 3) для payload > 8 KB.

---

## Адаптивная синхронизация состояний

- **Механизм:** гибрид `interest management + predictive layers`. Базовый слой отправляет только критичные AOI-объекты, прогнозный слой агрегирует перемещения и навыки с горизонтом 2–3 тика.
- **Динамика:** при загрузке > 80% сервер урезает радиус AOI на 20% и отключает низкоприоритетные каналы (эмоции, косметика) вместо снижения тикрейта.
- **Гранулярность:**
  - Tier A (PvP Core): полный state-diff каждый тик, delta-компрессия.
  - Tier B (PvPvE, Siege): state-diff каждые 2 тика, предиктивные spline-пути.
  - Tier C (PvE Social): агрегированные snapshot-чанки раз в 4 тика.
- **Конфигурация:** параметры `aoi-radius`, `predictive-budget-ms`, `low-priority-channel-threshold` выставляются через Match Profile.

---

## Edge и региональные сервера

- **Размещение:** edge-ноды NA, EU, Asia, LATAM, EMEA. Каждая зона держит региональный матчмейкинг с параметром `region-affinity` ≥ 0.7.
- **Роутинг:** Anycast DNS + GeoIP, fallback на ближайший edge с latency < 80 мс.
- **Миграция:** если загрузка edge превышает 85%, сессии переводятся на соседний регион через live-migration (см. ниже).
- **Данные:** состояния игроков синхронизируются через CRDT-репликацию и вторичный кэш (см. раздел ниже), доля write operations на центральный кластер ≤ 30%.

### Параметры edge-нод

- **Аппаратные классы:**  
  - `edge.tier.esports` — 16 vCPU, 32 GB RAM, 10 Gbps, NVMe 1 TB.  
  - `edge.tier.massive` — 24 vCPU, 48 GB RAM, 25 Gbps, NVMe 2 TB.  
  - `edge.tier.social` — 12 vCPU, 24 GB RAM, 5 Gbps, SSD 512 GB.  
- **Пулы развертывания:** Terraform workspace `edge-prod` поддерживает пул прогретых инстансов (минимум 2 на регион) с readiness-пробами < 30 секунд.  
- **QoS-пресеты:**  
  - PvP профили активируют `qos-profile=low-latency` (DSCP 46, приоритет канала 0).  
  - PvE профили используют `qos-profile=balanced` (DSCP 26).  
  - Social профили включают `qos-profile=throughput` (DSCP 10).
- **Порог деградации:** при `TickDuration p95 > target * 1.2` в течение 45 секунд edge-нода маркируется `overloaded` и инициирует миграцию.

---

## Гибридная симуляция AI

- **Архитектура:** выделенные AI-воркеры (микросервис на GPU/CPU пулах) считают сложные NPC-поведения; геймплей-сервер получает агрегаты (trajectory bundle, intent bundle).
- **Интерфейс:** gRPC streaming, SLA на response < 6 мс для PvP и < 12 мс для PvE.
- **Балансировка:** при нагрузке тикрейта AI-воркеры масштабируются горизонтально, геймплей-сервер держит `ai-fallback-mode` (упрощённые FSM скрипты).
- **Параметры профиля:** `ai-offload-ratio`, `aggregated-update-interval`, `fallback-timeout-ms`.

---

## Античит и целостность

- **Esports / Competitive:**
  - Серверные проверки every tick (позиция, скорость, частота действий).
  - Повторная симуляция сервером до 120 тиков в прошлое для выяснения коллизий.
  - Сбор геймплейных replay-файлов для арбитража.
- **Extraction / Open World:**
  - Проверка целостности лута, контроль teleport/clip.
  - Отложенная форензика с анализом heatmap передвижений.
- **PvE / Social:**
  - Базовые проверки, фокус на защите экономики (дубликаты, gold-dupe detection).
- **Поведенческий уровень:** ML-аналитика паттернов кликов и перемещений, параметры `telemetry-sample-rate` (по умолчанию 20 Гц для PvP) и `anomaly-threshold` настраиваются для профиля.

---

## Network Chaos и стресс-тесты

- **Частота:** ежеквартальные распределённые тесты на 150% номинальной нагрузки для каждого профиля.
- **Метрики:** допустимый PacketLoss ≤ 2% при стресс-режиме, деградация тикрейта не более 15%.
- **Сценарии:** массовое подключение, edge-failover, отключение AI-воркеров, флуд телеметрии.
- **Автоматизация:** использование Kubernetes Chaos Mesh + k6, результаты сохраняются в `replay/chaos-report`.

---

## Второй уровень кэширования состояний

- **Технология:** Hazelcast/Ignite кластер в пределах региона, replication factor 2.
- **Назначение:** хранение горячих snapshot-чанков и быстрый recovery после failover.
- **Параметр SLO:** `state-recovery-time` ≤ 5 секунд для PvP и ≤ 12 секунд для PvE.
- **Интеграция:** геймплей-сервер пушит deltas в data grid каждый тик, edge-ноды подтягивают состояние перед миграцией.

---

## Обслуживание матчей без перерывов

- **Live-migration:** graceful handoff с дублированием состояния на standby-инстанс, задержка переключения ≤ 250 мс.
- **Плановое обслуживание:** максимум 2 миграции в час на профиль, очередь миграций управляется control-plane.
- **Синхронизация:** перед переключением отправляется pre-freeze snapshot, клиенты получают уведомление только при задержке > 150 мс.

---

## Политика live-migration

- **Триггеры запуска:**  
  - `cpu-load > 0.82` или `network-usage > 0.75` в течение 60 секунд.  
  - Плановое обслуживание с флагом `maintenance=true` из control-plane.  
  - Edge-фейловер при потере heartbeat 3 интервала подряд.
- **Этапы:**  
  1. **Prepare:** вторичный инстанс прогревается и синхронизирует состояние через data grid (`state-sync-mode=shadow`).  
  2. **Freeze:** активный инстанс фиксирует запись в Kafka `match-state`, отправляет pre-freeze snapshot, блокирует новые соединения.  
  3. **Transfer:** клиенты перенаправляются через API Gateway (HTTP 307) на standby-инстанс, ожидаемый даунтайм ≤ 250 мс.  
  4. **Resume:** standby становится основным, старый инстанс переводится в режим диагностики.
- **Мониторинг:** метрики `live-migration.duration`, `session-drop-rate`, `reconnect-p95` публикуются в Prometheus; алерт при `session-drop-rate > 0.5%`.
- **Аварийный откат:** при неудачном переносе control-plane переключает сессию обратно за ≤ 400 мс и помечает профиль `degraded`.

---

## Инструменты разработчиков

- **Telemetry Dumps:** для PvP/Esports — каждые 5 минут, детализация frame-by-frame; для PvE — каждые 15 минут агрегаты.
- **Replay Storage:** хранение 48 часов для Ranked, 12 часов для Casual, экспорт в аналитические пайплайны.
- **Debug Channels:** возможность включать расширенный лог state-sync на 2% инстансов без влияния на остальных.

---

## Мониторинг и SLO

- **Основные метрики:** TickDuration p95, NetworkLatency p95, PacketLoss %, ActiveConnections, AOIEntities, StateRecoveryTime p95, ChaosTestPassRate.
- **SLO по профилям:**
  - Esports Arena: TickDuration p95 ≤ 8.5 мс, NetworkLatency p95 ≤ 35 мс, StateRecoveryTime ≤ 5 с.
  - Competitive Siege: TickDuration p95 ≤ 11 мс, NetworkLatency p95 ≤ 45 мс, ChaosTestPassRate ≥ 0.95.
  - Extraction Ops: TickDuration p95 ≤ 14 мс, PacketLoss ≤ 1.5%, StateRecoveryTime ≤ 8 с.
  - PvE Raids: TickDuration p95 ≤ 18 мс, AI Budget Utilization ≤ 85%, StateRecoveryTime ≤ 12 с.
- **Alerting:** PagerDuty инцидент при превышении SLO двух интервалов подряд; автоматическое масштабирование до диагностики.

---

## Связанные документы

- `./part1-architecture-zones.md` — зоны, инстансы, распределение игроков.
- `./part2-protocol-optimization.md` — сетевой протокол и оптимизации.
- `../realtime-server/README.md` — общая навигация по разделу.
- `../../infrastructure/caching-strategy.md` — уровни кэширования для realtime трафика.
- `../../infrastructure/anti-cheat-system.md` — уровни защиты и интеграция с профилями.

---

## История изменений

- 2025-11-08 09:26 — финализация профилей, добавлены edge-параметры и политика live-migration, статус готовности обновлён на `ready`.
- 2025-11-07 16:03 — расширена матрица профилей, добавлены адаптивная синхронизация и гибридный AI.

