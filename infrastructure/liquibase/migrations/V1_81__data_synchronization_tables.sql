-- Issue: #142102432
-- Data Synchronization System Database Schema
-- Создание таблиц для системы синхронизации данных между слоями и механиками:
-- - saga (Saga транзакции)
-- - sync_state (состояние синхронизации)
-- - conflicts (конфликты синхронизации)
-- - consistency_checks (проверки консистентности)
-- - Обновление outbox (добавление полей для обработки)

-- Создание схемы mvp_meta, если её нет (уже создана в V1_0, но для безопасности)
CREATE SCHEMA IF NOT EXISTS mvp_meta;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'saga_status') THEN
        CREATE TYPE saga_status AS ENUM ('pending', 'in_progress', 'completed', 'compensating', 'failed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'outbox_status') THEN
        CREATE TYPE outbox_status AS ENUM ('pending', 'processing', 'processed', 'failed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'conflict_type') THEN
        CREATE TYPE conflict_type AS ENUM ('version', 'timestamp', 'state', 'custom');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'consistency_check_status') THEN
        CREATE TYPE consistency_check_status AS ENUM ('pending', 'running', 'passed', 'failed', 'warning');
    END IF;
END $$;

-- Обновление таблицы outbox (добавление полей для обработки)
DO $$ BEGIN
    -- Добавляем поля, если их нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'mvp_meta' AND table_name = 'outbox' AND column_name = 'processed_at') THEN
        ALTER TABLE mvp_meta.outbox ADD COLUMN processed_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'mvp_meta' AND table_name = 'outbox' AND column_name = 'status') THEN
        ALTER TABLE mvp_meta.outbox ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'pending';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'mvp_meta' AND table_name = 'outbox' AND column_name = 'retry_count') THEN
        ALTER TABLE mvp_meta.outbox ADD COLUMN retry_count INTEGER NOT NULL DEFAULT 0 CHECK (retry_count >= 0);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'mvp_meta' AND table_name = 'outbox' AND column_name = 'error_message') THEN
        ALTER TABLE mvp_meta.outbox ADD COLUMN error_message TEXT;
    END IF;
END $$;

-- Индексы для outbox (если их еще нет)
CREATE INDEX IF NOT EXISTS idx_outbox_status_created_at 
    ON mvp_meta.outbox(status, created_at) WHERE status = 'pending';
CREATE INDEX IF NOT EXISTS idx_outbox_aggregate 
    ON mvp_meta.outbox(aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS idx_outbox_event_type 
    ON mvp_meta.outbox(event_type);

-- Таблица Saga транзакций
CREATE TABLE IF NOT EXISTS mvp_meta.saga (
  saga_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  -- контекст Saga
    error_message TEXT,
  saga_type VARCHAR(255) NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'in_progress', 'completed', 'compensating', 'failed', 'cancelled')),
  steps JSONB NOT NULL DEFAULT '[]',
  -- массив шагов Saga
    compensation_steps JSONB DEFAULT '[]',
  -- компенсирующие шаги
    context JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  current_step INTEGER NOT NULL DEFAULT 0 CHECK (current_step >= 0),
  total_steps INTEGER NOT NULL DEFAULT 0 CHECK (total_steps >= 0)
);

-- Индексы для saga
CREATE INDEX IF NOT EXISTS idx_saga_status 
    ON mvp_meta.saga(status) WHERE status IN ('pending', 'in_progress', 'compensating');
CREATE INDEX IF NOT EXISTS idx_saga_type 
    ON mvp_meta.saga(saga_type);
CREATE INDEX IF NOT EXISTS idx_saga_created_at 
    ON mvp_meta.saga(created_at DESC);

-- Таблица состояния синхронизации
CREATE TABLE IF NOT EXISTS mvp_meta.sync_state (
  -- FK characters или сервис
    PRIMARY KEY (key, category),
  updated_by UUID,
  key VARCHAR(255) NOT NULL,
  category VARCHAR(100) NOT NULL,
  value JSONB NOT NULL DEFAULT '{}',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  version INTEGER NOT NULL DEFAULT 1 CHECK (version >= 0)
);

-- Индексы для sync_state
CREATE INDEX IF NOT EXISTS idx_sync_state_category 
    ON mvp_meta.sync_state(category);
CREATE INDEX IF NOT EXISTS idx_sync_state_updated_at 
    ON mvp_meta.sync_state(updated_at DESC);
CREATE INDEX IF NOT EXISTS idx_sync_state_updated_by 
    ON mvp_meta.sync_state(updated_by) WHERE updated_by IS NOT NULL;

-- Таблица конфликтов синхронизации
CREATE TABLE IF NOT EXISTS mvp_meta.conflicts (
  conflict_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  -- стратегия разрешения конфликта
    resolved_by UUID,
  key VARCHAR(255) NOT NULL,
  category VARCHAR(100) NOT NULL,
  conflict_type VARCHAR(100) NOT NULL CHECK (conflict_type IN ('version', 'timestamp', 'state', 'custom')),
  resolution_strategy VARCHAR(100),
  old_value JSONB,
  new_value JSONB,
  -- FK characters или сервис
    resolution_data JSONB DEFAULT '{}',
  detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_at TIMESTAMP
);

