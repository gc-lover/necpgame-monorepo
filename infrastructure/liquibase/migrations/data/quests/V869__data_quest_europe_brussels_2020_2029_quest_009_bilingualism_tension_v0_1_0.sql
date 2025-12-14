-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-009-bilingualism-tension.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:40:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-bilingualism-tension
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
    'canon-quest-brussels-2020-2029-bilingualism-tension',
    'Брюссель 2020-2029 — Двуязычие и напряжение',
    'Игрок изучает противостояние языковых общин, наблюдая двуязычные надписи и политические кризисы.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"survey_bilingual_signs","text":"Исследовать районы Брюсселя и отметить двуязычные надписи","type":"analysis","target":"bilingual_signs","count":1,"optional":false},
      {"id":"visit_info_center","text":"Посетить инфоцентр о бельгийской политике и узнать о кризисе 541 дня","type":"interact","target":"politics_info_center","count":1,"optional":false},
      {"id":"compare_economies","text":"Сравнить экономические различия Фландрии и Валлонии","type":"analysis","target":"regional_economies","count":1,"optional":false},
      {"id":"talk_communities","text":"Пообщаться с NPC из обеих общин, понять аргументы сторон","type":"dialogue","target":"community_dialogues","count":1,"optional":false},
      {"id":"choose_outcome","text":"Сделать выбор — поддержать единство или изучить предпосылки раскола","type":"choice","target":"unity_or_split","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":3000,"money":0,"reputation":{"politics":25},"items":[],"unlocks":{"achievements":["belgian_crisis_expert"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-bilingualism-tension",
        "title": "Брюссель 2020-2029 — Двуязычие и напряжение",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:30:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "political-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-009-bilingualism-tension.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "medium"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о языковом расколе Бельгии находился в Markdown и не раскрывал политические механики.",
        "goal": "Структурировать исследование фламандско-валлонского конфликта в YAML, подчеркнув билингвальность Брюсселя и политические кризисы.",
        "essence": "Игрок изучает противостояние языковых общин, наблюдая за двуязычными надписями и историей политического тупика.",
        "key_points": [
          "Фландрия и Валлония отличаются языком и экономикой, что вызывает напряжение и угрозу раскола.",
          "Брюссель играет роль билингвального острова, где официальные надписи дублируются.",
          "Рекордный кризис без правительства 541 день демонстрирует хрупкость политической системы."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Бельгия разделена между фламандцами и валлонами, а Брюссель служит нейтральной столицей.\\nКвест объясняет причины языкового конфликта и его современную значимость.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Исследовать районы Брюсселя и отметить двуязычные надписи.\\n2. Посетить информационный центр о бельгийской политике и узнать о 541-дневном кризисе.\\n3. Сравнить экономические различия Фландрии и Валлонии.\\n4. Пообщаться с NPC из обеих общин, чтобы понять аргументы сторон.\\n5. Сделать выбор сценария — поддержать единство или изучить предпосылки раскола.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Указывается билингвальный статус Брюсселя, статистика политических кризисов и роль региональных правительств.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 3000 XP.\\nВалюта: 0.\\nПолитика: +25.\\nАчивка: «Знаток бельгийского кризиса».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Политические, лингвистические и социальные механики демонстрируют сложность управления многоязычной страной.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Двуязычие и напряжение» в YAML и описание политического конфликта."}
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















