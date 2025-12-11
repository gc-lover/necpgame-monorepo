-- Issue: #1560
-- liquibase formatted sql

-- changeset database-engineer:088-create-projectile-forms-tables
-- comment: Создание таблиц для системы уникальных форм проджектайлов

-- ============================================================================
-- 1. ТАБЛИЦА: projectile_forms
-- ============================================================================

CREATE TABLE IF NOT EXISTS projectile_forms (
  id VARCHAR(50) PRIMARY KEY,
  description TEXT,
  name VARCHAR(100) NOT NULL UNIQUE,
  type VARCHAR(50) NOT NULL,
  visual_effect VARCHAR(100),
  parameters JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  -- Constraints
    CONSTRAINT chk_projectile_form_type CHECK (
        type IN (
            'point', 'line', 'fan', 'spiral', 'wave', 
            'circle', 'square', 'chain', 'split', 'ricochet', 'phasing'
        )
    )
);

-- Index
CREATE INDEX idx_projectile_forms_type ON projectile_forms(type);
CREATE INDEX idx_projectile_forms_name ON projectile_forms(name);

-- Comment
COMMENT ON TABLE projectile_forms IS 'Формы проджектайлов для системы оружия';
COMMENT ON COLUMN projectile_forms.id IS 'Уникальный идентификатор формы';
COMMENT ON COLUMN projectile_forms.type IS 'Тип формы (point, line, fan, spiral, wave, circle, square, chain, split, ricochet, phasing)';
COMMENT ON COLUMN projectile_forms.parameters IS 'JSON параметры формы (spread_angle, projectile_count, etc.)';

-- ============================================================================
-- 2. ТАБЛИЦА: projectile_compatibility
-- ============================================================================

CREATE TABLE IF NOT EXISTS projectile_compatibility (
  id SERIAL PRIMARY KEY,
  weapon_type VARCHAR(50) NOT NULL,
  projectile_form_id VARCHAR(50) NOT NULL,
  restrictions JSONB DEFAULT '{}',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  is_compatible BOOLEAN NOT NULL DEFAULT true,
  -- Foreign Keys
    CONSTRAINT fk_projectile_compatibility_form 
        FOREIGN KEY (projectile_form_id) 
        REFERENCES projectile_forms(id) 
        ON DELETE CASCADE,
  -- Constraints
    CONSTRAINT chk_weapon_type CHECK (
        weapon_type IN (
            'pistols', 'rifles', 'shotguns', 'smg', 'sniper_rifles',
            'launchers', 'melee', 'laser_weapons', 'energy_weapons', 'throwable'
        )
    ),
  -- Unique constraint
    CONSTRAINT uq_projectile_compatibility_weapon_form 
        UNIQUE (weapon_type, projectile_form_id)
);

-- Indexes
CREATE INDEX idx_projectile_compatibility_weapon ON projectile_compatibility(weapon_type);
CREATE INDEX idx_projectile_compatibility_form ON projectile_compatibility(projectile_form_id);
CREATE INDEX idx_projectile_compatibility_compatible ON projectile_compatibility(is_compatible);

-- Comment
COMMENT ON TABLE projectile_compatibility IS 'Совместимость форм проджектайлов с типами оружия';

-- ============================================================================
-- 3. ТАБЛИЦА: trajectory_algorithms
-- ============================================================================

CREATE TABLE IF NOT EXISTS trajectory_algorithms (
  id VARCHAR(50) PRIMARY KEY,
  description TEXT,
  name VARCHAR(100) NOT NULL UNIQUE,
  projectile_form_id VARCHAR(50) NOT NULL,
  algorithm_type VARCHAR(50) NOT NULL,
  parameters JSONB NOT NULL DEFAULT '{}',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  performance_cost INT NOT NULL DEFAULT 1,
  -- Foreign Keys
    CONSTRAINT fk_trajectory_algorithm_form 
        FOREIGN KEY (projectile_form_id) 
        REFERENCES projectile_forms(id) 
        ON DELETE CASCADE,
  -- Constraints
    CONSTRAINT chk_trajectory_algorithm_type CHECK (
        algorithm_type IN (
            'linear_trajectory', 'parallel_lines', 'cone_spread',
            'helical_trajectory', 'sinusoidal_trajectory', 'radial_spread',
            'grid_pattern', 'chain_reaction', 'split_trajectory',
            'bounce_trajectory', 'penetration_trajectory'
        )
    ),
  CONSTRAINT chk_performance_cost CHECK (performance_cost BETWEEN 1 AND 10)
);

