-- Issue: #300
-- Import quest from: america\boston\2020-2029\quest-002-boston-tea-party.yaml
-- Generated: 2025-12-19T22:21:00.000000

BEGIN;

-- Quest: boston-tea-party-2020
INSERT INTO gameplay.quest_definitions (
    quest_id,
    title,
    description,
    quest_type,
    level_min,
    level_max,
    requirements,
    objectives,
    rewards,
    branches,
    content_data,
    version,
    is_active
) VALUES (
    'boston-tea-party-2020',
    'Чай в гавань!',
    'Присоединяйтесь к Sons of Liberty в протесте против британского налога на чай',
    'historical_reenactment',
    5,
    15,
    '{"tags": ["america", "boston", "quest", "tea-party", "revolution", "protest"]}'::jsonb,
    '[
        {
            "id": "visit-tea-party-museum",
            "title": "Посетить Музей Бостонского чаепития",
            "description": "Прибыть в Boston Tea Party Ships & Museum на пристани Конгресс-стрит",
            "type": "location_visit",
            "coordinates": "42.3521,-71.0513",
            "required": true,
            "order": 1
        },
        {
            "id": "learn-about-tea-act",
            "title": "Изучить Tea Act 1773",
            "description": "Просмотреть экспозицию о британском налогообложении чая",
            "type": "information_gathering",
            "required": true,
            "order": 2
        },
        {
            "id": "join-sons-of-liberty",
            "title": "Присоединиться к Sons of Liberty",
            "description": "Встретить реконструкторов в костюмах колонистов",
            "type": "npc_interaction",
            "npc_id": "samuel_adams_reenactor",
            "required": true,
            "order": 3
        },
        {
            "id": "board-the-ship",
            "title": "Подняться на корабль",
            "description": "Зайти на реплику корабля Dartmouth в гавани",
            "type": "location_visit",
            "coordinates": "42.3521,-71.0513",
            "required": true,
            "order": 4
        },
        {
            "id": "throw-tea-boxes",
            "title": "Бросить ящики с чаем",
            "description": "Участвовать в интерактивной реконструкции - бросить 5 ящиков чая за борт",
            "type": "mini_game",
            "game_type": "throwing",
            "target_count": 5,
            "required": true,
            "order": 5
        },
        {
            "id": "avoid-british-patrol",
            "title": "Избежать британского патруля",
            "description": "Скрыться от кораблей британского флота в гавани",
            "type": "stealth_mini_game",
            "required": false,
            "order": 6
        },
        {
            "id": "hear-paul-revere-signal",
            "title": "Услышать сигнал Пола Ревира",
            "description": "Обратите внимание на сигнал ''Один если сушей'' с Олд Норт Черч",
            "type": "environmental_interaction",
            "required": false,
            "order": 7
        },
        {
            "id": "complete-protest",
            "title": "Завершить протест",
            "description": "Убедиться, что весь чай оказался в гавани Бостона",
            "type": "completion_check",
            "required": true,
            "order": 8
        }
    ]'::jsonb,
    '{
        "experience": 7500,
        "achievement": {
            "id": "tea-party-participant",
            "title": "Участник Чайной Вечеринки",
            "description": "Успешно провел Бостонское чаепитие",
            "icon": "tea-cup-rebel"
        },
        "knowledge": {
            "topic": "american-revolution",
            "points": 150
        },
        "item": {
            "id": "liberty-cap",
            "name": "Колпак Свободы",
            "description": "Символ республиканских идей",
            "rarity": "rare"
        },
        "social": {
            "type": "reputation",
            "faction": "sons_of_liberty",
            "amount": 100
        }
    }'::jsonb,
    '[
        {
            "id": "peaceful-protest",
            "condition": "Бросил чай аккуратно, без повреждений корабля",
            "title": "Мирный Протест",
            "description": "Дополнительная награда за ненасильственный подход",
            "rewards": {
                "achievement": {
                    "id": "peaceful-revolutionary",
                    "title": "Мирный Революционер"
                }
            }
        },
        {
            "id": "radical-action",
            "condition": "Повредил корабль или устроил хаос",
            "title": "Радикальные Действия",
            "description": "Привлек внимание британцев, но усилил эффект протеста",
            "rewards": {
                "item": {
                    "id": "wanted-poster",
                    "name": "Плакат ''Разыскивается''",
                    "rarity": "uncommon"
                }
            }
        }
    ]'::jsonb,
    '{
        "metadata": {
            "id": "canon-quest-boston-2020-2029-tea-party",
            "title": "Бостон 2020-2029 — Бостонское чаепитие",
            "document_type": "canon",
            "category": "quest",
            "status": "draft",
            "version": "1.0.0",
            "last_updated": "2025-12-19T22:21:00Z",
            "concept_approved": false,
            "owners": [{"role": "content_writer", "contact": "content@necp.game"}],
            "tags": ["america", "boston", "quest", "tea-party", "revolution", "protest"],
            "topics": ["timeline-author", "historical-events"],
            "related_systems": ["gameplay-service", "quest-service", "character-service", "social-service"],
            "risk_level": "low"
        },
        "summary": {
            "problem": "Необходимо создать квест, посвященный Бостонскому чаепитию в современном Бостоне",
            "goal": "Разработать интерактивный квест о событиях 1773 года с использованием музея Boston Tea Party Ships & Museum",
            "essence": "Игрок участвует в реконструкции исторического протеста, бросая ящики чая в гавань и узнавая о налоговом сопротивлении",
            "key_points": [
                "Интерактивная реконструкция на кораблях-музеях",
                "Образовательный контент о принципах налогообложения",
                "Тематика протеста и Sons of Liberty",
                "Мини-игры с бросанием чая"
            ]
        },
        "difficulty": "medium",
        "estimated_duration": 60,
        "recommended_level": "5-15"
    }'::jsonb,
    1,
    true
) ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    quest_type = EXCLUDED.quest_type,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    requirements = EXCLUDED.requirements,
    objectives = EXCLUDED.objectives,
    rewards = EXCLUDED.rewards,
    branches = EXCLUDED.branches,
    content_data = EXCLUDED.content_data,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest