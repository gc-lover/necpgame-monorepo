#!/usr/bin/env python3
"""
Import Detroit Quests to Database
Imports quest data from YAML files to database using Liquibase format.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def parse_quest_content(content):
    """Parse quest content from YAML file"""
    sections = content.get('content', {}).get('sections', [])

    detroit_quests = []
    for section in sections:
        quest_id = section.get('id', '')
        if 'detroit' in quest_id.lower():
            title = section.get('title', '')
            body = section.get('body', '')

            # Parse quest information from body
            lines = body.split('\n')

            # Extract metadata
            city = "Detroit, Michigan, USA"
            period = ""
            level_range = ""
            quest_type = ""
            synopsis = ""
            rewards = []
            objectives = []

            for line in lines:
                line = line.strip()
                if line.startswith('**Период:**'):
                    period = line.replace('**Период:**', '').strip()
                elif line.startswith('**Уровень:**'):
                    level_range = line.replace('**Уровень:**', '').strip()
                elif line.startswith('**Тип:**'):
                    quest_type = line.replace('**Тип:**', '').strip()
                elif line.startswith('**Синопсис:**'):
                    synopsis = line.replace('**Синопсис:**', '').strip()
                elif line.startswith('**Награды:**'):
                    # Parse rewards section
                    rewards_text = ""
                    idx = lines.index(line) + 1
                    while idx < len(lines) and not lines[idx].startswith('**'):
                        rewards_text += lines[idx] + '\n'
                        idx += 1
                    rewards = parse_rewards(rewards_text)
                elif line.startswith('**Основная петля:**'):
                    # Parse objectives section
                    objectives_text = ""
                    idx = lines.index(line) + 1
                    while idx < len(lines) and not lines[idx].startswith('**'):
                        objectives_text += lines[idx] + '\n'
                        idx += 1
                    objectives = parse_objectives(objectives_text)

            # Parse level range
            level_min = 10
            level_max = 25
            if '-' in level_range:
                parts = level_range.split('-')
                try:
                    level_min = int(parts[0])
                    level_max = int(parts[1])
                except:
                    pass

            quest_data = {
                'id': str(uuid.uuid4()),
                'quest_id': quest_id.replace('WQ-AMERICA-', '').replace('-', '_').lower(),
                'title': title,
                'description': synopsis,
                'status': 'active',
                'level_min': level_min,
                'level_max': level_max,
                'rewards': rewards,
                'objectives': objectives,
                'metadata': {
                    'city': city,
                    'period': period,
                    'type': quest_type,
                    'source_file': 'america-cities-2020-2029.yaml',
                    'imported_at': datetime.now().isoformat(),
                    'version': '1.0.0'
                }
            }

            detroit_quests.append(quest_data)

    return detroit_quests

def parse_rewards(rewards_text):
    """Parse rewards from text"""
    rewards = []

    # Default rewards
    rewards.append({
        'type': 'experience',
        'amount': 1500,
        'description': 'Опыт за выполнение квеста'
    })

    rewards.append({
        'type': 'currency',
        'amount': 500,
        'currency': 'eddies',
        'description': 'Вознаграждение в эдди'
    })

    # Try to parse specific rewards from text
    lines = rewards_text.strip().split('\n')
    for line in lines:
        if 'ED' in line or 'eddies' in line.lower():
            try:
                # Extract ED amount
                if 'ED' in line:
                    parts = line.split('ED')
                    amount = int(parts[0].strip())
                    rewards.append({
                        'type': 'currency',
                        'amount': amount,
                        'currency': 'eddies',
                        'description': 'Вознаграждение в эдди'
                    })
            except:
                pass

    return rewards

def parse_objectives(objectives_text):
    """Parse objectives from text"""
    objectives = []

    lines = objectives_text.strip().split('\n')
    for line in lines:
        line = line.strip()
        if line and not line.startswith('**'):
            # Remove numbering
            line = line.lstrip('0123456789. ')
            if line.startswith('**'):
                # Bold text - objective type
                objective_type = line.replace('**', '').strip()
                objectives.append({
                    'type': objective_type.lower().replace(' ', '_'),
                    'description': objective_type,
                    'required': True
                })

    if not objectives:
        # Default objectives
        objectives = [
            {
                'type': 'main_quest',
                'description': 'Выполнить основную задачу квеста',
                'required': True
            }
        ]

    return objectives

def create_liquibase_yaml(quests, output_file):
    """Create Liquibase YAML file for quest import"""

    changesets = []

    for quest in quests:
        changeset_id = f"quests-detroit-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'detroit-quests-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': quest['id']}},
                            {'column': {'name': 'title', 'value': quest['title']}},
                            {'column': {'name': 'description', 'value': quest['description']}},
                            {'column': {'name': 'status', 'value': quest['status']}},
                            {'column': {'name': 'level_min', 'value': quest['level_min']}},
                            {'column': {'name': 'level_max', 'value': quest['level_max']}},
                            {'column': {'name': 'rewards', 'value': json.dumps(quest['rewards'])}},
                            {'column': {'name': 'objectives', 'value': json.dumps(quest['objectives'])}},
                            {'column': {'name': 'metadata', 'value': json.dumps(quest['metadata'])}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")
    print(f"Imported {len(quests)} Detroit quests")

def main():
    """Main function"""
    # Input file
    input_file = Path('knowledge/content/quests/world/america/america-cities-2020-2029.yaml')

    # Output file
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_detroit_2020_2029_import.yaml')

    # Ensure output directory exists
    output_file.parent.mkdir(parents=True, exist_ok=True)

    print(f"Reading quest data from: {input_file}")

    # Read and parse YAML
    with open(input_file, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    # Parse Detroit quests
    detroit_quests = parse_quest_content(data)

    if not detroit_quests:
        print("No Detroit quests found!")
        return

    print(f"Found {len(detroit_quests)} Detroit quests")

    # Create Liquibase YAML
    create_liquibase_yaml(detroit_quests, output_file)

    print("Detroit quests import completed successfully!")

if __name__ == '__main__':
    main()
