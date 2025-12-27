#!/usr/bin/env python3
"""
Washington DC Part 2 Quests Import Script
Imports Washington DC quest data from YAML files to Liquibase YAML format for database insertion.
Processes Part 2 (files 6-10).
"""

import os
import yaml
import json
from pathlib import Path
from datetime import datetime

def load_yaml_file(file_path):
    """Load and parse a YAML file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    except Exception as e:
        print(f"Error loading {file_path}: {e}")
        return None

def process_quest_data(quest_data, file_path):
    """Process individual quest data into database format."""
    try:
        # Extract data from quest_definition section
        quest_def = quest_data.get('quest_definition', {})
        metadata = quest_data.get('metadata', {})

        quest = {
            'quest_id': metadata.get('id', ''),
            'title': quest_def.get('title', metadata.get('title', '')),
            'description': quest_def.get('description', ''),
            'type': quest_def.get('quest_type', 'side'),
            'level_min': quest_def.get('level_min', 1),
            'level_max': quest_def.get('level_max', 50),
            'rewards': json.dumps(quest_def.get('rewards', {}), ensure_ascii=False),
            'objectives': json.dumps(quest_def.get('objectives', []), ensure_ascii=False),
            'location': quest_def.get('location', 'washington_dc'),
            'npc_start': quest_def.get('npc_start', ''),
            'npc_end': quest_def.get('npc_end', ''),
            'prerequisites': json.dumps(quest_def.get('requirements', {}).get('required_quests', []), ensure_ascii=False),
            'follow_up_quests': json.dumps(quest_def.get('follow_up_quests', []), ensure_ascii=False),
            'time_limit': quest_def.get('time_limit', None),
            'is_repeatable': quest_def.get('is_repeatable', False),
            'faction': quest_def.get('faction', ''),
            'created_at': datetime.now().isoformat(),
            'updated_at': datetime.now().isoformat(),
            'source_file': file_path
        }
        return quest
    except Exception as e:
        print(f"Error processing quest data from {file_path}: {e}")
        return None

def create_liquibase_yaml(quests, output_file):
    """Create Liquibase YAML file with quest data."""
    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': f'data_quests_washington_dc_part2_import_{datetime.now().strftime("%Y%m%d%H%M%S")}',
                    'author': 'washington_dc_quests_importer',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'quests',
                                'columns': [
                                    {'column': {'name': key, 'value': value}}
                                    for key, value in quest.items()
                                ]
                            }
                        }
                        for quest in quests
                    ]
                }
            }
        ]
    }

    # Ensure output directory exists
    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")

def main():
    """Main function to import Washington DC Part 2 quests."""
    input_dir = Path('knowledge/canon/lore/timeline-author/quests/america/washington-dc/2020-2029')
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_washington_dc_part2_import.yaml')

    quests = []

    # Process quest files 6-10 (Part 2)
    quest_files = sorted([f for f in input_dir.glob('quest-*.yaml')])[5:10]

    for quest_file in quest_files:
        print(f"Processing {quest_file.name}")
        quest_data = load_yaml_file(quest_file)

        if quest_data:
            processed_quest = process_quest_data(quest_data, str(quest_file))
            if processed_quest:
                quests.append(processed_quest)
        else:
            print(f"Failed to load {quest_file}")

    if quests:
        create_liquibase_yaml(quests, output_file)
        print(f"Successfully processed {len(quests)} Washington DC Part 2 quests")
    else:
        print("No quests were processed")

if __name__ == '__main__':
    main()
