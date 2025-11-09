# Остров Гранвилл — культурный хаб Ванкувера

**Статус:** review  
**Версия:** 1.0.0  
**Последнее обновление:** 2025-11-09 11:09  
**Приоритет:** medium  

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:09  
**api-readiness-notes:** Перепроверено 2025-11-09 11:09. Квест описывает все ветви, KPI, зависимости с quest-engine, economy и social-service; определены API каталоги и требования к данным, документ готов к постановке задач.

**target-domain:** narrative-quests  
**target-microservice:** narrative-service (8087)  
**target-secondary-services:** gameplay-service (8083), social-service (8084), economy-service (8089)  
**target-frontend-module:** modules/narrative/quests  
**api-directory:** api/v1/narrative/quests/america/vancouver/granville-island.yaml  

**quest-id:** VANCOUVER-2029-009  
**city:** Ванкувер | **region:** Америка | **period:** 2020-2029  
**quest-type:** social-exploration | **difficulty:** easy | **solo-group:** solo/duo | **time:** 2-4h  
**quest-arcs:** cultural-tourism, artisan-community, covert-logistics  

---

**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-114: api/v1/narrative/quests/america/vancouver/granville-island.yaml (2025-11-09)
- Last Updated: 2025-11-09 22:05
---

## Краткое описание
Игрок прибывает на Granville Island, где сочетаются ремесленные мастерские, гастрономия и подпольные логистические операции. Квест балансирует между поддержкой локальных артистов, корпоративным шпионажем и экономическим влиянием игрока на район. Финальный исход определяет, сохранит ли остров творческий дух, станет ли витриной корпораций или уйдёт в подпольную экономику.

---

## Ключевые персонажи и фракции
- **Рина Чжан** — куратор арт-пространства, представитель культурной коалиции. Доступ к социальным бонусам и легальным контрактам.
- **Майло “Ferry” Дуглас** — оператор Aquabus, посредник между туристами и теневыми поставками. Проводник во время первой сцены.
- **Сол Моралес** — дистрибьютор Granville Island Brewing, курирует дегустационные события и торговые соглашения.
- **Axiom Logistics** — корпорация, пытающаяся монополизировать поставки на рынок. Приводит к ветке корпоративного давления.
- **Коллектив “Tide Syndicate”** — подпольная группа, контрабандирующая редкие ингредиенты и арт-объекты, управляет чёрным рынком.

---

## Структура квеста
| Этап | Локация | Сцена | Проверки | Основные действия | Зависимости |
| --- | --- | --- | --- | --- | --- |
| A1 | Aquabus Pier | Прибытие на остров | `social.reputation >= 20` для VIP посадки | Выбор проводника (Рина vs Майло), установка начальной репутации | `travel-system`, `weather-service` |
| A2 | Public Market | Обзор рынка | `economy.credit >= 200` для премиум-доступа | Микрозадачи по дегустациям, торговля с NPC | `inventory-service`, `dynamic-pricing` |
| A3 | Artisan Alley | Мастерские | Проверки навыков `crafting.*` и `performance.*` | Создание арт-объекта, выступление, переговоры | `crafting-service`, `social-service` |
| A4 | Brewing District | Пивоварня | Тесты на устойчивость к алкоголю `physique.check` | Подписание контракта, саботаж оборудования или поддержка бренда | `economy-service`, `telemetry` |
| A5 | Overnight Logistics | Закулисье острова | Стелс-проверки `stealth.check`, `hacking.min` | Выбор: поддержка артистов, помощь Axiom или Tide Syndicate | `quest-engine`, `combat-lite` |
| A6 | Granville Forum | Финал | Суммарные KPI культуры и экономики | Решение Совета острова, награждение, изменение глобальных флагов | `world-state`, `event-bus` |

---

## Ветвления и исходы
- **Artisan Revival (Good Ending)**  
  - Условие: KPI `culture.score >= 80`, `illegal.actions <= 1`.  
  - Эффект: Unlock blueprint `crafting.recipe.granville-stage`, повышение рейтинга `social.reputation +15`, открытие еженедельных ивентов с легальными продажами.
- **Corporate Showcase (Neutral Ending)**  
  - Условие: Заключён контракт с Axiom, `economy.margin >= 25%`, минимальный саботаж.  
  - Эффект: Доступ к высокодоходным заказам, рост налога `tax.rate +2%`, снижение `culture.score -15`, открытие миссий corporate PR.
- **Tide Syndicate Takeover (Shadow Ending)**  
  - Условие: Поддержка Tide Syndicate, саботаж склада, успешный stealth в A5.  
  - Эффект: Разблокировка теневых поставок (`black-market` каталог), `economy.blackMarketAccess = true`, глобальный риск рейдов NCPD.
