-- Achievement System Initial Data
-- Version: V002
-- Description: Initial data for achievement categories, definitions, rewards, and chains

-- =================================================================================================
-- ACHIEVEMENT CATEGORIES
-- =================================================================================================

INSERT INTO achievement_categories (category_key, name, description, icon_url, color_code, sort_order) VALUES
('combat', '{"en": "Combat", "ru": "Бой"}', '{"en": "Master the art of combat", "ru": "Овладей искусством боя"}', '/icons/achievements/combat.png', '#FF4444', 1),
('social', '{"en": "Social", "ru": "Социальное"}', '{"en": "Connect with other players", "ru": "Общайся с другими игроками"}', '/icons/achievements/social.png', '#44FF44', 2),
('economy', '{"en": "Economy", "ru": "Экономика"}', '{"en": "Build your fortune", "ru": "Создай свое состояние"}', '/icons/achievements/economy.png', '#FFFF44', 3),
('exploration', '{"en": "Exploration", "ru": "Исследование"}', '{"en": "Discover the world", "ru": "Открой мир"}', '/icons/achievements/exploration.png', '#4444FF', 4),
('special', '{"en": "Special", "ru": "Особые"}', '{"en": "Unique accomplishments", "ru": "Уникальные достижения"}', '/icons/achievements/special.png', '#FF44FF', 5),
('seasonal', '{"en": "Seasonal", "ru": "Сезонные"}', '{"en": "Limited time achievements", "ru": "Ограниченные по времени"}', '/icons/achievements/seasonal.png', '#44FFFF', 6),
('guild', '{"en": "Guild", "ru": "Гильдия"}', '{"en": "Guild achievements", "ru": "Гильдейские достижения"}', '/icons/achievements/guild.png', '#FF8844', 7);

-- =================================================================================================
-- ACHIEVEMENT TAGS
-- =================================================================================================

INSERT INTO achievement_tags (tag_key, name, description, color_code) VALUES
('first_steps', '{"en": "First Steps", "ru": "Первые шаги"}', '{"en": "Beginner achievements", "ru": "Достижения новичка"}', '#88FF88'),
('master', '{"en": "Master", "ru": "Мастер"}', '{"en": "Advanced achievements", "ru": "Продвинутые достижения"}', '#FF8888'),
('legend', '{"en": "Legend", "ru": "Легенда"}', '{"en": "Legendary achievements", "ru": "Легендарные достижения"}', '#FFFF88'),
('speedrun', '{"en": "Speedrun", "ru": "Скоростной"}', '{"en": "Time-based achievements", "ru": "Достижения по времени"}', '#8888FF'),
('collection', '{"en": "Collection", "ru": "Коллекция"}', '{"en": "Collectible achievements", "ru": "Коллекционные достижения"}', '#FF88FF'),
('social', '{"en": "Social", "ru": "Социальный"}', '{"en": "Social achievements", "ru": "Социальные достижения"}', '#88FFFF'),
('rare', '{"en": "Rare", "ru": "Редкий"}', '{"en": "Rare achievements", "ru": "Редкие достижения"}', '#FFA500');

-- =================================================================================================
-- ACHIEVEMENT REWARDS
-- =================================================================================================

INSERT INTO achievement_rewards (reward_key, name, description, reward_type, reward_category, value_data, rarity, is_stackable, max_stack) VALUES
-- Currency rewards
('credits_100', '{"en": "100 Credits", "ru": "100 Кредитов"}', '{"en": "Game currency", "ru": "Игровая валюта"}', 'CURRENCY', 'CURRENCY', '{"currency": "credits", "amount": 100}', 'common', true, 999),
('credits_500', '{"en": "500 Credits", "ru": "500 Кредитов"}', '{"en": "Game currency", "ru": "Игровая валюта"}', 'CURRENCY', 'CURRENCY', '{"currency": "credits", "amount": 500}', 'uncommon', true, 999),
('premium_50', '{"en": "50 Premium Currency", "ru": "50 Премиум Валюты"}', '{"en": "Premium game currency", "ru": "Премиум игровая валюта"}', 'CURRENCY', 'CURRENCY', '{"currency": "premium", "amount": 50}', 'rare', true, 999),

-- Cosmetic rewards
('title_warrior', '{"en": "Warrior", "ru": "Воин"}', '{"en": "Combat achievement title", "ru": "Титул за боевые достижения"}', 'TITLE', 'TITLE', '{"title_id": "warrior", "color": "#8B0000"}', 'uncommon', false, 1),
('title_explorer', '{"en": "Explorer", "ru": "Исследователь"}', '{"en": "Exploration achievement title", "ru": "Титул за исследования"}', 'TITLE', 'TITLE', '{"title_id": "explorer", "color": "#006400"}', 'uncommon', false, 1),
('title_master', '{"en": "Master", "ru": "Мастер"}', '{"en": "Master achievement title", "ru": "Титул мастера"}', 'TITLE', 'TITLE', '{"title_id": "master", "color": "#FFD700"}', 'rare', false, 1),

