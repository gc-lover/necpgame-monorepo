-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\oceania\cities\auckland-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.660539

BEGIN;

-- Lore: canon-region-oceania-auckland-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-region-oceania-auckland-2020-2093',
        'Окленд 2020-2093 — Геотермальный хаб',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-region-oceania-auckland-2020-2093",
        "title": "Окленд 2020-2093 — Геотермальный хаб",
        "document_type": "canon",
        "category": "timeline-author",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-11T23:22:00+00:00",
        "concept_approved": false,
        "concept_reviewed_at": "",
        "owners": [
          {
            "role": "lore_analyst",
            "contact": "lore@necp.game"
          }
        ],
        "tags": [
          "oceania",
          "auckland",
          "geothermal"
        ],
        "topics": [
          "regional-history",
          "cultural-integration"
        ],
        "related_systems": [
          "narrative-service",
          "world-state"
        ],
        "related_documents": [
          {
            "id": "canon-region-oceania-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/oceania/cities/auckland-2020-2093.md",
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
        "problem": "Материал по Окленду был в Markdown и не формировал структуру эпох и механик для квестов.",
        "goal": "Перенести ключевые этапы развития города в YAML и подчеркнуть маори-цифровой синтез.",
        "essence": "Окленд превращается из города парусов в полинезийский техно-хаб, экспортирующий островные протоколы.",
        "key_points": [
          "Уточнены пять эпох от геотермальных серверов до пакета островных решений.",
          "Зафиксированы маори-протоколы, вулканический щит и антарктические экспедиции.",
          "Подготовлены хуки для сюжетов об экологии, туризме и логистике в Южном океане."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Город Парусов 2.0",
            "body": "Запускаются геотермальные серверы, питаемые вулканами, усиливая энергосеть города.\nМаори-киберпанк соединяет культурные традиции с цифровыми платформами, поддерживая маршрут в Антарктиду.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Эко-столица",
            "body": "Город достигает 100% зелёной энергии и строит островные сети связи с Тихим океаном.\nТуристические проекты типа «Хоббитон-серверов» привлекают посетителей в цифровые ландшафты.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+ Вулканический щит",
            "body": "Инфраструктура под вулканами усиливает защиту кальдер, а биокоралловые плантации выращивают органические процессоры.\nМаори-протоколы фиксируют культурное наследие в сетевых архивах.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Южнотихоокеанский центр",
            "body": "Полинезийская сеть объединяет острова, обеспечивая логистику и общие стандарты.\nАнтарктические экспедиции используют Окленд как стартовую базу.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет островов",
            "body": "Город экспортирует протоколы управления островными системами и устойчивости инфраструктуры.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "Геотермальные серверы, вулканический щит, маори-протоколы и антарктические экспедиции раскрывают приключения экологии и логистики.\n",
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
        "github_issue": 73,
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
          "changes": "Конвертированы авторские события Окленда в YAML и выделены ключевые механики."
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