-- Issue: #138
-- Achievement System Database Schema Enhancement
-- Дополнение схемы БД системы достижений согласно архитектуре:
-- - Добавление недостающих полей в achievements (icon_url, sort_order)
-- - Добавление таблиц player_titles и achievement_events_log
-- - Обновление индексов

-- Добавление недостающих полей в achievements
ALTER TABLE mvp_core.achievements
ADD COLUMN IF NOT EXISTS icon_url VARCHAR(500),
ADD COLUMN IF NOT EXISTS sort_order INTEGER DEFAULT 0;

-- Обновление индексов для achievements
CREATE INDEX IF NOT EXISTS idx_achievements_category_type ON mvp_core.achievements(category, type);
CREATE INDEX IF NOT EXISTS idx_achievements_code ON mvp_core.achievements(code);
CREATE INDEX IF NOT EXISTS idx_achievements_sort_order ON mvp_core.achievements(sort_order);

-- Таблица титулов игроков
CREATE TABLE IF NOT EXISTS mvp_core.player_titles (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL,
    title_id UUID NOT NULL REFERENCES mvp_core.achievements(id) ON DELETE CASCADE,
    is_equipped BOOLEAN NOT NULL DEFAULT false,
    unlocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(account_id, title_id)
);

-- Индексы для player_titles
CREATE INDEX IF NOT EXISTS idx_player_titles_account_id ON mvp_core.player_titles(account_id);
CREATE INDEX IF NOT EXISTS idx_player_titles_account_equipped ON mvp_core.player_titles(account_id, is_equipped) WHERE is_equipped = true;
CREATE INDEX IF NOT EXISTS idx_player_titles_account_title ON mvp_core.player_titles(account_id, title_id);

-- Таблица лога событий достижений для аналитики
CREATE TABLE IF NOT EXISTS mvp_core.achievement_events_log (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    account_id UUID NOT NULL,
    achievement_id UUID NOT NULL REFERENCES mvp_core.achievements(id) ON DELETE CASCADE,
    event_type VARCHAR(30) NOT NULL CHECK (event_type IN ('progress_updated', 'unlocked', 'reward_distributed')),
    event_data JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для achievement_events_log
CREATE INDEX IF NOT EXISTS idx_achievement_events_log_account_id ON mvp_core.achievement_events_log(account_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_achievement_events_log_achievement_id ON mvp_core.achievement_events_log(achievement_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_achievement_events_log_created_at ON mvp_core.achievement_events_log(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_achievement_events_log_event_type ON mvp_core.achievement_events_log(event_type, created_at DESC);

-- Обновление индексов для player_achievements (добавление композитных индексов из архитектуры)
CREATE INDEX IF NOT EXISTS idx_player_achievements_account_unlocked ON mvp_core.player_achievements(player_id, status) WHERE status = 'unlocked';
CREATE INDEX IF NOT EXISTS idx_player_achievements_achievement_unlocked ON mvp_core.player_achievements(achievement_id, status) WHERE status = 'unlocked';

-- Комментарии к таблицам
COMMENT ON TABLE mvp_core.player_titles IS 'Титулы игроков, полученные за достижения';
COMMENT ON TABLE mvp_core.achievement_events_log IS 'Лог событий достижений для аналитики и мониторинга';

-- Комментарии к колонкам
COMMENT ON COLUMN mvp_core.achievements.icon_url IS 'URL иконки достижения';
COMMENT ON COLUMN mvp_core.achievements.sort_order IS 'Порядок сортировки достижений';
COMMENT ON COLUMN mvp_core.player_titles.account_id IS 'ID аккаунта игрока (для связи с accounts)';
COMMENT ON COLUMN mvp_core.player_titles.title_id IS 'ID достижения, которое даёт титул (reward_type = title)';
COMMENT ON COLUMN mvp_core.player_titles.is_equipped IS 'Флаг экипированного титула';
COMMENT ON COLUMN mvp_core.achievement_events_log.account_id IS 'ID аккаунта игрока';
COMMENT ON COLUMN mvp_core.achievement_events_log.event_type IS 'Тип события: progress_updated, unlocked, reward_distributed';
COMMENT ON COLUMN mvp_core.achievement_events_log.event_data IS 'JSONB данные события для аналитики';


