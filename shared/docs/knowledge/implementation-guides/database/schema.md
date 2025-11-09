# Database Schema — MVP

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Data Guild  
**Связанные документы:** `migrations.md`, `mvp-initial-data.md`, `gameplay-service` ERD

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08
**api-readiness-notes:** Определены сущности, связи и индексы для MVP. Готово к генерации миграций и реализации в BACK-JAVA.

---

## 1. Обзор

- БД: PostgreSQL 15.
- Схема: `mvp_core` (gameplay, social, economy), `mvp_meta` (аудит, ETL).
- Принципы: нормализация до 3НФ, soft-delete через `deleted_at`, аудиторские поля `created_at`, `updated_at`.

## 2. ERD (ключевые сущности)

- `player_account` — учётная запись игрока.
- `character` — персонаж, связь с аккаунтом.
- `weapon_profile` — конфигурация оружия и модулей.
- `ballistics_metric` — результаты симуляций.
- `order` — заказ игрока/фракции.
- `order_phase` — этапы заказа.
- `order_application` — заявки исполнителей.
- `order_review` — отзывы.
- `crafting_blueprint` — чертежи.
- `crafting_job` — активные крафтовые задания.
- `world_district_state` — показатели `city.unrest`.

## 3. Таблицы

### 3.1 `player_account`
- `id UUID PK`
- `email text unique`
- `password_hash text`
- `status enum('active','banned','pending')`
- `created_at timestamptz`
- `updated_at timestamptz`

### 3.2 `character`
- `id UUID PK`
- `account_id UUID FK -> player_account`
- `origin enum`
- `class enum`
- `faction enum`
- `level int`
- `created_at`, `updated_at`

### 3.3 `weapon_profile`
- `id UUID PK`
- `character_id UUID FK`
- `weapon_type enum`
- `mods jsonb`
- `stats jsonb`
- `created_at`, `updated_at`

### 3.4 `ballistics_metric`
- `id UUID PK`
- `weapon_profile_id UUID FK`
- `trajectory jsonb`
- `ricochet_chain jsonb`
- `damage_output numeric`
- `energy_usage numeric`
- `created_at`

### 3.5 `order`
- `id UUID PK`
- `creator_id UUID`
- `order_type enum`
- `title text`
- `description text`
- `reward jsonb`
- `deadline timestamptz`
- `state enum('draft','published','in_progress','completed','dispute','cancelled')`
- `access_level enum('public','faction','private')`
- `reputation_gate jsonb`
- `escrow_amount numeric`
- `created_at`, `updated_at`

### 3.6 `order_phase`
- `id UUID PK`
- `order_id UUID FK`
- `sequence int`
- `name text`
- `requirements jsonb`
- `status enum('pending','active','done','failed')`
- `updated_at`

### 3.7 `order_application`
- `id UUID PK`
- `order_id UUID FK`
- `applicant_id UUID`
- `message text`
- `bid jsonb`
- `status enum('submitted','accepted','rejected','withdrawn')`
- `created_at`

### 3.8 `order_review`
- `id UUID PK`
- `order_id UUID FK`
- `author_id UUID`
- `target enum('customer','contractor')`
- `rating smallint`
- `comment text`
- `created_at`

### 3.9 `crafting_blueprint`
- `id UUID PK`
- `category enum`
- `tier enum`
- `requirements jsonb`
- `output jsonb`
- `created_at`, `updated_at`

### 3.10 `crafting_job`
- `id UUID PK`
- `order_id UUID FK`
- `blueprint_id UUID FK`
- `owner_id UUID`
- `status enum('queued','processing','completed','failed')`
- `materials jsonb`
- `started_at`, `completed_at`

### 3.11 `world_district_state`
- `id UUID PK`
- `district_code text unique`
- `unrest_level smallint`
- `modifiers jsonb`
- `updated_at`

## 4. Индексы и ограничения

- Уникальные индексы: `UX_order_title_owner`, `UX_weapon_profile_character_type`.
- Частичные индексы по `state` для заказов и крафтов.
- Проверки целостности для JSON полей через `CHECK` (`jsonb_typeof`).

## 5. Аудит и метрики

- Таблица `mvp_meta.outbox` для Kafka транзакций.
- Логирование в `mvp_meta.event_log` (event_id, payload, status).

## 6. Следующие шаги

- [x] Подготовить ERD (dbdiagram).
- [x] Зафиксировать таблицы и связи.
- [ ] Сгенерировать SQL миграции (`migrations.md`).
