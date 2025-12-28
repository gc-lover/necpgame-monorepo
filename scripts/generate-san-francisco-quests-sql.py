#!/usr/bin/env python3
"""
Generate SQL for New San Francisco 2020-2029 Quests
Generate SQL migration files for the 5 newly created San Francisco quests

Usage:
    python scripts/generate-san-francisco-quests-sql.py
"""

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
        'metadata': {
            'id': data['metadata']['id'],
            'version': data['metadata']['version'],
            'source_file': str(file_path)
        },
        'rewards': rewards,
        'objectives': db_objectives,
        'created_at': datetime.now().isoformat(),
        'updated_at': datetime.now().isoformat()
    }

def escape_sql_string(s):
    """Escape string for SQL"""
    return s.replace("'", "''")

def generate_sql_insert(quest_data):
    """Generate SQL INSERT statement for quest"""
    title = escape_sql_string(quest_data['title'])
    description = escape_sql_string(quest_data['description'])

    sql = f"""
INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives, created_at, updated_at
) VALUES (
    '{title}',
    '{description}',
    {quest_data['level_min']},
    {quest_data['level_max']},
    '{quest_data['status']}',
    '{json.dumps(quest_data['metadata'])}',
    '{json.dumps(quest_data['rewards'])}',
    '{json.dumps(quest_data['objectives'])}',
    '{quest_data['created_at']}',
    '{quest_data['updated_at']}'
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = EXCLUDED.updated_at;
"""
    return sql

def main():
    print("🚀 Generating SQL for new San Francisco 2020-2029 quests...")

    # Quest files to process
    quest_files = [
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml',
        'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml'
    ]

    # Generate SQL file
    sql_file = 'infrastructure/liquibase/migrations/data/quests/V001__import_new_san_francisco_quests.sql'

    # Ensure directory exists
    Path(sql_file).parent.mkdir(parents=True, exist_ok=True)

    with open(sql_file, 'w', encoding='utf-8') as f:
        f.write("-- Import New San Francisco 2020-2029 Quests\n")
        f.write(f"-- Generated on {datetime.now().isoformat()}\n")
        f.write("-- Issue: #2265\n\n")

        for file_path in quest_files:
            try:
                # Check if file exists
                if not Path(file_path).exists():
                    print(f"⚠️  File not found: {file_path}")
                    continue

                # Load quest data
                quest_data = load_quest_from_yaml(file_path)
                print(f"📖 Processed quest: {quest_data['title']}")

                # Generate SQL
                sql = generate_sql_insert(quest_data)
                f.write(f"-- Quest: {quest_data['metadata']['id']}\n")
                f.write(sql)
                f.write("\n")

            except Exception as e:
                print(f"❌ Error processing {file_path}: {e}")
                continue

    print(f"✅ SQL file generated: {sql_file}")
    print("🎉 Generation complete!")
    print("\n📊 Generated quests:")
    print("   - Crypto Blockchain Revolution")
    print("   - AI Ethics Crisis")
    print("   - Cyberspace Graffiti Wars")
    print("   - Biohacking Underground")
    print("   - Drone Wars San Francisco")
    print("\n💡 To apply: Run liquibase migration or execute the SQL file directly")

if __name__ == '__main__':
    main()
