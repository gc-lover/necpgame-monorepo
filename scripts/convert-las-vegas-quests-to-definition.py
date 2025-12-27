#!/usr/bin/env python3
"""
Convert Las Vegas quest-definition files to import-compatible format for database import.

Issue: Convert Las Vegas quests to proper import format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List, Optional

class LasVegasQuestConverter:
    """
    Convert Las Vegas quest-definition files to import-compatible format.
    """

    def __init__(self):
        self.source_dir = Path("knowledge/canon/lore/timeline-author/quests/america/las-vegas")
        self.output_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def convert_all_quests(self) -> List[str]:
        """Convert all Las Vegas quest-definition files to import format."""
        converted_files = []

        # Find all Las Vegas quest files in subdirectories
        quest_files = []
        for year_dir in self.source_dir.glob("*"):
            if year_dir.is_dir():
                quest_files.extend(list(year_dir.glob("quest-*.yaml")))

        quest_files.sort()

        print(f"Found {len(quest_files)} Las Vegas quest files to convert")

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

        # Skip if already converted (check for existing import file)
        if output_file.exists():
            print(f"[SKIP] Already converted: {output_file.name}")
            return output_file

        # Create import-compatible structure
        import_data = {
            'metadata': {
                'id': quest_id,
                'title': data.get('metadata', {}).get('title', ''),
                'english_title': data.get('metadata', {}).get('title', ''),
                'type': 'casino',
                'location': 'Las Vegas',
                'time_period': self.extract_time_period(quest_file),
                'difficulty': 'medium',
                'estimated_duration': '2-4 hours',
                'player_level': '15-30',
                'tags': data.get('metadata', {}).get('tags', [])
            },
            'quest_definition': {
                'status': 'active',
                'level_min': 15,
                'level_max': 30,
                'rewards': {
                    'xp': 5500,
                    'currency': 18000,
                    'reputation': {
                        'vegas_high_roller': 20,
                        'entertainment_industry': 25,
                        'desert_nomad': 15
                    },
                    'unlocks': {
                        'achievements': ['vegas_legend'],
                        'flags': [f'vegas_{base_id.replace("-", "_")}'],
                        'items': ['casino_chip', 'show_ticket']
                    }
                },
                'objectives': self.extract_objectives(data),
                'description': data.get('summary', {}).get('problem', ''),
                'duration_hours': 3,
                'group_size': '1-4',
                'difficulty': 'medium',
                'type': 'casino',
                'faction_affinity': {
                    'corporate': 20,
                    'street': 30,
                    'nomad': 10
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
                'locations': ['Las Vegas Strip', 'Casino floors', 'Desert outskirts'],
                'npcs': self.extract_related_npcs(data),
                'items': []
            },
            'visual_design': {
                'atmosphere': 'Neon lights, slot machines, desert heat',
                'soundtrack': 'Jazz, show tunes, electronic beats',
                'key_visuals': [
                    'Flaming fountains',
                    'Casino tables',
                    'Show performances'
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
                "Explore Las Vegas landmarks",
                "Participate in casino activities",
                "Interact with entertainment industry",
                "Solve desert challenges"
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
                    'role': char.get('role', 'Casino worker'),
                    'location': 'Las Vegas',
                    'personality': char.get('personality', 'Entertaining'),
                    'dialogues': {
                        'introduction': char.get('background', 'Welcome to Vegas!')
                    }
                }
                npcs.append(npc)

        # Add default NPCs if none found
        if not npcs:
            npcs = [{
                'name': 'Casino Host',
                'role': 'Entertainment coordinator',
                'location': 'Casino floor',
                'personality': 'Charismatic and energetic',
                'dialogues': {
                    'introduction': 'Welcome to the greatest show on Earth!'
                }
            }]

        return npcs[:3]  # Limit to 3 NPCs

    def extract_phases(self, data: Dict[str, Any]) -> List[Dict[str, Any]]:
        """Extract quest phases."""
        return [{
            'name': 'Arrival',
            'objectives': ['Arrive in Las Vegas', 'Get oriented'],
            'duration': '1 hour',
            'rewards': 'Local knowledge'
        }, {
            'name': 'Gambling',
            'objectives': ['Try casino games', 'Build reputation'],
            'duration': '1.5 hours',
            'rewards': 'Casino credits'
        }, {
            'name': 'Entertainment',
            'objectives': ['Attend shows', 'Network with industry'],
            'duration': '1 hour',
            'rewards': 'Show tickets and connections'
        }]

    def extract_outcomes(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Extract quest outcomes."""
        return {
            'jackpot': {
                'description': 'Major win at casino',
                'rewards': {
                    'xp': 7000,
                    'currency': 25000,
                    'reputation': 30
                },
                'next_quests': ['vegas_empire']
            },
            'moderate_success': {
                'description': 'Good experience with moderate wins',
                'rewards': {
                    'xp': 5500,
                    'currency': 18000,
                    'reputation': 20
                }
            },
            'bust': {
                'description': 'Lost everything at casino',
                'penalties': {
                    'currency': -5000,
                    'reputation': -15
                },
                'next_quests': ['vegas_redemption']
            }
        }

    def extract_related_npcs(self, data: Dict[str, Any]) -> List[str]:
        """Extract related NPC names."""
        npcs = []
        characters = data.get('quest', {}).get('characters', [])
        for char in characters:
            if isinstance(char, dict) and 'name' in char:
                npcs.append(char['name'])
        return npcs[:3] if npcs else ['Casino Host']


def main():
    """Main function."""
    print("Starting Las Vegas quest conversion...")

    converter = LasVegasQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\nConversion completed. Converted {len(converted_files)} files.")
    print("Run batch-import-las-vegas-quests.py to import them to database.")


if __name__ == "__main__":
    main()
