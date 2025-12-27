--liquibase formatted sql

--changeset backend:quest-art-basel-miami-2020-2029-import runOnChange:true
--comment: Import quest "Арт Базель (Майами 2020-2029)" into database

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
    'content-narrative-quest-art-basel-miami-2020-2029',
    '{
        "id": "content-narrative-quest-art-basel-miami-2020-2029",
        "title": "Quest: Art Basel Miami (Miami 2020-2029)",
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
            "art-festival",
            "contemporary-art",
            "nft",
            "digital-art"
        ],
        "topics": [
            "contemporary-art-scene",
            "digital-art-revolution",
            "nft-marketplace",
            "corporate-art-patronage",
            "underground-art-movements"
        ],
        "related_systems": [
            "narrative-service",
            "quest-service",
            "art-mechanics",
            "digital-asset-systems"
        ],
        "related_documents": [
            {
                "id": "content-quests-master-list-2020-2093",
                "relation": "references"
            },
            {
                "id": "mechanics-digital-art-mechanics",
                "relation": "implements"
            }
        ],
        "source": "shared/docs/knowledge/canon/narrative/quests/art-basel-miami-2020-2029.md",
        "visibility": "internal",
        "audience": [
            "content",
            "narrative",
            "live-ops"
        ],
        "risk_level": "medium"
    }',
    'content-narrative-quest-art-basel-miami-2020-2029',
    '[Canon/Lore] Квест: Арт Базель (Майами 2020-2029)',
    'Майами 2020-2029 — центр мирового искусства, где Art Basel стал эпическим событием цифровой эпохи.',
    'active',
    16,
    34,
    82,
    'medium',
    'narrative',
    'contemporary_art_scene',
    '{
        "experience": 34500,
        "currency": {
            "type": "eddies",
            "amount": 58000
        },
        "items": [
            {
                "name": "Digital Art Masterpiece NFT",
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
            "level": 16
        }
    ]',
    '[
        {
            "id": "witness_art_sale",
            "title": "Стать свидетелем продажи искусства",
            "description": "Увидеть скандальную продажу цифрового произведения искусства",
            "type": "event_witness",
            "optional": false
        },
        {
            "id": "choose_art_path",
            "title": "Выбрать художественный путь",
            "description": "Определить свою позицию в мире современного искусства",
            "type": "choice",
            "optional": false
        },
        {
            "id": "create_digital_art",
            "title": "Создать цифровое искусство",
            "description": "Создать интерактивное произведение искусства для выставки",
            "type": "creative_task",
            "success_rate": 0.75,
            "optional": false
        },
        {
            "id": "network_artists",
            "title": "Установить контакты с художниками",
            "description": "Нанять 4 художника для совместной выставки",
            "type": "recruitment",
            "count": 4,
            "optional": false
        },
        {
            "id": "curate_exhibition",
            "title": "Кураторствовать выставку",
            "description": "Организовать и провести художественную выставку на Art Basel",
            "type": "event_organization",
            "optional": false
        },
        {
            "id": "influence_art_market",
            "title": "Влиять на арт-рынок",
            "description": "Изменить цены на цифровое искусство на 25%",
            "type": "market_manipulation",
            "threshold": 25,
            "optional": true
        },
        {
            "id": "final_art_decision",
            "title": "Принять финальное художественное решение",
            "description": "Определить будущее искусства в Майами",
            "type": "choice",
            "optional": false
        }
    ]',
    '{
        "art_market_disruption": {
            "speaker": "frustrated_artist",
            "text": "Ces corporations ont tout detruit! Mon art digital vaut des millions sur le marche noir, mais les galeristes prennent 90% des profits. L''art devrait etre libre, pas une marchandise!",
            "choices": [
                {
                    "text": "Je vais creer de l''art authentique, loin du systeme commercial.",
                    "next_node": "authentic_artist",
                    "faction_points": {
                        "independents": 20
                    }
                },
                {
                    "text": "Le marche digital offre des opportunites incroyables.",
                    "next_node": "market_opportunist",
                    "faction_points": {
                        "entrepreneurs": 15
                    }
                },
                {
                    "text": "Je vais hacker le systeme et redistribuer la richesse artistique.",
                    "next_node": "digital_revolutionary",
                    "faction_points": {
                        "hackers": 25
                    }
                }
            ]
        }
    }',
    '{
        "digital_artist": {
            "title": "Путь Цифрового Художника",
            "description": "Создание революционного цифрового искусства, свободного от корпоративного контроля",
            "requirements": {
                "digital_creativity": 70
            },
            "outcomes": [
                "artistic_revolution",
                "digital_innovation",
                "underground_fame"
            ],
            "rewards_modifier": 1.3
        },
        "art_market_tycoon": {
            "title": "Путь Магната Арт-Рынка",
            "description": "Контроль над рынком цифрового искусства и NFT через бизнес-империю",
            "requirements": {
                "business_networking": 65
            },
            "outcomes": [
                "market_dominance",
                "financial_empire",
                "industry_reshaping"
            ],
            "rewards_modifier": 1.5
        },
        "art_curator": {
            "title": "Путь Куратора Искусства",
            "description": "Организация и управление художественными событиями и коллекциями",
            "requirements": {
                "curation_expertise": 60
            },
            "outcomes": [
                "cultural_influence",
                "event_success",
                "artistic_network"
            ],
            "rewards_modifier": 1.2
        },
        "art_hacker": {
            "title": "Путь Хакера Искусства",
            "description": "Использование технологий для подрыва системы и перераспределения художественной ценности",
            "requirements": {
                "hacking_skills": 55
            },
            "outcomes": [
                "system_disruption",
                "wealth_redistribution",
                "digital_anarchy"
            ],
            "rewards_modifier": 1.1
        }
    }',
    '{
        "wynwood_art_district": {
            "name": "Арт-Дистрикт Винвуд",
            "description": "Исторический район с галереями, уличным искусством и цифровыми инсталляциями",
            "type": "cultural_district",
            "coordinates": {
                "lat": 25.8017,
                "lng": -80.1992
            },
            "activities": [
                "art_exhibitions",
                "gallery_visits",
                "street_art_creation"
            ]
        },
        "art_basel_main_venue": {
            "name": "Главная Площадка Art Basel",
            "description": "Центральная выставочная площадка с самыми престижными галереями",
            "type": "exhibition_hall",
            "coordinates": {
                "lat": 25.7617,
                "lng": -80.1939
            },
            "activities": [
                "premier_exhibitions",
                "networking_events",
                "art_auctions"
            ]
        },
        "underground_art_lab": {
            "name": "Подпольная Художественная Лаборатория",
            "description": "Секретная студия для создания революционного цифрового искусства",
            "type": "creative_workshop",
            "coordinates": {
                "lat": 25.7726,
                "lng": -80.1856
            },
            "activities": [
                "digital_art_creation",
                "hacker_meetings",
                "experimental_projects"
            ]
        }
    }',
    '{
        "revolutionary_artist": {
            "name": "Алекс Нексус Чанг",
            "role": "Революционный цифровой художник",
            "personality": "visionary, rebellious, technically_gifted",
            "background": "Бывший корпоративный дизайнер, теперь создает вирусное искусство",
            "dialogue_style": "futuristic_vision, technical_enthusiasm",
            "faction": "innovators"
        },
        "elite_collector": {
            "name": "Виктория фон Рихтен",
            "role": "Элитный коллекционер искусства",
            "personality": "discerning, wealthy, influential",
            "background": "Наследница европейской династии, контролирует значительную часть арт-рынка",
            "dialogue_style": "sophisticated_taste, business_acumen",
            "faction": "patrons"
        },
        "art_market_analyst": {
            "name": "Доктор Маркус Рейн",
            "role": "Аналитик арт-рынка",
            "personality": "analytical, ambitious, calculating",
            "background": "Специалист по цифровым активам, предсказывает тренды искусства",
            "dialogue_style": "data_driven, market_forecasting",
            "faction": "capitalists"
        },
        "underground_curator": {
            "name": "Рэй Шэдоу Гомес",
            "role": "Подпольный куратор",
            "personality": "resourceful, street_smart, idealistic",
            "background": "Бывший галерист, ушедший в подполье против коммерциализации искусства",
            "dialogue_style": "grassroots_activism, practical_underground",
            "faction": "rebels"
        }
    }',
    '{
        "neural_art_implant": {
            "name": "Нейронный Имплант Искусства",
            "type": "cyberware_upgrade",
            "description": "Имплант для глубокого восприятия и создания цифрового искусства",
            "rarity": "legendary",
            "faction_bonus": "innovators"
        },
        "digital_art_nft": {
            "name": "NFT Цифрового Искусства",
            "type": "digital_asset",
            "description": "Токен уникального цифрового произведения искусства",
            "rarity": "epic",
            "faction_bonus": "capitalists"
        },
        "holographic_projector": {
            "name": "Голографический Проектор",
            "type": "tech_gadget",
            "description": "Устройство для создания интерактивных художественных инсталляций",
            "rarity": "rare",
            "faction_bonus": "innovators"
        },
        "art_market_scanner": {
            "name": "Сканер Арт-Рынка",
            "type": "analysis_tool",
            "description": "Устройство для анализа трендов и цен на цифровое искусство",
            "rarity": "uncommon",
            "faction_bonus": "capitalists"
        }
    }',
    '{
        "nft_market_crash": {
            "title": "Крах Рынка NFT",
            "description": "Массовое падение цен на цифровое искусство из-за спекулятивного пузыря",
            "trigger_conditions": {
                "market_volatility": "high",
                "speculative_bubble": "burst"
            },
            "outcomes": [
                "wealth_redistribution",
                "market_restructuring",
                "artistic_opportunities"
            ]
        },
        "corporate_art_takeover": {
            "title": "Корпоративный Захват Искусства",
            "description": "Попытка мегакорпорации монополизировать весь арт-рынок Майами",
            "trigger_conditions": {
                "corporate_influence": "dominant",
                "market_consolidation": "advancing"
            },
            "outcomes": [
                "industry_monopolization",
                "artistic_resistance",
                "market_disruption"
            ]
        },
        "digital_art_virus": {
            "title": "Вирус Цифрового Искусства",
            "description": "Хакерская атака, которая делает все NFT в Майами общедоступными",
            "trigger_conditions": {
                "hacker_activity": "intense",
                "digital_security": "breached"
            },
            "outcomes": [
                "art_democratization",
                "system_chaos",
                "revolutionary_change"
            ]
        }
    }',
    '{
        "art_heists": {
            "description": "Тайные операции по краже и перепродаже цифровых произведений искусства",
            "difficulty": "hard",
            "rewards": "artistic_valor"
        },
        "market_manipulation": {
            "description": "Использование данных и влияния для изменения цен на арт-рынке",
            "difficulty": "medium",
            "rewards": "financial_gain"
        },
        "digital_hacking": {
            "description": "Хакерские атаки на системы хранения и продажи искусства",
            "difficulty": "extreme",
            "rewards": "digital_freedom"
        }
    }',
    '{
        "artistic_revolution": {
            "title": "Художественная Революция",
            "description": "Успешная демократизация искусства и разрушение корпоративного контроля",
            "requirements": {
                "digital_democracy": "achieved",
                "corporate_defeat": "complete",
                "art_freedom": "restored"
            },
            "rewards": {
                "experience": 40000,
                "currency": {
                    "type": "eddies",
                    "amount": 75000
                },
                "items": [
                    {
                        "name": "Digital Art Revolution Manifesto",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "art_democratization",
                "creative_freedom",
                "cultural_transformation"
            ]
        },
        "market_dominance": {
            "title": "Доминирование на Рынке",
            "description": "Контроль над всем арт-рынком Майами и международным влиянием",
            "requirements": {
                "market_control": "absolute",
                "financial_empire": "established",
                "industry_leadership": "achieved"
            },
            "rewards": {
                "experience": 37500,
                "currency": {
                    "type": "eddies",
                    "amount": 85000
                },
                "items": [
                    {
                        "name": "Global Art Market Crown",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "market_supremacy",
                "wealth_accumulation",
                "industry_control"
            ]
        },
        "curation_legend": {
            "title": "Легенда Кураторства",
            "description": "Стать самым влиятельным куратором современного искусства в мире",
            "requirements": {
                "cultural_impact": "massive",
                "network_influence": "global",
                "artistic_legacy": "established"
            },
            "rewards": {
                "experience": 38500,
                "currency": {
                    "type": "eddies",
                    "amount": 65000
                },
                "items": [
                    {
                        "name": "Master Curator''s Seal",
                        "type": "achievement_item",
                        "rarity": "legendary"
                    }
                ]
            },
            "narrative_consequences": [
                "cultural_influence",
                "artistic_network",
                "legacy_enduring"
            ]
        },
        "digital_anarchy": {
            "title": "Цифровая Анархия",
            "description": "Разрушение системы и создание новой парадигмы цифрового искусства",
            "requirements": {
                "system_disruption": "complete",
                "digital_freedom": "achieved",
                "anarchy_established": true
            },
            "rewards": {
                "experience": 35500,
                "currency": {
                    "type": "eddies",
                    "amount": 55000
                },
                "items": [
                    {
                        "name": "Digital Anarchy Code",
                        "type": "achievement_item",
                        "rarity": "epic"
                    }
                ]
            },
            "narrative_consequences": [
                "system_destruction",
                "creative_anarchy",
                "new_art_paradigm"
            ]
        },
        "corporate_absorption": {
            "title": "Корпоративное Поглощение",
            "description": "Полная коммерциализация искусства и потеря художественной аутентичности",
            "requirements": {
                "corporate_victory": "decisive",
                "art_commercialization": "complete",
                "creative_suppression": "achieved"
            },
            "rewards": {
                "experience": 20000,
                "currency": {
                    "type": "eddies",
                    "amount": 35000
                },
                "items": [
                    {
                        "name": "Corporate Art Compliance Badge",
                        "type": "quest_item",
                        "rarity": "common"
                    }
                ]
            },
            "narrative_consequences": [
                "art_homogenization",
                "creative_stifling",
                "corporate_dominance"
            ]
        }
    }',
    '[
        "dialogue_system: multilingual_artistic",
        "art_creation_mechanics: digital_specialized",
        "nft_marketplace: blockchain_integrated",
        "curation_system: event_driven",
        "hacking_mechanics: digital_art_focused"
    ]',
    '[
        "authenticity_vs_commercialization_balance",
        "digital_art_creation_complexity",
        "market_manipulation_risks",
        "faction_influence_scaling",
        "artistic_freedom_vs_structure"
    ]',
    '[
        "contemporary_art_accuracy",
        "nft_marketplace_realism",
        "digital_art_technology_balance",
        "corporate_art_representation",
        "artistic_revolution_themes",
        "market_economy_simulation"
    ]',
    NOW(),
    NOW()
);

--rollback DELETE FROM gameplay.quest_definitions WHERE id = 'content-narrative-quest-art-basel-miami-2020-2029';
