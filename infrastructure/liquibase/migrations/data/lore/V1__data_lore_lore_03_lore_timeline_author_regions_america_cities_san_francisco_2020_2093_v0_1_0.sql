-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\san-francisco-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.290018

BEGIN;

-- Lore: canon-lore-america-san-francisco-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-america-san-francisco-2020-2093',
        'Сан-Франциско — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-america-san-francisco-2020-2093",
        "title": "Сан-Франциско — авторские события 2020–2093",
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
          "america",
          "san-francisco"
        ],
        "topics": [
          "timeline-author",
          "innovation"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-america-2020-2093",
            "relation": "references"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/san-francisco-2020-2093.md",
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
        "problem": "Таймлайн Сан-Франциско в Markdown не отражал взаимодействие тех-инноваций, сейсмозащиты и DAO-управления.",
        "goal": "Систематизировать развитие залива SF от кремниевой меты до экспорта протоколов тех-столиц.",
        "essence": "Сан-Франциско сочетает долину AI, сейсмо-щиты и городской DAO, формируя «пакет залива».",
        "key_points": [
          "Этапы от генеративных ИИ и ко-ливингов до нулевых выбросов и глобального тех-моста.",
          {
            "Хуки": "купола тумана, Сан-Франциско—Токио, городской DAO, нулевые выбросы."
          },
          "Подготовлены сюжетные опоры для стартап-культуры, международных тех-альянсов и климатической инновации."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Кремниевая мета",
            "body": "- «Долина AI»: генеративные платформы.\n- «Ко-ливинги 3.0»: модульные ульи.\n- «Бэй-логистика»: автономные паромы и трамваи.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Тех-регуляция",
            "body": "- «Этика имплантов»: местные нормы.\n- «Сейсмо-щиты»: новая инфраструктура безопасности.\n- «Купола тумана»: климат-контроль побережья.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Тихоокеанский мост",
            "body": "- «Сан-Франциско—Токио»: технологическая ось.\n- «Подземные центры»: серверы в скале.\n- «Креативный экспорт»: стартап-культура BD.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Свободный город?",
            "body": "- «Городской DAO»: децентрализованное управление.\n- «Нулевые выбросы»: тотальная декарбонизация.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет залива SF",
            "body": "- Экспорт протоколов тех-столиц, экологического и DAO-управления.\n- Сан-Франциско закрепляется как глобальный эксперимент тех-цивилизации.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Долина AI, сейсмо-щиты, купола тумана, городской DAO, нулевые выбросы.\n- Сюжеты о стартапах, международных тех-альянсах и климатических инициативах.\n",
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
        "github_issue": 1281,
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
          "changes": "Конвертация авторских событий Сан-Франциско в структурированный YAML."
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