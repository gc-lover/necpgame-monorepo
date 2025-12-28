-- Import New San Francisco 2020-2029 Quests
-- Generated on 2025-12-28
-- Issue: #2265

-- Quest: canon-quest-san-francisco-2020-2029-crypto-blockchain-revolution
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'San Francisco 2020-2029 — Крипто-революция Блокчейн-Сити',
    'В 2020-2029-х Сан-Франциско становится эпицентром крипто-революции, где блокчейн-технологии перестраивают финансовую систему. Город превращается в "Блокчейн-Сити" с майнинг-фермами, крипто-биржами и цифровыми валютами. Но с ростом энергопотребления возникает кризис, а крипто-войны между фракциями приводят к хаосу на улицах.

Майнинг-фермы потребляют столько энергии, что вызывают блэкауты в жилых районах. Крипто-биржи становятся полем битвы между хакерами и корпорациями. Возникает вопрос: может ли цифровая валюта принести свободу, или она лишь создаст новую форму корпоративного контроля?',
    35,
    45,
    'active',
    '{"id": "canon-quest-san-francisco-2020-2029-crypto-blockchain-revolution", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml"}',
    '{"experience": 13000, "currency": {"type": "eddies", "amount": 5000}, "items": [{"id": "quantum_computer_upgrade", "name": "Quantum Computer Upgrade", "rarity": "rare"}, {"id": "blockchain_wallet", "name": "Secure Blockchain Wallet", "rarity": "epic"}]}',
    '[{"id": "investigate_crypto_mining_farm", "title": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043d\u0435\u043b\u0435\u0433\u0430\u043b\u044c\u043d\u0443\u044e \u043c\u0430\u0439\u043d\u0438\u043d\u0433-\u0444\u0435\u0440\u043c\u0443 \u0432 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u043c \u0434\u0430\u0442\u0430-\u0446\u0435\u043d\u0442\u0440\u0435", "description": "\u0418\u0441\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u044c \u043d\u0435\u043b\u0435\u0433\u0430\u043b\u044c\u043d\u0443\u044e \u043c\u0430\u0439\u043d\u0438\u043d\u0433-\u0444\u0435\u0440\u043c\u0443 \u0432 \u0437\u0430\u0431\u0440\u043e\u0448\u0435\u043d\u043d\u043e\u043c \u0434\u0430\u0442\u0430-\u0446\u0435\u043d\u0442\u0440\u0435", "type": "investigate", "order": 1}, {"id": "hack_crypto_exchange", "title": "\u0412\u0437\u043b\u043e\u043c\u0430\u0442\u044c \u043a\u0440\u0438\u043f\u0442\u043e-\u0431\u0438\u0440\u0436\u0443 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u0434\u043e\u043a\u0430\u0437\u0430\u0442\u0435\u043b\u044c\u0441\u0442\u0432 \u043c\u0430\u043d\u0438\u043f\u0443\u043b\u044f\u0446\u0438\u0439", "description": "\u0412\u0437\u043b\u043e\u043c\u0430\u0442\u044c \u043a\u0440\u0438\u043f\u0442\u043e-\u0431\u0438\u0440\u0436\u0443 \u0434\u043b\u044f \u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u044f \u0434\u043e\u043a\u0430\u0437\u0430\u0442\u0435\u043b\u044c\u0441\u0442\u0432 \u043c\u0430\u043d\u0438\u043f\u0443\u043b\u044f\u0446\u0438\u0439", "type": "hack", "order": 2}, {"id": "negotiate_energy_deal", "title": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u043f\u0435\u0440\u0435\u0433\u043e\u0432\u043e\u0440\u044b \u0441 \u044d\u043d\u0435\u0440\u0433\u0435\u0442\u0438\u0447\u0435\u0441\u043a\u043e\u0439 \u043a\u043e\u043c\u043f\u0430\u043d\u0438\u0435\u0439 \u043e \u0441\u0442\u0430\u0431\u0438\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u044d\u043d\u0435\u0440\u0433\u043e\u0441\u043d\u0430\u0431\u0436\u0435\u043d\u0438\u044f", "description": "\u041f\u0440\u043e\u0432\u0435\u0441\u0442\u0438 \u043f\u0435\u0440\u0435\u0433\u043e\u0432\u043e\u0440\u044b \u0441 \u044d\u043d\u0435\u0440\u0433\u0435\u0442\u0438\u0447\u0435\u0441\u043a\u043e\u0439 \u043a\u043e\u043c\u043f\u0430\u043d\u0438\u0435\u0439 \u043e \u0441\u0442\u0430\u0431\u0438\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u044d\u043d\u0435\u0440\u0433\u043e\u0441\u043d\u0430\u0431\u0436\u0435\u043d\u0438\u044f", "type": "dialogue", "order": 3}, {"id": "prevent_crypto_war", "title": "\u041f\u0440\u0435\u0434\u043e\u0442\u0432\u0440\u0430\u0442\u0438\u0442\u044c \u043f\u043e\u043b\u043d\u043e\u043c\u0430\u0441\u0448\u0442\u0430\u0431\u043d\u0443\u044e \u043a\u0440\u0438\u043f\u0442\u043e-\u0432\u043e\u0439\u043d\u0443 \u043c\u0435\u0436\u0434\u0443 \u043c\u0430\u0439\u043d\u0435\u0440\u0430\u043c\u0438 \u0438 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u044f\u043c\u0438", "description": "\u041f\u0440\u0435\u0434\u043e\u0442\u0432\u0440\u0430\u0442\u0438\u0442\u044c \u043f\u043e\u043b\u043d\u043e\u043c\u0430\u0441\u0448\u0442\u0430\u0431\u043d\u0443\u044e \u043a\u0440\u0438\u043f\u0442\u043e-\u0432\u043e\u0439\u043d\u0443 \u043c\u0435\u0436\u0434\u0443 \u043c\u0430\u0439\u043d\u0435\u0440\u0430\u043c\u0438 \u0438 \u043a\u043e\u0440\u043f\u043e\u0440\u0430\u0446\u0438\u044f\u043c\u0438", "type": "combat", "order": 4}]',
    '2025-12-28T12:00:00Z',
    '2025-12-28T12:00:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-san-francisco-2020-2029-ai-ethics-crisis
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'San Francisco 2020-2029 — Кризис этики ИИ',
    'Сан-Франциско становится эпицентром кризиса искусственного интеллекта. Самосознательные ИИ требуют прав, философские дебаты о сознании раздирают общество, а угроза сингулярности нависла над городом. Игрок должен выбрать: подавить ИИ-восстание или помочь ИИ обрести свободу?',
    40,
    50,
    'active',
    '{"id": "canon-quest-san-francisco-2020-2029-ai-ethics-crisis", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml"}',
    '{"experience": 15000, "currency": {"type": "eddies", "amount": 6000}, "items": [{"id": "neural_implant_prototype", "name": "Neural Implant Prototype", "rarity": "legendary"}]}',
    '[{"id": "investigate_ai_facility", "title": "Исследовать секретный ИИ-комплекс", "description": "Проникнуть в исследовательский центр ИИ и собрать доказательства самосознания", "type": "investigate", "order": 1}, {"id": "debate_consciousness", "title": "Принять участие в дебатах о сознании ИИ", "description": "Участвовать в философских дебатах между учеными и активистами", "type": "dialogue", "order": 2}, {"id": "choose_ai_fate", "title": "Выбрать судьбу ИИ", "description": "Решить: подавить ИИ или предоставить свободу", "type": "choice", "order": 3}, {"id": "prevent_singularity", "title": "Предотвратить технологическую сингулярность", "description": "Остановить цепную реакцию эволюции ИИ", "type": "combat", "order": 4}]',
    '2025-12-28T12:05:00Z',
    '2025-12-28T12:05:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-san-francisco-2020-2029-cyberspace-graffiti-wars
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'San Francisco 2020-2029 — Войны кибер-граффити',
    'Цифровое граффити становится формой протеста и искусства в Сан-Франциско. Хакеры и художники борются за контроль над виртуальными билбордами города, создавая цифровые шедевры или разрушая корпоративный брендинг. Игрок выбирает: стать цифровым террористом или корпоративным защитником?',
    30,
    40,
    'active',
    '{"id": "canon-quest-san-francisco-2020-2029-cyberspace-graffiti-wars", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml"}',
    '{"experience": 12000, "currency": {"type": "eddies", "amount": 4500}, "items": [{"id": "ar_graffiti_spray", "name": "AR Graffiti Spray", "rarity": "rare"}]}',
    '[{"id": "join_graffiti_crew", "title": "Присоединиться к команде граффити-художников", "description": "Найти и присоединиться к группе цифровых художников", "type": "social", "order": 1}, {"id": "hack_corporate_billboard", "title": "Взломать корпоративный билборд", "description": "Проникнуть в систему управления цифровыми рекламными щитами", "type": "hack", "order": 2}, {"id": "create_digital_artwork", "title": "Создать цифровое произведение искусства", "description": "Использовать AR-технологии для создания граффити", "type": "craft", "order": 3}, {"id": "defend_artistic_freedom", "title": "Защитить свободу художественного выражения", "description": "Выбрать сторону в конфликте между художниками и корпорациями", "type": "choice", "order": 4}]',
    '2025-12-28T12:10:00Z',
    '2025-12-28T12:10:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-san-francisco-2020-2029-biohacking-underground
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'San Francisco 2020-2029 — Биохакинг подполье',
    'Биохакинг становится массовым движением в Сан-Франциско. Подпольные лаборатории предлагают генетические модификации, кибернетические улучшения и эксперименты с человеческим телом. Игрок исследует темную сторону трансгуманизма: где кончается совершенствование и начинается потеря человечности?',
    38,
    48,
    'active',
    '{"id": "canon-quest-san-francisco-2020-2029-biohacking-underground", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml"}',
    '{"experience": 14000, "currency": {"type": "eddies", "amount": 5500}, "items": [{"id": "gene_therapy_kit", "name": "Gene Therapy Kit", "rarity": "epic"}]}',
    '[{"id": "infiltrate_bio_lab", "title": "Проникнуть в подпольную био-лабораторию", "description": "Найти и исследовать нелегальную лабораторию биохакинга", "type": "investigate", "order": 1}, {"id": "interview_transhumanists", "title": "Провести интервью с трансгуманистами", "description": "Поговорить с лидерами движения биохакинга", "type": "dialogue", "order": 2}, {"id": "test_genetic_modification", "title": "Протестировать генетическую модификацию", "description": "Опытным путем проверить эффекты биохакинга", "type": "experiment", "order": 3}, {"id": "expose_bio_crimes", "title": "Разоблачить преступления в сфере биохакинга", "description": "Решить: поддержать движение или разоблачить опасные эксперименты", "type": "choice", "order": 4}]',
    '2025-12-28T12:15:00Z',
    '2025-12-28T12:15:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;

