-- Issue: #140890865
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create mentorship reputation calculation tables for mentor performance metrics

-- Table: mentorship.student_reviews
CREATE TABLE IF NOT EXISTS mentorship.student_reviews
(
    id           UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    mentor_id    UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(mentor_id),
    student_id   UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(mentee_id),
    contract_id  UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(id),
    rating       DECIMAL(3,2)             CHECK (rating >= 1.0 AND rating <= 5.0),
    review_text  TEXT,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Table: mentorship.completed_lessons
CREATE TABLE IF NOT EXISTS mentorship.completed_lessons
(
    id                 UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    contract_id        UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(id) ON DELETE CASCADE,
    mentor_id          UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(mentor_id),
    student_id         UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(mentee_id),
    lesson_schedule_id UUID                     REFERENCES mentorship.lesson_schedules(id) ON DELETE SET NULL,
    completion_status  VARCHAR(50)              NOT NULL        DEFAULT 'completed' CHECK (completion_status IN ('completed', 'successful', 'failed', 'incomplete')),
    completion_score   DECIMAL(5,2)             CHECK (completion_score >= 0 AND completion_score <= 100),
    feedback_text      TEXT,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Table: mentorship.academy_ratings
CREATE TABLE IF NOT EXISTS mentorship.academy_ratings
(
    id         UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    mentor_id  UUID                     NOT NULL        REFERENCES mentorship.mentorship_contracts(mentor_id),
    academy_id UUID,  -- Optional reference to academy system
    rating     DECIMAL(3,2)             NOT NULL        CHECK (rating >= 1.0 AND rating <= 5.0),
    criteria   VARCHAR(50)              NOT NULL        CHECK (criteria IN ('content_quality', 'teaching_method', 'communication', 'academy_rating', 'overall_performance')),
    rated_by   UUID                     NOT NULL,  -- User who gave the rating
    created_at TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for MMO reputation queries)
CREATE INDEX IF NOT EXISTS idx_student_reviews_mentor_id ON mentorship.student_reviews(mentor_id);
CREATE INDEX IF NOT EXISTS idx_student_reviews_rating ON mentorship.student_reviews(mentor_id, rating);
CREATE INDEX IF NOT EXISTS idx_student_reviews_created_at ON mentorship.student_reviews(mentor_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_completed_lessons_mentor_id ON mentorship.completed_lessons(mentor_id);
CREATE INDEX IF NOT EXISTS idx_completed_lessons_contract_id ON mentorship.completed_lessons(contract_id);
CREATE INDEX IF NOT EXISTS idx_completed_lessons_status ON mentorship.completed_lessons(mentor_id, completion_status);
CREATE INDEX IF NOT EXISTS idx_completed_lessons_created_at ON mentorship.completed_lessons(mentor_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_academy_ratings_mentor_id ON mentorship.academy_ratings(mentor_id);
CREATE INDEX IF NOT EXISTS idx_academy_ratings_criteria ON mentorship.academy_ratings(mentor_id, criteria);
CREATE INDEX IF NOT EXISTS idx_academy_ratings_created_at ON mentorship.academy_ratings(mentor_id, created_at DESC);

-- Composite indexes for reputation calculation queries
CREATE INDEX IF NOT EXISTS idx_student_reviews_mentor_rating ON mentorship.student_reviews(mentor_id, rating, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_completed_lessons_mentor_status ON mentorship.completed_lessons(mentor_id, completion_status, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_academy_ratings_mentor_criteria_rating ON mentorship.academy_ratings(mentor_id, criteria, rating);

-- Functions to update updated_at timestamps
CREATE OR REPLACE FUNCTION update_student_reviews_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_completed_lessons_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_academy_ratings_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers to automatically update updated_at
CREATE TRIGGER trigger_student_reviews_updated_at
    BEFORE UPDATE ON mentorship.student_reviews
    FOR EACH ROW
    EXECUTE FUNCTION update_student_reviews_updated_at();

CREATE TRIGGER trigger_completed_lessons_updated_at
    BEFORE UPDATE ON mentorship.completed_lessons
    FOR EACH ROW
    EXECUTE FUNCTION update_completed_lessons_updated_at();

CREATE TRIGGER trigger_academy_ratings_updated_at
    BEFORE UPDATE ON mentorship.academy_ratings
    FOR EACH ROW
    EXECUTE FUNCTION update_academy_ratings_updated_at();

-- Comments for API documentation
COMMENT ON TABLE mentorship.student_reviews IS 'Student reviews and ratings for mentors in NECPGAME mentorship system';
COMMENT ON COLUMN mentorship.student_reviews.mentor_id IS 'Reference to the mentor being reviewed';
COMMENT ON COLUMN mentorship.student_reviews.student_id IS 'Reference to the student giving the review';
COMMENT ON COLUMN mentorship.student_reviews.contract_id IS 'Reference to the mentorship contract';
COMMENT ON COLUMN mentorship.student_reviews.rating IS 'Rating from 1.0 to 5.0 given by student';
COMMENT ON COLUMN mentorship.student_reviews.review_text IS 'Optional detailed review text';

COMMENT ON TABLE mentorship.completed_lessons IS 'Completed lessons tracking for mentorship performance metrics';
COMMENT ON COLUMN mentorship.completed_lessons.contract_id IS 'Reference to mentorship contract';
COMMENT ON COLUMN mentorship.completed_lessons.completion_status IS 'Lesson completion status: completed, successful, failed, incomplete';
COMMENT ON COLUMN mentorship.completed_lessons.completion_score IS 'Numerical score for lesson completion (0-100)';
COMMENT ON COLUMN mentorship.completed_lessons.feedback_text IS 'Optional feedback from student or mentor';

COMMENT ON TABLE mentorship.academy_ratings IS 'Academy and content quality ratings for mentors';
COMMENT ON COLUMN mentorship.academy_ratings.mentor_id IS 'Reference to the rated mentor';
COMMENT ON COLUMN mentorship.academy_ratings.criteria IS 'Rating criteria: content_quality, teaching_method, communication, academy_rating, overall_performance';
COMMENT ON COLUMN mentorship.academy_ratings.rating IS 'Rating from 1.0 to 5.0';
COMMENT ON COLUMN mentorship.academy_ratings.rated_by IS 'User ID who gave this rating';

-- BACKEND NOTE: Tables optimized for MMO reputation calculations
-- Expected queries: Complex aggregations with CTEs, frequent mentor reputation lookups
-- Cache strategy: Redis cache for reputation scores, TTL 30m, invalidate on new reviews/ratings
-- Performance: Single query with CTE aggregation for real-time reputation calculation