-- Индексы для conflicts
CREATE INDEX IF NOT EXISTS idx_conflicts_key_category 
    ON mvp_meta.conflicts(key, category);
CREATE INDEX IF NOT EXISTS idx_conflicts_type 
    ON mvp_meta.conflicts(conflict_type);
CREATE INDEX IF NOT EXISTS idx_conflicts_resolved_at 
    ON mvp_meta.conflicts(resolved_at) WHERE resolved_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_conflicts_detected_at 
    ON mvp_meta.conflicts(detected_at DESC);

-- Таблица проверок консистентности
CREATE TABLE IF NOT EXISTS mvp_meta.consistency_checks (
  service_name VARCHAR(255) NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'running', 'passed', 'failed', 'warning')),
  violations JSONB DEFAULT '[]',
  -- длительность проверки в миллисекундах
    metadata JSONB DEFAULT '{}' -- дополнительная информация о проверке,
  -- массив нарушений консистентности
    checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  duration_ms INTEGER,
  check_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  check_type VARCHAR(100) NOT NULL
);

-- Индексы для consistency_checks
CREATE INDEX IF NOT EXISTS idx_consistency_checks_service 
    ON mvp_meta.consistency_checks(service_name);
CREATE INDEX IF NOT EXISTS idx_consistency_checks_status 
    ON mvp_meta.consistency_checks(status) WHERE status IN ('pending', 'running', 'failed', 'warning');
CREATE INDEX IF NOT EXISTS idx_consistency_checks_type 
    ON mvp_meta.consistency_checks(check_type);
CREATE INDEX IF NOT EXISTS idx_consistency_checks_checked_at 
    ON mvp_meta.consistency_checks(checked_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE mvp_meta.outbox IS 'Outbox Pattern для гарантированной доставки событий через Event Bus';
COMMENT ON TABLE mvp_meta.saga IS 'Saga Pattern для распределенных транзакций между сервисами';
COMMENT ON TABLE mvp_meta.sync_state IS 'Состояние синхронизации данных между слоями и механиками';
COMMENT ON TABLE mvp_meta.conflicts IS 'Конфликты синхронизации данных для разрешения';
COMMENT ON TABLE mvp_meta.consistency_checks IS 'Проверки консистентности данных между сервисами';

-- Комментарии к колонкам
COMMENT ON COLUMN mvp_meta.outbox.processed_at IS 'Время обработки события';
COMMENT ON COLUMN mvp_meta.outbox.status IS 'Статус обработки: pending, processing, processed, failed';
COMMENT ON COLUMN mvp_meta.outbox.retry_count IS 'Количество попыток обработки (0+)';
COMMENT ON COLUMN mvp_meta.outbox.error_message IS 'Сообщение об ошибке при обработке';
COMMENT ON COLUMN mvp_meta.saga.saga_type IS 'Тип Saga транзакции';
COMMENT ON COLUMN mvp_meta.saga.status IS 'Статус Saga: pending, in_progress, completed, compensating, failed, cancelled';
COMMENT ON COLUMN mvp_meta.saga.current_step IS 'Текущий шаг Saga (0+)';
COMMENT ON COLUMN mvp_meta.saga.total_steps IS 'Общее количество шагов Saga (0+)';
COMMENT ON COLUMN mvp_meta.saga.steps IS 'Массив шагов Saga в JSONB';
COMMENT ON COLUMN mvp_meta.saga.compensation_steps IS 'Компенсирующие шаги Saga в JSONB';
COMMENT ON COLUMN mvp_meta.saga.context IS 'Контекст Saga в JSONB';
COMMENT ON COLUMN mvp_meta.sync_state.key IS 'Ключ состояния синхронизации';
COMMENT ON COLUMN mvp_meta.sync_state.category IS 'Категория состояния (service, layer, mechanic)';
COMMENT ON COLUMN mvp_meta.sync_state.value IS 'Значение состояния в JSONB';
COMMENT ON COLUMN mvp_meta.sync_state.version IS 'Версия состояния для оптимистичной блокировки (0+)';
COMMENT ON COLUMN mvp_meta.conflicts.conflict_type IS 'Тип конфликта: version, timestamp, state, custom';
COMMENT ON COLUMN mvp_meta.conflicts.resolution_strategy IS 'Стратегия разрешения конфликта';
COMMENT ON COLUMN mvp_meta.conflicts.resolution_data IS 'Данные разрешения конфликта в JSONB';
COMMENT ON COLUMN mvp_meta.consistency_checks.service_name IS 'Имя сервиса для проверки';
COMMENT ON COLUMN mvp_meta.consistency_checks.check_type IS 'Тип проверки консистентности';
COMMENT ON COLUMN mvp_meta.consistency_checks.status IS 'Статус проверки: pending, running, passed, failed, warning';
COMMENT ON COLUMN mvp_meta.consistency_checks.violations IS 'Массив нарушений консистентности в JSONB';
COMMENT ON COLUMN mvp_meta.consistency_checks.duration_ms IS 'Длительность проверки в миллисекундах';

