-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\stories\underground-hacker-revolution.yaml
-- Generated: 2025-12-21T02:15:39.913505

BEGIN;

-- Lore: story-underground-hacker-revolution
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'story-underground-hacker-revolution',
    'Подпольная хакерская революция',
    'canon',
    'narrative-story',
    '{
  "metadata": {
    "id": "story-underground-hacker-revolution",
    "title": "Подпольная хакерская революция",
    "document_type": "canon",
    "category": "narrative-story",
    "status": "draft",
    "version": "1.0.0",
    "last_updated": "2025-12-14T12:00:00+00:00",
    "concept_approved": false,
    "concept_reviewed_at": "",
    "owners": [
      {
        "role": "concept_director",
        "contact": "concept@necp.game"
      }
    ],
    "tags": [
      "hacker",
      "revolution",
      "underground",
      "cyberpunk"
    ],
    "topics": [
      "digital_revolution",
      "underground_movements",
      "technological_resistance"
    ],
    "related_systems": [
      "narrative-service",
      "network-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/stories/underground-hacker-revolution.md",
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
    "problem": "Хакеры организуют глобальную сеть сопротивления корпоративному контролю над данными.",
    "goal": "Показать как цифровое подполье борется с тотальным надзором.",
    "essence": "В мире где данные = власть, хакеры становятся новыми революционерами.",
    "key_points": [
      "Глобальная сеть подпольных хакеров",
      "Борьба за цифровые права и приватность",
      "Эскалация от цифровых атак к физическим действиям"
    ]
  },
  "story_narrative": {
    "hook": "Анонимный манифест появляется в даркнете, призывая к цифровой революции.",
    "inciting_incident": "Массовый сбой инфраструктуры в Амстердаме раскрывает уязвимости систем.",
    "rising_action": [
      "Формирование альянсов между хакерскими группами",
      "Кибератаки на корпоративные дата-центры",
      "Физические диверсии против инфраструктуры наблюдения",
      "Международная охота за лидерами движения"
    ],
    "climax": "Одновременная атака на все основные серверы корпораций.",
    "falling_action": "Правительства вынуждены пойти на уступки в области приватности.",
    "resolution": "Новые законы о цифровых правах, но хакеры продолжают борьбу."
  },
  "characters": {
    "primary": [
      {
        "name": "Ghost Protocol",
        "role": "Лидер революции (анонимный)",
        "motivation": "Защитить цифровую свободу",
        "arc": "От одиночки к символу движения"
      },
      {
        "name": "Cipher Queen",
        "role": "Стратег кибервойны",
        "motivation": "Месть за потерю семьи от корпоративного шпионажа",
        "arc": "От мстителя к визионеру"
      }
    ],
    "supporting": [
      {
        "name": "Data Nomad",
        "role": "Связной между группами",
        "motivation": "Выжить в цифровом мире",
        "arc": "От изгоя к герою сопротивления"
      }
    ]
  },
  "themes": [
    "Приватность как фундаментальное право",
    "Технология может служить как контролю, так и свободе",
    "Цифровая революция неизбежна"
  ],
  "world_building": {
    "locations": [
      {
        "Amsterdam": "Центр европейского хакерского движения"
      },
      {
        "Dark Web Hubs": "Скрытые серверы по всему миру"
      },
      {
        "Corporate Data Centers": "Цели атак"
      }
    ],
    "technology": [
      {
        "Quantum Encryption": "Новые методы защиты данных"
      },
      {
        "Neural Hacking": "Прямой доступ к мозгу через импланты"
      },
      {
        "AI Guardians": "Искусственный интеллект для защиты систем"
      }
    ]
  },
  "timeline": [
    {
      "date": "2027-01-01",
      "event": "Публикация \"Digital Liberation Manifesto\""
    },
    {
      "date": "2027-03-15",
      "event": "Первая массовая кибератака"
    },
    {
      "date": "2027-06-01",
      "event": "Формирование \"Net Liberation Front\""
    },
    {
      "date": "2027-09-30",
      "event": "Глобальная операция \"Freedom Crash\""
    }
  ],
  "player_impact": {
    "choices": [
      {
        "join_revolution": "Стать частью хакерского сопротивления"
      },
      {
        "corporate_security": "Работать против революционеров"
      },
      {
        "neutral_observer": "Следить за развитием событий"
      }
    ],
    "consequences": [
      {
        "skill_unlocks": "Новые хакерские способности"
      },
      {
        "faction_reputation": "Изменения отношений с корпорациями и подпольем"
      },
      {
        "story_branches": "Доступ к альтернативным сюжетным линиям"
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 140879399,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "1.0.0",
      "date": "2025-12-14",
      "author": "content_writer",
      "changes": "Создан нарратив \"Подпольная хакерская революция\" с полной структурой истории."
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