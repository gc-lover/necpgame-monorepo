#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for New York Part 1 Quests
Creates supporting NPC characters and dialogue nodes for New York Part 1 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_newyork_part1_npcs():
    """Generate NPC characters for New York Part 1 quests"""

    newyork_part1_npcs = [
        {
            "id": "newyork-lady-liberty-guardian-2078",
            "name": "Colonel Liberty",
            "role": "Liberty Island Security Chief",
            "faction": "Statue of Liberty Preservation Society",
            "location": "New York, Liberty Island",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'1\"",
                "build": "athletic",
                "hair": "gray crew cut",
                "eyes": "steel blue",
                "clothing_style": "military security uniform",
                "distinctive_features": "Medal of Honor ribbon, stern expression"
            },
            "personality": "patriotic, disciplined, protective of American heritage",
            "background": "Former military officer now protecting the Statue of Liberty from corporate exploitation",
            "quest_connections": ["quest-001-lady-liberty"],
            "dialogue_topics": ["statue-of-liberty", "american-history", "corporate-threats"]
        },
        {
            "id": "newyork-wall-street-broker-2078",
            "name": "Victoria Voss",
            "role": "Wall Street Trader",
            "faction": "Financial District Elite",
            "location": "New York, Wall Street",
            "appearance": {
                "age": 38,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'9\"",
                "build": "slim",
                "hair": "long black with red highlights",
                "eyes": "sharp green",
                "clothing_style": "business power suit",
                "distinctive_features": "Neural implant ports visible, expensive jewelry"
            },
            "personality": "ambitious, calculating, opportunistic",
            "background": "High-frequency trader who survived multiple market crashes and cyber attacks",
            "quest_connections": ["quest-002-wall-street-crash"],
            "dialogue_topics": ["stock-market", "cyber-attacks", "financial-power"]
        },
        {
            "id": "newyork-times-square-performer-2078",
            "name": "Jax Sterling",
            "role": "Street Performer",
            "faction": "Times Square Artists Collective",
            "location": "New York, Times Square",
            "appearance": {
                "age": 28,
                "gender": "male",
                "ethnicity": "latino",
                "height": "5'11\"",
                "build": "fit",
                "hair": "styled mohawk",
                "eyes": "dark brown",
                "clothing_style": "cyberpunk street wear",
                "distinctive_features": "LED tattoos that glow, holographic performer gear"
            },
            "personality": "charismatic, rebellious, street-smart",
            "background": "Rising star in Times Square's underground performance scene",
            "quest_connections": ["quest-003-times-square-chase"],
            "dialogue_topics": ["street-performance", "times-square-culture", "underground-art"]
        },
        {
            "id": "newyork-pizza-rat-delivery-2078",
            "name": "Mama Maria",
            "role": "Pizza Parlor Owner",
            "faction": "Little Italy Merchants",
            "location": "New York, Little Italy",
            "appearance": {
                "age": 65,
                "gender": "female",
                "ethnicity": "italian american",
                "height": "5'3\"",
                "build": "plump",
                "hair": "gray bun",
                "eyes": "warm brown",
                "clothing_style": "traditional apron",
                "distinctive_features": "Flour-dusted apron, gold cross necklace"
            },
            "personality": "warm, no-nonsense, family-oriented",
            "background": "Third-generation pizza maker whose famous rat delivery service became legendary",
            "quest_connections": ["quest-004-pizza-rat"],
            "dialogue_topics": ["italian-heritage", "pizza-tradition", "rat-delivery-legend"]
        },
        {
            "id": "newyork-subway-engineer-2078",
            "name": "Chief Engineer Rodriguez",
            "role": "Subway System Chief Engineer",
            "faction": "NYC Transit Authority",
            "location": "New York, Subway Control Center",
            "appearance": {
                "age": 58,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "5'10\"",
                "build": "stocky",
                "hair": "balding",
                "eyes": "tired but sharp",
                "clothing_style": "engineer coveralls",
                "distinctive_features": "Tool belt, grease-stained hands"
            },
            "personality": "experienced, pragmatic, dedicated",
            "background": "Veteran engineer maintaining New York's crumbling subway system",
            "quest_connections": ["quest-005-subway-survival"],
            "dialogue_topics": ["subway-system", "infrastructure-challenges", "urban-survival"]
        }
    ]

    return newyork_part1_npcs

