-- Issue: #2247
-- liquibase formatted sql

--changeset backend:guild-system-tables dbms:postgresql
--comment: Create guild system tables with optimized indexes for MMOFPS performance

BEGIN;

-- Create guilds table with optimized structure
CREATE TABLE IF NOT EXISTS social.guilds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL CHECK (length(name) >= 3 AND length(name) <= 100),
    description TEXT CHECK (length(description) <= 1000),
    leader_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    member_count INTEGER DEFAULT 1 CHECK (member_count >= 1 AND member_count <= 100),
    max_members INTEGER DEFAULT 50 CHECK (max_members >= 10 AND max_members <= 100),
    level INTEGER DEFAULT 1 CHECK (level >= 1 AND level <= 100),
    experience BIGINT DEFAULT 0 CHECK (experience >= 0),
    reputation INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Create guild_members table
CREATE TABLE IF NOT EXISTS social.guild_members (
    user_id UUID NOT NULL,
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL DEFAULT 'member' CHECK (role IN ('leader', 'officer', 'member')),
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (user_id, guild_id)
);

-- Create guild_announcements table
CREATE TABLE IF NOT EXISTS social.guild_announcements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL CHECK (length(title) >= 1 AND length(title) <= 200),
    content TEXT NOT NULL CHECK (length(content) >= 1 AND length(content) <= 5000),
    author_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_pinned BOOLEAN DEFAULT false
);

-- Create indexes for performance (MMOFPS optimization)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guilds_leader_id ON social.guilds(leader_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guilds_level ON social.guilds(level DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guilds_reputation ON social.guilds(reputation DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guilds_active ON social.guilds(is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guild_members_guild_id ON social.guild_members(guild_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guild_members_user_id ON social.guild_members(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guild_announcements_guild_id ON social.guild_announcements(guild_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_guild_announcements_pinned ON social.guild_announcements(is_pinned) WHERE is_pinned = true;

-- Add updated_at trigger function
CREATE OR REPLACE FUNCTION social.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Add triggers for updated_at
CREATE TRIGGER update_guilds_updated_at BEFORE UPDATE ON social.guilds
    FOR EACH ROW EXECUTE FUNCTION social.update_updated_at_column();

CREATE TRIGGER update_guild_announcements_updated_at BEFORE UPDATE ON social.guild_announcements
    FOR EACH ROW EXECUTE FUNCTION social.update_updated_at_column();

-- Insert some test data for development
INSERT INTO social.guilds (id, name, description, leader_id, member_count, level, experience, reputation) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Warriors of Light', 'Elite guild for skilled players', '660e8400-e29b-41d4-a716-446655440000', 25, 15, 50000, 1200),
('550e8400-e29b-41d4-a716-446655440002', 'Shadow Hunters', 'Stealth and assassination specialists', '660e8400-e29b-41d4-a716-446655440001', 18, 12, 35000, 950),
('550e8400-e29b-41d4-a716-446655440003', 'Cyber Knights', 'Heavy combat and cyberware experts', '660e8400-e29b-41d4-a716-446655440002', 32, 20, 75000, 1800)
ON CONFLICT (id) DO NOTHING;

-- Insert test members
INSERT INTO social.guild_members (user_id, guild_id, role, joined_at) VALUES
('660e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', 'leader', NOW() - INTERVAL '30 days'),
('660e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', 'officer', NOW() - INTERVAL '20 days'),
('660e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440001', 'member', NOW() - INTERVAL '10 days'),
('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', 'leader', NOW() - INTERVAL '25 days'),
('660e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440002', 'member', NOW() - INTERVAL '15 days'),
('660e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 'leader', NOW() - INTERVAL '35 days'),
('660e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440003', 'officer', NOW() - INTERVAL '28 days'),
('660e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440003', 'member', NOW() - INTERVAL '5 days')
ON CONFLICT (user_id, guild_id) DO NOTHING;

COMMIT;

