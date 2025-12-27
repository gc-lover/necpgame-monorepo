--liquibase formatted sql

--changeset backend:quest-miami-heat-game-miami-2020-2029-import runOnChange:true
--comment: Import quest "Игра Майами Хит (Майами 2020-2029)" into database

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
    'content-narrative-quest-miami-heat-game-miami-2020-2029',
    '{
        "id": "content-narrative-quest-miami-heat-game-miami-2020-2029",
        "title": "Quest: Miami Heat Game Miami (Miami 2020-2029)",
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
            "basketball",
            "sports",
            "gambling",
            "celebrity-culture"
        ],
        "topics": [
            "professional-sports",
            "celebrity-culture",
            "gambling-syndicates",
            "corporate-sponsorship",
            "fan-loyalty"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "sports-mechanics",
            "gambling-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-sports-gambling-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/miami-heat-game-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-miami-heat-game-miami-2020-2029',
    '[Canon/Lore] Квест: Игра Майами Хит (Майами 2020-2029)',
    'Майами Хит 2020-2029 — легендарная баскетбольная команда, ставшая символом города.',
    'active',
    12,
    26,
    72,
    'medium',
    'narrative',
    'sports_entertainment',
    '{
        "experience": 26500,
        "currency": {
            "type": "eddies",
            "amount": 43000
        },
        "items": [
            {
                "name": "Miami Heat Championship Ring Replica",
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
            "reputation": "miami_connected"
        }
    ]',
    '[
        {
            "id": "witness_gambling_deal",
            "title": "Стать свидетелем сделки по ставкам",
            "description": "Увидеть подозрительную договоренность перед матчем",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_sports_role",
            "title": "Выбрать спортивную роль",
            "description": "Определить свою позицию в мире баскетбола Майами",
            "type": "choice",
            "optional": false
        },
        {
            "id": "place_betting_wager",
            "title": "Сделать ставку на игру",
            "description": "Поставить деньги на исход матча Майами Хит",
            "type": "gambling_action",
            "risk_level": "high",
            "optional": false
        },
        {
            "id": "gather_fan_support",
            "title": "Собрать поддержку фанатов",
            "description": "Нанять 3 фанатов для поддержки команды",
            "type": "recruitment",
            "count": 3,
            "optional": false
        },
        {
            "id": "influence_game_outcome",
            "title": "Влиять на исход игры",
            "description": "Использовать различные методы для изменения результата матча",
            "type": "manipulation",
            "optional": false
        },
        {
            "id": "organize_fan_event",
            "title": "Организовать фанатское мероприятие",
            "description": "Создать публичное мероприятие в поддержку Майами Хит",
            "type": "event_organization",
            "optional": true
        },
        {
            "id": "final_sports_decision",
            "title": "Принять финальное спортивное решение",
            "description": "Определить исход истории с Майами Хит",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "gambling_exposure": {
            "speaker": "shady_bookmaker",
            "text": "Hey amigo, tu as vu cette reunion secrete avant le match? Des types en costard parlent de ''fixer'' le jeu. Ca sent mauvais pour Miami Heat...",
            "choices": [
                {
                    "text": "Je veux en savoir plus. Ca pourrait etre lucratif.",
                    "next_node": "gambling_opportunity",
                    "faction_points": {
                        "gamblers": 20
                    }
                },
                {
                    "text": "C''est malhonnête! Je dois proteger l''equipe.",
                    "next_node": "team_loyalist",
                    "faction_points": {
                        "fans": 15
                    }
                },
                {
                    "text": "Ca m''interesse. Peut-etre que je peux gagner gros.",
                    "next_node": "betting_enthusiast",
                    "faction_points": {
                        "opportunists": 10
                    }
                }
            ]
        }
    }',
    '{
        "gambling_lord": {
            "title": "Путь Лорда Азартных Игр",
            "description": "Контроль над ставками и манипуляция результатами матчей для максимальной прибыли",
            "requirements": {
                "gambling_expertise": 60
            },
            "outcomes": [
                "betting_empire",
                "sports_corruption",
                "underworld_wealth"
            ],
            "rewards_modifier": 1.4
        },
        "team_champion": {
            "title": "Путь Чемпиона Команды",
            "description": "Защита честности спорта и поддержка Майами Хит в их стремлении к победе",
            "requirements": {
                "fan_loyalty": 60
            },
            "outcomes": [
                "championship_glory",
                "fan_hero_status",
                "team_legacy"
            ],
            "rewards_modifier": 1.2
        },
        "corporate_sponsor": {
            "title": "Путь Корпоративного Спонсора",
            "description": "Интеграция бизнеса и спорта для взаимной выгоды",
            "requirements": {
                "business_networking": 50
            },
            "outcomes": [
                "commercial_success",
                "team_stability",
                "fan_business_growth"
            ],
            "rewards_modifier": 1.3
        },
        "investigative_reporter": {
            "title": "Путь Расследующего Журналиста",
            "description": "Разоблачение коррупции в профессиональном баскетболе Майами",
            "requirements": {
                "investigation_skills": 50
            },
            "outcomes": [
                "scandal_exposure",
                "sports_reform",
                "personal_risk"
            ],
            "rewards_modifier": 0.9
        }
    }',
    '{
        "american_airlines_arena": {
            "name": "American Airlines Arena",
            "description": "Домашняя арена Майами Хит с неоновым освещением и AR-эффектами",
            "type": "sports_venue",
            "coordinates": {
                "lat": 25.7814,
                "lng": -80.1867
            },
            "activities": [
                "basketball_games",
                "fan_meetings",
                "press_conferences"
            ]
        },
        "south_beach_sports_bar": {
            "name": "Спортивный Бар Саут-Бич",
            "description": "Известное место встреч фанатов и обсуждения ставок на спорт",
            "type": "fan_hangout",
            "coordinates": {
                "lat": 25.7826,
                "lng": -80.1342
            },
            "activities": [
                "betting_discussions",
                "fan_gatherings",
                "sports_analysis"
            ]
        },
        "wynwood_sports_complex": {
            "name": "Спортивный Комплекс Винвуд",
            "description": "Современный комплекс для тренировок и корпоративных мероприятий",
            "type": "training_facility",
            "coordinates": {
                "lat": 25.8045,
                "lng": -80.1989
            },
            "activities": [
                "team_practices",
                "sponsor_meetings",
                "fan_events"
            ]
        }
    }',
    '{
        "star_player": {
            "name": "Дуэйн Флэш Уэйд",
            "role": "Звезда Майами Хит",
            "personality": "charismatic, competitive, principled",
            "background": "Легендарный игрок с имплантами усиления рефлексов",
            "dialogue_style": "confident_champion, motivational_speaker",
            "faction": "team_loyalists"
        },
        "shady_bookmaker": {
            "name": "Винни Кости Розарио",
            "role": "Букмекер подполья",
            "personality": "calculating, charming, ruthless",
            "background": "Контролирует большую часть ставок на спорт в Майами",
            "dialogue_style": "smooth_operator, persuasive_dealer",
            "faction": "gamblers"
        },
        "team_owner": {
            "name": "Мистер Аренио",
            "role": "Владелец команды",
            "personality": "ambitious, corporate, pragmatic",
            "background": "Бизнесмен, видящий в команде источник прибыли",
            "dialogue_style": "business_executive, profit_focused",
            "faction": "corporate"
        },
        "diehard_fan": {
            "name": "Карлос Гром Мендес",
            "role": "Ярый фанат",
            "personality": "passionate, loyal, street_smart",
            "background": "Посещает все матчи, знает все статистику команды",
            "dialogue_style": "enthusiastic_fan, team_expert",
            "faction": "fans"
        }
    }',
    '{
        "heat_jersey_autographed": {
            "name": "Автограф Майами Хит",
            "type": "quest_collectible",
            "description": "Футболка с автографом звездного игрока команды",
            "rarity": "epic",
            "faction_bonus": "fans"
        },
        "betting_terminal": {
            "name": "Терминал для Ставок",
            "type": "quest_gadget",
            "description": "Портативное устройство для размещения ставок в реальном времени",
            "rarity": "uncommon",
            "faction_bonus": "gamblers"
        },
        "championship_trophy_mini": {
            "name": "Миниатюрный Кубок Чемпиона",
            "type": "quest_trophy",
            "description": "Реплика чемпионского кубка NBA для победителей",
            "rarity": "legendary",
            "faction_bonus": "team_loyalists"
        },
        "corporate_contract": {
            "name": "Корпоративный Контракт",
            "type": "quest_document",
            "description": "Спонсорский договор с корпорацией для команды",
            "rarity": "rare",
            "faction_bonus": "corporate"
        }
    }',
    '{
        "championship_game": {
            "title": "Финальная Игра Чемпионата",
            "description": "Решающий матч Майами Хит за чемпионство NBA",
            "trigger_conditions": {
                "player_influence": 30,
                "season_progress": "finals"
            },
            "outcomes": [
                "championship_victory",
                "heartbreaking_defeat",
                "controversial_result"
            ]
        },
        "gambling_scandal": {
            "title": "Скандал с Ставками",
            "description": "Разоблачение крупной схемы договорных матчей в NBA",
            "trigger_conditions": {
                "investigation_depth": "deep",
                "evidence_collected": "substantial"
            },
            "outcomes": [
                "league_wide_investigation",
                "team_disqualification",
                "gambling_crackdown"
            ]
        },
        "fan_riot": {
            "title": "Фанатский Бунт",
            "description": "Массовые беспорядки после спорного решения в матче",
            "trigger_conditions": {
                "fan_tension": "high",
                "controversial_call": "made"
            },
            "outcomes": [
                "stadium_damage",
                "media_outrage",
                "team_reputation_hit"
            ]
        }
    }',
    '{
        "street_brawls": {
            "description": "Драки между конкурирующими фанатскими группами",
            "difficulty": "medium",
            "rewards": "street_reputation"
        },
        "stealth_infiltration": {
            "description": "Проникновение в раздевалки и офисы для сбора информации",
            "difficulty": "hard",
            "rewards": "intelligence_bonuses"
        },
        "chase_sequences": {
            "description": "Погони от букмекеров или корпоративных агентов",
            "difficulty": "high",
            "rewards": "evasion_skills"
        }
    }',
    '{
        "championship_glory": {
            "title": "Слава Чемпиона",
            "description": "Успешная защита честности спорта и победа в чемпионате",
            "requirements": {
                "team_loyalty": "dominant",
                "corruption_exposed": true,
                "championship_won": true
            },
            "rewards": {
                "experience": 32000,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "NBA Championship Ring",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "team_legend",
                "fan_loyalty_boost",
                "sports_integrity_restored"
            ]
        },
        "gambling_empire": {
            "title": "Империя Азартных Игр",
            "description": "Контроль над всеми ставками на спорт в Майами",
            "requirements": {
                "gambling_network": "dominant",
                "betting_operations": "successful",
                "competitors_eliminated": true
            },
            "rewards": {
                "experience": 30000,
                "currency": {
                    "type": "eddies",
                    "amount": 65000
                },
                "items": [
                    {
                        "name": "Golden Betting Chip",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "underworld_power",
                "sports_corruption",
                "constant_surveillance"
            ]
        },
        "corporate_takeover": {
            "title": "Корпоративный Захват",
            "description": "Успешная коммерциализация команды и спортивного бизнеса",
            "requirements": {
                "corporate_influence": "dominant",
                "business_deals": "profitable",
                "fan_commercialization": "accepted"
            },
            "rewards": {
                "experience": 28000,
                "currency": {
                    "type": "eddies",
                    "amount": 60000
                },
                "items": [
                    {
                        "name": "Corporate Sports Empire Token",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "business_success",
                "sports_commercialization",
                "fan_division"
            ]
        },
        "scandal_exposure": {
            "title": "Разоблачение Скандала",
            "description": "Полное разоблачение коррупции в профессиональном баскетболе",
            "requirements": {
                "investigation_complete": true,
                "evidence_overwhelming": true,
                "whistleblower_safe": true
            },
            "rewards": {
                "experience": 24000,
                "currency": {
                    "type": "eddies",
                    "amount": 35000
                },
                "items": [
                    {
                        "name": "Journalist''s Integrity Award",
                        "type": "achievement_item",
                        "rarity": "uncommon"
                    }
                ]
            },
            "narrative_consequences": [
                "sports_reform",
                "personal_recognition",
                "industry_disruption"
            ]
        },
        "fan_tragedy": {
            "title": "Фанатская Трагедия",
            "description": "Катастрофические последствия для фанатов и команды",
            "requirements": {
                "decisions_failed": "critical",
                "fan_revolt": "violent",
                "team_destroyed": true
            },
            "rewards": {
                "experience": 15000,
                "currency": {
                    "type": "eddies",
                    "amount": 20000
                },
                "items": [
                    {
                        "name": "Shattered Dreams Trophy",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "community_tragedy",
                "sports_decline",
                "personal_guilty"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_sports",
        "gambling_mechanics: advanced_betting",
        "sports_simulation: nba_realistic",
        "faction_system: sports_world_dynamics",
        "event_system: championship_driven"
    ]',
    '[
        "gambling_risk_reward_balance",
        "sports_drama_vs_realism",
        "fan_loyalty_vs_corruption",
        "corporate_influence_scaling",
        "championship_stakes_appropriate"
    ]',
    '[
        "sports_realism_accuracy",
        "gambling_mechanics_balance",
        "fan_culture_authenticity",
        "corporate_sports_representation",
        "championship_tension_building",
        "multiple_ending_accessibility"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-miami-heat-game-miami-2020-2029';
