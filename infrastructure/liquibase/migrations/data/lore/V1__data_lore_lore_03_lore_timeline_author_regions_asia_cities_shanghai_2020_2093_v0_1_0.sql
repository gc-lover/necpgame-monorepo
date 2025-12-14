-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\shanghai-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.659499

BEGIN;

-- Lore: canon-region-asia-shanghai-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-asia-shanghai-2020-2093',
    'Шанхай 2020-2093 — Вертикальная гавань',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-asia-shanghai-2020-2093",
    "title": "Шанхай 2020-2093 — Вертикальная гавань",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T01:14:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "asia",
      "shanghai",
      "logistics"
    ],
    "topics": [
      "regional-history",
      "economic-networks"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-asia-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/shanghai-2020-2093.md",
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
    "problem": "Хронология Шанхая в Markdown не описывала сетевые шлюзы и купольные консорциумы в структуре знаний.",
    "goal": "Конвертировать авторские эпохи города в YAML, подчёркивая экономические и технологические параметры для сценариев.",
    "essence": "Шанхай от вертикальных кварталов Пудуна и морских ворот Янцзы переходит к экспорту «Пакета Шанхая» с купольными консорциумами.",
    "key_points": [
      "Пять эпох фиксируют рост буферов совместимости, рынков памяти и купольных пактов.",
      "Выделены шлюзы Shanghai Gateway, синдикаты прошивок и суды сетей.",
      "Подготовлены хуки для сюжетов логистики, BD-банкинга и сетевого контроля."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Вертикальные кварталы",
        "body": "«Стэки Пудуна» вводят квоты света и воздуха, провоцируя социальные взрывы.\n«Сети Бунда» обеспечивают финансовые хабы с нулевыми каналами, а «Ночники Рекламы» накрывают залив AR-слоями.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Морские ворота",
        "body": "«Янцзы-Шлюз» обслуживает дроны-баржи оффлайн-пакетов, «Лабиринты Баз» формируют подземные логистические узлы.\n«Рынки Генома» становятся центром контрафактных ген-услуг.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+ шлюзы совместимости",
        "body": "«Shanghai Gateway» продаёт буферы совместимости сетей.\n«Синдикаты Прошивок» соревнуются за лучшие имплант-прошивки, а «Рынок Памяти» развивает BD-банкинг.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Консорциумы куполов",
        "body": "«Пакт Пудуна» координирует купола, а «Суды Сетей» разбирают конфликты по шлюзам и лицензиям.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Шанхая",
        "body": "Город экспортирует экономические и сетевые параметры, навязывая стандарты управления мегаполисам.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Шлюзы совместимости, рынки памяти и консорциумы куполов создают сюжетные ветви торговли, шантажа и перестройки сетей.\n",
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
      "changes": "Конвертирована хронология Шанхая в YAML и отмечены ключевые механики буферов и куполов."
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