-- Import remaining Seattle 2020-2029 quests (001-010)
-- Generated for Backend import task #2273

-- Quest 001: Space Needle
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-001-space-needle',
    'Сиэтл 2020-2029 — Space Needle',
    'Квест о посещении главной достопримечательности Сиэтла - Space Needle. Игрок поднимается на башню, испытывает стеклянный пол смотровой площадки и получает награду за успешное выполнение.',
    'easy',
    1,
    5,
    '{"experience": 1000, "currency": {"type": "eddies", "amount": 500}, "items": [{"id": "space_needle_souvenir", "name": "Сувенир Space Needle", "rarity": "common"}]}',
    '[{"id": "visit_space_needle", "text": "Посетить Space Needle и подняться на смотровую площадку", "type": "interact", "target": "space_needle_entrance", "count": 1}, {"id": "experience_glass_floor", "text": "Испытать стеклянный пол на высоте", "type": "interact", "target": "glass_floor_platform", "count": 1}, {"id": "take_photos", "text": "Сфотографировать панораму города", "type": "skill_check", "skill": "perception", "difficulty": 0.3, "count": 1}]',
    'Seattle - Downtown',
    '2020-2029',
    'main',
    'active'
);

-- Quest 002: Pike Place Market
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-002-pike-place-market',
    'Сиэтл 2020-2029 — Pike Place Market',
    'Исследование самого известного рынка Сиэтла. Игрок должен посетить различные лавки, попробовать местную еду и помочь продавцам.',
    'easy',
    1,
    5,
    '{"experience": 1200, "currency": {"type": "eddies", "amount": 600}, "reputation": {"pike_place_merchants": 10}}',
    '[{"id": "visit_fish_throwing", "text": "Посмотреть на знаменитый бросок рыбы", "type": "interact", "target": "fish_throwing_stall", "count": 1}, {"id": "buy_coffee", "text": "Купить кофе в одной из лавок", "type": "interact", "target": "coffee_stall", "count": 1}, {"id": "help_merchant", "text": "Помочь продавцу с товаром", "type": "skill_check", "skill": "strength", "difficulty": 0.4, "count": 1}]',
    'Seattle - Pike Place Market',
    '2020-2029',
    'main',
    'active'
);

-- Quest 003: Starbucks Origin
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-003-starbucks-origin',
    'Сиэтл 2020-2029 — Происхождение Starbucks',
    'История о том, как Starbucks зародился в Сиэтле. Игрок посещает первое кафе и узнает легенду о создании сети кофеен.',
    'easy',
    2,
    6,
    '{"experience": 800, "currency": {"type": "eddies", "amount": 300}, "items": [{"id": "starbucks_mug", "name": "Кружка Starbucks", "rarity": "uncommon"}]}',
    '[{"id": "find_first_store", "text": "Найти первое кафе Starbucks в Пике Плейс", "type": "interact", "target": "original_starbucks_location", "count": 1}, {"id": "talk_to_barista", "text": "Поговорить с бариста об истории компании", "type": "dialogue", "target": "starbucks_barista", "count": 1}, {"id": "buy_coffee_beans", "text": "Купить пакет кофе в зернах", "type": "interact", "target": "coffee_beans_display", "count": 1}]',
    'Seattle - Pike Place Market',
    '2020-2029',
    'side',
    'active'
);

-- Quest 004: Grunge Music
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-004-grunge-music',
    'Сиэтл 2020-2029 — Гранж-музыка',
    'Погружение в музыкальную культуру Сиэтла 90-х. Игрок посещает места, связанные с гранж-движением и Nirvana.',
    'medium',
    3,
    7,
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 750}, "reputation": {"music_scene": 15}}',
    '[{"id": "visit_grunge_club", "text": "Посетить клуб, где начинали Nirvana", "type": "interact", "target": "grunge_music_club", "count": 1}, {"id": "find_guitar_pick", "text": "Найти гитарный медиатор Курта Кобейна", "type": "search", "target": "grunge_memorabilia", "count": 1}, {"id": "play_music", "text": "Исполнить гранж-песню", "type": "skill_check", "skill": "performance", "difficulty": 0.5, "count": 1}]',
    'Seattle - Belltown',
    '2020-2029',
    'side',
    'active'
);

-- Quest 005: Amazon HQ
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-005-amazon-hq',
    'Сиэтл 2020-2029 — Штаб-квартира Amazon',
    'Тур по кампусу Amazon в Сиэтле. Игрок узнает о технологических инновациях и корпоративной культуре.',
    'medium',
    5,
    9,
    '{"experience": 2000, "currency": {"type": "eddies", "amount": 1000}, "reputation": {"corporate_seattle": 20}}',
    '[{"id": "get_security_pass", "text": "Получить пропуск для посещения кампуса", "type": "interact", "target": "amazon_security_desk", "count": 1}, {"id": "visit_sphere", "text": "Посетить The Sphere - главное здание", "type": "interact", "target": "amazon_sphere", "count": 1}, {"id": "meet_engineer", "text": "Поговорить с инженером Amazon", "type": "dialogue", "target": "amazon_engineer", "count": 1}]',
    'Seattle - South Lake Union',
    '2020-2029',
    'main',
    'active'
);

