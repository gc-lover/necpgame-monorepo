-- Issue: #56
-- Import quest from: europe\brussels\2020-2029\quest-002-manneken-pis.yaml
-- Version: 0.1.0
-- Generated: 2025-12-08T22:20:00.000000

BEGIN;

-- Quest: canon-quest-brussels-2020-2029-manneken-pis
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
    'canon-quest-brussels-2020-2029-manneken-pis',
    'Брюссель 2020-2029 — Писающий мальчик',
    'Игрок обнаруживает культовую статую высотой 61 см и сталкивается с характерным бельгийским юмором.',
    'side',
    NULL,
    NULL,
    '{"required_quests":[],"required_flags":[],"required_reputation":{},"required_items":[]}'::jsonb,
    '[
      {"id":"find_statue","text":"Найти статую в узкой улочке, пройти через толпу туристов","type":"travel","target":"manneken_location","count":1,"optional":false},
      {"id":"note_size","text":"Оценить размеры статуи (61 см) и реакцию посетителей","type":"analysis","target":"manneken_size","count":1,"optional":false},
      {"id":"record_costume","text":"Зафиксировать внешний вид и костюм статуи, если надет","type":"interact","target":"manneken_costume","count":1,"optional":false},
      {"id":"reflect_humor","text":"Осмыслить местный юмор и символику города","type":"analysis","target":"brussels_humor","count":1,"optional":false}
    ]'::jsonb,
    '{"experience":500,"money":0,"reputation":{"humor":10},"items":[],"unlocks":{"achievements":["disappointed_tourist"]}}'::jsonb,
    '[]'::jsonb,
    '{
      "metadata": {
        "id": "canon-quest-brussels-2020-2029-manneken-pis",
        "title": "Брюссель 2020-2029 — Писающий мальчик",
        "document_type": "canon",
        "category": "quest",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T02:25:00Z",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}],
        "tags": ["europe", "brussels", "quest"],
        "topics": ["timeline-author", "tourist-attraction"],
        "related_systems": ["narrative-service"],
        "related_documents": [
          {"id": "canon-region-europe-brussels-2020-2093", "relation": "references"}
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/europe/brussels/2020-2029/quest-002-manneken-pis.md",
        "visibility": "internal",
        "audience": ["lore", "narrative", "live_ops"],
        "risk_level": "low"
      },
      "review": {
        "chain": [{"role": "lore_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}],
        "next_actions": []
      },
      "summary": {
        "problem": "Квест о Manneken Pis не был структурирован и не отражал культурный юмор Брюсселя.",
        "goal": "Перевести маршрут к статуе в YAML, подчеркнув размеры, коллекцию костюмов и реакцию туристов.",
        "essence": "Игрок обнаруживает культовую статую высотой 61 см и сталкивается с характерным бельгийским юмором.",
        "key_points": [
          "Толпы туристов и ожидания, не совпадающие с реальностью.",
          "Более 1000 костюмов, которыми украшают статую.",
          "Оптимальный пример культурного абсурда и символики города."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "overview",
            "title": "Контекст",
            "body": "Manneken Pis — исторический символ Брюсселя, отражающий местное чувство юмора и традиции.\\nКвест делает акцент на восприятии туристами и коллекции костюмов статуи.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "quest-steps",
            "title": "Этапы",
            "body": "1. Найти статую в узкой улочке и пройти через толпу туристов.\\n2. Оценить размеры — 61 см — и реакцию посетителей.\\n3. Зафиксировать внешний вид статуи, особенно если она в одном из праздничных костюмов.\\n4. Осмыслить местный юмор и значение символа для города.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "regional-details",
            "title": "Региональные детали",
            "body": "Отмечаются 1000+ костюмов, музей коллекции и роль статуи в городской идентичности.",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "rewards",
            "title": "Награды",
            "body": "Опыт: 500 XP.\\nВалюта: 0.\\nЮмор: +10.\\nАчивка: «Разочарованный турист».",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics",
            "title": "Механики",
            "body": "Туристические и юмористические элементы демонстрируют феномен ожиданий и реальности.",
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
        {"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Писающий мальчик» в YAML и фиксация туристических механик."}
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








