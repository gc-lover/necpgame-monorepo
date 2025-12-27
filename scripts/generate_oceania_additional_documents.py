import yaml
from pathlib import Path
from datetime import datetime

ADDITIONAL_OCEANIA_DOCUMENTS = [
    # Additional Oceania regions and special areas
    {
        'name': 'Pacific Islands Federation',
        'type': 'regional_overview',
        'description': 'Обзор тихоокеанских островных государств и их уникальной экосистемы'
    },
    {
        'name': 'Great Barrier Reef Megacity',
        'type': 'special_location',
        'description': 'Искусственные платформы и подводные города Большого Барьерного Рифа'
    },
    {
        'name': 'New Zealand South Island Tech Hub',
        'type': 'economic_analysis',
        'description': 'Технологический кластер Южного острова Новой Зеландии'
    },
    {
        'name': 'Australian Outback Research Stations',
        'type': 'scientific_facilities',
        'description': 'Исследовательские станции в австралийской глубинке'
    },
    {
        'name': 'Polynesian Cultural Revival',
        'type': 'cultural_analysis',
        'description': 'Возрождение полинезийской культуры в цифровую эпоху'
    },
    {
        'name': 'Micronesian Network Hubs',
        'type': 'infrastructure_overview',
        'description': 'Сетевые узлы Микронезии в глобальной инфраструктуре'
    },
    {
        'name': 'Tasman Sea Underwater Cities',
        'type': 'submarine_colonies',
        'description': 'Подводные города в Тасмановом море'
    },
    {
        'name': 'Solomon Islands Mining Operations',
        'type': 'industrial_analysis',
        'description': 'Горнодобывающие операции Соломоновых островов'
    },
    {
        'name': 'Papua New Guinea Tribal Enclaves',
        'type': 'social_analysis',
        'description': 'Племенные анклавы в Папуа-Новой Гвинее'
    },
    {
        'name': 'Fiji Resort Mega-Complexes',
        'type': 'tourism_economy',
        'description': 'Мега-комплексы отдыха на Фиджи'
    },
    {
        'name': 'Northern Territory AI Research',
        'type': 'technological_research',
        'description': 'Исследования ИИ в Северной Территории Австралии'
    },
    {
        'name': 'Cook Islands Climate Research',
        'type': 'environmental_science',
        'description': 'Климатические исследования на островах Кука'
    }
]

