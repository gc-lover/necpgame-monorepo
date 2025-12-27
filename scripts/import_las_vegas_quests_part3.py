#!/usr/bin/env python3
"""
Las Vegas Quests Part 3 Import Script
Imports Las Vegas quest data from YAML files to Liquibase YAML format for database insertion.
Processes Part 3: files 9-11 (desert-heat, what-happens-in-vegas, neon-nightmares)
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
            'type': quest_data.get('type', 'MAIN'),
            'status': quest_data.get('status', 'ACTIVE'),
            'level_requirement': quest_data.get('level_requirement', 1),
            'reward_xp': quest_data.get('reward', {}).get('xp', 0),
            'reward_currency': quest_data.get('reward', {}).get('currency', 0),
            'location': quest_data.get('location', 'Las Vegas'),
            'timeline': quest_data.get('timeline', '2020-2029'),
            'created_at': datetime.now().isoformat(),
            'updated_at': datetime.now().isoformat()
        }

        # Add objectives if they exist
        objectives = quest_data.get('objectives', [])
        if objectives:
            quest['objectives'] = json.dumps(objectives, ensure_ascii=False)

        # Add NPC data if available
        npc_data = quest_data.get('npc', {})
        if npc_data:
            quest['npc_id'] = npc_data.get('id')
            quest['npc_name'] = npc_data.get('name')

        return quest
    except Exception as e:
        print(f"Error processing quest data from {file_path}: {e}")
        return None

def create_liquibase_yaml(quests_data, output_file):
    """Create Liquibase YAML file with quest data."""
    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': 'insert-las-vegas-quests-part3',
                    'author': 'backend-agent',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'quests',
                                'columns': []
                            }
                        }
                    ]
                }
            }
        ]
    }

    columns = liquibase_data['databaseChangeLog'][0]['changeSet']['changes'][0]['insert']['columns']

    for quest in quests_data:
        if quest:
            for key, value in quest.items():
                if value is not None:
                    columns.append({
                        'column': {
                            'name': key,
                            'value': str(value)
                        }
                    })

    # Write to file
    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")

def main():
    """Main function to process Las Vegas quest files Part 3."""
    # Base directory for Las Vegas quests
    base_dir = Path("knowledge/canon/lore/timeline-author/quests/america/las-vegas")

    if not base_dir.exists():
        print(f"Directory {base_dir} does not exist!")
        return

    # Find all YAML files in subdirectories
    yaml_files = []
    for subdir in base_dir.iterdir():
        if subdir.is_dir():
            yaml_files.extend(list(subdir.glob("*.yaml")))

    print(f"Reading Las Vegas quest files from: {base_dir}")
    print(f"Found {len(yaml_files)} YAML files")

    if not yaml_files:
        print("No YAML files found!")
        return

    # Sort files by name to ensure consistent ordering
    yaml_files.sort(key=lambda x: x.name)

    # Part 3 would be files 9-11 (0-indexed: 8,9,10)
    start_idx = 8
    end_idx = min(11, len(yaml_files))

    selected_files = yaml_files[start_idx:end_idx]
    print(f"Processing Part 3: files {start_idx+1}-{end_idx} ({len(selected_files)} files)")

    quests_data = []
    processed_count = 0

    for file_path in selected_files:
        print(f"Processing: {file_path.name}")
        quest_data = load_yaml_file(file_path)

        if quest_data:
            processed_quest = process_quest_data(quest_data, file_path)
            if processed_quest:
                quests_data.append(processed_quest)
                processed_count += 1

    if not quests_data:
        print("No quest data processed!")
        return

    print(f"Successfully parsed {processed_count} Las Vegas quests from Part 3")

    # Create Liquibase YAML file
    output_file = "infrastructure/liquibase/data/gameplay/quests/data_quests_las_vegas_part3_import.yaml"
    os.makedirs(os.path.dirname(output_file), exist_ok=True)

    create_liquibase_yaml(quests_data, output_file)

    print(f"Las Vegas quests Part 3 import completed successfully!")
    print(f"Imported {len(quests_data)} quests from Part 3")

if __name__ == "__main__":
    main()
