--liquibase formatted sql

--changeset combat:session_players_support runOnChange:true
--comment: Add support for players in combat sessions with position tracking and health management

-- Create combat_session_players table
CREATE TABLE IF NOT EXISTS gameplay.combat_session_players (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id VARCHAR(255) NOT NULL,
    player_id VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'spectating', 'disconnected')),
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    position JSONB DEFAULT '{"x": 0, "y": 0, "z": 0}',
    health INTEGER NOT NULL DEFAULT 100 CHECK (health >= 0),
    max_health INTEGER NOT NULL DEFAULT 100 CHECK (max_health > 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_session_player UNIQUE (session_id, player_id),
    CONSTRAINT fk_combat_session_players_session FOREIGN KEY (session_id)
        REFERENCES gameplay.combat_sessions(id) ON DELETE CASCADE
);

-- Add indexes for performance
CREATE INDEX IF NOT EXISTS idx_combat_session_players_session_id ON gameplay.combat_session_players(session_id);
CREATE INDEX IF NOT EXISTS idx_combat_session_players_player_id ON gameplay.combat_session_players(player_id);
CREATE INDEX IF NOT EXISTS idx_combat_session_players_status ON gameplay.combat_session_players(status);
CREATE INDEX IF NOT EXISTS idx_combat_session_players_joined_at ON gameplay.combat_session_players(joined_at);

-- Add updated_at trigger
CREATE OR REPLACE FUNCTION update_combat_session_players_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER IF NOT EXISTS trigger_combat_session_players_updated_at
    BEFORE UPDATE ON gameplay.combat_session_players
    FOR EACH ROW EXECUTE FUNCTION update_combat_session_players_updated_at();

-- Add comments
COMMENT ON TABLE gameplay.combat_session_players IS 'Players participating in combat sessions with real-time state tracking';
COMMENT ON COLUMN gameplay.combat_session_players.session_id IS 'Reference to the combat session';
COMMENT ON COLUMN gameplay.combat_session_players.player_id IS 'Unique player identifier';
COMMENT ON COLUMN gameplay.combat_session_players.status IS 'Player status: active, spectating, or disconnected';
COMMENT ON COLUMN gameplay.combat_session_players.position IS 'Player position in 3D space as JSON {x,y,z}';
COMMENT ON COLUMN gameplay.combat_session_players.health IS 'Current health points';
COMMENT ON COLUMN gameplay.combat_session_players.max_health IS 'Maximum health points';

-- Grant permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.combat_session_players TO necpgame_app;
GRANT USAGE ON SEQUENCE combat_session_players_id_seq TO necpgame_app;

--rollback DROP TABLE IF EXISTS gameplay.combat_session_players;
--rollback DROP TRIGGER IF EXISTS trigger_combat_session_players_updated_at ON gameplay.combat_session_players;
--rollback DROP FUNCTION IF EXISTS update_combat_session_players_updated_at();
