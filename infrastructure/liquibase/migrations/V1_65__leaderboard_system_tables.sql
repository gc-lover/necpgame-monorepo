-- Issue: #140887681
-- Leaderboard System Database Schema
-- Создание таблиц для системы лидербордов:
-- - leaderboards (определения лидербордов)
-- - leaderboard_entries (записи в лидербордах)
-- - leaderboard_snapshots (снапшоты для сезонов)
-- - leaderboard_seasons (сезоны лидербордов)
-- - leaderboard_rewards (награды за позиции)

-- Создание схемы world, если её нет
CREATE SCHEMA IF NOT EXISTS world;

-- Создание ENUM типов для соответствия архитектуре
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'leaderboard_type') THEN
        CREATE TYPE leaderboard_type AS ENUM ('global', 'class', 'seasonal', 'friend', 'guild');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'update_frequency') THEN
        CREATE TYPE update_frequency AS ENUM ('realtime', 'hourly', 'daily', 'weekly');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'leaderboard_tier') THEN
        CREATE TYPE leaderboard_tier AS ENUM ('diamond', 'platinum', 'gold', 'silver', 'bronze');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'season_status') THEN
        CREATE TYPE season_status AS ENUM ('active', 'ended', 'archived');
    END IF;
END $$;

-- Таблица определений лидербордов
CREATE TABLE IF NOT EXISTS world.leaderboards (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('global', 'class', 'seasonal', 'friend', 'guild')),
    scope VARCHAR(100) NOT NULL,
    metric_type VARCHAR(100) NOT NULL,
    update_frequency VARCHAR(20) NOT NULL CHECK (update_frequency IN ('realtime', 'hourly', 'daily', 'weekly')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    season_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для leaderboards
CREATE INDEX IF NOT EXISTS idx_leaderboards_code_type ON world.leaderboards(code, type);
CREATE INDEX IF NOT EXISTS idx_leaderboards_type_active ON world.leaderboards(type, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_leaderboards_season_id ON world.leaderboards(season_id) WHERE season_id IS NOT NULL;

-- Таблица записей в лидербордах
CREATE TABLE IF NOT EXISTS world.leaderboard_entries (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    leaderboard_id UUID NOT NULL REFERENCES world.leaderboards(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    score DECIMAL(20, 2) NOT NULL DEFAULT 0,
    rank INTEGER NOT NULL DEFAULT 0,
    previous_rank INTEGER,
    tier VARCHAR(20) CHECK (tier IN ('diamond', 'platinum', 'gold', 'silver', 'bronze')),
    metadata JSONB DEFAULT '{}'::jsonb,
    season_id UUID,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(leaderboard_id, player_id, season_id)
);

-- Индексы для leaderboard_entries
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_leaderboard_rank ON world.leaderboard_entries(leaderboard_id, rank);
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_player_leaderboard ON world.leaderboard_entries(player_id, leaderboard_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_leaderboard_score ON world.leaderboard_entries(leaderboard_id, score DESC);
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_season_rank ON world.leaderboard_entries(season_id, rank) WHERE season_id IS NOT NULL;

-- Таблица снапшотов лидербордов (для сезонов)
CREATE TABLE IF NOT EXISTS world.leaderboard_snapshots (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    leaderboard_id UUID NOT NULL REFERENCES world.leaderboards(id) ON DELETE CASCADE,
    season_id UUID NOT NULL,
    snapshot_data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для leaderboard_snapshots
CREATE INDEX IF NOT EXISTS idx_leaderboard_snapshots_leaderboard_season ON world.leaderboard_snapshots(leaderboard_id, season_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_snapshots_season ON world.leaderboard_snapshots(season_id);

-- Таблица сезонов лидербордов
CREATE TABLE IF NOT EXISTS world.leaderboard_seasons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'ended', 'archived')),
    rewards_distributed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date > start_date)
);

-- Индексы для leaderboard_seasons
CREATE INDEX IF NOT EXISTS idx_leaderboard_seasons_status_end_date ON world.leaderboard_seasons(status, end_date);
CREATE INDEX IF NOT EXISTS idx_leaderboard_seasons_start_end_date ON world.leaderboard_seasons(start_date, end_date);

-- Таблица наград за позиции в лидербордах
CREATE TABLE IF NOT EXISTS world.leaderboard_rewards (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    leaderboard_id UUID NOT NULL REFERENCES world.leaderboards(id) ON DELETE CASCADE,
    season_id UUID REFERENCES world.leaderboard_seasons(id) ON DELETE CASCADE,
    tier VARCHAR(20) NOT NULL CHECK (tier IN ('diamond', 'platinum', 'gold', 'silver', 'bronze')),
    rank_min INTEGER NOT NULL CHECK (rank_min > 0),
    rank_max INTEGER NOT NULL CHECK (rank_max >= rank_min),
    rewards JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для leaderboard_rewards
CREATE INDEX IF NOT EXISTS idx_leaderboard_rewards_leaderboard_tier ON world.leaderboard_rewards(leaderboard_id, tier);
CREATE INDEX IF NOT EXISTS idx_leaderboard_rewards_season_tier ON world.leaderboard_rewards(season_id, tier) WHERE season_id IS NOT NULL;

-- Обновление внешних ключей для season_id
DO $$ 
BEGIN
    -- Добавляем внешний ключ для leaderboards.season_id, если его нет
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_schema = 'world' 
        AND table_name = 'leaderboards' 
        AND constraint_name = 'leaderboards_season_id_fkey'
    ) THEN
        ALTER TABLE world.leaderboards 
        ADD CONSTRAINT leaderboards_season_id_fkey 
        FOREIGN KEY (season_id) REFERENCES world.leaderboard_seasons(id) ON DELETE SET NULL;
    END IF;
    
    -- Добавляем внешний ключ для leaderboard_entries.season_id, если его нет
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_schema = 'world' 
        AND table_name = 'leaderboard_entries' 
        AND constraint_name = 'leaderboard_entries_season_id_fkey'
    ) THEN
        ALTER TABLE world.leaderboard_entries 
        ADD CONSTRAINT leaderboard_entries_season_id_fkey 
        FOREIGN KEY (season_id) REFERENCES world.leaderboard_seasons(id) ON DELETE SET NULL;
    END IF;
    
    -- Добавляем внешний ключ для leaderboard_snapshots.season_id, если его нет
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_schema = 'world' 
        AND table_name = 'leaderboard_snapshots' 
        AND constraint_name = 'leaderboard_snapshots_season_id_fkey'
    ) THEN
        ALTER TABLE world.leaderboard_snapshots 
        ADD CONSTRAINT leaderboard_snapshots_season_id_fkey 
        FOREIGN KEY (season_id) REFERENCES world.leaderboard_seasons(id) ON DELETE CASCADE;
    END IF;
