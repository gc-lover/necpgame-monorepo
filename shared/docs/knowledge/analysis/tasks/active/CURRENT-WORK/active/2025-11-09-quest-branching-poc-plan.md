# Quest Branching Liquibase PoC

**Статус:** in-progress  
**Версия:** 0.1.0  
**Дата создания:** 2025-11-09  
**Последнее обновление:** 2025-11-09 14:35  
**Приоритет:** высокий  
**Автор:** AI Manager

---

## Цель

Проверить полный цикл миграций `quest-branching-v1`, убедиться в корректности shadow-write, материализованных представлений и RLS, подготовить отчёт для последующей постановки задач.

---

## Подготовка среды

- PostgreSQL 15+ с расширением `pgcrypto` (для UUID).
- Отдельная база `quest_branching_poc`.
- Права superuser для запуска RLS и ролей.
- Liquibase 4.17+ и `liquibase.properties` с подключением к PoC-базе.
- Скрипты в `scripts/poc/quest-branching/` (см. `README.md`, `run_poc.ps1`, SQL-файлы).

---

## Последовательность

1. **Очистка**  
   - `DROP SCHEMA public CASCADE; CREATE SCHEMA public;`  
   - Выполнить базовые миграции проекта (если требуются зависимости).

2. **Запуск миграций**  
   - `liquibase update --tag=quest-branching-v1`.
   - Зафиксировать время выполнения (ожидаемо < 90 секунд).

3. **Проверка shadow-write**  
   - Вставить данные в `quests` и `quest_progress` (старт/завершение квеста).  
   - Убедиться, что `quest_branches` создаёт запись `default`.  
   - Проверить появление флага `quest:<id>:completed` в `player_flags`.

4. **Материализованное представление**  
   - Выполнить `REFRESH MATERIALIZED VIEW CONCURRENTLY quest_path_popularity;`.  
   - Убедиться, что записи отражают популярность путей.

5. **RLS**  
   - `SET app.current_character_id = '<uuid>';`  
   - Проверить, что `SELECT * FROM quest_progress;` возвращает строки только текущего персонажа.  
   - Убедиться, что пользователи без установки параметра получают пустой результат.

6. **Rollback**  
   - `SELECT quest_branching_rollback();`.  
   - Проверить, что триггеры отключены, MV удалено, политики RLS сняты.  
   - `liquibase rollbackOneTag quest-branching-v1`.

7. **Повторный цикл**  
   - Использовать `run_poc.ps1` (выполнять шаги последовательно) или повторить п.п.2–6 вручную.

---

## Метрики

- Время `liquibase update` и `rollback`.  
- Время `quest_branching_rollback()` (ожидаемо < 2 сек).  
- Проверка наличия записей в `quest_path_popularity` и актуальности процентов.  
- Количество записей в `player_flags` после завершения квеста.

---

## Результаты PoC (2025-11-09 13:01)

- `liquibase update` (docker liquibase/liquibase) — 58 changeSet, ~55 сек, выполнено без ошибок после правок (`BRIN`/`GIN` индексы через SQL, функции/роллы через `splitStatements="false"`).
- Скрипт `sample_data.sql` — создаёт тестовый квест, ветку и прогресс (`ON CONFLICT` приведён к PK/уникальным ограничениям).
- `quest_path_popularity` обновляется командой `REFRESH MATERIALIZED VIEW` (без `CONCURRENTLY`, уникальный индекс на `(quest_id, chosen_path)`).
- RLS-политики работают (при установке `app.current_character_id` видим 1 запись; без параметра — суперпользователь всё равно видит строки, требуется non-superuser для полной проверки).
- `quest_branching_rollback()` — отключает триггеры, снимает политики и роли (через revoke) без ошибок.
- `liquibase rollback --tag=quest-branching-v1` — корректно выполняется (из-за тега после апдейта откат 0 changeSet; для чистого отката использовать helper + `drop database`).
- Подготовлены скрипты `run_poc.ps1`, `refresh_mv.sql`, `sample_data.sql`, `check_rls.sql`, `bootstrap` (через psql) + `liquibase.properties`.

### Замеченные доработки
- `quest_branching_rollback()` требовал замены `DISABLE TRIGGER IF EXISTS` на проверку через `pg_trigger` и добавление `REVOKE ...` перед `DROP ROLE`.
- Для PoC необходима предварительная схема (минимальные `quests`, `quest_progress`, `characters`, `factions`, `npcs`).
- Liquibase 5.0.1 не поддерживает `indexType` — индексы BRIN/GIN переведены на `sql` блоки.
- Команда `rollbackOneTag` недоступна в CLI 5.0.1 — используем `rollback --tag=...`.

### Следующие шаги
- Автоматизировать прогон (вынести bootstrap в отдельный SQL, добавить шаг `tag`/`rollback` в `run_poc.ps1`).
- Зафиксировать метрики в отчёте (таблица update/refresh/rollback, вердикт по RLS).
- При необходимости расширить PoC данными и прогнать нагрузки (k6 профили).

---

## Отчёт

- Таблица с метриками (update, refresh, rollback).  
- Логи команды `liquibase update` и `rollback`.  
- Список найденных проблем (если есть) с рекомендациями.

---

## Следующие шаги

- Выполнить прогон и собрать отчёт.  
- На основании результата актуализировать `readiness-tracker.yaml` и подготовить пакет задач в API-SWAGGER.


