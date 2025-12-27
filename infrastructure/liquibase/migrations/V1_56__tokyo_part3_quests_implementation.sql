-- Issue: #140893161
-- liquibase formatted sql

--changeset backend:tokyo-part3-quests-2094-2100 dbms:postgresql
--comment: Add Tokyo Part 3 quests for 2094-2100 era - Neural Overload, Corporate Memory Theft, Virtual Sanctuary, Neon Prophet, Midnight Market

BEGIN;

-- Insert Tokyo Part 3 quests into quest_definitions table
INSERT INTO gameplay.quest_definitions (
    quest_id,
    title,
    description,
    quest_type,
    difficulty_level,
    era,
    location,
    estimated_duration_minutes,
    neural_requirement,
    cyberware_tier_requirement,
    is_active,
    created_at,
    updated_at,
    metadata
) VALUES
-- Neural Overload Quest
('tokyo-part3-neural-overload-2094',
 'Нейронная перегрузка',
 'Исследуйте пределы человеческого сознания в эпоху 2094-2100. Ваши нейронные импланты достигли критической нагрузки, и только хакерские модификации могут спасти ваш разум от полного краха.',
 'main',
 'legendary',
 '2094-2100',
 'Tokyo Neural District',
 240,
 8,
 'legendary',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "neural-networks",
   "objectives": [
     "Исследовать Neural Overload Labs",
     "Найти альтернативные нейронные пути",
     "Модифицировать импланты для перегрузки",
     "Выжить после нейронной бури"
   ],
   "rewards": [
     "Neural Overload Implant (Legendary)",
     "10000 Cyber Credits",
     "Access to Neural District VIP areas",
     "Corporate Black ICE immunity"
   ],
   "branching_logic": {
     "nodes": ["investigation", "hacking", "survival"],
     "transitions": [
       {"from": "investigation", "to": "hacking", "condition": "neural_level >= 8"},
       {"from": "hacking", "to": "survival", "condition": "cyberware_tier == legendary"}
     ]
   },
   "related_systems": ["neural-service", "stealth-service", "exploration-service"]
 }'::jsonb),

-- Corporate Memory Theft Quest
('tokyo-part3-corporate-memory-theft-2095',
 'Корпоративное воровство памяти',
 'В эпоху 2095 корпоративные секреты хранятся не в дата-центрах, а в живых нейронных сетях. Вам предстоит проникнуть в сознание корпоративного директора и украсть его воспоминания.',
 'main',
 'hard',
 '2094-2100',
 'Tokyo Corporate Towers',
 180,
 7,
 'elite',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "corporate-espionage",
   "objectives": [
     "Проникнуть в Corporate Memory Vault",
     "Установить нейронную связь с целью",
     "Извлечь корпоративные секреты",
     "Стереть следы присутствия"
   ],
   "rewards": [
     "Corporate Memory Shard (Rare)",
     "7500 Cyber Credits",
     "Access to Corporate Intelligence Network",
     "Memory Theft Cyberware Upgrade"
   ],
   "branching_logic": {
     "nodes": ["infiltration", "extraction", "escape"],
     "transitions": [
       {"from": "infiltration", "to": "extraction", "condition": "stealth_skill >= 85"},
       {"from": "extraction", "to": "escape", "condition": "memory_integrity > 0.7"}
     ]
   },
   "related_systems": ["memory-service", "stealth-service", "social-service"]
 }'::jsonb),

-- Virtual Sanctuary Quest
('tokyo-part3-virtual-sanctuary-2096',
 'Виртуальное убежище',
 'В 2096 году реальность стала токсичной. Единственное безопасное место - это виртуальное убежище, созданное группой повстанцев. Найдите путь к этому цифровому раю.',
 'side',
 'medium',
 '2094-2100',
 'Tokyo Cyberspace',
 120,
 6,
 'advanced',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "virtual-reality",
   "objectives": [
     "Найти координаты Virtual Sanctuary",
     "Пройти аутентификацию убежища",
     "Помочь повстанцам в их миссии",
     "Вернуться в реальный мир"
   ],
   "rewards": [
     "Virtual Sanctuary Access Key",
     "5000 Cyber Credits",
     "Neural Firewall Upgrade",
     "Rebel Alliance Reputation"
   ],
   "branching_logic": {
     "nodes": ["search", "authentication", "assistance"],
     "transitions": [
       {"from": "search", "to": "authentication", "condition": "neural_level >= 6"},
       {"from": "authentication", "to": "assistance", "condition": "has_access_key"}
     ]
   },
   "related_systems": ["neural-service", "social-service", "exploration-service"]
 }'::jsonb),

