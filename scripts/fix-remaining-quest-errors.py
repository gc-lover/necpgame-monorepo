#!/usr/bin/env python3
"""
Fix remaining quest validation errors.

This script fixes remaining validation issues:
1. Invalid status values (should be draft/review/approved/published)
2. quest_definition.rewards should be dictionary instead of list
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List


class RemainingQuestErrorsFixer:
    """Fixes remaining validation errors in quest YAML files"""

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

    def fix_invalid_status(self, data: Dict[str, Any]) -> bool:
        """Fix invalid status values"""
        if 'metadata' not in data or 'status' not in data['metadata']:
            return False

        current_status = data['metadata']['status']
        valid_statuses = ['draft', 'review', 'approved', 'published']

        if current_status not in valid_statuses:
            # Map common invalid statuses to valid ones
            status_mapping = {
                'active': 'approved',
                'inactive': 'draft',
                'pending': 'draft',
                'ready': 'approved',
                'complete': 'published'
            }
            new_status = status_mapping.get(current_status, 'draft')
            data['metadata']['status'] = new_status
            return True
        return False

    def fix_rewards_structure(self, data: Dict[str, Any]) -> bool:
        """Fix quest_definition.rewards to be a dictionary"""
        if 'quest_definition' not in data or 'rewards' not in data['quest_definition']:
            return False

        rewards = data['quest_definition']['rewards']
        if isinstance(rewards, list):
            # Convert list to dictionary structure
            new_rewards = {}
            for reward in rewards:
                if isinstance(reward, dict):
                    # Try to extract meaningful reward data
                    reward_type = reward.get('type', 'experience')
                    reward_value = reward.get('value', reward.get('amount', 100))
                    new_rewards[reward_type] = reward_value
                elif isinstance(reward, str):
                    # Simple string reward
                    new_rewards['item'] = reward

            if not new_rewards:
                new_rewards = {'experience': 100}  # Default reward

            data['quest_definition']['rewards'] = new_rewards
            return True
        return False

    def fix_quest_file(self, file_path: Path):
        """Fix a single quest file"""
        data = self.load_yaml(file_path)
        if not data:
            return

        changed = False

        # Fix invalid status
        if self.fix_invalid_status(data):
            changed = True
            print(f"Fixed status in {file_path}")

        # Fix rewards structure
        if self.fix_rewards_structure(data):
            changed = True
            print(f"Fixed rewards structure in {file_path}")

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
    fixer = RemainingQuestErrorsFixer()
    fixer.run()


if __name__ == "__main__":
    main()
