-- Issue: #138
-- liquibase formatted sql

-- changeset database-engineer:092-create-achievement-system-tables

-- =====================================================
-- Achievement System Tables
-- =====================================================

-- Table: achievement_definitions
CREATE TABLE IF NOT EXISTS achievement_definitions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title_id UUID,
  description TEXT NOT NULL,
  name VARCHAR(200) NOT NULL,
  category VARCHAR(50) NOT NULL CHECK (category IN (
        'combat',
        'economy',
        'social',
        'exploration',
        'quests',
        'pvp',
        'pve',
        'special'
    )),
  type VARCHAR(20) NOT NULL CHECK (type IN (
        'standard',
        'progressive',
        'repeatable'
    )),
  icon_url VARCHAR(500),
  reward_currency VARCHAR(50),
  reward_items JSONB DEFAULT '[]',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  required_count INTEGER DEFAULT 1,
  reward_amount INTEGER DEFAULT 0,
  hidden BOOLEAN DEFAULT false,
  CONSTRAINT achievement_definitions_name_unique UNIQUE (name)
);

CREATE INDEX idx_achievement_definitions_category ON achievement_definitions(category);
CREATE INDEX idx_achievement_definitions_type ON achievement_definitions(type);
CREATE INDEX idx_achievement_definitions_hidden ON achievement_definitions(hidden);

-- Table: player_achievement_progress
CREATE TABLE IF NOT EXISTS player_achievement_progress (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL,
  achievement_id UUID NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
  unlocked_at TIMESTAMP,
  claimed_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  current_count INTEGER DEFAULT 0,
  CONSTRAINT player_achievement_progress_unique UNIQUE (player_id, achievement_id)
);

CREATE INDEX idx_player_achievement_progress_player ON player_achievement_progress(player_id);
CREATE INDEX idx_player_achievement_progress_achievement ON player_achievement_progress(achievement_id);
CREATE INDEX idx_player_achievement_progress_unlocked ON player_achievement_progress(unlocked_at) WHERE unlocked_at IS NOT NULL;

-- Table: player_titles
CREATE TABLE IF NOT EXISTS player_titles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL,
  achievement_id UUID REFERENCES achievement_definitions(id) ON DELETE SET NULL,
  description TEXT,
  title VARCHAR(100) NOT NULL,
  unlocked_at TIMESTAMP NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  is_active BOOLEAN DEFAULT false
);

CREATE INDEX idx_player_titles_player ON player_titles(player_id);
CREATE INDEX idx_player_titles_active ON player_titles(player_id, is_active) WHERE is_active = true;
CREATE INDEX idx_player_titles_achievement ON player_titles(achievement_id);

-- =====================================================
-- Seed Data: Initial Achievements
-- =====================================================

-- Combat achievements
INSERT INTO achievement_definitions (id, name, description, category, type, required_count, reward_currency, reward_amount)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'First Blood', 'Defeat your first enemy', 'combat', 'standard', 1, 'credits', 100),
    ('550e8400-e29b-41d4-a716-446655440002', 'Sharpshooter', 'Get 100 headshots', 'combat', 'progressive', 100, 'credits', 500),
    ('550e8400-e29b-41d4-a716-446655440003', 'Killer', 'Defeat 1000 enemies', 'combat', 'progressive', 1000, 'credits', 1000);

-- Economy achievements
INSERT INTO achievement_definitions (id, name, description, category, type, required_count, reward_currency, reward_amount)
VALUES
    ('550e8400-e29b-41d4-a716-446655440011', 'First Trade', 'Complete your first trade', 'economy', 'standard', 1, 'credits', 50),
    ('550e8400-e29b-41d4-a716-446655440012', 'Rich', 'Earn 1,000,000 credits', 'economy', 'progressive', 1000000, 'premium', 10);

-- Social achievements
INSERT INTO achievement_definitions (id, name, description, category, type, required_count, reward_currency, reward_amount)
VALUES
    ('550e8400-e29b-41d4-a716-446655440021', 'Social Butterfly', 'Join a clan', 'social', 'standard', 1, 'credits', 100),
    ('550e8400-e29b-41d4-a716-446655440022', 'Mentor', 'Help 10 new players', 'social', 'progressive', 10, 'credits', 500);

-- Exploration achievements
INSERT INTO achievement_definitions (id, name, description, category, type, required_count, reward_currency, reward_amount)
VALUES
    ('550e8400-e29b-41d4-a716-446655440031', 'Explorer', 'Discover 10 locations', 'exploration', 'progressive', 10, 'credits', 200),
    ('550e8400-e29b-41d4-a716-446655440032', 'World Traveler', 'Visit all continents', 'exploration', 'standard', 7, 'credits', 1000);

















