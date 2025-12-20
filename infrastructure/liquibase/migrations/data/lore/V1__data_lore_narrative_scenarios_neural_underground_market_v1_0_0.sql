-- Issue: #40, #552, #558, #559, #560, #561, #562, #563, #564
-- Import lore from: narrative\scenarios\neural-underground-market.yaml
-- Generated: 2025-12-21T02:15:39.808391

BEGIN;

-- Lore: scenario-neural-underground-market
INSERT INTO narrative.lore_entries (
    lore_id, title, document_type, category,
    content_data, version
)
VALUES (
    'scenario-neural-underground-market',
    'Нейральный подпольный рынок',
    'canon',
    'narrative-scenario',
    '{
  "metadata": {
    "id": "scenario-neural-underground-market",
    "title": "Нейральный подпольный рынок",
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
      "neural",
      "underground",
      "market",
      "cybernetics"
    ],
    "topics": [
      "black_market",
      "cybernetic_enhancement",
      "digital_economy"
    ],
    "related_systems": [
      "narrative-service",
      "economy-service",
      "progression-service"
    ],
    "related_documents": [],
    "source": "shared/docs/knowledge/canon/narrative/scenarios/neural-underground-market.md",
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
    "problem": "Игрок строит империю черного рынка нейральных имплантов в цифровую эпоху.",
    "goal": "Создать и поддерживать подпольную сеть торговли запрещенными кибернетическими технологиями.",
    "essence": "В мире где разум можно модифицировать, черный рынок технологий становится воротами в новую реальность.",
    "key_points": [
      "Торговля экспериментальными нейральными имплантами",
      "Конкуренция с корпоративными монополиями",
      "Этические последствия торговли сознанием"
    ]
  },
  "scenario_structure": {
    "setup": {
      "player_background": "Бывший корпоративный техник с доступом к запрещенным технологиям",
      "initial_choice": "Выбрать специализацию (оффensive, defensive, или experimental implants)",
      "stakes": "Контроль над рынком сознания и будущее человеческой эволюции"
    },
    "phases": [
      {
        "phase_1": "Основание",
        "description": "Создание первой лаборатории и вербовка команды техников",
        "duration": "2 игровых дня"
      },
      {
        "phase_2": "Расширение",
        "description": "Привлечение клиентов и расширение ассортимента",
        "duration": "4 игровых дня"
      },
      {
        "phase_3": "Конкуренция",
        "description": "Борьба с другими поставщиками и корпоративными рейдами",
        "duration": "3 игровых дня"
      },
      {
        "phase_4": "Империя",
        "description": "Управление глобальной сетью и инновации в технологиях",
        "duration": "5+ игровых дней"
      }
    ],
    "victory_conditions": [
      {
        "market_dominance": "Монополия на черном рынке нейральных имплантов"
      },
      {
        "technological_breakthrough": "Создание революционной технологии"
      },
      {
        "corporate_takeover": "Поглощение легального рынка корпорациями"
      }
    ]
  },
  "economic_mechanics": {
    "implant_types": [
      {
        "offensive": "Боевые импланты для улучшения реакции и точности"
      },
      {
        "defensive": "Защитные системы, firewall для нейральной сети"
      },
      {
        "experimental": "Новые способности (телепатия, предвидение, control)"
      }
    ],
    "pricing_model": [
      {
        "base_cost": "Стоимость материалов и разработки"
      },
      {
        "risk_premium": "Надбавка за нелегальность и опасность"
      },
      {
        "customization": "Персонализация под клиента"
      }
    ],
    "market_dynamics": [
      {
        "supply_scarcity": "Редкие компоненты влияют на цены"
      },
      {
        "demand_fluctuation": "Популярность технологий меняется"
      },
      {
        "competition_impact": "Действия конкурентов влияют на рынок"
      }
    ]
  },
  "risks": [
    {
      "corporate_raids": "Атаки от Militech или Arasaka security teams"
    },
    {
      "implant_failures": "Риск смерти клиента от некачественных имплантов"
    },
    {
      "betrayal": "Предательство членов команды"
    },
    {
      "addiction": "Зависимость от постоянных апгрейдов"
    }
  ],
  "factions": [
    {
      "name": "Corporate Enforcers",
      "type": "Антагонист",
      "motivation": "Уничтожить подпольный рынок и вернуть монополию",
      "tactics": "Рейды, подстава клиентов, промышленный шпионаж"
    },
    {
      "name": "Rival Syndicates",
      "type": "Конкуренты",
      "motivation": "Захват доли рынка через насилие или инновации",
      "tactics": "Саботаж, наем киллеров, подделка технологий"
    },
    {
      "name": "Client Base",
      "type": "Клиенты",
      "motivation": "Доступ к запрещенным технологиям",
      "tactics": "Лояльность, рекомендации, защита от корпораций"
    }
  ],
  "player_roles": [
    {
      "black_market_dealer": "Фокус на торговле и отношениях с клиентами"
    },
    {
      "neural_engineer": "Специализация на разработке и улучшении имплантов"
    },
    {
      "network_coordinator": "Управление логистикой и безопасностью"
    },
    {
      "innovation_lead": "Исследования новых технологий и breakthrough"
    }
  ],
  "consequences": {
    "technological": [
      {
        "implant_evolution": "Создание новых типов нейральных технологий"
      },
      {
        "black_market_innovation": "Ускоренное развитие подпольных технологий"
      },
      {
        "corporate_response": "Разработка контр-технологий корпорациями"
      }
    ],
    "social": [
      {
        "enhancement_divide": "Углубление раскола между enhanced и natural"
      },
      {
        "underground_community": "Формирование субкультуры модифицированных"
      },
      {
        "ethical_debates": "Общественные дискуссии о модификации сознания"
      }
    ],
    "economic": [
      {
        "market_disruption": "Влияние на легальную экономику имплантов"
      },
      {
        "wealth_accumulation": "Огромное состояние от нелегальной торговли"
      },
      {
        "resource_redistribution": "Перераспределение богатства в подполье"
      }
    ]
  },
  "player_choices": [
    {
      "quality_focus": "Лучшие импланты по премиум ценам"
    },
    {
      "volume_business": "Массовое производство по низким ценам"
    },
    {
      "ethical_dealing": "Избегать особо опасных модификаций"
    },
    {
      "radical_innovation": "Экспериментировать с consciousness-altering tech"
    }
  ],
  "replayability": [
    {
      "multiple_endings": "Легализация бизнеса, уничтожение корпорациями, technological singularity"
    },
    {
      "technology_trees": "Разные пути развития нейральных технологий"
    },
    {
      "client_stories": "Уникальные нарративы клиентов и их трансформации"
    },
    {
      "market_events": "Случайные события влияющие на экономику"
    }
  ],
  "timeline": [
    {
      "day_1": "Первая сделка с экспериментальным имплантом"
    },
    {
      "day_3": "Привлечение первого техник в команду"
    },
    {
      "day_7": "Первый рейд корпоративной безопасности"
    },
    {
      "day_14": "Запуск массового производства"
    },
    {
      "day_30": "Конфликт с конкурентами"
    }
  ],
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
      "changes": "Создан нарратив сценария \"Нейральный подпольный рынок\" с полной структурой торговли сознанием."
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