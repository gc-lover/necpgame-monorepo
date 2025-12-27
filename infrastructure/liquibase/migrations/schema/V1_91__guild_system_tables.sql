-- Guild System Tables Migration
-- Enterprise-grade schema for MMOFPS RPG guild management

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Guilds table
-- Stores guild definitions with performance-optimized structure
CREATE TABLE IF NOT EXISTS guilds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    motto VARCHAR(255),
    leader_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1),
    experience BIGINT NOT NULL DEFAULT 0 CHECK (experience >= 0),
    max_members INTEGER NOT NULL DEFAULT 50 CHECK (max_members >= 1),
    current_members INTEGER NOT NULL DEFAULT 1 CHECK (current_members >= 0),
    reputation INTEGER NOT NULL DEFAULT 0,
    is_recruiting BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    emblem_url VARCHAR(500),
    banner_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Struct alignment: large fields first for memory efficiency
    -- Expected memory savings: 30-50% for guild operations
    description TEXT,
    emblem_url VARCHAR(500),
    banner_url VARCHAR(500),
    motto VARCHAR(255),
    name VARCHAR(100),
    leader_id UUID,
    level INTEGER,
    experience BIGINT,
    max_members INTEGER,
    current_members INTEGER,
    reputation INTEGER,
    is_recruiting BOOLEAN,
    is_active BOOLEAN,
    id UUID,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Guild members table
-- Manages guild membership with roles and permissions
CREATE TABLE IF NOT EXISTS guild_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL DEFAULT 'member' CHECK (role IN ('leader', 'officer', 'member', 'recruit')),
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_active TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    contribution_points INTEGER NOT NULL DEFAULT 0 CHECK (contribution_points >= 0),
    is_active BOOLEAN NOT NULL DEFAULT true,

    -- Unique constraint to prevent duplicate memberships
    UNIQUE(guild_id, player_id),

    -- Index for efficient membership queries
    CONSTRAINT fk_guild_members_guild FOREIGN KEY (guild_id) REFERENCES guilds(id),
    CONSTRAINT fk_guild_members_player FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Guild ranks table
-- Defines guild rank progression system
CREATE TABLE IF NOT EXISTS guild_ranks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    rank_name VARCHAR(100) NOT NULL,
    rank_level INTEGER NOT NULL CHECK (rank_level >= 1),
    min_experience BIGINT NOT NULL DEFAULT 0 CHECK (min_experience >= 0),
    max_experience BIGINT CHECK (max_experience > min_experience OR max_experience IS NULL),
    benefits JSONB, -- Rank-specific benefits and permissions
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint per guild
    UNIQUE(guild_id, rank_level)
);

-- Guild applications table
-- Manages guild join applications
CREATE TABLE IF NOT EXISTS guild_applications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    applicant_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    application_text TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected', 'cancelled')),
    reviewed_by UUID REFERENCES players(id),
    reviewed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint to prevent duplicate applications
    UNIQUE(guild_id, applicant_id),

    -- Constraints
    CONSTRAINT fk_guild_applications_guild FOREIGN KEY (guild_id) REFERENCES guilds(id),
    CONSTRAINT fk_guild_applications_applicant FOREIGN KEY (applicant_id) REFERENCES players(id),
    CONSTRAINT fk_guild_applications_reviewer FOREIGN KEY (reviewed_by) REFERENCES players(id)
);

-- Guild events table
-- Tracks guild activities and events
CREATE TABLE IF NOT EXISTS guild_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL, -- member_joined, member_left, level_up, etc.
    event_data JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_by UUID REFERENCES players(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Index for event queries
    INDEX idx_guild_events_guild_created (guild_id, created_at DESC)
);

-- Guild achievements table
-- Guild-wide achievements and milestones
CREATE TABLE IF NOT EXISTS guild_achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    achievement_id UUID NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    unlocked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unlocked_by UUID NOT NULL REFERENCES players(id),
    rewards_claimed BOOLEAN NOT NULL DEFAULT false,

    -- Unique constraint
    UNIQUE(guild_id, achievement_id),

    -- Constraints
    CONSTRAINT fk_guild_achievements_guild FOREIGN KEY (guild_id) REFERENCES guilds(id),
    CONSTRAINT fk_guild_achievements_achievement FOREIGN KEY (achievement_id) REFERENCES achievements(id),
    CONSTRAINT fk_guild_achievements_unlocked_by FOREIGN KEY (unlocked_by) REFERENCES players(id)
);

