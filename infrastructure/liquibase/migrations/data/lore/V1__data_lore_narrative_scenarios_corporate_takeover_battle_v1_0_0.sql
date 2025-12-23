-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\corporate-takeover-battle.yaml
-- Generated: 2025-12-21T02:15:39.694615

BEGIN;

-- Lore: scenario-corporate-takeover-battle
INSERT INTO narrative.lore_entries (lore_id, title, document_type, category,
                                    content_data, version)
VALUES ('scenario-corporate-takeover-battle',
        'Корпоративный переворот: Битва за контроль',
        'canon',
        'narrative-scenario',
        '{
      "metadata": {
        "id": "scenario-corporate-takeover-battle",
        "title": "Корпоративный переворот: Битва за контроль",
        "document_type": "canon",
        "category": "narrative-scenario",
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
          "corporate",
          "takeover",
          "battle",
          "power-struggle"
        ],
        "topics": [
          "corporate_warfare",
          "boardroom_intrigue",
          "hostile_takeover"
        ],
        "related_systems": [
          "narrative-service",
          "economy-service",
          "combat-service"
        ],
        "related_documents": [],
        "source": "shared/docs/knowledge/canon/narrative/scenarios/corporate-takeover-battle.md",
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
        "problem": "Игрок оказывается в центре корпоративного переворота в европейской корпорации.",
        "goal": "Выбрать сторону и повлиять на исход борьбы за контроль над компанией.",
        "essence": "Корпоративная война - это не только доски объявлений, но и уличные бои, кибератаки и предательства.",
        "key_points": [
          "Множество фракций внутри корпорации",
          "Комбинация boardroom politics и street warfare",
          "Долгосрочные последствия для экономики Европы"
        ]
      },
      "scenario_structure": {
        "setup": {
          "player_background": "Независимый наемник получает контракт от одной из фракций",
          "initial_choice": "Выбрать сторону или остаться нейтральным",
          "stakes": "Контроль над технологией, влияющей на всю европейскую экономику"
        },
        "phases": [
          {
            "phase_1": "Подготовка",
            "description": "Сбор информации, формирование альянсов, подготовка ресурсов",
            "duration": "3 игровых дня"
          },
          {
            "phase_2": "Теневая война",
            "description": "Кибератаки, саботаж, тайные встречи",
            "duration": "5 игровых дней"
          },
          {
            "phase_3": "Открытый конфликт",
            "description": "Физические столкновения, захват зданий",
            "duration": "2 игровых дня"
          },
          {
            "phase_4": "Разрешение",
            "description": "Финальная битва, определение победителя",
            "duration": "1 игровой день"
          }
        ],
        "victory_conditions": [
          {
            "faction_victory": "Выбранная фракция захватывает контроль"
          },
          {
            "neutral_success": "Уцелеть и получить выгоду от обеих сторон"
          },
          {
            "compromise": "Достичь мирного решения с разделением активов"
          }
        ]
      },
      "factions": [
        {
          "name": "Традиционалисты",
          "leader": "Старый CEO, консервативный подход",
          "motivation": "Сохранить статус-кво и европейские традиции",
          "strengths": "Политические связи, лояльные сотрудники",
          "weaknesses": "Технологическая отсталость, сопротивление изменениям"
        },
        {
          "name": "Инноваторы",
          "leader": "Молодой CTO, технологический визионер",
          "motivation": "Внедрить радикальные инновации для глобального доминирования",
          "strengths": "Передовые технологии, поддержка молодежи",
          "weaknesses": "Финансовая нестабильность, непопулярность среди старых кадров"
        },
        {
          "name": "Внешние инвесторы",
          "leader": "Международный консорциум",
          "motivation": "Приобрести активы для диверсификации",
          "strengths": "Огромный капитал, международная поддержка",
          "weaknesses": "Отсутствие локального понимания, культурные барьеры"
        }
      ],
      "player_roles": [
        {
          "mercenary": "Прямое участие в боевых действиях"
        },
        {
          "infiltrator": "Шпионаж и саботаж"
        },
        {
          "diplomat": "Посредничество и переговоры"
        },
        {
          "hacker": "Кибервойна и информационный контроль"
        }
      ],
      "consequences": {
        "economic": [
          {
            "market_disruption": "Изменение цен на акции, экономическая нестабильность"
          },
          {
            "technology_release": "Утечка или захват инноваций"
          },
          {
            "job_market": "Массовые увольнения или новые возможности"
          }
        ],
        "political": [
          {
            "government_involvement": "Вмешательство европейских регуляторов"
          },
          {
            "international_tensions": "Вовлечение других стран"
          },
          {
            "policy_changes": "Новые законы о корпоративном контроле"
          }
        ],
        "social": [
          {
            "public_opinion": "Восприятие корпораций обществом"
          },
          {
            "labor_movements": "Активизация профсоюзов"
          },
          {
            "media_coverage": "Глобальное освещение событий"
          }
        ]
      },
      "player_choices": [
        {
          "ally_with_traditionalists": "Стабильность, но медленное развитие"
        },
        {
          "ally_with_innovators": "Риск, но потенциал роста"
        },
        {
          "ally_with_investors": "Прибыль, но потеря независимости"
        },
        {
          "play_both_sides": "Максимальная выгода, максимальный риск"
        },
        {
          "sabotage_all": "Хаос для личной выгоды"
        }
      ],
      "replayability": [
        {
          "multiple_endings": "8 разных исходов в зависимости от выборов"
        },
        {
          "scalable_difficulty": "Уровень вовлеченности корпораций"
        },
        {
          "random_events": "Неожиданные повороты (предательства, внешние угрозы)"
        },
        {
          "player_impact": "Решения влияют на будущие сценарии"
        }
      ],
      "timeline": [
        {
          "day_1": "Получение начального контракта"
        },
        {
          "day_3": "Первая встреча с фракциями"
        },
        {
          "day_5": "Начало теневой войны"
        },
        {
          "day_8": "Эскалация к открытому конфликту"
        },
        {
          "day_10": "Финальное разрешение"
        }
      ],
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
          "changes": "Создан нарратив сценария \"Корпоративный переворот\" с полной структурой конфликта."
        }
      ],
      "validation": {
        "checksum": "",
        "schema_version": "1.0"
      }
    }'::jsonb,
        1) ON CONFLICT (lore_id) DO
UPDATE SET
    title = EXCLUDED.title,
    document_type = EXCLUDED.document_type,
    category = EXCLUDED.category,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;