--liquibase formatted sql

--changeset backend:quest-hurricane-survival-miami-2020-2029-import runOnChange:true
--comment: Import quest "Выживание в урагане (Майами 2020-2029)" into database

-- Insert quest definition
INSERT INTO gameplay.quest_definitions (
    id,
    metadata,
    quest_id,
    title,
    description,
    status,
    level_min,
    level_max,
    estimated_duration,
    difficulty,
    category,
    type,
    rewards,
    prerequisites,
    objectives,
    dialogue_tree,
    branching_paths,
    locations,
    npcs,
    items,
    events,
    combat_elements,
    endings,
    technical_requirements,
    balance_notes,
    qa_checklist,
    created_at,
    updated_at
) VALUES (
    'content-narrative-quest-hurricane-survival-miami-2020-2029',
    '{
        "id": "content-narrative-quest-hurricane-survival-miami-2020-2029",
        "title": "Quest: Hurricane Survival Miami (Miami 2020-2029)",
        "document_type": "content",
        "category": "narrative",
        "status": "approved",
        "version": "1.0.0",
        "last_updated": "2025-12-27T00:00:00Z",
        "concept_approved": true,
        "concept_reviewed_at": "2025-12-27T00:00:00Z",
        "owners": [
            {
                "role": "content_director",
                "contact": "content@necp.game"
            }
        ],
        "tags": [
            "narrative",
            "quest",
            "cyberpunk",
            "miami",
            "natural-disaster",
            "climate-change",
            "survival",
            "social-inequality"
        ],
        "topics": [
            "climate-disaster-survival",
            "corporate-disaster-response",
            "social-class-divide",
            "technological-aid",
            "community-resilience"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "disaster-mechanics",
            "environmental-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-climate-disaster-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/hurricane-survival-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "high"
    }',
    'content-narrative-quest-hurricane-survival-miami-2020-2029',
    '[Canon/Lore] Квест: Выживание в урагане (Майами 2020-2029)',
    'Майами 2020-2029 — город на линии огня климатических катастроф. Ураганы стали не просто природными явлениями.',
    'active',
    18,
    36,
    88,
    'hard',
    'narrative',
    'disaster_survival',
    '{
        "experience": 36500,
        "currency": {
            "type": "eddies",
            "amount": 62000
        },
        "items": [
            {
                "name": "Storm Survivor Medal",
                "type": "quest_item",
                "rarity": "epic"
            }
        ]
    }',
    '[
        {
            "completed": "tutorial_basics"
        },
        {
            "level": 18
        }
    ]',
    '[
        {
            "id": "witness_weather_manipulation",
            "title": "Стать свидетелем манипуляции погодой",
            "description": "Увидеть корпоративный саботаж систем предупреждения об урагане",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_survival_path",
            "title": "Выбрать путь выживания",
            "description": "Определить свою позицию в борьбе с надвигающимся ураганом",
            "type": "choice",
            "optional": false
        },
        {
            "id": "prepare_shelter",
            "title": "Подготовить укрытие",
            "description": "Найти или укрепить безопасное место для пережидания шторма",
            "type": "preparation_task",
            "optional": false
        },
        {
            "id": "rescue_civilians",
            "title": "Спасать гражданских",
            "description": "Эвакуировать 6 жителей из зоны опасности",
            "type": "rescue_operation",
            "count": 6,
            "optional": false
        },
        {
            "id": "expose_corporate_sabotage",
            "title": "Разоблачить корпоративный саботаж",
            "description": "Собрать доказательства манипуляции погодой корпорацией",
            "type": "investigation",
            "optional": false
        },
        {
            "id": "maintain_community_morale",
            "title": "Поддерживать моральный дух сообщества",
            "description": "Повысить уровень доверия в сообществе на 30%",
            "type": "social_task",
            "threshold": 30,
            "optional": true
        },
        {
            "id": "final_disaster_decision",
            "title": "Принять финальное решение в катастрофе",
            "description": "Определить исход борьбы с ураганом и его последствиями",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "storm_approaches": {
            "speaker": "worried_meteorologist",
            "text": "Mon Dieu! Les satellites montrent une anomalie dans le systeme de prediction! Quelqu''un manipule les donnees meteorologiques! Cette tempete n''est pas naturelle!",
            "choices": [
                {
                    "text": "Je vais aider a evacuer les gens et proteger la communaute.",
                    "next_node": "community_savior",
                    "faction_points": {
                        "protectors": 25
                    }
                },
                {
                    "text": "C''est une opportunite d''affaires. Les secours coutent cher.",
                    "next_node": "disaster_profiteer",
                    "faction_points": {
                        "exploiters": 20
                    }
                },
                {
                    "text": "Je vais enqueter sur cette manipulation meteorologique.",
                    "next_node": "truth_seeker",
                    "faction_points": {
                        "investigators": 18
                    }
                }
            ]
        }
    }',
    '{
        "disaster_hero": {
            "title": "Путь Героя Катастрофы",
            "description": "Самоотверженное спасение жителей и восстановление сообщества после урагана",
            "requirements": {
                "rescues_completed": 10
            },
            "outcomes": [
                "community_salvation",
                "heroic_legacy",
                "social_reform"
            ],
            "rewards_modifier": 1.4
        },
        "corporate_disaster_agent": {
            "title": "Путь Корпоративного Агента по Катастрофам",
            "description": "Работа на корпорации по контролю последствий урагана и извлечению выгоды",
            "requirements": {
                "corporate_loyalty": 70
            },
            "outcomes": [
                "corporate_profits",
                "disaster_monopolization",
                "wealth_accumulation"
            ],
            "rewards_modifier": 1.5
        },
        "environmental_revolutionary": {
            "title": "Путь Экологического Революционера",
            "description": "Борьба против климатического оружия и за экологическую справедливость",
            "requirements": {
                "investigation_success": 80
            },
            "outcomes": [
                "climate_justice",
                "corporate_exposure",
                "environmental_awareness"
            ],
            "rewards_modifier": 1.2
        },
        "survival_expert": {
            "title": "Путь Эксперта по Выживанию",
            "description": "Максимальная персональная подготовка и выживание любой ценой",
            "requirements": {
                "survival_skills": 75
            },
            "outcomes": [
                "personal_survival",
                "resource_hoarding",
                "adaptive_mastery"
            ],
            "rewards_modifier": 1.1
        }
    }',
    '{
        "downtown_miami_evacuation": {
            "name": "Эвакуационный Центр Эвриглайдс",
            "description": "Бывший аэропорт, превращенный в центр эвакуации с корпоративной охраной",
            "type": "emergency_shelter",
            "coordinates": {
                "lat": 25.7959,
                "lng": -80.2870
            },
            "activities": [
                "civilian_evacuation",
                "resource_distribution",
                "security_enforcement"
            ]
        },
        "corporate_bunker_complex": {
            "name": "Корпоративный Бункерный Комплекс",
            "description": "Подземный комплекс для элиты с автономными системами жизнеобеспечения",
            "type": "elite_shelter",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1939
            },
            "activities": [
                "elite_protection",
                "strategic_planning",
                "resource_hoarding"
            ]
        },
        "flooded_neighborhood": {
            "name": "Затопленный Район",
            "description": "Низменный район, полностью затопленный во время урагана",
            "type": "disaster_zone",
            "coordinates": {
                "lat": 25.7743,
                "lng": -80.1937
            },
            "activities": [
                "rescue_operations",
                "survival_navigation",
                "resource_scavenging"
            ]
        }
    }',
    '{
        "emergency_coordinator": {
            "name": "Капитан Эмма Торрес",
            "role": "Координатор Чрезвычайных Ситуаций",
            "personality": "authoritative, compassionate, overwhelmed",
            "background": "Ветеран служб спасения, борется с бюрократией и коррупцией",
            "dialogue_style": "command_decision, urgent_action",
            "faction": "protectors"
        },
        "corporate_executive": {
            "name": "Мистер Виктор Рейн",
            "role": "Корпоративный Руководитель",
            "personality": "calculating, privileged, detached",
            "background": "Директор по чрезвычайным ситуациям мегакорпорации",
            "dialogue_style": "corporate_detachment, profit_focused",
            "faction": "collaborators"
        },
        "neighborhood_leader": {
            "name": "Мария Мама Санчес",
            "role": "Лидер Сообщества",
            "personality": "resilient, community-focused, resourceful",
            "background": "Местная активистка, потеряла дом в предыдущем урагане",
            "dialogue_style": "grassroots_activism, neighborhood_pride",
            "faction": "resistors"
        },
        "weather_manipulator": {
            "name": "Доктор Элиас Шторм",
            "role": "Специалист по Манипуляции Погодой",
            "personality": "brilliant, unethical, ambitious",
            "background": "Бывший ученый, теперь работает на корпорацию по контролю климата",
            "dialogue_style": "scientific_detachment, experimental_enthusiasm",
            "faction": "exploiters"
        }
    }',
    '{
        "emergency_weather_scanner": {
            "name": "Аварийный Сканер Погоды",
            "type": "survival_tool",
            "description": "Устройство для отслеживания урагана и поиска безопасных зон",
            "rarity": "rare",
            "faction_bonus": "protectors"
        },
        "corporate_evacuation_pass": {
            "name": "Корпоративный Пропуск на Эвакуацию",
            "type": "access_token",
            "description": "Пропуск, дающий доступ к элитным укрытиям корпорации",
            "rarity": "epic",
            "faction_bonus": "collaborators"
        },
        "community_radio_transmitter": {
            "name": "Радиопередатчик Сообщества",
            "type": "communication_device",
            "description": "Устройство для координации спасательных операций в сообществе",
            "rarity": "uncommon",
            "faction_bonus": "resistors"
        },
        "weather_manipulation_detector": {
            "name": "Детектор Манипуляции Погодой",
            "type": "analysis_tool",
            "description": "Устройство для обнаружения искусственных изменений в погоде",
            "rarity": "legendary",
            "faction_bonus": "investigators"
        }
    }',
    '{
        "levee_failure": {
            "title": "Провал Плотин",
            "description": "Искусственный прорыв дамб, вызывающий массовые наводнения",
            "trigger_conditions": {
                "storm_intensity": "extreme",
                "sabotage_suspected": true
            },
            "outcomes": [
                "massive_flooding",
                "emergency_evacuation",
                "infrastructure_damage"
            ]
        },
        "power_grid_collapse": {
            "title": "Коллапс Электросети",
            "description": "Полный blackout города из-за повреждений инфраструктуры",
            "trigger_conditions": {
                "wind_speeds": "catastrophic",
                "grid_vulnerability": "high"
            },
            "outcomes": [
                "communication_breakdown",
                "looting_increase",
                "rescue_difficulty"
            ]
        },
        "elite_rescue_operation": {
            "title": "Элитная Спасательная Операция",
            "description": "Корпорация проводит эксклюзивную эвакуацию для VIP-персон",
            "trigger_conditions": {
                "corporate_influence": "dominant",
                "public_dissatisfaction": "rising"
            },
            "outcomes": [
                "social_inequality_exacerbated",
                "public_outrage",
                "resistance_movement"
            ]
        }
    }',
    '{
        "rescue_operations": {
            "description": "Спасательные миссии в условиях экстремальной погоды и разрушений",
            "difficulty": "extreme",
            "rewards": "heroic_reputation"
        },
        "sabotage_prevention": {
            "description": "Защита критической инфраструктуры от корпоративных диверсантов",
            "difficulty": "hard",
            "rewards": "community_protection"
        },
        "resource_defense": {
            "description": "Охрана припасов и укрытий от мародеров и спекулянтов",
            "difficulty": "medium",
            "rewards": "survival_security"
        }
    }',
    '{
        "heroic_salvation": {
            "title": "Героическое Спасение",
            "description": "Самоотверженное спасение всего сообщества и восстановление справедливости",
            "requirements": {
                "community_saved": "complete",
                "corporate_exposed": true,
                "social_reform": "achieved"
            },
            "rewards": {
                "experience": 42000,
                "currency": {
                    "type": "eddies",
                    "amount": 78000
                },
                "items": [
                    {
                        "name": "Hurricane Hero Memorial",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "community_unity",
                "social_justice",
                "disaster_resilience"
            ]
        },
        "corporate_dominance": {
            "title": "Корпоративное Доминирование",
            "description": "Контроль корпорации над восстановлением и извлечение максимальной выгоды",
            "requirements": {
                "corporate_control": "absolute",
                "disaster_monopolization": "complete",
                "profits_maximized": true
            },
            "rewards": {
                "experience": 38500,
                "currency": {
                    "type": "eddies",
                    "amount": 92000
                },
                "items": [
                    {
                        "name": "Corporate Disaster Empire Seal",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "corporate_hegemony",
                "social_division",
                "environmental_exploitation"
            ]
        },
        "revolutionary_uprising": {
            "title": "Революционное Восстание",
            "description": "Народное восстание против корпораций и справедливое перераспределение ресурсов",
            "requirements": {
                "investigation_successful": true,
                "public_awakening": "achieved",
                "resistance_victory": "complete"
            },
            "rewards": {
                "experience": 39500,
                "currency": {
                    "type": "eddies",
                    "amount": 68000
                },
                "items": [
                    {
                        "name": "Climate Justice Revolution Flag",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "social_revolution",
                "climate_awareness",
                "equitable_recovery"
            ]
        },
        "survival_specialist": {
            "title": "Специалист по Выживанию",
            "description": "Личное выживание и адаптация к постапокалиптическому миру после урагана",
            "requirements": {
                "personal_survival": "maximized",
                "adaptive_strategies": "mastered",
                "resource_independence": "achieved"
            },
            "rewards": {
                "experience": 36500,
                "currency": {
                    "type": "eddies",
                    "amount": 72000
                },
                "items": [
                    {
                        "name": "Ultimate Survivor Implant",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "personal_empowerment",
                "survival_expertise",
                "adaptive_mastery"
            ]
        },
        "total_devastation": {
            "title": "Полное Разрушение",
            "description": "Катastroфические последствия урагана и полный коллапс социального порядка",
            "requirements": {
                "disaster_uncontrolled": true,
                "social_breakdown": "complete",
                "recovery_impossible": "confirmed"
            },
            "rewards": {
                "experience": 22000,
                "currency": {
                    "type": "eddies",
                    "amount": 18000
                },
                "items": [
                    {
                        "name": "Apocalypse Witness Badge",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "societal_collapse",
                "environmental_catastrophe",
                "human_tragedy"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_emergency",
        "disaster_mechanics: dynamic_weather",
        "survival_systems: environmental_hazards",
        "social_mechanics: community_dynamics",
        "investigation_system: weather_manipulation"
    ]',
    '[
        "disaster_progression_realism",
        "social_inequality_mechanics",
        "survival_vs_heroism_balance",
        "corporate_influence_scaling",
        "community_morale_dynamics"
    ]',
    '[
        "hurricane_disaster_accuracy",
        "climate_change_realism",
        "social_inequality_handling",
        "corporate_disaster_response",
        "community_resilience_themes",
        "survival_mechanics_balance"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-hurricane-survival-miami-2020-2029';
