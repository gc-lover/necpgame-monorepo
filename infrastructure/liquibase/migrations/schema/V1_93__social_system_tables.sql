-- Social System Tables Migration
-- Enterprise-grade schema for MMOFPS RPG social interactions

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Friendships table
-- Manages player-to-player friendships with status tracking
CREATE TABLE IF NOT EXISTS friendships (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    requester_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    addressee_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'blocked')),
    requested_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP WITH TIME ZONE,
    blocked_at TIMESTAMP WITH TIME ZONE,

    -- Unique constraint to prevent duplicate friendship requests
    UNIQUE(requester_id, addressee_id),

    -- Check constraint to prevent self-friending
    CHECK (requester_id != addressee_id),

    -- Constraints
    CONSTRAINT fk_friendships_requester FOREIGN KEY (requester_id) REFERENCES players(id),
    CONSTRAINT fk_friendships_addressee FOREIGN KEY (addressee_id) REFERENCES players(id)
);

-- Player relationships table
-- Advanced relationship system with reputation and trust levels
CREATE TABLE IF NOT EXISTS player_relationships (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    target_player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    relationship_type VARCHAR(50) NOT NULL, -- friend, rival, mentor, mentee, ally, enemy, etc.
    trust_level INTEGER NOT NULL DEFAULT 0 CHECK (trust_level >= -100 AND trust_level <= 100),
    reputation_score INTEGER NOT NULL DEFAULT 0,
    interaction_count INTEGER NOT NULL DEFAULT 0 CHECK (interaction_count >= 0),
    last_interaction TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint per relationship type between players
    UNIQUE(player_id, target_player_id, relationship_type),

    -- Check constraint to prevent self-relationships
    CHECK (player_id != target_player_id),

    -- Constraints
    CONSTRAINT fk_relationships_player FOREIGN KEY (player_id) REFERENCES players(id),
    CONSTRAINT fk_relationships_target FOREIGN KEY (target_player_id) REFERENCES players(id)
);

-- Social circles table
-- Groups of players with shared interests or activities
CREATE TABLE IF NOT EXISTS social_circles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    creator_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    circle_type VARCHAR(50) NOT NULL DEFAULT 'interest' CHECK (circle_type IN ('interest', 'activity', 'location', 'guild_related')),
    max_members INTEGER DEFAULT 50 CHECK (max_members > 0),
    current_members INTEGER NOT NULL DEFAULT 1 CHECK (current_members >= 0),
    is_private BOOLEAN NOT NULL DEFAULT false,
    join_code VARCHAR(20) UNIQUE, -- For private circles
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_social_circles_creator FOREIGN KEY (creator_id) REFERENCES players(id)
);

-- Social circle members table
-- Membership in social circles
CREATE TABLE IF NOT EXISTS social_circle_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    circle_id UUID NOT NULL REFERENCES social_circles(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL DEFAULT 'member' CHECK (role IN ('creator', 'moderator', 'member')),
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,

    -- Unique constraint per circle
    UNIQUE(circle_id, player_id),

    -- Constraints
    CONSTRAINT fk_circle_members_circle FOREIGN KEY (circle_id) REFERENCES social_circles(id),
    CONSTRAINT fk_circle_members_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Social events table
-- Events and gatherings organized by players
CREATE TABLE IF NOT EXISTS social_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    organizer_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL DEFAULT 'casual' CHECK (event_type IN ('casual', 'competitive', 'educational', 'celebration')),
    location_type VARCHAR(20) NOT NULL DEFAULT 'virtual' CHECK (location_type IN ('virtual', 'physical', 'mixed')),
    location_details JSONB, -- Virtual coordinates, physical address, etc.
    max_participants INTEGER CHECK (max_participants > 0),
    current_participants INTEGER NOT NULL DEFAULT 1 CHECK (current_participants >= 0),
    starts_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ends_at TIMESTAMP WITH TIME ZONE,
    is_public BOOLEAN NOT NULL DEFAULT true,
    is_cancelled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_social_events_organizer FOREIGN KEY (organizer_id) REFERENCES players(id),
    CHECK (ends_at IS NULL OR ends_at > starts_at)
);

