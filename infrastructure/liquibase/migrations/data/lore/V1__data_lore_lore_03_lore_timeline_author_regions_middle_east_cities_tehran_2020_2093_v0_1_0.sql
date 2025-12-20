-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east\cities\tehran-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.645858

BEGIN;

-- Lore: canon-region-middle-east-tehran-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-middle-east-tehran-2020-2093',
    'Тегеран 2020-2093 — Пакет автономии',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-middle-east-tehran-2020-2093",
    "title": "Тегеран 2020-2093 — Пакет автономии",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T01:26:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "middle-east",
      "tehran",
      "autonomy"
    ],
    "topics": [
      "regional-history",
      "sanctions-tech"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-middle-east-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east/cities/tehran-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative"
    ],
    "risk_level": "high"
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
    "problem": "История Тегерана оставалась в Markdown и не фиксировала механики санкций и технологической автономии в базе знаний.",
    "goal": "Структурировать ключевые эпохи города от изоляции к экспорту «пакета автономии».",
    "essence": "Тегеран строит национальные сети, альянсы и стандарты, превращаясь в двигателe технологической независимости региона.",
    "key_points": [
      "Пять эпох показывают путь от санкций-обхода и эльбурзских бункеров до экспорта независимых протоколов.",
      "Акцентированы персидские серверы, исламские протоколы и иранский блок.",
      "Подготовлены хуки для сценариев шёлкового пути, ядерных серверов и культурного экспорта."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Изоляция и инновации",
        "body": "«Санкции-Обход» запускает альтернативные технологии.\n«Персидские Серверы» формируют национальную сеть, «Эльбурз Бункеры» защищают горные дата-хранилища.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Открытие или закрытие?",
        "body": "«Шёлковый Путь 2.0» укрепляет связь с Китаем, «Исламские Протоколы» обеспечивают религиозно-совместимые технологии.\n«Подземный Тегеран» расширяет метро-города.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Региональная держава",
        "body": "«Персидский Залив Контроль» закрепляет морские маршруты, «Ядерные Серверы» поддерживают энергетическую независимость.\n«Культурный Экспорт» усиливает персидскую эстетику.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Ближневосточный игрок",
        "body": "«Иранский Блок» формирует альянс с соседями, «Технологическая Автономия» закрепляет собственные стандарты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет автономии",
        "body": "Тегеран экспортирует протоколы технологической независимости и предлагает услуги по обходу внешнего давления.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Санкции-обход, персидские серверы, эльбурз бункеры, исламские протоколы и иранский блок предлагают квесты по контрабанде технологий, дипломатии и культурной экспансии.\n",
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
    "github_issue": 72,
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
      "changes": "Конвертирована хроника Тегерана в YAML и отмечены механики технологической автономии."
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