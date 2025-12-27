#!/usr/bin/env python3
"""
Convert Moscow quest-definition files to import-compatible format for database import.

Issue: Convert Moscow quests to proper import format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List

class MoscowQuestConverter:
    """
    Convert Moscow quest-definition files to import-compatible format.
    """

    def __init__(self):
        self.source_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def convert_all_quests(self) -> List[str]:
        """Convert all Moscow quest-definition files to import format."""
        converted_files = []

        # Find Moscow quest files
        moscow_patterns = [
            "moscow-*quest-definition.yaml",
            "moscow-*.yaml"
        ]

        quest_files = []
        for pattern in moscow_patterns:
            quest_files.extend(list(self.source_dir.glob(pattern)))

        # Remove duplicates
        quest_files = list(set(quest_files))
        quest_files.sort()

        print(f"Found {len(quest_files)} Moscow quest files to convert")

        for quest_file in quest_files:
            try:
                converted_file = self.convert_quest_file(quest_file)
                if converted_file:
                    converted_files.append(converted_file)
                    print(f"[OK] Converted {quest_file.name} -> {converted_file.name}")
                else:
                    print(f"[ERROR] Failed to convert {quest_file.name}")
            except Exception as e:
                print(f"[ERROR] Error converting {quest_file.name}: {e}")

        return converted_files

    def convert_quest_file(self, quest_file: Path) -> Path:
        """Convert a single quest file to import-compatible format."""
        # Load existing quest_definition
        with open(quest_file, 'r', encoding='utf-8') as f:
            quest_data = yaml.safe_load(f)

        # Convert to import-compatible format
        import_format = self.convert_to_import_format(quest_data)

        # Create output filename
        base_name = quest_file.stem
        output_filename = f"{base_name}_import.yaml"
        output_file = self.output_dir / output_filename

        # Write converted format
        with open(output_file, 'w', encoding='utf-8') as f:
            yaml.safe_dump(import_format, f, default_flow_style=False, allow_unicode=True, indent=2)

        return output_file

    def convert_to_import_format(self, quest_data: Dict[str, Any]) -> Dict[str, Any]:
        """Convert quest_definition to import-compatible format."""
        metadata = quest_data['metadata']
        quest_def = quest_data['quest_definition']

        # Extract rewards in correct format
        rewards = self.convert_rewards(quest_def.get('rewards', []))

        # Convert objectives
        objectives = self.convert_objectives(quest_def.get('objectives', []))

        # Build import format
        import_format = {
            'metadata': metadata,
            'quest_definition': {
                'quest_type': quest_def.get('quest_type', 'side_story'),
                'level_min': quest_def.get('level_min', 1),
                'level_max': quest_def.get('level_max', 10),
                'requirements': quest_def.get('requirements', {
                    'faction': 'neutral',
                    'completed_quests': [],
                    'skills': []
                }),
                'objectives': objectives,
                'rewards': rewards
            }
        }

        return import_format

    def convert_rewards(self, rewards: List[Dict[str, Any]]) -> Dict[str, Any]:
        """Convert rewards list to dictionary format."""
        reward_dict = {
            'experience': 2000,
            'currency': 1500,
            'items': [],
            'reputation': {}
        }

        for reward in rewards:
            reward_type = reward.get('type', '')
            if reward_type == 'experience':
                reward_dict['experience'] = reward.get('amount', 2000)
            elif reward_type == 'currency':
                reward_dict['currency'] = reward.get('amount', 1500)
            elif reward_type == 'item':
                reward_dict['items'].append({
                    'id': reward.get('item_id', 'unknown_item'),
                    'name': reward.get('name', 'Unknown Item'),
                    'type': 'collectible'
                })
            elif reward_type == 'reputation':
                faction = reward.get('faction', 'unknown')
                amount = reward.get('amount', 0)
                reward_dict['reputation'][faction] = amount

        return reward_dict

    def convert_objectives(self, objectives: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """Convert objectives to correct format."""
        converted_objectives = []

        for obj in objectives:
            converted_obj = {
                'id': obj.get('id', 'unknown'),
                'type': obj.get('type', 'exploration'),
                'description': obj.get('description', 'Objective'),
                'target': 1
            }
            converted_objectives.append(converted_obj)

        return converted_objectives


def main():
    """Main conversion function."""
    converter = MoscowQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\n[SUCCESS] Successfully converted {len(converted_files)} Moscow quests to import format")
    print("\nConverted files:")
    for file in converted_files:
        print(f"  - {file.name}")

    print("\nReady for database import using batch-import-moscow-quests.py script")
    return converted_files


if __name__ == '__main__':
    main()
