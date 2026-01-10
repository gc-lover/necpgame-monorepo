-- Dynamic Quests Implementation - Player Choice Based Stories
-- Migration for loading dynamic quest examples into gameplay.quest_definitions table
-- Issue: #2244 - Система динамических квестов на основе выбора игрока

-- Quest 1: Street Justice Dilemma
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    is_repeatable, rewards, objectives, prerequisites, location, npc_giver,
    is_active, metadata
) VALUES (
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    'dynamic-street-justice-dilemma',
    'Уличное Правосудие: Выбор Совести',
    'В темном переулке Night City вы становитесь свидетелем жестокой расправы уличной банды Maelstrom над одиноким риггером. Ваше решение определит не только судьбу жертвы, но и ваше будущее в этом районе.',
    'social',
    'normal',
    5,
    false,
    '{
        "experience": 2500,
        "currency": {"type": "eddies", "amount": 800},
        "items": [{"id": "street_justice_badge", "name": "Значок Уличного Правосудия", "rarity": "uncommon"}],
        "reputation": {"street_reputation": 15}
    }'::jsonb,
    '[
        {"id": "witness_incident", "description": "Стать свидетелем инцидента", "type": "witness", "count": 1},
        {"id": "make_choice", "description": "Принять решение о вмешательстве", "type": "choice", "count": 1},
        {"id": "resolve_situation", "description": "Разрешить ситуацию", "type": "custom", "count": 1}
    ]'::jsonb,
    '{"level_min": 5, "location": "Night City - Watson District"}'::jsonb,
    'Night City - Watson District',
    'Случайный прохожий',
    true,
    '{
        "dynamic_quest": true,
        "branching_paths": ["hero_path", "bandit_path", "neutral_path", "informant_path"],
        "choice_points": ["initial_intervention"],
        "consequences": ["immediate", "short_term", "long_term", "butterfly"],
        "tags": ["social", "justice", "street_life"],
        "choice_mechanics": {
            "initial_choice": {
                "options": ["intervene_help", "intervene_join", "observe_neutral", "call_authorities"],
                "timing": "immediate"
            }
        }
    }'::jsonb
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    metadata = EXCLUDED.metadata,
    updated_at = CURRENT_TIMESTAMP;

-- Quest 2: Corporate Betrayal Conspiracy
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    is_repeatable, rewards, objectives, prerequisites, location, npc_giver,
    faction_requirements, is_active, metadata
) VALUES (
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    'dynamic-corporate-betrayal-conspiracy',
    'Корпоративное Предательство: Сеть Интриг',
    'Работая в Arasaka, вы обнаруживаете доказательства внутренней измены. Ваш непосредственный начальник тайно сотрудничает с Militech. Раскрытие информации может разрушить вашу карьеру или стоить жизни.',
    'corporate',
    'hard',
    15,
    false,
    '{
        "experience": 5000,
        "currency": {"type": "eddies", "amount": 2500},
        "items": [{"id": "corporate_whistleblower_implant", "name": "Имплант Разоблачителя", "rarity": "rare"}],
        "reputation": {"corporate_reputation": 20, "underground_reputation": 10}
    }'::jsonb,
    '[
        {"id": "discover_evidence", "description": "Обнаружить компрометирующие материалы", "type": "investigate", "count": 1},
        {"id": "evaluate_options", "description": "Оценить возможные варианты действий", "type": "choice", "count": 1},
        {"id": "execute_plan", "description": "Выполнить выбранный план", "type": "custom", "count": 1}
    ]'::jsonb,
    '{"level_min": 15, "employment": "Arasaka", "location": "Night City - Corpo Plaza"}'::jsonb,
    'Night City - Corpo Plaza',
    'Анонимный источник',
    '{"Arasaka": {"min_reputation": 10}}'::jsonb,
    true,
    '{
        "dynamic_quest": true,
        "branching_paths": ["whistleblower_path", "blackmail_path", "silence_path", "betrayal_path"],
        "choice_points": ["evidence_discovery", "strategy_selection"],
        "consequences": ["immediate", "short_term", "long_term", "butterfly"],
        "tags": ["corporate", "intrigue", "betrayal", "career"],
        "choice_mechanics": {
            "evidence_discovery": {
                "options": ["report_internally", "blackmail_boss", "sell_to_competitor", "destroy_evidence"],
                "risk_levels": ["medium", "high", "critical", "low"]
            }
        }
    }'::jsonb
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    faction_requirements = EXCLUDED.faction_requirements,
    metadata = EXCLUDED.metadata,
    updated_at = CURRENT_TIMESTAMP;

