-- Issue: #140875791
-- Social System - Relationships, NPC Hiring, Player Orders Enhancement
-- Создание таблиц для социальной системы:
-- - Отношения с NPC (npc_relationships)
-- - Найм NPC (npc_hiring_contracts, npc_hiring_tasks, npc_hiring_performance, npc_hiring_economy, npc_hiring_limits, npc_hiring_chains, npc_hiring_groups, npc_hiring_automation)
-- - Отношения между игроками (player_relationships, trust_levels, trust_contracts, alliances, alliance_members, character_ratings, social_capital, interaction_history, relationship_arbitration)
-- - Дополнение player_orders (multi_executor_orders, order_auctions, auction_bids, order_options, order_arbitration, order_insurance, order_ratings, order_reputation, order_economy, order_telemetry)

-- Создание схемы social, если её нет
CREATE SCHEMA IF NOT EXISTS social;

-- Таблица отношений с NPC
CREATE TABLE IF NOT EXISTS social.npc_relationships (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  npc_id UUID NOT NULL,
  relationship_type VARCHAR(100),
  relationship_data JSONB,
  last_interaction TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reputation_value INTEGER NOT NULL DEFAULT 0 CHECK (reputation_value >= -100 AND reputation_value <= 100),
  trust_level INTEGER NOT NULL DEFAULT 0 CHECK (trust_level >= 0 AND trust_level <= 100),
  interaction_count INTEGER NOT NULL DEFAULT 0,
  UNIQUE(character_id, npc_id)
);

-- Индексы для npc_relationships
CREATE INDEX IF NOT EXISTS idx_npc_relationships_character_id ON social.npc_relationships(character_id);
CREATE INDEX IF NOT EXISTS idx_npc_relationships_npc_id ON social.npc_relationships(npc_id);
CREATE INDEX IF NOT EXISTS idx_npc_relationships_reputation ON social.npc_relationships(character_id, reputation_value);

-- Таблица контрактов найма NPC
CREATE TABLE IF NOT EXISTS social.npc_hiring_contracts (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  npc_id UUID NOT NULL,
  hiring_type VARCHAR(50) NOT NULL,
  contract_type VARCHAR(20) NOT NULL CHECK (contract_type IN ('salary', 'one-time', 'profit-share', 'combined')),
  status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'terminated', 'completed')),
  autonomy_level VARCHAR(20) NOT NULL DEFAULT 'direct' CHECK (autonomy_level IN ('direct', 'autonomous', 'hybrid')),
  terms JSONB,
  start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_date TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  salary_amount DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK (salary_amount >= 0)
);

-- Индексы для npc_hiring_contracts
CREATE INDEX IF NOT EXISTS idx_npc_hiring_contracts_character_id ON social.npc_hiring_contracts(character_id, status);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_contracts_npc_id ON social.npc_hiring_contracts(npc_id, status);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_contracts_status ON social.npc_hiring_contracts(status, end_date) WHERE status = 'active';

-- Таблица задач нанятых NPC
CREATE TABLE IF NOT EXISTS social.npc_hiring_tasks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  contract_id UUID NOT NULL REFERENCES social.npc_hiring_contracts(id) ON DELETE CASCADE,
  npc_id UUID NOT NULL,
  task_description TEXT,
  task_type VARCHAR(50) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'in-progress', 'completed', 'failed')),
  resources JSONB,
  assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  started_at TIMESTAMP,
  completed_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  priority INTEGER NOT NULL DEFAULT 0
);

-- Индексы для npc_hiring_tasks
CREATE INDEX IF NOT EXISTS idx_npc_hiring_tasks_contract_id ON social.npc_hiring_tasks(contract_id, status);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_tasks_npc_id ON social.npc_hiring_tasks(npc_id, status);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_tasks_status ON social.npc_hiring_tasks(status, priority DESC);

