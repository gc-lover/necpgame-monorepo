# Specter / Helios Progression Panel — UI/UX & Seasonal Activities

**ID документа:** `specter-helios-progression-panel`  
**Тип:** ui-spec  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-08  
**Последнее обновление:** 2025-11-08 01:05  
**Приоритет:** высокий  
**Связанные документы:** `../../02-gameplay/world/economy-specter-helios-balance.md`, `../ui/guild-contract-board.md`, `../../02-gameplay/world/city-unrest-escalations.md`, `../../04-narrative/quests/side/2025-11-08-mission-kaede-family-rescue.md`  
**target-domain:** frontend/ui  
**target-мicroservices:** world-service, economy-service, social-service, narrative-service  
**target-frontend-модули:** modules/world, modules/guild/raids, modules/events  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 01:05
**api-readiness-notes:** Панель прогрессии, сезонные активности и API контуры подготовлены для реализации.

---

## 1. Цель
- Отображать прогресс гильдий и игроков в конфликте Specter vs Helios.
- Показывать War Meter, City Unrest, сезонные модификаторы и личные достижения.
- Интеграция с контрактами, рейдами, миссией Kaede и экономикой.

## 2. Основные виджеты

| Виджет | Описание | Данные / API | Примечания |
|--------|----------|--------------|-----------|
| War Meter | Ползунок Specter ↔ Helios (0–1) | `GET /api/v1/world/war-meter` | Предоставляет динамику за неделю |
| City Unrest Card | Текущий уровень + модификаторы | `GET /api/v1/world/city-unrest/state` | Ссылка на Crisis Hub |
| Seasonal Track | Активный сезон, награды, срок | `GET /api/v1/world/seasons/current` | Поддержка сезонов Winter Rush, Proxy War, etc. |
| Contract Progress | Личные/гильдейские очки | `GET /api/v1/world/contracts/progression` | Связано с Guild Contract Board |
| NPC Bonds | Лояльность Kaede/Kaori/Dr. Lysander | `GET /api/v1/narrative/npc/bonds` | Визуализация доверия |
| Milestone Timeline | Список событий недели | `GET /api/v1/world/war-meter/history` | Отметки: рейды, миссии, PvP |

## 3. UX-потоки

### 3.1 Home Flow
1. Игрок открывает панель (HUD → World Pulse → Progression).
2. Видит общую сводку: War Meter, Unrest, сезон.
3. Разворачивает вкладки: `Specter`, `Helios`, `Balanced`.
4. Смотрит задания (CTA: «Принять контракт», «Запустить миссию», «Просмотреть кат-сцену»).

### 3.2 Seasonal Activity Flow
1. На Seasonal Track отображается текущая активность (например, Winter Rush).
2. Игрок видит этапы (Tier 1–5) и награды.
3. Нажимает `View Activity` → подробности (PvE, PvP, Rescue).
4. CTA направляет к соответствующему модулю (Raid, Mission, Event).

### 3.3 NPC Bond Flow
1. В секции NPC Bonds отображается доверие Kaede и других ключевых NPC.
2. Игрок видит последние взаимодействия (например, «Семья спасена», «Helios доверяет»).
3. CTA `View Dialogue` запускает соответствующую кат-сцену.

## 4. ASCII мокап
```
┌────────────────────────────────────────────────────────────────┐
│ SPECTER / HELIOS PROGRESSION PANEL            Season: Winter Rush│
├─────────────┬─────────────┬─────────────┬──────────────┬────────┤
│ War Meter   │ City Unrest │ Seasonal    │ Contracts     │ NPC Bond│
│ Specter 42% │ Crisis 62   │ Tier 3/5    │ Guild Rank: 7 │ Kaede: 78│
│ Helios 58%  │ Δ +4 (Helios)| Time left 05d│ Personal Pts: 320│ Kaori: 65│
├─────────────┴─────────────┴─────────────┴──────────────┴────────┤
│ Milestones (Week 47)                                           ▼ │
│ - Specter Surge Apex cleared (War Meter +6%)                    │
│ - Helios CM-Viper victory (Unrest +5)                           │
│ - Kaede Family Rescue: SUCCESS (Unrest -12, Bond +25)           │
│                                                                 │
│ Specter Actions                                                 │
│ [Launch Family Rescue]  [Run Specter Parade]  [Open Contract Board]│
│                                                                 │
│ Helios Actions                                                  │
│ [Deploy CM-Aegis]  [Join CM-Viper]  [Activate Blackwall Signet] │
│                                                                 │
│ Balanced Operations                                             │
│ [Schedule Underlink Mediator]  [View Neutral Supply Run]       │
│                                                                 │
│ Seasonal Rewards                                                │
│ Tier 3 Unlocked: Specter Winter Cloak (Claim)                   │
│ Next Tier: Complete 2 Rescue missions / 1 Helios victory        │
└────────────────────────────────────────────────────────────────┘
```

## 5. API Требования (frontend)
- `GET /api/v1/world/war-meter`
- `GET /api/v1/world/city-unrest/state`
- `GET /api/v1/world/seasons/current`
- `GET /api/v1/world/contracts/progression`
- `GET /api/v1/narrative/npc/bonds`
- `GET /api/v1/world/milestones/week`
- WebSocket: `WAR_METER_UPDATE`, `CITY_UNREST_UPDATE`, `SEASONAL_PROGRESS_UPDATE`, `NPC_BOND_UPDATE`.

## 6. API Требования (backend)
- World-service: агрегировать war meter, milestones, seasonal активность.
- Economy-service: выдача наград за сезонные треки (`POST /api/v1/economy/seasons/reward`).
- Social-service: обновления фидов (`POST /api/v1/social/progression/broadcast`).
- Narrative-service: доступ к кат-сценам / диалогам (`POST /api/v1/narrative/progression/event`).

## 7. Seasonal Activities

| Сезон | Модификаторы | Активности | Награды |
|-------|--------------|------------|---------|
| Winter Rush | `transport_cost -10%`, `specter-favor ×1.1` | `Family Rescue`, `Snowfall Run` | `specter-winter-cloak`, `helios-winter-drone` |
| Proxy War | `war_meter` тик ускорен | PvP `CM-Phalanx`, `Underlink Mediator` | `war-effort-cache`, `blackwall-signet` |
| Neon Jubilee | `city_unrest` decay +20% | Social events, Specter Parade | `social-trust +6`, косметика |

- Seasonal трек лежит в `seasonal_progress(player_id, season_id, tier, points)`.
- Недельный сброс в понедельник 04:00 UTC.

## 8. Persistence
- `war_meter_history(date, value_specter, value_helios)`
- `seasonal_activity(player_id, season_id, points, tier)`
- `npc_bonds(player_id, npc_id, score, last_update)`
- `progression_milestones(guild_id, milestone_id, completed_at)`

## 9. Telemetry и SLA
- Telemetry events: `progress_panel_open`, `progress_action_click`, `season_tier_claim`, `bond_change`.
- KPIs: использование панели ≥ 70% активных рейдеров; среднее время отклика < 200 мс; сезонная конверсия Tier 3 ≥ 60%.
- SLA: `progression_fetch ≤ 180 мс`, `action_trigger ≤ 220 мс`.
- Dashboards: `specter-helios-progress`, `seasonal-track`, `npc-bond-heatmap`.

## 10. История изменений
- 2025-11-08 01:05 — Создан дизайн панели прогрессии Specter/Helios с сезонными активностями и API.


