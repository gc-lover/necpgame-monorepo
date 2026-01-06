-- Weapon Elemental Effects Initial Data
-- Version: V002
-- Description: Initial data for elemental types, effects, interactions, and weapon configurations

-- =================================================================================================
-- ELEMENTAL TYPES
-- =================================================================================================

INSERT INTO elemental_types (
    element_key, name, description, color_code, icon_url,
    base_damage_type, visual_effect_type, sound_effect_type
) VALUES
('fire', '{"en": "Fire", "ru": "Огонь"}',
 '{"en": "Burning flames that cause damage over time and spread to nearby targets", "ru": "Пламя, наносящее урон со временем и распространяющееся на соседние цели"}',
 '#FF4500', '/icons/elements/fire.png', 'FIRE', 'PARTICLES', 'CONTINUOUS'),

('ice', '{"en": "Ice", "ru": "Лед"}',
 '{"en": "Freezing cold that slows movement and can completely freeze targets", "ru": "Ледяной холод, замедляющий движение и способный полностью заморозить цели"}',
 '#00BFFF', '/icons/elements/ice.png', 'COLD', 'MODEL_OVERLAY', 'BURST'),

('poison', '{"en": "Poison", "ru": "Яд"}',
 '{"en": "Toxic substances that build up and cause damage over time", "ru": "Токсичные вещества, накапливающиеся и наносящие урон со временем"}',
 '#32CD32', '/icons/elements/poison.png', 'POISON', 'SCREEN_DISTORTION', 'LOOP'),

('acid', '{"en": "Acid", "ru": "Кислота"}',
 '{"en": "Corrosive substances that damage armor and surfaces", "ru": "Разъедающие вещества, повреждающие броню и поверхности"}',
 '#FFD700', '/icons/elements/acid.png', 'ACID', 'PARTICLES', 'CONTINUOUS');

-- =================================================================================================
-- ELEMENTAL EFFECTS
-- =================================================================================================

INSERT INTO elemental_effects (
    effect_key, element_id, name, description, effect_type, damage_type,
    base_damage, damage_per_second, duration_seconds, tick_interval_seconds,
    max_stacks, stat_modifiers, visual_config, sound_config
) VALUES
-- Fire Effects
('fire_burn', 1, '{"en": "Burn", "ru": "Горение"}',
 '{"en": "Target takes fire damage over time", "ru": "Цель получает урон огнем со временем"}',
 'DOT_DAMAGE', 'FIRE', 0, 15, 5, 1.0, 3,
 '{}', '{"particles": "fire_sparks", "color": "#FF4500"}', '{"sound": "fire_crackle", "loop": true}'),

('fire_spread', 1, '{"en": "Fire Spread", "ru": "Распространение огня"}',
 '{"en": "Fire can jump to nearby targets", "ru": "Огонь может перекинуться на соседние цели"}',
 'STATUS_EFFECT', 'FIRE', 0, 0, 0, 0, 1,
 '{}', '{"particles": "fire_jump", "color": "#FF6347"}', '{"sound": "fire_whoosh"}'),

-- Ice Effects
('ice_slow', 2, '{"en": "Frost Slow", "ru": "Ледяное замедление"}',
 '{"en": "Target movement and attack speed reduced", "ru": "Замедление движения и скорости атаки цели"}',
 'MOVEMENT_MODIFIER', 'COLD', 0, 0, 8, 0, 1,
 '{"movement_speed": -0.4, "attack_speed": -0.3}', '{"overlay": "frost_crystal", "color": "#87CEEB"}', '{"sound": "ice_crack"}'),

('ice_freeze', 2, '{"en": "Deep Freeze", "ru": "Глубокая заморозка"}',
 '{"en": "Target completely frozen and unable to move", "ru": "Цель полностью заморожена и не может двигаться"}',
 'STATUS_EFFECT', 'COLD', 0, 0, 3, 0, 1,
 '{"movement_speed": -1.0, "attack_speed": -1.0}', '{"overlay": "ice_block", "color": "#FFFFFF"}', '{"sound": "ice_shatter"}'),

-- Poison Effects
('poison_damage', 3, '{"en": "Toxin Buildup", "ru": "Накопление токсинов"}',
 '{"en": "Poison damage that builds up over time", "ru": "Урон ядом, накапливающийся со временем"}',
 'DOT_DAMAGE', 'POISON', 0, 8, 10, 2.0, 5,
 '{"health_regen": -0.5}', '{"distortion": "poison_wave", "color": "#32CD32"}', '{"sound": "poison_bubble"}'),

('poison_spread', 3, '{"en": "Contagion", "ru": "Заражение"}',
 '{"en": "Poison can spread to nearby targets", "ru": "Яд может распространиться на соседние цели"}',
 'STATUS_EFFECT', 'POISON', 0, 0, 0, 0, 1,
 '{}', '{"particles": "poison_cloud", "color": "#228B22"}', '{"sound": "poison_spread"}'),

