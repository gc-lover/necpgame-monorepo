#!/usr/bin/env python3
"""
Batch import Miami quests to database using import-quest-to-db.py.

Issue: Quests America Miami Part 3 - Import to DB
"""

import subprocess
import sys
from pathlib import Path

def batch_import_miami_quests():
    """Import all Miami quest definition files to database."""
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

    # Find all Miami quest import files
    miami_quest_files = list(quest_dir.rglob("*miami*import.yaml"))

    print(f"Found {len(miami_quest_files)} Miami quest import files to process")
    print(f"Looking in directory: {quest_dir}")
    print(f"Base directory: {base_dir}")
    if miami_quest_files:
        print("Files found:")
        for f in miami_quest_files[:3]:  # Show first 3 files
            print(f"  {f}")
    else:
        print("No files found with pattern *miami*import.yaml")

    for quest_file in miami_quest_files:
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
                print(f"Return code: {result.returncode}")
                print(f"Stdout: {result.stdout}")
                print(f"Stderr: {result.stderr}")
                failed_count += 1
        except Exception as e:
            print(f"[ERROR] Exception importing {quest_file.name}: {e}")
            failed_count += 1

    print("\n[SUMMARY] Import completed:")
    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if imported_count > 0:
        print("\n[SUCCESS] Miami quests imported successfully!")
    else:
        print("\n[WARNING] No Miami quests were imported. Check logs above for details.")
    return imported_count > 0


if __name__ == "__main__":
    success = batch_import_miami_quests()
    sys.exit(0 if success else 1)