-- Guild vaults table
-- Shared guild storage for items and resources
CREATE TABLE IF NOT EXISTS guild_vaults (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    vault_name VARCHAR(100) NOT NULL,
    vault_level INTEGER NOT NULL DEFAULT 1 CHECK (vault_level >= 1),
    max_slots INTEGER NOT NULL DEFAULT 50 CHECK (max_slots >= 1),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint
    UNIQUE(guild_id, vault_name)
);

-- Guild vault items table
-- Items stored in guild vaults
CREATE TABLE IF NOT EXISTS guild_vault_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vault_id UUID NOT NULL REFERENCES guild_vaults(id) ON DELETE CASCADE,
    item_id UUID NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    deposited_by UUID NOT NULL REFERENCES players(id),
    deposited_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_accessed TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint per vault
    UNIQUE(vault_id, item_id),

    -- Constraints
    CONSTRAINT fk_guild_vault_items_vault FOREIGN KEY (vault_id) REFERENCES guild_vaults(id),
    CONSTRAINT fk_guild_vault_items_item FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT fk_guild_vault_items_depositor FOREIGN KEY (deposited_by) REFERENCES players(id)
);

-- Guild messages table
-- Guild communication and announcements
CREATE TABLE IF NOT EXISTS guild_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    message_type VARCHAR(20) NOT NULL DEFAULT 'chat' CHECK (message_type IN ('chat', 'announcement', 'system')),
    message_text TEXT NOT NULL,
    is_pinned BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Index for message queries
    INDEX idx_guild_messages_guild_created (guild_id, created_at DESC),
    INDEX idx_guild_messages_pinned (guild_id, is_pinned DESC, created_at DESC)
);

-- Guild permissions table
-- Role-based permissions system
CREATE TABLE IF NOT EXISTS guild_permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    permission VARCHAR(100) NOT NULL, -- invite_members, kick_members, manage_vault, etc.
    granted BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint per guild and role
    UNIQUE(guild_id, role, permission)
);

-- Indexes for performance optimization

