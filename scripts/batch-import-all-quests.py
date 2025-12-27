#!/usr/bin/env python3
"""
Batch Quest Import Script
Automatically imports all quest YAML files from knowledge/canon/lore/timeline-author/quests/
to Liquibase migrations for database insertion.

This script supports the enterprise-grade domain architecture and creates proper
migration files for the specialized-domain (gameplay mechanics).

Issue: #615
"""

import argparse
import subprocess
import sys
from pathlib import Path
from typing import List


def main():
    parser = argparse.ArgumentParser(
        description='Batch import all quest YAML files to Liquibase migrations'
    )
    parser.add_argument(
        '--quests-dir', '-d',
        type=str,
        default='knowledge/canon/lore/timeline-author/quests',
        help='Root directory containing quest YAML files'
    )
    parser.add_argument(
        '--output-dir', '-o',
        type=str,
        default='infrastructure/liquibase/migrations/data/quests',
        help='Output directory for migration files'
    )
    parser.add_argument(
        '--force', '-F',
        action='store_true',
        help='Overwrite existing migration files'
    )
    parser.add_argument(
        '--dry-run',
        action='store_true',
        help='Show what would be done without actually doing it'
    )
    parser.add_argument(
        '--max-files', '-m',
        type=int,
        default=50,
        help='Maximum number of files to process (for testing)'
    )

    args = parser.parse_args()

    quests_dir = Path(args.quests_dir)
    output_dir = Path(args.output_dir)

    # Validate input directory
    if not quests_dir.exists():
        print(f"ERROR: Quests directory not found: {quests_dir}")
        sys.exit(1)

    if not quests_dir.is_dir():
        print(f"ERROR: Not a directory: {quests_dir}")
        sys.exit(1)

    # Find all YAML files
    yaml_files = list(quests_dir.rglob('*.yaml'))
    yaml_files.sort()  # Consistent ordering

    if not yaml_files:
        print(f"ERROR: No YAML files found in {quests_dir}")
        sys.exit(1)

    print(f"Found {len(yaml_files)} YAML files in {quests_dir}")

    # Limit files for testing
    if len(yaml_files) > args.max_files:
        print(f"Limiting to first {args.max_files} files (use --max-files to change)")
        yaml_files = yaml_files[:args.max_files]

    # Process each file
    processed = 0
    failed = 0

    for quest_file in yaml_files:
        print(f"\nProcessing: {quest_file.relative_to(quests_dir)}")

        if args.dry_run:
            print("  DRY RUN: Would import quest file")
            processed += 1
            continue

        # Run the enhanced import script
        cmd = [
            sys.executable,
            'scripts/simple-quest-import-enhanced.py',
            '--quest-file', str(quest_file),
            '--output-dir', str(output_dir)
        ]

        if args.force:
            cmd.append('--force')

        try:
            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                cwd=Path(__file__).parent.parent
            )

            if result.returncode == 0:
                print("  SUCCESS: Quest imported successfully")
                processed += 1
            else:
                print(f"  ERROR: Failed to import quest")
                print(f"  STDOUT: {result.stdout}")
                print(f"  STDERR: {result.stderr}")
                failed += 1

        except Exception as e:
            print(f"  ERROR: Exception occurred: {e}")
            failed += 1

    # Summary
    print("\n=== BATCH IMPORT SUMMARY ===")
    print(f"Total files found: {len(list(quests_dir.rglob('*.yaml')))}")
    print(f"Files processed: {processed}")
    print(f"Files failed: {failed}")
    print(f"Success rate: {(processed / (processed + failed) * 100):.1f}%" if processed + failed > 0 else "N/A")

    if failed > 0:
        print("\nWARNING: Some files failed to import. Check the output above for details.")
        sys.exit(1)
    else:
        print("\nSUCCESS: All quests imported successfully!")


if __name__ == '__main__':
    main()
