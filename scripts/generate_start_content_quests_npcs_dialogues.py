#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Start Content Quests
Creates supporting NPC characters and dialogue nodes for start content quests (level 1).
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_start_content_npcs():
    """Generate NPC characters for start content quests"""

    start_npcs = [
        {
            "id": "start-quest-mentor-2020",
            "name": "Guide Gideon",
            "role": "City Guide",
            "faction": "Civilians",
            "location": "Starting City",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'11\"",
                "build": "fit",
                "hair": "graying brown",
                "eyes": "wise blue",
                "clothing_style": "practical guide outfit",
                "distinctive_features": "Map collection, helpful smile"
            },
            "personality": "helpful, patient, knowledgeable",
            "background": "A seasoned guide who helps new arrivals understand the city",
            "quest_connections": ["start-quest-orientation", "start-quest-tutorial"],
            "dialogue_topics": ["city-navigation", "local-customs", "getting-started"]
        },
        {
            "id": "start-quest-trader-2020",
            "name": "Market Molly",
            "role": "Local Trader",
            "faction": "Civilians",
            "location": "Market District",
            "appearance": {
                "age": 32,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "average",
                "hair": "long brown",
                "eyes": "friendly brown",
                "clothing_style": "merchant apron",
                "distinctive_features": "Always has samples, welcoming demeanor"
            },
            "personality": "friendly, business-savvy, generous",
            "background": "A local trader who helps newcomers with basic supplies and information",
            "quest_connections": ["start-quest-supplies", "start-quest-equipment"],
            "dialogue_topics": ["trading", "supplies", "market-info"]
        },
        {
            "id": "start-quest-guard-2020",
            "name": "Sergeant Steele",
            "role": "City Guard",
            "faction": "City Authority",
            "location": "City Gates",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'2\"",
                "build": "muscular",
                "hair": "short military cut",
                "eyes": "alert blue",
                "clothing_style": "guard uniform",
                "distinctive_features": "Badge of authority, stern but fair expression"
            },
            "personality": "disciplined, protective, firm",
            "background": "A city guard who maintains order and helps with basic city defense",
            "quest_connections": ["start-quest-defense", "start-quest-patrol"],
            "dialogue_topics": ["city-safety", "local-laws", "protection"]
        },
        {
            "id": "start-quest-innkeeper-2020",
            "name": "Innkeeper Iris",
            "role": "Inn Manager",
            "faction": "Civilians",
            "location": "Local Inn",
            "appearance": {
                "age": 40,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'7\"",
                "build": "comfortable",
                "hair": "tied back",
                "eyes": "warm brown",
                "clothing_style": "innkeeper apron",
                "distinctive_features": "Always has a fresh towel, motherly presence"
            },
            "personality": "nurturing, reliable, good listener",
            "background": "An innkeeper who provides rest and information to newcomers",
            "quest_connections": ["start-quest-rest", "start-quest-information"],
            "dialogue_topics": ["rest", "local-gossip", "comfort"]
        },
        {
            "id": "start-quest-blacksmith-2020",
            "name": "Forge Master Finn",
            "role": "Blacksmith",
            "faction": "Craftsmen",
            "location": "Forge District",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'0\"",
                "build": "strong",
                "hair": "singed gray",
                "eyes": "intense brown",
                "clothing_style": "leather apron",
                "distinctive_features": "Burn scars on arms, soot on face"
            },
            "personality": "skilled, direct, proud of craft",
            "background": "A master blacksmith who teaches basic crafting and repairs",
            "quest_connections": ["start-quest-crafting", "start-quest-repair"],
            "dialogue_topics": ["smithing", "crafting", "equipment"]
        },
        {
            "id": "start-quest-healer-2020",
            "name": "Healer Hannah",
            "role": "Medical Assistant",
            "faction": "Civilians",
            "location": "Medical District",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'5\"",
                "build": "petite",
                "hair": "neat bun",
                "eyes": "compassionate green",
                "clothing_style": "healer robes",
                "distinctive_features": "Medic kit, healing herbs"
            },
            "personality": "compassionate, knowledgeable, gentle",
            "background": "A healer who teaches basic first aid and provides medical help",
            "quest_connections": ["start-quest-healing", "start-quest-first-aid"],
            "dialogue_topics": ["healing", "medicine", "health"]
        },
        {
            "id": "start-quest-farmer-2020",
            "name": "Farmer Frank",
            "role": "Local Farmer",
            "faction": "Civilians",
            "location": "Outskirts",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'9\"",
                "build": "sturdy",
                "hair": "sun-bleached",
                "eyes": "weather-beaten blue",
                "clothing_style": "work clothes",
                "distinctive_features": "Calloused hands, farming tools"
            },
            "personality": "hard-working, honest, down-to-earth",
            "background": "A farmer who teaches about local resources and survival",
            "quest_connections": ["start-quest-farming", "start-quest-gathering"],
            "dialogue_topics": ["farming", "resources", "survival"]
        },
        {
            "id": "start-quest-teacher-2020",
            "name": "Teacher Tessa",
            "role": "Community Educator",
            "faction": "Civilians",
            "location": "Community Center",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "fit",
                "hair": "neat brown",
                "eyes": "intelligent hazel",
                "clothing_style": "teacher attire",
                "distinctive_features": "Stack of books, encouraging smile"
            },
            "personality": "encouraging, knowledgeable, patient",
            "background": "A teacher who helps newcomers learn basic skills and knowledge",
            "quest_connections": ["start-quest-learning", "start-quest-skills"],
            "dialogue_topics": ["education", "skills", "knowledge"]
        }
    ]

    return start_npcs

