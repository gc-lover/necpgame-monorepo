CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS mvp_meta.outbox (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  aggregate_type VARCHAR(100) NOT NULL,
  aggregate_id VARCHAR(100) NOT NULL,
  event_type VARCHAR(100) NOT NULL,
  payload JSONB NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_meta.event_log (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  source VARCHAR(100) NOT NULL,
  level VARCHAR(20) NOT NULL,
  message TEXT NOT NULL,
  payload JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reference tables
CREATE TABLE IF NOT EXISTS mvp_core.ref_origin (
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS mvp_core.ref_class (
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS mvp_core.ref_faction (
  code VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

-- Accounts and characters
CREATE TABLE IF NOT EXISTS mvp_core.player_account (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  external_id VARCHAR(100) UNIQUE,
  nickname VARCHAR(50) UNIQUE NOT NULL,
  origin_code VARCHAR(50) REFERENCES mvp_core.ref_origin(code),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.character (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  account_id UUID NOT NULL REFERENCES mvp_core.player_account(id),
  name VARCHAR(60) NOT NULL,
  class_code VARCHAR(50) REFERENCES mvp_core.ref_class(code),
  faction_code VARCHAR(50) REFERENCES mvp_core.ref_faction(code),
  level INT DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

-- Weapons and ballistics
CREATE TABLE IF NOT EXISTS mvp_core.weapon_profile (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID REFERENCES mvp_core.character(id),
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50),
  stats JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.ballistics_metric (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  weapon_id UUID NOT NULL REFERENCES mvp_core.weapon_profile(id) ON DELETE CASCADE,
  metrics JSONB NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders (contracts)
CREATE TABLE IF NOT EXISTS mvp_core."order" (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  title VARCHAR(120) NOT NULL,
  owner_account_id UUID REFERENCES mvp_core.player_account(id),
  state VARCHAR(30) DEFAULT 'open',
  access VARCHAR(30) DEFAULT 'public',
  payload JSONB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_order_title_owner
  ON mvp_core."order"(title, owner_account_id);

CREATE TABLE IF NOT EXISTS mvp_core.order_phase (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID NOT NULL REFERENCES mvp_core."order"(id) ON DELETE CASCADE,
  phase_no INT NOT NULL,
  name VARCHAR(120),
  status VARCHAR(30) DEFAULT 'planned',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.order_application (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID NOT NULL REFERENCES mvp_core."order"(id) ON DELETE CASCADE,
  applicant_account_id UUID NOT NULL REFERENCES mvp_core.player_account(id),
  status VARCHAR(30) DEFAULT 'pending',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.order_review (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID NOT NULL REFERENCES mvp_core."order"(id) ON DELETE CASCADE,
  reviewer_account_id UUID NOT NULL REFERENCES mvp_core.player_account(id),
  rating INT CHECK (rating BETWEEN 1 AND 5),
  comment TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crafting
CREATE TABLE IF NOT EXISTS mvp_core.crafting_blueprint (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  code VARCHAR(100) UNIQUE NOT NULL,
  name VARCHAR(150),
  inputs JSONB NOT NULL,
  output JSONB NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS mvp_core.crafting_job (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  account_id UUID REFERENCES mvp_core.player_account(id),
  blueprint_id UUID REFERENCES mvp_core.crafting_blueprint(id),
  status VARCHAR(30) DEFAULT 'queued',
  started_at TIMESTAMP,
  finished_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- World districts
CREATE TABLE IF NOT EXISTS mvp_core.world_district_state (
  code VARCHAR(50) PRIMARY KEY,
  unrest INT DEFAULT 0,
  modifiers JSONB,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


