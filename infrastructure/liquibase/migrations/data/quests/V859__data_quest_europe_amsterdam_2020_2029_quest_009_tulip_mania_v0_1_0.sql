-- Issue: #55
-- Import quest from: europe\amsterdam\2020-2029\quest-009-tulip-mania.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:00:00.000000

BEGIN;

-- Quest: canon-quest-amsterdam-2020-2029-tulip-mania
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
    'canon-quest-amsterdam-2020-2029-tulip-mania',
    'Амстердам 2020-2029 — Тюльпаномания',
    'Игрок изучает первый финансовый пузырь мира и наблюдает, как тюльпаны остаются символом Нидерландов.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"visit_museum","text":"Посетить музей, посвящённый тюльпаномании, и изучить лоты аукционов XVII века","type":"interact","target":"tulip_museum","count":1,"optional":false},
      {"id":"trace_prices","text":"Проследить рост цен на редкие луковицы до стоимости домов","type":"analysis","target":"tulip_price_history","count":1,"optional":false},
      {"id":"simulate_bubble","text":"Смоделировать спекуляцию и крах, анализируя документы участников рынка","type":"simulation","target":"tulip_market_sim","count":1,"optional":false},
      {"id":"visit_keukenhof","text":"Отправиться весной в парк Keukenhof и увидеть поля тюльпанов","type":"travel","target":"keukenhof_trip","count":1,"optional":false},
      {"id":"lessons_learned","text":"Сформулировать уроки о рисках пузырей и влиянии на экономику","type":"analysis","target":"bubble_lessons","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":3000,"money":0,"reputation":{"economy":20},"items":[],"unlocks":{"achievements":["bubble_expert"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-amsterdam-2020-2029-tulip-mania",
        "title": "Амстердам 2020-2029 — Тюльпаномания",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "amsterdam", "quest"],
        "topics": ["timeline-author", "economic-history"],
        "related_systems": ["narrative-service", "economy-service"],
        "related_documents": [
          {"id": "canon-region-europe-amsterdam-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-009-tulip-mania.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест об истории тюльпаномании находился в Markdown и не раскрывал экономические механики пузыря 1637 года.",
        "goal": "Описать в YAML образовательный маршрут, связывающий музейные экспозиции и современный парк Keukenhof.",
        "essence": "Игрок изучает первый финансовый пузырь мира и наблюдает, как тюльпаны остаются символом Нидерландов.",
        "key_points": [
          "Тюльпаны в XVII веке стоили дороже домов и обрушили рынки после краха.",
          "Маршрут включает музейные архивы, документы спекуляций и поездку в Keukenhof.",
          "Квест подкрепляет экономическую ветку Амстердама и обучает рискам пузырей."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Тюльпаномания 1630-х считается первым документированным финансовым пузырём.\\nКвест соединяет исторические архивы и современное цветочное наследие страны.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Посетить музей, посвящённый тюльпаномании, и изучить лоты аукционов XVII века.\\n2. Проследить, как цены на редкие луковицы выросли до стоимости городских домов.\\n3. Смоделировать спекуляцию и крах, анализируя документы участников рынка.\\n4. Отправиться весной в парк Keukenhof и увидеть поля из семи миллионов тюльпанов.\\n5. Сформулировать уроки о рисках пузырей и влиянии на современную экономику.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Подчёркивается символика тюльпанов, значимость Keukenhof и роль торговли в развитии Голландии.\\nОтмечаются даты кризиса 1637 года и последствия для купцов.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 3000 XP.\\nВалюта: 0.\\nЭкономика: +20.\\nАчивка: «Знаток пузырей».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Экономические и образовательные механики демонстрируют спекулятивное поведение и его последствия.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Тюльпаномания» в YAML и описание экономического пузыря 1637 года."}
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















