-- Import New Seattle 2020-2029 Quests
-- Generated on 2025-12-28
-- Issue: #2249

-- Quest: canon-quest-seattle-011
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Утечка корпоративных данных',
    'Игрок расследует утечку данных из крупной технологической корпорации в Сиэтле, помогая жертвам кибератаки.',
    25,
    35,
    'active',
    '{"id": "canon-quest-seattle-011", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-011-seattle-corporate-data-leak.yaml"}',
    '{"experience": 8000, "currency": {"type": "eddies", "amount": 2500}}',
    '[{"id": "meet_contacts", "title": "Встретиться с местными контактами", "description": "Встретиться с местными контактами", "type": "social", "order": 1}, {"id": "gather_evidence", "title": "Собрать доказательства корпоративных нарушений", "description": "Собрать доказательства корпоративных нарушений", "type": "investigate", "order": 2}, {"id": "organize_resistance", "title": "Организовать сопротивление", "description": "Организовать сопротивление", "type": "organization", "order": 3}, {"id": "complete_mission", "title": "Завершить основную миссию", "description": "Завершить основную миссию", "type": "main", "order": 4}]',
    '2025-12-28T13:00:00Z',
    '2025-12-28T13:00:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-012
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Кофейная культура подполья',
    'Исследовать подпольную кофейную культуру в Сиэтле, раскрывая тайны альтернативного кофе.',
    20,
    30,
    'active',
    '{"id": "canon-quest-seattle-012", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-012-seattle-coffee-culture-underground.yaml"}',
    '{"experience": 6000, "currency": {"type": "eddies", "amount": 1800}}',
    '[{"id": "find_underground_cafe", "title": "Найти подпольное кафе", "description": "Найти подпольное кафе", "type": "investigate", "order": 1}, {"id": "taste_alternative_coffee", "title": "Попробовать альтернативный кофе", "description": "Попробовать альтернативный кофе", "type": "experience", "order": 2}, {"id": "learn_coffee_secrets", "title": "Изучить секреты кофе", "description": "Изучить секреты кофе", "type": "learning", "order": 3}]',
    '2025-12-28T13:05:00Z',
    '2025-12-28T13:05:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-013
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Проект плавающих городов',
    'Исследовать амбициозный проект плавающих городов в Сиэтле, борясь с климатическими изменениями.',
    28,
    38,
    'active',
    '{"id": "canon-quest-seattle-013", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-013-floating-cities-project.yaml"}',
    '{"experience": 9000, "currency": {"type": "eddies", "amount": 3200}}',
    '[{"id": "examine_floating_platforms", "title": "Осмотреть плавающие платформы", "description": "Осмотреть плавающие платформы", "type": "investigate", "order": 1}, {"id": "talk_environmentalists", "title": "Поговорить с экологами", "description": "Поговорить с экологами", "type": "dialogue", "order": 2}, {"id": "combat_climate_deniers", "title": "Бороться с отрицателями климата", "description": "Бороться с отрицателями климата", "type": "combat", "order": 3}]',
    '2025-12-28T13:10:00Z',
    '2025-12-28T13:10:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-014
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Исследовательский центр нейронных технологий',
    'Проникнуть в засекреченный исследовательский центр нейронных технологий в Сиэтле.',
    35,
    45,
    'active',
    '{"id": "canon-quest-seattle-014", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-014-neural-research-facility.yaml"}',
    '{"experience": 12000, "currency": {"type": "eddies", "amount": 4500}}',
    '[{"id": "infiltrate_facility", "title": "Проникнуть в центр", "description": "Проникнуть в центр", "type": "infiltration", "order": 1}, {"id": "gather_research_data", "title": "Собрать исследовательские данные", "description": "Собрать исследовательские данные", "type": "data_collection", "order": 2}, {"id": "escape_pursuit", "title": "Сбежать от преследования", "description": "Сбежать от преследования", "type": "escape", "order": 3}]',
    '2025-12-28T13:15:00Z',
    '2025-12-28T13:15:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-015
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Падение техно-миллиардеров',
    'Расследовать падение техно-миллиардеров в Сиэтле и их влияние на город.',
    30,
    40,
    'active',
    '{"id": "canon-quest-seattle-015", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-015-seattle-tech-bro-downfall.yaml"}',
    '{"experience": 10000, "currency": {"type": "eddies", "amount": 3800}}',
    '[{"id": "investigate_corporate_fall", "title": "Расследовать падение корпораций", "description": "Расследовать падение корпораций", "type": "investigate", "order": 1}, {"id": "interview_survivors", "title": "Интервью с выжившими", "description": "Интервью с выжившими", "type": "interview", "order": 2}, {"id": "expose_corruption", "title": "Разоблачить коррупцию", "description": "Разоблачить коррупцию", "type": "exposure", "order": 3}]',
    '2025-12-28T13:20:00Z',
    '2025-12-28T13:20:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-apocalypse-eve-2029
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Перед апокалипсисом',
    'Надвигающийся апокалипсис 2029 года требует финальных решений от жителей Сиэтла',
    25,
    50,
    'active',
    '{"id": "canon-quest-seattle-apocalypse-eve-2029", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-040-apocalypse-eve.yaml"}',
    '{"experience": 25000, "currency": {"type": "eddies", "amount": 10000}}',
    '[{"id": "witness_omens", "title": "Стать свидетелем знамений надвигающейся катастрофы", "description": "Стать свидетелем знамений надвигающейся катастрофы", "type": "observation", "order": 1}, {"id": "rally_allies", "title": "Собрать союзников из всех фракций для финального противостояния", "description": "Собрать союзников из всех фракций для финального противостояния", "type": "recruitment", "order": 2}, {"id": "secure_safe_zone", "title": "Организовать безопасную зону для выживших", "description": "Организовать безопасную зону для выживших", "type": "preparation", "order": 3}, {"id": "confront_antagonists", "title": "Противостоять антагонистам, ускоряющим апокалипсис", "description": "Противостоять антагонистам, ускоряющим апокалипсис", "type": "confrontation", "order": 4}]',
    '2025-12-28T13:25:00Z',
    '2025-12-28T13:25:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;
