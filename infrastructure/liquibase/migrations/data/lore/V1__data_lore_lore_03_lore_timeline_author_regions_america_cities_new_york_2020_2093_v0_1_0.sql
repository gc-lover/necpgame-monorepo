-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\new-york-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.414963

BEGIN;

-- Lore: canon-lore-america-new-york-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-new-york-2020-2093',
    'Нью-Йорк — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-new-york-2020-2093",
    "title": "Нью-Йорк — авторские события 2020–2093",
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
      "america",
      "new-york"
    ],
    "topics": [
      "timeline-author",
      "finance"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/new-york-2020-2093.md",
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
    "problem": "Нью-Йорк в Markdown не фиксировал финансовые тени, островные купола и судебные прецеденты приватности.",
    "goal": "Оцифровать трансформацию Нью-Йорка в глобальный пакет финансовой и приватной совместимости.",
    "essence": "Нью-Йорк управляет биржами, куполами и банками памяти, экспортируя «пакет Нью-Йорка».",
    "key_points": [
      "Этапы от фондовых теней до экспорта приватных параметров.",
      {
        "Хуки": "алгоритмические биржи, островные купола, водные трассы, BD-банкинг, суды приватности."
      },
      "Завязки для сюжетов о нулевых каналах, уличных водных маршрутах и юридических войнах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Фондовые тени",
        "body": "- Алгоритмические биржи и нулевые каналы Уолл-стрит.\n- Контракты на медиа-манипуляции.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Островные купола",
        "body": "- Купольные уставы Манхэттена и Бруклина.\n- Водные оффлайн-трассы между boroughs.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Банки памяти",
        "body": "- Лицензии BD-банкинга как новый рынок.\n- Суды приватности формируют стандарты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Мосты совместимости",
        "body": "- Буферы сетей восточного побережья.\n- Согласование протоколов с другими мегаполисами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Нью-Йорка",
        "body": "- Экспорт финансовых и приватных параметров.\n- Нью-Йорк закрепляется как столица цифровых бирж.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Алгоритмические биржи, водные оффлайн-трассы, банки памяти, суды приватности, мосты совместимости.\n- Сюжеты об инфо-войнах, юридических прецедентах и сетевых буферах побережья.\n",
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
    "github_issue": 1284,
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
      "changes": "Конвертация авторских событий Нью-Йорка в структурированный YAML."
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