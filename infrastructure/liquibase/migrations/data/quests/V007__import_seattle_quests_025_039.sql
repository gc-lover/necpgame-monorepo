-- Import Seattle quests 025-039 to quest_definitions table
-- Generated for Backend import task #2273

-- Quest 025: Seattle 2020-2029 — Империя теневой экономики
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-025-shadow-economy-empire',
    'Seattle 2020-2029 — Империя теневой экономики',
    'Quest 025 description - Quest 025 Shadow Economy Empire',
    'medium',
    31,
    41,
    '{"experience": 100}',
    '[{"id": "navigate_darknet_market", "text": "Навигировать по даркнет-маркету Сиэтла", "type": "travel", "target": "underground_crypto_exchange", "count": 1, "optional": false}, {"id": "trace_money_laundering", "text": "Проследить схемы отмывания денег", "type": "investigate", "target": "crypto_laundering_network", "count": 3, "optional": false}, {"id": "rescue_exploited_workers", "text": "Спаси эксплуатируемых рабочих цифровых ферм", "type": "rescue", "target": "crypto_mining_slaves", "count": 2, "optional": false}, {"id": "expose_shadow_banker", "text": "Разоблачить теневое банковское предприятие", "type": "hack", "target": "decentralized_bank_system", "count": 1, "optional": false}, {"id": "choose_economic_future", "text": "Принять решение о будущем экономики", "type": "choice", "target": "financial_system_reform", "count": 1, "optional": true}]',
    'Seattle',
    '2020-2029',
    'main',
    'active'
);

