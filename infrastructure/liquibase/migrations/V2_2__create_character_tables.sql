-- Issue: #75
-- Character management service database schema
-- Migration: V2_2__create_character_tables

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slots_purchased INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create characters table
CREATE TABLE IF NOT EXISTS characters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    character_class VARCHAR(20) NOT NULL CHECK (character_class IN ('solo', 'nomad', 'corpo', 'fixer', 'netrunner', 'techie', 'media', 'rockerboy', 'edgerunner')),
    origin VARCHAR(20) NOT NULL CHECK (origin IN ('nomad', 'corpo', 'street_kid', 'edgerunner', 'trauma_team')),
    level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1),
    experience INTEGER NOT NULL DEFAULT 0 CHECK (experience >= 0),
    appearance JSONB,
    attributes JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'deleted', 'dead')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Create character_slots table (for future slot management)
CREATE TABLE IF NOT EXISTS character_slots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    slot_number INTEGER NOT NULL CHECK (slot_number >= 1 AND slot_number <= 10),
    character_id UUID REFERENCES characters(id) ON DELETE SET NULL,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(player_id, slot_number)
);

-- Create character_restore_queue table
CREATE TABLE IF NOT EXISTS character_restore_queue (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    requested_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    restored_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected', 'expired')),
    UNIQUE(character_id)
);

-- Create character_state_snapshots table
CREATE TABLE IF NOT EXISTS character_state_snapshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    snapshot_data JSONB NOT NULL,
    reason VARCHAR(50) NOT NULL, -- 'switch', 'delete', 'backup'
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create character_equipment_refs table
CREATE TABLE IF NOT EXISTS character_equipment_refs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    equipment_type VARCHAR(20) NOT NULL, -- 'weapon', 'armor', 'cyberware', 'clothing'
    equipment_id UUID NOT NULL, -- References inventory items
    slot VARCHAR(20), -- Specific slot for equipment
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(character_id, equipment_type, slot)
);

-- Create character_inventory_refs table
CREATE TABLE IF NOT EXISTS character_inventory_refs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    inventory_id UUID NOT NULL, -- References main inventory
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(character_id, inventory_id)
);

-- Create character_activity_log table
CREATE TABLE IF NOT EXISTS character_activity_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    activity_type VARCHAR(30) NOT NULL CHECK (activity_type IN ('login', 'logout', 'combat', 'quest', 'trade', 'crafting', 'leveling', 'death', 'revival')),
    description TEXT,
    location VARCHAR(100), -- Game zone/area
    experience_gained INTEGER DEFAULT 0,
    metadata JSONB, -- Additional activity data
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_characters_player_id ON characters(player_id);
CREATE INDEX IF NOT EXISTS idx_characters_status ON characters(status);
CREATE INDEX IF NOT EXISTS idx_characters_name ON characters(name);
CREATE INDEX IF NOT EXISTS idx_characters_created_at ON characters(created_at);
CREATE INDEX IF NOT EXISTS idx_character_slots_player_id ON character_slots(player_id);
CREATE INDEX IF NOT EXISTS idx_character_restore_queue_character_id ON character_restore_queue(character_id);
CREATE INDEX IF NOT EXISTS idx_character_restore_queue_expires_at ON character_restore_queue(expires_at);
CREATE INDEX IF NOT EXISTS idx_character_state_snapshots_character_id ON character_state_snapshots(character_id);
CREATE INDEX IF NOT EXISTS idx_character_equipment_refs_character_id ON character_equipment_refs(character_id);
CREATE INDEX IF NOT EXISTS idx_character_inventory_refs_character_id ON character_inventory_refs(character_id);
CREATE INDEX IF NOT EXISTS idx_character_activity_log_character_id ON character_activity_log(character_id);
CREATE INDEX IF NOT EXISTS idx_character_activity_log_player_id ON character_activity_log(player_id);
CREATE INDEX IF NOT EXISTS idx_character_activity_log_created_at ON character_activity_log(created_at);
CREATE INDEX IF NOT EXISTS idx_character_activity_log_activity_type ON character_activity_log(activity_type);

-- Create partial indexes for active characters
CREATE INDEX IF NOT EXISTS idx_characters_active_player_id ON characters(player_id) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_character_activity_log_recent ON character_activity_log(created_at DESC) WHERE created_at > NOW() - INTERVAL '30 days';

-- Insert default attributes templates (for new characters)
CREATE TABLE IF NOT EXISTS character_class_templates (
    character_class VARCHAR(20) PRIMARY KEY,
    base_attributes JSONB NOT NULL,
    description TEXT
);

