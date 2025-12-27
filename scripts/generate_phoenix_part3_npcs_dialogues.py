#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Phoenix Part 3 Quests
Creates supporting NPC characters and dialogue nodes for Phoenix Part 3 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_phoenix_part3_npcs():
    """Generate NPC characters for Phoenix Part 3 quests"""

    phoenix_part3_npcs = [
        {
            "id": "phoenix-desert-survival-expert-2020",
            "name": "Sage Coyote",
            "role": "Desert Survival Guide",
            "faction": "Desert Nomads",
            "location": "Phoenix, Desert Outskirts",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "native american",
                "height": "5'11\"",
                "build": "lean and weathered",
                "hair": "long black with gray",
                "eyes": "dark brown",
                "clothing_style": "traditional desert robes",
                "distinctive_features": "Tribal tattoos, weathered skin"
            },
            "personality": "wise, resourceful, connected to the land",
            "background": "Expert desert survivalist who guides people through the harsh Arizona desert",
            "quest_connections": ["quest-011-phoenix-desert-survival-expert"],
            "dialogue_topics": ["desert-survival", "native-heritage", "phoenix-heat"]
        },
        {
            "id": "phoenix-solar-empire-leader-2020",
            "name": "Dr. Solaria Kane",
            "role": "Solar Energy Baroness",
            "faction": "Solar Empire Corporation",
            "location": "Phoenix, Solar Tower",
            "appearance": {
                "age": 52,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'8\"",
                "build": "fit",
                "hair": "silver bob",
                "eyes": "piercing blue",
                "clothing_style": "corporate power suit",
                "distinctive_features": "Solar-powered jewelry, confident aura"
            },
            "personality": "visionary, ambitious, technologically advanced",
            "background": "CEO of the largest solar energy corporation in the Southwest",
            "quest_connections": ["quest-011-solar-empire"],
            "dialogue_topics": ["solar-energy", "corporate-power", "phoenix-renewal"]
        },
        {
            "id": "phoenix-solar-energy-mogul-2020",
            "name": "Magnus Ray",
            "role": "Solar Panel Magnate",
            "faction": "Renewable Energy Alliance",
            "location": "Phoenix, Industrial District",
            "appearance": {
                "age": 48,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'2\"",
                "build": "impressive",
                "hair": "short black",
                "eyes": "intense brown",
                "clothing_style": "engineer executive",
                "distinctive_features": "Solar tattoos on arms, confident posture"
            },
            "personality": "innovative, driven, environmentally conscious",
            "background": "Inventor and manufacturer of revolutionary solar panel technology",
            "quest_connections": ["quest-012-phoenix-solar-energy-mogul"],
            "dialogue_topics": ["solar-technology", "renewable-energy", "phoenix-innovation"]
        },
        {
            "id": "phoenix-water-reclamation-engineer-2020",
            "name": "Dr. Aqua Voss",
            "role": "Water Reclamation Engineer",
            "faction": "Water Works Corporation",
            "location": "Phoenix, Water Treatment Plant",
            "appearance": {
                "age": 41,
                "gender": "female",
                "ethnicity": "asian american",
                "height": "5'6\"",
                "build": "petite",
                "hair": "long black ponytail",
                "eyes": "sharp brown",
                "clothing_style": "lab coat and boots",
                "distinctive_features": "Safety goggles, tool belt"
            },
            "personality": "brilliant, detail-oriented, humanitarian",
            "background": "Leading engineer in water reclamation technology for desert cities",
            "quest_connections": ["quest-012-water-reclamation-megacity"],
            "dialogue_topics": ["water-reclamation", "desert-living", "phoenix-water-crisis"]
        },
        {
            "id": "phoenix-border-wall-commander-2020",
            "name": "Colonel Steele",
            "role": "Border Defense Commander",
            "faction": "Border Security Force",
            "location": "Phoenix, Border Wall",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "military fit",
                "hair": "crew cut gray",
                "eyes": "steely blue",
                "clothing_style": "military uniform",
                "distinctive_features": "Medals and badges, stern expression"
            },
            "personality": "disciplined, protective, no-nonsense",
            "background": "Commander overseeing AI-enhanced border wall defenses",
            "quest_connections": ["quest-013-border-wall-ai-defense"],
            "dialogue_topics": ["border-security", "ai-defense", "phoenix-protection"]
        }
    ]

    return phoenix_part3_npcs

def generate_phoenix_part3_dialogues(npcs):
    """Generate dialogue nodes for Phoenix Part 3 NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Howdy! I'm {npc['name']}, {npc['role']} here in the Valley of the Sun. Hot enough for ya?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What's Phoenix like these days?",
                    "next_dialogue_id": f"{npc['id']}-phoenix-info"
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
            "text": f"I'm {npc['role']} working in Phoenix. {npc['background']}",
            "responses": [
                {
                    "text": "What's your role in the city?",
                    "next_dialogue_id": f"{npc['id']}-role-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(about_me_dialogue)

        # Generate phoenix-info dialogue
        phoenix_topics = npc["dialogue_topics"]
        phoenix_text = f"Phoenix is a city of extremes - scorching heat, endless desert, and relentless innovation. We're known for our {', '.join(phoenix_topics[:2])} culture."

        phoenix_dialogue = {
            "id": f"{npc['id']}-phoenix-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": phoenix_text,
            "responses": [
                {
                    "text": "Tell me more about the Valley of the Sun",
                    "next_dialogue_id": f"{npc['id']}-valley-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(phoenix_dialogue)

        # Generate role-info dialogue
        role_dialogue = {
            "id": f"{npc['id']}-role-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": "My work here keeps this desert city functioning. Every day brings new challenges and opportunities.",
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

        # Generate valley-info dialogue
        valley_text = "The Valley of the Sun has transformed from a sprawling desert suburb into a high-tech desert metropolis. Solar power, water reclamation, and AI defense keep us thriving."

        valley_dialogue = {
            "id": f"{npc['id']}-valley-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": valley_text,
            "responses": [
                {
                    "text": "That's impressive",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(valley_dialogue)

        # Generate help-offer dialogue
        help_dialogue = {
            "id": f"{npc['id']}-help-offer",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "quest",
            "text": "Actually, there are some pressing matters that could use your assistance. Phoenix always needs capable people.",
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

    changeset_id = f"npcs-phoenix-part3-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'phoenix-part3-npcs-import',
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
    print(f"Generated {len(npcs)} Phoenix Part 3 NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-phoenix-part3-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'phoenix-part3-dialogues-import',
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
    print(f"Generated {len(dialogues)} Phoenix Part 3 dialogue nodes")

def main():
    """Main function to generate Phoenix Part 3 NPCs and dialogues"""

    print("Generating Phoenix Part 3 NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_phoenix_part3_npcs()
    print(f"Generated {len(npcs)} Phoenix Part 3 NPCs")

    # Generate dialogues
    dialogues = generate_phoenix_part3_dialogues(npcs)
    print(f"Generated {len(dialogues)} Phoenix Part 3 dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_phoenix_part3_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_phoenix_part3_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("Phoenix Part 3 NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
