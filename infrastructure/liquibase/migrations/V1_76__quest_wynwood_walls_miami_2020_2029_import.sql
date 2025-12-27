--liquibase formatted sql

--changeset backend:quest-wynwood-walls-miami-2020-2029-import runOnChange:true
--comment: Import quest "Стены Винвуда (Майами 2020-2029)" into database

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
    'content-narrative-quest-wynwood-walls-miami-2020-2029',
    '{
        "id": "content-narrative-quest-wynwood-walls-miami-2020-2029",
        "title": "Quest: Wynwood Walls Miami (Miami 2020-2029)",
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
            "street-art",
            "cultural-preservation",
            "gentrification",
            "urban-defense"
        ],
        "topics": [
            "street-art-preservation",
            "urban-culture-defense",
            "corporate-takeover-threat",
            "community-resistance",
            "artistic-freedom"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "cultural-defense-mechanics",
            "urban-combat-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-street-art-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/wynwood-walls-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-wynwood-walls-miami-2020-2029',
    '[Canon/Lore] Квест: Стены Винвуда (Майами 2020-2029)',
    'Винвуд 2020-2029 — район Майами, где стены стали холстом для тысяч историй.',
    'active',
    10,
    25,
    70,
    'medium',
    'narrative',
    'cultural_defense',
    '{
        "experience": 24500,
        "currency": {
            "type": "eddies",
            "amount": 41000
        },
        "items": [
            {
                "name": "Street Artist''s Spray Kit",
                "type": "quest_item",
                "rarity": "uncommon"
            }
        ]
    }',
    '[
        {
            "completed": "tutorial_basics"
        },
        {
            "level": 10
        }
    ]',
    '[
        {
            "id": "witness_corporate_raid",
            "title": "Стать свидетелем корпоративного рейда",
            "description": "Увидеть попытку корпорации захватить стены Винвуда",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_artistic_path",
            "title": "Выбрать художественный путь",
            "description": "Определить свою позицию в конфликте искусства и бизнеса",
            "type": "choice",
            "optional": false
        },
        {
            "id": "create_street_art",
            "title": "Создать уличное искусство",
            "description": "Нарисовать граффити в защиту Винвуда",
            "type": "creative_task",
            "success_rate": 0.8,
            "optional": false
        },
        {
            "id": "gather_artists",
            "title": "Собрать художников",
            "description": "Нанять 3 уличных художника для совместной работы",
            "type": "recruitment",
            "count": 3,
            "optional": false
        },
        {
            "id": "defend_walls",
            "title": "Защитить стены",
            "description": "Отразить атаку корпоративных сил на стены",
            "type": "defense_mission",
            "optional": false
        },
        {
            "id": "organize_art_show",
            "title": "Организовать художественную выставку",
            "description": "Создать публичную демонстрацию искусства Винвуда",
            "type": "event_organization",
            "optional": true
        },
        {
            "id": "final_artistic_decision",
            "title": "Принять финальное художественное решение",
            "description": "Определить исход борьбы за стены Винвуда",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "corporate_threat": {
            "speaker": "street_artist",
            "text": "Tu vois ca? Ces enfoires en costard veulent transformer nos murs en putain de panneau publicitaire! Notre art, notre histoire, notre vie - tout ca pour leurs profits!",
            "choices": [
                {
                    "text": "Je vais vous aider a defendre les murs!",
                    "next_node": "artist_alliance",
                    "faction_points": {
                        "artists": 20
                    }
                },
                {
                    "text": "Le progres economique est inevitable...",
                    "next_node": "corporate_sympathizer",
                    "faction_points": {
                        "corporates": 15
                    }
                },
                {
                    "text": "Je prefere rester neutre dans ce conflit.",
                    "next_node": "observer_path",
                    "faction_points": {
                        "neutrals": 10
                    }
                }
            ]
        }
    }',
    '{
        "revolutionary_artist": {
            "title": "Путь Революционного Художника",
            "description": "Радикальная защита аутентичного уличного искусства любой ценой",
            "requirements": {
                "artist_loyalty": 50
            },
            "outcomes": [
                "artistic_revolution",
                "cultural_preservation",
                "underground_legend"
            ],
            "rewards_modifier": 1.2
        },
        "corporate_collaborator": {
            "title": "Путь Корпоративного Коллаборатора",
            "description": "Интеграция искусства в корпоративную культуру с выгодой для всех",
            "requirements": {
                "corporate_loyalty": 50
            },
            "outcomes": [
                "commercial_success",
                "mainstream_exposure",
                "artistic_compromise"
            ],
            "rewards_modifier": 1.4
        },
        "mediator_path": {
            "title": "Путь Посредника",
            "description": "Поиск баланса между коммерцией и аутентичностью искусства",
            "requirements": {
                "mediator_points": 50
            },
            "outcomes": [
                "balanced_solution",
                "mutual_benefits",
                "sustainable_future"
            ],
            "rewards_modifier": 1.3
        },
        "strategic_defender": {
            "title": "Путь Стратегического Защитника",
            "description": "Организация эффективной обороны культурного наследия",
            "requirements": {
                "activist_points": 50
            },
            "outcomes": [
                "community_strength",
                "defensive_success",
                "local_empowerment"
            ],
            "rewards_modifier": 1.1
        }
    }',
    '{
        "wynwood_walls_complex": {
            "name": "Комплекс Стен Винвуда",
            "description": "Знаменитый комплекс уличного искусства с тысячами квадратных метров граффити",
            "type": "cultural_site",
            "coordinates": {
                "lat": 25.8017,
                "lng": -80.1992
            },
            "activities": [
                "graffiti_creation",
                "art_exhibitions",
                "cultural_protests"
            ]
        },
        "underground_art_studio": {
            "name": "Подпольная Художественная Студия",
            "description": "Секретная мастерская для создания революционного искусства",
            "type": "hidden_workshop",
            "coordinates": {
                "lat": 25.8039,
                "lng": -80.1987
            },
            "activities": [
                "art_preparation",
                "activist_meetings",
                "equipment_storage"
            ]
        },
        "corporate_headquarters": {
            "name": "Корпоративная Штаб-Квартира",
            "description": "Офис корпорации, планирующей захват Винвуда",
            "type": "business_tower",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1939
            },
            "activities": [
                "corporate_meetings",
                "surveillance_operations",
                "negotiation_sessions"
            ]
        }
    }',
    '{
        "veteran_artist": {
            "name": "Диего Спрей Мартинес",
            "role": "Легендарный уличный художник",
            "personality": "passionate, rebellious, wise",
            "background": "20 лет в уличном искусстве, пережил множество арестов",
            "dialogue_style": "spanish_influenced, artistic_rants",
            "faction": "artists"
        },
        "corporate_rep": {
            "name": "Виктория Ченг",
            "role": "Корпоративный менеджер по культуре",
            "personality": "professional, ambitious, pragmatic",
            "background": "Специалист по брендингу, видит искусство как бизнес-актив",
            "dialogue_style": "business_formal, persuasive_arguments",
            "faction": "corporates"
        },
        "young_activist": {
            "name": "Джейми Нексус Гарсия",
            "role": "Молодой активист и техно-художник",
            "personality": "idealistic, tech-savvy, energetic",
            "background": "Студент искусства, сочетает традиционное и цифровое",
            "dialogue_style": "modern_slang, enthusiastic_ideas",
            "faction": "innovators"
        },
        "community_leader": {
            "name": "Мария Сантьяго",
            "role": "Лидер сообщества Винвуда",
            "personality": "diplomatic, community_focused, determined",
            "background": "Местная жительница, борется за сохранение района",
            "dialogue_style": "community_centered, practical_solutions",
            "faction": "activists"
        }
    }',
    '{
        "spray_kit_pro": {
            "name": "Профессиональный Набор для Граффити",
            "type": "quest_tool",
            "description": "Высококачественные баллоны с краской для создания уличного искусства",
            "rarity": "rare",
            "faction_bonus": "artists"
        },
        "ar_goggles": {
            "name": "AR-Очки для Искусства",
            "type": "quest_gadget",
            "description": "Очки дополненной реальности для просмотра интерактивного граффити",
            "rarity": "uncommon",
            "faction_bonus": "innovators"
        },
        "corporate_contract": {
            "name": "Корпоративный Контракт",
            "type": "quest_document",
            "description": "Предложение о сотрудничестве с корпорацией",
            "rarity": "uncommon",
            "faction_bonus": "corporates"
        },
        "community_manifesto": {
            "name": "Манифест Сообщества",
            "type": "quest_document",
            "description": "Документ с требованиями сохранения аутентичной культуры",
            "rarity": "rare",
            "faction_bonus": "activists"
        }
    }',
    '{
        "corporate_buyout_attempt": {
            "title": "Попытка Корпоративного Выкупа",
            "description": "Корпорация пытается купить комплекс стен для коммерческого использования",
            "trigger_conditions": {
                "corporate_influence": "rising",
                "community_resistance": "weak"
            },
            "outcomes": [
                "ownership_transfer",
                "public_outrage",
                "legal_battles"
            ]
        },
        "art_vandalism_incident": {
            "title": "Инцидент Вандализма Искусства",
            "description": "Неизвестные портят знаменитые граффити в Винвуде",
            "trigger_conditions": {
                "artistic_tensions": "high",
                "rival_artists": "active"
            },
            "outcomes": [
                "art_destruction",
                "community_division",
                "artistic_rivalry"
            ]
        },
        "international_art_festival": {
            "title": "Международный Фестиваль Искусства",
            "description": "Глобальное событие привлекает внимание к уличному искусству Винвуда",
            "trigger_conditions": {
                "community_efforts": "successful",
                "media_attention": "gained"
            },
            "outcomes": [
                "worldwide_recognition",
                "tourism_increase",
                "cultural_preservation"
            ]
        }
    }',
    '{
        "defensive_battles": {
            "description": "Защита стен от корпоративных сил и вандалов",
            "difficulty": "medium",
            "rewards": "community_loyalty"
        },
        "stealth_operations": {
            "description": "Тайные операции по созданию искусства и саботажу",
            "difficulty": "hard",
            "rewards": "artistic_reputation"
        },
        "protest_management": {
            "description": "Организация и управление культурными протестами",
            "difficulty": "easy",
            "rewards": "activist_points"
        }
    }',
    '{
        "artistic_triumph": {
            "title": "Триумф Искусства",
            "description": "Успешная защита аутентичного уличного искусства от коммерциализации",
            "requirements": {
                "artist_dominance": true,
                "walls_preserved": "complete",
                "cultural_victory": "achieved"
            },
            "rewards": {
                "experience": 29500,
                "currency": {
                    "type": "eddies",
                    "amount": 48000
                },
                "items": [
                    {
                        "name": "Wynwood Guardian Medal",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "artistic_freedom",
                "cultural_preservation",
                "underground_fame"
            ]
        },
        "corporate_success": {
            "title": "Корпоративный Успех",
            "description": "Успешная интеграция искусства в корпоративную культуру",
            "requirements": {
                "corporate_dominance": true,
                "business_deal": "completed",
                "commercialization": "achieved"
            },
            "rewards": {
                "experience": 27500,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "Corporate Art Director Badge",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "commercial_growth",
                "mainstream_success",
                "artistic_compromise"
            ]
        },
        "balanced_compromise": {
            "title": "Сбалансированный Компромисс",
            "description": "Мирное сосуществование искусства и бизнеса в Винвуде",
            "requirements": {
                "mediator_dominance": true,
                "compromise_reached": true,
                "mutual_benefits": "achieved"
            },
            "rewards": {
                "experience": 28500,
                "currency": {
                    "type": "eddies",
                    "amount": 52000
                },
                "items": [
                    {
                        "name": "Wynwood Unity Accord",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "balanced_development",
                "sustainable_growth",
                "cultural_harmony"
            ]
        },
        "community_defeat": {
            "title": "Поражение Сообщества",
            "description": "Корпорация успешно захватывает и коммерциализирует стены",
            "requirements": {
                "corporate_victory": "decisive",
                "community_defense": "failed",
                "artistic_loss": "complete"
            },
            "rewards": {
                "experience": 18000,
                "currency": {
                    "type": "eddies",
                    "amount": 28000
                },
                "items": [
                    {
                        "name": "Defeated Activist Badge",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "cultural_loss",
                "gentrification_success",
                "community_displacement"
            ]
        },
        "artistic_revolution": {
            "title": "Художественная Революция",
            "description": "Радикальная трансформация уличного искусства в Винвуде",
            "requirements": {
                "revolutionary_actions": "taken",
                "innovation_success": "achieved",
                "paradigm_shift": "created"
            },
            "rewards": {
                "experience": 31000,
                "currency": {
                    "type": "eddies",
                    "amount": 45000
                },
                "items": [
                    {
                        "name": "Art Revolution Catalyst",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "artistic_innovation",
                "cultural_transformation",
                "global_influence"
            ]
        }
    }',
    '[
        "dialogue_system: cultural_diversity",
        "art_creation_mechanics: street_art_specialized",
        "faction_system: cultural_conflict",
        "event_system: community_driven",
        "localization: miami_cultural_terms"
    ]',
    '[
        "artistic_choice_consequences",
        "corporate_vs_community_balance",
        "cultural_authenticity_preservation",
        "community_engagement_scaling",
        "artistic_freedom_vs_commercialization"
    ]',
    '[
        "street_art_cultural_accuracy",
        "gentrification_themes_handling",
        "corporate_representation_balance",
        "artistic_freedom_vs_commercialization",
        "community_activism_realism",
        "multiple_ending_balance"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-wynwood-walls-miami-2020-2029';
