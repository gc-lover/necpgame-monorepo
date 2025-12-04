-- Issue: #140892117
-- Voice Lobby System Database Schema
-- Создание таблиц для системы голосовых лобби:
-- - voice_lobbies (голосовые лобби)
-- - lobby_participants (участники лобби)
-- - lobby_subchannels (подканалы лобби)
-- - party_finder_searches (поиски групп)
-- - lobby_telemetry (телеметрия лобби)

-- Создание схемы social, если её нет (уже создана в V1_52, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'voice_lobby_type') THEN
        CREATE TYPE voice_lobby_type AS ENUM ('activity', 'clan', 'guild', 'raid', 'tournament');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lobby_participant_role') THEN
        CREATE TYPE lobby_participant_role AS ENUM ('leader', 'commander', 'party_leader', 'officer', 'member');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lobby_subchannel_type') THEN
        CREATE TYPE lobby_subchannel_type AS ENUM ('main', 'role_based', 'commander', 'party');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'party_finder_search_status') THEN
        CREATE TYPE party_finder_search_status AS ENUM ('searching', 'found', 'cancelled', 'timeout');
    END IF;
END $$;

-- Таблица голосовых лобби
CREATE TABLE IF NOT EXISTS social.voice_lobbies (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  owner_id UUID NOT NULL,
  -- FK characters
    party_id UUID,
  -- FK parties
    guild_id UUID,
  -- FK guilds
    clan_id UUID,
  description TEXT,
  name VARCHAR(255) NOT NULL,
  -- FK clans
    voice_provider_room_id VARCHAR(255),
  region VARCHAR(50),
  language VARCHAR(10),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  closed_at TIMESTAMP,
  max_participants INTEGER NOT NULL DEFAULT 8 CHECK (max_participants > 0),
  max_subchannels INTEGER NOT NULL DEFAULT 4 CHECK (max_subchannels >= 0),
  is_public BOOLEAN NOT NULL DEFAULT false,
  is_active BOOLEAN NOT NULL DEFAULT true,
  type voice_lobby_type NOT NULL
);

