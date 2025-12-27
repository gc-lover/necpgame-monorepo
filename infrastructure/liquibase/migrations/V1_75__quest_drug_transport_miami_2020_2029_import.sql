--liquibase formatted sql

--changeset backend:quest-drug-transport-miami-2020-2029-import runOnChange:true
--comment: Import quest "Перевозка наркотиков (Майами 2020-2029)" into database

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
    'content-narrative-quest-drug-transport-miami-2020-2029',
    '{
        "id": "content-narrative-quest-drug-transport-miami-2020-2029",
        "title": "Quest: Drug Transport Miami (Miami 2020-2029)",
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
            "drug-trafficking",
            "smuggling",
            "cartel",
            "high-stakes"
        ],
        "topics": [
            "drug-cartels",
            "smuggling-operations",
            "high-seas-transport",
            "corruption",
            "underworld-networks"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "smuggling-mechanics",
            "faction-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-smuggling-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/drug-transport-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "high"
    }',
    'content-narrative-quest-drug-transport-miami-2020-2029',
    '[Canon/Lore] Квест: Перевозка наркотиков (Майами 2020-2029)',
    'Майами 2020-2029 — город солнца и теней. Океанские волны скрывают миллиардные сделки, а прибрежные клубы служат фасадом для операций картелей.',
    'active',
    12,
    28,
    75,
    'hard',
    'narrative',
    'smuggling_operation',
    '{
        "experience": 28500,
        "currency": {
            "type": "eddies",
            "amount": 52000
        },
        "items": [
            {
                "name": "Smuggler''s Toolkit",
                "type": "quest_item",
                "rarity": "rare"
            }
        ]
    }',
    '[
        {
            "completed": "tutorial_basics"
        },
        {
            "level": 12
        },
        {
            "reputation": "underworld_aware"
        }
    ]',
    '[
        {
            "id": "choose_transport_method",
            "title": "Выбрать метод перевозки",
            "description": "Определить способ доставки груза в Майами",
            "type": "choice",
            "optional": false
        },
        {
            "id": "plan_route",
            "title": "Спланировать маршрут",
            "description": "Проложить безопасный путь через патрули береговой охраны",
            "type": "planning",
            "optional": false
        },
        {
            "id": "acquire_ship",
            "title": "Приобрести судно",
            "description": "Найти или арендовать подходящее судно для перевозки",
            "type": "procurement",
            "optional": false
        },
        {
            "id": "avoid_coast_guard",
            "title": "Избежать береговой охраны",
            "description": "Уклониться от патрулей и таможни",
            "type": "stealth_navigation",
            "success_rate": 0.7,
            "optional": false
        },
        {
            "id": "negotiate_buyers",
            "title": "Переговорить с покупателями",
            "description": "Установить контакт с местными дистрибьюторами",
            "type": "negotiation",
            "optional": false
        },
        {
            "id": "handle_competition",
            "title": "Обработать конкуренцию",
            "description": "Решить проблему с конкурирующими картелями",
            "type": "confrontation",
            "optional": true
        },
        {
            "id": "final_delivery",
            "title": "Совершить финальную доставку",
            "description": "Завершить операцию по перевозке груза",
            "type": "delivery",
            "optional": false
        }
    ]',
    '{
        "cartel_contact": {
            "speaker": "cartel_representative",
            "text": "Tu veux gagner beaucoup d''argent? On a une cargaison speciale qui arrive. Tres rentable, mais tres dangereux. T''es partant?",
            "choices": [
                {
                    "text": "Oui, je suis interesse. Quel est le plan?",
                    "next_node": "operation_details",
                    "faction_points": {
                        "cartels": 20
                    }
                },
                {
                    "text": "Ca semble trop risque. Peut-etre pas pour moi.",
                    "next_node": "polite_decline",
                    "faction_points": {
                        "independents": 5
                    }
                },
                {
                    "text": "Je pourrais informer les autorites...",
                    "next_node": "dangerous_choice",
                    "faction_points": {
                        "police": 15
                    }
                }
            ]
        }
    }',
    '{
        "loyal_smuggler": {
            "title": "Путь Верного Контрабандиста",
            "description": "Полная лояльность картелю, максимальная прибыль, максимальный риск",
            "requirements": {
                "cartel_loyalty": 60
            },
            "outcomes": [
                "cartel_promotion",
                "huge_profit",
                "underworld_respect"
            ],
            "rewards_modifier": 1.5
        },
        "double_agent": {
            "title": "Путь Двойного Агента",
            "description": "Сотрудничество с полицией под прикрытием картеля",
            "requirements": {
                "police_loyalty": 40,
                "cartel_infiltration": 30
            },
            "outcomes": [
                "police_hero",
                "cartel_betrayal_exposed",
                "moral_conflict"
            ],
            "rewards_modifier": 1.2
        },
        "independent_operator": {
            "title": "Путь Независимого Оператора",
            "description": "Сохранение независимости, собственная сеть распространения",
            "requirements": {
                "independent_network": 50
            },
            "outcomes": [
                "personal_empire",
                "freedom_maintained",
                "constant_threats"
            ],
            "rewards_modifier": 1.3
        },
        "whistleblower": {
            "title": "Путь Информатора",
            "description": "Предательство картеля ради справедливости",
            "requirements": {
                "police_alliance": 70
            },
            "outcomes": [
                "cartel_destruction",
                "personal_sacrifice",
                "justice_served"
            ],
            "rewards_modifier": 0.8
        }
    }',
    '{
        "miami_coastline": {
            "name": "Побережье Майами",
            "description": "Извилистый берег Флориды с множеством скрытых бухт и пляжей",
            "type": "coastal_route",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1918
            },
            "activities": [
                "smuggling_landings",
                "coast_guard_evasion",
                "cartel_meetings"
            ]
        },
        "biscayne_bay": {
            "name": "Бухта Бискейн",
            "description": "Защищенная бухта для скрытных операций и хранения груза",
            "type": "safe_haven",
            "coordinates": {
                "lat": 25.7317,
                "lng": -80.2089
            },
            "activities": [
                "cargo_transfer",
                "temporary_storage",
                "emergency_hideouts"
            ]
        },
        "everglades_access": {
            "name": "Доступ к Эверглейдс",
            "description": "Тропические болота как альтернативный маршрут доставки",
            "type": "alternative_route",
            "coordinates": {
                "lat": 25.5517,
                "lng": -80.7539
            },
            "activities": [
                "air_transport",
                "swamp_navigation",
                "wildlife_evasion"
            ]
        }
    }',
    '{
        "cartel_boss": {
            "name": "Эль Тигре",
            "role": "Босс колумбийского картеля",
            "personality": "ruthless, calculating, charismatic",
            "background": "Легендарный контрабандист с имплантами усиления",
            "dialogue_style": "spanish_accents, business_direct",
            "faction": "columbian_cartel"
        },
        "undercover_agent": {
            "name": "Детектив Сара Родригес",
            "role": "Тайный агент DEA",
            "personality": "determined, ethical, conflicted",
            "background": "Внедрена в картель под прикрытием",
            "dialogue_style": "professional_spanish, moral_questions",
            "faction": "police"
        },
        "veteran_smuggler": {
            "name": "Капитан Хуан Карлос",
            "role": "Опытный мореход",
            "personality": "grizzled, experienced, loyal",
            "background": "20 лет в контрабанде, пережил множество штормов",
            "dialogue_style": "sailor_slang, practical_advice",
            "faction": "smugglers"
        },
        "local_dealer": {
            "name": "Марио Быстрый Сантьяго",
            "role": "Местный дистрибьютор",
            "personality": "ambitious, street_smart, opportunistic",
            "background": "Начал с уличной торговли, теперь контролирует район",
            "dialogue_style": "miami_slang, dealmaker",
            "faction": "dealers"
        }
    }',
    '{
        "smugglers_toolkit": {
            "name": "Набор Контрабандиста",
            "type": "quest_equipment",
            "description": "Комплект инструментов для скрытной навигации и уклонения от патрулей",
            "rarity": "rare",
            "faction_bonus": "smugglers"
        },
        "encrypted_communicator": {
            "name": "Шифрованный Коммуникатор",
            "type": "quest_item",
            "description": "Устройство для безопасной связи с картелем",
            "rarity": "uncommon",
            "faction_bonus": "cartels"
        },
        "police_badge_fake": {
            "name": "Фальшивый Полицейский Значок",
            "type": "quest_disguise",
            "description": "Для проникновения в охраняемые зоны под прикрытием",
            "rarity": "rare",
            "faction_bonus": "double_agents"
        },
        "cartel_contract": {
            "name": "Контракт Картеля",
            "type": "quest_document",
            "description": "Документ, подтверждающий членство в картеле",
            "rarity": "epic",
            "faction_bonus": "cartels"
        }
    }',
    '{
        "coast_guard_patrol": {
            "title": "Патруль Береговой Охраны",
            "description": "Внезапная проверка прибрежной зоны дронами и катерами",
            "trigger_conditions": {
                "route_visibility": "high",
                "patrol_activity": "increased"
            },
            "outcomes": [
                "cargo_confiscation",
                "emergency_evasion",
                "cartel_retaliation"
            ]
        },
        "rival_cartel_attack": {
            "title": "Атака Соперничающего Картеля",
            "description": "Конкуренты пытаются перехватить груз силой",
            "trigger_conditions": {
                "territory_overlap": true,
                "cargo_value": "high"
            },
            "outcomes": [
                "territorial_war",
                "alliance_opportunities",
                "cargo_diversion"
            ]
        },
        "police_raid": {
            "title": "Полицейский Рейд",
            "description": "DEA проводит крупномасштабную операцию против картеля",
            "trigger_conditions": {
                "police_intelligence": "strong",
                "operation_scale": "large"
            },
            "outcomes": [
                "mass_arrests",
                "evidence_destruction",
                "underworld_upheaval"
            ]
        }
    }',
    '{
        "naval_chase": {
            "description": "Погоня на море с уклонением от патрулей и конкурентов",
            "difficulty": "hard",
            "rewards": "smuggling_experience"
        },
        "cartel_warfare": {
            "description": "Стрельба и тактические операции против конкурирующих групп",
            "difficulty": "medium",
            "rewards": "underworld_reputation"
        },
        "infiltration_missions": {
            "description": "Проникновение в охраняемые зоны и кража информации",
            "difficulty": "hard",
            "rewards": "intelligence_bonuses"
        }
    }',
    '{
        "cartel_empire": {
            "title": "Империя Картеля",
            "description": "Успешная интеграция в картель с ростом по карьерной лестнице",
            "requirements": {
                "cartel_loyalty": "dominant",
                "operation_success": "complete",
                "leadership_proven": true
            },
            "rewards": {
                "experience": 35000,
                "currency": {
                    "type": "eddies",
                    "amount": 75000
                },
                "items": [
                    {
                        "name": "Cartel Empire Ring",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "underworld_prominence",
                "increased_power",
                "constant_danger"
            ]
        },
        "police_victory": {
            "title": "Победа Полиции",
            "description": "Успешная операция под прикрытием с разоблачением картеля",
            "requirements": {
                "police_loyalty": "dominant",
                "cartel_infiltration": "successful",
                "evidence_collected": "complete"
            },
            "rewards": {
                "experience": 30000,
                "currency": {
                    "type": "eddies",
                    "amount": 45000
                },
                "items": [
                    {
                        "name": "Justice Medal of Honor",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "cartel_disruption",
                "personal_redemption",
                "ongoing_threats"
            ]
        },
        "independent_wealth": {
            "title": "Независимое Богатство",
            "description": "Успешная независимая операция с собственным бизнесом",
            "requirements": {
                "independent_network": "established",
                "personal_profit": "maximized",
                "cartel_loyalty": "minimal"
            },
            "rewards": {
                "experience": 32000,
                "currency": {
                    "type": "eddies",
                    "amount": 65000
                },
                "items": [
                    {
                        "name": "Independent Trader License",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "personal_wealth",
                "business_opportunities",
                "competitive_threats"
            ]
        },
        "prison_sentence": {
            "title": "Тюремное Заключение",
            "description": "Арест и осуждение за участие в наркотрафике",
            "requirements": {
                "operation_failure": "critical",
                "evidence_against": "overwhelming",
                "escape_attempts": "failed"
            },
            "rewards": {
                "experience": 15000,
                "currency": {
                    "type": "eddies",
                    "amount": 0
                },
                "items": [
                    {
                        "name": "Prison Release Papers",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "criminal_record",
                "underworld_rejection",
                "rehabilitation_opportunity"
            ]
        },
        "violent_end": {
            "title": "Насильственный Конец",
            "description": "Гибель в перестрелке или морской катастрофе",
            "requirements": {
                "confrontation_failed": true,
                "survival_chance": 0,
                "betrayal_exposed": true
            },
            "rewards": {
                "experience": 12000,
                "currency": {
                    "type": "eddies",
                    "amount": 10000
                },
                "items": [
                    {
                        "name": "Memorial Plaque",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "legacy_impact",
                "underworld_legend",
                "family_consequences"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_support",
        "smuggling_mechanics: advanced_navigation",
        "faction_system: underworld_dynamics",
        "combat_system: naval_specialized",
        "event_system: dynamic_threats"
    ]',
    '[
        "high_risk_high_reward_mechanics",
        "moral_choice_consequences",
        "faction_reputation_scaling",
        "police_infiltration_balance",
        "economic_simulation_accuracy"
    ]',
    '[
        "drug_trade_realism",
        "smuggling_route_accuracy",
        "cartel_representations",
        "police_procedure_accuracy",
        "moral_ambiguity_handling",
        "multiple_ending_accessibility"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-drug-transport-miami-2020-2029';