-- Quest 3: Memory Merchant Mystery
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    is_repeatable, max_completions, rewards, objectives, prerequisites,
    location, npc_giver, is_active, metadata
) VALUES (
    '550e8400-e29b-41d4-a716-446655440003'::uuid,
    'dynamic-memory-merchant-mystery',
    'Торговец Воспоминаниями: Цена Памяти',
    'В подпольном Memory Market вы находите торговца воспоминаниями, предлагающего купить чужие переживания. Среди товаров - воспоминания известных людей, запрещенные опыты и даже воспоминания о будущем.',
    'mystical',
    'easy',
    3,
    true,
    3,
    '{
        "experience": 800,
        "currency": {"type": "eddies", "amount": 200},
        "items": [{"id": "memory_fragment", "name": "Фрагмент Воспоминаний", "rarity": "common"}],
        "sanity_effect": -5
    }'::jsonb,
    '[
        {"id": "enter_market", "description": "Найти Memory Market", "type": "explore", "count": 1},
        {"id": "choose_memory", "description": "Выбрать воспоминание для покупки", "type": "choice", "count": 1},
        {"id": "experience_memory", "description": "Пережить выбранное воспоминание", "type": "custom", "count": 1}
    ]'::jsonb,
    '{"level_min": 3, "location": "Night City - Memory Market"}'::jsonb,
    'Night City - Memory Market',
    'Торговец Воспоминаниями',
    true,
    '{
        "dynamic_quest": true,
        "branching_paths": ["celebrity_path", "future_path", "personal_path", "refusal_path"],
        "choice_points": ["memory_selection"],
        "consequences": ["immediate", "short_term", "long_term", "butterfly"],
        "tags": ["mystical", "memory", "psychological", "underground"],
        "choice_mechanics": {
            "memory_selection": {
                "options": ["buy_own_memory", "buy_celebrity_memory", "buy_future_memory", "refuse_purchase"],
                "costs": [1000, 5000, 10000, 0]
            }
        }
    }'::jsonb
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    metadata = EXCLUDED.metadata,
    updated_at = CURRENT_TIMESTAMP;

-- Create indexes for dynamic quest queries
CREATE INDEX IF NOT EXISTS idx_quest_definitions_dynamic
ON gameplay.quest_definitions ((metadata->>'dynamic_quest'))
WHERE (metadata->>'dynamic_quest')::boolean = true;

CREATE INDEX IF NOT EXISTS idx_quest_definitions_choice_points
ON gameplay.quest_definitions USING GIN ((metadata->'choice_points'));

CREATE INDEX IF NOT EXISTS idx_quest_definitions_branching_paths
ON gameplay.quest_definitions USING GIN ((metadata->'branching_paths'));

CREATE INDEX IF NOT EXISTS idx_quest_definitions_tags
ON gameplay.quest_definitions USING GIN ((metadata->'tags'));

-- Add comments for documentation
COMMENT ON COLUMN gameplay.quest_definitions.metadata IS 'JSON metadata for dynamic quest configuration including choice mechanics, branching paths, and consequences';

-- Create table for tracking player choices in dynamic quests
CREATE TABLE IF NOT EXISTS gameplay.player_dynamic_quest_choices (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    quest_definition_id UUID NOT NULL REFERENCES gameplay.quest_definitions(id) ON DELETE CASCADE,
    choice_point VARCHAR(255) NOT NULL,
    selected_option VARCHAR(255) NOT NULL,
    choice_metadata JSONB,
    consequences_applied JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(player_id, quest_definition_id, choice_point)
);

-- Create indexes for player choice queries
CREATE INDEX IF NOT EXISTS idx_player_choices_player_quest
ON gameplay.player_dynamic_quest_choices (player_id, quest_definition_id);