def generate_newyork_part1_dialogues(npcs):
    """Generate dialogue nodes for New York Part 1 NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hey there! I'm {npc['name']}, {npc['role']} here in the Big Apple. What brings you to New York?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What's the story with this city?",
                    "next_dialogue_id": f"{npc['id']}-newyork-story"
                },
                {
                    "text": "Goodbye",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                }
            ]
        }
        dialogues.append(greeting_dialogue)

        # Generate about-me dialogue
        about_me_dialogue = {
            "id": f"{npc['id']}-about-me",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": f"I'm {npc['role']} in New York. {npc['background']}",
            "responses": [
                {
                    "text": "What's your role here?",
                    "next_dialogue_id": f"{npc['id']}-role-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(about_me_dialogue)

        # Generate newyork-story dialogue
        newyork_topics = npc["dialogue_topics"]
        newyork_text = f"New York has always been the city that never sleeps, but these days... it's more like the city that never stops fighting to survive. We're known for our {', '.join(newyork_topics[:2])} culture."

        newyork_dialogue = {
            "id": f"{npc['id']}-newyork-story",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": newyork_text,
            "responses": [
                {
                    "text": "Tell me more about the Big Apple",
                    "next_dialogue_id": f"{npc['id']}-big-apple-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(newyork_dialogue)

        # Generate role-info dialogue
        role_dialogue = {
            "id": f"{npc['id']}-role-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": "My role here is to keep this city running, one day at a time. There's always something that needs fixing or protecting.",
            "responses": [
                {
                    "text": "How can I help?",
                    "next_dialogue_id": f"{npc['id']}-help-offer"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(role_dialogue)

        # Generate big-apple-info dialogue
        big_apple_text = "The Big Apple has seen better days, but she's still got that spark. From Lady Liberty to the subway rats, this city's full of stories and surprises."

        big_apple_dialogue = {
            "id": f"{npc['id']}-big-apple-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": big_apple_text,
            "responses": [
                {
                    "text": "That's inspiring",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(big_apple_dialogue)

        # Generate help-offer dialogue
        help_dialogue = {
            "id": f"{npc['id']}-help-offer",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "quest",
            "text": "Actually, there are some things that need doing around here. I could use someone with your... talents.",
            "responses": [
                {
                    "text": "I'm interested",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                },
                {
                    "text": "Maybe later",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(help_dialogue)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPCs"""

    changeset_id = f"npcs-newyork-part1-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'newyork-part1-npcs-import',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'npcs',
                                'columns': [
                                    {
                                        'column': {
                                            'name': 'id',
                                            'value': npc['id']
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'data',
                                            'value': json.dumps(npc, ensure_ascii=False)
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'created_at',
                                            'value': now
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'updated_at',
                                            'value': now
                                        }
                                    }
                                ]
                            }
                        } for npc in npcs
                    ]
                }
            }
        ]
    }

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, indent=2)

    print(f"Created Liquibase NPCs file: {output_file}")
    print(f"Generated {len(npcs)} New York Part 1 NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-newyork-part1-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'newyork-part1-dialogues-import',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'dialogues',
                                'columns': [
                                    {
                                        'column': {
                                            'name': 'id',
                                            'value': dialogue['id']
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'data',
                                            'value': json.dumps(dialogue, ensure_ascii=False)
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'created_at',
                                            'value': now
                                        }
                                    },
                                    {
                                        'column': {
                                            'name': 'updated_at',
                                            'value': now
                                        }
                                    }
                                ]
                            }
                        } for dialogue in dialogues
                    ]
                }
            }
        ]
    }

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, indent=2)

    print(f"Created Liquibase dialogues file: {output_file}")
    print(f"Generated {len(dialogues)} New York Part 1 dialogue nodes")

def main():
    """Main function to generate New York Part 1 NPCs and dialogues"""

    print("Generating New York Part 1 NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_newyork_part1_npcs()
    print(f"Generated {len(npcs)} New York Part 1 NPCs")

    # Generate dialogues
    dialogues = generate_newyork_part1_dialogues(npcs)
    print(f"Generated {len(dialogues)} New York Part 1 dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_newyork_part1_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_newyork_part1_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("New York Part 1 NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
