-- Issue: #323
-- Announcement System Database Schema
-- Создание таблиц для системы объявлений:
-- - announcements (объявления)
-- - player_announcement_reads (прочтения объявлений игроками)
-- - patch_notes (патчноуты)
-- - announcement_telemetry (телеметрия объявлений)

-- Создание схемы content, если её нет
CREATE SCHEMA IF NOT EXISTS content;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'announcement_type') THEN
        CREATE TYPE announcement_type AS ENUM ('game_news', 'patch_notes', 'maintenance', 'event', 'promotion', 'community', 'emergency');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'announcement_priority') THEN
        CREATE TYPE announcement_priority AS ENUM ('low', 'medium', 'high', 'critical');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'announcement_display_style') THEN
        CREATE TYPE announcement_display_style AS ENUM ('news_feed', 'popup', 'modal', 'banner', 'toast');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'announcement_status') THEN
        CREATE TYPE announcement_status AS ENUM ('draft', 'scheduled', 'published', 'archived');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'announcement_telemetry_event') THEN
        CREATE TYPE announcement_telemetry_event AS ENUM ('displayed', 'read', 'clicked', 'dismissed');
    END IF;
END $$;

-- Таблица объявлений
CREATE TABLE IF NOT EXISTS content.announcements (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    type announcement_type NOT NULL,
    priority announcement_priority NOT NULL DEFAULT 'medium',
    display_style announcement_display_style NOT NULL DEFAULT 'news_feed',
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    media_urls JSONB DEFAULT '[]'::jsonb,
    targeting_criteria JSONB DEFAULT '{}'::jsonb,
    delivery_channels JSONB DEFAULT '[]'::jsonb,
    status announcement_status NOT NULL DEFAULT 'draft',
    scheduled_publish_at TIMESTAMP,
    published_at TIMESTAMP,
    archived_at TIMESTAMP,
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_announcements_created_by FOREIGN KEY (created_by) REFERENCES mvp_core.characters(id) ON DELETE SET NULL
);

-- Индексы для announcements
CREATE INDEX IF NOT EXISTS idx_announcements_type ON content.announcements(type, status);
CREATE INDEX IF NOT EXISTS idx_announcements_status ON content.announcements(status, published_at DESC);
CREATE INDEX IF NOT EXISTS idx_announcements_priority ON content.announcements(priority, status) WHERE status = 'published';
CREATE INDEX IF NOT EXISTS idx_announcements_scheduled_publish_at ON content.announcements(scheduled_publish_at) WHERE status = 'scheduled';
CREATE INDEX IF NOT EXISTS idx_announcements_published_at ON content.announcements(published_at DESC) WHERE status = 'published';
CREATE INDEX IF NOT EXISTS idx_announcements_created_by ON content.announcements(created_by) WHERE created_by IS NOT NULL;

