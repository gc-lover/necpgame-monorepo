#!/usr/bin/env python3
"""
Convert Seoul quest-definition files to import-compatible format for database import.

Issue: Convert Seoul quests to proper import format
"""

import os
import yaml
from pathlib import Path
from typing import Dict, Any, List, Optional

class SeoulQuestConverter:
    """
    Convert Seoul quest-definition files to import-compatible format.
    """

    def __init__(self):
        self.source_dir = Path("knowledge/canon/lore/timeline-author/quests/asia/seoul")
        self.output_dir = Path("knowledge/canon/narrative/quests")
        self.output_dir.mkdir(parents=True, exist_ok=True)

    def convert_all_quests(self) -> List[str]:
        """Convert all Seoul quest-definition files to import format."""
        converted_files = []

        # Find all Seoul quest files in subdirectories
        quest_files = []
        for year_dir in self.source_dir.glob("*"):
            if year_dir.is_dir():
                quest_files.extend(list(year_dir.glob("quest-*.yaml")))

        quest_files.sort()

        print(f"Found {len(quest_files)} Seoul quest files to convert")

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
                'type': 'cultural',
                'location': 'Seoul',
                'time_period': self.extract_time_period(quest_file),
                'difficulty': 'medium',
                'estimated_duration': '2-4 hours',
                'player_level': '12-25',
                'tags': data.get('metadata', {}).get('tags', [])
            },
            'quest_definition': {
                'status': 'active',
                'level_min': 12,
                'level_max': 25,
                'rewards': {
                    'xp': 6000,
                    'currency': 18000,
                    'reputation': {
                        'korean_culture': 25,
                        'asian_traditions': 20,
                        'modern_asia': 30
                    },
                    'unlocks': {
                        'achievements': ['seoul_explorer'],
                        'flags': [f'seoul_{base_id.replace("-", "_")}'],
                        'items': ['korean_cultural_artifact']
                    }
                },
                'objectives': self.extract_objectives(data),
                'description': data.get('summary', {}).get('problem', ''),
                'duration_hours': 3,
                'group_size': '1-3',
                'difficulty': 'medium',
                'type': 'cultural',
                'faction_affinity': {
                    'corporate': 20,
                    'street': 25,
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
                'locations': ['Seoul landmarks', 'Korean cultural sites'],
                'npcs': self.extract_related_npcs(data),
                'items': []
            },
            'visual_design': {
                'atmosphere': 'Vibrant Korean city life with traditional-modern blend',
                'soundtrack': 'K-pop mixed with traditional Korean music',
                'key_visuals': [
                    'Neon lights of Gangnam',
                    'Ancient palaces',
                    'Modern skyscrapers'
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
                "Explore Seoul cultural landmarks",
                "Experience Korean traditions",
                "Interact with local community",
                "Learn about Korean history"
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
                    'location': 'Seoul',
                    'personality': char.get('personality', 'Friendly'),
                    'dialogues': {
                        'introduction': char.get('background', 'Welcome to Seoul!')
                    }
                }
                npcs.append(npc)

        # Add default NPCs if none found
        if not npcs:
            npcs = [{
                'name': 'Local Guide',
                'role': 'Cultural guide',
                'location': 'Seoul downtown',
                'personality': 'Knowledgeable and enthusiastic',
                'dialogues': {
                    'introduction': 'Let me show you the wonders of Seoul!'
                }
            }]

        return npcs[:3]  # Limit to 3 NPCs

    def extract_phases(self, data: Dict[str, Any]) -> List[Dict[str, Any]]:
        """Extract quest phases."""
        return [{
            'name': 'Arrival',
            'objectives': ['Arrive in Seoul', 'Get oriented'],
            'duration': '1 hour',
            'rewards': 'Local knowledge'
        }, {
            'name': 'Exploration',
            'objectives': ['Visit key locations', 'Experience culture'],
            'duration': '1.5 hours',
            'rewards': 'Cultural insights'
        }, {
            'name': 'Immersion',
            'objectives': ['Deep dive into traditions', 'Complete main tasks'],
            'duration': '1 hour',
            'rewards': 'Cultural mastery'
        }]

    def extract_outcomes(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Extract quest outcomes."""
        return {
            'cultural_enlightenment': {
                'description': 'Deep understanding of Korean culture',
                'rewards': {
                    'xp': 7500,
                    'currency': 22000,
                    'reputation': 35
                },
                'next_quests': ['korean_mastery']
            },
            'good_experience': {
                'description': 'Enjoyable cultural experience',
                'rewards': {
                    'xp': 6000,
                    'currency': 18000,
                    'reputation': 25
                }
            },
            'superficial_visit': {
                'description': 'Basic tourist experience',
                'rewards': {
                    'xp': 4500,
                    'currency': 14000,
                    'reputation': 15
                },
                'next_quests': ['deeper_cultural_dive']
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
    print("Starting Seoul quest conversion...")

    converter = SeoulQuestConverter()
    converted_files = converter.convert_all_quests()

    print(f"\nConversion completed. Converted {len(converted_files)} files.")
    print("Run batch-import-seoul-quests.py to import them to database.")


if __name__ == "__main__":
    main()
