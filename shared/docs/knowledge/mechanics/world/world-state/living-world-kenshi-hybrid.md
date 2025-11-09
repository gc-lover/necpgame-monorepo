---
api-readiness: ready
**api-readiness-check-date:** 2025-11-08 00:22
api-readiness-notes: Полностью описаны численные коэффициенты Action XP, контракт world-service/gameplay-service и UX-потоки хроники мира; документ готов к постановке API задач.
---

# Living World & Use-Based Progression — Kenshi Hybrid

---

**Статус:** review  
**Версия:** 0.2.0  
**Дата создания:** 2025-11-08  
**Дата обновления:** 2025-11-08 09:30  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**target-domain:** gameplay-world-state, gameplay-progression  
**target-microservice:** world-service (8086); gameplay-service (8083)  
**target-frontend-module:** modules/world/state, modules/progression/skills

---

## Цели

- Запустить симуляцию живого мира, реагирующего на смену власти, логику логистики и автономные NPC-отряды.
- Расширить систему рангов и навыков за счёт Action XP (использование = прогресс) с ограничениями по усталости.
- Обеспечить непрерывный UX: хроника мира, управление базами, тренажёры, визуализация влияния игроков.

---

## Слои симуляции

### Фракционные войны
- `world.faction.<id>.control` хранит текущего владельца города/региона.
- Триггеры смены: смерть лидера, победы рейдов (3 события за 24 часа), экономическое истощение (supply < 30%).
- Последствия: запуск арок `Rebuild`, `Reprisal`, `Occupation` с прогрессом по таймерам.

### Поселения и логистика игроков
- Уровни баз: `camp`, `outpost`, `stronghold`, `city`. Упgrad-условия: население, рейтинг репутации, объём экспорта.
- Логистические ивенты: `TaxCollectorVisit`, `RaidWarning`, `MerchantConvoy` масштабируются от богатства базы.
- Экономическое влияние транслируется в `economy-world-impact.md` через коэффициент `RegionalPriceModifier`.

### Дорожная сеть и отряды
- world-service генерирует `AutonomousSquad` с целями `patrol`, `raid`, `escort`, `trade`.
- При генерации рассчитываются ETA, маршрут, сила. Игроки могут перехватить, сопровождая или атакуя.
- Все столкновения пишутся в `world.timeline` и доступны фронтенду как события хроники.

### Реактивные события
- После смены власти запускаются события `PowerVacuum`, `PoliticalPurge`, `ReliefCaravan`.
- Эти события создают хуки для квестов, экономических контрактов и PvP активностей.

---

## Гибридная прокачка «Действие + Ранги»

### Модель опыта
- Итоговая формула: `TotalXP = QuestXP + ActionXP × ActivityMultiplier × FatigueModifier`.
- Action XP начисляется поминутно или за события (убийство босса, логистический рейд).
- Ранговые пороги повышаются Action XP: `RequiredQuestXP = BaseQuestXP × (1 + ActionXP ÷ SoftCap)`.

### Коэффициенты Action XP
| Навык | Базовое действие | Action XP за единицу | ActivityMultiplier | FatigueLimit (в час) |
| --- | --- | --- | --- | --- |
| Strength | Перенос >60 кг | 4 | 1.5 (боевой режим) | 600 |
| Resilience | Получение урона без нокдауна | 3 | 1.2 | 500 |
| Stealth | Перемещение рядом с врагом | 2 | 1.8 (ночью) | 450 |
| Hacking | Взлом устройств | 6 | 2.0 (боевой инцидент) | 400 |
| Engineering | Ремонт турелей | 5 | 1.3 | 550 |
| Medicine | Стабилизация союзника | 5 | 1.6 (под огнём) | 500 |
| Logistics | Защита каравана | 8 | 1.4 | 650 |

