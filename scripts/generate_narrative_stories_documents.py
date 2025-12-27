import yaml
from pathlib import Path
from datetime import datetime

NARRATIVE_DOCUMENTS = [
    {
        'name': 'narrative-emergent-player-stories',
        'title': 'Narrative - Emergent Player Stories',
        'description': 'Возникающие истории на основе действий игроков'
    },
    {
        'name': 'narrative-branching-consequences',
        'title': 'Narrative - Branching Consequences',
        'description': 'Ветвящиеся последствия выборов игроков'
    },
    {
        'name': 'narrative-faction-influence-narratives',
        'title': 'Narrative - Faction Influence Narratives',
        'description': 'Нарративы влияния фракций на мир'
    },
    {
        'name': 'narrative-time-travel-mechanics',
        'title': 'Narrative - Time Travel Mechanics',
        'description': 'Механики путешествий во времени в нарративе'
    },
    {
        'name': 'narrative-alternate-timeline-events',
        'title': 'Narrative - Alternate Timeline Events',
        'description': 'События альтернативных временных линий'
    },
    {
        'name': 'narrative-memory-manipulation-stories',
        'title': 'Narrative - Memory Manipulation Stories',
        'description': 'Истории манипуляции памятью'
    },
    {
        'name': 'narrative-corporate-espionage-sagas',
        'title': 'Narrative - Corporate Espionage Sagas',
        'description': 'Саги корпоративного шпионажа'
    },
    {
        'name': 'narrative-street-level-legends',
        'title': 'Narrative - Street Level Legends',
        'description': 'Легенды уличного уровня'
    },
    {
        'name': 'narrative-hacker-underground-myths',
        'title': 'Narrative - Hacker Underground Myths',
        'description': 'Мифы хакерского подполья'
    },
    {
        'name': 'narrative-post-apocalyptic-survival-tales',
        'title': 'Narrative - Post-Apocalyptic Survival Tales',
        'description': 'Истории выживания в постапокалипсисе'
    }
]