-- Quest 026: Сиэтл 2020-2029 — Теневые хакеры Amazon
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-026-amazon-shadow-hackers',
    'Сиэтл 2020-2029 — Теневые хакеры Amazon',
    'Quest 026 description - Quest 026 Amazon Shadow Hackers',
    'medium',
    10,
    25,
    '{"experience": 5000, "currency": {"type": "eddies", "amount": 2500}, "reputation": {"street_cred": 30, "corporate_hate": 15}, "items": [{"id": "amazon_data_chip", "name": "Чип с данными Amazon", "type": "quest_item", "rarity": "rare"}]}',
    '[{"id": "meet_hacker_contact", "text": "Найти контакт хакерской группы в районе Pike Place Market", "type": "interact", "target": "street_hacker_contact", "count": 1, "optional": false}, {"id": "infiltrate_amazon_campus", "text": "Проникнуть на территорию Amazon HQ, избегая патрулей охраны", "type": "interact", "target": "amazon_security_perimeter", "count": 1, "optional": false}, {"id": "hack_corporate_network", "text": "Взломать корпоративную сеть Amazon и скачать ценные данные", "type": "skill_check", "target": "network_intrusion", "count": 1, "optional": false, "skill": "hacking", "difficulty": 0.7}, {"id": "extract_data", "text": "Извлечь украденные данные из системы безопасности", "type": "interact", "target": "data_extraction_terminal", "count": 1, "optional": false}, {"id": "escape_pursuit", "text": "Сбежать от корпоративной охраны через канализацию Сиэтла", "type": "interact", "target": "underground_escape_route", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 027: Сиэтл 2020-2029 — Подпольный риппердок
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-027-underground-ripperdoc',
    'Сиэтл 2020-2029 — Подпольный риппердок',
    'Quest 027 description - Quest 027 Underground Ripperdoc',
    'medium',
    15,
    30,
    '{"experience": 7500, "currency": {"type": "eddies", "amount": 1800}, "reputation": {"street_cred": 25, "medical_black_market": 40}, "items": [{"id": "experimental_implant", "name": "Экспериментальный имплант", "type": "cybernetic", "rarity": "epic"}, {"id": "ripperdoc_contacts", "name": "Контакты подпольных риппердоков", "type": "quest_item", "rarity": "uncommon"}]}',
    '[{"id": "find_underground_clinic", "text": "Найти вход в подпольную клинику риппердока в канализации под Pioneer Square", "type": "interact", "target": "underground_clinic_entrance", "count": 1, "optional": false}, {"id": "consult_ripperdoc", "text": "Проконсультироваться с риппердоком о доступных имплантах", "type": "interact", "target": "ripperdoc_consultation", "count": 1, "optional": false}, {"id": "undergo_implant_procedure", "text": "Пройти процедуру установки экспериментального импланта", "type": "skill_check", "target": "implant_surgery", "count": 1, "optional": false, "skill": "body", "difficulty": 0.6}, {"id": "defend_clinic", "text": "Защитить клинику от атаки корпоративных наемников", "type": "combat", "target": "corporate_raiders", "count": 5, "optional": false}, {"id": "complete_operation", "text": "Завершить операцию и получить награду от риппердока", "type": "interact", "target": "operation_completion", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 028: Сиэтл 2020-2029 — Эко-протесты против корпораций
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-028-eco-protest-revolution',
    'Сиэтл 2020-2029 — Эко-протесты против корпораций',
    'Quest 028 description - Quest 028 Eco Protest Revolution',
    'medium',
    8,
    20,
    '{"experience": 4200, "currency": {"type": "eddies", "amount": 1200}, "reputation": {"environmental_awareness": 35, "corporate_hate": 20, "community_respect": 15}, "items": [{"id": "eco_activist_badge", "name": "Значок эко-активиста", "type": "accessory", "rarity": "uncommon"}]}',
    '[{"id": "join_eco_movement", "text": "Найти и присоединиться к эко-активистам в районе Discovery Park", "type": "interact", "target": "eco_activist_camp", "count": 1, "optional": false}, {"id": "participate_peaceful_protest", "text": "Участвовать в мирном протесте против химического завода корпорации", "type": "interact", "target": "corporate_chemical_plant", "count": 1, "optional": false}, {"id": "gather_evidence", "text": "Собрать доказательства корпоративного загрязнения окружающей среды", "type": "interact", "target": "pollution_evidence", "count": 3, "optional": false}, {"id": "sabotage_pollution_operation", "text": "Повредить оборудование для очистки сточных вод на заводе", "type": "skill_check", "target": "sabotage_equipment", "count": 1, "optional": true, "skill": "technical", "difficulty": 0.5}, {"id": "protect_eco_leader", "text": "Защитить лидера эко-движения от корпоративных наемников", "type": "combat", "target": "corporate_thugs", "count": 3, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 029: Сиэтл 2020-2029 — Виртуальные сны нейронных сетей
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-029-virtual-reality-neural-dreams',
    'Сиэтл 2020-2029 — Виртуальные сны нейронных сетей',
    'Quest 029 description - Quest 029 Virtual Reality Neural Dreams',
    'medium',
    20,
    35,
    '{"experience": 8500, "currency": {"type": "eddies", "amount": 3200}, "reputation": {"tech_savvy": 40, "neural_research": 50, "digital_consciousness": 25}, "items": [{"id": "neural_interface_prototype", "name": "Прототип нейро-интерфейса", "type": "cybernetic", "rarity": "legendary"}, {"id": "digital_dream_data", "name": "Данные виртуальных снов", "type": "data_chip", "rarity": "rare"}]}',
    '[{"id": "discover_vr_lab", "text": "Найти подпольную VR-лабораторию в заброшенном здании South Lake Union", "type": "interact", "target": "abandoned_vr_lab", "count": 1, "optional": false}, {"id": "install_neural_interface", "text": "Пройти процедуру установки экспериментального нейро-интерфейса", "type": "skill_check", "target": "neural_implant_installation", "count": 1, "optional": false, "skill": "intelligence", "difficulty": 0.8}, {"id": "enter_virtual_dream", "text": "Погрузиться в виртуальный мир снов ИИ через нейро-интерфейс", "type": "interact", "target": "vr_dream_entry", "count": 1, "optional": false}, {"id": "navigate_dream_world", "text": "Исследовать виртуальный мир и найти других участников эксперимента", "type": "interact", "target": "dream_world_navigation", "count": 3, "optional": false}, {"id": "defeat_digital_horrors", "text": "Победить цифровые кошмары и аномалии в виртуальном пространстве", "type": "combat", "target": "digital_anomalies", "count": 4, "optional": false}, {"id": "save_other_participants", "text": "Спаси других участников эксперимента от цифрового безумия", "type": "interact", "target": "participant_rescue", "count": 2, "optional": false}, {"id": "exit_virtual_reality", "text": "Безопасно выйти из виртуального мира и сохранить сознание", "type": "skill_check", "target": "consciousness_extraction", "count": 1, "optional": false, "skill": "willpower", "difficulty": 0.7}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 030: Сиэтл 2020-2029 — Уличные гонки в дождевом городе
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-030-rain-city-street-racing',
    'Сиэтл 2020-2029 — Уличные гонки в дождевом городе',
    'Quest 030 description - Quest 030 Rain City Street Racing',
    'medium',
    12,
    28,
    '{"experience": 6200, "currency": {"type": "eddies", "amount": 2800}, "reputation": {"street_cred": 35, "racing_underground": 45, "vehicle_expertise": 20}, "items": [{"id": "rain_tuned_engine", "name": "Дождевой тюнинг-двигатель", "type": "vehicle_part", "rarity": "rare"}, {"id": "racing_league_card", "name": "Карта гоночной лиги", "type": "access_card", "rarity": "uncommon"}]}',
    '[{"id": "join_racing_league", "text": "Найти и присоединиться к подпольной гоночной лиге в районе SoDo", "type": "interact", "target": "underground_racing_league", "count": 1, "optional": false}, {"id": "modify_vehicle", "text": "Модифицировать транспорт для гонок в дождевых условиях Сиэтла", "type": "skill_check", "target": "vehicle_modification", "count": 1, "optional": false, "skill": "technical", "difficulty": 0.6}, {"id": "win_first_race", "text": "Выиграть первую гонку в дождевых условиях по улицам города", "type": "racing", "target": "rain_street_race", "count": 1, "optional": false}, {"id": "sabotage_rival", "text": "Опционально: саботировать транспорт корпоративного соперника", "type": "skill_check", "target": "rival_sabotage", "count": 1, "optional": true, "skill": "stealth", "difficulty": 0.7}, {"id": "survive_ambush", "text": "Выжить в засаде от корпоративных наемников после победы", "type": "combat", "target": "corporate_ambush", "count": 4, "optional": false}, {"id": "claim_victory", "text": "Забрать приз и получить репутацию в гоночном подполье", "type": "interact", "target": "victory_claim", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 031: Сиэтл 2020-2029 — Нейронная сеть хакеров
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-031-neural-net-hackers',
    'Сиэтл 2020-2029 — Нейронная сеть хакеров',
    'Quest 031 description - Quest 031 Neural Net Hackers',
    'medium',
    15,
    25,
    '{"experience": 5000, "currency": {"type": "eddies", "amount": 2500}, "reputation": {"corporate": 200, "underground": -100}, "items": [{"id": "neural_implant_blueprint", "name": "Чертежи нейронного импланта", "type": "blueprint", "rarity": "rare", "quantity": 1}]}',
    '[{"id": "meet_corporate_contact", "text": "Встретиться с корпоративным контактом в отеле Westin на 7-й авеню", "type": "interact", "target": "westin_hotel_corporate_contact", "count": 1, "optional": false}, {"id": "gather_intelligence", "text": "Собрать разведданные о нейронной сети хакеров в подпольных барах Pioneer Square", "type": "interact", "target": "pioneer_square_underground_bars", "count": 3, "optional": false}, {"id": "infiltrate_server_room", "text": "Проникнуть в серверную комнату заброшенного дата-центра Amazon", "type": "interact", "target": "abandoned_amazon_datacenter", "count": 1, "optional": false}, {"id": "hack_neural_network", "text": "Взломать нейронную сеть и скачать доказательства деятельности хакеров", "type": "interact", "target": "neural_network_mainframe", "count": 1, "optional": false}, {"id": "confront_hacker_leader", "text": "Противостоять лидеру хакеров и принять решение о судьбе сети", "type": "interact", "target": "hacker_leader_confrontation", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'main',
    'active'
);

