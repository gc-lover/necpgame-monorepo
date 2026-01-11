--liquibase formatted sql

--changeset faction-service:create-factions-table
CREATE TABLE IF NOT EXISTS factions (
    faction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    leader_id UUID NOT NULL,
    reputation INTEGER NOT NULL DEFAULT 0 CHECK (reputation >= -1000 AND reputation <= 1000),
    influence INTEGER NOT NULL DEFAULT 0 CHECK (influence >= 0),
    diplomatic_stance VARCHAR(20) NOT NULL DEFAULT 'neutral' CHECK (diplomatic_stance IN ('neutral', 'expansionist', 'defensive', 'isolationist')),
    member_count INTEGER NOT NULL DEFAULT 1 CHECK (member_count >= 1),
    max_members INTEGER NOT NULL DEFAULT 1000 CHECK (max_members >= 1 AND max_members <= 10000),
    activity_status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (activity_status IN ('active', 'dormant', 'disbanding')),
    requirements JSONB NOT NULL DEFAULT '{
        "min_reputation": -100,
        "min_influence": 0,
        "application_required": false,
        "approval_required": true,
        "min_member_level": 1
    }',
    statistics JSONB NOT NULL DEFAULT '{
        "wars_declared": 0,
        "wars_won": 0,
        "alliances_formed": 0,
        "territories_claimed": 0,
        "influence_gained": 0,
        "average_member_reputation": 0.0
    }',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_factions_reputation (reputation DESC),
    INDEX idx_factions_influence (influence DESC),
    INDEX idx_factions_activity_status (activity_status),
    INDEX idx_factions_leader_id (leader_id),
    INDEX idx_factions_created_at (created_at DESC),

    -- GIN indexes for JSON fields
    INDEX idx_factions_requirements_gin (requirements) USING GIN,
    INDEX idx_factions_statistics_gin (statistics) USING GIN
);

--changeset faction-service:create-diplomatic-relations-table
CREATE TABLE IF NOT EXISTS diplomatic_relations (
    faction_id UUID NOT NULL,
    target_faction_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'neutral' CHECK (status IN ('neutral', 'allied', 'hostile', 'at_war')),
    standing INTEGER NOT NULL DEFAULT 0 CHECK (standing >= -1000 AND standing <= 1000),
    established_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_action_at TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (faction_id, target_faction_id),

    -- Foreign key constraints
    CONSTRAINT fk_diplomatic_relations_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,
    CONSTRAINT fk_diplomatic_relations_target FOREIGN KEY (target_faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_diplomatic_relations_status (status),
    INDEX idx_diplomatic_relations_standing (standing DESC),
    INDEX idx_diplomatic_relations_established_at (established_at DESC),
    INDEX idx_diplomatic_relations_last_action_at (last_action_at DESC),

    -- Ensure no self-relations
    CONSTRAINT chk_no_self_relation CHECK (faction_id != target_faction_id)
);

--changeset faction-service:create-diplomatic-actions-table
CREATE TABLE IF NOT EXISTS diplomatic_actions (
    action_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL,
    action_type VARCHAR(30) NOT NULL CHECK (action_type IN ('declare_war', 'propose_alliance', 'offer_peace', 'break_alliance', 'propose_trade')),
    target_faction_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected', 'expired')),
    message TEXT,
    treaty_terms TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    response_deadline TIMESTAMP WITH TIME ZONE,

    -- Foreign key constraints
    CONSTRAINT fk_diplomatic_actions_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,
    CONSTRAINT fk_diplomatic_actions_target FOREIGN KEY (target_faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_diplomatic_actions_status (status),
    INDEX idx_diplomatic_actions_action_type (action_type),
    INDEX idx_diplomatic_actions_created_at (created_at DESC),
    INDEX idx_diplomatic_actions_response_deadline (response_deadline),
    INDEX idx_diplomatic_actions_faction_target (faction_id, target_faction_id),

    -- Ensure no self-actions
    CONSTRAINT chk_no_self_action CHECK (faction_id != target_faction_id)
);

--changeset faction-service:create-territories-table
CREATE TABLE IF NOT EXISTS territories (
    territory_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100),
    boundaries JSONB NOT NULL,
    control_level DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (control_level >= 0.0 AND control_level <= 1.0),
    claimed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_conflict_at TIMESTAMP WITH TIME ZONE,

    -- Indexes for performance
    INDEX idx_territories_control_level (control_level DESC),
    INDEX idx_territories_claimed_at (claimed_at DESC),
    INDEX idx_territories_last_conflict_at (last_conflict_at DESC),

    -- GIN index for boundaries
    INDEX idx_territories_boundaries_gin (boundaries) USING GIN
);

--changeset faction-service:create-territory-claims-table
CREATE TABLE IF NOT EXISTS territory_claims (
    claim_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL,
    center_x DECIMAL(10,6) NOT NULL,
    center_y DECIMAL(10,6) NOT NULL,
    radius DECIMAL(8,2) NOT NULL CHECK (radius >= 10 AND radius <= 1000),
    claim_type VARCHAR(20) NOT NULL DEFAULT 'expansion' CHECK (claim_type IN ('expansion', 'reclamation', 'conquest')),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'contested', 'rejected')),
    justification TEXT,
    established_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    dispute_period INTEGER NOT NULL DEFAULT 7 CHECK (dispute_period >= 1 AND dispute_period <= 30),

    -- Foreign key constraints
    CONSTRAINT fk_territory_claims_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_territory_claims_status (status),
    INDEX idx_territory_claims_claim_type (claim_type),
    INDEX idx_territory_claims_established_at (established_at DESC),
    INDEX idx_territory_claims_faction (faction_id),
    INDEX idx_territory_claims_center (center_x, center_y),

    -- Spatial index for geographic queries
    INDEX idx_territory_claims_spatial USING GIST (ST_Point(center_x, center_y))
);

