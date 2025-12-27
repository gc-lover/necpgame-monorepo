#!/usr/bin/env python3
"""
Convert Seoul quest files to include quest_definition sections
"""

import os
import yaml
from pathlib import Path

def add_quest_definition_to_file(file_path):
    """Add quest_definition section to a Seoul quest file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        # Skip if already has quest_definition
        if 'quest_definition' in data:
            print(f"Skipping {file_path} - already has quest_definition")
            return

        # Extract quest info from content
        summary = data.get('summary', {})
        key_points = summary.get('key_points', [])

        # Basic quest definition template
        quest_def = {
            'quest_type': 'side',
            'level_min': 1,
            'level_max': None,
            'requirements': {
                'required_quests': [],
                'required_flags': [],
                'required_reputation': {},
                'required_items': []
            },
            'objectives': [
                {
                    'id': 'main_objective',
                    'text': 'Complete the main quest objectives',
                    'type': 'interact',
                    'target': 'quest_target',
                    'count': 1,
                    'optional': False
                }
            ],
            'rewards': {
                'xp': 1000,
                'money': 1000
            }
        }

        # Insert quest_definition before content
        content_index = None
        for i, (key, value) in enumerate(data.items()):
            if key == 'content':
                content_index = i
                break

        if content_index is not None:
            # Insert before content
            items = list(data.items())
            items.insert(content_index, ('quest_definition', quest_def))
            data = dict(items)
        else:
            # Append at end
            data['quest_definition'] = quest_def

        # Write back
        with open(file_path, 'w', encoding='utf-8') as f:
            yaml.safe_dump(data, f, default_flow_style=False, allow_unicode=True, indent=2)

        print(f"Added quest_definition to {file_path}")

    except Exception as e:
        print(f"Error processing {file_path}: {e}")

def main():
    seoul_dir = Path("knowledge/canon/lore/timeline-author/quests/asia/seoul")

    # Process all YAML files in seoul directories
    for yaml_file in seoul_dir.rglob("*.yaml"):
        if yaml_file.is_file():
            add_quest_definition_to_file(yaml_file)

if __name__ == "__main__":
    main()
