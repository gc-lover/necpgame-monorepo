#!/usr/bin/env python3
"""
Fix Seattle migration SQL to match actual quest_definitions table schema
Issue: #2273
"""

import re
from pathlib import Path

def fix_migration():
    """Fix the migration SQL to match the actual quest_definitions table schema"""

    migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_2020_2029_quests.sql")

    if not migration_file.exists():
        print(f"[ERROR] Migration file not found: {migration_file}")
        return

    print(f"[READING] {migration_file}")

    with open(migration_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Replace the column list and values for each INSERT
    # Old format: category, difficulty, level_requirement, location, is_active
    # New format: difficulty, level_min, level_max, rewards, objectives, location, time_period, quest_type, status

    # Pattern to match each INSERT block
    pattern = r"INSERT INTO gameplay\.quest_definitions \(\s*quest_id, title, description, category, difficulty, level_requirement,\s*rewards, objectives, location, is_active\s*\) VALUES \(\s*'([^']*)',\s*'([^']*)',\s*'([^']*)',\s*'([^']*)',\s*'([^']*)',\s*(\d+),\s*(\{[^}]*\}),\s*(\[[^\]]*\]),\s*'([^']*)',\s*(true|false)\s*\);"

    def replace_match(match):
        quest_id = match.group(1)
        title = match.group(2)
        description = match.group(3)
        category = match.group(4)  # This becomes quest_type
        difficulty = match.group(5)
        level_req = int(match.group(6))
        rewards = match.group(7)
        objectives = match.group(8)
        location = match.group(9)
        is_active = match.group(10)

        # Convert is_active to status
        status = 'active' if is_active == 'true' else 'inactive'

        # Set level range based on level_req
        level_min = level_req
        level_max = level_req + 4  # Add some range

        return f"""INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, difficulty, level_min, level_max,
    rewards, objectives, location, time_period, quest_type, status
) VALUES (
    '{quest_id}',
    '{title}',
    '{description}',
    '{difficulty}',
    {level_min},
    {level_max},
    '{rewards}',
    '{objectives}',
    '{location}',
    '2020-2029',
    '{category}',
    '{status}'
);"""

    new_content = re.sub(pattern, replace_match, content, flags=re.MULTILINE | re.DOTALL)

    # Write back the fixed content
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write(new_content)

    print(f"[SUCCESS] Fixed migration file saved to {migration_file}")
    print("[CONTENT PREVIEW]:")
    lines = new_content.split('\n')[4:25]  # Show lines 5-25
    for line in lines:
        print(line)

if __name__ == "__main__":
    fix_migration()


