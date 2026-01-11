-- Issue: #2045 - Дополнить механику репутации - добавить динамические последствия и квестовые триггеры
-- liquibase formatted sql

--changeset author:backend-agent dbms:postgresql

BEGIN;
--comment: Add reputation consequences and quest triggers tables for enhanced reputation system

-- Table: reputation.reputation_events
-- Stores all reputation change events with consequences and triggers
CREATE TABLE IF NOT EXISTS reputation.reputation_events
(
    id              UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    subject_id      UUID                     NOT NULL,       -- Player whose reputation changed
    target_id       UUID,                                    -- Target of reputation (faction, location, etc.)
    category        VARCHAR(50)               NOT NULL        CHECK (category IN ('combat', 'social', 'trading', 'leadership', 'craftsmanship', 'reliability', 'generosity', 'combat_skill')),
    points          INTEGER                   NOT NULL,       -- Points added/subtracted
    old_value       INTEGER                   NOT NULL,       -- Reputation value before change
    new_value       INTEGER                   NOT NULL,       -- Reputation value after change
    description     TEXT                      NOT NULL,       -- Human-readable description
    event_type      VARCHAR(50)               NOT NULL        DEFAULT 'manual' CHECK (event_type IN ('manual', 'quest', 'achievement', 'decay', 'system')),
    metadata        JSONB,                                    -- Additional event metadata
    created_at      TIMESTAMP WITH TIME ZONE  NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Table: reputation.active_consequences
-- Stores currently active reputation consequences
CREATE TABLE IF NOT EXISTS reputation.active_consequences
(
    id              UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    event_id        UUID                     NOT NULL        REFERENCES reputation.reputation_events(id) ON DELETE CASCADE,
    player_id       UUID                     NOT NULL,       -- Affected player
    consequence_type VARCHAR(100)             NOT NULL        CHECK (consequence_type IN ('social_penalty', 'economic_penalty', 'quest_penalty', 'faction_penalty', 'access_restriction', 'relationship_damage')),
    target_type     VARCHAR(50)               NOT NULL        DEFAULT 'player' CHECK (target_type IN ('player', 'faction', 'location', 'global')),
    severity        VARCHAR(20)               NOT NULL        CHECK (severity IN ('minor', 'moderate', 'major', 'critical')),
    duration_hours  INTEGER                   NOT NULL        DEFAULT 0, -- 0 = permanent
    description     TEXT                      NOT NULL,
    parameters      JSONB,                                    -- Consequence-specific parameters
    expires_at      TIMESTAMP WITH TIME ZONE,                 -- NULL for permanent consequences
    created_at      TIMESTAMP WITH TIME ZONE  NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE  NOT NULL        DEFAULT CURRENT_TIMESTAMP
);

-- Table: reputation.quest_triggers
-- Stores quest trigger definitions and their evaluations
CREATE TABLE IF NOT EXISTS reputation.quest_triggers
(
    id              UUID                     PRIMARY KEY     DEFAULT gen_random_uuid(),
    event_id        UUID                     NOT NULL        REFERENCES reputation.reputation_events(id) ON DELETE CASCADE,
    player_id       UUID                     NOT NULL,       -- Player for whom quest was triggered
    quest_id        VARCHAR(255)             NOT NULL,       -- ID of the triggered quest
    trigger_type    VARCHAR(50)              NOT NULL        CHECK (trigger_type IN ('unlocked', 'blocked', 'requirements_modified', 'rewards_modified')),
    condition_category VARCHAR(50)            NOT NULL        CHECK (condition_category IN ('combat', 'social', 'trading', 'leadership', 'craftsmanship', 'reliability', 'generosity', 'combat_skill', 'overall')),
    condition_operator VARCHAR(20)            NOT NULL        CHECK (condition_operator IN ('greater_than', 'less_than', 'equal', 'greater_equal', 'less_equal', 'between')),
    condition_threshold INTEGER               NOT NULL,       -- Threshold value for condition
    condition_threshold_max INTEGER,                        -- Upper threshold for 'between' operator
    reason          TEXT                      NOT NULL,       -- Why this quest was triggered
    quest_data      JSONB,                                    -- Quest details (title, category, etc.)
    processed_at    TIMESTAMP WITH TIME ZONE  NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    expires_at      TIMESTAMP WITH TIME ZONE,                 -- When trigger expires (NULL = permanent)
    is_active       BOOLEAN                   NOT NULL        DEFAULT true
);

-- Indexes for performance optimization (MMOFPS-scale queries)

-- Reputation events indexes
CREATE INDEX IF NOT EXISTS idx_reputation_events_subject_id ON reputation.reputation_events(subject_id);
CREATE INDEX IF NOT EXISTS idx_reputation_events_target_id ON reputation.reputation_events(target_id);
CREATE INDEX IF NOT EXISTS idx_reputation_events_category ON reputation.reputation_events(category);
CREATE INDEX IF NOT EXISTS idx_reputation_events_created_at ON reputation.reputation_events(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_reputation_events_subject_category ON reputation.reputation_events(subject_id, category);

-- Active consequences indexes
CREATE INDEX IF NOT EXISTS idx_active_consequences_player_id ON reputation.active_consequences(player_id);
CREATE INDEX IF NOT EXISTS idx_active_consequences_event_id ON reputation.active_consequences(event_id);
CREATE INDEX IF NOT EXISTS idx_active_consequences_type ON reputation.active_consequences(consequence_type);
CREATE INDEX IF NOT EXISTS idx_active_consequences_expires_at ON reputation.active_consequences(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_active_consequences_player_type ON reputation.active_consequences(player_id, consequence_type);

-- Quest triggers indexes
CREATE INDEX IF NOT EXISTS idx_quest_triggers_player_id ON reputation.quest_triggers(player_id);
CREATE INDEX IF NOT EXISTS idx_quest_triggers_event_id ON reputation.quest_triggers(event_id);
CREATE INDEX IF NOT EXISTS idx_quest_triggers_quest_id ON reputation.quest_triggers(quest_id);
CREATE INDEX IF NOT EXISTS idx_quest_triggers_active ON reputation.quest_triggers(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_quest_triggers_expires_at ON reputation.quest_triggers(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_quest_triggers_player_quest ON reputation.quest_triggers(player_id, quest_id);

-- Partial indexes for active records
CREATE INDEX IF NOT EXISTS idx_active_consequences_active ON reputation.active_consequences(player_id, expires_at) WHERE expires_at IS NULL OR expires_at > CURRENT_TIMESTAMP;
CREATE INDEX IF NOT EXISTS idx_quest_triggers_active_recent ON reputation.quest_triggers(player_id, processed_at DESC) WHERE is_active = true;

-- Comments for documentation
COMMENT ON TABLE reputation.reputation_events IS 'Stores all reputation change events with full audit trail';
COMMENT ON TABLE reputation.active_consequences IS 'Currently active reputation consequences affecting players';
COMMENT ON TABLE reputation.quest_triggers IS 'Quest triggers activated by reputation changes';

COMMENT ON COLUMN reputation.reputation_events.metadata IS 'JSON metadata for extensibility (game events, modifiers, etc.)';
COMMENT ON COLUMN reputation.active_consequences.parameters IS 'Consequence-specific parameters (multipliers, affected entities, etc.)';
COMMENT ON COLUMN reputation.quest_triggers.quest_data IS 'Quest metadata (title, category, requirements, rewards)';

COMMIT;