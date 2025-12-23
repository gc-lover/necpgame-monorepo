-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\johannesburg-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.185163

BEGIN;

-- Lore: canon-lore-africa-johannesburg-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-africa-johannesburg-2020-2093',
        'Йоханнесбург — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-africa-johannesburg-2020-2093",
        "title": "Йоханнесбург — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:15:00+00:00",
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
          "johannesburg"
        ],
        "topics": [
          "timeline-author",
          "resources"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-africa-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1298",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1298",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:15:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/johannesburg-2020-2093.md",
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
        "problem": "Хронология Йоханнесбурга в Markdown не обеспечивала структурных связей с ресурсными и финансовыми сюжетами.",
        "goal": "Оцифровать трансформацию города от «золотого» мегаполиса до экспортёра протоколов ресурсной экономики.",
        "essence": "Йоханнесбург сочетает автономные шахты, климатические купола и афрофутуристическую культуру, создавая «пакет ресурсов».",
        "key_points": [
          "Этапы от автономной добычи до южноафриканской тех-оси.",
          "Подчёркнуты подземные центры, купола климата и финтех-хабы.",
          "Добавлены хуки для сюжетов о беспилотном транспорте и афрофутуризме."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Золотой город 2.0",
            "body": "- «Шахты 2.0»: автономная добыча редкоземов.\n- «Город-безопасность»: сети частной охраны.\n- «Соуэто AR»: культурные слои и туристический контент.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Пан-африканский узел",
            "body": "- «JHB финтех»: банковские протоколы и кросс-африканские расчёты.\n- «Водные коридоры»: инфраструктура для засушливых районов.\n- «Транспорт 3.0»: беспилотные магистрали.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Региональная крепость",
            "body": "- «Купола климата»: защита от жары и пыли.\n- «Подземные центры»: дата-центры в шахтах.\n- «Энерго-кластеры»: солнечные фермы для мегаполиса.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Южноафриканский хаб",
            "body": "- «Йоханнесбург—Кейптаун»: тех-ось совместных проектов.\n- «Культурный экспорт»: афрофутуризм и новые медиа.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет ресурсов",
            "body": "- Экспорт протоколов управления ресурсной экономикой.\n- Модели устойчивого развития для шахт и финтех-платформ.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Шахты 2.0, подземные центры, купола климата, афрофутуризм.\n- Сценарии о финтехе и беспилотных транспортных сетях.\n",
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
        "github_issue": 1298,
        "needs_task": false,
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
          "changes": "Конвертация авторских событий Йоханнесбурга в структурированный YAML."
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