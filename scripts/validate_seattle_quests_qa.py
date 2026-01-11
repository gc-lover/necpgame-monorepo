#!/usr/bin/env python3
"""
QA Validation for Seattle 2020-2029 Quests Import
Issue: #2249 - Backend completed quest import
"""

import json
from pathlib import Path

def validate_seattle_quests():
    """Validate Seattle quests import data quality"""

    print("=" * 60)
    print("QA VALIDATION: Seattle 2020-2029 Quests Import")
    print("=" * 60)

    # Check migration file
    migration_file = Path("infrastructure/liquibase/migrations/data/quests/V008__import_new_seattle_2020_2029_quests.sql")

    if not migration_file.exists():
        print("[ERROR] MIGRATION FILE MISSING")
        return False

    print("[OK] Migration file found")

    # Read migration content
    with open(migration_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Extract quest data
    quests_data = []
    lines = content.split('\n')

    current_quest = None
    for i, line in enumerate(lines):
        line = line.strip()

        if line.startswith("-- Quest:"):
            if current_quest:
                quests_data.append(current_quest)

            quest_name = line.replace("-- Quest:", "").strip()
            current_quest = {
                'name': quest_name,
                'data': [],
                'line_start': i + 1
            }

        elif current_quest and line.startswith("VALUES ("):
            # Start collecting VALUES data
            current_quest['data'].append(line)

        elif current_quest and line.startswith("'"):
            # Continue collecting data lines
            current_quest['data'].append(line)

        elif current_quest and line.startswith(");"):
            # End of VALUES block
            current_quest['data'].append(line)

    if current_quest:
        quests_data.append(current_quest)

    print(f"[OK] Found {len(quests_data)} quests in migration file")

    # Validate each quest
    validation_results = []
    total_rewards = {'experience': 0, 'currency': 0, 'items': 0, 'reputation': 0}

    for quest in quests_data:
        quest_result = validate_single_quest(quest)
        validation_results.append(quest_result)

        # Aggregate rewards
        if quest_result['valid'] and quest_result['rewards']:
            rewards = quest_result['rewards']
            total_rewards['experience'] += rewards.get('experience', 0)
            total_rewards['currency'] += rewards.get('currency', {}).get('amount', 0)
            total_rewards['items'] += len(rewards.get('items', []))
            if 'reputation' in rewards:
                total_rewards['reputation'] += 1

    # Summary
    print("\n" + "=" * 60)
    print("VALIDATION SUMMARY")
    print("=" * 60)

    valid_quests = sum(1 for r in validation_results if r['valid'])
    print(f"Valid quests: {valid_quests}/{len(quests_data)}")

    if valid_quests == len(quests_data):
        print("[OK] ALL QUESTS PASSED VALIDATION")

        print("\nTOTAL REWARDS SUMMARY:")
        print(f"  - Experience: {total_rewards['experience']}")
        print(f"  - Currency: {total_rewards['currency']} eddies")
        print(f"  - Items: {total_rewards['items']} total")
        print(f"  - Reputation changes: {total_rewards['reputation']} quests")

        print("\n[OK] QA VALIDATION PASSED")
        print("Issue #2249 - Ready for production deployment")

        return True
    else:
        print("[ERROR] SOME QUESTS FAILED VALIDATION")
        for result in validation_results:
            if not result['valid']:
                print(f"  - {result['name']}: {result['errors']}")

        return False

def validate_single_quest(quest):
    """Validate a single quest structure"""

    result = {
        'name': quest['name'],
        'valid': True,
        'errors': [],
        'rewards': {}
    }

    try:
        # Combine all data lines
        data_lines = '\n'.join(quest['data'])

        # Extract quest_id
        if "'ai-rights-movement-seattle-2020-2029'" in data_lines:
            quest_id = 'ai-rights-movement-seattle-2020-2029'
        elif "'orbital-access-card'" in data_lines:
            quest_id = 'corporate-shadow-wars-seattle-2020-2029'
        elif "'quantum-data-core'" in data_lines:
            quest_id = 'data-divers-guild-seattle-2020-2029'
        elif "'sonic-emitter'" in data_lines:
            quest_id = 'underground-artists-seattle-2020-2029'
        elif "'bioluminescent-seed'" in data_lines:
            quest_id = 'eco-warriors-seattle-2020-2029'
        elif "'neural-hack-interface'" in data_lines:
            quest_id = 'hackers-guild-seattle-2020-2029'
        elif "'corporate-intel-chip'" in data_lines:
            quest_id = 'corporate-spy-seattle-2020-2029'
        elif "'premium-coffee-blend'" in data_lines:
            quest_id = 'coffee-enthusiasts-seattle-2020-2029'
        else:
            result['valid'] = False
            result['errors'].append("Could not identify quest_id")
            return result

        # Validate rewards JSON
        rewards_start = data_lines.find('{"experience":')
        rewards_end = data_lines.find('}', rewards_start) + 1

        if rewards_start == -1:
            result['valid'] = False
            result['errors'].append("Rewards JSON not found")
            return result

        rewards_json = data_lines[rewards_start:rewards_end]
        rewards = json.loads(rewards_json)

        result['rewards'] = rewards

        # Validate rewards structure
        if 'experience' not in rewards:
            result['errors'].append("Missing experience reward")
            result['valid'] = False

        if 'currency' not in rewards:
            result['errors'].append("Missing currency reward")
            result['valid'] = False
        elif not isinstance(rewards['currency'], dict) or 'amount' not in rewards['currency']:
            result['errors'].append("Invalid currency structure")
            result['valid'] = False

        if 'items' not in rewards:
            result['errors'].append("Missing items reward")
            result['valid'] = False

        # Validate objectives JSON
        objectives_start = data_lines.find('[{"id":')
        objectives_end = data_lines.find(']', objectives_start) + 1

        if objectives_start == -1:
            result['valid'] = False
            result['errors'].append("Objectives JSON not found")
            return result

        objectives_json = data_lines[objectives_start:objectives_end]
        objectives = json.loads(objectives_json)

        if len(objectives) < 2:
            result['errors'].append("Quest should have at least 2 objectives")
            result['valid'] = False

        # Validate location and time period
        if 'Seattle' not in data_lines:
            result['errors'].append("Location should contain 'Seattle'")
            result['valid'] = False

        if '2020-2029' not in data_lines:
            result['errors'].append("Time period should be '2020-2029'")
            result['valid'] = False

    except json.JSONDecodeError as e:
        result['valid'] = False
        result['errors'].append(f"JSON parsing error: {e}")

    except Exception as e:
        result['valid'] = False
        result['errors'].append(f"Validation error: {e}")

    return result

if __name__ == "__main__":
    success = validate_seattle_quests()
    exit(0 if success else 1)