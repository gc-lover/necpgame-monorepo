import yaml
from pathlib import Path
from datetime import datetime

HACKING_DOCUMENTS = [
    {
        'name': 'combat-hacking-quickhacks-database',
        'title': 'Combat Hacking - Quickhacks Database',
        'description': 'База данных быстрых хаков для боевых ситуаций'
    },
    {
        'name': 'combat-hacking-daemon-integration',
        'title': 'Combat Hacking - Daemon Integration',
        'description': 'Интеграция демонов в систему боевого хакерства'
    },
    {
        'name': 'combat-hacking-cyberpsychosis-effects',
        'title': 'Combat Hacking - Cyberpsychosis Effects',
        'description': 'Влияние киберпсихоза на боевые хаки'
    }
]

def create_hacking_document_template(doc_data):
    """Create a detailed combat hacking document template"""
    template = {
        'metadata': {
            'id': f'canon-mechanics-{doc_data["name"]}',
            'title': doc_data['title'],
            'document_type': 'canon',
            'category': 'mechanics',
            'subcategory': 'combat-hacking',
            'status': 'draft',
            'version': '1.0.0',
            'last_updated': datetime.now().strftime('%Y-%m-%dT%H:%M:%SZ'),
            'concept_approved': False,
            'concept_reviewed_at': '',
            'owners': [
                {
                    'role': 'backend_director',
                    'contact': 'backend@necp.game'
                }
            ],
            'tags': [
                'combat',
                'hacking',
                'cyberpunk',
                'mechanics'
            ],
            'topics': [
                'game-mechanics',
                'combat-systems',
                'hacking'
            ],
            'related_systems': [
                'combat-service',
                'backend-service'
            ],
            'related_documents': [
                {
                    'id': 'canon-mechanics-combat-hacking',
                    'relation': 'part_of'
                },
                {
                    'id': 'canon-mechanics-combat-hacking-types',
                    'relation': 'references'
                }
            ],
            'source': f'shared/docs/knowledge/mechanics/combat/{doc_data["name"]}.yaml',
            'visibility': 'internal',
            'audience': [
                'backend',
                'systems',
                'design'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо детализировать {doc_data["description"].lower()} для интеграции в боевую систему.',
            'goal': f'Описать технические аспекты, баланс и игровые механики {doc_data["description"].lower()}.',
            'essence': doc_data["description"],
            'key_points': [
                'Технические требования к реализации',
                'Баланс и игровой дизайн',
                'Интеграция с существующими системами',
                'Производительность и оптимизация'
            ]
        },
        'technical_specification': {
            'system_requirements': {
                'processing_power': 'high/medium/low',
                'network_latency': 'max_ms',
                'memory_usage': 'estimated_mb',
                'database_queries': 'per_second'
            },
            'data_structures': {
                'quickhack_table': [
                    {
                        'id': 'example_quickhack',
                        'name': 'Example Quickhack',
                        'type': 'combat/utility',
                        'cost': 10,
                        'cooldown': 30,
                        'duration': 15,
                        'effects': ['damage_boost', 'speed_increase'],
                        'requirements': {
                            'level': 5,
                            'skill': 'hacking'
                        }
                    }
                ],
                'daemon_integration': {
                    'available_daemons': ['combat', 'stealth', 'utility'],
                    'integration_points': ['pre_combat', 'mid_combat', 'post_combat'],
                    'resource_costs': {
                        'memory': 20,
                        'processing': 15
                    }
                }
            },
            'algorithms': {
                'hacking_success_rate': 'formula_based_on_skill_and_difficulty',
                'counter_hacking_defense': 'passive_active_mechanic',
                'cyberpsychosis_risk': 'exponential_based_on_usage'
            }
        },
        'gameplay_mechanics': {
            'player_interaction': {
                'input_methods': ['radial_menu', 'hotkeys', 'voice_commands'],
                'targeting_system': 'line_of_sight_and_network_range',
                'feedback_system': 'visual_audio_haptic'
            },
            'balancing_factors': {
                'difficulty_scaling': 'based_on_enemy_level_and_type',
                'resource_management': 'ram_capacity_and_recharge',
                'risk_reward_ratio': 'higher_risk_for_better_rewards'
            },
            'progression_system': {
                'skill_tree': ['basic_hacks', 'advanced_hacks', 'master_hacks'],
                'upgrade_paths': ['damage', 'utility', 'stealth'],
                'specialization_bonuses': ['corporate', 'street', 'nomad']
            }
        },
        'combat_integration': {
            'synergy_mechanics': {
                'with_weapons': 'hack_amplification',
                'with_implants': 'neural_boosting',
                'with_teamwork': 'coordinated_attacks'
            },
            'counterplay_options': {
                'defensive_hacks': ['firewall', 'encryption', 'anti_virus'],
                'environmental_factors': ['network_jamming', 'emp_fields'],
                'player_choices': ['avoid_hackers', 'counter_hack', 'overwhelm']
            },
            'dynamic_balance': {
                'scaling_difficulty': 'adapts_to_player_skill',
                'emergent_gameplay': 'unpredictable_combinations',
                'meta_evolution': 'community_driven_balance'
            }
        },
        'implementation_notes': {
            'backend_requirements': [
                'Real-time network simulation',
                'Concurrent hack processing',
                'State synchronization across clients',
                'Anti-cheat integration'
            ],
            'frontend_requirements': [
                'Smooth UI transitions',
                'Clear visual feedback',
                'Audio cues for hack status',
                'Accessibility considerations'
            ],
            'testing_requirements': [
                'Performance benchmarks',
                'Balance playtesting',
                'Edge case validation',
                'Multiplayer synchronization'
            ]
        },
        'future_expansions': {
            'planned_features': [
                'Advanced daemon AI',
                'Custom hack creation',
                'Hacking mini-games',
                'Cross-platform integration'
            ],
            'research_areas': [
                'Neural interface optimization',
                'Quantum computing integration',
                'AI-assisted hacking',
                'Blockchain security'
            ]
        }
    }

    return template

def main():
    """Generate 3 combat hacking documents"""
    combat_dir = Path('knowledge/mechanics/combat')

    generated_count = 0

    for doc_data in HACKING_DOCUMENTS:
        doc_file = combat_dir / f'{doc_data["name"]}.yaml'

        # Check if document already exists
        if doc_file.exists():
            print(f"Document already exists: {doc_data['name']}")
            continue

        print(f"Generating document: {doc_data['title']}")

        template = create_hacking_document_template(doc_data)

        with open(doc_file, 'w', encoding='utf-8') as f:
            yaml.dump(template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        generated_count += 1

    print(f"Generated {generated_count} combat hacking documents")

if __name__ == '__main__':
    main()
