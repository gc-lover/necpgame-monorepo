#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Side Quests
Creates supporting NPC characters and dialogue nodes for side quests from 2020-2077 period.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_side_quests_npcs():
    """Generate NPC characters for side quests"""

    side_npcs = [
        {
            "id": "side-quest-merchant-2020",
            "name": "Mysterious Merchant",
            "role": "Underground Trader",
            "faction": "Neutral",
            "location": "Various Cities",
            "appearance": {
                "age": 35,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'10\"",
                "build": "average",
                "hair": "short brown",
                "eyes": "brown",
                "clothing_style": "discreet business",
                "distinctive_features": "Always carrying a briefcase"
            },
            "personality": "mysterious, helpful, opportunistic",
            "background": "A trader who deals in rare items and information across cities",
            "quest_connections": ["side-quest-black-market", "side-quest-rare-item"],
            "dialogue_topics": ["trading", "underground-network", "rare-goods"]
        },
        {
            "id": "side-quest-hacker-2020",
            "name": "Shadow Netrunner",
            "role": "Data Broker",
            "faction": "Underground",
            "location": "Various Cities",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "petite",
                "hair": "neon blue",
                "eyes": "green with cybernetic implants",
                "clothing_style": "cyberpunk streetwear",
                "distinctive_features": "Multiple data ports, holographic tattoos"
            },
            "personality": "cunning, tech-savvy, cautious",
            "background": "A skilled netrunner who helps with data-related side quests",
            "quest_connections": ["side-quest-data-theft", "side-quest-hacking"],
            "dialogue_topics": ["cyber-security", "data-brokering", "netrunning"]
        },
        {
            "id": "side-quest-mechanic-2020",
            "name": "Gearhead Gus",
            "role": "Street Mechanic",
            "faction": "Civilians",
            "location": "Various Cities",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'11\"",
                "build": "muscular",
                "hair": "greasy black",
                "eyes": "brown",
                "clothing_style": "work overalls",
                "distinctive_features": "Oil stains on hands, tool belt"
            },
            "personality": "gruff, skilled, reliable",
            "background": "A mechanic who fixes vehicles and cyberware for side jobs",
            "quest_connections": ["side-quest-vehicle-repair", "side-quest-cyberware"],
            "dialogue_topics": ["mechanics", "vehicles", "repairs"]
        },
        {
            "id": "side-quest-journalist-2020",
            "name": "Investigative Iris",
            "role": "Underground Journalist",
            "faction": "Media",
            "location": "Various Cities",
            "appearance": {
                "age": 31,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "short red",
                "eyes": "sharp blue",
                "clothing_style": "practical urban",
                "distinctive_features": "Always with a recorder, press badge"
            },
            "personality": "curious, determined, ethical",
            "background": "An investigative journalist who uncovers corruption and secrets",
            "quest_connections": ["side-quest-investigation", "side-quest-corruption"],
            "dialogue_topics": ["journalism", "investigation", "corruption"]
        },
        {
            "id": "side-quest-doctor-2020",
            "name": "Dr. Fix-It",
            "role": "Street Doctor",
            "faction": "Civilians",
            "location": "Various Cities",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'0\"",
                "build": "lean",
                "hair": "graying",
                "eyes": "brown behind glasses",
                "clothing_style": "lab coat over street clothes",
                "distinctive_features": "Medical bag, stethoscope"
            },
            "personality": "compassionate, knowledgeable, discreet",
            "background": "A doctor who helps with medical side quests and treatments",
            "quest_connections": ["side-quest-medical", "side-quest-healing"],
            "dialogue_topics": ["medicine", "healthcare", "treatment"]
        },
        {
            "id": "side-quest-smuggler-2020",
            "name": "Captain Contraband",
            "role": "Smuggler",
            "faction": "Criminal",
            "location": "Various Cities",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'1\"",
                "build": "tough",
                "hair": "long gray",
                "eyes": "piercing blue",
                "clothing_style": "leather jacket",
                "distinctive_features": "Captain's hat, smuggling scars"
            },
            "personality": "rough, experienced, loyal",
            "background": "A smuggler who helps transport goods and people illegally",
            "quest_connections": ["side-quest-smuggling", "side-quest-transport"],
            "dialogue_topics": ["smuggling", "transport", "black-market"]
        },
        {
            "id": "side-quest-artist-2020",
            "name": "Canvas Cara",
            "role": "Street Artist",
            "faction": "Civilians",
            "location": "Various Cities",
            "appearance": {
                "age": 26,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'5\"",
                "build": "slender",
                "hair": "colorful dreadlocks",
                "eyes": "expressive brown",
                "clothing_style": "artistic bohemian",
                "distinctive_features": "Paint stains, sketchbook"
            },
            "personality": "creative, free-spirited, observant",
            "background": "A street artist who creates murals and helps with artistic side quests",
            "quest_connections": ["side-quest-art", "side-quest-graffiti"],
            "dialogue_topics": ["art", "creativity", "expression"]
        },
        {
            "id": "side-quest-bodyguard-2020",
            "name": "Iron Mike",
            "role": "Personal Security",
            "faction": "Mercenary",
            "location": "Various Cities",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'3\"",
                "build": "muscular",
                "hair": "bald",
                "eyes": "stern brown",
                "clothing_style": "tactical gear",
                "distinctive_features": "Military bearing, cybernetic arm"
            },
            "personality": "stoic, professional, protective",
            "background": "A former military bodyguard who provides security services",
            "quest_connections": ["side-quest-protection", "side-quest-escort"],
            "dialogue_topics": ["security", "protection", "combat"]
        },
        {
            "id": "side-quest-chef-2020",
            "name": "Culinary Carlos",
            "role": "Street Chef",
            "faction": "Civilians",
            "location": "Various Cities",
            "appearance": {
                "age": 33,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'9\"",
                "build": "stocky",
                "hair": "short black",
                "eyes": "warm brown",
                "clothing_style": "chef whites",
                "distinctive_features": "Chef hat, apron with stains"
            },
            "personality": "passionate, friendly, food-obsessed",
            "background": "A chef who creates amazing food and helps with culinary side quests",
            "quest_connections": ["side-quest-cooking", "side-quest-recipe"],
            "dialogue_topics": ["cooking", "food", "culinary-arts"]
        },
        {
            "id": "side-quest-gambler-2020",
            "name": "Lucky Lena",
            "role": "Professional Gambler",
            "faction": "Criminal",
            "location": "Various Cities",
            "appearance": {
                "age": 29,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "petite",
                "hair": "long black",
                "eyes": "sharp green",
                "clothing_style": "elegant casino wear",
                "distinctive_features": "Poker face, lucky charm bracelet"
            },
            "personality": "calculating, charming, risk-taking",
            "background": "A skilled gambler who helps with casino and gambling related side quests",
            "quest_connections": ["side-quest-gambling", "side-quest-casino"],
            "dialogue_topics": ["gambling", "luck", "strategy"]
        }
    ]

    return side_npcs

