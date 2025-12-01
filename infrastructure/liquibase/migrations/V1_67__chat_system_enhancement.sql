-- Issue: #140889577
-- Chat System Database Schema Enhancement
-- Дополнение схемы системы чата:
-- - Обновление chat_channels (добавление read_permission, write_permission, изменение типов)
-- - Обновление chat_messages (добавление message_type, is_edited, изменение полей)
-- - Обновление chat_bans (добавление ban_type, изменение полей)
-- - Обновление chat_reports (обновление структуры)
-- - Создание chat_channel_members (участники каналов)
-- - Создание chat_ignores (игнорируемые игроки)
-- - Создание chat_message_history (архив сообщений)
-- Примечание: Базовые таблицы уже созданы в V1_10 и V1_25

-- Создание ENUM типов для соответствия архитектуре
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'chat_channel_type') THEN
        CREATE TYPE chat_channel_type AS ENUM ('GLOBAL', 'TRADE', 'NEWBIE', 'LOCAL', 'PARTY', 'RAID', 'GUILD', 'GUILD_OFFICER', 'WHISPER', 'SYSTEM', 'COMBAT_LOG', 'RP_EMOTE');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'chat_message_type') THEN
        CREATE TYPE chat_message_type AS ENUM ('text', 'system', 'emote', 'command');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'chat_ban_type') THEN
        CREATE TYPE chat_ban_type AS ENUM ('channel', 'global');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'chat_report_status') THEN
        CREATE TYPE chat_report_status AS ENUM ('pending', 'reviewed', 'resolved', 'dismissed');
    END IF;
END $$;

-- Обновление chat_channels для добавления недостающих полей
DO $$ 
BEGIN
    -- Добавляем read_permission, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_channels' 
                   AND column_name = 'read_permission') THEN
        ALTER TABLE social.chat_channels ADD COLUMN read_permission JSONB DEFAULT '{}'::jsonb;
    END IF;
    
    -- Добавляем write_permission, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_channels' 
                   AND column_name = 'write_permission') THEN
        ALTER TABLE social.chat_channels ADD COLUMN write_permission JSONB DEFAULT '{}'::jsonb;
    END IF;
    
    -- Переименовываем max_length в max_message_length, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_channels' 
               AND column_name = 'max_length') THEN
        ALTER TABLE social.chat_channels RENAME COLUMN max_length TO max_message_length;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_channels' 
                   AND column_name = 'max_message_length') THEN
        ALTER TABLE social.chat_channels ADD COLUMN max_message_length INTEGER NOT NULL DEFAULT 500;
    END IF;
    
    -- Переименовываем type в channel_type, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_channels' 
               AND column_name = 'type') THEN
        ALTER TABLE social.chat_channels RENAME COLUMN type TO channel_type;
    END IF;
END $$;

-- Обновление chat_messages для добавления недостающих полей
DO $$ 
BEGIN
    -- Добавляем message_type, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_messages' 
                   AND column_name = 'message_type') THEN
        ALTER TABLE social.chat_messages ADD COLUMN message_type VARCHAR(20) NOT NULL DEFAULT 'text' CHECK (message_type IN ('text', 'system', 'emote', 'command'));
    END IF;
    
    -- Добавляем is_edited, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_messages' 
                   AND column_name = 'is_edited') THEN
        ALTER TABLE social.chat_messages ADD COLUMN is_edited BOOLEAN NOT NULL DEFAULT false;
    END IF;
    
    -- Добавляем is_deleted, если его нет (вместо deleted_at)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_messages' 
                   AND column_name = 'is_deleted') THEN
        ALTER TABLE social.chat_messages ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT false;
    END IF;
    
    -- Переименовываем content в message_text, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_messages' 
               AND column_name = 'content') THEN
        ALTER TABLE social.chat_messages RENAME COLUMN content TO message_text;
    END IF;
    
    -- Переименовываем formatted в formatted_text, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_messages' 
               AND column_name = 'formatted') THEN
        ALTER TABLE social.chat_messages RENAME COLUMN formatted TO formatted_text;
    END IF;
    
    -- Переименовываем sender_id в sender_id (оставляем как есть, но проверяем FK)
    -- Переименовываем sender_name - удаляем, так как можно получить из character
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_messages' 
               AND column_name = 'sender_name') THEN
        ALTER TABLE social.chat_messages DROP COLUMN sender_name;
    END IF;
    
    -- Добавляем updated_at, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_messages' 
                   AND column_name = 'updated_at') THEN
        ALTER TABLE social.chat_messages ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

