-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\framework.yaml
-- Generated: 2025-12-21T02:15:39.801812

BEGIN;

-- Lore: main-story-framework
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-framework',
    'Основной сюжет — Каркас',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-framework",
    "title": "Основной сюжет — Каркас",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T07:25:00+00:00",
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
      "framework",
      "branching"
    ],
    "topics": [
      "story-architecture",
      "branching-logic"
    ],
    "related_systems": [
      "narrative-service",
      "progression-service"
    ],
    "related_documents": [
      {
        "id": "main-story-2020-2030",
        "relation": "supports"
      },
      {
        "id": "main-story-2030-2045",
        "relation": "supports"
      },
      {
        "id": "main-story-2045-2060",
        "relation": "supports"
      },
      {
        "id": "main-story-2060-2077",
        "relation": "supports"
      },
      {
        "id": "main-story-2077",
        "relation": "supports"
      },
      {
        "id": "main-story-2078-2093",
        "relation": "supports"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/framework.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "liveops"
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
    "problem": "Каркас основного сюжета находился в Markdown и не обеспечивал стандартизированные метаданные для автоматизации пайплайна.",
    "goal": "Оформить акты, правила ветвления и переходов между эпохами в виде знания, пригодного для валидации и задач Concept Director.",
    "essence": "Документ задаёт универсальную матрицу актов, веток и переносимых состояний для всех эпох основной кампании.",
    "key_points": [
      "Четырёхактная структура повторяется для каждой эпохи.",
      "Правила ветвления описывают условия для классов, фракций и origins.",
      "Переходы фиксируют переносимые и непереносимые состояния.",
      "Фракционные правила задают конфликты Arasaka/Militech, NetWatch/Blackwall и городских DAO."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "act_structure",
        "title": "Универсальная актовая структура",
        "body": "Каждая эпоха основной кампании следует четырём актам:\n1. Завязка эпохи — ввод конфликтов и ключевых фракций.\n2. Эскалация — классовые ветки и усиление корпоративного напряжения.\n3. Развилка — проверки навыков и фракционные решения.\n4. Развязка — последствия для экономики, сетей и политики, подготовка следующей эпохи.\n",
        "mechanics_links": []
      },
      {
        "id": "branching_rules",
        "title": "Правила ветвления",
        "body": "- Ветки по классу: уникальные решения узлов, доступ к имплантам и проверкам навыков.\n- Ветки по фракции и корпорации: награды, репутация и конфликт интересов.\n- Ветки по origin: стартовые бонусы и альтернативные маршруты в ранних узлах.\n- Shooter skill tests: пороги по эпохам с учётом world-events и модификаторов окружения.\n",
        "mechanics_links": []
      },
      {
        "id": "era_transitions",
        "title": "Переходы между эпохами",
        "body": "- Переносимые состояния: репутации, артефакты, юридические статусы, уровень человечности.\n- Непереносимые состояния: ситуативные бафы, долги, часть перков.\n- Мосты: задания порога эпохи фиксируют концовку и открывают бонусы или штрафы в следующей фазе.\n",
        "mechanics_links": []
      },
      {
        "id": "faction_rules",
        "title": "Фракционные правила",
        "body": "- Arasaka vs Militech: дуальные ветки соглашений, саботажа и рейдов.\n- NetWatch и Blackwall: стандартные протоколы против чёрных сетей.\n- Городские DAO и купола: режимы голосований, параметры экономики и безопасности.\n",
        "mechanics_links": []
      },
      {
        "id": "origins",
        "title": "Origins",
        "body": "- Street Kid: уличные связи, бонус к Stealth и Intimidation.\n- Corpo: корпоративный доступ, бонус к Persuasion и Trading.\n- Nomad: мобильность и логистика, бонус к Survival и Driving.\n- Outlaw Scholar, Clinic Rat, Data Orphan: авторские варианты с доступом к редким знаниям, медицине и аномальным сетевым следам.\n",
        "mechanics_links": []
      },
      {
        "id": "references",
        "title": "Связанная документация",
        "body": "- Матрица сюжета: `README.yaml`\n- Ключевые миссии: `../../quests/main/*`\n- Shooter-проверки: `../../quest-shooter-checks.yaml`\n- Мировые события: `../../../02-gameplay/world/events/*`\n",
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
      "changes": "Конвертация каркаса основной кампании в YAML."
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