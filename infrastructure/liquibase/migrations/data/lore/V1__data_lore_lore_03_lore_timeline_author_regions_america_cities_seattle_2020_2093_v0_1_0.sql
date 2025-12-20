-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\seattle-2020-2093.yaml
-- Generated: 2025-12-21T02:15:39.304316

BEGIN;

-- Lore: canon-lore-america-seattle-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-seattle-2020-2093',
    'Сиэтл — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-seattle-2020-2093",
    "title": "Сиэтл — авторские события 2020–2093",
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
      "seattle"
    ],
    "topics": [
      "timeline-author",
      "innovation"
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
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/seattle-2020-2093.md",
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
    "problem": "Сиэтл в Markdown не объединял каскадийскую интеграцию, экотехнологии и сецессию НСША.",
    "goal": "Структурировать путь Сиэтла от изумрудного города до потенциальной каскадийской республики и пакета инноваций.",
    "essence": "Сиэтл соединяет ретрансляторы Спейс-Нидл, каскадийский союз и AI-лидерство, экспортируя «пакет инноваций».",
    "key_points": [
      "Этапы от изумрудного города 2.0 до сецессионного движения и экспорта технологических протоколов.",
      {
        "Хуки": "дождевые генераторы, подземный Сиэтл, азиато-американский мост, эко-технологии, сецессионное движение."
      },
      "Обеспечены сюжетные точки для каскадийской мечты, вулканической защиты и AI-биотех партнерства."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "era-2020-2029",
        "title": "2020–2029 — Изумрудный город 2.0",
        "body": "- «Спейс-Нидл ретранслятор»: узел связи.\n- «Корпоративные кампусы»: империи Amazon и Microsoft.\n- «Дождевые генераторы»: энергия из осадков.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2030-2039",
        "title": "2030–2039 — Каскадийская мечта",
        "body": "- «Сиэтл-Ванкувер союз»: трансграничная интеграция.\n- «Гранж-BD»: цифровая музыкальная культура.\n- «Подземный Сиэтл»: расширение исторических туннелей.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2040-2060",
        "title": "2040–2060 — Red+: Тихоокеанские ворота",
        "body": "- «Азиато-американский мост»: ключевой хаб.\n- «Вулканическая защита»: мониторинг Рейнира.\n- «Эко-технологии»: зелёная столица Америки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2061-2077",
        "title": "2061–2077 — Каскадийская республика?",
        "body": "- «Сецессионное движение»: независимость от НСША.\n- «Технологическое лидерство»: AI и биотех.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "era-2078-2093",
        "title": "2078–2093 — Пакет инноваций",
        "body": "- Экспорт протоколов технологического развития и экобаланса.\n- Сиэтл позиционируется как каскадийский эталон инноваций.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "hooks",
        "title": "Механики и хуки",
        "body": "- Спейс-Нидл ретранслятор, каскадийская мечта, подземный Сиэтл, сецессионное движение, эко-технологии.\n- Сюжеты о каскадийском союзе, AI-лидерстве и экологических инициативах.\n",
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
    "github_issue": 1278,
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
      "changes": "Конвертация авторских событий Сиэтла в структурированный YAML."
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