def create_narrative_document_template(doc_data):
    """Create a detailed narrative document template"""
    template = {
        'metadata': {
            'id': f'canon-narrative-{doc_data["name"]}',
            'title': doc_data['title'],
            'document_type': 'canon',
            'category': 'narrative',
            'subcategory': 'stories-scenarios-events',
            'status': 'draft',
            'version': '1.0.0',
            'last_updated': datetime.now().strftime('%Y-%m-%dT%H:%M:%SZ'),
            'concept_approved': False,
            'concept_reviewed_at': '',
            'owners': [
                {
                    'role': 'narrative_director',
                    'contact': 'narrative@necp.game'
                }
            ],
            'tags': [
                'narrative',
                'stories',
                'scenarios',
                'events'
            ],
            'topics': [
                'storytelling',
                'narrative-design',
                'player-choice'
            ],
            'related_systems': [
                'narrative-service',
                'story-service',
                'event-service'
            ],
            'related_documents': [
                {
                    'id': 'canon-narrative-main-story-overview',
                    'relation': 'references'
                },
                {
                    'id': 'canon-narrative-key-narrative-events',
                    'relation': 'part_of'
                }
            ],
            'source': f'shared/docs/knowledge/canon/narrative/{doc_data["name"]}.yaml',
            'visibility': 'internal',
            'audience': [
                'narrative',
                'design',
                'content'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо детализировать {doc_data["description"].lower()} для создания увлекательного и связного нарратива.',
            'goal': f'Описать ключевые элементы, механики и влияние на игровой опыт {doc_data["description"].lower()}.',
            'essence': doc_data["description"],
            'key_points': [
                'Нарративная структура и элементы',
                'Механики взаимодействия с игроком',
                'Влияние на мир и персонажей',
                'Интеграция с игровыми системами'
            ]
        },
        'narrative_structure': {
            'core_concept': {
                'premise': f'Основная идея {doc_data["description"].lower()}',
                'themes': ['cyberpunk', 'transhumanism', 'corporate_control', 'personal_freedom'],
                'tone': 'dark, gritty, hopeful',
                'scope': 'individual stories impacting world narrative'
            },
            'character_arcs': [
                {
                    'character_type': 'protagonist',
                    'arc_type': 'redemption',
                    'key_moments': ['awakening', 'crisis', 'transformation'],
                    'player_influence': 'high'
                },
                {
                    'character_type': 'antagonist',
                    'arc_type': 'downfall',
                    'key_moments': ['rise', 'conflict', 'defeat'],
                    'player_influence': 'medium'
                },
                {
                    'character_type': 'supporting_cast',
                    'arc_type': 'growth',
                    'key_moments': ['introduction', 'development', 'resolution'],
                    'player_influence': 'variable'
                }
            ],
            'plot_progression': {
                'acts': [
                    {
                        'act': 'setup',
                        'duration': '20%',
                        'focus': 'world_introduction',
                        'player_agency': 'exploration'
                    },
                    {
                        'act': 'confrontation',
                        'duration': '60%',
                        'focus': 'conflict_development',
                        'player_agency': 'decision_making'
                    },
                    {
                        'act': 'resolution',
                        'duration': '20%',
                        'focus': 'climax_and_ending',
                        'player_agency': 'final_choice'
                    }
                ],
                'pacing_elements': [
                    'slow_burn_world_building',
                    'intense_action_sequences',
                    'quiet_reflection_moments',
                    'sudden_twists_and_revelations'
                ]
            }
        },
        'player_interaction_mechanics': {
            'choice_systems': [
                {
                    'choice_type': 'dialogue_options',
                    'impact_level': 'conversation_outcome',
                    'consequence_scope': 'relationship_status'
                },
                {
                    'choice_type': 'action_decisions',
                    'impact_level': 'story_branching',
                    'consequence_scope': 'faction_reputation'
                },
                {
                    'choice_type': 'moral_dilemmas',
                    'impact_level': 'character_alignment',
                    'consequence_scope': 'world_state_changes'
                },
                {
                    'choice_type': 'resource_allocation',
                    'impact_level': 'opportunity_costs',
                    'consequence_scope': 'personal_development'
                }
            ],
            'consequence_tracking': {
                'immediate_effects': 'dialogue_outcomes',
                'short_term_effects': 'quest_availability',
                'long_term_effects': 'world_state_modifications',
                'butterfly_effects': 'unintended_consequences'
            },
            'narrative_feedback': {
                'visual_indicators': 'story_importance_markers',
                'audio_cues': 'emotional_music_changes',
                'text_notifications': 'consequence_summaries',
                'environmental_changes': 'world_reaction_to_choices'
            }
        },
        'world_integration': {
            'environmental_narrative': [
                'neon-lit_cityscapes_telling_stories',
                'abandoned_structures_with_hidden_histories',
                'crowded_streets_with_overheard_conversations',
                'corporate_towers_symbolizing_power_dynamics'
            ],
            'faction_influence': [
                'corporate_takeovers_changing_city_layouts',
                'gang_wars_affecting_safe_zones',
                'political_decisions_altering_laws_and_regulations',
                'technological_breakthroughs_enabling_new_stories'
            ],
            'character_integration': [
                'NPCs_responding_to_player_fame_and_reputation',
                'returning_characters_with_updated_backstories',
                'new_characters_introduced_based_on_player_actions',
                'character_relationships_evolving_over_time'
            ]
        },
        'technical_implementation': {
            'narrative_engine': {
                'state_management': 'complex_player_choice_tracking',
                'content_delivery': 'dynamic_story_fragment_assembly',
                'performance_optimization': 'efficient_narrative_data_structures',
                'scalability': 'support_for_thousands_of_story_variants'
            },
            'content_creation_tools': [
                {
                    'tool': 'narrative_scripting_language',
                    'purpose': 'writing_conditional_story_logic',
                    'complexity': 'high',
                    'accessibility': 'experienced_writers'
                },
                {
                    'tool': 'story_branching_visualizer',
                    'purpose': 'mapping_choice_consequences',
                    'complexity': 'medium',
                    'accessibility': 'designers_and_writers'
                },
                {
                    'tool': 'player_impact_simulator',
                    'purpose': 'testing_narrative_consequences',
                    'complexity': 'low',
                    'accessibility': 'all_team_members'
                }
            ],
            'quality_assurance': [
                'narrative_coherence_testing',
                'choice_consequence_validation',
                'player_experience_flow_analysis',
                'cultural_sensitivity_reviews'
            ]
        },
        'content_examples': {
            'sample_stories': [
                {
                    'title': 'Corporate Espionage Gone Wrong',
                    'plot_hook': 'A routine data theft uncovers a conspiracy',
                    'player_choices': ['Go to authorities', 'Sell information', 'Investigate personally'],
                    'consequences': ['Faction war', 'Personal betrayal', 'Heroic sacrifice']
                },
                {
                    'title': 'Street Legend Rising',
                    'plot_hook': 'A mysterious stranger offers impossible odds',
                    'player_choices': ['Accept challenge', 'Decline politely', 'Confront stranger'],
                    'consequences': ['Legendary status', 'Missed opportunity', 'Unexpected alliance']
                },
                {
                    'title': 'Memory Market Mystery',
                    'plot_hook': 'Buying memories reveals hidden truths',
                    'player_choices': ['Erase painful memories', 'Sell valuable knowledge', 'Investigate source'],
                    'consequences': ['Identity crisis', 'Market manipulation', 'Truth uncovered']
                }
            ],
            'branching_scenarios': [
                {
                    'scenario': 'Faction Loyalty Test',
                    'branches': 3,
                    'impact_scope': 'regional',
                    'replayability': 'high'
                },
                {
                    'scenario': 'Moral Choice Dilemma',
                    'branches': 4,
                    'impact_scope': 'personal',
                    'replayability': 'medium'
                },
                {
                    'scenario': 'Resource Allocation Crisis',
                    'branches': 2,
                    'impact_scope': 'faction',
                    'replayability': 'low'
                }
            ]
        },
        'future_expansions': {
            'planned_features': [
                'AI_generated_personal_stories',
                'Player_created_content_integration',
                'Cross_platform_narrative_continuity',
                'Live_service_story_updates'
            ],
            'research_areas': [
                'Procedural_narrative_generation',
                'Emotional_player_modeling',
                'Cultural_narrative_adaptation',
                'Accessibility_in_storytelling'
            ]
        }
    }

    return template

def main():
    """Generate 10 narrative stories documents"""
    narrative_dir = Path('knowledge/canon/narrative')

    generated_count = 0

    for doc_data in NARRATIVE_DOCUMENTS:
        doc_file = narrative_dir / f'{doc_data["name"]}.yaml'

        # Check if document already exists
        if doc_file.exists():
            print(f"Document already exists: {doc_data['name']}")
            continue

        print(f"Generating document: {doc_data['title']}")

        template = create_narrative_document_template(doc_data)

        with open(doc_file, 'w', encoding='utf-8') as f:
            yaml.dump(template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        generated_count += 1

    print(f"Generated {generated_count} narrative stories documents")

if __name__ == '__main__':
    main()