CREATE INDEX IF NOT EXISTS idx_player_choices_choice_point
ON gameplay.player_dynamic_quest_choices (choice_point, selected_option);

-- Add comments
COMMENT ON TABLE gameplay.player_dynamic_quest_choices IS 'Tracks player choices in dynamic quests for consequence calculation and replay';
COMMENT ON COLUMN gameplay.player_dynamic_quest_choices.choice_metadata IS 'Additional metadata about the choice context';
COMMENT ON COLUMN gameplay.player_dynamic_quest_choices.consequences_applied IS 'Record of what consequences were triggered by this choice';

-- Create table for dynamic quest consequences tracking
CREATE TABLE IF NOT EXISTS gameplay.dynamic_quest_consequences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    quest_definition_id UUID NOT NULL REFERENCES gameplay.quest_definitions(id) ON DELETE CASCADE,
    choice_id UUID NOT NULL REFERENCES gameplay.player_dynamic_quest_choices(id) ON DELETE CASCADE,
    consequence_type VARCHAR(50) NOT NULL, -- immediate, short_term, long_term, butterfly
    consequence_key VARCHAR(255) NOT NULL,
    consequence_data JSONB,
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE,

    UNIQUE(player_id, quest_definition_id, consequence_key)
);

-- Create indexes for consequence queries
CREATE INDEX IF NOT EXISTS idx_quest_consequences_player
ON gameplay.dynamic_quest_consequences (player_id, consequence_type);

CREATE INDEX IF NOT EXISTS idx_quest_consequences_expiry
ON gameplay.dynamic_quest_consequences (expires_at)
WHERE expires_at IS NOT NULL;

-- Add comments
COMMENT ON TABLE gameplay.dynamic_quest_consequences IS 'Tracks applied consequences from dynamic quest choices';
COMMENT ON COLUMN gameplay.dynamic_quest_consequences.consequence_type IS 'Type of consequence: immediate, short_term, long_term, butterfly';
COMMENT ON COLUMN gameplay.dynamic_quest_consequences.consequence_key IS 'Unique identifier for the consequence effect';
COMMENT ON COLUMN gameplay.dynamic_quest_consequences.consequence_data IS 'Data payload for the consequence effect';

-- Insert sample data for testing dynamic quests
-- This data can be used for development and testing purposes

-- Sample player choice for Street Justice quest
INSERT INTO gameplay.player_dynamic_quest_choices (
    id, player_id, quest_definition_id, choice_point, selected_option, choice_metadata
) VALUES (
    '550e8400-e29b-41d4-a716-446655440101'::uuid,
    '550e8400-e29b-41d4-a716-446655440000'::uuid, -- Sample player ID
    '550e8400-e29b-41d4-a716-446655440001'::uuid, -- Street Justice quest
    'initial_intervention',
    'intervene_help',
    '{"alignment_modifier": 10, "risk_assessment": "medium"}'::jsonb
) ON CONFLICT (player_id, quest_definition_id, choice_point) DO NOTHING;

-- Sample consequences for the choice
INSERT INTO gameplay.dynamic_quest_consequences (
    id, player_id, quest_definition_id, choice_id, consequence_type, consequence_key, consequence_data
) VALUES (
    '550e8400-e29b-41d4-a716-446655440201'::uuid,
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    '550e8400-e29b-41d4-a716-446655440101'::uuid,
    'immediate',
    'street_reputation_gain',
    '{"amount": 10, "description": "Reputation increase for helping victim"}'::jsonb
) ON CONFLICT (player_id, quest_definition_id, consequence_key) DO NOTHING;

INSERT INTO gameplay.dynamic_quest_consequences (
    id, player_id, quest_definition_id, choice_id, consequence_type, consequence_key, consequence_data, expires_at
) VALUES (
    '550e8400-e29b-41d4-a716-446655440202'::uuid,
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    '550e8400-e29b-41d4-a716-446655440101'::uuid,
    'butterfly',
    'maelstrom_retaliation_risk',
    '{"chance": 0.3, "effect": "random_maelstrom_ambush", "description": "Risk of Maelstrom retaliation"}'::jsonb,
    CURRENT_TIMESTAMP + INTERVAL '30 days'
) ON CONFLICT (player_id, quest_definition_id, consequence_key) DO NOTHING;