-- Neon Prophet Quest
('tokyo-part3-neon-prophet-2097',
 'Неоновый Пророк',
 'В 2097 году появляется мистическая фигура в киберпространстве - Неоновый Пророк. Его пророчества могут изменить будущее Токио, но цена за них высока.',
 'side',
 'hard',
 '2094-2100',
 'Tokyo Neon District',
 150,
 7,
 'advanced',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "cyber-prophecy",
   "objectives": [
     "Найти Неонового Пророка в киберпространстве",
     "Задать вопрос о будущем",
     "Интерпретировать полученное пророчество",
     "Использовать знания для преимущества"
   ],
   "rewards": [
     "Prophecy Crystal (Epic)",
     "6000 Cyber Credits",
     "Future Vision Cyberware",
     "Mystic Reputation Boost"
   ],
   "branching_logic": {
     "nodes": ["search", "encounter", "interpretation"],
     "transitions": [
       {"from": "search", "to": "encounter", "condition": "cyberware_tier >= advanced"},
       {"from": "encounter", "to": "interpretation", "condition": "offering_accepted"}
     ]
   },
   "related_systems": ["neural-service", "social-service", "lore-service"]
 }'::jsonb),

-- Midnight Market Quest
('tokyo-part3-midnight-market-2098',
 'Полуночный рынок',
 'В 2098 году, когда солнце заходит за горизонты мегаполиса, оживает Полуночный рынок - нелегальная торговая площадка, где продают запрещенные технологии и украденные воспоминания.',
 'side',
 'medium',
 '2094-2100',
 'Tokyo Underground',
 90,
 5,
 'advanced',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "black-market",
   "objectives": [
     "Найти вход на Полуночный рынок",
     "Установить контакты с торговцами",
     "Приобрести редкие предметы",
     "Избежать патрулей корпораций"
   ],
   "rewards": [
     "Midnight Market Pass",
     "4000 Cyber Credits",
     "Random Rare Item",
     "Black Market Contacts"
   ],
   "branching_logic": {
     "nodes": ["location", "contacts", "trading"],
     "transitions": [
       {"from": "location", "to": "contacts", "condition": "has_pass"},
       {"from": "contacts", "to": "trading", "condition": "reputation >= 50"}
     ]
   },
   "related_systems": ["economy-service", "social-service", "stealth-service"]
 }'::jsonb),

-- Football Hooligans Quest (London 2061-2077)
('london-football-hooligans-2061-2077',
 'Neon Pitch Warfare',
 'Футбол в 2061 году - это корпоративная война на улицах Лондона. Две банды хулиганов, финансируемые Arasaka и Militech, ведут беспощадную борьбу за контроль над фанатами и территорией.',
 'main',
 'medium',
 '2061-2077',
 'London, East End',
 240,
 3,
 'advanced',
 true,
 NOW(),
 NOW(),
 '{
   "theme": "corporate-sports-war",
   "objectives": [
     "Расследовать конфликт хулиганов в Ист-Энде",
     "Встретиться с представителями Neon Devils и Iron Wolves",
     "Выбрать сторону или объединить банды против корпораций",
     "Участвовать в подпольном матче с кибер-правилами",
     "Разобраться с корпоративным вмешательством"
   ],
   "rewards": [
     "Neon Devils или Iron Wolves репутация +50",
     "Кибер-футбольные бутсы",
     "Adrenaline Surge имплант",
     "Документы о коррупции в спорте",
     "15000-25000 eddies"
   ],
   "branching_logic": {
     "nodes": ["investigation", "alliance", "match", "resolution"],
     "transitions": [
       {"from": "investigation", "to": "alliance", "condition": "met_both_leaders"},
       {"from": "alliance", "to": "match", "condition": "chose_side OR united_factions"},
       {"from": "match", "to": "resolution", "condition": "match_completed"}
     ]
   },
   "related_systems": ["narrative-service", "quest-service", "relationship-service", "world-service"]
 }'::jsonb);

