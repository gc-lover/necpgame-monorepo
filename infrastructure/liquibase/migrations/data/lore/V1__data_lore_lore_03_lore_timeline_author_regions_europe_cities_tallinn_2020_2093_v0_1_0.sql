-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\tallinn-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.585820

BEGIN;

-- Lore: canon-lore-europe-tallinn-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-tallinn-2020-2093',
        'Таллин — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-tallinn-2020-2093",
        "title": "Таллин — авторские события 2020–2093",
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
          "europe",
          "tallinn"
        ],
        "topics": [
          "timeline-author",
          "e-governance"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-europe-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/tallinn-2020-2093.md",
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
        "problem": "Таллин в Markdown не объединял э-резидентство, кибербезопасность и экспорт цифрового управления.",
        "goal": "Зафиксировать этапы превращения города в эталон э-государства и модель цифровой идентичности.",
        "essence": "Таллин масштабирует э-резиденцию, национальный Blackwall и цифровых кочевников, предлагая «пакет э-государства».",
        "key_points": [
          "Этапы от э-столицы до модельного города и глобального стандарта.",
          {
            "Хуки": "блокчейн-правительство, национальный Blackwall, цифровые кочевники, эстонский стандарт."
          },
          "Сюжеты о кибербезопасности, цифровой демократии и экспорте протоколов госуслуг."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Э-столица",
            "body": "- «Э-резиденция 2.0»: глобальное цифровое гражданство.\n- «Старый город AR»: средневековье + киберпанк.\n- «Балтийский порт»: оффлайн-логистика.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Цифровое государство",
            "body": "- «100% цифровизация»: услуги онлайн/оффлайн.\n- «Блокчейн-правительство»: прозрачное управление.\n- «Стартап-виза»: привлечение талантов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Кибер-нация",
            "body": "- «Национальный Blackwall»: собственная защита.\n- «Балтийский щит»: региональная безопасность.\n- «Цифровые кочевники»: глобальная экосистема.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Модельный город",
            "body": "- «Эстонский стандарт»: экспорт э-управления.\n- «Кибер-патриотизм»: цифровая идентичность.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет э-государства",
            "body": "- Экспорт протоколов цифрового управления и кибербезопасности.\n- Таллин становится хабом э-гражданства и международных сервисов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Э-резиденция 2.0, блокчейн-правительство, национальный Blackwall, цифровые кочевники, эстонский стандарт.\n- Сценарии о цифровом гражданстве, кибербезопасности и экспорте госуслуг.\n",
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
        "github_issue": 1244,
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
          "changes": "Конвертация авторских событий Таллина в структурированный YAML."
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