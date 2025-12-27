--liquibase formatted sql

--changeset backend:quest-speedboat-race-miami-2020-2029-import runOnChange:true
--comment: Import quest "Гонка на скоростных катерах (Майами 2020-2029)" into database

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
    'content-narrative-quest-speedboat-race-miami-2020-2029',
    '{
        "id": "content-narrative-quest-speedboat-race-miami-2020-2029",
        "title": "Quest: Speedboat Race Miami (Miami 2020-2029)",
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
            "boat-racing",
            "speed",
            "underground-racing",
            "adrenaline"
        ],
        "topics": [
            "high-speed-racing",
            "illegal-racing-scenes",
            "boat-modifications",
            "corporate-sponsorship",
            "adrenaline-culture"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "racing-mechanics",
            "vehicle-customization"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-boat-racing-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/speedboat-race-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "high"
    }',
    'content-narrative-quest-speedboat-race-miami-2020-2029',
    '[Canon/Lore] Квест: Гонка на скоростных катерах (Майами 2020-2029)',
    'Майами 2020-2029 — город скорости и адреналина. Океанские воды стали ареной для самых опасных гонок в мире.',
    'active',
    15,
    32,
    80,
    'hard',
    'narrative',
    'high_speed_racing',
    '{
        "experience": 32500,
        "currency": {
            "type": "eddies",
            "amount": 55000
        },
        "items": [
            {
                "name": "Turbocharged Speedboat Engine",
                "type": "quest_item",
                "rarity": "legendary"
            }
        ]
    }',
    '[
        {
            "completed": "tutorial_basics"
        },
        {
            "level": 15
        },
        {
            "reputation": "street_racer"
        }
    ]',
    '[
        {
            "id": "witness_race_accident",
            "title": "Стать свидетелем гоночной аварии",
            "description": "Увидеть крушение во время подпольной лодочной гонки",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_racing_path",
            "title": "Выбрать гоночный путь",
            "description": "Определить свою позицию в мире скоростных катеров",
            "type": "choice",
            "optional": false
        },
        {
            "id": "acquire_speedboat",
            "title": "Приобрести скоростной катер",
            "description": "Найти или собрать подходящий катер для гонок",
            "type": "vehicle_acquisition",
            "optional": false
        },
        {
            "id": "customize_boat",
            "title": "Модифицировать катер",
            "description": "Установить улучшения для повышения скорости и маневренности",
            "type": "vehicle_modification",
            "success_rate": 0.8,
            "optional": false
        },
        {
            "id": "join_racing_circuit",
            "title": "Присоединиться к гоночному кругу",
            "description": "Войти в подпольную сеть лодочных гонок Майами",
            "type": "network_infiltration",
            "optional": false
        },
        {
            "id": "win_qualifying_race",
            "title": "Выиграть квалификационную гонку",
            "description": "Доказать свои навыки в предварительной гонке",
            "type": "racing_competition",
            "optional": false
        },
        {
            "id": "final_race_decision",
            "title": "Принять финальное гоночное решение",
            "description": "Определить исход карьеры в мире скоростных катеров",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "race_accident_scene": {
            "speaker": "injured_racer",
            "text": "Merde! Cette course etait truquee! Les moteurs suralimentes, les paris manipules... Tout le monde joue avec nos vies pour de l''argent!",
            "choices": [
                {
                    "text": "Je veux devenir pilote et gagner les plus gros prix!",
                    "next_node": "aspiring_champion",
                    "faction_points": {
                        "racers": 25
                    }
                },
                {
                    "text": "Je prefere travailler sur les bateaux - les modifier pour la victoire.",
                    "next_node": "master_mechanic",
                    "faction_points": {
                        "mechanics": 20
                    }
                },
                {
                    "text": "Ca sent l''opportunite d''affaires. Les paris, c''est lucratif.",
                    "next_node": "betting_opportunity",
                    "faction_points": {
                        "gamblers": 15
                    }
                }
            ]
        }
    }',
    '{
        "racing_champion": {
            "title": "Путь Чемпиона Гонок",
            "description": "Стать легендой водных гонок Майами через мастерство и отвагу",
            "requirements": {
                "racing_victories": 8
            },
            "outcomes": [
                "championship_glory",
                "celebrity_status",
                "racing_legacy"
            ],
            "rewards_modifier": 1.5
        },
        "boat_modifier": {
            "title": "Путь Модификатора Катеров",
            "description": "Стать мастером по настройке и улучшению скоростных катеров",
            "requirements": {
                "modification_expertise": 70
            },
            "outcomes": [
                "engineering_breakthrough",
                "lucrative_contracts",
                "industry_influence"
            ],
            "rewards_modifier": 1.3
        },
        "gambling_tycoon": {
            "title": "Путь Азартного Магната",
            "description": "Контроль над ставками и манипуляция результатами гонок",
            "requirements": {
                "betting_network": 65
            },
            "outcomes": [
                "gambling_empire",
                "financial_dominance",
                "underworld_power"
            ],
            "rewards_modifier": 1.4
        },
        "corporate_sponsor": {
            "title": "Путь Корпоративного Спонсора",
            "description": "Интеграция бизнеса и гонок через спонсорство и маркетинг",
            "requirements": {
                "business_acumen": 60
            },
            "outcomes": [
                "commercial_success",
                "mainstream_exposure",
                "racing_professionalism"
            ],
            "rewards_modifier": 1.2
        }
    }',
    '{
        "biscayne_bay_racing_circuit": {
            "name": "Гоночный Круг Бухты Бискейн",
            "description": "Подпольная трасса для лодочных гонок с препятствиями и высокими скоростями",
            "type": "racing_track",
            "coordinates": {
                "lat": 25.7317,
                "lng": -80.2089
            },
            "activities": [
                "speedboat_races",
                "boat_modification",
                "spectator_events"
            ]
        },
        "underground_boat_shop": {
            "name": "Подпольная Лодочная Мастерская",
            "description": "Секретная мастерская для модификации и ремонта скоростных катеров",
            "type": "workshop",
            "coordinates": {
                "lat": 25.7726,
                "lng": -80.1856
            },
            "activities": [
                "vehicle_customization",
                "part_acquisition",
                "mechanic_training"
            ]
        },
        "corporate_dockyard": {
            "name": "Корпоративная Верфь",
            "description": "Официальная верфь корпорации для профессиональных гоночных катеров",
            "type": "corporate_facility",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1939
            },
            "activities": [
                "official_races",
                "sponsorship_deals",
                "technology_showcase"
            ]
        }
    }',
    '{
        "veteran_racer": {
            "name": "Капитан Шторм Гарсия",
            "role": "Легендарный гонщик",
            "personality": "fearless, experienced, charismatic",
            "background": "15 лет в лодочных гонках, пережил десятки аварий",
            "dialogue_style": "sailor_bravado, practical_wisdom",
            "faction": "veterans"
        },
        "chief_mechanic": {
            "name": "Инженер Мария Искра Родригес",
            "role": "Главный механик",
            "personality": "brilliant, detail-oriented, passionate",
            "background": "Бывший инженер корпорации, теперь работает в подполье",
            "dialogue_style": "technical_expertise, enthusiastic_engineering",
            "faction": "mechanics"
        },
        "corporate_sponsor": {
            "name": "Мистер Викторио",
            "role": "Корпоративный спонсор",
            "personality": "ambitious, calculating, polished",
            "background": "Руководитель отдела маркетинга крупной корпорации",
            "dialogue_style": "business_formal, persuasive_deals",
            "faction": "corporates"
        },
        "street_bookmaker": {
            "name": "Быстрый Луис",
            "role": "Букмекер подполья",
            "personality": "shrewd, street-smart, opportunistic",
            "background": "Контролирует большую часть ставок на лодочные гонки в Майами",
            "dialogue_style": "miami_slang, dealmaker_talk",
            "faction": "gamblers"
        }
    }',
    '{
        "turbo_engine_core": {
            "name": "Турбо-Двигатель Ядро",
            "type": "vehicle_upgrade",
            "description": "Высокопроизводительный двигатель для экстремальной скорости",
            "rarity": "epic",
            "faction_bonus": "engineers"
        },
        "anti_grav_stabilizer": {
            "name": "Антигравитационный Стабилизатор",
            "type": "vehicle_modification",
            "description": "Система для лучшего контроля на высоких скоростях",
            "rarity": "rare",
            "faction_bonus": "innovators"
        },
        "neural_racing_implant": {
            "name": "Нейронный Имплант Гонщика",
            "type": "cyberware_upgrade",
            "description": "Имплант для предугадывания волн и улучшения реакции",
            "rarity": "legendary",
            "faction_bonus": "champions"
        },
        "corporate_sponsorship_deal": {
            "name": "Корпоративный Спонсорский Контракт",
            "type": "quest_document",
            "description": "Выгодный контракт на спонсорство гоночной команды",
            "rarity": "uncommon",
            "faction_bonus": "corporates"
        }
    }',
    '{
        "championship_finale": {
            "title": "Финал Чемпионата",
            "description": "Решающая гонка сезона с максимальными ставками и рисками",
            "trigger_conditions": {
                "season_progress": "finals",
                "player_ranking": "top_5"
            },
            "outcomes": [
                "championship_victory",
                "career_defining_moment",
                "massive_payday"
            ]
        },
        "sabotage_incident": {
            "title": "Инцидент Саботажа",
            "description": "Попытка саботажа катеров перед важной гонкой",
            "trigger_conditions": {
                "competition_intensity": "high",
                "rival_conflicts": "escalating"
            },
            "outcomes": [
                "race_cancellation",
                "investigation_launched",
                "underworld_war"
            ]
        },
        "corporate_buyout": {
            "title": "Корпоративный Выкуп",
            "description": "Попытка корпорации монополизировать лодочные гонки",
            "trigger_conditions": {
                "corporate_interest": "growing",
                "underground_scene": "profitable"
            },
            "outcomes": [
                "racing_professionalization",
                "traditional_scene_disruption",
                "sponsor_opportunities"
            ]
        }
    }',
    '{
        "boat_chases": {
            "description": "Преследование на воде с уклонением от патрулей и конкурентов",
            "difficulty": "high",
            "rewards": "evasion_mastery"
        },
        "sabotage_operations": {
            "description": "Тайные операции по подрыву катеров соперников",
            "difficulty": "hard",
            "rewards": "strategic_advantage"
        },
        "racing_duels": {
            "description": "Прямые соревнования с другими гонщиками на воде",
            "difficulty": "extreme",
            "rewards": "racing_glory"
        }
    }',
    '{
        "racing_legend": {
            "title": "Легенда Гонок",
            "description": "Стать непобедимым чемпионом лодочных гонок Майами",
            "requirements": {
                "championship_wins": 5,
                "fan_following": "massive",
                "racing_legacy": "established"
            },
            "rewards": {
                "experience": 38000,
                "currency": {
                    "type": "eddies",
                    "amount": 75000
                },
                "items": [
                    {
                        "name": "Miami Speedboat Champion Trophy",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "racing_celebrity",
                "wealth_accumulation",
                "danger_constant"
            ]
        },
        "engineering_giant": {
            "title": "Инженерный Гигант",
            "description": "Революция в технологии лодочных гонок через инновации",
            "requirements": {
                "technological_breakthroughs": 4,
                "industry_impact": "significant",
                "patents_owned": 8
            },
            "rewards": {
                "experience": 36000,
                "currency": {
                    "type": "eddies",
                    "amount": 65000
                },
                "items": [
                    {
                        "name": "Revolutionary Boat Engine Prototype",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "technological_impact",
                "business_success",
                "industry_transformation"
            ]
        },
        "gambling_lord": {
            "title": "Лорд Азартных Игр",
            "description": "Контроль над всеми ставками на лодочные гонки в Майами",
            "requirements": {
                "betting_network_control": "complete",
                "financial_empire": "established",
                "law_enforcement_evasion": "successful"
            },
            "rewards": {
                "experience": 34000,
                "currency": {
                    "type": "eddies",
                    "amount": 80000
                },
                "items": [
                    {
                        "name": "Golden Speedboat Betting Token",
                        "type": "achievement_item",
                        "rarity": "rare"
                    }
                ]
            },
            "narrative_consequences": [
                "underworld_dominance",
                "massive_wealth",
                "constant_threats"
            ]
        },
        "corporate_mogul": {
            "title": "Корпоративный Магнат",
            "description": "Превращение лодочных гонок в прибыльный бизнес",
            "requirements": {
                "corporate_control": "established",
                "mainstream_success": "achieved",
                "fan_commercialization": "profitable"
            },
            "rewards": {
                "experience": 32000,
                "currency": {
                    "type": "eddies",
                    "amount": 70000
                },
                "items": [
                    {
                        "name": "Corporate Racing Empire Contract",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "business_empire",
                "racing_professionalization",
                "cultural_shift"
            ]
        },
        "crash_victim": {
            "title": "Жертва Катастрофы",
            "description": "Гибель в аварии во время гонки, положив конец карьере",
            "requirements": {
                "fatal_accident": "occurred",
                "survival_failed": true,
                "medical_rescue": "unsuccessful"
            },
            "rewards": {
                "experience": 18000,
                "currency": {
                    "type": "eddies",
                    "amount": 15000
                },
                "items": [
                    {
                        "name": "Racing Memorial Plaque",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "career_ended",
                "legacy_remembered",
                "family_impact"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_racing",
        "racing_mechanics: advanced_boat_physics",
        "vehicle_system: dynamic_modification",
        "betting_system: real_time_odds",
        "event_system: championship_driven"
    ]',
    '[
        "speed_vs_safety_tradeoff",
        "modification_risk_reward",
        "betting_economy_balance",
        "corporate_influence_scaling",
        "racing_skill_progression"
    ]',
    '[
        "boat_racing_realism",
        "speed_mechanics_balance",
        "modification_systems",
        "gambling_mechanics_accuracy",
        "corporate_sports_representation",
        "high_stakes_racing_drama"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-speedboat-race-miami-2020-2029';
