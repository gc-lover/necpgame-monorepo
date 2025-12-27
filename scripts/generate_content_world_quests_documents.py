import yaml
from pathlib import Path
from datetime import datetime

WORLD_QUESTS_DOCUMENTS = [
    {
        'name': 'content-world-quests-epic-sagas',
        'title': 'Content - World Quests Epic Sagas',
        'description': 'Эпические саги мировых квестов'
    },
    {
        'name': 'content-world-quests-regional-conflicts',
        'title': 'Content - World Quests Regional Conflicts',
        'description': 'Региональные конфликты в мировых квестах'
    },
    {
        'name': 'content-world-quests-mystery-investigations',
        'title': 'Content - World Quests Mystery Investigations',
        'description': 'Расследования тайн в мировых квестах'
    },
    {
        'name': 'content-world-quests-heroic-challenges',
        'title': 'Content - World Quests Heroic Challenges',
        'description': 'Героические испытания в мировых квестах'
    },
    {
        'name': 'content-world-quests-exploration-discoveries',
        'title': 'Content - World Quests Exploration Discoveries',
        'description': 'Исследования и открытия в мировых квестах'
    }
]

def create_world_quests_document_template(doc_data):
    """Create a detailed world quests document template"""
    template = {
        'metadata': {
            'id': f'content-world-quests-{doc_data["name"].split("-")[-1]}',
            'title': doc_data['title'],
            'document_type': 'content',
            'category': 'world-quests',
            'subcategory': 'quest-types',
            'status': 'draft',
            'version': '1.0.0',
            'last_updated': datetime.now().strftime('%Y-%m-%dT%H:%M:%SZ'),
            'concept_approved': False,
            'concept_reviewed_at': '',
            'owners': [
                {
                    'role': 'content_designer',
                    'contact': 'content@necp.game'
                }
            ],
            'tags': [
                'content',
                'world-quests',
                'narrative'
            ],
            'topics': [
                'quest-design',
                'storytelling',
                'world-building'
            ],
            'related_systems': [
                'quest-service',
                'narrative-service',
                'world-state-service'
            ],
            'related_documents': [
                {
                    'id': 'content-quest-system-overview',
                    'relation': 'references'
                },
                {
                    'id': 'content-world-building-framework',
                    'relation': 'part_of'
                }
            ],
            'source': f'knowledge/content/world-quests/{doc_data["name"]}.yaml',
            'visibility': 'internal',
            'audience': [
                'content',
                'design',
                'narrative'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо разработать {doc_data["description"].lower()} для создания увлекательного и связного мира.',
            'goal': f'Описать структуру, механики и интеграцию {doc_data["description"].lower()} в игровую вселенную.',
            'essence': doc_data["description"],
            'key_points': [
                'Нарративная структура квестов',
                'Механики взаимодействия с игроком',
                'Влияние на мир и персонажей',
                'Связь с основным лором'
            ]
        },
        'quest_structure': {
            'core_elements': [
                {
                    'element': 'narrative_hook',
                    'description': 'Захватывающее начало квеста',
                    'purpose': 'привлечение внимания игрока',
                    'examples': ['таинственное исчезновение', 'необычное событие', 'личная просьба']
                },
                {
                    'element': 'character_development',
                    'description': 'Развитие персонажей в квесте',
                    'purpose': 'создание эмоциональной связи',
                    'examples': ['личный рост героя', 'отношения с NPC', 'моральные дилеммы']
                },
                {
                    'element': 'world_integration',
                    'description': 'Интеграция с игровым миром',
                    'purpose': 'создание ощущения живого мира',
                    'examples': ['реакция NPC', 'изменения окружения', 'последствия действий']
                },
                {
                    'element': 'reward_system',
                    'description': 'Система вознаграждений',
                    'purpose': 'мотивация и прогрессия',
                    'examples': ['опыт', 'предметы', 'репутация', 'новые возможности']
                }
            ],
            'narrative_arcs': [
                {
                    'arc_type': 'hero_journey',
                    'stages': ['призыв', 'испытания', 'преображение', 'возвращение'],
                    'emotional_impact': 'высокий',
                    'player_agency': 'высокая'
                },
                {
                    'arc_type': 'mystery_solving',
                    'stages': ['открытие', 'расследование', 'разгадка', 'разрешение'],
                    'emotional_impact': 'средний',
                    'player_agency': 'высокая'
                },
                {
                    'arc_type': 'conflict_resolution',
                    'stages': ['эскалация', 'конфронтация', 'разрешение', 'последствия'],
                    'emotional_impact': 'переменный',
                    'player_agency': 'очень высокая'
                },
                {
                    'arc_type': 'exploration_discovery',
                    'stages': ['открытие', 'исследование', 'открытие', 'интеграция'],
                    'emotional_impact': 'низкий',
                    'player_agency': 'очень высокая'
                }
            ]
        },
        'player_engagement': {
            'interaction_mechanics': [
                {
                    'mechanic': 'choice_systems',
                    'description': 'Системы выбора в квестах',
                    'impact': 'на исход и последствия',
                    'complexity': 'высокая'
                },
                {
                    'mechanic': 'pacing_control',
                    'description': 'Контроль темпа повествования',
                    'impact': 'на вовлеченность игрока',
                    'complexity': 'средняя'
                },
                {
                    'mechanic': 'dynamic_content',
                    'description': 'Динамический контент квестов',
                    'impact': 'на реиграбельность',
                    'complexity': 'высокая'
                },
                {
                    'mechanic': 'personalization',
                    'description': 'Персонализация квестов',
                    'impact': 'на эмоциональную связь',
                    'complexity': 'очень высокая'
                }
            ],
            'difficulty_scaling': [
                {
                    'aspect': 'skill_based',
                    'adjustment': 'на основе умений игрока',
                    'benefits': 'справедливость',
                    'challenges': 'баланс сложности'
                },
                {
                    'aspect': 'time_based',
                    'adjustment': 'на основе затраченного времени',
                    'benefits': 'адаптация к стилю игры',
                    'challenges': 'предсказуемость'
                },
                {
                    'aspect': 'choice_based',
                    'adjustment': 'на основе предыдущих выборов',
                    'benefits': 'персонализация',
                    'challenges': 'сложность дизайна'
                },
                {
                    'aspect': 'social_based',
                    'adjustment': 'на основе социальных связей',
                    'benefits': 'глубина отношений',
                    'challenges': 'зависимость от прогрессии'
                }
            ]
        },
        'world_integration': {
            'environmental_storytelling': [
                {
                    'technique': 'location_narratives',
                    'description': 'Истории через локации',
                    'examples': ['исторические места', 'символические объекты', 'скрытые истории']
                },
                {
                    'technique': 'character_reactions',
                    'description': 'Реакции персонажей на действия',
                    'examples': ['NPC комментарии', 'изменение отношений', 'распространение слухов']
                },
                {
                    'technique': 'environmental_changes',
                    'description': 'Изменения окружения',
                    'examples': ['новые объекты', 'изменения ландшафта', 'атмосферные эффекты']
                },
                {
                    'technique': 'consequence_systems',
                    'description': 'Системы последствий',
                    'examples': ['долгосрочные эффекты', 'цепные реакции', 'альтернативные исходы']
                }
            ],
            'faction_interactions': [
                {
                    'interaction_type': 'alliance_building',
                    'mechanisms': ['совместные квесты', 'обмен ресурсами', 'дипломатические миссии'],
                    'benefits': ['новые возможности', 'преимущества', 'альянсы']
                },
                {
                    'interaction_type': 'rivalry_management',
                    'mechanisms': ['конкуренция', 'саботаж', 'нейтралитет'],
                    'benefits': ['конфликты', 'вызовы', 'альтернативы']
                },
                {
                    'interaction_type': 'power_dynamics',
                    'mechanisms': ['влияние на фракции', 'политические интриги', 'баланс сил'],
                    'benefits': ['стратегическая глубина', 'дипломатия', 'влияние на мир']
                }
            ]
        },
        'technical_implementation': {
            'quest_engine': {
                'state_management': 'Отслеживание прогрессии квестов',
                'condition_systems': 'Проверка условий выполнения',
                'reward_distribution': 'Распределение наград',
                'branching_logic': 'Логика ветвлений сюжета'
            },
            'content_delivery': {
                'dynamic_generation': 'Процедурная генерация контента',
                'personalization_engine': 'Персонализация под игрока',
                'adaptive_difficulty': 'Адаптивная сложность',
                'performance_optimization': 'Оптимизация производительности'
            },
            'data_structures': [
                {
                    'structure': 'quest_templates',
                    'purpose': 'шаблоны квестов',
                    'complexity': 'высокая'
                },
                {
                    'structure': 'narrative_nodes',
                    'purpose': 'узлы повествования',
                    'complexity': 'средняя'
                },
                {
                    'structure': 'choice_consequences',
                    'purpose': 'последствия выборов',
                    'complexity': 'высокая'
                },
                {
                    'structure': 'world_state_changes',
                    'purpose': 'изменения состояния мира',
                    'complexity': 'очень высокая'
                }
            ]
        },
        'quality_assurance': {
            'narrative_coherence': [
                'проверка связности сюжета',
                'валидация персонажей',
                'проверка временной последовательности',
                'тестирование ветвлений'
            ],
            'player_experience': [
                'тестирование вовлеченности',
                'проверка баланса сложности',
                'валидация эмоционального воздействия',
                'оценка реиграбельности'
            ],
            'technical_validation': [
                'проверка производительности',
                'тестирование состояний',
                'валидация условий',
                'проверка интеграции'
            ]
        },
        'future_expansions': {
            'emerging_features': [
                'AI_generated_narratives',
                'player_created_quests',
                'cross_platform_stories',
                'live_service_updates'
            ],
            'advanced_mechanics': [
                'procedural_storytelling',
                'emotional_modeling',
                'cultural_adaptation',
                'social_narrative_systems'
            ],
            'research_areas': [
                'narrative_ai_algorithms',
                'emotional_player_modeling',
                'cultural_storytelling_patterns',
                'accessibility_in_narrative_design'
            ]
        }
    }

    return template

def main():
    """Generate 5 content world quests documents"""
    world_quests_dir = Path('knowledge/content/world-quests')

    generated_count = 0

    for doc_data in WORLD_QUESTS_DOCUMENTS:
        doc_file = world_quests_dir / f'{doc_data["name"]}.yaml'

        # Check if document already exists
        if doc_file.exists():
            print(f"Document already exists: {doc_data['name']}")
            continue

        print(f"Generating document: {doc_data['title']}")

        template = create_world_quests_document_template(doc_data)

        with open(doc_file, 'w', encoding='utf-8') as f:
            yaml.dump(template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        generated_count += 1

    print(f"Generated {generated_count} content world quests documents")

if __name__ == '__main__':
    main()