-- Create indexes for Tokyo Part 3 quests
CREATE INDEX IF NOT EXISTS idx_tokyo_part3_quests_era
ON gameplay.quest_definitions (era)
WHERE era = '2094-2100';

CREATE INDEX IF NOT EXISTS idx_tokyo_part3_quests_neural_req
ON gameplay.quest_definitions (neural_requirement)
WHERE era = '2094-2100' AND neural_requirement >= 5;

-- Insert quest objectives for each quest
INSERT INTO gameplay.quest_objectives (
    quest_id,
    objective_id,
    title,
    description,
    objective_type,
    is_required,
    order_index,
    created_at,
    updated_at
) VALUES
-- Neural Overload objectives
('tokyo-part3-neural-overload-2094', 'no-001', 'Исследовать Neural Overload Labs', 'Найти и проникнуть в лаборатории Neural Overload', 'exploration', true, 1, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'no-002', 'Найти альтернативные нейронные пути', 'Обнаружить обходные пути в нейронной сети', 'puzzle', true, 2, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'no-003', 'Модифицировать импланты', 'Провести модификацию имплантов для перегрузки', 'crafting', true, 3, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'no-004', 'Выжить после нейронной бури', 'Пережить нейронную перегрузку', 'survival', true, 4, NOW(), NOW()),

-- Corporate Memory Theft objectives
('tokyo-part3-corporate-memory-theft-2095', 'cmt-001', 'Проникнуть в Memory Vault', 'Получить доступ к корпоративному хранилищу памяти', 'infiltration', true, 1, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'cmt-002', 'Установить нейронную связь', 'Создать стабильную нейронную связь с целью', 'hacking', true, 2, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'cmt-003', 'Извлечь секреты', 'Скопировать корпоративные секреты', 'extraction', true, 3, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'cmt-004', 'Стереть следы', 'Удалить все доказательства присутствия', 'cleanup', true, 4, NOW(), NOW()),

-- Virtual Sanctuary objectives
('tokyo-part3-virtual-sanctuary-2096', 'vs-001', 'Найти координаты', 'Получить координаты виртуального убежища', 'search', true, 1, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'vs-002', 'Пройти аутентификацию', 'Успешно пройти проверку убежища', 'authentication', true, 2, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'vs-003', 'Помочь повстанцам', 'Выполнить задачу для повстанцев', 'assistance', true, 3, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'vs-004', 'Вернуться в реальность', 'Безопасно выйти из виртуального мира', 'exit', true, 4, NOW(), NOW()),

-- Neon Prophet objectives
('tokyo-part3-neon-prophet-2097', 'np-001', 'Найти Пророка', 'Обнаружить Неонового Пророка в киберпространстве', 'search', true, 1, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'np-002', 'Задать вопрос', 'Получить пророчество от Пророка', 'interaction', true, 2, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'np-003', 'Интерпретировать пророчество', 'Раскрыть смысл полученного пророчества', 'analysis', true, 3, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'np-004', 'Использовать знания', 'Применить полученные знания', 'application', true, 4, NOW(), NOW()),

-- Midnight Market objectives
('tokyo-part3-midnight-market-2098', 'mm-001', 'Найти рынок', 'Обнаружить вход на Полуночный рынок', 'navigation', true, 1, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'mm-002', 'Установить контакты', 'Завести знакомства с торговцами', 'social', true, 2, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'mm-003', 'Совершить сделку', 'Купить или продать товар', 'trading', true, 3, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'mm-004', 'Избежать патрулей', 'Уйти незамеченным от корпоративных патрулей', 'stealth', true, 4, NOW(), NOW()),

-- Football Hooligans objectives
('london-football-hooligans-2061-2077', 'fh-001', 'Расследовать конфликт', 'Исследовать конфликт хулиганов в Ист-Энде', 'investigation', true, 1, NOW(), NOW()),
('london-football-hooligans-2061-2077', 'fh-002', 'Встретиться с лидерами', 'Поговорить с представителями обеих банд', 'social', true, 2, NOW(), NOW()),
('london-football-hooligans-2061-2077', 'fh-003', 'Выбрать сторону', 'Присоединиться к Neon Devils или Iron Wolves', 'decision', true, 3, NOW(), NOW()),
('london-football-hooligans-2061-2077', 'fh-004', 'Участвовать в матче', 'Играть в подпольном футболе с кибер-усилениями', 'combat', true, 4, NOW(), NOW()),
('london-football-hooligans-2061-2077', 'fh-005', 'Разрешить конфликт', 'Разобраться с корпоративным вмешательством', 'resolution', true, 5, NOW(), NOW());

