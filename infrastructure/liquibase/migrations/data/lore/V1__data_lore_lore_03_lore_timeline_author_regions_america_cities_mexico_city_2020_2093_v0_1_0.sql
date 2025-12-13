-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\mexico-city-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.398280

BEGIN;

-- Lore: canon-lore-america-mexico-city-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-mexico-city-2020-2093',
    'Мехико — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-mexico-city-2020-2093",
    "title": "Мехико — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:25:00+00:00",
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
      "mexico-city"
    ],
    "topics": [
      "timeline-author",
      "media"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      },
      {
        "id": "github-issue-1287",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1287",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:25:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/mexico-city-2020-2093.md",
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
    "problem": "Таймлайн Мехико в Markdown не связывал полит-медиа, купольные каналы и BD-банкинг.",
    "goal": "Структурировать трансформацию Мехико как центровой столицы уличных и медиа протоколов.",
    "essence": "Мехико превращает турбо-медиа, купольные каналы и рынки памяти в экспортируемый «пакет Мехико».",
    "key_points": [
      "Этапы от полит-медиа BD до шлюзов совместимости Центральной Мексики.",
      {
        "Хуки": "суды против пиратов памяти, купола-каналы, BD-банкинг, уличные кодексы."
      },
      "Заготовлены сценарии для конфликтов между корпорациями и уличными сетями."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Турбо-медиа",
        "body": "- BD-студии становятся политической силой.\n- Суды против «пиратов памяти».\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Купола-каналы",
        "body": "- Купольные уставы и уличные кодексы.\n- Канальные оффлайн-трассы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Рынки памяти",
        "body": "- BD-банкинг как новая экономика.\n- Серые рынки памяти и данных.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Шлюзы совместимости",
        "body": "- Буферы и шлюзы для центральной Мексики.\n- Нормы совместимости для соседних куполов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Мехико",
        "body": "- Экспорт медиа и уличных протоколов.\n- Мехико становится столицей полит-медиа в регионе.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Полит-медиа BD, купола-каналы, суды памяти, BD-банкинг, шлюзы совместимости.\n- Сюжеты о конфликтах корпораций и уличных сетей.\n",
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
    "github_issue": 1287,
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
      "changes": "Конвертация авторских событий Мехико в структурированный YAML."
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