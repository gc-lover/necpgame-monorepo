#!/usr/bin/env python3
"""
Batch import selected Tokyo quests to database using import-quest-to-db.py.
Imports specific quests: tea-ceremony, harajuku-fashion
"""

import subprocess
import sys
from pathlib import Path

def batch_import_selected_tokyo_quests():
    """Import specific Tokyo quest files."""
    # Script can be run from project root or scripts directory
    current_dir = Path.cwd()

    # If we're in scripts directory, go up one level
    if current_dir.name == "scripts":
        base_dir = current_dir.parent
    else:
        base_dir = current_dir

    imported_count = 0
    failed_count = 0

    # Selected Tokyo quests to import
    selected_quests = [
        "quest-028-tea-ceremony.yaml",     # Чайная церемония
        "quest-029-harajuku-fashion.yaml"  # Мода Харадзюку
    ]

    # Find quest directory (2040-2060 period)
    quest_dir = base_dir / "knowledge" / "canon" / "lore" / "timeline-author" / "quests" / "asia" / "tokyo" / "2040-2060"

    if not quest_dir.exists():
        print(f"[ERROR] Tokyo quest directory not found: {quest_dir}")
        return

    print(f"[INFO] Importing selected Tokyo quests: {selected_quests}")

    for quest_filename in selected_quests:
        quest_file = quest_dir / quest_filename

        if not quest_file.exists():
            print(f"[WARNING] Quest file not found: {quest_filename}")
            failed_count += 1
            continue

        print(f"[INFO] Importing {quest_filename}...")

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
                print(f"[OK] Successfully imported {quest_filename}")
                imported_count += 1
            else:
                print(f"[ERROR] Failed to import {quest_filename}: {result.stderr}")
                failed_count += 1

        except subprocess.TimeoutExpired:
            print(f"[ERROR] Timeout importing {quest_filename}")
            failed_count += 1
        except Exception as e:
            print(f"[ERROR] Exception importing {quest_filename}: {e}")
            failed_count += 1

    print("\n[SUMMARY] Import completed:")    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if imported_count > 0:
        print("\n[SUCCESS] Selected Tokyo quests imported successfully!")
    else:
        print("\n[WARNING] No Tokyo quests were imported. Check logs above for details.")

if __name__ == "__main__":
    batch_import_selected_tokyo_quests()
