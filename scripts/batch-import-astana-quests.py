#!/usr/bin/env python3
"""
Batch import Astana quests to database using import-quest-to-db.py.
"""

import subprocess
import sys
from pathlib import Path

def batch_import_astana_quests():
    """Import all Astana quest files."""
    # Script can be run from project root or scripts directory
    current_dir = Path.cwd()

    # If we're in scripts directory, go up one level
    if current_dir.name == "scripts":
        base_dir = current_dir.parent
    else:
        base_dir = current_dir

    imported_count = 0
    failed_count = 0

    # Find all quest files for Astana
    quest_dir = base_dir / "knowledge" / "canon" / "lore" / "timeline-author" / "quests" / "cis" / "astana" / "2020-2029"

    if not quest_dir.exists():
        print(f"[ERROR] Astana quest directory not found: {quest_dir}")
        return

    quest_files = list(quest_dir.glob("*.yaml"))

    print(f"[INFO] Found {len(quest_files)} Astana quest files to process")

    for quest_file in quest_files:
        print(f"[INFO] Importing {quest_file.name}...")

        # Run import command
        cmd = [
            sys.executable,
            str(base_dir / "scripts" / "import-quest-to-db.py"),
            "--quest-file",
            str(quest_file)
        ]

        try:
            result = subprocess.run(cmd, capture_output=True, text=True, timeout=60)

            if result.returncode == 0:
                print(f"[OK] Successfully imported {quest_file.name}")
                imported_count += 1
            else:
                print(f"[ERROR] Failed to import {quest_file.name}: {result.stderr}")
                failed_count += 1

        except subprocess.TimeoutExpired:
            print(f"[ERROR] Timeout importing {quest_file.name}")
            failed_count += 1
        except Exception as e:
            print(f"[ERROR] Exception importing {quest_file.name}: {e}")
            failed_count += 1

    print("\n[SUMMARY] Import completed:")
    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if imported_count > 0:
        print("\n[SUCCESS] Astana quests imported successfully!")
    else:
        print("\n[WARNING] No Astana quests were imported. Check logs above for details.")

if __name__ == "__main__":
    batch_import_astana_quests()
