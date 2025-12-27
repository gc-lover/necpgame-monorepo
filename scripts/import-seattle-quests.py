#!/usr/bin/env python3
"""
Import Seattle 2020-2029 quests to database migrations.
Backend agent import script for issue #2249.
"""

import os
import yaml
import json
from pathlib import Path
from datetime import datetime
import hashlib
import uuid

def generate_migration_id(content_type, quest_id):
    """Generate unique migration ID."""
    hash_obj = hashlib.md5(f"{content_type}-{quest_id}".encode())
    return hash_obj.hexdigest()[:16]

def create_migration_file(quest_data, output_dir):
    """Create Liquibase migration file for a quest."""
    quest_id = quest_data['metadata']['id']
    migration_id = generate_migration_id('quests', quest_id)

    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')

    filename = f"data_quests_{quest_id.replace('canon-quest-seattle-', '')}_{migration_id}_{timestamp}.yaml"
    filepath = Path(output_dir) / filename

    # Extract quest definition
    qd = quest_data.get('quest_definition', {})
    summary = quest_data.get('summary', {})

    # Prepare data for migration
    metadata = {
        'id': quest_id,
        'version': quest_data['metadata'].get('version', '1.0.0'),
        'source_file': str(quest_data['metadata'].get('source', ''))
    }

    # Generate unique ID for database
    db_id = str(uuid.uuid4().int >> 64)  # Use 64-bit int

    # Prepare objectives as JSON
    objectives = qd.get('objectives', [])
    objectives_json = json.dumps(objectives, ensure_ascii=False)

    # Prepare rewards as JSON
    rewards = qd.get('rewards', {})
    rewards_json = json.dumps(rewards, ensure_ascii=False)

    # Create migration content
    migration = {
        'databaseChangeLog': [{
            'changeSet': {
                'id': f'quests-{quest_id.replace("canon-quest-seattle-", "")}-{migration_id}',
                'author': 'backend-agent-import',
                'changes': [{
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': db_id}},
                            {'column': {'name': 'metadata', 'value': json.dumps(metadata, ensure_ascii=False)}},
                            {'column': {'name': 'quest_id', 'value': quest_id}},
                            {'column': {'name': 'title', 'value': quest_data['metadata']['title']}},
                            {'column': {'name': 'description', 'value': summary.get('essence', '')}},
                            {'column': {'name': 'status', 'value': 'active'}},
                            {'column': {'name': 'level_min', 'value': qd.get('level_min')}},
                            {'column': {'name': 'level_max', 'value': qd.get('level_max')}},
                            {'column': {'name': 'rewards', 'value': rewards_json}},
                            {'column': {'name': 'objectives', 'value': objectives_json}}
                        ]
                    }
                }]
            }
        }]
    }

    # Write migration file
    with open(filepath, 'w', encoding='utf-8') as f:
        yaml.dump(migration, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created migration: {filename}")
    return filepath

def main():
    """Main import function."""
    quest_files = [
        'knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-026-amazon-shadow-hackers.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-027-underground-ripperdoc.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-028-eco-protest-revolution.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-029-virtual-reality-neural-dreams.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/quest-030-rain-city-street-racing.yaml'
    ]

    output_dir = 'infrastructure/liquibase/data/gameplay/quests'
    os.makedirs(output_dir, exist_ok=True)

    created_files = []

    for quest_file in quest_files:
        if os.path.exists(quest_file):
            print(f"Processing: {quest_file}")
            try:
                with open(quest_file, 'r', encoding='utf-8') as f:
                    quest_data = yaml.safe_load(f)

                migration_file = create_migration_file(quest_data, output_dir)
                created_files.append(migration_file)

            except Exception as e:
                print(f"Error processing {quest_file}: {e}")
        else:
            print(f"File not found: {quest_file}")

    print(f"\nImport completed. Created {len(created_files)} migration files:")
    for file_path in created_files:
        print(f"  - {file_path}")

if __name__ == '__main__':
    main()
