#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Seattle Quests
Creates supporting NPC characters and dialogue nodes for Seattle city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_seattle_npcs():
    """Generate NPC characters for Seattle quests"""

    seattle_npcs = [
        {
            "id": "seattle-tour-guide-2020",
            "name": "Rain City Rachel",
            "role": "City Tour Guide",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 32,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "athletic",
                "hair": "long dark",
                "eyes": "brown",
                "clothing_style": "casual tech worker",
                "distinctive_features": "Seattle accent, knows all the coffee spots"
            },
            "personality": "enthusiastic, knowledgeable about Seattle landmarks",
            "background": "Local tour guide who loves sharing Seattle's history and hidden gems",
            "quest_connections": ["quest-001-space-needle", "quest-002-pike-place-market"],
            "dialogue_topics": ["seattle-landmarks", "coffee-culture", "tech-scene"]
        },
        {
            "id": "seattle-coffee-expert-2020",
            "name": "Starbucks Sam",
            "role": "Coffee Historian",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "stocky",
                "hair": "short brown",
                "eyes": "blue",
                "clothing_style": "casual barista",
                "distinctive_features": "Expert on Seattle coffee culture"
            },
            "personality": "passionate about coffee, loves storytelling",
            "background": "Former barista who knows the history of coffee in Seattle",
            "quest_connections": ["quest-003-starbucks-origin"],
            "dialogue_topics": ["coffee-history", "starbucks-story", "local-cafes"]
        },
        {
            "id": "seattle-grunge-musician-2020",
            "name": "Kurt's Shadow",
            "role": "Underground Musician",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "lean",
                "hair": "long unkempt",
                "eyes": "piercing blue",
                "clothing_style": "grunge style",
                "distinctive_features": "Tattoos, guitar case"
            },
            "personality": "introspective, passionate about music",
            "background": "Local musician keeping the grunge spirit alive",
            "quest_connections": ["quest-004-grunge-music"],
            "dialogue_topics": ["grunge-history", "underground-music", "seattle-scene"]
        },
        {
            "id": "seattle-amazon-employee-2020",
            "name": "Alexa Jones",
            "role": "Tech Worker",
            "faction": "Corporation",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "asian",
                "height": "5'4\"",
                "build": "petite",
                "hair": "black ponytail",
                "eyes": "brown",
                "clothing_style": "corporate casual",
                "distinctive_features": "Always has latest tech gadgets"
            },
            "personality": "ambitious, tech-savvy",
            "background": "Mid-level employee at Amazon HQ, knows corporate secrets",
            "quest_connections": ["quest-005-amazon-hq"],
            "dialogue_topics": ["tech-industry", "corporate-life", "amazon-culture"]
        },
        {
            "id": "seattle-mountain-guide-2020",
            "name": "Rainier Rick",
            "role": "Mountain Guide",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'2\"",
                "build": "muscular",
                "hair": "gray beard",
                "eyes": "gray",
                "clothing_style": "outdoor gear",
                "distinctive_features": "Weather-beaten face, mountaineer boots"
            },
            "personality": "experienced, cautious",
            "background": "Veteran mountain guide who knows Mount Rainier like his backyard",
            "quest_connections": ["quest-006-mount-rainier"],
            "dialogue_topics": ["mountaineering", "rainier-history", "outdoor-adventures"]
        },
        {
            "id": "seattle-weather-expert-2020",
            "name": "Drizzle Dana",
            "role": "Meteorologist",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'7\"",
                "build": "average",
                "hair": "curly brown",
                "eyes": "brown",
                "clothing_style": "practical rain gear",
                "distinctive_features": "Always prepared for rain"
            },
            "personality": "cheerful despite the weather",
            "background": "Local weather expert who embraces Seattle's rainy climate",
            "quest_connections": ["quest-007-rain-rain-rain"],
            "dialogue_topics": ["seattle-weather", "rain-culture", "climate-adaptation"]
        },
        {
            "id": "seattle-boeing-engineer-2020",
            "name": "747 Frank",
            "role": "Aerospace Engineer",
            "faction": "Corporation",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'9\"",
                "build": "average",
                "hair": "receding gray",
                "eyes": "blue",
                "clothing_style": "engineer coveralls",
                "distinctive_features": "Safety glasses, Boeing badge"
            },
            "personality": "proud, knowledgeable",
            "background": "Boeing factory worker with decades of experience",
            "quest_connections": ["quest-008-boeing-factory"],
            "dialogue_topics": ["aerospace-industry", "boeing-history", "aviation-tech"]
        },
        {
            "id": "seattle-fisherman-2020",
            "name": "Salmon Steve",
            "role": "Commercial Fisherman",
            "faction": "Civilians",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "native american",
                "height": "6'0\"",
                "build": "strong",
                "hair": "long black",
                "eyes": "dark brown",
                "clothing_style": "fisherman gear",
                "distinctive_features": "Calloused hands, fish tattoos"
            },
            "personality": "hardworking, nature-loving",
            "background": "Local fisherman who knows Puget Sound's salmon runs",
            "quest_connections": ["quest-009-seafood-salmon"],
            "dialogue_topics": ["fishing-industry", "salmon-conservation", "marine-life"]
        },
        {
            "id": "seattle-tech-entrepreneur-2020",
            "name": "Startup Sarah",
            "role": "Tech Entrepreneur",
            "faction": "Corporation",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 30,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'5\"",
                "build": "slim",
                "hair": "short pixie",
                "eyes": "green",
                "clothing_style": "business casual",
                "distinctive_features": "Multiple devices, confident posture"
            },
            "personality": "ambitious, innovative",
            "background": "Tech entrepreneur navigating Seattle's startup scene",
            "quest_connections": ["quest-010-tech-boom-gentrification"],
            "dialogue_topics": ["startup-culture", "tech-innovation", "gentrification"]
        },
        {
            "id": "seattle-underground-hacker-2020",
            "name": "Matrix Max",
            "role": "Cybersecurity Expert",
            "faction": "Underground",
            "location": "Seattle, Washington",
            "appearance": {
                "age": 26,
                "gender": "male",
                "ethnicity": "asian",
                "height": "5'8\"",
                "build": "slight",
                "hair": "neon blue",
                "eyes": "brown",
                "clothing_style": "cyberpunk",
                "distinctive_features": "Neural implants visible"
            },
            "personality": "mysterious, tech-savvy",
            "background": "Underground hacker navigating Seattle's digital underworld",
            "quest_connections": ["quest-017-ghost-in-the-cloud"],
            "dialogue_topics": ["cybersecurity", "underground-networks", "neural-tech"]
        }
    ]

    return seattle_npcs

