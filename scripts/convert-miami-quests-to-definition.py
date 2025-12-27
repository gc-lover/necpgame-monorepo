#!/usr/bin/env python3
"""
Convert Miami quest-definition files to import-compatible format for database import.

Issue: Convert Miami quests to proper import format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List, Optional

class MiamiQuestConverter:
    """
    Convert Miami quest-definition files to import-compatible format.
    """

    def __init__(self):
        # Script can be run from project root or scripts directory
        current_dir = Path.cwd()

        # If we're in scripts directory, go up one level
        if current_dir.name == "scripts":
            self.base_dir = current_dir.parent
        else:
            self.base_dir = current_dir

        self.source_dir = self.base_dir / "knowledge" / "canon" / "lore" / "timeline-author" / "quests" / "america" / "miami"
        self.output_dir = self.base_dir / "knowledge" / "canon" / "narrative" / "quests"
        self.output_dir.mkdir(parents=True, exist_ok=True)

        print(f"Looking in: {self.source_dir}")

    def convert_all_quests(self) -> List[str]:
        """Convert all Miami quest-definition files to import format."""
        converted_files = []

        # Find all Miami quest files in subdirectories
        quest_files = []
        for year_dir in self.source_dir.glob("*"):
            if year_dir.is_dir():
                quest_files.extend(list(year_dir.glob("quest-*.yaml")))

        quest_files.sort()

        print(f"Found {len(quest_files)} Miami quest files to convert")

        for quest_file in quest_files:
            try:
                converted_file = self.convert_quest_file(quest_file)
                if converted_file:
                    converted_files.append(converted_file)
                    print(f"[OK] Converted {quest_file.name} -> {converted_file.name}")
                else:
                    print(f"[ERROR] Failed to convert {quest_file.name}")
            except Exception as e:
                print(f"[ERROR] Exception converting {quest_file.name}: {e}")

        print(f"\n[SUMMARY] Conversion completed:")
        print(f"  Successfully converted: {len(converted_files)}")
        print(f"  Failed: {len(quest_files) - len(converted_files)}")

        return converted_files

    def convert_quest_file(self, quest_file: Path) -> Optional[Path]:
        """Convert a single quest file to import format."""
        try:
            # Read the original quest file
            with open(quest_file, 'r', encoding='utf-8') as f:
                quest_data = yaml.safe_load(f)

            # Create new structure for import
            import_data = self.transform_quest_data(quest_data, quest_file.name)

            # Generate output filename
            base_name = quest_file.stem.replace('_', '-')
            output_filename = f"{base_name}_import.yaml"
            output_file = self.output_dir / output_filename

            # Write the converted file
            with open(output_file, 'w', encoding='utf-8') as f:
                yaml.dump(import_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

            return output_file

        except Exception as e:
            print(f"Error converting {quest_file.name}: {e}")
            return None

    def transform_quest_data(self, original_data: Dict[str, Any], filename: str) -> Dict[str, Any]:
        """Transform quest data from timeline-author format to import format."""
        # Extract basic metadata
        metadata = original_data.get('metadata', {})

        # Build new structure
        import_data = {
            'metadata': {
                'id': f"canon-narrative-quests-{filename.replace('.yaml', '').replace('_', '-')}",
                'title': metadata.get('title', original_data.get('title', 'Unknown Quest')),
                'english_title': metadata.get('english_title', metadata.get('title', 'Unknown Quest')),
                'type': self.infer_quest_type(metadata, original_data),
                'location': 'Miami',
                'time_period': self.extract_time_period(filename),
                'difficulty': metadata.get('difficulty', 'medium'),
                'estimated_duration': metadata.get('duration', '6 часов'),
                'player_level': self.extract_level_range(metadata),
                'tags': self.generate_tags(metadata, original_data)
            },
            'quest_definition': {
                'status': 'active',
                'level_min': self.extract_min_level(metadata),
                'level_max': self.extract_max_level(metadata),
                'rewards': self.extract_rewards(metadata, original_data),
                'objectives': self.extract_objectives(original_data),
            }
        }

        # Add narrative elements if available
        if 'narrative_context' in original_data:
            import_data.update(self.extract_narrative_elements(original_data))

        return import_data

    def infer_quest_type(self, metadata: Dict, original_data: Dict) -> str:
        """Infer quest type from metadata and content."""
        title = metadata.get('title', '').lower()
        if any(word in title for word in ['race', 'speedboat', 'racer']):
            return 'racing_drama'
        elif any(word in title for word in ['art', 'gallery', 'basel']):
            return 'cultural_exploration'
        elif any(word in title for word in ['hurricane', 'survival']):
            return 'survival_drama'
        elif any(word in title for word in ['cyber', 'hack', 'neural']):
            return 'cyberpunk_adventure'
        elif any(word in title for word in ['drug', 'run']):
            return 'crime_drama'
        else:
            return 'exploration_adventure'

    def extract_time_period(self, filename: str) -> str:
        """Extract time period from filename."""
        if '2078-2093' in filename:
            return '2078-2093'
        elif '2020-2029' in filename:
            return '2020-2029'
        else:
            return '2020-2093'

    def extract_level_range(self, metadata: Dict) -> str:
        """Extract level range for players."""
        min_level = metadata.get('level_min', 15)
        max_level = metadata.get('level_max', 35)
        return f"{min_level}-{max_level}"

    def extract_min_level(self, metadata: Dict) -> int:
        """Extract minimum level."""
        return metadata.get('level_min', 15)

    def extract_max_level(self, metadata: Dict) -> int:
        """Extract maximum level."""
        return metadata.get('level_max', 35)

    def generate_tags(self, metadata: Dict, original_data: Dict) -> List[str]:
        """Generate tags for the quest."""
        tags = ['miami', 'america']
        title = metadata.get('title', '').lower()

        if any(word in title for word in ['beach', 'ocean', 'bay']):
            tags.extend(['beach', 'ocean', 'water'])
        if any(word in title for word in ['art', 'cultural', 'museum']):
            tags.extend(['art', 'culture', 'cultural'])
        if any(word in title for word in ['race', 'speedboat']):
            tags.extend(['racing', 'boats', 'speed'])
        if any(word in title for word in ['cyber', 'hack', 'neural']):
            tags.extend(['cyberpunk', 'technology', 'hacking'])
        if any(word in title for word in ['hurricane', 'storm']):
            tags.extend(['survival', 'weather', 'disaster'])
        if any(word in title for word in ['drug', 'crime']):
            tags.extend(['crime', 'underground', 'dangerous'])

        return tags

    def extract_rewards(self, metadata: Dict, original_data: Dict) -> Dict[str, Any]:
        """Extract rewards information."""
        return {
            'xp': metadata.get('xp_reward', 5000),
            'currency': metadata.get('currency_reward', 2000),
            'reputation': metadata.get('reputation_rewards', {}),
            'unlocks': {
                'achievements': [metadata.get('achievement', 'miami_explorer')],
                'items': metadata.get('item_rewards', [])
            }
        }

    def extract_objectives(self, original_data: Dict) -> List[str]:
        """Extract quest objectives."""
        objectives = original_data.get('objectives', [])
        if not objectives:
            # Generate default objectives based on title
            title = original_data.get('metadata', {}).get('title', '')
            objectives = [
                f"Исследовать {title.lower()}",
                "Выполнить основную задачу квеста",
                "Вернуться с результатами"
            ]
        return objectives

    def extract_narrative_elements(self, original_data: Dict) -> Dict[str, Any]:
        """Extract narrative elements from original data."""
        narrative = {}

        if 'narrative_context' in original_data:
            context = original_data['narrative_context']
            narrative['narrative_context'] = {
                'background': context.get('background', ''),
                'plot_hook': context.get('plot_hook', ''),
                'key_events': context.get('key_events', [])
            }

        if 'gameplay_mechanics' in original_data:
            narrative['gameplay_mechanics'] = original_data['gameplay_mechanics']

        if 'additional_npcs' in original_data:
            narrative['additional_npcs'] = original_data['additional_npcs']

        return narrative


def main():
    """Main function to run the converter."""
    print("Starting Miami quest conversion...")

    converter = MiamiQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\nConversion completed. Converted {len(converted_files)} files.")
    print("Run batch-import-miami-quests.py to import them to database.")


if __name__ == "__main__":
    main()
