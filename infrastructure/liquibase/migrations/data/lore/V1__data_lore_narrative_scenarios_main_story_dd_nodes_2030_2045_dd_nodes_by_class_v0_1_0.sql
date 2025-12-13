-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\dd-nodes\2030-2045-dd-nodes-by-class.yaml
-- Generated: 2025-12-13T21:13:37.817735

BEGIN;

-- Lore: main-story-2030-2045-dd-nodes
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2030-2045-dd-nodes',
    '2030–2045 — Shooter skill tests по классам',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2030-2045-dd-nodes",
    "title": "2030–2045 — Shooter skill tests по классам",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T09:35:00+00:00",
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
      "mid-era",
      "shooter-tests"
    ],
    "topics": [
      "story-arc",
      "gameplay-integration"
    ],
    "related_systems": [
      "quest-engine",
      "combat-service",
      "diplomacy-hub"
    ],
    "related_documents": [
      {
        "id": "main-story-2030-2045",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/dd-nodes/2030-2045-dd-nodes-by-class.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "systems"
    ],
    "risk_level": "medium"
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
    "problem": "Узлы shooter-тестов эпохи 2030–2045 лежали в Markdown и не были связаны с остальными документами основного сюжета.",
    "goal": "Зафиксировать проверки куполов, нейроэтики и кризисов каналов в валидируемом YAML с явными зависимостями.",
    "essence": "Документ охватывает акты с купольными режимами, заражёнными микрокодами, рейдами «Ковчегов» и легализованным PvP.",
    "key_points": [
      "Купольный акт A1/A2 определяет социальные и исследовательские пороги для политиков и номадов.",
      "Блок нейроэтики задаёт медтех, corpo и fixer-проверки доступа к серым имплантам.",
      "Заражённые микрокоды и дефицит каналов формируют хакерские и торговые проверки.",
      "Рейд «Ковчегов» и ликвидаторские тендеры поднимают требования мультиклассового PvP."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "node_list",
        "title": "Узлы и пороги",
        "body": "- **A1 Устав Купола:** Politician/Teacher — social/communication — threshold 0.66–0.74; коалиция даёт −0.03; критический провал вызывает протесты и режим хаоса.\n- **A2 Картография Низа:** Nomad/Techie — exploration/tech — threshold 0.66–0.74; спец-инструмент уменьшает пороги; критический успех открывает скрытые маршруты.\n- **B1 Нейроэтика:** Medtech/Corpo/Fixer — medicine/legal/trading — threshold 0.66–0.74; успех выдаёт доступ к имплантам, провал переносит риски в 2045–2060.\n- **B2 Заражённые микрокоды:** Netrunner — hacking — threshold 0.70–0.78; критический провал создаёт «баг недели» для городских серверов.\n- **C1 Дефицит каналов:** Trader/Techie — trading/tech — threshold 0.66–0.74; успех стабилизирует цены, провал повышает стоимость ресурсов.\n- **C2 Пласт памяти:** Media/Rockerboy — investigation/charisma — threshold 0.66–0.74; критический провал приводит к судебному иску.\n- **C3 Рейд «Ковчегов»:** Solo/Netrunner/Social — multi-metric — threshold 0.70–0.78; результат фиксирует моральный флаг, влияющий на дальнейшие акторы.\n- **D1 Ликвидаторские тендеры:** Corpo/Solo/Stealth — threshold 0.70–0.78; оформленный PvP влияет на статус в эпохе 2045–2060.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "escalation",
        "title": "Эскалация угроз",
        "body": "Кризисы каналов и заражённые микрокоды поднимают системную сложность governance- и combat-проверок. Успешная нейроэтика снижает стоимость S-tier имплантов и открывает ветки медтех-квестов, тогда как провалы формируют долгосрочные дебаффы для социальных сценариев и экономических параметров.\n",
        "mechanics_links": [
          "canon/narrative/scenarios/main-story/framework.yaml"
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
      "changes": "Конвертация shooter-узлов 2030–2045 в YAML."
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