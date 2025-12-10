-- Issue: #52
-- Import quest from: content\quests\side\SQ-2078-003-craft-fair.yaml
-- Version: 1.0.0
-- Generated: 2025-12-08T19:58:00.000000

BEGIN;

-- Quest: content-quest-sq-2078-003-craft-fair
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
    'content-quest-sq-2078-003-craft-fair',
    'SQ-2078-003 — Ярмарка ремесел',
    'Игрок организует ремесленную ярмарку, контролируя качество, маркетинг и защиту от ботов.',
    'side',
    40,
    44,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[]'::jsonb,
    '{"experience":null,"money":null,"reputation":{},"items":[],"unlocks":{"achievements":[],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "content-quest-sq-2078-003-craft-fair",
        "title": "SQ-2078-003 — Ярмарка ремесел",
        "document_type": "content",
        "category": "quest-side",
        "status": "approved",
        "version": "1.0.0",
        "last_updated": "2025-11-06T00:00:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "2025-11-06T00:00:00Z",
        "owners": [{"role": "content_lead", "contact": "content@necp.game"}],
        "tags": ["side-quest", "craft", "trade"],
        "topics": ["economy", "community"],
        "related_systems": ["gameplay-service", "quest-service", "character-service", "economy-service", "crafting-service", "social-service"],
        "related_documents": [
          {"id": "content-quest-sq-2078-002-fair-manipulation", "relation": "parallels"}
        ],
        "source": "shared/docs/knowledge/content/quests/side/SQ-2078-003-craft-fair.md",
        "visibility": "internal",
        "audience": ["content", "narrative", "economy"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест «Ярмарка ремесел» хранился в Markdown и не был доступен инструментам согласования.",
        "goal": "Стандартизировать описание ремесленной ярмарки в YAML согласно шаблону знаний.",
        "essence": "Игрок организует ремесленную ярмарку, контролируя качество, маркетинг и защиту от ботов.",
        "key_points": [
          "20 узлов охватывают отбор экспонентов, стандартизацию, логистику и маркетинговые активности.",
          "Репутация: +12 к CraftGuilds и +8 к Leagues за успешную организацию.",
          "Награды: лицензии, рыночные баффы и 600–1600 едди."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "quest_brief",
            "title": "Концепция квеста",
            "body": "Игроки Trader/Media при поддержке Techie организуют ярмарку ремесленных изделий. Нужно обеспечить прозрачный отбор,\\nвыдержать стандарты качества и удержать интерес аудитории за счёт медиа-кампаний.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest_flow",
            "title": "Ключевые этапы",
            "body": "Диалоговое дерево на 20 узлов включает подготовку площадки, проверку продукции, маркетинговые спринты, пресечение\\nботовых атак и выдачу лицензий участникам. Каждый этап дополняет ремесленную экосистему города.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards_reputation",
            "title": "Репутация и награды",
            "body": "Успешная ярмарка приносит +12 к CraftGuilds и +8 к Leagues. Игрок получает лицензии, баффы для рынков и 600–1600 едди,\\nчто усиливает связь с ремесленными сообществами.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 52,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "1.0.0", "date": "2025-11-06", "author": "concept_director", "changes": "Перенос квеста «Ярмарка ремесел» в YAML и описание ключевых этапов."}
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

