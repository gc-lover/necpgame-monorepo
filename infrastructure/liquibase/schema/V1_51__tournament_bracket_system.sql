-- Issue: #2210
-- Tournament Bracket System Schema for NECPGAME competitive gameplay
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create tournament bracket system tables for competitive gameplay

BEGIN;

-- Table: tournament.tournaments
CREATE TABLE IF NOT EXISTS tournament.tournaments
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    game_mode VARCHAR(50) NOT NULL, -- deathmatch, team_deathmatch, capture_flag, etc.
    tournament_type VARCHAR(20) NOT NULL DEFAULT 'single_elimination' CHECK (tournament_type IN ('single_elimination', 'double_elimination', 'round_robin', 'swiss')),
    max_participants INTEGER NOT NULL CHECK (max_participants > 0),
    current_participants INTEGER NOT NULL DEFAULT 0 CHECK (current_participants >= 0),
    min_skill_level INTEGER DEFAULT 1,
    max_skill_level INTEGER DEFAULT 100,
    entry_fee INTEGER DEFAULT 0, -- in game currency
    prize_pool JSONB, -- distribution of rewards
    status VARCHAR(20) NOT NULL DEFAULT 'registration' CHECK (status IN ('registration', 'in_progress', 'completed', 'cancelled')),
    registration_start TIMESTAMP WITH TIME ZONE,
    registration_end TIMESTAMP WITH TIME ZONE,
    start_time TIMESTAMP WITH TIME ZONE,
    end_time TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rules JSONB, -- tournament-specific rules and settings
    metadata JSONB -- additional tournament configuration
);

-- Table: tournament.participants
CREATE TABLE IF NOT EXISTS tournament.participants
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    player_id VARCHAR(255) NOT NULL,
    player_name VARCHAR(100) NOT NULL,
    skill_rating INTEGER DEFAULT 1000,
    registration_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'registered' CHECK (status IN ('registered', 'confirmed', 'disqualified', 'withdrawn')),
    seed INTEGER, -- seeding for bracket placement
    division VARCHAR(50), -- for multi-division tournaments
    metadata JSONB,
    UNIQUE(tournament_id, player_id)
);

-- Table: tournament.brackets
CREATE TABLE IF NOT EXISTS tournament.brackets
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    bracket_name VARCHAR(100) NOT NULL, -- winners, losers, etc.
    round_number INTEGER NOT NULL,
    round_name VARCHAR(50), -- Round of 16, Quarterfinals, etc.
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'in_progress', 'completed')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB,
    UNIQUE(tournament_id, bracket_name, round_number)
);

-- Table: tournament.matches
CREATE TABLE IF NOT EXISTS tournament.matches
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    bracket_id UUID NOT NULL REFERENCES tournament.brackets(id) ON DELETE CASCADE,
    match_number INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'in_progress', 'completed', 'cancelled', 'forfeit')),
    scheduled_time TIMESTAMP WITH TIME ZONE,
    start_time TIMESTAMP WITH TIME ZONE,
    end_time TIMESTAMP WITH TIME ZONE,
    duration INTERVAL,
    winner_id UUID REFERENCES tournament.participants(id),
    winner_score INTEGER,
    loser_id UUID REFERENCES tournament.participants(id),
    loser_score INTEGER,
    map_name VARCHAR(100),
    game_mode VARCHAR(50),
    server_id VARCHAR(100), -- which game server hosted this match
    spectator_count INTEGER DEFAULT 0,
    replay_available BOOLEAN DEFAULT false,
    replay_url TEXT,
    statistics JSONB, -- detailed match statistics
    events JSONB, -- significant match events
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: tournament.match_participants
CREATE TABLE IF NOT EXISTS tournament.match_participants
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id UUID NOT NULL REFERENCES tournament.matches(id) ON DELETE CASCADE,
    participant_id UUID NOT NULL REFERENCES tournament.participants(id) ON DELETE CASCADE,
    team VARCHAR(20), -- 'home', 'away', or team identifier
    player_slot INTEGER, -- position in the match (0-15 for squads)
    status VARCHAR(20) NOT NULL DEFAULT 'confirmed' CHECK (status IN ('confirmed', 'ready', 'playing', 'disconnected', 'forfeit')),
    joined_at TIMESTAMP WITH TIME ZONE,
    left_at TIMESTAMP WITH TIME ZONE,
    score INTEGER DEFAULT 0,
    kills INTEGER DEFAULT 0,
    deaths INTEGER DEFAULT 0,
    assists INTEGER DEFAULT 0,
    statistics JSONB, -- detailed player statistics for this match
    UNIQUE(match_id, participant_id)
);

-- Table: tournament.spectators
CREATE TABLE IF NOT EXISTS tournament.spectators
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id UUID NOT NULL REFERENCES tournament.matches(id) ON DELETE CASCADE,
    spectator_id VARCHAR(255) NOT NULL, -- player ID of spectator
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    session_duration INTERVAL GENERATED ALWAYS AS (left_at - joined_at) STORED,
    metadata JSONB
);

