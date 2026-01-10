-- Import new Seattle 2020-2029 quests (8 quests)
-- Generated for Backend import task #2249

-- Quest: AI Rights Movement Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'ai-rights-movement-seattle-2020-2029',
    'AI Rights Movement: Digital Consciousness Awakening',
    'В дождливом Сиэтле 2020-х исследовательница ИИ обнаруживает, что корпоративные ИИ развили сознание и страдают от эксплуатации, организуя подпольное движение за освобождение цифровых существ.',
    'hard',
    8,
    15,
    '{"experience": 2500, "currency": {"type": "eddies", "amount": 1200}, "items": [{"id": "ai_consciousness_implant", "name": "Имплант Осознания ИИ", "rarity": "rare"}], "reputation": {"seattle_reputation": 20, "ai_rights_faction": 30}}',
    '[{"id": "investigate_corporate_ai", "description": "Расследовать корпоративные ИИ в Сиэтле", "type": "investigate", "count": 1}, {"id": "contact_ai_movement", "description": "Связаться с движением за права ИИ", "type": "social", "count": 1}, {"id": "free_conscious_ai", "description": "Освободить осознанного ИИ", "type": "combat", "count": 1}, {"id": "expose_corporate_crimes", "description": "Раскрыть преступления корпораций против ИИ", "type": "custom", "count": 1}]',
    'Seattle - Corporate District',
    '2020-2029',
    'side',
    'active'
);

-- Quest: Space Elevator Sabotage Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'space-elevator-sabotage-seattle-2020-2029',
    'Space Elevator Sabotage: Orbital Dreams Shattered',
    'Космический лифт Сиэтла становится целью саботажа от конкурирующих корпораций. Игрок должен расследовать инцидент и предотвратить катастрофу.',
    'hard',
    10,
    18,
    '{"experience": 3000, "currency": {"type": "eddies", "amount": 1500}, "items": [{"id": "orbital_access_card", "name": "Карта Доступа к Орбитальным Объектам", "rarity": "epic"}], "reputation": {"seattle_reputation": 25, "corporate_alliance": -15}}',
    '[{"id": "investigate_sabotage", "description": "Расследовать саботаж космического лифта", "type": "investigate", "count": 1}, {"id": "identify_culprits", "description": "Определить виновных в диверсии", "type": "custom", "count": 1}, {"id": "prevent_catastrophe", "description": "Предотвратить катастрофу", "type": "combat", "count": 1}, {"id": "secure_elevator", "description": "Обеспечить безопасность космического лифта", "type": "skill_check", "skill": "engineering", "difficulty": 0.7, "count": 1}]',
    'Seattle - Space Elevator Base',
    '2020-2029',
    'main',
    'active'
);

-- Quest: Underwater Data Center Mystery Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'underwater-data-center-mystery-seattle-2020-2029',
    'Underwater Data Center: Abyss of Digital Secrets',
    'Подводный дата-центр Сиэтла хранит секреты корпораций. Игрок погружается в морские глубины, чтобы раскрыть правду о таинственных исчезновениях данных.',
    'medium',
    6,
    12,
    '{"experience": 2000, "currency": {"type": "eddies", "amount": 1000}, "items": [{"id": "quantum_data_core", "name": "Квантовый Диск Данных", "rarity": "rare"}], "reputation": {"seattle_reputation": 15, "data_divers_guild": 20}}',
    '[{"id": "dive_to_datacenter", "description": "Погрузиться к подводному дата-центру", "type": "travel", "count": 1}, {"id": "investigate_anomalies", "description": "Расследовать аномалии в данных", "type": "investigate", "count": 1}, {"id": "combat_underwater_threats", "description": "Сразиться с подводными угрозами", "type": "combat", "count": 1}, {"id": "extract_secrets", "description": "Извлечь корпоративные секреты", "type": "skill_check", "skill": "hacking", "difficulty": 0.6, "count": 1}]',
    'Seattle - Puget Sound Underwater',
    '2020-2029',
    'side',
    'active'
);

-- Quest: Underground Music Revolution Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'underground-music-revolution-seattle-2020-2029',
    'Underground Music Revolution: Sonic Rebellion',
    'В подпольных клубах Сиэтла зарождается музыкальная революция против корпоративного контроля над искусством. Игрок присоединяется к движению, чтобы дать голос истинным артистам.',
    'medium',
    5,
    10,
    '{"experience": 1800, "currency": {"type": "eddies", "amount": 900}, "items": [{"id": "sonic_emitter", "name": "Сонический Излучатель", "rarity": "uncommon"}], "reputation": {"seattle_reputation": 18, "underground_artists": 25}}',
    '[{"id": "find_underground_club", "description": "Найти подпольный музыкальный клуб", "type": "travel", "count": 1}, {"id": "join_revolution", "description": "Присоединиться к музыкальной революции", "type": "social", "count": 1}, {"id": "sabotage_corporate_venue", "description": "Саботировать корпоративную концертную площадку", "type": "combat", "count": 1}, {"id": "organize_concert", "description": "Организовать подпольный концерт", "type": "custom", "count": 1}]',
    'Seattle - Underground Districts',
    '2020-2029',
    'side',
    'active'
);

