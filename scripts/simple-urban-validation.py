#!/usr/bin/env python3
"""
Simple QA validation for Urban Interactive Objects
"""

import yaml
import os
from pathlib import Path

def main():
    print("QA Testing: Urban Interactive Objects")
    print("=" * 40)

    # Check source file
    yaml_path = Path("knowledge/canon/interactive-objects/urban-interactive-objects-2020-2093.yaml")
    print(f"Checking YAML file: {yaml_path}")

    if not yaml_path.exists():
        print("ERROR: YAML file not found")
        return False

    try:
        with open(yaml_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
        print("OK: YAML file loaded successfully")
    except Exception as e:
        print(f"ERROR: Failed to load YAML: {e}")
        return False

    # Check content
    if 'content' not in data:
        print("ERROR: No content section in YAML")
        return False

    if 'sections' not in data['content']:
        print("ERROR: No sections in content")
        return False

    objects = data['content']['sections']
    print(f"OK: Found {len(objects)} interactive objects")

    # Check expected objects
    expected_ids = ['street_terminal', 'ar_billboard', 'access_door', 'delivery_drone', 'garbage_chute', 'security_camera']
    found_ids = [obj.get('id') for obj in objects if 'id' in obj]

    missing = [eid for eid in expected_ids if eid not in found_ids]
    if missing:
        print(f"ERROR: Missing objects: {missing}")
        return False

    print("OK: All expected objects found")

    # Check migration file
    migration_path = Path("infrastructure/liquibase/migrations/knowledge/interactives/data_interactives_urban-interactive-objects-2020-2093.yaml")
    print(f"Checking migration file: {migration_path}")

    if not migration_path.exists():
        print("ERROR: Migration file not found")
        return False

    try:
        with open(migration_path, 'r', encoding='utf-8') as f:
            migration_data = yaml.safe_load(f)
        print("OK: Migration file loaded successfully")
    except Exception as e:
        print(f"ERROR: Failed to load migration: {e}")
        return False

    # Check import script
    script_path = Path("scripts/migrations/apply_interactive_migrations.py")
    if script_path.exists():
        print("OK: Import script exists")
    else:
        print("ERROR: Import script not found")
        return False

    print("\nQA VALIDATION: PASSED")
    print("Urban Interactive Objects ready for import")
    return True

if __name__ == '__main__':
    success = main()
    exit(0 if success else 1)
