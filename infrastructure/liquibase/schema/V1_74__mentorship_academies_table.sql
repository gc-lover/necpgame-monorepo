-- Issue: #140890865
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create mentorship.academies table for educational institutions and programs

-- Table: mentorship.academies
CREATE TABLE IF NOT EXISTS mentorship.academies
(
    id               UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    name             VARCHAR(200)             NOT NULL,
    description      TEXT                     NOT NULL,
    academy_type     VARCHAR(50)              NOT NULL        DEFAULT 'general' CHECK (academy_type IN ('general', 'specialized', 'corporate', 'community', 'premium')),
    founder_id       UUID,
    location         TEXT                     NOT NULL,
    programs_count   INTEGER                  DEFAULT 0       CHECK (programs_count >= 0),
    total_students   INTEGER                  DEFAULT 0       CHECK (total_students >= 0),
    reputation_score DECIMAL(3,2)             DEFAULT 0.0     CHECK (reputation_score >= 0.0 AND reputation_score <= 5.0),
    tuition_fee      DECIMAL(10,2)            CHECK (tuition_fee >= 0),
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for MMO academy queries)
CREATE INDEX IF NOT EXISTS idx_academies_academy_type ON mentorship.academies(academy_type);
CREATE INDEX IF NOT EXISTS idx_academies_location ON mentorship.academies(location);
CREATE INDEX IF NOT EXISTS idx_academies_reputation_score ON mentorship.academies(reputation_score DESC);
CREATE INDEX IF NOT EXISTS idx_academies_created_at ON mentorship.academies(created_at DESC);

-- Composite indexes for common MMO queries
CREATE INDEX IF NOT EXISTS idx_academies_type_reputation ON mentorship.academies(academy_type, reputation_score DESC);
CREATE INDEX IF NOT EXISTS idx_academies_type_location ON mentorship.academies(academy_type, location);

-- Partial indexes for premium academies (most common filter)
CREATE INDEX IF NOT EXISTS idx_academies_premium ON mentorship.academies(reputation_score DESC, total_students DESC)
    WHERE academy_type = 'premium';

-- Full-text search index for academy names and descriptions
CREATE INDEX IF NOT EXISTS idx_academies_search ON mentorship.academies USING GIN (to_tsvector('english', name || ' ' || description));

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_academies_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_academies_updated_at
    BEFORE UPDATE ON mentorship.academies
    FOR EACH ROW
    EXECUTE FUNCTION update_academies_updated_at();

-- Comments for API documentation
COMMENT ON TABLE mentorship.academies IS 'Educational academies and institutions in NECPGAME mentorship system';
COMMENT ON COLUMN mentorship.academies.id IS 'Unique academy identifier (UUID)';
COMMENT ON COLUMN mentorship.academies.name IS 'Academy name (max 200 chars)';
COMMENT ON COLUMN mentorship.academies.description IS 'Detailed academy description';
COMMENT ON COLUMN mentorship.academies.academy_type IS 'Academy type: general, specialized, corporate, community, premium';
COMMENT ON COLUMN mentorship.academies.founder_id IS 'Reference to academy founder (optional)';
COMMENT ON COLUMN mentorship.academies.location IS 'Academy location (physical or virtual)';
COMMENT ON COLUMN mentorship.academies.programs_count IS 'Number of educational programs offered';
COMMENT ON COLUMN mentorship.academies.total_students IS 'Total number of enrolled students';
COMMENT ON COLUMN mentorship.academies.reputation_score IS 'Academy reputation score (0.0-5.0)';
COMMENT ON COLUMN mentorship.academies.tuition_fee IS 'Monthly/yearly tuition fee';
COMMENT ON COLUMN mentorship.academies.created_at IS 'Academy creation timestamp';
COMMENT ON COLUMN mentorship.academies.updated_at IS 'Academy last update timestamp';

-- BACKEND NOTE: Table optimized for MMO academy management
-- Expected queries: Filter by type/location, sort by reputation, search by name
-- Performance: Full-text search, composite indexes for common filters
-- Cache strategy: Redis cache for academy listings, TTL 1h
-- Scaling: Partitioning by academy_type for large datasets
