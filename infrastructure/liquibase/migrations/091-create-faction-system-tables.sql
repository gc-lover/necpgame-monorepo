-- Issue: #1442
-- liquibase formatted sql

-- changeset database-engineer:091-create-faction-system-tables

-- =====================================================
-- Faction System Tables
-- =====================================================

-- Table: factions
CREATE TABLE IF NOT EXISTS factions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  leader_clan_id UUID,
  ideology TEXT,
  description TEXT,
  name VARCHAR(100) NOT NULL UNIQUE,
  type VARCHAR(50) NOT NULL CHECK (type IN (
        'criminal_gang',
        'professional_guild',
        'political_movement',
        'corporate_alliance',
        'religious_sect',
        'scientific_org'
    )),
  status VARCHAR(50) NOT NULL DEFAULT 'active' CHECK (status IN (
        'active',
        'disbanded',
        'suspended'
    )),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_factions_type ON factions(type);
CREATE INDEX idx_factions_status ON factions(status);
CREATE INDEX idx_factions_leader_clan ON factions(leader_clan_id);

COMMENT ON TABLE factions IS 'Основная таблица фракций';
COMMENT ON COLUMN factions.type IS 'Тип фракции: криминал, гильдия, политика, корпорация, секта, научная';
COMMENT ON COLUMN factions.ideology IS 'Идеология и цели фракции';

-- Table: faction_hierarchy
CREATE TABLE IF NOT EXISTS faction_hierarchy (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  clan_id UUID NOT NULL,
  player_id UUID,
  appointed_by UUID,
  role VARCHAR(100) NOT NULL CHECK (role IN (
        'leader',
        'officer',
        'council_member',
        'member',
        'recruit'
    )),
  permissions JSONB NOT NULL DEFAULT '{}',
  appointed_at TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE(faction_id, clan_id)
);

CREATE INDEX idx_faction_hierarchy_faction ON faction_hierarchy(faction_id);
CREATE INDEX idx_faction_hierarchy_clan ON faction_hierarchy(clan_id);
CREATE INDEX idx_faction_hierarchy_player ON faction_hierarchy(player_id);
CREATE INDEX idx_faction_hierarchy_role ON faction_hierarchy(faction_id, role);

COMMENT ON TABLE faction_hierarchy IS 'Иерархия и роли внутри фракций';
COMMENT ON COLUMN faction_hierarchy.permissions IS 'JSON с правами: {"can_invite": true, "can_kick": false}';

-- Table: faction_niches
CREATE TABLE IF NOT EXISTS faction_niches (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  description TEXT,
  name VARCHAR(100) NOT NULL UNIQUE,
  type VARCHAR(50) NOT NULL CHECK (type IN (
        'territorial',
        'economic',
        'political',
        'criminal',
        'technological',
        'social'
    )),
  benefits JSONB NOT NULL DEFAULT '{}',
  requirements JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  max_controllers INT DEFAULT 1
);

CREATE INDEX idx_faction_niches_type ON faction_niches(type);

COMMENT ON TABLE faction_niches IS 'Каталог доступных ниш для фракций';
COMMENT ON COLUMN faction_niches.benefits IS 'JSON с преимуществами: {"resource_bonus": 0.2, "abilities": []}';
COMMENT ON COLUMN faction_niches.requirements IS 'JSON с требованиями: {"min_members": 10, "min_reputation": 100}';

-- Table: faction_niche_control
CREATE TABLE IF NOT EXISTS faction_niche_control (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  niche_id UUID NOT NULL REFERENCES faction_niches(id) ON DELETE CASCADE,
  contested_by UUID REFERENCES factions(id),
  controlled_since TIMESTAMP NOT NULL DEFAULT NOW(),
  control_strength FLOAT NOT NULL DEFAULT 1.0 CHECK (control_strength >= 0.0 AND control_strength <= 1.0),
  contested BOOLEAN NOT NULL DEFAULT false,
  UNIQUE(faction_id, niche_id)
);

CREATE INDEX idx_faction_niche_control_faction ON faction_niche_control(faction_id);
CREATE INDEX idx_faction_niche_control_niche ON faction_niche_control(niche_id);
CREATE INDEX idx_faction_niche_control_contested ON faction_niche_control(contested);

COMMENT ON TABLE faction_niche_control IS 'Контроль ниш фракциями';
COMMENT ON COLUMN faction_niche_control.control_strength IS 'Сила контроля: 0.0 (слабо) - 1.0 (полно)';

-- Table: faction_relations
CREATE TABLE IF NOT EXISTS faction_relations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  target_faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  relation_type VARCHAR(50) NOT NULL DEFAULT 'neutral' CHECK (relation_type IN (
        'allied',
        'neutral',
        'competitive',
        'hostile',
        'trading'
    )),
  treaty_data JSONB,
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  relation_value INT NOT NULL DEFAULT 0 CHECK (relation_value >= -100 AND relation_value <= 100),
  UNIQUE(faction_id, target_faction_id),
  CHECK (faction_id != target_faction_id)
);

