-- Issue: #140890234
-- Import quest from: america\\las-vegas\\2020-2029\\quest-001-strip.yaml
-- Generated: 2025-12-23T22:40:00.000000

BEGIN;

-- Quest: quest-vegas-2029-strip
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('quest-vegas-2029-strip', 'Лас-Вегас 2020-2029 — Прогулка по Стрипу', 'Квест проводит игрока по Las Vegas Strip, знакомя с ключевыми отелями, достопримечательностями и атмосферой города, который никогда не спит.',
        'side', 5, NULL,
        '{\"required_quests\": [], \"required_flags\": [], \"required_reputation\": {}, \"required_items\": []}'::jsonb,
        '[{\"id\": \"welcome_sign_photo\", \"type\": \"interact\", \"target\": \"welcome_sign\", \"count\": 1, \"optional\": false}, {\"id\": \"bellagio_fountains\", \"type\": \"interact\", \"target\": \"bellagio_fountains\", \"count\": 1, \"optional\": false}, {\"id\": \"casino_visit\", \"type\": \"interact\", \"target\": \"caesars_palace\", \"count\": 1, \"optional\": false}, {\"id\": \"street_performers\", \"type\": \"interact\", \"target\": \"street_performers\", \"count\": 1, \"optional\": false}, {\"id\": \"neon_panorama\", \"type\": \"interact\", \"target\": \"strip_night_view\", \"count\": 1, \"optional\": false}]'::jsonb,
        '{\"xp\": 1500, \"currency\": 0, \"attributes\": {\"gambling\": 20}, \"achievements\": [{\"id\": \"strip_conqueror\", \"name\": \"Покоритель Стрипа\"}], \"reputation\": {}, \"items\": []}'::jsonb,
        '[{\"condition\": \"Все объективы выполнены\", \"outcome\": \"Игрок получает все награды и достижение\", \"next_quests\": []}, {\"condition\": \"Пропущены казино\", \"outcome\": \"Уменьшенное вознаграждение XP, нет баффа азарта\", \"next_quests\": []}]'::jsonb,
        '{\"sections\": [{\"id\": \"overview\", \"title\": \"Описание\", \"body\": \"Квест проводит игрока по Las Vegas Strip, знакомя с ключевыми отелями, достопримечательностями и атмосферой города, который никогда не спит.\"}, {\"id\": \"stages\", \"title\": \"Этапы\", \"body\": \"1. Сделать фото у знака «Welcome to Fabulous Las Vegas». 2. Посетить Bellagio и наблюдать фонтанное шоу. 3. Осмотреть Caesars Palace, Venetian и Luxor, собирая впечатления о тематическом дизайне. 4. Взаимодействовать с уличными артистами и посетить интерактивные аттракционы. 5. Зафиксировать неоновую панораму ночью и составить путевой отчёт.\"}, {\"id\": \"regional_details\", \"title\": \"Региональные детали\", \"body\": \"Las Vegas Strip тянется на 6,8 км; ежегодный оборот казино превышает $50 млрд. Город функционирует 24/7 и воспроизводит копии мировых достопримечательностей, усиливая эффект «искусственного мегаполиса».\"}]}'::jsonb,
        '2.0.0', true);

COMMIT;