-- Cosmetic items
('emote_victory', '{"en": "Victory Emote", "ru": "Эмут Победы"}', '{"en": "Celebrate your victories", "ru": "Отпразднуй свои победы"}', 'COSMETIC', 'EMOTE', '{"emote_id": "victory", "animation": "victory_pose"}', 'common', false, 1),
('weapon_trail_fire', '{"en": "Fire Weapon Trail", "ru": "Огненный след оружия"}', '{"en": "Fiery weapon trail effect", "ru": "Огненный эффект следа оружия"}', 'COSMETIC', 'WEAPON_TRAIL', '{"effect_id": "fire_trail", "particles": "fire"}', 'rare', false, 1),

-- Boosters
('xp_booster_2x_1h', '{"en": "2x XP Booster (1h)", "ru": "Усилитель XP 2x (1ч)"}', '{"en": "Double XP for 1 hour", "ru": "Удвоенный XP на 1 час"}', 'BOOSTER', 'XP_BOOST', '{"multiplier": 2.0, "duration_hours": 1}', 'uncommon', false, 1),
('currency_booster_2x_1h', '{"en": "2x Currency Booster (1h)", "ru": "Усилитель валюты 2x (1ч)"}', '{"en": "Double currency for 1 hour", "ru": "Удвоенная валюта на 1 час"}', 'BOOSTER', 'CURRENCY_BOOST', '{"multiplier": 2.0, "duration_hours": 1}', 'rare', false, 1),

-- Exclusive items
('pet_companion_drone', '{"en": "Companion Drone", "ru": "Дрон-спутник"}', '{"en": "A loyal companion drone", "ru": "Верный дрон-спутник"}', 'EXCLUSIVE', 'PET', '{"pet_id": "companion_drone", "abilities": ["recon", "support"]}', 'epic', false, 1),
('vehicle_hover_scooter', '{"en": "Hover Scooter", "ru": "Ховер-скутер"}', '{"en": "Fast personal transport", "ru": "Быстрый личный транспорт"}', 'EXCLUSIVE', 'VEHICLE', '{"vehicle_id": "hover_scooter", "speed": 80, "durability": 150}', 'legendary', false, 1);

-- =================================================================================================
-- ACHIEVEMENT DEFINITIONS
-- =================================================================================================

-- Combat Achievements
INSERT INTO achievement_definitions (code, title, description, category, difficulty, achievement_type, max_progress, conditions, rewards, sort_order) VALUES
('first_blood', '{"en": "First Blood", "ru": "Первая кровь"}', '{"en": "Kill your first enemy", "ru": "Убей своего первого врага"}', 'COMBAT', 'EASY', 'STANDARD', 1,
 '{"enemy_kills": 1}', '{"credits": 100}', 1),

('combat_novice', '{"en": "Combat Novice", "ru": "Новичок боя"}', '{"en": "Kill 10 enemies", "ru": "Убей 10 врагов"}', 'COMBAT', 'EASY', 'PROGRESSIVE', 10,
 '{"enemy_kills": 10}', '{"credits": 250, "emote": "thumbs_up"}', 2),

('combat_warrior', '{"en": "Warrior", "ru": "Воин"}', '{"en": "Kill 100 enemies", "ru": "Убей 100 врагов"}', 'COMBAT', 'MEDIUM', 'PROGRESSIVE', 100,
 '{"enemy_kills": 100}', '{"credits": 1000, "title": "warrior"}', 3),

('combat_master', '{"en": "Combat Master", "ru": "Мастер боя"}', '{"en": "Kill 1000 enemies", "ru": "Убей 1000 врагов"}', 'COMBAT', 'HARD', 'PROGRESSIVE', 1000,
 '{"enemy_kills": 1000}', '{"credits": 5000, "cosmetic": "weapon_trail_fire", "title": "master"}', 4),

('perfect_game', '{"en": "Perfect Game", "ru": "Идеальная игра"}', '{"en": "Complete a game without taking damage", "ru": "Заверши игру без получения урона"}', 'COMBAT', 'HARD', 'STANDARD', 1,
 '{"damage_taken": 0, "game_completed": true}', '{"credits": 2000, "title": "perfect"}', 5),

-- Social Achievements
('first_friend', '{"en": "First Friend", "ru": "Первый друг"}', '{"en": "Add your first friend", "ru": "Добавь своего первого друга"}', 'SOCIAL', 'EASY', 'STANDARD', 1,
 '{"friends_added": 1}', '{"credits": 50}', 1),

