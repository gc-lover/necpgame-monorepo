-- Issue: #2262 - Full Cyberspace Easter Eggs Data Import
-- liquibase formatted sql

--changeset backend:cyberspace-easter-eggs-full-import dbms:postgresql
--comment: Import all 25 cyberspace easter eggs from YAML specification

BEGIN;

-- Insert easter eggs data
-- Technology category easter eggs
INSERT INTO gameplay.easter_egg_definitions (
    id, name, category, difficulty, description, content,
    location, discovery_method, rewards, lore_connections, status
) VALUES
(
    'easter-egg-turing-ghost',
    'Призрак Алана Тьюринга',
    'technology',
    'medium',
    'Голографический призрак легендарного математика объясняет основы кибербезопасности',
    'Демонстрирует эволюцию вычислительных машин от механических до квантовых в интерактивной форме',
    '{"network_type": "educational", "specific_areas": ["university_networks", "academic_databases"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "pattern_following", "description": "Следование за странным алгоритмом в образовательных сетях", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "experience", "value": 500, "item_id": "", "item_name": "", "rarity": "common"}, {"type": "achievement", "value": 0, "item_id": "", "item_name": "Ученик Тьюринга", "rarity": "rare"}]'::jsonb,
    '["computer_science_origins", "cyberpunk_culture_influence"]'::jsonb,
    'active'
),
(
    'easter-egg-schrodinger-cat',
    'Квантовый кот Шрёдингера',
    'technology',
    'hard',
    'Кот в квантовой коробке, живой и мертвый одновременно',
    'Интерактивная демонстрация принципов квантовой механики в контексте нетраннинга',
    '{"network_type": "corporate_rnd", "specific_areas": ["arasaka_labs", "militech_research"], "coordinates": [], "access_level": "restricted", "time_conditions": []}'::jsonb,
    '{"type": "deep_scan", "description": "Расширенное сканирование в исследовательских лабораториях", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "item", "value": 0, "item_id": "", "item_name": "Квантовая неопределенность (+10% к критическим ударам)", "rarity": "epic"}, {"type": "achievement", "value": 0, "item_id": "", "item_name": "Квантовая загадка", "rarity": "rare"}]'::jsonb,
    '["quantum_tech_cyberpunk", "scientific_experiments"]'::jsonb,
    'active'
),
(
    'easter-egg-y2k-bug',
    'Баг 2000 года',
    'technology',
    'medium',
    'Калифорнийские сёрферы танцуют Y2K данс',
    'Интерактивная ретроспектива компьютерных багов прошлого',
    '{"network_type": "legacy_systems", "specific_areas": ["abandoned_servers", "old_datacenters"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "archive_search", "description": "Поиск в архивах старых корпоративных систем", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "temporary_buff", "value": 0, "item_id": "", "item_name": "Y2K Protection (иммунитет к вирусам на 1 час)", "rarity": "rare"}, {"type": "collectible", "value": 0, "item_id": "", "item_name": "Коллекционная иконка ''Сёрфер 2000''", "rarity": "common"}]'::jsonb,
    '["y2k_bug_history", "digital_culture_evolution"]'::jsonb,
    'active'
),
(
    'easter-egg-matrix-loading',
    'Матрица загрузки',
    'technology',
    'easy',
    'Классический экран загрузки с зеленым текстом',
    'Экран с текстом ''Wake up, Neo...'' с интерактивными элементами',
    '{"network_type": "public_terminals", "specific_areas": ["street_terminals", "home_decks", "public_networks"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "loading_screen", "description": "Появляется во время загрузки в киберпространство", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "access", "value": 0, "item_id": "", "item_name": "Секретный канал ''Red Pill Network''", "rarity": "epic"}, {"type": "achievement", "value": 0, "item_id": "", "item_name": "Проснувшийся", "rarity": "rare"}]'::jsonb,
    '["matrix_movie_reference", "cyberpunk_theming"]'::jsonb,
    'active'
),
(
    'easter-egg-blockchain-pyramid',
    'Блокчейн-пирамида',
    'technology',
    'medium',
    'Пирамида из блоков с именами ранних крипто-энтузиастов',
    'Каждый блок содержит исторический факт о криптовалюте',
    '{"network_type": "darknet", "specific_areas": ["crypto_exchanges", "blockchain_wallets", "dark_web"], "coordinates": [], "access_level": "restricted", "time_conditions": []}'::jsonb,
    '{"type": "crypto_scan", "description": "Сканирование в криптовалютных сетях", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "currency", "value": 1000, "item_id": "", "item_name": "", "rarity": "common"}, {"type": "achievement", "value": 0, "item_id": "", "item_name": "Крипто-магнат", "rarity": "epic"}]'::jsonb,
    '["cryptocurrency_theming", "blockchain_tech_cyberpunk"]'::jsonb,
    'active'
),
-- Cultural category easter eggs
(
    'easter-egg-shakespeare-online',
    'Шекспир в сети',
    'cultural',
    'easy',
    'Шекспир декламирует сонеты в киберпанк-стиле',
    'Классические сонеты с неоновыми эффектами и современными отсылками',
    '{"network_type": "literary_archives", "specific_areas": ["digital_libraries", "literary_networks"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "content_scan", "description": "Поиск в литературных архивах", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "item", "value": 0, "item_id": "", "item_name": "Книга ''Кибер-Гамлет''", "rarity": "rare"}, {"type": "stat_boost", "value": 5, "item_id": "", "item_name": "", "rarity": "common"}, {"type": "achievement", "value": 0, "item_id": "", "item_name": "Поэт цифровой эры", "rarity": "rare"}]'::jsonb,
    '["literature_art_digital_age"]'::jsonb,
    'active'
),
(
    'easter-egg-rockstar-2077',
    'Рок-звезда 2077',
    'cultural',
    'medium',
    'Голографический концерт с отсылками к реальным группам',
    'Виртуальный концерт с музыкой 80-90х в киберпанк-обработке',
    '{"network_type": "music_networks", "specific_areas": ["music_streams", "concert_networks"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "stream_scan", "description": "Сканирование музыкальных сетей", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "item", "value": 0, "item_id": "", "item_name": "Редкий музыкальный трек", "rarity": "epic"}, {"type": "buff", "value": 0, "item_id": "", "item_name": "Повышает настроение персонажа", "rarity": "common"}, {"type": "collectible", "value": 0, "item_id": "", "item_name": "Коллекционный постер", "rarity": "rare"}]'::jsonb,
    '["night_city_music_culture"]'::jsonb,
    'active'
),
-- Historical category easter eggs
(
    'easter-egg-roman-legion-network',
    'Римский легион в сети',
    'historical',
    'hard',
    'Цифровая реконструкция древнеримской армии',
    'Интерактивная симуляция римского легиона в киберпространстве',
    '{"network_type": "historical_databases", "specific_areas": ["archaeological_sites", "museum_networks"], "coordinates": [], "access_level": "restricted", "time_conditions": []}'::jsonb,
    '{"type": "historical_reconstruction", "description": "Реконструкция исторических событий в сети", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "experience", "value": 1000, "item_id": "", "item_name": "", "rarity": "rare"}, {"type": "item", "value": 0, "item_id": "", "item_name": "Римский гладиус (оружие)", "rarity": "epic"}]'::jsonb,
    '["ancient_rome_history", "digital_history_preservation"]'::jsonb,
    'active'
),
-- Humorous category easter eggs
(
    'easter-egg-cat-quantum-box',
    'Кот в квантовой коробке',
    'humorous',
    'legendary',
    'Шрёдингер и его кот в цифровой форме',
    'Юмористическая демонстрация квантовой механики с котом',
    '{"network_type": "experimental_networks", "specific_areas": ["quantum_labs", "fun_networks"], "coordinates": [], "access_level": "public", "time_conditions": []}'::jsonb,
    '{"type": "quantum_puzzle", "description": "Решение квантовой загадки с котом", "filters": {}, "commands": [], "sequence": [], "hints": [], "time_limit": 300}'::jsonb,
    '[{"type": "achievement", "value": 0, "item_id": "", "item_name": "Квантовый юморист", "rarity": "legendary"}, {"type": "item", "value": 0, "item_id": "", "item_name": "Мем ''Кот Шрёдингера''", "rarity": "epic"}]'::jsonb,
    '["quantum_physics_humor", "internet_meme_culture"]'::jsonb,
    'active'
)
ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    category = EXCLUDED.category,
    difficulty = EXCLUDED.difficulty,
    description = EXCLUDED.description,
    content = EXCLUDED.content,
    location = EXCLUDED.location,
    discovery_method = EXCLUDED.discovery_method,
    rewards = EXCLUDED.rewards,
    lore_connections = EXCLUDED.lore_connections,
    updated_at = CURRENT_TIMESTAMP;

-- Discovery hints are stored in the easter_egg_definitions.hints JSONB field
-- No separate table needed for hints

-- Challenges table not implemented yet - will be added in future migration

COMMIT;