def generate_start_content_dialogues(npcs):
    """Generate dialogue nodes for start content NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hello there! I'm {npc['name']}. Welcome to the city! How can I help you get started?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "I'm new here. What should I know?",
                    "next_dialogue_id": f"{npc['id']}-welcome-info"
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
            "text": f"I'm {npc['role']} here in the city. {npc['background']}",
            "responses": [
                {
                    "text": "What can you teach me?",
                    "next_dialogue_id": f"{npc['id']}-teaching-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(about_me_dialogue)

        # Generate welcome-info dialogue
        welcome_topics = npc["dialogue_topics"]
        welcome_text = f"As a newcomer, you should know about {', '.join(welcome_topics[:2])}. It's essential for getting around here."

        welcome_dialogue = {
            "id": f"{npc['id']}-welcome-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": welcome_text,
            "responses": [
                {
                    "text": "Can you help me with that?",
                    "next_dialogue_id": f"{npc['id']}-help-offer"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(welcome_dialogue)

        # Generate teaching-info dialogue
        teaching_dialogue = {
            "id": f"{npc['id']}-teaching-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": f"I can teach you the basics of {', '.join(welcome_topics[:2])}. It will help you survive and thrive here.",
            "responses": [
                {
                    "text": "I'd like to learn",
                    "next_dialogue_id": f"{npc['id']}-help-offer"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(teaching_dialogue)

        # Generate help-offer dialogue
        help_dialogue = {
            "id": f"{npc['id']}-help-offer",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "quest",
            "text": "Great! I have some tasks that will teach you exactly what you need to know. Ready to get started?",
            "responses": [
                {
                    "text": "Yes, I'm ready!",
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

    changeset_id = f"npcs-start-content-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'start-content-npcs-import',
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
    print(f"Generated {len(npcs)} start content NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-start-content-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'start-content-dialogues-import',
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
    print(f"Generated {len(dialogues)} start content dialogue nodes")

def main():
    """Main function to generate start content NPCs and dialogues"""

    print("Generating start content NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_start_content_npcs()
    print(f"Generated {len(npcs)} start content NPCs")

    # Generate dialogues
    dialogues = generate_start_content_dialogues(npcs)
    print(f"Generated {len(dialogues)} start content dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_start_content_quests_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_start_content_quests_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("Start content NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
