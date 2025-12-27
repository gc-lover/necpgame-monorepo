#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Toronto Part 1 Quests
Creates supporting NPC characters and dialogue nodes for Toronto Part 1 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_toronto_part1_npcs():
    """Generate NPC characters for Toronto Part 1 quests"""

    toronto_part1_npcs = [
        {
            "id": "toronto-cn-tower-operator-2020",
            "name": "Captain Tower",
            "role": "CN Tower Operations Manager",
            "faction": "Tourism Board",
            "location": "Toronto, CN Tower",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "canadian",
                "height": "6'0\"",
                "build": "athletic",
                "hair": "short brown"
            },
            "personality": {
                "traits": ["proud", "knowledgeable", "patriotic"],
                "background": "Former military officer managing Toronto's iconic landmark",
                "motivations": ["promote tourism", "maintain Canadian heritage", "ensure visitor safety"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 85,
                "empathy": 75,
                "morality": 85
            }
        },
        {
            "id": "toronto-hockey-coach-2020",
            "name": "Coach Gordie",
            "role": "Hockey Coach",
            "faction": "Toronto Maple Leafs",
            "location": "Toronto, Air Canada Centre",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "canadian",
                "height": "5'11\"",
                "build": "stocky",
                "hair": "gray crew cut"
            },
            "personality": {
                "traits": ["tough", "disciplined", "passionate"],
                "background": "Former NHL player turned coach, lives and breathes hockey",
                "motivations": ["win Stanley Cup", "develop young talent", "preserve hockey culture"]
            },
            "stats": {
                "intelligence": 75,
                "charisma": 80,
                "empathy": 70,
                "morality": 80
            }
        },
        {
            "id": "toronto-poutine-chef-2020",
            "name": "Chef Pierre",
            "role": "Poutine Specialist",
            "faction": "Culinary Association",
            "location": "Toronto, Kensington Market",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "french-canadian",
                "height": "5'9\"",
                "build": "solid",
                "hair": "black mustache"
            },
            "personality": {
                "traits": ["artistic", "proud", "welcoming"],
                "background": "Immigrant chef perfecting the art of Quebec's national dish",
                "motivations": ["create perfect poutine", "share culture", "support local ingredients"]
            },
            "stats": {
                "intelligence": 70,
                "charisma": 90,
                "empathy": 85,
                "morality": 75
            }
        },
        {
            "id": "toronto-diversity-coordinator-2020",
            "name": "Dr. Aisha Patel",
            "role": "Multiculturalism Coordinator",
            "faction": "City of Toronto",
            "location": "Toronto, City Hall",
            "appearance": {
                "age": 42,
                "gender": "female",
                "ethnicity": "south asian",
                "height": "5'5\"",
                "build": "petite",
                "hair": "long black, professional"
            },
            "personality": {
                "traits": ["diplomatic", "inclusive", "educated"],
                "background": "Sociologist specializing in urban multiculturalism",
                "motivations": ["promote harmony", "fight discrimination", "celebrate diversity"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 85,
                "empathy": 95,
                "morality": 90
            }
        },
        {
            "id": "toronto-niagara-guide-2020",
            "name": "Ranger Mike",
            "role": "Niagara Falls Tour Guide",
            "faction": "Ontario Parks",
            "location": "Toronto, Union Station",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "canadian",
                "height": "5'10\"",
                "build": "fit",
                "hair": "red beard"
            },
            "personality": {
                "traits": ["enthusiastic", "knowledgeable", "outdoorsy"],
                "background": "Environmental scientist turned tour guide",
                "motivations": ["protect natural wonders", "educate visitors", "promote tourism"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 80,
                "empathy": 70,
                "morality": 85
            }
        }
    ]

    return toronto_part1_npcs

def generate_toronto_part1_dialogues(npcs):
    """Generate dialogue nodes for Toronto Part 1 NPCs"""

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
                "quest_context": f"toronto_part1_quest_{(i % 5) + 1:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"toronto_part1_quest_{(i % 5) + 1:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*greets you warmly* Hello there! I'm {npc_name}. Welcome to Toronto - the greatest city in Canada!",
                        "responses": [
                            {
                                "text": "I'm looking for work.",
                                "next_node": f"{dialogue_id}_work",
                                "conditions": {"player_level": {"min": 1, "max": 20}}
                            },
                            {
                                "text": "Tell me about Toronto.",
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
                        "text": f"I hear you're looking for opportunities. Toronto always has work for those willing to help. Interested in something related to {npc.get('role', 'city life').lower()}?",
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
                        "text": f"Toronto is truly special - a mosaic of cultures, innovation, and natural beauty. As someone working in {npc.get('role', 'the city').lower()}, I see the best of what makes this place great every day.",
                        "responses": [
                            {
                                "text": "What makes Toronto unique?",
                                "next_node": f"{dialogue_id}_unique",
                                "conditions": {}
                            },
                            {
                                "text": "Tell me about Canadian culture.",
                                "next_node": f"{dialogue_id}_culture",
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
                    "language": "en-CA",
                    "region": "toronto"
                },
                "created_at": datetime.now().isoformat(),
                "updated_at": datetime.now().isoformat()
            }
            dialogues.append(dialogue_node)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create a Liquibase YAML file for NPC data"""

    changeset_id = f"data_npcs_toronto_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'toronto_npcs_generator',
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

    changeset_id = f"data_dialogues_toronto_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'toronto_dialogues_generator',
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
    """Generate NPCs and Dialogues for Toronto Part 1"""

    npcs = generate_toronto_part1_npcs()
    dialogues = generate_toronto_part1_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_toronto_part1_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_toronto_part1_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Toronto Part 1")

if __name__ == '__main__':
    main()
