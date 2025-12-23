-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\amsterdam-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.481957

BEGIN;

-- Lore: canon-lore-europe-amsterdam-2020-2093
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('canon-lore-europe-amsterdam-2020-2093',
        'Амстердам — авторские события 2020–2093',
        'canon',
        'lore',
        '{
      "metadata": {
        "id": "canon-lore-europe-amsterdam-2020-2093",
        "title": "Амстердам — авторские события 2020–2093",
        "document_type": "canon",
        "category": "lore",
        "status": "draft",
        "version": "0.1.0",
        "last_updated": "2025-11-23T04:05:00+00:00",
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
          "amsterdam"
        ],
        "topics": [
          "timeline-author",
          "bioethics"
        ],
        "related_systems": [
          "narrative-service",
          "world-service"
        ],
        "related_documents": [
          {
            "id": "canon-lore-regions-europe-2020-2093",
            "relation": "references"
          },
          {
            "id": "github-issue-1255",
            "title": "GitHub Issue",
            "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1255",
            "relation": "migrated_to",
            "migrated_at": "2025-11-23T04:05:00+00:00"
          }
        ],
        "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/amsterdam-2020-2093.md",
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
        "problem": "Markdown-описание Амстердама не связывало либертарианские реформы, биоэксперименты и роль города как гавани диссидентов.",
        "goal": "Структурировать развитие Амстердама как европейского центра биоэтики и DAO-управления.",
        "essence": "Амстердам объединяет канал-лаборатории, децентрализованное управление и гавань свободы, формируя «пакет свободы».",
        "key_points": [
          "Этапы от либертарианского купола до биоэтического центра и экспорта протоколов.",
          {
            "Хуки": "DAO-управление, дельта-хабы, подземные воды, комитет этики, гавань диссидентов."
          },
          "Сценарии о свободных технологиях, беглецах и европейских стандартах био-модификаций."
        ]
      },
      "content": {
        "sections": [
          {
            "id": "era-2020-2029",
            "title": "2020–2029 — Либертарианский купол",
            "body": "- «Зелёные каналы»: био-модификация при минимуме регуляций.\n- «Дельта-хабы»: плавучие дата-центры на каналах.\n- «Кофешопы нейро»: легальные точки тестирования имплантов.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2030-2039",
            "title": "2030–2039 — Био-Амстердам",
            "body": "- «Лаборатории каналов»: плавучие биолабы.\n- «DAO-управление»: полностью децентрализованная власть.\n- «Велосипеды+»: нейро-интерфейсные городские сети.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2040-2060",
            "title": "2040–2060 — Red+: Свободная гавань",
            "body": "- «Гавань свободы»: убежище для диссидентов и хакеров.\n- «Экспериментальная зона»: тестирование запрещённых технологий.\n- «Подземные воды»: скрытые дата-маршруты.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2061-2077",
            "title": "2061–2077 — Биоэтический центр",
            "body": "- «Комитет этики»: стандарты био-модификаций для Европы.\n- «Референдумы граждан»: нейро-голосование по ключевым решениям.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "era-2078-2093",
            "title": "2078–2093 — Пакет свободы",
            "body": "- Экспорт либертарианских протоколов управления и биоэтики.\n- Амстердам становится эталоном свободных городов Европы.\n",
            "mechanics_links": [],
            "assets": []
          },
          {
            "id": "hooks",
            "title": "Механики и хуки",
            "body": "- DAO-управление, дельта-хабы, подземные воды, комитет этики, гавань свободы.\n- Сюжеты о беглецах, биоэкспериментах и децентрализованном управлении.\n",
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
        "github_issue": 1255,
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
          "changes": "Конвертация авторских событий Амстердама в структурированный YAML."
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