### Усталость и мягкие капы
- `SoftCap = FatigueLimit × 1.0`. После превышения действует `FatigueModifier = 0.25`.
- `FatigueScore = (ActionXP ÷ FatigueLimit) × 100`. При >120% накладывается дебафф на реген, замедляется тренировка.

### Тренажёры
- Объекты Dojo, HackLab, Field Hospital добавляют контролируемый прирост Action XP (1/3 от боевого).
- Каждая сессия расходует ресурсы (`training_credits` из экономики) и увеличивает усталость.

---

## Данные и модели

### World-service
- `FactionControl`
  - `factionId: UUID`
  - `regionId: UUID`
  - `controlScore: int (-100..100)`
  - `lastChange: datetime`
  - `pendingEvents: List<EventRef>`
- `Settlement`
  - `settlementId`
  - `status: enum {camp, outpost, stronghold, city}`
  - `population`
  - `production: Map<Resource, int>`
  - `defenseRating`
  - `ownerFactionId`
- `LogisticsRoute`
  - `routeId`
  - `originSettlementId`
  - `destinationSettlementId`
  - `type: enum {trade, tax, reinforcement}`
  - `securityLevel`
  - `activeSquads: List<SquadRef>`
- `AutonomousSquad`
  - `squadId`
  - `composition`
  - `mission`
  - `currentWaypoint`
  - `eta`
  - `threatLevel`

### Gameplay-service (Action XP)
- `ActionXpRecord`
  - `characterId`
  - `skillId`
  - `actionType`
  - `xpGained`
  - `multiplier`
  - `fatigueScore`
  - `sourceEventId`
- `SkillFatigue`
  - `characterId`
  - `skillId`
  - `dailyXpTotal`
  - `fatigueModifier`
  - `resetAt`

### Observability
- `world.timeline` хранит события с типом, описанием, координатами и участниками.
- `action_xp.metrics` агрегирует xp по навыкам, fatigue и источникам.

---

## API контракты

### World-service REST

1. `GET /api/v1/world/factions/{id}/control`
   - Параметры: `regionId` (optional)
   - Ответ: `{ "regionId": "uuid", "controlScore": 65, "ownerFactionId": "uuid", "pendingEvents": [ ... ] }`

2. `POST /api/v1/world/factions/control-shift`
   - Тело: `{ "regionId": "uuid", "newOwnerFactionId": "uuid", "trigger": "leader_death", "evidence": { ... } }`
   - Ответ: `{ "eventId": "uuid", "timelineEntryId": "uuid" }`
   - Валидация: проверка триггера и контрольного порога (например, суммарное влияние >1000).

3. `GET /api/v1/world/logistics/routes`
   - Фильтры: `type`, `status`, `ownerFactionId`
   - Ответ: `{ "routes": [ { "routeId": "uuid", "status": "active", "securityLevel": 3, "activeSquads": [ ... ] } ] }`

4. `POST /api/v1/world/logistics/routes`
   - Создание маршрута игроком или фракцией.
   - Тело: `{ "originSettlementId": "uuid", "destinationSettlementId": "uuid", "type": "trade", "requestedSecurity": 3 }`
   - Ответ: `{ "routeId": "uuid", "scheduledAt": "2025-11-08T00:30:00Z" }`

5. `GET /api/v1/world/chronicle`
   - Параметры: `cursor`, `limit`, `filter[eventType]`
   - Ответ: поток событий для UX хроники.

### Gameplay-service REST

1. `POST /api/v1/progression/action-xp`
   - Тело: `{ "characterId": "uuid", "entries": [ { "skillId": "stealth", "xp": 12, "activityMultiplier": 1.8, "source": "stealth_kill", "fatigueScore": 85 } ] }`
   - Ответ: `{ "updatedSkills": [ { "skillId": "stealth", "totalXp": 5420, "fatigueModifier": 0.75 } ] }`

2. `GET /api/v1/progression/action-xp/summary`
   - Параметры: `characterId`, `skillId`
   - Ответ: `{ "dailyXpTotal": 480, "fatigueModifier": 0.5, "resetAt": "2025-11-08T23:59:00Z" }`