('social_butterfly', '{"en": "Social Butterfly", "ru": "Социальная бабочка"}', '{"en": "Add 10 friends", "ru": "Добавь 10 друзей"}', 'SOCIAL', 'MEDIUM', 'PROGRESSIVE', 10,
 '{"friends_added": 10}', '{"credits": 500, "emote": "wave"}', 2),

('team_player', '{"en": "Team Player", "ru": "Командный игрок"}', '{"en": "Complete 5 co-op missions", "ru": "Заверши 5 кооперативных миссий"}', 'SOCIAL', 'MEDIUM', 'PROGRESSIVE', 5,
 '{"coop_missions_completed": 5}', '{"credits": 750}', 3),

-- Economy Achievements
('first_purchase', '{"en": "First Purchase", "ru": "Первая покупка"}', '{"en": "Buy your first item", "ru": "Купи свой первый предмет"}', 'ECONOMY', 'EASY', 'STANDARD', 1,
 '{"items_purchased": 1}', '{"credits": 25}', 1),

('wealth_builder', '{"en": "Wealth Builder", "ru": "Собиратель богатства"}', '{"en": "Accumulate 10,000 credits", "ru": "Накопи 10,000 кредитов"}', 'ECONOMY', 'MEDIUM', 'STANDARD', 1,
 '{"credits_owned": 10000}', '{"premium_currency": 25}', 2),

('merchant', '{"en": "Merchant", "ru": "Торговец"}', '{"en": "Sell 50 items", "ru": "Продай 50 предметов"}', 'ECONOMY', 'MEDIUM', 'PROGRESSIVE', 50,
 '{"items_sold": 50}', '{"credits": 1000}', 3),

-- Exploration Achievements
('first_discovery', '{"en": "First Discovery", "ru": "Первое открытие"}', '{"en": "Discover your first location", "ru": "Открой свою первую локацию"}', 'EXPLORATION', 'EASY', 'STANDARD', 1,
 '{"locations_discovered": 1}', '{"credits": 75}', 1),

('explorer', '{"en": "Explorer", "ru": "Исследователь"}', '{"en": "Discover 25 locations", "ru": "Открой 25 локаций"}', 'EXPLORATION', 'MEDIUM', 'PROGRESSIVE', 25,
 '{"locations_discovered": 25}', '{"credits": 500, "title": "explorer"}', 2),

('world_traveler', '{"en": "World Traveler", "ru": "Путешественник мира"}', '{"en": "Visit all major cities", "ru": "Посети все крупные города"}', 'EXPLORATION', 'HARD', 'STANDARD', 1,
 '{"major_cities_visited": ["all"]}', '{"credits": 2500, "cosmetic": "world_map_overlay"}', 3),

-- Special Achievements
('speed_demon', '{"en": "Speed Demon", "ru": "Демон скорости"}', '{"en": "Complete a race in under 2 minutes", "ru": "Заверши гонку менее чем за 2 минуты"}', 'SPECIAL', 'HARD', 'STANDARD', 1,
 '{"race_completed": true, "time_under": 120}', '{"credits": 1500, "title": "speed_demon"}', 1),

('hidden_master', '{"en": "Hidden Master", "ru": "Скрытый мастер"}', '{"en": "Find all hidden collectibles", "ru": "Найди все скрытые коллекционные предметы"}', 'SPECIAL', 'LEGENDARY', 'HIDDEN', 1,
 '{"hidden_collectibles_found": ["all"]}', '{"credits": 5000, "exclusive_item": "master_key"}', 2);

-- =================================================================================================
-- ACHIEVEMENT CHAINS
-- =================================================================================================

INSERT INTO achievement_chains (chain_key, name, description, chain_type, total_achievements, reward_data) VALUES
('combat_journey', '{"en": "Combat Journey", "ru": "Путь бойца"}', '{"en": "Master the art of combat", "ru": "Овладей искусством боя"}', 'LINEAR', 4,
 '{"title": "combat_master", "cosmetic": "legendary_weapon_skin", "credits": 10000}'),

('social_circle', '{"en": "Social Circle", "ru": "Социальный круг"}', '{"en": "Build your social network", "ru": "Создай свою социальную сеть"}', 'LINEAR', 3,
 '{"title": "social_butterfly", "emote_pack": "social_emotes", "credits": 2500}');

-- Associate achievements with chains
INSERT INTO achievement_chain_members (chain_id, achievement_id, position, is_required) VALUES
(1, 1, 1, true),  -- First blood
(1, 2, 2, true),  -- Combat novice
(1, 3, 3, true),  -- Warrior
(1, 4, 4, true),  -- Combat master

(2, 6, 1, true),  -- First friend
(2, 7, 2, true),  -- Social butterfly
(2, 8, 3, true);  -- Team player

-- =================================================================================================
-- SEASONAL ACHIEVEMENTS
-- =================================================================================================

