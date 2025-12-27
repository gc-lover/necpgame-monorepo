#!/usr/bin/env python3
"""
Batch import Las Vegas quests to database.

Issue: Import all converted Las Vegas quest definitions to Liquibase migrations
"""

import subprocess
import sys
from pathlib import Path

def batch_import_las_vegas_quests():
    """Import all Las Vegas quest definition files to database."""
    quest_dir = Path("knowledge/canon/narrative/quests")
    imported_count = 0
    failed_count = 0

    # Find all Las Vegas quest import files
    las_vegas_quest_files = list(quest_dir.glob("*las-vegas*import.yaml"))

    print(f"Found {len(las_vegas_quest_files)} Las Vegas quest import files to process")

    for quest_file in las_vegas_quest_files:
        try:
            print(f"\n[INFO] Importing {quest_file.name}...")

            # Run import command
            cmd = [
                sys.executable,
                "scripts/import-quest-to-db.py",
                "--quest-file", str(quest_file)
            ]

            result = subprocess.run(cmd, capture_output=True, text=True, cwd=Path.cwd())

            if result.returncode == 0:
                print(f"[OK] Successfully imported {quest_file.name}")
                imported_count += 1
            else:
                print(f"[ERROR] Failed to import {quest_file.name}")
                print(f"Error: {result.stderr}")
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
        print("\n[SUCCESS] All Las Vegas quests imported successfully!")
        return 0

if __name__ == "__main__":
    sys.exit(batch_import_las_vegas_quests())
