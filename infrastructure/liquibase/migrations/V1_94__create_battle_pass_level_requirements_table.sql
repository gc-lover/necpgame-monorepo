--liquibase formatted sql

-- changeset gc-lover:1636-3
-- Issue: #1636
CREATE TABLE IF NOT EXISTS gameplay.battle_pass_level_requirements (
    season_id UUID NOT NULL REFERENCES gameplay.battle_pass_seasons(id),
    level INT NOT NULL,
    xp_required INT NOT NULL,
    cumulative_xp INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (season_id, level)
);

COMMENT ON TABLE gameplay.battle_pass_level_requirements IS 'Таблица для хранения требований к опыту для каждого уровня боевого пропуска.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.season_id IS 'Идентификатор сезона боевого пропуска.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.level IS 'Уровень боевого пропуска.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.xp_required IS 'Количество опыта, необходимое для достижения этого уровня.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.cumulative_xp IS 'Общее количество опыта, необходимое для достижения этого уровня.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.created_at IS 'Время создания записи.';
COMMENT ON COLUMN gameplay.battle_pass_level_requirements.updated_at IS 'Время последнего обновления записи.';









