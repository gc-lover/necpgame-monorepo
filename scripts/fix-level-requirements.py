#!/usr/bin/env python3
"""
Fix quest level requirements validation errors.

This script adds missing level_min and level_max fields to quest_definition.
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List


class LevelRequirementsFixer:
    """Fixes missing level requirements in quest YAML files"""

    def __init__(self):
        self.fixed_count = 0
        self.errors = []

    def load_yaml(self, file_path: Path) -> Dict[str, Any]:
        """Load YAML file safely"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f)
        except Exception as e:
            self.errors.append(f"Failed to load {file_path}: {e}")
            return {}

    def save_yaml(self, file_path: Path, data: Dict[str, Any]):
        """Save YAML file with proper formatting"""
        try:
            with open(file_path, 'w', encoding='utf-8') as f:
                yaml.dump(data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)
        except Exception as e:
            self.errors.append(f"Failed to save {file_path}: {e}")

    def add_missing_level_fields(self, data: Dict[str, Any]) -> bool:
        """Add missing level_min and level_max fields"""
        if 'quest_definition' not in data:
            data['quest_definition'] = {}

        quest_def = data['quest_definition']
        changed = False

        if 'level_min' not in quest_def:
            # Determine level_min based on quest type and difficulty
            quest_type = quest_def.get('quest_type', 'side')
            difficulty = data.get('metadata', {}).get('difficulty', 'normal')

            if quest_type == 'main':
                level_min = 1
            elif difficulty == 'hard':
                level_min = 10
            elif difficulty == 'medium':
                level_min = 5
            else:
                level_min = 1

            quest_def['level_min'] = level_min
            changed = True

        if 'level_max' not in quest_def:
            # level_max is usually null (no upper limit) or level_min + some range
            level_min = quest_def.get('level_min', 1)
            difficulty = data.get('metadata', {}).get('difficulty', 'normal')

            if difficulty == 'easy':
                level_max = level_min + 5
            elif difficulty == 'medium':
                level_max = level_min + 10
            elif difficulty == 'hard':
                level_max = level_min + 15
            else:
                level_max = None  # No upper limit

            quest_def['level_max'] = level_max
            changed = True

        return changed

    def fix_quest_file(self, file_path: Path):
        """Fix a single quest file"""
        data = self.load_yaml(file_path)
        if not data:
            return

        # Add missing level fields
        if self.add_missing_level_fields(data):
            self.save_yaml(file_path, data)
            self.fixed_count += 1
            print(f"Added level requirements to {file_path}")

    def find_quest_files(self, base_path: Path) -> List[Path]:
        """Find all quest YAML files"""
        quest_files = []
        for yaml_file in base_path.rglob('*.yaml'):
            if 'quest' in yaml_file.name.lower():
                quest_files.append(yaml_file)
        return quest_files

    def run(self, base_path: Path = None):
        """Run the fixer on all quest files"""
        if base_path is None:
            base_path = Path('knowledge/canon')

        print(f"Searching for quest files in {base_path}")
        quest_files = self.find_quest_files(base_path)

        print(f"Found {len(quest_files)} quest files")

        for quest_file in quest_files:
            try:
                self.fix_quest_file(quest_file)
            except Exception as e:
                self.errors.append(f"Error fixing {quest_file}: {e}")

        print(f"\nFixed {self.fixed_count} files")
        if self.errors:
            print(f"Errors encountered: {len(self.errors)}")
            for error in self.errors[:10]:  # Show first 10 errors
                print(f"  {error}")


def main():
    fixer = LevelRequirementsFixer()
    fixer.run()


if __name__ == "__main__":
    main()
