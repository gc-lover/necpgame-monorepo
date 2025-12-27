-- Issue: #140890865
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create mentorship.mentor_reputations table for mentor discovery and ranking

-- Table: mentorship.mentor_reputations
CREATE TABLE IF NOT EXISTS mentorship.mentor_reputations
(
    mentor_id             UUID                     PRIMARY KEY,
    reputation_score      DECIMAL(5,2)             DEFAULT 0.0 CHECK (reputation_score >= 0.0 AND reputation_score <= 100.0),
    average_rating        DECIMAL(3,2)             DEFAULT 0.0 CHECK (average_rating >= 0.0 AND average_rating <= 5.0),
    total_reviews         INTEGER                  DEFAULT 0   CHECK (total_reviews >= 0),
    successful_graduates  INTEGER                  DEFAULT 0   CHECK (successful_graduates >= 0),
    total_students        INTEGER                  DEFAULT 0   CHECK (total_students >= 0),
    experience_years      INTEGER                  DEFAULT 0   CHECK (experience_years >= 0),
    specialization_score  DECIMAL(5,2)             DEFAULT 0.0 CHECK (specialization_score >= 0.0 AND specialization_score <= 100.0),
    availability_status   VARCHAR(20)              DEFAULT 'available' CHECK (availability_status IN ('available', 'busy', 'unavailable')),
    last_active           TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL   DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for MMO mentor discovery)
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_reputation_score ON mentorship.mentor_reputations(reputation_score DESC);
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_average_rating ON mentorship.mentor_reputations(average_rating DESC);
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_availability ON mentorship.mentor_reputations(availability_status);
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_last_active ON mentorship.mentor_reputations(last_active DESC);

-- Composite indexes for discovery queries
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_score_rating ON mentorship.mentor_reputations(reputation_score DESC, average_rating DESC);
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_available_score ON mentorship.mentor_reputations(availability_status, reputation_score DESC)
    WHERE availability_status = 'available';

-- Partial index for active mentors (most common query)
CREATE INDEX IF NOT EXISTS idx_mentor_reputations_active_high_score ON mentorship.mentor_reputations(reputation_score DESC, last_active DESC)
    WHERE availability_status = 'available' AND reputation_score >= 50;

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_mentor_reputations_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_mentor_reputations_updated_at
    BEFORE UPDATE ON mentorship.mentor_reputations
    FOR EACH ROW
    EXECUTE FUNCTION update_mentor_reputations_updated_at();

-- Comments for API documentation
COMMENT ON TABLE mentorship.mentor_reputations IS 'Mentor reputation scores and statistics for discovery and ranking in NECPGAME mentorship system';
COMMENT ON COLUMN mentorship.mentor_reputations.mentor_id IS 'Reference to mentor (primary key)';
COMMENT ON COLUMN mentorship.mentor_reputations.reputation_score IS 'Overall reputation score (0-100)';
COMMENT ON COLUMN mentorship.mentor_reputations.average_rating IS 'Average rating from student reviews (0-5)';
COMMENT ON COLUMN mentorship.mentor_reputations.total_reviews IS 'Total number of student reviews received';
COMMENT ON COLUMN mentorship.mentor_reputations.successful_graduates IS 'Number of students who successfully completed mentorship';
COMMENT ON COLUMN mentorship.mentor_reputations.total_students IS 'Total number of students mentored';
COMMENT ON COLUMN mentorship.mentor_reputations.experience_years IS 'Years of mentoring experience';
COMMENT ON COLUMN mentorship.mentor_reputations.specialization_score IS 'Score indicating specialization depth (0-100)';
COMMENT ON COLUMN mentorship.mentor_reputations.availability_status IS 'Current availability status: available, busy, unavailable';
COMMENT ON COLUMN mentorship.mentor_reputations.last_active IS 'Last activity timestamp for recency ranking';
COMMENT ON COLUMN mentorship.mentor_reputations.created_at IS 'Reputation record creation timestamp';
COMMENT ON COLUMN mentorship.mentor_reputations.updated_at IS 'Reputation record last update timestamp';

-- BACKEND NOTE: Table optimized for MMO mentor discovery
-- Expected queries: Filter by reputation, availability, sort by composite score
-- Performance: Partial indexes for common filters, composite indexes for ranking
-- Cache strategy: Redis cache for top mentors, TTL 5m, invalidate on reputation updates
-- Scaling: Partitioning by availability_status for large mentor pools
