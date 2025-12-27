#!/usr/bin/env python3
"""
Batch import Seoul quests to database using import-quest-to-db.py.

Issue: Quests Asia Seoul - Import to DB
"""

import os
import sys
import subprocess
import glob
from pathlib import Path

class SeoulQuestImporter:
    """Import Seoul quests to database."""

    def __init__(self):
        self.script_dir = Path("scripts")
        self.import_script = self.script_dir / "import-quest-to-db.py"
        self.quests_dir = Path("knowledge/canon/narrative/quests")
        self.success_count = 0
        self.fail_count = 0
        self.failed_files = []

    def find_seoul_quests(self) -> list:
        """Find all Seoul quest import files."""
        pattern = "*seoul*_import.yaml"
        seoul_files = list(self.quests_dir.glob(pattern))
        seoul_files.sort()
        return seoul_files

    def import_quest(self, quest_file: Path) -> bool:
        """Import single quest file."""
        try:
            print(f"Importing: {quest_file.name}")

            # Run import script
            result = subprocess.run([
                sys.executable, str(self.import_script),
                "--quest-file", str(quest_file)
            ], capture_output=True, text=True, timeout=60)

            if result.returncode == 0:
                print(f"[OK] Successfully imported {quest_file.name}")
                return True
            else:
                print(f"[ERROR] Failed to import {quest_file.name}")
                print(f"STDOUT: {result.stdout}")
                print(f"STDERR: {result.stderr}")
                return False

        except subprocess.TimeoutExpired:
            print(f"[TIMEOUT] Import timed out for {quest_file.name}")
            return False
        except Exception as e:
            print(f"[EXCEPTION] Error importing {quest_file.name}: {e}")
            return False

    def import_all_quests(self):
        """Import all Seoul quests."""
        seoul_quests = self.find_seoul_quests()

        if not seoul_quests:
            print("No Seoul quest files found. Run convert-seoul-quests-to-definition.py first.")
            return

        print(f"Found {len(seoul_quests)} Seoul quest files to import")

        for quest_file in seoul_quests:
            if self.import_quest(quest_file):
                self.success_count += 1
            else:
                self.fail_count += 1
                self.failed_files.append(quest_file.name)

        self.print_summary()

    def print_summary(self):
        """Print import summary."""
        print("\n=== SEOUL QUESTS IMPORT SUMMARY ===")
        print(f"Successfully imported: {self.success_count}")
        print(f"Failed imports: {self.fail_count}")
        print(f"Total processed: {self.success_count + self.fail_count}")

        if self.failed_files:
            print("\nFailed files:")
            for failed in self.failed_files:
                print(f"  - {failed}")

        if self.success_count > 0:
            print(f"\n[OK] Successfully imported {self.success_count} Seoul quests to database")
        else:
            print("\n[ERROR] No quests were successfully imported")


def main():
    """Main function."""
    print("Starting Seoul quests batch import...")

    if not Path("scripts/import-quest-to-db.py").exists():
        print("ERROR: import-quest-to-db.py not found in scripts directory")
        sys.exit(1)

    importer = SeoulQuestImporter()
    importer.import_all_quests()


if __name__ == "__main__":
    main()