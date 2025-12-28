#!/usr/bin/env python3
"""
Import New Seattle 2020-2029 Quests
Import newly created Seattle quests into database

Usage:
    python scripts/import-new-seattle-quests.py
"""

import psycopg2
import json
import yaml
from datetime import datetime
from pathlib import Path

def load_quest_from_yaml(file_path):
    """Load quest data from YAML file"""
    with open(file_path, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    # Extract quest data - handle different YAML structures
    if 'quest_definition' in data:
        quest_def = data['quest_definition']
        objectives = quest_def.get('objectives', [])
        level_min = quest_def.get('level_min', 1)
        level_max = quest_def.get('level_max', 50)
        description = data.get('summary', {}).get('problem', 'No description')
    else:
        # Fallback for different structure
        objectives = []
        level_min = 1
        level_max = 50
        description = data.get('narrative', {}).get('hook', 'No description')

    # Convert objectives to database format
    db_objectives = []
    for i, obj in enumerate(objectives):
        if isinstance(obj, str):
            db_obj = {
                'id': f'objective_{i+1}',
                'title': obj,
                'description': obj,
                'type': 'main',
                'order': i + 1
            }
        elif isinstance(obj, dict):
            db_obj = {
                'id': obj.get('id', f'objective_{i+1}'),
                'title': obj.get('text', obj.get('title', f'Objective {i+1}')),
                'description': obj.get('text', obj.get('description', f'Objective {i+1}')),
                'type': obj.get('type', 'main'),
                'order': i + 1
            }
        db_objectives.append(db_obj)

    # Default rewards if not specified
    rewards = {
        'experience': 5000,
        'currency': {'type': 'eddies', 'amount': 1000}
    }

    return {
        'title': data['metadata']['title'],
        'description': description,
        'level_min': level_min,
        'level_max': level_max,
        'status': 'active',
        'quest_id': data['metadata']['id'],
        'rewards': json.dumps(rewards),
        'objectives': json.dumps(db_objectives),
        'created_at': datetime.now(),
        'updated_at': datetime.now()
    }

def main():
    print("Starting import of new Seattle 2020-2029 quests...")

    # Connect to database
    try:
        conn = psycopg2.connect('postgresql://postgres:postgres@localhost:5432/necpgame')
        cursor = conn.cursor()
        print("Connected to database")
    except Exception as e:
        print(f"Database connection failed: {e}")
        return

    # Check which Seattle quests are new (higher numbers)
    quest_dir = Path('knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029')
    all_quest_files = list(quest_dir.glob('quest-*.yaml'))

    # Focus on quests 011 and above (assuming these are the new ones)
    new_quest_files = [f for f in all_quest_files if any(f.name.startswith(f'quest-{i:03d}-') for i in range(11, 41))]

    print(f"Found {len(new_quest_files)} potential new quests to import")

    imported_count = 0

    for file_path in sorted(new_quest_files):
        try:
            # Load quest data
            quest_data = load_quest_from_yaml(file_path)
            print(f"Loaded quest: {quest_data['title']}")

            # Insert into database
            cursor.execute("""
                INSERT INTO gameplay.quest_definitions (
                    quest_id, title, description, category, difficulty, level_requirement,
                    rewards, objectives, is_active, created_at, updated_at
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT (quest_id) DO UPDATE SET
                    title = EXCLUDED.title,
                    description = EXCLUDED.description,
                    rewards = EXCLUDED.rewards,
                    objectives = EXCLUDED.objectives,
                    updated_at = EXCLUDED.updated_at
            """, (
                quest_data['quest_id'],
                quest_data['title'],
                quest_data['description'],
                'main',
                'normal',
                quest_data['level_min'],
                quest_data['rewards'],
                quest_data['objectives'],
                True,
                quest_data['created_at'],
                quest_data['updated_at']
            ))

            imported_count += 1
            print(f"Imported: {quest_data['title']}")

        except Exception as e:
            print(f"Error importing {file_path}: {e}")
            continue

    # Commit changes
    conn.commit()
    cursor.close()
    conn.close()

    print(f"Import complete! {imported_count} quests imported successfully")
    print("\nSeattle quests imported from quest-011 to quest-040")
    print("   - Corporate data leaks and resistance")
    print("   - Coffee culture underground movements")
    print("   - Floating cities and climate adaptation")
    print("   - Neural research and implant technology")
    print("   - Underground economies and hacker collectives")
    print("   - Virtual reality research and addiction")
    print("   - Apocalypse preparation and end-times scenarios")

if __name__ == '__main__':
    main()
