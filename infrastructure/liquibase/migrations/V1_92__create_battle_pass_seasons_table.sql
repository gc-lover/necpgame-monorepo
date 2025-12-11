--liquibase formatted sql

-- changeset gc-lover:1636-1
-- Issue: #1636
CREATE TABLE IF NOT EXISTS gameplay.battle_pass_seasons (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMPTZ NOT NULL,
    end_date TIMESTAMPTZ NOT NULL,
    max_level INT NOT NULL DEFAULT 100,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE gameplay.battle_pass_seasons IS 'Таблица для хранения информации о сезонах боевого пропуска.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.id IS 'Уникальный идентификатор сезона.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.name IS 'Название сезона.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.start_date IS 'Дата и время начала сезона.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.end_date IS 'Дата и время окончания сезона.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.max_level IS 'Максимальный уровень боевого пропуска в сезоне.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.is_active IS 'Признак активности сезона.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.created_at IS 'Время создания записи.';
COMMENT ON COLUMN gameplay.battle_pass_seasons.updated_at IS 'Время последнего обновления записи.';





