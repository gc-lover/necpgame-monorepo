-- Issue: #55
-- Import quest from: europe\amsterdam\2020-2029\quest-010-dutch-tolerance.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:00:00.000000

BEGIN;

-- Quest: canon-quest-amsterdam-2020-2029-dutch-tolerance
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
    'canon-quest-amsterdam-2020-2029-dutch-tolerance',
    'Амстердам 2020-2029 — Голландская толерантность',
    'Игрок исследует прогрессивные законы и решает, поддерживать ли прагматичную модель свободы.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"study_gedogen","text":"Изучить философию gedogen и исторические причины толерантности","type":"analysis","target":"gedogen_concept","count":1,"optional":false},
      {"id":"analyze_laws","text":"Проанализировать законы о марихуане, проституции, однополых браках и эвтаназии","type":"analysis","target":"dutch_laws","count":1,"optional":false},
      {"id":"talk_experts","text":"Побеседовать с NPC-экспертами и жителями о плюсах и рисках модели","type":"dialogue","target":"tolerance_dialogues","count":1,"optional":false},
      {"id":"visit_locations","text":"Посетить кофешоп, район красных фонарей и общественный центр","type":"travel","target":"tolerance_locations","count":1,"optional":false},
      {"id":"take_position","text":"Принять позицию — поддержать, критиковать или предложить баланс","type":"choice","target":"tolerance_choice","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":4000,"money":0,"reputation":{"philosophy":25},"items":[],"unlocks":{"achievements":["tolerance_philosopher"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-amsterdam-2020-2029-dutch-tolerance",
        "title": "Амстердам 2020-2029 — Голландская толерантность",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "amsterdam", "quest"],
        "topics": ["timeline-author", "social-policy"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-amsterdam-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-010-dutch-tolerance.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о голландской толерантности оставался в Markdown и не описывал политические выборы игрока.",
        "goal": "Сформировать YAML-документ, показывающий политику gedogen и ключевые реформы Нидерландов.",
        "essence": "Игрок исследует прогрессивные законы и решает, поддерживать ли прагматичную модель свободы.",
        "key_points": [
          "Gedogen отражает практику терпимости и регулирования через легализацию.",
          "Легализация марихуаны, проституции, эвтаназии и однополых браков демонстрирует прогрессивность общества.",
          "Квест поддерживает социально-политическую ветку Амстердама и даёт игроку выбор."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Нидерланды известны политикой терпимости, основанной на прагматизме и контролируемой свободе.\\nКвест объясняет термин gedogen и то, как город сочетает либеральные практики с государственным управлением.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Изучить философию gedogen и исторические причины толерантности.\\n2. Проанализировать законы о марихуане, проституции, однополых браках и эвтаназии.\\n3. Побеседовать с NPC-экспертами и жителями, обсуждая плюсы и риски модели.\\n4. Посетить ключевые локации (coffee shop, район красных фонарей, общественный центр).\\n5. Принять позицию — поддержать, критиковать или предложить баланс.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Указываются даты принятия законов, значение слова gedogen и роль Амстердама как витрины для туристов и политиков.\\nПодчёркивается прагматичный подход к контролю и социальным контрактам.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 4000 XP.\\nВалюта: 0.\\nФилософия: +25.\\nАчивка: «Философ толерантности».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Социальные, политические и выборные механики позволяют игроку формировать отношение к модели свободы.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Голландская толерантность» в YAML и раскрытие политики gedogen."}
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









