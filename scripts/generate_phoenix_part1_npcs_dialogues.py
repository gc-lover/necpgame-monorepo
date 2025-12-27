#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Phoenix Part 1 Quests
Creates supporting NPC characters and dialogue nodes for Phoenix city quests Part 1.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_phoenix_part1_npcs():
    """Generate NPC characters for Phoenix Part 1 quests"""

    phoenix_npcs = [
        {
            "id": "phoenix-tour-guide-2020",
            "name": "Desert Rose",
            "role": "City Tour Guide",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "long dark",
                "eyes": "brown",
                "clothing_style": "desert casual",
                "distinctive_features": "Sunglasses, knows all desert spots"
            },
            "personality": "enthusiastic, heat-adapted",
            "background": "Local tour guide who knows Phoenix's desert landscapes and hidden gems",
            "quest_connections": ["quest-001-desert-heat", "quest-008-sedona-red-rocks"],
            "dialogue_topics": ["desert-life", "phoenix-landmarks", "arizona-culture"]
        },
        {
            "id": "phoenix-canyon-guide-2020",
            "name": "Canyon Carl",
            "role": "Park Ranger",
            "faction": "Government",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "fit",
                "hair": "short gray",
                "eyes": "blue",
                "clothing_style": "park ranger uniform",
                "distinctive_features": "Wide-brimmed hat, hiking boots"
            },
            "personality": "experienced, safety-conscious",
            "background": "Grand Canyon guide who knows every trail and viewpoint",
            "quest_connections": ["quest-002-grand-canyon"],
            "dialogue_topics": ["grand-canyon", "hiking-safety", "arizona-wildlife"]
        },
        {
            "id": "phoenix-cactus-expert-2020",
            "name": "Saguaro Sam",
            "role": "Botanist",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "5'8\"",
                "build": "average",
                "hair": "black graying",
                "eyes": "brown",
                "clothing_style": "field researcher",
                "distinctive_features": "Field notebook, cactus samples"
            },
            "personality": "knowledgeable, patient",
            "background": "Desert botanist specializing in saguaro cacti and desert flora",
            "quest_connections": ["quest-003-cactus-saguaro"],
            "dialogue_topics": ["desert-botany", "saguaro-cacti", "arizona-ecology"]
        },
        {
            "id": "phoenix-native-elder-2020",
            "name": "Spirit Walker",
            "role": "Cultural Elder",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 68,
                "gender": "female",
                "ethnicity": "native american",
                "height": "5'4\"",
                "build": "wise",
                "hair": "long silver",
                "eyes": "dark brown",
                "clothing_style": "traditional native",
                "distinctive_features": "Tribal jewelry, speaks softly"
            },
            "personality": "wise, spiritually connected",
            "background": "Native American elder sharing stories of ancestral lands and heritage",
            "quest_connections": ["quest-004-native-american-heritage"],
            "dialogue_topics": ["native-culture", "ancestral-lands", "arizona-history"]
        },
        {
            "id": "phoenix-retiree-2020",
            "name": "Golf Buddy Bob",
            "role": "Retired Golfer",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 70,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "fit",
                "hair": "white",
                "eyes": "blue",
                "clothing_style": "golf attire",
                "distinctive_features": "Golf clubs, friendly smile"
            },
            "personality": "sociable, relaxed",
            "background": "Retired northerner who moved to Phoenix for the weather and golf",
            "quest_connections": ["quest-005-retirement-city"],
            "dialogue_topics": ["retirement-life", "golf-courses", "phoenix-weather"]
        },
        {
            "id": "phoenix-water-engineer-2020",
            "name": "Aquifer Alice",
            "role": "Hydrologist",
            "faction": "Government",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 40,
                "gender": "female",
                "ethnicity": "asian",
                "height": "5'6\"",
                "build": "professional",
                "hair": "black",
                "eyes": "brown",
                "clothing_style": "lab coat",
                "distinctive_features": "Water testing equipment"
            },
            "personality": "concerned, analytical",
            "background": "Water resource engineer working on Phoenix's water crisis solutions",
            "quest_connections": ["quest-006-water-crisis"],
            "dialogue_topics": ["water-conservation", "arizona-water", "sustainability"]
        },
        {
            "id": "phoenix-food-vendor-2020",
            "name": "Taco Teresa",
            "role": "Food Truck Owner",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 38,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'5\"",
                "build": "curvy",
                "hair": "dark curly",
                "eyes": "brown",
                "clothing_style": "chef apron",
                "distinctive_features": "Always has fresh tortillas"
            },
            "personality": "passionate, generous",
            "background": "Mexican food expert who knows authentic Phoenix Mexican cuisine",
            "quest_connections": ["quest-007-mexican-food"],
            "dialogue_topics": ["mexican-cuisine", "phoenix-food", "cultural-fusion"]
        },
        {
            "id": "phoenix-baseball-coach-2020",
            "name": "Diamond Dan",
            "role": "Baseball Coach",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 48,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'1\"",
                "build": "athletic",
                "hair": "short brown",
                "eyes": "green",
                "clothing_style": "baseball uniform",
                "distinctive_features": "Baseball cap, whistle"
            },
            "personality": "motivational, team-oriented",
            "background": "Spring training coach who knows Phoenix's baseball culture",
            "quest_connections": ["quest-009-spring-training"],
            "dialogue_topics": ["baseball", "spring-training", "phoenix-sports"]
        },
        {
            "id": "phoenix-urban-planner-2020",
            "name": "Metro Mike",
            "role": "City Planner",
            "faction": "Government",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'11\"",
                "build": "professional",
                "hair": "black",
                "eyes": "brown",
                "clothing_style": "business casual",
                "distinctive_features": "City planning maps"
            },
            "personality": "visionary, concerned",
            "background": "Urban planner working on Phoenix's growth and sprawl challenges",
            "quest_connections": ["quest-010-urban-sprawl"],
            "dialogue_topics": ["urban-planning", "city-growth", "phoenix-development"]
        },
        {
            "id": "phoenix-survival-expert-2020",
            "name": "Desert Hawk",
            "role": "Survival Instructor",
            "faction": "Civilians",
            "location": "Phoenix, Arizona",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "native american",
                "height": "6'2\"",
                "build": "lean muscular",
                "hair": "long black",
                "eyes": "sharp brown",
                "clothing_style": "desert survival gear",
                "distinctive_features": "Survival knife, weathered face"
            },
            "personality": "stoic, skilled",
            "background": "Desert survival expert teaching urban dwellers desert skills",
            "quest_connections": ["quest-011-phoenix-desert-survival-expert"],
            "dialogue_topics": ["desert-survival", "arizona-wilderness", "outdoor-skills"]
        }
    ]

    return phoenix_npcs

