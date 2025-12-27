#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Sao Paulo Part 1 Quests
Creates supporting NPC characters and dialogue nodes for Sao Paulo Part 1 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_sao_paulo_part1_npcs():
    """Generate NPC characters for Sao Paulo Part 1 quests"""

    sao_paulo_part1_npcs = [
        {
            "id": "sao-paulo-mega-city-mayor-2020",
            "name": "Mayor Ricardo Nunes",
            "role": "Mayor of São Paulo",
            "faction": "City Government",
            "location": "São Paulo, City Hall",
            "appearance": {
                "age": 58,
                "gender": "male",
                "ethnicity": "brazilian",
                "height": "5'10\"",
                "build": "athletic",
                "hair": "black with gray temples"
            },
            "personality": {
                "traits": ["ambitious", "charismatic", "pragmatic"],
                "background": "Former business executive turned politician",
                "motivations": ["modernize São Paulo", "combat inequality", "attract investment"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 90,
                "empathy": 70,
                "morality": 65
            }
        },
        {
            "id": "sao-paulo-traffic-engineer-2020",
            "name": "Dr. Maria Silva",
            "role": "Traffic Systems Engineer",
            "faction": "CET - Traffic Department",
            "location": "São Paulo, Traffic Control Center",
            "appearance": {
                "age": 42,
                "gender": "female",
                "ethnicity": "brazilian",
                "height": "5'6\"",
                "build": "petite",
                "hair": "dark brown, tied back"
            },
            "personality": {
                "traits": ["analytical", "frustrated", "dedicated"],
                "background": "MIT-educated engineer specializing in urban mobility",
                "motivations": ["solve traffic crisis", "implement smart city solutions", "improve quality of life"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 60,
                "empathy": 75,
                "morality": 80
            }
        },
        {
            "id": "sao-paulo-favela-leader-2020",
            "name": "Joaquim 'Quim' Santos",
            "role": "Community Leader",
            "faction": "Favela Residents Association",
            "location": "São Paulo, Paraisópolis Favela",
            "appearance": {
                "age": 35,
                "gender": "male",
                "ethnicity": "brazilian",
                "height": "5'11\"",
                "build": "muscular",
                "hair": "dreadlocks"
            },
            "personality": {
                "traits": ["resilient", "community-oriented", "suspicious of outsiders"],
                "background": "Born and raised in favela, became leader through community work",
                "motivations": ["improve favela conditions", "fight inequality", "preserve community culture"]
            },
            "stats": {
                "intelligence": 75,
                "charisma": 85,
                "empathy": 90,
                "morality": 85
            }
        },
        {
            "id": "sao-paulo-chef-traditional-2020",
            "name": "Chef Ana Costa",
            "role": "Traditional Brazilian Chef",
            "faction": "Culinary Heritage Society",
            "location": "São Paulo, Historic Restaurant",
            "appearance": {
                "age": 55,
                "gender": "female",
                "ethnicity": "brazilian",
                "height": "5'4\"",
                "build": "curvy",
                "hair": "black with highlights"
            },
            "personality": {
                "traits": ["passionate", "traditional", "welcoming"],
                "background": "Fourth-generation restaurant owner preserving family recipes",
                "motivations": ["preserve culinary traditions", "educate about Brazilian culture", "support local ingredients"]
            },
            "stats": {
                "intelligence": 70,
                "charisma": 85,
                "empathy": 95,
                "morality": 90
            }
        },
        {
            "id": "sao-paulo-street-artist-2020",
            "name": "Luna Rodriguez",
            "role": "Street Artist",
            "faction": "Urban Art Collective",
            "location": "São Paulo, Street Art District",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "latina",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "colorful streaks, shaved sides"
            },
            "personality": {
                "traits": ["creative", "rebellious", "socially conscious"],
                "background": "Self-taught artist using walls as canvas to express social commentary",
                "motivations": ["express political views", "beautify urban spaces", "inspire community dialogue"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 75,
                "empathy": 85,
                "morality": 70
            }
        }
    ]

    return sao_paulo_part1_npcs

def generate_sao_paulo_part1_dialogues(npcs):
    """Generate dialogue nodes for Sao Paulo Part 1 NPCs"""

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
                "quest_context": f"sao_paulo_part1_quest_{(i % 5) + 1:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"sao_paulo_part1_quest_{(i % 5) + 1:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*greets you professionally* Hello, I'm {npc_name}. How can I help you today?",
                        "responses": [
                            {
                                "text": "I'm looking for work.",
                                "next_node": f"{dialogue_id}_work",
                                "conditions": {"player_level": {"min": 1, "max": 20}}
                            },
                            {
                                "text": "Tell me about São Paulo.",
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
                        "text": f"I might have some tasks that need doing. Are you interested in helping the {npc.get('faction', 'city')}?",
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
                        "text": f"São Paulo is an incredible city - a true melting pot of cultures and innovation. The {npc.get('role', 'residents')} here face many challenges, but the spirit of the people is unmatched.",
                        "responses": [
                            {
                                "text": "What challenges does the city face?",
                                "next_node": f"{dialogue_id}_challenges",
                                "conditions": {}
                            },
                            {
                                "text": "Tell me more about the culture.",
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
                    "language": "pt-BR",
                    "region": "sao_paulo"
                },
                "created_at": datetime.now().isoformat(),
                "updated_at": datetime.now().isoformat()
            }
            dialogues.append(dialogue_node)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create a Liquibase YAML file for NPC data"""

    changeset_id = f"data_npcs_sao_paulo_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'sao_paulo_npcs_generator',
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

    changeset_id = f"data_dialogues_sao_paulo_part1_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'sao_paulo_dialogues_generator',
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
    """Generate NPCs and Dialogues for Sao Paulo Part 1"""

    npcs = generate_sao_paulo_part1_npcs()
    dialogues = generate_sao_paulo_part1_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_sao_paulo_part1_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_sao_paulo_part1_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Sao Paulo Part 1")

if __name__ == '__main__':
    main()
