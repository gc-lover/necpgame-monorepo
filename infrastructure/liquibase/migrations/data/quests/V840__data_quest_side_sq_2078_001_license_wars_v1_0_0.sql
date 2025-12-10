-- Issue: #52
-- Import quest from: content\quests\side\SQ-2078-001-license-wars.yaml
-- Version: 1.0.0
-- Generated: 2025-12-08T19:40:00.000000

BEGIN;

-- Quest: content-quest-sq-2078-001-license-wars
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
    'content-quest-sq-2078-001-license-wars',
    'SQ-2078-001 — Войны лицензий',
    'Игрок управляет Trader/Fixer связкой, проводит 20-узловой диалог и балансирует интересы гильдий и корпораций.',
    'side',
    40,
    44,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[]'::jsonb,
    '{"experience":null,"money":null,"reputation":{},"items":[],"unlocks":{"achievements":[],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "content-quest-sq-2078-001-license-wars",
        "title": "SQ-2078-001 — Войны лицензий",
        "document_type": "content",
        "category": "quest-side",
        "status": "approved",
        "version": "1.0.0",
        "last_updated": "2025-11-06T00:00:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "2025-11-06T00:00:00Z",
        "owners": [{"role": "content_lead", "contact": "content@necp.game"}],
        "tags": ["side-quest", "trade", "legal"],
        "topics": ["economy", "diplomacy"],
        "related_systems": ["gameplay-service", "quest-service", "character-service", "economy-service", "faction-service"],
        "related_documents": [
          {"id": "canon-detailed-events-2078-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/content/quests/side/SQ-2078-001-license-wars.md",
        "visibility": "internal",
        "audience": ["content", "narrative", "economy"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест «Войны лицензий» оставался в Markdown и не попадал в процессы автоматизации.",
        "goal": "Формализовать side quest о борьбе за экспортные лицензии в структурированном YAML.",
        "essence": "Игрок управляет Trader/Fixer связкой, проводит 20-узловой диалог и балансирует интересы гильдий и корпораций.",
        "key_points": [
          "Контент охватывает экономические и юридические проверки с DC 20–22 и групповым финалом.",
          "Репутационные изменения: +12 к лигам и ремесленным гильдиям, до -10 к корпорациям.",
          "Награды включают лицензионные баффы и эдди в диапазоне 700–1800."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "quest_brief",
            "title": "Концепция квеста",
            "body": "Битва за экспортные лицензии разворачивается между гильдиями и корп-альянсами. Игроки уровня 40–44 с классами Trader/Fixer\\nведут переговоры, защищают заявку и отражают саботаж конкурентов, используя поддержку Legal и Media.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "dialogue_flow",
            "title": "Диалоговое дерево и проверки",
            "body": "Диалог содержит 20 узлов: начиная с объявления тендера и подготовки заявки, заканчивая подведением итогов и запуском экспорта.\\nВстречаются проверки Trader, Legal, Tech, Media, Social, Investigation, Stealth и Hacking с DC 20–22, а финал требует группового\\nпорога (threshold 3) для успешного подписания контрактов.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards_reputation",
            "title": "Репутация и награды",
            "body": "Успешное прохождение даёт +12 к репутации Лиг и Craft Guilds, одновременно снижая отношения с корпорациями до -10 в процессе.\\nДобыча: лицензионные бонусы, рыночные баффы и от 700 до 1800 едди, что усиливает экономическое влияние игрока.",
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
        {"version": "1.0.0", "date": "2025-11-06", "author": "concept_director", "changes": "Перенос квеста «Войны лицензий» в формат YAML и описание 20-узловой структуры."}
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

