-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.212556

BEGIN;

-- Lore: canon-lore-regions-africa-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-regions-africa-2020-2093',
        'Африка 2020–2093 — авторские события',
        'canon',
        'timeline-author',
        '{
      "metadata": {
        "id": "canon-lore-regions-africa-2020-2093",
        "title": "Африка 2020–2093 — авторские события",
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
          "africa",
          "timeline"
        ],
        "topics": [
          "timeline-author",
          "resources",
          "bio-tech"
        ],
        "related_systems": [
          "narrative-service",
          "world-service",
          "economy-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-2045-2060-author-events",
            "relation": "references"
          },
          {
            "id": "canon-lore-2078-2090-author-events",
            "relation": "references"
          },
          {
            "id": "canon-lore-factions-corps-2020-2093",
            "relation": "complements"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa-2020-2093.md",
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
        "problem": "Таймлайн Африки хранился в Markdown и не был привязан к механикам ресурсных войн, био-протоколов и культурного экспорта.",
        "goal": "Формализовать этапы развития региона для авторских событий и систем живого мира.",
        "essence": "Африка превращается в арену за редкоземы и био-протоколы, отстаивая суверенитет данных и экспортируя кибер-культуру.",
        "key_points": [
          "Корпоративные анклавы и свободные зоны задают стартовый баланс 2020-х.",
          "Ресурсные войны и кибер-племена формируют конфликты 2030-х.",
          "Купола Экватора и биотехнологические рынки меняют правила Red+ периода.",
          "Альянс куполов и культурный ренессанс противостоят неоколониализму 2060-х.",
          "К 2080-м африканские города контролируют память и экспортируют культурные параметры."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "timeline_2020_2029",
            "title": "2020–2029 — корпоративные анклавы и свободные зоны",
            "body": "- Корпоративные зоны Лагоса и серые пригороды.\n- Кейптаун как морской хаб оффлайн-пакетов.\n- Найроби и пустынные коридоры как ключевые логистические точки.\n",
            "mechanics_links": [
              "mechanics/world/events/world-events-framework.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2030_2039",
            "title": "2030–2039 — ресурсные войны и кибер-племена",
            "body": "- Корпоративные войны за редкоземы Конго.\n- Кибер-племена Сахеля как гибрид традиций и технологий.\n- Занзибар как фрипорт для BD-контента.\n",
            "mechanics_links": [
              "mechanics/world/events/live-events-system.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2040_2060",
            "title": "2040–2060 — Red+: автономные зоны и биотехнологии",
            "body": "- Климат-контролируемые купола Экватора.\n- Африканские стандарты генной модификации («Био-Протоколы»).\n- Серые рынки экспериментальных имплантов и сахаские туннели.\n",
            "mechanics_links": [
              "mechanics/economy/economy-contracts.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2061_2077",
            "title": "2061–2077 — неоколониализм 2.0 и сопротивление",
            "body": "- Альянс куполов против корп-хегемонии.\n- Единый «Африканский стандарт» безопасности и торговли.\n- Proxy-войны и киберпанк-ренессанс BD-культуры.\n",
            "mechanics_links": [
              "mechanics/world/events/world-events-framework.yaml"
            ],
            "assets": []
          },
          {
            "id": "timeline_2078_2093",
            "title": "2078–2093 — суверенитет данных и культурный экспорт",
            "body": "- Суверенитет памяти и контроль архивов.\n- Экспорт эстетики и био-протоколов.\n- Федерация куполов как политический союз.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "mechanics_and_hooks",
            "title": "Ключевые механики и квестовые хуки",
            "body": "- Ресурсные войны, био-протоколы, кибер-племена, сахаские туннели, суверенитет данных.\n- Хуки: экстракция редкоземов, контрабанда через туннели, дипломатия в Федерации куполов, охота за прототипами.\n",
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
          "timeline-author/2045-2060-author-events.md",
          "timeline-author/2078-2090-author-events.md",
          "timeline-author/factions/corps-2020-2093.md"
        ],
        "decisions": []
      },
      "implementation": {
        "needs_task": false,
        "github_issue": 73,
        "queue_reference": [
          "shared/trackers/queues/concept/queued.yaml#canon-lore-regions-africa-2020-2093"
        ],
        "blockers": []
      },
      "history": [
        {
          "version": "0.1.0",
          "date": "2025-11-12",
          "author": "narrative_team",
          "changes": "Конвертирован региональный таймлайн Африки в YAML и привязан к механикам ресурсных войн и культурного экспорта."
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