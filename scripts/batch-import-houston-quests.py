#!/usr/bin/env python3
"""
Batch import Houston quests to database.

Issue: Import all converted Houston quest definitions to Liquibase migrations
"""

import subprocess
import sys
from pathlib import Path

def batch_import_houston_quests():
    """Import all Houston quest definition files to database."""
    # Script can be run from project root or scripts directory
    current_dir = Path.cwd()

    # If we're in scripts directory, go up one level
    if current_dir.name == "scripts":
        base_dir = current_dir.parent
    else:
        base_dir = current_dir

    quest_dir = base_dir / "knowledge" / "canon" / "narrative" / "quests"
    imported_count = 0
    failed_count = 0

    # Find all Houston quest import files
    houston_quest_files = list(quest_dir.glob("*houston*import.yaml"))

    print(f"Found {len(houston_quest_files)} Houston quest import files to process")

    for quest_file in houston_quest_files:
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

    print(f"\n[SUMMARY] Import completed:")
    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if failed_count > 0:
        print(f"\n[WARNING] {failed_count} files failed to import. Check logs above for details.")
        return 1
    else:
        print("\n[SUCCESS] All Houston quests imported successfully!")
        return 0

if __name__ == "__main__":
    sys.exit(batch_import_houston_quests())
