#!/usr/bin/env python3
"""
Import Phoenix Part 1 Quests to Database
Imports quest data from YAML files to database using Liquibase format.
Imports first part of Phoenix quests from 2020-2029 period.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def parse_phoenix_quest(yaml_file):
    """Parse individual Phoenix quest from YAML file"""
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
                    'amount': 1200,
                    'description': 'Опыт за выполнение квеста'
                },
                {
                    'type': 'currency',
                    'amount': 500,
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
                'city': 'Phoenix, Arizona, USA',
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
        changeset_id = f"quests-phoenix-part1-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'phoenix-part1-quests-import',
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
    print(f"Imported {len(changesets)} Phoenix Part 1 quests")

def main():
    """Main function"""
    # Input directory with Phoenix quests
    input_dir = Path('knowledge/canon/lore/timeline-author/quests/america/phoenix/2020-2029')

    # Output file for Liquibase migration
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_phoenix_part1_import.yaml')

    print(f"Reading Phoenix quest files from: {input_dir}")

    # Find all YAML files in the directory
    yaml_files = list(input_dir.glob('*.yaml'))
    print(f"Found {len(yaml_files)} YAML files")

    # Parse all Phoenix quests
    phoenix_quests = []
    for yaml_file in yaml_files:
        print(f"Processing: {yaml_file.name}")
        quest_data = parse_phoenix_quest(yaml_file)
        if quest_data:
            phoenix_quests.append(quest_data)

    if not phoenix_quests:
        print("No Phoenix quests found!")
        return

    print(f"Successfully parsed {len(phoenix_quests)} Phoenix quests")

    # Create Liquibase YAML
    create_liquibase_yaml(phoenix_quests, output_file)

    print("Phoenix Part 1 quests import completed successfully!")

if __name__ == '__main__':
    main()