-- Индексы для voice_lobbies
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_owner_id 
    ON social.voice_lobbies(owner_id);
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_type 
    ON social.voice_lobbies(type);
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_party_id 
    ON social.voice_lobbies(party_id) WHERE party_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_guild_id 
    ON social.voice_lobbies(guild_id) WHERE guild_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_clan_id 
    ON social.voice_lobbies(clan_id) WHERE clan_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_is_active 
    ON social.voice_lobbies(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_is_public 
    ON social.voice_lobbies(is_public) WHERE is_public = true;
CREATE INDEX IF NOT EXISTS idx_voice_lobbies_region_language 
    ON social.voice_lobbies(region, language) WHERE region IS NOT NULL AND language IS NOT NULL;

-- Таблица участников лобби
CREATE TABLE IF NOT EXISTS social.lobby_participants (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  lobby_id UUID NOT NULL REFERENCES social.voice_lobbies(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  subchannel_id UUID,
  joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  left_at TIMESTAMP,
  -- FK lobby_subchannels
    is_muted BOOLEAN NOT NULL DEFAULT false,
  is_deafened BOOLEAN NOT NULL DEFAULT false,
  -- FK characters
    role lobby_participant_role NOT NULL DEFAULT 'member',
  UNIQUE(lobby_id, character_id)
);

-- Индексы для lobby_participants
CREATE INDEX IF NOT EXISTS idx_lobby_participants_lobby_id 
    ON social.lobby_participants(lobby_id);
CREATE INDEX IF NOT EXISTS idx_lobby_participants_character_id 
    ON social.lobby_participants(character_id);
CREATE INDEX IF NOT EXISTS idx_lobby_participants_subchannel_id 
    ON social.lobby_participants(subchannel_id) WHERE subchannel_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_lobby_participants_role 
    ON social.lobby_participants(role);
CREATE INDEX IF NOT EXISTS idx_lobby_participants_joined_at 
    ON social.lobby_participants(joined_at DESC);

-- Таблица подканалов лобби
CREATE TABLE IF NOT EXISTS social.lobby_subchannels (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  lobby_id UUID NOT NULL REFERENCES social.voice_lobbies(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  voice_provider_channel_id VARCHAR(255),
  role_restrictions JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  max_participants INTEGER CHECK (max_participants IS NULL OR max_participants > 0),
  type lobby_subchannel_type NOT NULL DEFAULT 'main'
);

-- Индексы для lobby_subchannels
CREATE INDEX IF NOT EXISTS idx_lobby_subchannels_lobby_id 
    ON social.lobby_subchannels(lobby_id);
CREATE INDEX IF NOT EXISTS idx_lobby_subchannels_type 
    ON social.lobby_subchannels(type);
CREATE INDEX IF NOT EXISTS idx_lobby_subchannels_name 
    ON social.lobby_subchannels(name);

-- Добавляем внешний ключ для subchannel_id в lobby_participants
ALTER TABLE social.lobby_participants 
    ADD CONSTRAINT fk_lobby_participants_subchannel 
    FOREIGN KEY (subchannel_id) REFERENCES social.lobby_subchannels(id) ON DELETE SET NULL;

-- Таблица поисков групп
CREATE TABLE IF NOT EXISTS social.party_finder_searches (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL,
  -- FK characters
    role VARCHAR(50),
  activity VARCHAR(50),
  region VARCHAR(50),
  language VARCHAR(10),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  found_at TIMESTAMP,
  rating INTEGER CHECK (rating IS NULL OR rating >= 0),
  min_level INTEGER CHECK (min_level IS NULL OR min_level >= 0),
  max_level INTEGER CHECK (max_level IS NULL OR max_level >= 0),
  status party_finder_search_status NOT NULL DEFAULT 'searching'
);

-- Индексы для party_finder_searches
CREATE INDEX IF NOT EXISTS idx_party_finder_searches_character_id 
    ON social.party_finder_searches(character_id);
CREATE INDEX IF NOT EXISTS idx_party_finder_searches_status 
    ON social.party_finder_searches(status) WHERE status = 'searching';
CREATE INDEX IF NOT EXISTS idx_party_finder_searches_role_activity 
    ON social.party_finder_searches(role, activity) WHERE role IS NOT NULL AND activity IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_party_finder_searches_region_language 
    ON social.party_finder_searches(region, language) WHERE region IS NOT NULL AND language IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_party_finder_searches_rating 
    ON social.party_finder_searches(rating) WHERE rating IS NOT NULL;

-- Таблица телеметрии лобби
CREATE TABLE IF NOT EXISTS social.lobby_telemetry (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  lobby_id UUID REFERENCES social.voice_lobbies(id) ON DELETE CASCADE,
  character_id UUID,
  event_type VARCHAR(50) NOT NULL,
  -- FK characters
    event_data JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для lobby_telemetry
CREATE INDEX IF NOT EXISTS idx_lobby_telemetry_lobby_id 
    ON social.lobby_telemetry(lobby_id) WHERE lobby_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_lobby_telemetry_character_id 
    ON social.lobby_telemetry(character_id) WHERE character_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_lobby_telemetry_event_type 
    ON social.lobby_telemetry(event_type);
CREATE INDEX IF NOT EXISTS idx_lobby_telemetry_created_at 
    ON social.lobby_telemetry(created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE social.voice_lobbies IS 'Голосовые лобби для различных типов активностей (activity, clan, guild, raid, tournament)';
COMMENT ON TABLE social.lobby_participants IS 'Участники голосовых лобби с ролями и настройками (muted, deafened)';
COMMENT ON TABLE social.lobby_subchannels IS 'Подканалы лобби для разделения участников (main, role_based, commander, party)';
COMMENT ON TABLE social.party_finder_searches IS 'Поиски групп для подбора игроков по ролям, рейтингу, активности, региону, языку';
COMMENT ON TABLE social.lobby_telemetry IS 'Телеметрия событий лобби для аналитики и мониторинга';

-- Комментарии к колонкам
COMMENT ON COLUMN social.voice_lobbies.type IS 'Тип лобби: activity, clan, guild, raid, tournament';
COMMENT ON COLUMN social.voice_lobbies.voice_provider_room_id IS 'ID комнаты в провайдере голосовой связи';
COMMENT ON COLUMN social.voice_lobbies.max_participants IS 'Максимальное количество участников';
COMMENT ON COLUMN social.voice_lobbies.max_subchannels IS 'Максимальное количество подканалов';
COMMENT ON COLUMN social.lobby_participants.role IS 'Роль участника: leader, commander, party_leader, officer, member';
COMMENT ON COLUMN social.lobby_participants.subchannel_id IS 'ID подканала, в котором находится участник';
COMMENT ON COLUMN social.lobby_participants.is_muted IS 'Участник заглушен';
COMMENT ON COLUMN social.lobby_participants.is_deafened IS 'Участник оглушен';
COMMENT ON COLUMN social.lobby_subchannels.type IS 'Тип подканала: main, role_based, commander, party';
COMMENT ON COLUMN social.lobby_subchannels.voice_provider_channel_id IS 'ID канала в провайдере голосовой связи';
COMMENT ON COLUMN social.lobby_subchannels.role_restrictions IS 'Ограничения по ролям в JSONB';
COMMENT ON COLUMN social.party_finder_searches.status IS 'Статус поиска: searching, found, cancelled, timeout';
COMMENT ON COLUMN social.party_finder_searches.rating IS 'Рейтинг игрока для поиска';
COMMENT ON COLUMN social.lobby_telemetry.event_type IS 'Тип события (created, joined, left, muted, etc.)';
COMMENT ON COLUMN social.lobby_telemetry.event_data IS 'Данные события в JSONB';

