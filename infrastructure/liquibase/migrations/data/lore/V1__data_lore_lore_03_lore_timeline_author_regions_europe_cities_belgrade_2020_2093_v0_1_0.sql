-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\belgrade-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.497368

BEGIN;

-- Lore: canon-lore-europe-belgrade-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-belgrade-2020-2093',
    'Белград — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-belgrade-2020-2093",
    "title": "Белград — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T00:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "narrative_team",
        "contact": "narrative@necp.game"
      }
    ],
    "tags": [
      "regions",
      "europe",
      "belgrade"
    ],
    "topics": [
      "timeline-author",
      "mediation"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/belgrade-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "worldbuilding",
      "live_ops"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "narrative_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Белград в Markdown не показывал связку речной логистики, балканского арбитража и культурных мостов в единой структуре.",
    "goal": "Оцифровать путь Белграда от речного узла до посреднического пакета балканского региона.",
    "essence": "Белград соединяет Дунайско-Савский коридор, балканскую сеть и нейтральный арбитраж, создавая «пакет моста».",
    "key_points": [
      "Этапы от балканского узла до возможной федерации и экспорта посреднических протоколов.",
      {
        "Хуки": "сплавы-рынки, подземные коммуникации, балканский арбитраж, речные серверы, балканский неон."
      },
      "Сюжеты о региональной интеграции, нейтралитете и культурных обменах Восток-Запад."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Балканский узел",
        "body": "- «Дунай-Сава коридор»: речная логистика.\n- «Сплавы»: плавучие рынки на реках.\n- «Калемегдан AR»: цифровая крепость.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Региональная интеграция",
        "body": "- «Балканская сеть»: объединение городов региона.\n- «Фрилансер-столица»: удалённая работа на мировые рынки.\n- «Подземные коммуникации»: бункеры как дата-центры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Нейтральный хаб",
        "body": "- «Балканский арбитраж»: посредник в конфликтах.\n- «Культурные мосты»: связь Восток-Запад.\n- «Речные серверы»: охлаждение дата-центров водой.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Балканская федерация?",
        "body": "- «Объединение или раздел»: политические игры.\n- «Культурный экспорт»: балканский неон.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет моста",
        "body": "- Экспорт протоколов посреднических городов и речной логистики.\n- Белград закрепляется как медиатор Балкан.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Дунай-Сава коридор, балканская сеть, балканский арбитраж, речные серверы, балканский неон.\n- Сюжеты о нейтралитете, культурных мостах и речных рынках.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 1251,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "concept_director",
      "changes": "Конвертация авторских событий Белграда в структурированный YAML."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    0
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;