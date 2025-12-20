-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\events-lore\world-events-timeline.yaml
-- Generated: 2025-12-21T02:15:39.684669

BEGIN;

-- Lore: canon-narrative-world-events-timeline
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-world-events-timeline',
    'Временная шкала мировых событий NECPGAME',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-world-events-timeline",
    "title": "Временная шкала мировых событий NECPGAME",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T00:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "narrative_director",
        "contact": "narrative@necp.game"
      }
    ],
    "tags": [
      "timeline",
      "world-events",
      "league"
    ],
    "topics": [
      "story-chronology",
      "live-ops"
    ],
    "related_systems": [
      "narrative-service",
      "live-ops-service",
      "analytics-service"
    ],
    "related_documents": [
      {
        "id": "canon-narrative-events-lore-index",
        "relation": "references"
      },
      {
        "id": "mechanics-world-events-world-events-framework",
        "relation": "complements"
      },
      {
        "id": "canon-narrative-sid-system",
        "relation": "influences"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/events-lore/world-events-timeline.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "live_ops"
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
    "problem": "Временная шкала мировых событий была в Markdown и не связывалась с периодами лиг и метриками SID.",
    "goal": "Структурировать события по категориям (лор, авторские, MMO) и периодам лиг для дальнейшей интеграции.",
    "essence": "Документ объединяет события из канона Cyberpunk, авторские идеи и MMO-активности, задавая основу Live Ops.",
    "key_points": [
      "Корпоративные войны, космические и технологические события задают глобальный контекст.",
      "Авторские события добавляют солнечные вспышки, взлом Blackwall и войны с искинами.",
      "Уникальные MMO-события делятся на глобальные, фракционные и региональные активности.",
      "Каждая лига (2020–2090) имеет набор событий и влияний на экономику, технологии и фракции."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "lore_events",
        "title": "События из лора Cyberpunk",
        "body": "Описаны корпоративные войны Arasaka vs Militech, космические конфликты и технологические скачки,\nвлияющие на все лиги и выборы игроков.\n",
        "mechanics_links": [
          "canon/lore/_03-lore/factions/corporations/CORPORATE-POLITICS-MASTER-INDEX.yaml"
        ],
        "assets": []
      },
      {
        "id": "authored_events",
        "title": "Авторские события",
        "body": "Солнечные вспышки, взлом Blackwall и войны с искинами добавляют уникальные испытания, влияющие на импланты,\nсвязь и глобальную угрозу AI.\n",
        "mechanics_links": [
          "mechanics/combat/combat-cyberpsychosis.yaml"
        ],
        "assets": []
      },
      {
        "id": "mmo_events",
        "title": "Уникальные события для MMORPG",
        "body": "Регулярные глобальные, фракционные и региональные события формируют Live Ops-календари, от торговых блокад\nдо локальных кризисов.\n",
        "mechanics_links": [
          "mechanics/world/events/live-events-system.yaml"
        ],
        "assets": []
      },
      {
        "id": "league_timeline",
        "title": "Временная шкала по лигам",
        "body": "Периоды 2020–2090 описывают ключевые события, развитие технологий, эскалацию конфликтов и подготовку финала 2090–2093.\nМатериал синхронизируется с SID и сценарием основного сюжета.\n",
        "mechanics_links": [
          "canon/narrative/sid-endings/sid-system.yaml",
          "canon/narrative/main-story/framework.yaml"
        ],
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
    "needs_task": false,
    "github_issue": 67,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml#canon-narrative-world-events-timeline"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "narrative_team",
      "changes": "Временная шкала мировых событий структурирована для интеграции в Live Ops и SID."
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