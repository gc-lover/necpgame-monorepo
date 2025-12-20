-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\europe\cities\budapest-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.515958

BEGIN;

-- Lore: canon-lore-europe-budapest-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-europe-budapest-2020-2093',
    'Будапешт — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-europe-budapest-2020-2093",
    "title": "Будапешт — авторские события 2020–2093",
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
      "budapest"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/europe/cities/budapest-2020-2093.md",
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
    "problem": "Markdown-файл Будапешта не связывал дунайскую логистику, V4-стандарты и культурный экспорт.",
    "goal": "Структурировать эволюцию Будапешта как дунайского протокольного хаба.",
    "essence": "Будапешт объединяет мосты-ретрансляторы, подземные архивы и гастро-BD, экспортируя «пакет Дуная».",
    "key_points": [
      "Этапы от дунайского узла до регионального центра и экспорта речных протоколов.",
      {
        "Хуки": "парламент-серверы, V4-стандарты, исторические купола, подземные архивы, гастро-BD."
      },
      "Подготовлены сцены для логистики Дуная, культурных фестивалей и тех-оси Венгрия—Австрия—Чехия."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Дунайский узел",
        "body": "- «Мосты-ретрансляторы»: связи Буды и Пешта.\n- «Купальни AR»: термальные комплексы в цифровом исполнении.\n- «Парламент-серверы»: государственные дата-центры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Центральная ось",
        "body": "- «V4-стандарты»: регуляторные протоколы для региона.\n- «Дунай-логистика»: речные коридоры.\n- «Метро-расширение»: подземные магистрали.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Культурная крепость",
        "body": "- «Исторические купола»: защита центра.\n- «Подземные архивы»: хранилища памяти.\n- «Гастро-BD»: кухня как медиа.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Региональный центр",
        "body": "- «Венгрия—Австрия—Чехия»: технологическая ось.\n- «Культурный экспорт»: дунайский неон.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет Дуная",
        "body": "- Экспорт протоколов речных мегаполисов и культурных связей.\n- Будапешт становится эталоном дунайской логистики и культуры.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Парламент-серверы, мосты-ретрансляторы, V4-стандарты, исторические купола, гастро-BD.\n- Сюжеты о Дунае, тех-оси и культурном экспорте.\n",
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
    "github_issue": 1249,
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
      "changes": "Конвертация авторских событий Будапешта в структурированный YAML."
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