-- Table: tournament.results
CREATE TABLE IF NOT EXISTS tournament.results
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    participant_id UUID NOT NULL REFERENCES tournament.participants(id) ON DELETE CASCADE,
    final_position INTEGER,
    total_score INTEGER DEFAULT 0,
    total_kills INTEGER DEFAULT 0,
    total_deaths INTEGER DEFAULT 0,
    total_assists INTEGER DEFAULT 0,
    matches_played INTEGER DEFAULT 0,
    matches_won INTEGER DEFAULT 0,
    matches_lost INTEGER DEFAULT 0,
    average_match_duration INTERVAL,
    skill_rating_change INTEGER DEFAULT 0,
    rewards JSONB, -- earned rewards and prizes
    achievements JSONB, -- tournament achievements unlocked
    statistics JSONB, -- comprehensive tournament statistics
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tournament_id, participant_id)
);

-- Indexes for performance (optimized for tournament queries)
CREATE INDEX IF NOT EXISTS idx_tournaments_status ON tournament.tournaments(status);
CREATE INDEX IF NOT EXISTS idx_tournaments_start_time ON tournament.tournaments(start_time);
CREATE INDEX IF NOT EXISTS idx_tournaments_game_mode ON tournament.tournaments(game_mode);
CREATE INDEX IF NOT EXISTS idx_participants_tournament ON tournament.participants(tournament_id);
CREATE INDEX IF NOT EXISTS idx_participants_player ON tournament.participants(player_id);
CREATE INDEX IF NOT EXISTS idx_participants_skill ON tournament.participants(skill_rating DESC);
CREATE INDEX IF NOT EXISTS idx_brackets_tournament ON tournament.brackets(tournament_id);
CREATE INDEX IF NOT EXISTS idx_matches_tournament ON tournament.matches(tournament_id);
CREATE INDEX IF NOT EXISTS idx_matches_status ON tournament.matches(status);
CREATE INDEX IF NOT EXISTS idx_matches_scheduled ON tournament.matches(scheduled_time);
CREATE INDEX IF NOT EXISTS idx_match_participants_match ON tournament.match_participants(match_id);
CREATE INDEX IF NOT EXISTS idx_match_participants_participant ON tournament.match_participants(participant_id);
CREATE INDEX IF NOT EXISTS idx_spectators_match ON tournament.spectators(match_id);
CREATE INDEX IF NOT EXISTS idx_results_tournament ON tournament.results(tournament_id);
CREATE INDEX IF NOT EXISTS idx_results_position ON tournament.results(final_position);

-- Partial indexes for active tournaments
CREATE INDEX IF NOT EXISTS idx_tournaments_active ON tournament.tournaments(start_time, end_time)
    WHERE status IN ('registration', 'in_progress');

CREATE INDEX IF NOT EXISTS idx_matches_active ON tournament.matches(tournament_id, scheduled_time)
    WHERE status IN ('scheduled', 'in_progress');

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_tournaments_prize_pool_gin ON tournament.tournaments USING GIN (prize_pool);
CREATE INDEX IF NOT EXISTS idx_tournaments_rules_gin ON tournament.tournaments USING GIN (rules);
CREATE INDEX IF NOT EXISTS idx_matches_statistics_gin ON tournament.matches USING GIN (statistics);
CREATE INDEX IF NOT EXISTS idx_match_participants_stats_gin ON tournament.match_participants USING GIN (statistics);
CREATE INDEX IF NOT EXISTS idx_results_rewards_gin ON tournament.results USING GIN (rewards);

-- Function to update updated_at timestamp for tournaments
CREATE OR REPLACE FUNCTION update_tournament_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to update match updated_at timestamp
CREATE OR REPLACE FUNCTION update_match_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers to automatically update updated_at
CREATE TRIGGER trigger_tournament_updated_at
    BEFORE UPDATE ON tournament.tournaments
    FOR EACH ROW EXECUTE FUNCTION update_tournament_updated_at();

CREATE TRIGGER trigger_match_updated_at
    BEFORE UPDATE ON tournament.matches
    FOR EACH ROW EXECUTE FUNCTION update_match_updated_at();

-- Comments for API documentation
COMMENT ON TABLE tournament.tournaments IS 'Tournament definitions and configurations';
COMMENT ON TABLE tournament.participants IS 'Tournament participant registrations and info';
COMMENT ON TABLE tournament.brackets IS 'Tournament bracket structure and rounds';
COMMENT ON TABLE tournament.matches IS 'Individual tournament matches and results';
COMMENT ON TABLE tournament.match_participants IS 'Players participating in specific matches';
COMMENT ON TABLE tournament.spectators IS 'Spectator sessions for tournament matches';
COMMENT ON TABLE tournament.results IS 'Final tournament results and statistics';

COMMENT ON COLUMN tournament.tournaments.tournament_type IS 'Type of tournament bracket system';
COMMENT ON COLUMN tournament.tournaments.prize_pool IS 'JSONB distribution of tournament prizes';
COMMENT ON COLUMN tournament.tournaments.rules IS 'JSONB tournament-specific rules and settings';
COMMENT ON COLUMN tournament.matches.statistics IS 'JSONB detailed match statistics and performance metrics';
COMMENT ON COLUMN tournament.results.rewards IS 'JSONB earned rewards, prizes, and achievements';

-- BACKEND NOTE: Tournament bracket system optimized for MMOFPS competitive gameplay
-- Expected performance: 1000+ concurrent tournaments, 10000+ active matches
-- Memory per tournament: ~50KB, per match: ~25KB
-- Cache strategy: Redis cache for active tournaments/matches, TTL 1h
-- Hot queries: Active tournaments, upcoming matches, live results

COMMIT;
