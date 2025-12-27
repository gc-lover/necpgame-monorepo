#!/usr/bin/env python3
"""
Import Asian and Middle Eastern Cities Quests to Database
Imports quest data from YAML files to database using Liquibase format.
Imports quests from Asian and Middle Eastern cities from 2020-2093 period.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib
import os

def parse_asian_middle_eastern_quest(yaml_file):
    """Parse individual Asian/Middle Eastern quest from YAML file"""
    try:
        with open(yaml_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        if not isinstance(data, dict):
            print(f"Warning: Skipping {yaml_file} - not a valid quest dict")
            return None

        metadata = data.get('metadata', {})
        quest_def = data.get('quest_definition', {})

        # Extract quest information
        quest_id = metadata.get('id', '')
        if not quest_id:
            print(f"Warning: Skipping {yaml_file} - no quest ID")
            return None

        title = metadata.get('title', '')
        description = data.get('summary', {}).get('essence', '')

        # Get level requirements
        level_min = quest_def.get('level_min', 1)
        level_max = quest_def.get('level_max', 50)

        # Get rewards
        rewards = quest_def.get('rewards', [])
        objectives = quest_def.get('objectives', [])

        # Create quest data structure
        quest_data = {
            'quest_id': quest_id,
            'title': title,
            'description': description,
            'level_min': level_min,
            'level_max': level_max,
            'rewards': json.dumps(rewards, ensure_ascii=False),
            'objectives': json.dumps(objectives, ensure_ascii=False),
            'source_file': str(yaml_file)
        }

        return quest_data

    except Exception as e:
        print(f"Error parsing {yaml_file}: {e}")
        return None

def create_liquibase_yaml(quests, output_file):
    """Create Liquibase YAML file for Asian/Middle Eastern cities quests"""

    now = datetime.now().isoformat()

    changes = []
    for quest in quests:
        changeset_id = f"quests-asian-middle-eastern-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        change = {
            'changeSet': {
                'id': changeset_id,
                'author': 'asian-middle-eastern-cities-quests-import',
                'changes': [
                    {
                        'insert': {
                            'tableName': 'quests',
                            'columns': [
                                {
                                    'column': {
                                        'name': 'id',
                                        'value': quest['quest_id']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'title',
                                        'value': quest['title']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'description',
                                        'value': quest['description']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'level_min',
                                        'value': quest['level_min']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'level_max',
                                        'value': quest['level_max']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'rewards',
                                        'value': quest['rewards']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'objectives',
                                        'value': quest['objectives']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'source_file',
                                        'value': quest['source_file']
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'created_at',
                                        'value': now
                                    }
                                },
                                {
                                    'column': {
                                        'name': 'updated_at',
                                        'value': now
                                    }
                                }
                            ]
                        }
                    }
                ]
            }
        }
        changes.append(change)

    liquibase_data = {
        'databaseChangeLog': changes
    }

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, indent=2)

def find_yaml_files(directory):
    """Recursively find all YAML files in directory"""
    yaml_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.yaml'):
                yaml_files.append(Path(root) / file)
    return yaml_files

def main():
    """Main function to import Asian and Middle Eastern cities quests"""

    print("Reading Asian and Middle Eastern cities quest files from: knowledge\\canon\\lore\\timeline-author\\quests\\asia")

    # Process Asia directory
    asia_dir = Path('knowledge/canon/lore/timeline-author/quests/asia')
    asian_yaml_files = find_yaml_files(asia_dir)

    print(f"Found {len(asian_yaml_files)} YAML files in Asia")

    # Check for CIS directory (contains Middle Eastern cities)
    cis_dir = Path('knowledge/canon/lore/timeline-author/quests/cis')
    if cis_dir.exists():
        cis_yaml_files = find_yaml_files(cis_dir)
        print(f"Found {len(cis_yaml_files)} YAML files in CIS (Middle Eastern)")
        all_yaml_files = asian_yaml_files + cis_yaml_files
    else:
        all_yaml_files = asian_yaml_files

    quests = []
    for yaml_file in all_yaml_files:
        print(f"Processing: {yaml_file.name}")
        quest_data = parse_asian_middle_eastern_quest(yaml_file)
        if quest_data:
            quests.append(quest_data)

    if not quests:
        print("No valid quests found!")
        return

    print(f"Successfully parsed {len(quests)} Asian and Middle Eastern cities quests")

    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_asian_middle_eastern_cities_2020_2093_import.yaml')
    create_liquibase_yaml(quests, output_file)

    print(f"Created Liquibase YAML file: {output_file}")
    print(f"Imported {len(quests)} Asian and Middle Eastern cities quests")
    print("Asian and Middle Eastern cities quests import completed successfully!")

if __name__ == "__main__":
    main()
