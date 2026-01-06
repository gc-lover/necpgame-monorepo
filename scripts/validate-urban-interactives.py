#!/usr/bin/env python3
"""
QA Script: Validate Urban Interactive Objects Data Structure
Tests YAML structure, required fields, and data integrity.
Issue: #1870 - QA Testing
"""

import yaml
import json
from pathlib import Path
from typing import Dict, Any, List

def validate_yaml_structure(yaml_path: str) -> Dict[str, Any]:
    """Validate YAML file structure and return data"""
    try:
        with open(yaml_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        if not isinstance(data, dict):
            raise ValueError("YAML root must be a dictionary")

        # Validate metadata
        if 'metadata' not in data:
            raise ValueError("Missing 'metadata' section")

        metadata = data['metadata']
        required_meta = ['id', 'title', 'document_type', 'category', 'status', 'version']
        for field in required_meta:
            if field not in metadata:
                raise ValueError(f"Missing required metadata field: {field}")

        # Validate content
        if 'content' not in data:
            raise ValueError("Missing 'content' section")

        content = data['content']
        if not isinstance(content, list):
            raise ValueError("'content' must be a list")

        return data

    except Exception as e:
        print(f"[ERROR] YAML validation failed for {yaml_path}: {e}")
        return None

def validate_interactive_object(obj: Dict[str, Any], index: int) -> List[str]:
    """Validate a single interactive object"""
    errors = []

    # Required fields
    required_fields = ['id', 'name', 'category', 'description', 'era', 'threat_level']
    for field in required_fields:
        if field not in obj:
            errors.append(f"Object {index}: Missing required field '{field}'")

    # Validate category
    if 'category' in obj:
        valid_categories = [
            'urban_data_access', 'urban_information', 'urban_security',
            'urban_mobility', 'urban_navigation', 'urban_surveillance'
        ]
        if obj['category'] not in valid_categories:
            errors.append(f"Object {index}: Invalid category '{obj['category']}'")

    # Validate threat level
    if 'threat_level' in obj:
        valid_threats = ['utility', 'strategic', 'tactical']
        if obj['threat_level'] not in valid_threats:
            errors.append(f"Object {index}: Invalid threat_level '{obj['threat_level']}'")

    # Validate JSON fields
    json_fields = ['interaction_data', 'effects_data', 'telemetry_data', 'visual_data', 'audio_data', 'balance_data']
    for field in json_fields:
        if field in obj:
            try:
                if isinstance(obj[field], str):
                    json.loads(obj[field])
                elif isinstance(obj[field], (dict, list)):
                    json.dumps(obj[field])  # Validate serializable
            except Exception as e:
                errors.append(f"Object {index}: Invalid JSON in '{field}': {e}")

    return errors

def validate_migration_yaml(migration_path: str) -> Dict[str, Any]:
    """Validate migration YAML file structure"""
    try:
        with open(migration_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        if 'databaseChangeLog' not in data:
            raise ValueError("Missing 'databaseChangeLog' section")

        changelog = data['databaseChangeLog']
        if not isinstance(changelog, list) or len(changelog) == 0:
            raise ValueError("'databaseChangeLog' must be non-empty list")

        changeset = changelog[0]['changeSet']
        if 'changes' not in changeset:
            raise ValueError("Missing 'changes' in changeSet")

        return data

    except Exception as e:
        print(f"[ERROR] Migration validation failed for {migration_path}: {e}")
        return None

def main():
    """Main QA validation function"""
    print("QA Testing: Urban Interactive Objects Import and Gameplay Integration")
    print("=" * 70)

    project_root = Path(__file__).parent.parent

    # Test 1: Validate source YAML file
    print("\n1️⃣  Testing Source YAML Structure...")
    yaml_path = project_root / 'knowledge' / 'canon' / 'interactive-objects' / 'urban-interactive-objects-2020-2093.yaml'

    if not yaml_path.exists():
        print(f"[ERROR] Source YAML file not found: {yaml_path}")
        return False

    yaml_data = validate_yaml_structure(str(yaml_path))
    if not yaml_data:
        return False

    print(f"[OK] Source YAML structure validated: {len(yaml_data['content'])} objects")

    # Test 2: Validate individual objects
    print("\n2️⃣  Testing Individual Object Validation...")
    content = yaml_data['content']
    total_errors = []

    expected_categories = ['urban_data_access', 'urban_information', 'urban_security',
                          'urban_mobility', 'urban_navigation', 'urban_surveillance']

    found_categories = set()
    for i, obj in enumerate(content):
        errors = validate_interactive_object(obj, i)
        total_errors.extend(errors)
        if 'category' in obj:
            found_categories.add(obj['category'])

    if total_errors:
        print(f"[ERROR] Found {len(total_errors)} validation errors:")
        for error in total_errors[:10]:  # Show first 10 errors
            print(f"  - {error}")
        if len(total_errors) > 10:
            print(f"  ... and {len(total_errors) - 10} more errors")
        return False

    print(f"[OK] All {len(content)} objects validated successfully")
    print(f"[OK] Found categories: {sorted(found_categories)}")

    # Test 3: Validate migration file
    print("\n3️⃣  Testing Migration File Structure...")
    migration_path = project_root / 'infrastructure' / 'liquibase' / 'migrations' / 'knowledge' / 'interactives' / 'data_interactives_urban-interactive-objects-2020-2093.yaml'

    if not migration_path.exists():
        print(f"[ERROR] Migration file not found: {migration_path}")
        return False

    migration_data = validate_migration_yaml(str(migration_path))
    if not migration_data:
        return False

    print("[OK] Migration file structure validated")

    # Test 4: Validate import script
    print("\n4️⃣  Testing Import Script Availability...")
    import_script = project_root / 'scripts' / 'migrations' / 'apply_interactive_migrations.py'

    if not import_script.exists():
        print(f"[ERROR] Import script not found: {import_script}")
        return False

    print("[OK] Import script found and accessible")

    # Test 5: Check for required urban objects
    print("\n5️⃣  Testing Required Urban Objects...")
    expected_objects = [
        'street_terminal', 'ar_billboard', 'access_door',
        'delivery_drone', 'garbage_chute', 'security_camera'
    ]

    found_objects = [obj.get('id', '') for obj in content]
    missing_objects = [obj for obj in expected_objects if obj not in found_objects]

    if missing_objects:
        print(f"[ERROR] Missing required urban objects: {missing_objects}")
        return False

    print(f"[OK] All {len(expected_objects)} required urban objects found")

    # Final validation
    print("\n🎯 FINAL VALIDATION RESULTS:")
    print("=" * 50)
    print("✅ Source YAML structure: VALID")
    print("✅ Individual objects: VALID")
    print("✅ Migration file: VALID")
    print("✅ Import script: AVAILABLE")
    print("✅ Required objects: PRESENT")
    print(f"✅ Total objects: {len(content)}")
    print(f"✅ Categories covered: {len(found_categories)}/6")

    print("\n🏆 QA VALIDATION: PASSED - Urban Interactive Objects Ready for Import")
    print("\n📋 Next Steps:")
    print("1. Start PostgreSQL database")
    print("2. Run: python scripts/migrations/apply_interactive_migrations.py")
    print("3. Verify data in database")
    print("4. Test API endpoints (if service running)")

    return True

if __name__ == '__main__':
    success = main()
    exit(0 if success else 1)
