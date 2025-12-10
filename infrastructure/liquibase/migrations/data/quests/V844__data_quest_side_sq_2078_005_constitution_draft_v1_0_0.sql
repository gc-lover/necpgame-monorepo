-- Issue: #52
-- Import quest from: content\quests\side\SQ-2078-005-constitution-draft.yaml
-- Version: 1.0.0
-- Generated: 2025-12-08T20:06:00.000000

BEGIN;

-- Quest: content-quest-sq-2078-005-constitution-draft
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
    'content-quest-sq-2078-005-constitution-draft',
    'SQ-2078-005 — Черновик конституции',
    'Игроки создают и защищают новую конституцию купола, балансируя интересы лиг, граждан и корпораций.',
    'side',
    42,
    46,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[]'::jsonb,
    '{"experience":null,"money":null,"reputation":{},"items":[],"unlocks":{"achievements":[],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "content-quest-sq-2078-005-constitution-draft",
        "title": "SQ-2078-005 — Черновик конституции",
        "document_type": "content",
        "category": "quest-side",
        "status": "approved",
        "version": "1.0.0",
        "last_updated": "2025-11-06T00:00:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "2025-11-06T00:00:00Z",
        "owners": [{"role": "content_lead", "contact": "content@necp.game"}],
        "tags": ["side-quest", "politics", "legal"],
        "topics": ["governance", "diplomacy"],
        "related_systems": ["gameplay-service", "quest-service", "character-service", "governance-system", "diplomacy-service", "social-service"],
        "related_documents": [
          {"id": "canon-lore-timeline-author-index", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/content/quests/side/SQ-2078-005-constitution-draft.md",
        "visibility": "internal",
        "audience": ["content", "narrative", "politics"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Сценарий «Черновик конституции» был в Markdown и не использовался в пайплайне знаний.",
        "goal": "Задокументировать политико-правовой квест в соответствии с шаблоном знаний.",
        "essence": "Игроки создают и защищают новую конституцию купола, балансируя интересы лиг, граждан и корпораций.",
        "key_points": [
          "24 узла: созыв конвента, сбор мнений, юр-драфт, слушания, давление корпораций и групповой вотум (threshold 4).",
          "Репутация: +20 к Leagues и +15 к Citizens при демократическом исходе.",
          "Награды: наследуемые права конституции и 800–2000 едди."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "quest_brief",
            "title": "Концепция квеста",
            "body": "Классы Politician/Legal/Media работают над основным законом для нового купола. Требуется учитывать голоса граждан,\\nтребования лиг и давление корпораций, сохраняя легитимность процесса.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest_flow",
            "title": "Узлы и проверки",
            "body": "Квест охватывает 24 узла: от созыва конвента и юридического драфта до общественных слушаний и медиа-кампании. Финал требует\\nгруппового порога 4 при голосовании и успешной защиты от апелляций.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards_reputation",
            "title": "Репутация и награды",
            "body": "Демократический путь приносит +20 к репутации Leagues и +15 к Citizens, а также даёт трансферное наследие «Constitution rights»\\nи 800–2000 едди.",
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
        {"version": "1.0.0", "date": "2025-11-06", "author": "concept_director", "changes": "Конвертирован квест «Черновик конституции» в YAML и описаны политические узлы."}
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