def create_oceania_document_template(doc_data):
    """Create a detailed document template for Oceania regions"""
    doc_name = doc_data['name'].lower().replace(' ', '-').replace('\'', '')

    template = {
        'metadata': {
            'id': f'canon-lore-oceania-{doc_name}-2020-2093',
            'title': f'{doc_data["name"]}: {doc_data["description"]} (2020-2093)',
            'document_type': 'canon',
            'category': 'locations',
            'subcategory': doc_data['type'],
            'status': 'draft',
            'version': '1.0.0',
            'last_updated': datetime.now().strftime('%Y-%m-%dT%H:%M:%SZ'),
            'concept_approved': False,
            'concept_reviewed_at': '',
            'owners': [
                {
                    'role': 'world_director',
                    'contact': 'world@necp.game'
                }
            ],
            'tags': [
                'oceania',
                'regional-document',
                doc_data['type'],
                'world-building'
            ],
            'topics': [
                'worldbuilding',
                'locations',
                'regional-analysis'
            ],
            'related_systems': [
                'world-service',
                'infrastructure-service'
            ],
            'related_documents': [
                {
                    'id': 'canon-lore-oceania-2020-2093',
                    'relation': 'belongs_to'
                }
            ],
            'source': f'shared/docs/knowledge/canon/lore/timeline-author/regions/oceania/{doc_name}-2020-2093.yaml',
            'visibility': 'internal',
            'audience': [
                'concept',
                'narrative',
                'systems'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо зафиксировать детали {doc_data["name"]} для построения сюжетов Океании.',
            'goal': f'Описать ключевые аспекты, фракции, технологии и сюжетные возможности.',
            'essence': doc_data["description"],
            'key_points': [
                'Географическое и историческое значение',
                'Экономическая и технологическая роль',
                'Социальные и культурные особенности',
                'Ключевые конфликты и возможности'
            ]
        },
        'detailed_analysis': {
            'overview': {
                'location': '[Geographic location]',
                'population': '[Population data]',
                'area': '[Area coverage]',
                'strategic_importance': '[Geopolitical significance]'
            },
            'economic_profile': {
                'primary_industries': ['[Industry 1]', '[Industry 2]'],
                'corporate_presence': ['[Corporation 1]', '[Corporation 2]'],
                'economic_status': 'thriving/declining/stable',
                'trade_relations': ['[Trading partner 1]', '[Trading partner 2]']
            },
            'technological_infrastructure': {
                'net_integration': 'high/medium/low',
                'cybernetic_adoption': 'high/medium/low',
                'key_technologies': ['[Technology 1]', '[Technology 2]'],
                'research_facilities': ['[Facility 1]', '[Facility 2]']
            },
            'social_structure': {
                'population_composition': '[Demographic breakdown]',
                'cultural_characteristics': ['[Cultural trait 1]', '[Cultural trait 2]'],
                'social_challenges': ['[Challenge 1]', '[Challenge 2]'],
                'community_organizations': ['[Organization 1]', '[Organization 2]']
            }
        },
        'key_stakeholders': {
            'corporate_entities': [
                {
                    'name': '[Corporation name]',
                    'sector': '[Primary sector]',
                    'influence_level': 'high/medium/low',
                    'operations': '[Type of operations]'
                }
            ],
            'government_bodies': [
                {
                    'name': '[Government entity]',
                    'jurisdiction': '[Area of control]',
                    'effectiveness': 'high/medium/low',
                    'policies': '[Key policies]'
                }
            ],
            'faction_groups': [
                {
                    'name': '[Faction name]',
                    'ideology': '[Ideological orientation]',
                    'strength': 'high/medium/low',
                    'territories': '[Controlled areas]'
                }
            ],
            'local_communities': [
                {
                    'name': '[Community name]',
                    'population': '[Population size]',
                    'cultural_background': '[Cultural heritage]',
                    'economic_role': '[Economic function]'
                }
            ]
        },
        'plot_opportunities': {
            'main_story_arcs': [
                '[Major storyline 1]',
                '[Major storyline 2]',
                '[Major storyline 3]'
            ],
            'side_quests': [
                '[Side quest 1]',
                '[Side quest 2]'
            ],
            'faction_conflicts': [
                '[Conflict 1]',
                '[Conflict 2]'
            ],
            'economic_opportunities': [
                '[Economic opportunity 1]',
                '[Economic opportunity 2]'
            ],
            'technological_breakthroughs': [
                '[Tech breakthrough 1]',
                '[Tech breakthrough 2]'
            ]
        },
        'timeline_evolution': {
            '2020_2030': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]'],
                'challenges': ['[Challenge 1]', '[Challenge 2]']
            },
            '2030_2040': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]'],
                'challenges': ['[Challenge 1]', '[Challenge 2]']
            },
            '2040_2060': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]'],
                'challenges': ['[Challenge 1]', '[Challenge 2]']
            },
            '2060_2077': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]'],
                'challenges': ['[Challenge 1]', '[Challenge 2]']
            },
            '2077_2093': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]'],
                'challenges': ['[Challenge 1]', '[Challenge 2]']
            }
        },
        'environmental_considerations': {
            'climate_patterns': ['[Climate pattern 1]', '[Climate pattern 2]'],
            'environmental_challenges': ['[Challenge 1]', '[Challenge 2]'],
            'sustainability_initiatives': ['[Initiative 1]', '[Initiative 2]'],
            'biodiversity_conservation': ['[Conservation effort 1]', '[Conservation effort 2]']
        }
    }

    return template

def main():
    """Generate additional Oceania documents"""
    oceania_dir = Path('knowledge/canon/lore/timeline-author/regions/oceania')

    generated_count = 0

    for doc_data in ADDITIONAL_OCEANIA_DOCUMENTS:
        doc_name = doc_data['name'].lower().replace(' ', '-').replace('\'', '').replace(',', '')

        # Check if document already exists
        doc_file = oceania_dir / f'{doc_name}-2020-2093.yaml'
        if doc_file.exists():
            print(f"Document already exists: {doc_name}")
            continue

        print(f"Generating document: {doc_data['name']}")

        template = create_oceania_document_template(doc_data)

        with open(doc_file, 'w', encoding='utf-8') as f:
            yaml.dump(template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        generated_count += 1

    print(f"Generated {generated_count} additional Oceania documents")

if __name__ == '__main__':
    main()
