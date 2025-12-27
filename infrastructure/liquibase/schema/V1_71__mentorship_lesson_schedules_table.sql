-- Issue: #140890865
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create mentorship.lesson_schedules table for lesson scheduling system

-- Table: mentorship.lesson_schedules
CREATE TABLE IF NOT EXISTS mentorship.lesson_schedules
(
    id          UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    contract_id UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(id) ON DELETE CASCADE,
    lesson_date TIMESTAMP WITH TIME ZONE NOT NULL,
    lesson_time VARCHAR(50)              NOT NULL,
    location    TEXT                     NOT NULL,
    format      VARCHAR(50)              NOT NULL        DEFAULT 'online' CHECK (format IN ('online', 'offline', 'hybrid')),
    resources   JSONB,
    status      VARCHAR(50)              NOT NULL        DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'confirmed', 'completed', 'cancelled')),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for MMO mentorship queries)
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_contract_id ON mentorship.lesson_schedules(contract_id);
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_status ON mentorship.lesson_schedules(status);
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_lesson_date ON mentorship.lesson_schedules(lesson_date);
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_contract_date ON mentorship.lesson_schedules(contract_id, lesson_date);

-- Composite index for common queries (contract + status + date)
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_contract_status_date ON mentorship.lesson_schedules(contract_id, status, lesson_date);

-- Partial index for active schedules (most common query)
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_active ON mentorship.lesson_schedules(lesson_date, lesson_time)
    WHERE status IN ('scheduled', 'confirmed');

-- GIN index for JSONB resources search
CREATE INDEX IF NOT EXISTS idx_lesson_schedules_resources_gin ON mentorship.lesson_schedules USING GIN (resources);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_lesson_schedules_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_lesson_schedules_updated_at
    BEFORE UPDATE ON mentorship.lesson_schedules
    FOR EACH ROW
    EXECUTE FUNCTION update_lesson_schedules_updated_at();

-- Comments for API documentation
COMMENT ON TABLE mentorship.lesson_schedules IS 'Lesson schedules for NECPGAME mentorship system';
COMMENT ON COLUMN mentorship.lesson_schedules.id IS 'Unique lesson schedule identifier (UUID)';
COMMENT ON COLUMN mentorship.lesson_schedules.contract_id IS 'Reference to mentorship contract';
COMMENT ON COLUMN mentorship.lesson_schedules.lesson_date IS 'Date and time of the lesson';
COMMENT ON COLUMN mentorship.lesson_schedules.lesson_time IS 'Time slot for the lesson (e.g., "14:00-16:00")';
COMMENT ON COLUMN mentorship.lesson_schedules.location IS 'Lesson location (online URL or physical address)';
COMMENT ON COLUMN mentorship.lesson_schedules.format IS 'Lesson format: online, offline, hybrid';
COMMENT ON COLUMN mentorship.lesson_schedules.resources IS 'JSONB resources needed for lesson (materials, tools, links)';
COMMENT ON COLUMN mentorship.lesson_schedules.status IS 'Schedule status: scheduled, confirmed, completed, cancelled';
COMMENT ON COLUMN mentorship.lesson_schedules.created_at IS 'Schedule creation timestamp';
COMMENT ON COLUMN mentorship.lesson_schedules.updated_at IS 'Schedule last update timestamp';

-- BACKEND NOTE: Column order optimized for struct alignment (large → small types)
-- Expected memory per row: ~512 bytes (JSONB resources)
-- Hot queries: SELECT by contract_id, status, date_range
-- Cache strategy: Redis cache for upcoming lessons, TTL 30m
