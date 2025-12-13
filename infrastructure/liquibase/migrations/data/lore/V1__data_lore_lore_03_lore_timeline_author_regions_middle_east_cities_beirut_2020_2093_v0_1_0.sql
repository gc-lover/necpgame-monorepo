-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east\cities\beirut-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.698722

BEGIN;

-- Lore: canon-lore-middle-east-beirut-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-middle-east-beirut-2020-2093',
    'Бейрут — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-middle-east-beirut-2020-2093",
    "title": "Бейрут — авторские события 2020–2093",
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
      "middle-east",
      "beirut"
    ],
    "topics": [
      "timeline-author",
      "diplomacy"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-middle-east-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east/cities/beirut-2020-2093.md",
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
    "problem": "История Бейрута в Markdown не связывала восстановление порта, мультиконфессиональные протоколы и нейтральную гавань.",
    "goal": "Оцифровать путь Бейрута к мультикультурной столице переговоров и экспортёру «пакета мозаики».",
    "essence": "Бейрут восстанавливает порт, строит культурные мосты и управляет нейтралитетом, предлагая миру мультикультурные протоколы.",
    "key_points": [
      "Этапы от восстановления до левантийского центра и экспорта много-конфессионального управления.",
      {
        "Хуки": "Порт 2.0, мульти-конфессиональные протоколы, бейрутский нейтралитет, подводные расширения, финансовый офшор."
      },
      "Подготовлены опоры для сюжетов о переговорных площадках, хактивистах и культурной дипломатии."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Восстановление и ренессанс",
        "body": "- «Порт 2.0»: восстановление после катастроф.\n- «Ливанские кедры AR»: цифровое наследие.\n- «Средиземноморские хабы»: оффлайн-логистика.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Мозаика культур",
        "body": "- «Мульти-конфессиональные протоколы»: сосуществование.\n- «Финансовый офшор»: криптовалютный центр.\n- «Горные бункеры»: защищённые архивы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Нейтральная гавань",
        "body": "- «Бейрутский нейтралитет»: площадка переговоров.\n- «Культурные мосты»: синтез Восток-Запад.\n- «Подводные расширения»: защита от затопления.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Левантийский центр",
        "body": "- «Средиземноморский альянс»: объединение побережья.\n- «Культурный экспорт»: левантийская эстетика и медиа.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет мозаики",
        "body": "- Экспорт протоколов мультикультурного управления и переговоров.\n- Бейрут закрепляется как нейтральная столица Средиземноморья.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Порт 2.0, мульти-конфессиональные протоколы, бейрутский нейтралитет, подводные расширения, финансовый офшор.\n- Сюжеты о дипломатии, культурных мостах и криптовалютных хабах.\n",
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
    "github_issue": 1239,
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
      "changes": "Конвертация авторских событий Бейрута в структурированный YAML."
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