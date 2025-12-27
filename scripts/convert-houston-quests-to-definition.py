#!/usr/bin/env python3
"""
Convert Houston quest-definition files to import-compatible format for database import.

Issue: Convert Houston quests to proper import format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List, Optional

class HoustonQuestConverter:
    """
    Convert Houston quest-definition files to import-compatible format.
    """

    def __init__(self):
        self.source_dir = Path("knowledge/canon/lore/timeline-author/quests/america/houston")
        self.output_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def convert_all_quests(self) -> List[str]:
        """Convert all Houston quest-definition files to import format."""
        converted_files = []

        # Find all Houston quest files in subdirectories
        quest_files = []
        for year_dir in self.source_dir.glob("*"):
            if year_dir.is_dir():
                quest_files.extend(list(year_dir.glob("quest-*.yaml")))

        quest_files.sort()

        print(f"Found {len(quest_files)} Houston quest files to convert")

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
        """Convert single quest file to import format."""
        with open(quest_file, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        if not data:
            return None

        # Extract quest ID from metadata
        quest_id = data.get('metadata', {}).get('id', '')
        if not quest_id:
            return None

        # Remove 'canon-quest-' prefix and create new filename
        base_id = quest_id.replace('canon-quest-', '')
        output_filename = f"{base_id}_import.yaml"
        output_file = self.output_dir / output_filename

        # Create import-compatible structure
        import_data = {
            'metadata': {
                'id': quest_id,
                'title': data.get('metadata', {}).get('title', ''),
                'english_title': data.get('metadata', {}).get('title', ''),
                'type': 'exploration',
                'location': 'Houston',
                'time_period': self.extract_time_period(quest_file),
                'difficulty': 'medium',
                'estimated_duration': '2-4 hours',
                'player_level': '10-25',
                'tags': data.get('metadata', {}).get('tags', [])
            },
            'quest_definition': {
                'status': 'active',
                'level_min': 10,
                'level_max': 25,
                'rewards': {
                    'xp': 5000,
                    'currency': 15000,
                    'reputation': {
                        'houston_community': 20,
                        'american_heritage': 15,
                        'explorers_guild': 25
                    },
                    'unlocks': {
                        'achievements': ['houston_explorer'],
                        'flags': [f'houston_{base_id.replace("-", "_")}'],
                        'items': ['houston_memento']
                    }
                },
                'objectives': self.extract_objectives(data),
                'description': data.get('summary', {}).get('problem', ''),
                'duration_hours': 3,
                'group_size': '1-3',
                'difficulty': 'medium',
                'type': 'exploration',
                'faction_affinity': {
                    'corporate': 10,
                    'street': 5,
                    'nomad': 15
                }
            },
            'narrative_context': {
                'background': data.get('quest', {}).get('background', ''),
                'themes': data.get('quest', {}).get('themes', [])
            },
            'npc_interactions': self.extract_npcs(data),
            'quest_phases': self.extract_phases(data),
            'outcomes': self.extract_outcomes(data),
            'related_content': {
                'locations': ['Houston, Texas'],
                'npcs': self.extract_related_npcs(data),
                'items': []
            },
            'visual_design': {
                'atmosphere': 'Hot and humid Texas climate',
                'soundtrack': 'American folk and space exploration themes',
                'key_visuals': [
                    'NASA Space Center',
                    'Texas landscape',
                    'Space artifacts'
                ]
            }
        }

        # Write converted data
        with open(output_file, 'w', encoding='utf-8') as f:
            yaml.dump(import_data, f, default_flow_style=False, allow_unicode=True, sort_keys=False)

        return output_file

    def extract_time_period(self, quest_file: Path) -> str:
        """Extract time period from file path."""
        path_parts = quest_file.parts
        for part in path_parts:
            if '-' in part and len(part.split('-')) == 2:
                try:
                    start, end = part.split('-')
                    if len(start) == 4 and len(end) == 4:
                        return f"{start}-{end}"
                except:
                    pass
        return "2020-2029"

    def extract_objectives(self, data: Dict[str, Any]) -> List[str]:
        """Extract objectives from quest data."""
        objectives = []

        # Try to extract from quest structure
        quest_data = data.get('quest', {})
        if 'objectives' in quest_data:
            for obj in quest_data['objectives']:
                if isinstance(obj, dict) and 'description' in obj:
                    objectives.append(obj['description'])
                elif isinstance(obj, str):
                    objectives.append(obj)

        # If no objectives found, create default ones
        if not objectives:
            objectives = [
                "Explore Houston landmarks",
                "Complete historical research",
                "Interact with local NPCs",
                "Solve local challenges"
            ]

        return objectives[:4]  # Limit to 4 objectives

    def extract_npcs(self, data: Dict[str, Any]) -> List[Dict[str, Any]]:
        """Extract NPC interactions from quest data."""
        npcs = []

        # Try to extract from quest characters
        characters = data.get('quest', {}).get('characters', [])
        for char in characters:
            if isinstance(char, dict):
                npc = {
                    'name': char.get('name', 'Unknown NPC'),
                    'role': char.get('role', 'Local resident'),
                    'location': 'Houston',
                    'personality': char.get('personality', 'Friendly'),
                    'dialogues': {
                        'introduction': char.get('background', 'Hello, stranger!')
                    }
                }
                npcs.append(npc)

        # Add default NPCs if none found
        if not npcs:
            npcs = [{
                'name': 'Local Guide',
                'role': 'Tour guide',
                'location': 'Houston downtown',
                'personality': 'Knowledgeable and enthusiastic',
                'dialogues': {
                    'introduction': 'Welcome to Houston! Let me show you around.'
                }
            }]

        return npcs[:3]  # Limit to 3 NPCs

    def extract_phases(self, data: Dict[str, Any]) -> List[Dict[str, Any]]:
        """Extract quest phases."""
        return [{
            'name': 'Exploration',
            'objectives': ['Explore the area', 'Gather information'],
            'duration': '1 hour',
            'rewards': 'Knowledge and experience'
        }, {
            'name': 'Investigation',
            'objectives': ['Investigate key locations', 'Solve puzzles'],
            'duration': '1.5 hours',
            'rewards': 'Clues and artifacts'
        }, {
            'name': 'Resolution',
            'objectives': ['Complete main objectives', 'Wrap up story'],
            'duration': '30 minutes',
            'rewards': 'Final rewards and closure'
        }]

    def extract_outcomes(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Extract quest outcomes."""
        return {
            'success': {
                'description': 'Quest completed successfully',
                'rewards': {
                    'xp': 6000,
                    'currency': 18000,
                    'reputation': 25
                },
                'next_quests': ['houston_followup']
            },
            'failure': {
                'description': 'Quest failed',
                'penalties': {
                    'reputation': -10
                },
                'next_quests': ['houston_retry']
            }
        }

    def extract_related_npcs(self, data: Dict[str, Any]) -> List[str]:
        """Extract related NPC names."""
        npcs = []
        characters = data.get('quest', {}).get('characters', [])
        for char in characters:
            if isinstance(char, dict) and 'name' in char:
                npcs.append(char['name'])
        return npcs[:3] if npcs else ['Local Guide']


def main():
    """Main function."""
    print("Starting Houston quest conversion...")

    converter = HoustonQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\nConversion completed. Converted {len(converted_files)} files.")
    print("Run batch-import-houston-quests.py to import them to database.")


if __name__ == "__main__":
    main()
