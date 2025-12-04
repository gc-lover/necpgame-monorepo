-- Issue: #316
-- Maintenance Mode System Database Schema
-- Создание таблиц для системы режима обслуживания:
-- - maintenance_windows (окна обслуживания)
-- - maintenance_status (текущий статус обслуживания)
-- - maintenance_notifications (уведомления об обслуживании)
-- - maintenance_telemetry (телеметрия обслуживания)

-- Создание схемы admin, если её нет (уже создана в V1_84, но для безопасности)
CREATE SCHEMA IF NOT EXISTS admin;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'maintenance_window_type') THEN
        CREATE TYPE maintenance_window_type AS ENUM ('scheduled', 'emergency', 'hot_fix', 'rollback', 'upgrade');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'maintenance_window_status') THEN
        CREATE TYPE maintenance_window_status AS ENUM ('planned', 'starting', 'in_progress', 'ending', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'maintenance_notification_type') THEN
        CREATE TYPE maintenance_notification_type AS ENUM ('advance_24h', 'advance_1h', 'immediate', 'completion');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'maintenance_notification_channel') THEN
        CREATE TYPE maintenance_notification_channel AS ENUM ('in_game', 'email', 'push', 'social_media');
    END IF;
END $$;

-- Таблица окон обслуживания
CREATE TABLE IF NOT EXISTS admin.maintenance_windows (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  created_by UUID,
  reason TEXT NOT NULL,
  impact TEXT,
  affected_services JSONB DEFAULT '[]'::jsonb,
  scheduled_start TIMESTAMP NOT NULL,
  scheduled_end TIMESTAMP NOT NULL,
  actual_start TIMESTAMP,
  actual_end TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estimated_duration INTEGER CHECK (estimated_duration IS NULL OR estimated_duration > 0),
  type maintenance_window_type NOT NULL,
  status maintenance_window_status NOT NULL DEFAULT 'planned',
  CONSTRAINT fk_maintenance_windows_created_by FOREIGN KEY (created_by) REFERENCES admin.admin_users(id) ON DELETE SET NULL,
  CONSTRAINT chk_maintenance_windows_scheduled_dates CHECK (scheduled_end > scheduled_start),
  CONSTRAINT chk_maintenance_windows_actual_dates CHECK (
        (actual_start IS NULL AND actual_end IS NULL) OR
        (actual_start IS NOT NULL AND (actual_end IS NULL OR actual_end >= actual_start))
    )
);

