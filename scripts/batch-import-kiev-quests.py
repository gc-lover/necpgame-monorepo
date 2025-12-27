#!/usr/bin/env python3
"""
Batch import Kiev quests to database.

Issue: Import all converted Kiev quest definitions to Liquibase migrations
"""

import subprocess
import sys
from pathlib import Path

def batch_import_kiev_quests():
    """Import all Kiev quest definition files to database."""
    quest_dir = Path("knowledge/canon/narrative/quests")
    imported_count = 0
    failed_count = 0

    # Find all Kiev quest definition files
    kiev_quest_files = list(quest_dir.glob("kiev_*_quest_definition.yaml"))

    print(f"Found {len(kiev_quest_files)} Kiev quest definition files to import")

    for quest_file in kiev_quest_files:
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
    print(f"  Total: {imported_count + failed_count}")

    return imported_count, failed_count

if __name__ == '__main__':
    batch_import_kiev_quests()
