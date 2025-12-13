-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\cis-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.594529

BEGIN;

-- Lore: canon-lore-regions-cis-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-regions-cis-2020-2093',
    'СНГ 2020–2093 — авторские события',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-lore-regions-cis-2020-2093",
    "title": "СНГ 2020–2093 — авторские события",
    "document_type": "canon",
    "category": "timeline-author",
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
      "cis",
      "timeline"
    ],
    "topics": [
      "timeline-author",
      "logistics",
      "sovereignty"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-2045-2060-author-events",
        "relation": "references"
      },
      {
        "id": "canon-lore-2060-2077-author-events",
        "relation": "references"
      },
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/cis-2020-2093.md",
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
    "problem": "Региональный таймлайн СНГ оставался в Markdown и не был привязан к механикам северных караванов и купольных хартии.",
    "goal": "Зафиксировать ключевые эпохи региона для сюжетов, караванных событий и параметров перезапуска мира.",
    "essence": "Постсоветские города эволюционируют в сеть купольных республик, управляющих экстремальной логистикой и кодексами ремесленников.",
    "key_points": [
      "Свободные порты и корпоративные архологии формируют экономику 2020-х.",
      "Подземная логистика и кибер-казачьи артели защищают маршруты 2030-х.",
      "«Тёплые коридоры» Сибири и мастерские совместимости определяют Red+ эпоху.",
      "Купольные хартии и кодекс ремесленника задают социальные нормы 2060-х.",
      "Советы параметров и ледяные архивы готовят регион к перезапуску лиг."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "timeline_2020_2029",
        "title": "2020–2029 — свободные порты и трансконтинентальные артерии",
        "body": "- «СПб Фрипорт»: свободная зона импорта имплантов под управлением городских DAO.\n- «Москва Архология»: корпоративный анклав с многоуровневой идентификацией и серыми слоями.\n- «ЦАР-Трубоплеты»: кланы, охраняющие и саботирующие энергетические артерии (PvPvE-сценарии).\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2030_2039",
        "title": "2030–2039 — карта подземки и кибер-казачьи артели",
        "body": "- Подпольные линии высокоскоростной логистики («Карта МТЛ»).\n- Кибер-казачьи артели с кодексами чести и контрактами охраны.\n- Алтайские смарт-убежища с оффлайн-пакетами.\n",
        "mechanics_links": [
          "mechanics/world/events/live-events-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2040_2060",
        "title": "2040–2060 — Red+: сети и зимние ярмарки",
        "body": "- Сибирские «тёплые коридоры» как сезонные окна обмена через Blackwall.\n- Минские нейро-ремесленные мастерские адаптеров совместимости.\n- Черноморские рынки оффлайн-пакетов.\n",
        "mechanics_links": [
          "mechanics/economy/economy-logistics.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2061_2077",
        "title": "2061–2077 — купольные республики и кодексы",
        "body": "- Купольные хартии Казани, Новосибирска и Алматы.\n- Кодекс ремесленника, защищающий права рипдоков и инженеров.\n- Северные автономные конвои на сверхдальних дистанциях.\n",
        "mechanics_links": [
          "mechanics/economy/economy-contracts.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2078_2093",
        "title": "2078–2093 — параметр-советы и наследие",
        "body": "- Советы параметров, формирующие пакеты правил для перезапусков.\n- Ледяные архивы для сохранения истории и телеметрии.\n- Крестовые караваны и фестивали обмена между лигами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "mechanics_and_hooks",
        "title": "Ключевые механики и хуки",
        "body": "- Зимние окна обмена, кодексы ремесленников, экстремальные караваны, переносимые архивы.\n- Хуки: охрана каравана через тёплый коридор, суд за нарушение кодекса, миссии в подземной логистике.\n",
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
      "timeline-author/2045-2060-author-events.md",
      "timeline-author/2060-2077-author-events.md"
    ],
    "decisions": []
  },
  "implementation": {
    "needs_task": false,
    "github_issue": 73,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml#canon-lore-regions-cis-2020-2093"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Конвертирован региональный таймлайн СНГ в YAML и связан с механиками купольных республик и караванов."
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