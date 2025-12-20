-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\sid-endings\sid-system.yaml
-- Generated: 2025-12-21T02:15:39.875838

BEGIN;

-- Lore: canon-narrative-sid-system
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-sid-system',
    'SID — Story Impact & Divergence',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-sid-system",
    "title": "SID — Story Impact & Divergence",
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
      "endings",
      "branching",
      "league-system"
    ],
    "topics": [
      "story-impact",
      "live-ops"
    ],
    "related_systems": [
      "narrative-service",
      "analytics-service",
      "live-ops-service"
    ],
    "related_documents": [
      {
        "id": "canon-narrative-scenarios-index",
        "relation": "references"
      },
      {
        "id": "mechanics-world-events-world-events-framework",
        "relation": "complements"
      },
      {
        "id": "mechanics-social-relationships-system",
        "relation": "influences"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/sid-endings/README.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "analytics"
    ],
    "risk_level": "critical"
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
    "problem": "Система множественных концовок SID была описана в Markdown и не была синхронизирована с сервисами лиг и аналитики.",
    "goal": "Формализовать принципы SID, структуру периодов и отслеживание метрик для глобальных титров лиги.",
    "essence": "SID агрегирует действия игроков, определяет пороги и ветвления и формирует концовки мира, регионов, фракций и NPC.",
    "key_points": [
      "Коллективное влияние игроков суммируется по фракционным, региональным, идеологическим и технологическим осям.",
      "Структура включает периоды 2020–2093, каталоги концовок и финальные титры.",
      "Отслеживаются глобальные, региональные и критические пороги с точками невозврата.",
      "Основной выход — персонализированные титры лиги с учётом вклада игроков."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Описание и вдохновение",
        "body": "SID объединяет опыт Cyberpunk 2077, Baldur's Gate 3, EVE Online, WoW и Kenshi/RimWorld. Цель — показать последствия\nдействий всех игроков в финале лиги и задать эмоциональное финальное шоу с глобальными титрами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "principles",
        "title": "Основные принципы",
        "body": "Коллективное влияние, множественные оси выбора, непредсказуемость и эмоциональные титры. Каждый выбор имеет последствия,\nа действия игроков суммируются в глобальный счёт.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "structure",
        "title": "Структура системы",
        "body": "Описаны временные периоды (2020–2093), каталоги концовок (регионы, фракции, города, NPC, глобальные финалы) и шаблоны титров.\nКаждый раздел связан с подкаталогами periods/, endings/ и credits/.\n",
        "mechanics_links": [
          "canon/narrative/sid-endings/endings/regions/europe/europe-endings.yaml"
        ],
        "assets": []
      },
      {
        "id": "tracking",
        "title": "Механики отслеживания",
        "body": "Глобальные счётчики (фракции, регионы, идеологии, технологии), пороговые события разных масштабов и точки невозврата,\nопределяющие доступные концовки.\n",
        "mechanics_links": [
          "analytics/faction-analytics-balance.yaml"
        ],
        "assets": []
      },
      {
        "id": "branching",
        "title": "Типы ветвлений",
        "body": "Линейные, параллельные, условные и смешанные ветвления задают разнообразие финалов. Детально описаны механики выбора\nи оценка вкладов игроков.\n",
        "mechanics_links": [
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
      "shared/trackers/queues/concept/queued.yaml#canon-narrative-sid-system"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "narrative_team",
      "changes": "Система SID перенесена в YAML с описанием принципов, структуры и отслеживания."
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