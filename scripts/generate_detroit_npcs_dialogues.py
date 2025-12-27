#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Detroit Quests
Creates supporting NPC characters and dialogue nodes for Detroit city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_detroit_npcs():
    """Generate NPC characters for Detroit quests"""

    detroit_npcs = [
        {
            "id": "detroit-motown-producer-2020",
            "name": "Berry Gordy Jr.",
            "role": "Music Producer",
            "faction": "Motown Legacy",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 94,
                "gender": "male",
                "ethnicity": "african american",
                "height": "5'7\"",
                "build": "slender",
                "hair": "gray",
                "eyes": "dark brown",
                "clothing_style": "classic business",
                "distinctive_features": "Signature glasses, dignified presence"
            },
            "personality": "wise, passionate about music, business-savvy",
            "background": "Legendary founder of Motown Records, still active in Detroit's music scene",
            "quest_connections": ["quest-002-motown-music", "quest-023-motown-music-producer"],
            "dialogue_topics": ["motown-history", "music-production", "detroit-culture"]
        },
        {
            "id": "detroit-urban-farmer-2020",
            "name": "Grace Lee Boggs",
            "role": "Community Activist",
            "faction": "Urban Renewal",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 103,
                "gender": "female",
                "ethnicity": "asian american",
                "height": "5'3\"",
                "build": "petite",
                "hair": "white",
                "eyes": "sharp brown",
                "clothing_style": "practical activist",
                "distinctive_features": "Bright eyes full of wisdom"
            },
            "personality": "wise, revolutionary, compassionate",
            "background": "Long-time Detroit activist working on urban farming and community renewal",
            "quest_connections": ["quest-011-vertical-farming-revolution", "quest-020-vertical-farming-pioneer"],
            "dialogue_topics": ["urban-farming", "community-activism", "detroit-renewal"]
        },
        {
            "id": "detroit-robotics-engineer-2020",
            "name": "Carlos Rodriguez",
            "role": "Robotics Engineer",
            "faction": "Tech Innovation",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "latino",
                "height": "6'0\"",
                "build": "fit",
                "hair": "black with gray",
                "eyes": "brown",
                "clothing_style": "engineer overalls",
                "distinctive_features": "Safety goggles, tool belt"
            },
            "personality": "innovative, detail-oriented, passionate about technology",
            "background": "Leading robotics engineer revitalizing Detroit's manufacturing sector",
            "quest_connections": ["quest-017-robotics-revolution-engineer", "quest-011-autonomous-vehicle-revolution"],
            "dialogue_topics": ["robotics", "automotive-tech", "detroit-manufacturing"]
        },
        {
            "id": "detroit-street-artist-2020",
            "name": "Tyree Guyton",
            "role": "Street Artist",
            "faction": "Cultural Revival",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 62,
                "gender": "male",
                "ethnicity": "african american",
                "height": "5'10\"",
                "build": "athletic",
                "hair": "dreadlocks",
                "eyes": "intense brown",
                "clothing_style": "artist casual",
                "distinctive_features": "Paint-stained hands, creative energy"
            },
            "personality": "creative, socially conscious, transformative",
            "background": "Famous Detroit street artist known for Heidelberg Project",
            "quest_connections": ["quest-004-abandoned-ruins", "quest-026-abandoned-factory-exploration"],
            "dialogue_topics": ["street-art", "urban-transformation", "detroit-art-scene"]
        },
        {
            "id": "detroit-underground-dj-2020",
            "name": "Derrick May",
            "role": "Techno Pioneer",
            "faction": "Electronic Music",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 56,
                "gender": "male",
                "ethnicity": "african american",
                "height": "6'1\"",
                "build": "slender",
                "hair": "short afro",
                "eyes": "piercing brown",
                "clothing_style": "techno artist",
                "distinctive_features": "Signature glasses, electronic accessories"
            },
            "personality": "innovative, rhythmic, underground",
            "background": "One of the Belleville Three, pioneer of Detroit techno music",
            "quest_connections": ["quest-005-techno-birthplace", "quest-025-underground-music-scene"],
            "dialogue_topics": ["detroit-techno", "electronic-music", "underground-culture"]
        },
        {
            "id": "detroit-neural-clinic-owner-2020",
            "name": "Dr. Neuralink",
            "role": "Neural Enhancement Specialist",
            "faction": "Medical Tech",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 48,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'8\"",
                "build": "professional",
                "hair": "long black",
                "eyes": "sharp green",
                "clothing_style": "medical professional",
                "distinctive_features": "Neural interface ports visible"
            },
            "personality": "professional, innovative, ethical",
            "background": "Leading specialist in neural implants and cybernetic enhancements",
            "quest_connections": ["quest-012-neural-implant-underground", "quest-024-cybernetic-enhancement-clinic"],
            "dialogue_topics": ["neural-implants", "cybernetics", "medical-tech"]
        },
        {
            "id": "detroit-hockey-coach-2020",
            "name": "Coach Gordie Howe",
            "role": "Hockey Coach",
            "faction": "Sports Legacy",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 92,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "athletic",
                "hair": "white",
                "eyes": "blue",
                "clothing_style": "hockey coach",
                "distinctive_features": "Red Wings jersey, confident posture"
            },
            "personality": "tough, knowledgeable, team-oriented",
            "background": "Legendary Detroit Red Wings player and coach, Mr. Hockey himself",
            "quest_connections": ["quest-009-red-wings-hockey"],
            "dialogue_topics": ["hockey", "detroit-sports", "red-wings-legacy"]
        },
        {
            "id": "detroit-urban-planner-2020",
            "name": "Marcus Greene",
            "role": "Urban Planner",
            "faction": "City Development",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "african american",
                "height": "5'11\"",
                "build": "fit",
                "hair": "short black",
                "eyes": "determined brown",
                "clothing_style": "business casual",
                "distinctive_features": "Architect blueprints, purposeful stride"
            },
            "personality": "visionary, determined, community-focused",
            "background": "Urban planner working on Detroit's renaissance and smart city initiatives",
            "quest_connections": ["quest-010-revival-hope", "quest-022-urban-renewal-architect"],
            "dialogue_topics": ["urban-planning", "city-renewal", "detroit-future"]
        }
    ]

    return detroit_npcs

