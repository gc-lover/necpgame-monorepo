--liquibase formatted sql

--changeset necp:data_industrial_interactives_v1_0_0 runOnChange:false
--comment: Import industrial interactives data from YAML specification

-- Insert main industrial interactive objects
INSERT INTO world_interactives (interactive_name, display_name, category, description, base_health, is_destructible, respawn_time_seconds) VALUES
('electrical_panel', 'Электрощит/Рубильник', 'power_control', 'Панели управления электропитанием для локального контроля освещения, лазерных систем и безопасности. Взлом или отключение создает зоны темноты, снижает эффективность патрулей или вызывает аварии.', 200, true, 180),
('control_valve', 'Контрольный клапан', 'fluid_control', 'Клапаны управления потоками химических веществ, пара или охлаждающих жидкостей. Могут вызывать утечки, паровые взрывы или отключение систем охлаждения.', 150, true, 240),
('conveyor_system', 'Конвейерная система', 'transportation', 'Промышленные конвейеры для перемещения материалов и грузов. Могут быть перенаправлены для создания альтернативных маршрутов или блокировки проходов.', 300, false, 0),
('industrial_crane', 'Промышленный кран', 'heavy_machinery', 'Грузоподъемные краны и манипуляторы для перемещения тяжелых объектов. Могут быть использованы для создания баррикад или альтернативных проходов.', 500, true, 600)
ON CONFLICT (interactive_name) DO UPDATE SET
    display_name = EXCLUDED.display_name,
    category = EXCLUDED.category,
    description = EXCLUDED.description,
    base_health = EXCLUDED.base_health,
    is_destructible = EXCLUDED.is_destructible,
    respawn_time_seconds = EXCLUDED.respawn_time_seconds,
    updated_at = NOW();

-- Insert electrical panel types
INSERT INTO interactive_types (interactive_id, type_name, variant_name, signal_strength, jamming_resistance, bandwidth_mbps) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'electrical_panel'), 'main_distribution', 'power_grid', 80, 70, 0),
((SELECT id FROM world_interactives WHERE interactive_name = 'electrical_panel'), 'security_grid', 'laser_systems', 60, 50, 0),
((SELECT id FROM world_interactives WHERE interactive_name = 'electrical_panel'), 'emergency_backup', 'auxiliary_power', 40, 30, 0)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    signal_strength = EXCLUDED.signal_strength,
    jamming_resistance = EXCLUDED.jamming_resistance,
    bandwidth_mbps = EXCLUDED.bandwidth_mbps,
    updated_at = NOW();

-- Insert control valve types (using medical station schema since valves control fluids)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, healing_rate_per_second, cyberware_repair, trauma_team_available) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'control_valve'), 'chemical_valve', 'toxic_release', -20, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'control_valve'), 'steam_valve', 'pressure_release', -15, false, false),
((SELECT id FROM world_interactives WHERE interactive_name = 'control_valve'), 'coolant_valve', 'overheat_risk', -10, false, false)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    healing_rate_per_second = EXCLUDED.healing_rate_per_second,
    cyberware_repair = EXCLUDED.cyberware_repair,
    trauma_team_available = EXCLUDED.trauma_team_available,
    updated_at = NOW();

-- Insert conveyor system types (using logistics container schema)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, storage_capacity, security_level, loot_quality) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'conveyor_system'), 'material_transport', 'bulk_goods', 2000, 'none', 'common'),
((SELECT id FROM world_interactives WHERE interactive_name = 'conveyor_system'), 'assembly_line', 'component_flow', 500, 'basic', 'uncommon'),
((SELECT id FROM world_interactives WHERE interactive_name = 'conveyor_system'), 'sorting_system', 'package_distribution', 1000, 'basic', 'common')
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    storage_capacity = EXCLUDED.storage_capacity,
    security_level = EXCLUDED.security_level,
    loot_quality = EXCLUDED.loot_quality,
    updated_at = NOW();

-- Insert industrial crane types (using faction blockpost takeover mechanics)
INSERT INTO interactive_types (interactive_id, type_name, variant_name, takeover_method, takeover_cost_eddies_min, takeover_cost_eddies_max, takeover_success_rate_min, takeover_success_rate_max, takeover_detection_risk_percent, takeover_time_seconds) VALUES
((SELECT id FROM world_interactives WHERE interactive_name = 'industrial_crane'), 'overhead_crane', 'heavy_lift', 'hacking', 300, 600, 50, 75, 35, 45),
((SELECT id FROM world_interactives WHERE interactive_name = 'industrial_crane'), 'gantry_crane', 'precision_lift', 'hacking', 400, 800, 45, 70, 40, 60),
((SELECT id FROM world_interactives WHERE interactive_name = 'industrial_crane'), 'jib_crane', 'mobile_lift', 'assault', 200, 500, 60, 85, 50, 30)
ON CONFLICT (interactive_id, type_name, COALESCE(variant_name, '')) DO UPDATE SET
    takeover_method = EXCLUDED.takeover_method,
    takeover_cost_eddies_min = EXCLUDED.takeover_cost_eddies_min,
    takeover_cost_eddies_max = EXCLUDED.takeover_cost_eddies_max,
    takeover_success_rate_min = EXCLUDED.takeover_success_rate_min,
    takeover_success_rate_max = EXCLUDED.takeover_success_rate_max,
    takeover_detection_risk_percent = EXCLUDED.takeover_detection_risk_percent,
    takeover_time_seconds = EXCLUDED.takeover_time_seconds,
    updated_at = NOW();

--rollback DELETE FROM interactive_types WHERE interactive_id IN (SELECT id FROM world_interactives WHERE interactive_name IN ('electrical_panel', 'control_valve', 'conveyor_system', 'industrial_crane'));
--rollback DELETE FROM world_interactives WHERE interactive_name IN ('electrical_panel', 'control_valve', 'conveyor_system', 'industrial_crane');