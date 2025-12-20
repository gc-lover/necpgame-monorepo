-- Issue: #300
-- Import quest from: america\boston\2020-2029\quest-003-harvard-mit.yaml
-- Generated: 2025-12-19T22:21:00.000000

BEGIN;

-- Quest: boston-harvard-mit-rivalry-2020
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
    'boston-harvard-mit-rivalry-2020',
    'Красный vs Технический',
    'Разрешите вечное соперничество между Гарвардом и MIT в интеллектуальном поединке',
    'educational_competition',
    10,
    25,
    '{"tags": ["america", "boston", "quest", "education", "rivalry", "technology"]}'::jsonb,
    '[
        {
            "id": "visit-harvard-yard",
            "title": "Посетить Гарвард Ярд",
            "description": "Прибыть в исторический двор Гарвардского университета",
            "type": "location_visit",
            "coordinates": "42.3744,-71.1189",
            "required": true,
            "order": 1
        },
        {
            "id": "visit-mit-campus",
            "title": "Посетить кампус MIT",
            "description": "Исследовать технологический кампус Массачусетского технологического института",
            "type": "location_visit",
            "coordinates": "42.3601,-71.0942",
            "required": true,
            "order": 2
        },
        {
            "id": "solve-liberal-arts-puzzle",
            "title": "Решить гуманитарную задачу",
            "description": "Ответить на вопросы по истории, литературе и философии в Гарварде",
            "type": "quiz",
            "subject": "humanities",
            "question_count": 5,
            "required": false,
            "order": 3
        },
        {
            "id": "solve-engineering-challenge",
            "title": "Решить техническую задачу",
            "description": "Выполнить инженерные задачи и головоломки в MIT",
            "type": "puzzle",
            "subject": "engineering",
            "difficulty": "hard",
            "required": false,
            "order": 4
        },
        {
            "id": "attend-harvard-lecture",
            "title": "Послушать лекцию в Гарварде",
            "description": "Посетить лекцию по гуманитарным наукам в Sanders Theatre",
            "type": "attendance",
            "topic": "Этика ИИ в обществе",
            "duration": 30,
            "required": false,
            "order": 5
        },
        {
            "id": "attend-mit-seminar",
            "title": "Посетить семинар в MIT",
            "description": "Участвовать в техническом семинаре в Media Lab",
            "type": "attendance",
            "topic": "Будущее робототехники",
            "duration": 45,
            "required": false,
            "order": 6
        },
        {
            "id": "choose-side",
            "title": "Выбрать сторону",
            "description": "Присоединиться к одной из академических традиций",
            "type": "choice",
            "options": {
                "harvard": "Гарвард - традиции и гуманитарные науки",
                "mit": "MIT - инновации и технологии",
                "neutral": "Остаться нейтральным наблюдателем"
            },
            "required": true,
            "order": 7
        },
        {
            "id": "complete-final-challenge",
            "title": "Завершить финальное испытание",
            "description": "Пройти итоговое испытание в выбранном университете",
            "type": "final_exam",
            "required": true,
            "order": 8
        }
    ]'::jsonb,
    '{
        "experience": 10000,
        "achievement": {
            "id": "academic-champion",
            "title": "Академический Чемпион",
            "description": "Завершил соперничество Гарвард vs MIT",
            "icon": "diploma-badge"
        },
        "knowledge": {
            "topic": "education",
            "points": 200
        }
    }'::jsonb,
    '[
        {
            "id": "harvard-path",
            "condition": "Выбрал сторону Гарварда",
            "title": "Путь Традиций",
            "description": "Фокус на гуманитарных дисциплинах и классическом образовании",
            "objectives": [
                {
                    "id": "harvard-debate-club",
                    "title": "Присоединиться к дебат-клубу"
                },
                {
                    "id": "library-research",
                    "title": "Провести исследование в Widener Library"
                }
            ]
        },
        {
            "id": "mit-path",
            "condition": "Выбрал сторону MIT",
            "title": "Путь Инноваций",
            "description": "Фокус на технологиях и инженерных решениях",
            "objectives": [
                {
                    "id": "hackathon-participation",
                    "title": "Участвовать в хакатоне"
                },
                {
                    "id": "robotics-lab",
                    "title": "Работать в лаборатории робототехники"
                }
            ]
        },
        {
            "id": "balanced-path",
            "condition": "Остался нейтральным",
            "title": "Баланс Знаний",
            "description": "Комбинация подходов обоих университетов",
            "objectives": [
                {
                    "id": "interdisciplinary-project",
                    "title": "Работать над междисциплинарным проектом"
                }
            ]
        }
    ]'::jsonb,
    '{
        "metadata": {
            "id": "canon-quest-boston-2020-2029-harvard-mit",
            "title": "Бостон 2020-2029 — Гарвард vs MIT",
            "document_type": "canon",
            "category": "quest",
            "status": "draft",
            "version": "1.0.0",
            "last_updated": "2025-12-19T22:21:00Z",
            "concept_approved": false,
            "owners": [{"role": "content_writer", "contact": "content@necp.game"}],
            "tags": ["america", "boston", "quest", "education", "rivalry", "technology"],
            "topics": ["timeline-author", "academic-life"],
            "related_systems": ["gameplay-service", "quest-service", "character-service", "education-service"],
            "risk_level": "low"
        },
        "summary": {
            "problem": "Необходимо создать квест, отражающий знаменитое академическое соперничество между Гарвардом и MIT в Бостоне",
            "goal": "Разработать образовательный квест с элементами соревнования между двумя ведущими университетами",
            "essence": "Игрок участвует в академическом соревновании, посещая кампусы, решая задачи и выбирая сторону в вечном споре",
            "key_points": [
                "Исследование кампусов Гарварда и MIT",
                "Решение академических задач разной сложности",
                "Выбор между гуманитарным и техническим подходом",
                "Тематические награды от каждого университета"
            ]
        },
        "difficulty": "hard",
        "estimated_duration": 90,
        "recommended_level": "10-25"
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