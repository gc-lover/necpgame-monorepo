-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-005-atomium.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-atomium
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
    'canon-quest-brussels-2020-2029-atomium',
    'Брюссель 2020-2029 — Атомиум',
    'Игрок поднимается внутри увеличенного атома железа, изучая выставки и оптимизм 1950-х.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"explore_exterior","text":"Исследовать внешний вид Atomium и девять сфер","type":"interact","target":"atomium_exterior","count":1,"optional":false},
      {"id":"ride_escalators","text":"Подняться по эскалаторам внутри труб и посетить выставки","type":"travel","target":"atomium_tubes","count":1,"optional":false},
      {"id":"visit_panorama","text":"Добраться до верхней смотровой площадки и оценить панораму","type":"interact","target":"atomium_panorama","count":1,"optional":false},
      {"id":"analyze_context","text":"Разобраться с контекстом атомного оптимизма и влиянием на дизайн","type":"analysis","target":"atomium_context","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1200,"money":-15,"reputation":{"aesthetics":15},"items":[],"unlocks":{"achievements":["atomic_explorer"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-atomium",
        "title": "Брюссель 2020-2029 — Атомиум",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:25:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "architectural-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-005-atomium.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест «Атомиум» не был структурирован в YAML и не раскрывал символику Expo 1958.",
        "goal": "Описать посещение Atomium, подчеркнув футуристический символ атомного века и панораму Брюсселя.",
        "essence": "Игрок поднимается внутри увеличенного атома железа, изучая выставки и оптимизм 1950-х.",
        "key_points": [
          "Atomium построен к Expo 1958, представляет атом железа в 165 млрд раз крупнее.",
          "Девять сфер, эскалаторы в трубах и смотровая площадка раскрывают архитектурный опыт.",
          "Квест усиливает архитектурную линию и исторический контекст послевоенного оптимизма."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Atomium — один из главных символов Брюсселя, отражающий веру в атомную эпоху и прогресс середины XX века.\\nКвест знакомит игроков с историей Expo 1958 и архитектурой на стыке науки и искусства.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Исследовать внешний вид Atomium и девять сфер, соединённых трубами.\\n2. Подняться по эскалаторам внутри труб и посетить выставки в сферах.\\n3. Добраться до верхней смотровой площадки и оценить панораму Брюсселя.\\n4. Разобраться с контекстом атомного оптимизма и его влиянием на дизайн.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Указывается высота 102 метра, масштаб увеличения атома железа и значение Expo 1958 для города.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1200 XP.\\nВалюта: −15.\\nЭстетика: +15.\\nАчивка: «Атомный исследователь».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Туристические, исторические и символические механики подчёркивают футуристическую архитектуру.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Атомиум» в YAML и структурирование архитектурных этапов."}
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















