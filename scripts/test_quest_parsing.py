#!/usr/bin/env python3
"""
Test quest parsing
"""

import yaml
import json
from pathlib import Path

# Test parsing one quest file
quest_file = 'knowledge/canon/narrative/quests/wynwood-walls-miami-2020-2029.yaml'
quest_path = Path(quest_file)

print(f'Testing quest file: {quest_path}')
print(f'File exists: {quest_path.exists()}')

if quest_path.exists():
    with open(quest_path, 'r', encoding='utf-8') as f:
        data = yaml.safe_load(f)

    metadata = data.get('metadata', {})
    quest_def = data.get('quest_definition', {})

    print(f'Quest ID: {metadata.get("id")}')
    print(f'Title: {metadata.get("title")}')
    print(f'Level min: {quest_def.get("level_min")}')
    print(f'Level max: {quest_def.get("level_max")}')
    print('Quest parsing successful!')
else:
    print('Quest file not found')