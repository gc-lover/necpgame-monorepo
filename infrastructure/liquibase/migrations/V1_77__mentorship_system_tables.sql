-- Issue: #140890865
-- Mentorship System Database Schema
-- Создание таблиц для системы наставничества:
-- - mentorship_contracts (договоры наставничества)
-- - mentorship_schedules (расписания уроков)
-- - mentorship_lessons (уроки наставничества)
-- - mentorship_skill_progress (прогресс навыков учеников)
-- - mentor_reputation (репутация наставников)
-- - mentorship_reviews (отзывы о наставниках)
-- - academies (академии и образовательные центры)
-- - academy_programs (программы академий)
-- - academy_enrollments (записи в академии)
-- - mentorship_chains (цепочки наставничества)
-- - mentorship_content (учебный контент)

-- Создание схемы social, если её нет (уже создана в V1_52, но для безопасности)
CREATE SCHEMA IF NOT EXISTS social;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_type') THEN
        CREATE TYPE mentorship_type AS ENUM ('player_to_player', 'player_to_npc', 'npc_to_player', 'npc_to_npc', 'academy');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_contract_status') THEN
        CREATE TYPE mentorship_contract_status AS ENUM ('active', 'completed', 'terminated');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_payment_model') THEN
        CREATE TYPE mentorship_payment_model AS ENUM ('paid', 'grant', 'influence_points', 'resources');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'lesson_format') THEN
        CREATE TYPE lesson_format AS ENUM ('theoretical', 'practical', 'content_based', 'examination', 'group', 'vr');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_schedule_status') THEN
        CREATE TYPE mentorship_schedule_status AS ENUM ('scheduled', 'in-progress', 'completed', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_lesson_status') THEN
        CREATE TYPE mentorship_lesson_status AS ENUM ('scheduled', 'in-progress', 'completed', 'failed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'academy_type') THEN
        CREATE TYPE academy_type AS ENUM ('corporate', 'gang', 'independent');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'academy_program_status') THEN
        CREATE TYPE academy_program_status AS ENUM ('active', 'inactive');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'academy_enrollment_status') THEN
        CREATE TYPE academy_enrollment_status AS ENUM ('enrolled', 'in-progress', 'completed', 'dropped');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'academy_payment_status') THEN
        CREATE TYPE academy_payment_status AS ENUM ('pending', 'paid', 'refunded');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_chain_type') THEN
        CREATE TYPE mentorship_chain_type AS ENUM ('linear', 'network', 'faction', 'academy');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_content_type') THEN
        CREATE TYPE mentorship_content_type AS ENUM ('guide', 'simulation', 'vr_scene', 'video', 'text');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'content_moderation_status') THEN
        CREATE TYPE content_moderation_status AS ENUM ('pending', 'approved', 'rejected');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'content_monetization_status') THEN
        CREATE TYPE content_monetization_status AS ENUM ('free', 'paid', 'subscription');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mentorship_payment_status') THEN
        CREATE TYPE mentorship_payment_status AS ENUM ('pending', 'paid', 'failed', 'refunded');
    END IF;
END $$;

-- Таблица договоров наставничества
CREATE TABLE IF NOT EXISTS social.mentorship_contracts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    mentor_id UUID NOT NULL, -- FK accounts/characters
    mentee_id UUID NOT NULL, -- FK accounts/characters
    mentorship_type mentorship_type NOT NULL,
    contract_type VARCHAR(50),
    skill_track VARCHAR(50),
    start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP,
    status mentorship_contract_status NOT NULL DEFAULT 'active',
    payment_model mentorship_payment_model,
    payment_amount DECIMAL(10,2) DEFAULT 0 CHECK (payment_amount >= 0),
    terms JSONB DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_contracts
