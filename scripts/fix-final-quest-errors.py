#!/usr/bin/env python3
"""
Fix final quest validation errors.

This script fixes the last remaining validation issues:
1. Missing quest_definition.quest_type field
2. Missing content section
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List


class FinalQuestErrorsFixer:
    """Fixes final validation errors in quest YAML files"""

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

    def add_missing_quest_type(self, data: Dict[str, Any]) -> bool:
        """Add missing quest_definition.quest_type field"""
        if 'quest_definition' not in data:
            data['quest_definition'] = {}

        if 'quest_type' not in data['quest_definition']:
            # Determine quest type based on content or default to 'side'
            title = data.get('metadata', {}).get('title', '')
            if any(word in title.lower() for word in ['main', 'story', 'campaign']):
                quest_type = 'main'
            else:
                quest_type = 'side'

            data['quest_definition']['quest_type'] = quest_type
            return True
        return False

    def add_missing_content_section(self, data: Dict[str, Any]) -> bool:
        """Add missing content section with overview and stages"""
        if 'content' in data:
            return False  # Content section exists

        # Get quest info for generating content
        title = data.get('metadata', {}).get('title', 'Quest')
        quest_type = data.get('quest_definition', {}).get('quest_type', 'side')
        objectives = data.get('quest_definition', {}).get('objectives', [])

        # Create basic content structure
        content = {
            'sections': [
                {
                    'id': 'overview',
                    'title': f'Обзор: {title}',
                    'body': f'Этот {quest_type} квест "{title}" предлагает увлекательное приключение в мире игры.',
                    'mechanics_links': [],
                    'assets': []
                },
                {
                    'id': 'stages',
                    'title': 'Этапы выполнения',
                    'body': 'Этапы квеста:\n' + '\n'.join([
                        f"{i+1}. {obj.get('text', f'Задача {i+1}')}" for i, obj in enumerate(objectives[:3])
                    ]) if objectives else 'Этапы квеста определяются динамически.',
                    'mechanics_links': [],
                    'assets': []
                }
            ]
        }

        data['content'] = content
        return True

    def fix_quest_file(self, file_path: Path):
        """Fix a single quest file"""
        data = self.load_yaml(file_path)
        if not data:
            return

        changed = False

        # Add missing quest type
        if self.add_missing_quest_type(data):
            changed = True
            print(f"Added quest_type to {file_path}")

        # Add missing content section
        if self.add_missing_content_section(data):
            changed = True
            print(f"Added content section to {file_path}")

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
    fixer = FinalQuestErrorsFixer()
    fixer.run()


if __name__ == "__main__":
    main()
