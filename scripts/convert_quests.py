import re
import json

def convert_v1_84_to_quests():
    v1_84_path = r'c:\NECPGAME\infrastructure\liquibase\migrations\V1_84__seattle_quests_016_039_import.sql'
    output_path = r'c:\NECPGAME\infrastructure\liquibase\migrations\data\quests\V007__import_seattle_quests_017_039.sql'

    with open(v1_84_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find all INSERT INTO gameplay.quest_definitions statements
    pattern = re.compile(r"INSERT INTO gameplay\.quest_definitions \((.*?)\) VALUES \((.*?)\) ON CONFLICT \(quest_id\) DO UPDATE SET.*?;", re.DOTALL)
    matches = pattern.findall(content)

    new_statements = []
    
    # We want 017-039. match[1] contains the values.
    # 016 is the first one. Let's skip it if it's 016.
    
    for columns_str, values_str in matches:
        columns = [c.strip() for c in columns_str.split(',')]
        
        # The values are in order: quest_id, title, description, difficulty, level_min, level_max
        # We can extract them by finding all strings in single quotes
        strings = re.findall(r"'(.*?)'", values_str, re.DOTALL)
        if len(strings) < 3:
            continue
            
        quest_id = strings[0]
        title = strings[1]
        description = strings[2]
        
        if quest_id == 'quest-016-rain-city-underground':
            continue # already in V005
            
        # level_min/max are usually outside quotes
        # Find numeric values
        nums = re.findall(r",\s*(\d+|NULL)", values_str)
        if len(nums) >= 2:
            # The first two numbers after the strings should be difficulty (string) then level_min, level_max
            # Actually, difficulty is a string. So nums[0] might be level_min.
            # Let's look for them after the 4th string (difficulty)
            level_min = nums[0] if len(nums) > 0 else 1
            level_max = nums[1] if len(nums) > 1 else 'NULL'
        else:
            level_min = 1
            level_max = 'NULL'
            
        # rewards (JSONB)
        # Search for '{"experience": ...}'::jsonb
        rewards_match = re.search(r"('\{.*\}')::jsonb", values_str)
        rewards = rewards_match.group(1) if rewards_match else '{}'
        
        # objectives (JSONB)
        # Search for the second JSONB
        all_jsonb = re.findall(r"('\[\{.*\}\]')::jsonb", values_str)
        objectives = all_jsonb[0] if all_jsonb else '[]'
        
        # metadata
        metadata = {
            "id": f"canon-quest-seattle-{quest_id.replace('quest-', '')}",
            "version": "1.0.0",
            "source_file": f"knowledge/canon/lore/timeline-author/quests/america/seattle/2020-2029/{quest_id}.yaml"
        }
        metadata_json = json.dumps(metadata, ensure_ascii=False)
        
        new_stmt = f"""INSERT INTO gameplay.quests (
    title, description, level_min, level_max, status,
    metadata, rewards, objectives
) VALUES (
    '{title}',
    '{description}',
    {level_min},
    {level_max},
    'active',
    '{metadata_json}'::jsonb,
    {rewards}::jsonb,
    {objectives}::jsonb
) ON CONFLICT (metadata->>'id') DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    level_min = EXCLUDED.level_min,
    level_max = EXCLUDED.level_max,
    rewards = EXCLUDED.rewards,
    objectives = EXCLUDED.objectives,
    updated_at = CURRENT_TIMESTAMP;"""
        new_statements.append(new_stmt)

    with open(output_path, 'w', encoding='utf-8') as f:
        f.write("-- Import Seattle quests 017-039 into gameplay.quests table\n")
        f.write("-- Generated from V1_84\n")
        f.write("-- Issue: #2273\n\n")
        f.write("\n\n".join(new_statements))
        f.write("\n")

if __name__ == "__main__":
    convert_v1_84_to_quests()
