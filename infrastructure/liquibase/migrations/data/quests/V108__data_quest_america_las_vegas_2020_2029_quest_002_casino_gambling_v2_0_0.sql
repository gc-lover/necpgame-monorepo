-- Issue: #140890234
-- Import quest from: america\\las-vegas\\2020-2029\\quest-002-casino-gambling.yaml
-- Generated: 2025-12-23T22:45:00.000000

BEGIN;

-- Quest: quest-vegas-2029-casino-gambling
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('quest-vegas-2029-casino-gambling', 'Лас-Вегас 2020-2029 — Казино и азарт', 'Квест знакомит игрока с миром казино Las Vegas, раскрывая механики азартных игр, стратегии ставок и психологические ловушки индустрии развлечений.',
        'side', 5, NULL,
        '{\"required_quests\": [], \"required_flags\": [], \"required_reputation\": {}, \"required_items\": []}'::jsonb,
        '[{\"id\": \"learn_casino_games\", \"text\": \"Изучить правила рулетки, блэкджека, слотов и покера\", \"type\": \"interact\", \"target\": \"casino_tutorial\", \"count\": 1, \"optional\": false}, {\"id\": \"play_roulette\", \"text\": \"Сыграть в рулетку с минимальной ставкой\", \"type\": \"interact\", \"target\": \"roulette_table\", \"count\": 1, \"optional\": false}, {\"id\": \"play_blackjack\", \"text\": \"Сыграть в блэкджек, применяя стратегию\", \"type\": \"interact\", \"target\": \"blackjack_table\", \"count\": 1, \"optional\": false}, {\"id\": \"try_slots\", \"text\": \"Попробовать игровые автоматы\", \"type\": \"interact\", \"target\": \"slot_machines\", \"count\": 1, \"optional\": false}, {\"id\": \"observe_house_edge\", \"text\": \"Осознать преимущество казино и управление рисками\", \"type\": \"interact\", \"target\": \"gambling_psychology\", \"count\": 1, \"optional\": false}]'::jsonb,
        '{\"xp\": 1200, \"currency\": 0, \"attributes\": {\"luck\": 15}, \"achievements\": [{\"id\": \"casino_novice\", \"name\": \"Новичок казино\"}], \"reputation\": {\"casino_owners\": 10}, \"items\": []}'::jsonb,
        '[{\"condition\": \"Выиграл в игры\", \"outcome\": \"Дополнительные кредиты и повышенная удача\", \"next_quests\": []}, {\"condition\": \"Проиграл ставки\", \"outcome\": \"Уменьшенное XP, урок о рисках\", \"next_quests\": []}]'::jsonb,
        '{\"sections\": [{\"id\": \"overview\", \"title\": \"Описание\", \"body\": \"Квест знакомит игрока с миром казино Las Vegas, раскрывая механики азартных игр, стратегии ставок и психологические ловушки индустрии развлечений.\"}, {\"id\": \"stages\", \"title\": \"Этапы\", \"body\": \"1. Изучить правила основных казино-игр. 2. Сыграть в рулетку и блэкджек с минимальными ставками. 3. Попробовать игровые автоматы. 4. Осознать преимущество казино и управление рисками.\"}, {\"id\": \"gambling_mechanics\", \"title\": \"Механики азарта\", \"body\": \"Казино используют house edge (преимущество казино) для обеспечения прибыли. Игры с разными шансами: слоты 5-10%, рулетка 5.26%, блэкджек может быть побеждён стратегией.\"}]}'::jsonb,
        '2.0.0', true);

COMMIT;
