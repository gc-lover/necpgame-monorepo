-- Issue: #300
-- Import quest from: america\boston\2020-2029\quest-001-freedom-trail.yaml
-- Generated: 2025-12-19T22:21:00.000000

BEGIN;

-- Quest: boston-freedom-trail-2020
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
    'boston-freedom-trail-2020',
    'По следам Свободы',
    'Следуйте по исторической красной линии через Бостон и узнайте историю рождения Америки',
    'exploration',
    1,
    10,
    '{"tags": ["america", "boston", "quest", "freedom-trail", "revolution"]}'::jsonb,
    '[
        {
            "id": "visit-boston-common",
            "title": "Бостон Коммон",
            "description": "Посетите старейший общественный парк в Америке",
            "type": "location_visit",
            "coordinates": "42.3551,-71.0657",
            "required": true,
            "order": 1
        },
        {
            "id": "visit-state-house",
            "title": "Массачусетс Стейт Хаус",
            "description": "Осмотрите золотой купол и узнайте о правительстве штата",
            "type": "location_visit",
            "coordinates": "42.3588,-71.0638",
            "required": true,
            "order": 2
        },
        {
            "id": "visit-park-street-church",
            "title": "Парк Стрит Черч",
            "description": "Посетите церковь, где пел гимн ''Америка''",
            "type": "location_visit",
            "coordinates": "42.3565,-71.0621",
            "required": true,
            "order": 3
        },
        {
            "id": "visit-granary-burying-ground",
            "title": "Гранари Бериинг Граунд",
            "description": "Почтите память Сэмюэля Адамса и Пола Ревира",
            "type": "location_visit",
            "coordinates": "42.3578,-71.0617",
            "required": true,
            "order": 4
        },
        {
            "id": "visit-king-chapel",
            "title": "Кингс Чепел",
            "description": "Осмотрите старейшую церковь Бостона",
            "type": "location_visit",
            "coordinates": "42.3583,-71.0602",
            "required": true,
            "order": 5
        },
        {
            "id": "visit-benjamin-franklin-statue",
            "title": "Статуя Бенджамина Франклина",
            "description": "Найдите статую молодого Франклина на School Street",
            "type": "location_visit",
            "coordinates": "42.3574,-71.0589",
            "required": true,
            "order": 6
        },
        {
            "id": "visit-old-corner-bookstore",
            "title": "Олд Корнер Букстор",
            "description": "Посетите место, где печатались революционные памфлеты",
            "type": "location_visit",
            "coordinates": "42.3572,-71.0585",
            "required": true,
            "order": 7
        },
        {
            "id": "visit-old-south-meeting-house",
            "title": "Олд Саус Митинг Хаус",
            "description": "Место собрания патриотов перед Бостонским чаепитием",
            "type": "location_visit",
            "coordinates": "42.3575,-71.0566",
            "required": true,
            "order": 8
        },
        {
            "id": "visit-old-state-house",
            "title": "Олд Стейт Хаус",
            "description": "Здание, где заседал колониальный совет Массачусетса",
            "type": "location_visit",
            "coordinates": "42.3588,-71.0569",
            "required": true,
            "order": 9
        },
        {
            "id": "visit-boston-massacre-site",
            "title": "Место Бостонской резни",
            "description": "Почтите память жертв конфликта 1770 года",
            "type": "location_visit",
            "coordinates": "42.3601,-71.0567",
            "required": true,
            "order": 10
        },
        {
            "id": "visit-faneuil-hall",
            "title": "Фэнюэл Холл",
            "description": "Посетите ''Колыбель Свободы'' и рынок",
            "type": "location_visit",
            "coordinates": "42.3601,-71.0544",
            "required": true,
            "order": 11
        },
        {
            "id": "visit-paul-revere-house",
            "title": "Дом Пола Ревира",
            "description": "Осмотрите дом знаменитого всадника",
            "type": "location_visit",
            "coordinates": "42.3636,-71.0534",
            "required": true,
            "order": 12
        },
        {
            "id": "visit-old-north-church",
            "title": "Олд Норт Черч",
            "description": "Церковь, где висели сигналы ''Один если сушей''",
            "type": "location_visit",
            "coordinates": "42.3663,-71.0545",
            "required": true,
            "order": 13
        },
        {
            "id": "visit-copp-hill-burying-ground",
            "title": "Коппс Хилл Бериинг Граунд",
            "description": "Кладбище с могилами революционеров",
            "type": "location_visit",
            "coordinates": "42.3675,-71.0567",
            "required": true,
            "order": 14
        },
        {
            "id": "visit-uss-constitution",
            "title": "USS Constitution",
            "description": "Посетите старейший военный корабль США",
            "type": "location_visit",
            "coordinates": "42.3724,-71.0567",
            "required": true,
            "order": 15
        },
        {
            "id": "visit-bunker-hill-monument",
            "title": "Банкер Хилл Монумент",
            "description": "Финальная точка - место знаменитой битвы",
            "type": "location_visit",
            "coordinates": "42.3763,-71.0610",
            "required": true,
            "order": 16
        }
    ]'::jsonb,
    '{
        "experience": 5000,
        "achievement": {
            "id": "freedom-trail-complete",
            "title": "Следопыт Свободы",
            "description": "Завершил маршрут по Freedom Trail",
            "icon": "freedom-trail-badge"
        },
        "knowledge": {
            "topic": "american-revolution",
            "points": 100
        },
        "item": {
            "id": "revolutionary-medallion",
            "name": "Революционная медаль",
            "description": "Сувенир с изображением Минервы",
            "rarity": "uncommon"
        }
    }'::jsonb,
    '[]'::jsonb,
    '{
        "metadata": {
            "id": "canon-quest-boston-2020-2029-freedom-trail",
            "title": "Бостон 2020-2029 — Тропа Свободы",
            "document_type": "canon",
            "category": "quest",
            "status": "draft",
            "version": "1.0.0",
            "last_updated": "2025-12-19T22:21:00Z",
            "concept_approved": false,
            "owners": [{"role": "content_writer", "contact": "content@necp.game"}],
            "tags": ["america", "boston", "quest", "freedom-trail", "revolution"],
            "topics": ["timeline-author", "historical-sites"],
            "related_systems": ["gameplay-service", "quest-service", "character-service"],
            "risk_level": "low"
        },
        "summary": {
            "problem": "Необходимо создать квест по Freedom Trail в Бостоне для периода 2020-2029",
            "goal": "Разработать интерактивный квест, знакомящий игроков с историческими местами Американской революции",
            "essence": "Игрок следует по красной линии через 16 исторических точек, собирая артефакты и получая знания об основании США",
            "key_points": [
                "Следование по красной линии через весь Бостон",
                "Взаимодействие с историческими артефактами",
                "Образовательный контент об Американской революции",
                "Система достижений за посещение всех точек"
            ]
        },
        "difficulty": "easy",
        "estimated_duration": 45,
        "recommended_level": "1-10"
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