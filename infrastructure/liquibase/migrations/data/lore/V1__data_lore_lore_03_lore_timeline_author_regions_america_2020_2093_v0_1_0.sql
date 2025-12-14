-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.604566

BEGIN;

-- Lore: canon-lore-regions-america-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-regions-america-2020-2093',
    'Америки 2020–2093 — авторские события',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-lore-regions-america-2020-2093",
    "title": "Америки 2020–2093 — авторские события",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-12T00:00:00+00:00",
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
      "americas",
      "timeline"
    ],
    "topics": [
      "timeline-author",
      "sovereignty",
      "media"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-2020-2030-author-events",
        "relation": "references"
      },
      {
        "id": "canon-lore-2078-2090-author-events",
        "relation": "references"
      },
      {
        "id": "canon-lore-city-night-city-echo",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america-2020-2093.md",
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
    "problem": "Авторский таймлайн Америк находился в Markdown и не был интегрирован с механиками экстракт-зон, медиа-войн и дата-штормов.",
    "goal": "Перенести региональный материал в структурированный YAML и привязать его к сюжетам НСША и свободных городов.",
    "essence": "Американские купола балансируют между свободными городами и НСША, используя медиа-войны, оффлайн-коридоры и суверенные пакеты прав.",
    "key_points": [
      "Экстракт-зоны и пограничные конвои определяют экономику 2020-х.",
      "Неоновые коридоры и медиа-войны доминируют в 2030-х.",
      "Red+ приносит прорывы через Blackwall и дата-штормы в 2040–2060-х.",
      "Прокси-войны PMC и торги компетенций формируют 2060–2070-е.",
      "К 2080-м развивается экспорт суверенных пакетов и прецеденты «движка мира»."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "timeline_2020_2029",
        "title": "2020–2029 — свободные штаты и купольные уставы",
        "body": "- «Сан-Диего Купол-Рынок»: легализованная экстракт-зона для фиксеров.\n- «Техас Пограничье»: автономные конвои и частные армии за пропускную способность.\n- «Карибские Платформы»: оффшорные дата-хабы и стартовые площадки.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2030_2039",
        "title": "2030–2039 — неоновые коридоры и медиа-войны",
        "body": "- Мексиканские BD-студии становятся политической силой и судятся с «пиратами памяти».\n- Бразильские агро-матрицы запускают войны за аффиксы урожая.\n- Андские микрo-транклинии обеспечивают оффлайн-обмен.\n",
        "mechanics_links": [
          "mechanics/world/events/live-events-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2040_2060",
        "title": "2040–2060 — Red+: ошибки Blackwall и дата-штормы",
        "body": "- «Найт-Сити Эхо» создаёт юридические прецеденты из-за прорывов логов.\n- «Панамский Клин» соединяет океаны оффлайн-пакетами.\n- Андские дата-штормы нарушают локальные сети.\n",
        "mechanics_links": [
          "mechanics/combat/combat-hacking-combat-integration.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2061_2077",
        "title": "2061–2077 — НСША, свободные города и прокси",
        "body": "- Торги компетенций между НСША и свободными городами.\n- «Форт Колорадо» как полигон прокси-войн PMC.\n- Неон-сеть Западного берега формирует совместимые стандарты куполов.\n",
        "mechanics_links": [
          "mechanics/economy/economy-contracts.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2078_2093",
        "title": "2078–2093 — параметры суверенитета и наследие лиг",
        "body": "- Западный пакт распространяет переносимые правила.\n- Амазония экспортирует био-параметры в новые лиги.\n- Суд Найт-Сити создаёт прецеденты по «движку мира».\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "mechanics_and_hooks",
        "title": "Ключевые механики и квестовые хуки",
        "body": "- Экстракт-зоны, медиа-войны BD, дата-штормы, торги компетенций и перенос пакетов суверенитета.\n- Хуки: контракт на Форт Колорадо, следствие по «Эхо» Найт-Сити, караван через Панаму.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      "timeline-author/2020-2030-author-events.md",
      "timeline-author/2078-2090-author-events.md"
    ],
    "decisions": []
  },
  "implementation": {
    "needs_task": false,
    "github_issue": 73,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml#canon-lore-regions-america-2020-2093"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Конвертирован региональный таймлайн Америк в YAML и связан с механиками суверенитета и медиа-войн."
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