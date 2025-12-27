#!/usr/bin/env python3
"""
Enhanced quest import script that supports both quest_definition and content structures.
Based on the SOLID principles from the migrations generators.
"""

import json
import uuid
import yaml
from pathlib import Path
from typing import Dict, Any
from datetime import datetime


def main():
    import argparse

    parser = argparse.ArgumentParser(description='Import quest YAML to Liquibase migration')
    parser.add_argument('--quest-file', '-f', type=str, required=True, help='Path to quest YAML file')
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
    """Validate that quest YAML has required structure (supports both formats)"""
    if 'metadata' not in quest_data:
        print("ERROR: Missing required field: metadata")
        return False

    metadata = quest_data.get('metadata', {})

    if 'id' not in metadata:
        print("ERROR: Missing metadata.id")
        return False

    if 'title' not in metadata:
        print("ERROR: Missing metadata.title")
        return False

    # Check if we have quest_definition, content, or gameplay structure
    has_quest_def = 'quest_definition' in quest_data
    has_content = 'content' in quest_data
    has_gameplay = 'gameplay' in quest_data

    if not has_quest_def and not has_content and not has_gameplay:
        print("ERROR: Missing required field: quest_definition, content, or gameplay")
        return False

    return True


def _generate_migration(quest_data: Dict[str, Any], quest_file: Path) -> Dict[str, Any]:
    """Generate Liquibase migration data from quest YAML (supports multiple formats)"""

    metadata = quest_data['metadata']
    content = quest_data.get('content', {})
    summary = quest_data.get('summary', {})
    gameplay = quest_data.get('gameplay', {})
    narrative = quest_data.get('narrative', {})

    # Handle multiple formats: quest_definition, content, or gameplay
    quest_def = quest_data.get('quest_definition', {})
    if not quest_def and gameplay:
        # Extract from gameplay structure
        rewards = gameplay.get('rewards', {})
        quest_def = {
            'level_min': 1,  # Default if not specified
            'level_max': None,
            'rewards': rewards,
            'objectives': narrative.get('objectives', gameplay.get('objectives', ['Complete quest']))
        }
    elif not quest_def and content:
        # Extract from content structure - use default values
        quest_def = {
            'level_min': None,
            'level_max': None,
            'rewards': {
                'xp': 1500,
                'currency': {'amount': 500, 'type': 'eddies'}
            },
            'objectives': [
                'Complete the quest objectives'
            ]
        }
    elif not quest_def:
        # No quest_definition and no content - use minimal defaults
        quest_def = {
            'level_min': 1,
            'level_max': None,
            'rewards': {'xp': 1000},
            'objectives': ['Complete quest']
        }

    # Generate unique ID
    quest_id = str(uuid.uuid4())[:16]  # 16-character ID

    # Prepare metadata JSON
    project_root = Path(__file__).parent.parent
    absolute_quest_file = quest_file.resolve()
    metadata_json = {
        'id': metadata['id'],
        'version': metadata.get('version', '2.0.0'),
        'source_file': str(absolute_quest_file.relative_to(project_root))
    }

    # Prepare rewards JSON
    rewards = quest_def.get('rewards', {})
    rewards_json = {}

    if 'experience' in rewards:
        rewards_json['xp'] = rewards['experience']
    elif 'xp' in rewards:
        rewards_json['xp'] = rewards['xp']
    
    if 'money' in rewards:
        if isinstance(rewards['money'], dict):
            rewards_json.update(rewards['money'])
        else:
            rewards_json['money'] = rewards['money']
    
    if 'reputation' in rewards:
        rewards_json['reputation'] = rewards['reputation']
    if 'unlocks' in rewards:
        rewards_json['unlocks'] = rewards['unlocks']
    if 'currency' in rewards:
        if isinstance(rewards['currency'], dict):
            rewards_json.update(rewards['currency'])
        else:
            rewards_json['currency'] = rewards['currency']

    # If rewards is still empty, use defaults
    if not rewards_json:
        rewards_json = {'xp': 1000}

    # Prepare objectives JSON
    objectives = quest_def.get('objectives', [])
    if not objectives and narrative:
        objectives = narrative.get('objectives', [])
    
    objectives_json = []
    for obj in objectives:
        if isinstance(obj, dict):
            objectives_json.append(obj.get('description', str(obj)))
        else:
            objectives_json.append(str(obj))
    
    # If still empty, use default
    if not objectives_json:
        objectives_json = ['Complete quest']

    # Generate changeset ID
    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
    changeset_id = f"data_quests_{metadata['id'].replace('canon-quest-', '').replace('-', '_')}_{quest_id}_{timestamp}"

    # Build migration structure
    migration_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'author': 'quest-import-script',
                    'changes': [
                        {
                            'insert': {
                                'columns': [
                                    {'column': {'name': 'id', 'value': quest_id}},
                                    {'column': {'name': 'quest_id', 'value': metadata['id']}},
                                    {'column': {'name': 'title', 'value': metadata['title']}},
                                    {'column': {'name': 'description', 'value': narrative.get('hook', summary.get('essence', metadata.get('title', '')))}},
                                    {'column': {'name': 'status', 'value': 'active'}},
                                    {'column': {'name': 'level_min', 'value': quest_def.get('level_min')}},
                                    {'column': {'name': 'level_max', 'value': quest_def.get('level_max')}},
                                    {'column': {'name': 'rewards', 'value': json.dumps(rewards_json, ensure_ascii=False)}},
                                    {'column': {'name': 'objectives', 'value': json.dumps(objectives_json, ensure_ascii=False)}},
                                    {'column': {'name': 'metadata', 'value': json.dumps(metadata_json, ensure_ascii=False)}}
                                ],
                                'tableName': 'gameplay.quest_definitions'
                            }
                        }
                    ],
                    'id': changeset_id
                }
            }
        ]
    }

    return migration_data


def _generate_migration_filename(quest_data: Dict[str, Any], output_dir: Path) -> Path:
    """Generate migration filename"""
    metadata = quest_data['metadata']
    quest_id = metadata['id'].replace('canon-quest-', '').replace('-', '_')
    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
    hash_suffix = str(uuid.uuid4())[:8]

    filename = f"data_quests_{quest_id}_{hash_suffix}_{timestamp}.yaml"
    return output_dir / filename


if __name__ == '__main__':
    main()