def generate_detroit_dialogues(npcs):
    """Generate dialogue nodes for Detroit NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hello there! I'm {npc['name']}. What brings you to the Motor City?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What do you know about Detroit?",
                    "next_dialogue_id": f"{npc['id']}-detroit-info"
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
            "text": f"I'm {npc['role']} here in Detroit. {npc['background']}",
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

        # Generate detroit-info dialogue
        detroit_topics = npc["dialogue_topics"]
        detroit_text = f"Detroit has such a rich and complex history! We're known for our {', '.join(detroit_topics[:2])} culture."

        detroit_dialogue = {
            "id": f"{npc['id']}-detroit-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": detroit_text,
            "responses": [
                {
                    "text": "Tell me more about the Motor City",
                    "next_dialogue_id": f"{npc['id']}-motor-city-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(detroit_dialogue)

        # Generate role-info dialogue
        role_dialogue = {
            "id": f"{npc['id']}-role-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": f"My role here is to help preserve and build upon Detroit's legacy. There's so much potential in this city!",
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

        # Generate motor-city-info dialogue
        motor_city_text = f"The Motor City has been through so much - bankruptcy, rebirth, innovation. We're a city of survivors and innovators!"

        motor_city_dialogue = {
            "id": f"{npc['id']}-motor-city-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": motor_city_text,
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
        dialogues.append(motor_city_dialogue)

        # Generate help-offer dialogue
        help_dialogue = {
            "id": f"{npc['id']}-help-offer",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "quest",
            "text": "Actually, there are some things you could help with. I have tasks that would really make a difference here in Detroit.",
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

    changeset_id = f"npcs-detroit-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'detroit-npcs-import',
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
    print(f"Generated {len(npcs)} Detroit NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-detroit-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'detroit-dialogues-import',
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
    print(f"Generated {len(dialogues)} Detroit dialogue nodes")

def main():
    """Main function to generate Detroit NPCs and dialogues"""

    print("Generating Detroit NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_detroit_npcs()
    print(f"Generated {len(npcs)} Detroit NPCs")

    # Generate dialogues
    dialogues = generate_detroit_dialogues(npcs)
    print(f"Generated {len(dialogues)} Detroit dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_detroit_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_detroit_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("Detroit NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()