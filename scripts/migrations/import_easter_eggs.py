#!/usr/bin/env python3
"""
Import Cyberspace Easter Eggs from YAML to database
Creates Liquibase data migration with INSERT statements
"""

import json
import os
import sys
from pathlib import Path
from datetime import datetime

# Add scripts directory to Python path
scripts_dir = Path(__file__).parent.parent
sys.path.insert(0, str(scripts_dir))

def load_yaml_file(file_path):
    """Load YAML file and return data"""
    try:
        import yaml

        # Read the file and extract only the easter_eggs section
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Find the easter_eggs section
        lines = content.split('\n')
        easter_eggs_start = -1
        for i, line in enumerate(lines):
            if line.strip() == 'easter_eggs:':
                easter_eggs_start = i
                break

        if easter_eggs_start == -1:
            print("ERROR: Could not find 'easter_eggs:' section in YAML file")
            sys.exit(1)

        # Extract only the easter_eggs section
        easter_eggs_content = '\n'.join(lines[easter_eggs_start:])
        easter_eggs_content = 'easter_eggs:\n' + '\n'.join(lines[easter_eggs_start + 1:])

        return yaml.safe_load(easter_eggs_content)
    except ImportError:
        print("ERROR: PyYAML not installed. Run: pip install PyYAML")
        sys.exit(1)
    except Exception as e:
        print(f"ERROR: Failed to load YAML file {file_path}: {e}")
        sys.exit(1)

def generate_liquibase_migration(easter_eggs_data, output_file):
    """Generate Liquibase data migration file"""

    migration_content = f"""-- Issue: #2262 - Import Cyberspace Easter Eggs Data
-- Auto-generated from knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml
-- liquibase formatted sql

--changeset backend:easter-eggs-data-import dbms:postgresql
--comment: Import 25 cyberspace easter eggs with full metadata and discovery mechanics

BEGIN;
"""

    # Import easter eggs
    for egg in easter_eggs_data.get('easter_eggs', []):
        if not isinstance(egg, dict) or 'id' not in egg:
            continue

        # Prepare JSON fields
        location_json = json.dumps(egg.get('location', {}), ensure_ascii=False)
        discovery_method_json = json.dumps(egg.get('discovery_method', {}), ensure_ascii=False)
        rewards_json = json.dumps(egg.get('rewards', []), ensure_ascii=False)
        lore_connections_json = json.dumps(egg.get('lore_connections', []), ensure_ascii=False)

        migration_content += f"""
INSERT INTO easter_eggs (
    id, name, category, difficulty, description, content,
    location, discovery_method, rewards, lore_connections,
    status, created_at, updated_at
) VALUES (
    '{egg['id']}',
    '{egg['name'].replace("'", "''")}',
    '{egg.get('category', 'technological')}',
    '{egg.get('difficulty', 'medium')}',
    '{egg.get('description', '').replace("'", "''")}',
    '{egg.get('content', '').replace("'", "''")}',
    '{location_json}'::jsonb,
    '{discovery_method_json}'::jsonb,
    '{rewards_json}'::jsonb,
    '{lore_connections_json}'::jsonb,
    'active',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (id) DO UPDATE SET
    name = EXCLUDED.name,
    category = EXCLUDED.category,
    difficulty = EXCLUDED.difficulty,
    description = EXCLUDED.description,
    content = EXCLUDED.content,
    location = EXCLUDED.location,
    discovery_method = EXCLUDED.discovery_method,
    rewards = EXCLUDED.rewards,
    lore_connections = EXCLUDED.lore_connections,
    updated_at = CURRENT_TIMESTAMP;
"""

    migration_content += "\nCOMMIT;\n"

    # Write migration file
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(migration_content)

    print(f"SUCCESS: Generated migration file: {output_file}")
    return True

def main():
    # Input YAML file
    yaml_file = Path("knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml")

    if not yaml_file.exists():
        print(f"ERROR: YAML file not found: {yaml_file}")
        sys.exit(1)

    # Output migration file
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    migration_file = Path(f"infrastructure/liquibase/migrations/data/V1_72__easter_eggs_data_import_{timestamp}.sql")

    # Load YAML data
    print(f"Loading YAML file: {yaml_file}")
    data = load_yaml_file(yaml_file)

    # Generate migration
    print("Generating Liquibase migration...")
    success = generate_liquibase_migration(data, migration_file)

    if success:
        print(f"✅ Migration generated successfully: {migration_file}")
        print("Run the following to apply:")
        print(f"  python scripts/migrations/apply-migrations.py --env dev")
    else:
        print("❌ Failed to generate migration")
        sys.exit(1)

if __name__ == '__main__':
    main()
