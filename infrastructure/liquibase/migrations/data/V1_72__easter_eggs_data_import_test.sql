-- Issue: #2262 - Import Cyberspace Easter Eggs Data (Test)
-- liquibase formatted sql

--changeset backend:easter-eggs-data-import-test dbms:postgresql
--comment: Import sample cyberspace easter eggs for testing

BEGIN;

-- Insert sample easter egg for testing
INSERT INTO easter_eggs (
    id, name, category, difficulty, description, content,
    location, discovery_method, rewards, lore_connections,
    status, created_at, updated_at
) VALUES (
    'easter-egg-turing-ghost',
    'Призрак Алана Тьюринга',
    'technology',
    'medium',
    'Голографический призрак легендарного математика объясняет основы кибербезопасности',
    'Демонстрирует эволюцию вычислительных машин от механических до квантовых в интерактивной форме',
    '{"network_type": "educational", "specific_areas": ["university_networks", "academic_databases"]}'::jsonb,
    '{"type": "pattern_following", "description": "Следование за странным алгоритмом в образовательных сетях"}'::jsonb,
    '[{"type": "experience", "value": 500}, {"type": "achievement", "name": "Криптограф"}]'::jsonb,
    '["cryptography_basics", "turing_machine_history"]'::jsonb,
    'active',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO UPDATE SET
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

COMMIT;