CREATE INDEX idx_faction_relations_faction ON faction_relations(faction_id);
CREATE INDEX idx_faction_relations_target ON faction_relations(target_faction_id);
CREATE INDEX idx_faction_relations_type ON faction_relations(relation_type);

COMMENT ON TABLE faction_relations IS 'Отношения между фракциями';
COMMENT ON COLUMN faction_relations.relation_value IS 'Значение отношений: -100 (война) до +100 (союз)';
COMMENT ON COLUMN faction_relations.treaty_data IS 'JSON с данными договоров';

-- Table: faction_conflicts
CREATE TABLE IF NOT EXISTS faction_conflicts (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  attacker_faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  defender_faction_id UUID NOT NULL REFERENCES factions(id) ON DELETE CASCADE,
  winner_faction_id UUID REFERENCES factions(id),
  description TEXT,
  conflict_type VARCHAR(50) NOT NULL CHECK (conflict_type IN (
        'territorial',
        'economic',
        'political',
        'ideological',
        'resource'
    )),
  status VARCHAR(50) NOT NULL DEFAULT 'active' CHECK (status IN (
        'active',
        'resolved',
        'ongoing',
        'stalemate'
    )),
  resolution_type VARCHAR(50),
  stakes JSONB,
  started_at TIMESTAMP NOT NULL DEFAULT NOW(),
  ended_at TIMESTAMP,
  CHECK (attacker_faction_id != defender_faction_id)
);

CREATE INDEX idx_faction_conflicts_attacker ON faction_conflicts(attacker_faction_id);
CREATE INDEX idx_faction_conflicts_defender ON faction_conflicts(defender_faction_id);
CREATE INDEX idx_faction_conflicts_status ON faction_conflicts(status);
CREATE INDEX idx_faction_conflicts_type ON faction_conflicts(conflict_type);

COMMENT ON TABLE faction_conflicts IS 'История конфликтов между фракциями';
COMMENT ON COLUMN faction_conflicts.stakes IS 'JSON с ставками конфликта: {"territory": "zone_id", "resources": []}';

-- Table: faction_conflict_battles
CREATE TABLE IF NOT EXISTS faction_conflict_battles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  conflict_id UUID NOT NULL REFERENCES faction_conflicts(id) ON DELETE CASCADE,
  battle_data JSONB,
  battle_date TIMESTAMP NOT NULL DEFAULT NOW(),
  attacker_score INT NOT NULL DEFAULT 0,
  defender_score INT NOT NULL DEFAULT 0,
  participants_count INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_faction_conflict_battles_conflict ON faction_conflict_battles(conflict_id);

COMMENT ON TABLE faction_conflict_battles IS 'Отдельные битвы в рамках конфликта';

-- Seed initial niches
INSERT INTO faction_niches (id, name, type, description, benefits, requirements, max_controllers) VALUES
(gen_random_uuid(), 'Urban Territory Control', 'territorial', 'Control over urban zones', '{"territory_bonus": 0.2}', '{"min_members": 20}', 3),
(gen_random_uuid(), 'Trade Routes Dominance', 'economic', 'Control over trade routes', '{"trade_bonus": 0.15}', '{"min_reputation": 50}', 5),
(gen_random_uuid(), 'Political Influence', 'political', 'Influence on regional politics', '{"vote_weight": 1.5}', '{"min_members": 50}', 2),
(gen_random_uuid(), 'Black Market Network', 'criminal', 'Access to black market', '{"illegal_trade_bonus": 0.25}', '{"min_reputation": -50}', 3),
(gen_random_uuid(), 'Tech Innovation Hub', 'technological', 'Advanced technology access', '{"tech_bonus": 0.3}', '{"min_members": 15}', 2),
(gen_random_uuid(), 'Social Movement', 'social', 'Influence on public opinion', '{"reputation_gain": 0.2}', '{"min_members": 100}', 4)
ON CONFLICT DO NOTHING;

-- rollback DROP TABLE IF EXISTS faction_conflict_battles CASCADE;
-- rollback DROP TABLE IF EXISTS faction_conflicts CASCADE;
-- rollback DROP TABLE IF EXISTS faction_relations CASCADE;
-- rollback DROP TABLE IF EXISTS faction_niche_control CASCADE;
-- rollback DROP TABLE IF EXISTS faction_niches CASCADE;
-- rollback DROP TABLE IF EXISTS faction_hierarchy CASCADE;
-- rollback DROP TABLE IF EXISTS factions CASCADE;