END $$;

-- Комментарии к таблицам
COMMENT ON TABLE world.leaderboards IS 'Определения лидербордов (глобальные, сезонные, дружественные, гильдийные)';
COMMENT ON TABLE world.leaderboard_entries IS 'Записи в лидербордах (позиции игроков)';
COMMENT ON TABLE world.leaderboard_snapshots IS 'Снапшоты лидербордов для сезонов';
COMMENT ON TABLE world.leaderboard_seasons IS 'Сезоны лидербордов';
COMMENT ON TABLE world.leaderboard_rewards IS 'Награды за позиции в лидербордах';

-- Комментарии к колонкам
COMMENT ON COLUMN world.leaderboards.code IS 'Уникальный код лидерборда';
COMMENT ON COLUMN world.leaderboards.type IS 'Тип лидерборда: global, class, seasonal, friend, guild';
COMMENT ON COLUMN world.leaderboards.scope IS 'Область видимости (server, class, season, friends, guild)';
COMMENT ON COLUMN world.leaderboards.metric_type IS 'Тип метрики (overall_power, combat_score, economic_score, social_score)';
COMMENT ON COLUMN world.leaderboards.update_frequency IS 'Частота обновления: realtime, hourly, daily, weekly';
COMMENT ON COLUMN world.leaderboard_entries.score IS 'Очки игрока в лидерборде';
COMMENT ON COLUMN world.leaderboard_entries.rank IS 'Текущая позиция игрока';
COMMENT ON COLUMN world.leaderboard_entries.previous_rank IS 'Предыдущая позиция игрока (nullable)';
COMMENT ON COLUMN world.leaderboard_entries.tier IS 'Тир игрока: diamond, platinum, gold, silver, bronze';
COMMENT ON COLUMN world.leaderboard_entries.metadata IS 'Дополнительные данные (денормализованные)';
COMMENT ON COLUMN world.leaderboard_snapshots.snapshot_data IS 'Полные данные рейтинга на момент создания снапшота (JSONB)';
COMMENT ON COLUMN world.leaderboard_seasons.status IS 'Статус сезона: active, ended, archived';
COMMENT ON COLUMN world.leaderboard_seasons.rewards_distributed IS 'Распределены ли награды за сезон';
COMMENT ON COLUMN world.leaderboard_rewards.rank_min IS 'Минимальная позиция для тира';
COMMENT ON COLUMN world.leaderboard_rewards.rank_max IS 'Максимальная позиция для тира';
COMMENT ON COLUMN world.leaderboard_rewards.rewards IS 'Награды (валюта, предметы, титулы) в формате JSONB';


