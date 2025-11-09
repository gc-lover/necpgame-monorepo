# Combat Combos & Synergies Brief

**Приоритет:** high  
**Статус:** draft  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 15:05  
**Связанный документ:** `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`

---

## 1. Сводка готовности
- **OpenAPI каталог:** `api/v1/gameplay/combat/combos-synergies.yaml` (микросервис `gameplay-service`, порт 8083).
- **Фронтенд модуль:** `modules/combat/combos`.
- **Статус документа:** approved, `api-readiness: ready` (проверено 2025-11-09 02:48).
- **Область:** комбо способности, командные синергии, оборудoвание/импланты, тайминговые комбинации, scoring.

---

## 2. REST backlog
| Endpoint | Назначение | Приоритет | Примечания |
| --- | --- | --- | --- |
| `GET /combat/combos/catalog` | Каталог доступных комбо (solo/team/equipment/implant/timing) | P0 | Возвращает описание, требования, бонусы, сложность |
| `GET /combat/combos/{comboId}` | Детальная информация о конкретном комбо | P0 | |
| `POST /combat/combos/activate` | Регистрация активации комбо (персонаж/команда) | P0 | Payload: `comboId`, `participants`, `context`, `success`, `scoring` |
| `POST /combat/combos/synergy` | Применение синергии (ability ↔ ability / equipment / implant) | P0 | Возвращает модификаторы, cooldown adjustments |
| `GET /combat/combos/loadout` | Настройка активных комбо/синергий персонажа | P1 | |
| `POST /combat/combos/loadout` | Обновление конфигурации комбо и приоритетов | P1 | |
| `POST /combat/combos/score` | Отправка результатов scoring (execution, damage, coordination) | P1 | Используется для рейтингов |
| `GET /combat/combos/analytics` | Метрики эффективности комбо (для баланс/аналитики) | P2 | |

### REST требования
- `activate` должен проверять доступность комбо (cooldowns, условия, участники), обновлять состояние и публиковать события.
- Синергии должны учитывать мультипликаторы урона, cooldown reduction, киберпсихоз.
- Scoring хранит параметры: `executionDifficulty`, `damageOutput`, `visualImpact`, `teamCoordination`.

---

## 3. WebSocket
- `wss://api.necp.game/v1/combat/combos/{sessionId}` — live поток активированных комбо/synergy (для UI, аналитики).
- Payload: `comboId`, `participants`, `success`, `bonusEffects`, `score`, `chain`.
- Возможный дополнительный канал `player/{characterId}` для персональных уведомлений о доступности комбо.

---

## 4. Event Bus
- `combat.combo.activated` — базовое событие (участники, тип, бонусы, damage multiplier).
- `combat.combo.synergy` — фиксация применения синергии (ability/equipment/implant).
- `combat.combo.failure` — для аналитики/баланса (неудачные попытки).
- `combat.combo.score` — итоговый рейтинг и очки.
- Входящие события: `combat.ability.activated`, `combat.freerun.actions`, `combat.stealth.action`, `quest.trigger`, `combat.ai.state`.

---

## 5. Storage
- `combo_catalog` — описание всех комбинаций (тип, требования, бонусы, сложность, cooldown).
- `combo_activations` — журнал активаций (`comboId`, `participants`, `success`, `bonus`, `score`, `sessionId`, `timestamp`).
- `combo_synergies` — связи между способностями/имплантами/экипировкой.
- `combo_loadouts` — настройки персонажей по активным комбо и приоритетам.
- `combo_scoring_history` — хранение результатов scoring (для рейтингов и экономики).
- `combo_failure_log` — неудачные попытки и причины.

---

## 6. Зависимости
- `combat-abilities` — активации и синергии способностей.
- `combat-implants-types`, `combat-shooting`, `combat-stealth`, `combat-freerun` — использование в контекстах.
- `progression-backend` — наличие навыков/классовых бонусов.
- `quest-engine` — квестовые условия, награды за Legendary combos.
- `analytics-service` — статистика использования и эффективности комбо.
- `economy-service` — награды/рейтинги за комбо (если применимо).

---

## 7. Требования / ограничения
- Валидация участников: каждый combo имеет минимум/максимум участников, роли.
- Управление cooldown: комбинации могут сокращать КД способностей, нужен учёт.
- Поддержка chain-комбо (Combo -> Combo), мультипликаторы.
- Логирование критических combo (Legendary) для наград и античита.
- Поддержка рейтингов и achievements (комбо scoring, категории Bronze→Diamond).
- Возможность расширения каталога без перезапуска сервиса (конфигурационные таблицы).

---

## 8. Следующие шаги
1. Синхронизировать с combat abilities/freerun/stealth брифами для унифицированных payload (особенно события).
2. При появлении окна у ДУАПИТАСК разбить задачи: Stage P0 (catalog/activate/synergy), Stage P1 (loadout/score), Stage P2 (analytics/rating).
3. Обновить `TODO.md`, `current-status.md`, `readiness-tracker.yaml` после передачи брифа.
4. Определить взаимодействия с `combat-ai-enemies` (combo реакции врагов) и quest achievements.

---

## История
- 2025-11-09 15:05 — создан черновик брифа для комбо/синергий на основе `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`.

