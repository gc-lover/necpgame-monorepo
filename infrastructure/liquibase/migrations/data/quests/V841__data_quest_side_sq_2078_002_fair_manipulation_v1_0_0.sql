-- Issue: #52
-- Import quest from: content\quests\side\SQ-2078-002-fair-manipulation.yaml
-- Version: 1.0.0
-- Generated: 2025-12-08T19:48:00.000000

BEGIN;

-- Quest: content-quest-sq-2078-002-fair-manipulation
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
    'content-quest-sq-2078-002-fair-manipulation',
    'SQ-2078-002 — Манипуляции на ярмарках',
    'Игроки расследуют ботов и ценовые подставы на ярмарках, используя Netrunner/Trader и медиа-поддержку.',
    'side',
    40,
    44,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[]'::jsonb,
    '{"experience":null,"money":null,"reputation":{},"items":[],"unlocks":{"achievements":[],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "content-quest-sq-2078-002-fair-manipulation",
        "title": "SQ-2078-002 — Манипуляции на ярмарках",
        "document_type": "content",
        "category": "quest-side",
        "status": "approved",
        "version": "1.0.0",
        "last_updated": "2025-11-06T00:00:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "2025-11-06T00:00:00Z",
        "owners": [{"role": "content_lead", "contact": "content@necp.game"}],
        "tags": ["side-quest", "hacking", "media"],
        "topics": ["economy", "cybersecurity"],
        "related_systems": ["gameplay-service", "quest-service", "character-service", "economy-service", "hacking-service", "broadcast-service"],
        "related_documents": [
          {"id": "content-quest-sq-2078-001-license-wars", "relation": "complements"}
        ],
        "source": "shared/docs/knowledge/content/quests/side/SQ-2078-002-fair-manipulation.md",
        "visibility": "internal",
        "audience": ["content", "narrative", "economy"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест «Манипуляции на ярмарках» находился вне структурированного формата.",
        "goal": "Задокументировать сценарий анти-манипуляционного расследования в YAML для автоматизации.",
        "essence": "Игроки расследуют ботов и ценовые подставы на ярмарках, используя Netrunner/Trader и медиа-поддержку.",
        "key_points": [
          "20 узлов проверок с фокусом на Hacking, Trader, Legal и Media (DC 20–22).",
          "Репутация: +12 к лигам и ремесленным гильдиям, до -10 к корпорациям.",
          "Награды: лицензии, рыночные баффы и 700–1800 едди."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "quest_brief",
            "title": "Концепция квеста",
            "body": "Параметрические ярмарки подверглись манипуляциям со стороны корпоративных ботов. Игроки уровня 40–44 раскрывают схемы,\\nведут переговоры и восстанавливают справедливые цены, используя Netrunner/Trader связку и поддержку Media.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "dialogue_flow",
            "title": "Диалог и проверки",
            "body": "Диалоговое дерево содержит 20 шагов: от жалобы гильдий и трассировки ботов до юридических апелляций и медиа-прессинга.\\nФинал требует группового порога 3 для успешных переговоров и санкций против нарушителей.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards_reputation",
            "title": "Репутация и награды",
            "body": "Успех приносит +12 к Leagues и CraftGuilds, снижает отношения с корпорациями до -10 и выдаёт экономические бонусы: лицензии,\\nрыночные усиления и 700–1800 едди.",
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
        {"version": "1.0.0", "date": "2025-11-06", "author": "concept_director", "changes": "Перенос квеста «Манипуляции на ярмарках» в YAML и описание узлов расследования."}
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