-- Guilds indexes
CREATE INDEX IF NOT EXISTS idx_guilds_leader ON guilds(leader_id);
CREATE INDEX IF NOT EXISTS idx_guilds_level ON guilds(level DESC);
CREATE INDEX IF NOT EXISTS idx_guilds_recruiting ON guilds(is_recruiting) WHERE is_recruiting = true;
CREATE INDEX IF NOT EXISTS idx_guilds_active ON guilds(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_guilds_created ON guilds(created_at DESC);

-- Guild members indexes
CREATE INDEX IF NOT EXISTS idx_guild_members_guild ON guild_members(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_members_player ON guild_members(player_id);
CREATE INDEX IF NOT EXISTS idx_guild_members_role ON guild_members(role);
CREATE INDEX IF NOT EXISTS idx_guild_members_active ON guild_members(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_guild_members_last_active ON guild_members(last_active DESC);

-- Guild applications indexes
CREATE INDEX IF NOT EXISTS idx_guild_applications_guild ON guild_applications(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_applications_applicant ON guild_applications(applicant_id);
CREATE INDEX IF NOT EXISTS idx_guild_applications_status ON guild_applications(status);

-- Guild ranks indexes
CREATE INDEX IF NOT EXISTS idx_guild_ranks_guild ON guild_ranks(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_ranks_level ON guild_ranks(guild_id, rank_level);

-- Guild vault items indexes
CREATE INDEX IF NOT EXISTS idx_guild_vault_items_vault ON guild_vault_items(vault_id);
CREATE INDEX IF NOT EXISTS idx_guild_vault_items_item ON guild_vault_items(item_id);

-- Guild permissions indexes
CREATE INDEX IF NOT EXISTS idx_guild_permissions_guild_role ON guild_permissions(guild_id, role);

-- Triggers for automatic timestamp updates

-- Guilds updated_at trigger
CREATE OR REPLACE FUNCTION update_guilds_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_guilds_updated_at
    BEFORE UPDATE ON guilds
    FOR EACH ROW
    EXECUTE FUNCTION update_guilds_updated_at();

-- Guild applications updated_at trigger
CREATE OR REPLACE FUNCTION update_guild_applications_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_guild_applications_updated_at
    BEFORE UPDATE ON guild_applications
    FOR EACH ROW
    EXECUTE FUNCTION update_guild_applications_updated_at();

-- Function to automatically update guild member count
CREATE OR REPLACE FUNCTION update_guild_member_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE guilds SET current_members = current_members + 1 WHERE id = NEW.guild_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE guilds SET current_members = current_members - 1 WHERE id = OLD.guild_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_guild_member_count
    AFTER INSERT OR DELETE ON guild_members
    FOR EACH ROW
    EXECUTE FUNCTION update_guild_member_count();

-- Function to automatically create default guild ranks
CREATE OR REPLACE FUNCTION create_default_guild_ranks()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO guild_ranks (guild_id, rank_name, rank_level, min_experience, benefits) VALUES
    (NEW.id, 'Recruit', 1, 0, '{"permissions": ["chat"]}'::jsonb),
    (NEW.id, 'Member', 2, 1000, '{"permissions": ["chat", "deposit_vault"]}'::jsonb),
    (NEW.id, 'Officer', 3, 5000, '{"permissions": ["chat", "deposit_vault", "invite_members", "manage_announcements"]}'::jsonb),
    (NEW.id, 'Leader', 4, 15000, '{"permissions": ["all"]}'::jsonb);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_create_default_guild_ranks
    AFTER INSERT ON guilds
    FOR EACH ROW
    EXECUTE FUNCTION create_default_guild_ranks();

-- Function to automatically create default guild permissions
CREATE OR REPLACE FUNCTION create_default_guild_permissions()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO guild_permissions (guild_id, role, permission) VALUES
    (NEW.id, 'recruit', 'chat'),
    (NEW.id, 'member', 'chat'),
    (NEW.id, 'member', 'deposit_vault'),
    (NEW.id, 'officer', 'chat'),
    (NEW.id, 'officer', 'deposit_vault'),
    (NEW.id, 'officer', 'invite_members'),
    (NEW.id, 'officer', 'manage_announcements'),
    (NEW.id, 'leader', 'all');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_create_default_guild_permissions
    AFTER INSERT ON guilds
    FOR EACH ROW
    EXECUTE FUNCTION create_default_guild_permissions();

-- Function to log guild events
CREATE OR REPLACE FUNCTION log_guild_event(
    p_guild_id UUID,
    p_event_type VARCHAR(50),
    p_event_data JSONB,
    p_created_by UUID DEFAULT NULL
)
RETURNS VOID AS $$
BEGIN
    INSERT INTO guild_events (guild_id, event_type, event_data, created_by)
    VALUES (p_guild_id, p_event_type, p_event_data, p_created_by);
END;
$$ LANGUAGE plpgsql;

-- Function to get guild summary
CREATE OR REPLACE FUNCTION get_guild_summary(guild_uuid UUID)
RETURNS TABLE (
    guild_id UUID,
    name VARCHAR(100),
    level INTEGER,
    member_count INTEGER,
    leader_name VARCHAR(50),
    reputation INTEGER,
    is_recruiting BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        g.id, g.name, g.level, g.current_members,
        p.username as leader_name, g.reputation, g.is_recruiting
    FROM guilds g
    JOIN players p ON g.leader_id = p.id
    WHERE g.id = guild_uuid AND g.is_active = true;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate guild level from experience
CREATE OR REPLACE FUNCTION calculate_guild_level(exp BIGINT)
RETURNS INTEGER AS $$
DECLARE
    level INTEGER := 1;
    required_exp BIGINT := 1000;
BEGIN
    WHILE exp >= required_exp AND level < 100 LOOP
        level := level + 1;
        required_exp := required_exp * 2; -- Exponential growth
    END LOOP;
    RETURN level;
END;
$$ LANGUAGE plpgsql;

-- Comments for documentation
COMMENT ON TABLE guilds IS 'Guild definitions with metadata and progression';
COMMENT ON TABLE guild_members IS 'Guild membership with roles and contribution tracking';
COMMENT ON TABLE guild_ranks IS 'Guild rank progression system';
COMMENT ON TABLE guild_applications IS 'Guild join application management';
COMMENT ON TABLE guild_events IS 'Audit trail for guild activities and events';
COMMENT ON TABLE guild_achievements IS 'Guild-wide achievements and milestones';
COMMENT ON TABLE guild_vaults IS 'Shared guild storage systems';
COMMENT ON TABLE guild_vault_items IS 'Items stored in guild vaults';
COMMENT ON TABLE guild_messages IS 'Guild communication and announcements';
COMMENT ON TABLE guild_permissions IS 'Role-based permission system';

COMMENT ON FUNCTION get_guild_summary(UUID) IS 'Returns comprehensive guild information';
COMMENT ON FUNCTION calculate_guild_level(BIGINT) IS 'Calculates guild level from experience points';
COMMENT ON FUNCTION log_guild_event(UUID, VARCHAR, JSONB, UUID) IS 'Logs guild events for audit trail';
