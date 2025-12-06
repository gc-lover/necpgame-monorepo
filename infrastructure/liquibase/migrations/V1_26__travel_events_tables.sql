-- Migration: Travel Events System Tables
-- Description: Creates tables for travel events system including events catalog, instances, cooldowns, skill checks, rewards, penalties, and telemetry

CREATE TABLE IF NOT EXISTS travel_events (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  description TEXT,
  event_code VARCHAR(50) UNIQUE NOT NULL,
  event_name VARCHAR(255) NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  epoch_id VARCHAR(50) NOT NULL,
  zone_types JSONB,
  skill_checks JSONB,
  rewards JSONB,
  penalties JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  base_probability DECIMAL(5,4) NOT NULL DEFAULT 0.15,
  cooldown_hours INTEGER NOT NULL DEFAULT 6
);

CREATE INDEX idx_travel_events_event_type ON travel_events(event_type);
CREATE INDEX idx_travel_events_epoch_id ON travel_events(epoch_id);
CREATE INDEX idx_travel_events_event_code ON travel_events(event_code);

CREATE TABLE IF NOT EXISTS travel_event_zones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES travel_events(id) ON DELETE CASCADE,
    zone_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(event_id, zone_id)
);

CREATE INDEX idx_travel_event_zones_event_id ON travel_event_zones(event_id);
CREATE INDEX idx_travel_event_zones_zone_id ON travel_event_zones(zone_id);

CREATE TABLE IF NOT EXISTS travel_event_instances (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_id UUID NOT NULL REFERENCES travel_events(id) ON DELETE CASCADE,
  character_id UUID NOT NULL,
  zone_id UUID NOT NULL,
  epoch_id VARCHAR(50) NOT NULL,
  state VARCHAR(20) NOT NULL DEFAULT 'triggered',
  skill_check_results JSONB,
  rewards_distributed JSONB,
  penalties_applied JSONB,
  started_at TIMESTAMP NOT NULL DEFAULT NOW(),
  completed_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  CHECK (state IN ('triggered', 'started', 'in-progress', 'completed', 'cancelled', 'failed'))
);

CREATE INDEX idx_travel_event_instances_event_id ON travel_event_instances(event_id);
CREATE INDEX idx_travel_event_instances_character_id ON travel_event_instances(character_id);
CREATE INDEX idx_travel_event_instances_zone_id ON travel_event_instances(zone_id);
CREATE INDEX idx_travel_event_instances_state ON travel_event_instances(state);
CREATE INDEX idx_travel_event_instances_character_event ON travel_event_instances(character_id, event_id);

CREATE TABLE IF NOT EXISTS travel_event_cooldowns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    last_triggered_at TIMESTAMP NOT NULL,
    cooldown_until TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(character_id, event_type)
);

CREATE INDEX idx_travel_event_cooldowns_character_id ON travel_event_cooldowns(character_id);
CREATE INDEX idx_travel_event_cooldowns_event_type ON travel_event_cooldowns(event_type);
CREATE INDEX idx_travel_event_cooldowns_cooldown_until ON travel_event_cooldowns(cooldown_until);

CREATE TABLE IF NOT EXISTS travel_event_skill_checks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_instance_id UUID NOT NULL REFERENCES travel_event_instances(id) ON DELETE CASCADE,
  skill_name VARCHAR(50) NOT NULL,
  modifiers JSONB,
  performed_at TIMESTAMP NOT NULL DEFAULT NOW(),
  dc INTEGER NOT NULL,
  roll_result INTEGER NOT NULL,
  success BOOLEAN NOT NULL,
  critical_success BOOLEAN NOT NULL DEFAULT FALSE,
  critical_failure BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_travel_event_skill_checks_instance_id ON travel_event_skill_checks(event_instance_id);
CREATE INDEX idx_travel_event_skill_checks_skill_name ON travel_event_skill_checks(skill_name);

CREATE TABLE IF NOT EXISTS travel_event_rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_instance_id UUID NOT NULL REFERENCES travel_event_instances(id) ON DELETE CASCADE,
    character_id UUID NOT NULL,
    reward_type VARCHAR(20) NOT NULL,
    reward_data JSONB NOT NULL,
    distributed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (reward_type IN ('loot', 'reputation', 'eddies', 'item'))
);

CREATE INDEX idx_travel_event_rewards_instance_id ON travel_event_rewards(event_instance_id);
CREATE INDEX idx_travel_event_rewards_character_id ON travel_event_rewards(character_id);
CREATE INDEX idx_travel_event_rewards_reward_type ON travel_event_rewards(reward_type);

CREATE TABLE IF NOT EXISTS travel_event_penalties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_instance_id UUID NOT NULL REFERENCES travel_event_instances(id) ON DELETE CASCADE,
    character_id UUID NOT NULL,
    penalty_type VARCHAR(20) NOT NULL,
    penalty_data JSONB NOT NULL,
    applied_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (penalty_type IN ('damage', 'heat', 'reputation', 'confiscation'))
);

CREATE INDEX idx_travel_event_penalties_instance_id ON travel_event_penalties(event_instance_id);
CREATE INDEX idx_travel_event_penalties_character_id ON travel_event_penalties(character_id);
CREATE INDEX idx_travel_event_penalties_penalty_type ON travel_event_penalties(penalty_type);

CREATE TABLE IF NOT EXISTS travel_event_telemetry (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_instance_id UUID REFERENCES travel_event_instances(id) ON DELETE SET NULL,
  character_id UUID NOT NULL,
  zone_id UUID NOT NULL,
  event_type VARCHAR(50) NOT NULL,
  epoch_id VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  rewards_value DECIMAL(10,2),
  penalties_value DECIMAL(10,2),
  duration_seconds INTEGER,
  skill_checks_count INTEGER DEFAULT 0,
  success BOOLEAN
);

CREATE INDEX idx_travel_event_telemetry_instance_id ON travel_event_telemetry(event_instance_id);
CREATE INDEX idx_travel_event_telemetry_character_id ON travel_event_telemetry(character_id);
CREATE INDEX idx_travel_event_telemetry_event_type ON travel_event_telemetry(event_type);
CREATE INDEX idx_travel_event_telemetry_created_at ON travel_event_telemetry(created_at);

CREATE OR REPLACE FUNCTION update_travel_events_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER travel_events_updated_at
    BEFORE UPDATE ON travel_events
    FOR EACH ROW
    EXECUTE FUNCTION update_travel_events_updated_at();

CREATE TRIGGER travel_event_instances_updated_at
    BEFORE UPDATE ON travel_event_instances
    FOR EACH ROW
    EXECUTE FUNCTION update_travel_events_updated_at();

CREATE TRIGGER travel_event_cooldowns_updated_at
    BEFORE UPDATE ON travel_event_cooldowns
    FOR EACH ROW
    EXECUTE FUNCTION update_travel_events_updated_at();






































