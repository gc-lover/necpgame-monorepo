-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\chicago-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.228976

BEGIN;

-- Lore: canon-lore-america-chicago-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-america-chicago-2020-2093',
        'Чикаго — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-america-chicago-2020-2093",
        "title": "Чикаго — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:25:00+00:00",
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
          "america",
          "chicago"
        ],
        "topics": [
          "timeline-author",
          "industry"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-america-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1291",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1291",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:25:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/chicago-2020-2093.md",
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
        "problem": "Описание Чикаго в Markdown не фиксировало стратегию озёрного альянса и индустриальные хуки.",
        "goal": "Сформировать дорожную карту превращения Чикаго в континентальный промышленный центр.",
        "essence": "Чикаго соединяет озёрные платформы, зимние купола и синдикаты рабочих, экспортируя «пакет индустрии».",
        "key_points": [
          "Этапы от ветреного города 2.0 до автоматизированного производства.",
          {
            "Хуки": "чикагская петля, озёрный союз, зимние купола, корн-биотопливо, синдикаты рабочих."
          },
          "Подготовлены сценарии для логистики Великих озёр и профсоюзных движений."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Ветреный город 2.0",
            "body": "- «Чикагская петля»: финансовый район с AI-трейдингом.\n- «Мичиган-платформы»: плавучие дата-центры на озере.\n- «Джаз-клубы BD»: цифровая музыкальная сцена.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Озёрный союз",
            "body": "- «Великие озёра альянс»: межгородская координация.\n- «Ветровые фермы»: энергетическая независимость региона.\n- «Подземный Чикаго»: тоннели как логистическая сеть.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Midwest Fortress",
            "body": "- «Континентальный хаб»: центр НСША.\n- «Зимние купола»: защита от экстремальных холодов.\n- «Корн-биореакторы»: биотопливо из кукурузы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Промышленное сердце",
            "body": "- «Производственные автоматы»: роботизированные заводы.\n- «Синдикаты рабочих»: цифровые профсоюзы и коллективы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет индустрии",
            "body": "- Экспорт протоколов автоматизированного производства.\n- Чикаго закрепляет статус индустриального стандарта.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Чикагская петля, озёрный союз, зимние купола, корн-биореакторы, синдикаты рабочих.\n- Сюжеты о профсоюзах, логистике и энергетике Великих озёр.\n",
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
        "github_issue": 1291,
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
          "changes": "Конвертация авторских событий Чикаго в структурированный YAML."
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