-- Таблица эффективности нанятых NPC
CREATE TABLE IF NOT EXISTS social.npc_hiring_performance (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  contract_id UUID NOT NULL REFERENCES social.npc_hiring_contracts(id) ON DELETE CASCADE,
  npc_id UUID NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  efficiency_score DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (efficiency_score >= 0 AND efficiency_score <= 100),
  loyalty_score DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (loyalty_score >= 0 AND loyalty_score <= 100),
  skill_level INTEGER NOT NULL DEFAULT 0 CHECK (skill_level >= 0 AND skill_level <= 100),
  relationship_level INTEGER NOT NULL DEFAULT 0 CHECK (relationship_level >= 0 AND relationship_level <= 100),
  tasks_completed INTEGER NOT NULL DEFAULT 0,
  tasks_failed INTEGER NOT NULL DEFAULT 0,
  UNIQUE(contract_id, npc_id)
);

-- Индексы для npc_hiring_performance
CREATE INDEX IF NOT EXISTS idx_npc_hiring_performance_contract_id ON social.npc_hiring_performance(contract_id);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_performance_npc_id ON social.npc_hiring_performance(npc_id);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_performance_efficiency ON social.npc_hiring_performance(efficiency_score DESC);

-- Таблица экономики найма NPC
CREATE TABLE IF NOT EXISTS social.npc_hiring_economy (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  contract_id UUID NOT NULL REFERENCES social.npc_hiring_contracts(id) ON DELETE CASCADE,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  transaction_id UUID,
  payment_type VARCHAR(20) NOT NULL CHECK (payment_type IN ('salary', 'one-time', 'profit-share')),
  status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'failed')),
  payment_date TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(10, 2) NOT NULL CHECK (amount >= 0)
);

-- Индексы для npc_hiring_economy
CREATE INDEX IF NOT EXISTS idx_npc_hiring_economy_contract_id ON social.npc_hiring_economy(contract_id);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_economy_character_id ON social.npc_hiring_economy(character_id, status);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_economy_payment_date ON social.npc_hiring_economy(payment_date, status);

-- Таблица лимитов найма для персонажей
CREATE TABLE IF NOT EXISTS social.npc_hiring_limits (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  hiring_type VARCHAR(50) NOT NULL,
  scaling_factors JSONB,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  current_count INTEGER NOT NULL DEFAULT 0 CHECK (current_count >= 0),
  max_count INTEGER NOT NULL DEFAULT 0 CHECK (max_count >= 0),
  UNIQUE(character_id, hiring_type)
);

-- Индексы для npc_hiring_limits
CREATE INDEX IF NOT EXISTS idx_npc_hiring_limits_character_id ON social.npc_hiring_limits(character_id);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_limits_hiring_type ON social.npc_hiring_limits(hiring_type);

-- Таблица цепочек найма
CREATE TABLE IF NOT EXISTS social.npc_hiring_chains (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  npc_ids UUID[] NOT NULL,
  chain_name VARCHAR(100) NOT NULL,
  chain_type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  efficiency_bonus DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (efficiency_bonus >= 0)
);

-- Индексы для npc_hiring_chains
CREATE INDEX IF NOT EXISTS idx_npc_hiring_chains_character_id ON social.npc_hiring_chains(character_id);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_chains_chain_type ON social.npc_hiring_chains(chain_type);

