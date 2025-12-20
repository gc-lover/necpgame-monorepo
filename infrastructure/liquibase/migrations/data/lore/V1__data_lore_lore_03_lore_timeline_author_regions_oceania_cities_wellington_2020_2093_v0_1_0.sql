-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\oceania\cities\wellington-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.675527

BEGIN;

-- Lore: canon-region-oceania-wellington-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-oceania-wellington-2020-2093',
    'Веллингтон 2020-2093 — Ветреная столица',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-oceania-wellington-2020-2093",
    "title": "Веллингтон 2020-2093 — Ветреная столица",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T23:22:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "oceania",
      "wellington",
      "creative-industry"
    ],
    "topics": [
      "regional-history",
      "resilience"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-oceania-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/oceania/cities/wellington-2020-2093.md",
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
    "problem": "Описание Веллингтона было в Markdown и не отражало сейсмическую устойчивость и креативные механики в базе знаний.",
    "goal": "Структурировать городские эпохи, соединяющие политический статус, киноиндустрию и устойчивость.",
    "essence": "Веллингтон эволюционирует от ветреной столицы к Тихоокеанскому парламенту, экспортируя протоколы устойчивости.",
    "key_points": [
      "Уточнены пять эпох, связывающих парламентские серверы, креативные кластеры и защиту от катастроф.",
      "Отмечены маори-протоколы, подводные кабели и геотермальная энергетика.",
      "Подготовлены хуки для сюжетов о политике, медиа и устойчивости."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Ветреная столица",
        "body": "Парламент размещает защищённые серверы, обеспечивая цифровой суверенитет региона.\nГород переходит на ветровую энергетику и развивает киностудии BD как Голливуд Океании.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Креативный хаб",
        "body": "Производство спецэффектов 2.0 выводит Веллингтон на мировой рынок контента.\nМаори-протоколы и подводные кабели связывают творческие коллективы с глобальными сетями.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ Сейсмическая защита",
        "body": "Умные здания и подземные убежища минимизируют риски землетрясений.\nГеотермальные серверы дополняют энергосистему и обеспечивают контент-хабы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Политический центр",
        "body": "Создаётся Тихоокеанский парламент, задающий экологические стандарты для региона.\nВеллингтон становится площадкой для дипломатических кампаний и зелёных инициатив.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет устойчивости",
        "body": "Город экспортирует протоколы устойчивого развития и медиаплатформы для поддержки островных союзов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Киностудии BD, сейсмическая защита, маори-протоколы и Тихоокеанский парламент открывают цепочки квестов политики и медиа.\n",
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
    "github_issue": 73,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Конвертированы авторские события Веллингтона в YAML и зафиксированы ключевые механики."
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