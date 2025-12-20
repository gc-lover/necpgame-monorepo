-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\origins\nomad.yaml
-- Generated: 2025-12-21T02:15:39.830508

BEGIN;

-- Lore: origin-nomad
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'origin-nomad',
    'Origin: Nomad',
    'canon',
    'origin-story',
    '{
  "metadata": {
    "id": "origin-nomad",
    "title": "Origin: Nomad",
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
      "nomad",
      "logistics"
    ],
    "topics": [
      "origins",
      "narrative"
    ],
    "related_systems": [
      "logistics-service",
      "quest-engine"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/origins/nomad.md",
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
    "problem": "Описание происхождения Nomad не приводит структуры выбора и бонусов для логистических сюжетов.",
    "goal": "Сформировать эпохальные этапы, фракционные связи и преимущества для номадских игроков.",
    "essence": "Игрок оперирует сухими трассами и теплым коридорам, противопоставляя клановую логистику корп системам.",
    "key_points": [
      "Завязка требует проверок driving и survival на магистралях и вне города.",
      "Эпохи раскрывают картографию Низа, теплые коридоры и автономные эвакуации.",
      "Фракционные бонусы дают скидки и снижают пороги supply и driving."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Кратко",
        "body": "- Номад клан курсирует между куполами и Badlands, доставляя грузы и людей.\n- Основные навыки: driving и survival с порогами 0.68 на трассах и 0.62 вне города.\n- Стартовый транспорт: модульный грузовик с секретными отсеками.\n",
        "mechanics_links": [
          "mechanics/quests/quest-system.yaml"
        ]
      },
      {
        "id": "inciting_incident",
        "title": "Завязка 2020-2030",
        "body": "Клан открывает сухую трассу для обхода корп пошлин. Игрок решает, работать ли с Aldecaldos, помогать независимым беженцам или продавать маршруты корпорациям.\n",
        "mechanics_links": [
          "mechanics/economy/trading-routes-global.yaml"
        ]
      },
      {
        "id": "epoch_path",
        "title": "Траектория эпох",
        "body": "- 2030-2045: контрабанда пропускной способности и картография Низа.\n- 2045-2060: оффлайн бандлы и теплые коридоры, защищающие связь.\n- 2060-2077: автономные эвакуации во время радиомолчаний и войн.\n- 2077: создание коридоров Dogtown для спасения и штурма.\n- 2078-2093: логистика экспедиций за заслон и участие в перезапуске сетей.\n",
        "mechanics_links": []
      },
      {
        "id": "factions",
        "title": "Фракции и союзники",
        "body": "- Aldecaldos: семейные связи, защита караванов.\n- Nomad convoys: обмен маршрутов и запасов.\n- Противостояние корп логистике создает постоянный риск.\n",
        "mechanics_links": []
      },
      {
        "id": "rewards",
        "title": "Бонусы и последствия",
        "body": "- Приоритетные маршруты и скидки на поставки.\n- Снижение порогов supply и driving в миссиях перевозки.\n- Рост heat при предательстве клана или сотрудничестве с корпорациями.\n",
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
      "changes": "Конверсия origin Nomad в формат знаний."
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