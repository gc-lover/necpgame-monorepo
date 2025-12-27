#!/usr/bin/env python3
"""
Batch import timeline-author quests to database using import-quest-to-db.py.

Imports all timeline-author quest files for specified cities.
"""

import subprocess
import sys
from pathlib import Path

def batch_import_timeline_author_quests(cities):
    """Import all timeline-author quest files for specified cities."""
    # Script can be run from project root or scripts directory
    current_dir = Path.cwd()

    # If we're in scripts directory, go up one level
    if current_dir.name == "scripts":
        base_dir = current_dir.parent
    else:
        base_dir = current_dir

    imported_count = 0
    failed_count = 0

    for city in cities:
        print(f"\n[INFO] Processing quests for city: {city}")

        # Find all quest files for this city in all subdirectories
        timeline_dir = base_dir / "knowledge" / "canon" / "lore" / "timeline-author" / "quests"
        quest_files = []

        if city in ["miami", "phoenix"]:
            # American cities
            city_dir = timeline_dir / "america" / city
            if city_dir.exists():
                quest_files = list(city_dir.rglob("*.yaml"))
        elif city == "tokyo":
            # Asian cities
            city_dir = timeline_dir / "asia" / city
            if city_dir.exists():
                quest_files = list(city_dir.rglob("*.yaml"))

        print(f"Found {len(quest_files)} quest files for {city}")

        for quest_file in quest_files:
            try:
                print(f"\n[INFO] Importing {quest_file.name}...")

                # Run import command
                cmd = [
                    sys.executable,
                    str(base_dir / "scripts" / "import-quest-to-db.py"),
                    "--quest-file", str(quest_file)
                ]

                result = subprocess.run(cmd, capture_output=True, text=True, cwd=base_dir)

                if result.returncode == 0:
                    print(f"[OK] Successfully imported {quest_file.name}")
                    imported_count += 1
                else:
                    print(f"[ERROR] Failed to import {quest_file.name}")
                    print(f"Error output: {result.stderr}")
                    failed_count += 1
            except Exception as e:
                print(f"[ERROR] Exception importing {quest_file.name}: {e}")
                failed_count += 1

    print("\n[SUMMARY] Import completed:")
    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if imported_count > 0:
        print("\n[SUCCESS] Timeline-author quests imported successfully!")
    else:
        print("\n[WARNING] No quests were imported. Check logs above for details.")

    return imported_count > 0


if __name__ == "__main__":
    # Cities to import
    cities = ["miami", "phoenix", "tokyo"]

    success = batch_import_timeline_author_quests(cities)
    sys.exit(0 if success else 1)