-- Social event participants table
-- Participants in social events
CREATE TABLE IF NOT EXISTS social_event_participants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_id UUID NOT NULL REFERENCES social_events(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'registered' CHECK (status IN ('registered', 'confirmed', 'attended', 'no_show', 'cancelled')),
    registered_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP WITH TIME ZONE,

    -- Unique constraint per event
    UNIQUE(event_id, player_id),

    -- Constraints
    CONSTRAINT fk_event_participants_event FOREIGN KEY (event_id) REFERENCES social_events(id),
    CONSTRAINT fk_event_participants_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Social messages table
-- Private messaging between players
CREATE TABLE IF NOT EXISTS social_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    recipient_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    message_type VARCHAR(20) NOT NULL DEFAULT 'text' CHECK (message_type IN ('text', 'system', 'invitation')),
    content TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    read_at TIMESTAMP WITH TIME ZONE,
    sent_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_social_messages_sender FOREIGN KEY (sender_id) REFERENCES players(id),
    CONSTRAINT fk_social_messages_recipient FOREIGN KEY (recipient_id) REFERENCES players(id)
);

-- Reputation system table
-- Player reputation scores and history
CREATE TABLE IF NOT EXISTS player_reputation (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    reputation_type VARCHAR(50) NOT NULL, -- combat, trading, social, leadership, etc.
    reputation_score INTEGER NOT NULL DEFAULT 0,
    reputation_level VARCHAR(20) NOT NULL DEFAULT 'neutral' CHECK (reputation_level IN ('hated', 'disliked', 'neutral', 'liked', 'respected', 'revered')),
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint per reputation type
    UNIQUE(player_id, reputation_type),

    -- Constraints
    CONSTRAINT fk_player_reputation_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Social achievements table
-- Achievements related to social interactions
CREATE TABLE IF NOT EXISTS social_achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    achievement_type VARCHAR(50) NOT NULL, -- first_friend, social_butterfly, event_organizer, etc.
    achievement_data JSONB,
    unlocked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Index for queries
    INDEX idx_social_achievements_player_type (player_id, achievement_type),

    -- Constraints
    CONSTRAINT fk_social_achievements_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Social activity log table
-- Audit trail for social interactions
CREATE TABLE IF NOT EXISTS social_activity_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    activity_type VARCHAR(50) NOT NULL, -- friend_request, message_sent, event_created, etc.
    activity_data JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Index for activity queries
    INDEX idx_social_activity_player_time (player_id, created_at DESC),

    -- Constraints
    CONSTRAINT fk_social_activity_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Indexes for performance optimization

-- Friendships indexes
CREATE INDEX IF NOT EXISTS idx_friendships_requester ON friendships(requester_id);
CREATE INDEX IF NOT EXISTS idx_friendships_addressee ON friendships(addressee_id);
CREATE INDEX IF NOT EXISTS idx_friendships_status ON friendships(status) WHERE status = 'pending';

-- Player relationships indexes
CREATE INDEX IF NOT EXISTS idx_player_relationships_player ON player_relationships(player_id);
CREATE INDEX IF NOT EXISTS idx_player_relationships_target ON player_relationships(target_player_id);
CREATE INDEX IF NOT EXISTS idx_player_relationships_type ON player_relationships(relationship_type);
CREATE INDEX IF NOT EXISTS idx_player_relationships_trust ON player_relationships(trust_level);

-- Social circles indexes
CREATE INDEX IF NOT EXISTS idx_social_circles_creator ON social_circles(creator_id);
CREATE INDEX IF NOT EXISTS idx_social_circles_type ON social_circles(circle_type);
CREATE INDEX IF NOT EXISTS idx_social_circles_private ON social_circles(is_private) WHERE is_private = false;

-- Social circle members indexes
CREATE INDEX IF NOT EXISTS idx_social_circle_members_circle ON social_circle_members(circle_id);
CREATE INDEX IF NOT EXISTS idx_social_circle_members_player ON social_circle_members(player_id);

-- Social events indexes
CREATE INDEX IF NOT EXISTS idx_social_events_organizer ON social_events(organizer_id);
CREATE INDEX IF NOT EXISTS idx_social_events_type ON social_events(event_type);
CREATE INDEX IF NOT EXISTS idx_social_events_starts ON social_events(starts_at);
CREATE INDEX IF NOT EXISTS idx_social_events_public ON social_events(is_public) WHERE is_public = true;

-- Social event participants indexes
CREATE INDEX IF NOT EXISTS idx_social_event_participants_event ON social_event_participants(event_id);
CREATE INDEX IF NOT EXISTS idx_social_event_participants_player ON social_event_participants(player_id);

-- Social messages indexes
CREATE INDEX IF NOT EXISTS idx_social_messages_sender ON social_messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_social_messages_recipient ON social_messages(recipient_id);
CREATE INDEX IF NOT EXISTS idx_social_messages_sent ON social_messages(sent_at DESC);
CREATE INDEX IF NOT EXISTS idx_social_messages_unread ON social_messages(recipient_id, is_read) WHERE is_read = false;

-- Player reputation indexes
CREATE INDEX IF NOT EXISTS idx_player_reputation_player ON player_reputation(player_id);
CREATE INDEX IF NOT EXISTS idx_player_reputation_level ON player_reputation(reputation_level);

-- Triggers for automatic timestamp updates

-- Friendships timestamps (no trigger needed as timestamps are set at specific events)

-- Player relationships updated_at trigger
CREATE OR REPLACE FUNCTION update_player_relationships_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_player_relationships_updated_at
    BEFORE UPDATE ON player_relationships
    FOR EACH ROW
    EXECUTE FUNCTION update_player_relationships_updated_at();

-- Social circles updated_at trigger
CREATE OR REPLACE FUNCTION update_social_circles_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_social_circles_updated_at
    BEFORE UPDATE ON social_circles
    FOR EACH ROW
    EXECUTE FUNCTION update_social_circles_updated_at();

-- Social events updated_at trigger
CREATE OR REPLACE FUNCTION update_social_events_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_social_events_updated_at
    BEFORE UPDATE ON social_events
    FOR EACH ROW
    EXECUTE FUNCTION update_social_events_updated_at();

-- Function to automatically update social circle member count
CREATE OR REPLACE FUNCTION update_social_circle_member_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE social_circles SET current_members = current_members + 1 WHERE id = NEW.circle_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE social_circles SET current_members = current_members - 1 WHERE id = OLD.circle_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_social_circle_member_count
    AFTER INSERT OR DELETE ON social_circle_members
    FOR EACH ROW
    EXECUTE FUNCTION update_social_circle_member_count();

-- Function to automatically update social event participant count
CREATE OR REPLACE FUNCTION update_social_event_participant_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE social_events SET current_participants = current_participants + 1 WHERE id = NEW.event_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE social_events SET current_participants = current_participants - 1 WHERE id = OLD.event_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_social_event_participant_count
    AFTER INSERT OR DELETE ON social_event_participants
    FOR EACH ROW
    EXECUTE FUNCTION update_social_event_participant_count();

-- Function to calculate reputation level from score
CREATE OR REPLACE FUNCTION calculate_reputation_level(score INTEGER)
RETURNS VARCHAR(20) AS $$
BEGIN
    RETURN CASE
        WHEN score <= -100 THEN 'hated'
        WHEN score <= -50 THEN 'disliked'
        WHEN score < 50 THEN 'neutral'
        WHEN score < 100 THEN 'liked'
        WHEN score < 200 THEN 'respected'
        ELSE 'revered'
    END;
END;
$$ LANGUAGE plpgsql;

-- Function to update reputation level automatically
CREATE OR REPLACE FUNCTION update_reputation_level()
RETURNS TRIGGER AS $$
BEGIN
    NEW.reputation_level := calculate_reputation_level(NEW.reputation_score);
    NEW.last_updated := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_reputation_level
    BEFORE INSERT OR UPDATE ON player_reputation
    FOR EACH ROW
    EXECUTE FUNCTION update_reputation_level();

-- Function to log social activity
CREATE OR REPLACE FUNCTION log_social_activity(
    p_player_id UUID,
    p_activity_type VARCHAR(50),
    p_activity_data JSONB DEFAULT NULL,
    p_ip_address INET DEFAULT NULL,
    p_user_agent TEXT DEFAULT NULL
)
RETURNS VOID AS $$
BEGIN
    INSERT INTO social_activity_log (player_id, activity_type, activity_data, ip_address, user_agent)
    VALUES (p_player_id, p_activity_type, p_activity_data, p_ip_address, p_user_agent);
END;
$$ LANGUAGE plpgsql;

-- Function to get player social summary
CREATE OR REPLACE FUNCTION get_player_social_summary(player_uuid UUID)
RETURNS TABLE (
    friend_count BIGINT,
    pending_requests BIGINT,
    relationship_count BIGINT,
    circle_count BIGINT,
    event_count BIGINT,
    unread_messages BIGINT,
    reputation_score BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(DISTINCT f.requester_id + f.addressee_id) FILTER (WHERE f.status = 'accepted')::BIGINT as friend_count,
        COUNT(*) FILTER (WHERE f.status = 'pending' AND f.addressee_id = player_uuid)::BIGINT as pending_requests,
        COUNT(DISTINCT pr.id)::BIGINT as relationship_count,
        COUNT(DISTINCT scm.id)::BIGINT as circle_count,
        COUNT(DISTINCT sep.id)::BIGINT as event_count,
        COUNT(DISTINCT sm.id) FILTER (WHERE sm.is_read = false AND sm.recipient_id = player_uuid)::BIGINT as unread_messages,
        COALESCE(SUM(pr_rep.reputation_score), 0)::BIGINT as reputation_score
    FROM players p
    LEFT JOIN friendships f ON (f.requester_id = p.id OR f.addressee_id = p.id) AND f.status = 'accepted'
    LEFT JOIN player_relationships pr ON pr.player_id = p.id
    LEFT JOIN social_circle_members scm ON scm.player_id = p.id AND scm.is_active = true
    LEFT JOIN social_event_participants sep ON sep.player_id = p.id AND sep.status IN ('registered', 'confirmed')
    LEFT JOIN social_messages sm ON sm.recipient_id = p.id
    LEFT JOIN player_reputation pr_rep ON pr_rep.player_id = p.id
    WHERE p.id = player_uuid;
END;
$$ LANGUAGE plpgsql;

-- Comments for documentation
COMMENT ON TABLE friendships IS 'Player friendship system with request/accept/block states';
COMMENT ON TABLE player_relationships IS 'Advanced relationship system with trust and reputation';
COMMENT ON TABLE social_circles IS 'Player-created groups and communities';
COMMENT ON TABLE social_circle_members IS 'Membership in social circles';
COMMENT ON TABLE social_events IS 'Player-organized events and gatherings';
COMMENT ON TABLE social_event_participants IS 'Event participation tracking';
COMMENT ON TABLE social_messages IS 'Private messaging system';
COMMENT ON TABLE player_reputation IS 'Reputation scores across different categories';
COMMENT ON TABLE social_achievements IS 'Social interaction achievements';
COMMENT ON TABLE social_activity_log IS 'Audit trail for social activities';

COMMENT ON FUNCTION calculate_reputation_level(INTEGER) IS 'Calculates reputation level from score';
COMMENT ON FUNCTION get_player_social_summary(UUID) IS 'Returns comprehensive social statistics for a player';
COMMENT ON FUNCTION log_social_activity(UUID, VARCHAR, JSONB, INET, TEXT) IS 'Logs social activities for audit';
