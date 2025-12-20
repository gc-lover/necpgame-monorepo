-- Issue: #140874394
-- Notification System Database Schema
-- Создание таблиц для системы уведомлений:
-- - notifications.notifications (уведомления)
-- - notifications.user_notification_settings (настройки пользователей)
-- - notifications.notification_reads (прочтения уведомлений)
-- - notifications.notification_delivery_logs (логи доставки)

-- Создание схемы notifications, если её нет
CREATE SCHEMA IF NOT EXISTS notifications;

-- Создание ENUM типов для оптимизации (меньше места чем VARCHAR)
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'notification_type') THEN
        CREATE TYPE notification_type AS ENUM ('system', 'social', 'combat', 'quest', 'guild', 'marketplace', 'admin');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'notification_priority') THEN
        CREATE TYPE notification_priority AS ENUM ('low', 'normal', 'high', 'urgent');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'notification_status') THEN
        CREATE TYPE notification_status AS ENUM ('queued', 'sent', 'delivered', 'read', 'failed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'notification_channel') THEN
        CREATE TYPE notification_channel AS ENUM ('in_game', 'push', 'email', 'sms');
    END IF;
END $$;

-- Основная таблица уведомлений (column order: large → small for alignment)
CREATE TABLE IF NOT EXISTS notifications.notifications (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id UUID NOT NULL,                    -- 16 bytes (UUID)
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  expires_at TIMESTAMP,                     -- 8 bytes (nullable)
  data JSONB DEFAULT '{}'::jsonb,           -- Variable (JSON)
  title VARCHAR(200) NOT NULL,              -- Variable (up to 200 chars)
  body TEXT,                               -- Variable
  type notification_type NOT NULL,         -- 1 byte (enum)
  priority notification_priority NOT NULL DEFAULT 'normal', -- 1 byte (enum)
  status notification_status NOT NULL DEFAULT 'queued',    -- 1 byte (enum)
  read_at TIMESTAMP,                       -- 8 bytes (nullable)
  -- Foreign key constraint
  CONSTRAINT fk_notifications_user_id FOREIGN KEY (user_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  -- Performance indexes
  INDEX idx_notifications_user_id (user_id),
  INDEX idx_notifications_status (status),
  INDEX idx_notifications_type (type),
  INDEX idx_notifications_priority (priority),
  INDEX idx_notifications_created_at (created_at),
  INDEX idx_notifications_expires_at (expires_at),
  INDEX idx_notifications_user_status (user_id, status),
  INDEX idx_notifications_user_type (user_id, type)
);

-- Таблица настроек уведомлений пользователей
CREATE TABLE IF NOT EXISTS notifications.user_notification_settings (
  user_id UUID PRIMARY KEY,                 -- 16 bytes
  enabled BOOLEAN NOT NULL DEFAULT true,    -- 1 byte
  channel_settings JSONB DEFAULT '{
    "in_game": {"enabled": true, "min_priority": "low"},
    "push": {"enabled": false, "min_priority": "normal"},
    "email": {"enabled": false, "min_priority": "high"},
    "sms": {"enabled": false, "min_priority": "urgent"}
  }'::jsonb,                                -- JSONB
  type_settings JSONB DEFAULT '{
    "system": {"enabled": true, "channels": ["in_game"]},
    "social": {"enabled": true, "channels": ["in_game", "push"]},
    "combat": {"enabled": true, "channels": ["in_game"]},
    "quest": {"enabled": true, "channels": ["in_game"]},
    "guild": {"enabled": true, "channels": ["in_game", "push"]},
    "marketplace": {"enabled": true, "channels": ["in_game"]},
    "admin": {"enabled": true, "channels": ["in_game", "push"]}
  }'::jsonb,                                -- JSONB
  quiet_hours_start TIME DEFAULT '22:00',   -- 8 bytes (nullable TIME)
  quiet_hours_end TIME DEFAULT '08:00',     -- 8 bytes (nullable TIME)
  timezone VARCHAR(50) DEFAULT 'UTC',       -- Variable
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  -- Foreign key constraint
  CONSTRAINT fk_user_settings_user_id FOREIGN KEY (user_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE
);

-- Таблица прочтений уведомлений (для аналитики)
CREATE TABLE IF NOT EXISTS notifications.notification_reads (
  id BIGSERIAL PRIMARY KEY,                 -- 8 bytes
  notification_id UUID NOT NULL,            -- 16 bytes
  user_id UUID NOT NULL,                    -- 16 bytes
  read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  channel notification_channel,             -- 1 byte (enum)
  user_agent TEXT,                          -- Variable
  ip_address INET,                          -- 4-16 bytes
  -- Foreign key constraints
  CONSTRAINT fk_reads_notification_id FOREIGN KEY (notification_id) REFERENCES notifications.notifications(id) ON DELETE CASCADE,
  CONSTRAINT fk_reads_user_id FOREIGN KEY (user_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  -- Performance indexes
  INDEX idx_notification_reads_user_id (user_id),
  INDEX idx_notification_reads_notification_id (notification_id),
  INDEX idx_notification_reads_read_at (read_at),
  INDEX idx_notification_reads_channel (channel)
);

-- Таблица логов доставки (для отладки и аналитики)
CREATE TABLE IF NOT EXISTS notifications.notification_delivery_logs (
  id BIGSERIAL PRIMARY KEY,                 -- 8 bytes
  notification_id UUID NOT NULL,            -- 16 bytes
  user_id UUID NOT NULL,                    -- 16 bytes
  channel notification_channel NOT NULL,    -- 1 byte
  status VARCHAR(20) NOT NULL,              -- Variable (success, failed, pending)
  attempt_count INTEGER DEFAULT 1,          -- 4 bytes
  error_message TEXT,                       -- Variable
  delivered_at TIMESTAMP,                   -- 8 bytes (nullable)
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  -- Foreign key constraints
  CONSTRAINT fk_delivery_logs_notification_id FOREIGN KEY (notification_id) REFERENCES notifications.notifications(id) ON DELETE CASCADE,
  CONSTRAINT fk_delivery_logs_user_id FOREIGN KEY (user_id) REFERENCES mvp_core.characters(id) ON DELETE CASCADE,
  -- Performance indexes
  INDEX idx_delivery_logs_notification_id (notification_id),
  INDEX idx_delivery_logs_user_id (user_id),
  INDEX idx_delivery_logs_status (status),
  INDEX idx_delivery_logs_channel (channel),
  INDEX idx_delivery_logs_created_at (created_at)
);

-- Partitioning для таблиц с большим объемом данных (time-series partitioning)
-- Разделяем notification_reads по месяцам для производительности
-- Issue: #140874394 - Performance optimization for notification analytics

-- Функция для создания партиций notification_reads
CREATE OR REPLACE FUNCTION notifications.create_notification_reads_partition(target_month DATE)
RETURNS VOID AS $$
DECLARE
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    partition_name := 'notification_reads_' || to_char(target_month, 'YYYY_MM');
    start_date := date_trunc('month', target_month);
    end_date := start_date + INTERVAL '1 month';

    EXECUTE format('
        CREATE TABLE IF NOT EXISTS notifications.%I
        PARTITION OF notifications.notification_reads
        FOR VALUES FROM (%L) TO (%L)',
        partition_name, start_date, end_date);

    -- Создаем индексы для партиции
    EXECUTE format('
        CREATE INDEX IF NOT EXISTS idx_%s_read_at ON notifications.%I (read_at)',
        partition_name, partition_name);
END;
$$ LANGUAGE plpgsql;

-- Создаем партиции для следующих 6 месяцев (опережающее создание)
DO $$
DECLARE
    i INTEGER := 0;
    target_date DATE;
BEGIN
    FOR i IN 0..5 LOOP
        target_date := CURRENT_DATE + (i || ' months')::INTERVAL;
        target_date := date_trunc('month', target_date);
        PERFORM notifications.create_notification_reads_partition(target_date);
    END LOOP;
END $$;

-- Аналогично для delivery_logs
CREATE OR REPLACE FUNCTION notifications.create_delivery_logs_partition(target_month DATE)
RETURNS VOID AS $$
DECLARE
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    partition_name := 'delivery_logs_' || to_char(target_month, 'YYYY_MM');
    start_date := date_trunc('month', target_month);
    end_date := start_date + INTERVAL '1 month';

    EXECUTE format('
        CREATE TABLE IF NOT EXISTS notifications.%I
        PARTITION OF notifications.notification_delivery_logs
        FOR VALUES FROM (%L) TO (%L)',
        partition_name, start_date, end_date);
END;
$$ LANGUAGE plpgsql;

-- Создаем партиции для delivery_logs
DO $$
DECLARE
    i INTEGER := 0;
    target_date DATE;
BEGIN
    FOR i IN 0..5 LOOP
        target_date := CURRENT_DATE + (i || ' months')::INTERVAL;
        target_date := date_trunc('month', target_date);
        PERFORM notifications.create_delivery_logs_partition(target_date);
    END LOOP;
END $$;

-- Триггеры для автоматического обновления updated_at
CREATE OR REPLACE FUNCTION notifications.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Применяем триггеры
DROP TRIGGER IF EXISTS update_notifications_updated_at ON notifications.notifications;
CREATE TRIGGER update_notifications_updated_at
    BEFORE UPDATE ON notifications.notifications
    FOR EACH ROW EXECUTE FUNCTION notifications.update_updated_at_column();

DROP TRIGGER IF EXISTS update_user_settings_updated_at ON notifications.user_notification_settings;
CREATE TRIGGER update_user_settings_updated_at
    BEFORE UPDATE ON notifications.user_notification_settings
    FOR EACH ROW EXECUTE FUNCTION notifications.update_updated_at_column();

-- Materialized view для аналитики уведомлений (быстрые запросы статистики)
CREATE MATERIALIZED VIEW IF NOT EXISTS notifications.notification_stats AS
SELECT
    DATE_TRUNC('day', created_at) as date,
    type,
    status,
    COUNT(*) as count,
    COUNT(*) FILTER (WHERE read_at IS NOT NULL) as read_count,
    AVG(EXTRACT(EPOCH FROM (read_at - created_at))) as avg_read_time_seconds
FROM notifications.notifications
WHERE created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY DATE_TRUNC('day', created_at), type, status
ORDER BY date DESC, type, status;

-- Индекс для materialized view
CREATE INDEX IF NOT EXISTS idx_notification_stats_date_type ON notifications.notification_stats (date, type);

-- Функция для обновления статистики (вызывать по cron)
CREATE OR REPLACE FUNCTION notifications.refresh_notification_stats()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY notifications.notification_stats;
END;
$$ LANGUAGE plpgsql;

-- Комментарии для документации
COMMENT ON SCHEMA notifications IS 'Schema for notification system - handles in-game notifications, push notifications, and delivery tracking';
COMMENT ON TABLE notifications.notifications IS 'Core notifications table with partitioning and optimized indexes for MMOFPS performance';
COMMENT ON TABLE notifications.user_notification_settings IS 'User preferences for notification delivery channels and types';
COMMENT ON TABLE notifications.notification_reads IS 'Analytics table for notification read events (partitioned by month)';
COMMENT ON TABLE notifications.notification_delivery_logs IS 'Delivery tracking and debugging information (partitioned by month)';
COMMENT ON MATERIALIZED VIEW notifications.notification_stats IS 'Pre-calculated notification statistics for dashboard queries';
