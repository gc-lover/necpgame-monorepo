-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\vienna-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.672714

BEGIN;

-- Lore: canon-region-europe-vienna-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-vienna-2020-2093',
    'Вена 2020-2093 — Пакет дипломатии',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-vienna-2020-2093",
    "title": "Вена 2020-2093 — Пакет дипломатии",
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
      "vienna",
      "diplomacy"
    ],
    "topics": [
      "regional-history",
      "neutral-mediation"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/vienna-2020-2093.md",
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
    "problem": "Описание Вены оставалось в Markdown и не фиксировало нейтральные дипломатические механики в базе знаний.",
    "goal": "Сформировать YAML с эпохами города, подчеркнув хофбург-серверы, венский нейтралитет и экспорт протоколов посредничества.",
    "essence": "Вена укрепляет роль нейтральной столицы Европы, совмещая культурное наследие и защищённые переговорные хабы.",
    "key_points": [
      "Пять эпох показывают путь от имперского наследия до глобального пакета дипломатии.",
      "Отмечены оперные BD, альпийские бункеры и европейские суды как ключевые механики.",
      "Подготовлены хуки для сценариев переговоров, архивов и культурных событий."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Имперское наследие",
        "body": "«Хофбург Серверы» превращают дворцовые комплексы в дата-центры.\n«Дунай-Хабы» обслуживают речную логистику, «Венские Протоколы» задают дипломатические стандарты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Культурный центр",
        "body": "«Оперные BD» переводят классику в цифровой формат.\n«Кофейни 3.0» объединяют традиции и нейро-интерфейсы, «Альпийские Бункеры» защищают европейские архивы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: нейтральная зона",
        "body": "«Венский Нейтралитет» закрепляет площадки для переговоров.\n«Музейные Архивы» обеспечивают хранение культурного наследия, «Подземная Вена» расширяет метро-города.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Дипломатическая столица",
        "body": "«Европейские Суды» создают арбитражные центры.\n«Культурный Экспорт» продвигает венскую эстетику и образовательные программы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет дипломатии",
        "body": "Город экспортирует протоколы нейтрального посредничества и сервисы компромиссных технологий.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Хофбург серваки, венский нейтралитет, оперные BD, альпийские бункеры и европейские суды создают сценарии переговоров, шпионажа и культурного обмена.\n",
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
      "changes": "Конвертирована хронология Вены в YAML и выделены механики нейтрального посредничества."
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