def generate_side_quests_dialogues(npcs):
    """Generate dialogue nodes for side quests NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hey there. I'm {npc['name']}. What can I do for you?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What kind of work do you do?",
                    "next_dialogue_id": f"{npc['id']}-work-info"
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
            "text": f"I'm {npc['role']}. {npc['background']}",
            "responses": [
                {
                    "text": "What kind of work do you do?",
                    "next_dialogue_id": f"{npc['id']}-work-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(about_me_dialogue)

        # Generate work-info dialogue
        work_topics = npc["dialogue_topics"]
        work_text = f"I specialize in {', '.join(work_topics[:2])}. Got any jobs in that area?"

        work_dialogue = {
            "id": f"{npc['id']}-work-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": work_text,
            "responses": [
                {
                    "text": "I might have something for you",
                    "next_dialogue_id": f"{npc['id']}-job-interest"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(work_dialogue)

        # Generate job-interest dialogue
        job_dialogue = {
            "id": f"{npc['id']}-job-interest",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "quest",
            "text": "Really? I'm always interested in good work. What's the job?",
            "responses": [
                {
                    "text": "I'll let you know when I have something",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(job_dialogue)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPCs"""

    changeset_id = f"npcs-side-quests-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'side-quests-npcs-import',
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
    print(f"Generated {len(npcs)} side quests NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-side-quests-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'side-quests-dialogues-import',
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
    print(f"Generated {len(dialogues)} side quests dialogue nodes")

def main():
    """Main function to generate side quests NPCs and dialogues"""

    print("Generating side quests NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_side_quests_npcs()
    print(f"Generated {len(npcs)} side quests NPCs")

    # Generate dialogues
    dialogues = generate_side_quests_dialogues(npcs)
    print(f"Generated {len(dialogues)} side quests dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_side_quests_2020_2077_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_side_quests_2020_2077_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("Side quests NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
