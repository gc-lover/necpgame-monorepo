-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\stories\transhumanist-manifesto.yaml
-- Generated: 2025-12-14T16:03:09.092552

BEGIN;

-- Lore: story-transhumanist-manifesto
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'story-transhumanist-manifesto',
    'Трансгуманистический манифест',
    'canon',
    'narrative-story',
    '{
  "metadata": {
    "id": "story-transhumanist-manifesto",
    "title": "Трансгуманистический манифест",
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
      "transhumanist",
      "manifesto",
      "evolution",
      "philosophy"
    ],
    "topics": [
      "human_evolution",
      "technological_philosophy",
      "post_humanism"
    ],
    "related_systems": [
      "narrative-service",
      "progression-service",
      "social-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/stories/transhumanist-manifesto.md",
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
    "problem": "Философское движение за преодоление человеческих ограничений через технологию сталкивается с реальностью.",
    "goal": "Исследовать идеи трансгуманизма через призму личного выбора и глобальных последствий.",
    "essence": "Человечество стоит на пороге следующей эволюционной стадии, но цена может быть слишком высока.",
    "key_points": [
      "Философские дебаты о природе человека",
      "Конфликт между естественной эволюцией и технологической",
      "Этические дилеммы бессмертия и совершенства"
    ]
  },
  "story_narrative": {
    "hook": "Анонимный манифест \"Beyond Human Limits\" становится вирусным хитом в сети.",
    "inciting_incident": "Автор манифеста предлагает игроку стать первым пост-человеком.",
    "rising_action": [
      "Погружение в мир трансгуманистических идей и технологий",
      "Конфликты с традиционалистами и религиозными группами",
      "Личные трансформации и потеря человечности",
      "Глобальные последствия массового принятия идей"
    ],
    "climax": "Выбор между спасением человечества или его трансформацией.",
    "falling_action": "Реализация выбранного пути меняет мир навсегда.",
    "resolution": "Новый этап человеческой истории или возврат к традиционным ценностям."
  },
  "characters": {
    "primary": [
      {
        "name": "Dr. Elysium",
        "role": "Автор манифеста, трансгуманистический философ",
        "motivation": "Освободить человечество от биологических ограничений",
        "arc": "От идеалиста к тирану или мученику"
      },
      {
        "name": "The Candidate",
        "role": "Игрок, первый кандидат на полную трансформацию",
        "motivation": "Личные амбиции vs страх потери идентичности",
        "arc": "От обычного человека к пост-человеческому существу или обратно"
      }
    ],
    "supporting": [
      {
        "name": "Traditionalist Leader",
        "role": "Противник трансгуманизма, защитник человеческой природы",
        "motivation": "Сохранить аутентичность человеческого опыта",
        "arc": "От противника к союзнику или жертве прогресса"
      }
    ]
  },
  "themes": [
    "Что значит быть человеком в эпоху технологий",
    "Свобода выбора vs биологическая детерминация",
    "Цена прогресса и неизбежность изменения"
  ],
  "world_building": {
    "transhumanist_factions": [
      {
        "Radical_Transhumanists": "Полная замена биологии технологией"
      },
      {
        "Moderate_Enhancers": "Улучшение без потери человечности"
      },
      {
        "Natural_Preservationists": "Отказ от любых модификаций"
      }
    ],
    "technologies": [
      {
        "consciousness_upload": "Загрузка разума в цифровые носители"
      },
      {
        "genetic_rewriting": "Полная перезапись ДНК"
      },
      {
        "neural_expansion": "Увеличение когнитивных способностей"
      }
    ]
  },
  "timeline": [
    {
      "date": "2035-01-01",
      "event": "Публикация манифеста \"Beyond Human Limits\""
    },
    {
      "date": "2035-06-15",
      "event": "Первые эксперименты с consciousness transfer"
    },
    {
      "date": "2036-03-01",
      "event": "Формирование глобального трансгуманистического движения"
    },
    {
      "date": "2036-12-25",
      "event": "Первый успешный consciousness upload"
    },
    {
      "date": "2037-07-04",
      "event": "Кульминация - решение о будущем человечества"
    }
  ],
  "player_impact": {
    "choices": [
      {
        "embrace_transhumanism": "Полная трансформация в пост-человека"
      },
      {
        "moderate_enhancement": "Ограниченные улучшения сохраняя человечность"
      },
      {
        "reject_technology": "Возврат к естественной форме"
      }
    ],
    "consequences": [
      {
        "identity_changes": "Изменение восприятия себя и окружающего мира"
      },
      {
        "social_isolation": "Потеря связей с unmodified людьми"
      },
      {
        "philosophical_awakening": "Новое понимание существования"
      }
    ]
  },
  "appendix": {
    "glossary": [],
    "references": [],
    "decisions": []
  },
  "implementation": {
    "github_issue": 140875775,
    "needs_task": false,
    "queue_reference": [],
    "blockers": []
  },
  "history": [
    {
      "version": "1.0.0",
      "date": "2025-12-14",
      "author": "content_writer",
      "changes": "Создан нарратив \"Трансгуманистический манифест\" с полной структурой философской эволюции."
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