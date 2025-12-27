#!/usr/bin/env python3
"""
Convert Kiev 2020-2029 canon quests to quest_definition format for database import.

Issue: Convert 8 Kiev quests to proper quest_definition YAML format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List

class KievQuestConverter:
    """
    Convert Kiev canon quest files to quest_definition format.
    """

    def __init__(self):
        self.source_dir = Path("knowledge/canon/lore/timeline-author/quests/cis/kiev/2020-2029")
        self.output_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def convert_all_quests(self) -> List[str]:
        """Convert all Kiev 2020-2029 quests to quest_definition format."""
        converted_files = []

        # Get all quest files
        quest_files = list(self.source_dir.glob("quest-*.yaml"))
        quest_files.sort()  # Sort by filename

        print(f"Found {len(quest_files)} Kiev quest files to convert")

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
        """Convert a single quest file to quest_definition format."""
        # Load canon quest
        with open(quest_file, 'r', encoding='utf-8') as f:
            canon_data = yaml.safe_load(f)

        # Generate quest_definition
        quest_def = self.generate_quest_definition(canon_data)

        # Create output filename
        quest_id = canon_data['metadata']['id']
        safe_id = quest_id.replace('canon-quest-', '').replace('-', '_')
        output_filename = f"kiev_{safe_id}_quest_definition.yaml"
        output_file = self.output_dir / output_filename

        # Write quest_definition
        with open(output_file, 'w', encoding='utf-8') as f:
            yaml.safe_dump(quest_def, f, default_flow_style=False, allow_unicode=True, indent=2)

        return output_file

    def generate_quest_definition(self, canon_data: Dict[str, Any]) -> Dict[str, Any]:
        """Generate quest_definition from canon quest data."""
        metadata = canon_data['metadata']
        content = canon_data.get('content', {})
        summary = canon_data.get('summary', {})

        # Extract quest level from filename or content
        quest_id = metadata['id']
        level_range = self.extract_level_range(quest_id, content)

        # Generate objectives from content sections
        objectives = self.generate_objectives(content.get('sections', []))

        # Generate rewards from summary or content
        rewards = self.generate_rewards(content, summary)

        # Build quest_definition
        quest_def = {
            'metadata': {
                'id': f"narrative-quests-{metadata['id'].replace('canon-quest-', '')}",
                'title': metadata['title']
            },
            'quest_definition': {
                'quest_type': 'side_story',
                'level_min': level_range[0],
                'level_max': level_range[1],
                'requirements': {
                    'faction': 'neutral',
                    'completed_quests': [],
                    'skills': []
                },
                'objectives': objectives,
                'rewards': rewards
            }
        }

        return quest_def

    def extract_level_range(self, quest_id: str, content: Dict[str, Any]) -> tuple:
        """Extract level range from quest content or use defaults."""
        # Extract quest number from filename pattern quest-NNN-
        import re
        match = re.search(r'quest-(\d+)', quest_id)
        if match:
            quest_number = int(match.group(1))
        else:
            quest_number = 1  # Default

        if quest_number <= 3:
            return (1, 10)  # Early game quests
        elif quest_number <= 6:
            return (8, 18)  # Mid game
        else:
            return (15, 25)  # Late game

    def generate_objectives(self, sections: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """Generate objectives from content sections."""
        objectives = []

        for i, section in enumerate(sections):
            section_id = section.get('id', f'objective_{i+1}')
            title = section.get('title', 'Objective')
            body = section.get('body', '')

            # Determine objective type based on content
            obj_type = self.determine_objective_type(title, body)

            objective = {
                'id': section_id,
                'type': obj_type,
                'description': title,
                'target': 1
            }

            objectives.append(objective)

        return objectives

    def determine_objective_type(self, title: str, body: str) -> str:
        """Determine objective type based on title and content."""
        title_lower = title.lower()
        body_lower = body.lower()

        if any(word in title_lower for word in ['встреча', 'диалог', 'беседа', 'разговор']):
            return 'dialogue'
        elif any(word in title_lower for word in ['сбор', 'поиск', 'найти']):
            return 'collection'
        elif any(word in title_lower for word in ['выбор', 'решение']):
            return 'choice'
        elif any(word in title_lower for word in ['исследование', 'изучение']):
            return 'exploration'
        elif any(word in title_lower for word in ['путешествие', 'паломничество']):
            return 'travel'
        else:
            return 'exploration'  # Default

    def generate_rewards(self, content: Dict[str, Any], summary: Dict[str, Any]) -> Dict[str, Any]:
        """Generate rewards from content and summary."""
        rewards = {
            'experience': 2000,
            'currency': 1500,
            'items': [],
            'reputation': {}
        }

        # Look for rewards in summary or content
        if 'key_points' in summary:
            key_points = summary['key_points']
            for point in key_points:
                if 'xp' in point.lower():
                    rewards['experience'] = self.extract_number_from_text(point) or 2000
                elif 'репутаци' in point.lower():
                    # Add reputation rewards
                    pass

        # Add some default items based on quest theme
        quest_title = content.get('title', '')
        if 'лавр' in quest_title.lower():
            rewards['items'].append({
                'id': 'lavra_blessing_amulet',
                'name': 'Амулет благословения лавры',
                'type': 'accessory'
            })
        elif 'майдан' in quest_title.lower():
            rewards['items'].append({
                'id': 'maidan_memorial_coin',
                'name': 'Памятная монета Майдана',
                'type': 'collectible'
            })

        return rewards

    def extract_number_from_text(self, text: str) -> int:
        """Extract number from text like '2000 XP'."""
        import re
        numbers = re.findall(r'\d+', text)
        return int(numbers[0]) if numbers else None


def main():
    """Main conversion function."""
    converter = KievQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\n[SUCCESS] Successfully converted {len(converted_files)} Kiev quests to quest_definition format")
    print("\nConverted files:")
    for file in converted_files:
        print(f"  - {file.name}")

    print("\nReady for database import using import-quest-to-db.py script")
    return converted_files


if __name__ == '__main__':
    main()
