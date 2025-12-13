-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\sid-endings\endings\factions\corporations\arasaka-endings.yaml
-- Generated: 2025-12-13T21:13:37.871104

BEGIN;

-- Lore: canon-narrative-sid-faction-arasaka-endings
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'canon-narrative-sid-faction-arasaka-endings',
    'SID корпорация Arasaka — концовки',
    'canon',
    'narrative',
    '{
  "metadata": {
    "id": "canon-narrative-sid-faction-arasaka-endings",
    "title": "SID корпорация Arasaka — концовки",
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
      "arasaka"
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
        "id": "canon-narrative-sid-city-night-city-endings",
        "relation": "complements"
      },
      {
        "id": "canon-narrative-sid-npc-hanako-arasaka",
        "relation": "references"
      },
      {
        "id": "canon-narrative-sid-npc-yorinobu-arasaka",
        "relation": "references"
      }
    ],
    "source": "shared/docs/knowledge/canon/narrative/sid-endings/endings/factions/corporations/arasaka-endings.md",
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
    "problem": "Концовки Arasaka не были представлены в стандартизированном формате знаний.",
    "goal": "Структурировать пять сценариев исхода корпорации с условиями, ключевыми последствиями и влиянием на глобальные системы.",
    "essence": "Документ описывает траектории Arasaka от глобальной империи и трансформации до уничтожения, симуляционного контроля и внутреннего раскола.",
    "key_points": [
      "Каждый финал использует показатели `arasaka_power`, `final_war`, `simulation_awareness` и `internal_conflict`.",
      "Сценарии фиксируют судьбы Hanako и Yorinobu, влияние на регионы и статус Night City.",
      "Финалы задают стартовые параметры следующей лиги, включая возможный контроль над симуляцией."
    ]
  },
  "content": {
    "sections": [
      {
        "id": "empire",
        "title": "A1 Империя Arasaka",
        "body": "Абсолютная победа корпорации приводит к мировому доминированию Hanako Arasaka, переименованию Night City и тотальному контролю\nнад регионами.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "fall",
        "title": "A2 Падение гиганта",
        "body": "Проигрыш финальной войны уничтожает Arasaka, распределяет активы между соперниками и оставляет семью в изгнании, создавая\nвакуум власти в Азии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "new_erasaka",
        "title": "A3 Новая Arasaka",
        "body": "Корпорация проходит реформу, отказывается от военных контрактов и смещает фокус на культуру и технологии мягкой силы, сохраняя\nвлияние без агрессии.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "beyond",
        "title": "A4 За заслоном",
        "body": "Высокая осведомлённость о симуляции позволяет Arasaka либо захватить параметры реальности, либо быть поглощённой системой,\nсоздавая уникальные эффекты.\n",
        "mechanics_links": [],
        "assets": []
      },
      {
        "id": "fracture",
        "title": "A5 Расколотая Arasaka",
        "body": "Внутренняя война между Hanako и Yorinobu делит корпорацию на фракции, ослабляет глобальное влияние и открывает пространство\nдля конкурентов.\n",
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
        "link": "../../../periods/finale-2090-2093/README.yaml"
      },
      {
        "title": "Концовки Militech",
        "link": "./militech-endings.yaml"
      },
      {
        "title": "Концовки Night City",
        "link": "../../cities/night-city-endings.yaml"
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
      "changes": "Концовки Arasaka перенесены в YAML с параметрами условий и последствий."
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