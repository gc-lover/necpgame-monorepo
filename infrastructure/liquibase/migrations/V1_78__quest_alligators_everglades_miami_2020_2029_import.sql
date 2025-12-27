--liquibase formatted sql

--changeset backend:quest-alligators-everglades-miami-2020-2029-import runOnChange:true
--comment: Import quest "Аллигаторы Эверглейдс (Майами 2020-2029)" into database

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
    'content-narrative-quest-alligators-everglades-miami-2020-2029',
    '{
        "id": "content-narrative-quest-alligators-everglades-miami-2020-2029",
        "title": "Quest: Alligators Everglades Miami (Miami 2020-2029)",
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
            "wildlife",
            "everglades",
            "ecology",
            "poaching",
            "bio-engineering"
        ],
        "topics": [
            "wildlife-conservation",
            "bio-engineering-ethics",
            "poaching-syndicates",
            "environmental-corruption",
            "human-wildlife-conflict"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "wildlife-mechanics",
            "environmental-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-wildlife-conservation-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/alligators-everglades-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "high"
    }',
    'content-narrative-quest-alligators-everglades-miami-2020-2029',
    '[Canon/Lore] Квест: Аллигаторы Эверглейдс (Майами 2020-2029)',
    'Эверглейдс 2020-2029 — древние болота Флориды, ставшие полигоном для экспериментов корпораций.',
    'active',
    14,
    30,
    78,
    'hard',
    'narrative',
    'wildlife_conservation',
    '{
        "experience": 29500,
        "currency": {
            "type": "eddies",
            "amount": 48000
        },
        "items": [
            {
                "name": "Ancient Gator Scale",
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
            "level": 14
        },
        {
            "reputation": "nature_aware"
        }
    ]',
    '[
        {
            "id": "witness_bio_experiment",
            "title": "Стать свидетелем био-эксперимента",
            "description": "Увидеть незаконный эксперимент корпорации над аллигаторами",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_nature_path",
            "title": "Выбрать путь природы",
            "description": "Определить свою позицию в конфликте экологии и технологий",
            "type": "choice",
            "optional": false
        },
        {
            "id": "explore_everglades",
            "title": "Исследовать Эверглейдс",
            "description": "Проникнуть вглубь болот и изучить местную экосистему",
            "type": "exploration",
            "danger_level": "extreme",
            "optional": false
        },
        {
            "id": "communicate_with_gators",
            "title": "Общаться с аллигаторами",
            "description": "Установить контакт с модифицированными разумными аллигаторами",
            "type": "animal_communication",
            "success_rate": 0.6,
            "optional": false
        },
        {
            "id": "confront_poachers",
            "title": "Противостоять браконьерам",
            "description": "Остановить группу браконьеров, охотящихся на редких аллигаторов",
            "type": "confrontation",
            "optional": false
        },
        {
            "id": "restore_ecosystem",
            "title": "Восстановить экосистему",
            "description": "Очистить болота от токсичных отходов корпораций",
            "type": "environmental_restoration",
            "optional": true
        },
        {
            "id": "final_nature_decision",
            "title": "Принять финальное решение природы",
            "description": "Определить будущее Эверглейдс и аллигаторов",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "bio_engineering_discovery": {
            "speaker": "distressed_scientist",
            "text": "Mon Dieu! Ces aligators... ils ont ete modifies genetiquement! Des implants neuraux, des mutations controlees. C''est de la folie pure!",
            "choices": [
                {
                    "text": "Je dois proteger ces creatures. La nature doit survivre!",
                    "next_node": "conservation_activist",
                    "faction_points": {
                        "ecologists": 25
                    }
                },
                {
                    "text": "Ca pourrait etre lucratif. Ces aligators valent une fortune.",
                    "next_node": "poacher_opportunity",
                    "faction_points": {
                        "hunters": 20
                    }
                },
                {
                    "text": "C''est fascinant. Je veux en savoir plus sur ces modifications.",
                    "next_node": "scientific_curiosity",
                    "faction_points": {
                        "researchers": 18
                    }
                }
            ]
        }
    }',
    '{
        "ecosystem_guardian": {
            "title": "Путь Стража Экосистемы",
            "description": "Защита дикой природы Эверглейдс от всех угроз, восстановление баланса",
            "requirements": {
                "ecology_loyalty": 70
            },
            "outcomes": [
                "ecosystem_restoration",
                "wildlife_protection",
                "natural_harmony"
            ],
            "rewards_modifier": 1.3
        },
        "bio_engineer": {
            "title": "Путь Био-Инженера",
            "description": "Использование науки для улучшения и защиты модифицированных аллигаторов",
            "requirements": {
                "research_points": 60
            },
            "outcomes": [
                "enhanced_wildlife",
                "scientific_breakthrough",
                "ethical_advancement"
            ],
            "rewards_modifier": 1.4
        },
        "poaching_master": {
            "title": "Путь Мастера Браконьерства",
            "description": "Контроль над черным рынком модифицированных аллигаторов и их продуктов",
            "requirements": {
                "hunting_expertise": 65
            },
            "outcomes": [
                "black_market_empire",
                "personal_wealth",
                "ecosystem_damage"
            ],
            "rewards_modifier": 1.5
        },
        "nature_spirit": {
            "title": "Путь Духа Природы",
            "description": "Мистическое общение с разумными аллигаторами и восстановление древних связей",
            "requirements": {
                "animal_communication": 60
            },
            "outcomes": [
                "spiritual_awakening",
                "ancient_knowledge",
                "natural_alliance"
            ],
            "rewards_modifier": 1.2
        }
    }',
    '{
        "deep_everglades": {
            "name": "Глубокие Эверглейдс",
            "description": "Сердце болот с нетронутой дикой природой и скрытыми лабораториями",
            "type": "wilderness_preserve",
            "coordinates": {
                "lat": 25.3217,
                "lng": -80.8217
            },
            "activities": [
                "wildlife_observation",
                "survival_navigation",
                "secret_research"
            ]
        },
        "modified_gator_lair": {
            "name": "Логово Модифицированных Аллигаторов",
            "description": "Убежище разумных аллигаторов с технологическими улучшениями",
            "type": "animal_sanctuary",
            "coordinates": {
                "lat": 25.4672,
                "lng": -80.6839
            },
            "activities": [
                "animal_communication",
                "tribal_meetings",
                "technological_exchange"
            ]
        },
        "corporate_pollution_site": {
            "name": "Корпоративный Загрязнитель",
            "description": "Незаконная свалка токсичных отходов, отравляющая экосистему",
            "type": "industrial_waste_dump",
            "coordinates": {
                "lat": 25.5517,
                "lng": -80.7539
            },
            "activities": [
                "environmental_cleanup",
                "sabotage_operations",
                "evidence_collection"
            ]
        }
    }',
    '{
        "ancient_gator_spirit": {
            "name": "Дух Древнего Аллигатора",
            "role": "Разумный лидер аллигаторов",
            "personality": "wise, territorial, ancient",
            "background": "Первый модифицированный аллигатор, достигший разума",
            "dialogue_style": "telepathic_communication, primal_wisdom",
            "faction": "gator_tribe"
        },
        "rogue_scientist": {
            "name": "Доктор Элиза Торрес",
            "role": "Бывший корпоративный ученый",
            "personality": "regretful, knowledgeable, determined",
            "background": "Ушла из корпорации после этических проблем с экспериментами",
            "dialogue_style": "scientific_explanation, moral_conflict",
            "faction": "researchers"
        },
        "master_poacher": {
            "name": "Эль Кроко",
            "role": "Легендарный браконьер",
            "personality": "ruthless, skilled, opportunistic",
            "background": "30 лет охоты на редких животных в Эверглейдс",
            "dialogue_style": "hunter_bravado, practical_knowledge",
            "faction": "hunters"
        },
        "eco_warrior": {
            "name": "Рэйвен Уотерс",
            "role": "Активистка по защите природы",
            "personality": "passionate, resourceful, uncompromising",
            "background": "Дочь коренных жителей Флориды, борется за Эверглейдс",
            "dialogue_style": "activist_rhetoric, survival_expertise",
            "faction": "ecologists"
        }
    }',
    '{
        "gator_neural_implant": {
            "name": "Нейронный Имплант Аллигатора",
            "type": "quest_artifact",
            "description": "Технологическое устройство, дающее аллигаторам разум",
            "rarity": "legendary",
            "faction_bonus": "researchers"
        },
        "ancient_gator_scale": {
            "name": "Чешуя Древнего Аллигатора",
            "type": "quest_relic",
            "description": "Редкая чешуя от первого разумного аллигатора",
            "rarity": "epic",
            "faction_bonus": "shamans"
        },
        "bio_engineering_kit": {
            "name": "Набор Био-Инженерии",
            "type": "quest_equipment",
            "description": "Инструменты для модификации и лечения диких животных",
            "rarity": "rare",
            "faction_bonus": "researchers"
        },
        "poachers_tracking_device": {
            "name": "Трекер Браконьера",
            "type": "quest_gadget",
            "description": "Устройство для отслеживания и охоты на редких животных",
            "rarity": "uncommon",
            "faction_bonus": "hunters"
        }
    }',
    '{
        "gator_intelligence_awakening": {
            "title": "Пробуждение Разума Аллигаторов",
            "description": "Группа аллигаторов достигает коллективного разума и начинает организованное сопротивление",
            "trigger_conditions": {
                "player_influence": 40,
                "research_progress": "advanced"
            },
            "outcomes": [
                "animal_uprising",
                "ecosystem_revolution",
                "human_wildlife_alliance"
            ]
        },
        "corporate_bio_weapon_deployment": {
            "title": "Развертывание Биологического Оружия",
            "description": "Корпорация выпускает модифицированных аллигаторов как оружие",
            "trigger_conditions": {
                "corporate_hostility": "high",
                "environmental_tension": "extreme"
            },
            "outcomes": [
                "wildlife_weaponization",
                "ecosystem_destruction",
                "human_casualties"
            ]
        },
        "ancient_ritual_ceremony": {
            "title": "Древний Ритуал Обряда",
            "description": "Мистическая церемония связи человека и природы в Эверглейдс",
            "trigger_conditions": {
                "spiritual_awakening": "achieved",
                "nature_harmony": "restored"
            },
            "outcomes": [
                "mystical_enlightenment",
                "ancient_knowledge_revealed",
                "natural_balance_restored"
            ]
        }
    }',
    '{
        "wildlife_encounters": {
            "description": "Столкновения с модифицированными аллигаторами и дикой природой",
            "difficulty": "hard",
            "rewards": "survival_experience"
        },
        "poacher_hunts": {
            "description": "Тактические операции против групп браконьеров в болотах",
            "difficulty": "medium",
            "rewards": "hunting_prowess"
        },
        "environmental_hazards": {
            "description": "Выживание в токсичных зонах и природных ловушках Эверглейдс",
            "difficulty": "extreme",
            "rewards": "environmental_adaptation"
        }
    }',
    '{
        "ecosystem_harmony": {
            "title": "Гармония Экосистемы",
            "description": "Успешное восстановление баланса между природой и технологиями",
            "requirements": {
                "ecology_dominance": true,
                "ecosystem_restored": "complete",
                "human_wildlife_peace": "achieved"
            },
            "rewards": {
                "experience": 35000,
                "currency": {
                    "type": "eddies",
                    "amount": 62000
                },
                "items": [
                    {
                        "name": "Everglades Harmony Amulet",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "natural_balance_restored",
                "wildlife_protection_established",
                "technological_nature_integration"
            ]
        },
        "bio_engineering_revolution": {
            "title": "Био-Инженерная Революция",
            "description": "Успешная эволюция аллигаторов в разумных существ через науку",
            "requirements": {
                "research_dominance": true,
                "bio_modifications": "successful",
                "ethical_framework": "established"
            },
            "rewards": {
                "experience": 33000,
                "currency": {
                    "type": "eddies",
                    "amount": 58000
                },
                "items": [
                    {
                        "name": "Bio-Evolution Catalyst",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "intelligent_wildlife_emergence",
                "scientific_breakthrough",
                "new_era_biology"
            ]
        },
        "poaching_empire": {
            "title": "Империя Браконьерства",
            "description": "Контроль над черным рынком модифицированных животных",
            "requirements": {
                "hunting_dominance": true,
                "black_market_control": "established",
                "personal_wealth": "maximized"
            },
            "rewards": {
                "experience": 31000,
                "currency": {
                    "type": "eddies",
                    "amount": 72000
                },
                "items": [
                    {
                        "name": "Poacher''s Master Trophy",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "wildlife_exploitation",
                "personal_fortune",
                "ecosystem_degradation"
            ]
        },
        "spiritual_awakening": {
            "title": "Духовное Пробуждение",
            "description": "Мистическое единение с природой и разумными аллигаторами",
            "requirements": {
                "shaman_dominance": true,
                "spiritual_connection": "achieved",
                "ancient_knowledge": "unlocked"
            },
            "rewards": {
                "experience": 34000,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "Nature Spirit Essence",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "mystical_nature_bond",
                "ancient_wisdom_restored",
                "human_animal_harmony"
            ]
        },
        "ecological_catastrophe": {
            "title": "Экологическая Катастрофа",
            "description": "Полное уничтожение экосистемы Эверглейдс из-за конфликтов",
            "requirements": {
                "decisions_failed": "catastrophic",
                "environmental_damage": "irreversible",
                "wildlife_extinction": "imminent"
            },
            "rewards": {
                "experience": 16000,
                "currency": {
                    "type": "eddies",
                    "amount": 25000
                },
                "items": [
                    {
                        "name": "Last Gator''s Lament",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "ecosystem_collapse",
                "wildlife_mass_extinction",
                "environmental_tragedy"
            ]
        }
    }',
    '[
        "dialogue_system: animal_communication",
        "wildlife_mechanics: advanced_ecosystem",
        "environmental_systems: dynamic_pollution",
        "bio_engineering: ethical_modification",
        "survival_mechanics: wilderness_expert"
    ]',
    '[
        "ethical_choice_consequences",
        "wildlife_ai_realism",
        "environmental_impact_scaling",
        "bio_engineering_risk_reward",
        "nature_technology_integration"
    ]',
    '[
        "wildlife_conservation_accuracy",
        "bio_engineering_ethics_handling",
        "environmental_science_realism",
        "animal_behavior_authenticity",
        "ecological_impact_assessment",
        "spiritual_elements_balance"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-alligators-everglades-miami-2020-2029';
