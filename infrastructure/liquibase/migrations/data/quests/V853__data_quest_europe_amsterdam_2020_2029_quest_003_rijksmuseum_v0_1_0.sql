-- Issue: #55
-- Import quest from: europe\amsterdam\2020-2029\quest-003-rijksmuseum.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T21:06:00.000000

BEGIN;

-- Quest: canon-quest-amsterdam-2020-2029-rijksmuseum
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
    'canon-quest-amsterdam-2020-2029-rijksmuseum',
    'Амстердам 2020-2029 — Рейксмузеум',
    'Посещение Рейксмузеума с «Ночным дозором», Вермеером и делфтским фарфором укрепляет культурную идентичность города.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"enter_museum","text":"Войти в Рейксмузеум и получить доступ к главному залу","type":"interact","target":"rijksmuseum_entry","count":1,"optional":false},
      {"id":"view_night_watch","text":"Изучить «Ночной дозор» Рембрандта","type":"interact","target":"night_watch","count":1,"optional":false},
      {"id":"view_vermeer_hals","text":"Осмотреть «Молочницу» Вермеера и работы Ф. Хальса","type":"interact","target":"vermeer_hals","count":1,"optional":false},
      {"id":"see_delftware","text":"Посетить экспозицию делфтского фарфора и артефактов Золотого века","type":"interact","target":"delftware_expo","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":1500,"money":-20,"reputation":{"culture":20},"items":[],"unlocks":{"achievements":["golden_age_connoisseur"],"flags":[]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-amsterdam-2020-2029-rijksmuseum",
        "title": "Амстердам 2020-2029 — Рейксмузеум",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:10:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "amsterdam", "quest"],
        "topics": ["timeline-author", "cultural-tour"],
        "related_systems": ["narrative-service", "world-state"],
        "related_documents": [
          {"id": "canon-region-europe-amsterdam-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-003-rijksmuseum.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Описание квеста «Рейксмузеум» не было структурировано для интеграции в каноническую базу.",
        "goal": "Перевести музейный маршрут в YAML, выделив masterpieces и образовательную ценность.",
        "essence": "Посещение Рейксмузеума знакомит игроков с ключевыми полотнами Золотого века и формирует культурную идентичность Амстердама.",
        "key_points": [
          "Фокус на «Ночном дозоре» Рембрандта и коллекции шедевров Вермеера и Ф. Хальса.",
          "Делфтский фарфор и тематические залы раскрывают экономику и эстетические мотивы XVII века.",
          "Квест усиливает культурные и образовательные механики Амстердама."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Рейксмузеум — национальное собрание искусства Нидерландов, символ Золотого века и культурного влияния Амстердама.\\nКвест позиционирует музей как точку интереса для социально-культурных сценариев.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Войти в Рейксмузеум и получить доступ к главному залу.\\n2. Изучить «Ночной дозор» Рембрандта как центральный экспонат.\\n3. Осмотреть «Молочницу» Вермеера и «Весёлого пьяницу» Франса Хальса.\\n4. Посетить экспозиции делфтского фарфора и других артефактов Золотого века.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Подчёркивается роль мастеров света и композиции в формировании визуальной школы Нидерландов.\\nЭкспозиция демонстрирует связь между торговым процветанием и культурным взлётом XVII века.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 1500 XP.\\nВалюта: −20.\\nКультура: +20.\\nАчивка: «Ценитель Золотого Века».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Культурные, образовательные и исторические элементы поддерживают социальные активности и ивенты Live Ops.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "references",
            "title": "Связи",
            "body": "Включён в культурную линию Амстердама и метанарративы о европейском искусстве.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Рейксмузеум» в YAML и структурирование музейных этапов."}
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








