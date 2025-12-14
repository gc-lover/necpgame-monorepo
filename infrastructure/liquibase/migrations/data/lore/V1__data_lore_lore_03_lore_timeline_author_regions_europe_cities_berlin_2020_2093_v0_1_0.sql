-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\berlin-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.761712

BEGIN;

-- Lore: canon-region-europe-berlin-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-berlin-2020-2093',
    'Берлин 2020-2093 — Пакет Берлина',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-berlin-2020-2093",
    "title": "Берлин 2020-2093 — Пакет Берлина",
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
      "berlin",
      "community"
    ],
    "topics": [
      "regional-history",
      "dao-governance"
    ],
    "related_systems": [
      "narrative-service",
      "systems-design"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/berlin-2020-2093.md",
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
    "problem": "Хронология Берлина была в Markdown и не структурировала механики открытых стандартов и городских DAO.",
    "goal": "Сформировать YAML, описывающий эволюцию города от техно-коммун до экспорта комьюнити-процедур.",
    "essence": "Берлин становится площадкой для открытых стандартов, референдумов и нулевых каналов, экспортируя «пакет Берлина».",
    "key_points": [
      "Пять эпох фиксируют рост community-моделей и автономных организаций.",
      "Подчёркнуты Standard 2052, референдум Null и Ночь Null как ключевые механики.",
      "Подготовлены хуки для сценариев DAO, городских выборов и кибербезопасности."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Техно-коммуны",
        "body": "«Коммуны Spree» организуют кооперативы сетей и энергии.\n«Берлинские Ключи» дают гражданские токены доступа, «U-Bahn Тоннели» скрывают маршруты экстракции.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Спирали Рейна",
        "body": "«Рейн-Спирали» объединяют дата-центры и вертикальные фермы.\n«Ночные Порты» обслуживают оффлайн-пакеты, «Сцена Null» формирует клубную субкультуру нулевых каналов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: стандарты сообществ",
        "body": "«Standard 2052» задаёт открытые протоколы совместимости.\n«Комиссии Районов» выбирают режимы безопасности, «Зелёный Код» внедряет био-метрики в уличную политику.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Городские АО",
        "body": "«АО Берлин» переводит город на автономное управление.\n«Референдум Null» определяет инвестиции в нулевые каналы, «Тендеры Патрулей» запускают конкурсы community-патрулей.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Берлина",
        "body": "Город экспортирует открытые стандарты, процедуры комьюнити и проводит «Ночь Null» как ежегодный стресс-тест.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Комьюнити-стандарты, городские DAO, нулевые каналы, референдумы и тендеры патрулей обеспечивают сюжетные ветви социальной инженерии и техно-политики.\n",
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
      "changes": "Конвертирована хронология Берлина в YAML и выделены механики открытых стандартов и DAO."
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