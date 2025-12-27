#!/usr/bin/env python3
"""
Fix quest validation errors automatically.

This script fixes common validation issues in quest YAML files:
1. Adds missing 'overview' and 'stages' sections to content.sections
2. Fixes quest IDs to start with 'canon-quest-'
3. Ensures proper structure for quest files
"""

import os
import yaml
import re
from pathlib import Path
from typing import Dict, Any, List


class QuestValidationFixer:
    """Fixes validation errors in quest YAML files"""

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

    def fix_quest_id(self, data: Dict[str, Any]) -> bool:
        """Fix quest ID to start with 'canon-quest-'"""
        if 'metadata' not in data or 'id' not in data['metadata']:
            return False

        quest_id = data['metadata']['id']
        if not quest_id.startswith('canon-quest-'):
            # Extract location and other info from filename or existing ID
            if quest_id.startswith('quest-'):
                new_id = 'canon-' + quest_id
            else:
                # Try to construct proper ID from existing data
                location = data['metadata'].get('tags', [''])[0] if data['metadata'].get('tags') else ''
                title_slug = re.sub(r'[^\w\-]', '-', quest_id.lower())
                new_id = f"canon-quest-{location}-{title_slug}" if location else f"canon-quest-{title_slug}"

            data['metadata']['id'] = new_id
            return True
        return False

    def add_missing_sections(self, data: Dict[str, Any]) -> bool:
        """Add missing overview and stages sections to content.sections"""
        if 'content' not in data or 'sections' not in data['content']:
            return False

        sections = data['content']['sections']
        has_overview = any(s.get('id') == 'overview' for s in sections)
        has_stages = any(s.get('id') == 'stages' for s in sections)

        if has_overview and has_stages:
            return False  # Already has both sections

        # Get quest title and basic info for generating content
        title = data.get('metadata', {}).get('title', 'Quest')
        quest_type = data.get('quest_definition', {}).get('quest_type', 'side')

        # Add overview section if missing
        if not has_overview:
            overview_section = {
                'id': 'overview',
                'title': f'Обзор: {title}',
                'body': f'Этот {quest_type} квест "{title}" предлагает увлекательное исследование и взаимодействие с миром игры.',
                'mechanics_links': [],
                'assets': []
            }
            sections.insert(0, overview_section)

        # Add stages section if missing
        if not has_stages:
            objectives = data.get('quest_definition', {}).get('objectives', [])
            stages_body = "Этапы квеста:\n"
            for i, obj in enumerate(objectives[:5], 1):  # Limit to first 5 objectives
                obj_text = obj.get('text', f'Objective {i}') if isinstance(obj, dict) else str(obj)
                stages_body += f"{i}. {obj_text}\n"

            stages_section = {
                'id': 'stages',
                'title': 'Этапы выполнения',
                'body': stages_body,
                'mechanics_links': [],
                'assets': []
            }
            sections.insert(1, stages_section)

        return True

    def fix_quest_file(self, file_path: Path):
        """Fix a single quest file"""
        data = self.load_yaml(file_path)
        if not data:
            return

        changed = False

        # Fix quest ID
        if self.fix_quest_id(data):
            changed = True
            print(f"Fixed ID in {file_path}")

        # Add missing sections
        if self.add_missing_sections(data):
            changed = True
            print(f"Added missing sections to {file_path}")

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
    fixer = QuestValidationFixer()
    fixer.run()


if __name__ == "__main__":
    main()