-- Таблица групповых контрактов
CREATE TABLE IF NOT EXISTS social.npc_hiring_groups (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  contract_ids UUID[] NOT NULL,
  group_name VARCHAR(100) NOT NULL,
  shared_bonuses JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для npc_hiring_groups
CREATE INDEX IF NOT EXISTS idx_npc_hiring_groups_character_id ON social.npc_hiring_groups(character_id);

-- Таблица настроек автоматизации найма
CREATE TABLE IF NOT EXISTS social.npc_hiring_automation (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  automation_type VARCHAR(50) NOT NULL,
  configuration JSONB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  enabled BOOLEAN NOT NULL DEFAULT false,
  UNIQUE(character_id, automation_type)
);

-- Индексы для npc_hiring_automation
CREATE INDEX IF NOT EXISTS idx_npc_hiring_automation_character_id ON social.npc_hiring_automation(character_id, enabled);
CREATE INDEX IF NOT EXISTS idx_npc_hiring_automation_type ON social.npc_hiring_automation(automation_type, enabled);

-- Таблица отношений между игроками
CREATE TABLE IF NOT EXISTS social.player_relationships (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id_1 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  character_id_2 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  relationship_type VARCHAR(20) NOT NULL CHECK (relationship_type IN ('friends', 'close_allies', 'pact', 'neutral', 'enemies', 'nemesis')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reputation_value INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_character_order CHECK (character_id_1 < character_id_2),
  UNIQUE(character_id_1, character_id_2)
);

-- Индексы для player_relationships
CREATE INDEX IF NOT EXISTS idx_player_relationships_character_1 ON social.player_relationships(character_id_1);
CREATE INDEX IF NOT EXISTS idx_player_relationships_character_2 ON social.player_relationships(character_id_2);
CREATE INDEX IF NOT EXISTS idx_player_relationships_type ON social.player_relationships(relationship_type);

-- Таблица уровней доверия между игроками
CREATE TABLE IF NOT EXISTS social.trust_levels (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id_1 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  character_id_2 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  trust_level INTEGER NOT NULL DEFAULT 0 CHECK (trust_level >= 0 AND trust_level <= 100),
  trust_value INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_trust_character_order CHECK (character_id_1 < character_id_2),
  UNIQUE(character_id_1, character_id_2)
);

-- Индексы для trust_levels
CREATE INDEX IF NOT EXISTS idx_trust_levels_character_1 ON social.trust_levels(character_id_1);
CREATE INDEX IF NOT EXISTS idx_trust_levels_character_2 ON social.trust_levels(character_id_2);
CREATE INDEX IF NOT EXISTS idx_trust_levels_trust_level ON social.trust_levels(trust_level DESC);

-- Таблица договоров доверия
CREATE TABLE IF NOT EXISTS social.trust_contracts (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id_1 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  character_id_2 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  contract_type VARCHAR(30) NOT NULL CHECK (contract_type IN ('resource_sharing', 'profit_sharing', 'base_access')),
  status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'terminated', 'expired')),
  parameters JSONB,
  start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_date TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для trust_contracts
CREATE INDEX IF NOT EXISTS idx_trust_contracts_character_1 ON social.trust_contracts(character_id_1, status);
CREATE INDEX IF NOT EXISTS idx_trust_contracts_character_2 ON social.trust_contracts(character_id_2, status);
CREATE INDEX IF NOT EXISTS idx_trust_contracts_status ON social.trust_contracts(status, end_date) WHERE status = 'active';

-- Таблица союзов
CREATE TABLE IF NOT EXISTS social.alliances (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  creator_id UUID NOT NULL,
  member_ids UUID[],
  description TEXT,
  alliance_type VARCHAR(20) NOT NULL CHECK (alliance_type IN ('combat', 'trade', 'clan', 'faction')),
  creator_type VARCHAR(20) NOT NULL CHECK (creator_type IN ('player', 'clan', 'faction')),
  name VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'active', 'terminated')),
  member_types JSONB,
  parameters JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для alliances
CREATE INDEX IF NOT EXISTS idx_alliances_creator_id ON social.alliances(creator_id, status);
CREATE INDEX IF NOT EXISTS idx_alliances_type ON social.alliances(alliance_type, status);
CREATE INDEX IF NOT EXISTS idx_alliances_status ON social.alliances(status);

-- Таблица участников союзов
CREATE TABLE IF NOT EXISTS social.alliance_members (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    alliance_id UUID NOT NULL REFERENCES social.alliances(id) ON DELETE CASCADE,
    member_id UUID NOT NULL,
    member_type VARCHAR(20) NOT NULL CHECK (member_type IN ('player', 'clan', 'faction')),
    role VARCHAR(50),
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP
);

-- Индексы для alliance_members
CREATE INDEX IF NOT EXISTS idx_alliance_members_alliance_id ON social.alliance_members(alliance_id);
CREATE INDEX IF NOT EXISTS idx_alliance_members_member_id ON social.alliance_members(member_id, member_type);
CREATE INDEX IF NOT EXISTS idx_alliance_members_active ON social.alliance_members(alliance_id) WHERE left_at IS NULL;

-- Таблица рейтингов персонажей
CREATE TABLE IF NOT EXISTS social.character_ratings (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  rating_type VARCHAR(30) NOT NULL CHECK (rating_type IN ('overall', 'combat', 'trade', 'reliability', 'social_influence')),
  factors JSONB,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rating_value DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (rating_value >= 0 AND rating_value <= 100),
  UNIQUE(character_id, rating_type)
);

