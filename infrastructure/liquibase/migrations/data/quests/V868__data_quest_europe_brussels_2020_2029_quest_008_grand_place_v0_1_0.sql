-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-008-grand-place.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-grand-place
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
    'canon-quest-brussels-2020-2029-grand-place',
    'Брюссель 2020-2029 — Гранд Плас',
    'Игрок исследует Grand-Place, восхищаясь золотыми фасадами, UNESCO-статусом и цветочным ковром.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"enter_square","text":"Войти на площадь и осмотреть золотые фасады","type":"interact","target":"grand_place_entry","count":1,"optional":false},
      {"id":"visit_hotel_de_ville","text":"Посетить Hôtel de Ville и музейные экспозиции","type":"interact","target":"hotel_de_ville","count":1,"optional":false},
      {"id":"see_flower_carpet","text":"Если сезон, увидеть цветочный ковёр из 750 000 цветов","type":"event","target":"flower_carpet","count":1,"optional":false},
      {"id":"view_lighting","text":"Вернуться вечером для наблюдения подсветки фасадов","type":"interact","target":"grand_place_lighting","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1200,"money":0,"reputation":{"aesthetics":20},"items":[],"unlocks":{"achievements":["grand_place_master"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-grand-place",
        "title": "Брюссель 2020-2029 — Гранд Плас",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "architectural-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-008-grand-place.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о Grand-Place не был структурирован и не выделял UNESCO-статус и цветочный ковёр.",
        "goal": "Перевести посещение площади в YAML, описывая архитектуру гильдейских домов и события.",
        "essence": "Игрок исследует Grand-Place, восхищаясь золотыми фасадами и световым шоу песчаной площади.",
        "key_points": [
          "UNESCO-объект с признанием Виктора Гюго как «красивейшей площади».",
          "Раз в два года площадь покрывается ковром из 750 тысяч цветов.",
          "Квест усиливает архитектурную линию и культурные события Брюсселя."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Grand-Place — сердце Брюсселя и пример богатых гильдейских домов XVII века.\\nКвест подчёркивает историческую ценность, UNESCO-статус и ночную подсветку.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Войти на площадь и осмотреть золотые фасады.\\n2. Посетить Hôtel de Ville и музейные экспозиции в гильдейских домах.\\n3. Если игра происходит в августе чётного года, увидеть цветочный ковёр из 750 000 цветов.\\n4. Вернуться вечером для наблюдения подсветки фасадов.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечается UNESCO-статус, исторические здания и роль площади в городской идентичности.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1200 XP.\\nВалюта: 0.\\nЭстетика: +20.\\nАчивка: «Хозяин Grand-Place».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Туристические, эстетические и событийные механики демонстрируют архитектурное великолепие площади.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Гранд Плас» в YAML и описание архитектурных особенностей."}
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