3. `POST /api/v1/progression/fatigue/reset`
   - Тело: `{ "characterId": "uuid", "skillId": "strength", "consumableId": "rest_pack" }`
   - Используется для восстановления усталости через экономические предметы.

### Events (Kafka)
- `world.faction.controlShifted`
  - Payload: `{ "regionId", "previousOwner", "newOwner", "trigger", "timelineEntryId" }`
- `world.logistics.routeCreated`
  - `{ "routeId", "origin", "destination", "securityLevel", "initiator" }`
- `world.squad.spawned`
  - `{ "squadId", "mission", "factionId", "waypoints" }`
- `gameplay.actionXp.gained`
  - `{ "characterId", "skillId", "xp", "fatigueScore", "sourceEventId" }`
- `gameplay.actionXp.softCapReached`
  - `{ "characterId", "skillId", "dayTotal", "timestamp" }`

### GraphQL (frontend gateway)
- `worldChronicle(cursor, filters)` возвращает события с описанием, участниками, координатами.
- `actionXpOverview(characterId)` объединяет XP, усталость, прогресс ранга.

---

## UX-потоки

### Хроника мира
1. Экран `World Chronicle` отображает ленту событий с фильтрами по типу и региону.
2. При клике открывается модальный просмотр: карта, участники, изменение влияния.
3. Игрок может подписаться на фракции или логистические маршруты, получая уведомления.

### Управление базой
1. На экране базы показываются метрики населения, производства, безопасность, состояние логистики.
2. Кнопка «Улучшить статус» запускает мастер: требования, выбор условий (налоги, союзники), подтверждение.
3. Полоса прогресса отображает необходимые ресурсы и время; события рейдов подсвечиваются.

### Тренажёры и усталость
1. Игрок открывает интерфейс тренажёра (Dojo/HackLab) и выбирает программу.
2. UI показывает базовый XP, ожидаемую усталость, стоимость ресурсов.
3. После сессии отображается отчёт: полученный XP, новые уровни, изменение fatigue.
4. При превышении лимита отображается предупреждение и кнопка «Отдохнуть/купить стим».

### Логистические маршруты
1. На карте мира отображаются активные маршруты с состоянием (безопасен, атакован).
2. Игрок выбирает маршрут и может назначить сопровождение, рейд или усиление охраны.
3. Успешные действия обновляют хронику мира и выдаются бонусы Action XP.

### Истории героя
1. В разделе профиля есть вкладка «Истории»: генерация повествования на основе активности (освобождение города, защита каравана).
2. UX формирует карточки с названием, участниками, влиянием на мир и наградами.

---

## Баланс и аналитика
- `controlShiftRate` следит за тем, чтобы не более 3 смен власти в регионе за неделю.
- `actionXpPerSkill` агрегируется с разбивкой по навык/тип активности; алерты при >150% от целевого.
- `fatigueOverflow` отслеживает массовые переработки и предлагает увеличить soft cap или стоимость тренажёра.
- `routeSurvivalRate` показывает долю завершённых логистических маршрутов без атак.

---

## Связи с документами
- `progression-skills.md`: обновить таблицы рангов и добавить коэффициенты `ActionXpMultiplier`, `FatigueCap`.
- `global-state-core.md`: добавить агрегаты `faction_control`, `logistics_routes`, `player_story_arcs`.
- `quest-system.md`: использовать события `PowerVacuum`, `LogisticsDisruption` как хуки для генерации заданий.
- `economy-world-impact.md`: связать производство баз с индексом региональных цен.

---

## История изменений
- v0.2.0 (2025-11-08) — добавлены модели данных, API, UX-потоки, таблицы коэффициентов, устранены открытые вопросы.
- v0.1.0 (2025-11-08) — черновик концепции живого мира и Action XP.


