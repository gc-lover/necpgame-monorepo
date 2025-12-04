-- Issue: #403
-- Admin Moderation System Database Schema
-- Создание таблиц для системы администрирования и модерации:
-- - admin_users (администраторы и модераторы)
-- - admin_actions_log (журнал действий администраторов)
-- - admin_sanctions (санкции, выданные администраторами)

-- Создание схемы admin, если её нет
CREATE SCHEMA IF NOT EXISTS admin;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'admin_role') THEN
        CREATE TYPE admin_role AS ENUM ('SUPER_ADMIN', 'ADMIN', 'MODERATOR');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'admin_action_target_type') THEN
        CREATE TYPE admin_action_target_type AS ENUM ('player', 'economy', 'content', 'moderation');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'admin_sanction_type') THEN
        CREATE TYPE admin_sanction_type AS ENUM ('WARNING', 'TEMPORARY_BAN', 'PERMANENT_BAN', 'MUTE', 'KICK');
    END IF;
END $$;

-- Таблица администраторов и модераторов
CREATE TABLE IF NOT EXISTS admin.admin_users (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  last_login_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN NOT NULL DEFAULT true,
  role admin_role NOT NULL DEFAULT 'MODERATOR'
);

-- Индексы для admin_users
CREATE INDEX IF NOT EXISTS idx_admin_users_username ON admin.admin_users(username);
CREATE INDEX IF NOT EXISTS idx_admin_users_email ON admin.admin_users(email);
CREATE INDEX IF NOT EXISTS idx_admin_users_role ON admin.admin_users(role);
CREATE INDEX IF NOT EXISTS idx_admin_users_is_active ON admin.admin_users(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_admin_users_last_login_at ON admin.admin_users(last_login_at DESC) WHERE last_login_at IS NOT NULL;

-- Таблица журнала действий администраторов
CREATE TABLE IF NOT EXISTS admin.admin_actions_log (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  admin_id UUID NOT NULL,
  target_id UUID,
  reason TEXT,
  user_agent TEXT,
  action_type VARCHAR(100) NOT NULL,
  ip_address VARCHAR(45),
  details JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  target_type admin_action_target_type NOT NULL,
  CONSTRAINT fk_admin_actions_log_admin FOREIGN KEY (admin_id) REFERENCES admin.admin_users(id) ON DELETE RESTRICT
);

-- Индексы для admin_actions_log
CREATE INDEX IF NOT EXISTS idx_admin_actions_log_admin_id ON admin.admin_actions_log(admin_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_actions_log_action_type ON admin.admin_actions_log(action_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_actions_log_target ON admin.admin_actions_log(target_type, target_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_actions_log_created_at ON admin.admin_actions_log(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_actions_log_ip_address ON admin.admin_actions_log(ip_address) WHERE ip_address IS NOT NULL;

-- Таблица санкций, выданных администраторами
CREATE TABLE IF NOT EXISTS admin.admin_sanctions (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL,
  admin_id UUID NOT NULL,
  reason TEXT NOT NULL,
  expires_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  duration INTEGER CHECK (duration IS NULL OR duration > 0),
  is_active BOOLEAN NOT NULL DEFAULT true,
  sanction_type admin_sanction_type NOT NULL,
  CONSTRAINT fk_admin_sanctions_admin FOREIGN KEY (admin_id) REFERENCES admin.admin_users(id) ON DELETE RESTRICT,
  CONSTRAINT chk_admin_sanctions_expires_at CHECK (
        (sanction_type = 'PERMANENT_BAN' AND expires_at IS NULL) OR
        (sanction_type != 'PERMANENT_BAN' AND expires_at IS NOT NULL)
    )
);

-- Индексы для admin_sanctions
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_player_id ON admin.admin_sanctions(player_id, is_active, expires_at);
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_admin_id ON admin.admin_sanctions(admin_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_is_active ON admin.admin_sanctions(is_active, expires_at) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_expires_at ON admin.admin_sanctions(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_sanction_type ON admin.admin_sanctions(sanction_type, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_admin_sanctions_active_expired ON admin.admin_sanctions(is_active, expires_at) WHERE is_active = true AND expires_at <= CURRENT_TIMESTAMP;

-- Комментарии к таблицам
COMMENT ON TABLE admin.admin_users IS 'Администраторы и модераторы системы';
COMMENT ON TABLE admin.admin_actions_log IS 'Журнал действий администраторов';
COMMENT ON TABLE admin.admin_sanctions IS 'Санкции, выданные администраторами';

-- Комментарии к колонкам
COMMENT ON COLUMN admin.admin_users.role IS 'Роль: SUPER_ADMIN, ADMIN, MODERATOR';
COMMENT ON COLUMN admin.admin_users.is_active IS 'Активен ли администратор';
COMMENT ON COLUMN admin.admin_actions_log.action_type IS 'Тип действия (например, ban_player, edit_economy, moderate_content)';
COMMENT ON COLUMN admin.admin_actions_log.target_type IS 'Тип цели: player, economy, content, moderation';
COMMENT ON COLUMN admin.admin_actions_log.details IS 'Детали действия (JSONB)';
COMMENT ON COLUMN admin.admin_sanctions.sanction_type IS 'Тип санкции: WARNING, TEMPORARY_BAN, PERMANENT_BAN, MUTE, KICK';
COMMENT ON COLUMN admin.admin_sanctions.duration IS 'Длительность в секундах (NULL для постоянных)';
COMMENT ON COLUMN admin.admin_sanctions.is_active IS 'Активна ли санкция';
COMMENT ON COLUMN admin.admin_sanctions.expires_at IS 'Время истечения (NULL для постоянных)';

