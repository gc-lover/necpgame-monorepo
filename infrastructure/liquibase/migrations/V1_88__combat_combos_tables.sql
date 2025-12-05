-- Issue: #1525
-- Combat Combos System Tables
-- Tables for combo loadouts, scoring, and analytics

-- Combo Loadouts Table
CREATE TABLE IF NOT EXISTS gameplay.combo_loadouts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    active_combos UUID[] NOT NULL DEFAULT '{}',
    preferences JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_combo_loadouts_character FOREIGN KEY (character_id) REFERENCES characters.characters(id) ON DELETE CASCADE
);

-- Combo Activations Table
CREATE TABLE IF NOT EXISTS gameplay.combo_activations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    combo_id UUID NOT NULL,
    character_id UUID NOT NULL,
    activated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_combo_activations_character FOREIGN KEY (character_id) REFERENCES characters.characters(id) ON DELETE CASCADE
);

-- Combo Scores Table
CREATE TABLE IF NOT EXISTS gameplay.combo_scores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    activation_id UUID NOT NULL,
    execution_difficulty INTEGER NOT NULL CHECK (execution_difficulty >= 0 AND execution_difficulty <= 100),
    damage_output INTEGER NOT NULL CHECK (damage_output >= 0),
    visual_impact INTEGER NOT NULL CHECK (visual_impact >= 0 AND visual_impact <= 100),
    team_coordination INTEGER CHECK (team_coordination >= 0 AND team_coordination <= 100),
    total_score INTEGER NOT NULL,
    category VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_combo_scores_activation FOREIGN KEY (activation_id) REFERENCES gameplay.combo_activations(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_combo_loadouts_character_id ON gameplay.combo_loadouts(character_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_combo_loadouts_character_unique ON gameplay.combo_loadouts(character_id);

CREATE INDEX IF NOT EXISTS idx_combo_activations_combo_id ON gameplay.combo_activations(combo_id);
CREATE INDEX IF NOT EXISTS idx_combo_activations_character_id ON gameplay.combo_activations(character_id);
CREATE INDEX IF NOT EXISTS idx_combo_activations_activated_at ON gameplay.combo_activations(activated_at);

CREATE INDEX IF NOT EXISTS idx_combo_scores_activation_id ON gameplay.combo_scores(activation_id);
CREATE INDEX IF NOT EXISTS idx_combo_scores_timestamp ON gameplay.combo_scores(timestamp);
CREATE INDEX IF NOT EXISTS idx_combo_scores_category ON gameplay.combo_scores(category);
CREATE INDEX IF NOT EXISTS idx_combo_scores_total_score ON gameplay.combo_scores(total_score);

-- Covering index for analytics queries (Issue: #1525 - Performance optimization)
-- Note: combo_id and character_id are obtained via JOIN with combo_activations
CREATE INDEX IF NOT EXISTS idx_combo_scores_analytics_covering ON gameplay.combo_scores(activation_id, timestamp, total_score, category)
    WHERE total_score > 0;

COMMENT ON TABLE gameplay.combo_loadouts IS 'Stores player combo loadouts and preferences';
COMMENT ON TABLE gameplay.combo_activations IS 'Tracks combo activations for scoring';
COMMENT ON TABLE gameplay.combo_scores IS 'Stores combo performance scores and analytics';