--changeset faction-service:create-reputation-events-table
CREATE TABLE IF NOT EXISTS reputation_events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    value_change INTEGER NOT NULL CHECK (value_change >= -500 AND value_change <= 500),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    description TEXT,

    -- Foreign key constraints
    CONSTRAINT fk_reputation_events_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_reputation_events_faction_timestamp (faction_id, timestamp DESC),
    INDEX idx_reputation_events_event_type (event_type),
    INDEX idx_reputation_events_value_change (value_change),
    INDEX idx_reputation_events_timestamp (timestamp DESC)
);

--changeset faction-service:create-treaties-table
CREATE TABLE IF NOT EXISTS treaties (
    treaty_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL,
    target_faction_id UUID NOT NULL,
    treaty_type VARCHAR(30) NOT NULL CHECK (treaty_type IN ('alliance', 'trade_agreement', 'non_aggression_pact', 'ceasefire')),
    signed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE,
    terms TEXT NOT NULL,

    -- Foreign key constraints
    CONSTRAINT fk_treaties_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,
    CONSTRAINT fk_treaties_target FOREIGN KEY (target_faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_treaties_treaty_type (treaty_type),
    INDEX idx_treaties_signed_at (signed_at DESC),
    INDEX idx_treaties_expires_at (expires_at),
    INDEX idx_treaties_faction_target (faction_id, target_faction_id),

    -- Ensure no self-treaties
    CONSTRAINT chk_no_self_treaty CHECK (faction_id != target_faction_id)
);

--changeset faction-service:create-border-disputes-table
CREATE TABLE IF NOT EXISTS border_disputes (
    dispute_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    disputed_territory UUID NOT NULL,
    claimants UUID[] NOT NULL CHECK (array_length(claimants, 1) >= 2),
    dispute_started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    resolution_deadline TIMESTAMP WITH TIME ZONE,

    -- Foreign key constraints
    CONSTRAINT fk_border_disputes_territory FOREIGN KEY (disputed_territory) REFERENCES territories(territory_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_border_disputes_started_at (dispute_started_at DESC),
    INDEX idx_border_disputes_resolution_deadline (resolution_deadline),
    INDEX idx_border_disputes_territory (disputed_territory),

    -- GIN index for claimants array
    INDEX idx_border_disputes_claimants_gin (claimants) USING GIN
);

--changeset faction-service:create-influence-zones-table
CREATE TABLE IF NOT EXISTS influence_zones (
    zone_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL,
    center_x DECIMAL(10,6) NOT NULL,
    center_y DECIMAL(10,6) NOT NULL,
    radius DECIMAL(8,2) NOT NULL CHECK (radius >= 0),
    influence_level DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (influence_level >= 0.0 AND influence_level <= 1.0),
    contested_by UUID[] DEFAULT '{}',

    -- Foreign key constraints
    CONSTRAINT fk_influence_zones_faction FOREIGN KEY (faction_id) REFERENCES factions(faction_id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_influence_zones_faction (faction_id),
    INDEX idx_influence_zones_influence_level (influence_level DESC),
    INDEX idx_influence_zones_center (center_x, center_y),

    -- Spatial index for geographic queries
    INDEX idx_influence_zones_spatial USING GIST (ST_Point(center_x, center_y)),

    -- GIN index for contested_by array
    INDEX idx_influence_zones_contested_by_gin (contested_by) USING GIN
);

--changeset faction-service:add-triggers
-- Function to update faction updated_at timestamp
CREATE OR REPLACE FUNCTION update_faction_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for factions table
CREATE TRIGGER trg_update_faction_updated_at
    BEFORE UPDATE ON factions
    FOR EACH ROW
    EXECUTE FUNCTION update_faction_updated_at();

-- Function to update faction member count
CREATE OR REPLACE FUNCTION update_faction_member_count()
RETURNS TRIGGER AS $$
BEGIN
    -- This would be implemented to track member changes
    -- For now, it's a placeholder
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--changeset faction-service:add-comments
COMMENT ON TABLE factions IS 'Core faction entities with diplomatic and statistical data';
COMMENT ON TABLE diplomatic_relations IS 'Diplomatic relationships between factions';
COMMENT ON TABLE diplomatic_actions IS 'Pending and historical diplomatic actions';
COMMENT ON TABLE territories IS 'Geographic territories controlled by factions';
COMMENT ON TABLE territory_claims IS 'Pending and historical territory claims';
COMMENT ON TABLE reputation_events IS 'Historical reputation changes for auditing';
COMMENT ON TABLE treaties IS 'Active and historical diplomatic treaties';
COMMENT ON TABLE border_disputes IS 'Ongoing territorial disputes between factions';
COMMENT ON TABLE influence_zones IS 'Faction influence zones and contested areas';