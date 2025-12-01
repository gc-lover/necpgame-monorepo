-- Issue: #140875800
-- League System and Meta Mechanics Database Schema
-- Создание таблиц для системы лиг и мета-механик:
-- - Лиги (leagues)
-- - Мета-прогресс игроков (player_legacy)
-- - Статистика лиг (league_statistics)
-- - Hall of Fame (hall_of_fame_entries)
-- - Legacy Shop (legacy_shop_items)
-- - Legacy Items игроков (player_legacy_items)

-- Создание схемы league, если её нет
CREATE SCHEMA IF NOT EXISTS league;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'league_phase') THEN
        CREATE TYPE league_phase AS ENUM ('Start', 'Rise', 'Crisis', 'Endgame', 'Finale');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hall_of_fame_category') THEN
        CREATE TYPE hall_of_fame_category AS ENUM ('Story', 'Economy', 'PvP', 'Alternative');
    END IF;
END $$;

-- Таблица лиг
CREATE TABLE IF NOT EXISTS league.leagues (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    seed BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    current_phase league_phase NOT NULL DEFAULT 'Start',
    time_acceleration DECIMAL(5, 2) NOT NULL DEFAULT 1.0 CHECK (time_acceleration > 0),
    game_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для leagues
CREATE INDEX IF NOT EXISTS idx_leagues_is_active ON league.leagues(is_active, start_date DESC) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_leagues_current_phase ON league.leagues(current_phase, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_leagues_dates ON league.leagues(start_date, end_date);

-- Таблица мета-прогресса игроков
CREATE TABLE IF NOT EXISTS league.player_legacy (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL, -- Assuming accounts table exists
    legacy_points INTEGER NOT NULL DEFAULT 0 CHECK (legacy_points >= 0),
    global_rating DECIMAL(10, 2) NOT NULL DEFAULT 0.0 CHECK (global_rating >= 0),
    titles TEXT[] DEFAULT '{}',
    cosmetics UUID[] DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(account_id)
);

-- Индексы для player_legacy
CREATE INDEX IF NOT EXISTS idx_player_legacy_account_id ON league.player_legacy(account_id);
CREATE INDEX IF NOT EXISTS idx_player_legacy_global_rating ON league.player_legacy(global_rating DESC);
CREATE INDEX IF NOT EXISTS idx_player_legacy_legacy_points ON league.player_legacy(legacy_points DESC);

-- Таблица статистики лиг
CREATE TABLE IF NOT EXISTS league.league_statistics (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES league.leagues(id) ON DELETE CASCADE,
    phase league_phase NOT NULL,
    player_count INTEGER NOT NULL DEFAULT 0 CHECK (player_count >= 0),
    economy_metrics JSONB,
    pvp_metrics JSONB,
    quest_metrics JSONB,
    top_players JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(league_id, phase)
);

-- Индексы для league_statistics
CREATE INDEX IF NOT EXISTS idx_league_statistics_league_id ON league.league_statistics(league_id, phase);
CREATE INDEX IF NOT EXISTS idx_league_statistics_phase ON league.league_statistics(phase, updated_at DESC);

-- Таблица Hall of Fame
CREATE TABLE IF NOT EXISTS league.hall_of_fame_entries (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES league.leagues(id) ON DELETE CASCADE,
    account_id UUID NOT NULL,
    category hall_of_fame_category NOT NULL,
    rank INTEGER NOT NULL CHECK (rank > 0),
    achievement VARCHAR(255) NOT NULL,
    statue_model UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для hall_of_fame_entries
CREATE INDEX IF NOT EXISTS idx_hall_of_fame_league_id ON league.hall_of_fame_entries(league_id, category, rank);
CREATE INDEX IF NOT EXISTS idx_hall_of_fame_account_id ON league.hall_of_fame_entries(account_id);
CREATE INDEX IF NOT EXISTS idx_hall_of_fame_category ON league.hall_of_fame_entries(category, rank);

-- Таблица предметов Legacy Shop
CREATE TABLE IF NOT EXISTS league.legacy_shop_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    item_description TEXT,
    item_type VARCHAR(50) NOT NULL,
    legacy_points_cost INTEGER NOT NULL CHECK (legacy_points_cost > 0),
    is_available BOOLEAN NOT NULL DEFAULT true,
    max_purchases_per_league INTEGER CHECK (max_purchases_per_league IS NULL OR max_purchases_per_league > 0),
    item_data JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для legacy_shop_items
CREATE INDEX IF NOT EXISTS idx_legacy_shop_items_is_available ON league.legacy_shop_items(is_available, legacy_points_cost);
CREATE INDEX IF NOT EXISTS idx_legacy_shop_items_item_type ON league.legacy_shop_items(item_type, is_available);

-- Таблица Legacy Items игроков
CREATE TABLE IF NOT EXISTS league.player_legacy_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL,
    league_id UUID NOT NULL REFERENCES league.leagues(id) ON DELETE CASCADE,
    shop_item_id UUID NOT NULL REFERENCES league.legacy_shop_items(id) ON DELETE CASCADE,
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_used BOOLEAN NOT NULL DEFAULT false,
    used_at TIMESTAMP,
    item_data JSONB,
    UNIQUE(account_id, league_id, shop_item_id)
);

-- Индексы для player_legacy_items
CREATE INDEX IF NOT EXISTS idx_player_legacy_items_account_id ON league.player_legacy_items(account_id, league_id);
CREATE INDEX IF NOT EXISTS idx_player_legacy_items_league_id ON league.player_legacy_items(league_id, is_used);
CREATE INDEX IF NOT EXISTS idx_player_legacy_items_shop_item_id ON league.player_legacy_items(shop_item_id);

-- Таблица истории покупок Legacy Items
CREATE TABLE IF NOT EXISTS league.legacy_purchase_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL,
    shop_item_id UUID NOT NULL REFERENCES league.legacy_shop_items(id) ON DELETE CASCADE,
    league_id UUID NOT NULL REFERENCES league.leagues(id) ON DELETE CASCADE,
    legacy_points_spent INTEGER NOT NULL CHECK (legacy_points_spent > 0),
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для legacy_purchase_history
CREATE INDEX IF NOT EXISTS idx_legacy_purchase_history_account_id ON league.legacy_purchase_history(account_id, purchased_at DESC);
CREATE INDEX IF NOT EXISTS idx_legacy_purchase_history_league_id ON league.legacy_purchase_history(league_id, purchased_at DESC);
CREATE INDEX IF NOT EXISTS idx_legacy_purchase_history_shop_item_id ON league.legacy_purchase_history(shop_item_id);

-- Таблица регистраций на финальные события
CREATE TABLE IF NOT EXISTS league.end_event_registrations (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    league_id UUID NOT NULL REFERENCES league.leagues(id) ON DELETE CASCADE,
    account_id UUID NOT NULL,
    character_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    registration_data JSONB,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(league_id, account_id)
);

-- Индексы для end_event_registrations
CREATE INDEX IF NOT EXISTS idx_end_event_registrations_league_id ON league.end_event_registrations(league_id);
CREATE INDEX IF NOT EXISTS idx_end_event_registrations_account_id ON league.end_event_registrations(account_id);

-- Комментарии к таблицам
COMMENT ON TABLE league.leagues IS 'Лиги (сезоны) с фазами и временным ускорением';
COMMENT ON TABLE league.player_legacy IS 'Мета-прогресс игроков (титулы, косметика, Legacy Points, глобальный рейтинг)';
COMMENT ON TABLE league.league_statistics IS 'Статистика лиг по фазам (экономика, PvP, квесты, топ игроки)';
COMMENT ON TABLE league.hall_of_fame_entries IS 'Hall of Fame - лучшие игроки каждой лиги по категориям';
COMMENT ON TABLE league.legacy_shop_items IS 'Предметы в Legacy Shop (покупка за Legacy Points)';
COMMENT ON TABLE league.player_legacy_items IS 'Legacy Items игроков (купленные предметы для новой лиги)';
COMMENT ON TABLE league.legacy_purchase_history IS 'История покупок Legacy Items';
COMMENT ON TABLE league.end_event_registrations IS 'Регистрации на финальные события лиги';

-- Комментарии к колонкам
COMMENT ON COLUMN league.leagues.seed IS 'Seed для генерации вариаций мира';
COMMENT ON COLUMN league.leagues.current_phase IS 'Текущая фаза лиги: Start, Rise, Crisis, Endgame, Finale';
COMMENT ON COLUMN league.leagues.time_acceleration IS 'Ускорение времени (15-30 игровых дней за реальный день)';
COMMENT ON COLUMN league.leagues.game_date IS 'Текущая игровая дата';
COMMENT ON COLUMN league.player_legacy.legacy_points IS 'Legacy Points для покупки Legacy Items';
COMMENT ON COLUMN league.player_legacy.global_rating IS 'Глобальный рейтинг с мягким сбросом (20%)';
COMMENT ON COLUMN league.player_legacy.titles IS 'Титулы игрока (массив строк)';
COMMENT ON COLUMN league.player_legacy.cosmetics IS 'Косметика игрока (массив UUID)';
COMMENT ON COLUMN league.league_statistics.phase IS 'Фаза лиги для статистики';
COMMENT ON COLUMN league.league_statistics.economy_metrics IS 'Экономические метрики (JSONB)';
COMMENT ON COLUMN league.league_statistics.pvp_metrics IS 'PvP метрики (JSONB)';
COMMENT ON COLUMN league.league_statistics.quest_metrics IS 'Квестовые метрики (JSONB)';
COMMENT ON COLUMN league.league_statistics.top_players IS 'Топ игроки (JSONB)';
COMMENT ON COLUMN league.hall_of_fame_entries.category IS 'Категория: Story, Economy, PvP, Alternative';
COMMENT ON COLUMN league.hall_of_fame_entries.rank IS 'Ранг в категории';
COMMENT ON COLUMN league.hall_of_fame_entries.statue_model IS 'UUID 3D-модели статуи победителя';
COMMENT ON COLUMN league.legacy_shop_items.max_purchases_per_league IS 'Максимальное количество покупок за лигу (NULL = без ограничений)';
COMMENT ON COLUMN league.player_legacy_items.is_used IS 'Флаг использования Legacy Item в лиге';
COMMENT ON COLUMN league.player_legacy_items.used_at IS 'Время использования Legacy Item';