-- Quest 032: Сиэтл 2020-2029 — Корпоративная кража имплантов
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-032-corporate-implant-theft',
    'Сиэтл 2020-2029 — Корпоративная кража имплантов',
    'Quest 032 description - Quest 032 Corporate Implant Theft',
    'medium',
    12,
    20,
    '{"experience": 3500, "currency": {"type": "eddies", "amount": 1800}, "reputation": {"corporate": -150, "underground": 300}, "items": [{"id": "prototype_neural_implant", "name": "Прототип нейронного импланта", "type": "implant", "rarity": "epic", "quantity": 1}]}',
    '[{"id": "investigate_crime_scene", "text": "Осмотреть место первой кражи в лаборатории Militech", "type": "interact", "target": "militech_lab_crime_scene", "count": 1, "optional": false}, {"id": "interview_witnesses", "text": "Опросить свидетелей и сотрудников лаборатории", "type": "interact", "target": "lab_employees_interviews", "count": 3, "optional": false}, {"id": "track_ripperdoc_network", "text": "Отследить сеть подпольных риппердоков в Redmond", "type": "interact", "target": "redmond_ripperdoc_network", "count": 1, "optional": false}, {"id": "infiltrate_black_market", "text": "Проникнуть на черный рынок имплантов в заброшенном метро", "type": "interact", "target": "abandoned_subway_market", "count": 1, "optional": false}, {"id": "confront_master_ripperdoc", "text": "Противостоять главному риппердоку и решить судьбу имплантов", "type": "interact", "target": "master_ripperdoc_confrontation", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 033: Сиэтл 2020-2029 — Зависимость виртуальной реальности
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-033-virtual-reality-addiction',
    'Сиэтл 2020-2029 — Зависимость виртуальной реальности',
    'Quest 033 description - Quest 033 Virtual Reality Addiction',
    'medium',
    8,
    18,
    '{"experience": 2800, "currency": {"type": "eddies", "amount": 1200}, "reputation": {"community": 250, "corporate": -50}, "items": [{"id": "vr_interface_implant", "name": "VR интерфейс имплант", "type": "implant", "rarity": "uncommon", "quantity": 1}]}',
    '[{"id": "meet_family", "text": "Встретиться с семьей зависимого в их доме в Capitol Hill", "type": "interact", "target": "capitol_hill_family_home", "count": 1, "optional": false}, {"id": "investigate_vr_clinic", "text": "Исследовать подпольную VR-клинику в Ballard", "type": "interact", "target": "ballard_vr_clinic", "count": 1, "optional": false}, {"id": "rescue_victim", "text": "Вывести жертву из виртуальной реальности насильно", "type": "interact", "target": "vr_pod_rescue", "count": 1, "optional": false}, {"id": "trace_distribution_network", "text": "Отследить сеть распространения цифровых наркотиков", "type": "interact", "target": "digital_drug_distribution", "count": 1, "optional": false}, {"id": "confront_dealer", "text": "Противостоять главному распространителю и разрушить сеть", "type": "interact", "target": "dealer_confrontation", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 034: Сиэтл 2020-2029 — Корпоративная война в тенях
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-034-corporate-warfare-shadows',
    'Сиэтл 2020-2029 — Корпоративная война в тенях',
    'Quest 034 description - Quest 034 Corporate Warfare Shadows',
    'medium',
    20,
    30,
    '{"experience": 8000, "currency": {"type": "eddies", "amount": 5000}, "reputation": {"corporate": 1000, "winner_corp": 500}, "items": [{"id": "quantum_processor_blueprint", "name": "Чертежи квантового процессора", "type": "blueprint", "rarity": "legendary", "quantity": 1}]}',
    '[{"id": "accept_corporate_offer", "text": "Принять предложение от корпоративного представителя в шикарном ресторане", "type": "interact", "target": "corporate_restaurant_meeting", "count": 1, "optional": false}, {"id": "steal_competitor_data", "text": "Украсть данные о квантовых исследованиях из лаборатории конкурента", "type": "interact", "target": "competitor_quantum_lab", "count": 1, "optional": false}, {"id": "sabotage_rival_operations", "text": "Провести саботаж операций конкурирующей корпорации", "type": "interact", "target": "rival_corporate_facility", "count": 1, "optional": false}, {"id": "uncover_master_conspiracy", "text": "Раскрыть главный заговор в корпоративном совете директоров", "type": "interact", "target": "corporate_board_meeting", "count": 1, "optional": false}, {"id": "make_final_choice", "text": "Принять окончательное решение в пользу одной из корпораций", "type": "interact", "target": "final_corporate_stand", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'main',
    'active'
);

