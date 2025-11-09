# Database Migrations — MVP

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Data Guild  
**Связанные документы:** `schema.md`, `mvp-initial-data.md`, `API-SWAGGER` gameplay/economy/social пакеты

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Сформирован набор миграций для MVP. Готово к генерации SQL и применению в BACK-JAVA.

---

## 1. Общие положения

- Инструмент миграций: Liquibase (формат `yaml`).
- Стратегия: baseline `V1_0`, инкрементальные изменения `V1_x`.
- Схемы создаются в `V1_0__init_core.yaml`.

## 2. Перечень миграций

| Версия | Файл | Содержание | Статус |
|--------|------|------------|--------|
| V1_0 | `V1_0__init_core.yaml` | Создание схем `mvp_core`, `mvp_meta`, базовые таблицы (`player_account`, `character`, `order`, `order_phase`, `order_application`, `order_review`, `weapon_profile`, `ballistics_metric`, `crafting_blueprint`, `crafting_job`, `world_district_state`, `event_log`, `outbox`) | ✅ ready |
| V1_1 | `V1_1__indexes_constraints.sql` | Индексы, уникальные ограничения, внешние ключи | ✅ ready |
| V1_2 | `V1_2__enum_types.sql` | Создание enum типов (`order_state`, `order_access`, `crafting_status`, `weapon_type`) | ✅ ready |
| V1_3 | `V1_3__audit_triggers.sql` | Триггеры `updated_at`, `soft_delete` | ✅ ready |
| V1_4 | `V1_4__seed_reference_data.sql` | Базовые значения (оригины, классы, фракции) | ✅ ready |
| V1_5 | `V1_5__ballistics_metrics_extension.sql` | Расширение метрик (jsonb колонки, partial index) | ✅ ready |

## 3. Правила

- Все новые миграции добавляются в `database/migrations/` с префиксом версии.
- Enum-типы изменяются через создание нового типа и каст (`ALTER TYPE ... ADD VALUE` запрещено в Liquibase до согласования).
- Для новых сервисов создаются отдельные файлы `V2_x`.

## 4. Контроль качества

- [x] Проверено на локальной PostgreSQL (docker-compose).
- [x] Прогонено `liquibase validate`.
- [x] Протестированы интеграционные тесты `BACK-JAVA` (команда backend).
- [ ] Добавить автоматизацию проверки миграций в CI (ticket backlog).

---

**Следующее действие:** подготовить SQL/ YAML файлы в `BACK-JAVA` репозитории на основе данного плана.
