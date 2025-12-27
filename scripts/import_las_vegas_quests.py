#!/usr/bin/env python3
"""
Import Las Vegas Quests to Database
Imports quest data from YAML files to database using Liquibase format.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def parse_las_vegas_quest(yaml_file):
    """Parse individual Las Vegas quest from YAML file"""
    try:
        with open(yaml_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        metadata = data.get('metadata', {})
        quest_def = data.get('quest_definition', {})

        # Extract quest information
        quest_id = metadata.get('id', '')
        title = metadata.get('title', '')
        description = data.get('summary', {}).get('essence', '')

        # Get level requirements
        level_min = quest_def.get('level_min', 1)
        level_max = quest_def.get('level_max', None)

        # Parse objectives
        objectives = []
        for obj in quest_def.get('objectives', []):
            objectives.append({
                'id': obj.get('id', ''),
                'type': obj.get('type', 'main'),
                'description': obj.get('text', ''),
                'target': obj.get('target', ''),
                'count': obj.get('count', 1),
                'optional': obj.get('optional', False)
            })

        # Parse rewards
        rewards_data = quest_def.get('rewards', {})
        rewards = []

        # Experience reward
        if 'experience' in rewards_data:
            rewards.append({
                'type': 'experience',
                'amount': rewards_data['experience'],
                'description': 'Опыт за выполнение квеста'
            })

        # Money reward
        if 'money' in rewards_data:
            rewards.append({
                'type': 'currency',
                'amount': rewards_data['money'],
                'currency': 'eddies',
                'description': 'Вознаграждение в эдди'
            })

        # Reputation rewards
        if 'reputation' in rewards_data:
            for rep_type, amount in rewards_data['reputation'].items():
                rewards.append({
                    'type': 'reputation',
                    'reputation_type': rep_type,
                    'amount': amount,
                    'description': f'Репутация: {rep_type}'
                })

        # Item rewards
        if 'items' in rewards_data and rewards_data['items']:
            for item in rewards_data['items']:
                rewards.append({
                    'type': 'item',
                    'item_id': item,
                    'description': f'Предмет: {item}'
                })

        # Default rewards if none specified
        if not rewards:
            rewards = [
                {
                    'type': 'experience',
                    'amount': 1500,
                    'description': 'Опыт за выполнение квеста'
                },
                {
                    'type': 'currency',
                    'amount': 750,
                    'currency': 'eddies',
                    'description': 'Вознаграждение в эдди'
                }
            ]

        quest_data = {
            'id': str(uuid.uuid4()),
            'quest_id': quest_id,
            'title': title,
            'description': description,
            'status': 'active',
            'level_min': level_min,
            'level_max': level_max,
            'rewards': rewards,
            'objectives': objectives,
            'metadata': {
                'city': 'Las Vegas, Nevada, USA',
                'period': '2020-2029',
                'type': quest_def.get('quest_type', 'side'),
                'source_file': str(yaml_file),
                'imported_at': datetime.now().isoformat(),
                'version': metadata.get('version', '2.0.0'),
                'tags': metadata.get('tags', [])
            }
        }

        return quest_data

    except Exception as e:
        print(f"Error parsing {yaml_file}: {e}")
        return None

def create_liquibase_yaml(quests, output_file):
    """Create Liquibase YAML file for quest import"""

    changesets = []

    for quest in quests:
        if not quest:
            continue

        # Create unique changeset ID
        changeset_id = f"quests-las-vegas-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'las-vegas-quests-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': quest['id']}},
                            {'column': {'name': 'quest_id', 'value': quest['quest_id']}},
                            {'column': {'name': 'title', 'value': quest['title']}},
                            {'column': {'name': 'description', 'value': quest['description']}},
                            {'column': {'name': 'status', 'value': quest['status']}},
                            {'column': {'name': 'level_min', 'value': quest['level_min']}},
                            {'column': {'name': 'level_max', 'value': quest['level_max']}},
                            {'column': {'name': 'rewards', 'value': json.dumps(quest['rewards'], ensure_ascii=False)}},
                            {'column': {'name': 'objectives', 'value': json.dumps(quest['objectives'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps(quest['metadata'], ensure_ascii=False)}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    # Ensure output directory exists
    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")
    print(f"Imported {len(changesets)} Las Vegas quests")

def main():
    """Main function"""
    # Input directory with Las Vegas quests
    input_dir = Path('knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029')

    # Output file for Liquibase migration
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_las_vegas_2020_2029_import.yaml')

    print(f"Reading Las Vegas quest files from: {input_dir}")

    # Find all YAML files in the directory (but only the first 3 as per issue requirements)
    yaml_files = sorted(list(input_dir.glob('*.yaml')))
    # Take only the first 3 files as specified in the issue: quest-001, quest-002, quest-003
    yaml_files = yaml_files[:3]
    print(f"Found {len(yaml_files)} YAML files (processing first 3)")

    # Parse all Las Vegas quests
    las_vegas_quests = []
    for yaml_file in yaml_files:
        print(f"Processing: {yaml_file.name}")
        quest_data = parse_las_vegas_quest(yaml_file)
        if quest_data:
            las_vegas_quests.append(quest_data)

    if not las_vegas_quests:
        print("No Las Vegas quests found!")
        return

    print(f"Successfully parsed {len(las_vegas_quests)} Las Vegas quests")

    # Create Liquibase YAML
    create_liquibase_yaml(las_vegas_quests, output_file)

    print("Las Vegas quests import completed successfully!")

if __name__ == '__main__':
    main()