-- Indexes
CREATE INDEX idx_trajectory_algorithms_form ON trajectory_algorithms(projectile_form_id);
CREATE INDEX idx_trajectory_algorithms_type ON trajectory_algorithms(algorithm_type);
CREATE INDEX idx_trajectory_algorithms_performance ON trajectory_algorithms(performance_cost);

-- Comment
COMMENT ON TABLE trajectory_algorithms IS 'Алгоритмы расчета траекторий для форм проджектайлов';
COMMENT ON COLUMN trajectory_algorithms.performance_cost IS 'Стоимость расчета (1-10, где 10 - самый дорогой)';

-- ============================================================================
-- 4. НАЧАЛЬНЫЕ ДАННЫЕ: Формы проджектайлов
-- ============================================================================

INSERT INTO projectile_forms (id, name, type, description, parameters, visual_effect) VALUES
('form_point', 'Point', 'point', 'Стандартная пуля - прямая траектория', 
 '{"accuracy": "high", "damage_multiplier": 1.0}', 'single_tracer_line'),

('form_line_h', 'Line Horizontal', 'line', 'Горизонтальная линия снарядов',
 '{"projectile_count": 5, "spacing": 0.5, "orientation": "horizontal"}', 'multiple_tracer_lines'),

('form_line_v', 'Line Vertical', 'line', 'Вертикальная линия снарядов',
 '{"projectile_count": 5, "spacing": 0.5, "orientation": "vertical"}', 'multiple_tracer_lines'),

('form_fan_h', 'Fan Horizontal', 'fan', 'Горизонтальный веер',
 '{"spread_angle": 30, "projectile_count": 8}', 'fan_pattern_tracers'),

('form_fan_v', 'Fan Vertical', 'fan', 'Вертикальный веер',
 '{"spread_angle": 30, "projectile_count": 8}', 'fan_pattern_tracers'),

('form_fan_360', 'Fan Circular', 'fan', 'Круговой веер (360°)',
 '{"spread_angle": 360, "projectile_count": 12}', 'fan_pattern_tracers'),

('form_spiral', 'Spiral', 'spiral', 'Спиральная траектория',
 '{"spiral_radius": 0.5, "rotation_speed": 360, "projectile_count": 3}', 'spiral_trail_effect'),

('form_wave', 'Wave', 'wave', 'Волнообразная траектория',
 '{"amplitude": 1.0, "frequency": 2.0}', 'wave_trail_effect'),

('form_circle', 'Circle', 'circle', 'Круговое рассеивание',
 '{"projectile_count": 16, "radius": 1.0}', 'radial_tracers'),

('form_square', 'Square', 'square', 'Квадратный паттерн',
 '{"grid_size": 3, "spacing": 0.5}', 'grid_pattern_tracers'),

('form_chain', 'Chain', 'chain', 'Цепная реакция между целями',
 '{"max_targets": 5, "chain_radius": 10, "damage_falloff": [1.0, 0.7, 0.5, 0.3, 0.2]}', 'electric_arc_effect'),

('form_split', 'Split', 'split', 'Разделение на части',
 '{"split_count": 5, "split_angle": 30}', 'explosion_split_effect'),

('form_ricochet', 'Ricochet', 'ricochet', 'Рикошеты от поверхностей',
 '{"max_bounces": 5, "damage_falloff": [1.0, 0.9, 0.8, 0.7, 0.6]}', 'bounce_spark_effect'),

('form_phasing', 'Phasing', 'phasing', 'Прохождение через препятствия',
 '{"max_penetration_depth": 2.0, "damage_through_walls": 0.8}', 'quantum_phase_effect')

ON CONFLICT (id) DO NOTHING;

-- ============================================================================
-- 5. НАЧАЛЬНЫЕ ДАННЫЕ: Совместимость
-- ============================================================================

INSERT INTO projectile_compatibility (weapon_type, projectile_form_id, is_compatible) VALUES
-- Пистолеты
('pistols', 'form_point', true),
('pistols', 'form_line_h', true),
('pistols', 'form_fan_h', true),
('pistols', 'form_spiral', true),
('pistols', 'form_ricochet', true),

-- Винтовки
('rifles', 'form_point', true),
('rifles', 'form_line_h', true),
('rifles', 'form_phasing', true),

-- Дробовики
('shotguns', 'form_fan_h', true),
('shotguns', 'form_fan_v', true),
('shotguns', 'form_circle', true),
('shotguns', 'form_square', true),
('shotguns', 'form_split', true),

