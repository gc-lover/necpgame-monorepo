-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\warsaw-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.679886

BEGIN;

-- Lore: canon-region-europe-warsaw-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-warsaw-2020-2093',
    'Варшава 2020-2093 — Пакет памяти',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-warsaw-2020-2093",
    "title": "Варшава 2020-2093 — Пакет памяти",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T02:20:00+00:00",
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
      "warsaw",
      "transit"
    ],
    "topics": [
      "regional-history",
      "heritage-security"
    ],
    "related_systems": [
      "narrative-service",
      "world-state"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-region-cis-2020-2093",
        "relation": "bridges"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/warsaw-2020-2093.md",
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
    "problem": "Хронология Варшавы находилась в Markdown и не описывала транзитные маршруты, щит Blackwall и экспорт протоколов памяти.",
    "goal": "Сформировать структурированный YAML с эпохами Варшавы, выделив логистический хаб Восток-Запад, цифровые мемориалы и буферные механики.",
    "essence": "Варшава становится щитом памяти и транзита между ЕС и СНГ, совмещая контрабандные маршруты и экспорт исторических архивов.",
    "key_points": [
      "Пять эпох показывают эволюцию от транзитного узла до глобального пакета памяти.",
      "Зафиксированы механики подпольных маршрутов, восточного щита и кооперативов районов.",
      "Подготовлены хуки для сценариев логистики, безопасности и культурного наследия."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Транзитный узел Восток-Запад",
        "body": "«Висла-Порт» превращает Варшаву в логистический хаб ЕС—СНГ.\n«Подпольные Маршруты» обслуживают серые каналы имплантов, «Память Восстания» запускает AR-мемориалы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Купол индустрии",
        "body": "«Промышленные Анклавы» выпускают военные дроны.\n«Серые Фиксеры» курируют восточно-западные контракты, «Подземная Варшава» хранит данные в бункерах.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: щит Blackwall",
        "body": "«Восточный Щит» блокирует эхо DataKrash со стороны СНГ.\n«Кооперативы Районов» развивают локальное самоуправление, «Памятники Данных» сохраняют историю.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Федеративный хаб",
        "body": "«Балтийско-Черноморский Коридор» закрепляет торговый маршрут.\n«Стандарты Совместимости» сводят протоколы ЕС и СНГ.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет памяти",
        "body": "Город экспортирует исторические архивы и протоколы работы с коллективной памятью.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Логистические контракты, подпольные маршруты, восточный щит, районные кооперативы и экспорт данных задают сценарии транзита, безопасности и культурного наследия.\n",
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
      "changes": "Конвертирована хронология Варшавы в YAML и выделены механики транзита и защиты памяти."
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