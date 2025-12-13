-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\havana-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.385948

BEGIN;

-- Lore: canon-lore-america-havana-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-havana-2020-2093',
    'Гавана — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-havana-2020-2093",
    "title": "Гавана — авторские события 2020–2093",
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
      "havana"
    ],
    "topics": [
      "timeline-author",
      "island"
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
        "id": "github-issue-1290",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1290",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:25:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/havana-2020-2093.md",
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
    "problem": "Markdown-вариант Гаваны не связывал ретро-эстетику, пиратские сети и климатическую защиту в единой структуре.",
    "goal": "Зафиксировать этапы превращения островной столицы в карибский офшор культуры и данных.",
    "essence": "Гавана комбинирует неоновый Малекон, пиратские своды и карибскую федерацию, создавая «пакет свободы».",
    "key_points": [
      "Выделены пять эпох от ретро-имплантов до экспорта островного управления.",
      {
        "Хуки": "карибская сеть, ром-биореакторы, климат-купола, пиратские своды, офшорный центр."
      },
      "Обеспечены опорные сюжеты для карибских квестов, музыкальных BD и хакерских анклавов."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Открытие и ностальгия",
        "body": "- «Малекон неон»: ретро-набережная с AR-слоями.\n- «Ретро-импланты»: винтажный киберпанк стиль.\n- «Карибская сеть»: интеграция островных данных.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Островная федерация",
        "body": "- «Карибский союз»: политическое объединение регионов.\n- «Ром-биореакторы»: синтез традиций и технологий.\n- «Подводные расширения»: противостояние затоплению.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Тропический рай",
        "body": "- «Климат-купола»: защита от ураганов.\n- «Музыкальные BD»: цифровые сальса и регги.\n- «Пиратские своды»: легализованные карибские хакеры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Карибская столица",
        "body": "- «Офшорный центр»: криптовалюты и дата-хабы.\n- «Культурный экспорт»: карибская эстетика в мире.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет свободы",
        "body": "- Экспорт протоколов островного управления и свободных рынков.\n- Гавана закрепляется как столица карибского сетевого пространства.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Малекон неон, ретро-импланты, пиратские своды, карибский союз, офшорный центр.\n- Сюжеты о карибской федерации, хакерах и музыкальных BD.\n",
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
    "github_issue": 1290,
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
      "changes": "Конвертация авторских событий Гаваны в структурированный YAML."
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