-- Quest 035: Сиэтл 2020-2029 — Кризис климатических беженцев
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-035-climate-refugee-crisis',
    'Сиэтл 2020-2029 — Кризис климатических беженцев',
    'Quest 035 description - Quest 035 Climate Refugee Crisis',
    'medium',
    10,
    20,
    '{"experience": 4000, "currency": {"type": "eddies", "amount": 1500}, "reputation": {"community": 400, "corporate": -200}, "items": [{"id": "refugee_aid_package", "name": "Пакет гуманитарной помощи", "type": "consumable", "rarity": "common", "quantity": 5}]}',
    '[{"id": "visit_refugee_camp", "text": "Посетить лагерь климатических беженцев на окраине Сиэтла", "type": "interact", "target": "refugee_camp_entrance", "count": 1, "optional": false}, {"id": "assess_humanitarian_needs", "text": "Оценить гуманитарные потребности беженцев", "type": "interact", "target": "camp_assessment", "count": 1, "optional": false}, {"id": "investigate_corporate_exploitation", "text": "Расследовать корпоративную эксплуатацию беженцев", "type": "interact", "target": "corporate_exploitation_evidence", "count": 1, "optional": false}, {"id": "organize_relief_effort", "text": "Организовать распределение гуманитарной помощи", "type": "interact", "target": "relief_distribution", "count": 1, "optional": false}, {"id": "confront_corporate_interests", "text": "Противостоять корпоративным интересам и принять решение", "type": "interact", "target": "corporate_confrontation", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 036: Сиэтл 2020-2029 — Информатор в подземелье
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-036-underground-informant',
    'Сиэтл 2020-2029 — Информатор в подземелье',
    'Quest 036 description - Quest 036 Underground Informant',
    'medium',
    5,
    15,
    '{"experience": 2500, "currency": {"type": "eddies", "amount": 500}, "reputation": {"street_cred": 10, "underworld": 15}, "items": [{"id": "corporate_data_chip", "name": "Чип с данными корпораций", "type": "data", "rarity": "uncommon", "description": "Зашифрованные данные о корпоративных проектах Сиэтла"}]}',
    '[{"id": "find_underground_entrance", "text": "Найти вход в подземные туннели под Pioneer Square", "type": "location", "target": "pioneer_square_underground_entrance", "count": 1, "optional": false}, {"id": "navigate_tunnels", "text": "Пройти через заброшенные туннели и миновать охрану", "type": "location", "target": "underground_tunnels_checkpoint", "count": 1, "optional": false}, {"id": "reach_hidden_bar", "text": "Добраться до подпольного бара ''The Vault''", "type": "location", "target": "the_vault_bar_entrance", "count": 1, "optional": false}, {"id": "contact_informant", "text": "Найти и заговорить с информатором по имени ''Whisper''", "type": "interact", "target": "whisper_informant", "count": 1, "optional": false}, {"id": "complete_task", "text": "Выполнить задание информатора: доставить посылку в порт", "type": "delivery", "target": "informant_package_delivery", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 037: Сиэтл 2020-2029 — Корпоративный шантаж в банке
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-037-corporate-blackmail',
    'Сиэтл 2020-2029 — Корпоративный шантаж в банке',
    'Quest 037 description - Quest 037 Corporate Blackmail',
    'medium',
    10,
    25,
    '{"experience": 5000, "currency": {"type": "eddies", "amount": 2500}, "reputation": {"corporate_rivalry": 20, "underworld": 25}, "items": [{"id": "corporate_access_card", "name": "Корпоративная карта доступа", "type": "key_item", "rarity": "rare", "description": "Открывает доступ к корпоративным объектам в Сиэтле"}, {"id": "financial_data_chip", "name": "Чип с финансовыми данными", "type": "data", "rarity": "uncommon", "description": "Данные о корпоративных транзакциях и счетах"}]}',
    '[{"id": "gather_intelligence", "text": "Собрать информацию о менеджере банка через информаторов", "type": "gather_info", "target": "bank_manager_intelligence", "count": 3, "optional": false}, {"id": "infiltrate_bank", "text": "Проникнуть в здание банка, минуя охрану и системы безопасности", "type": "location", "target": "bank_building_interior", "count": 1, "optional": false}, {"id": "access_executive_floor", "text": "Добраться до executive этажа, где находится офис менеджера", "type": "location", "target": "executive_floor_access", "count": 1, "optional": false}, {"id": "find_compromising_data", "text": "Найти компрометирующие данные на корпоративного менеджера", "type": "search", "target": "manager_compromising_files", "count": 1, "optional": false}, {"id": "confront_manager", "text": "Провести шантаж менеджера и получить желаемое", "type": "interact", "target": "manager_blackmail_confrontation", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 038: Сиэтл 2020-2029 — Протест против корпорации
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-038-corporate-protest',
    'Сиэтл 2020-2029 — Протест против корпорации',
    'Quest 038 description - Quest 038 Corporate Protest',
    'medium',
    15,
    30,
    '{"experience": 7500, "currency": {"type": "eddies", "amount": 1000}, "reputation": {"activism": 30, "street_cred": 20, "corporate_rivalry": 25}, "items": [{"id": "resistance_armband", "name": "Браслет Сопротивления", "type": "accessory", "rarity": "uncommon", "description": "Символ принадлежности к движению сопротивления"}, {"id": "protest_flyers", "name": "Пропагандистские листовки", "type": "consumable", "rarity": "common", "description": "Можно распространять для вербовки сторонников"}]}',
    '[{"id": "join_resistance", "text": "Присоединиться к движению сопротивления в Capitol Hill", "type": "interact", "target": "resistance_meeting_capitol_hill", "count": 1, "optional": false}, {"id": "gather_supporters", "text": "Набрать 10+ сторонников для протеста", "type": "recruit", "target": "protest_supporters", "count": 10, "optional": false}, {"id": "plan_protest_route", "text": "Спланировать маршрут протеста через корпоративный район", "type": "planning", "target": "protest_route_planning", "count": 1, "optional": false}, {"id": "acquire_permits", "text": "Получить или подделать разрешения на проведение протеста", "type": "gather_info", "target": "protest_permits", "count": 1, "optional": false}, {"id": "execute_protest", "text": "Провести протест у здания корпорации, преодолевая охрану", "type": "event", "target": "corporate_protest_execution", "count": 1, "optional": false}, {"id": "achieve_objective", "text": "Добиться цели протеста: освобождения политзаключенного или отмены закона", "type": "success_condition", "target": "protest_objective_achieved", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);

