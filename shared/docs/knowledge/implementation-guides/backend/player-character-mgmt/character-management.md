# Player & Character Management — сводный документ

**Статус:** approved  
**Версия:** 1.1.0  
**Дата:** 2025-11-09  
**Приоритет:** критический  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 02:47  
**api-readiness-notes:** Перепроверено 2025-11-09 02:47: CRUD персонажей, переключение, восстановление и события `Character*` подтверждены; документ готов к постановке задач для character-service.

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-097: Player Character Lifecycle API — `api/v1/characters/players/players.yaml`
    - Создано: 2025-11-09 17:05
    - Завершено: 2025-11-09 19:35
    - Доп. файлы: `players-models.yaml`, `players-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-097-character-management-api.md`
- Last Updated: 2025-11-09 19:35
---

---

## Микросервис и каталоги

- **Микросервис:** `character-service` (порт 8082)  
- **OpenAPI каталог:** `api/v1/characters/players/`  
- **Target спецификации:**  
  - `api/v1/characters/players.yaml` — управление персонажами (CRUD, восстановление)  
  - `api/v1/players/player-management.yaml` — управление профилем игрока и слотами  
  - `api/v1/players/character-switching.yaml` — переключение активного персонажа  
- **Frontend модуль:** `modules/characters`

---

## Обзор функциональности

Система управления игроками и персонажами обеспечивает полный жизненный цикл персонажей в NECPGAME:

1. **Профиль игрока:** единый аккаунт, настройки, социальные связи.  
2. **Создание/удаление персонажей:** слоты, уникальные имена, soft-delete с восстановлением.  
3. **Переключение персонажей:** сохранение состояния, загрузка нового персонажа, события `CharacterSwitched`.  
4. **Состояние мира:** сохранение прогресса, локации, инвентаря, связей с другими сервисами.  
5. **Расчёт статов:** пересчёт характеристик при изменении навыков, экипировки, баффов.  
6. **Интеграции:** события для auth/session, economy (кошельки), gameplay (боевые состояния), social (статусы онлайн).

---

## Доменные ограничения MVP

- Минимум 3 слота персонажей на аккаунт, расширения — через economy сервис.  
- Лимиты на активных персонажей в командной игре — 1 активный персонаж на сессию.  
- Поддержка номадских/корпо/стриткид origin с заранее заданными наборами атрибутов.  
- Импорт внешнего профиля запрещён; все персонажи создаются в игре.  
- Soft-delete: персонаж сохраняется 30 дней, затем переносится в архивную таблицу.  
- Переключение возможно только вне боя (проверка через gameplay-service).  
- Переход в `dead` статус требует подтверждения (механика восстановления через клинику).

---

## Ключевые сущности и таблицы

- `players`: профиль игрока, аккаунтные настройки, прогресс.  
- `characters`: базовая информация персонажа, атрибуты, навыки, прогресс.  
- `character_slots`: управление слотовой системой.  
- `character_restore_queue`: очередь восстановления удалённых персонажей.  
- `character_state_snapshots`: снимки состояния для переключения.  
- `character_equipment`, `character_inventory_refs`: ссылки на инвентарь и экипировку.  
- `character_activity_log`: аудит критичных действий.  
- Дополнительные индексы обеспечивают быстрый поиск по аккаунту, статусу, зоне.

> Полные DDL схемы и сценарии находятся в `part1-creation-deletion.md` и `part2-switching-management.md`. Настоящий документ объединяет требования и определяет границы API.

---

## Потоки данных

1. **Создание персонажа:**  
   - Проверка доступного слота → генерация базовых атрибутов → запись в `characters` → публикация события `CharacterCreated`.  
2. **Удаление персонажа:**  
   - Soft-delete с указанием `deleted`, `can_restore_until`, публикация `CharacterDeleted`.  
3. **Восстановление:**  
   - Проверка периода восстановления → копия из `character_state_snapshots` → очистка очереди восстановления.  
4. **Переключение:**  
   - Сохранение состояния текущего персонажа → загрузка нового → обновление активной сессии.  
5. **Расчёт статов:**  
   - Атрибуты + экипировка + баффы → обновление derived stats → уведомление gameplay-компонентов.

---

## API Endpoints (черновой список)

- `POST /api/v1/players/{accountId}/characters` — создание персонажа.  
- `DELETE /api/v1/players/{accountId}/characters/{characterId}` — soft-delete.  
- `POST /api/v1/players/{accountId}/characters/{characterId}/restore` — восстановление.  
- `POST /api/v1/players/{accountId}/switch` — переключение активного персонажа.  
- `GET /api/v1/players/{accountId}/characters` — список персонажей, статусы, слоты.  
- `PATCH /api/v1/players/{accountId}/characters/{characterId}/appearance` — апдейт внешности.  
- `POST /api/v1/players/{accountId}/characters/{characterId}/recalculate` — пересчёт статов.  
- `GET /api/v1/players/{accountId}/activity` — аудит действий персонажей.  
- `POST /api/v1/players/{accountId}/slots/purchase` — расширение слотов.

---

## Интеграции

- **Auth-service:** валидация токенов при создании/управлении персонажами.  
- **Session-service:** обновление активного персонажа, heartbeats, AFK.  
- **Gameplay-service:** статус `IN_COMBAT`, снаряжение, боевые навыки.  
- **Economy-service:** управление валютой, покупка слотов, синхронизация кошелька.  
- **Inventory-service:** привязка инвентаря/экипировки по ID.  
- **Social-service:** статусы друзей, приглашения в группу.  
- **Notification-service:** письма об удалениях/восстановлениях.

---

## Сценарии ошибок и валидации

- Уникальные имена персонажей (case insensitive).  
- Проверка классов и origin против справочника (`narrative-service`).  
- Ограничение по весу/инвентарю при восстановлении персонажа.  
- Запрет переключения при активных боевых событиях или незавершённом кат-сцене.  
- Автоматический backup состояния персонажа перед удалением/переключением.  
- Валидация кастомной внешности (списки доступных параметров).  
- Ошибка `429` при попытке создать >3 персонажей за короткий период (анти-спам).

---

## Требования к событиям

- `CharacterCreated`, `CharacterDeleted`, `CharacterRestored`, `CharacterSwitched`, `CharacterStatsUpdated`.  
- События содержат ссылки на персонажа, аккаунт, timestamp, список изменений.  
- Delivery: Kafka topic `characters.lifecycle.*`.  
- Подписчики: gameplay (обновление боевого состояния), economy (валюта/слоты), social (статусы), notification (уведомления).

---

## Следующие шаги для API

2. Проверить консистентность с `inventory`, `progression`, `session management`.  
3. После генерации спецификации уведомить backend/front для реализации.  
4. Обновить `brain-mapping.yaml` и `implementation-tracker.yaml` при старте работ.

---

## Связанные документы

- [Part 1 — Creation & Deletion](./part1-creation-deletion.md)  
- [Part 2 — Switching & Management](./part2-switching-management.md)  
- [Backend Architecture Overview](../README.md)  
- [Progression Backend](../progression-backend.md)  
- [Inventory System](../inventory-system/part1-core-system.md)

---

## История изменений

- **1.1.0 (2025-11-09):** Добавлен сводный документ, объединяющий Part 1/Part 2 и фиксирующий готовность для API.  
- **1.0.1 (2025-11-07):** Актуализированы части Part 1 и Part 2.  
- **1.0.0 (2025-11-06):** Исходная версия системы.

