#!/usr/bin/env python3
"""
Convert Tokyo quest YAML files to import format.

Issue: Convert all Tokyo quest definitions to database import format
"""

import yaml
from pathlib import Path


def convert_tokyo_quests():
    """Convert all Tokyo quest files to import format"""
    # Script can be run from project root or scripts directory
    current_dir = Path.cwd()

    # If we're in scripts directory, go up one level
    if current_dir.name == "scripts":
        base_dir = current_dir.parent
    else:
        base_dir = current_dir

    # Source directory
    source_dir = base_dir / "knowledge" / "canon" / "lore" / "timeline-author" / "quests" / "asia" / "tokyo"

    # Target directory
    target_dir = base_dir / "knowledge" / "canon" / "narrative" / "quests"
    target_dir.mkdir(parents=True, exist_ok=True)

    converted_count = 0
    failed_count = 0

    print(f"Looking in: {source_dir}")

    # Find all YAML files in Tokyo directory
    yaml_files = list(source_dir.rglob("*.yaml"))

    print(f"Found {len(yaml_files)} Tokyo quest files to convert")

    for yaml_file in yaml_files:
        if not yaml_file.is_file():
            continue

        try:
            print(f"\n[INFO] Converting {yaml_file.name}...")

            # Load quest data
            with open(yaml_file, 'r', encoding='utf-8') as f:
                quest_data = yaml.safe_load(f)

            # Convert to import format
            import_data = {
                'metadata': {
                    'id': yaml_file.stem,
                    'title': quest_data.get('title', yaml_file.stem.replace('-', ' ').title()),
                    'version': '1.0.0'
                },
                'quest_definition': {
                    'title': quest_data.get('title', yaml_file.stem.replace('-', ' ').title()),
                    'description': quest_data.get('description', ''),
                    'level_min': quest_data.get('level_min', 1),
                    'level_max': quest_data.get('level_max', 50),
                    'rewards': quest_data.get('rewards', {}),
                    'objectives': quest_data.get('objectives', [])
                }
            }

            # Write import file
            import_filename = f'{yaml_file.stem}_import.yaml'
            import_path = target_dir / import_filename

            with open(import_path, 'w', encoding='utf-8') as f:
                yaml.safe_dump(import_data, f, default_flow_style=False, allow_unicode=True, indent=2)

            print(f"[OK] Converted {yaml_file.name} -> {import_filename}")
            converted_count += 1

        except Exception as e:
            print(f"[ERROR] Failed to convert {yaml_file.name}: {e}")
            failed_count += 1

    print(f"\n[SUMMARY] Conversion completed:")
    print(f"  Successfully converted: {converted_count}")
    print(f"  Failed: {failed_count}")

    if failed_count > 0:
        print(f"\n[WARNING] {failed_count} files failed to convert. Check logs above for details.")
        return 1
    else:
        print("\n[SUCCESS] All Tokyo quests converted successfully!")
        return 0


if __name__ == "__main__":
    import sys
    sys.exit(convert_tokyo_quests())
