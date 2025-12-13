-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\tashkent-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.579250

BEGIN;

-- Lore: canon-lore-cis-tashkent-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-cis-tashkent-2020-2093',
    'Ташкент — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-cis-tashkent-2020-2093",
    "title": "Ташкент — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:05:00+00:00",
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
      "cis",
      "tashkent"
    ],
    "topics": [
      "timeline-author",
      "resource-management"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-cis-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-middle-east-2020-2093",
        "relation": "complements"
      },
      {
        "id": "github-issue-1257",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1257",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:05:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/tashkent-2020-2093.md",
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
    "problem": "Таймлайн Ташкента находился в Markdown и не раскрывал пустынные хуки для квестов и систем ресурсного менеджмента.",
    "goal": "Структурировать ключевые эпохи Ташкента как оазисного хаба, управляющего водой, энергией и культурой.",
    "essence": "Ташкент объединяет пустынные протоколы, номадские маршруты и культурный синтез исламского и техномира.",
    "key_points": [
      "Выделены эпохи от оазиса Центральной Азии до экспорта пустынного пакета.",
      "Зафиксированы хуки для каракумских серверов, оазис-протоколов и исламо-кибер синтеза.",
      "Подготовлена основа для сценариев о водных ресурсах и кочевых ретрансляторах."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Оазис Центральной Азии",
        "body": "- «Чирчик-энергия»: гидроэлектростанции для дата-центров.\n- «Регистан 3.0»: историческая площадь с AR-слоями.\n- «Узбек-текстиль+»: умные ткани и адаптивная одежда.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Шёлковый узел",
        "body": "- «Новый шёлковый путь»: ключевая точка маршрута.\n- «Самаркандские архивы»: цифровизация культурного наследия.\n- «Ферганские мастерские»: кустарные импланты и ремесленные цепочки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Пустынная крепость",
        "body": "- «Каракумские серверы»: дата-центры, охлаждаемые ночным холодом.\n- «Оазис-протоколы»: стандарты управления водой и энергией.\n- «Кочевые маршруты»: номадские ретрансляторы в пустыне.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Культурный синтез",
        "body": "- «Исламо-кибер синтез»: интеграция традиций и технологий.\n- «Медресе-университеты»: образовательные центры нового поколения.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет оазиса",
        "body": "- Экспорт протоколов управления ресурсами в пустынных регионах.\n- Распространение стандартов номадских сетей и культурного синтеза.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Регистан 3.0, каракумские серверы, оазис-протоколы и исламо-кибер синтез.\n- Квесты о водном дефиците, номадских ретрансляторах и культурной дипломатии.\n",
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
    "github_issue": 1257,
    "needs_task": false,
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
      "changes": "Конвертация авторских событий Ташкента в структурированный YAML."
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