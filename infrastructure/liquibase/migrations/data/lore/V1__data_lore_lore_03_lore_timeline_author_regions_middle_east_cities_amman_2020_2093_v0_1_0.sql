-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east\cities\amman-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.614512

BEGIN;

-- Lore: canon-lore-middle-east-amman-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-middle-east-amman-2020-2093',
        'Амман — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-middle-east-amman-2020-2093",
        "title": "Амман — авторские события 2020–2093",
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
          "middle-east",
          "amman"
        ],
        "topics": [
          "timeline-author",
          "desert"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-middle-east-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east/cities/amman-2020-2093.md",
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
        "problem": "Амман в Markdown не отражал синтез древних маршрутов, беженской инфраструктуры и пустынной инженерии.",
        "goal": "Структурировать этапы развития столицы как левантийского посредника и экспортёра пустынных технологий.",
        "essence": "Амман объединяет Петру AR, набатейские протоколы и водное управление, формируя «пакет пустыни».",
        "key_points": [
          "Этапы от города семи холмов до левантийского центра и водного хаба.",
          {
            "Хуки": "беженские протоколы, пустынные серверы, набатейские маршруты 2.0, Петра AR."
          },
          "Готовы сюжетные линии о нейтралитете, водных технологиях и культурном экспорте Набатеев."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Город семи холмов",
            "body": "- «Древняя Петра AR»: цифровое наследие.\n- «Беженские протоколы»: управление миграцией.\n- «Пустынные серверы»: ночное охлаждение.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Региональный посредник",
            "body": "- «Иорданский нейтралитет»: площадка переговоров.\n- «Мёртвое море BD»: уникальный туризм.\n- «Солнечные пустыни»: энергия Вади-Рум.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Пустынная крепость",
            "body": "- «Водные технологии»: опреснение и сохранение.\n- «Подземные города»: защита от жары.\n- «Набатейские протоколы»: торговые пути 2.0.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Левантийский центр",
            "body": "- «Иордано-палестинская интеграция»: экономический союз.\n- «Культурный экспорт»: набатейский неон.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет пустыни",
            "body": "- Экспорт протоколов водного управления и пустынной инфраструктуры.\n- Амман закрепляется как пустынный центр технологий и дипломатии.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Петра AR, беженские протоколы, пустынные серверы, водные технологии, набатейские маршруты.\n- Сюжеты о нейтралитете, торговых путях и пустынных хабах.\n",
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
        "github_issue": 1241,
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
          "changes": "Конвертация авторских событий Аммана в структурированный YAML."
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