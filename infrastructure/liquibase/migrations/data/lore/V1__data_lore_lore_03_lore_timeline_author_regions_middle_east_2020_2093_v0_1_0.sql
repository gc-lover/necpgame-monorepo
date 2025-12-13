-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: lore\_03-lore\timeline-author\regions\middle-east-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.720493

BEGIN;

-- Lore: canon-lore-regions-middle-east-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-lore-regions-middle-east-2020-2093',
    'Ближний Восток 2020–2093 — авторские события',
    'canon',
    'timeline-author',
    '{
  "metadata": {
    "id": "canon-lore-regions-middle-east-2020-2093",
    "title": "Ближний Восток 2020–2093 — авторские события",
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
      "middle-east",
      "timeline"
    ],
    "topics": [
      "timeline-author",
      "geo-politics",
      "energy"
    ],
    "related_systems": [
      "narrative-service",
      "world-service",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "canon-lore-regions-europe-2020-2093",
        "relation": "complements"
      },
      {
        "id": "canon-lore-factions-nomads-2020-2093",
        "relation": "references"
      },
      {
        "id": "canon-lore-2060-2077-author-events",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/regions/middle-east-2020-2093.md",
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
    "problem": "Региональный таймлайн Ближнего Востока существовал в Markdown и не был связан с механиками водных войн и энергетических картелей.",
    "goal": "Зафиксировать ключевые эпохи региона для авторских событий, Live Ops и сюжетных арок.",
    "essence": "Ближний Восток превращается из нефтяного центра в сеть дата-оазисов и энергетических картелей, определяющих баланс лиг.",
    "key_points": [
      "Переход от нефтяной экономики к дата-центрам и смарт-городам.",
      "Климaт-купола, водные контракты и пустынные номадские сети как новые системы власти.",
      "Энергетические картели и прокси-конфликты формируют экономику 2060–2070-х.",
      "Архивы цивилизаций и экспорт солнечной энергии задают повестку 2080-х."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "timeline_2020_2029",
        "title": "2020–2029 — нефть к данным и смарт-города",
        "body": "- «Дубай Вертикаль»: мегадата-центр и киберспортивные арены, задающие стандарт элитных услуг.\n- «Эр-Рияд Трансформация»: госпрограммы перехода от нефти к дата-экономике.\n- «Тель-Авив Стартапы»: синергия военных технологий и кибер-стартапов.\n- «Стамбул Мост»: транзитный хаб между Европой и Азией.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2030_2039",
        "title": "2030–2039 — водные войны и климат-купола",
        "body": "- Корпоративные войны за опреснение и контроль водных ресурсов.\n- Климaт-купола Залива для элиты; пустынные номады управляют солнечными фермами.\n- Иерусалим становится нейтральной зоной архива религиозных и цифровых данных.\n",
        "mechanics_links": [
          "mechanics/economy/economy-logistics.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2040_2060",
        "title": "2040–2060 — Red+ и оазисы данных",
        "body": "- Автономные города-оазисы со своими сетями, защищённые песчаными штормами.\n- Кочевые бедуинские ретрансляторы связывают оффлайн-маршруты.\n- Персидский коридор служит оффлайн-артерией между куполами.\n",
        "mechanics_links": [
          "mechanics/world/events/live-events-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2061_2077",
        "title": "2061–2077 — нео-халифат данных и прокси-войны",
        "body": "- «Цифровая Умма» объединяет города через единые стандарты.\n- Энергетические картели контролируют солнечную генерацию как «новую нефть».\n- Прокси-конфликты и кибер-паломничества формируют сюжетные линии.\n",
        "mechanics_links": [
          "mechanics/economy/economy-energy.yaml"
        ],
        "assets": []
      },
      {
        "id": "timeline_2078_2093",
        "title": "2078–2093 — архивы цивилизаций и экспорт энергии",
        "body": "- Пустынные бункеры-хранилища культурного наследия.\n- Экспорт солнечной энергии и систем выживания в новые лиги.\n- Мета-религиозные суды о цифровом бессмертии задают конфликты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "mechanics_and_hooks",
        "title": "Ключевые механики и квестовые хуки",
        "body": "- Водные контракты, климат-купола, энергетические картели, бедуин-сети и песчаные штормы.\n- Хуки: корпоративные водные войны, защита караванов в буре, паломничество в цифровые архивы, саботаж энергетического картеля.\n",
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
      "timeline-author/2060-2077-author-events.md",
      "timeline-author/regions/europe-2020-2093.md",
      "timeline-author/factions/nomads-2020-2093.md"
    ],
    "decisions": []
  },
  "implementation": {
    "needs_task": false,
    "github_issue": 73,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml#canon-lore-regions-middle-east-2020-2093"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Конвертирован региональный таймлайн Ближнего Востока в YAML и связан с ключевыми механиками."
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