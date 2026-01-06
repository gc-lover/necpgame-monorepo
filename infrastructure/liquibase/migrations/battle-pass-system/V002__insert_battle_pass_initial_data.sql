-- Battle Pass System Initial Data
-- Version: V002
-- Description: Initial data for battle pass tracks, seasons, levels, rewards, and challenges

-- =================================================================================================
-- BATTLE PASS TRACKS
-- =================================================================================================

INSERT INTO battle_pass_tracks (track_key, name, description, track_type, price_cents, currency, is_enabled) VALUES
('free', 'Бесплатный трек', 'Базовый трек с основными наградами', 'FREE', NULL, NULL, true),
('premium', 'Премиум трек', 'Расширенный трек с дополнительными наградами', 'PREMIUM', 999, 'USD', true),
('ultimate', 'Ультимативный трек', 'Максимальный трек со всеми наградами', 'ULTIMATE', 1999, 'USD', true);

-- =================================================================================================
-- BATTLE PASS SEASONS
-- =================================================================================================

INSERT INTO battle_pass_seasons (
    season_key, name, description, season_type, status, start_date, end_date,
    max_level, base_xp_per_level, xp_multiplier, is_active
) VALUES
('season_2025_winter', 'Зима 2025', 'Первый сезон Battle Pass с зимней тематикой', 'REGULAR', 'ACTIVE',
 '2025-12-01 00:00:00+00', '2025-02-28 23:59:59+00', 100, 1000, 1.0, true);

-- Associate tracks with season
INSERT INTO battle_pass_season_tracks (season_id, track_id, is_default) VALUES
(1, 1, true),  -- Free track as default
(1, 2, false), -- Premium track
(1, 3, false); -- Ultimate track

-- =================================================================================================
-- BATTLE PASS REWARDS
-- =================================================================================================

INSERT INTO battle_pass_rewards (reward_key, name, description, reward_type, reward_category, rarity, value_data, is_stackable, max_stack) VALUES
-- Currency rewards
('credits_100', '100 Кредитов', 'Игровая валюта', 'CURRENCY', 'CURRENCY', 'common', '{"currency": "credits", "amount": 100}', true, 999),
('credits_500', '500 Кредитов', 'Игровая валюта', 'CURRENCY', 'CURRENCY', 'uncommon', '{"currency": "credits", "amount": 500}', true, 999),
('premium_currency_50', '50 Премиум Валюты', 'Премиум игровая валюта', 'CURRENCY', 'CURRENCY', 'rare', '{"currency": "premium", "amount": 50}', true, 999),

-- Cosmetic rewards
('emote_dance', 'Танцевальный эмут', 'Косметический танец', 'COSMETICS', 'EMOTE', 'common', '{"emote_id": "dance_01", "animation": "cyber_dance"}', false, 1),
('title_champion', 'Чемпион', 'Звание чемпиона', 'TITLES', 'TITLE', 'rare', '{"title_id": "champion", "color": "#FFD700"}', false, 1),
('weapon_skin_neon', 'Неоновый скин оружия', 'Яркий неоновый скин', 'COSMETICS', 'WEAPON_SKIN', 'epic', '{"skin_id": "neon_glow", "effect": "glowing_particles"}', false, 1),

-- Item rewards
('boost_xp_2x', '2x XP Буст', 'Удвоение получаемого опыта на 1 час', 'BOOSTERS', 'XP_BOOST', 'uncommon', '{"multiplier": 2.0, "duration_hours": 1}', false, 1),
('boost_currency_2x', '2x Валютный Буст', 'Удвоение получаемой валюты на 1 час', 'BOOSTERS', 'CURRENCY_BOOST', 'rare', '{"multiplier": 2.0, "duration_hours": 1}', false, 1),

-- Exclusive rewards
('pet_cyber_fox', 'Кибер-лиса', 'Милый питомец кибер-лиса', 'EXCLUSIVE', 'PET', 'legendary', '{"pet_id": "cyber_fox", "abilities": ["hack_assist", "stealth_mode"]}', false, 1),
('vehicle_hover_board', 'Ховер-доска', 'Летающая доска для быстрого перемещения', 'EXCLUSIVE', 'VEHICLE', 'legendary', '{"vehicle_id": "hover_board", "speed": 50, "durability": 100}', false, 1);

-- =================================================================================================
-- BATTLE PASS LEVELS (First 20 levels for demonstration)
-- =================================================================================================

