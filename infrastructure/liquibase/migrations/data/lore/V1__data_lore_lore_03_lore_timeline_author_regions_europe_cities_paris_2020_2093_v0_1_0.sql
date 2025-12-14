-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\paris-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.799017

BEGIN;

-- Lore: canon-region-europe-paris-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-europe-paris-2020-2093',
    'Париж 2020-2093 — Суд реальностей',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-europe-paris-2020-2093",
    "title": "Париж 2020-2093 — Суд реальностей",
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
      "paris",
      "neuroethics"
    ],
    "topics": [
      "regional-history",
      "culture-governance"
    ],
    "related_systems": [
      "narrative-service",
      "social-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/paris-2020-2093.md",
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
    "problem": "Париж оставался в Markdown и не учитывал нейроэтические суды и культурные купола в базе знаний.",
    "goal": "Структурировать эпохи Парижа в YAML, выделив катакомбы, комитеты нейроэтики и экспорт «пакета Парижа».",
    "essence": "Париж превращается в лабораторию реальностей, где суды, фестивали и BD-архивы задают глобальные стандарты культуры и безопасности.",
    "key_points": [
      "Пять эпох показывают путь от катакомб красного рынка до экспорта правил культуры.",
      "Зафиксированы суд реальностей, комитет 2048 и карнавал линков как ключевые механики.",
      "Подготовлены хуки для сценариев нейроэтики, уличной безопасности и фестивальных событий."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Катакомбы Красного Рынка",
        "body": "«BD-Катакомбы» проводят кураторские ярмарки памяти и этики.\n«Сена-Ленты» доставляют оффлайн-пакеты по реке, «Неон-Манифест» насыщает улицы перформансами с хард-виаром.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Панорамные купола",
        "body": "«Купол Лувра» объединяет искусство и BD-архивы.\n«Эйфелева Сеть» управляет DroneWays и брендовыми баталиями, «Кухня-Хаби» строят гастрономические цепочки снабжения.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: комитеты нейроэтики",
        "body": "«Комитет 2048» определяет стандарты редактирования BD.\n«Мирные Окна» создают зоны цифрового детокса, «Карнавал Линков» обеспечивает совместимость сетей.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Суд реальностей",
        "body": "«Tribunal de Réalité» разбирает вмешательства в «движок мира».\n«Сеть Музеев» формирует устойчивые архивы, «Зелёные Балконы» внедряют био-параметры в городской слой.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Парижа",
        "body": "Город экспортирует правила культуры, нейроэтики и уличной безопасности, «Ночь Параметров» тестирует сценарии города.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "Суд реальностей, комитеты нейроэтики, фестивали совместимости, речные оффлайн-пакеты и перформансы создают сюжетные ветки культуры и контроля контента.\n",
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
      "changes": "Конвертирована хронология Парижа в YAML и выделены механики нейроэтики и судов реальностей."
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