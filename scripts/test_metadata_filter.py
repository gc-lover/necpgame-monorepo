#!/usr/bin/env python3
"""
Test script to check metadata filtering in knowledge/ directory.
"""

import yaml
from pathlib import Path

def check_file_metadata(file_path):
    """Check if file has proper metadata structure."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Check if it starts with metadata
        if not content.strip().startswith('metadata:'):
            return False, "No metadata section"

        # Try to parse as YAML
        spec = yaml.safe_load(content)
        if not isinstance(spec, dict):
            return False, "Not a dict structure"

        metadata = spec.get('metadata', {})
        if not metadata:
            return False, "No metadata key"

        if not metadata.get('id'):
            return False, "No id in metadata"

        return True, "Valid"

    except Exception as e:
        return False, f"Error: {e}"

def main():
    """Main test function."""
    knowledge_dir = Path("knowledge")

    total_files = 0
    valid_files = 0
    invalid_files = 0

    print("Checking metadata in knowledge/ directory...")
    print("=" * 60)

    for yaml_file in knowledge_dir.rglob("*.yaml"):
        total_files += 1
        is_valid, reason = check_file_metadata(yaml_file)

        if is_valid:
            valid_files += 1
        else:
            invalid_files += 1
            if invalid_files <= 10:  # Show first 10 invalid files
                print(f"INVALID: {yaml_file.relative_to(knowledge_dir)} - {reason}")

    print("=" * 60)
    print(f"Total YAML files: {total_files}")
    print(f"Valid files (with metadata): {valid_files}")
    print(f"Invalid files (no metadata): {invalid_files}")
    print(".1f")

if __name__ == '__main__':
    main()



