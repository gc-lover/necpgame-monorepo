# Combat Stealth Brief

**Приоритет:** high  
**Статус:** draft  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 14:35  
**Связанный документ:** `.BRAIN/02-gameplay/combat/combat-stealth.md`

---

## 1. Сводка готовности
- **OpenAPI каталог:** `api/v1/gameplay/combat/stealth.yaml` (микросервис `gameplay-service`, порт 8083).
- **Фронтенд модуль:** `modules/combat/stealth`.
- **Документ:** версия 1.1.0, статус `approved`, `api-readiness: ready` (проверено 2025-11-09 02:49).
- **Основные механики:** каналы обнаружения (визуальное, аудио, технологическое, сетевое), импланты, социальная инженерия, взаимодействие с киберпространством, система ачивок/наград за стелс.

---

## 2. REST backlog
| Endpoint | Назначение | Приоритет | Примечания |
| --- | --- | --- | --- |
| `POST /combat/stealth/actions` | Выполнение стелс-действия (move, hide, distract, takedown) с расчётом обнаружения | P0 | Возвращает `detectionLevel`, `reputationImpact`, `achievementProgress` |
| `POST /combat/stealth/detection` | Принудительный пересчёт уровня обнаружения (GM/скрипт) | P0 | Используется для сценариев/квестов |
| `GET /combat/stealth/profile` | Настройки стелс-параметров персонажа (импланты, улучшения, навыки) | P0 | |
| `PUT /combat/stealth/profile` | Обновление активных модификаторов (перки, экипировка) | P1 | |
| `POST /combat/stealth/disguise` | Включение маскировки (тип, длительность, потребление ресурса) | P1 | |
| `POST /combat/stealth/hack` | Взаимодействие с сетью камер/датчиков (отключение, взлом, перенастройка) | P1 | Интеграция с `combat-hacking` |
| `POST /combat/stealth/achievement` | Логирование прогресса стелс-ачивок (без обнаружения, без убийств) | P1 | |
| `GET /combat/stealth/detection-map` | Текущая карта угроз (каналы обнаружения, активные сенсоры) | P2 | |
| `POST /combat/stealth/reputation` | Применение репутационных последствий (если стелс провален) | P2 | |

### REST требования
- Любое действие возвращает новый `detectionLevel`, `suspicion`, `canRetry`, `reputationDelta`.
- API `disguise` должно учитывать тип маскировки (NPC, толпа, кибер-невидимость) и взаимодействие с имплантами (энергия, перегрев).
- `hack` маршруты используют структуру сетей (зоны, фракции), отслеживают уровень взлома и реакцию безопасности.

---

## 3. WebSocket
- `wss://api.necp.game/v1/combat/stealth/{sessionId}` — поток изменений обнаружения, статусов маскировки, тревог и ачивок.
- Payload: `characterId`, `detectionChannel`, `level`, `source`, `timestamp`, `achievementProgress`, `reputationImpact`.
- Опциональный канал `player/{accountId}` для уведомлений вне боевых сессий (например, квестовые сцены).

---

## 4. Event Bus
- `combat.stealth.action` — логирует каждое действие (тип, успех, detection delta, использованные импланты/навыки).
- `combat.stealth.detection` — изменение уровня обнаружения (консьюмеры: AI, analytics, quest-engine).
- `combat.stealth.disguise` — события маскировки (включение/выключение, тип, ресурс).
- `combat.stealth.hack` — результат взаимодействия с сетью датчиков/камер.
- `combat.stealth.achievement` — обновление прогресса/награды.
- Входящие события: `combat.hacking.alert`, `npc.alert`, `quest.trigger`, `social.reputation.update`, `environment.alert`.

---

## 5. Storage
- `stealth_actions` — журнал действий (`actionType`, `channel`, `success`, `detectionBefore/After`, `achievementFlags`).
- `stealth_profiles` — настройки персонажей (`implants`, `perks`, `equipment`, `preferredChannel`).
- `stealth_detection_state` — текущие уровни обнаружения по каналам (`visual`, `audio`, `tech`, `network`).
- `stealth_disguises` — активные маскировки (тип, источник, время, ресурс).
- `stealth_network_nodes` — состояние камер/датчиков (статус, владение, взлом, восстановление).
- `stealth_achievements` — прогресс и награды.
- `stealth_reputation_buffs` — записи об отмене/уменьшении репутационных штрафов при чистом прохождении.

---

## 6. Зависимости
- `combat-hacking` — отключение камер/датчиков, сетевые реакции.
- `combat-implants-types` — импланты стелса (невидимость, снижение сигнатур).
- `combat-abilities` — стелс-атаки, мобильные способности и эффекты.
- `combat-freerun` — побег/уклонение после обнаружения.
- `quest-engine` — триггеры и бонусы за стелс-прохождения.
- `social-service` — репутация и реакции NPC.
- `analytics-service` — мониторинг качества стелса, каналов обнаружения, достижений.
- `economy-service` — награды/штрафы за стелс (если требуется).

---

## 7. Требования и ограничения
- Поддержка различных уровней обнаружения (подозрение, частичное, полное) с таймерами восстановления.
- Система маскировок должна учитывать конфликты имплантов и источников (не все маскировки совместимы).
- Ачивки и награды: хранить параметры (без обнаружения, без убийств, время прохождения).
- Интеграция с киберпространством: `stealth.hack` должен обновлять как физические, так и виртуальные сенсоры.
- Логирование всех GM/скриптовых операций (динамический пересчёт, выдача наград).
- Поддержка анти-чит: валидация невозможных перемещений, некорректных уровней маскировки.

---

## 8. Следующие шаги
1. Согласовать с combat wave необходимость объединённого WebSocket (stealth/freerun/combat) или оставить отдельным.
2. При появлении окна у ДУАПИТАСК — разбить задачи по этапам: P0 (actions/detection/core events), P1 (disguise/hack/achievements), P2 (reputation, advanced analytics).
3. Обновить `TODO.md`, `current-status.md`, `readiness-tracker.yaml` после передачи брифа.
4. Свериться с `combat-hacking` и `quest-engine` пакетами, чтобы унифицировать структуру сетей/флагов.

---

## История
- 2025-11-09 14:35 — создан черновик брифа для стелс-системы на основе `.BRAIN/02-gameplay/combat/combat-stealth.md`.

