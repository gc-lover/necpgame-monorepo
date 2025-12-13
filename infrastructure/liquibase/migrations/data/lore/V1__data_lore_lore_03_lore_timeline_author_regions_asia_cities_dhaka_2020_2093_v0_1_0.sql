-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\dhaka-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.481190

BEGIN;

-- Lore: canon-lore-asia-dhaka-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-dhaka-2020-2093',
    'Дакка — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-dhaka-2020-2093",
    "title": "Дакка — авторские события 2020–2093",
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
      "asia",
      "dhaka"
    ],
    "topics": [
      "timeline-author",
      "climate"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/dhaka-2020-2093.md",
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
    "problem": "Климатические сценарии Дакки находились в Markdown и не были связаны с базой знаний.",
    "goal": "Структурировать эпохи Дакки как дельтового мегаполиса с сильной климат-адаптацией и социальной кооперацией.",
    "essence": "Дакка превращает дельту в аркологию, комбинируя купола, водные серверы и экспорт «пакета выживаемости».",
    "key_points": [
      "Этапы от дельта-барьеров до экспорта протоколов климатической адаптации.",
      {
        "Зафиксированы хуки": "вертикальные трущобы, голубые коридоры, водные серверы."
      },
      "Создана база для сюжетов о социальном жилье и климат-миграции."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Мегаполис рек (Delta-City)",
        "body": "- «Дельта-барьеры»: климат-купола и дамбы.\n- «Речные логистические хабы»: лодки-дроны.\n- «Текстиль 3.0»: умные ткани для имплантов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Климат-миграция",
        "body": "- «Вертикальные трущобы»: небоскрёбы-общежития.\n- «Голубые коридоры»: эвакуационные маршруты.\n- «Социальные кооперативы»: микро-DAO районов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Город-аркология",
        "body": "- «Купола над Павна»: защитные экосистемы.\n- «Водные серверы»: дата-центры с жидкостным охлаждением.\n- «Речные мосты 2.0»: многоуровневая инфраструктура.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Производственный узел",
        "body": "- «Текстильный экспорт имплантов»: биосовместимые материалы.\n- «Культурный экспорт»: BD-искусство дельты Ганга.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет выживаемости",
        "body": "- Экспорт протоколов климат-адаптации дельтовых городов.\n- Дакка как эталон выживаемости в условиях подъёма моря.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Дельта-барьеры, водные серверы, вертикальные трущобы, голубые коридоры, социальные кооперативы.\n- Сюжеты о климат-миграции и аркологиях.\n",
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
    "github_issue": 1274,
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
      "changes": "Конвертация авторских событий Дакки в структурированный YAML."
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