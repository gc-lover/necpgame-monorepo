-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\classes\2078-2093-classes-branches.yaml
-- Generated: 2025-12-14T16:03:08.983828

BEGIN;

-- Lore: main-story-2078-2093-classes
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2078-2093-classes',
    '2078–2093 — Ветки по классам',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2078-2093-classes",
    "title": "2078–2093 — Ветки по классам",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T07:25:00+00:00",
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
      "late-league",
      "class-branches"
    ],
    "topics": [
      "gameplay-integration",
      "story-arc"
    ],
    "related_systems": [
      "narrative-service",
      "quest-engine",
      "league-service"
    ],
    "related_documents": [
      {
        "id": "main-story-2078-2093",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/classes/2078-2093-classes-branches.md",
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
    "problem": "Классовые ветки поздней лиги были в Markdown и не обеспечивали структурированных ссылок на пороги навыков.",
    "goal": "Зафиксировать вклады каждого класса в событие перезапуска и соответствующие thresholds в формате YAML.",
    "essence": "Документ связывает ключевые роли классов с параметрическими ярмарками, экспедициями и финальными переговорами.",
    "key_points": [
      "Solo и Netrunner отвечают за безопасность и Blackwall-операции.",
      "Techie и Nomad обеспечивают сенсорные мосты и логистику.",
      "Fixer, Trader и Politician ведут квоты влияния и конституции.",
      "Media, Rockerboy и Teacher удерживают общественное мнение и коммуникацию."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "class_branches",
        "title": "Роли классов 2078–2093",
        "body": "- Solo: безопасность ярмарок и центров параметров (Combat 0.74–0.82).\n- Netrunner: экспедиции за Blackwall и работа с артефактами (Hacking 0.78–0.88).\n- Techie: мосты сенсоров и переносимость параметров (Tech 0.74–0.82).\n- Fixer: квоты влияния и торги правами (Trading/Social 0.74–0.82).\n- Rockerboy и Media: общественные дебаты и медиа-суды (Charisma 0.74–0.82).\n- Nomad: логистика экспедиций и перезапуска (Driving/Survival 0.74–0.82).\n- Corpo: лоббизм параметров и юридические протоколы (Legal/Social 0.74–0.82).\n- Lawman: соблюдение порядка на глобальном событии (Legal/Intimidation 0.74–0.82).\n- Medtech: медицинская поддержка и борьба с нулевой пылью (Medicine 0.70–0.78).\n- Politician: разработка конституций лиг (Social/Legal 0.78–0.84).\n- Trader: торги артефактами и квотами (Trading 0.74–0.82).\n- Teacher: просветительские кампании и тест-параметры (Communication 0.66–0.74).\n",
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
      "changes": "Конвертация классовых веток 2078–2093 в YAML."
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