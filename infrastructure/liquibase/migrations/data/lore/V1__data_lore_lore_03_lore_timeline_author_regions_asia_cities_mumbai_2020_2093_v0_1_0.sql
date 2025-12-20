-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\asia\cities\mumbai-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.380500

BEGIN;

-- Lore: canon-lore-asia-mumbai-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-asia-mumbai-2020-2093',
    'Мумбаи — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-asia-mumbai-2020-2093",
    "title": "Мумбаи — авторские события 2020–2093",
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
      "mumbai"
    ],
    "topics": [
      "timeline-author",
      "finance"
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
        "id": "github-issue-1266",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1266",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T03:55:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/asia/cities/mumbai-2020-2093.md",
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
    "problem": "Хронология Мумбаи была в Markdown и не связывала финансовые, культурные и климатические арки.",
    "goal": "Оформить мегаполис как финансовую столицу с духовным капитализмом и адаптацией к подъёму воды.",
    "essence": "Мумбаи сочетает Далал-стрит, Болливуд и плавучие кварталы, чтобы экспортировать «пакет гармонии».",
    "key_points": [
      "Этапы от финансовой столицы до духовного капитализма.",
      {
        "Выделены хуки": "Дхарави 2.0, Филм-Сити 3.0, биолюминесцентный залив, карма-рейтинг."
      },
      "Сформирована база для сюжетов о духовном капитализме и климатической адаптации."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Финансовая столица Индии",
        "body": "- «Далал-стрит»: AI-трейдинг и финансовые кластеры.\n- «Дхарави 2.0»: серый рынок имплантов в трущобах.\n- «Ворота Индии»: порт для оффлайн-пакетов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Болливуд-имперум",
        "body": "- «Филм-сити 3.0»: глобальное BD-производство.\n- «Морской драйв неон»: набережная технологий и развлечений.\n- «Подводные расширения»: искусственные острова в заливе.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Город на воде",
        "body": "- «Плавучие кварталы»: адаптация к подъёму моря.\n- «Мумбаи-мосты»: сеть мостов между островами-куполами.\n- «Биолюминесцентный залив»: эко-технологии очистки воды.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Дхарма-капитализм",
        "body": "- «Духовный капитал»: синтез философии и бизнеса.\n- «Карма-рейтинг»: социальная система на индийских концептах.\n- «Ашрам-корпорации»: корпорации с духовными практиками.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет гармонии",
        "body": "- Экспорт протоколов синтеза духовности и технологий.\n- Мумбаи становится символом духовного капитализма.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Дхарави 2.0, Филм-сити 3.0, плавучие кварталы, карма-рейтинг, ашрам-корпорации.\n- Сюжеты о духовном капитализме и климатической адаптации.\n",
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
    "github_issue": 1266,
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
      "changes": "Конвертация авторских событий Мумбаи в структурированный YAML."
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