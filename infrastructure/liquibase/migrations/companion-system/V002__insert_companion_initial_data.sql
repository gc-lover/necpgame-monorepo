-- Companion System Initial Data
-- Version: V002
-- Description: Initial data for companion types, templates, abilities, and progression

-- =================================================================================================
-- COMPANION TYPES
-- =================================================================================================

INSERT INTO companion_types (type_key, name, description, category, rarity, base_level, max_level, purchase_cost, currency_type, is_purchasable, is_enabled) VALUES
-- Combat Drones
('combat_drone_attack', 'Боевой дрон атаки', 'Агрессивный дрон для ближнего боя', 'combat_drone', 'common', 1, 50, 1000, 'credits', true, true),
('combat_drone_support', 'Дрон поддержки', 'Дрон для лечения и баффов союзников', 'combat_drone', 'uncommon', 1, 50, 2500, 'credits', true, true),
('combat_drone_recon', 'Разведывательный дрон', 'Дрон для обнаружения врагов и сбора разведданных', 'combat_drone', 'rare', 1, 50, 5000, 'credits', true, true),
('combat_drone_sniper', 'Снайперский дрон', 'Дрон для точных дальних выстрелов', 'combat_drone', 'epic', 1, 50, 10000, 'credits', true, true),

-- Utility Companions
('utility_loot_collector', 'Сборщик лута', 'Компаньон для автоматического сбора предметов', 'utility', 'common', 1, 30, 500, 'credits', true, true),
('utility_hacking_assistant', 'Помощник по хакерству', 'Компаньон для помощи в взломе систем', 'utility', 'uncommon', 1, 40, 3000, 'credits', true, true),
('utility_crafting_bot', 'Крафтовый бот', 'Компаньон для помощи в создании предметов', 'utility', 'rare', 1, 35, 7500, 'credits', true, true),

-- Pets
('pet_cyber_cat', 'Кибер-кошка', 'Милый питомец с хакерскими способностями', 'pet', 'uncommon', 1, 25, 1500, 'credits', true, true),
('pet_neural_dog', 'Нейронный пес', 'Лояльный питомец с боевыми навыками', 'pet', 'rare', 1, 30, 4000, 'credits', true, true),
('pet_data_raven', 'Дроно-воран', 'Разведывательный питомец с данными о врагах', 'pet', 'epic', 1, 35, 8000, 'credits', true, true),

-- Vehicles
('vehicle_hover_scooter', 'Ховер-скутер', 'Быстрое транспортное средство', 'vehicle', 'common', 1, 20, 2000, 'credits', true, true),
('vehicle_attack_bike', 'Боевой байк', 'Вооруженное транспортное средство', 'vehicle', 'rare', 1, 40, 15000, 'credits', true, true);

-- =================================================================================================
-- COMPANION TEMPLATES
-- =================================================================================================

INSERT INTO companion_templates (companion_type_id, template_key, name, description, base_stats, appearance_data, is_default) VALUES
-- Combat Drone Attack
(1, 'drone_attack_mk1', 'Боевой дрон Мк.I', 'Стандартный боевой дрон', '{"health": 100, "damage": 25, "armor": 10, "speed": 15, "range": 5}', '{"color": "red", "pattern": "military"}', true),
(1, 'drone_attack_mk2', 'Боевой дрон Мк.II', 'Улучшенная версия боевого дрона', '{"health": 120, "damage": 30, "armor": 12, "speed": 16, "range": 6}', '{"color": "red", "pattern": "advanced"}', false),

-- Combat Drone Support
(2, 'drone_support_healer', 'Дрон-целитель', 'Специализируется на лечении', '{"health": 80, "healing": 20, "armor": 8, "speed": 12, "range": 15}', '{"color": "blue", "pattern": "medical"}', true),
(2, 'drone_support_buffer', 'Дрон-баффер', 'Усиливает союзников', '{"health": 90, "buff_power": 25, "armor": 9, "speed": 13, "range": 10}', '{"color": "green", "pattern": "support"}', false),

-- Utility Loot Collector
(5, 'collector_basic', 'Базовый сборщик', 'Простой сборщик лута', '{"health": 50, "collection_speed": 10, "armor": 5, "speed": 10}', '{"color": "yellow", "pattern": "industrial"}', true),

-- Cyber Cat
(9, 'cat_hacker', 'Хакер-кошка', 'Кошачий помощник по хакерству', '{"health": 60, "hacking_skill": 15, "armor": 6, "speed": 18, "stealth": 12}', '{"color": "purple", "pattern": "cyber"}', true);

-- =================================================================================================
-- COMPANION ABILITIES
-- =================================================================================================

INSERT INTO companion_abilities (ability_key, name, description, type, category, cooldown_seconds, energy_cost, range, duration_seconds, effect_data) VALUES
-- Combat Abilities
('drone_attack_primary', 'Основная атака', 'Стандартная атака дрона', 'active', 'attack', 2, 10, 5, 0, '{"damage": 25, "damage_type": "kinetic"}'),
('drone_support_heal', 'Лечение', 'Восстанавливает здоровье союзника', 'active', 'support', 5, 20, 15, 0, '{"healing": 30, "heal_type": "energy"}'),
('drone_recon_scan', 'Сканирование', 'Обнаруживает скрытых врагов', 'active', 'utility', 10, 15, 25, 30, '{"reveal_duration": 30, "scan_radius": 25}'),