INSERT INTO achievement_seasons (season_key, name, description, start_date, end_date, theme_data, is_active) VALUES
('winter_2025', '{"en": "Winter Wonderland 2025", "ru": "Зимняя сказка 2025"}', '{"en": "Winter-themed seasonal achievements", "ru": "Зимние сезонные достижения"}',
 '2025-12-01 00:00:00+00', '2025-02-28 23:59:59+00',
 '{"theme": "winter", "colors": ["#00FFFF", "#FFFFFF", "0000FF"], "effects": ["snow_particles", "ice_glow"]}', true);

-- Seasonal achievements
INSERT INTO achievement_definitions (code, title, description, category, difficulty, achievement_type, max_progress, conditions, rewards, sort_order) VALUES
('winter_snowball_fight', '{"en": "Snowball Champion", "ru": "Чемпион снежков"}', '{"en": "Win 10 snowball fights", "ru": "Выиграй 10 снежных битв"}', 'SEASONAL', 'MEDIUM', 'PROGRESSIVE', 10,
 '{"snowball_fights_won": 10}', '{"credits": 500, "cosmetic": "winter_hat"}', 1),

('winter_ice_queen', '{"en": "Ice Queen", "ru": "Ледяная королева"}', '{"en": "Defeat the Ice Queen boss", "ru": "Победи босса Ледяную королеву"}', 'SEASONAL', 'HARD', 'STANDARD', 1,
 '{"boss_defeated": "ice_queen"}', '{"credits": 2000, "exclusive_item": "ice_crown"}', 2);

-- Associate seasonal achievements
INSERT INTO achievement_season_members (season_id, achievement_id, is_featured, bonus_multiplier) VALUES
(1, (SELECT id FROM achievement_definitions WHERE code = 'winter_snowball_fight'), true, 1.5),
(1, (SELECT id FROM achievement_definitions WHERE code = 'winter_ice_queen'), true, 2.0);

-- =================================================================================================
-- ACHIEVEMENT TAGS ASSIGNMENT
-- =================================================================================================

-- Assign tags to achievements
INSERT INTO achievement_definition_tags (achievement_id, tag_id) VALUES
-- First steps tags
(1, 1), (2, 1), (6, 1), (9, 1), (11, 1), -- First blood, combat novice, first friend, first purchase, first discovery

-- Master tags
(3, 2), (4, 2), (7, 2), (8, 2), (12, 2), (13, 2), -- Warrior, combat master, social butterfly, team player, wealth builder, merchant

-- Legend tags
(5, 3), (15, 3), (16, 3), -- Perfect game, hidden master, ice queen

-- Speedrun tags
(14, 4), -- Speed demon

-- Collection tags
(13, 5), -- Merchant (selling items)

-- Social tags
(6, 6), (7, 6), (8, 6), -- First friend, social butterfly, team player

-- Rare tags
(5, 7), (15, 7), (16, 7); -- Perfect game, hidden master, ice queen

-- =================================================================================================
-- ACHIEVEMENT REWARDS ASSIGNMENT
-- =================================================================================================

-- Associate rewards with achievements
INSERT INTO achievement_definition_rewards (achievement_id, reward_id, quantity) VALUES
-- Combat achievements
(1, 1, 1), -- First blood: 100 credits
(2, 2, 1), -- Combat novice: 500 credits
(3, 2, 1), -- Warrior: 500 credits
(3, 4, 1), -- Warrior: Warrior title
(4, 2, 1), -- Combat master: 500 credits
(4, 5, 1), -- Combat master: 2x XP booster
(4, 6, 1), -- Combat master: Fire weapon trail
(5, 2, 1), -- Perfect game: 500 credits

-- Social achievements
(6, 1, 1), -- First friend: 100 credits
(7, 2, 1), -- Social butterfly: 500 credits
(7, 3, 1), -- Social butterfly: Victory emote
(8, 2, 1), -- Team player: 500 credits

-- Economy achievements
(9, 1, 1), -- First purchase: 100 credits
(10, 3, 1), -- Wealth builder: 50 premium currency
(11, 2, 1), -- Merchant: 500 credits

-- Exploration achievements
(11, 1, 1), -- First discovery: 100 credits
(12, 2, 1), -- Explorer: 500 credits
(12, 5, 1), -- Explorer: Explorer title
(13, 2, 1), -- World traveler: 500 credits

-- Special achievements
(14, 2, 1), -- Speed demon: 500 credits
(14, 4, 1), -- Speed demon: Warrior title
(15, 2, 1), -- Hidden master: 500 credits
(15, 8, 1), -- Hidden master: Hover scooter

-- Seasonal achievements
(16, 2, 1), -- Snowball champion: 500 credits
(17, 2, 1), -- Ice queen: 500 credits
(17, 9, 1); -- Ice queen: Companion drone