-- Acid Effects
('acid_corrosion', 4, '{"en": "Armor Corrosion", "ru": "Коррозия брони"}',
 '{"en": "Reduces target armor effectiveness", "ru": "Снижает эффективность брони цели"}',
 'DEFENSE_MODIFIER', 'ACID', 0, 0, 12, 0, 1,
 '{"armor_effectiveness": -0.25}', '{"particles": "acid_drip", "color": "#FFD700"}', '{"sound": "acid_bubble"}'),

('acid_surface_damage', 4, '{"en": "Surface Erosion", "ru": "Эрозия поверхности"}',
 '{"en": "Damages surfaces and creates hazardous areas", "ru": "Повреждает поверхности и создает опасные зоны"}',
 'STATUS_EFFECT', 'ACID', 5, 0, 0, 0, 1,
 '{}', '{"particles": "acid_pool", "color": "#FFA500"}', '{"sound": "acid_sizzle"}');

-- =================================================================================================
-- ELEMENTAL EFFECT MODIFIERS
-- =================================================================================================

INSERT INTO elemental_effect_modifiers (effect_id, modifier_type, modifier_key, damage_multiplier, duration_multiplier, effect_chance_bonus) VALUES
-- Fire modifiers
(1, 'ARMOR_TYPE', 'light_armor', 1.5, 1.2, 0.15), -- Fire more effective vs light armor
(1, 'ARMOR_TYPE', 'heavy_armor', 0.8, 0.9, -0.10), -- Fire less effective vs heavy armor
(1, 'TARGET_TYPE', 'organic', 1.3, 1.1, 0.10), -- Fire more effective vs organic targets

-- Ice modifiers
(3, 'WEAPON_TYPE', 'shotgun', 1.2, 1.3, 0.20), -- Ice more effective with shotguns
(3, 'ENVIRONMENT', 'water', 1.4, 1.5, 0.25), -- Ice stronger near water
(3, 'TARGET_TYPE', 'mechanical', 0.9, 0.8, -0.15), -- Ice less effective vs mechanical

-- Poison modifiers
(5, 'TARGET_TYPE', 'organic', 1.4, 1.2, 0.20), -- Poison very effective vs organic
(5, 'TARGET_TYPE', 'mechanical', 0.6, 0.7, -0.25), -- Poison ineffective vs mechanical
(5, 'ENVIRONMENT', 'underground', 1.1, 1.3, 0.10), -- Poison stronger underground

-- Acid modifiers
(7, 'ARMOR_TYPE', 'heavy_armor', 1.6, 1.4, 0.30), -- Acid very effective vs heavy armor
(7, 'TARGET_TYPE', 'mechanical', 1.2, 1.1, 0.15), -- Acid effective vs mechanical
(7, 'TARGET_TYPE', 'organic', 0.9, 0.8, -0.10), -- Acid less effective vs organic;

-- =================================================================================================
-- ELEMENTAL INTERACTIONS
-- =================================================================================================

INSERT INTO elemental_interactions (
    primary_element_id, secondary_element_id, interaction_type,
    result_element_id, damage_multiplier, duration_multiplier,
    description, visual_config, sound_config
) VALUES
-- Fire + Ice = Steam (counter interaction)
(1, 2, 'COUNTER', NULL, 0.3, 0.5,
 '{"en": "Creates steam cloud, reducing fire damage", "ru": "Создает облако пара, снижая урон огня"}',
 '{"effect": "steam_cloud", "duration": 5}', '{"sound": "steam_hiss"}'),

-- Fire + Poison = Toxic Explosion (chain reaction)
(1, 3, 'CHAIN_REACTION', 3, 2.0, 1.5,
 '{"en": "Creates toxic explosion with amplified damage", "ru": "Создает токсичный взрыв с усиленным уроном"}',
 '{"effect": "toxic_explosion", "radius": 3}', '{"sound": "explosion_toxic"}'),

-- Ice + Poison = Frozen Toxin (amplify)
(2, 3, 'AMPLIFY', 3, 1.5, 2.0,
 '{"en": "Ice preserves poison, extending duration", "ru": "Лед сохраняет яд, увеличивая длительность"}',
 '{"effect": "frozen_poison", "color": "#00CED1"}', '{"sound": "ice_crystal"}'),

-- Ice + Acid = Corrosive Sludge (combine)
(2, 4, 'COMBINE', 4, 1.8, 1.3,
 '{"en": "Creates corrosive sludge with combined effects", "ru": "Создает разъедающую жижу с комбинированными эффектами"}',
 '{"effect": "corrosive_sludge", "spread_radius": 2}', '{"sound": "sludge_bubble"}'),

