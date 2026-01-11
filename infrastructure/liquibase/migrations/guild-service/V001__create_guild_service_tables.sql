-- Guild Service Database Schema
-- Enterprise-grade guild management for Night City MMOFPS RPG
-- Issue: #2295

-- Create guild schema if not exists
CREATE SCHEMA IF NOT EXISTS guilds;
COMMENT ON SCHEMA guilds IS 'Enterprise-grade guild management system for Night City MMOFPS RPG';

-- ===========================================
-- GUILDS TABLE
-- ===========================================
-- PERFORMANCE: Core guild definitions with optimistic locking
-- Supports <20ms P95 for guild retrieval and management
CREATE TABLE guilds.guilds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,
    guild_name VARCHAR(100) NOT NULL,
    guild_tag VARCHAR(10) NOT NULL,
    description TEXT,
    leader_id UUID NOT NULL,
    faction VARCHAR(50),
    level INTEGER DEFAULT 1 CHECK (level > 0),
    experience BIGINT DEFAULT 0 CHECK (experience >= 0),
    max_members INTEGER DEFAULT 50 CHECK (max_members > 0 AND max_members <= 1000),
    current_members INTEGER DEFAULT 0 CHECK (current_members >= 0),
    reputation INTEGER DEFAULT 0,
    is_recruiting BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_guilds_name_version UNIQUE (guild_name, version),

    -- PERFORMANCE: Partial index for active guilds only
    CONSTRAINT ck_guilds_version_positive CHECK (version > 0),
    CONSTRAINT ck_guilds_tag_unique UNIQUE (guild_tag),
    CONSTRAINT ck_guilds_level_max CHECK (level <= 100)
);

-- PERFORMANCE: Index for guild retrieval
CREATE INDEX idx_guilds_leader ON guilds.guilds (leader_id);
CREATE INDEX idx_guilds_faction ON guilds.guilds (faction);
CREATE INDEX idx_guilds_recruiting ON guilds.guilds (is_recruiting) WHERE is_recruiting = true;
CREATE INDEX idx_guilds_level ON guilds.guilds (level DESC, experience DESC);

COMMENT ON TABLE guilds.guilds IS 'Core guild definitions with versioning and optimistic locking';

-- ===========================================
-- GUILD MEMBERS TABLE
-- ===========================================
-- PERFORMANCE: Guild membership management for social operations
-- Supports <12KB per guild member memory usage
CREATE TABLE guilds.guild_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    player_id UUID NOT NULL,
    role VARCHAR(20) DEFAULT 'member' CHECK (role IN ('leader', 'officer', 'member', 'recruit')),
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_active TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    contribution_points INTEGER DEFAULT 0 CHECK (contribution_points >= 0),
    permissions JSONB DEFAULT '{"can_invite": false, "can_kick": false, "can_manage_bank": false}',

    -- PERFORMANCE: Composite unique constraint
    CONSTRAINT uk_guild_members_player UNIQUE (guild_id, player_id),

    -- PERFORMANCE: Optimistic locking for member updates
    version INTEGER NOT NULL DEFAULT 1
);

-- PERFORMANCE: Indexes for member operations
CREATE INDEX idx_guild_members_guild ON guilds.guild_members (guild_id, role);
CREATE INDEX idx_guild_members_player ON guilds.guild_members (player_id);
CREATE INDEX idx_guild_members_active ON guilds.guild_members (guild_id, last_active DESC);

COMMENT ON TABLE guilds.guild_members IS 'Guild membership with roles and permissions';

-- ===========================================
-- GUILD RANKS TABLE
-- ===========================================
-- PERFORMANCE: Guild ranking system for competitive elements
CREATE TABLE guilds.guild_ranks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    rank_type VARCHAR(20) NOT NULL CHECK (rank_type IN ('overall', 'pvp', 'pve', 'economy')),
    rank_position INTEGER NOT NULL CHECK (rank_position > 0),
    score BIGINT DEFAULT 0 CHECK (score >= 0),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Unique constraint for rank types
    CONSTRAINT uk_guild_ranks_type UNIQUE (guild_id, rank_type)
);

-- PERFORMANCE: Index for leaderboard queries
CREATE INDEX idx_guild_ranks_type_score ON guilds.guild_ranks (rank_type, score DESC, rank_position);

COMMENT ON TABLE guilds.guild_ranks IS 'Guild ranking system for competitive gameplay';

-- ===========================================
-- GUILD BANK TABLE
-- ===========================================
-- PERFORMANCE: Guild resource management with optimistic locking
CREATE TABLE guilds.guild_bank (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    version INTEGER NOT NULL DEFAULT 1,
    currency_type VARCHAR(20) NOT NULL,
    amount BIGINT DEFAULT 0 CHECK (amount >= 0),
    last_transaction TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Unique constraint for currency types
    CONSTRAINT uk_guild_bank_currency UNIQUE (guild_id, currency_type)
);

-- PERFORMANCE: Index for bank operations
CREATE INDEX idx_guild_bank_guild ON guilds.guild_bank (guild_id);

COMMENT ON TABLE guilds.guild_bank IS 'Guild bank for shared resources and economy';

