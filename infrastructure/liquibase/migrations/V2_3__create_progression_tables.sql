-- Issue: #79
-- Progression service database schema
-- Migration: V2_3__create_progression_tables

-- Create character_progression table
CREATE TABLE IF NOT EXISTS character_progression (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1 AND level <= 100),
    experience BIGINT NOT NULL DEFAULT 0 CHECK (experience >= 0),
    experience_to_next BIGINT NOT NULL DEFAULT 1000,
    attribute_points INTEGER NOT NULL DEFAULT 0 CHECK (attribute_points >= 0),
    skill_points INTEGER NOT NULL DEFAULT 0 CHECK (skill_points >= 0),
    attributes JSONB NOT NULL DEFAULT '{
        "intelligence": 3,
        "reflexes": 3,
        "dexterity": 3,
        "technology": 3,
        "cool": 3,
        "willpower": 3,
        "luck": 3,
        "movement": 3,
        "body": 3,
        "empathy": 3
    }'::jsonb,
    total_attribute_points_spent INTEGER NOT NULL DEFAULT 0,
    last_level_up_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(character_id)
);

-- Create skill_experience table
CREATE TABLE IF NOT EXISTS skill_experience (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    skill_id VARCHAR(50) NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    skill_category VARCHAR(20) NOT NULL CHECK (skill_category IN ('combat', 'technical', 'social', 'awareness', 'body', 'control', 'sense')),
    related_attribute VARCHAR(20) NOT NULL,
    current_level INTEGER NOT NULL DEFAULT 0 CHECK (current_level >= 0 AND current_level <= 10),
    max_level INTEGER NOT NULL DEFAULT 10 CHECK (max_level >= 1 AND max_level <= 10),
    experience BIGINT NOT NULL DEFAULT 0 CHECK (experience >= 0),
    experience_to_next BIGINT NOT NULL DEFAULT 100,
    is_unlocked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(character_id, skill_id)
);

-- Create progression_events table for audit trail
CREATE TABLE IF NOT EXISTS progression_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL,
    event_data JSONB NOT NULL,
    experience_gained BIGINT DEFAULT 0,
    level_before INTEGER,
    level_after INTEGER,
    attribute_points_gained INTEGER DEFAULT 0,
    skill_points_gained INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create level_requirements table
CREATE TABLE IF NOT EXISTS level_requirements (
    level INTEGER PRIMARY KEY CHECK (level >= 1 AND level <= 100),
    experience_required BIGINT NOT NULL CHECK (experience_required > 0),
    attribute_points_granted INTEGER NOT NULL DEFAULT 5 CHECK (attribute_points_granted >= 0),
    skill_points_granted INTEGER NOT NULL DEFAULT 3 CHECK (skill_points_granted >= 0),
    description TEXT
);

-- Create skill_definitions table
CREATE TABLE IF NOT EXISTS skill_definitions (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('combat', 'technical', 'social', 'awareness', 'body', 'control', 'sense')),
    related_attribute VARCHAR(20) NOT NULL,
    max_level INTEGER NOT NULL DEFAULT 10 CHECK (max_level >= 1 AND max_level <= 10),
    base_difficulty INTEGER NOT NULL DEFAULT 1 CHECK (base_difficulty >= 1 AND base_difficulty <= 6),
    unlock_requirements JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create progression_multipliers table for experience bonuses