-- Free track levels
INSERT INTO battle_pass_levels (season_id, track_id, level, xp_required, reward_data, is_premium_locked) VALUES
(1, 1, 1, 0, '{"credits": 50}', false),
(1, 1, 2, 1000, '{"emote": "thumbs_up"}', false),
(1, 1, 3, 2000, '{"credits": 75}', false),
(1, 1, 4, 3000, '{"title": "rookie"}', false),
(1, 1, 5, 4000, '{"credits": 100}', false),
(1, 1, 6, 5000, '{"boost": "xp_1_5x_30min"}', false),
(1, 1, 7, 6000, '{"credits": 125}', false),
(1, 1, 8, 7000, '{"cosmetic": "weapon_trail_blue"}', false),
(1, 1, 9, 8000, '{"credits": 150}', false),
(1, 1, 10, 9000, '{"title": "experienced"}', false),
(1, 1, 11, 10000, '{"credits": 175}', false),
(1, 1, 12, 11000, '{"boost": "currency_1_5x_30min"}', false),
(1, 1, 13, 12000, '{"credits": 200}', false),
(1, 1, 14, 13000, '{"cosmetic": "armor_glow_green"}', false),
(1, 1, 15, 14000, '{"credits": 225}', false),
(1, 1, 16, 15000, '{"title": "veteran"}', false),
(1, 1, 17, 16000, '{"credits": 250}', false),
(1, 1, 18, 17000, '{"boost": "xp_2x_1hour"}', false),
(1, 1, 19, 18000, '{"credits": 275}', false),
(1, 1, 20, 19000, '{"cosmetic": "pet_collar_gold"}', false);

-- Premium track levels (with bonus rewards)
INSERT INTO battle_pass_levels (season_id, track_id, level, xp_required, reward_data, bonus_reward_data, is_premium_locked) VALUES
(1, 2, 1, 0, '{"credits": 100}', '{"premium_currency": 25}', false),
(1, 2, 2, 1000, '{"emote": "victory_dance"}', '{"boost": "xp_1_25x_15min"}', false),
(1, 2, 3, 2000, '{"credits": 150}', '{"cosmetic": "weapon_skin_basic"}', false),
(1, 2, 4, 3000, '{"title": "elite"}', '{"credits": 50}', false),
(1, 2, 5, 4000, '{"credits": 200}', '{"boost": "currency_1_25x_15min"}', false),
(1, 2, 6, 5000, '{"cosmetic": "armor_pattern_cyber"}', '{"premium_currency": 25}', false),
(1, 2, 7, 6000, '{"credits": 250}', '{"emote": "flex"}', false),
(1, 2, 8, 7000, '{"title": "champion"}', '{"boost": "xp_1_5x_30min"}', false),
(1, 2, 9, 8000, '{"credits": 300}', '{"cosmetic": "pet_accessory_cyber"}', false),
(1, 2, 10, 9000, '{"boost": "xp_2x_1hour"}', '{"premium_currency": 50}', false),
(1, 2, 11, 10000, '{"credits": 350}', '{"title": "legend"}', false),
(1, 2, 12, 11000, '{"cosmetic": "weapon_trail_rainbow"}', '{"boost": "currency_1_5x_30min"}', false),
(1, 2, 13, 12000, '{"credits": 400}', '{"emote": "show_off"}', false),
(1, 2, 14, 13000, '{"title": "master"}', '{"cosmetic": "armor_glow_rainbow"}', false),
(1, 2, 15, 14000, '{"credits": 450}', '{"boost": "xp_2_5x_30min"}', false),
(1, 2, 16, 15000, '{"cosmetic": "pet_collar_diamond"}', '{"premium_currency": 75}', false),
(1, 2, 17, 16000, '{"credits": 500}', '{"title": "grandmaster"}', false),
(1, 2, 18, 17000, '{"boost": "currency_2x_1hour"}', '{"cosmetic": "weapon_skin_legendary"}', false),
(1, 2, 19, 18000, '{"credits": 550}', '{"emote": "legendary_pose"}', false),
(1, 2, 20, 19000, '{"title": "mythical"}', '{"premium_currency": 100}', false);

-- Associate rewards with levels
INSERT INTO battle_pass_level_rewards (level_id, reward_id, quantity, is_guaranteed) VALUES
-- Free track level 1: 50 credits
(1, 1, 1, true),
-- Free track level 5: 100 credits
(5, 2, 1, true),
-- Free track level 10: Experienced title
(10, 5, 1, true),
-- Premium track level 1: 100 credits + 25 premium
(21, 2, 1, true),
(21, 3, 25, true),
-- Premium track level 5: 200 credits + currency boost
(25, 2, 1, true),
(25, 7, 1, true);

-- =================================================================================================
-- CHALLENGES
-- =================================================================================================

