-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\sofia-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.575021

BEGIN;

-- Lore: canon-lore-europe-sofia-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-sofia-2020-2093',
        'София — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-sofia-2020-2093",
        "title": "София — авторские события 2020–2093",
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
          "sofia"
        ],
        "topics": [
          "timeline-author",
          "mountains"
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
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/sofia-2020-2093.md",
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
        "problem": "София в Markdown не показывала роль горных серверов, термальных BD и коридора Балканы—Чёрное море.",
        "goal": "Сформировать дорожную карту Софии как горного киберспа и балканского хаба.",
        "essence": "София сочетает витоша-серверы, термальные BD и горные убежища, экспортируя «пакет гор».",
        "key_points": [
          "Этапы от горной крепости до регионального хаба и экспорта протоколов горных городов.",
          {
            "Хуки": "софийские протоколы, древние церкви AR, термальные BD, горные убежища, балкано-черноморский коридор."
          },
          "Сюжеты о горной логистике, wellness-индустрии и кириллица-неоне."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Горная крепость",
            "body": "- «Витоша-серверы»: дата-центры в горах.\n- «Софийские протоколы»: балканские стандарты.\n- «Фрилансер-экосистема»: IT-аутсорсинг.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Балканский ИТ-центр",
            "body": "- «Региональный аутсорсинг»: европейская площадка.\n- «Древние церкви AR»: культурное наследие.\n- «Метро-расширение»: подземные магистрали.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Киберспа-столица",
            "body": "- «Термальные BD»: релакс-индустрия.\n- «Подземные архивы»: защищённые хранилища.\n- «Горные убежища»: эвакуационные точки.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Региональный хаб",
            "body": "- «Балкано-черноморский коридор»: логистика.\n- «Культурный экспорт»: кириллица-неон.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет гор",
            "body": "- Экспорт протоколов горных городов и wellness-индустрии.\n- София закрепляется как горный киберспа Европы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Витоша-серверы, термальные BD, горные убежища, балкано-черноморский коридор, кириллица-неон.\n- Сюжеты о горной логистике, wellness и культурном экспорте.\n",
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
        "github_issue": 1246,
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
          "changes": "Конвертация авторских событий Софии в структурированный YAML."
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