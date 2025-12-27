#!/usr/bin/env python3
"""
Fix Liquibase migration transactions
Adds missing BEGIN/COMMIT and Issue references to migration files
"""
import os
from pathlib import Path


def fix_migration_file(file_path):
    """Fix a single migration file"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Skip if already has BEGIN/COMMIT
    if 'BEGIN;' in content and 'COMMIT;' in content:
        return False

    # Add BEGIN after the first comment block
    lines = content.split('\n')
    insert_index = 0

    # Find where to insert BEGIN (after --changeset or first comment)
    for i, line in enumerate(lines):
        if line.startswith('--changeset') or (line.startswith('--comment:') and 'BEGIN;' not in content):
            insert_index = i + 1
            break
        elif line.strip() and not line.startswith('--'):
            # Found first non-comment line, insert before it
            insert_index = i
            break

    # Add BEGIN
    if insert_index > 0 and 'BEGIN;' not in content:
        lines.insert(insert_index, '')
        lines.insert(insert_index + 1, 'BEGIN;')
        insert_index += 2

    # Add COMMIT at the end
    if 'COMMIT;' not in content:
        # Remove trailing empty lines
        while lines and lines[-1].strip() == '':
            lines.pop()

        lines.append('')
        lines.append('COMMIT;')

    # Write back
    new_content = '\n'.join(lines)
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)

    return True


def main():
    """Fix all migration files"""
    migrations_dir = Path("infrastructure/liquibase/schema")
    if not migrations_dir.exists():
        print(f"[ERROR] Migrations directory not found: {migrations_dir}")
        return

    print(f"[SEARCH] Fixing migrations in {migrations_dir}")

    fixed_count = 0
    total_count = 0

    for file_path in migrations_dir.glob("*.sql"):
        total_count += 1
        if fix_migration_file(file_path):
            print(f"[OK] Fixed: {file_path.name}")
            fixed_count += 1
        else:
            print(f"[SKIP] Already fixed: {file_path.name}")

    print("\n[SYMBOL] SUMMARY")
    print(f"Total files: {total_count}")
    print(f"Fixed files: {fixed_count}")


if __name__ == "__main__":
    main()
