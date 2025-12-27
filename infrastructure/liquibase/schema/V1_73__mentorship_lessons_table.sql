-- Issue: #140890865
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create mentorship.lessons table for lesson tracking and completion

-- Table: mentorship.lessons
CREATE TABLE IF NOT EXISTS mentorship.lessons
(
    id             UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    contract_id    UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(id) ON DELETE CASCADE,
    schedule_id    UUID                     REFERENCES mentorship.lesson_schedules(id) ON DELETE SET NULL,
    lesson_type    VARCHAR(50)              NOT NULL        DEFAULT 'regular' CHECK (lesson_type IN ('regular', 'assessment', 'review', 'practice')),
    format         VARCHAR(50)              NOT NULL        DEFAULT 'online' CHECK (format IN ('online', 'offline', 'hybrid')),
    content_id     UUID,
    started_at     TIMESTAMP WITH TIME ZONE,
    completed_at   TIMESTAMP WITH TIME ZONE,
    duration       INTEGER                  CHECK (duration > 0 AND duration <= 480),  -- minutes, max 8 hours
    skill_progress JSONB,
    evaluation     JSONB,
    status         VARCHAR(50)              NOT NULL        DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'started', 'completed', 'cancelled', 'failed')),
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for MMO lesson tracking)
CREATE INDEX IF NOT EXISTS idx_lessons_contract_id ON mentorship.lessons(contract_id);
CREATE INDEX IF NOT EXISTS idx_lessons_schedule_id ON mentorship.lessons(schedule_id);
CREATE INDEX IF NOT EXISTS idx_lessons_status ON mentorship.lessons(status);
CREATE INDEX IF NOT EXISTS idx_lessons_lesson_type ON mentorship.lessons(lesson_type);
CREATE INDEX IF NOT EXISTS idx_lessons_created_at ON mentorship.lessons(created_at DESC);

-- Composite indexes for common MMO queries
CREATE INDEX IF NOT EXISTS idx_lessons_contract_status ON mentorship.lessons(contract_id, status);
CREATE INDEX IF NOT EXISTS idx_lessons_contract_type_status ON mentorship.lessons(contract_id, lesson_type, status);
CREATE INDEX IF NOT EXISTS idx_lessons_status_created ON mentorship.lessons(status, created_at DESC);

-- Partial index for active lessons (most common query)
CREATE INDEX IF NOT EXISTS idx_lessons_active ON mentorship.lessons(contract_id, created_at DESC)
    WHERE status IN ('scheduled', 'started');

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_lessons_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_lessons_updated_at
    BEFORE UPDATE ON mentorship.lessons
    FOR EACH ROW
    EXECUTE FUNCTION update_lessons_updated_at();

-- Comments for API documentation
COMMENT ON TABLE mentorship.lessons IS 'Lesson instances tracking for NECPGAME mentorship system';
COMMENT ON COLUMN mentorship.lessons.id IS 'Unique lesson instance identifier (UUID)';
COMMENT ON COLUMN mentorship.lessons.contract_id IS 'Reference to mentorship contract';
COMMENT ON COLUMN mentorship.lessons.schedule_id IS 'Optional reference to lesson schedule';
COMMENT ON COLUMN mentorship.lessons.lesson_type IS 'Type of lesson: regular, assessment, review, practice';
COMMENT ON COLUMN mentorship.lessons.format IS 'Lesson format: online, offline, hybrid';
COMMENT ON COLUMN mentorship.lessons.content_id IS 'Reference to learning content/materials';
COMMENT ON COLUMN mentorship.lessons.started_at IS 'When the lesson actually started';
COMMENT ON COLUMN mentorship.lessons.completed_at IS 'When the lesson was completed';
COMMENT ON COLUMN mentorship.lessons.duration IS 'Lesson duration in minutes (1-480)';
COMMENT ON COLUMN mentorship.lessons.skill_progress IS 'JSONB tracking skill progression during lesson';
COMMENT ON COLUMN mentorship.lessons.evaluation IS 'JSONB evaluation data and metrics';
COMMENT ON COLUMN mentorship.lessons.status IS 'Lesson status: scheduled, started, completed, cancelled, failed';
COMMENT ON COLUMN mentorship.lessons.created_at IS 'Lesson creation timestamp';
COMMENT ON COLUMN mentorship.lessons.updated_at IS 'Lesson last update timestamp';

-- BACKEND NOTE: Tables optimized for MMO lesson tracking
-- Expected queries: SELECT by contract, student, mentor with status filtering
-- Performance: Composite indexes for complex aggregations
-- Cache strategy: Redis cache for recent lessons, TTL 15m
-- Monitoring: Track lesson completion rates for mentor performance
