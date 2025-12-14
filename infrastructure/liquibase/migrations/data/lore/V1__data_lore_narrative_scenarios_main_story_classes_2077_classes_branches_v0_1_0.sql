-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\classes\2077-classes-branches.yaml
-- Generated: 2025-12-14T16:03:08.981049

BEGIN;

-- Lore: main-story-2077-classes
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2077-classes',
    '2077 — Ветки по классам',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2077-classes",
    "title": "2077 — Ветки по классам",
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
      "class-branches"
    ],
    "topics": [
      "story-arc",
      "gameplay-integration"
    ],
    "related_systems": [
      "narrative-service",
      "quest-engine",
      "security-service"
    ],
    "related_documents": [
      {
        "id": "main-story-2077",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/classes/2077-classes-branches.md",
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
    "problem": "Роли классов в событиях 2077 не были формализованы для автоматизированного пайплайна.",
    "goal": "Структурировать вклад каждого класса в операцию Dogtown, президентский протокол и финальную фазу заговора.",
    "essence": "Каждый класс получает конкретные задачи и shooter-пороги, влияющие на спецоперации и фракционные концовки.",
    "key_points": [
      "Solo и Lawman отвечают за силовое давление на блокпосты Dogtown.",
      "Netrunner и Techie поддерживают сетевые заслоны и технику.",
      "Fixer, Politician и Trader контролируют чёрный рынок и дипломатические окна.",
      "Медиа, Nomad и Medtech удерживают общественное мнение и эвакуацию."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "class_responsibilities",
        "title": "Ответственность классов",
        "body": "- **Solo:** штурмы Dogtown; Combat 0.74–0.82, критический успех открывает уникальное вооружение.\n- **Netrunner:** работа с заслонами; Hacking 0.78–0.86, критический провал выдаёт отслеживание спецслужб.\n- **Techie:** техническая поддержка операции; Tech 0.70–0.78.\n- **Fixer:** доступ и контракты в Dogtown; Social/Trading 0.74–0.82.\n- **Rockerboy и Media:** медиа-прикрытие операции; Charisma/Investigation 0.74–0.82.\n- **Nomad:** организация эвакуационных коридоров; Driving 0.74–0.82.\n- **Corpo:** политическое прикрытие; Legal/Social 0.74–0.82.\n- **Lawman:** операционные ордера; Legal/Intimidation 0.74–0.82.\n- **Medtech:** эвакуация и медицинская поддержка; Medicine 0.70–0.78.\n- **Politician:** дипломатия и протокол; Social 0.78–0.82.\n- **Trader:** управление чёрным рынком Dogtown; Trading 0.74–0.82.\n- **Teacher:** координация гражданских и информационных кампаний; Communication 0.66–0.74.\n",
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
      "changes": "Конвертация классовых веток 2077 в YAML."
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