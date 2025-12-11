-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-007-frites-origin.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-frites-origin
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
    'canon-quest-brussels-2020-2029-frites-origin',
    'Брюссель 2020-2029 — Происхождение фрит',
    'Игрок пробует хрустящие frites и узнаёт, почему бельгийцы спорят за их происхождение.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"find_friterie","text":"Найти friterie и изучить меню соусов","type":"interact","target":"friterie_menu","count":1,"optional":false},
      {"id":"watch_double_fry","text":"Наблюдать процесс двойной обжарки","type":"interact","target":"double_fry","count":1,"optional":false},
      {"id":"taste_sauces","text":"Попробовать популярные соусы и оценить вкус","type":"interact","target":"frites_sauces","count":1,"optional":false},
      {"id":"learn_history","text":"Узнать историю происхождения и спор с Францией","type":"analysis","target":"frites_history","count":1,"optional":false},
      {"id":"value_identity","text":"Завершить квест осознанием культурной ценности frites","type":"analysis","target":"frites_identity","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":500,"money":-4,"reputation":{"culture":10},"items":[],"unlocks":{"achievements":["frites_master"],"buffs":[{"id":"hp_buff","value":"+15% HP","duration_hours":4}]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-frites-origin",
        "title": "Брюссель 2020-2029 — Происхождение фрит",
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-007-frites-origin.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест об истории бельгийского картофеля фри не был интегрирован в YAML и не подчёркивал национальную гордость.",
        "goal": "Описать уличную дегустацию frites с акцентом на двойную обжарку и ассортимент соусов.",
        "essence": "Игрок пробует хрустящие frites из бельгийского киоска и узнаёт, почему они не «французские».",
        "key_points": [
          "Двойная обжарка создаёт характерную текстуру.",
          "Важность соусов (Andalouse, Samurai, Americaine) и подачи в бумажном конусе.",
          "Квест подчёркивает спор за происхождение и национальную идентичность."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Бельгийцы считают себя изобретателями frites, а киоски-friteries стали гастрономическим символом страны.\\nКвест объясняет технологию и культурную значимость блюда.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Найти friterie и изучить меню соусов.\\n2. Заказать порцию frites и наблюдать процесс двойной обжарки.\\n3. Попробовать популярные соусы и оценить вкус.\\n4. Узнать историю происхождения и спор с Францией.\\n5. Завершить квест осознанием культурной ценности frites.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечается происхождение блюда, наличие более 20 соусов и повсеместность киосков по Брюсселю.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 500 XP.\\nВалюта: −4.\\nБафф: +15% HP на 4 часа.\\nАчивка: «Фрит мастер».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Кулинарные и уличные механики демонстрируют влияние гастрономии на социальную идентичность.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Происхождение фрит» в YAML и описание национальной гастрономии."}
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









