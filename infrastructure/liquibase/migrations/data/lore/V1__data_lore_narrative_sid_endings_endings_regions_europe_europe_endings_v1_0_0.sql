-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\sid-endings\endings\regions\europe\europe-endings.yaml
-- Generated: 2025-12-14T16:03:09.050281

BEGIN;

-- Lore: canon-narrative-sid-region-europe-endings
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-sid-region-europe-endings',
    'SID регион Европа — концовки',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-sid-region-europe-endings",
    "title": "SID регион Европа — концовки",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "1.0.0",
    "last_updated": "2025-11-12T00:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "narrative_director",
        "contact": "narrative@necp.game"
      }
    ],
    "tags": [
      "sid",
      "endings",
      "europe"
    ],
    "topics": [
      "narrative",
      "branching"
    ],
    "related_systems": [
      "narrative-service"
    ],
    "related_documents": [
      {
        "id": "canon-narrative-sid-finale-navigation",
        "relation": "references"
      },
      {
        "id": "canon-narrative-sid-region-europe-cities",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/sid-endings/endings/regions/europe/europe-endings.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "systems-design"
    ],
    "risk_level": "medium"
  },
  "review": {
    "chain": [
      {
        "role": "narrative_director",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Европейские финалы SID были описаны в Markdown и не соответствовали стандарту знаний.",
    "goal": "Зафиксировать шесть сценариев региона с условиями активации, влиянием на ключевые города и титрами.",
    "essence": "Документ раскрывает альтернативные будущие Европы от утопии и империи до распада, киберпанк-дистопии, зелёной революции и балканизации.",
    "key_points": [
      "Каждая ветвь имеет набор показателей `prosperity`, `stability`, `tech_level`, `ecology_level` и `political_fragmentation`.",
      "Сценарии описывают судьбы города-лидеров и социально-экономические эффекты.",
      "Финалы влияют на стартовые условия следующей лиги и миграционные потоки."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "utopia",
        "title": "E1 Европейская утопия",
        "body": "Высокие показатели процветания, стабильности и свободы создают синтез социального государства и технологий с низким\nнеравенством и культурным расцветом.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "empire",
        "title": "E2 Европейская империя",
        "body": "Объединённая милитаризированная Европа превращается в экспансионистскую империю, усиливая корпорации и вооружённые силы,\nвступая в конфликты с другими блоками.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "collapse",
        "title": "E3 Распад Европы",
        "body": "Кризис стабильности и процветания приводит к распаду ЕС, миграционному хаосу и локальным войнам, возвращая национализм\nи экономический спад.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "cyberpunk",
        "title": "E4 Киберпанк Европа",
        "body": "Технологическая насыщенность и корпоративное доминирование порождают классическую киберпанк-дистопию с экстремальным\nнеравенством и подпольным сопротивлением.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "green",
        "title": "E5 Зеленая Европа",
        "body": "Регион делает ставку на экологию и возобновляемую энергетику, снижает зависимость от имплантов и строит устойчивые города-сад.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "balkanization",
        "title": "E6 Балканизация",
        "body": "Высокая фрагментация рождает десятки городов-государств, что приводит к торговым войнам, нестабильности и постоянным\nлокальным конфликтам.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      {
        "title": "Глобальная система концовок SID",
        "link": "../../README.yaml"
      },
      {
        "title": "Финал 2090-2093",
        "link": "../../../periods/finale-2090-2093/README.yaml"
      },
      {
        "title": "Концовки городов Европы",
        "link": "../../cities/"
      }
    ],
    "decisions": []
  },
  "implementation": {
    "github_issue": 133,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "1.0.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Сценарии европейских финалов перенесены в YAML и дополнены параметрами влияния на регион."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    1
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;