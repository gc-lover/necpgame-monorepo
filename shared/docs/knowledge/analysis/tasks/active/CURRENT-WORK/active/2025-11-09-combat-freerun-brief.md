# Combat Freerun Brief

**Приоритет:** high  
**Статус:** draft  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 14:15  
**Связанный документ:** `.BRAIN/02-gameplay/combat/combat-freerun.md`

---

## 1. Сводка готовности
- **OpenAPI каталог:** `api/v1/gameplay/combat/freerun.yaml` (микросервис `gameplay-service`, порт 8083).
- **Фронтенд модуль:** `modules/combat/movement`.
- **Статус документа:** approved, версия 1.1.0 (проверено 2025-11-09 03:20). Блокирующих TODO нет, балансные уточнения не мешают API.
- **Ключевые особенности:** смешанный паркур (авто + ручной), выносливость как ресурс, интеграция с боевыми системами (атаки с воздуха, мобильные способности, уклонения), поддержка имплантов и навыков.

---

## 2. REST (черновой backlog)
| Приоритет | Endpoint | Описание | Источник |
| --- | --- | --- | --- |
| P0 | `POST /combat/freerun/actions` | Отправка действия паркура (jump, climb, slide, wall-run); возвращает результат, выносливость, штрафы | раздел «Интеграция с боем» |
| P0 | `POST /combat/freerun/combo` | Применение контекстного комбо (рывок-удар, таран) с проверкой имплантов и cooldown | «Комбо паркур+бой» |
| P0 | `POST /combat/freerun/stamina/recover` | Принудительный запуск восстановления выносливости (чистка состояния, баффы) | «Ограничения паркура» |
| P1 | `GET /combat/freerun/loadout` | Конфигурация паркур-навыков, имплантов и перков персонажа | «Определенные решения / Интеграция» |
| P1 | `PUT /combat/freerun/loadout` | Изменение активных манёвров, привязка к быстрым слотам | idem |
| P1 | `POST /combat/freerun/mobile-ability` | Каст мобильной версии способности (на слайде/в прыжке) с модификацией стоимости/кд | «Влияние на способности» |
| P2 | `POST /combat/freerun/routes` | Сохранение пользовательского маршрута (опционально) | «Вопросы для дальнейшей проработки» |
| P2 | `GET /combat/freerun/telemetry` | Телеметрия паркура (для аналитики/баланса) | «TODO дальнейшей проработки» |

### REST требования
- Каждое действие учитывает текущую выносливость, импланты и навыки. Сервер возвращает обновлённые значения (`stamina`, `cooldowns`, `modifiers`).
- Контекстные комбо требуют проверки положения (взаимодействие с `combat-session` и `movement`), используют физику шутера: положение, скорость, столкновения, line-of-sight. Никаких кубиков.
- API для мобильных способностей должно поддерживать связь с `combat-abilities` (идентификатор способности, новые модификаторы).

---

## 3. WebSocket
| Канал | Назначение | Payload |
| --- | --- | --- |
| `wss://api.necp.game/v1/combat/freerun/{sessionId}` | Live-поток паркур действий в боевой сессии | `actionType`, `success`, `stamina`, `position`, `comboTriggers`, `abilityOverrides` |
| `wss://api.necp.game/v1/combat/freerun/telemetry/{characterId}` | Отладочный канал для тренировок/арен | `movementVector`, `stamina`, `penalty`, `buffs` |

---

## 4. Event Bus
- `combat.freerun.action` — `characterId`, `sessionId`, `action`, `success`, `staminaBefore/After`, `implants`, `skills` (консьюмеры: analytics-service, quest-engine, combat-session).
- `combat.freerun.combo` — результат комбо, синхронизация с combat abilities и progression.
- `combat.freerun.stamina` — уведомление об истощении/восстановлении выносливости (используется AI и economy для баффов).
- `combat.freerun.mobile-ability` — события мобильных способностей (интеграция с `combat-abilities`, cooldown-management).

Входящие события: `combat.session.events` (для контекстов), `ability.used`, `implant.effect.updated`, `quest.objective-progress` (паркур-триггеры).

---

## 5. Хранение
- `freerun_actions` — журнал действий (`actionType`, `position`, `success`, `staminaCost`, `implants`, `perks`, `sessionId`).
- `freerun_profiles` — настройки персонажа (`preferredAutomationLevel`, `enabledMoves`, `bindings`, `lastUpdated`).
- `freerun_combos_history` — история комбо (тип, результат, нанесённый эффект).
- `freerun_stamina_state` — текущее значение выносливости, коэффициенты восстановления, активные баффы.
- `freerun_routes` (опционально) — сохранённые маршруты (маршруты, чекпоинты, автор, режим).

---

## 6. Зависимости
- `combat-session` — контекст боевой сессии, проверка позиций и синхронизация с raid WebSocket.
- `combat-abilities` — мобильные версии способностей, модификация стоимости/кд.
- `combat-shooter-core` (будет создан) — общие параметры движения и шутерной физики.
- `inventory` — импланты и экипировка, влияющие на паркур.
- `analytics` — получение телеметрии (`combat.freerun.*`).
- `quest-engine` — использование паркур событий в квестах (`quest:objective-progress`).
- `economy` — стоимость покупки улучшений паркура (через импланты/перки, слоты).

---

## 7. Требования/ограничения
- Реализовать анти-спам: ограничение частоты `freerun.actions`, штрафы за спам-комбо.
- Учитывать высоту/скорость при расчёте бонусов (атаки с воздуха, мобильные способности).
- Поддержка локализации названий манёвров и подсказок.
- Логи безопасности (`freerun_actions_audit`) для GM-операций и ручного изменения выносливости.
- Подготовить схему интеграции с anti-cheat (проверка невозможных траекторий).

---

## 8. Следующие шаги
1. Подтвердить с combat core необходимость WebSocket `freerun/{sessionId}` (возможно, объединить с общим combat stream).
2. При появлении окна у ДУАПИТАСК — разложить REST и события по задачам Stage P0 (actions+stamina), Stage P1 (combo/mobile abilities), Stage P2 (routes/telemetry).
3. Обновить `TODO.md`, `current-status.md`, `readiness-tracker.yaml` после передачи брифа.
4. Синхронизировать с `combat-abilities` и `quest engine` пакетами, чтобы согласовать shooter-параметры (скорость, stamina, detection) и не дублировать события.

---

## История
- 2025-11-09 14:15 — создан черновой бриф для паркур-системы на основе `.BRAIN/02-gameplay/combat/combat-freerun.md`.