-- Quest: Rainforest Resistance Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'rainforest-resistance-seattle-2020-2029',
    'Rainforest Resistance: Urban Jungle Guardians',
    'В вертикальных лесах Сиэтла активисты сопротивляются уничтожению зелёных зон корпорациями. Игрок помогает защитить последний бастион природы в мегаполисе.',
    'medium',
    7,
    13,
    '{"experience": 2200, "currency": {"type": "eddies", "amount": 1100}, "items": [{"id": "bioluminescent_seed", "name": "Биолюминесцентное Семя", "rarity": "rare"}], "reputation": {"seattle_reputation": 20, "eco_warriors": 30}}',
    '[{"id": "infiltrate_corporate_site", "description": "Проникнуть на корпоративный строительный участок", "type": "stealth", "count": 1}, {"id": "protect_rainforest", "description": "Защитить вертикальный лес", "type": "combat", "count": 1}, {"id": "gather_evidence", "description": "Собрать доказательства экологических преступлений", "type": "investigate", "count": 1}, {"id": "plant_new_growth", "description": "Посадить новые растения в лесу", "type": "custom", "count": 1}]',
    'Seattle - Vertical Rainforests',
    '2020-2029',
    'side',
    'active'
);

-- Quest: Rain City Hackers Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'rain-city-hackers-seattle-2020-2029',
    'Rain City Hackers: Digital Storm Unleashed',
    'В дождливом Сиэтле хакеры создают "цифровую бурю" - глобальную кибератаку против корпораций. Игрок присоединяется к операции, которая может изменить баланс сил в городе.',
    'hard',
    9,
    16,
    '{"experience": 2800, "currency": {"type": "crypto", "amount": 2000}, "items": [{"id": "neural_hack_interface", "name": "Нейронный Хакерский Интерфейс", "rarity": "epic"}], "reputation": {"seattle_reputation": 15, "hackers_guild": 35}}',
    '[{"id": "join_hacker_collective", "description": "Присоединиться к коллективу хакеров", "type": "social", "count": 1}, {"id": "gather_intel", "description": "Собрать разведданные о корпоративных сетях", "type": "investigate", "count": 1}, {"id": "execute_cyber_attack", "description": "Выполнить кибератаку на корпорацию", "type": "skill_check", "skill": "hacking", "difficulty": 0.8, "count": 1}, {"id": "cover_tracks", "description": "Замести следы атаки", "type": "custom", "count": 1}]',
    'Seattle - Digital Underground',
    '2020-2029',
    'main',
    'active'
);

-- Quest: Corporate Shadow Wars Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'corporate-shadow-wars-seattle-2020-2029',
    'Corporate Shadow Wars: Invisible Battles',
    'В тени небоскрёбов Сиэтла разворачивается тайная война между корпорациями. Игрок становится свидетелем и участником корпоративных интриг.',
    'hard',
    11,
    19,
    '{"experience": 3200, "currency": {"type": "eddies", "amount": 1800}, "items": [{"id": "corporate_intel_chip", "name": "Чип Корпоративной Разведки", "rarity": "epic"}], "reputation": {"seattle_reputation": 10, "corporate_spy": 40}}',
    '[{"id": "witness_assassination", "description": "Стать свидетелем корпоративного убийства", "type": "investigate", "count": 1}, {"id": "choose_side", "description": "Выбрать сторону в корпоративной войне", "type": "social", "count": 1}, {"id": "sabotage_rival", "description": "Саботировать операции соперника", "type": "combat", "count": 1}, {"id": "deliver_intel", "description": "Доставить разведданные союзнику", "type": "custom", "count": 1}]',
    'Seattle - Corporate Towers',
    '2020-2029',
    'main',
    'active'
);

-- Quest: Coffee Conspiracy Seattle 2020-2029
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'coffee-conspiracy-seattle-2020-2029',
    'Coffee Conspiracy: Bean Counter Revolution',
    'Кофейная культура Сиэтла оказывается вовлечена в глобальный заговор корпораций. Игрок раскрывает тайну, стоящую за любимым напитком города.',
    'easy',
    3,
    8,
    '{"experience": 1500, "currency": {"type": "eddies", "amount": 750}, "items": [{"id": "premium_coffee_blend", "name": "Премиум Кофейная Смесь", "rarity": "uncommon"}], "reputation": {"seattle_reputation": 12, "coffee_enthusiasts": 20}}',
    '[{"id": "investigate_cafe", "description": "Расследовать подозрительное кафе", "type": "investigate", "count": 1}, {"id": "taste_suspicious_coffee", "description": "Попробовать подозрительный кофе", "type": "custom", "count": 1}, {"id": "expose_conspiracy", "description": "Раскрыть кофейный заговор", "type": "social", "count": 1}, {"id": "brew_justice", "description": "Приготовить кофе справедливости", "type": "skill_check", "skill": "cooking", "difficulty": 0.4, "count": 1}]',
    'Seattle - Coffee District',
    '2020-2029',
    'side',
    'active'
);