- **Failure / Early Exit**  
  - При провале ключевых проверок или нарушении лимита по времени (`time > 6h`).  
  - Эффект: Репутация -10, событие `event.quest.fail.granville`, квест повторим через 72 внутриигровых часа.

---

## Системные интеграции
- **quest-engine (narrative-service)** — управление состояниями, ветвлениями, журналом задач.
- **economy-service** — расчёт цен на торговых точках, налоги, контракты с Axiom и Tide Syndicate.
- **social-service** — трекинг репутации, реакции NPC, разблокировка ивентов.
- **gameplay-service** — проверки навыков (crafting/performance/stealth), мини-игры дегустации, стелс сегменты.
- **world-service** — обновление мировых флагов (атмосфера района, доступность Aquabus).
- **telemetry-service** — KPI для аналитики событий.

---

## Требования к API
### REST (`api/v1/narrative/quests/america/vancouver/granville-island.yaml`)
- `GET /quests/granville-island` — состояние квеста, активные цели, доступные ветви.
- `POST /quests/granville-island/actions` — выполнение этапов (payload включает `stageId`, `skillCheck`, `choiceId`).
- `POST /quests/granville-island/contracts` — оформление соглашений (Axiom, Artisan, Tide).
- `POST /quests/granville-island/outcome` — фиксация концовки и начисление наград.

### Events (EventBus)
- `quest.granville.stage.completed` — `{ stageId, success, skillCheckResult, timestamp }`.
- `quest.granville.contract.signed` — `{ contractType, margin, reputationDelta }`.
- `quest.granville.outcome.changed` — `{ outcome, cultureScore, economyScore }`.

### WebSocket Streams
- `/ws/quests/granville/live` — обновления активности и реакции NPC в реальном времени для UI.

---

## Данные и хранение
- **Tables**  
  - `narrative.quest_state` (`questId`, `playerId`, `stage`, `branch`, `progress`, `timeout`).  
  - `economy.vendor_contracts` (`vendorId`, `playerId`, `margin`, `complianceScore`).  
  - `social.reputation_log` (`playerId`, `source`, `delta`, `reason`).
- **Reference Data**  
  - Каталог NPC (`npc.rina`, `npc.milo`, `npc.sol`).  
  - Каталог сцен (`scene.granville.aquabus`, `scene.granville.market`, ...).  
  - KPI схемы (`kpi.culture.score`, `kpi.economy.margin`, `kpi.community.trust`).

---

## Награды и KPI
| Метрика | Условие | Значение |
| --- | --- | --- |
| `XP` | Завершение квеста | 2 400 |
| `culture.score` | Успешные арт-события, поддержка ремесленников | +35 |
| `economy.margin` | Заключённые контракты, продажи | +15% (cap 30%) |
| `reputation.social` | Artisan Ending / Corporate Ending | +15 / +5 |
| `black-market.access` | Tide Ending | true |
| Ачивки | `granville.artisan.champion`, `granville.corporate.curator`, `granville.tide.shadow` | В зависимости от финала |

Дополнительно начисляется валютная награда: `Eurodollars +600` при корпоративной ветке, `Eurodollars +250` при ремесленной, `Contraband Tokens +3` при теневой.

---

## Проверки и условия
- Таймер этапа A2 — 45 минут реального времени, нарушает доступ к VIP событию.
- Порог `hacking.skill >= 40` открывает скрытый терминал Tide Syndicate.
- Порог `performance.skill >= 35` позволяет сыграть лайв-сет и получить бонусный `culture.score +10`.
- `compliance.score` отслеживает нарушения санитарных норм (3 нарушения → штраф, переход в нейтральную концовку).

---

## UX и фронтенд
- UI модуль `modules/narrative/quests` отображает прогресс и доступные ветви по дорожной карте (timeline view).
- Для A3 используется мини-игра с drag-and-drop элементов крафта, данные из `crafting-service`.
- WebSocket обновления выводят реакцию толпы и спрос на товары, используются компонентами `quests-live-widget`.

---

## Связанные документы
- `.BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-010-most-livable-city.md`
- `.BRAIN/02-gameplay/economy/player-market/player-market-core.md`
- `.BRAIN/04-narrative/quest-system.md`
- `.BRAIN/02-gameplay/social/npc-hiring-world-impact-детально.md`

---

## Следующие шаги для API задач
- Подготовить в `brain-mapping.yaml` привязку к каталогу `api/v1/narrative/quests/america/vancouver/granville-island.yaml`.
- Сформировать задачу ДУАПИТАСК на создание REST/WS/Event спецификаций (narrative-service + economy/social интеграции).
- Сообщить менеджеру о необходимости синхронизации с фронтендом: live widgets, контрактный UI и карта острова.
