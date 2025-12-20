-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\origins\outlaw-scholar.yaml
-- Generated: 2025-12-21T02:15:39.834394

BEGIN;

-- Lore: origin-outlaw-scholar
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'origin-outlaw-scholar',
    'Origin: Outlaw Scholar',
    'canon',
    'origin-story',
    '{
  "metadata": {
    "id": "origin-outlaw-scholar",
    "title": "Origin: Outlaw Scholar",
    "document_type": "canon",
    "category": "origin-story",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-05T18:37:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "origin",
      "scholar",
      "outlaw"
    ],
    "topics": [
      "origins",
      "narrative"
    ],
    "related_systems": [
      "analytics-service",
      "quest-engine"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/origins/outlaw-scholar.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative"
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
    "problem": "История незаконного исследователя сетей описана тезисно и требует форматирования для передачи командам.",
    "goal": "Сформировать дуги эпох, фракционные связи и игровые бонусы для Outlaw Scholar.",
    "essence": "Игрок действует как независимый архивариус, вскрывающий скрытые данные и суды, балансируя между университетами, омбудсменами и медиа.",
    "key_points": [
      "Завязка строится на доступе к полу закрытым данным и проверках analysis и investigation.",
      "Эпохальная траектория ведет от ковчегов памяти к архивам за заслоном после 2078.",
      "Фракционные бонусы снижают пороги анализа и открывают уникальные ветви расследований."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Кратко",
        "body": "- Роль: независимый исследователь сетей и архивов.\n- Стартовые навыки: высокий analysis и investigation, проверка на уровне 0.66 и выше.\n- Инструменты: подпольные терминалы, доступ к полу закрытым хранилищам, сеть информаторов.\n",
        "mechanics_links": [
          "mechanics/quests/quest-system.yaml"
        ]
      },
      {
        "id": "inciting_incident",
        "title": "Завязка",
        "body": "Игрок обнаруживает кэш зашифрованных записей о мета судах и сталкивается с выбором: продать данные корпорациям, передать университету или раскрыть их общественности. Каждый выбор меняет отношения с фракциями и открывает ветви расследований.\n",
        "mechanics_links": [
          "mechanics/social/social-mechanics-overview.yaml"
        ]
      },
      {
        "id": "epoch_path",
        "title": "Траектория эпох",
        "body": "- 2030-2045: проекты \"Пласт памяти\" и \"Ковчеги\" возвращают утраченную историю.\n- 2045-2060: исследования ИИ оракулов и \"Тихой Сводки\" формируют стандарты совместимости данных.\n- 2060-2077: участие в мета судах, защита прав доступа и публикация кейсов.\n- 2078-2093: экспедиции за заслон в поисках архивных артефактов реальности.\n",
        "mechanics_links": []
      },
      {
        "id": "factions",
        "title": "Фракции и союзники",
        "body": "- Университетские кластеры предоставляют лаборатории и ученые визы.\n- Омбудсмены защищают от преследования корпораций.\n- Медиа суды помогают транслировать разоблачения и усиливают репутацию героя.\n",
        "mechanics_links": [
          "mechanics/economy/stock-exchange/stock-analytics.yaml"
        ]
      },
      {
        "id": "rewards",
        "title": "Бонусы и последствия",
        "body": "- Снижение порогов investigation и analysis на проверках знаний.\n- Доступ к уникальным веткам мета суда и архивным контрактам.\n- Угроза преследования со стороны корпораций при утечках данных.\n",
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
      "date": "2025-11-05",
      "author": "concept_team",
      "changes": "Конверсия origin Outlaw Scholar в формат знаний."
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