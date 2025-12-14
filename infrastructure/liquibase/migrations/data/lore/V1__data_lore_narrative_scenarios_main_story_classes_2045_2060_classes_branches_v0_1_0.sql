-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\classes\2045-2060-classes-branches.yaml
-- Generated: 2025-12-14T16:03:08.974697

BEGIN;

-- Lore: main-story-2045-2060-classes
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2045-2060-classes',
    '2045–2060 — Ветки по классам',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2045-2060-classes",
    "title": "2045–2060 — Ветки по классам",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T10:05:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "github_issue_number": 133,
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "main-story",
      "class-branches",
      "late-era"
    ],
    "topics": [
      "story-arc",
      "gameplay-integration"
    ],
    "related_systems": [
      "narrative-service",
      "quest-engine",
      "economy-service"
    ],
    "related_documents": [
      {
        "id": "main-story-2045-2060",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/classes/2045-2060-classes-branches.md",
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
    "problem": "Классовые ветки периода 2045–2060 существовали только в Markdown и не синхронизировались с основным знанием.",
    "goal": "Сформировать YAML-документ, описывающий роли классов в эпоху Blackwall, тёплых коридоров и красных рынков.",
    "essence": "Каждая роль получает специфические shooter-пороги и последствия, влияющие на устойчивость сетей и экономику региона.",
    "key_points": [
      "Solo и Lawman поддерживают безопасность петель и рынков.",
      "Netrunner и Fixer управляют коридорами обмена и флагами NetWatch.",
      "Corpo и Politician формируют кодексы совместимости и регулирование коридоров.",
      "Medtech и Teacher заботятся о психостабильности и просвещении граждан."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "class_responsibilities",
        "title": "Ответственность классов",
        "body": "- **Solo:** охрана «рёбер города» и зачистки «красных рынков»; Combat/Defense 0.66–0.74; критический успех выдаёт приватные лицензии.\n- **Netrunner:** часовые Blackwall и коридоры обмена; Hacking 0.74–0.82; топ-дека снижает пороги, критический провал фиксирует NetWatch flag.\n- **Techie:** строительство и ремонт петель; Tech 0.66–0.74; успех повышает стабильность регионов.\n- **Fixer:** монетизация тёплых коридоров; Trading 0.66–0.74; критический успех приносит налоговые льготы.\n- **Rockerboy/Media:** разоблачения ИИ-культов и инфовойны; Charisma/Investigation 0.66–0.74; последствия — общественные бафы или баны.\n- **Nomad:** межгородская контрабанда оффлайн-бандлов; Driving/Survival 0.66–0.74; критический провал ведёт к перехвату.\n- **Corpo:** кодексы совместимости против монополий; Legal/Social 0.66–0.74; влияние на фракционный доступ.\n- **Lawman:** патрули разломов и контроль рынков; Intimidation/Investigation 0.66–0.74; ордера дают бонус −0.04.\n- **Medtech:** клиники психостабильности и дефектных имплантов; Medicine 0.66–0.74; критический успех выдаёт патенты.\n- **Politician:** политика коридоров и регулирование; Social/Legal 0.66–0.74; исходы меняют региональные параметры.\n- **Trader:** ядра рынков и оффлайн-лицензии; Trading 0.66–0.74; критический успех даёт эксклюзивы брендов.\n- **Teacher:** просветительские кампании по безопасности; Communication 0.62–0.70; поддержка города снижает пороги.\n",
        "mechanics_links": [
          "canon/narrative/scenarios/main-story/framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "era_signals",
        "title": "Сигналы эпохи",
        "body": "Blackwall-пограничье и тёплые коридоры создают дефицит безопасности, требующий координации Solo, Lawman и Netrunner. Экономические решения корпораций определяют налоговые режимы красных рынков, а образовательные кампании Teacher удерживают население от паники при всплесках NetWatch.\n",
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
      "changes": "Конвертация классовых веток 2045–2060 в YAML."
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