-- ПП
('smg', 'form_point', true),
('smg', 'form_line_h', true),
('smg', 'form_fan_h', true),
('smg', 'form_spiral', true),

-- Снайперские винтовки
('sniper_rifles', 'form_point', true),
('sniper_rifles', 'form_phasing', true),
('sniper_rifles', 'form_ricochet', true),

-- Пусковые установки
('launchers', 'form_point', true),
('launchers', 'form_split', true),

-- Ближний бой
('melee', 'form_wave', true),
('melee', 'form_chain', true),

-- Лазерное оружие
('laser_weapons', 'form_point', true),
('laser_weapons', 'form_wave', true),
('laser_weapons', 'form_phasing', true),

-- Энергетическое оружие
('energy_weapons', 'form_chain', true),
('energy_weapons', 'form_wave', true),
('energy_weapons', 'form_spiral', true),
('energy_weapons', 'form_phasing', true),
('energy_weapons', 'form_ricochet', true),

-- Метательное оружие
('throwable', 'form_point', true),
('throwable', 'form_fan_h', true),
('throwable', 'form_ricochet', true)

ON CONFLICT (weapon_type, projectile_form_id) DO NOTHING;

-- ============================================================================
-- 6. НАЧАЛЬНЫЕ ДАННЫЕ: Алгоритмы траекторий
-- ============================================================================

INSERT INTO trajectory_algorithms (id, name, projectile_form_id, algorithm_type, parameters, performance_cost) VALUES
('algo_linear', 'Linear Trajectory', 'form_point', 'linear_trajectory',
 '{"speed": 100.0, "gravity_factor": 0.0}', 1),

('algo_parallel_lines', 'Parallel Lines', 'form_line_h', 'parallel_lines',
 '{"line_count": 5, "spacing": 0.5, "speed": 100.0}', 2),

('algo_cone_spread', 'Cone Spread', 'form_fan_h', 'cone_spread',
 '{"spread_angle": 30, "projectile_count": 8, "speed": 100.0}', 3),

('algo_helical', 'Helical Trajectory', 'form_spiral', 'helical_trajectory',
 '{"radius": 0.5, "rotation_speed": 360, "forward_speed": 100.0}', 4),

('algo_sinusoidal', 'Sinusoidal Wave', 'form_wave', 'sinusoidal_trajectory',
 '{"amplitude": 1.0, "frequency": 2.0, "speed": 100.0}', 3),

('algo_radial_spread', 'Radial Spread', 'form_circle', 'radial_spread',
 '{"projectile_count": 16, "speed": 100.0}', 4),

('algo_grid_pattern', 'Grid Pattern', 'form_square', 'grid_pattern',
 '{"grid_size": 3, "spacing": 0.5, "speed": 100.0}', 3),

('algo_chain_reaction', 'Chain Reaction', 'form_chain', 'chain_reaction',
 '{"max_targets": 5, "chain_radius": 10.0, "chain_delay": 0.1}', 5),

('algo_split', 'Split Trajectory', 'form_split', 'split_trajectory',
 '{"split_count": 5, "split_angle": 30, "split_distance": 5.0}', 4),

('algo_bounce', 'Bounce Trajectory', 'form_ricochet', 'bounce_trajectory',
 '{"max_bounces": 5, "bounce_factor": 0.9, "speed": 100.0}', 5),

('algo_penetration', 'Penetration Trajectory', 'form_phasing', 'penetration_trajectory',
 '{"max_penetration_depth": 2.0, "speed": 100.0}', 3)

ON CONFLICT (id) DO NOTHING;

-- ============================================================================
-- 7. INDEXES ДЛЯ ПРОИЗВОДИТЕЛЬНОСТИ
-- ============================================================================

-- Индекс для быстрого поиска совместимых форм для оружия
CREATE INDEX IF NOT EXISTS idx_compatibility_lookup 
ON projectile_compatibility(weapon_type, is_compatible);

-- Индекс для поиска алгоритмов по стоимости производительности
CREATE INDEX IF NOT EXISTS idx_algorithms_by_cost 
ON trajectory_algorithms(performance_cost, algorithm_type);

-- ============================================================================
-- 8. ROLLBACK
-- ============================================================================

-- rollback DROP TABLE IF EXISTS trajectory_algorithms;
-- rollback DROP TABLE IF EXISTS projectile_compatibility;
-- rollback DROP TABLE IF EXISTS projectile_forms;


























