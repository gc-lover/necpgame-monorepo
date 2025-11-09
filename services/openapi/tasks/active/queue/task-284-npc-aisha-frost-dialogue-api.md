# Task ID: API-TASK-284
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 04:20  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-280 (faction social dialogues API), API-TASK-283 (quest branching database API), API-TASK-273 (seasonal events schedule API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-aisha-frost-dialogue.yaml`, охватывающую интерактивный диалог с Айшей Фрост (координатор Neon Ghosts). Диалог опирается на состояния (scout/trusted/corporate/exposed/crisis/specter), репутации, флаги и мировые события (Underlink Lockdown, Helios pressure, Maelstrom double agents). Нужно реализовать REST/WS контуры narrative-service с интеграцией в social-, world-, economy- и quest-сервисы.

---

## 🎯 Цель задания

Обеспечить:
- Каталог состояний NPC (`state`, `entry conditions`, `required flags`) с доступными приветствиями и ветками
- Управление узлами `scout-evaluation`, `underlink-brief`, `trusted-routing`, `corporate-ultimatum`, `crisis-directives`, `specter-directive` и дальнейшими ответвлениями
- Поддержку проверок статов, ресурсов, бафов, событий и модификаций репутаций
- Синхронизацию с quest branching (Neon Ghosts deliveries), world events (city unrest, Underlink), economy modifiers и Specter статусы
- Телеметрию: кто инициирует, какие ветки/опции выбираются, исходы проверок и последствия (flags/events/rewards)

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-aisha-frost.md` — состояния, узлы, YAML-структуры, проверки
- Дополнительно:
  - `.BRAIN/04-narrative/dialogues/faction-social-lines.md`
  - `.BRAIN/04-narrative/quests/side/2025-11-07-quest-neon-ghosts.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/02-gameplay/world/seasonal-events-2020-2093.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-aisha-frost.yaml`  
**Микросервис:** narrative-service (ядро диалога)  
**Интеграции:** social-service (репутации), world-service (события/флаги), economy-service (баффы/награды), gameplay-service (квесты Neon Ghosts), analytics-service (телеметрия), notification-service (UI и Specter оповещения)  
**Frontend:** `modules/narrative/side-quests`, overlays для Specter/Affinity панелей

---

## 🧩 Обязательные секции

1. Метаданные NPC  
   - `GET /api/v1/narrative/dialogues/aisha-frost` — текущие состояния, доступные узлы, активные события, cooldown’ы

2. Управление состоянием  
   - `POST /api/v1/narrative/dialogues/aisha-frost/state/resolve` — вычисление состояния по репутациям/флагам (`scout`, `trusted`, `corporate`, `exposed`, `crisis`, `specter`)
   - `POST /api/v1/narrative/dialogues/aisha-frost/state/override` — GM-инструмент (lock/reset state)

3. Узлы и выборы  
   - `GET /api/v1/narrative/dialogues/aisha-frost/nodes/{nodeId}`  
   - `POST /api/v1/narrative/dialogues/aisha-frost/nodes/{nodeId}/options/{optionId}` — выполнение опции, проверка ресурсов/статов, применение outcomes (flags, buffs, events, rewards, reputation)

4. Интеграция с квестами  
   - `POST /api/v1/narrative/dialogues/aisha-frost/quests/neon-ghosts` — хук для запуска/обновления квестов Neon Ghosts  
   - `GET /api/v1/narrative/dialogues/aisha-frost/quests` — связанные поручения, статусы `underlink`, `specter`, `helios`

5. События и последствия  
   - `POST /api/v1/narrative/dialogues/aisha-frost/events/apply` — триггер world events (`neon_ghosts_night_run`, `neon_ghosts_resistance`, `neon_lockdown` и др.)  
   - `POST /api/v1/narrative/dialogues/aisha-frost/alert` — управление уровнями тревоги Helios/Maelstrom

6. WebSocket / Streaming  
   - `/ws/narrative/dialogues/aisha-frost` — `StateChanged`, `OptionExecuted`, `CheckResolved`, `EventTriggered`, `AlertLevelChanged`, `SpecterDirectiveIssued`

7. Схемы данных  
   - `AishaDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `CheckResult`, `EventPayload`, `AlertLevel`, `SpecterDirective`, `QuestHook`, `TelemetryRecord`

8. Безопасность  
   - RBAC: `player`, `specter`, `gm`  
   - Ограничения по rate limit (повторное выполнение узлов), cooldownы из документа (`cooldown: 900` и т.д.)

9. Observability  
   - Метрики: `aisha_dialogue_attempts`, `option_success_rate`, `specter_directive_completion`, `underlink_stability_delta`, `helios_alert_level`  
   - Логи и корреляция (`dialogueSessionId`, `questSessionId`, `characterId`)

10. FAQ / Edge cases  
    - Повторное обращение после провала (cooldown)  
    - Совмещение состояний (например, `specter` + `corporate`)  
    - Управление alert-уровнями, влияние на city unrest  
    - GM override, тестовые режимы

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/aisha-frost` соблюдён у всех игровых REST маршрутов.  
2. Состояния и условия соответствуют документу (`scout`, `trusted`, `corporate`, `exposed`, `crisis`, `specter`) с нужными флагами и репутациями.  
3. Проверки статов и ресурсов (Persuasion, Strategy, Logistics, Negotiation, Resolve, Leadership, Investigation и т.д.) поддерживают DC и модификаторы (флаги, предметы, репутации).  
4. Outcomes реализуют все эффекты: unlock node, события, бафы, spawn companion, alert level, reputation delta, городские модификаторы.  
5. Интеграции с world events, quest hooks и Specter системой возвращают подтверждения и ошибки (Error schema).  
6. Поддержан мониторинг alert уровней Helios/Maelstrom и Underlink stability.  
7. WebSocket payload включает `state`, `nodeId`, `optionId`, `check`, `outcome`, `alertLevel`, `eventKey`.  
8. Документированы лимиты (cooldowns, ограничения на элитные поручения, условия Specter).  
9. Target Architecture описывает взаимодействие narrative-service с social/world/analytics/gameplay и UI (`modules/narrative/side-quests`).  
10. FAQ покрывает edge cases: возвращение после Lockdown, конфликтные флаги (double agent + specter), одновременные GM операции.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

