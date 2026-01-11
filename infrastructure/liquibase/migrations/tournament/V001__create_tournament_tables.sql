-- Issue: #2284 - Tournament Service Database Schema
-- liquibase formatted sql

-- changeset gc-lover:1
CREATE SCHEMA IF NOT EXISTS tournament;

-- changeset gc-lover:2
CREATE TABLE tournament.tournaments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    tournament_type VARCHAR(50) NOT NULL, -- single_elimination, double_elimination, round_robin, swiss
    status VARCHAR(50) NOT NULL DEFAULT 'draft', -- draft, active, completed, cancelled
    max_players INTEGER NOT NULL DEFAULT 16,
    current_round INTEGER DEFAULT 0,
    prize_pool DECIMAL(18,4) DEFAULT 0.0,
    entry_fee DECIMAL(18,4) DEFAULT 0.0,
    start_time TIMESTAMP WITH TIME ZONE,
    end_time TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    rules JSONB, -- Tournament-specific rules and settings
    metadata JSONB -- Additional tournament metadata
);

-- changeset gc-lover:3
CREATE INDEX idx_tournaments_status ON tournament.tournaments (status);
CREATE INDEX idx_tournaments_type ON tournament.tournaments (tournament_type);
CREATE INDEX idx_tournaments_start_time ON tournament.tournaments (start_time);
CREATE INDEX idx_tournaments_end_time ON tournament.tournaments (end_time);

-- changeset gc-lover:4
CREATE TABLE tournament.participants (
    user_id UUID NOT NULL,
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    seed INTEGER,
    status VARCHAR(50) NOT NULL DEFAULT 'registered', -- registered, active, eliminated, disqualified
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    eliminated_at TIMESTAMP WITH TIME ZONE,
    final_rank INTEGER,
    total_score INTEGER DEFAULT 0,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    draws INTEGER DEFAULT 0,
    average_score DECIMAL(8,2) DEFAULT 0.0,
    PRIMARY KEY (user_id, tournament_id)
);

-- changeset gc-lover:5
CREATE INDEX idx_participants_tournament_id ON tournament.participants (tournament_id);
CREATE INDEX idx_participants_user_id ON tournament.participants (user_id);
CREATE INDEX idx_participants_status ON tournament.participants (status);

-- changeset gc-lover:6
CREATE TABLE tournament.matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    round INTEGER NOT NULL,
    position INTEGER NOT NULL, -- Position in the bracket
    player1_id UUID REFERENCES tournament.participants(user_id) ON DELETE SET NULL,
    player2_id UUID REFERENCES tournament.participants(user_id) ON DELETE SET NULL,
    winner_id UUID REFERENCES tournament.participants(user_id) ON DELETE SET NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, in_progress, completed, cancelled
    start_time TIMESTAMP WITH TIME ZONE,
    end_time TIMESTAMP WITH TIME ZONE,
    score1 INTEGER DEFAULT 0,
    score2 INTEGER DEFAULT 0,
    next_match_id UUID REFERENCES tournament.matches(id) ON DELETE SET NULL,
    bracket_position JSONB, -- Position in bracket visualization
    match_data JSONB -- Additional match-specific data
);

-- changeset gc-lover:7
CREATE INDEX idx_matches_tournament_id ON tournament.matches (tournament_id);
CREATE INDEX idx_matches_round ON tournament.matches (round);
CREATE INDEX idx_matches_status ON tournament.matches (status);
CREATE INDEX idx_matches_player1 ON tournament.matches (player1_id);
CREATE INDEX idx_matches_player2 ON tournament.matches (player2_id);
CREATE INDEX idx_matches_winner ON tournament.matches (winner_id);

-- changeset gc-lover:8
CREATE TABLE tournament.leaderboard_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    user_id UUID NOT NULL,
    rank INTEGER NOT NULL,
    score INTEGER NOT NULL DEFAULT 0,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    win_rate DECIMAL(5,2) DEFAULT 0.0,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(tournament_id, user_id)
);

-- changeset gc-lover:9
CREATE INDEX idx_leaderboard_tournament_id ON tournament.leaderboard_entries (tournament_id);
CREATE INDEX idx_leaderboard_rank ON tournament.leaderboard_entries (rank);
CREATE INDEX idx_leaderboard_user_id ON tournament.leaderboard_entries (user_id);

-- changeset gc-lover:10
CREATE TABLE tournament.spectators (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES tournament.tournaments(id) ON DELETE CASCADE,
    user_id UUID NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    left_at TIMESTAMP WITH TIME ZONE,
    UNIQUE(tournament_id, user_id)
);

-- changeset gc-lover:11
CREATE INDEX idx_spectators_tournament_id ON tournament.spectators (tournament_id);
CREATE INDEX idx_spectators_user_id ON tournament.spectators (user_id);