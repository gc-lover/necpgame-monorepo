#!/usr/bin/env python3
"""
Run all content migration generators using SOLID architecture.
"""

import sys
from pathlib import Path

# Add the migrations module to the path
sys.path.insert(0, str(Path(__file__).parent))

from migrations.generators import (
    QuestMigrationGenerator,
    NpcsMigrationGenerator,
    DialoguesMigrationGenerator,
    LoreMigrationGenerator,
    EnemiesMigrationGenerator,
    InteractivesMigrationGenerator,
    ItemsMigrationGenerator,
    CultureMigrationGenerator,
    DocumentationMigrationGenerator
)


class AllContentMigrationsRunner:
    """Runner for all content migration generators."""

    def __init__(self):
        self.generators = [
            QuestMigrationGenerator,
            NpcsMigrationGenerator,
            DialoguesMigrationGenerator,
            LoreMigrationGenerator,
            EnemiesMigrationGenerator,
            InteractivesMigrationGenerator,
            ItemsMigrationGenerator,
            CultureMigrationGenerator,
            DocumentationMigrationGenerator  # Universal documentation generator
        ]

    def run_all(self):
        """Run all generators in sequence."""
        print("Starting all content migrations generation...")
        print("Using SOLID architecture with dependency injection")

        successful = 0
        failed = 0

        for generator_class in self.generators:
            try:
                print(f"\n{'='*60}")
                print(f"Running {generator_class.__name__}")
                print(f"{'='*60}")

                generator = generator_class()
                generator.run()
                successful += 1

            except Exception as e:
                print(f"Failed to run {generator_class.__name__}: {e}")
                failed += 1

        print(f"\n{'='*60}")
        print("All Content Migrations Generation Complete")
        print(f"{'='*60}")
        print(f"Successful generators: {successful}")
        print(f"Failed generators: {failed}")

        if failed > 0:
            print("Some generators failed. Check the output above for details.")
            return False
        else:
            print("All content migrations generated successfully!")
            return True


def main():
    runner = AllContentMigrationsRunner()
    success = runner.run_all()
    sys.exit(0 if success else 1)


if __name__ == '__main__':
    main()