-- Таблица прочтений объявлений игроками
CREATE TABLE IF NOT EXISTS content.player_announcement_reads (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL,
    announcement_id UUID NOT NULL,
    displayed_at TIMESTAMP,
    read_at TIMESTAMP,
    clicked_at TIMESTAMP,
    dismissed_at TIMESTAMP,
    engagement_time INTEGER DEFAULT 0 CHECK (engagement_time >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_player_announcement_reads_character FOREIGN KEY (character_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_player_announcement_reads_announcement FOREIGN KEY (announcement_id) REFERENCES content.announcements(id) ON DELETE CASCADE,
    CONSTRAINT uq_player_announcement_reads UNIQUE (character_id, announcement_id)
);

-- Индексы для player_announcement_reads
CREATE INDEX IF NOT EXISTS idx_player_announcement_reads_character_id ON content.player_announcement_reads(character_id, read_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_announcement_reads_announcement_id ON content.player_announcement_reads(announcement_id, read_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_announcement_reads_read_at ON content.player_announcement_reads(read_at DESC) WHERE read_at IS NOT NULL;

-- Таблица патчноутов
CREATE TABLE IF NOT EXISTS content.patch_notes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    version VARCHAR(50) NOT NULL UNIQUE,
    release_date TIMESTAMP NOT NULL,
    improvements JSONB DEFAULT '[]'::jsonb,
    bug_fixes JSONB DEFAULT '[]'::jsonb,
    known_issues JSONB DEFAULT '[]'::jsonb,
    attachments JSONB DEFAULT '[]'::jsonb,
    announcement_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_patch_notes_announcement FOREIGN KEY (announcement_id) REFERENCES content.announcements(id) ON DELETE SET NULL
);

-- Индексы для patch_notes
CREATE INDEX IF NOT EXISTS idx_patch_notes_version ON content.patch_notes(version);
CREATE INDEX IF NOT EXISTS idx_patch_notes_release_date ON content.patch_notes(release_date DESC);
CREATE INDEX IF NOT EXISTS idx_patch_notes_announcement_id ON content.patch_notes(announcement_id) WHERE announcement_id IS NOT NULL;

-- Таблица телеметрии объявлений
CREATE TABLE IF NOT EXISTS content.announcement_telemetry (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_type announcement_telemetry_event NOT NULL,
    announcement_id UUID NOT NULL,
    character_id UUID,
    event_data JSONB DEFAULT '{}'::jsonb,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_announcement_telemetry_announcement FOREIGN KEY (announcement_id) REFERENCES content.announcements(id) ON DELETE CASCADE,
    CONSTRAINT fk_announcement_telemetry_character FOREIGN KEY (character_id) REFERENCES mvp_core.characters(id) ON DELETE SET NULL
);

-- Индексы для announcement_telemetry
CREATE INDEX IF NOT EXISTS idx_announcement_telemetry_event_type ON content.announcement_telemetry(event_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_announcement_telemetry_announcement_id ON content.announcement_telemetry(announcement_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_announcement_telemetry_character_id ON content.announcement_telemetry(character_id, created_at DESC) WHERE character_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_announcement_telemetry_created_at ON content.announcement_telemetry(created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE content.announcements IS 'Объявления для игроков (новости, патчноуты, события, промо)';
COMMENT ON TABLE content.player_announcement_reads IS 'Прочтения объявлений игроками с метриками взаимодействия';
COMMENT ON TABLE content.patch_notes IS 'Патчноуты с описанием изменений';
COMMENT ON TABLE content.announcement_telemetry IS 'Телеметрия взаимодействий с объявлениями';

-- Комментарии к колонкам
COMMENT ON COLUMN content.announcements.type IS 'Тип объявления: game_news, patch_notes, maintenance, event, promotion, community, emergency';
COMMENT ON COLUMN content.announcements.priority IS 'Приоритет объявления: low, medium, high, critical';
COMMENT ON COLUMN content.announcements.display_style IS 'Стиль отображения: news_feed, popup, modal, banner, toast';
COMMENT ON COLUMN content.announcements.status IS 'Статус объявления: draft, scheduled, published, archived';
COMMENT ON COLUMN content.announcements.targeting_criteria IS 'Критерии таргетинга (JSONB): уровень, регион, фракция и т.д.';
COMMENT ON COLUMN content.announcements.delivery_channels IS 'Каналы доставки (JSONB): in_game, email, push и т.д.';
COMMENT ON COLUMN content.player_announcement_reads.engagement_time IS 'Время взаимодействия с объявлением в секундах';
COMMENT ON COLUMN content.patch_notes.version IS 'Версия патча (уникальная)';
COMMENT ON COLUMN content.patch_notes.improvements IS 'Список улучшений (JSONB)';
COMMENT ON COLUMN content.patch_notes.bug_fixes IS 'Список исправлений багов (JSONB)';
COMMENT ON COLUMN content.patch_notes.known_issues IS 'Список известных проблем (JSONB)';

