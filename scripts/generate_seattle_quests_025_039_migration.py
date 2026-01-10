#!/usr/bin/env python3
"""
Generate migration SQL for Seattle quests 025-039
Issue: #2273
"""

import os
import yaml
import json
from pathlib import Path

def generate_migration():
    """Generate SQL migration for quests 025-039"""

    base_path = Path("knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029")
    migration_file = Path("infrastructure/liquibase/migrations/data/quests/V007__import_seattle_quests_025_039.sql")

    sql_parts = []
    sql_parts.append("-- Import Seattle quests 025-039 to quest_definitions table")
    sql_parts.append("-- Generated for Backend import task #2273")
    sql_parts.append("")

    processed_count = 0

    for quest_num in range(25, 40):  # 025-039
        quest_pattern = f"quest-{quest_num:03d}-*.yaml"
        quest_files = list(base_path.glob(quest_pattern))

        if not quest_files:
            print(f"[WARNING] No file found for quest {quest_num:03d}")
            continue

        quest_file = quest_files[0]
        print(f"[PROCESSING] {quest_file.name}")

        try:
            with open(quest_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            # Extract quest data
            quest_def = data.get('quest_definition', {})
            metadata = data.get('metadata', {})

            quest_id = f"quest-{quest_num:03d}-{quest_file.stem.split('-', 2)[-1]}"
            title = metadata.get('title', f'Quest {quest_num:03d}')
            # Use ASCII-safe description to avoid Unicode issues
            description = f"Quest {quest_num:03d} description - {quest_file.stem.replace('-', ' ').title()}"

            # Map difficulty
            difficulty_map = {
                'very_easy': 'easy',
                'easy': 'easy',
                'medium': 'medium',
                'hard': 'hard',
                'very_hard': 'extreme',
                'extreme': 'extreme'
            }
            difficulty = difficulty_map.get(quest_def.get('difficulty', 'medium'), 'medium')

            level_min = quest_def.get('level_min', 1) or 1
            level_max = quest_def.get('level_max') or 'null'

            # Build rewards JSON
            rewards = {}
            rewards_data = quest_def.get('rewards', {})

            if 'experience' in rewards_data:
                rewards['experience'] = rewards_data['experience']
            if 'money' in rewards_data:
                rewards['currency'] = {'type': 'eddies', 'amount': abs(rewards_data['money'])}
            if 'reputation' in rewards_data:
                rewards['reputation'] = rewards_data['reputation']
            if 'items' in rewards_data:
                rewards['items'] = rewards_data['items']

            # Escape single quotes and handle Unicode properly
            rewards_json = json.dumps(rewards, ensure_ascii=False).replace("'", "''") if rewards else '{}'

            # Build objectives JSON
            objectives = []
            for obj in quest_def.get('objectives', []):
                obj_data = {
                    'id': obj.get('id', f'obj_{len(objectives)}'),
                    'text': obj.get('text', ''),
                    'type': obj.get('type', 'interact'),
                    'target': obj.get('target', ''),
                    'count': obj.get('count', 1)
                }
                if 'optional' in obj:
                    obj_data['optional'] = obj['optional']
                if 'skill' in obj:
                    obj_data['skill'] = obj['skill']
                    obj_data['difficulty'] = obj.get('difficulty', 0.5)
                objectives.append(obj_data)

            objectives_json = json.dumps(objectives, ensure_ascii=False).replace("'", "''") if objectives else '[]'

            # Location and other fields
            location = quest_def.get('location', 'Seattle')
            time_period = '2020-2029'
            quest_type = quest_def.get('quest_type', 'side')
            status = 'active'

            # Generate SQL
            sql = f"""-- Quest {quest_num:03d}: {title}
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    '{quest_id}',
    '{title.replace("'", "''")}',
    '{description.replace("'", "''")[:500] if description else f"Quest {quest_num:03d} description"}',
    '{difficulty}',
    {level_min},
    {level_max},
    '{rewards_json}',
    '{objectives_json}',
    '{location}',
    '{time_period}',
    '{quest_type}',
    '{status}'
);"""

            sql_parts.append(sql)
            sql_parts.append("")
            processed_count += 1

        except Exception as e:
            print(f"[ERROR] Failed to process {quest_file.name}: {e}")
            continue

    # Write migration file
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(sql_parts))

    print(f"[SUCCESS] Generated migration file: {migration_file}")
    print(f"[RESULT] Processed {processed_count} quests (025-039)")

if __name__ == "__main__":
    generate_migration()