CREATE TABLE IF NOT EXISTS progression_multipliers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (multiplier >= 0.1 AND multiplier <= 5.0),
    source_type VARCHAR(30) NOT NULL, -- 'achievement', 'event', 'item', 'buff'
    source_id VARCHAR(100), -- ID источника бонуса
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_character_progression_character_id ON character_progression(character_id);
CREATE INDEX IF NOT EXISTS idx_character_progression_level ON character_progression(level);
CREATE INDEX IF NOT EXISTS idx_skill_experience_character_id ON skill_experience(character_id);
CREATE INDEX IF NOT EXISTS idx_skill_experience_skill_id ON skill_experience(skill_id);
CREATE INDEX IF NOT EXISTS idx_skill_experience_category ON skill_experience(skill_category);
CREATE INDEX IF NOT EXISTS idx_progression_events_character_id ON progression_events(character_id);
CREATE INDEX IF NOT EXISTS idx_progression_events_event_type ON progression_events(event_type);
CREATE INDEX IF NOT EXISTS idx_progression_events_created_at ON progression_events(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_skill_definitions_category ON skill_definitions(category);
CREATE INDEX IF NOT EXISTS idx_skill_definitions_related_attribute ON skill_definitions(related_attribute);

-- Insert level requirements (exponential growth formula)
INSERT INTO level_requirements (level, experience_required, attribute_points_granted, skill_points_granted, description) VALUES
    (1, 0, 0, 0, 'Starting level'),
    (2, 1000, 5, 3, 'First level up'),
    (3, 2250, 5, 3, 'Continued growth'),
    (4, 3750, 5, 3, 'Building foundation'),
    (5, 5625, 5, 3, 'Halfway point'),
    (6, 7875, 5, 3, 'Gaining momentum'),
    (7, 10500, 5, 3, 'Experienced runner'),
    (8, 13500, 5, 3, 'Skilled operative'),
    (9, 16912, 5, 3, 'Veteran status'),
    (10, 20736, 5, 3, 'Double digits'),
    (11, 25000, 5, 3, 'Elite operative'),
    (12, 29725, 5, 3, 'Master level'),
    (13, 34912, 5, 3, 'Legendary status'),
    (14, 40600, 5, 3, 'Night City legend'),
    (15, 46812, 5, 3, 'Maximum level')
ON CONFLICT (level) DO NOTHING;

-- Insert basic skill definitions
INSERT INTO skill_definitions (id, name, description, category, related_attribute, max_level, base_difficulty, unlock_requirements) VALUES
    ('athletics', 'Athletics', 'Physical fitness and movement skills', 'body', 'movement', 10, 1, NULL),
    ('brawling', 'Brawling', 'Unarmed combat techniques', 'combat', 'body', 10, 1, NULL),
    ('melee', 'Melee Weapons', 'Close combat weapon proficiency', 'combat', 'reflexes', 10, 2, NULL),
    ('pistols', 'Pistols', 'Handgun proficiency', 'combat', 'reflexes', 10, 2, NULL),
    ('rifles', 'Rifles', 'Long-range weapon proficiency', 'combat', 'reflexes', 10, 3, NULL),
    ('electronics', 'Electronics', 'Electronic device operation and repair', 'technical', 'technology', 10, 2, NULL),
    ('hacking', 'Hacking', 'Cybernetic intrusion and data manipulation', 'technical', 'intelligence', 10, 4, '{"level": 3}'),
    ('stealth', 'Stealth', 'Concealment and infiltration techniques', 'awareness', 'dexterity', 10, 3, NULL),
    ('perception', 'Perception', 'Environmental awareness and detection', 'awareness', 'intelligence', 10, 2, NULL),
    ('persuasion', 'Persuasion', 'Social manipulation and negotiation', 'social', 'cool', 10, 2, NULL),
    ('intimidation', 'Intimidation', 'Coercion and threat assessment', 'social', 'cool', 10, 1, NULL),
    ('street_knowledge', 'Street Knowledge', 'Urban survival and information network', 'awareness', 'intelligence', 10, 2, NULL),
    ('driving', 'Driving', 'Vehicle operation and control', 'technical', 'reflexes', 10, 2, NULL),
    ('medicine', 'Medicine', 'Medical treatment and cyberware installation', 'technical', 'intelligence', 10, 3, '{"level": 2}'),
    ('crafting', 'Crafting', 'Item creation and modification', 'technical', 'dexterity', 10, 3, NULL)
ON CONFLICT (id) DO NOTHING;

-- Create trigger functions
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers
CREATE TRIGGER update_character_progression_updated_at
    BEFORE UPDATE ON character_progression
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_skill_experience_updated_at
    BEFORE UPDATE ON skill_experience
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_skill_definitions_updated_at
    BEFORE UPDATE ON skill_definitions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create function to calculate experience for level
CREATE OR REPLACE FUNCTION calculate_experience_for_level(target_level INTEGER)
RETURNS BIGINT AS $$
DECLARE
    base_exp BIGINT := 1000;
    growth_rate DECIMAL := 1.25;
    total_exp BIGINT := 0;
    current_level INTEGER := 2;
BEGIN
    IF target_level <= 1 THEN
        RETURN 0;
    END IF;

    WHILE current_level < target_level LOOP
        total_exp := total_exp + (base_exp * POWER(growth_rate, current_level - 2)::BIGINT);
        current_level := current_level + 1;
    END LOOP;

    RETURN total_exp;
END;
$$ language 'plpgsql';

-- Create function to get character progression summary
CREATE OR REPLACE FUNCTION get_character_progression_summary(char_id UUID)
RETURNS TABLE (
    level INTEGER,
    experience BIGINT,
    experience_to_next BIGINT,
    attribute_points INTEGER,
    skill_points INTEGER,
    attributes JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cp.level,
        cp.experience,
        cp.experience_to_next,
        cp.attribute_points,
        cp.skill_points,
        cp.attributes
    FROM character_progression cp
    WHERE cp.character_id = char_id;
END;
$$ language 'plpgsql';

-- Comments for documentation
COMMENT ON TABLE character_progression IS 'Core progression data for characters including level, experience, and attributes';
COMMENT ON TABLE skill_experience IS 'Individual skill progression and experience tracking';
COMMENT ON TABLE progression_events IS 'Audit trail of all progression-related events';
COMMENT ON TABLE level_requirements IS 'Experience requirements and rewards for each level';
COMMENT ON TABLE skill_definitions IS 'Master list of available skills with their properties';
COMMENT ON TABLE progression_multipliers IS 'Experience multipliers from various sources';

COMMENT ON FUNCTION calculate_experience_for_level(INTEGER) IS 'Calculate total experience required to reach a specific level';
COMMENT ON FUNCTION get_character_progression_summary(UUID) IS 'Get a summary of character progression data';