-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis\cities\yerevan-2020-2093.yaml
-- Generated: 2025-12-14T16:03:08.733823

BEGIN;

-- Lore: canon-region-cis-yerevan-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-region-cis-yerevan-2020-2093',
    'Ереван 2020-2093 — Высотный техно-анклав',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-region-cis-yerevan-2020-2093",
    "title": "Ереван 2020-2093 — Высотный техно-анклав",
    "document_type": "canon",
    "category": "timeline-author",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-23T04:05:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "lore_analyst",
        "contact": "lore@necp.game"
      }
    ],
    "tags": [
      "cis",
      "yerevan"
    ],
    "topics": [
      "regional-history",
      "diaspora-networks"
    ],
    "related_systems": [
      "narrative-service"
    ],
    "related_documents": [
      {
        "id": "canon-region-cis-index",
        "relation": "references"
      },
      {
        "id": "github-issue-1254",
        "title": "GitHub Issue",
        "link": "https://github.com/gc-lover/necpgame-monorepo/issues/1254",
        "relation": "migrated_to",
        "migrated_at": "2025-11-23T04:05:00+00:00"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis/cities/yerevan-2020-2093.md",
    "visibility": "internal",
    "audience": [
      "lore",
      "narrative"
    ],
    "risk_level": "low"
  },
  "review": {
    "chain": [
      {
        "role": "lore_lead",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "История Еревана 2020-2093 была в Markdown и отсутствовала в единой базе знаний.",
    "goal": "Перенести ключевые вехи развития города в YAML для дальнейшего использования в сценариях.",
    "essence": "Ереван превращается в высотный техно-анклав с сетью диаспор и нейросетевыми университетами.",
    "key_points": [
      {
        "2030-е": "рост высотных кампусов и возвращение диаспоры."
      },
      {
        "2050-е": "создание университета нейросетей и торгового хаба дронов."
      },
      {
        "2090-е": "формирование независимого техно-сити с глобальными связями."
      }
    ]
  },
  "content": {
    "sections": [
      {
        "id": "timeline_2020s",
        "title": "2020–2029 — Возвращение диаспоры",
        "body": "Ереван запускает программы для возвращения специалистов диаспоры.\nПоявляются первые высотные кампусы и технопарки.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2030s",
        "title": "2030–2039 — Технопарк и надстройки",
        "body": "Город строит надстройки поверх старых кварталов, создавая многоуровневые улицы и высотные ангары.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2040s",
        "title": "2040–2049 — Климатические станции",
        "body": "На горах устанавливают климатические станции для защиты от песчаных бурь.\nРазвиваются солнечные фермы.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2050s",
        "title": "2050–2059 — Университет нейросетей",
        "body": "Основан университет нейросетей и дроновой логистики, привлекающий инвесторов со всего мира.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2060s",
        "title": "2060–2069 — Торговый хаб",
        "body": "Ереван становится хабом для торговли дронами и энергоносителями по коридору Кавказ—Ближний Восток.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2070s",
        "title": "2070–2079 — Техно-анклавы",
        "body": "В городе формируются закрытые техно-анклавы, управляемые диаспорными советами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2080s",
        "title": "2080–2089 — Нейроинфраструктура",
        "body": "Широкое внедрение нейроинтерфейсов делает Ереван центром исследования памяти и идентичности.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "timeline_2090s",
        "title": "2090–2093 — Глобальный узел",
        "body": "Город создаёт сеть независимых высотных городов, связанных с Лос-Анджелесом, Дубаями и Токио.\n",
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
    "github_issue": 1254,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Сформирована хронология Еревана с акцентом на диаспоры, высотные комплексы и нейросетевые инициативы."
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