-- Import remaining Seattle 2020-2029 quests (001-010)
-- Generated for Backend import task #2273

-- Quest 001: Space Needle
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active
) VALUES (
    'quest-001-space-needle',
    'Сиэтл 2020-2029 — Space Needle',
    'Квест о посещении главной достопримечательности Сиэтла - Space Needle. Игрок поднимается на башню, испытывает стеклянный пол смотровой площадки и получает награду за успешное выполнение.',
    'main',
    'easy',
    1,
    '{"experience": 1000, "currency": {"type": "eddies", "amount": 500}, "items": [{"id": "space_needle_souvenir", "name": "Сувенир Space Needle", "rarity": "common"}]}',
    '[{"id": "visit_space_needle", "text": "Посетить Space Needle и подняться на смотровую площадку", "type": "interact", "target": "space_needle_entrance", "count": 1}, {"id": "experience_glass_floor", "text": "Испытать стеклянный пол на высоте", "type": "interact", "target": "glass_floor_platform", "count": 1}, {"id": "take_photos", "text": "Сфотографировать панораму города", "type": "skill_check", "skill": "perception", "difficulty": 0.3, "count": 1}]',
    'Seattle - Downtown',
    true
);

-- Quest 002: Pike Place Market
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-pike-place-market',
    'quest-002-pike-place-market',
    'Сиэтл 2020-2029 — Pike Place Market',
    'Исследование самого известного рынка Сиэтла. Игрок должен посетить различные лавки, попробовать местную еду и помочь продавцам.',
    'main',
    'easy',
    1,
    '{"experience": 1200, "currency": {"type": "eddies", "amount": 600}, "reputation": {"pike_place_merchants": 10}}',
    '[{"id": "visit_fish_throwing", "text": "Посмотреть на знаменитый бросок рыбы", "type": "interact", "target": "fish_throwing_stall", "count": 1}, {"id": "buy_coffee", "text": "Купить кофе в одной из лавок", "type": "interact", "target": "coffee_stall", "count": 1}, {"id": "help_merchant", "text": "Помочь продавцу с товаром", "type": "skill_check", "skill": "strength", "difficulty": 0.4, "count": 1}]',
    'Seattle - Pike Place Market',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 003: Starbucks Origin
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-starbucks-origin',
    'quest-003-starbucks-origin',
    'Сиэтл 2020-2029 — Происхождение Starbucks',
    'История о том, как Starbucks зародился в Сиэтле. Игрок посещает первое кафе и узнает легенду о создании сети кофеен.',
    'side',
    'easy',
    2,
    '{"experience": 800, "currency": {"type": "eddies", "amount": 300}, "items": [{"id": "starbucks_mug", "name": "Кружка Starbucks", "rarity": "uncommon"}]}',
    '[{"id": "find_first_store", "text": "Найти первое кафе Starbucks в Пике Плейс", "type": "interact", "target": "original_starbucks_location", "count": 1}, {"id": "talk_to_barista", "text": "Поговорить с бариста об истории компании", "type": "dialogue", "target": "starbucks_barista", "count": 1}, {"id": "buy_coffee_beans", "text": "Купить пакет кофе в зернах", "type": "interact", "target": "coffee_beans_display", "count": 1}]',
    'Seattle - Pike Place Market',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 004: Grunge Music
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-grunge-music',
    'quest-004-grunge-music',
    'Сиэтл 2020-2029 — Гранж-музыка',
    'Погружение в музыкальную культуру Сиэтла 90-х. Игрок посещает места, связанные с гранж-движением и Nirvana.',
    'side',
    'normal',
    3,
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 750}, "reputation": {"music_scene": 15}}',
    '[{"id": "visit_grunge_club", "text": "Посетить клуб, где начинали Nirvana", "type": "interact", "target": "grunge_music_club", "count": 1}, {"id": "find_guitar_pick", "text": "Найти гитарный медиатор Курта Кобейна", "type": "search", "target": "grunge_memorabilia", "count": 1}, {"id": "play_music", "text": "Исполнить гранж-песню", "type": "skill_check", "skill": "performance", "difficulty": 0.5, "count": 1}]',
    'Seattle - Belltown',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 005: Amazon HQ
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-amazon-hq',
    'quest-005-amazon-hq',
    'Сиэтл 2020-2029 — Штаб-квартира Amazon',
    'Тур по кампусу Amazon в Сиэтле. Игрок узнает о технологических инновациях и корпоративной культуре.',
    'main',
    'normal',
    5,
    '{"experience": 2000, "currency": {"type": "eddies", "amount": 1000}, "reputation": {"corporate_seattle": 20}}',
    '[{"id": "get_security_pass", "text": "Получить пропуск для посещения кампуса", "type": "interact", "target": "amazon_security_desk", "count": 1}, {"id": "visit_sphere", "text": "Посетить The Sphere - главное здание", "type": "interact", "target": "amazon_sphere", "count": 1}, {"id": "meet_engineer", "text": "Поговорить с инженером Amazon", "type": "dialogue", "target": "amazon_engineer", "count": 1}]',
    'Seattle - South Lake Union',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 006: Mount Rainier
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-mount-rainier',
    'quest-006-mount-rainier',
    'Сиэтл и гора Райнир',
    'Поездка к подножию горы Райнир - символа штата Вашингтон. Игрок наслаждается природой и горными пейзажами.',
    'side',
    'easy',
    2,
    '{"experience": 1800, "currency": {"type": "eddies", "amount": 400}, "items": [{"id": "rainier_photos", "name": "Фотографии горы Райнир", "rarity": "common"}]}',
    '[{"id": "drive_to_paradise", "text": "Доехать до Paradise - входа в парк", "type": "travel", "target": "mount_rainier_entrance", "count": 1}, {"id": "hike_trail", "text": "Пройти по горной тропе", "type": "interact", "target": "mountain_trail", "count": 1}, {"id": "see_wildlife", "text": "Увидеть диких животных", "type": "observe", "target": "mountain_wildlife", "count": 1}]',
    'Mount Rainier National Park',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 007: Rain, Rain, Rain
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-rain-rain-rain',
    'quest-007-rain-rain-rain',
    'Сиэтл — дождь, дождь, дождь',
    'Знаменитый дождливый климат Сиэтла. Игрок учится жить с постоянными дождями и находит преимущества дождливой погоды.',
    'side',
    'easy',
    1,
    '{"experience": 900, "currency": {"type": "eddies", "amount": 350}, "items": [{"id": "rain_jacket", "name": "Дождевик", "rarity": "common"}]}',
    '[{"id": "walk_in_rain", "text": "Прогуляться под дождем", "type": "interact", "target": "rainy_street", "count": 1}, {"id": "find_umbrella", "text": "Найти зонтик в кафе", "type": "search", "target": "cafe_umbrella_stand", "count": 1}, {"id": "enjoy_rain", "text": "Насладиться атмосферой дождливого города", "type": "observe", "target": "rainy_cityscape", "count": 1}]',
    'Seattle - Downtown',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 008: Boeing Factory
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-boeing-factory',
    'quest-008-boeing-factory',
    'Сиэтл — завод Boeing',
    'Экскурсия на завод Boeing - авиационного гиганта. Игрок узнает о производстве самолетов и истории авиации.',
    'main',
    'normal',
    4,
    '{"experience": 2200, "currency": {"type": "eddies", "amount": 900}, "reputation": {"aviation_industry": 25}}',
    '[{"id": "get_tour_ticket", "text": "Получить билет на экскурсию", "type": "interact", "target": "boeing_tour_desk", "count": 1}, {"id": "see_assembly_line", "text": "Посмотреть сборочную линию", "type": "interact", "target": "aircraft_assembly", "count": 1}, {"id": "meet_pilot", "text": "Поговорить с пилотом-испытателем", "type": "dialogue", "target": "test_pilot", "count": 1}]',
    'Seattle - Boeing Field',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 009: Seafood Salmon
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-seafood-salmon',
    'quest-009-seafood-salmon',
    'Сиэтл — морепродукты и лосось',
    'Кулинарное приключение с морепродуктами Сиэтла. Игрок пробует знаменитый лосось и другие дары океана.',
    'side',
    'easy',
    2,
    '{"experience": 1100, "currency": {"type": "eddies", "amount": 550}, "items": [{"id": "salmon_recipe", "name": "Рецепт лосося", "rarity": "uncommon"}]}',
    '[{"id": "visit_fish_market", "text": "Посетить рыбный рынок", "type": "interact", "target": "pike_place_fish_market", "count": 1}, {"id": "try_salmon", "text": "Попробовать свежего лосося", "type": "interact", "target": "salmon_restaurant", "count": 1}, {"id": "learn_recipe", "text": "Научиться готовить лосося", "type": "skill_check", "skill": "cooking", "difficulty": 0.3, "count": 1}]',
    'Seattle - Pike Place Market',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Quest 010: Tech Boom Gentrification
INSERT INTO gameplay.quest_definitions (
    id, quest_id, title, description, category, difficulty, level_requirement,
    rewards, objectives, location, is_active, created_at, updated_at
) VALUES (
    'canon-quest-seattle-tech-boom-gentrification',
    'quest-010-tech-boom-gentrification',
    'Сиэтл — Tech boom и джентрификация',
    'Исследование влияния технологического бума на город. Игрок видит контраст между богатыми тех-компаниями и обычными жителями.',
    'main',
    'hard',
    8,
    '{"experience": 3000, "currency": {"type": "eddies", "amount": 1500}, "reputation": {"social_activist": 30}}',
    '[{"id": "visit_tech_district", "text": "Посетить район с офисами тех-компаний", "type": "interact", "target": "tech_campus_area", "count": 1}, {"id": "talk_locals", "text": "Поговорить с местными жителями", "type": "dialogue", "target": "displaced_resident", "count": 1}, {"id": "investigate_housing", "text": "Исследовать проблемы с жильем", "type": "investigate", "target": "gentrification_evidence", "count": 1}]',
    'Seattle - Capitol Hill',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