-- Индексы для character_ratings
CREATE INDEX IF NOT EXISTS idx_character_ratings_character_id ON social.character_ratings(character_id);
CREATE INDEX IF NOT EXISTS idx_character_ratings_type ON social.character_ratings(rating_type, rating_value DESC);

-- Таблица социального капитала
CREATE TABLE IF NOT EXISTS social.social_capital (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  trust_level_average DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (trust_level_average >= 0 AND trust_level_average <= 100),
  rating_average DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (rating_average >= 0 AND rating_average <= 100),
  social_capital_score DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  relationship_count INTEGER NOT NULL DEFAULT 0 CHECK (relationship_count >= 0),
  alliance_count INTEGER NOT NULL DEFAULT 0 CHECK (alliance_count >= 0),
  UNIQUE(character_id)
);

-- Индексы для social_capital
CREATE INDEX IF NOT EXISTS idx_social_capital_character_id ON social.social_capital(character_id);
CREATE INDEX IF NOT EXISTS idx_social_capital_score ON social.social_capital(social_capital_score DESC);

-- Таблица истории взаимодействий
CREATE TABLE IF NOT EXISTS social.interaction_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id_1 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    character_id_2 UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    event_type VARCHAR(30) NOT NULL CHECK (event_type IN ('trade', 'combat', 'contracts', 'quests', 'disputes', 'arbitration')),
    event_data JSONB,
    outcome JSONB,
    evidence JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для interaction_history
CREATE INDEX IF NOT EXISTS idx_interaction_history_character_1 ON social.interaction_history(character_id_1, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_interaction_history_character_2 ON social.interaction_history(character_id_2, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_interaction_history_event_type ON social.interaction_history(event_type, created_at DESC);

-- Таблица арбитража отношений
CREATE TABLE IF NOT EXISTS social.relationship_arbitration (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  complainant_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  defendant_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  reason TEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'in-review', 'resolved', 'dismissed')),
  evidence JSONB,
  decision JSONB,
  resolved_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для relationship_arbitration
CREATE INDEX IF NOT EXISTS idx_relationship_arbitration_complainant ON social.relationship_arbitration(complainant_id, status);
CREATE INDEX IF NOT EXISTS idx_relationship_arbitration_defendant ON social.relationship_arbitration(defendant_id, status);
CREATE INDEX IF NOT EXISTS idx_relationship_arbitration_status ON social.relationship_arbitration(status, created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE social.npc_relationships IS 'Отношения персонажей с NPC (репутация, доверие, взаимодействия)';
COMMENT ON TABLE social.npc_hiring_contracts IS 'Контракты найма NPC (зарплата, сроки, статус)';
COMMENT ON TABLE social.npc_hiring_tasks IS 'Задачи нанятых NPC';
COMMENT ON TABLE social.npc_hiring_performance IS 'Эффективность нанятых NPC (навыки, лояльность, выполнение задач)';
COMMENT ON TABLE social.npc_hiring_economy IS 'Экономика найма NPC (выплаты, транзакции)';
COMMENT ON TABLE social.npc_hiring_limits IS 'Лимиты найма для персонажей';
COMMENT ON TABLE social.npc_hiring_chains IS 'Цепочки найма NPC (синергии)';
COMMENT ON TABLE social.npc_hiring_groups IS 'Групповые контракты найма';
COMMENT ON TABLE social.npc_hiring_automation IS 'Настройки автоматизации найма NPC';
COMMENT ON TABLE social.player_relationships IS 'Отношения между игроками (друзья, союзники, враги)';
COMMENT ON TABLE social.trust_levels IS 'Уровни доверия между игроками';
COMMENT ON TABLE social.trust_contracts IS 'Договоры доверия (обмен ресурсами, прибыль, доступ к базе)';
COMMENT ON TABLE social.alliances IS 'Союзы между игроками/кланами/фракциями';
COMMENT ON TABLE social.alliance_members IS 'Участники союзов';
COMMENT ON TABLE social.character_ratings IS 'Рейтинги персонажей (общий, боевой, торговый, надежность, влияние)';
COMMENT ON TABLE social.social_capital IS 'Социальный капитал персонажей';
COMMENT ON TABLE social.interaction_history IS 'История взаимодействий между игроками';
COMMENT ON TABLE social.relationship_arbitration IS 'Арбитраж отношений между игроками';


