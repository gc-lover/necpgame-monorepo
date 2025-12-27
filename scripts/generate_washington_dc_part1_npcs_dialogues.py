#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Washington DC Part 1 Quests
Creates supporting NPC characters and dialogue nodes for Washington DC Part 1 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_washington_dc_part1_npcs():
    """Generate NPC characters for Washington DC Part 1 quests"""

    washington_dc_part1_npcs = [
        {
            "id": "washington-dc-tour-guide-2020",
            "name": "Agent Sarah Mitchell",
            "role": "Secret Service Tour Guide",
            "faction": "United States Secret Service",
            "location": "Washington DC, White House",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'8\"",
                "build": "athletic",
                "hair": "blonde, professional bun"
            },
            "personality": {
                "traits": ["professional", "patriotic", "vigilant"],
                "background": "Former military intelligence officer turned Secret Service agent",
                "motivations": ["protect democracy", "educate citizens", "maintain security"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 80,
                "empathy": 70,
                "morality": 95
            }
        },
        {
            "id": "washington-dc-congressman-2020",
            "name": "Congressman Marcus Hale",
            "role": "US Congressman",
            "faction": "Democratic Party",
            "location": "Washington DC, Capitol Building",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "african american",
                "height": "6'1\"",
                "build": "distinguished",
                "hair": "graying temples"
            },
            "personality": {
                "traits": ["ambitious", "eloquent", "principled"],
                "background": "Lawyer and community activist elected to Congress",
                "motivations": ["serve constituents", "pass meaningful legislation", "fight corruption"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 95,
                "empathy": 80,
                "morality": 75
            }
        },
        {
            "id": "washington-dc-historian-2020",
            "name": "Dr. Elizabeth Grant",
            "role": "Civil War Historian",
            "faction": "Smithsonian Institution",
            "location": "Washington DC, Lincoln Memorial",
            "appearance": {
                "age": 48,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'6\"",
                "build": "scholarly",
                "hair": "brown, glasses"
            },
            "personality": {
                "traits": ["scholarly", "passionate", "thoughtful"],
                "background": "PhD in American History, specializes in Civil War and Lincoln",
                "motivations": ["preserve history", "educate public", "promote unity"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 75,
                "empathy": 85,
                "morality": 90
            }
        },
        {
            "id": "washington-dc-museum-curator-2020",
            "name": "Dr. James Wong",
            "role": "Museum Curator",
            "faction": "Smithsonian Institution",
            "location": "Washington DC, National Mall",
            "appearance": {
                "age": 41,
                "gender": "male",
                "ethnicity": "asian american",
                "height": "5'9\"",
                "build": "slender",
                "hair": "black, neatly combed"
            },
            "personality": {
                "traits": ["meticulous", "knowledgeable", "dedicated"],
                "background": "Art historian and curator with expertise in American cultural artifacts",
                "motivations": ["preserve cultural heritage", "make history accessible", "inspire learning"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 70,
                "empathy": 80,
                "morality": 85
            }
        },
        {
            "id": "washington-dc-engineer-2020",
            "name": "Chief Engineer Robert Kline",
            "role": "Monument Maintenance Engineer",
            "faction": "National Park Service",
            "location": "Washington DC, Washington Monument",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "sturdy",
                "hair": "salt and pepper"
            },
            "personality": {
                "traits": ["practical", "reliable", "proud"],
                "background": "Civil engineer with 30 years experience in historic preservation",
                "motivations": ["maintain monuments", "ensure safety", "honor history"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 65,
                "empathy": 75,
                "morality": 90
            }
        }
    ]

    return washington_dc_part1_npcs

def generate_washington_dc_part1_dialogues(npcs):
    """Generate dialogue nodes for Washington DC Part 1 NPCs"""

    dialogues = []

    for npc in npcs:
        npc_id = npc['id']
        npc_name = npc['name']

        # Generate multiple dialogue nodes per NPC
        dialogue_count = 3  # Generate 3 dialogue nodes per NPC

        for i in range(dialogue_count):
            dialogue_id = f"{npc_id}_dialogue_{i+1}"
            dialogue_node = {
                "id": dialogue_id,
                "npc_id": npc_id,
                "quest_context": f"washington_dc_part1_quest_{(i % 5) + 1:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"washington_dc_part1_quest_{(i % 5) + 1:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*salutes professionally* Good day. I'm {npc_name} with the {npc.get('faction', 'federal government')}. How can I assist you today?",
                        "responses": [
                            {
                                "text": "I'm looking for work.",
                                "next_node": f"{dialogue_id}_work",
                                "conditions": {"player_level": {"min": 1, "max": 20}}
                            },
                            {
                                "text": "Tell me about Washington DC.",
                                "next_node": f"{dialogue_id}_city_info",
                                "conditions": {}
                            },
                            {
                                "text": "Goodbye.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_work": {
                        "text": f"The capital is always in need of capable individuals. As someone in {npc.get('role', 'government service').lower()}, I occasionally need assistance with matters of national importance. Are you interested?",
                        "responses": [
                            {
                                "text": "Yes, I'm interested.",
                                "next_node": "quest_offer",
                                "conditions": {},
                                "actions": ["offer_quest"]
                            },
                            {
                                "text": "What kind of work?",
                                "next_node": f"{dialogue_id}_work_details",
                                "conditions": {}
                            },
                            {
                                "text": "Maybe later.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_city_info": {
                        "text": f"Washington DC is the heart of American democracy - a city of monuments, museums, and the machinery of government. Every corner tells a story of our nation's history and ideals.",
                        "responses": [
                            {
                                "text": "What makes DC special?",
                                "next_node": f"{dialogue_id}_special",
                                "conditions": {}
                            },
                            {
                                "text": "Tell me about American history.",
                                "next_node": f"{dialogue_id}_history",
                                "conditions": {}
                            },
                            {
                                "text": "I need to go.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    }
                },
                "localization": {
                    "language": "en-US",
                    "region": "washington_dc"
                },
                "created_at": datetime.now().isoformat(),
                "updated_at": datetime.now().isoformat()
            }
            dialogues.append(dialogue_node)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create a Liquibase YAML file for NPC data"""

    changeset_id = f"data_npcs_washington_dc_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'washington_dc_npcs_generator',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'npcs',
                                'columns': [
                                    {'column': {'name': 'npc_id', 'value': npc['id']}},
                                    {'column': {'name': 'name', 'value': npc['name']}},
                                    {'column': {'name': 'role', 'value': npc['role']}},
                                    {'column': {'name': 'faction', 'value': npc['faction']}},
                                    {'column': {'name': 'location', 'value': npc['location']}},
                                    {'column': {'name': 'appearance', 'value': json.dumps(npc['appearance'], ensure_ascii=False)}},
                                    {'column': {'name': 'personality', 'value': json.dumps(npc['personality'], ensure_ascii=False)}},
                                    {'column': {'name': 'stats', 'value': json.dumps(npc['stats'], ensure_ascii=False)}},
                                    {'column': {'name': 'created_at', 'value': datetime.now().isoformat()}},
                                    {'column': {'name': 'updated_at', 'value': datetime.now().isoformat()}}
                                ]
                            }
                        } for npc in npcs
                    ]
                }
            }
        ]
    }

    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created NPC Liquibase YAML file: {output_file}")

def create_liquibase_dialogues(dialogues, output_file):
    """Create a Liquibase YAML file for dialogue data"""

    changeset_id = f"data_dialogues_washington_dc_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'washington_dc_dialogues_generator',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'dialogues',
                                'columns': [
                                    {'column': {'name': 'dialogue_id', 'value': dialogue['id']}},
                                    {'column': {'name': 'npc_id', 'value': dialogue['npc_id']}},
                                    {'column': {'name': 'quest_context', 'value': dialogue['quest_context']}},
                                    {'column': {'name': 'dialogue_type', 'value': dialogue['dialogue_type']}},
                                    {'column': {'name': 'trigger_conditions', 'value': json.dumps(dialogue['trigger_conditions'], ensure_ascii=False)}},
                                    {'column': {'name': 'dialogue_flow', 'value': json.dumps(dialogue['dialogue_flow'], ensure_ascii=False)}},
                                    {'column': {'name': 'localization', 'value': json.dumps(dialogue['localization'], ensure_ascii=False)}},
                                    {'column': {'name': 'created_at', 'value': dialogue['created_at']}},
                                    {'column': {'name': 'updated_at', 'value': dialogue['updated_at']}}
                                ]
                            }
                        } for dialogue in dialogues
                    ]
                }
            }
        ]
    }

    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Dialogue Liquibase YAML file: {output_file}")

def main():
    """Generate NPCs and Dialogues for Washington DC Part 1"""

    npcs = generate_washington_dc_part1_npcs()
    dialogues = generate_washington_dc_part1_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_washington_dc_part1_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_washington_dc_part1_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Washington DC Part 1")

if __name__ == '__main__':
    main()