CREATE INDEX IF NOT EXISTS idx_mentorship_contracts_mentor_id 
    ON social.mentorship_contracts(mentor_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_contracts_mentee_id 
    ON social.mentorship_contracts(mentee_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_contracts_type_status 
    ON social.mentorship_contracts(mentorship_type, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_contracts_skill_track 
    ON social.mentorship_contracts(skill_track) WHERE skill_track IS NOT NULL;

-- Таблица расписаний уроков
CREATE TABLE IF NOT EXISTS social.mentorship_schedules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES social.mentorship_contracts(id) ON DELETE CASCADE,
    lesson_date TIMESTAMP NOT NULL,
    lesson_time TIME,
    location VARCHAR(255),
    format lesson_format NOT NULL,
    resources JSONB DEFAULT '{}',
    status mentorship_schedule_status NOT NULL DEFAULT 'scheduled',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_schedules
CREATE INDEX IF NOT EXISTS idx_mentorship_schedules_contract_id 
    ON social.mentorship_schedules(contract_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_schedules_lesson_date 
    ON social.mentorship_schedules(lesson_date, status) WHERE status = 'scheduled';

-- Таблица уроков наставничества
CREATE TABLE IF NOT EXISTS social.mentorship_lessons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES social.mentorship_contracts(id) ON DELETE CASCADE,
    schedule_id UUID REFERENCES social.mentorship_schedules(id) ON DELETE SET NULL,
    lesson_type VARCHAR(50),
    format lesson_format NOT NULL,
    content_id UUID, -- FK mentorship_content (nullable)
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration INTEGER CHECK (duration >= 0),
    skill_progress JSONB DEFAULT '{}',
    evaluation JSONB DEFAULT '{}',
    status mentorship_lesson_status NOT NULL DEFAULT 'scheduled',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_lessons
CREATE INDEX IF NOT EXISTS idx_mentorship_lessons_contract_id 
    ON social.mentorship_lessons(contract_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_lessons_schedule_id 
    ON social.mentorship_lessons(schedule_id) WHERE schedule_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_mentorship_lessons_status_started 
    ON social.mentorship_lessons(status, started_at) WHERE started_at IS NOT NULL;

-- Таблица прогресса навыков учеников
CREATE TABLE IF NOT EXISTS social.mentorship_skill_progress (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES social.mentorship_contracts(id) ON DELETE CASCADE,
    mentee_id UUID NOT NULL, -- FK accounts/characters
    skill_track VARCHAR(50) NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    initial_level INTEGER NOT NULL DEFAULT 0 CHECK (initial_level >= 0),
    current_level INTEGER NOT NULL DEFAULT 0 CHECK (current_level >= 0),
    target_level INTEGER NOT NULL DEFAULT 0 CHECK (target_level >= 0),
    experience_gained INTEGER NOT NULL DEFAULT 0 CHECK (experience_gained >= 0),
    bonus_applied DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (bonus_applied >= 0),
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(contract_id, skill_track, skill_name)
);

-- Индексы для mentorship_skill_progress
CREATE INDEX IF NOT EXISTS idx_mentorship_skill_progress_contract_id 
    ON social.mentorship_skill_progress(contract_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_skill_progress_mentee_id 
    ON social.mentorship_skill_progress(mentee_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_skill_progress_skill_track 
    ON social.mentorship_skill_progress(skill_track, skill_name);

-- Таблица репутации наставников
CREATE TABLE IF NOT EXISTS social.mentor_reputation (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    mentor_id UUID NOT NULL, -- FK accounts/characters
    reputation_score DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (reputation_score >= 0 AND reputation_score <= 100),
    total_students INTEGER NOT NULL DEFAULT 0 CHECK (total_students >= 0),
    successful_graduates INTEGER NOT NULL DEFAULT 0 CHECK (successful_graduates >= 0),
    average_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 CHECK (average_rating >= 0 AND average_rating <= 5),
    total_reviews INTEGER NOT NULL DEFAULT 0 CHECK (total_reviews >= 0),
    content_quality_score DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (content_quality_score >= 0 AND content_quality_score <= 100),
    academy_rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 CHECK (academy_rating >= 0 AND academy_rating <= 5),
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(mentor_id)
);

-- Индексы для mentor_reputation
CREATE INDEX IF NOT EXISTS idx_mentor_reputation_mentor_id 
    ON social.mentor_reputation(mentor_id);
CREATE INDEX IF NOT EXISTS idx_mentor_reputation_score 
    ON social.mentor_reputation(reputation_score DESC);
CREATE INDEX IF NOT EXISTS idx_mentor_reputation_rating 
    ON social.mentor_reputation(average_rating DESC);

-- Таблица отзывов о наставниках
CREATE TABLE IF NOT EXISTS social.mentorship_reviews (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES social.mentorship_contracts(id) ON DELETE CASCADE,
    mentor_id UUID NOT NULL, -- FK accounts/characters
    mentee_id UUID NOT NULL, -- FK accounts/characters
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_reviews
CREATE INDEX IF NOT EXISTS idx_mentorship_reviews_contract_id 
    ON social.mentorship_reviews(contract_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_reviews_mentor_id 
    ON social.mentorship_reviews(mentor_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_reviews_mentee_id 
    ON social.mentorship_reviews(mentee_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_reviews_rating 
    ON social.mentorship_reviews(rating DESC);

-- Таблица академий и образовательных центров
CREATE TABLE IF NOT EXISTS social.academies (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    academy_name VARCHAR(255) NOT NULL,
    academy_type academy_type NOT NULL,
    location VARCHAR(255),
    description TEXT,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 CHECK (rating >= 0 AND rating <= 5),
    total_students INTEGER NOT NULL DEFAULT 0 CHECK (total_students >= 0),
    total_programs INTEGER NOT NULL DEFAULT 0 CHECK (total_programs >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для academies
CREATE INDEX IF NOT EXISTS idx_academies_academy_type 
    ON social.academies(academy_type);
CREATE INDEX IF NOT EXISTS idx_academies_rating 
    ON social.academies(rating DESC);

-- Таблица программ академий
CREATE TABLE IF NOT EXISTS social.academy_programs (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    academy_id UUID NOT NULL REFERENCES social.academies(id) ON DELETE CASCADE,
    program_name VARCHAR(255) NOT NULL,
    skill_track VARCHAR(50) NOT NULL,
    duration INTEGER NOT NULL DEFAULT 0 CHECK (duration >= 0),
    cost DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (cost >= 0),
    requirements JSONB DEFAULT '{}',
    schedule JSONB DEFAULT '{}',
    status academy_program_status NOT NULL DEFAULT 'active',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для academy_programs
CREATE INDEX IF NOT EXISTS idx_academy_programs_academy_id 
    ON social.academy_programs(academy_id, status);
CREATE INDEX IF NOT EXISTS idx_academy_programs_skill_track 
    ON social.academy_programs(skill_track);
CREATE INDEX IF NOT EXISTS idx_academy_programs_status 
    ON social.academy_programs(status) WHERE status = 'active';

-- Таблица записей в академии
CREATE TABLE IF NOT EXISTS social.academy_enrollments (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    academy_id UUID NOT NULL REFERENCES social.academies(id) ON DELETE CASCADE,
    program_id UUID NOT NULL REFERENCES social.academy_programs(id) ON DELETE CASCADE,
    character_id UUID NOT NULL, -- FK accounts/characters
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    status academy_enrollment_status NOT NULL DEFAULT 'enrolled',
    payment_status academy_payment_status NOT NULL DEFAULT 'pending'
);

-- Индексы для academy_enrollments
CREATE INDEX IF NOT EXISTS idx_academy_enrollments_academy_program 
    ON social.academy_enrollments(academy_id, program_id, status);
CREATE INDEX IF NOT EXISTS idx_academy_enrollments_character_id 
    ON social.academy_enrollments(character_id, status);
CREATE INDEX IF NOT EXISTS idx_academy_enrollments_status 
    ON social.academy_enrollments(status);

-- Таблица цепочек наставничества
CREATE TABLE IF NOT EXISTS social.mentorship_chains (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    chain_name VARCHAR(255) NOT NULL,
    chain_type mentorship_chain_type NOT NULL,
    contract_ids UUID[] NOT NULL DEFAULT '{}',
    character_ids UUID[] NOT NULL DEFAULT '{}',
    chain_level INTEGER NOT NULL DEFAULT 1 CHECK (chain_level >= 1),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_chains
CREATE INDEX IF NOT EXISTS idx_mentorship_chains_chain_type 
    ON social.mentorship_chains(chain_type);
CREATE INDEX IF NOT EXISTS idx_mentorship_chains_chain_level 
    ON social.mentorship_chains(chain_level);

-- Таблица учебного контента
CREATE TABLE IF NOT EXISTS social.mentorship_content (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    creator_id UUID NOT NULL, -- FK accounts/characters
    content_type mentorship_content_type NOT NULL,
    content_name VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    description TEXT,
    skill_track VARCHAR(50),
    content_data JSONB DEFAULT '{}',
    moderation_status content_moderation_status NOT NULL DEFAULT 'pending',
    monetization_status content_monetization_status NOT NULL DEFAULT 'free',
    price DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (price >= 0),
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 CHECK (rating >= 0 AND rating <= 5),
    views INTEGER NOT NULL DEFAULT 0 CHECK (views >= 0),
    usage_count INTEGER NOT NULL DEFAULT 0 CHECK (usage_count >= 0),
    is_public BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_content
CREATE INDEX IF NOT EXISTS idx_mentorship_content_creator_id 
    ON social.mentorship_content(creator_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_content_type_skill 
    ON social.mentorship_content(content_type, skill_track) WHERE skill_track IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_mentorship_content_is_public 
    ON social.mentorship_content(is_public) WHERE is_public = true;
CREATE INDEX IF NOT EXISTS idx_mentorship_content_rating 
    ON social.mentorship_content(rating DESC);
CREATE INDEX IF NOT EXISTS idx_mentorship_content_moderation 
    ON social.mentorship_content(moderation_status) WHERE moderation_status = 'pending';

-- Таблица экономики наставничества
CREATE TABLE IF NOT EXISTS social.mentorship_economy (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES social.mentorship_contracts(id) ON DELETE CASCADE,
    payment_type mentorship_payment_model NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    payer_id UUID NOT NULL, -- FK accounts/characters
    recipient_id UUID NOT NULL, -- FK accounts/characters
    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status mentorship_payment_status NOT NULL DEFAULT 'pending',
    transaction_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для mentorship_economy
CREATE INDEX IF NOT EXISTS idx_mentorship_economy_contract_id 
    ON social.mentorship_economy(contract_id);
CREATE INDEX IF NOT EXISTS idx_mentorship_economy_payer_id 
    ON social.mentorship_economy(payer_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_economy_recipient_id 
    ON social.mentorship_economy(recipient_id, status);
CREATE INDEX IF NOT EXISTS idx_mentorship_economy_payment_date 
    ON social.mentorship_economy(payment_date, status);

-- Комментарии к таблицам
COMMENT ON TABLE social.mentorship_contracts IS 'Договоры наставничества между игроками и NPC';
COMMENT ON TABLE social.mentorship_schedules IS 'Расписания уроков наставничества';
COMMENT ON TABLE social.mentorship_lessons IS 'Уроки наставничества (теоретические, практические, VR)';
COMMENT ON TABLE social.mentorship_skill_progress IS 'Прогресс навыков учеников в рамках наставничества';
COMMENT ON TABLE social.mentor_reputation IS 'Репутация наставников (рейтинг, количество учеников, качество контента)';
COMMENT ON TABLE social.mentorship_reviews IS 'Отзывы о наставниках от учеников';
COMMENT ON TABLE social.academies IS 'Академии и образовательные центры (корпоративные, бандитские, независимые)';
COMMENT ON TABLE social.academy_programs IS 'Программы обучения в академиях';
COMMENT ON TABLE social.academy_enrollments IS 'Записи персонажей в академии';
COMMENT ON TABLE social.mentorship_chains IS 'Цепочки наставничества (линейные, сетевые, фракционные, академические)';
COMMENT ON TABLE social.mentorship_content IS 'Учебный контент (гайды, симуляции, VR сцены, видео, тексты)';
COMMENT ON TABLE social.mentorship_economy IS 'Экономика наставничества (платежи, гранты, влияние, ресурсы)';

-- Комментарии к колонкам
COMMENT ON COLUMN social.mentorship_contracts.mentorship_type IS 'Тип наставничества: player_to_player, player_to_npc, npc_to_player, npc_to_npc, academy';
COMMENT ON COLUMN social.mentorship_contracts.payment_model IS 'Модель оплаты: paid, grant, influence_points, resources';
COMMENT ON COLUMN social.mentorship_contracts.terms IS 'Условия контракта в JSONB';
COMMENT ON COLUMN social.mentorship_schedules.format IS 'Формат урока: theoretical, practical, content_based, examination, group, vr';
COMMENT ON COLUMN social.mentorship_lessons.format IS 'Формат урока: theoretical, practical, content_based, examination, group, vr';
COMMENT ON COLUMN social.mentorship_lessons.skill_progress IS 'Прогресс навыков после урока в JSONB';
COMMENT ON COLUMN social.mentorship_lessons.evaluation IS 'Оценка урока в JSONB';
COMMENT ON COLUMN social.mentorship_skill_progress.bonus_applied IS 'Бонус опыта от наставничества';
COMMENT ON COLUMN social.mentor_reputation.reputation_score IS 'Общий рейтинг наставника (0-100)';
COMMENT ON COLUMN social.mentor_reputation.average_rating IS 'Средний рейтинг отзывов (0-5)';
COMMENT ON COLUMN social.mentor_reputation.content_quality_score IS 'Оценка качества контента (0-100)';
COMMENT ON COLUMN social.mentor_reputation.academy_rating IS 'Рейтинг в академии (0-5)';
COMMENT ON COLUMN social.mentorship_reviews.rating IS 'Рейтинг наставника (1-5)';
COMMENT ON COLUMN social.academies.academy_type IS 'Тип академии: corporate, gang, independent';
COMMENT ON COLUMN social.academies.rating IS 'Рейтинг академии (0-5)';
COMMENT ON COLUMN social.academy_programs.requirements IS 'Требования для поступления в JSONB';
COMMENT ON COLUMN social.academy_programs.schedule IS 'Расписание программы в JSONB';
COMMENT ON COLUMN social.mentorship_chains.contract_ids IS 'Массив ID контрактов в цепочке';
COMMENT ON COLUMN social.mentorship_chains.character_ids IS 'Массив ID персонажей в цепочке';
COMMENT ON COLUMN social.mentorship_chains.chain_level IS 'Уровень цепочки (глубина иерархии)';
COMMENT ON COLUMN social.mentorship_content.content_type IS 'Тип контента: guide, simulation, vr_scene, video, text';
COMMENT ON COLUMN social.mentorship_content.content_data IS 'Данные контента в JSONB';
COMMENT ON COLUMN social.mentorship_content.moderation_status IS 'Статус модерации: pending, approved, rejected';
COMMENT ON COLUMN social.mentorship_content.monetization_status IS 'Статус монетизации: free, paid, subscription';
COMMENT ON COLUMN social.mentorship_content.price IS 'Цена контента (если monetization_status = paid)';
COMMENT ON COLUMN social.mentorship_content.rating IS 'Рейтинг контента (0-5)';
COMMENT ON COLUMN social.mentorship_content.views IS 'Количество просмотров контента';
COMMENT ON COLUMN social.mentorship_content.usage_count IS 'Количество использований контента';
COMMENT ON COLUMN social.mentorship_economy.payment_type IS 'Тип платежа: paid, grant, influence_points, resources';
COMMENT ON COLUMN social.mentorship_economy.status IS 'Статус платежа: pending, paid, failed, refunded';

