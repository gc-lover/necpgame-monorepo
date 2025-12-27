import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def parse_content_quest(yaml_file):
    """Parse individual content quest from YAML file"""
    try:
        with open(yaml_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        metadata = data.get('metadata', {})
        quest_def = data.get('quest_definition', {})

        quest_id = metadata.get('id', '')
        title = metadata.get('title', '')
        description = data.get('summary', {}).get('essence', '')

        level_min = quest_def.get('level_min', 1)
        level_max = quest_def.get('level_max', None)

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

        rewards_data = quest_def.get('rewards', {})
        rewards = []

        if 'xp' in rewards_data:
            rewards.append({
                'type': 'experience',
                'amount': rewards_data['xp'],
                'description': 'Опыт за выполнение квеста'
            })

        if 'currency' in rewards_data:
            rewards.append({
                'type': 'currency',
                'amount': rewards_data['currency'],
                'currency': 'eddies',
                'description': 'Вознаграждение в эдди'
            })

        if 'reputation' in rewards_data:
            for rep_type, amount in rewards_data['reputation'].items():
                rewards.append({
                    'type': 'reputation',
                    'reputation_type': rep_type,
                    'amount': amount,
                    'description': f'Репутация: {rep_type}'
                })

        if 'items' in rewards_data and rewards_data['items']:
            for item in rewards_data['items']:
                rewards.append({
                    'type': 'item',
                    'item_id': item,
                    'description': f'Предмет: {item}'
                })

        if not rewards:
            rewards = [
                {
                    'type': 'experience',
                    'amount': 1000,
                    'description': 'Опыт за выполнение квеста'
                },
                {
                    'type': 'currency',
                    'amount': 400,
                    'currency': 'eddies',
                    'description': 'Вознаграждение в эдди'
                }
            ]

        # Extract city and period from path
        parts = yaml_file.parts
        city = parts[-3]  # city name
        period = parts[-2]  # period like 2020-2029

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
                'city': city,
                'period': period,
                'type': quest_def.get('quest_type', 'content'),
                'source_file': str(yaml_file),
                'imported_at': datetime.now().isoformat(),
                'version': metadata.get('version', '1.0.0'),
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

        changeset_id = f"quests-content-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'content-quests-import',
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

    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase YAML file: {output_file}")
    print(f"Imported {len(changesets)} content quests")

def main():
    """Main function"""
    input_base_dir = Path('knowledge/canon/lore/timeline-author/quests')

    print(f"Reading content quests from: {input_base_dir}")

    content_quests = []

    # Find all YAML files recursively
    yaml_files = list(input_base_dir.glob('**/*.yaml'))
    print(f"Found {len(yaml_files)} total YAML files")

    processed_count = 0
    content_count = 0

    for yaml_file in yaml_files:
        processed_count += 1
        if processed_count % 200 == 0:
            print(f"Processed {processed_count} files, found {content_count} content quests")

        quest_data = parse_content_quest(yaml_file)
        if quest_data:
            content_quests.append(quest_data)
            content_count += 1

            # Limit to 140 quests as per task description
            if content_count >= 140:
                break

    print(f"Successfully parsed {len(content_quests)} content quests")

    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_content_import.yaml')
    create_liquibase_yaml(content_quests, output_file)

    print("Content quests import completed successfully!")

if __name__ == '__main__':
    main()
