#!/usr/bin/env python3
"""
Batch import Tokyo quests to database.

Issue: Import all converted Tokyo quest definitions to Liquibase migrations
"""

import subprocess
import sys
from pathlib import Path

def batch_import_tokyo_quests():
    """Import all Tokyo quest definition files to database."""
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

    # Find all import files and filter for Tokyo-related quests
    all_import_files = list(quest_dir.glob("*import.yaml"))
    tokyo_keywords = [
        'tokyo', 'shibuya', 'akihabara', 'yakuza', 'ramen', 'sumo', 'shrine', 'katana',
        'pachinko', 'cyber', 'maglev', 'corporate', 'geisha', 'sakura', 'robot', 'ninja',
        'manga', 'karaoke', 'sushi', 'cosplay', 'emperor', 'virtual', 'neural', 'digital',
        'fuji', 'pirates', 'samurai', 'anime', 'taiko', 'onsen', 'boso', 'tea', 'harajuku',
        'yokai', 'climate', 'posthuman', 'rewilding', 'democracy', 'shogun', 'arasaka',
        'bullet', 'zen', 'pokemon', 'love', 'kendo', 'neon', 'takao', 'forgotten', 'bushido',
        'hanami', 'godzilla', 'legacy', 'simulation', 'shinto', 'sayonara', 'rising'
    ]

    tokyo_quest_files = [
        f for f in all_import_files
        if any(keyword in f.name.lower() for keyword in tokyo_keywords)
    ]

    print(f"Found {len(tokyo_quest_files)} Tokyo quest import files to process")

    for quest_file in tokyo_quest_files:
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

    print(f"\n[SUMMARY] Import completed:")
    print(f"  Successfully imported: {imported_count}")
    print(f"  Failed: {failed_count}")

    if failed_count > 0:
        print(f"\n[WARNING] {failed_count} files failed to import. Check logs above for details.")
        return 1
    else:
        print("\n[SUCCESS] All Tokyo quests imported successfully!")
        return 0

if __name__ == "__main__":
    sys.exit(batch_import_tokyo_quests())
