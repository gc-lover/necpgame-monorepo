#!/usr/bin/env python3
"""
Los Angeles Quests Migration Generator
Generates database migration for Los Angeles quests from YAML file

Usage:
    python scripts/generate-los-angeles-quests-migration.py

Author: NECPGAME Backend Agent
"""

import yaml
import json
from pathlib import Path
from datetime import datetime

def extract_quest_data(content_section):
    """Extract quest data from content section"""
    quest_data = {}

    # Extract description from body
    body = content_section.get('body', '')

    # Extract city and period
    if '**Город:**' in body:
        lines = body.split('\n')
        for line in lines:
            if '**Город:**' in line:
                quest_data['city'] = line.split('**Город:**')[1].strip()
            elif '**Период:**' in line:
                period = line.split('**Период:**')[1].strip()
                quest_data['period'] = period.split('(')[0].strip() if '(' in period else period
            elif '**Уровень:**' in line:
                level_range = line.split('**Уровень:**')[1].strip()
                if '-' in level_range:
                    min_level, max_level = level_range.split('-')
                    quest_data['level_min'] = int(min_level.strip())
                    quest_data['level_max'] = int(max_level.strip())
            elif '**Тип:**' in line:
                quest_data['quest_type'] = line.split('**Тип:**')[1].strip()

    # Extract synopsis
    if '**Синопсис:**' in body:
        synopsis_start = body.find('**Синопсис:**')
        next_section = body.find('**Главный NPC:**', synopsis_start)
        if next_section == -1:
            next_section = body.find('**Основная петля:**', synopsis_start)
        if next_section == -1:
            next_section = len(body)

        quest_data['description'] = body[synopsis_start:next_section].replace('**Синопсис:**', '').strip()

    return quest_data

def generate_migration_yaml(quest_data, quest_id):
    """Generate Liquibase migration YAML"""
    migration = {
        'databaseChangeLog': [{
            'changeSet': {
                'id': f'quests-{quest_id}',
                'author': 'content-migration-generator',
                'changes': [{
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {
                                'column': {'name': 'id', 'valueComputed': 'gen_random_uuid()'}
                            },
                            {
                                'column': {
                                    'name': 'metadata',
                                    'value': json.dumps({
                                        'id': quest_id,
                                        'version': '1.0.0',
                                        'source_file': f'knowledge/content/quests/world/america/america-los-angeles-2020-2029.yaml'
                                    })
                                }
                            },
                            {
                                'column': {'name': 'title', 'value': quest_data.get('title', f'Quest: {quest_id}')}
                            },
                            {
                                'column': {'name': 'description', 'value': quest_data.get('description', '')}
                            },
                            {
                                'column': {'name': 'status', 'value': 'active'}
                            },
                            {
                                'column': {'name': 'level_min', 'value': quest_data.get('level_min', 1)}
                            },
                            {
                                'column': {'name': 'level_max', 'value': quest_data.get('level_max', 50)}
                            },
                            {
                                'column': {'name': 'quest_type', 'value': quest_data.get('quest_type', 'main')}
                            },
                            {
                                'column': {
                                    'name': 'rewards',
                                    'value': json.dumps({
                                        'experience': 15000,
                                        'currency': {'type': 'eddies', 'amount': 25000},
                                        'items': []
                                    })
                                }
                            },
                            {
                                'column': {
                                    'name': 'objectives',
                                    'value': json.dumps([
                                        {
                                            'id': 'main_objective',
                                            'title': 'Complete main quest objective',
                                            'description': 'Complete the primary quest goal',
                                            'type': 'main',
                                            'order': 1
                                        }
                                    ])
                                }
                            },
                            {
                                'column': {'name': 'created_at', 'value': datetime.now().isoformat()}
                            },
                            {
                                'column': {'name': 'updated_at', 'value': datetime.now().isoformat()}
                            }
                        ]
                    }
                }]
            }
        }]
    }

    return migration

def main():
    # Load Los Angeles quests file
    yaml_file = Path('knowledge/content/quests/world/america/america-los-angeles-2020-2029.yaml')

    with open(yaml_file, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    # Extract quests from content sections
    content_sections = data.get('content', {}).get('sections', [])
    print(f"Found {len(content_sections)} sections")

    quest_count = 0
    for section in content_sections:
        print(f"Processing section: {section.get('title', 'No title')}")
        if 'title' in section and 'WQ-AMERICA-' in section['title']:
            quest_data = extract_quest_data(section)
            print(f"Extracted quest data: {quest_data}")

            # Generate quest ID
            title_parts = section['title'].split(' ')
            quest_id = None
            if len(title_parts) >= 1:
                quest_code = title_parts[0]  # WQ-AMERICA-2022-001
                # Extract numbers from WQ-AMERICA-2022-001
                code_parts = quest_code.split('-')
                if len(code_parts) >= 4:
                    year = code_parts[2]
                    number = code_parts[3]
                    quest_id = f"content-world-los-angeles-{year}-{number}"

            if quest_data and 'city' in quest_data:  # Simplified condition
                # Set title
                quest_data['title'] = section['title']

                # Generate quest_id if not set
                if 'quest_id' not in quest_data:
                    title_parts = section['title'].split(' ')
                    if len(title_parts) >= 1:
                        quest_code = title_parts[0]  # WQ-AMERICA-2022-001
                        code_parts = quest_code.split('-')
                        if len(code_parts) >= 4:
                            year = code_parts[2]
                            number = code_parts[3]
                            quest_data['quest_id'] = f"content-world-los-angeles-{year}-{number}"

                if 'quest_id' in quest_data:
                    # Generate migration
                    migration = generate_migration_yaml(quest_data, quest_data['quest_id'])

                    # Save migration file (use safe filename)
                    safe_filename = quest_data['quest_id'].replace('content-world-', 'los-angeles-quest-')
                    migration_file = Path(f'infrastructure/liquibase/migrations/gameplay/quests/data_quests_{safe_filename}.yaml')
                    with open(migration_file, 'w', encoding='utf-8') as f:
                        yaml.dump(migration, f, default_flow_style=False, allow_unicode=True)

                    print(f"Generated migration for quest: {quest_data['quest_id']}")
                    quest_count += 1

    print(f"Total migrations generated: {quest_count}")

if __name__ == '__main__':
    main()
