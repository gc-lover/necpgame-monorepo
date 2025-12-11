-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-004-belgian-beer.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:20:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-belgian-beer
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
    'canon-quest-brussels-2020-2029-belgian-beer',
    'Брюссель 2020-2029 — Бельгийское пиво',
    'Игрок исследует Delirium Café и другие точки Брюсселя, пробуя 1500+ сортов и узнавая монастырские корни пивоварения.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"visit_delirium","text":"Посетить Delirium Café и изучить меню из тысяч сортов","type":"interact","target":"delirium_cafe","count":1,"optional":false},
      {"id":"taste_styles","text":"Продегустировать Trappist, Lambic, Dubbel/Tripel/Quadrupel","type":"interact","target":"beer_styles","count":1,"optional":false},
      {"id":"note_glasses","text":"Обратить внимание на уникальные бокалы под каждый сорт","type":"analysis","target":"beer_glasses","count":1,"optional":false},
      {"id":"record_strength","text":"Зафиксировать крепости и роль пивных традиций","type":"analysis","target":"beer_strength","count":1,"optional":false},
      {"id":"culture_identity","text":"Понять, как пиво формирует культурную идентичность Бельгии","type":"analysis","target":"beer_identity","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1500,"money":-40,"reputation":{"culture":15,"charisma":10},"items":[],"unlocks":{"achievements":["beer_connoisseur"],"buffs":[{"id":"charisma_boost","value":"+25% Charisma","duration_hours":6}]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-belgian-beer",
        "title": "Брюссель 2020-2029 — Бельгийское пиво",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:25:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "culinary-tour"],
        "related_systems": ["narrative-service", "economy-service"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-004-belgian-beer.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест дегустации бельгийского пива не был структурирован и не описывал разнообразие сортов и традиции бокалов.",
        "goal": "Сформировать YAML-описание дегустационного маршрута, подчёркивая Trappist, Lambic и культуру уникальных бокалов.",
        "essence": "Игрок исследует Delirium Café и другие точки Брюсселя, пробуя 1500+ сортов и познавая монастырские корни пивоварения.",
        "key_points": [
          "Делириум предлагает меню на 2000 позиций, а бельгийские пивовары поддерживают уникальные бокалы.",
          "Trappist, Lambic, Dubbel, Tripel и Quadrupel отражают разнообразие стилей и крепостей до 12%.",
          "Квест укрепляет кулинарную линию и представляет пиво как культурную традицию."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Бельгия известна более чем 1500 сортами пива, включая Trappist, Lambic и современную крафтовую сцену.\\nКвест демонстрирует культурную значимость пивоварения и его влияние на экономику.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Посетить Delirium Café и изучить многостраничное меню сортов.\\n2. Продегустировать Trappist, Lambic, Dubbel/Tripel/Quadrupel, обсуждая вкусовые особенности.\\n3. Обратить внимание на уникальные бокалы под каждый сорт.\\n4. Зафиксировать роль пивных традиций и крепость напитков.\\n5. Понять, как пиво формирует культурную идентичность Бельгии.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечаются монастырские традиции Trappist, спонтанная ферментация Lambic и требования к подаче напитка.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1500 XP.\\nВалюта: −40.\\nБафф: +25% Charisma на 6 часов.\\nАчивка: «Пивной знаток».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Кулинарные, дегустационные и культурные механики создают сценарии общения и изучения традиций.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Бельгийское пиво» в YAML и описание пивных традиций."}
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









