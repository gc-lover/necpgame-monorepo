-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\manila-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.373723

BEGIN;

-- Lore: canon-lore-asia-manila-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-asia-manila-2020-2093',
        'Манила — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-asia-manila-2020-2093",
        "title": "Манила — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T03:55:00+00:00",
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
          "manila"
        ],
        "topics": [
          "timeline-author",
          "diaspora"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-asia-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1267",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1267",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T03:55:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/manila-2020-2093.md",
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
        "problem": "Манила была описана в Markdown и не отражала диаспоральные связи и климатические вызовы в общей базе знаний.",
        "goal": "Структурировать эпохи Манилы как города архипелага и диаспоры с акцентом на климат и экспат сети.",
        "essence": "Манила выстраивает плавучие кварталы, экспортирует OFW-протоколы и создаёт «пакет архипелага».",
        "key_points": [
          "Этапы от архипелага 2.0 до экспорта островных протоколов.",
          {
            "Хуки": "OFW, калеся, плавучие кварталы, католический киберпанк."
          },
          "Подготовлена основа для сюжетов о климатической миграции и диаспорах."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Архипелаг 2.0",
            "body": "- «OFW-город»: сети филиппинской диаспоры.\n- «Калеся smart»: модернизированный транспорт.\n- «Базилька AR»: цифровые религиозные слои.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — ЮВА-экспат",
            "body": "- «Манила–Сеул коридор»: экспорт талантов.\n- «БПО 3.0»: колл-центры с нейро-интерфейсами.\n- «Диаспора DAO»: глобальные кооперативы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Плавучие кварталы",
            "body": "- «Плавучие города»: адаптация к тайфунам.\n- «Шторм-щиты»: защита бухты.\n- «Католический киберпанк»: синтез веры и технологий.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Диаспорный узел",
            "body": "- «OFW-протоколы»: стандарты труда и репатриации.\n- «Филиппинская культура BD»: экспорт развлечений.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет архипелага",
            "body": "- Экспорт протоколов островных городов и диаспоральных сетей.\n- Манила становится глобальными «воротами диаспоры».\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- OFW, плавучие кварталы, шторм-щиты, католический киберпанк, диаспора DAO.\n- Сюжеты о возвращении диаспоры и климатической миграции.\n",
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
        "github_issue": 1267,
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
          "changes": "Конвертация авторских событий Манилы в структурированный YAML."
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