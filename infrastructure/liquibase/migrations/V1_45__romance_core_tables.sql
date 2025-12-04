--liquibase formatted sql

--changeset necpgame:V1_45_romance_core_tables
--comment: Create tables for romance core system (Issue: #140876112)

CREATE TABLE IF NOT EXISTS social.romance_relationships (
  relationship_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL,
  target_id UUID NOT NULL,
  flags TEXT[] DEFAULT '{}',
  romance_type VARCHAR(20) NOT NULL CHECK (romance_type IN ('player_player', 'player_npc', 'player_digital_avatar')),
  relationship_stage VARCHAR(30) NOT NULL DEFAULT 'stranger' CHECK (relationship_stage IN ('stranger', 'acquaintance', 'friend', 'close_friend', 'romantic_interest', 'dating', 'in_relationship', 'engaged', 'married')),
  consent_status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (consent_status IN ('pending', 'accepted', 'rejected', 'revoked')),
  metadata JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  relationship_score INTEGER NOT NULL DEFAULT 0 CHECK (relationship_score >= -100 AND relationship_score <= 100),
  chemistry_score INTEGER NOT NULL DEFAULT 0 CHECK (chemistry_score >= 0 AND chemistry_score <= 100),
  trust_score INTEGER NOT NULL DEFAULT 0 CHECK (trust_score >= 0 AND trust_score <= 100),
  physical_intimacy INTEGER NOT NULL DEFAULT 0 CHECK (physical_intimacy >= 0 AND physical_intimacy <= 100),
  emotional_intimacy INTEGER NOT NULL DEFAULT 0 CHECK (emotional_intimacy >= 0 AND emotional_intimacy <= 100),
  relationship_health INTEGER NOT NULL DEFAULT 100 CHECK (relationship_health >= 0 AND relationship_health <= 100),
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  is_romantic BOOLEAN NOT NULL DEFAULT FALSE,
  is_public BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT unique_romance_relationship UNIQUE (player_id, target_id, romance_type)
);

CREATE INDEX IF NOT EXISTS idx_romance_relationships_player ON social.romance_relationships(player_id, romance_type);
CREATE INDEX IF NOT EXISTS idx_romance_relationships_target ON social.romance_relationships(target_id, romance_type);
CREATE INDEX IF NOT EXISTS idx_romance_relationships_active ON social.romance_relationships(is_active, is_romantic);
CREATE INDEX IF NOT EXISTS idx_romance_relationships_type ON social.romance_relationships(romance_type);
CREATE INDEX IF NOT EXISTS idx_romance_relationships_stage ON social.romance_relationships(relationship_stage);

CREATE TABLE IF NOT EXISTS social.player_player_romance_profiles (
  profile_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL UNIQUE,
  relationship_status VARCHAR(20) NOT NULL DEFAULT 'single' CHECK (relationship_status IN ('single', 'dating', 'in_relationship', 'engaged', 'married')),
  romance_preferences JSONB DEFAULT '{}',
  privacy_settings JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  max_concurrent_romances INTEGER NOT NULL DEFAULT 1 CHECK (max_concurrent_romances >= 1),
  polyamory_allowed BOOLEAN NOT NULL DEFAULT FALSE,
  age_verification BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_player_player_romance_profiles_player_id ON social.player_player_romance_profiles(player_id);
CREATE INDEX IF NOT EXISTS idx_player_player_romance_profiles_status ON social.player_player_romance_profiles(relationship_status);

CREATE TABLE IF NOT EXISTS social.romance_privacy_settings (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL,
  block_list UUID[] DEFAULT '{}',
  romance_type VARCHAR(20) NOT NULL CHECK (romance_type IN ('player_player', 'player_npc', 'player_digital_avatar')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  show_relationship_status BOOLEAN NOT NULL DEFAULT TRUE,
  show_romance_events BOOLEAN NOT NULL DEFAULT TRUE,
  allow_romance_requests BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT unique_romance_privacy UNIQUE (player_id, romance_type)
);

CREATE INDEX IF NOT EXISTS idx_romance_privacy_settings_player_id ON social.romance_privacy_settings(player_id);
CREATE INDEX IF NOT EXISTS idx_romance_privacy_settings_type ON social.romance_privacy_settings(romance_type);

CREATE TABLE IF NOT EXISTS social.romance_notifications (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  player_id UUID NOT NULL,
  relationship_id UUID,
  message TEXT NOT NULL,
  notification_type VARCHAR(50) NOT NULL,
  metadata JSONB DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_read BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_romance_notifications_player_id ON social.romance_notifications(player_id);
CREATE INDEX IF NOT EXISTS idx_romance_notifications_read ON social.romance_notifications(player_id, is_read);
CREATE INDEX IF NOT EXISTS idx_romance_notifications_created_at ON social.romance_notifications(created_at DESC);

-- Trigger for updating updated_at
CREATE OR REPLACE FUNCTION social.update_romance_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_romance_relationships_updated_at
    BEFORE UPDATE ON social.romance_relationships
    FOR EACH ROW
    EXECUTE FUNCTION social.update_romance_updated_at();

CREATE TRIGGER trigger_player_player_romance_profiles_updated_at
    BEFORE UPDATE ON social.player_player_romance_profiles
    FOR EACH ROW
    EXECUTE FUNCTION social.update_romance_updated_at();

CREATE TRIGGER trigger_romance_privacy_settings_updated_at
    BEFORE UPDATE ON social.romance_privacy_settings
    FOR EACH ROW
    EXECUTE FUNCTION social.update_romance_updated_at();