-- Quest 039: Сиэтл 2020-2029 — Тайный альянс с факцией
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    'quest-039-faction-alliance',
    'Сиэтл 2020-2029 — Тайный альянс с факцией',
    'Quest 039 description - Quest 039 Faction Alliance',
    'medium',
    20,
    35,
    '{"experience": 10000, "currency": {"type": "eddies", "amount": 3000}, "reputation": {"faction_loyalty": 40, "diplomatic": 25}, "items": [{"id": "faction_signet_ring", "name": "Перстень Фракции", "type": "accessory", "rarity": "rare", "description": "Символ принадлежности к фракции и знак доверия"}, {"id": "alliance_contract", "name": "Договор Альянса", "type": "document", "rarity": "epic", "description": "Юридически обязывающий договор с фракцией"}]}',
    '[{"id": "establish_contact", "text": "Установить контакт с представителем выбранной фракции через нейтрального посредника", "type": "interact", "target": "faction_representative_contact", "count": 1, "optional": false}, {"id": "prove_loyalty", "text": "Выполнить тестовое задание для демонстрации лояльности фракции", "type": "task_completion", "target": "faction_loyalty_task", "count": 1, "optional": false}, {"id": "gather_intelligence", "text": "Собрать разведданные о конкурирующих фракциях", "type": "espionage", "target": "rival_faction_intelligence", "count": 5, "optional": false}, {"id": "mediate_negotiation", "text": "Посредничать в переговорах между фракциями для формирования альянса", "type": "diplomacy", "target": "inter_faction_negotiation", "count": 1, "optional": false}, {"id": "execute_secret_operation", "text": "Провести совместную секретную операцию с новой фракцией-союзником", "type": "mission", "target": "alliance_secret_operation", "count": 1, "optional": false}, {"id": "solidify_alliance", "text": "Закрепить альянс формальным договором или ритуалом", "type": "ceremony", "target": "alliance_ceremony", "count": 1, "optional": false}]',
    'Seattle',
    '2020-2029',
    'side',
    'active'
);
