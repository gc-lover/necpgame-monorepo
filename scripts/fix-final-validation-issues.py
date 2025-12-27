#!/usr/bin/env python3
"""
Fix final validation issues for quest files.

This script fixes the last remaining validation errors:
1. Missing summary section
2. Missing content.sections
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List


class FinalValidationFixer:
    """Fixes final validation issues in quest YAML files"""

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

    def add_missing_summary(self, data: Dict[str, Any]) -> bool:
        """Add missing summary section"""
        if 'summary' in data:
            return False

        # Get quest info for generating summary
        title = data.get('metadata', {}).get('title', 'Quest')
        quest_type = data.get('quest_definition', {}).get('quest_type', 'side')

        summary = {
            'problem': f'Необходимо выполнить {quest_type} квест "{title}" для развития сюжета.',
            'goal': f'Завершить квест "{title}" и получить награды.',
            'essence': f'Квест "{title}" предлагает увлекательные задания и исследование мира игры.'
        }

        data['summary'] = summary
        return True

    def add_missing_content_sections(self, data: Dict[str, Any]) -> bool:
        """Add missing content.sections"""
        if 'content' in data and 'sections' in data['content']:
            return False

        if 'content' not in data:
            data['content'] = {}

        if 'sections' not in data['content']:
            # Get quest info
            title = data.get('metadata', {}).get('title', 'Quest')
            quest_type = data.get('quest_definition', {}).get('quest_type', 'side')

            sections = [
                {
                    'id': 'overview',
                    'title': f'Обзор: {title}',
                    'body': f'Этот {quest_type} квест "{title}" предлагает увлекательное приключение в мире NECPGAME.',
                    'mechanics_links': [],
                    'assets': []
                },
                {
                    'id': 'stages',
                    'title': 'Этапы выполнения',
                    'body': f'Квест "{title}" включает несколько этапов выполнения основных заданий.',
                    'mechanics_links': [],
                    'assets': []
                }
            ]

            data['content']['sections'] = sections
            return True

        return False

    def fix_quest_file(self, file_path: Path):
        """Fix a single quest file"""
        data = self.load_yaml(file_path)
        if not data:
            return

        changed = False

        # Add missing summary
        if self.add_missing_summary(data):
            changed = True
            print(f"Added summary to {file_path}")

        # Add missing content sections
        if self.add_missing_content_sections(data):
            changed = True
            print(f"Added content.sections to {file_path}")

        # Save if changed
        if changed:
            self.save_yaml(file_path, data)
            self.fixed_count += 1

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
    fixer = FinalValidationFixer()
    fixer.run()


if __name__ == "__main__":
    main()
