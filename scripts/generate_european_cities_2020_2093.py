import yaml
import os
from pathlib import Path
from datetime import datetime

EUROPEAN_CITIES = [
    "amsterdam", "antwerp", "athens", "barcelona", "belgrade", "bern", "birmingham",
    "brussels", "bucharest", "budapest", "copenhagen", "dublin", "edinburgh",
    "florence", "frankfurt", "glasgow", "gothenburg", "hamburg", "helsinki",
    "kiev", "liverpool", "lyon", "manchester", "munich", "nice", "oslo",
    "prague", "rome", "rotterdam", "stockholm", "stuttgart", "thessaloniki",
    "turin", "valencia", "venice", "vienna", "warsaw", "zurich"
]

def create_city_template(city_name):
    """Create a detailed city template for European cities"""
    template = {
        'metadata': {
            'id': f'canon-lore-{city_name}-detailed-2020-2093',
            'title': f'{city_name.title()}: [City Concept] (2020-2093)',
            'document_type': 'canon',
            'category': 'locations',
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
                'world-city',
                city_name.lower(),
                'europe'
            ],
            'topics': [
                'worldbuilding',
                'locations'
            ],
            'related_systems': [
                'world-service',
                'infrastructure-service'
            ],
            'related_documents': [
                {
                    'id': 'canon-lore-locations-overview-2020-2093',
                    'relation': 'references'
                },
                {
                    'id': 'canon-lore-world-cities-master-index',
                    'relation': 'references'
                }
            ],
            'source': f'shared/docs/knowledge/canon/lore/locations/world-cities/{city_name}-detailed-2020-2093.yaml',
            'visibility': 'internal',
            'audience': [
                'concept',
                'narrative',
                'systems'
            ],
            'risk_level': 'medium'
        },
        'review': {
            'chain': [
                {
                    'role': 'world_director',
                    'reviewer': '',
                    'reviewed_at': '',
                    'status': 'pending'
                }
            ],
            'next_actions': []
        },
        'summary': {
            'problem': f'Необходимо зафиксировать структуру {city_name.title()} для построения сюжетов и экономических систем Европы.',
            'goal': f'Описать вертикальные уровни, фракции, маршруты и сюжетные узлы города {city_name.title()}.',
            'essence': f'{city_name.title()} представляет собой уникальную композицию европейской культуры, технологий и социальных движений.',
            'key_points': [
                'Географическое положение и исторический контекст',
                'Экономическая специализация и корпоративное присутствие',
                'Социальная структура и культурные особенности',
                'Технологические инновации и инфраструктура',
                'Ключевые локации и сюжетные узлы'
            ]
        },
        'city_overview': {
            'basic_info': {
                'name': city_name.title(),
                'country': '[Country]',
                'population_2020': '[Population in 2020]',
                'population_2093': '[Population in 2093]',
                'area_km2': '[Area in km²]',
                'timezone': 'CET/CEST',
                'coordinates': '[Latitude, Longitude]'
            },
            'economic_profile': {
                'primary_industries': [
                    '[Primary industry 1]',
                    '[Primary industry 2]'
                ],
                'corporate_presence': [
                    {
                        'corporation': '[Corporation name]',
                        'sector': '[Sector]',
                        'influence_level': 'high/medium/low'
                    }
                ],
                'economic_status': 'thriving/declining/stable'
            },
            'technological_level': {
                'infrastructure_rating': 'A/B/C/D',
                'net_access_level': 'high/medium/low',
                'cybernetic_adoption_rate': 'high/medium/low',
                'key_technologies': [
                    '[Technology 1]',
                    '[Technology 2]'
                ]
            }
        },
        'vertical_structure': {
            'upper_levels': {
                'description': 'Корпоративные башни и элитные районы',
                'key_locations': [
                    '[Location 1]',
                    '[Location 2]'
                ],
                'social_class': 'Corporate elite, high-ranking officials',
                'security_level': 'high'
            },
            'mid_levels': {
                'description': 'Средний класс, профессионалы, сервисы',
                'key_locations': [
                    '[Location 1]',
                    '[Location 2]'
                ],
                'social_class': 'Middle class, professionals, service workers',
                'security_level': 'medium'
            },
            'street_level': {
                'description': 'Коммунальные зоны, торговля, развлечения',
                'key_locations': [
                    '[Location 1]',
                    '[Location 2]'
                ],
                'social_class': 'Working class, merchants, street culture',
                'security_level': 'medium'
            },
            'underground': {
                'description': 'Подпольные сети, нелегальные операции',
                'key_locations': [
                    '[Location 1]',
                    '[Location 2]'
                ],
                'social_class': 'Outcasts, criminals, resistance movements',
                'security_level': 'low'
            }
        },
        'factions_and_groups': {
            'corporate': [
                {
                    'name': '[Corporation name]',
                    'influence': 'high/medium/low',
                    'operations': '[Type of operations]'
                }
            ],
            'local_government': {
                'structure': '[Government structure]',
                'effectiveness': 'high/medium/low',
                'corruption_level': 'high/medium/low'
            },
            'criminal_elements': [
                {
                    'name': '[Gang/Organization name]',
                    'territory': '[Territory]',
                    'activities': '[Criminal activities]'
                }
            ],
            'resistance_groups': [
                {
                    'name': '[Resistance group name]',
                    'ideology': '[Ideology]',
                    'strength': 'high/medium/low'
                }
            ]
        },
        'key_locations': [
            {
                'name': '[Location name]',
                'type': 'corporate/office/residential/commercial',
                'significance': '[Historical/cultural/economic significance]',
                'coordinates': '[In-game coordinates]'
            }
        ],
        'plot_hooks': {
            'personal_quests': [
                '[Personal quest idea 1]',
                '[Personal quest idea 2]'
            ],
            'faction_conflicts': [
                '[Faction conflict idea 1]',
                '[Faction conflict idea 2]'
            ],
            'economic_opportunities': [
                '[Economic opportunity 1]',
                '[Economic opportunity 2]'
            ]
        },
        'timeline_evolution': {
            '2020_2030': {
                'key_events': [
                    '[Event 1]',
                    '[Event 2]'
                ],
                'developments': [
                    '[Development 1]',
                    '[Development 2]'
                ]
            },
            '2030_2040': {
                'key_events': [
                    '[Event 1]',
                    '[Event 2]'
                ],
                'developments': [
                    '[Development 1]',
                    '[Development 2]'
                ]
            },
            '2040_2060': {
                'key_events': [
                    '[Event 1]',
                    '[Event 2]'
                ],
                'developments': [
                    '[Development 1]',
                    '[Development 2]'
                ]
            },
            '2060_2077': {
                'key_events': [
                    '[Event 1]',
                    '[Event 2]'
                ],
                'developments': [
                    '[Development 1]',
                    '[Development 2]'
                ]
            },
            '2077_2093': {
                'key_events': [
                    '[Event 1]',
                    '[Event 2]'
                ],
                'developments': [
                    '[Development 1]',
                    '[Development 2]'
                ]
            }
        }
    }

    return template

def main():
    """Generate European cities templates"""
    output_dir = Path('knowledge/canon/lore/locations/world-cities')

    # Check existing cities
    existing_cities = []
    for file in output_dir.glob('*.yaml'):
        if 'detailed-2020-2093' in file.name:
            city_name = file.name.split('-detailed-2020-2093')[0]
            existing_cities.append(city_name.lower())

    print(f"Found {len(existing_cities)} existing European cities")
    print(f"Existing cities: {', '.join(existing_cities)}")

    # Generate missing cities
    generated_count = 0
    for city in EUROPEAN_CITIES:
        if city.lower() not in existing_cities:
            print(f"Generating template for: {city}")

            template = create_city_template(city)
            filename = f"{city}-detailed-2020-2093.yaml"
            filepath = output_dir / filename

            with open(filepath, 'w', encoding='utf-8') as f:
                yaml.dump(template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

            generated_count += 1

    print(f"Generated {generated_count} new European city templates")

if __name__ == '__main__':
    main()
