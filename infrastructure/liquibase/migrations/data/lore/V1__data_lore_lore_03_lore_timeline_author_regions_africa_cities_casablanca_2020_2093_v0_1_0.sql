-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\casablanca-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.170755

BEGIN;

-- Lore: canon-lore-africa-casablanca-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-africa-casablanca-2020-2093',
        'Касабланка — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-africa-casablanca-2020-2093",
        "title": "Касабланка — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:10:00+00:00",
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
          "casablanca"
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
            "id": "canon-lore-regions-africa-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1302",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1302",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:10:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/casablanca-2020-2093.md",
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
        "problem": "Таймлайн Касабланки существовал в Markdown и не интегрировался с логистическими и культурными сценариями Магриба.",
        "goal": "Систематизировать эпохи города как атлантического хаба с гибридной нормативкой и штормозащитой.",
        "essence": "Касабланка укрепляет морскую инфраструктуру, цифровые медины и смешанные протоколы, экспортируя «пакет пролива».",
        "key_points": [
          "Зафиксированы этапы от магриб-хаба до экспорта портовых стандартов.",
          "Подчёркнуты шторм-щиты, порт-серверы и медина AR как ключевые хуки.",
          "Добавлена связка с энергетикой Сахары и трансатлантическими кабелями."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Магриб-хаб",
            "body": "- «Атлантический порт»: автономная логистика и дроны-швартовщики.\n- «Медина AR»: цифровые слои поверх исторических рынков.\n- «Франко-арабские протоколы»: смешанная нормативная база.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Североафриканский коридор",
            "body": "- «Касабланка—Танжер»: высокоскоростная тех-магистраль.\n- «Сахарские ветра»: ветряные фермы для энергосети.\n- «Евро-Африка кабели»: подводные линии данных.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Морская крепость",
            "body": "- «Шторм-щиты»: защита побережья от штормов и набегов.\n- «Порт-серверы»: дата-центры на причалах.\n- «Купола пыли»: фильтры против хамсина.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Магрибский узел",
            "body": "- «Культурный экспорт»: берберо-неон эстетика и медиа.\n- «Мост в Африку»: интеграция с западноафриканскими коридорами.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет пролива",
            "body": "- Экспорт протоколов атлантических портов и смешанных регуляций.\n- Расширение сетей кабелей и мер безопасности на глобальный уровень.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- Шторм-щиты, порт-серверы, медина AR, купола пыли.\n- Сюжеты о гибридной нормативке и трансатлантических кабелях.\n",
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
        "github_issue": 1302,
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
          "changes": "Конвертация авторских событий Касабланки в структурированный YAML."
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