-- Poison + Acid = Mutagenic Poison (amplify)
(3, 4, 'AMPLIFY', 3, 2.2, 1.8,
 '{"en": "Creates extremely potent mutagenic poison", "ru": "Создает чрезвычайно сильный мутагенный яд"}',
 '{"effect": "mutagenic_cloud", "mutation_chance": 0.1}', '{"sound": "mutation_gurgle"}');

-- =================================================================================================
-- ELEMENTAL INTERACTION TRIGGERS
-- =================================================================================================

INSERT INTO elemental_interaction_triggers (
    interaction_id, trigger_type, trigger_condition, effect_config, probability, cooldown_seconds
) VALUES
-- Fire + Ice trigger (on contact)
(1, 'ON_CONTACT', '{}', '{"steam_cloud_radius": 2, "visibility_reduction": 0.6}', 1.0, 0),

-- Fire + Poison trigger (on stack overflow)
(2, 'ON_STACK_OVERFLOW', '{"min_stacks": 3}', '{"explosion_damage": 150, "poison_spread_chance": 0.4}', 0.8, 10),

-- Ice + Poison trigger (on time expire)
(3, 'ON_TIME_EXPIRE', '{"remaining_duration_under": 2}', '{"toxin_preservation": true, "damage_boost": 0.25}', 1.0, 0),

-- Poison + Acid trigger (on damage received)
(5, 'ON_DAMAGE_RECEIVED', '{"damage_type": "FIRE"}', '{"mutation_effects": ["damage_boost", "spread_increase"]}', 0.6, 30);

-- =================================================================================================
-- WEAPON ELEMENTAL CONFIGURATIONS
-- =================================================================================================

INSERT INTO weapon_elemental_configs (
    weapon_type, weapon_subtype, element_id, base_effect_chance,
    effect_duration_seconds, effect_damage_multiplier,
    ammo_consumption_modifier, heat_generation_modifier,
    recoil_modifier, fire_rate_modifier, config_data
) VALUES
-- Rifles with Fire
('rifle', 'assault_rifle', 1, 0.25, 6, 1.0, 1.1, 1.3, 1.0, 0.95,
 '{"spread_increase": 0.15, "ignition_chance": 0.1}'),

('rifle', 'sniper_rifle', 1, 0.35, 8, 1.2, 1.2, 1.4, 1.05, 0.9,
 '{"headshot_multiplier": 1.5, "penetration_bonus": 0.2}'),

-- Shotguns with Ice
('shotgun', 'combat_shotgun', 2, 0.40, 5, 0.9, 1.0, 0.8, 1.2, 0.85,
 '{"spread_slow": 0.3, "freeze_chance": 0.15}'),

('shotgun', 'tactical_shotgun', 2, 0.30, 7, 1.1, 1.1, 0.9, 1.1, 0.9,
 '{"area_freeze": true, "slow_duration_bonus": 2}'),

-- Pistols with Poison
('pistol', 'combat_pistol', 3, 0.20, 8, 0.8, 0.9, 1.1, 0.95, 1.05,
 '{"toxin_buildup": 1.2, "spread_radius": 1.5}'),

('pistol', 'revolver', 3, 0.45, 6, 1.3, 1.3, 1.5, 1.1, 0.8,
 '{"critical_poison": true, "damage_per_tick": 12}'),

-- Melee with Acid
('melee', 'combat_knife', 4, 0.60, 4, 0.7, 1.0, 0.7, 1.0, 1.0,
 '{"armor_reduction": 0.25, "surface_damage": true}'),

('melee', 'crowbar', 4, 0.50, 5, 0.8, 1.0, 0.8, 1.0, 1.0,
 '{"corrosion_dot": true, "metal_bonus": 0.3}'),

-- Grenades with Fire
('grenade', 'incendiary_grenade', 1, 0.90, 10, 1.5, 1.0, 2.0, 0.0, 0.0,
 '{"explosion_radius": 4, "burn_damage": 25, "spread_chance": 0.8}'),

('grenade', 'cryo_grenade', 2, 0.85, 12, 1.4, 1.0, 0.5, 0.0, 0.0,
 '{"freeze_radius": 3, "slow_strength": 0.8, "freeze_duration": 6}'),

-- Launchers with Poison
('launcher', 'toxic_launcher', 3, 0.75, 15, 1.6, 1.2, 1.8, 1.3, 0.3,
 '{"cloud_radius": 5, "toxin_damage": 20, "spread_chance": 0.6}'),

('launcher', 'acid_launcher', 4, 0.70, 12, 1.7, 1.3, 1.9, 1.4, 0.25,
 '{"corrosion_area": 4, "armor_penetration": 0.5, "surface_damage": 30}');

-- =================================================================================================
-- WEAPON ELEMENTAL UPGRADES
-- =================================================================================================

