--liquibase formatted sql

--changeset backend:quest-cuban-sandwich-miami-2020-2029-import runOnChange:true
--comment: Import quest "Кубинский сэндвич (Майами 2020-2029)" into database

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
    'content-narrative-quest-cuban-sandwich-miami-2020-2029',
    '{
        "id": "content-narrative-quest-cuban-sandwich-miami-2020-2029",
        "title": "Quest: Cuban Sandwich Miami (Miami 2020-2029)",
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
            "food",
            "culinary-tradition",
            "family-recipe",
            "street-food"
        ],
        "topics": [
            "culinary-heritage",
            "food-innovation",
            "family-traditions",
            "corporate-takeover",
            "cultural-authenticity"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "food-mechanics",
            "cultural-preservation"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-culinary-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/cuban-sandwich-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-cuban-sandwich-miami-2020-2029',
    '[Canon/Lore] Квест: Кубинский сэндвич (Майами 2020-2029)',
    'Майами 2020-2029 — город, где еда стала полем битвы между традицией и инновацией.',
    'active',
    10,
    24,
    68,
    'medium',
    'narrative',
    'culinary_heritage',
    '{
        "experience": 25500,
        "currency": {
            "type": "eddies",
            "amount": 42000
        },
        "items": [
            {
                "name": "Authentic Cuban Sandwich Recipe",
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
            "level": 10
        }
    ]',
    '[
        {
            "id": "witness_recipe_theft",
            "title": "Стать свидетелем кражи рецепта",
            "description": "Увидеть попытку корпорации украсть семейный рецепт кубинского сэндвича",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_culinary_path",
            "title": "Выбрать кулинарный путь",
            "description": "Определить свою позицию в конфликте традиций и инноваций",
            "type": "choice",
            "optional": false
        },
        {
            "id": "gather_ingredients",
            "title": "Собрать ингредиенты",
            "description": "Найти аутентичные ингредиенты для настоящего кубинского сэндвича",
            "type": "ingredient_hunt",
            "optional": false
        },
        {
            "id": "master_cooking_technique",
            "title": "Освоить технику приготовления",
            "description": "Научиться правильно готовить кубинский сэндвич по семейному рецепту",
            "type": "cooking_skill",
            "success_rate": 0.75,
            "optional": false
        },
        {
            "id": "establish_food_cart",
            "title": "Организовать фуд-карт",
            "description": "Создать уличный киоск с аутентичными кубинскими сэндвичами",
            "type": "business_setup",
            "optional": false
        },
        {
            "id": "compete_cooking_contest",
            "title": "Участвовать в кулинарном конкурсе",
            "description": "Победить в конкурсе на лучший кубинский сэндвич Майами",
            "type": "competition",
            "optional": true
        },
        {
            "id": "final_food_decision",
            "title": "Принять финальное кулинарное решение",
            "description": "Определить будущее кубинского сэндвича в Майами",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "recipe_confrontation": {
            "speaker": "traditional_chef",
            "text": "Mira, amigo! Esa corporacion esta tratando de patentar NUESTRA receta! El sandwich cubano no es suyo, es de Miami, de nuestras familias!",
            "choices": [
                {
                    "text": "Tienes razon. Vamos a proteger nuestras tradiciones culinarias!",
                    "next_node": "tradition_defender",
                    "faction_points": {
                        "traditionalists": 20
                    }
                },
                {
                    "text": "Quizas sea hora de innovar. La tecnologia puede mejorar la comida.",
                    "next_node": "innovation_supporter",
                    "faction_points": {
                        "innovators": 15
                    }
                },
                {
                    "text": "Esto suena como una buena oportunidad de negocio.",
                    "next_node": "business_opportunity",
                    "faction_points": {
                        "entrepreneurs": 10
                    }
                }
            ]
        }
    }',
    '{
        "culinary_traditionalist": {
            "title": "Путь Традиционалиста",
            "description": "Защита аутентичных рецептов и семейных традиций кубинской кухни любой ценой",
            "requirements": {
                "traditionalist_loyalty": 60
            },
            "outcomes": [
                "heritage_preservation",
                "family_legacy",
                "cultural_resistance"
            ],
            "rewards_modifier": 1.2
        },
        "culinary_innovator": {
            "title": "Путь Инноватора",
            "description": "Использование технологий для улучшения и эволюции кубинской кухни",
            "requirements": {
                "innovation_points": 50
            },
            "outcomes": [
                "culinary_revolution",
                "tech_integration",
                "modern_classics"
            ],
            "rewards_modifier": 1.4
        },
        "business_tycoon": {
            "title": "Путь Бизнесмена",
            "description": "Построение империи на основе аутентичной кубинской кухни",
            "requirements": {
                "business_acumen": 55
            },
            "outcomes": [
                "food_empire",
                "commercial_success",
                "mainstream_impact"
            ],
            "rewards_modifier": 1.3
        },
        "street_food_legend": {
            "title": "Путь Легенды Уличной Еды",
            "description": "Стать иконой уличной кухни Майами через мастерство и страсть",
            "requirements": {
                "culinary_mastery": 50
            },
            "outcomes": [
                "street_fame",
                "community_hero",
                "culinary_influence"
            ],
            "rewards_modifier": 1.1
        }
    }',
    '{
        "little_havana_food_district": {
            "name": "Район Маленькая Гавана",
            "description": "Исторический район с аутентичными кубинскими ресторанами и семейными кафе",
            "type": "cultural_district",
            "coordinates": {
                "lat": 25.7642,
                "lng": -80.2209
            },
            "activities": [
                "traditional_cooking",
                "family_recipes",
                "cultural_celebrations"
            ]
        },
        "south_beach_food_trucks": {
            "name": "Фуд-траки Саут-Бич",
            "description": "Современная зона уличной еды с инновационными кухнями и технологиями",
            "type": "street_food_zone",
            "coordinates": {
                "lat": 25.7826,
                "lng": -80.1342
            },
            "activities": [
                "food_innovation",
                "tech_demonstrations",
                "culinary_competitions"
            ]
        },
        "corporate_kitchen_lab": {
            "name": "Корпоративная Кухонная Лаборатория",
            "description": "Секретная лаборатория корпорации по разработке запатентованных рецептов",
            "type": "research_facility",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1939
            },
            "activities": [
                "recipe_development",
                "patent_filing",
                "product_testing"
            ]
        }
    }',
    '{
        "family_chef": {
            "name": "Абuela Мария",
            "role": "Семейный шеф-повар",
            "personality": "wise, passionate, uncompromising",
            "background": "70 лет в кулинарии, хранительница семейных рецептов кубинской кухни",
            "dialogue_style": "grandmotherly_wisdom, spanish_accents",
            "faction": "traditionalists"
        },
        "tech_chef": {
            "name": "Доктор Алекс Ченг",
            "role": "Инновационный шеф-повар",
            "personality": "creative, tech-savvy, ambitious",
            "background": "Специалист по молекулярной гастрономии с нейронными имплантами",
            "dialogue_style": "scientific_enthusiasm, futuristic_vision",
            "faction": "innovators"
        },
        "street_vendor": {
            "name": "Педро Быстрый Сантьяго",
            "role": "Уличный торговец",
            "personality": "street_smart, charismatic, entrepreneurial",
            "background": "Начал с маленького фургончика, теперь контролирует несколько районов",
            "dialogue_style": "miami_slang, business_sharp",
            "faction": "entrepreneurs"
        },
        "food_critic": {
            "name": "София Валенсия",
            "role": "Кулинарный критик",
            "personality": "discerning, influential, ethical",
            "background": "Известный гурман с миллионами подписчиков в соцсетях",
            "dialogue_style": "sophisticated_taste, moral_standards",
            "faction": "preservers"
        }
    }',
    '{
        "authentic_cuban_ham": {
            "name": "Аутентичный Кубинский Хам",
            "type": "quest_ingredient",
            "description": "Редкий хам, приготовленный по традиционному кубинскому рецепту",
            "rarity": "rare",
            "faction_bonus": "traditionalists"
        },
        "family_recipe_scroll": {
            "name": "Свиток Семейного Рецепта",
            "type": "quest_document",
            "description": "Древний рецепт кубинского сэндвича, передаваемый поколениями",
            "rarity": "epic",
            "faction_bonus": "traditionalists"
        },
        "nano_seasoning_kit": {
            "name": "Набор Нановкусовых Добавок",
            "type": "quest_gadget",
            "description": "Технологический набор для создания новых вкусовых комбинаций",
            "rarity": "uncommon",
            "faction_bonus": "innovators"
        },
        "mobile_food_cart": {
            "name": "Мобильный Фуд-Карт",
            "type": "quest_vehicle",
            "description": "Полностью оборудованный передвижной кухонный фургон",
            "rarity": "rare",
            "faction_bonus": "entrepreneurs"
        }
    }',
    '{
        "recipe_patent_war": {
            "title": "Война Патентов на Рецепты",
            "description": "Корпорация начинает судебную войну против семейных ресторанов за использование запатентованных ингредиентов",
            "trigger_conditions": {
                "corporate_aggression": "high",
                "community_resistance": "growing"
            },
            "outcomes": [
                "legal_battles",
                "public_awareness",
                "cultural_backlash"
            ]
        },
        "food_festival_showdown": {
            "title": "Противостояние на Фуд-Фестивале",
            "description": "Финальное соревнование между традиционными и инновационными поварами",
            "trigger_conditions": {
                "culinary_tension": "peak",
                "community_involvement": "high"
            },
            "outcomes": [
                "winner_crowned",
                "culinary_trend_set",
                "media_coverage"
            ]
        },
        "ingredient_black_market": {
            "title": "Черный Рынок Ингредиентов",
            "description": "Рост подпольной торговли аутентичными кубинскими ингредиентами",
            "trigger_conditions": {
                "ingredient_scarcity": "increasing",
                "corporate_control": "expanding"
            },
            "outcomes": [
                "underground_networks",
                "price_inflation",
                "quality_decline"
            ]
        }
    }',
    '{
        "culinary_duels": {
            "description": "Соревнования поваров в приготовлении блюд в реальном времени",
            "difficulty": "medium",
            "rewards": "reputation_points"
        },
        "sabotage_missions": {
            "description": "Тайные операции по защите рецептов от корпоративного шпионажа",
            "difficulty": "hard",
            "rewards": "loyalty_bonuses"
        },
        "food_cart_defense": {
            "description": "Защита уличных киосков от корпоративных рейдеров и конкурентов",
            "difficulty": "easy",
            "rewards": "business_protection"
        }
    }',
    '{
        "tradition_victory": {
            "title": "Победа Традиций",
            "description": "Успешная защита аутентичной кубинской кухни от корпоративного захвата",
            "requirements": {
                "traditionalist_dominance": true,
                "recipes_preserved": "complete",
                "cultural_victory": "achieved"
            },
            "rewards": {
                "experience": 30500,
                "currency": {
                    "type": "eddies",
                    "amount": 50000
                },
                "items": [
                    {
                        "name": "Cuban Culinary Heritage Medal",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "tradition_preserved",
                "family_recipes_saved",
                "cultural_resistance_success"
            ]
        },
        "innovation_triumph": {
            "title": "Триумф Инноваций",
            "description": "Успешная интеграция технологий в традиционную кубинскую кухню",
            "requirements": {
                "innovator_dominance": true,
                "tech_integration": "successful",
                "modern_classics": "created"
            },
            "rewards": {
                "experience": 28500,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "Digital Culinary Revolution Chip",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "culinary_evolution",
                "tech_food_integration",
                "modern_traditions"
            ]
        },
        "business_empire": {
            "title": "Бизнес-Империя",
            "description": "Построение коммерческой империи на основе кубинской кухни",
            "requirements": {
                "entrepreneur_dominance": true,
                "business_empire": "established",
                "market_control": "achieved"
            },
            "rewards": {
                "experience": 27500,
                "currency": {
                    "type": "eddies",
                    "amount": 60000
                },
                "items": [
                    {
                        "name": "Miami Food Empire Franchise",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "commercial_success",
                "culinary_mainstream",
                "entrepreneurial_legend"
            ]
        },
        "street_food_legend": {
            "title": "Легенда Уличной Еды",
            "description": "Стать иконой уличной кухни Майами через мастерство и страсть",
            "requirements": {
                "culinary_mastery": "dominant",
                "street_fame": "achieved",
                "community_impact": "significant"
            },
            "rewards": {
                "experience": 29500,
                "currency": {
                    "type": "eddies",
                    "amount": 48000
                },
                "items": [
                    {
                        "name": "Miami Street Food Icon Statue",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "street_food_revolution",
                "community_celebrity",
                "culinary_influence"
            ]
        },
        "corporate_domination": {
            "title": "Корпоративное Доминирование",
            "description": "Корпорация полностью захватывает рынок кубинской кухни Майами",
            "requirements": {
                "corporate_victory": "decisive",
                "traditional_defeat": "complete",
                "innovation_suppression": "achieved"
            },
            "rewards": {
                "experience": 16500,
                "currency": {
                    "type": "eddies",
                    "amount": 30000
                },
                "items": [
                    {
                        "name": "Corporate Food Control Chip",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "tradition_lost",
                "corporate_monopoly",
                "cultural_homogenization"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_culinary",
        "cooking_mechanics: advanced_preparation",
        "ingredient_system: dynamic_sourcing",
        "business_mechanics: food_service_simulation",
        "event_system: culinary_competition_driven"
    ]',
    '[
        "culinary_choice_consequences",
        "tradition_vs_innovation_balance",
        "business_vs_culture_dynamics",
        "ingredient_scarcity_scaling",
        "cooking_skill_progression"
    ]',
    '[
        "cuban_cuisine_accuracy",
        "food_culture_representation",
        "culinary_innovation_balance",
        "business_mechanics_realism",
        "tradition_preservation_themes",
        "multiple_ending_culinary_impact"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-cuban-sandwich-miami-2020-2029';