INSERT INTO battle_pass_challenges (
    challenge_key, name, description, challenge_type, challenge_category,
    target_value, reward_xp, reward_data, is_active
) VALUES
-- Daily challenges
('daily_combat_kills', 'Ежедневные убийства', 'Убейте 10 врагов', 'DAILY', 'COMBAT', 10, 500, '{"bonus_xp": 500}', true),
('daily_quests_complete', 'Ежедневные квесты', 'Завершите 3 квеста', 'DAILY', 'PROGRESSION', 3, 750, '{"bonus_xp": 750}', true),
('daily_explore_areas', 'Исследование', 'Посетите 5 новых локаций', 'DAILY', 'EXPLORATION', 5, 400, '{"bonus_xp": 400}', true),

-- Weekly challenges
('weekly_boss_kills', 'Боссы недели', 'Убейте 3 босса', 'WEEKLY', 'COMBAT', 3, 2500, '{"bonus_xp": 2500, "cosmetic_unlock": "boss_slayer_title"}', true),
('weekly_social_interactions', 'Социальные связи', 'Добавьте 10 друзей', 'WEEKLY', 'SOCIAL', 10, 1000, '{"bonus_xp": 1000}', true),
('weekly_collection_items', 'Коллекционер', 'Соберите 25 редких предметов', 'WEEKLY', 'COLLECTION', 25, 1500, '{"bonus_xp": 1500}', true),

-- Seasonal challenges
('seasonal_level_up', 'Прогресс сезона', 'Достичь 50 уровня', 'SEASONAL', 'PROGRESSION', 50, 10000, '{"bonus_xp": 10000, "exclusive_cosmetic": "season_champion_emblem"}', true),
('seasonal_premium_purchase', 'Премиум поддержка', 'Купить премиум Battle Pass', 'SEASONAL', 'PROGRESSION', 1, 5000, '{"bonus_xp": 5000}', true);

-- Associate challenges with season
INSERT INTO battle_pass_season_challenges (season_id, challenge_id, is_required, sort_order) VALUES
(1, 1, false, 1),   -- Daily combat kills
(1, 2, false, 2),   -- Daily quests
(1, 3, false, 3),   -- Daily exploration
(1, 4, false, 4),   -- Weekly boss kills
(1, 5, false, 5),   -- Weekly social
(1, 6, false, 6),   -- Weekly collection
(1, 7, true, 7),    -- Seasonal level up (required)
(1, 8, false, 8);   -- Seasonal premium

-- =================================================================================================
-- PREMIUM TIERS
-- =================================================================================================

INSERT INTO battle_pass_premium_tiers (
    tier_key, name, description, price_cents, currency, duration_days, features
) VALUES
('basic', 'Базовый премиум', 'Доступ к премиум треку на текущий сезон', 999, 'USD', 90,
 '{"premium_track": true, "bonus_xp": 1.1, "exclusive_rewards": false}'),
('advanced', 'Расширенный премиум', 'Базовый премиум + следующий сезон', 1799, 'USD', 180,
 '{"premium_track": true, "bonus_xp": 1.15, "exclusive_rewards": true, "next_season_access": true}'),
('ultimate', 'Ультимативный премиум', 'Расширенный премиум + все награды', 2999, 'USD', 365,
 '{"premium_track": true, "bonus_xp": 1.25, "exclusive_rewards": true, "next_season_access": true, "all_rewards_unlocked": true}'),
('lifetime', 'Пожизненный премиум', 'Навсегда премиум для всех сезонов', 9999, 'USD', NULL,
 '{"premium_track": true, "bonus_xp": 1.5, "exclusive_rewards": true, "all_season_access": true, "all_rewards_unlocked": true, "priority_support": true}');

-- =================================================================================================
-- SEASON CONFIGURATION
-- =================================================================================================

INSERT INTO battle_pass_season_config (season_id, config_key, config_value, description) VALUES
(1, 'xp_multiplier_events', '{"quest_completion": 1.5, "combat_victory": 1.2, "daily_login": 0.5}',
 'Множители XP для разных типов активности'),
(1, 'premium_bonuses', '{"xp_boost": 1.25, "reward_priority": true, "exclusive_items": true}',
 'Бонусы для премиум игроков'),
(1, 'challenge_refresh_times', '{"daily": "00:00:00Z", "weekly": "Monday 00:00:00Z"}',
 'Время обновления челленджей'),
(1, 'season_themes', '{"winter": {"colors": ["#00FFFF", "#FFFFFF", "#0000FF"], "effects": ["snow_particles", "ice_glow"]}}',
 'Тематические настройки сезона');
