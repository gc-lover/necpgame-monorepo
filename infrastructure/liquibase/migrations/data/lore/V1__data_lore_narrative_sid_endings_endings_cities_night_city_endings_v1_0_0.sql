-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\sid-endings\endings\cities\night-city-endings.yaml
-- Generated: 2025-12-21T02:15:39.842614

BEGIN;

-- Lore: canon-narrative-sid-city-night-city-endings
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-sid-city-night-city-endings',
    'SID город Night City — концовки',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-sid-city-night-city-endings",
    "title": "SID город Night City — концовки",
    "document_type": "canon",
    "category": "narrative",
    "status": "draft",
    "version": "1.0.0",
    "last_updated": "2025-11-12T00:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "narrative_director",
        "contact": "narrative@necp.game"
      }
    ],
    "tags": [
      "sid",
      "endings",
      "night-city"
    ],
    "topics": [
      "narrative",
      "branching"
    ],
    "related_systems": [
      "narrative-service"
    ],
    "related_documents": [
      {
        "id": "canon-narrative-sid-finale-navigation",
        "relation": "references"
      },
      {
        "id": "canon-narrative-sid-npc-marco-fix-sanchez",
        "relation": "complements"
      },
      {
        "id": "canon-narrative-sid-faction-arasaka-endings",
        "relation": "complements"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/sid-endings/endings/cities/night-city-endings.md",
    "visibility": "internal",
    "audience": [
      "narrative",
      "systems-design"
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
    "problem": "Концовки Night City находились в Markdown и не были включены в структурированную базу знаний.",
    "goal": "Описать шесть финальных сценариев Night City с условиями активации, статусами районов и влиянием на лигу.",
    "essence": "Документ детализирует исходы города от легендарной независимости до полного разрушения и симуляционных откровений.",
    "key_points": [
      "Каждая концовка имеет набор параметров `power`, `awareness` и состояние независимости.",
      "Структура фиксирует влияние на районы, ключевых NPC и титры для эпилога.",
      "Результаты напрямую связаны с глобальными ветвями SID и последующей лигой."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "legend",
        "title": "NC1 Легенда Night City",
        "body": "Город сохраняет независимость при сбалансированных силовых показателях. Районы остаются живыми, а Marco \"Fix\" Sanchez,\nJosé \"Tigre\" Ramirez и Viktor Vektor становятся живыми легендами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "arasaka_city",
        "title": "NC2 Arasaka City",
        "body": "Arasaka получает полный контроль над Night City, трансформируя его в корпоративную диктатуру с тотальным надзором и\nликвидацией независимых акторов.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "ruins",
        "title": "NC3 Руины Night City",
        "body": "Пересечение критических показателей разрушения приводит к полному уничтожению города ядерным ударом, войной или катастрофой,\nоставляя лишь зону отчуждения.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "window",
        "title": "NC4 Окно в реальность",
        "body": "Night City становится центром раскрытия симуляции при высокой `simulation_awareness`, превращаясь в мост между мирами и\nэпицентр метафизических явлений.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "free_city",
        "title": "NC5 Свободный город",
        "body": "Совет фиксеров управляет городом при высоком уровне независимости, вытесненных корпорациях и ограниченных бандах,\nсоздавая редкий пример самоорганизации.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "for_sale",
        "title": "NC6 Город на продажу",
        "body": "Коррупция и раздробленные корпорации приводят к распродаже районов разным игрокам рынка, формируя постоянные торговые войны\nи утрату гражданской субъектности.\n",
        "mechanics_links": [],
        "assets": []
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [
      {
        "title": "Финал 2090-2093",
        "link": "../../periods/finale-2090-2093/README.yaml"
      },
      {
        "title": "Концовки NPC Marco \"Fix\" Sanchez",
        "link": "../npcs/marco-fix-sanchez.yaml"
      },
      {
        "title": "Концовки Arasaka",
        "link": "../factions/corporations/arasaka-endings.yaml"
      }
    ],
    "decisions": []
  },
  "implementation": {
    "github_issue": 133,
    "needs_task": false,
    "queue_reference": [
      "shared/trackers/queues/concept/queued.yaml"
    ],
    "blockers": []
  },
  "history": [
    {
      "version": "1.0.0",
      "date": "2025-11-12",
      "author": "narrative_team",
      "changes": "Концовки Night City перенесены в YAML с условиями, описаниями районов и титрами."
    }
  ],
  "validation": {
    "checksum": "",
    "schema_version": "1.0"
  }
}'::jsonb,
    1
)
ON CONFLICT (lore_id) DO UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;


COMMIT;