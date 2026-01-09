#!/usr/bin/env python3
"""
Check which Seattle 2020-2029 quests are missing from database.
"""

import os
import sys
import yaml
from pathlib import Path

# Add scripts directory to Python path
scripts_dir = Path(__file__).parent
sys.path.insert(0, str(scripts_dir))

def check_missing_quests():
    """Check which quests are missing from database."""

    # Quest files to check (001-010, 016-039)
    quest_files = [
        "quest-001-space-needle.yaml",
        "quest-002-pike-place-market.yaml",
        "quest-003-starbucks-origin.yaml",
        "quest-004-grunge-music.yaml",
        "quest-005-amazon-hq.yaml",
        "quest-006-mount-rainier.yaml",
        "quest-007-rain-rain-rain.yaml",
        "quest-008-boeing-factory.yaml",
        "quest-009-seafood-salmon.yaml",
        "quest-010-tech-boom-gentrification.yaml",
        "quest-016-rain-city-underground.yaml",
        "quest-017-ghost-in-the-cloud.yaml",
        "quest-018-pacific-gateway.yaml",
        "quest-019-emergent-ecologies.yaml",
        "quest-020-shadow-economy-empire.yaml",
        "quest-021-virtual-reality-research.yaml",
        "quest-022-floating-cities-vision.yaml",
        "quest-023-neural-implant-revolution.yaml",
        "quest-024-cyberpunk-music-revolution.yaml",
        "quest-025-shadow-economy-empire.yaml",
        "quest-031-neural-net-hackers.yaml",
        "quest-032-corporate-implant-theft.yaml",
        "quest-033-virtual-reality-addiction.yaml",
        "quest-034-corporate-warfare-shadows.yaml",
        "quest-035-climate-refugee-crisis.yaml",
        "quest-036-underground-informant.yaml",
        "quest-037-corporate-blackmail.yaml",
        "quest-038-corporate-protest.yaml",
        "quest-039-faction-alliance.yaml"
    ]

    quest_dir = Path("knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029")

    missing_quests = []
    existing_quests = []

    for quest_file in quest_files:
        file_path = quest_dir / quest_file
        if not file_path.exists():
            print(f"File not found: {quest_file}")
            continue

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            title = data.get('metadata', {}).get('title', 'Unknown')

            # Check if quest exists in database
            import subprocess
            result = subprocess.run([
                'docker', 'exec', 'necpgame-postgres-1', 'psql', '-U', 'postgres',
                '-d', 'necpgame', '-c',
                f"SELECT COUNT(*) FROM gameplay.quest_definitions WHERE title = '{title.replace("'", "''")}';"
            ], capture_output=True, text=True)

            count = 0
            for line in result.stdout.split('\n'):
                if line.strip().isdigit():
                    count = int(line.strip())
                    break

            if count == 0:
                missing_quests.append((quest_file, title))
                print(f"MISSING: {quest_file} - {title}")
            else:
                existing_quests.append((quest_file, title))
                print(f"EXISTS: {quest_file} - {title}")

        except Exception as e:
            print(f"Error processing {quest_file}: {e}")

    print(f"\nSummary:")
    print(f"Missing quests: {len(missing_quests)}")
    print(f"Existing quests: {len(existing_quests)}")

    return missing_quests

if __name__ == "__main__":
    check_missing_quests()





