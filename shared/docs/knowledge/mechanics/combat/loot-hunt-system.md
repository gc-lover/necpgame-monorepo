---

---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:07
**api-readiness-notes:** Перепроверено 2025-11-09 03:07: генерация лута, эвенты, риск/награда, связь с экономикой и экстрактом готовы к формированию API задач.
---

# Loot Hunt System - Киберконтракты на добычу

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 20:39  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Dynamic loot shooter gameplay loop  
**Размер:** ~330 строк ✅  
**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (8083)  
**target-frontend-module:** modules/combat/loot-hunt

---

## Краткое описание

Loot Hunt — основной PvPvE луп для охоты за имплантами, данными и редкими ресурсами. Система связывает процедурную генерацию контрактов, динамические эвенты и экстракцию.

---

## Игровая фантазия

- Получение контрактов от фиксеров/корпораций
- Инфильтрация зон: промзоны, дата-фермы, руины
- Сбор трофеев, взлом сейфов, доставка ядер данных
- Решение: экстрактиться или продолжать ради повышенных наград

---

## Типы зон

| Код | Описание | Основной лут | Угрозы |
|-----|----------|--------------|--------|
| ZN-01 | Abandoned Megastructure | Прототипы имплантов | Дроны Arasaka |
| ZN-02 | Blackwall Breach | Чёрные данные | Netwatch ICE |
| ZN-03 | Urban Ruins | Компоненты оружия | Банды, ловушки |
| ZN-04 | Offshore Vault | Криптовалюта | Militech SpecOps |

---

## Цикл миссии

1. Контракт — выбор зоны, целей, ограничений
2. Подготовка — анализ угрозы, сбор loadout, планирование в Voice Lobby
3. Инфильтрация — управление Heat/Exposure, стелс и саботаж
4. Loot Phase — Smart Loot, мини-эвенты (Black Market Drop, System Overload)
5. Extraction — точки выхода, Emergency Lift, риск перехвата

---

## Риск/Награда

- Heat Level влияет на редкость лута и появление элитных врагов
- Exposure Meter увеличивает шанс PvP-вторжений
- Insurance контракт страхует экипировку (см. economy/insurance)
- Fallback Rewards выдают анализ данных при провале

---

## Эвенты

1. Black Market Drop — капсула с уникальным лутом
2. System Overload — энергетические всплески, глич-кейсы
3. Corporate Sweep — рейды корпораций, таймер экстракции
4. Fixer Contract Chain — серия мини-задач

---

## Генератор контрактов

```pseudo
Select TargetZone based on player_profile.preferred_risk
Select LootTable using faction_reputation, heat forecast
Assign Objectives (primary, secondary, optional)
Calculate Reward = base + heat_modifier + streak_bonus
```

---

## Лут и редкости

- Тиры: Common → Mythic
- Семейства: импланты, моды оружия, кибердек-плагины, ядра
- Smart Loot учитывает состав команды, soft pity через 4 миссии без эпика

---

## Экономика и обмен

- Loot Appraisal, fixer auctions, black market reputation
- Crafting hooks для переработки ядер в компоненты

---

## Интеграции

- `combat-extract.md`, `economy/trade-system.md`, `progression/progression-paths.md`, голосовые лобби и клановые экспедиции

---

## Антагонисты

- Corp Strike Teams, Rogue Runners, Blackwall Echoes, environmental hazards

---

## Технические детали

- Instance Server, Spawn Controller (Rust gRPC), Kafka `loot.hunt.events`, AI Director

---

## API контуры

- `POST /api/v1/loot-hunt/contracts/request`
- `GET /api/v1/loot-hunt/contracts/active`
- `POST /api/v1/loot-hunt/match/{instanceId}/telemetry`
- `POST /api/v1/loot-hunt/extraction/trigger`

---

## Таблицы данных

`loot_contracts`, `loot_instances`, `loot_telemetry` — UUID, JSONB, timestamps

---

## Лор

