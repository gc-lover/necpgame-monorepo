-- Issue: #54
-- Import quest from: europe\berlin\2020-2029\quest-004-reichstag-dome.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T20:18:00.000000

BEGIN;

-- Quest: canon-quest-berlin-2020-2029-reichstag-dome
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
    'canon-quest-berlin-2020-2029-reichstag-dome',
    'Берлин 2020-2029 — Купол Рейхстага',
    'Игрок поднимается по спиральной рампе купола Рейхстага, наблюдая пленарный зал и панораму Берлина как символ прозрачной демократии.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"book_visit","text":"Забронировать бесплатный визит и пройти проверку безопасности","type":"interact","target":"reichstag_booking","count":1,"optional":false},
      {"id":"ascend_ramp","text":"Подняться по спиральной рампе в куполе Norman Foster","type":"interact","target":"reichstag_ramp","count":1,"optional":false},
      {"id":"view_panorama","text":"Осмотреть панорамы Берлина и Тиргартена","type":"interact","target":"berlin_panorama","count":1,"optional":false},
      {"id":"observe_plenary","text":"Заглянуть в пленарный зал и отметить надпись «Dem Deutschen Volke»","type":"interact","target":"plenary_view","count":1,"optional":false},
      {"id":"study_exhibits","text":"Изучить экспозиции о восстановлении и сохранённых граффити 1945 года","type":"interact","target":"reichstag_exhibits","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1000,"money":0,"reputation":{"culture":10},"items":[],"unlocks":{"achievements":["transparent_democracy"],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-berlin-2020-2029-reichstag-dome",
        "title": "Берлин 2020-2029 — Купол Рейхстага",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "berlin", "quest"],
        "topics": ["timeline-author", "political-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-berlin-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/berlin/2020-2029/quest-004-reichstag-dome.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о стеклянном куполе Рейхстага оставался в Markdown и не связывал символику прозрачности с политической веткой.",
        "goal": "Перевести визит в купол в YAML, выделив этапы доступа, архитектурные детали и обзоры города.",
        "essence": "Игрок поднимается по спиральной рампе в куполе Foster и наблюдает парламент и панораму Берлина.",
        "key_points": [
          "Запись на бесплатный визит и процедуры безопасности.",
          "Спиральный маршрут и вид на Тиргартен и зал заседаний.",
          "Купол как символ прозрачности немецкой демократии."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Стеклянный купол Рейхстага стал иконой пост reunification Германии и архитектурным жестом прозрачности власти.\\nКвест показывает, как граждане наблюдают парламентскую работу сверху.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Забронировать бесплатный визит и пройти проверку безопасности.\\n2. Подняться по спиральной рампе в куполе Norman Foster.\\n3. Осмотреть панорамы Берлина и Тиргартена.\\n4. Заглянуть внутрь пленарного зала и отметить надпись «Dem Deutschen Volke».\\n5. Изучить экспозиции о восстановлении здания и сохранённые граффити 1945 года.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Описываются солнечные зеркала, системы вентиляции и сохранённые следы войны.\\nПодчёркивается роль Рейхстага как символа современной немецкой демократии.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1000 XP.\\nВалюта: 0.\\nКультура: +10.\\nАчивка: «Прозрачная демократия».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Туристические, политические и архитектурные механики связывают объект с веткой мира.",
            "mechanics_links": [],
            "assets": []
          }
        ]
      },
      "appendix": {"glossary": [], "references": [], "decisions": []},
      "implementation": {
        "needs_task": false,
        "github_issue": 54,
        "queue_reference": ["shared/trackers/queues/concept/queued.yaml"],
        "blockers": []
      },
      "history": [
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Купол Рейхстага» в YAML и описание демократического символа."}
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

