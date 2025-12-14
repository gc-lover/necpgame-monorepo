-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\america\cities\vancouver-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.597409

BEGIN;

-- Lore: canon-lore-america-vancouver-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-america-vancouver-2020-2093',
    'Ванкувер — авторские события 2020–2093',
    'canon',
    'lore',
    '{
  "metadata": {
    "id": "canon-lore-america-vancouver-2020-2093",
    "title": "Ванкувер — авторские события 2020–2093",
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
      "vancouver"
    ],
    "topics": [
      "timeline-author",
      "eco-tech",
      "sovereignty"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-america-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-asia-2020-2093",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/america/cities/vancouver-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "worldbuilding",
      "live_ops"
    ],
    "risk_level": "low"
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
    "problem": "Ванкуверский таймлайн оставался в Markdown и не использовался в системе региональных параметров.",
    "goal": "Структурировать события города для интеграции с сюжетами Тихоокеанского узла и каскадийских линий.",
    "essence": "Ванкувер развивается как нейтральный экологический узел, балансирующий между азиатскими связями и каскадийским альянсом.",
    "key_points": [
      "В 2020-х город становится тихоокеанскими воротами с AR-заповедниками и оффлайн-портами.",
      "2030-е закрепляют эко-статус через вертикальные леса и дождевые генераторы.",
      "В эпоху Red+ Ванкувер служит трансокеанским узлом и нейтральным убежищем.",
      "2060-е приносят каскадийскую республику и союзы западного побережья.",
      "К 2080-м город экспортирует эко-протоколы как «пакет баланса»."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "timeline_2020_2029",
        "title": "2020–2029 — тихоокеанские ворота",
        "body": "- «Азиато-Канадский мост» укрепляет связи с мегаполисами Азии.\n- «Стэнли-Парк 2.0» становится био-заповедником с AR-слоями.\n- Морские порты обеспечивают оффлайн-логистику через Тихий океан.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2030_2039",
        "title": "2030–2039 — эко-мегаполис",
        "body": "- «Зелёный Ванкувер» позиционируется как самый экологичный город Америк.\n- Вертикальные леса превращают здания в экосистемы.\n- Дождевые генераторы обеспечивают энергию из постоянных осадков.\n",
        "mechanics_links": [
          "mechanics/world/events/live-events-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2040_2060",
        "title": "2040–2060 — Red+: тихоокеанский узел",
        "body": "- Подводные кабели связывают Ванкувер с азиатскими хабами.\n- Коренные протоколы интегрируют традиции First Nations в управление куполом.\n- «Остров Убежище» закрепляет статус нейтральной зоны.\n",
        "mechanics_links": [
          "mechanics/economy/economy-logistics.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2061_2077",
        "title": "2061–2077 — каскадийская республика",
        "body": "- Союз с Сиэтлом формирует каскадийскую повестку.\n- Тихоокеанская федерация объединяет западное побережье против внешнего давления.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2078_2093",
        "title": "2078–2093 — пакет баланса",
        "body": "- Ванкувер экспортирует эко-технологические протоколы в новые лиги.\n- Город служит тестовой площадкой параметров устойчивости.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "mechanics_and_hooks",
        "title": "Ключевые механики и хуки",
        "body": "- Азиато-канадский мост, вертикальные леса, коренные протоколы, каскадийский союз, остров-убежище.\n- Сценарии: защита трансокеанского кабеля, медиа-кампания каскадийской федерации, дипломатия с коренными советами.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      "regions/america-2020-2093.md",
      "regions/asia-2020-2093.md"
    ],
    "decisions": []
  },
  "implementation": {
    "needs_task": false,
    "github_issue": 73,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml#canon-lore-america-vancouver-2020-2093"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Конвертирован таймлайн Ванкувера в YAML и привязан к каскадийским сюжетам."
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