-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-010-comic-strip-capital.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-comic-strip-capital
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
    'canon-quest-brussels-2020-2029-comic-strip-capital',
    'Брюссель 2020-2029 — Столица комиксов',
    'Игрок исследует муралы и музей комиксов, осознавая комиксы как часть идентичности Брюсселя.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"walk_comic_route","text":"Пройти Comic Strip Route и найти муралы с Тинтином, Смурфами и др.","type":"travel","target":"comic_route","count":1,"optional":false},
      {"id":"visit_bisc","text":"Посетить Belgian Comic Strip Center и изучить экспозиции","type":"interact","target":"comic_strip_center","count":1,"optional":false},
      {"id":"buy_comic","text":"Купить комикс или сувенир, связанный с Hergé/Peyo","type":"purchase","target":"comic_merch","count":1,"optional":false},
      {"id":"reflect_culture","text":"Осознать влияние комиксов на культурную политику и туризм","type":"analysis","target":"comic_culture","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1500,"money":-25,"reputation":{"culture":20},"items":[],"unlocks":{"achievements":["comic_reader"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-comic-strip-capital",
        "title": "Брюссель 2020-2029 — Столица комиксов",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "cultural-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-010-comic-strip-capital.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о комикс-культуре Брюсселя находился в Markdown и не описывал маршрут по муралам и музеям.",
        "goal": "Перевести комикс-тур в YAML, выделив роль Тинтина, Смурфов и Belgian Comic Strip Center.",
        "essence": "Игрок исследует городские муралы и музей, осознавая, что комиксы — часть национальной идентичности Бельгии.",
        "key_points": [
          "Комикс-муралы (50+) формируют маршрут с Тинтином, Смурфами и Lucky Luke.",
          "Belgian Comic Strip Center представляет историю и экспозиции.",
          "Квест усиливает культурную линию и знание местного искусства."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Брюссель гордится своими комиксами, породив героя Тинтина и Смурфов, что отражено в городском стрит-арте и музеях.\\nКвест подчёркивает позиционирование города как столицы комиксов.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Пройти Comic Strip Route и обнаружить муралы с Тинтином, Смурфами и другими героями.\\n2. Посетить Belgian Comic Strip Center и изучить экспозиции.\\n3. Купить комикс или сувенир, связанный с наследием Hergé и Peyo.\\n4. Осознать, как комиксы влияют на культурную политику и туризм Брюсселя.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Указываются 50+ муралов, история издателей и значимость комиксов как национальной гордости.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1500 XP.\\nВалюта: −25.\\nКультура: +20.\\nАчивка: «Читатель комиксов».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Культурные, туристические и образовательные механики демонстрируют влияние комиксов на городскую среду.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 56,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Столица комиксов» в YAML и описание комикс-маршрута."}
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













