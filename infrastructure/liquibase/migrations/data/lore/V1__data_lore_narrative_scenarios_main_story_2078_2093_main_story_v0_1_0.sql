-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\main-story\2078-2093-main-story.yaml
-- Generated: 2025-12-14T16:03:08.962913

BEGIN;

-- Lore: main-story-2078-2093
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'main-story-2078-2093',
    'Основной сюжет — 2078–2093',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "main-story-2078-2093",
    "title": "Основной сюжет — 2078–2093",
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
      "reboot"
    ],
    "topics": [
      "timeline_author",
      "story-arc"
    ],
    "related_systems": [
      "narrative-service",
      "league-service",
      "event-service"
    ],
    "related_documents": [
      {
        "id": "main-story-framework",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/main-story/2078-2093-main-story.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "liveops"
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
    "problem": "Финальный период основной кампании 2078–2093 был описан в Markdown и не увязывал акты с параметрическим перезапуском мира.",
    "goal": "Структурировать позднюю лигу, чтобы зафиксировать ярмарки параметров, Blackwall-экспедиции и выбор перезапуска как единый нарратив.",
    "essence": "Арка раскрывает подготовку к глобальному перезапуску сервера, где игроки голосуют параметрами, исследуют Blackwall и закрепляют наследие кампании.",
    "key_points": [
      "Параметрические ярмарки представляют миру открытые настройки безопасности, экономики и погоды.",
      "Архивы за заслоном обеспечивают рискованные Blackwall-рейды и поиск артефактов реальности.",
      "Конституции и квоты распределяют влияние между фракциями и классами.",
      "Финальная операция фиксирует выбор перезапуска и формирует переносимое наследие."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "context",
        "title": "Контекст поздней лиги",
        "body": "Открытая дискуссия о природе мира NECPGAME выходит на пик. Лига готовится к глобальному событию перезапуска,\nа игроки получают прямой доступ к параметрам симуляции, что усиливает политическую и социальную напряжённость.\n",
        "mechanics_links": []
      },
      {
        "id": "act_structure",
        "title": "Структура актов",
        "body": "- **Акт I — Параметрические ярмарки:** общественные выставки параметров и тестовые PvE/PvP режимы собирают метрики.\n- **Акт II — Архивы за заслоном:** экспедиции за Blackwall и сбор артефактов реальности формируют доказательную базу.\n- **Акт III — Конституции и квоты:** юридические и экономические ветки определяют распределение влияния.\n- **Акт IV — Выбор перезапуска:** глобальная операция объединяет классы и закрывает эпоху выбором параметров сервера.\n",
        "mechanics_links": []
      },
      {
        "id": "outcomes",
        "title": "Наследие и концовки",
        "body": "Концовки зависят от комбинаций параметров, одобренных лига-контроллерами. Конституции, мосты эпох и найденные артефакты\nстановятся переносимыми элементами для следующей кампании.\n",
        "mechanics_links": []
      },
      {
        "id": "dependencies",
        "title": "Связанные материалы",
        "body": "- Таймлайн-автор: `../../../lore/_03-lore/timeline-author/2078-2090-author-events.yaml`\n- Каркас кампании: `framework.yaml`\n",
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
      "changes": "Конвертация сюжета 2078–2093 в структурированный YAML."
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