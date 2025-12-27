import yaml
import os
from pathlib import Path
from datetime import datetime

WORLD_REGIONS = {
    'oceania': {
        'name': 'Oceania',
        'cities': [
            'auckland', 'brisbane', 'canberra', 'christchurch', 'darwin',
            'fiji', 'guam', 'honiara', 'honolulu', 'melbourne', 'noumea',
            'palau', 'papua-new-guinea', 'perth', 'port-moresby', 'sydney',
            'tonga', 'vanuatu', 'wellington'
        ]
    },
    'antarctica': {
        'name': 'Antarctica',
        'cities': [
            'mcmurdo-station', 'palmer-station', 'roth-era-station'
        ]
    },
    'arctic': {
        'name': 'Arctic Region',
        'cities': [
            'barrow', 'churchill', 'iqaluit', 'longyearbyen', 'murmansk',
            'norilsk', 'nuuk', 'tiksi', 'yellowknife'
        ]
    }
}

def create_region_template(region_name, region_data):
    """Create a detailed region template"""
    template = {
        'metadata': {
            'id': f'canon-lore-{region_name}-2020-2093',
            'title': f'{region_data["name"]}: Региональный обзор 2020-2093',
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
                'world-region',
                region_name.lower(),
                'regional-overview'
            ],
            'topics': [
                'worldbuilding',
                'locations',
                'regional-economics'
            ],
            'related_systems': [
                'world-service',
                'infrastructure-service'
            ],
            'related_documents': [
                {
                    'id': 'canon-lore-locations-overview-2020-2093',
                    'relation': 'references'
                }
            ],
            'source': f'shared/docs/knowledge/canon/lore/timeline-author/regions/{region_name}-2020-2093.yaml',
            'visibility': 'internal',
            'audience': [
                'concept',
                'narrative',
                'systems'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо зафиксировать региональную структуру {region_data["name"]} для построения глобальных сюжетов.',
            'goal': f'Описать ключевые города, экономику, фракции и сюжетные возможности региона.',
            'essence': f'{region_data["name"]} представляет собой уникальную композицию культур, технологий и конфликтов.',
            'key_points': [
                'Географическое положение и природные особенности',
                'Экономическая специализация и глобальные связи',
                'Политическая структура и международные отношения',
                'Технологические достижения и инфраструктура',
                'Ключевые конфликты и сюжетные возможности'
            ]
        },
        'regional_overview': {
            'geography': {
                'area_km2': '[Total area]',
                'climate_zones': '[Climate zones]',
                'natural_resources': '[Key resources]',
                'strategic_importance': '[Geopolitical significance]'
            },
            'population': {
                'total_population': '[Total population]',
                'population_density': '[Population density]',
                'urban_vs_rural': '[Urban/rural distribution]',
                'migration_patterns': '[Migration trends]'
            },
            'economy': {
                'gdp_per_capita': '[GDP per capita]',
                'primary_industries': '[Key industries]',
                'trade_partners': '[Major trading partners]',
                'economic_challenges': '[Economic issues]'
            },
            'politics': {
                'government_structure': '[Political system]',
                'international_alignments': '[Global alliances]',
                'internal_conflicts': '[Domestic conflicts]',
                'regional_influence': '[Regional power]'
            }
        },
        'key_cities': [
            {
                'name': city.replace('-', ' ').title(),
                'population': '[Population]',
                'economic_role': '[Economic function]',
                'strategic_importance': '[Strategic value]',
                'key_features': '[Unique characteristics]'
            } for city in region_data['cities'][:5]  # Limit to 5 major cities
        ],
        'corporate_presence': [
            {
                'corporation': '[Corporation name]',
                'sector': '[Primary sector]',
                'influence_level': 'high/medium/low',
                'regional_headquarters': '[HQ location]'
            }
        ],
        'faction_dynamics': {
            'local_factions': [
                {
                    'name': '[Faction name]',
                    'ideology': '[Ideological orientation]',
                    'strength': 'high/medium/low',
                    'territories': '[Controlled areas]'
                }
            ],
            'international_influence': [
                '[Major foreign powers]',
                '[International organizations]'
            ]
        },
        'plot_opportunities': {
            'regional_conflicts': [
                '[Major conflict 1]',
                '[Major conflict 2]'
            ],
            'economic_opportunities': [
                '[Economic plot 1]',
                '[Economic plot 2]'
            ],
            'technological_developments': [
                '[Tech plot 1]',
                '[Tech plot 2]'
            ],
            'social_movements': [
                '[Social plot 1]',
                '[Social plot 2]'
            ]
        },
        'timeline_evolution': {
            '2020_2030': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]']
            },
            '2030_2040': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]']
            },
            '2040_2060': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]']
            },
            '2060_2077': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]']
            },
            '2077_2093': {
                'key_events': ['[Event 1]', '[Event 2]'],
                'developments': ['[Development 1]', '[Development 2]']
            }
        }
    }

    return template

