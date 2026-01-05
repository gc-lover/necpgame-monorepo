-- Import Remaining Seattle 2020-2029 Quests
-- Generated on 2025-12-28
-- Issue: #2273

-- Quest: canon-quest-seattle-space-needle
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Space Needle',
    'Игрок посещает Space Needle, испытывает стеклянный пол смотровой площадки и наслаждается панорамой Пьюджет-Саунда.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-space-needle", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-001-space-needle.yaml"}',
    '{"experience": 1000, "currency": {"type": "eddies", "amount": -35}, "reputation": {"aesthetics": 15}}',
    '[{"id": "reach_space_needle", "title": "Добраться до Space Needle и пройти контроль безопасности", "description": "Добраться до Space Needle и пройти контроль безопасности", "type": "interact", "order": 1}, {"id": "ride_elevator", "title": "Подняться на лифте на высоту 184 м за 41 секунду", "description": "Подняться на лифте на высоту 184 м за 41 секунду", "type": "interact", "order": 2}, {"id": "walk_observation_deck", "title": "Пройтись по смотровой площадке с панорамой 360° и стеклянным полом", "description": "Пройтись по смотровой площадке с панорамой 360° и стеклянным полом", "type": "interact", "order": 3}]',
    '2025-12-28T14:00:00Z',
    '2025-12-28T14:00:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-pike-place-market
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Pike Place Market',
    'Игрок исследует исторический Pike Place Market, испытывает атмосферу рыбного рынка и взаимодействует с местными торговцами.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-pike-place-market", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-002-pike-place-market.yaml"}',
    '{"experience": 800, "currency": {"type": "eddies", "amount": -20}, "reputation": {"local": 10}}',
    '[{"id": "enter_market", "title": "Войти на территорию Pike Place Market", "description": "Войти на территорию Pike Place Market", "type": "interact", "order": 1}, {"id": "visit_fish_throwing", "title": "Посмотреть на бросание рыбы", "description": "Посмотреть на бросание рыбы", "type": "interact", "order": 2}, {"id": "buy_fresh_fish", "title": "Купить свежую рыбу у местного торговца", "description": "Купить свежую рыбу у местного торговца", "type": "interact", "order": 3}]',
    '2025-12-28T14:05:00Z',
    '2025-12-28T14:05:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-starbucks-origin
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Происхождение Starbucks',
    'Игрок посещает оригинальное кафе Starbucks в Pike Place Market и узнаёт историю создания империи кофе.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-starbucks-origin", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-003-starbucks-origin.yaml"}',
    '{"experience": 600, "currency": {"type": "eddies", "amount": -15}, "reputation": {"corporate": 5}}',
    '[{"id": "find_original_store", "title": "Найти оригинальный магазин Starbucks в Pike Place", "description": "Найти оригинальный магазин Starbucks в Pike Place", "type": "investigate", "order": 1}, {"id": "learn_coffee_roasting", "title": "Узнать о процессе обжарки кофе", "description": "Узнать о процессе обжарки кофе", "type": "learn", "order": 2}, {"id": "taste_original_blend", "title": "Попробовать оригинальную смесь кофе", "description": "Попробовать оригинальную смесь кофе", "type": "experience", "order": 3}]',
    '2025-12-28T14:10:00Z',
    '2025-12-28T14:10:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-grunge-music
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Музыка Grunge',
    'Игрок погружается в историю музыки grunge, посещает ключевые места и встречается с музыкантами.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-grunge-music", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-004-grunge-music.yaml"}',
    '{"experience": 900, "currency": {"type": "eddies", "amount": -25}, "reputation": {"underground": 15}}',
    '[{"id": "visit_paramount_theater", "title": "Посетить Paramount Theatre", "description": "Посетить Paramount Theatre", "type": "interact", "order": 1}, {"id": "find_grunge_memorials", "title": "Найти мемориалы grunge музыки", "description": "Найти мемориалы grunge музыки", "type": "investigate", "order": 2}, {"id": "meet_musician", "title": "Встретиться с музыкантом", "description": "Встретиться с музыкантом", "type": "social", "order": 3}]',
    '2025-12-28T14:15:00Z',
    '2025-12-28T14:15:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-amazon-hq
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Штаб-квартира Amazon',
    'Игрок посещает кампус Amazon в Сиэтле и узнаёт о технологическом гиганте.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-amazon-hq", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-005-amazon-hq.yaml"}',
    '{"experience": 1200, "currency": {"type": "eddies", "amount": -40}, "reputation": {"corporate": 20}}',
    '[{"id": "reach_amazon_campus", "title": "Добраться до кампуса Amazon", "description": "Добраться до кампуса Amazon", "type": "interact", "order": 1}, {"id": "tour_facilities", "title": "Осмотреть объекты кампуса", "description": "Осмотреть объекты кампуса", "type": "investigate", "order": 2}, {"id": "learn_company_culture", "title": "Узнать о культуре компании", "description": "Узнать о культуре компании", "type": "learn", "order": 3}]',
    '2025-12-28T14:20:00Z',
    '2025-12-28T14:20:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-mount-rainier
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Гора Rainier',
    'Игрок отправляется в поход к горе Rainier, испытывая природную красоту и преодолевая трудности.',
    5,
    15,
    'active',
    '{"id": "canon-quest-seattle-mount-rainier", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-006-mount-rainier.yaml"}',
    '{"experience": 2500, "currency": {"type": "eddies", "amount": -100}, "reputation": {"nature": 30}}',
    '[{"id": "prepare_equipment", "title": "Подготовить снаряжение для похода", "description": "Подготовить снаряжение для похода", "type": "prepare", "order": 1}, {"id": "reach_base_camp", "title": "Добраться до базового лагеря", "description": "Добраться до базового лагеря", "type": "travel", "order": 2}, {"id": "climb_summit", "title": "Взойти на вершину", "description": "Взойти на вершину", "type": "climb", "order": 3}]',
    '2025-12-28T14:25:00Z',
    '2025-12-28T14:25:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-rain-rain-rain
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Дождь, дождь, дождь',
    'Игрок переживает типичный дождливый день в Сиэтле и учится ценить дождь.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-rain-rain-rain", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-007-rain-rain-rain.yaml"}',
    '{"experience": 500, "currency": {"type": "eddies", "amount": 0}, "reputation": {"local": 5}}',
    '[{"id": "experience_rain", "title": "Почувствовать дождь на себе", "description": "Почувствовать дождь на себе", "type": "experience", "order": 1}, {"id": "find_shelter", "title": "Найти укрытие от дождя", "description": "Найти укрытие от дождя", "type": "interact", "order": 2}, {"id": "embrace_weather", "title": "Принять дождливую погоду", "description": "Принять дождливую погоду", "type": "learn", "order": 3}]',
    '2025-12-28T14:30:00Z',
    '2025-12-28T14:30:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-boeing-factory
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Завод Boeing',
    'Игрок посещает завод Boeing и узнаёт о авиационной промышленности Сиэтла.',
    3,
    10,
    'active',
    '{"id": "canon-quest-seattle-boeing-factory", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-008-boeing-factory.yaml"}',
    '{"experience": 1800, "currency": {"type": "eddies", "amount": -50}, "reputation": {"corporate": 15}}',
    '[{"id": "get_security_clearance", "title": "Получить разрешение на посещение", "description": "Получить разрешение на посещение", "type": "interact", "order": 1}, {"id": "tour_production_line", "title": "Осмотреть производственную линию", "description": "Осмотреть производственную линию", "type": "investigate", "order": 2}, {"id": "learn_aviation_history", "title": "Узнать историю авиации", "description": "Узнать историю авиации", "type": "learn", "order": 3}]',
    '2025-12-28T14:35:00Z',
    '2025-12-28T14:35:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-seafood-salmon
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Морепродукты и лосось',
    'Игрок знакомится с морепродуктами Сиэтла, особенно с местным лососём.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-seafood-salmon", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-009-seafood-salmon.yaml"}',
    '{"experience": 700, "currency": {"type": "eddies", "amount": -30}, "reputation": {"local": 10}}',
    '[{"id": "visit_fish_market", "title": "Посетить рыбный рынок", "description": "Посетить рыбный рынок", "type": "interact", "order": 1}, {"id": "taste_salmon", "title": "Попробовать лосося", "description": "Попробовать лосося", "type": "experience", "order": 2}, {"id": "learn_sustainable_fishing", "title": "Узнать об устойчивом рыболовстве", "description": "Узнать об устойчивом рыболовстве", "type": "learn", "order": 3}]',
    '2025-12-28T14:40:00Z',
    '2025-12-28T14:40:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-tech-boom-gentrification
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Сиэтл 2020-2029 — Тех-бум и джентрификация',
    'Игрок исследует влияние технологического бума на джентрификацию в Сиэтле.',
    8,
    20,
    'active',
    '{"id": "canon-quest-seattle-tech-boom-gentrification", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-010-tech-boom-gentrification.yaml"}',
    '{"experience": 3000, "currency": {"type": "eddies", "amount": -80}, "reputation": {"social": 25}}',
    '[{"id": "investigate_neighborhoods", "title": "Исследовать разные районы города", "description": "Исследовать разные районы города", "type": "investigate", "order": 1}, {"id": "interview_locals", "title": "Интервью с местными жителями", "description": "Интервью с местными жителями", "type": "social", "order": 2}, {"id": "analyze_changes", "title": "Проанализировать социальные изменения", "description": "Проанализировать социальные изменения", "type": "analyze", "order": 3}]',
    '2025-12-28T14:45:00Z',
    '2025-12-28T14:45:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-2029-rain-city-underground
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle 2020-2029 - Rain City Underground',
    'Navigate rain-soaked alleyways and hidden basements to discover Seattle vibrant alternative music scene.',
    8,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-2029-rain-city-underground", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-016-rain-city-underground.yaml"}',
    '{"experience": 2500, "currency": {"type": "eddies", "amount": 450}, "reputation": {"underground_artists": 60, "corporations": -40}}',
    '[{"id": "discover_hidden_venues", "title": "Discover hidden underground music venues in Seattle alleyways and basements", "description": "Discover hidden underground music venues in Seattle alleyways and basements", "type": "explore", "order": 1}, {"id": "attend_secret_concert", "title": "Attend a secret underground concert in a rain-soaked warehouse", "description": "Attend a secret underground concert in a rain-soaked warehouse", "type": "interact", "order": 2}, {"id": "network_with_locals", "title": "Network with local underground musicians and scene members", "description": "Network with local underground musicians and scene members", "type": "social", "order": 3}]',
    '2025-12-28T14:50:00Z',
    '2025-12-28T14:50:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-seattle-011
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'Seattle — Утечка корпоративных данных',
    'Игрок расследует утечку данных из крупной технологической корпорации в Сиэтле, помогая жертвам кибератаки.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-011", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-011-seattle-corporate-data-leak.yaml"}',
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 3000}, "reputation": {"underground": 15, "corporate": -10}}',
    '[{"id": "meet_contacts", "title": "Встретиться с местными контактами", "description": "Встретиться с местными контактами", "type": "social", "order": 1}, {"id": "gather_evidence", "title": "Собрать доказательства корпоративных нарушений", "description": "Собрать доказательства корпоративных нарушений", "type": "investigate", "order": 2}, {"id": "organize_resistance", "title": "Организовать сопротивление", "description": "Организовать сопротивление", "type": "social", "order": 3}, {"id": "complete_mission", "title": "Завершить основную миссию", "description": "Завершить основную миссию", "type": "complete", "order": 4}]',
    '2025-12-28T14:55:00Z',
    '2025-12-28T14:55:00Z'
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
    'Игрок погружается в альтернативную кофейную культуру Сиэтла, исследуя подпольные кофейни и их влияние на местное сообщество.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-012", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-012-seattle-coffee-culture-underground.yaml"}',
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 3000}, "reputation": {"underground": 15, "corporate": -10}}',
    '[{"id": "explore_coffee_scene", "title": "Исследовать альтернативную кофейную сцену", "description": "Исследовать альтернативную кофейную сцену", "type": "explore", "order": 1}, {"id": "meet_brewers", "title": "Познакомиться с подпольными бариста", "description": "Познакомиться с подпольными бариста", "type": "social", "order": 2}, {"id": "learn_traditions", "title": "Изучить уникальные традиции", "description": "Изучить уникальные традиции", "type": "learn", "order": 3}]',
    '2025-12-28T15:00:00Z',
    '2025-12-28T15:00:00Z'
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
    'Игрок исследует секретный проект корпораций по созданию плавающих городов на побережье Сиэтла.',
    10,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-013", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-013-floating-cities-project.yaml"}',
    '{"experience": 2500, "currency": {"type": "eddies", "amount": -100}, "items": [{"item_id": "achievement_floating_city_pioneer", "quantity": 1}, {"item_id": "floating_city_blueprints", "quantity": 1}]}',
    '[{"id": "visit_research_facility", "title": "Посетить исследовательский центр", "description": "Посетить исследовательский центр", "type": "explore", "order": 1}, {"id": "gather_intelligence", "title": "Собрать разведданные", "description": "Собрать разведданные", "type": "investigate", "order": 2}, {"id": "infiltrate_meeting", "title": "Проникнуть на встречу", "description": "Проникнуть на встречу", "type": "infiltrate", "order": 3}]',
    '2025-12-28T15:05:00Z',
    '2025-12-28T15:05:00Z'
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
    'Seattle — Нейро-исследовательский центр',
    'Игрок проникает в секретный нейро-исследовательский центр корпорации в Сиэтле.',
    12,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-014", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-014-neural-research-facility.yaml"}',
    '{"experience": 3000, "currency": {"type": "eddies", "amount": -150}, "items": [{"item_id": "achievement_neural_pioneer", "quantity": 1}, {"item_id": "experimental_neural_implant", "quantity": 1}]}',
    '[{"id": "infiltrate_facility", "title": "Проникнуть в исследовательский центр", "description": "Проникнуть в исследовательский центр", "type": "infiltrate", "order": 1}, {"id": "gather_research_data", "title": "Собрать исследовательские данные", "description": "Собрать исследовательские данные", "type": "investigate", "order": 2}, {"id": "escape_facility", "title": "Покинуть центр незамеченным", "description": "Покинуть центр незамеченным", "type": "escape", "order": 3}]',
    '2025-12-28T15:10:00Z',
    '2025-12-28T15:10:00Z'
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
    'Seattle — Падение тех-бро',
    'Игрок расследует падение влиятельного тех-магната в Сиэтле, раскрывая корпоративные интриги.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-seattle-015", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-015-seattle-tech-bro-downfall.yaml"}',
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 3000}, "reputation": {"underground": 15, "corporate": -10}}',
    '[{"id": "investigate_scandal", "title": "Расследовать скандал", "description": "Расследовать скандал", "type": "investigate", "order": 1}, {"id": "gather_evidence", "title": "Собрать доказательства", "description": "Собрать доказательства", "type": "investigate", "order": 2}, {"id": "expose_corruption", "title": "Раскрыть коррупцию", "description": "Раскрыть коррупцию", "type": "social", "order": 3}]',
    '2025-12-28T15:15:00Z',
    '2025-12-28T15:15:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;
