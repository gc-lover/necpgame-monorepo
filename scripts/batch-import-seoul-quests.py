#!/usr/bin/env python3
"""
Batch import all Seoul quest files to database
"""

import os
import subprocess
from pathlib import Path

def main():
    seoul_dir = Path("knowledge/canon/lore/timeline-author/quests/asia/seoul")

    # Process all YAML files in seoul directories
    for yaml_file in seoul_dir.rglob("*.yaml"):
        if yaml_file.is_file():
            print(f"Importing {yaml_file}")
            try:
                result = subprocess.run([
                    'python', 'scripts/import-quest-to-db.py',
                    '--quest-file', str(yaml_file),
                    '--verbose'
                ], capture_output=True, text=True)

                if result.returncode == 0:
                    print(f"[OK] Successfully imported {yaml_file.name}")
                else:
                    print(f"[ERROR] Failed to import {yaml_file.name}: {result.stderr}")

            except Exception as e:
                print(f"[ERROR] Error importing {yaml_file.name}: {e}")

if __name__ == "__main__":
    main()