def generate_phoenix_part1_dialogues(npcs):
    """Generate dialogue nodes for Phoenix Part 1 NPCs"""

    dialogues = []

    for npc in npcs:
        # Base dialogue node
        base_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc['id'],
            "type": "greeting",
            "text": f"Hello! I'm {npc['name']}, {npc['role'].lower()} here in Phoenix.",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about",
                    "conditions": []
                },
                {
                    "text": "What do you know about Phoenix?",
                    "next_dialogue_id": f"{npc['id']}-phoenix",
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        # About dialogue
        about_dialogue = {
            "id": f"{npc['id']}-about",
            "npc_id": npc['id'],
            "type": "information",
            "text": npc['background'],
            "responses": [
                {
                    "text": "Interesting! Tell me more about Phoenix",
                    "next_dialogue_id": f"{npc['id']}-phoenix",
                    "conditions": []
                },
                {
                    "text": "Goodbye",
                    "next_dialogue_id": None,
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        # Phoenix-specific dialogue
        phoenix_dialogue = {
            "id": f"{npc['id']}-phoenix",
            "npc_id": npc['id'],
            "type": "information",
            "text": f"Phoenix is a unique desert city! {npc['personality']}",
            "responses": [
                {
                    "text": "Thanks for the information",
                    "next_dialogue_id": None,
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        dialogues.extend([base_dialogue, about_dialogue, phoenix_dialogue])

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPC import"""

    changesets = []

    for npc in npcs:
        changeset_id = f"npcs-phoenix-part1-{npc['id']}-{hashlib.md5(str(npc).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'phoenix-part1-npcs-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.npc_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'npc_id', 'value': npc['id']}},
                            {'column': {'name': 'name', 'value': npc['name']}},
                            {'column': {'name': 'role', 'value': npc['role']}},
                            {'column': {'name': 'faction', 'value': npc['faction']}},
                            {'column': {'name': 'location', 'value': npc['location']}},
                            {'column': {'name': 'appearance', 'value': json.dumps(npc['appearance'], ensure_ascii=False)}},
                            {'column': {'name': 'personality', 'value': npc['personality']}},
                            {'column': {'name': 'background', 'value': npc['background']}},
                            {'column': {'name': 'quest_connections', 'value': json.dumps(npc['quest_connections'], ensure_ascii=False)}},
                            {'column': {'name': 'dialogue_topics', 'value': json.dumps(npc['dialogue_topics'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'city': 'Phoenix, Arizona, USA',
                                'period': '2020-2029',
                                'source': 'generated',
                                'created_at': datetime.now().isoformat()
                            }, ensure_ascii=False)}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase NPCs file: {output_file}")
    print(f"Generated {len(changesets)} Phoenix Part 1 NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogue import"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-phoenix-part1-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'phoenix-part1-dialogues-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.dialogue_nodes',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'dialogue_id', 'value': dialogue['id']}},
                            {'column': {'name': 'npc_id', 'value': dialogue['npc_id']}},
                            {'column': {'name': 'type', 'value': dialogue['type']}},
                            {'column': {'name': 'text', 'value': dialogue['text']}},
                            {'column': {'name': 'responses', 'value': json.dumps(dialogue['responses'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps(dialogue['metadata'], ensure_ascii=False)}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase dialogues file: {output_file}")
    print(f"Generated {len(changesets)} Phoenix Part 1 dialogue nodes")

def main():
    """Main function"""
    print("Generating Phoenix Part 1 NPCs and Dialogues...")

    # Generate NPCs
    phoenix_npcs = generate_phoenix_part1_npcs()
    print(f"Generated {len(phoenix_npcs)} Phoenix Part 1 NPCs")

    # Generate dialogues
    phoenix_dialogues = generate_phoenix_part1_dialogues(phoenix_npcs)
    print(f"Generated {len(phoenix_dialogues)} Phoenix Part 1 dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_phoenix_part1-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_phoenix_part1-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(phoenix_npcs, npcs_output)
    create_liquibase_dialogues(phoenix_dialogues, dialogues_output)

    print("Phoenix Part 1 NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
