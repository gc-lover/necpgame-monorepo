-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\factions\gangs\gangs-2020-2093.yaml
-- Generated: 2025-12-13T21:13:37.756277

BEGIN;

-- Lore: canon-narrative-faction-gangs-2020-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-faction-gangs-2020-2093',
    'Банды Night City — фракционные ветки (2020–2093)',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-faction-gangs-2020-2093",
    "title": "Банды Night City — фракционные ветки (2020–2093)",
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
      "gangs",
      "factions",
      "branching"
    ],
    "topics": [
      "street-politics",
      "faction-conflicts"
    ],
    "related_systems": [
      "narrative-service",
      "quest-service",
      "live-ops-service"
    ],
    "related_documents": [
      {
        "id": "canon-narrative-scenarios-faction-index",
        "relation": "references"
      },
      {
        "id": "mechanics-combat-combat-hacking-combat-integration",
        "relation": "influences"
      },
      {
        "id": "mechanics-social-relationships-system",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/factions/gangs/gangs-2020-2093.md",
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
    "problem": "Каталог веток банд Night City вёлся в Markdown и не синхронизировался с нарративными и Live Ops системами.",
    "goal": "Структурировать сюжеты Valentinos, Maelstrom, 6th Street и Tyger Claws с их эпохами, узлами и последствиями.",
    "essence": "Документ описывает философию и сценарии каждой банды, межфракционные войны и бонусы за лояльность или нейтралитет.",
    "key_points": [
      "Каждая банда имеет собственную философию, иерархию и сюжетные узлы по эпохам.",
      "Указаны ключевые выборы игроков с проверками DC и последствиями для репутации и контроля районов.",
      "Межфракционные конфликты формируют альянсы и эскалации в 2020–2093.",
      "Бонусы и штрафы за лояльность или независимость задают геймплейные эффекты."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "context",
        "title": "Общий контекст",
        "body": "После DataKrash банды контролируют районы Night City и вступают в конфликты между собой, корпорациями и NCPD.\nСценарии охватывают эпохи от восстановления до купольных войн и прокси-конфликтов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "valentinos",
        "title": "Valentinos (Heywood)",
        "body": "Семейная банда с кодексом чести. Сюжеты включают инициацию новичков, войны с 6th Street, участие в голосованиях DAO\nи последствия усиления или ослабления влияния на Heywood.\n",
        "mechanics_links": [
          "canon/narrative/npc-lore/important/padre-ibarra.md",
          "content/quests/faction-world/latam/valentinos-arc.yaml"
        ],
        "assets": []
      },
      {
        "id": "maelstrom",
        "title": "Maelstrom (Watson)",
        "body": "Культ трансгуманизма и киберпсихоза. Игроки сталкиваются с выбором имплантов, рейдами на клиники и риском потери\nчеловечности. Решения влияют на отношения с NCPD и доступ к редким имплантам.\n",
        "mechanics_links": [
          "mechanics/combat/combat-cyberpsychosis.yaml",
          "canon/narrative/npc-lore/factions/gangs/maelstrom-history-2015-2093.yaml"
        ],
        "assets": []
      },
      {
        "id": "sixth_street",
        "title": "6th Street (Santo Domingo)",
        "body": "Патриотическая милиция ветеранов, отстаивающая порядок. Сюжеты охватывают патрули, противостояние Valentinos\nи моральные дилеммы между безопасностью и произволом.\n",
        "mechanics_links": [
          "canon/narrative/npc-lore/factions/gangs/6th-street-history-2037-2093.yaml"
        ],
        "assets": []
      },
      {
        "id": "tyger_claws",
        "title": "Tyger Claws (Japantown/Westbrook)",
        "body": "Якудза, контролирующая развлечения. Игроки взаимодействуют с Wakako, защищают клубы или саботируют бизнес,\nчто влияет на экономику района и отношения с Maelstrom.\n",
        "mechanics_links": [
          "canon/narrative/npc-lore/factions/gangs/tyger-claws-history-2005-2093.yaml"
        ],
        "assets": []
      },
      {
        "id": "conflicts",
        "title": "Межбандовые конфликты и эпохи",
        "body": "Описаны территориальные войны 2020–2030, купольные войны 2030–2045, красные рынки 2045–2060 и участие банд\nв прокси-войнах корпораций 2060–2077.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "bonuses",
        "title": "Бонусы и нейтралитет",
        "body": "Указаны уникальные фракционные бонусы (скидки DC, ресурсы, вооружение) и штрафы. Нейтралитет даёт гибкость,\nно ограничивает доступ к эксклюзивным преимуществам.\n",
        "mechanics_links": [
          "mechanics/social/relationships-system.yaml"
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
      "shared/trackers/queues/concept/queued.yaml#canon-narrative-faction-gangs-2020-2093"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "narrative_team",
      "changes": "Структурированы ветки банд Night City по эпохам и сценариям."
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