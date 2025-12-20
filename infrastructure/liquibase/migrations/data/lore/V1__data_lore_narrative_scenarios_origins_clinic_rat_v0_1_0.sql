-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\origins\clinic-rat.yaml
-- Generated: 2025-12-21T02:15:39.816597

BEGIN;

-- Lore: origin-clinic-rat
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'origin-clinic-rat',
    'Origin: Clinic Rat',
    'canon',
    'origin-story',
    '{
  "metadata": {
    "id": "origin-clinic-rat",
    "title": "Origin: Clinic Rat",
    "document_type": "canon",
    "category": "origin-story",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-05T18:38:00+00:00",
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
      "medicine",
      "implants"
    ],
    "topics": [
      "origins",
      "narrative"
    ],
    "related_systems": [
      "medical-service",
      "cyberware-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/origins/clinic-rat.md",
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
    "problem": "История Clinic Rat описана тезисно и не структурирует медконфликты и выборы.",
    "goal": "Сформировать путь подпольного медика с акцентом на импланты, нейроэтику и сохранение человечности.",
    "essence": "Игрок вырос в подпольной клинике, управляет серыми имплантами и балансирует между Trauma Team, рипдоками и подпольем.",
    "key_points": [
      "Проверки medicine и tech определяют доступ к S-tier процедурам и последствия для пациентов.",
      "Эпохальные дуги показывают развитие нейроэтики, медицину тишин и экспедиции в зоны нулевой пыли.",
      "Фракционные бонусы снижают медицинские пороги, но повышают риск киберпсихоза при злоупотреблении серыми компонентами."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Кратко",
        "body": "- Происхождение: подпольная клиника и лаборатория, где герой с детства заменяет импланты.\n- Стартовые навыки: medicine и tech на уровне проверок ≥ 0.66.\n- Ключевой ресурс: сеть рипдоков и доступ к серым компонентам.\n",
        "mechanics_links": [
          "mechanics/quests/quest-system.yaml"
        ]
      },
      {
        "id": "inciting_incident",
        "title": "Завязка",
        "body": "Критический пациент после аварийной нейрохирургии требует решения: использовать серый имплант, обратиться к Trauma Team или отказаться. Проверки определяют репутацию, человечность и долгие последствия.\n",
        "mechanics_links": [
          "mechanics/social/social-mechanics-overview.yaml"
        ]
      },
      {
        "id": "epoch_path",
        "title": "Траектория эпох",
        "body": "- 2030-2045: нейроэтика серых имплантов и борьба за лицензии.\n- 2045-2060: психостабильность клиентов и исправление дефектов модулей.\n- 2060-2077: оффлайн медицина тишин для скрытых операций.\n- 2078-2093: экспедиции в зоны нулевой пыли и восстановление экипировки после враждебных сред.\n",
        "mechanics_links": []
      },
      {
        "id": "factions",
        "title": "Союзы и враги",
        "body": "- Подпольные клиники: редкие чертежи и расходники за сохранение секретности.\n- Trauma Team: официальные контракты при соблюдении протоколов.\n- Гильдии рипдоков: обмен процедурами и скидки.\n",
        "mechanics_links": []
      },
      {
        "id": "rewards",
        "title": "Бонусы и риски",
        "body": "- Снижение порогов medicine на подпольных операциях.\n- Скидки на импланты и ускоренный доступ к S-tier процедурам.\n- Риск киберпсихоза и потери человечности при злоупотреблении серыми компонентами.\n",
        "mechanics_links": [
          "mechanics/combat/combat-shooter-core.yaml"
        ]
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
      "changes": "Конверсия origin Clinic Rat в формат знаний."
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