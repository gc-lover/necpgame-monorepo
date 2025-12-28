#!/usr/bin/env python3
"""
Import San Francisco 2020-2029 Quests
Direct database import for San Francisco quests from YAML files

Usage:
    python scripts/import-san-francisco-quests.py
"""

import psycopg2
import yaml
import json
from datetime import datetime
from pathlib import Path

def extract_quest_data(quest_file):
    """Extract quest data from YAML file"""
    with open(quest_file, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    metadata = data.get('metadata', {})
    quest_def = data.get('quest_definition', {})
    rewards = quest_def.get('rewards', {})
    objectives = quest_def.get('objectives', [])

    # Extract basic quest info
    quest_data = {
        'title': metadata.get('title', 'Unknown Quest'),
        'description': quest_def.get('description', 'No description available'),
        'status': 'active',
        'level_min': quest_def.get('level_min', 1),
        'level_max': quest_def.get('level_max', 100),
        'metadata': {
            'id': metadata.get('id', f"quest-{Path(quest_file).stem}"),
            'version': metadata.get('version', '1.0.0'),
            'source_file': str(quest_file).replace('knowledge/', ''),
            'category': metadata.get('category', 'quests'),
            'tags': metadata.get('tags', [])
        },
        'rewards': rewards,
        'objectives': [{'id': obj.get('id', f'obj_{i}'), 'text': obj.get('text', obj.get('id', 'Unknown objective'))} for i, obj in enumerate(objectives)],
        'created_at': datetime.now(),
        'updated_at': datetime.now()
    }

    return quest_data

def main():
    # Connect to database
    try:
        conn = psycopg2.connect('postgresql://postgres:postgres@localhost:5432/necpgame')
        cursor = conn.cursor()
        print("Database connection established")
    except Exception as e:
        print(f"Database connection failed: {e}")
        return

    # Quest files to import (only the new 2020-2029 quests)
    quest_files = [
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml'
    ]

    applied = 0
    for quest_file in quest_files:
        try:
            if not Path(quest_file).exists():
                print(f'Warning: Quest file not found: {quest_file}')
                continue

            quest_data = extract_quest_data(quest_file)

            cursor.execute('''
                INSERT INTO gameplay.quest_definitions
                (metadata, title, description, status, level_min, level_max, rewards, objectives, created_at, updated_at)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ''', (
                json.dumps(quest_data['metadata']),
                quest_data['title'],
                quest_data['description'],
                quest_data['status'],
                quest_data['level_min'],
                quest_data['level_max'],
                json.dumps(quest_data['rewards']),
                json.dumps(quest_data['objectives']),
                quest_data['created_at'],
                quest_data['updated_at']
            ))

            applied += 1
            print(f'Applied quest: {quest_data["title"][:60]}...')

        except Exception as e:
            print(f'Failed to apply quest {quest_file}: {e}')
            continue

    try:
        conn.commit()
        print(f'\nSuccessfully committed {applied} quests to database')
    except Exception as e:
        print(f'Commit failed: {e}')
        conn.rollback()

    cursor.close()
    conn.close()

    print(f'\nTotal quests imported: {applied}')

    if applied == 5:
        print('\nAll 5 San Francisco 2020-2029 quests successfully imported!')
        print('Imported quests:')
        print('1. Crypto-Blockchain Revolution (Lv.35-45)')
        print('2. AI Ethics Crisis (Lv.38-48)')
        print('3. Cyberspace Graffiti Wars (Lv.30-40)')
        print('4. Biohacking Underground (Lv.32-42)')
        print('5. Drone Wars San Francisco (Lv.28-38)')
    else:
        print(f'\nWarning: Only {applied}/5 quests imported successfully')

if __name__ == '__main__':
    main()
