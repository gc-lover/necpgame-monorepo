-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\vilnius-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.676292

BEGIN;

-- Lore: canon-lore-europe-vilnius-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-vilnius-2020-2093',
    'Вильнюс — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-vilnius-2020-2093",
    "title": "Вильнюс — авторские события 2020–2093",
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
      "vilnius"
    ],
    "topics": [
      "timeline-author",
      "culture"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/vilnius-2020-2093.md",
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
    "problem": "Markdown-версия Вильнюса не отражала объединение исторической памяти, фотонной индустрии и балтийских коридоров.",
    "goal": "Структурировать развитие столицы Литвы как культурного и образовательного хаба региона.",
    "essence": "Вильнюс сочетает Гедиминас AR, литовские лазеры и подземные архивы, экспортируя «пакет памяти».",
    "key_points": [
      "Этапы от исторической столицы до образовательного центра и экспорта культурных протоколов.",
      {
        "Хуки": "балтийский путь 2.0, литовские лазеры, подземные архивы, культурная автономия."
      },
      "Сюжеты о сохранении наследия, фотонной экономике и образовании Балтии."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Историческая столица",
        "body": "- «Гедиминас AR»: башня как ретранслятор.\n- «Балтийский путь 2.0»: историческая память в цифре.\n- «Литовские лазеры»: фотонная индустрия.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Балтийская интеграция",
        "body": "- «Балтийская тройка»: Литва—Латвия—Эстония.\n- «Вильнюс Tech Park»: стартап-экосистема.\n- «Старый город купола»: защита наследия.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Культурная крепость",
        "body": "- «Литовские протоколы»: культурная автономия.\n- «Подземные архивы»: память нации.\n- «Балтийские коридоры»: региональная сеть.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Образовательный хаб",
        "body": "- «Университеты Балтии»: региональное лидерство.\n- «Культурный экспорт»: балтийская эстетика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет памяти",
        "body": "- Экспорт протоколов сохранения культуры и образования.\n- Вильнюс закрепляется как архив Балтии и центр фотонной науки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Балтийский путь 2.0, литовские лазеры, подземные архивы, литовские протоколы, университеты Балтии.\n- Сюжеты о культурной памяти, образовании и фотонной индустрии.\n",
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
    "github_issue": 1242,
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
      "changes": "Конвертация авторских событий Вильнюса в структурированный YAML."
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