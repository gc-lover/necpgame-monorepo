#!/usr/bin/env python3
"""
Toronto Part 3 Quests Import Script
Imports Toronto quest data from YAML files to Liquibase YAML format for database insertion.
Processes Part 3 (files 11-15).
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
        quest = {
            'quest_id': quest_data.get('id', ''),
            'title': quest_data.get('title', ''),
            'description': quest_data.get('description', ''),
            'type': quest_data.get('quest_type', 'side'),
            'level_min': quest_data.get('level_min', 1),
            'level_max': quest_data.get('level_max', 50),
            'rewards': json.dumps(quest_data.get('rewards', {}), ensure_ascii=False),
            'objectives': json.dumps(quest_data.get('objectives', []), ensure_ascii=False),
            'location': quest_data.get('location', ''),
            'npc_start': quest_data.get('npc_start', ''),
            'npc_end': quest_data.get('npc_end', ''),
            'prerequisites': json.dumps(quest_data.get('prerequisites', []), ensure_ascii=False),
            'follow_up_quests': json.dumps(quest_data.get('follow_up_quests', []), ensure_ascii=False),
            'time_limit': quest_data.get('time_limit', None),
            'is_repeatable': quest_data.get('is_repeatable', False),
            'faction': quest_data.get('faction', ''),
            'created_at': datetime.now().isoformat(),
            'updated_at': datetime.now().isoformat(),
            'source_file': file_path
        }
        return quest
    except Exception as e:
        print(f"Error processing quest data from {file_path}: {e}")
        return None

def create_liquibase_yaml(quests, output_file):
    """Create a Liquibase YAML file for inserting quests."""
    changeset_id = f"data_quests_toronto_part3_import_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'toronto_part3_quests_importer',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'quests',
                                'columns': [
                                    {'column': {'name': 'quest_id', 'value': quest['quest_id']}},
                                    {'column': {'name': 'title', 'value': quest['title']}},
                                    {'column': {'name': 'description', 'value': quest['description']}},
                                    {'column': {'name': 'type', 'value': quest['type']}},
                                    {'column': {'name': 'level_min', 'value': quest['level_min']}},
                                    {'column': {'name': 'level_max', 'value': quest['level_max']}},
                                    {'column': {'name': 'rewards', 'value': quest['rewards']}},
                                    {'column': {'name': 'objectives', 'value': quest['objectives']}},
                                    {'column': {'name': 'location', 'value': quest['location']}},
                                    {'column': {'name': 'npc_start', 'value': quest['npc_start']}},
                                    {'column': {'name': 'npc_end', 'value': quest['npc_end']}},
                                    {'column': {'name': 'prerequisites', 'value': quest['prerequisites']}},
                                    {'column': {'name': 'follow_up_quests', 'value': quest['follow_up_quests']}},
                                    {'column': {'name': 'time_limit', 'value': quest['time_limit']}},
                                    {'column': {'name': 'is_repeatable', 'value': quest['is_repeatable']}},
                                    {'column': {'name': 'faction', 'value': quest['faction']}},
                                    {'column': {'name': 'created_at', 'value': quest['created_at']}},
                                    {'column': {'name': 'updated_at', 'value': quest['updated_at']}},
                                    {'column': {'name': 'source_file', 'value': quest['source_file']}}
                                ]
                            }
                        } for quest in quests
                    ]
                }
            }
        ]
    }

    # Ensure output directory exists
    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    # Write to file
    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")

def main():
    """Main function to import Toronto Part 3 quests."""
    input_dir = Path('knowledge/canon/lore/timeline-author/quests/america/toronto/2020-2029')
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_toronto_part3_import.yaml')

    quests = []

    # Process next 5 quest files (Part 3: files 11-15)
    quest_files = sorted([f for f in input_dir.glob('quest-*.yaml')])[10:15]

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
        print(f"Successfully processed {len(quests)} Toronto Part 3 quests")
    else:
        print("No quests were processed")

if __name__ == '__main__':
    main()