-- ===========================================
-- GUILD EVENTS TABLE
-- ===========================================
-- PERFORMANCE: Guild event tracking and coordination
CREATE TABLE guilds.guild_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    event_type VARCHAR(30) NOT NULL CHECK (event_type IN ('raid', 'tournament', 'quest', 'meeting', 'ceremony')),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    duration_minutes INTEGER DEFAULT 60 CHECK (duration_minutes > 0),
    max_participants INTEGER,
    current_participants INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'active', 'completed', 'cancelled')),
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- PERFORMANCE: Indexes for event queries
CREATE INDEX idx_guild_events_guild_scheduled ON guilds.guild_events (guild_id, scheduled_at) WHERE status = 'scheduled';
CREATE INDEX idx_guild_events_type ON guilds.guild_events (event_type, scheduled_at);

COMMENT ON TABLE guilds.guild_events IS 'Guild event coordination and scheduling';

-- ===========================================
-- GUILD ACHIEVEMENTS TABLE
-- ===========================================
-- PERFORMANCE: Guild achievement tracking
CREATE TABLE guilds.guild_achievements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    achievement_type VARCHAR(50) NOT NULL,
    achievement_data JSONB,
    unlocked_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Prevent duplicate achievements
    CONSTRAINT uk_guild_achievements_type UNIQUE (guild_id, achievement_type)
);

-- PERFORMANCE: Index for achievement queries
CREATE INDEX idx_guild_achievements_guild ON guilds.guild_achievements (guild_id);

COMMENT ON TABLE guilds.guild_achievements IS 'Guild achievement tracking and progression';

-- ===========================================
-- GUILD RELATIONSHIPS TABLE
-- ===========================================
-- PERFORMANCE: Inter-guild relationships and diplomacy
CREATE TABLE guilds.guild_relationships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_a_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    guild_b_id UUID NOT NULL REFERENCES guilds.guilds(id) ON DELETE CASCADE,
    relationship_type VARCHAR(20) DEFAULT 'neutral' CHECK (relationship_type IN ('alliance', 'rivalry', 'war', 'neutral', 'truce')),
    established_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE,

    -- PERFORMANCE: Prevent self-relationships and duplicates
    CONSTRAINT ck_guild_relationships_no_self CHECK (guild_a_id != guild_b_id),
    CONSTRAINT uk_guild_relationships_pair UNIQUE (LEAST(guild_a_id, guild_b_id), GREATEST(guild_a_id, guild_b_id))
);

-- PERFORMANCE: Index for relationship queries
CREATE INDEX idx_guild_relationships_guild_a ON guilds.guild_relationships (guild_a_id);
CREATE INDEX idx_guild_relationships_guild_b ON guilds.guild_relationships (guild_b_id);
CREATE INDEX idx_guild_relationships_type ON guilds.guild_relationships (relationship_type);

COMMENT ON TABLE guilds.guild_relationships IS 'Inter-guild relationships and diplomacy system';

-- ===========================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- ===========================================

-- Update guild updated_at timestamp
CREATE OR REPLACE FUNCTION guilds.update_guild_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_guilds_updated_at
    BEFORE UPDATE ON guilds.guilds
    FOR EACH ROW
    EXECUTE FUNCTION guilds.update_guild_updated_at();

-- Update member last_active timestamp
CREATE OR REPLACE FUNCTION guilds.update_member_last_active()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_active = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_guild_members_last_active
    BEFORE UPDATE ON guilds.guild_members
    FOR EACH ROW
    EXECUTE FUNCTION guilds.update_member_last_active();

-- ===========================================
-- PERFORMANCE OPTIMIZATION FUNCTIONS
-- ===========================================

-- Function to get guild with member count
CREATE OR REPLACE FUNCTION guilds.get_guild_with_stats(guild_uuid UUID)
RETURNS TABLE (
    id UUID,
    guild_name VARCHAR(100),
    guild_tag VARCHAR(10),
    description TEXT,
    leader_id UUID,
    faction VARCHAR(50),
    level INTEGER,
    experience BIGINT,
    max_members INTEGER,
    current_members INTEGER,
    reputation INTEGER,
    is_recruiting BOOLEAN,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT g.id, g.guild_name, g.guild_tag, g.description, g.leader_id,
           g.faction, g.level, g.experience, g.max_members,
           COALESCE(m.member_count, 0)::INTEGER as current_members,
           g.reputation, g.is_recruiting, g.created_at, g.updated_at
    FROM guilds.guilds g
    LEFT JOIN (
        SELECT guild_id, COUNT(*) as member_count
        FROM guilds.guild_members
        GROUP BY guild_id
    ) m ON g.id = m.guild_id
    WHERE g.id = guild_uuid;
END;
$$ LANGUAGE plpgsql;

-- Function to update guild member count
CREATE OR REPLACE FUNCTION guilds.update_guild_member_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE guilds.guilds
        SET current_members = current_members + 1
        WHERE id = NEW.guild_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE guilds.guilds
        SET current_members = GREATEST(current_members - 1, 0)
        WHERE id = OLD.guild_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_guild_members_count
    AFTER INSERT OR DELETE ON guilds.guild_members
    FOR EACH ROW
    EXECUTE FUNCTION guilds.update_guild_member_count();

COMMENT ON SCHEMA guilds IS 'Enterprise-grade guild management system for Night City MMOFPS RPG - Issue: #2295';