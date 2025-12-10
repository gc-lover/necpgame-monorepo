-- Issue: #54
-- Import quest from: europe\berlin\2030-2039\quest-014-pergamon-altar.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T20:44:00.000000

BEGIN;

-- Quest: canon-quest-berlin-2030-2039-pergamon-altar
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
    'canon-quest-berlin-2030-2039-pergamon-altar',
    'Берлин 2030-2039 — Пергамский алтарь',
    'Игрок поднимается на Пергамский алтарь, запускает AR-реконструкцию и переживает ночную симуляцию богов.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"climb_altar","text":"Подняться по мраморным ступеням и активировать гид по гигантомахии","type":"interact","target":"pergamon_steps","count":1,"optional":false},
      {"id":"scan_scenes","text":"Сканировать сцены битвы богов и гигантов для коллекции «Античные легенды»","type":"interact","target":"giantomachy_scan","count":1,"optional":false},
      {"id":"run_ar","text":"Запустить AR-реконструкцию Пергама II века до н.э.","type":"interact","target":"pergamon_ar","count":1,"optional":false},
      {"id":"night_event","text":"Пережить ночную симуляцию богов и ответить на вопросы Зевса и Афины","type":"choice","target":"pergamon_night_event","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":2500,"money":0,"reputation":{"culture":15},"items":[{"id":"voice_of_pergamon","type":"record"}],"unlocks":{"achievements":[],"flags":["pergamon_night_tours"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-berlin-2030-2039-pergamon-altar",
        "title": "Берлин 2030-2039 — Пергамский алтарь",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T00:00:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "narrative_team", "contact": "narrative@necp.game"}],
        "tags": ["berlin", "museum", "mystic"],
        "topics": ["cultural-tour", "ar-experience"],
        "related_systems": ["narrative-service", "world-service"],
        "related_documents": [
          {"id": "canon-quest-berlin-2030-2039-museum-island", "relation": "complements"},
          {"id": "canon-region-europe-berlin-2020-2093", "relation": "contextualizes"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/berlin/2030-2039/quest-014-pergamon-altar.md",
        "visibility": "internal",
        "audience": ["narrative", "quest-design", "localization"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "narrative_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Посещение Пергамского алтаря не описывало AR-реконструкцию и ночную симуляцию богов.",
        "goal": "Структурировать музейный квест с акцентом на артефакт, фризы гигантомахии и встречу с ИИ-богами.",
        "essence": "Игрок поднимается по мраморным ступеням алтаря, видит древний Пергам через AR и сталкивается с цифровыми богами ночью.",
        "key_points": [
          "Алтарь — главный зал Pergamon Museum с историей вывоза в XIX веке.",
          "AR-переход переносит игрока в Пергам, показывая храм в первозданном виде.",
          "Ночная симуляция гигантомахии запускает мистический сценарий."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "exploration",
            "title": "Исследование алтаря",
            "body": "- Подняться по мраморным ступеням и активировать гид по барельефам гигантомахии.\\n- Сканировать сцены битвы богов и гигантов для коллекции «Античные легенды».\\n- Узнать историю вывоза алтаря в Берлин и споры о реституции.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "ar_sequence",
            "title": "AR-реконструкция",
            "body": "- Прожекторные карты перемещают игрока в Пергам II века до н.э.\\n- Звуковое сопровождение и голос жреца объясняют культ Зевса.\\n- Возможность сравнить оригинал и цифровое восстановление.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "night_event",
            "title": "Ночной ивент ИИ-богов",
            "body": "- После закрытия музея запускается симуляция оживления алтаря.\\n- ИИ-проекции Зевса и Афины задают вопросы игроку о верности и силе.\\n- Успешные ответы дают редкую запись «Voice of Pergamon».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "- 2 500 XP.\\n- +15 к параметру «Культура».\\n- Запись «Voice of Pergamon» и доступ к ночным турам музея.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 54,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml#canon-quest-berlin-2030-2039-pergamon-altar"],
        "blockers": []
      },
      "history": [
        {"version": "0.1.0", "date": "2025-11-12", "author": "narrative_team", "changes": "Конвертирован квест «Пергамский алтарь» в YAML с AR и мистической сценой."}
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

