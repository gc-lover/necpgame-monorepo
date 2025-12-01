-- Issue: #140887526
-- Party System Database Schema Enhancement
-- Дополнение схемы системы групп:
-- - Обновление parties (добавление name, status, settings)
-- - Обновление party_members (добавление last_active_at)
-- - Создание party_invitations
-- - Создание party_loot_logs
-- Примечание: Базовые таблицы уже созданы в V1_32

-- Создание схемы social, если её нет (уже создана в V1_32, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов для соответствия архитектуре
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'party_loot_mode') THEN
        CREATE TYPE party_loot_mode AS ENUM ('free_for_all', 'need_before_greed', 'master_looter', 'round_robin');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'party_status') THEN
        CREATE TYPE party_status AS ENUM ('active', 'disbanded');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'party_member_role') THEN
        CREATE TYPE party_member_role AS ENUM ('leader', 'member');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'invitation_status') THEN
        CREATE TYPE invitation_status AS ENUM ('pending', 'accepted', 'declined', 'expired');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_distribution_mode') THEN
        CREATE TYPE loot_distribution_mode AS ENUM ('free_for_all', 'need_before_greed', 'master_looter', 'round_robin');
    END IF;
END $$;

-- Обновление parties для добавления недостающих полей
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'parties' 
                   AND column_name = 'name') THEN
        ALTER TABLE social.parties ADD COLUMN name VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'parties' 
                   AND column_name = 'status') THEN
        ALTER TABLE social.parties ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'disbanded'));
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'parties' 
                   AND column_name = 'settings') THEN
        ALTER TABLE social.parties ADD COLUMN settings JSONB DEFAULT '{}'::jsonb;
    END IF;
END $$;

-- Обновление party_members для добавления last_active_at
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'party_members' 
                   AND column_name = 'last_active_at') THEN
        ALTER TABLE social.party_members ADD COLUMN last_active_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

-- Таблица приглашений в группы
CREATE TABLE IF NOT EXISTS social.party_invitations (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    party_id UUID NOT NULL REFERENCES social.parties(id) ON DELETE CASCADE,
    inviter_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    invitee_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'declined', 'expired')),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP,
    UNIQUE(party_id, invitee_id, status) WHERE status = 'pending'
);

-- Индексы для party_invitations
CREATE INDEX IF NOT EXISTS idx_party_invitations_invitee_status ON social.party_invitations(invitee_id, status) WHERE status = 'pending';
CREATE INDEX IF NOT EXISTS idx_party_invitations_party_status ON social.party_invitations(party_id, status);
CREATE INDEX IF NOT EXISTS idx_party_invitations_expires_at ON social.party_invitations(expires_at) WHERE status = 'pending';

-- Таблица логов распределения лута
CREATE TABLE IF NOT EXISTS social.party_loot_logs (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    party_id UUID NOT NULL REFERENCES social.parties(id) ON DELETE CASCADE,
    item_id UUID NOT NULL,
    distributed_to UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    distribution_mode VARCHAR(30) NOT NULL CHECK (distribution_mode IN ('free_for_all', 'need_before_greed', 'master_looter', 'round_robin')),
    roll_results JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для party_loot_logs
CREATE INDEX IF NOT EXISTS idx_party_loot_logs_party_id ON social.party_loot_logs(party_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_party_loot_logs_distributed_to ON social.party_loot_logs(distributed_to, created_at DESC);

-- Обновление индексов для parties
CREATE INDEX IF NOT EXISTS idx_parties_leader_status ON social.parties(leader_id, status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_parties_status_created ON social.parties(status, created_at DESC) WHERE status = 'active';

-- Обновление индексов для party_members
CREATE INDEX IF NOT EXISTS idx_party_members_account_party ON social.party_members(character_id, party_id);

-- Комментарии к таблицам
COMMENT ON TABLE social.parties IS 'Группы игроков (до 5 участников)';
COMMENT ON TABLE social.party_members IS 'Участники групп';
COMMENT ON TABLE social.party_invitations IS 'Приглашения в группы';
COMMENT ON TABLE social.party_loot_logs IS 'Логи распределения лута в группах';

-- Комментарии к колонкам
COMMENT ON COLUMN social.parties.name IS 'Название группы (nullable)';
COMMENT ON COLUMN social.parties.status IS 'Статус группы: active, disbanded';
COMMENT ON COLUMN social.parties.settings IS 'Дополнительные настройки группы (JSONB)';
COMMENT ON COLUMN social.party_members.last_active_at IS 'Время последней активности участника';
COMMENT ON COLUMN social.party_invitations.status IS 'Статус приглашения: pending, accepted, declined, expired';
COMMENT ON COLUMN social.party_invitations.expires_at IS 'Время истечения приглашения';
COMMENT ON COLUMN social.party_invitations.responded_at IS 'Время ответа на приглашение (nullable)';
COMMENT ON COLUMN social.party_loot_logs.item_id IS 'ID предмета (ссылка на items)';
COMMENT ON COLUMN social.party_loot_logs.distribution_mode IS 'Режим распределения лута';
COMMENT ON COLUMN social.party_loot_logs.roll_results IS 'Результаты бросков кубика для need-before-greed (JSONB)';