-- Обновление chat_bans для соответствия архитектуре
DO $$ 
BEGIN
    -- Добавляем ban_type, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_bans' 
                   AND column_name = 'ban_type') THEN
        ALTER TABLE social.chat_bans ADD COLUMN ban_type VARCHAR(20) NOT NULL DEFAULT 'channel' CHECK (ban_type IN ('channel', 'global'));
    END IF;
    
    -- Переименовываем character_id в player_id, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_bans' 
               AND column_name = 'character_id') THEN
        ALTER TABLE social.chat_bans RENAME COLUMN character_id TO player_id;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_bans' 
                   AND column_name = 'player_id') THEN
        ALTER TABLE social.chat_bans ADD COLUMN player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE;
    END IF;
    
    -- Переименовываем admin_id в banned_by, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_bans' 
               AND column_name = 'admin_id') THEN
        ALTER TABLE social.chat_bans RENAME COLUMN admin_id TO banned_by;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'chat_bans' 
                   AND column_name = 'banned_by') THEN
        ALTER TABLE social.chat_bans ADD COLUMN banned_by UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL;
    END IF;
    
    -- Удаляем channel_type, если есть (не используется в архитектуре)
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_bans' 
               AND column_name = 'channel_type') THEN
        ALTER TABLE social.chat_bans DROP COLUMN channel_type;
    END IF;
END $$;

-- Обновление chat_reports для соответствия архитектуре
DO $$ 
BEGIN
    -- Переименовываем reported_id в reported_player_id, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_reports' 
               AND column_name = 'reported_id') THEN
        ALTER TABLE social.chat_reports RENAME COLUMN reported_id TO reported_player_id;
    END IF;
    
    -- Переименовываем admin_id в reviewed_by, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_reports' 
               AND column_name = 'admin_id') THEN
        ALTER TABLE social.chat_reports RENAME COLUMN admin_id TO reviewed_by;
    END IF;
    
    -- Переименовываем resolved_at в reviewed_at, если нужно
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'chat_reports' 
               AND column_name = 'resolved_at') THEN
        ALTER TABLE social.chat_reports RENAME COLUMN resolved_at TO reviewed_at;
    END IF;
    
    -- Обновляем CHECK constraint для status
    IF EXISTS (SELECT 1 FROM information_schema.table_constraints 
               WHERE constraint_schema = 'social' 
               AND table_name = 'chat_reports' 
               AND constraint_name LIKE '%status%') THEN
        ALTER TABLE social.chat_reports DROP CONSTRAINT IF EXISTS chat_reports_status_check;
    END IF;
    ALTER TABLE social.chat_reports ADD CONSTRAINT chat_reports_status_check 
        CHECK (status IN ('pending', 'reviewed', 'resolved', 'dismissed'));
END $$;

