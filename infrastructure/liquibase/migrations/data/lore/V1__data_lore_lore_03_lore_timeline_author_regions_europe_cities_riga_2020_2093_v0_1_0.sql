-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\riga-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.565200

BEGIN;

-- Lore: canon-lore-europe-riga-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-riga-2020-2093',
    'Рига — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-riga-2020-2093",
    "title": "Рига — авторские события 2020–2093",
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
      "riga"
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
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/riga-2020-2093.md",
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
    "problem": "Рига в Markdown не описывала балтийские коридоры, кибер-янтарь и роль города как восточного форпоста ЕС.",
    "goal": "Структурировать развитие Риги в YAML, подчеркнув логистику Даугавы, балтийскую федерацию и экспорт «пакета янтаря».",
    "essence": "Рига объединяет даугавские хабы, кибер-янтарь и балтийские коридоры, защищая восточную периферию ЕС.",
    "key_points": [
      "Этапы от балтийского узла до экспортёра протоколов малых наций.",
      {
        "Хуки": "даугавские хабы, балтийская тройка, кибер-янтарь, подземные бункеры, прибалтийский союз."
      },
      "Сюжеты о логистике, финансовых потоках и культурной идентичности Балтики."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Балтийский узел",
        "body": "- «Даугава-хабы»: речная логистика и оффлайн-пакеты.\n- «Юглас AR»: цифровизация районов.\n- «Рижский порт»: контроль балтийских маршрутов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Балтийская тройка",
        "body": "- «Рига—Таллин—Вильнюс сеть»: интеграция столиц.\n- «Старый город купола»: защита архитектуры.\n- «IT-аутсорсинг»: региональная платформа.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Восточный форпост ЕС",
        "body": "- «Балтийские коридоры»: связь городов побережья.\n- «Кибер-янтарь»: валюта данных и доверия.\n- «Подземные бункеры»: наследие холодной войны.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Балтийская федерация",
        "body": "- «Прибалтийский союз»: политическое объединение.\n- «Культурный экспорт»: балтийская эстетика и медиа.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет янтаря",
        "body": "- Экспорт протоколов малых наций и механизмов цифровой суверенитетности.\n- Рига закрепляется как координатор северо-восточного побережья ЕС.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Даугавские хабы, балтийская тройка, кибер-янтарь, подземные бункеры, прибалтийский союз.\n- Сюжеты о логистике, финансовых протоколах и культурной интеграции Балтики.\n",
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
    "github_issue": 1245,
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
      "changes": "Конвертация авторских событий Риги в структурированный YAML."
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