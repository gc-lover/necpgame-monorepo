-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\africa\cities\lagos-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.352699

BEGIN;

-- Lore: canon-lore-africa-lagos-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-africa-lagos-2020-2093',
    'Лагос — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-africa-lagos-2020-2093",
    "title": "Лагос — авторские события 2020–2093",
    "document_type": "canon",
    "category": "lore",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:20:00+00:00",
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
      "lagos"
    ],
    "topics": [
      "timeline-author",
      "megacity"
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
        "id": "github-issue-1295",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1295",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:20:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/africa/cities/lagos-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "worldbuilding",
      "live_ops"
    ],
    "risk_level": "high"
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
    "problem": "Лагос в Markdown не связывал мегатрущобы, BD-Нолливуд и островные расширения в единую структуру знаний.",
    "goal": "Сформировать хронологию мегаполиса как столицу импровизации, культуры и выживания.",
    "essence": "Лагос развивает корпоративный анклав, BD-индустрию и плавучие районы, экспортируя «пакет выживания».",
    "key_points": [
      "Этапы от нигерийского колосса до западноафриканской столицы и экспортёра адаптивных протоколов.",
      {
        "Хуки": "серые мегатрущобы, BD-Нолливуд, плавучий Лагос, ЭКОВАС-серверы, кибер-афробит."
      },
      "Сценарии о борьбе за ресурсы, креативных трущобах и культурном экспорте Африки."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Нигерийский колосс",
        "body": "- «Виктория-айленд анклав»: корпоративный рай среди хаоса.\n- «Серые мегатрущобы»: крупнейшие нелегальные рынки имплантов.\n- «Лагос-порт»: западноафриканские ворота.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Нолливуд 2.0",
        "body": "- «BD-Нолливуд»: африканская BD-индустрия.\n- «Трущобы инноваций»: технологии из ничего.\n- «Транс-сахарские маршруты»: связь с севером.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Островные расширения",
        "body": "- «Плавучий Лагос»: защита от затопления.\n- «Кибер-афробит»: музыкальная революция.\n- «Энергетические войны»: нефть против солнца.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Западноафриканская столица",
        "body": "- «ЭКОВАС-серверы»: региональная интеграция.\n- «Африканский стиль»: культурный экспорт.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет выживания",
        "body": "- Экспорт протоколов импровизации, адаптации и культурного влияния.\n- Лагос становится главной лабораторией мегаполиса выживания.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Серые мегатрущобы, BD-Нолливуд, плавучий Лагос, трущобы инноваций, кибер-афробит.\n- Сюжеты о борьбе за ресурсы, культурном экспорте и адаптации мегаполиса.\n",
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
    "github_issue": 1295,
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
      "changes": "Конвертация авторских событий Лагоса в структурированный YAML."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    0
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;