-- Таблица участников каналов
CREATE TABLE IF NOT EXISTS social.chat_channel_members (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    channel_id UUID NOT NULL REFERENCES social.chat_channels(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP,
    UNIQUE(channel_id, player_id)
);

-- Индексы для chat_channel_members
CREATE INDEX IF NOT EXISTS idx_chat_channel_members_channel_id ON social.chat_channel_members(channel_id);
CREATE INDEX IF NOT EXISTS idx_chat_channel_members_player_id ON social.chat_channel_members(player_id);

-- Таблица игнорируемых игроков
CREATE TABLE IF NOT EXISTS social.chat_ignores (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    ignored_player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(player_id, ignored_player_id),
    CHECK (player_id != ignored_player_id)
);

-- Индексы для chat_ignores
CREATE INDEX IF NOT EXISTS idx_chat_ignores_player_id ON social.chat_ignores(player_id);
CREATE INDEX IF NOT EXISTS idx_chat_ignores_ignored_player_id ON social.chat_ignores(ignored_player_id);

-- Таблица истории сообщений (архив)
CREATE TABLE IF NOT EXISTS social.chat_message_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    channel_id UUID NOT NULL REFERENCES social.chat_channels(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    message_text TEXT NOT NULL,
    formatted_text TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для chat_message_history
CREATE INDEX IF NOT EXISTS idx_chat_message_history_channel_created ON social.chat_message_history(channel_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_chat_message_history_sender_created ON social.chat_message_history(sender_id, created_at DESC);

-- Обновление индексов для chat_channels
CREATE INDEX IF NOT EXISTS idx_chat_channels_type_active ON social.chat_channels(channel_type, is_active) WHERE deleted_at IS NULL AND is_active = true;

-- Обновление индексов для chat_messages
CREATE INDEX IF NOT EXISTS idx_chat_messages_channel_created ON social.chat_messages(channel_id, created_at DESC) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_chat_messages_sender_created ON social.chat_messages(sender_id, created_at DESC) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_chat_messages_created_at ON social.chat_messages(created_at DESC) WHERE is_deleted = false;

-- Обновление индексов для chat_bans
CREATE INDEX IF NOT EXISTS idx_chat_bans_player_active ON social.chat_bans(player_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_chat_bans_channel_active ON social.chat_bans(channel_id, is_active) WHERE is_active = true AND channel_id IS NOT NULL;

-- Обновление индексов для chat_reports
CREATE INDEX IF NOT EXISTS idx_chat_reports_status_created ON social.chat_reports(status, created_at DESC) WHERE status = 'pending';

-- Комментарии к таблицам
COMMENT ON TABLE social.chat_channels IS 'Каналы чата (глобальные, локальные, групповые, приватные)';
COMMENT ON TABLE social.chat_messages IS 'Сообщения чата';
COMMENT ON TABLE social.chat_channel_members IS 'Участники каналов чата';
COMMENT ON TABLE social.chat_bans IS 'Баны в чате';
COMMENT ON TABLE social.chat_reports IS 'Жалобы на сообщения';
COMMENT ON TABLE social.chat_ignores IS 'Игнорируемые игроки';
COMMENT ON TABLE social.chat_message_history IS 'История сообщений (архив)';

-- Комментарии к колонкам
COMMENT ON COLUMN social.chat_channels.channel_type IS 'Тип канала: GLOBAL, TRADE, NEWBIE, LOCAL, PARTY, RAID, GUILD, GUILD_OFFICER, WHISPER, SYSTEM, COMBAT_LOG, RP_EMOTE';
COMMENT ON COLUMN social.chat_channels.read_permission IS 'Права на чтение (JSONB)';
COMMENT ON COLUMN social.chat_channels.write_permission IS 'Права на запись (JSONB)';
COMMENT ON COLUMN social.chat_channels.max_message_length IS 'Максимальная длина сообщения (default: 500)';
COMMENT ON COLUMN social.chat_messages.message_type IS 'Тип сообщения: text, system, emote, command';
COMMENT ON COLUMN social.chat_messages.message_text IS 'Текст сообщения';
COMMENT ON COLUMN social.chat_messages.formatted_text IS 'Отформатированный текст';
COMMENT ON COLUMN social.chat_messages.is_edited IS 'Отредактировано ли сообщение';
COMMENT ON COLUMN social.chat_messages.is_deleted IS 'Удалено ли сообщение';
COMMENT ON COLUMN social.chat_channel_members.last_read_at IS 'Время последнего прочитанного сообщения (nullable)';
COMMENT ON COLUMN social.chat_bans.ban_type IS 'Тип бана: channel, global';
COMMENT ON COLUMN social.chat_bans.player_id IS 'ID забаненного игрока';
COMMENT ON COLUMN social.chat_bans.banned_by IS 'ID администратора, выдавшего бан (nullable)';
COMMENT ON COLUMN social.chat_reports.reported_player_id IS 'ID игрока, на которого пожаловались';
COMMENT ON COLUMN social.chat_reports.status IS 'Статус жалобы: pending, reviewed, resolved, dismissed';
COMMENT ON COLUMN social.chat_reports.reviewed_by IS 'ID администратора, рассмотревшего жалобу (nullable)';
COMMENT ON COLUMN social.chat_reports.reviewed_at IS 'Время рассмотрения жалобы (nullable)';


