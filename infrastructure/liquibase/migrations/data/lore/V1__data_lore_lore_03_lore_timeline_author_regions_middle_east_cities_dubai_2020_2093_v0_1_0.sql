-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east\cities\dubai-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.629087

BEGIN;

-- Lore: canon-region-middle-east-dubai-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-middle-east-dubai-2020-2093',
        'Дубай 2020-2093 — Пакет роскоши',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-middle-east-dubai-2020-2093",
        "title": "Дубай 2020-2093 — Пакет роскоши",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-12T01:29:00+00:00",
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
          "dubai",
          "luxury-tech"
        ],
        "topics": [
          "regional-history",
          "climate-control"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east/cities/dubai-2020-2093.md",
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
        "problem": "Хронология Дубая была в Markdown и не описывала климат-купол, элитные импланты и крипто-финансы в базе знаний.",
        "goal": "Конвертировать события города в YAML, выделив переход от нефти к климат-контролю и пакетам роскоши.",
        "essence": "Дубай делает ставку на вертикальные дата-центры, климат-купола и элитные сервисы, экспортируя «пакет роскоши» в мегаполисы мира.",
        "key_points": [
          "Пять эпох показывают эволюцию от нефтяной экономики к климат-контролю и крипто-финансам.",
          "Подчёркнуты Бурдж-дата, климат-купол и шариат-совместимые технологии.",
          "Подготовлены хуки для квестов роскоши, энергетики и офшорных схем."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Вертикальный рай",
            "body": "«Бурдж-Дата» запускает самый высокий дата-центр мира.\n«Пустынные Оазисы» поддерживают роскошь среди песков, «Киберспорт-Арены» привлекают глобальные турниры.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Переход от нефти",
            "body": "«Солнечные Фермы» обеспечивают энергетическую революцию.\n«Искусственные Острова 2.0» расширяют территорию, «Шариат-Совместимость» адаптирует технологии к традициям.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Климат-купол",
            "body": "«Полностью Контролируемая Среда» защищает от экстремальной жары.\n«Элитные Импланты» предлагают дорогие модификации, «Пустынные Штормы» служат естественной защитой от сканирования.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Глобальный хаб",
            "body": "«Транзитный Центр Мира» соединяет Восток и Запад.\n«Крипто-Финансы» превращают город в офшорный центр нового типа.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет роскоши",
            "body": "Дубай экспортирует протоколы климат-контроля и элитных технологий как услугу.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Бурдж-дата, климат-купол, элитные импланты, шариат-совместимость и крипто-финансы раскрывают сценарии роскоши, контроля и трансграничных сделок.\n",
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
          "changes": "Конвертирована хроника Дубая в YAML и выделены механики климат-купола и элитных технологий."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        0) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;