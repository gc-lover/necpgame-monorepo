#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Toronto Part 2 Quests
Creates supporting NPC characters and dialogue nodes for Toronto Part 2 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_toronto_part2_npcs():
    """Generate NPC characters for Toronto Part 2 quests"""

    toronto_part2_npcs = [
        {
            "id": "toronto-sorry-expert-2020",
            "name": "Dr. Margaret Thompson",
            "role": "Cultural Anthropologist",
            "faction": "Canadian Heritage Foundation",
            "location": "Toronto, University of Toronto",
            "appearance": {
                "age": 47,
                "gender": "female",
                "ethnicity": "canadian",
                "height": "5'5\"",
                "build": "scholarly",
                "hair": "brown, glasses"
            },
            "personality": {
                "traits": ["analytical", "diplomatic", "patient"],
                "background": "PhD in cultural studies, specializes in Canadian social norms",
                "motivations": ["preserve cultural identity", "educate about politeness", "promote understanding"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 80,
                "empathy": 90,
                "morality": 85
            }
        },
        {
            "id": "toronto-tim-hortons-manager-2020",
            "name": "Steve MacDonald",
            "role": "Tim Hortons Store Manager",
            "faction": "Tim Hortons Corporation",
            "location": "Toronto, Multiple Locations",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "canadian",
                "height": "5'10\"",
                "build": "solid",
                "hair": "red beard"
            },
            "personality": {
                "traits": ["reliable", "friendly", "traditional"],
                "background": "Third-generation Canadian, started as barista, rose to management",
                "motivations": ["provide quality service", "maintain traditions", "support community"]
            },
            "stats": {
                "intelligence": 75,
                "charisma": 85,
                "empathy": 80,
                "morality": 90
            }
        },
        {
            "id": "toronto-raptors-owner-2020",
            "name": "Lawrence Tan",
            "role": "Raptors Team Executive",
            "faction": "Toronto Raptors",
            "location": "Toronto, Scotiabank Arena",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "chinese-canadian",
                "height": "6'1\"",
                "build": "athletic",
                "hair": "black, silver temples"
            },
            "personality": {
                "traits": ["ambitious", "strategic", "confident"],
                "background": "Business executive who invested in sports franchises",
                "motivations": ["win championships", "grow basketball globally", "inspire youth"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 95,
                "empathy": 70,
                "morality": 75
            }
        },
        {
            "id": "toronto-language-teacher-2020",
            "name": "Marie-Claude Dubois",
            "role": "French Language Instructor",
            "faction": "Ontario Ministry of Education",
            "location": "Toronto, French School",
            "appearance": {
                "age": 38,
                "gender": "female",
                "ethnicity": "french-canadian",
                "height": "5'6\"",
                "build": "petite",
                "hair": "dark brown, ponytail"
            },
            "personality": {
                "traits": ["passionate", "encouraging", "cultured"],
                "background": "Immigrant from Quebec, dedicated to language preservation",
                "motivations": ["promote bilingualism", "preserve French culture", "educate youth"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 90,
                "empathy": 95,
                "morality": 80
            }
        },
        {
            "id": "toronto-healthcare-admin-2020",
            "name": "Dr. Raj Patel",
            "role": "Healthcare Administrator",
            "faction": "Ontario Health Ministry",
            "location": "Toronto, Hospital Administration",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "south asian-canadian",
                "height": "5'9\"",
                "build": "professional",
                "hair": "black, neatly combed"
            },
            "personality": {
                "traits": ["compassionate", "organized", "dedicated"],
                "background": "Medical doctor turned administrator, strong advocate for universal healthcare",
                "motivations": ["improve healthcare access", "reduce inequalities", "serve community"]
            },
            "stats": {
                "intelligence": 90,
                "charisma": 75,
                "empathy": 95,
                "morality": 95
            }
        }
    ]

    return toronto_part2_npcs

def generate_toronto_part2_dialogues(npcs):
    """Generate dialogue nodes for Toronto Part 2 NPCs"""

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
                "quest_context": f"toronto_part2_quest_{(i % 5) + 6:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"toronto_part2_quest_{(i % 5) + 6:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*greets you with typical Canadian politeness* Hello there! I'm {npc_name}. Sorry to bother you, but how can I help?",
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
                        "text": f"Sorry, I don't mean to impose, but Toronto is always looking for good people to help out. As someone working in {npc.get('role', 'the city').lower()}, I might have some opportunities. Would you be interested?",
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
                        "text": f"Toronto is quite something, eh? We're proud of our multicultural mosaic and the way everyone gets along. As someone in {npc.get('role', 'public service').lower()}, I see the best of Canadian values every day.",
                        "responses": [
                            {
                                "text": "What makes Toronto special?",
                                "next_node": f"{dialogue_id}_special",
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

    changeset_id = f"data_npcs_toronto_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

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

    changeset_id = f"data_dialogues_toronto_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

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
    """Generate NPCs and Dialogues for Toronto Part 2"""

    npcs = generate_toronto_part2_npcs()
    dialogues = generate_toronto_part2_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_toronto_part2_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_toronto_part2_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Toronto Part 2")

if __name__ == '__main__':
    main()
