#!/usr/bin/env python3
"""
Import New San Francisco 2020-2029 Quests
Import the 5 newly created San Francisco quests into database

Usage:
    python scripts/import-new-san-francisco-quests.py
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

    # Extract quest data
    quest_def = data['quest_definition']
    objectives = quest_def['objectives']

    # Convert objectives to database format
    db_objectives = []
    for obj in objectives:
        db_obj = {
            'id': obj['id'],
            'title': obj['text'],
            'description': obj['text'],
            'type': obj['type'],
            'order': len(db_objectives) + 1
        }
        db_objectives.append(db_obj)

    # Extract rewards
    rewards = {}
    experience_total = 0
    currency_total = 0
    items = []

    for obj in objectives:
        if 'rewards' in obj:
            for reward in obj['rewards']:
                if reward['type'] == 'experience':
                    experience_total += reward['amount']
                elif reward['type'] == 'money':
                    currency_total += reward['amount']
                elif reward['type'] == 'item':
                    items.append({
                        'id': reward['id'],
                        'name': reward['name'],
                        'rarity': reward['rarity']
                    })

    rewards['experience'] = experience_total
    rewards['currency'] = {'type': 'eddies', 'amount': currency_total}
    if items:
        rewards['items'] = items

    return {
        'title': data['metadata']['title'],
        'description': data['summary']['problem'],
        'level_min': quest_def['level_min'],
        'level_max': quest_def['level_max'],
        'status': 'active',
        'metadata': json.dumps({
            'id': data['metadata']['id'],
            'version': data['metadata']['version'],
            'source_file': str(file_path)
        }),
        'rewards': json.dumps(rewards),
        'objectives': json.dumps(db_objectives),
        'created_at': datetime.now(),
        'updated_at': datetime.now()
    }

def main():
    print("🚀 Starting import of new San Francisco 2020-2029 quests...")

    # Connect to database
    try:
        conn = psycopg2.connect('postgresql://postgres:postgres@localhost:5432/necpgame')
        cursor = conn.cursor()
        print("✅ Connected to database")
    except Exception as e:
        print(f"❌ Database connection failed: {e}")
        return

    # Quest files to import
    quest_files = [
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml'
    ]

    imported_count = 0

    for file_path in quest_files:
        try:
            # Check if file exists
            if not Path(file_path).exists():
                print(f"⚠️  File not found: {file_path}")
                continue

            # Load quest data
            quest_data = load_quest_from_yaml(file_path)
            print(f"📖 Loaded quest: {quest_data['title']}")

            # Insert into database
            cursor.execute("""
                INSERT INTO gameplay.quests (
                    title, description, level_min, level_max, status,
                    metadata, rewards, objectives, created_at, updated_at
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                ON CONFLICT (metadata->>'id') DO UPDATE SET
                    title = EXCLUDED.title,
                    description = EXCLUDED.description,
                    level_min = EXCLUDED.level_min,
                    level_max = EXCLUDED.level_max,
                    rewards = EXCLUDED.rewards,
                    objectives = EXCLUDED.objectives,
                    updated_at = EXCLUDED.updated_at
            """, (
                quest_data['title'],
                quest_data['description'],
                quest_data['level_min'],
                quest_data['level_max'],
                quest_data['status'],
                quest_data['metadata'],
                quest_data['rewards'],
                quest_data['objectives'],
                quest_data['created_at'],
                quest_data['updated_at']
            ))

            imported_count += 1
            print(f"✅ Imported: {quest_data['title']}")

        except Exception as e:
            print(f"❌ Error importing {file_path}: {e}")
            continue

    # Commit changes
    conn.commit()
    cursor.close()
    conn.close()

    print(f"🎉 Import complete! {imported_count} quests imported successfully")
    print("\n📊 Summary:")
    print(f"   - Crypto Blockchain Revolution")
    print(f"   - AI Ethics Crisis")
    print(f"   - Cyberspace Graffiti Wars")
    print(f"   - Biohacking Underground")
    print(f"   - Drone Wars San Francisco")

if __name__ == '__main__':
    main()
