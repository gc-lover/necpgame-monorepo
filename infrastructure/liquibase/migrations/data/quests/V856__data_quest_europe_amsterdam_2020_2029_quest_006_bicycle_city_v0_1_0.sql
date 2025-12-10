-- Issue: #55
-- Import quest from: europe\amsterdam\2020-2029\quest-006-bicycle-city.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T21:22:00.000000

BEGIN;

-- Quest: canon-quest-amsterdam-2020-2029-bicycle-city
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
    'canon-quest-amsterdam-2020-2029-bicycle-city',
    'Амстердам 2020-2029 — Город велосипедов',
    'Игрок арендует fiets, едет по красным велодорожкам, посещает велостоянку у вокзала и изучает культуру приоритета велосипеда.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"rent_bike","text":"Арендовать голландский велосипед и изучить правила","type":"interact","target":"rent_fiets","count":1,"optional":false},
      {"id":"ride_bikelanes","text":"Проехать по красным велодорожкам с плотным трафиком","type":"interact","target":"amsterdam_bikelanes","count":1,"optional":false},
      {"id":"visit_parking","text":"Посетить многоуровневую велостоянку у вокзала","type":"interact","target":"central_station_bike_parking","count":1,"optional":false},
      {"id":"awareness_test","text":"Пройти испытание на внимательность, избегая столкновений","type":"interact","target":"bike_awareness","count":1,"optional":false},
      {"id":"return_bike","text":"Вернуть велосипед и зафиксировать заметки о мобильности","type":"interact","target":"return_fiets","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":600,"money":-15,"reputation":{"mobility":10},"items":[],"unlocks":{"achievements":["dutch_cyclist"],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-amsterdam-2020-2029-bicycle-city",
        "title": "Амстердам 2020-2029 — Город велосипедов",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "amsterdam", "quest"],
        "topics": ["timeline-author", "urban-mobility"],
        "related_systems": ["narrative-service", "transport-system"],
        "related_documents": [
          {"id": "canon-region-europe-amsterdam-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-006-bicycle-city.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о велосипедной культуре Амстердама оставался в Markdown и не раскрывал системную роль транспорта.",
        "goal": "Описать поездку на велосипеде в YAML, подчеркнув инфраструктуру и ощущение города для игроков.",
        "essence": "Игрок арендует fiets и учится перемещаться по красным велодорожкам среди тысяч велосипедистов.",
        "key_points": [
          "Около 880 000 велосипедов в городе и плотная сеть велодорожек.",
          "Велостоянки у вокзала демонстрируют приоритет велосипеда как транспорта №1.",
          "Квест усиливает ветку городской мобильности и slice-of-life сцен."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Амстердам считается мировой столицей велосипедов, где жители всех возрастов используют fiets ежедневно.\\nКвест знакомит с городской инфраструктурой и культурой приоритета немоторизованного транспорта.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Арендовать классический голландский велосипед и изучить правила движения.\\n2. Проехать по красным велодорожкам, соблюдая приоритет и плотный трафик.\\n3. Посетить многоуровневую велостоянку у центрального вокзала и оценить масштаб хранения.\\n4. Пройти испытание на внимательность, избегая столкновений с туристами и общественным транспортом.\\n5. Вернуть велосипед и зафиксировать заметки о городской мобильности.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечаются красные велодорожки, трёхуровневые парковки и терминология вроде fiets.\\nПодчеркивается статистика велосипедов, приоритет на перекрёстках и культура повседневной езды.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 600 XP.\\nВалюта: −15.\\nМобильность: +10.\\nАчивка: «Голландский велосипедист».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Транспортные, социальные и навигационные механики показывают городскую жизнь Амстердама.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 55,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Город велосипедов» в YAML и описание инфраструктуры амстердамского транспорта."}
      ],
      "validation": {"checksum": "", "schema_version": "1.0"}
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