INSERT INTO character_class_templates (character_class, base_attributes, description) VALUES
    ('solo', '{"intelligence": 3, "reflexes": 6, "dexterity": 6, "technology": 4, "cool": 7, "willpower": 5, "luck": 5, "movement": 5, "body": 7, "empathy": 4}', 'Solo - мастер ближнего боя и выживания'),
    ('nomad', '{"intelligence": 4, "reflexes": 5, "dexterity": 6, "technology": 4, "cool": 6, "willpower": 6, "luck": 7, "movement": 7, "body": 6, "empathy": 4}', 'Nomad - эксперт вождения и выживания в пустоши'),
    ('corpo', '{"intelligence": 7, "reflexes": 4, "dexterity": 4, "technology": 6, "cool": 6, "willpower": 6, "luck": 4, "movement": 4, "body": 4, "empathy": 5}', 'Corpo - корпоративный интриган и технарь'),
    ('fixer', '{"intelligence": 6, "reflexes": 5, "dexterity": 6, "technology": 5, "cool": 6, "willpower": 4, "luck": 6, "movement": 5, "body": 5, "empathy": 5}', 'Fixer - связной и посредник'),
    ('netrunner', '{"intelligence": 8, "reflexes": 4, "dexterity": 4, "technology": 8, "cool": 5, "willpower": 6, "luck": 4, "movement": 3, "body": 3, "empathy": 3}', 'Netrunner - хакер киберпространства'),
    ('techie', '{"intelligence": 6, "reflexes": 5, "dexterity": 6, "technology": 7, "cool": 4, "willpower": 5, "luck": 5, "movement": 4, "body": 5, "empathy": 4}', 'Techie - инженер и ремонтник'),
    ('media', '{"intelligence": 6, "reflexes": 5, "dexterity": 5, "technology": 5, "cool": 6, "willpower": 4, "luck": 6, "movement": 4, "body": 4, "empathy": 6}', 'Media - журналист и манипулятор информацией'),
    ('rockerboy', '{"intelligence": 5, "reflexes": 5, "dexterity": 5, "technology": 4, "cool": 8, "willpower": 5, "luck": 6, "movement": 4, "body": 4, "empathy": 6}', 'Rockerboy - музыкант и бунтарь'),
    ('edgerunner', '{"intelligence": 4, "reflexes": 7, "dexterity": 7, "technology": 5, "cool": 6, "willpower": 4, "luck": 6, "movement": 6, "body": 6, "empathy": 3}', 'Edgerunner - наемник и авантюрист')
ON CONFLICT (character_class) DO NOTHING;

-- Create trigger functions
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers
CREATE TRIGGER update_players_updated_at
    BEFORE UPDATE ON players
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_characters_updated_at
    BEFORE UPDATE ON characters
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create function for automatic character cleanup
CREATE OR REPLACE FUNCTION cleanup_expired_character_restores()
RETURNS void AS $$
BEGIN
    -- Mark expired restore requests as expired
    UPDATE character_restore_queue
    SET status = 'expired'
    WHERE status = 'pending' AND expires_at < NOW();

    -- Permanently delete characters that have been soft-deleted for more than 30 days
    -- and have no pending restore requests
    DELETE FROM characters
    WHERE status = 'deleted'
      AND deleted_at < NOW() - INTERVAL '30 days'
      AND id NOT IN (
          SELECT character_id FROM character_restore_queue
          WHERE status = 'pending'
      );
END;
$$ language 'plpgsql';

-- Create function to get available character slots for a player
CREATE OR REPLACE FUNCTION get_available_character_slots(player_uuid UUID)
RETURNS INTEGER AS $$
DECLARE
    total_slots INTEGER;
    used_slots INTEGER;
BEGIN
    -- Get total slots (base 3 + purchased)
    SELECT COALESCE(slots_purchased, 0) + 3 INTO total_slots
    FROM players WHERE id = player_uuid;

    -- If no player record, return default
    IF total_slots IS NULL THEN
        RETURN 3;
    END IF;

    -- Get used slots (active characters)
    SELECT COUNT(*) INTO used_slots
    FROM characters
    WHERE player_id = player_uuid AND status = 'active';

    RETURN GREATEST(0, total_slots - used_slots);
END;
$$ language 'plpgsql';

-- Comments for documentation
COMMENT ON TABLE players IS 'Player accounts with character slot management';
COMMENT ON TABLE characters IS 'Character profiles and progression data';
COMMENT ON TABLE character_slots IS 'Character slot assignments and locking';
COMMENT ON TABLE character_restore_queue IS 'Queue for character restoration requests';
COMMENT ON TABLE character_state_snapshots IS 'Snapshots of character state for restoration';
COMMENT ON TABLE character_equipment_refs IS 'References to equipped items';
COMMENT ON TABLE character_inventory_refs IS 'References to character inventories';
COMMENT ON TABLE character_activity_log IS 'Log of character activities and events';
COMMENT ON TABLE character_class_templates IS 'Base attribute templates for character classes';

COMMENT ON FUNCTION cleanup_expired_character_restores() IS 'Clean up expired character restore requests and permanently delete old soft-deleted characters';
COMMENT ON FUNCTION get_available_character_slots(UUID) IS 'Calculate available character slots for a player';