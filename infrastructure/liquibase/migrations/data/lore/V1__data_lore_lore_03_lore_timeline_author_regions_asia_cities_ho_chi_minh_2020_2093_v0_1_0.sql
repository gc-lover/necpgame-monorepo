-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\ho-chi-minh-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.348703

BEGIN;

-- Lore: canon-lore-asia-ho-chi-minh-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-asia-ho-chi-minh-2020-2093',
        'Хошимин — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-asia-ho-chi-minh-2020-2093",
        "title": "Хошимин — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
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
          "asia",
          "ho-chi-minh"
        ],
        "topics": [
          "timeline-author",
          "logistics"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-asia-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/ho-chi-minh-2020-2093.md",
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
        "problem": "Таймлайн Хошимина в Markdown не отражал дельтовые и производственные хуки в структуре знаний.",
        "goal": "Оформить эпохи Хошимина с акцентом на дельтовую логистику, климат и производство имплантов.",
        "essence": "Хошимин сочетает канальные хабы, рисовые биореакторы и кибер-вьетнамскую культуру, создавая «пакет дельты».",
        "key_points": [
          "Этапы от Меконг-тех до экспорта дельтовых протоколов.",
          {
            "Хуки": "канальные хабы, робо-скутеры, рисовые биореакторы, плавучие серверы."
          },
          "Подготовлена база для сюжетов о производстве, климат-адаптации и культурном синтезе."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Меконг-тех",
            "body": "- «Канальные хабы»: логистика по Меконгу.\n- «Стартап-аркады»: гаражная экономика.\n- «Франко-колониал AR»: слои старого Сайгона.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — ЮВА производство",
            "body": "- «Монтаж имплантов»: контрактные фабрики.\n- «Робо-скутеры»: автономная доставка.\n- «Анти-паводковые валы»: защита от наводнений.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Дельтовая крепость",
            "body": "- «Рисовые биореакторы»: энергия из сельхоз-отходов.\n- «Плавучие серверы»: водяное охлаждение.\n- «Купола жары»: климат-управление для районов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Региональный хаб",
            "body": "- «АСЕАН-экспорт»: логистика Юго-Восточной Азии.\n- «Культурный синтез»: вьет-киберпанк и медиа.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет дельты",
            "body": "- Экспорт протоколов дельтовых городов по безопасности и логистике.\n- Хошимин становится эталоном устойчивой дельтовой экономики.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Канальные хабы, робо-скутеры, рисовые биореакторы, плавучие серверы, купола жары.\n- Сюжеты о производстве имплантов и климатической адаптации.\n",
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
        "github_issue": 1271,
        "needs_task": false,
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
          "changes": "Конвертация авторских событий Хошимина в структурированный YAML."
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