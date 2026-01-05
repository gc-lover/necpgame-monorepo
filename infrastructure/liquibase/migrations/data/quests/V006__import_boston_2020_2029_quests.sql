-- Import Boston 2020-2029 Quests
-- Generated on 2025-12-28
-- Issue: Boston quests import

-- Quest: canon-quest-boston-2029-001-freedom-trail
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Freedom Trail',
    'Квест по прохождению исторического маршрута Freedom Trail через 16 ключевых точек Бостона, от Boston Common до USS Constitution.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-001-freedom-trail", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-001-freedom-trail.yaml"}',
    '{"experience": 5000, "currency": {"type": "eddies", "amount": 1000}, "reputation": {"history": 30}}',
    '[{"id": "start_boston_common", "title": "Начать маршрут в Boston Common", "description": "Посетить парк Boston Common и ознакомиться с легендой Freedom Trail", "type": "interact", "order": 1}, {"id": "state_house", "title": "Посетить Massachusetts State House", "description": "Изучить архитектуру и символизм здания законодательного собрания", "type": "interact", "order": 2}, {"id": "granary_burying_ground", "title": "Исследовать Granary Burying Ground", "description": "Посетить кладбище с могилами отцов-основателей", "type": "interact", "order": 3}, {"id": "old_south_meeting_house", "title": "Посетить Old South Meeting House", "description": "Изучить место собрания, предшествовавшего Boston Tea Party", "type": "interact", "order": 4}, {"id": "boston_massacre_site", "title": "Посетить место Boston Massacre", "description": "Изучить историческую точку начала революции", "type": "interact", "order": 5}, {"id": "paul_revere_house", "title": "Посетить дом Пола Ревира", "description": "Исследовать дом серебряных дел мастера и патриота", "type": "interact", "order": 6}, {"id": "old_north_church", "title": "Посетить Old North Church", "description": "Изучить церковь, где зажгли сигнальные огни", "type": "interact", "order": 7}, {"id": "uss_constitution", "title": "Завершить маршрут у USS Constitution", "description": "Посетить старейший действующий корабль ВМС США", "type": "interact", "order": 8}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-002-boston-tea-party
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Boston Tea Party',
    'Квест, исследующий события Boston Tea Party и их влияние на Американскую революцию.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-002-boston-tea-party", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-002-boston-tea-party.yaml"}',
    '{"experience": 4500, "currency": {"type": "eddies", "amount": 900}, "reputation": {"history": 25}}',
    '[{"id": "griffins_wharf", "title": "Посетить Griffins Wharf", "description": "Найти место, где произошла Boston Tea Party", "type": "interact", "order": 1}, {"id": "tea_party_reenactment", "title": "Посмотреть реконструкцию событий", "description": "Изучить интерактивную реконструкцию событий Tea Party", "type": "interact", "order": 2}, {"id": "east_india_company", "title": "Исследовать роль East India Company", "description": "Понять экономические причины конфликта с британской короной", "type": "skill_check", "skill": "intelligence", "difficulty": 0.4, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-003-harvard-mit
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Harvard и MIT',
    'Квест, исследующий академическую элиту Бостона - университеты Harvard и MIT.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-003-harvard-mit", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-003-harvard-mit.yaml"}',
    '{"experience": 6000, "currency": {"type": "eddies", "amount": 1200}, "reputation": {"education": 40}}',
    '[{"id": "harvard_campus", "title": "Исследовать кампус Harvard", "description": "Посетить старейший университет США", "type": "interact", "order": 1}, {"id": "mit_campus", "title": "Посетить кампус MIT", "description": "Исследовать технологический центр Бостона", "type": "interact", "order": 2}, {"id": "academic_rivalry", "title": "Изучить академическое соперничество", "description": "Понять динамику между двумя университетами", "type": "skill_check", "skill": "perception", "difficulty": 0.5, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-004-fenway-park
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Fenway Park',
    'Квест о легендарном бейсбольном стадионе Fenway Park и культуре Boston Red Sox.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-004-fenway-park", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-004-fenway-park.yaml"}',
    '{"experience": 4000, "currency": {"type": "eddies", "amount": 800}, "reputation": {"sports": 20}}',
    '[{"id": "fenway_tour", "title": "Пройти тур по Fenway Park", "description": "Исследовать старейший стадион MLB", "type": "interact", "order": 1}, {"id": "green_monster", "title": "Посетить Green Monster", "description": "Изучить легендарную левую стенку поля", "type": "interact", "order": 2}, {"id": "red_sox_history", "title": "Изучить историю Boston Red Sox", "description": "Понять культуру и традиции команды", "type": "skill_check", "skill": "charisma", "difficulty": 0.3, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-005-clam-chowder
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Клам-Чаудер',
    'Квест о традиционной бостонской кухне и культуре морепродуктов.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-005-clam-chowder", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-005-clam-chowder.yaml"}',
    '{"experience": 3000, "currency": {"type": "eddies", "amount": 600}, "reputation": {"culinary": 15}}',
    '[{"id": "union_oyster_house", "title": "Посетить Union Oyster House", "description": "Старейший ресторан Америки с 1826 года", "type": "interact", "order": 1}, {"id": "clam_chowder_recipe", "title": "Изучить рецепт клам-чаудера", "description": "Понять традиционную бостонскую кухню", "type": "skill_check", "skill": "cooking", "difficulty": 0.4, "order": 2}, {"id": "seafood_market", "title": "Посетить рынок морепродуктов", "description": "Исследовать свежие морепродукты Бостона", "type": "interact", "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-006-boston-accent
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Boston Accent',
    'Квест об уникальном бостонском акценте и его культурном значении.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-006-boston-accent", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-006-boston-accent.yaml"}',
    '{"experience": 2500, "currency": {"type": "eddies", "amount": 500}, "reputation": {"linguistics": 10}}',
    '[{"id": "accent_recognition", "title": "Научиться распознавать Boston accent", "description": "Изучить характерные особенности бостонского произношения", "type": "skill_check", "skill": "perception", "difficulty": 0.3, "order": 1}, {"id": "local_conversation", "title": "Поговорить с местными жителями", "description": "Вовлечься в разговор с носителями бостонского акцента", "type": "interact", "order": 2}, {"id": "cultural_significance", "title": "Понять культурное значение", "description": "Изучить исторические и социальные аспекты акцента", "type": "skill_check", "skill": "intelligence", "difficulty": 0.4, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-007-paul-revere-ride
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Скачка Пола Ревира',
    'Квест о легендарной скачке Пола Ревира и событиях, предшествовавших революции.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-007-paul-revere-ride", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-007-paul-revere-ride.yaml"}',
    '{"experience": 5500, "currency": {"type": "eddies", "amount": 1100}, "reputation": {"history": 35}}',
    '[{"id": "revere_house", "title": "Посетить дом Пола Ревира", "description": "Исследовать дом серебряных дел мастера", "type": "interact", "order": 1}, {"id": "old_north_church", "title": "Посетить Old North Church", "description": "Изучить церковь с сигнальными огнями", "type": "interact", "order": 2}, {"id": "ride_route", "title": "Проследить маршрут скачки", "description": "Воссоздать путь Ревира до Лексингтона", "type": "interact", "order": 3}, {"id": "revolutionary_context", "title": "Понять революционный контекст", "description": "Изучить события, приведшие к скачке", "type": "skill_check", "skill": "intelligence", "difficulty": 0.5, "order": 4}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-008-samuel-adams-beer
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Пиво Сэмюэла Адамса',
    'Квест о пивоваренной традиции Бостона и роли Сэмюэла Адамса в революции.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-008-samuel-adams-beer", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-008-samuel-adams-beer.yaml"}',
    '{"experience": 4000, "currency": {"type": "eddies", "amount": 800}, "reputation": {"culinary": 25}}',
    '[{"id": "sam_adams_brewery", "title": "Посетить пивоварню Sam Adams", "description": "Исследовать современную пивоварню в Бостоне", "type": "interact", "order": 1}, {"id": "colonial_beer_tradition", "title": "Изучить колониальную пивную традицию", "description": "Понять роль пива в колониальном обществе", "type": "skill_check", "skill": "history", "difficulty": 0.4, "order": 2}, {"id": "sam_adams_role", "title": "Изучить роль Сэмюэла Адамса", "description": "Понять вклад Адамса в революцию", "type": "skill_check", "skill": "intelligence", "difficulty": 0.5, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-009-boston-marathon
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Boston Marathon',
    'Квест о легендарном Бостонском марафоне и его культурном значении.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-009-boston-marathon", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-009-boston-marathon.yaml"}',
    '{"experience": 6000, "currency": {"type": "eddies", "amount": 1200}, "reputation": {"athletics": 40}}',
    '[{"id": "marathon_history", "title": "Изучить историю марафона", "description": "Понять происхождение и традиции Boston Marathon", "type": "skill_check", "skill": "history", "difficulty": 0.3, "order": 1}, {"id": "boyleston_street", "title": "Посетить Boylston Street", "description": "Изучить финишную прямую марафона", "type": "interact", "order": 2}, {"id": "hopkinton_start", "title": "Посетить старт в Hopkinton", "description": "Исследовать место начала марафона", "type": "interact", "order": 3}, {"id": "runner_culture", "title": "Понять культуру бегунов", "description": "Изучить сообщество бегунов Бостона", "type": "skill_check", "skill": "charisma", "difficulty": 0.4, "order": 4}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-010-boston-massacre
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Boston Massacre',
    'Квест о событиях Boston Massacre и их роли в Американской революции.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-010-boston-massacre", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-010-boston-massacre.yaml"}',
    '{"experience": 5000, "currency": {"type": "eddies", "amount": 1000}, "reputation": {"history": 30}}',
    '[{"id": "massacre_site", "title": "Посетить место событий", "description": "Найти историческую точку Boston Massacre", "type": "interact", "order": 1}, {"id": "paul_revere_print", "title": "Изучить гравюру Пола Ревира", "description": "Понять пропагандистское значение гравюры", "type": "skill_check", "skill": "perception", "difficulty": 0.5, "order": 2}, {"id": "british_perspective", "title": "Понять британскую точку зрения", "description": "Изучить контекст событий с британской стороны", "type": "skill_check", "skill": "intelligence", "difficulty": 0.6, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-011-smart-city-initiative
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Smart City Initiative',
    'Квест о технологических инновациях Бостона и проектах умного города.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-011-smart-city-initiative", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-011-smart-city-initiative.yaml"}',
    '{"experience": 7000, "currency": {"type": "eddies", "amount": 1400}, "reputation": {"technology": 45}}',
    '[{"id": "mit_media_lab", "title": "Посетить MIT Media Lab", "description": "Исследовать центр инноваций в MIT", "type": "interact", "order": 1}, {"id": "smart_infrastructure", "title": "Изучить умную инфраструктуру", "description": "Понять системы умного города Бостона", "type": "skill_check", "skill": "engineering", "difficulty": 0.6, "order": 2}, {"id": "future_vision", "title": "Понять видение будущего", "description": "Изучить долгосрочные планы развития города", "type": "skill_check", "skill": "intelligence", "difficulty": 0.5, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-boston-2029-012-cyber-security-research
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    'Бостон 2020-2029 — Cyber Security Research',
    'Квест о кибербезопасности и исследовательских проектах в Бостоне.',
    1,
    NULL,
    'active',
    '{"id": "canon-quest-boston-2029-012-cyber-security-research", "version": "2.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/boston/2020-2029/quest-012-cyber-security-research.yaml"}',
    '{"experience": 8000, "currency": {"type": "eddies", "amount": 1600}, "reputation": {"cybersecurity": 50}}',
    '[{"id": "mit_cybersecurity_lab", "title": "Посетить лабораторию кибербезопасности MIT", "description": "Исследовать передовые проекты в области безопасности", "type": "interact", "order": 1}, {"id": "harvard_privacy_research", "title": "Изучить исследования приватности Harvard", "description": "Понять этические аспекты кибербезопасности", "type": "skill_check", "skill": "intelligence", "difficulty": 0.7, "order": 2}, {"id": "threat_analysis", "title": "Провести анализ угроз", "description": "Изучить современные киберугрозы", "type": "skill_check", "skill": "hacking", "difficulty": 0.6, "order": 3}]'
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;