-- Utility Abilities
('loot_collection_auto', 'Автосбор', 'Автоматически собирает лут', 'passive', 'utility', 0, 0, 0, 0, '{"collection_radius": 10, "collection_speed": 2}'),
('hacking_assist_boost', 'Хакерский буст', 'Увеличивает скорость хакерства', 'toggle', 'utility', 0, 5, 0, 0, '{"hack_speed_multiplier": 1.5, "energy_per_second": 2}'),

-- Pet Abilities
('cat_hack_overload', 'Перегрузка', 'Временно выводит из строя электронные устройства', 'active', 'utility', 30, 50, 8, 10, '{"disable_duration": 10, "range": 8}'),
('dog_combat_charge', 'Боевой рывок', 'Быстрая атака с повышенным уроном', 'active', 'attack', 15, 30, 3, 0, '{"damage": 40, "speed_boost": 2, "duration": 3}');

-- =================================================================================================
-- TEMPLATE ABILITIES MAPPING
-- =================================================================================================

INSERT INTO companion_template_abilities (companion_template_id, companion_ability_id, unlock_level, is_default) VALUES
-- Combat Drone Mk.I
(1, 1, 1, true), -- Primary attack at level 1

-- Combat Drone Mk.II
(2, 1, 1, true), -- Primary attack at level 1

-- Support Drone Healer
(3, 2, 1, true), -- Heal ability at level 1

-- Support Drone Buffer
(4, 3, 1, true), -- Scan ability at level 1

-- Loot Collector
(5, 4, 1, true), -- Auto collection at level 1

-- Cyber Cat
(6, 5, 1, true), -- Hack boost at level 1
(6, 6, 5, false); -- Hack overload at level 5

-- =================================================================================================
-- EXPERIENCE SOURCES
-- =================================================================================================

INSERT INTO companion_experience_sources (source_key, name, description, experience_multiplier, is_enabled) VALUES
('combat_kill', 'Убийство врага', 'Опыт за убийство врагов', 1.0, true),
('quest_completion', 'Завершение квеста', 'Опыт за выполнение квестов', 2.0, true),
('loot_collection', 'Сбор лута', 'Опыт за сбор предметов', 0.5, true),
('hacking_success', 'Успешный хак', 'Опыт за успешные хакерские операции', 1.5, true),
('exploration', 'Исследование', 'Опыт за открытие новых локаций', 0.8, true),
('daily_login', 'Ежедневный вход', 'Опыт за ежедневный вход в игру', 0.3, true),
('achievement_unlock', 'Достижение', 'Опыт за разблокировку достижений', 3.0, true);

-- =================================================================================================
-- LEVEL PROGRESSION
-- =================================================================================================

-- Insert level progression for combat drone attack (simplified)
INSERT INTO companion_levels (companion_template_id, level, experience_required, stat_multipliers, unlock_abilities) VALUES
(1, 1, 0, '{"health": 1.0, "damage": 1.0}', '[]'),
(1, 2, 100, '{"health": 1.05, "damage": 1.03}', '[]'),
(1, 3, 250, '{"health": 1.1, "damage": 1.06}', '[]'),
(1, 4, 450, '{"health": 1.15, "damage": 1.09}', '[]'),
(1, 5, 700, '{"health": 1.2, "damage": 1.12}', '[]'),
(1, 6, 1000, '{"health": 1.25, "damage": 1.15}', '[]'),
(1, 7, 1350, '{"health": 1.3, "damage": 1.18}', '[]'),
(1, 8, 1750, '{"health": 1.35, "damage": 1.21}', '[]'),
(1, 9, 2200, '{"health": 1.4, "damage": 1.24}', '[]'),
(1, 10, 2700, '{"health": 1.45, "damage": 1.27}', '[]'),
(1, 11, 3250, '{"health": 1.5, "damage": 1.3}', '[]'),
(1, 12, 3850, '{"health": 1.55, "damage": 1.33}', '[]'),
(1, 13, 4500, '{"health": 1.6, "damage": 1.36}', '[]'),
(1, 14, 5200, '{"health": 1.65, "damage": 1.39}', '[]'),
(1, 15, 5950, '{"health": 1.7, "damage": 1.42}', '[]'),
(1, 16, 6750, '{"health": 1.75, "damage": 1.45}', '[]'),
(1, 17, 7600, '{"health": 1.8, "damage": 1.48}', '[]'),
(1, 18, 8500, '{"health": 1.85, "damage": 1.51}', '[]'),
(1, 19, 9450, '{"health": 1.9, "damage": 1.54}', '[]'),
(1, 20, 10450, '{"health": 1.95, "damage": 1.57}', '[]');

-- =================================================================================================
-- INVENTORY SLOTS INITIALIZATION
-- =================================================================================================

-- Note: This would typically be done during character creation
-- The actual implementation would insert slots for each character

-- Example initialization for character creation trigger:
-- INSERT INTO companion_inventory_slots (character_id, slot_number, is_unlocked)
-- SELECT NEW.id, generate_series(1, 3), true
-- FROM characters WHERE id = NEW.id;
