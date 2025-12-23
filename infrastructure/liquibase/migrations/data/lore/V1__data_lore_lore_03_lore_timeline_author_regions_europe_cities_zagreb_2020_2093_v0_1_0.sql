-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\zagreb-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.605854

BEGIN;

-- Lore: canon-lore-europe-zagreb-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-zagreb-2020-2093',
        'Загреб — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-zagreb-2020-2093",
        "title": "Загреб — авторские события 2020–2093",
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
          "zagreb"
        ],
        "topics": [
          "timeline-author",
          "tourism"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/zagreb-2020-2093.md",
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
        "problem": "Загреб в Markdown не отражал адриатические коридоры, климат-купола и острова-серверы в структуре знаний.",
        "goal": "Оцифровать развитие Загреба как адриатического туристического и технологического узла.",
        "essence": "Загреб соединяет коридоры Загреб—Риека, острова-серверы и культурные протоколы, экспортируя «пакет побережья».",
        "key_points": [
          "Этапы от адриатических ворот до экспорта туристических протоколов.",
          {
            "Хуки": "коридор Загреб—Риека, подземные бункеры, климат-купола Адриатики, острова-серверы, альпийско-адриатический альянс."
          },
          "Сюжеты о туризме, распределённых центрах и климатической защите побережья."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Адриатические ворота",
            "body": "- «Загреб—Риека коридор»: связь столицы с морем.\n- «Медимурские стартапы»: рост IT-индустрии.\n- «Исторический центр AR»: австро-венгерское наследие в цифре.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Адриатический хаб",
            "body": "- «Хорватское побережье»: синтез туризма и технологий.\n- «Подземные бункеры»: наследие холодной войны.\n- «Балканская ось»: транзит север—юг.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Туристическая крепость",
            "body": "- «Климат-купола Адриатики»: защита побережья.\n- «Острова-серверы»: распределённые дата-центры.\n- «Культурные протоколы»: цифровой туризм.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Региональный центр",
            "body": "- «Альпийско-адриатический альянс»: экономическая кооперация.\n- «Экспорт туризма»: BD-путешествия и гибридные фестивали.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет побережья",
            "body": "- Экспорт протоколов туристических городов и климатических решений.\n- Загреб закрепляется как адриатический координатор.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Коридор Загреб—Риека, подземные бункеры, климат-купола, острова-серверы, альпийско-адриатический альянс.\n- Сюжеты о туризме, безопасности побережья и распределённых центрах данных.\n",
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
        "github_issue": 1243,
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
          "changes": "Конвертация авторских событий Загреба в структурированный YAML."
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