#!/usr/bin/env python3
"""
Unified migration generator runner using SOLID principles.
Eliminates code duplication across individual generator scripts.
"""

import sys
from pathlib import Path
from typing import Dict, Type
import argparse

# Import all generators
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

# Mapping of content types to generator classes
GENERATOR_MAPPING: Dict[str, Type] = {
    'quests': QuestMigrationGenerator,
    'npcs': NpcsMigrationGenerator,
    'dialogues': DialoguesMigrationGenerator,
    'lore': LoreMigrationGenerator,
    'enemies': EnemiesMigrationGenerator,
    'interactives': InteractivesMigrationGenerator,
    'items': ItemsMigrationGenerator,
    'culture': CultureMigrationGenerator,
    'documentation': DocumentationMigrationGenerator,
}


class MigrationGeneratorRunner:
    """Unified runner for all migration generators following SOLID principles."""

    def __init__(self):
        self.parser = argparse.ArgumentParser(
            description="Run migration generators for different content types"
        )
        self.parser.add_argument(
            'content_type',
            choices=list(GENERATOR_MAPPING.keys()),
            help='Type of content to generate migrations for (documentation = all YAML files from knowledge/)'
        )

    def run_generator(self, content_type: str) -> bool:
        """Run the specified generator type."""
        print(f"Starting {content_type} migration generator...")

        try:
            # Get generator class from mapping
            generator_class = GENERATOR_MAPPING[content_type]
            print(f"Found generator class: {generator_class.__name__}")

            # Create and run generator
            generator = generator_class()
            print(f"Generator created. Output dir: {generator.output_dir}")

            # Check input directories
            print(f"Input directories: {generator.input_dirs}")

            result = generator.run()
            print(f"Generator completed. Generated {len(result)} migration files.")

            # List generated files
            for file_path in result:
                print(f"  Generated: {Path(file_path).name}")

            return True

        except Exception as e:
            print(f"Error during {content_type} generation: {e}")
            import traceback
            traceback.print_exc()
            return False

    def run(self) -> bool:
        """Main execution method."""
        args = self.parser.parse_args()
        return self.run_generator(args.content_type)


def main():
    """Main entry point."""
    runner = MigrationGeneratorRunner()
    success = runner.run()
    sys.exit(0 if success else 1)


if __name__ == '__main__':
    main()
