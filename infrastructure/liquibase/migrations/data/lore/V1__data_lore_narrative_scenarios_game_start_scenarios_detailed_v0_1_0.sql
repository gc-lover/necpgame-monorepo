-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\game-start-scenarios-detailed.yaml
-- Generated: 2025-12-13T21:13:37.765691

BEGIN;

-- Lore: game-start-detailed-scenarios
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'game-start-detailed-scenarios',
    'Детальные сценарии начала игры',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "game-start-detailed-scenarios",
    "title": "Детальные сценарии начала игры",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "0.1.0",
    "last_updated": "2025-11-11T21:32:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "game-start",
      "onboarding",
      "narrative"
    ],
    "topics": [
      "player-journey",
      "story-arc"
    ],
    "related_systems": [
      "narrative-service",
      "onboarding-service",
      "quest-engine"
    ],
    "related_documents": [
      {
        "id": "game-start-unique-starts",
        "relation": "references"
      },
      {
        "id": "game-start-by-origin",
        "relation": "complements"
      },
      {
        "id": "game-start-by-faction",
        "relation": "complements"
      },
      {
        "id": "game-start-by-class",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/game-start-scenarios-detailed.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "onboarding"
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
    "problem": "Детальные сценарии начала игры находились в Markdown и не обеспечивали связки между происхождением, фракцией и классом.",
    "goal": "Собрать эталонные сценарии старта для ключевых комбинаций, описав вводные диалоги, первые действия и лор-опорные точки.",
    "essence": "Каждая комбинация происхождения, фракции и класса формирует уникальный монтаж вступления с конкретными задачами и социальными стимулами.",
    "key_points": [
      {
        "Покрыто три архетипа": "Street Kid, Corpo и Nomad с конкретными фракционными связками."
      },
      {
        "Стартовые миссии фиксируют шаги onboarding": "контакт NPC, перемещение, ключевое действие и награда."
      },
      "Диалоги задают тональность отношений и намечают ветки репутаций.",
      "Материал масштабируется до полного покрытия 45 комбинаций в будущих итерациях."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "overview",
        "title": "Общее описание",
        "body": "- Сценарии описывают первые игровые минуты, связывая происхождение, выбранную фракцию и класс игрока.\n- Каждый сценарий включает вступительную историю, ключевой диалог и последовательность первых действий.\n- Архитектура рассчитана на 45 комбинаций (3 происхождения × 5 фракций × 3 класса) с дальнейшим расширением.\n",
        "mechanics_links": [
          "canon/narrative/scenarios/game-starts/README.yaml"
        ],
        "assets": []
      },
      {
        "id": "street_kid_paths",
        "title": "Street Kid — первые сценарии",
        "body": "- **Arasaka + Solo — «Корпоративный вызов»**\n  - Вступление подчёркивает редкий шанс уличного бойца попасть в корп-структуру.\n  - Диалог с Марко акцентирует доверие к знанию улиц и необходимость охраны объекта.\n  - Стартовые шаги: принять контракт, добраться до объекта, отразить нападение, вернуться за наградой.\n- **Valentinos + Solo — «Уличная честь»**\n  - Упор на кодекс банды и защиту территории.\n  - Диалог с лидером Хосе объясняет ценность лояльности и ожидание быстрого реагирования.\n  - Шаги повторяют структуру обороны района и закрепляют первые репутационные бонусы.\n",
        "mechanics_links": [
          "content/quests/side/side-quests-2020-2030.md"
        ],
        "assets": []
      },
      {
        "id": "corpo_paths",
        "title": "Corpo — переходы в новые роли",
        "body": "- **Arasaka + Netrunner — «Корпоративный хакер»**\n  - История подчеркивает внутренние корпоративные интриги и важность лояльности.\n  - Диалог с Хироши Танака выстраивает ожидание результатов и начало конфликтов с конкурентами.\n  - Стартовые шаги: взлом системы конкурента, работа в киберпространстве, отчёт о выполнении.\n- **Valentinos + Netrunner — «Бывший корпоративный хакер»**\n  - Переход от корп-карьеры к уличной банде для усиления её цифровых возможностей.\n  - Хосе вербует персонажа, апеллируя к прагматизму и взаимной выгоде.\n  - Цепочка действий зеркалирует корпоративный сценарий, но добавляет социальный контекст банды.\n",
        "mechanics_links": [
          "canon/narrative/scenarios/factions/corporations/arasaka-2020-2093.yaml"
        ],
        "assets": []
      },
      {
        "id": "nomad_paths",
        "title": "Nomad — партнёрство с институтами",
        "body": "- **NCPD + Techie — «Независимый техник»**\n  - Нарратив фокусируется на адаптации номада к городской службе через контракт с NCPD.\n  - Диалог с Сарой Миллер объясняет редкость такой кооперации и даёт ориентир по этике патруля.\n  - Стартовые шаги: диагностика оборудования, ремонт и отчёт усиливают связь с городской инфраструктурой.\n",
        "mechanics_links": [
          "mechanics/world/events/world-events-framework.yaml"
        ],
        "assets": []
      },
      {
        "id": "coverage_status",
        "title": "Статус покрытия и масштабирование",
        "body": "- Текущая версия содержит три примерных матрицы (Street Kid, Corpo, Nomad) как эталон для будущего масштабирования.\n- Остальные комбинации остаются в бэклоге дизайн-команды и будут добавляться в ходе следующих батчей.\n- Ссылки на технические документы обеспечивают согласованность с шаблонами происхождений, фракций и классов.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      {
        "title": "Уникальные старты",
        "link": "shared/docs/knowledge/canon/05-technical/game-start-unique-starts.md"
      },
      {
        "title": "Старты по происхождениям",
        "link": "shared/docs/knowledge/canon/05-technical/game-start-by-origin.md"
      },
      {
        "title": "Старты по фракциям",
        "link": "shared/docs/knowledge/canon/05-technical/game-start-by-faction.md"
      },
      {
        "title": "Старты по классам",
        "link": "shared/docs/knowledge/canon/05-technical/game-start-by-class.md"
      }
    ],
    "decisions": []
  },
  "implementation": {
    "needs_task": false,
    "github_issue": 102,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "0.1.0",
      "date": "2025-11-11",
      "author": "concept_director",
      "changes": "Перенос детальных стартовых сценариев из Markdown и структурирование по комбинациям."
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