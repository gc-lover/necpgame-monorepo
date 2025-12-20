-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\stories\human-augmentation-revolution.yaml
-- Generated: 2025-12-21T02:15:39.902503

BEGIN;

-- Lore: story-human-augmentation-revolution
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'story-human-augmentation-revolution',
    'Революция аугментаций: Эволюция человека',
    'canon',
    'narrative-story',
    '{
  "metadata": {
    "id": "story-human-augmentation-revolution",
    "title": "Революция аугментаций: Эволюция человека",
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
      "human",
      "augmentation",
      "revolution",
      "evolution",
      "transhumanism"
    ],
    "topics": [
      "human_enhancement",
      "technological_evolution",
      "social_divide"
    ],
    "related_systems": [
      "narrative-service",
      "progression-service",
      "social-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/stories/human-augmentation-revolution.md",
    "visibility": "internal",
    "audience": [
      "concept",
      "narrative",
      "liveops"
    ],
    "risk_level": "critical"
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
    "problem": "Массовое внедрение нейральных имплантов меняет общество, создавая классовое разделение.",
    "goal": "Исследовать последствия технологической эволюции человека и социальные конфликты.",
    "essence": "Прогресс не делает нас лучше - он просто меняет правила игры.",
    "key_points": [
      "Разделение общества на augmented и natural",
      "Этические дебаты о человеческой природе",
      "Борьба за равенство в эпоху трансгуманизма"
    ]
  },
  "story_narrative": {
    "hook": "Первый ребенок рождается с врожденными нейральными имплантами.",
    "inciting_incident": "Корпорация выпускает доступные аугментации, доступные только элите.",
    "rising_action": [
      "Рост неравенства между augmented и natural людьми",
      "Формирование движений сопротивления и радикальных групп",
      "Черный рынок имплантов и подпольные модификации",
      "Правительственные попытки регулирования технологии"
    ],
    "climax": "Восстание natural людей против augmented привилегированных.",
    "falling_action": "Переговоры о равенстве и доступе к технологиям.",
    "resolution": "Новое общество, где аугментации доступны всем, но ценой изменения человеческой природы."
  },
  "characters": {
    "primary": [
      {
        "name": "Dr. Lena Voss",
        "role": "Изобретатель breakthrough аугментаций",
        "motivation": "Улучшить человечество через технологию",
        "arc": "От идеалиста к реалисту, осознающему социальные последствия"
      },
      {
        "name": "Marcus \"Natural\" Kane",
        "role": "Лидер движения против аугментаций",
        "motivation": "Сохранить \"чистоту\" человеческой природы",
        "arc": "От радикала к прагматику, ищущему компромисс"
      }
    ],
    "supporting": [
      {
        "name": "Enhanced Child",
        "role": "Первый ребенок с врожденными имплантами",
        "motivation": "Понять свою идентичность",
        "arc": "От confused youth к bridge between worlds"
      }
    ]
  },
  "themes": [
    "Что значит быть человеком в эпоху технологий",
    "Прогресс всегда создает новые формы неравенства",
    "Технология меняет не только тела, но и общество"
  ],
  "world_building": {
    "augmentations": [
      {
        "Neural Implants": "Улучшение памяти и обучения"
      },
      {
        "Physical Enhancements": "Сверхсила и скорость"
      },
      {
        "Sensory Upgrades": "Новые формы восприятия"
      },
      {
        "Lifespan Extension": "Значительное увеличение продолжительности жизни"
      }
    ],
    "social_structure": [
      {
        "Elite Augmented": "Богатые с полным набором улучшений"
      },
      {
        "Basic Enhanced": "Средний класс с ограниченными модификациями"
      },
      {
        "Natural Purists": "Отказывающиеся от технологий"
      },
      {
        "Underground Modified": "Самодельные и опасные аугментации"
      }
    ]
  },
  "timeline": [
    {
      "date": "2032-01-01",
      "event": "Рождение первого Enhanced ребенка"
    },
    {
      "date": "2032-06-15",
      "event": "Массовый релиз доступных аугментаций"
    },
    {
      "date": "2033-01-01",
      "event": "Первые случаи дискриминации natural людей"
    },
    {
      "date": "2033-09-15",
      "event": "Формирование движения сопротивления"
    },
    {
      "date": "2034-03-01",
      "event": "Восстание и гражданская война"
    },
    {
      "date": "2034-12-31",
      "event": "Мирное соглашение и равенство"
    }
  ],
  "player_impact": {
    "choices": [
      {
        "embrace_augmentation": "Стать полностью enhanced"
      },
      {
        "resist_technology": "Остаться natural"
      },
      {
        "moderate_approach": "Выбрать ограниченные улучшения"
      }
    ],
    "consequences": [
      {
        "social_divide": "Разные социальные круги в зависимости от выбора"
      },
      {
        "gameplay_mechanics": "Разные способности и возможности"
      },
      {
        "story_branches": "Альтернативные сюжетные линии для разных типов персонажей"
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
      "changes": "Создан нарратив \"Революция аугментаций\" с полной структурой трансгуманизма."
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