--liquibase formatted sql

--changeset necpgame:V1_28_player_feedback_tables
--comment: Create tables for player feedback system

CREATE SCHEMA IF NOT EXISTS feedback;

CREATE TABLE IF NOT EXISTS feedback.player_feedback (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(20),
    game_context JSONB,
    screenshots TEXT[],
    github_issue_number INTEGER,
    github_issue_url TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    votes_count INTEGER NOT NULL DEFAULT 0,
    merged_into UUID,
    moderation_status VARCHAR(50),
    moderation_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES mvp_core.character(id),
    CONSTRAINT fk_merged_into FOREIGN KEY (merged_into) REFERENCES feedback.player_feedback(id),
    CONSTRAINT chk_type CHECK (type IN ('feature_request', 'bug_report', 'wishlist', 'feedback')),
    CONSTRAINT chk_category CHECK (category IN ('gameplay', 'balance', 'content', 'technical', 'lore', 'ui_ux', 'other')),
    CONSTRAINT chk_priority CHECK (priority IN ('low', 'medium', 'high', 'critical') OR priority IS NULL),
    CONSTRAINT chk_status CHECK (status IN ('pending', 'in_review', 'approved', 'rejected', 'merged', 'closed')),
    CONSTRAINT chk_moderation_status CHECK (moderation_status IN ('pending', 'approved', 'rejected') OR moderation_status IS NULL)
);

CREATE INDEX IF NOT EXISTS idx_player_feedback_player_id ON feedback.player_feedback(player_id);
CREATE INDEX IF NOT EXISTS idx_player_feedback_status ON feedback.player_feedback(status);
CREATE INDEX IF NOT EXISTS idx_player_feedback_type ON feedback.player_feedback(type);
CREATE INDEX IF NOT EXISTS idx_player_feedback_category ON feedback.player_feedback(category);
CREATE INDEX IF NOT EXISTS idx_player_feedback_created_at ON feedback.player_feedback(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_feedback_votes_count ON feedback.player_feedback(votes_count DESC);

CREATE TABLE IF NOT EXISTS feedback.player_feedback_votes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    feedback_id UUID NOT NULL,
    player_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback FOREIGN KEY (feedback_id) REFERENCES feedback.player_feedback(id) ON DELETE CASCADE,
    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES mvp_core.character(id),
    CONSTRAINT uq_feedback_vote UNIQUE (feedback_id, player_id)
);

CREATE INDEX IF NOT EXISTS idx_player_feedback_votes_feedback_id ON feedback.player_feedback_votes(feedback_id);
CREATE INDEX IF NOT EXISTS idx_player_feedback_votes_player_id ON feedback.player_feedback_votes(player_id);

CREATE OR REPLACE FUNCTION update_feedback_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER player_feedback_updated_at
    BEFORE UPDATE ON feedback.player_feedback
    FOR EACH ROW
    EXECUTE FUNCTION update_feedback_updated_at();

--rollback DROP TRIGGER IF EXISTS player_feedback_updated_at ON feedback.player_feedback;
--rollback DROP FUNCTION IF EXISTS update_feedback_updated_at();
--rollback DROP TABLE IF EXISTS feedback.player_feedback_votes;
--rollback DROP TABLE IF EXISTS feedback.player_feedback;
--rollback DROP SCHEMA IF EXISTS feedback;