-- Insert quest rewards
INSERT INTO gameplay.quest_rewards (
    quest_id,
    reward_type,
    reward_id,
    quantity,
    probability,
    is_guaranteed,
    created_at,
    updated_at
) VALUES
-- Neural Overload rewards
('tokyo-part3-neural-overload-2094', 'item', 'neural-overload-implant', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'currency', 'cyber-credits', 10000, 1.0, true, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'access', 'neural-district-vip', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'immunity', 'corporate-black-ice', 1, 1.0, true, NOW(), NOW()),

-- Corporate Memory Theft rewards
('tokyo-part3-corporate-memory-theft-2095', 'item', 'corporate-memory-shard', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'currency', 'cyber-credits', 7500, 1.0, true, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'access', 'corporate-intelligence-network', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'upgrade', 'memory-theft-cyberware', 1, 1.0, true, NOW(), NOW()),

-- Virtual Sanctuary rewards
('tokyo-part3-virtual-sanctuary-2096', 'item', 'virtual-sanctuary-access-key', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'currency', 'cyber-credits', 5000, 1.0, true, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'upgrade', 'neural-firewall', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'reputation', 'rebel-alliance', 500, 1.0, true, NOW(), NOW()),

-- Neon Prophet rewards
('tokyo-part3-neon-prophet-2097', 'item', 'prophecy-crystal', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'currency', 'cyber-credits', 6000, 1.0, true, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'upgrade', 'future-vision-cyberware', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'reputation', 'mystic', 300, 1.0, true, NOW(), NOW()),

-- Midnight Market rewards
('tokyo-part3-midnight-market-2098', 'item', 'midnight-market-pass', 1, 1.0, true, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'currency', 'cyber-credits', 4000, 1.0, true, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'item', 'random-rare-item', 1, 0.5, false, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'contact', 'black-market-contacts', 1, 1.0, true, NOW(), NOW());

-- Insert quest prerequisites
INSERT INTO gameplay.quest_prerequisites (
    quest_id,
    prerequisite_type,
    prerequisite_id,
    required_value,
    created_at,
    updated_at
) VALUES
-- Neural Overload prerequisites
('tokyo-part3-neural-overload-2094', 'neural_level', 'neural_implant_level', 8, NOW(), NOW()),
('tokyo-part3-neural-overload-2094', 'cyberware_tier', 'cyberware_tier', 4, NOW(), NOW()), -- legendary = 4
('tokyo-part3-neural-overload-2094', 'quest', 'tokyo-part3-corporate-memory-theft-2095', 1, NOW(), NOW()),

-- Corporate Memory Theft prerequisites
('tokyo-part3-corporate-memory-theft-2095', 'neural_level', 'neural_implant_level', 7, NOW(), NOW()),
('tokyo-part3-corporate-memory-theft-2095', 'cyberware_tier', 'cyberware_tier', 3, NOW(), NOW()), -- elite = 3
('tokyo-part3-corporate-memory-theft-2095', 'skill', 'stealth_skill', 80, NOW(), NOW()),

-- Virtual Sanctuary prerequisites
('tokyo-part3-virtual-sanctuary-2096', 'neural_level', 'neural_implant_level', 6, NOW(), NOW()),
('tokyo-part3-virtual-sanctuary-2096', 'cyberware_tier', 'cyberware_tier', 2, NOW(), NOW()), -- advanced = 2

-- Neon Prophet prerequisites
('tokyo-part3-neon-prophet-2097', 'neural_level', 'neural_implant_level', 7, NOW(), NOW()),
('tokyo-part3-neon-prophet-2097', 'cyberware_tier', 'cyberware_tier', 2, NOW(), NOW()), -- advanced = 2

-- Midnight Market prerequisites
('tokyo-part3-midnight-market-2098', 'neural_level', 'neural_implant_level', 5, NOW(), NOW()),
('tokyo-part3-midnight-market-2098', 'cyberware_tier', 'cyberware_tier', 2, NOW(), NOW()); -- advanced = 2

COMMIT;
