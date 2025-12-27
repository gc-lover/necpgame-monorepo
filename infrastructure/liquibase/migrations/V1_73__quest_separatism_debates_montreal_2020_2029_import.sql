--liquibase formatted sql

--changeset backend:quest-separatism-debates-montreal-2020-2029-import runOnChange:true
--comment: Import quest "Дебаты о сепаратизме (Монреаль 2020-2029)" into database

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
    'content-narrative-quest-separatism-debates-montreal-2020-2029',
    '{
        "id": "content-narrative-quest-separatism-debates-montreal-2020-2029",
        "title": "Quest: Separatism Debates Montreal (Montreal 2020-2029)",
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
            "separatism",
            "political-debates",
            "cultural-identity"
        ],
        "topics": [
            "quebec-independence",
            "political-debates",
            "cultural-preservation",
            "sovereignty-discussions",
            "nationalist-movements"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "political-debate-system",
            "cultural-dynamics"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-political-debate-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/separatism-debates-montreal-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-separatism-debates-montreal-2020-2029',
    '[Canon/Lore] Квест: Дебаты о сепаратизме (Монреаль 2020-2029)',
    'Монреаль 2020-2029 — город находится на грани политического кризиса. Дебаты о независимости Квебека достигают апогея. Игрок становится частью политических дебатов Монреаля, где каждый выбор влияет на исход борьбы за независимость.',
    'active',
    12,
    26,
    85,
    'medium',
    'narrative',
    'political_debate',
    '{
        "experience": 25000,
        "currency": {
            "type": "eddies",
            "amount": 42000
        },
        "items": [
            {
                "name": "Quebec Sovereignty Token",
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
            "level": 12
        }
    ]',
    '[
        {
            "id": "join_debate",
            "title": "Присоединиться к дебатам",
            "description": "Выбрать сторону в политических дебатах Монреаля",
            "type": "choice",
            "optional": false
        },
        {
            "id": "attend_rally",
            "title": "Посетить митинг",
            "description": "Участвовать в политическом митинге",
            "type": "location_visit",
            "location": "montreal_old_port",
            "optional": false
        },
        {
            "id": "debate_opponent",
            "title": "Спорить с оппонентом",
            "description": "Выиграть политическую дискуссию",
            "type": "dialogue_choice",
            "success_rate": 0.7,
            "optional": false
        },
        {
            "id": "gather_supporters",
            "title": "Собрать сторонников",
            "description": "Нанять 5 сторонников для своей позиции",
            "type": "recruitment",
            "count": 5,
            "optional": true
        },
        {
            "id": "influence_public",
            "title": "Влиять на общественное мнение",
            "description": "Изменить рейтинг популярности своей позиции на 15%",
            "type": "influence",
            "target": "public_opinion",
            "threshold": 15,
            "optional": true
        },
        {
            "id": "final_decision",
            "title": "Принять финальное решение",
            "description": "Определить исход политических дебатов",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "initial_encounter": {
            "speaker": "quebec_nationalist",
            "text": "Ты не понимаешь, mon ami. Квебек - это не просто провинция. Это нация, культура, язык! Мы должны быть свободны от англо-саксонского доминирования!",
            "choices": [
                {
                    "text": "Я согласен с вами. Квебек должен быть независимым!",
                    "next_node": "federalist_response",
                    "faction_points": {
                        "separatists": 10
                    }
                },
                {
                    "text": "Но экономика Квебека зависит от Канады...",
                    "next_node": "economic_argument",
                    "faction_points": {
                        "federalists": 5
                    }
                },
                {
                    "text": "Это слишком радикально. Нужен компромисс.",
                    "next_node": "pragmatic_approach",
                    "faction_points": {
                        "pragmatists": 10
                    }
                }
            ]
        }
    }',
    '{
        "separatist_path": {
            "title": "Путь Сепаратиста",
            "description": "Полная поддержка независимости Квебека любой ценой",
            "requirements": {
                "separatists_points": 50
            },
            "outcomes": [
                "radical_independence_movement",
                "international_support_seeking",
                "cultural_preservation_focus"
            ],
            "rewards_modifier": 1.2
        },
        "federalist_path": {
            "title": "Путь Федералиста",
            "description": "Поддержка единства Канады и экономической стабильности",
            "requirements": {
                "federalists_points": 50
            },
            "outcomes": [
                "strengthened_federal_ties",
                "economic_reform_initiatives",
                "cultural_integration_programs"
            ],
            "rewards_modifier": 1.1
        },
        "pragmatic_path": {
            "title": "Путь Прагматика",
            "description": "Поиск компромисса между независимостью и единством",
            "requirements": {
                "pragmatists_points": 50
            },
            "outcomes": [
                "enhanced_autonomy_agreement",
                "bilateral_cooperation_accords",
                "cultural_exchange_programs"
            ],
            "rewards_modifier": 1.3
        }
    }',
    '{
        "montreal_old_port": {
            "name": "Старый Порт Монреаля",
            "description": "Исторический район, где проходят основные политические митинги",
            "type": "public_square",
            "coordinates": {
                "lat": 45.4995,
                "lng": -73.5536
            },
            "activities": [
                "political_rallies",
                "public_debates",
                "cultural_performances"
            ]
        },
        "quebec_national_assembly": {
            "name": "Национальное Собрание Квебека",
            "description": "Здание парламента Квебека в Квебек-Сити",
            "type": "government_building",
            "coordinates": {
                "lat": 46.8139,
                "lng": -71.2080
            },
            "activities": [
                "legislative_sessions",
                "press_conferences",
                "official_debates"
            ]
        },
        "montreal_francophone_university": {
            "name": "Франкофонный университет Монреаля",
            "description": "Центр интеллектуальных дебатов о квебекской идентичности",
            "type": "educational_institution",
            "coordinates": {
                "lat": 45.5048,
                "lng": -73.5772
            },
            "activities": [
                "academic_debates",
                "research_presentations",
                "student_protests"
            ]
        }
    }',
    '{
        "quebec_nationalist": {
            "name": "Марк Лефевр",
            "role": "Лидер сепаратистского движения",
            "personality": "passionate, charismatic, uncompromising",
            "background": "Бывший преподаватель университета, потерял работу из-за англофикации",
            "dialogue_style": "emotional, uses french_phrases",
            "faction": "separatists"
        },
        "federalist_activist": {
            "name": "Сара Макдональд",
            "role": "Координатор федеральных программ",
            "personality": "pragmatic, analytical, concerned",
            "background": "Экономист из Торонто, работает в Монреале",
            "dialogue_style": "factual, uses economic_arguments",
            "faction": "federalists"
        },
        "pragmatic_politician": {
            "name": "Жан-Пьер Дюмон",
            "role": "Независимый депутат",
            "personality": "diplomatic, thoughtful, balanced",
            "background": "Опытный политик, бывший министр",
            "dialogue_style": "measured, seeks_common_ground",
            "faction": "pragmatists"
        },
        "young_activist": {
            "name": "София Тремблей",
            "role": "Студентка-активистка",
            "personality": "idealistic, tech-savvy, impatient",
            "background": "Изучает политологию, активна в соцсетях",
            "dialogue_style": "modern, uses_social_media_references",
            "faction": "variable_based_on_player_choice"
        }
    }',
    '{
        "sovereignty_manifesto": {
            "name": "Манифест Суверенитета",
            "type": "quest_document",
            "description": "Документ с аргументами за независимость Квебека",
            "rarity": "uncommon",
            "faction_bonus": "separatists"
        },
        "federalist_briefing": {
            "name": "Федералистский Брифинг",
            "type": "quest_document",
            "description": "Анализ экономических преимуществ единства Канады",
            "rarity": "uncommon",
            "faction_bonus": "federalists"
        },
        "compromise_proposal": {
            "name": "Предложение Компромисса",
            "type": "quest_document",
            "description": "Детальный план расширенной автономии Квебека",
            "rarity": "rare",
            "faction_bonus": "pragmatists"
        }
    }',
    '{
        "referendum_announcement": {
            "title": "Объявление Референдума",
            "description": "Правительство объявляет о новом референдуме по независимости",
            "trigger_conditions": {
                "player_influence": 30,
                "debate_participation": 3
            },
            "outcomes": [
                "increased_public_interest",
                "media_coverage_boost",
                "faction_tension_rise"
            ]
        },
        "international_observers": {
            "title": "Прибытие Международных Наблюдателей",
            "description": "ООН отправляет наблюдателей для мониторинга политической ситуации",
            "trigger_conditions": {
                "international_attention": "high",
                "media_coverage": "extensive"
            },
            "outcomes": [
                "diplomatic_pressure",
                "increased_legitimacy",
                "foreign_investment_changes"
            ]
        },
        "cultural_festival": {
            "title": "Фестиваль Квебекской Культуры",
            "description": "Масштабный фестиваль для демонстрации квебекской идентичности",
            "trigger_conditions": {
                "cultural_focus": "high",
                "public_support": "growing"
            },
            "outcomes": [
                "national_pride_boost",
                "tourist_influx",
                "cultural_diplomacy_opportunities"
            ]
        }
    }',
    '{
        "verbal_duels": {
            "description": "Дебаты с использованием риторических приемов и фактов",
            "difficulty": "medium",
            "rewards": "influence_points"
        },
        "crowd_control": {
            "description": "Управление толпой во время митингов",
            "difficulty": "hard",
            "rewards": "supporter_loyalty"
        },
        "cyber_propaganda": {
            "description": "Хакерские атаки на оппозиционные медиа-платформы",
            "difficulty": "expert",
            "rewards": "information_advantage"
        }
    }',
    '{
        "independence_victory": {
            "title": "Триумф Независимости",
            "description": "Квебек становится независимым государством",
            "requirements": {
                "separatists_dominance": true,
                "international_support": "secured",
                "economic_plan": "viable"
            },
            "rewards": {
                "experience": 30000,
                "currency": {
                    "type": "eddies",
                    "amount": 50000
                },
                "items": [
                    {
                        "name": "Quebec Independence Medal",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "new_sovereign_state",
                "economic_challenges",
                "cultural_renaissance"
            ]
        },
        "federalist_success": {
            "title": "Укрепление Федерализма",
            "description": "Квебек остается в составе Канады с улучшенными правами",
            "requirements": {
                "federalists_dominance": true,
                "compromise_agreement": "reached",
                "economic_stability": "maintained"
            },
            "rewards": {
                "experience": 28000,
                "currency": {
                    "type": "eddies",
                    "amount": 45000
                },
                "items": [
                    {
                        "name": "Canadian Unity Award",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "enhanced_autonomy",
                "federal_investments",
                "cultural_preservation_guarantees"
            ]
        },
        "pragmatic_compromise": {
            "title": "Прагматический Компромисс",
            "description": "Достигнуто соглашение об особом статусе Квебека",
            "requirements": {
                "pragmatists_dominance": true,
                "bilateral_agreement": "signed",
                "public_support": "majority"
            },
            "rewards": {
                "experience": 32000,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "Quebec Accord Token",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "special_status_agreement",
                "balanced_autonomy",
                "long_term_stability"
            ]
        },
        "crisis_escalation": {
            "title": "Политический Кризис",
            "description": "Дебаты перерастают в социальный конфликт",
            "requirements": {
                "radical_actions": "taken",
                "public_division": "extreme",
                "violence_incidents": "multiple"
            },
            "rewards": {
                "experience": 20000,
                "currency": {
                    "type": "eddies",
                    "amount": 30000
                },
                "items": [
                    {
                        "name": "Chaos Catalyst",
                        "type": "quest_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "social_unrest",
                "international_concern",
                "emergency_measures"
            ]
        }
    }',
    '[
        "dialogue_system: advanced",
        "faction_system: dynamic",
        "influence_mechanics: sophisticated",
        "event_system: reactive",
        "localization: french_english_support"
    ]',
    '[
        "dialogue_choices_affect_multiple_factions",
        "time_pressure_on_key_decisions",
        "replayability_through_different_paths",
        "cultural_authenticity_verification_needed",
        "political_sensitivity_review_required"
    ]',
    '[
        "cultural_references_accuracy",
        "political_balance_maintained",
        "dialogue_branching_functionality",
        "faction_point_calculations",
        "multiple_ending_accessibility",
        "performance_impact_assessment"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-separatism-debates-montreal-2020-2029';
