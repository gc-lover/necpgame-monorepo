--liquibase formatted sql

--changeset backend:quest-quebec-french-montreal-2020-2029-import runOnChange:true
--comment: Import quest "Квебекский французский (Монреаль 2020-2029)" into database

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
    'content-narrative-quest-quebec-french-montreal-2020-2029',
    '{
        "id": "content-narrative-quest-quebec-french-montreal-2020-2029",
        "title": "Quest: Quebec French Montreal (Montreal 2020-2029)",
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
            "montreal",
            "quebec",
            "french-language",
            "cultural-preservation",
            "language-barrier"
        ],
        "topics": [
            "quebec-french",
            "language-preservation",
            "cultural-identity",
            "anglo-french-tensions",
            "linguistic-rights"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "cultural-dynamics",
            "language-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-language-barrier-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/quebec-french-montreal-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-quebec-french-montreal-2020-2029',
    '[Canon/Lore] Квест: Квебекский французский (Монреаль 2020-2029)',
    'Монреаль 2020-2029 — город на перекрестке культур. Англо-саксонское влияние угрожает традиционному квебекскому французскому языку.',
    'active',
    8,
    22,
    65,
    'medium',
    'narrative',
    'cultural_preservation',
    '{
        "experience": 19500,
        "currency": {
            "type": "eddies",
            "amount": 32000
        },
        "items": [
            {
                "name": "Quebec French Dictionary",
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
            "level": 8
        }
    ]',
    '[
        {
            "id": "choose_language_side",
            "title": "Выбрать сторону в языковом конфликте",
            "description": "Определить свою позицию в дебатах о квебекском французском",
            "type": "choice",
            "optional": false
        },
        {
            "id": "attend_language_event",
            "title": "Посетить языковое мероприятие",
            "description": "Участвовать в культурном событии франкофонной общины",
            "type": "location_visit",
            "location": "montreal_french_quarter",
            "optional": false
        },
        {
            "id": "learn_quebec_french",
            "title": "Изучить квебекский французский",
            "description": "Освоить базовые фразы и культурные особенности",
            "type": "skill_training",
            "skill": "quebec_french",
            "optional": false
        },
        {
            "id": "debate_language_purist",
            "title": "Спорить с пуристом языка",
            "description": "Выиграть дискуссию о чистоте квебекского французского",
            "type": "dialogue_choice",
            "success_rate": 0.75,
            "optional": false
        },
        {
            "id": "gather_cultural_supporters",
            "title": "Собрать культурных сторонников",
            "description": "Нанять 4 сторонников сохранения франкофонной культуры",
            "type": "recruitment",
            "count": 4,
            "optional": true
        },
        {
            "id": "influence_language_policy",
            "title": "Влиять на языковую политику",
            "description": "Изменить рейтинг поддержки языковых законов на 20%",
            "type": "influence",
            "target": "language_policy",
            "threshold": 20,
            "optional": true
        },
        {
            "id": "final_cultural_decision",
            "title": "Принять финальное культурное решение",
            "description": "Определить исход борьбы за квебекский французский",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "language_confrontation": {
            "speaker": "french_purist",
            "text": "Vous parlez anglais? Ici, c''est le Quebec! Notre langue, notre culture, notre identite! Le francais quebecois doit survivre!",
            "choices": [
                {
                    "text": "Je suis d''accord. Le francais quebecois doit etre protege!",
                    "next_node": "french_activist_response",
                    "faction_points": {
                        "french_purists": 15
                    }
                },
                {
                    "text": "Mais l''anglais est necessaire pour les affaires...",
                    "next_node": "pragmatic_businessman",
                    "faction_points": {
                        "pragmatists": 8
                    }
                },
                {
                    "text": "C''est trop extreme. Il faut trouver un equilibre.",
                    "next_node": "cultural_mediator",
                    "faction_points": {
                        "mediators": 12
                    }
                }
            ]
        }
    }',
    '{
        "purist_path": {
            "title": "Путь Пуриста",
            "description": "Радикальная защита чистоты квебекского французского любой ценой",
            "requirements": {
                "french_purists_points": 40
            },
            "outcomes": [
                "strict_language_laws",
                "cultural_separation",
                "linguistic_preservation"
            ],
            "rewards_modifier": 1.1
        },
        "pragmatic_path": {
            "title": "Путь Прагматика",
            "description": "Баланс между культурным наследием и экономическими реалиями",
            "requirements": {
                "pragmatists_points": 40
            },
            "outcomes": [
                "bilingual_compromise",
                "economic_growth",
                "cultural_adaptation"
            ],
            "rewards_modifier": 1.3
        },
        "mediator_path": {
            "title": "Путь Посредника",
            "description": "Поиск гармонии между франкофонной и англофонной культурами",
            "requirements": {
                "mediators_points": 40
            },
            "outcomes": [
                "cultural_bridge_building",
                "mutual_understanding",
                "bilingual_society"
            ],
            "rewards_modifier": 1.4
        },
        "innovator_path": {
            "title": "Путь Инноватора",
            "description": "Использование технологий для сохранения и развития языка",
            "requirements": {
                "innovators_points": 40
            },
            "outcomes": [
                "tech_language_preservation",
                "digital_cultural_tools",
                "modern_linguistic_identity"
            ],
            "rewards_modifier": 1.2
        }
    }',
    '{
        "montreal_french_quarter": {
            "name": "Французский квартал Монреаля",
            "description": "Исторический район Вьё-Монреаль с франкофонной атмосферой",
            "type": "cultural_district",
            "coordinates": {
                "lat": 45.5017,
                "lng": -73.5536
            },
            "activities": [
                "language_lessons",
                "cultural_festivals",
                "community_meetings"
            ]
        },
        "quebec_language_office": {
            "name": "Офис Квебекского языка",
            "description": "Государственная организация по защите французского языка",
            "type": "government_office",
            "coordinates": {
                "lat": 45.5167,
                "lng": -73.5667
            },
            "activities": [
                "policy_discussions",
                "language_enforcement",
                "cultural_programs"
            ]
        },
        "bilingual_community_center": {
            "name": "Центр двуязычной общины",
            "description": "Место встреч франкофонных и англофонных сообществ",
            "type": "community_center",
            "coordinates": {
                "lat": 45.5289,
                "lng": -73.5842
            },
            "activities": [
                "intercultural_dialogues",
                "language_exchange",
                "cultural_exchanges"
            ]
        }
    }',
    '{
        "french_purist": {
            "name": "Мари-Эв Леблан",
            "role": "Лингвист и активистка",
            "personality": "passionate, uncompromising, educated",
            "background": "Профессор лингвистики, потеряла работу из-за англофикации",
            "dialogue_style": "formal_french, passionate_speeches",
            "faction": "french_purists"
        },
        "anglo_businessman": {
            "name": "Джон Макдональд",
            "role": "Бизнесмен из Торонто",
            "personality": "pragmatic, ambitious, confident",
            "background": "Руководитель международной компании в Монреале",
            "dialogue_style": "business_english, economic_arguments",
            "faction": "pragmatists"
        },
        "cultural_mediator": {
            "name": "Симон Дюпюи",
            "role": "Посредник культур",
            "personality": "diplomatic, thoughtful, inclusive",
            "background": "Бывший дипломат, специалист по межкультурным отношениям",
            "dialogue_style": "balanced_viewpoints, bridge_building",
            "faction": "mediators"
        },
        "young_innovator": {
            "name": "Эмма Тремблей",
            "role": "Разработчик языковых приложений",
            "personality": "creative, tech-savvy, optimistic",
            "background": "Студентка информатики, создает приложения для изучения языков",
            "dialogue_style": "modern_french, tech_enthusiasm",
            "faction": "innovators"
        }
    }',
    '{
        "quebec_french_dictionary": {
            "name": "Словарь Квебекского Французского",
            "type": "quest_document",
            "description": "Цифровой словарь с региональными выражениями и культурным контекстом",
            "rarity": "common",
            "faction_bonus": "french_purists"
        },
        "language_law_manifesto": {
            "name": "Манифест Языкового Закона",
            "type": "quest_document",
            "description": "Документ с предложениями по защите французского языка",
            "rarity": "uncommon",
            "faction_bonus": "french_purists"
        },
        "cultural_bridge_proposal": {
            "name": "Предложение Культурного Моста",
            "type": "quest_document",
            "description": "План гармоничного сосуществования культур в Монреале",
            "rarity": "rare",
            "faction_bonus": "mediators"
        },
        "tech_language_app": {
            "name": "Приложение для Изучения Языка",
            "type": "quest_item",
            "description": "VR-приложение для погружения в квебекскую франкофонную культуру",
            "rarity": "epic",
            "faction_bonus": "innovators"
        }
    }',
    '{
        "language_protest": {
            "title": "Языковой Протест",
            "description": "Массовый митинг за защиту французского языка в Квебеке",
            "trigger_conditions": {
                "player_influence": 25,
                "cultural_tension": "high"
            },
            "outcomes": [
                "public_awareness_increase",
                "media_coverage_boost",
                "government_attention"
            ]
        },
        "bilingual_festival": {
            "title": "Двуязычный Фестиваль",
            "description": "Совместное культурное мероприятие франкофонных и англофонных сообществ",
            "trigger_conditions": {
                "harmony_efforts": "active",
                "community_cooperation": "growing"
            },
            "outcomes": [
                "cultural_exchange_opportunities",
                "mutual_understanding_boost",
                "economic_benefits"
            ]
        },
        "language_enforcement_raid": {
            "title": "Рейд по Соблюдению Языковых Законов",
            "description": "Государственная инспекция на соблюдение франкоязычных требований",
            "trigger_conditions": {
                "language_laws": "strict",
                "enforcement_agency": "active"
            },
            "outcomes": [
                "business_compliance_changes",
                "public_opinion_divide",
                "economic_impact"
            ]
        }
    }',
    '{
        "verbal_debates": {
            "description": "Дебаты с использованием риторических приемов и языковых аргументов",
            "difficulty": "medium",
            "rewards": "cultural_influence_points"
        },
        "cultural_performance": {
            "description": "Участие в культурных мероприятиях франкофонной общины",
            "difficulty": "easy",
            "rewards": "community_loyalty"
        },
        "digital_propaganda": {
            "description": "Создание контента для продвижения культурных ценностей",
            "difficulty": "hard",
            "rewards": "social_media_impact"
        }
    }',
    '{
        "linguistic_preservation": {
            "title": "Сохранение Языка",
            "description": "Успешная защита квебекского французского через строгие законы",
            "requirements": {
                "french_purists_dominance": true,
                "language_laws": "enforced",
                "cultural_separation": "achieved"
            },
            "rewards": {
                "experience": 24000,
                "currency": {
                    "type": "eddies",
                    "amount": 38000
                },
                "items": [
                    {
                        "name": "Quebec Language Guardian Medal",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "strict_language_protection",
                "cultural_preservation",
                "economic_challenges"
            ]
        },
        "bilingual_harmony": {
            "title": "Двуязычная Гармония",
            "description": "Мирное сосуществование франкофонной и англофонной культур",
            "requirements": {
                "mediators_dominance": true,
                "cultural_bridge": "built",
                "mutual_respect": "achieved"
            },
            "rewards": {
                "experience": 28000,
                "currency": {
                    "type": "eddies",
                    "amount": 45000
                },
                "items": [
                    {
                        "name": "Montreal Unity Accord",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "cultural_harmony",
                "bilingual_society",
                "economic_prosperity"
            ]
        },
        "pragmatic_compromise": {
            "title": "Прагматический Компромисс",
            "description": "Баланс между культурным наследием и экономической необходимостью",
            "requirements": {
                "pragmatists_dominance": true,
                "economic_balance": "maintained",
                "cultural_adaptation": "progressive"
            },
            "rewards": {
                "experience": 26000,
                "currency": {
                    "type": "eddies",
                    "amount": 42000
                },
                "items": [
                    {
                        "name": "Quebec Economic Bridge Token",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "economic_growth",
                "gradual_cultural_change",
                "pragmatic_solutions"
            ]
        },
        "innovation_victory": {
            "title": "Победа Инноваций",
            "description": "Использование технологий для сохранения и развития языка",
            "requirements": {
                "innovators_dominance": true,
                "tech_solutions": "implemented",
                "digital_culture": "thriving"
            },
            "rewards": {
                "experience": 29000,
                "currency": {
                    "type": "eddies",
                    "amount": 48000
                },
                "items": [
                    {
                        "name": "Digital Language Revolution Chip",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "tech_language_preservation",
                "modern_cultural_identity",
                "global_influence"
            ]
        },
        "cultural_conflict": {
            "title": "Культурный Конфликт",
            "description": "Эскалация напряженности между культурными группами",
            "requirements": {
                "radical_actions": "taken",
                "cultural_division": "extreme",
                "social_unrest": "rising"
            },
            "rewards": {
                "experience": 18000,
                "currency": {
                    "type": "eddies",
                    "amount": 25000
                },
                "items": [
                    {
                        "name": "Chaos Catalyst",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "social_division",
                "economic_damage",
                "international_concern"
            ]
        }
    }',
    '[
        "dialogue_system: advanced_french_english",
        "faction_system: cultural_dynamics",
        "influence_mechanics: social_media_focused",
        "event_system: community_driven",
        "localization: quebec_french_support"
    ]',
    '[
        "dialogue_choices_affect_cultural_tensions",
        "language_barrier_mechanics_integration",
        "social_media_influence_scaling",
        "cultural_authenticity_verification_needed",
        "bilingual_dialogue_balance_required"
    ]',
    '[
        "cultural_references_accuracy",
        "language_usage_authenticity",
        "faction_balance_maintained",
        "dialogue_branching_functionality",
        "multiple_ending_accessibility",
        "cultural_sensitivity_review"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-quebec-french-montreal-2020-2029';
