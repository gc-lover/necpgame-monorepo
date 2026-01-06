#!/usr/bin/env python3
"""
Fix Seattle migration SQL to match quest_definitions table schema
Issue: #2273
"""

import re
from pathlib import Path

def fix_migration():
    """Fix the migration SQL to match the quest_definitions table schema"""

    migration_file = Path("infrastructure/liquibase/migrations/data/quests/V005__import_remaining_seattle_2020_2029_quests.sql")

    if not migration_file.exists():
        print(f"[ERROR] Migration file not found: {migration_file}")
        return

    print(f"[READING] {migration_file}")

    with open(migration_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Remove id column from INSERT statements since it's auto-generated UUID
    # Remove created_at, updated_at since they have defaults
    pattern = r"INSERT INTO gameplay\.quest_definitions \(\s*id, quest_id, title, description, category, difficulty, level_requirement,\s*rewards, objectives, location, is_active, created_at, updated_at\s*\) VALUES \(\s*'[^']*',\s*([^;]+)\);"

    def replace_match(match):
        # Extract everything after the id value (first quoted string)
        rest = match.group(1)
        # Remove trailing created_at, updated_at values (last 2 CURRENT_TIMESTAMP entries)
        # Find the last two CURRENT_TIMESTAMP entries and remove them
        parts = rest.rstrip().rstrip(',').split(',')
        if len(parts) >= 2:
            # Remove the last two CURRENT_TIMESTAMP entries
            parts = parts[:-2]
        rest = ','.join(parts)

        return f"INSERT INTO gameplay.quest_definitions (\n    quest_id, title, description, category, difficulty, level_requirement,\n    rewards, objectives, location, is_active\n) VALUES (\n    {rest}\n);"

    new_content = re.sub(pattern, replace_match, content, flags=re.MULTILINE | re.DOTALL)

    # Also handle cases where there might be different formatting
    # Write back the fixed content
    with open(migration_file, 'w', encoding='utf-8') as f:
        f.write(new_content)

    print(f"[SUCCESS] Fixed migration file saved to {migration_file}")
    print("[CONTENT PREVIEW]:")
    lines = new_content.split('\n')[:20]  # Show first 20 lines
    for line in lines:
        print(line)

if __name__ == "__main__":
    fix_migration()


