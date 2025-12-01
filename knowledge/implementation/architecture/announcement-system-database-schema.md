<!-- Issue: #323 -->
# Announcement System - Database Schema

## Обзор

Схема базы данных для системы объявлений, управляющей новостями игры, патчноутами, событиями, промо-акциями и уведомлениями для игроков.

## ERD Диаграмма

```mermaid
erDiagram
    announcements ||--o{ player_announcement_reads : "read_by"
    announcements ||--o{ announcement_telemetry : "tracked"
    announcements ||--o| patch_notes : "has"
    character ||--o{ player_announcement_reads : "reads"
    character ||--o{ announcement_telemetry : "interacts"
    character ||--o{ announcements : "creates"

    announcements {
        uuid id PK
        announcement_type type
        announcement_priority priority
        announcement_display_style display_style
        varchar title
        text content
        jsonb media_urls
        jsonb targeting_criteria
        jsonb delivery_channels
        announcement_status status
        timestamp scheduled_publish_at
        timestamp published_at
        timestamp archived_at
        uuid created_by FK
        timestamp created_at
        timestamp updated_at
    }

    player_announcement_reads {
        uuid id PK
        uuid character_id FK
        uuid announcement_id FK
        timestamp displayed_at
        timestamp read_at
        timestamp clicked_at
        timestamp dismissed_at
        integer engagement_time
        timestamp created_at
        timestamp updated_at
    }

    patch_notes {
        uuid id PK
        varchar version UNIQUE
        timestamp release_date
        jsonb improvements
        jsonb bug_fixes
        jsonb known_issues
        jsonb attachments
        uuid announcement_id FK
        timestamp created_at
        timestamp updated_at
    }

    announcement_telemetry {
        uuid id PK
        announcement_telemetry_event event_type
        uuid announcement_id FK
        uuid character_id FK
        jsonb event_data
        timestamp created_at
    }
```

## Таблицы

### announcements

Объявления для игроков (новости, патчноуты, события, промо-акции).

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `type` (announcement_type) - Тип объявления: game_news, patch_notes, maintenance, event, promotion, community, emergency
- `priority` (announcement_priority) - Приоритет: low, medium, high, critical
- `display_style` (announcement_display_style) - Стиль отображения: news_feed, popup, modal, banner, toast
- `title` (VARCHAR(255)) - Заголовок объявления
- `content` (TEXT) - Содержимое объявления
- `media_urls` (JSONB) - URL медиа-файлов (изображения, видео)
- `targeting_criteria` (JSONB) - Критерии таргетинга (уровень, регион, фракция и т.д.)
- `delivery_channels` (JSONB) - Каналы доставки (in_game, email, push и т.д.)
- `status` (announcement_status) - Статус: draft, scheduled, published, archived
- `scheduled_publish_at` (TIMESTAMP) - Запланированное время публикации
- `published_at` (TIMESTAMP) - Время публикации
- `archived_at` (TIMESTAMP) - Время архивирования
- `created_by` (UUID, FK) - ID создателя (character)
- `created_at` (TIMESTAMP) - Время создания
- `updated_at` (TIMESTAMP) - Время обновления

**Индексы:**
- `idx_announcements_type` - По типу и статусу
- `idx_announcements_status` - По статусу и времени публикации
- `idx_announcements_priority` - По приоритету и статусу (только опубликованные)
- `idx_announcements_scheduled_publish_at` - По запланированному времени публикации
- `idx_announcements_published_at` - По времени публикации (только опубликованные)
- `idx_announcements_created_by` - По создателю

### player_announcement_reads

Прочтения объявлений игроками с метриками взаимодействия.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `character_id` (UUID, FK) - ID персонажа
- `announcement_id` (UUID, FK) - ID объявления
- `displayed_at` (TIMESTAMP) - Время отображения
- `read_at` (TIMESTAMP) - Время прочтения
- `clicked_at` (TIMESTAMP) - Время клика
- `dismissed_at` (TIMESTAMP) - Время закрытия
- `engagement_time` (INTEGER) - Время взаимодействия в секундах
- `created_at` (TIMESTAMP) - Время создания
- `updated_at` (TIMESTAMP) - Время обновления

**Ограничения:**
- UNIQUE (character_id, announcement_id) - Один персонаж может прочитать одно объявление один раз

**Индексы:**
- `idx_player_announcement_reads_character_id` - По персонажу и времени прочтения
- `idx_player_announcement_reads_announcement_id` - По объявлению и времени прочтения
- `idx_player_announcement_reads_read_at` - По времени прочтения (только прочитанные)

### patch_notes

Патчноуты с описанием изменений.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `version` (VARCHAR(50), UNIQUE) - Версия патча
- `release_date` (TIMESTAMP) - Дата релиза
- `improvements` (JSONB) - Список улучшений
- `bug_fixes` (JSONB) - Список исправлений багов
- `known_issues` (JSONB) - Список известных проблем
- `attachments` (JSONB) - Вложения (файлы, ссылки)
- `announcement_id` (UUID, FK) - ID связанного объявления
- `created_at` (TIMESTAMP) - Время создания
- `updated_at` (TIMESTAMP) - Время обновления

**Индексы:**
- `idx_patch_notes_version` - По версии
- `idx_patch_notes_release_date` - По дате релиза
- `idx_patch_notes_announcement_id` - По связанному объявлению

### announcement_telemetry

Телеметрия взаимодействий с объявлениями.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `event_type` (announcement_telemetry_event) - Тип события: displayed, read, clicked, dismissed
- `announcement_id` (UUID, FK) - ID объявления
- `character_id` (UUID, FK) - ID персонажа
- `event_data` (JSONB) - Дополнительные данные события
- `created_at` (TIMESTAMP) - Время события

**Индексы:**
- `idx_announcement_telemetry_event_type` - По типу события и времени
- `idx_announcement_telemetry_announcement_id` - По объявлению и времени
- `idx_announcement_telemetry_character_id` - По персонажу и времени
- `idx_announcement_telemetry_created_at` - По времени события

## ENUM типы

### announcement_type
- `game_news` - Новости игры
- `patch_notes` - Патчноуты
- `maintenance` - Обслуживание
- `event` - События
- `promotion` - Промо-акции
- `community` - Сообщество
- `emergency` - Срочные объявления

### announcement_priority
- `low` - Низкий приоритет
- `medium` - Средний приоритет
- `high` - Высокий приоритет
- `critical` - Критический приоритет

### announcement_display_style
- `news_feed` - Лента новостей
- `popup` - Всплывающее окно
- `modal` - Модальное окно
- `banner` - Баннер
- `toast` - Уведомление

### announcement_status
- `draft` - Черновик
- `scheduled` - Запланировано
- `published` - Опубликовано
- `archived` - Архивировано

### announcement_telemetry_event
- `displayed` - Отображено
- `read` - Прочитано
- `clicked` - Кликнуто
- `dismissed` - Закрыто

## Связи

- `announcements.created_by` → `mvp_core.characters.id` (SET NULL при удалении)
- `player_announcement_reads.character_id` → `mvp_core.characters.id` (CASCADE при удалении)
- `player_announcement_reads.announcement_id` → `content.announcements.id` (CASCADE при удалении)
- `patch_notes.announcement_id` → `content.announcements.id` (SET NULL при удалении)
- `announcement_telemetry.announcement_id` → `content.announcements.id` (CASCADE при удалении)
- `announcement_telemetry.character_id` → `mvp_core.characters.id` (SET NULL при удалении)

## Миграция

Файл: `infrastructure/liquibase/migrations/V1_82__announcement_system_tables.sql`