-- Индексы для maintenance_windows
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_type ON admin.maintenance_windows(type, scheduled_start DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_status ON admin.maintenance_windows(status, scheduled_start DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_scheduled_start ON admin.maintenance_windows(scheduled_start DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_scheduled_end ON admin.maintenance_windows(scheduled_end DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_active ON admin.maintenance_windows(status, scheduled_start, scheduled_end) WHERE status IN ('planned', 'starting', 'in_progress', 'ending');
CREATE INDEX IF NOT EXISTS idx_maintenance_windows_created_by ON admin.maintenance_windows(created_by) WHERE created_by IS NOT NULL;

-- Таблица текущего статуса обслуживания
CREATE TABLE IF NOT EXISTS admin.maintenance_status (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  current_window_id UUID,
  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_maintenance_mode BOOLEAN NOT NULL DEFAULT false,
  connection_blocking_enabled BOOLEAN NOT NULL DEFAULT false,
  graceful_shutdown_in_progress BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT fk_maintenance_status_window FOREIGN KEY (current_window_id) REFERENCES admin.maintenance_windows(id) ON DELETE SET NULL,
  CONSTRAINT chk_maintenance_status_single_row CHECK (id = '00000000-0000-0000-0000-000000000001'::uuid)
);

-- Установка единственной строки в maintenance_status
INSERT INTO admin.maintenance_status (id, is_maintenance_mode, connection_blocking_enabled, graceful_shutdown_in_progress)
VALUES ('00000000-0000-0000-0000-000000000001'::uuid, false, false, false)
ON CONFLICT (id) DO NOTHING;

-- Индексы для maintenance_status
CREATE INDEX IF NOT EXISTS idx_maintenance_status_is_maintenance_mode ON admin.maintenance_status(is_maintenance_mode) WHERE is_maintenance_mode = true;
CREATE INDEX IF NOT EXISTS idx_maintenance_status_current_window_id ON admin.maintenance_status(current_window_id) WHERE current_window_id IS NOT NULL;

-- Таблица уведомлений об обслуживании
CREATE TABLE IF NOT EXISTS admin.maintenance_notifications (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  window_id UUID NOT NULL,
  content JSONB DEFAULT '{}'::jsonb,
  sent_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  notification_type maintenance_notification_type NOT NULL,
  channel maintenance_notification_channel NOT NULL,
  CONSTRAINT fk_maintenance_notifications_window FOREIGN KEY (window_id) REFERENCES admin.maintenance_windows(id) ON DELETE CASCADE
);

-- Индексы для maintenance_notifications
CREATE INDEX IF NOT EXISTS idx_maintenance_notifications_window_id ON admin.maintenance_notifications(window_id, notification_type);
CREATE INDEX IF NOT EXISTS idx_maintenance_notifications_type ON admin.maintenance_notifications(notification_type, sent_at DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_notifications_channel ON admin.maintenance_notifications(channel, sent_at DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_notifications_sent_at ON admin.maintenance_notifications(sent_at DESC) WHERE sent_at IS NOT NULL;

-- Таблица телеметрии обслуживания
CREATE TABLE IF NOT EXISTS admin.maintenance_telemetry (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  window_id UUID NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  event_data JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_maintenance_telemetry_window FOREIGN KEY (window_id) REFERENCES admin.maintenance_windows(id) ON DELETE CASCADE
);

-- Индексы для maintenance_telemetry
CREATE INDEX IF NOT EXISTS idx_maintenance_telemetry_event_type ON admin.maintenance_telemetry(event_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_telemetry_window_id ON admin.maintenance_telemetry(window_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_maintenance_telemetry_created_at ON admin.maintenance_telemetry(created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE admin.maintenance_windows IS 'Окна обслуживания системы';
COMMENT ON TABLE admin.maintenance_status IS 'Текущий статус обслуживания (единственная строка)';
COMMENT ON TABLE admin.maintenance_notifications IS 'Уведомления об обслуживании';
COMMENT ON TABLE admin.maintenance_telemetry IS 'Телеметрия обслуживания';

-- Комментарии к колонкам
COMMENT ON COLUMN admin.maintenance_windows.type IS 'Тип окна: scheduled, emergency, hot_fix, rollback, upgrade';
COMMENT ON COLUMN admin.maintenance_windows.status IS 'Статус окна: planned, starting, in_progress, ending, completed, cancelled';
COMMENT ON COLUMN admin.maintenance_windows.estimated_duration IS 'Оценочная длительность в секундах';
COMMENT ON COLUMN admin.maintenance_windows.affected_services IS 'Затронутые сервисы (JSONB)';
COMMENT ON COLUMN admin.maintenance_status.is_maintenance_mode IS 'Включен ли режим обслуживания';
COMMENT ON COLUMN admin.maintenance_status.current_window_id IS 'ID текущего окна обслуживания';
COMMENT ON COLUMN admin.maintenance_status.connection_blocking_enabled IS 'Блокировка новых подключений';
COMMENT ON COLUMN admin.maintenance_status.graceful_shutdown_in_progress IS 'Идет ли плавное завершение работы';
COMMENT ON COLUMN admin.maintenance_notifications.notification_type IS 'Тип уведомления: advance_24h, advance_1h, immediate, completion';
COMMENT ON COLUMN admin.maintenance_notifications.channel IS 'Канал уведомления: in_game, email, push, social_media';
COMMENT ON COLUMN admin.maintenance_notifications.content IS 'Содержимое уведомления (JSONB)';

