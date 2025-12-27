import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def parse_europe_city_quest(yaml_file):
    """Parse individual Europe city quest from YAML file"""
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

        # Extract city and period from path
        city = yaml_file.parent.parent.name  # berlin, amsterdam, etc.
        period = yaml_file.parent.name  # 2020-2029, 2030-2039, etc.

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
                'type': quest_def.get('quest_type', 'side'),
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

        changeset_id = f"quests-europe-cities-{quest['quest_id']}-{hashlib.md5(str(quest).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'europe-cities-quests-import',
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
    print(f"Imported {len(changesets)} Europe cities quests")

def main():
    """Main function"""
    input_base_dir = Path('knowledge/canon/lore/timeline-author/quests/europe')

    # Process only berlin, amsterdam, brussels
    cities = ['berlin', 'amsterdam', 'brussels']
    output_file = Path('infrastructure/liquibase/data/gameplay/quests/data_quests_europe_cities_2020_2093_import.yaml')

    print(f"Reading Europe cities quest files from: {input_base_dir}")

    europe_cities_quests = []

    for city in cities:
        city_dir = input_base_dir / city
        if not city_dir.exists():
            print(f"Directory {city_dir} not found, skipping")
            continue

        print(f"Processing {city}...")

        # Find all YAML files recursively in subdirectories
        yaml_files = list(city_dir.glob('**/*.yaml'))
        print(f"Found {len(yaml_files)} YAML files in {city}")

        for yaml_file in yaml_files:
            print(f"Processing: {yaml_file.name}")
            quest_data = parse_europe_city_quest(yaml_file)
            if quest_data:
                europe_cities_quests.append(quest_data)

    if not europe_cities_quests:
        print("No Europe cities quests found!")
        return

    print(f"Successfully parsed {len(europe_cities_quests)} Europe cities quests")

    create_liquibase_yaml(europe_cities_quests, output_file)

    print("Europe cities quests import completed successfully!")

if __name__ == '__main__':
    main()