- Фиксеры Pilar «Pix», Dex «Ghost», аналитик Hana Ito, номад Kang «Switch`
- Зоны привязаны к событиям (Blackwall Surge, Corporate Divide)

---

## KPI и риски

- Retention, Average Loot Value, PvP Encounter Rate, анти-чит меры

---

## Готовность

- Геймплейный цикл, экономика, интеграции и лор полностью описаны, документ готов к ДУАПИТАСК

---

---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:28
**api-readiness-notes:** Расширенный лутингшутер-цикл: генерация лута, эвенты, риск/награда, связь с экономикой и экстрактом.
---

# Loot Hunt System - Киберконтракты на добычу

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 20:28  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Dynamic loot shooter gameplay loop  
**Размер:** ~330 строк ✅  
**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (8083)  
**target-frontend-module:** modules/combat/loot-hunt

---

## Краткое описание

Loot Hunt — основной PvPvE луп для охоты за имплантами, данными и редкими ресурсами. Система связывает процедурную генерацию контрактов, динамические эвенты и экстракцию.

---

## Игровая фантазия

- Получение контрактов от фиксеров/корпораций
- Инфильтрация зон: промзоны, дата-фермы, руины
- Сбор трофеев, взлом сейфов, доставка ядер данных
- Решение: экстрактиться или продолжать ради повышенных наград

---

## Типы зон

| Код | Описание | Основной лут | Угрозы |
|-----|----------|--------------|--------|
| ZN-01 | Abandoned Megastructure | Прототипы имплантов | Дроны Arasaka |
| ZN-02 | Blackwall Breach | Чёрные данные | Netwatch ICE |
| ZN-03 | Urban Ruins | Компоненты оружия | Банды, ловушки |
| ZN-04 | Offshore Vault | Криптовалюта | Militech SpecOps |

---

## Цикл миссии

1. Контракт — выбор зоны, целей, ограничений
2. Подготовка — анализ угрозы, сбор loadout, планирование в Voice Lobby
3. Инфильтрация — управление Heat/Exposure, стелс и саботаж
4. Loot Phase — Smart Loot, мини-эвенты (Black Market Drop, System Overload)
5. Extraction — точки выхода, Emergency Lift, риск перехвата

---

## Риск/Награда

- Heat Level влияет на редкость лута и появление элитных врагов
- Exposure Meter увеличивает шанс PvP-вторжений
- Insurance контракт страхует экипировку (см. economy/insurance)
- Fallback Rewards выдают анализ данных при провале

---

## Эвенты

1. Black Market Drop — капсула с уникальным лутом
2. System Overload — энергетические всплески, глич-кейсы
3. Corporate Sweep — рейды корпораций, таймер экстракции
4. Fixer Contract Chain — серия мини-задач

---

## Генератор контрактов

```pseudo
Select TargetZone based on player_profile.preferred_risk
Select LootTable using faction_reputation, heat forecast
Assign Objectives (primary, secondary, optional)
Calculate Reward = base + heat_modifier + streak_bonus
```

---

## Лут и редкости

- Тиры: Common → Mythic
- Семейства: импланты, моды оружия, кибердек-плагины, ядра
- Smart Loot учитывает состав команды, soft pity через 4 миссии без эпика

---

## Экономика и обмен

- Loot Appraisal на станции, fixer auctions, black market reputation
- Crafting hooks для переработки ядер в компоненты

---

## Интеграции

- `combat-extract.md`, `economy/trade-system.md`, `progression/progression-paths.md`
- Guild expeditions, Daily Reset, Voice Lobby автоконфигурация

---

## Антагонисты и угрозы

- Corp Strike Teams, Rogue Runners, Blackwall Echoes, environmental hazards

---

## Технические детали

- Instance Server (Kubernetes pods), Spawn Controller (Rust gRPC)
- Telemetry: Kafka `loot.hunt.events` → ClickHouse
- AI Director: Python RL-сервис

---

## API контуры

- `POST /api/v1/loot-hunt/contracts/request`
- `GET /api/v1/loot-hunt/contracts/active`
- `POST /api/v1/loot-hunt/match/{instanceId}/telemetry`
- `POST /api/v1/loot-hunt/extraction/trigger`

---

## Таблицы данных

`loot_contracts`, `loot_instances`, `loot_telemetry` — UUID, JSONB, временные метки

---

## Лор и NPC

- Фиксеры Pilar «Pix», Dex «Ghost», аналитик Hana Ito, номад Kang «Switch`
- Зоны привязаны к событиям (Blackwall Surge, Corporate Divide)

---

## KPI и риски

- Retention, Average Loot Value, PvP Encounter Rate, Anti-cheat меры

---

## Готовность

- Геймплейный цикл, экономика, интеграции и лор полностью описаны
- Документ готов к обработке ДУАПИТАСК


