-- Issue: #140890862
-- Family Relationships System Database Schema
-- Создание таблиц для системы семейных отношений с NPC:
-- - family_trees (семейные деревья)
-- - family_members (члены семьи)
-- - family_relationships (семейные отношения)
-- - family_emotions (эмоции членов семьи)
-- - family_events (семейные события: свадьбы, рождения, болезни, конфликты, трагедии, праздники)
-- - family_adoptions (усыновления)
-- - family_heritage (наследование и завещания)
-- - family_heritage_disputes (споры о наследстве)
-- - family_quests (семейные квесты)
-- - family_interactions (взаимодействия игрока с семьей)

-- Создание схемы social, если её нет (уже создана в V1_52, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'family_relationship_type') THEN
        CREATE TYPE family_relationship_type AS ENUM ('parent', 'child', 'sibling', 'spouse', 'extended', 'adopted', 'guardian', 'ward');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'family_event_type') THEN
        CREATE TYPE family_event_type AS ENUM ('wedding', 'birth', 'illness', 'conflict', 'tragedy', 'celebration', 'adoption', 'divorce', 'death', 'inheritance');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'family_event_status') THEN
        CREATE TYPE family_event_status AS ENUM ('planned', 'active', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'family_emotion_type') THEN
        CREATE TYPE family_emotion_type AS ENUM ('attachment', 'anxiety', 'pride', 'anger', 'love', 'jealousy', 'grief', 'joy', 'trust', 'betrayal');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'adoption_status') THEN
        CREATE TYPE adoption_status AS ENUM ('pending', 'approved', 'rejected', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'heritage_status') THEN
        CREATE TYPE heritage_status AS ENUM ('active', 'disputed', 'resolved', 'executed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'heritage_dispute_status') THEN
        CREATE TYPE heritage_dispute_status AS ENUM ('pending', 'in_review', 'resolved', 'dismissed');
    END IF;
END $$;

-- Таблица семейных деревьев
CREATE TABLE IF NOT EXISTS social.family_trees (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_name VARCHAR(255),
    clan_id UUID, -- FK clans (nullable, для кланов)
    head_of_family_id UUID, -- FK characters/NPC (nullable)
    region_id UUID, -- FK regions (nullable)
    family_type VARCHAR(50), -- 'core', 'extended', 'clan'
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_trees
CREATE INDEX IF NOT EXISTS idx_family_trees_head_of_family_id 
    ON social.family_trees(head_of_family_id) WHERE head_of_family_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_family_trees_clan_id 
    ON social.family_trees(clan_id) WHERE clan_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_family_trees_region_id 
    ON social.family_trees(region_id) WHERE region_id IS NOT NULL;

-- Таблица членов семьи
CREATE TABLE IF NOT EXISTS social.family_members (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    character_id UUID NOT NULL, -- FK characters/NPC
    member_type VARCHAR(50) NOT NULL, -- 'player', 'npc'
    role_in_family VARCHAR(50), -- 'head', 'elder', 'member', 'child'
    status VARCHAR(50) NOT NULL DEFAULT 'alive', -- 'alive', 'deceased', 'missing'
    profession VARCHAR(100),
    faction_id UUID, -- FK factions (nullable)
    birth_date TIMESTAMP,
    death_date TIMESTAMP,
    secrets JSONB DEFAULT '{}',
    attributes JSONB DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(family_tree_id, character_id)
);

-- Индексы для family_members
CREATE INDEX IF NOT EXISTS idx_family_members_family_tree_id 
    ON social.family_members(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_members_character_id 
    ON social.family_members(character_id);
CREATE INDEX IF NOT EXISTS idx_family_members_status 
    ON social.family_members(status) WHERE status = 'alive';

-- Таблица семейных отношений
CREATE TABLE IF NOT EXISTS social.family_relationships (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    character_id UUID NOT NULL, -- FK characters/NPC
    family_member_id UUID NOT NULL, -- FK characters/NPC
    relationship_type family_relationship_type NOT NULL,
    relationship_quality DECIMAL(3,2) NOT NULL DEFAULT 0.50 CHECK (relationship_quality >= 0.00 AND relationship_quality <= 1.00),
    trust_level INTEGER NOT NULL DEFAULT 50 CHECK (trust_level >= 0 AND trust_level <= 100),
    attachment_level INTEGER NOT NULL DEFAULT 50 CHECK (attachment_level >= 0 AND attachment_level <= 100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(character_id, family_member_id, relationship_type)
);

-- Индексы для family_relationships
CREATE INDEX IF NOT EXISTS idx_family_relationships_family_tree_id 
    ON social.family_relationships(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_relationships_character_id 
    ON social.family_relationships(character_id);
CREATE INDEX IF NOT EXISTS idx_family_relationships_family_member_id 
    ON social.family_relationships(family_member_id);
CREATE INDEX IF NOT EXISTS idx_family_relationships_type 
    ON social.family_relationships(relationship_type);

-- Таблица эмоций членов семьи
CREATE TABLE IF NOT EXISTS social.family_emotions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    character_id UUID NOT NULL, -- FK characters/NPC
    target_character_id UUID, -- FK characters/NPC (nullable, для эмоций к конкретному члену семьи)
    emotion_type family_emotion_type NOT NULL,
    intensity DECIMAL(3,2) NOT NULL DEFAULT 0.50 CHECK (intensity >= 0.00 AND intensity <= 1.00),
    source_event_id UUID, -- FK family_events (nullable)
    duration_days INTEGER DEFAULT 0 CHECK (duration_days >= 0),
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_emotions
CREATE INDEX IF NOT EXISTS idx_family_emotions_family_tree_id 
    ON social.family_emotions(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_emotions_character_id 
    ON social.family_emotions(character_id);
CREATE INDEX IF NOT EXISTS idx_family_emotions_target_character_id 
    ON social.family_emotions(target_character_id) WHERE target_character_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_family_emotions_emotion_type 
    ON social.family_emotions(emotion_type);
CREATE INDEX IF NOT EXISTS idx_family_emotions_expires_at 
    ON social.family_emotions(expires_at) WHERE expires_at IS NOT NULL;

-- Таблица семейных событий
CREATE TABLE IF NOT EXISTS social.family_events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    event_type family_event_type NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status family_event_status NOT NULL DEFAULT 'planned',
    planned_date TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    participants UUID[] NOT NULL DEFAULT '{}', -- массив ID участников
    consequences JSONB DEFAULT '{}', -- последствия события
    visual_signals JSONB DEFAULT '{}', -- визуальные сигналы
    impact_metrics JSONB DEFAULT '{}', -- метрики влияния
    quest_chain_id UUID, -- FK quests (nullable, для связанных квестов)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_events
CREATE INDEX IF NOT EXISTS idx_family_events_family_tree_id 
    ON social.family_events(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_events_event_type 
    ON social.family_events(event_type);
CREATE INDEX IF NOT EXISTS idx_family_events_status 
    ON social.family_events(status);
CREATE INDEX IF NOT EXISTS idx_family_events_planned_date 
    ON social.family_events(planned_date) WHERE planned_date IS NOT NULL;

-- Таблица усыновлений
CREATE TABLE IF NOT EXISTS social.family_adoptions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    adopter_id UUID NOT NULL, -- FK characters/NPC (усыновитель)
    adoptee_id UUID NOT NULL, -- FK characters/NPC (усыновляемый)
    adoption_type VARCHAR(50) NOT NULL, -- 'player_adopts_npc', 'npc_adopts_player', 'npc_adopts_npc'
    status adoption_status NOT NULL DEFAULT 'pending',
    reputation_requirement INTEGER DEFAULT 0 CHECK (reputation_requirement >= 0),
    resource_requirement JSONB DEFAULT '{}', -- требования по ресурсам
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_adoptions
CREATE INDEX IF NOT EXISTS idx_family_adoptions_family_tree_id 
    ON social.family_adoptions(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_adoptions_adopter_id 
    ON social.family_adoptions(adopter_id);
CREATE INDEX IF NOT EXISTS idx_family_adoptions_adoptee_id 
    ON social.family_adoptions(adoptee_id);
CREATE INDEX IF NOT EXISTS idx_family_adoptions_status 
    ON social.family_adoptions(status);

-- Таблица наследования и завещаний
CREATE TABLE IF NOT EXISTS social.family_heritage (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    testator_id UUID NOT NULL, -- FK characters/NPC (завещатель)
    will_content JSONB NOT NULL DEFAULT '{}', -- содержание завещания
    assets JSONB NOT NULL DEFAULT '{}', -- активы (ресурсы, имущество, деньги)
    beneficiaries JSONB NOT NULL DEFAULT '{}', -- бенефициары и их доли
    status heritage_status NOT NULL DEFAULT 'active',
    executed_at TIMESTAMP,
    tax_amount DECIMAL(10,2) DEFAULT 0 CHECK (tax_amount >= 0),
    faction_checks JSONB DEFAULT '{}', -- проверки фракций
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_heritage
CREATE INDEX IF NOT EXISTS idx_family_heritage_family_tree_id 
    ON social.family_heritage(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_heritage_testator_id 
    ON social.family_heritage(testator_id);
CREATE INDEX IF NOT EXISTS idx_family_heritage_status 
    ON social.family_heritage(status);

-- Таблица споров о наследстве
CREATE TABLE IF NOT EXISTS social.family_heritage_disputes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    heritage_id UUID NOT NULL REFERENCES social.family_heritage(id) ON DELETE CASCADE,
    claimant_id UUID NOT NULL, -- FK characters/NPC (истец)
    defendant_id UUID, -- FK characters/NPC (ответчик, nullable)
    dispute_reason TEXT NOT NULL,
    evidence JSONB DEFAULT '{}', -- доказательства
    status heritage_dispute_status NOT NULL DEFAULT 'pending',
    resolution JSONB, -- решение спора
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_heritage_disputes
CREATE INDEX IF NOT EXISTS idx_family_heritage_disputes_heritage_id 
    ON social.family_heritage_disputes(heritage_id);
CREATE INDEX IF NOT EXISTS idx_family_heritage_disputes_claimant_id 
    ON social.family_heritage_disputes(claimant_id);
CREATE INDEX IF NOT EXISTS idx_family_heritage_disputes_status 
    ON social.family_heritage_disputes(status);

-- Таблица семейных квестов
CREATE TABLE IF NOT EXISTS social.family_quests (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    quest_id UUID NOT NULL, -- FK quests
    family_event_id UUID REFERENCES social.family_events(id) ON DELETE SET NULL,
    quest_type VARCHAR(50), -- 'wedding', 'birth', 'conflict', 'tragedy', 'celebration', etc.
    giver_id UUID NOT NULL, -- FK characters/NPC (дающий квест)
    player_id UUID, -- FK characters (nullable, для квестов игрока)
    status VARCHAR(50) NOT NULL DEFAULT 'available', -- 'available', 'active', 'completed', 'failed'
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_quests
CREATE INDEX IF NOT EXISTS idx_family_quests_family_tree_id 
    ON social.family_quests(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_quests_quest_id 
    ON social.family_quests(quest_id);
CREATE INDEX IF NOT EXISTS idx_family_quests_family_event_id 
    ON social.family_quests(family_event_id) WHERE family_event_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_family_quests_giver_id 
    ON social.family_quests(giver_id);
CREATE INDEX IF NOT EXISTS idx_family_quests_player_id 
    ON social.family_quests(player_id) WHERE player_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_family_quests_status 
    ON social.family_quests(status);

-- Таблица взаимодействий игрока с семьей
CREATE TABLE IF NOT EXISTS social.family_interactions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    family_tree_id UUID NOT NULL REFERENCES social.family_trees(id) ON DELETE CASCADE,
    player_id UUID NOT NULL, -- FK characters
    family_member_id UUID NOT NULL, -- FK characters/NPC
    interaction_type VARCHAR(50) NOT NULL, -- 'dialogue', 'help', 'gift', 'visit', 'mediation'
    interaction_data JSONB DEFAULT '{}', -- данные взаимодействия
    relationship_change INTEGER DEFAULT 0, -- изменение отношения (-100 до 100)
    trust_change INTEGER DEFAULT 0, -- изменение доверия (-100 до 100)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для family_interactions
CREATE INDEX IF NOT EXISTS idx_family_interactions_family_tree_id 
    ON social.family_interactions(family_tree_id);
CREATE INDEX IF NOT EXISTS idx_family_interactions_player_id 
    ON social.family_interactions(player_id);
CREATE INDEX IF NOT EXISTS idx_family_interactions_family_member_id 
    ON social.family_interactions(family_member_id);
CREATE INDEX IF NOT EXISTS idx_family_interactions_type 
    ON social.family_interactions(interaction_type);
CREATE INDEX IF NOT EXISTS idx_family_interactions_created_at 
    ON social.family_interactions(created_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE social.family_trees IS 'Семейные деревья (ядро, расширение, кланы)';
COMMENT ON TABLE social.family_members IS 'Члены семьи (игроки и NPC)';
COMMENT ON TABLE social.family_relationships IS 'Семейные отношения между членами семьи';
COMMENT ON TABLE social.family_emotions IS 'Эмоции членов семьи (привязанность, тревога, гордость, гнев)';
COMMENT ON TABLE social.family_events IS 'Семейные события (свадьбы, рождения, болезни, конфликты, трагедии, праздники)';
COMMENT ON TABLE social.family_adoptions IS 'Усыновления (игрок усыновляет NPC, NPC усыновляет игрока, NPC усыновляет NPC)';
COMMENT ON TABLE social.family_heritage IS 'Наследование и завещания';
COMMENT ON TABLE social.family_heritage_disputes IS 'Споры о наследстве';
COMMENT ON TABLE social.family_quests IS 'Семейные квесты, связанные с событиями';
COMMENT ON TABLE social.family_interactions IS 'Взаимодействия игрока с семьей (диалоги, помощь, подарки, визиты, медиация)';

-- Комментарии к колонкам
COMMENT ON COLUMN social.family_trees.family_type IS 'Тип семьи: core, extended, clan';
COMMENT ON COLUMN social.family_members.member_type IS 'Тип члена: player, npc';
COMMENT ON COLUMN social.family_members.status IS 'Статус: alive, deceased, missing';
COMMENT ON COLUMN social.family_members.secrets IS 'Секреты члена семьи в JSONB';
COMMENT ON COLUMN social.family_members.attributes IS 'Атрибуты члена семьи в JSONB';
COMMENT ON COLUMN social.family_relationships.relationship_quality IS 'Качество отношения (0.00-1.00)';
COMMENT ON COLUMN social.family_relationships.trust_level IS 'Уровень доверия (0-100)';
COMMENT ON COLUMN social.family_relationships.attachment_level IS 'Уровень привязанности (0-100)';
COMMENT ON COLUMN social.family_emotions.intensity IS 'Интенсивность эмоции (0.00-1.00)';
COMMENT ON COLUMN social.family_emotions.duration_days IS 'Длительность эмоции в днях';
COMMENT ON COLUMN social.family_events.participants IS 'Массив ID участников события';
COMMENT ON COLUMN social.family_events.consequences IS 'Последствия события в JSONB';
COMMENT ON COLUMN social.family_events.visual_signals IS 'Визуальные сигналы события в JSONB';
COMMENT ON COLUMN social.family_events.impact_metrics IS 'Метрики влияния события в JSONB';
COMMENT ON COLUMN social.family_adoptions.adoption_type IS 'Тип усыновления: player_adopts_npc, npc_adopts_player, npc_adopts_npc';
COMMENT ON COLUMN social.family_adoptions.resource_requirement IS 'Требования по ресурсам для усыновления в JSONB';
COMMENT ON COLUMN social.family_heritage.will_content IS 'Содержание завещания в JSONB';
COMMENT ON COLUMN social.family_heritage.assets IS 'Активы (ресурсы, имущество, деньги) в JSONB';
COMMENT ON COLUMN social.family_heritage.beneficiaries IS 'Бенефициары и их доли в JSONB';
COMMENT ON COLUMN social.family_heritage.tax_amount IS 'Сумма налога на наследство';
COMMENT ON COLUMN social.family_heritage.faction_checks IS 'Проверки фракций в JSONB';
COMMENT ON COLUMN social.family_heritage_disputes.evidence IS 'Доказательства спора в JSONB';
COMMENT ON COLUMN social.family_heritage_disputes.resolution IS 'Решение спора в JSONB';
COMMENT ON COLUMN social.family_quests.quest_type IS 'Тип квеста: wedding, birth, conflict, tragedy, celebration, etc.';
COMMENT ON COLUMN social.family_interactions.interaction_type IS 'Тип взаимодействия: dialogue, help, gift, visit, mediation';
COMMENT ON COLUMN social.family_interactions.interaction_data IS 'Данные взаимодействия в JSONB';
COMMENT ON COLUMN social.family_interactions.relationship_change IS 'Изменение отношения (-100 до 100)';
COMMENT ON COLUMN social.family_interactions.trust_change IS 'Изменение доверия (-100 до 100)';

