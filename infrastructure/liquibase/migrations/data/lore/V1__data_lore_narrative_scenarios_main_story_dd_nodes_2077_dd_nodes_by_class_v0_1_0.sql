-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\dd-nodes\2077-dd-nodes-by-class.yaml
-- Generated: 2025-12-21T02:15:39.796057

BEGIN;

-- Lore: main-story-2077-dd-nodes
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2077-dd-nodes',
    '2077 — Shooter skill tests по классам',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2077-dd-nodes",
    "title": "2077 — Shooter skill tests по классам",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T07:45:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "main-story",
      "phantom-liberty",
      "shooter-tests"
    ],
    "topics": [
      "story-arc",
      "gameplay-integration"
    ],
    "related_systems": [
      "quest-engine",
      "combat-service",
      "security-service"
    ],
    "related_documents": [
      {
        "id": "main-story-2077",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/dd-nodes/2077-dd-nodes-by-class.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "systems"
    ],
    "risk_level": "high"
  },
  "review": {
    "chain": [
      {
        "role": "concept_director",
        "reviewer": "",
        "reviewed_at": "",
        "status": "pending"
      }
    ],
    "next_actions": []
  },
  "summary": {
    "problem": "Shooter-проверки эпохи 2077 оставались в Markdown и не обеспечивали автоматизированную проверку Threshold-констант.",
    "goal": "Формализовать узлы Dogtown, президентского протокола и финала заговора для дальнейших handoff-циклов.",
    "essence": "Узлы покрывают боевые, социальные и хакерские испытания с порогами 0.74–0.90 и критическими последствиями.",
    "key_points": [
      "Dogtown предъявляет требования к бою, интимидации и торговле.",
      "Президентский протокол сочетает социальные и хакерские проверки уровня 0.82–0.86.",
      "Информатор и коридоры эвакуации раскрывают критические ветки Nomad/Techie и Media.",
      "Финальный заговор объединяет мультиклассовые пороги и определяет концовки."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "node_list",
        "title": "Узлы и пороги",
        "body": "- **A1 Dogtown Checkpoint:** Solo, Lawman — intimidation/combat — threshold 0.74–0.82 — критический провал ведёт к бою.\n- **A2 Black Market:** Trader, Fixer — trading/deception — threshold 0.74–0.82 — критический провал: «кидок».\n- **B1 Presidential Chain:** Politician, Media — social — threshold 0.78→0.82 — приносит доступ или розыск.\n- **B2 Net Seal:** Netrunner — hacking — threshold 0.82→0.86 — критический провал запускает отслеживание.\n- **C1 Informant:** Rockerboy, Media — charisma — threshold 0.78 — предоставляет ключевую информацию.\n- **C2 Evac Corridors:** Nomad, Techie — logistics/tech — threshold 0.74–0.82 — формирует пути эвакуации.\n- **D1 Conspiracy Finale:** мультиклассовый узел — threshold 0.80–0.90 — определяет фракционные концовки.\n",
        "mechanics_links": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 133,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Конвертация shooter-узлов 2077 в YAML."
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