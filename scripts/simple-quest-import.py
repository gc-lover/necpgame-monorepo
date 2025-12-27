#!/usr/bin/env python3
"""
Simple Quest Import Script
Import quest YAML files to Liquibase migrations for database insertion.

Issue: #614
"""

import hashlib
import json
import uuid
from datetime import datetime
from pathlib import Path
from typing import Dict, Any, Optional

import yaml


def main():
    """Main import logic"""
    import argparse

    parser = argparse.ArgumentParser(description="Import quest YAML files to database via Liquibase migrations")
    parser.add_argument('--quest-file', '-f', type=str, required=True, help='Path to quest YAML file to import')
    parser.add_argument('--output-dir', '-o', type=str, default='infrastructure/liquibase/data/gameplay/quests', help='Output directory for migration files')
    parser.add_argument('--force', '-F', action='store_true', help='Overwrite existing migration file')

    args = parser.parse_args()

    quest_file = Path(args.quest_file)
    output_dir = Path(args.output_dir)

    # Validate input file
    if not quest_file.exists():
        print(f"ERROR: Quest file not found: {quest_file}")
        return

    if not quest_file.is_file():
        print(f"ERROR: Not a file: {quest_file}")
        return

    # Ensure output directory exists
    output_dir.mkdir(parents=True, exist_ok=True)

    # Load and parse quest YAML
    try:
        with open(quest_file, 'r', encoding='utf-8') as f:
            quest_data = yaml.safe_load(f)
    except Exception as e:
        print(f"ERROR: Failed to parse YAML file: {e}")
        return

    # Validate quest structure
    if not _validate_quest_structure(quest_data):
        return

    # Generate migration
    migration_data = _generate_migration(quest_data, quest_file)

    # Write migration file
    migration_file = _generate_migration_filename(quest_data, output_dir)

    if migration_file.exists() and not args.force:
        print(f"ERROR: Migration file already exists: {migration_file}")
        print("Use --force to overwrite")
        return

    try:
        with open(migration_file, 'w', encoding='utf-8') as f:
            yaml.safe_dump(migration_data, f, default_flow_style=False, allow_unicode=True, indent=2)

        print(f"SUCCESS: Generated migration file: {migration_file}")

    except Exception as e:
        print(f"ERROR: Failed to write migration file: {e}")
        return


def _validate_quest_structure(quest_data: Dict[str, Any]) -> bool:
    """Validate that quest YAML has required structure"""
    required_fields = ['metadata', 'quest_definition']

    for field in required_fields:
        if field not in quest_data:
            print(f"ERROR: Missing required field: {field}")
            return False

    metadata = quest_data.get('metadata', {})
    quest_def = quest_data.get('quest_definition', {})

    if 'id' not in metadata:
        print("ERROR: Missing metadata.id")
        return False

    if 'title' not in metadata:
        print("ERROR: Missing metadata.title")
        return False

    if 'level_min' not in quest_def:
        print("ERROR: Missing quest_definition.level_min")
        return False

    if 'level_max' not in quest_def:
        print("ERROR: Missing quest_definition.level_max")
        return False

    if 'rewards' not in quest_def:
        print("ERROR: Missing quest_definition.rewards")
        return False

    if 'objectives' not in quest_def:
        print("ERROR: Missing quest_definition.objectives")
        return False

    return True


def _generate_migration(quest_data: Dict[str, Any], quest_file: Path) -> Dict[str, Any]:
    """Generate Liquibase migration data from quest YAML"""

    metadata = quest_data['metadata']
    quest_def = quest_data['quest_definition']

    # Generate unique ID
    quest_id = str(uuid.uuid4())[:16]  # 16-character ID

    # Prepare metadata JSON
    project_root = Path(__file__).parent.parent
    metadata_json = {
        'id': metadata['id'],
        'version': metadata.get('version', '2.0.0'),
        'source_file': str(quest_file.relative_to(project_root))
    }

    # Prepare rewards JSON
    rewards = quest_def.get('rewards', {})
    rewards_json = {}

    if 'experience' in rewards:
        rewards_json['xp'] = rewards['experience']
    if 'money' in rewards:
        rewards_json.update(rewards['money'])
    if 'reputation' in rewards:
        rewards_json['reputation'] = rewards['reputation']
    if 'unlocks' in rewards:
        rewards_json['unlocks'] = rewards['unlocks']

    # Prepare objectives JSON
    objectives = quest_def.get('objectives', [])
    objectives_json = []

    for obj in objectives:
        if isinstance(obj, dict):
            objectives_json.append(obj.get('description', str(obj)))
        else:
            objectives_json.append(str(obj))

    # Generate changeset ID
    content_hash = hashlib.md5(str(quest_data).encode()).hexdigest()[:12]
    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
    changeset_id = f"quests-{metadata['id'].replace('_', '-')}-{content_hash}"

    # Build migration structure
    migration = {
        'databaseChangeLog': [{
            'changeSet': {
                'id': changeset_id,
                'author': 'quest-import-script',
                'changes': [{
                    'insert': {
                        'tableName': 'gameplay.quest_definitions',
                        'columns': [
                            {
                                'column': {
                                    'name': 'id',
                                    'value': quest_id
                                }
                            },
                            {
                                'column': {
                                    'name': 'metadata',
                                    'value': json.dumps(metadata_json, ensure_ascii=False)
                                }
                            },
                            {
                                'column': {
                                    'name': 'quest_id',
                                    'value': metadata['id']
                                }
                            },
                            {
                                'column': {
                                    'name': 'title',
                                    'value': metadata['title']
                                }
                            },
                            {
                                'column': {
                                    'name': 'description',
                                    'value': quest_def.get('description', metadata.get('title', ''))
                                }
                            },
                            {
                                'column': {
                                    'name': 'status',
                                    'value': 'active'
                                }
                            },
                            {
                                'column': {
                                    'name': 'level_min',
                                    'value': quest_def['level_min']
                                }
                            },
                            {
                                'column': {
                                    'name': 'level_max',
                                    'value': quest_def['level_max']
                                }
                            },
                            {
                                'column': {
                                    'name': 'rewards',
                                    'value': json.dumps(rewards_json, ensure_ascii=False)
                                }
                            },
                            {
                                'column': {
                                    'name': 'objectives',
                                    'value': json.dumps(objectives_json, ensure_ascii=False)
                                }
                            }
                        ]
                    }
                }]
            }
        }]
    }

    return migration


def _generate_migration_filename(quest_data: Dict[str, Any], output_dir: Path) -> Path:
    """Generate unique filename for migration"""
    metadata = quest_data['metadata']
    quest_id = metadata['id']

    # Create readable filename
    safe_name = quest_id.replace('/', '-').replace('_', '-')
    content_hash = hashlib.md5(str(quest_data).encode()).hexdigest()[:12]
    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')

    filename = f"data_quests_{safe_name}_{content_hash}_{timestamp}.yaml"
    return output_dir / filename


if __name__ == '__main__':
    main()