def generate_seattle_dialogues(npcs):
    """Generate dialogue nodes for Seattle NPCs"""

    dialogues = []

    for npc in npcs:
        # Base dialogue node
        base_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc['id'],
            "type": "greeting",
            "text": f"Hello! I'm {npc['name']}, {npc['role'].lower()} here in Seattle.",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about",
                    "conditions": []
                },
                {
                    "text": "What do you know about Seattle?",
                    "next_dialogue_id": f"{npc['id']}-seattle",
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        # About dialogue
        about_dialogue = {
            "id": f"{npc['id']}-about",
            "npc_id": npc['id'],
            "type": "information",
            "text": npc['background'],
            "responses": [
                {
                    "text": "Interesting! Tell me more about Seattle",
                    "next_dialogue_id": f"{npc['id']}-seattle",
                    "conditions": []
                },
                {
                    "text": "Goodbye",
                    "next_dialogue_id": None,
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        # Seattle-specific dialogue
        seattle_dialogue = {
            "id": f"{npc['id']}-seattle",
            "npc_id": npc['id'],
            "type": "information",
            "text": f"Seattle is an amazing city! {npc['personality']}",
            "responses": [
                {
                    "text": "Thanks for the information",
                    "next_dialogue_id": None,
                    "conditions": []
                }
            ],
            "metadata": {
                "location": npc['location'],
                "quest_relevance": npc.get('quest_connections', []),
                "topics": npc.get('dialogue_topics', [])
            }
        }

        dialogues.extend([base_dialogue, about_dialogue, seattle_dialogue])

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPC import"""

    changesets = []

    for npc in npcs:
        changeset_id = f"npcs-seattle-{npc['id']}-{hashlib.md5(str(npc).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'seattle-npcs-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.npc_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'npc_id', 'value': npc['id']}},
                            {'column': {'name': 'name', 'value': npc['name']}},
                            {'column': {'name': 'role', 'value': npc['role']}},
                            {'column': {'name': 'faction', 'value': npc['faction']}},
                            {'column': {'name': 'location', 'value': npc['location']}},
                            {'column': {'name': 'appearance', 'value': json.dumps(npc['appearance'], ensure_ascii=False)}},
                            {'column': {'name': 'personality', 'value': npc['personality']}},
                            {'column': {'name': 'background', 'value': npc['background']}},
                            {'column': {'name': 'quest_connections', 'value': json.dumps(npc['quest_connections'], ensure_ascii=False)}},
                            {'column': {'name': 'dialogue_topics', 'value': json.dumps(npc['dialogue_topics'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'city': 'Seattle, Washington, USA',
                                'period': '2020-2029',
                                'source': 'generated',
                                'created_at': datetime.now().isoformat()
                            }, ensure_ascii=False)}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase NPCs file: {output_file}")
    print(f"Generated {len(changesets)} Seattle NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogue import"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-seattle-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'seattle-dialogues-import',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.dialogue_nodes',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'dialogue_id', 'value': dialogue['id']}},
                            {'column': {'name': 'npc_id', 'value': dialogue['npc_id']}},
                            {'column': {'name': 'type', 'value': dialogue['type']}},
                            {'column': {'name': 'text', 'value': dialogue['text']}},
                            {'column': {'name': 'responses', 'value': json.dumps(dialogue['responses'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps(dialogue['metadata'], ensure_ascii=False)}}
                        ]
                    }
                }
            ]
        }

        changesets.append(changeset)

    liquibase_data = {
        'databaseChangeLog': changesets
    }

    output_file.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Liquibase dialogues file: {output_file}")
    print(f"Generated {len(changesets)} Seattle dialogue nodes")

def main():
    """Main function"""
    print("Generating Seattle NPCs and Dialogues...")

    # Generate NPCs
    seattle_npcs = generate_seattle_npcs()
    print(f"Generated {len(seattle_npcs)} Seattle NPCs")

    # Generate dialogues
    seattle_dialogues = generate_seattle_dialogues(seattle_npcs)
    print(f"Generated {len(seattle_dialogues)} Seattle dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_seattle-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_seattle-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(seattle_npcs, npcs_output)
    create_liquibase_dialogues(seattle_dialogues, dialogues_output)

    print("Seattle NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
