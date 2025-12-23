-- Issue: #140890241
-- Import quest from: america\\los-angeles\\2020-2029\\quest-001-hollywood-sign.yaml
-- Generated: 2025-12-23T22:50:00.000000

BEGIN;

-- Quest: canon-quest-los-angeles-hollywood-sign-2020-2029
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-los-angeles-hollywood-sign-2020-2029', 'Лос-Анджелес: знак Hollywood', 'Игроку предстоит проникнуть на территорию знака Hollywood, взобраться на букву H и установить хакерское устройство для распространения нелегального контента.',
        'side', 8, NULL,
        '{\"required_quests\": [], \"required_flags\": [], \"required_reputation\": {}, \"required_items\": []}'::jsonb,
        '[{\"id\": \"infiltrate_hollywood_hills\", \"text\": \"Проникнуть на территорию Hollywood Hills\", \"type\": \"interact\", \"target\": \"hollywood_hills_security\", \"count\": 1, \"optional\": false}, {\"id\": \"climb_hollywood_sign\", \"text\": \"Взобраться на букву H знака Hollywood\", \"type\": \"interact\", \"target\": \"hollywood_sign_h\", \"count\": 1, \"optional\": false}, {\"id\": \"install_hacker_device\", \"text\": \"Установить хакерское устройство на знак\", \"type\": \"interact\", \"target\": \"hacker_device\", \"count\": 1, \"optional\": false}, {\"id\": \"escape_pursuit\", \"text\": \"Уйти от преследования охраны\", \"type\": \"interact\", \"target\": \"security_pursuit\", \"count\": 1, \"optional\": false}]'::jsonb,
        '{\"xp\": 1800, \"currency\": 0, \"attributes\": {\"stealth\": 25}, \"achievements\": [{\"id\": \"hollywood_infiltrator\", \"name\": \"Проникатель Голливуда\"}], \"reputation\": {\"underground\": 15}, \"items\": []}'::jsonb,
        '[{\"condition\": \"Успешное проникновение\", \"outcome\": \"Полные награды и достижение\", \"next_quests\": []}, {\"condition\": \"Задержан охраной\", \"outcome\": \"Штраф и пониженная репутация\", \"next_quests\": []}]'::jsonb,
        '{\"sections\": [{\"id\": \"overview\", \"title\": \"Описание\", \"body\": \"Игроку предстоит проникнуть на территорию знака Hollywood, взобраться на букву H и установить хакерское устройство для распространения нелегального контента.\"}, {\"id\": \"hollywood_lore\", \"title\": \"Лор Голливуда\", \"body\": \"Знак Hollywood был установлен в 1923 году. Сегодня это символ индустрии развлечений, охраняемый круглосуточно.\"}]}'::jsonb,
        '2.0.0', true);

COMMIT;
