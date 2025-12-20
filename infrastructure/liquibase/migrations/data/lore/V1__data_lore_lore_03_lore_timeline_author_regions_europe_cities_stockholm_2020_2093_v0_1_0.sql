-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\stockholm-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.580915

BEGIN;

-- Lore: canon-region-europe-stockholm-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-stockholm-2020-2093',
    'Стокгольм 2020-2093 — Пакет равенства',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-stockholm-2020-2093",
    "title": "Стокгольм 2020-2093 — Пакет равенства",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T02:05:00+00:00",
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
      "stockholm",
      "welfare"
    ],
    "topics": [
      "regional-history",
      "social-engineering"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/stockholm-2020-2093.md",
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
    "problem": "Стокгольм в Markdown не учитывал архипелаг-серверы, блок-щиты и универсальные импланты в базе знаний.",
    "goal": "Структурировать эпохи Стокгольма в YAML, подчеркнув нордические стандарты и экспорт «пакета равенства».",
    "essence": "Стокгольм выстраивает социальную утопию через архипелаг-серверы, блок-щиты и универсальные импланты, закрепляя регион как образец цифрового государства.",
    "key_points": [
      "Пять эпох описывают путь от архипелаг-серверов до прямой демократии 2.0 и экспорта равенства.",
      "Выделены нордические блок-щиты, автономные коммуны и универсальный базовый имплант.",
      "Подготовлены хуки для сюжетов социальной инженерии, арктических архивов и нейро-голосования."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Скандинавская модель",
        "body": "«Архипелаг-Серверы» выносят дата-центры на острова.\n«Социальный Имплант» запускает госпрограммы модификаций, «Балтийские Хабы» обслуживают морские оффлайн маршруты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Нордический союз",
        "body": "«Скандинавский Стандарт» унифицирует протоколы региона.\n«Зелёная Энергия» переводит город на возобновляемые источники, «Метро-Лабиринты» расширяют подземный город.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: блок-щиты",
        "body": "«Нордические Блок-Щиты» защищают от DataKrash.\n«Автономные Коммуны» обеспечивают самодостаточные районы, «Ледяные Архивы» хранят данные в Арктике.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Социальная утопия",
        "body": "«Универсальный Базовый Имплант» выдаётся всем гражданам.\n«Прямая Демократия 2.0» внедряет нейро-голосование и прозрачные бюджеты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет равенства",
        "body": "Город экспортирует протоколы социального государства и сетевой демократии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Архипелаг-серверы, нордические блок-щиты, автономные коммуны, универсальный имплант и нейро-голосование создают сценарии равенства, кибербезопасности и арктических операций.\n",
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
      "changes": "Конвертирована хронология Стокгольма в YAML и выделены механики социального государства."
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