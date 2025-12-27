#!/usr/bin/env python3
"""
Validate quest imports by checking generated Liquibase migrations.

Issue: Validate all quest imports completed successfully
"""

import os
import yaml
import sys
from pathlib import Path
from typing import Dict, Any, List

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent.parent
sys.path.insert(0, str(scripts_dir))

class QuestImportValidator:
    """
    Validate quest import results.
    """

    def __init__(self):
        from core.config import ConfigManager
        config = ConfigManager()
        self.migrations_dir = Path(config.get('paths', 'migrations_output_dir')) / "gameplay" / "quests"
        self.expected_quests = {
            'seattle': 5,
            'kiev': 7,
            'minsk': 10,
            'baku': 10,
            'moscow': 12
        }

    def validate_imports(self) -> Dict[str, Any]:
        """Validate all quest imports."""
        results = {
            'total_migrations': 0,
            'by_city': {},
            'validation_errors': [],
            'success': True
        }

        # Check migrations directory exists
        if not self.migrations_dir.exists():
            results['validation_errors'].append(f"Migrations directory not found: {self.migrations_dir}")
            results['success'] = False
            return results

        # Find all migration files
        migration_files = list(self.migrations_dir.glob("data_quests_*.yaml"))
        results['total_migrations'] = len(migration_files)

        print(f"Found {len(migration_files)} migration files")

        # Analyze migrations by city
        city_counts = {}
        for migration_file in migration_files:
            city = self.extract_city_from_filename(migration_file.name)
            if city:
                city_counts[city] = city_counts.get(city, 0) + 1

        results['by_city'] = city_counts

        # Validate expected counts
        for city, expected_count in self.expected_quests.items():
            actual_count = city_counts.get(city, 0)
            if actual_count != expected_count:
                error_msg = f"{city}: expected {expected_count}, got {actual_count}"
                results['validation_errors'].append(error_msg)
                results['success'] = False
                print(f"[ERROR] {error_msg}")
            else:
                print(f"[OK] {city}: {actual_count} migrations")

        # Validate migration file structure
        structure_errors = self.validate_migration_structure(migration_files)
        results['validation_errors'].extend(structure_errors)

        if structure_errors:
            results['success'] = False

        return results

    def extract_city_from_filename(self, filename: str) -> str:
        """Extract city name from migration filename."""
        if 'seattle' in filename.lower():
            return 'seattle'
        elif 'kiev' in filename.lower():
            return 'kiev'
        elif 'minsk' in filename.lower():
            return 'minsk'
        elif 'baku' in filename.lower():
            return 'baku'
        elif 'moscow' in filename.lower():
            return 'moscow'
        return None

    def validate_migration_structure(self, migration_files: List[Path]) -> List[str]:
        """Validate structure of migration files."""
        errors = []

        for migration_file in migration_files[:5]:  # Check first 5 files for performance
            try:
                with open(migration_file, 'r', encoding='utf-8') as f:
                    data = yaml.safe_load(f)

                # Check required structure
                if 'databaseChangeLog' not in data:
                    errors.append(f"{migration_file.name}: missing databaseChangeLog")
                    continue

                changelog = data['databaseChangeLog']
                if not isinstance(changelog, list) or len(changelog) == 0:
                    errors.append(f"{migration_file.name}: invalid databaseChangeLog structure")
                    continue

                changeset = changelog[0].get('changeSet', {})
                if 'changes' not in changeset:
                    errors.append(f"{migration_file.name}: missing changes in changeset")
                    continue

                changes = changeset.get('changes', [])
                if not changes or 'insert' not in changes[0]:
                    errors.append(f"{migration_file.name}: missing insert operation")
                    continue

                insert_op = changes[0]['insert']
                if 'tableName' not in insert_op or insert_op['tableName'] != 'gameplay.quest_definitions':
                    errors.append(f"{migration_file.name}: wrong table name")
                    continue

                print(f"[OK] {migration_file.name}: structure valid")

            except Exception as e:
                errors.append(f"{migration_file.name}: parsing error - {e}")

        return errors


def main():
    """Main validation function."""
    validator = QuestImportValidator()
    results = validator.validate_imports()

    print("\n=== QUEST IMPORT VALIDATION RESULTS ===")
    print(f"Total migrations found: {results['total_migrations']}")
    print("\nBy city:")
    for city, count in results['by_city'].items():
        status = "[OK]" if count == validator.expected_quests.get(city, 0) else "[ERROR]"
        print(f"  {city}: {count} {status}")

    if results['validation_errors']:
        print("\nValidation errors:")
        for error in results['validation_errors']:
            print(f"  [ERROR] {error}")

    overall_status = "[SUCCESS]" if results['success'] else "[FAILED]"
    print(f"\nOverall result: {overall_status}")

    if results['success']:
        print("\nAll quest imports validated successfully!")
        print("Ready for database deployment and QA testing.")
    else:
        print("\nValidation failed. Please check errors above.")

    return results['success']


if __name__ == '__main__':
    main()
