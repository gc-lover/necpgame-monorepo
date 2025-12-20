-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\sid-endings\periods\2025-2030-foundation.yaml
-- Generated: 2025-12-21T02:15:39.864915

BEGIN;

-- Lore: canon-narrative-sid-period-2025-2030-foundation
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-sid-period-2025-2030-foundation',
    'SID период 2025-2030 — формирование фундамента',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-sid-period-2025-2030-foundation",
    "title": "SID период 2025-2030 — формирование фундамента",
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
      "timeline"
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
        "id": "canon-narrative-sid-period-2020-2025-early-choices",
        "relation": "references"
      },
      {
        "id": "canon-narrative-sid-period-2085-2090-world-on-edge",
        "relation": "precedes"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/sid-endings/periods/2025-2030-foundation.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "systems-design"
    ],
    "risk_level": "high"
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
    "problem": "Период 2025-2030 с ключевыми ветвлениями существовал вне структурированной формы.",
    "goal": "Описать консолидацию сил после стартовых выборов и зафиксировать параметры, определяющие глобальные союзы и технологические прорывы.",
    "essence": "Документ раскрывает развитие доминирующих фракций, эскалацию корпоративного конфликта, распределение технологий и формирование региональных блоков.",
    "key_points": [
      "Доминирующие силы расширяются мирно, агрессивно или через децентрализацию, влияя на неравенство и сопротивление.",
      "Конфликт Arasaka vs Militech может стать глобальным, локализованным или перейти в холодную фазу.",
      "Технологические ветви и региональные союзы задают будущие финалы и подготовку ключевых NPC."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "context",
        "title": "Контекст периода",
        "body": "Мир закрепляет итоги ранних решений: фракции укрепляют власть, регионы ищут союзников, технологии начинают диктовать\nновую норму.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "dominant_forces",
        "title": "Расширение доминирующих сил",
        "body": "Корпорации, банды или независимые сети получают один из четырёх сценариев экспансии, изменяющих показатели экономики,\nсопротивления и структуры власти.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "global_conflict",
        "title": "Глобальный масштаб конфликта",
        "body": "Исход ранней войны определяет, станет ли конфликт глобальным, локальным или перейдёт в дипломатическую холодную стадию.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "tech_breakthroughs",
        "title": "Технологические прорывы",
        "body": "Импланты, ИИ, оружие или сбалансированное развитие корректируют показатели киберпсихоза, безработицы и темпы прогресса.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "regional_alliances",
        "title": "Региональные союзы",
        "body": "Формируются азиатский и западный блоки, возможна фрагментация или редкий глобальный союз, что меняет влияние регионов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "npc_progression",
        "title": "Судьбы ключевых NPC",
        "body": "Marco \"Fix\" Sanchez, José \"Tigre\" Ramirez и Hiroshi Tanaka получают развилки развития, стагнации или падения, влияющие\nна их роли в будущих концовках.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "thresholds",
        "title": "Пороговые события и точки невозврата",
        "body": "Захват районов, публичные технологические релизы и формирование союзов служат триггерами для глобальных изменений,\nфиксируя структуру мира к 2030 году.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "finale_impact",
        "title": "Влияние на финал",
        "body": "Результаты периода направляют лигу к сценариям «Выжженная земля», «Биполярный мир», «Множественные центры силы» или\n«Технократия».\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      {
        "title": "Период 2030-2035",
        "link": "./2030-2035-divergence-start.md"
      },
      {
        "title": "Региональные концовки",
        "link": "../endings/regions/"
      },
      {
        "title": "Концовки NPC",
        "link": "../endings/npcs/"
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
      "changes": "Период 2025-2030 оформлен в YAML, структурированы ветви расширения, технологии и региональные союзы."
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