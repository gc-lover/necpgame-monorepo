#!/usr/bin/env python3
"""
Unified migration generator runner using SOLID principles.
Eliminates code duplication across individual generator scripts.
"""

import sys
from pathlib import Path
from typing import Dict, Type
import argparse

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent.parent
sys.path.insert(0, str(scripts_dir))

# Import all generators
from migrations.generators.quests_generator import QuestMigrationGenerator
from migrations.generators.npcs_generator import NpcsMigrationGenerator
from migrations.generators.npcs_v2_generator import NpcsV2MigrationGenerator
from migrations.generators.dialogues_generator import DialoguesMigrationGenerator
from migrations.generators.lore_generator import LoreMigrationGenerator
from migrations.generators.enemies_generator import EnemiesMigrationGenerator
from migrations.generators.interactives_generator import InteractivesMigrationGenerator
from migrations.generators.items_generator import ItemsMigrationGenerator
from migrations.generators.culture_generator import CultureMigrationGenerator
from migrations.generators.documentation_generator import DocumentationMigrationGenerator

# Mapping of content types to generator classes
GENERATOR_MAPPING: Dict[str, Type] = {
    'quests': QuestMigrationGenerator,
    'npcs': NpcsMigrationGenerator,
    'npcs-v2': NpcsV2MigrationGenerator,
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
            help='Type of content to generate migrations for (npcs-v2 = new format NPCs, documentation = all YAML files from knowledge/)'
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
