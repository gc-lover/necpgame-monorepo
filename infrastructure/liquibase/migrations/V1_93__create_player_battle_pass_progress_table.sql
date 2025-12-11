--liquibase formatted sql

-- changeset gc-lover:1636-2
-- Issue: #1636
CREATE TABLE IF NOT EXISTS gameplay.player_battle_pass_progress (
    character_id UUID PRIMARY KEY,
    season_id UUID NOT NULL REFERENCES gameplay.battle_pass_seasons(id),
    premium_purchased_at TIMESTAMPTZ,
    level INT NOT NULL DEFAULT 1,
    xp INT NOT NULL DEFAULT 0,
    xp_to_next_level INT NOT NULL DEFAULT 0,
    has_premium BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE gameplay.player_battle_pass_progress IS 'Таблица для хранения прогресса игроков в боевом пропуске.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.character_id IS 'Уникальный идентификатор персонажа.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.season_id IS 'Идентификатор сезона боевого пропуска.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.premium_purchased_at IS 'Дата и время покупки премиум пропуска.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.level IS 'Текущий уровень боевого пропуска игрока.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.xp IS 'Текущее количество опыта игрока в боевом пропуске.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.xp_to_next_level IS 'Количество опыта, необходимое для следующего уровня.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.has_premium IS 'Признак наличия премиум пропуска.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.created_at IS 'Время создания записи.';
COMMENT ON COLUMN gameplay.player_battle_pass_progress.updated_at IS 'Время последнего обновления записи.';





