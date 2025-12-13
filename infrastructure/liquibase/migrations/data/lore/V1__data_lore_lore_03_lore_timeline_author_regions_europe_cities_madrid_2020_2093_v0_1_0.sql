-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\madrid-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.640338

BEGIN;

-- Lore: canon-region-europe-madrid-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-madrid-2020-2093',
    'Мадрид 2020-2093 — Иберийский энергетический хаб',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-madrid-2020-2093",
    "title": "Мадрид 2020-2093 — Иберийский энергетический хаб",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T01:45:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "europe",
      "madrid",
      "energy"
    ],
    "topics": [
      "regional-history",
      "governance"
    ],
    "related_systems": [
      "narrative-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/madrid-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "lore_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Хронология Мадрида находилась в Markdown и не учитывала энергетику, климатические купола и южноевропейские альянсы в структуре знаний.",
    "goal": "Перевести события города в YAML, выделив иберийские хабы, DAO-муниципии и экспорт управленческих протоколов.",
    "essence": "Мадрид становится энергетическим и дипломатическим узлом Южной Европы, объединяя культуру, логистику и климатические решения.",
    "key_points": [
      "Пять эпох фиксируют рост от кофейных нейросетей до «пакета Иберии».",
      "Подчёркнуты солнечная мезета, антикризисные купола и южный альянс.",
      "Подготовлены хуки для сценариев энергетики, транспорта и культурного экспорта."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Иберийский хаб",
        "body": "«Кортадито 3.0» разворачивает кофейные нейросети площадей.\n«Мадрид—Барселона Коридор» обеспечивает гиперлогистику, «Музеи BD» переводят Прадо и Рейна София в цифру.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Южноевропейская ось",
        "body": "«Солнечная Мезета» формирует фермы возобновляемой энергии.\n«Испанские DAO-Мунсипии» внедряют цифровое самоуправление, «Стадионы AR» поддерживают массовые события.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: континентальный узел",
        "body": "«Иберо-Латино Коридоры» связывают Испанию с Латинской Америкой.\n«Антикризисные Купола» защищают от жары, «Метро-Мегаструктура» расширяет подземные магистрали.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Дипломатический центр",
        "body": "«Южный Альянс» укрепляет блок Испании, Португалии и Италии.\n«Культурный Экспорт» продвигает фламенко-неон и гастрономические сетки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Иберии",
        "body": "Город экспортирует протоколы южноевропейского управления и климатических решений.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Музеи BD, стадионы AR, антикризисные купола, DAO-муниципии и иберо-латино коридоры задают сюжетные ветви энергетики, логистики и дипломатии.\n",
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
    "needs_task": false,
    "github_issue": 71,
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
      "changes": "Конвертирована хронология Мадрида в YAML и выделены энергетические и дипломатические механики."
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