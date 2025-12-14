-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\los-angeles-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.533056

BEGIN;

-- Lore: canon-lore-america-los-angeles-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-los-angeles-2020-2093',
    'Лос-Анджелес — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-los-angeles-2020-2093",
    "title": "Лос-Анджелес — авторские события 2020–2093",
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
      "los-angeles"
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
        "id": "github-issue-1288",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1288",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:25:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/los-angeles-2020-2093.md",
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
    "problem": "Таймлайн Лос-Анджелеса в Markdown не связывал медиа-войны, купольные уставы и защиту от дата-штормов в структуре знаний.",
    "goal": "Структурировать развитие ЛА как эталона медиа-безопасности и экспортного стандарта западного побережья.",
    "essence": "Лос-Анджелес сочетает студии BD, купольные контракты и защиту сетей, формируя «пакет ЛА».",
    "key_points": [
      "Этапы от медиа-гексаграмм до стандарта западного берега.",
      {
        "Хуки": "студии BD, купольные уставы, дата-штормы, программы защиты сетей."
      },
      "Сценарии о фиксёрах, медиа-коридорах и региональной совместимости куполов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Медиа-гексаграммы",
        "body": "- «Студии BD»: конкуренция корпораций и пиратов.\n- «Каналы побережья»: бренды контролируют нулевые каналы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Неоновые коридоры",
        "body": "- Купольные уставы районов как система безопасности.\n- Контракты фиксеров на съёмки и охрану.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Дата-штормы",
        "body": "- Сбойные фронты данных над побережьем.\n- Программы защиты сетей против хаоса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Стандарт западного берега",
        "body": "- Совместимость куполов Калифорнии.\n- Экспорт региональных правил безопасности.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет ЛА",
        "body": "- Экспорт медиа и протоколов защиты.\n- ЛА становится управляющим узлом западного побережья.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Медиа-войны BD, купольные уставы, дата-штормы, программы защиты сетей, контракты фиксеров.\n- Сюжеты о брендах, пиратских потоках и совместимости куполов.\n",
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
    "github_issue": 1288,
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
      "changes": "Конвертация авторских событий Лос-Анджелеса в структурированный YAML."
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