INSERT INTO weapon_elemental_upgrades (
    base_config_id, upgrade_level, upgrade_cost,
    effect_chance_bonus, damage_multiplier_bonus, duration_bonus_seconds,
    unlock_requirements
) VALUES
-- Assault Rifle Fire upgrades
(1, 2, '{"credits": 5000, "materials": {"rare_metal": 3}}', 0.10, 0.2, 2,
 '{"player_level": 15, "kills_with_weapon": 50}'),

(1, 3, '{"credits": 15000, "materials": {"rare_metal": 8, "energy_cell": 2}}', 0.15, 0.4, 3,
 '{"player_level": 30, "kills_with_weapon": 200, "previous_upgrade": 2}'),

-- Combat Shotgun Ice upgrades
(3, 2, '{"credits": 6000, "materials": {"cryo_crystal": 4}}', 0.12, 0.25, 2,
 '{"player_level": 18, "kills_with_weapon": 75}'),

(3, 3, '{"credits": 18000, "materials": {"cryo_crystal": 10, "rare_metal": 5}}', 0.18, 0.45, 4,
 '{"player_level": 35, "kills_with_weapon": 300, "previous_upgrade": 2}');

-- =================================================================================================
-- ENVIRONMENTAL ELEMENTAL ZONES
-- =================================================================================================

INSERT INTO environmental_elemental_zones (
    zone_key, zone_type, element_id, effect_id,
    zone_center, zone_radius, zone_height, effect_strength,
    effect_interval_seconds, max_concurrent_effects, visual_config
) VALUES
('volcano_lava_pool', 'FIRE_SOURCE', 1, 1,
 '{"x": 1000.5, "y": 500.2, "z": 0}', 5.0, 1.0, 1.5, 2.0, 8,
 '{"particles": "lava_bubbles", "color": "#FF4500", "intensity": "high"}'),

('arctic_ice_field', 'WATER', 2, 3,
 '{"x": -500.3, "y": 1200.8, "z": 5}', 15.0, 2.0, 1.2, 3.0, 12,
 '{"particles": "snow_flakes", "color": "#87CEEB", "temperature_effect": true}'),

('chemical_spill', 'ACID_POOL', 4, 7,
 '{"x": 250.7, "y": -300.4, "z": 0}', 8.0, 0.5, 1.8, 1.5, 6,
 '{"particles": "acid_drips", "color": "#FFD700", "corrosion_effect": true}'),

('toxic_waste_dump', 'TOXIC_AREA', 3, 5,
 '{"x": -800.2, "y": -600.9, "z": 1}', 12.0, 3.0, 2.0, 4.0, 10,
 '{"particles": "toxic_gas", "color": "#32CD32", "poison_cloud": true}');

-- =================================================================================================
-- ELEMENTAL BALANCE CONFIGURATIONS
-- =================================================================================================

INSERT INTO elemental_balance_configs (
    config_key, config_type, target_element_id, config_data
) VALUES
('global_damage_multiplier', 'GLOBAL_MULTIPLIER', NULL,
 '{"damage_multiplier": 1.0, "duration_multiplier": 1.0, "effect_chance_multiplier": 1.0}'),

('fire_damage_boost', 'ELEMENT_SPECIFIC', 1,
 '{"damage_multiplier": 1.1, "burn_spread_chance": 0.12, "ignition_threshold": 3}'),

('ice_slow_nerf', 'ELEMENT_SPECIFIC', 2,
 '{"slow_percentage_max": 0.6, "freeze_duration_max": 4, "brittle_damage_bonus": 0.4}'),

('poison_balance', 'ELEMENT_SPECIFIC', 3,
 '{"max_stacks": 6, "spread_radius_max": 3, "regen_reduction_max": 0.7}'),

('acid_armor_pen', 'ELEMENT_SPECIFIC', 4,
 '{"armor_reduction_max": 0.35, "corrosion_rate": 0.8, "surface_damage_max": 45}');

-- =================================================================================================
-- A/B TESTING CONFIGURATIONS
-- =================================================================================================

INSERT INTO elemental_ab_tests (
    test_key, test_name, description, test_type, target_element_id,
    control_group_config, test_group_config, test_percentage,
    start_date, end_date
) VALUES
('fire_damage_increase', 'Fire Damage Boost Test', 'Testing increased fire damage for better combat feel', 'DAMAGE_MULTIPLIER', 1,
 '{"damage_multiplier": 1.0}', '{"damage_multiplier": 1.15}', 25.0,
 CURRENT_DATE, CURRENT_DATE + INTERVAL '14 days'),

('ice_freeze_duration', 'Ice Freeze Duration Test', 'Testing longer freeze duration vs faster freeze chance', 'DURATION', 2,
 '{"freeze_duration": 3}', '{"freeze_duration": 4}', 30.0,
 CURRENT_DATE, CURRENT_DATE + INTERVAL '21 days');
