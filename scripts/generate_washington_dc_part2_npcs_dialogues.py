#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Washington DC Part 2 Quests
Creates supporting NPC characters and dialogue nodes for Washington DC Part 2 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_washington_dc_part2_npcs():
    """Generate NPC characters for Washington DC Part 2 quests"""

    washington_dc_part2_npcs = [
        {
            "id": "washington-dc-veterans-advocate-2020",
            "name": "Colonel James Harrison",
            "role": "Vietnam Veterans Memorial Foundation Director",
            "faction": "Veterans Affairs",
            "location": "Washington DC, Vietnam Veterans Memorial",
            "appearance": {
                "age": 68,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "military bearing",
                "hair": "silver, crew cut"
            },
            "personality": {
                "traits": ["patriotic", "stoic", "compassionate"],
                "background": "Vietnam veteran who became advocate for veterans' rights",
                "motivations": ["honor fallen soldiers", "support veterans", "preserve history"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 80,
                "empathy": 95,
                "morality": 90
            }
        },
        {
            "id": "washington-dc-pentagon-official-2020",
            "name": "General Sarah Mitchell",
            "role": "Pentagon Public Affairs Officer",
            "faction": "United States Department of Defense",
            "location": "Washington DC, Pentagon",
            "appearance": {
                "age": 52,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'9\"",
                "build": "athletic",
                "hair": "blonde, professional"
            },
            "personality": {
                "traits": ["disciplined", "professional", "strategic"],
                "background": "Career military officer specializing in public communications",
                "motivations": ["maintain national security", "promote military values", "serve country"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 85,
                "empathy": 75,
                "morality": 85
            }
        },
        {
            "id": "washington-dc-supreme-court-clerk-2020",
            "name": "Justice Elena Rodriguez",
            "role": "Supreme Court Clerk",
            "faction": "Judicial Branch",
            "location": "Washington DC, Supreme Court Building",
            "appearance": {
                "age": 45,
                "gender": "female",
                "ethnicity": "latina",
                "height": "5'7\"",
                "build": "professional",
                "hair": "dark brown, sophisticated"
            },
            "personality": {
                "traits": ["intellectual", "principled", "analytical"],
                "background": "Harvard Law graduate, constitutional scholar",
                "motivations": ["uphold constitution", "protect civil rights", "ensure justice"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 80,
                "empathy": 85,
                "morality": 95
            }
        },
        {
            "id": "washington-dc-park-ranger-2020",
            "name": "Ranger Michael Chen",
            "role": "National Park Service Ranger",
            "faction": "National Park Service",
            "location": "Washington DC, Tidal Basin",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "asian american",
                "height": "5'11\"",
                "build": "fit",
                "hair": "black, ranger hat"
            },
            "personality": {
                "traits": ["dedicated", "knowledgeable", "patient"],
                "background": "Environmental science graduate, passionate about nature and history",
                "motivations": ["preserve natural beauty", "educate visitors", "protect environment"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 75,
                "empathy": 90,
                "morality": 85
            }
        },
        {
            "id": "washington-dc-lobbyist-2020",
            "name": "Victoria Sterling",
            "role": "Senior Lobbyist",
            "faction": "K Street Firms",
            "location": "Washington DC, K Street",
            "appearance": {
                "age": 48,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'6\"",
                "build": "elegant",
                "hair": "blonde, power suit"
            },
            "personality": {
                "traits": ["charismatic", "persuasive", "ambitious"],
                "background": "Former congressional aide turned successful lobbyist",
                "motivations": ["influence policy", "represent clients", "navigate power structures"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 95,
                "empathy": 70,
                "morality": 65
            }
        }
    ]

    return washington_dc_part2_npcs

def generate_washington_dc_part2_dialogues(npcs):
    """Generate dialogue nodes for Washington DC Part 2 NPCs"""

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
                "quest_context": f"washington_dc_part2_quest_{(i % 5) + 6:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"washington_dc_part2_quest_{(i % 5) + 6:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*extends hand professionally* Good day. I'm {npc_name} with the {npc.get('faction', 'federal government')}. How may I assist you in serving our great nation?",
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
                        "text": f"The capital always has need of capable individuals who understand the importance of service. As someone in {npc.get('role', 'government service').lower()}, I occasionally require assistance with matters of national significance. Are you prepared to contribute?",
                        "responses": [
                            {
                                "text": "Yes, I'm prepared.",
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
                                "text": "Perhaps later.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_city_info": {
                        "text": f"Washington DC stands as the beating heart of American democracy - a city where history, power, and the future converge. Every monument and institution here tells the story of our nation's enduring spirit and unwavering commitment to freedom.",
                        "responses": [
                            {
                                "text": "What makes DC unique?",
                                "next_node": f"{dialogue_id}_unique",
                                "conditions": {}
                            },
                            {
                                "text": "Tell me about American government.",
                                "next_node": f"{dialogue_id}_government",
                                "conditions": {}
                            },
                            {
                                "text": "I must go.",
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

    changeset_id = f"data_npcs_washington_dc_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

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

    changeset_id = f"data_dialogues_washington_dc_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

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
    """Generate NPCs and Dialogues for Washington DC Part 2"""

    npcs = generate_washington_dc_part2_npcs()
    dialogues = generate_washington_dc_part2_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_washington_dc_part2_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_washington_dc_part2_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Washington DC Part 2")

if __name__ == '__main__':
    main()
