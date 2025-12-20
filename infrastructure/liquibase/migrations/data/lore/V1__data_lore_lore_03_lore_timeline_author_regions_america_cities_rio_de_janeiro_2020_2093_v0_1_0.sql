-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\rio-de-janeiro-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.283919

BEGIN;

-- Lore: canon-lore-america-rio-de-janeiro-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-rio-de-janeiro-2020-2093',
    'Рио-де-Жанейро — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-rio-de-janeiro-2020-2093",
    "title": "Рио-де-Жанейро — авторские события 2020–2093",
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
      "america",
      "rio-de-janeiro"
    ],
    "topics": [
      "timeline-author",
      "coastline"
    ],
    "related_systems": [
      "narrative-service",
      "world-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/rio-de-janeiro-2020-2093.md",
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
    "problem": "Таймлайн Рио-де-Жанейро в Markdown не объединял береговую оборону, карнавальные технологии и лесные протоколы.",
    "goal": "Структурировать развитие Рио от города контрастов до экспортёра береговых решений.",
    "essence": "Рио соединяет фавелы AR, техно-карнавалы и шторм-щиты, формируя «пакет залива».",
    "key_points": [
      "Этапы от техно-карнавала до латино-хаба и экспорта береговых протоколов.",
      {
        "Хуки": "фавелы AR, карнавал BD, дроны-самбы, шторм-щиты, лесные протоколы."
      },
      "Готовые сюжетные точки для береговой обороны и культурного экспорта."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Город контрастов",
        "body": "- «Фавелы AR»: цифровые слои самоорганизации районов.\n- «Карнавал BD»: культурные мегасобытия с нейроинтерфейсами.\n- «Береговая защита»: первые ответные меры на шторма.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Техно-карнавал",
        "body": "- «Дроны-самбы»: автономные шоу во время парадов.\n- «Олимпийские хабы»: переиспользование олимпийской инфраструктуры.\n- «Нефтегаз Атлантики»: шельфовые проекты вдоль побережья.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Береговая крепость",
        "body": "- «Шторм-щиты»: оборонительная линия берегов.\n- «Подземные центры»: серверные в гранитных отрогах.\n- «Лесные протоколы»: восстановление Атлантического леса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Латино хаб",
        "body": "- «Рио—Сан-Паулу»: технологическая ось Бразилии.\n- «Культурный экспорт»: самба-неон и медиа-комплексы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет залива",
        "body": "- Экспорт протоколов береговых мегаполисов.\n- Рио позиционируется как главный консультант по прибрежной защите.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Фавелы AR, карнавал BD, шторм-щиты, дроны-самбы, лесные протоколы.\n- Сценарии о защите побережья и культурном влиянии.\n",
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
    "github_issue": 1282,
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
      "changes": "Конвертация авторских событий Рио-де-Жанейро в структурированный YAML."
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