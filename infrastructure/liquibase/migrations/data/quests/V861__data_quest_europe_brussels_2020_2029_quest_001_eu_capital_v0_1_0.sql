-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-001-eu-capital.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:20:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-eu-capital
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
    'canon-quest-brussels-2020-2029-eu-capital',
    'Брюссель 2020-2029 — Столица ЕС',
    'Игрок исследует квартал европейских институтов, знакомясь с парламентом, комиссией и символами объединённой Европы.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"visit_quarter","text":"Посетить European Quarter и ознакомиться с основными зданиями","type":"travel","target":"eu_quarter","count":1,"optional":false},
      {"id":"parlamentarium","text":"Зайти в Parlamentarium и изучить историю ЕС","type":"interact","target":"parlamentarium","count":1,"optional":false},
      {"id":"observe_parliament","text":"Посетить Европейский парламент и наблюдать за работой депутатов","type":"interact","target":"eu_parliament","count":1,"optional":false},
      {"id":"explore_berlaymont","text":"Исследовать Берлемон и окружение Европейской комиссии","type":"interact","target":"berlaymont_area","count":1,"optional":false},
      {"id":"flags_27","text":"Зафиксировать ансамбль флагов всех стран ЕС и роль Брюсселя","type":"analysis","target":"eu_flags","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":2500,"money":0,"reputation":{"politics":25},"items":[],"unlocks":{"achievements":["eurocrat"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-eu-capital",
        "title": "Брюссель 2020-2029 — Столица ЕС",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:25:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "political-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-001-eu-capital.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест «Столица ЕС» был в Markdown и не связывался с политическими механиками Брюсселя.",
        "goal": "Структурировать посещение европейских институтов в YAML, выделив ключевые объекты и образовательную ценность.",
        "essence": "Игрок исследует квартал европейских институтов, знакомясь с парламентом, комиссией и символами объединённой Европы.",
        "key_points": [
          "Европейский квартал концентрирует парламент, Берлемон и музеи ЕС.",
          "Подчёркивается масштабы бюрократии и присутствие 27 флагов.",
          "Квест усиливает политическую линию Брюсселя и связи с геополитикой."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Брюссель служит де-факто столицей Европейского союза, где размещены ключевые институты и работает свыше 40 тысяч еврократов.\\nКвест вводит игрока в инфраструктуру Европейского квартала и его роль в объединении Европы.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Посетить European Quarter и ознакомиться с основными зданиями.\\n2. Зайти в Parlamentarium для интерактивного знакомства с историей ЕС.\\n3. Посетить Европейский парламент и наблюдать за работой депутатов.\\n4. Исследовать Берлемон и окружение Европейской комиссии.\\n5. Зафиксировать ансамбль флагов всех стран ЕС и роль Брюсселя в принятии решений.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Подчёркивается статус столицы ЕС, масштабы бюрократии и символика объединённой Европы.\\nОтмечаются учреждения, библиотека и общественные пространства для граждан.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 2500 XP.\\nВалюта: 0.\\nПолитика: +25.\\nАчивка: «Еврократ».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Политические, образовательные и геополитические элементы демонстрируют влияние ЕС и важность дипломатии.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "references",
            "title": "Связи",
            "body": "Связан с политической линией Брюсселя и сюжетами о европейской бюрократии.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Столица ЕС» в YAML и структурирование политических этапов."}
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