def create_city_template(city_name, region_name):
    """Create a city template within a region"""
    template = {
        'metadata': {
            'id': f'canon-lore-{city_name}-{region_name}-2020-2093',
            'title': f'{city_name.title()}: Город в регионе {region_name.title()} (2020-2093)',
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
                region_name.lower()
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
                    'id': f'canon-lore-{region_name}-2020-2093',
                    'relation': 'belongs_to'
                }
            ],
            'source': f'shared/docs/knowledge/canon/lore/timeline-author/regions/{region_name}/cities/{city_name}-2020-2093.yaml',
            'visibility': 'internal',
            'audience': [
                'concept',
                'narrative',
                'systems'
            ],
            'risk_level': 'medium'
        },
        'summary': {
            'problem': f'Необходимо зафиксировать структуру города {city_name.title()} в регионе {region_name.title()}.',
            'goal': f'Описать вертикальные уровни, фракции, маршруты и сюжетные узлы города.',
            'essence': f'{city_name.title()} представляет собой типичный город региона {region_name.title()}.',
            'key_points': [
                'Географическое положение в регионе',
                'Экономическая роль в региональной экономике',
                'Социальная структура и местные особенности',
                'Технологические достижения',
                'Ключевые локации и сюжетные возможности'
            ]
        },
        'city_overview': {
            'basic_info': {
                'name': city_name.title(),
                'region': region_name.title(),
                'population': '[Population]',
                'area_km2': '[Area]',
                'founding_date': '[Founding date]',
                'coordinates': '[Latitude, Longitude]'
            },
            'economic_profile': {
                'primary_industries': ['[Industry 1]', '[Industry 2]'],
                'economic_status': 'thriving/declining/stable',
                'regional_importance': 'high/medium/low'
            },
            'technological_level': {
                'infrastructure_rating': 'A/B/C/D',
                'net_access_level': 'high/medium/low',
                'cybernetic_adoption': 'high/medium/low'
            }
        },
        'vertical_structure': {
            'upper_levels': {
                'description': 'Корпоративные и правительственные зоны',
                'key_locations': ['[Location 1]', '[Location 2]'],
                'social_class': 'Elite, corporate executives',
                'security_level': 'high'
            },
            'mid_levels': {
                'description': 'Средний класс, сервисы',
                'key_locations': ['[Location 1]', '[Location 2]'],
                'social_class': 'Professionals, merchants',
                'security_level': 'medium'
            },
            'street_level': {
                'description': 'Коммунальные зоны, торговля',
                'key_locations': ['[Location 1]', '[Location 2]'],
                'social_class': 'Working class, locals',
                'security_level': 'medium'
            },
            'underground': {
                'description': 'Подпольные сети',
                'key_locations': ['[Location 1]', '[Location 2]'],
                'social_class': 'Outcasts, criminals',
                'security_level': 'low'
            }
        },
        'factions_and_groups': {
            'corporate': [{'name': '[Corporation]', 'influence': 'medium'}],
            'local_government': {
                'structure': '[Government type]',
                'effectiveness': 'medium',
                'corruption_level': 'medium'
            },
            'criminal_elements': [{'name': '[Gang name]', 'territory': '[Area]'}]
        },
        'key_locations': [
            {
                'name': '[Location name]',
                'type': 'corporate/residential/commercial',
                'significance': '[Significance]',
                'coordinates': '[In-game coordinates]'
            }
        ],
        'plot_hooks': {
            'personal_quests': ['[Quest idea 1]', '[Quest idea 2]'],
            'faction_conflicts': ['[Conflict 1]', '[Conflict 2]'],
            'economic_opportunities': ['[Opportunity 1]', '[Opportunity 2]']
        }
    }

    return template

def main():
    """Generate missing world regions and cities"""
    regions_dir = Path('knowledge/canon/lore/timeline-author/regions')

    generated_regions = 0
    generated_cities = 0

    for region_name, region_data in WORLD_REGIONS.items():
        region_path = regions_dir / region_name

        # Create region directory if it doesn't exist
        region_path.mkdir(exist_ok=True)

        # Create region overview file if it doesn't exist
        region_file = regions_dir / f'{region_name}-2020-2093.yaml'
        if not region_file.exists():
            print(f"Generating region overview: {region_name}")
            region_template = create_region_template(region_name, region_data)

            with open(region_file, 'w', encoding='utf-8') as f:
                yaml.dump(region_template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

            generated_regions += 1

        # Create cities directory
        cities_dir = region_path / 'cities'
        cities_dir.mkdir(exist_ok=True)

        # Check existing cities
        existing_cities = []
        for file in cities_dir.glob('*.yaml'):
            city_name = file.name.split('-2020-2093')[0]
            existing_cities.append(city_name)

        # Generate missing cities
        for city in region_data['cities']:
            if city not in existing_cities:
                print(f"Generating city: {city} in {region_name}")
                city_template = create_city_template(city, region_name)

                city_file = cities_dir / f'{city}-2020-2093.yaml'
                with open(city_file, 'w', encoding='utf-8') as f:
                    yaml.dump(city_template, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

                generated_cities += 1

    print(f"Generated {generated_regions} region overviews and {generated_cities} city templates")

if __name__ == '__main__':
    main()
