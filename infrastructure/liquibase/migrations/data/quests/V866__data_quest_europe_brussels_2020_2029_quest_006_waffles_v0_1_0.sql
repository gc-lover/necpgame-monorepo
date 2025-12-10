-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-006-waffles.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-waffles
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
    'canon-quest-brussels-2020-2029-waffles',
    'Брюссель 2020-2029 — Бельгийские вафли',
    'Игрок пробует свежие вафли, сравнивая лёгкие Brussels и карамелизированные Liège стили.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"find_stall","text":"Найти уличный киоск и изучить меню","type":"interact","target":"waffle_stall","count":1,"optional":false},
      {"id":"choose_style","text":"Выбрать стиль вафель: Brussels или Liège","type":"choice","target":"waffle_style","count":1,"optional":false},
      {"id":"add_toppings","text":"Добавить топпинги при необходимости","type":"interact","target":"waffle_toppings","count":1,"optional":false},
      {"id":"compare_texture","text":"Сравнить текстуру и вкус, отметить отличие от американских","type":"analysis","target":"waffle_compare","count":1,"optional":false},
      {"id":"note_freshness","text":"Сделать вывод о значении свежести и уличной подачи","type":"analysis","target":"waffle_freshness","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":600,"money":-5,"reputation":{"culinary":10},"items":[],"unlocks":{"achievements":["waffle_master"],"buffs":[{"id":"energy_boost","value":"+15% Energy","duration_hours":4}]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-waffles",
        "title": "Брюссель 2020-2029 — Бельгийские вафли",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "street-food"],
        "related_systems": ["narrative-service", "economy-service"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-006-waffles.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о бельгийских вафлях не был структурирован и не отражал различия между Brussels и Liège стилями.",
        "goal": "Перевести маршрут дегустации вафель в YAML, подчёркивая выбор стиля и уличную культуру.",
        "essence": "Игрок пробует свежие вафли с прилавков Брюсселя, сравнивая лёгкие Brussels и карамелизированные Liège варианты.",
        "key_points": [
          "Уличные киоски предлагают два стилистически разных вида вафель.",
          "Топпинги и карамелизация подают квест как кулинарное исследование.",
          "Подчёркивается отличие от американских версий и важность свежести."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Бельгийские вафли стали глобальным символом гастрономии, однако их оригинальные разновидности Brussels и Liège значительно отличаются от привычных американских.\\nКвест демонстрирует уличную культуру и выбор между лёгким и карамелизированным стилем.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Найти уличный киоск и изучить меню.\\n2. Выбрать стиль вафель: лёгкие Brussels или карамельные Liège.\\n3. Добавить топпинги (клубника, шоколад, сливки) при необходимости.\\n4. Сравнить текстуру и вкус, фиксируя отличие от американских аналогов.\\n5. Сделать вывод о значении свежести и уличной подачи.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечаются особенности двух стилей, традиция уличных киосков и контраст с международными адаптациями.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 600 XP.\\nВалюта: −5.\\nБафф: +15% Energy на 4 часа.\\nАчивка: «Вафельный мастер».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Кулинарные и выборные механики создают сценарий сравнения и культурного обмена.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Бельгийские вафли» в YAML и описание кулинарных различий."}
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