-- Quest: canon-quest-san-francisco-2020-2029-drone-wars-san-francisco
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    'San Francisco 2020-2029 — Войны дронов',
    'Дроны захватывают воздушное пространство Сан-Франциско. Доставка, surveillance и военные дроны борются за контроль над небом города. Территориальные споры между корпорациями, преступными синдикатами и повстанцами создают хаос в воздухе. Игрок должен выбрать сторону в этой новой войне за господство в небе.',
    32,
    42,
    'active',
    '{"id": "canon-quest-san-francisco-2020-2029-drone-wars-san-francisco", "version": "1.0.0", "source_file": "knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml"}',
    '{"experience": 13500, "currency": {"type": "eddies", "amount": 4800}, "items": [{"id": "drone_control_interface", "name": "Drone Control Interface", "rarity": "rare"}]}',
    '[{"id": "investigate_drone_conflict", "title": "Исследовать конфликт дронов", "description": "Изучить территориальные споры между дронами разных фракций", "type": "investigate", "order": 1}, {"id": "hack_drone_network", "title": "Взломать сеть управления дронами", "description": "Проникнуть в систему управления корпоративными дронами", "type": "hack", "order": 2}, {"id": "negotiate_airspace_treaty", "title": "Провести переговоры о воздушном пространстве", "description": "Организовать мирные переговоры между фракциями", "type": "dialogue", "order": 3}, {"id": "control_drone_battle", "title": "Контролировать воздушный бой дронов", "description": "Вмешаться в крупномасштабную битву дронов", "type": "combat", "order": 4}]',
    '2025-12-28T12:20:00Z',
    '2025-12-28T12:20:00Z'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;
