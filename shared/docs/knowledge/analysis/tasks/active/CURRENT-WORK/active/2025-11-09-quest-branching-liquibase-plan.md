# Liquibase план: ветвящиеся квесты

**Статус:** in-progress  
**Версия:** 0.1.7  
**Дата создания:** 2025-11-09  
**Последнее обновление:** 2025-11-09 14:22  
**Приоритет:** высокий  
**Автор:** AI Manager

---

## Цели

- Разбить структуру из `2025-11-06-quest-branching-database-design.md` на чёткие Liquibase changeSet.
- Подготовить последовательность внедрения с возможностью shadow-write и отката.
- Обеспечить совместимость с текущими таблицами `quests` и `quest_progress`.

---

## Структура changelog

```
scripts/migrations/quest-branching/
  ├─ master.xml
  ├─ v1/
  │   ├─ 01-create-core-tables.xml
  │   ├─ 02-create-branching-tables.xml
  │   ├─ 03-create-world-state-tables.xml
  │   ├─ 04-indexes.xml
  │   ├─ 05-shadow-triggers.xml
  │   ├─ 06-materialized-views.xml
  │   ├─ 07-rls-and-roles.xml
  │   └─ 08-rollback-scripts.xml
  └─ README.md
```

`master.xml` подключает файлы по порядку и помечается тегом `quest-branching-v1`.

---

## Подробности changeSet

### 1. `01-create-core-tables.xml`

- Расширение `quests` (новые поля, default значения).
- Расширение `quest_progress`.
- Создание базовых ENUM через `CREATE TYPE` (если используется).
- Добавление служебных столбцов `version`, `has_branches`, `dialogue_tree_root`.
- Rollback: удаление новых столбцов (через `dropColumn`).

### 2. `02-create-branching-tables.xml`

- Таблицы: `quest_branches`, `dialogue_nodes`, `dialogue_choices`, `skill_checks`.
- Первичные ключи, внешние ключи, ограничения уникальности.
- Rollback: `dropTable` с `cascadeConstraints="true"`.

### 3. `03-create-world-state-tables.xml`

- Таблицы: `player_quest_choices`, `player_flags`, `world_state`, `world_state_history`, `territory_control`, `npc_states`, `quest_consequences`, `player_quest_consequences`.
- Дополнительные ссылки на `characters`, `factions`, `npcs`.
- Rollback: каскадное удаление таблиц.

### 4. `04-indexes.xml`

- Индексы из дизайн-документа (B-tree, GIN, BRIN).
- Создание `BRIN` на `quest_progress(last_interaction_at)`.
- Создание `GIN` на JSONB полях.
- Rollback: `dropIndex`.

### 5. `05-shadow-triggers.xml`

- Функции `fn_sync_quests_shadow`, `fn_sync_quest_progress_shadow`.
- Триггеры `AFTER INSERT/UPDATE/DELETE` на старые таблицы для записи в новые.
- Rollback: `DROP TRIGGER`, `DROP FUNCTION`.

### 6. `06-materialized-views.xml`

- `quest_path_popularity` + уникальный индекс.
- Следом changeSet с `REFRESH MATERIALIZED VIEW`.
- Rollback: `dropMaterializedView`.

### 7. `07-rls-and-roles.xml`

- Создание ролей `game_server_role`, `analytics_role`, `quest_admin_role`.
- Выдача прав на новые таблицы.
- Включение RLS для `quest_progress`, `player_quest_choices`, `player_flags`.
- Политики `USING (character_id = current_setting('app.current_character_id')::UUID)`.
- Rollback: `DROP POLICY`, `DROP ROLE`.

### 8. `08-rollback-scripts.xml`

- Snapshot для отката (выполнение `DELETE` из shadow таблиц, выключение триггеров).
- Удаление новых столбцов, если нужно вернуться к прежней схеме.

---

## Shadow-write детали

- Функции пишут в новые таблицы только при наличии соответствующих записей.
- Для `DELETE` используется хранение ID в `deleted_rows` и последующий `DELETE` в shadow таблицах.
- Триггеры оборачиваются в `WHEN (TG_OP = 'INSERT' OR TG_OP = 'UPDATE' ...)`.
- Вводим параметр `SET LOCAL quest_branching.migration = 'enabled'` для контроля экспериментов.

---

## Порядок деплоя

1. Запустить `liquibase update --tag=quest-branching-v1` на стенде.
2. Включить shadow-write, проверить Auditing (сравнение `quests` и `quest_branches`).
3. Прогнать импорт JSON (PoC).
4. После валидации — запустить на staging/prod.

---

## Требуемые параметры Liquibase

- `liquibase.hub.mode=off`.
- `liquibase.command.tablespace.default=gameplay`.
- `liquibase.command.rollbackOnError=true`.
- Контрольная сумма: включить автообновление (default).

---

## Метрики проверки

- Сравнить количество записей в старых и новых таблицах.
- Проверить, что триггеры не увеличивают время транзакции > 5%.
- Убедиться, что `quest_path_popularity` обновляется < 10 секунд.

---

- 0.1.7 (2025-11-09 14:22) – добавлен `v1/07-rls-and-roles.xml`, обновлены master.xml и README.
- 0.1.6 (2025-11-09 14:05) – добавлен `v1/06-materialized-views.xml`, обновлены master.xml и README.
- 0.1.5 (2025-11-09 13:52) – добавлен `v1/05-shadow-triggers.xml`, обновлены master.xml и README.
- 0.1.4 (2025-11-09 13:36) – добавлен `v1/04-indexes.xml`, обновлены master.xml и README.
- 0.1.3 (2025-11-09 13:21) – добавлен `v1/03-create-world-state-tables.xml`, master.xml и README обновлены.
- 0.1.1 (2025-11-09 12:56) – создан master.xml, README и файл `v1/01-create-core-tables.xml`, обновлено время.
- 0.1.2 (2025-11-09 13:00) – добавлен `v1/02-create-branching-tables.xml`, master.xml обновлён.
- 0.1.0 (2025-11-09 12:53) – черновой план changelog + порядок деплоя.