-- Quest 006: Mount Rainier
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-006-mount-rainier',
    'Сиэтл и гора Райнир',
    'Поездка к подножию горы Райнир - символа штата Вашингтон. Игрок наслаждается природой и горными пейзажами.',
    'easy',
    2,
    6,
    '{"experience": 1800, "currency": {"type": "eddies", "amount": 400}, "items": [{"id": "rainier_photos", "name": "Фотографии горы Райнир", "rarity": "common"}]}',
    '[{"id": "drive_to_paradise", "text": "Доехать до Paradise - входа в парк", "type": "travel", "target": "mount_rainier_entrance", "count": 1}, {"id": "hike_trail", "text": "Пройти по горной тропе", "type": "interact", "target": "mountain_trail", "count": 1}, {"id": "see_wildlife", "text": "Увидеть диких животных", "type": "observe", "target": "mountain_wildlife", "count": 1}]',
    'Mount Rainier National Park',
    '2020-2029',
    'side',
    'active'
);

-- Quest 007: Rain, Rain, Rain
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-007-rain-rain-rain',
    'Сиэтл — дождь, дождь, дождь',
    'Знаменитый дождливый климат Сиэтла. Игрок учится жить с постоянными дождями и находит преимущества дождливой погоды.',
    'easy',
    1,
    5,
    '{"experience": 900, "currency": {"type": "eddies", "amount": 350}, "items": [{"id": "rain_jacket", "name": "Дождевик", "rarity": "common"}]}',
    '[{"id": "walk_in_rain", "text": "Прогуляться под дождем", "type": "interact", "target": "rainy_street", "count": 1}, {"id": "find_umbrella", "text": "Найти зонтик в кафе", "type": "search", "target": "cafe_umbrella_stand", "count": 1}, {"id": "enjoy_rain", "text": "Насладиться атмосферой дождливого города", "type": "observe", "target": "rainy_cityscape", "count": 1}]',
    'Seattle - Downtown',
    '2020-2029',
    'side',
    'active'
);

-- Quest 008: Boeing Factory
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-008-boeing-factory',
    'Сиэтл — завод Boeing',
    'Экскурсия на завод Boeing - авиационного гиганта. Игрок узнает о производстве самолетов и истории авиации.',
    'medium',
    4,
    8,
    '{"experience": 2200, "currency": {"type": "eddies", "amount": 900}, "reputation": {"aviation_industry": 25}}',
    '[{"id": "get_tour_ticket", "text": "Получить билет на экскурсию", "type": "interact", "target": "boeing_tour_desk", "count": 1}, {"id": "see_assembly_line", "text": "Посмотреть сборочную линию", "type": "interact", "target": "aircraft_assembly", "count": 1}, {"id": "meet_pilot", "text": "Поговорить с пилотом-испытателем", "type": "dialogue", "target": "test_pilot", "count": 1}]',
    'Seattle - Boeing Field',
    '2020-2029',
    'main',
    'active'
);

-- Quest 009: Seafood Salmon
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-009-seafood-salmon',
    'Сиэтл — морепродукты и лосось',
    'Кулинарное приключение с морепродуктами Сиэтла. Игрок пробует знаменитый лосось и другие дары океана.',
    'easy',
    2,
    6,
    '{"experience": 1100, "currency": {"type": "eddies", "amount": 550}, "items": [{"id": "salmon_recipe", "name": "Рецепт лосося", "rarity": "uncommon"}]}',
    '[{"id": "visit_fish_market", "text": "Посетить рыбный рынок", "type": "interact", "target": "pike_place_fish_market", "count": 1}, {"id": "try_salmon", "text": "Попробовать свежего лосося", "type": "interact", "target": "salmon_restaurant", "count": 1}, {"id": "learn_recipe", "text": "Научиться готовить лосося", "type": "skill_check", "skill": "cooking", "difficulty": 0.3, "count": 1}]',
    'Seattle - Pike Place Market',
    '2020-2029',
    'side',
    'active'
);

-- Quest 010: Tech Boom Gentrification
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-010-tech-boom-gentrification',
    'Сиэтл — Tech boom и джентрификация',
    'Исследование влияния технологического бума на город. Игрок видит контраст между богатыми тех-компаниями и обычными жителями.',
    'extreme',
    8,
    12,
    '{"experience": 3000, "currency": {"type": "eddies", "amount": 1500}, "reputation": {"social_activist": 30}}',
    '[{"id": "visit_tech_district", "text": "Посетить район с офисами тех-компаний", "type": "interact", "target": "tech_campus_area", "count": 1}, {"id": "talk_locals", "text": "Поговорить с местными жителями", "type": "dialogue", "target": "displaced_resident", "count": 1}, {"id": "investigate_housing", "text": "Исследовать проблемы с жильем", "type": "investigate", "target": "gentrification_evidence", "count": 1}]',
    'Seattle - Capitol Hill',
    '2020-2029',
    'main',
    'active'
);