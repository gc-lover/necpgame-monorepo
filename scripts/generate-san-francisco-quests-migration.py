#!/usr/bin/env python3
"""
San Francisco Quests Migration Generator
Generates database migration for San Francisco quests from YAML files

Usage:
    python scripts/generate-san-francisco-quests-migration.py

Author: NECPGAME Backend Agent
"""

import yaml
import json
from datetime import datetime
from pathlib import Path

def generate_migration(quest_file):
    """Generate migration for a single quest file"""

    with open(quest_file, 'r', encoding='utf-8') as f:
        quest_data = yaml.safe_load(f)

    quest_def = quest_data.get('quest_definition', {})
    metadata = quest_data.get('metadata', {})

    # Generate unique ID
    quest_id = str(abs(hash(metadata['id'])))[:16]

    migration = {
        'databaseChangeLog': [{
            'changeSet': {
                'id': f'quests-{metadata["id"].replace("canon-quest-", "").replace("-", "-")}',
                'author': 'backend-agent-import',
                'changes': [{
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': quest_id}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'id': metadata['id'],
                                'version': metadata.get('version', '1.0.0'),
                                'source_file': quest_file.replace('knowledge/', '')
                            })}},
                            {'column': {'name': 'title', 'value': metadata.get('title', 'Unknown Quest')}},
                            {'column': {'name': 'description', 'value': quest_def.get('description', 'No description')}},
                            {'column': {'name': 'status', 'value': 'active'}},
                            {'column': {'name': 'level_min', 'value': quest_def.get('level_min', 1)}},
                            {'column': {'name': 'level_max', 'value': quest_def.get('level_max', 100)}},
                            {'column': {'name': 'rewards', 'value': json.dumps(quest_def.get('rewards', {}))}},
                            {'column': {'name': 'objectives', 'value': json.dumps([obj.get('text', obj.get('id', 'Unknown objective')) for obj in quest_def.get('objectives', [])])}},
                            {'column': {'name': 'created_at', 'value': datetime.now().isoformat()}},
                            {'column': {'name': 'updated_at', 'value': datetime.now().isoformat()}}
                        ]
                    }
                }]
            }
        }]
    }

    return migration

def main():
    """Main function to generate migrations for all San Francisco quests"""

    quest_files = [
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml'
    ]

    migrations_created = 0

    for quest_file in quest_files:
        if not Path(quest_file).exists():
            print(f'Warning: Quest file not found: {quest_file}')
            continue

        try:
            migration = generate_migration(quest_file)

            # Extract quest ID for filename
            quest_data = yaml.safe_load(open(quest_file, 'r', encoding='utf-8'))
            metadata = quest_data.get('metadata', {})
            quest_id = metadata['id'].replace('canon-quest-', '').replace('-', '_')

            output_file = f'infrastructure/liquibase/migrations/gameplay/quests/data_quests_{quest_id}.yaml'

            with open(output_file, 'w', encoding='utf-8') as f:
                yaml.dump(migration, f, default_flow_style=False, allow_unicode=True)

            print(f'Created migration: {output_file}')
            migrations_created += 1

        except Exception as e:
            print(f'Error processing {quest_file}: {e}')

    print(f'\nTotal migrations created: {migrations_created}')

if __name__ == '__main__':
    main()