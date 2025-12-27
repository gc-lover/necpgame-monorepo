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
            "id": "detroit-car-mechanic-2020",
            "name": "Rusty Rodriguez",
            "role": "Automotive Mechanic",
            "faction": "Merchants",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "5'9\"",
                "build": "stocky",
                "hair": "gray beard",
                "eyes": "brown",
                "clothing_style": "greasy mechanic overalls",
                "distinctive_features": "oil-stained hands, Detroit Tigers cap"
            },
            "personality": "gruff but knowledgeable, passionate about cars",
            "background": "Third-generation mechanic from Detroit's auto industry",
            "quest_connections": ["quest-001-motor-city", "quest-011-autonomous-vehicle-revolution"],
            "dialogue_topics": ["cars", "auto-industry", "detroit-history"]
        },
        {
            "id": "detroit-music-producer-2020",
            "name": "Diamond Diaz",
            "role": "Music Producer",
            "faction": "Artists",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 38,
                "gender": "female",
                "ethnicity": "african-american",
                "height": "5'6\"",
                "build": "athletic",
                "hair": "braided with gold accents",
                "eyes": "brown",
                "clothing_style": "urban streetwear",
                "distinctive_features": "gold chains, confident posture"
            },
            "personality": "creative, energetic, proud of Detroit music heritage",
            "background": "Music producer keeping Motown and techno traditions alive",
            "quest_connections": ["quest-002-motown-music", "quest-005-techno-birthplace"],
            "dialogue_topics": ["music", "techno", "motown-legacy"]
        },
        {
            "id": "detroit-community-activist-2020",
            "name": "Hope Williams",
            "role": "Community Organizer",
            "faction": "Activists",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 45,
                "gender": "female",
                "ethnicity": "african-american",
                "height": "5'8\"",
                "build": "fit",
                "hair": "natural afro",
                "eyes": "determined brown",
                "clothing_style": "practical activist wear",
                "distinctive_features": "activist buttons, community pride"
            },
            "personality": "passionate, resilient, hopeful for Detroit's future",
            "background": "Community organizer working on Detroit's revival",
            "quest_connections": ["quest-003-bankruptcy-2013", "quest-010-revival-hope"],
            "dialogue_topics": ["community", "revival", "detroit-challenges"]
        },
        {
            "id": "detroit-urban-explorer-2020",
            "name": "Ghost Hunter",
            "role": "Urban Explorer",
            "faction": "Adventurers",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 32,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "lean",
                "hair": "shaggy brown",
                "eyes": "sharp blue",
                "clothing_style": "practical explorer gear",
                "distinctive_features": "camera strap, adventurous spirit"
            },
            "personality": "curious, cautious, fascinated by abandoned places",
            "background": "Urban explorer documenting Detroit's abandoned buildings",
            "quest_connections": ["quest-004-abandoned-ruins"],
            "dialogue_topics": ["urban-exploration", "abandoned-buildings", "detroit-ghosts"]
        },
        {
            "id": "detroit-hot-dog-vendor-2020",
            "name": "Coney King",
            "role": "Food Vendor",
            "faction": "Merchants",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 58,
                "gender": "male",
                "ethnicity": "greek",
                "height": "5'7\"",
                "build": "solid",
                "hair": "white mustache",
                "eyes": "wise brown",
                "clothing_style": "food service apron",
                "distinctive_features": "thick accent, warm smile"
            },
            "personality": "friendly, proud of culinary traditions",
            "background": "Owner of a classic Coney Island hot dog stand",
            "quest_connections": ["quest-006-coney-island-hot-dogs"],
            "dialogue_topics": ["food", "hot-dogs", "culinary-traditions"]
        },
        {
            "id": "detroit-historian-2020",
            "name": "Dr. Civil Rights",
            "role": "Local Historian",
            "faction": "Educators",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 62,
                "gender": "female",
                "ethnicity": "african-american",
                "height": "5'5\"",
                "build": "dignified",
                "hair": "salt-and-pepper",
                "eyes": "wise",
                "clothing_style": "professional academic",
                "distinctive_features": "spectacles, air of authority"
            },
            "personality": "knowledgeable, passionate about history",
            "background": "Professor specializing in Detroit's civil rights history",
            "quest_connections": ["quest-007-1967-riots"],
            "dialogue_topics": ["history", "civil-rights", "social-justice"]
        },
        {
            "id": "detroit-rapper-2020",
            "name": "8 Mile MC",
            "role": "Rapper",
            "faction": "Artists",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 28,
                "gender": "male",
                "ethnicity": "african-american",
                "height": "5'11\"",
                "build": "muscular",
                "hair": "faded haircut",
                "eyes": "intense",
                "clothing_style": "hip-hop streetwear",
                "distinctive_features": "gold grill, confident swagger"
            },
            "personality": "talented, competitive, passionate about hip-hop",
            "background": "Emerging rapper inspired by 8 Mile Road legends",
            "quest_connections": ["quest-008-8-mile-road"],
            "dialogue_topics": ["hip-hop", "rap-battles", "detroit-music-scene"]
        },
        {
            "id": "detroit-hockey-fan-2020",
            "name": "Red Wing Forever",
            "role": "Sports Fan",
            "faction": "Civilians",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 41,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "average",
                "hair": "short red",
                "eyes": "blue",
                "clothing_style": "Red Wings jersey",
                "distinctive_features": "winged wheel tattoo, team pride"
            },
            "personality": "loyal, enthusiastic about hockey",
            "background": "Lifelong Red Wings fan through thick and thin",
            "quest_connections": ["quest-009-red-wings-hockey"],
            "dialogue_topics": ["hockey", "red-wings", "detroit-sports"]
        },
        {
            "id": "detroit-tech-entrepreneur-2020",
            "name": "Neural Net",
            "role": "Tech Entrepreneur",
            "faction": "Corporations",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 36,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'1\"",
                "build": "fit",
                "hair": "short black",
                "eyes": "sharp brown",
                "clothing_style": "modern business casual",
                "distinctive_features": "cybernetic implant visible, tech accessories"
            },
            "personality": "innovative, forward-thinking",
            "background": "Tech entrepreneur developing neural implant technology",
            "quest_connections": ["quest-012-neural-implant-underground"],
            "dialogue_topics": ["technology", "neural-implants", "innovation"]
        },
        {
            "id": "detroit-farmer-2020",
            "name": "Green Thumb",
            "role": "Urban Farmer",
            "faction": "Activists",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 49,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'7\"",
                "build": "strong",
                "hair": "long braided",
                "eyes": "green",
                "clothing_style": "practical farming clothes",
                "distinctive_features": "soil-stained hands, sun-weathered skin"
            },
            "personality": "dedicated, optimistic about urban farming",
            "background": "Urban farmer revolutionizing Detroit's food production",
            "quest_connections": ["quest-011-vertical-farming-revolution"],
            "dialogue_topics": ["farming", "urban-agriculture", "sustainability"]
        },
        {
            "id": "detroit-architect-2020",
            "name": "Urban Renewer",
            "role": "Architect",
            "faction": "Professionals",
            "location": "Detroit, Michigan",
            "appearance": {
                "age": 43,
                "gender": "male",
                "ethnicity": "african-american",
                "height": "6'0\"",
                "build": "professional",
                "hair": "short graying",
                "eyes": "focused",
                "clothing_style": "business casual",
                "distinctive_features": "blueprint tattoo, determined expression"
            },
            "personality": "visionary, committed to urban renewal",
            "background": "Architect leading Detroit's urban renewal projects",
            "quest_connections": ["quest-012-urban-renewal-tech"],
            "dialogue_topics": ["architecture", "urban-planning", "renewal"]
        }
    ]

    return detroit_npcs

def generate_detroit_dialogues(npcs):
    """Generate dialogue nodes for Detroit NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate basic greeting dialogue
        greeting_dialogue = {
            "id": f"dialogue-{npc['id']}-greeting",
            "npc_id": npc['id'],
            "type": "greeting",
            "conditions": {},
            "sections": [
                {
                    "id": "greeting",
                    "title": f"Знакомство с {npc['name']}",
                    "body": f"Приветствую в Motor City! Я {npc['name']}, {npc['role'].lower()}. Чем могу помочь?",
                    "mechanics_links": [],
                    "assets": []
                }
            ],
            "actions": {
                "set_flag": f"met_{npc['id']}"
            }
        }

        dialogues.append(greeting_dialogue)

        # Generate quest-specific dialogues
        for quest_id in npc['quest_connections']:
            quest_dialogue = {
                "id": f"dialogue-{npc['id']}-{quest_id}",
                "npc_id": npc['id'],
                "type": "quest_support",
                "conditions": {
                    "quest_active": quest_id
                },
                "sections": [
                    {
                        "id": "quest_help",
                        "title": f"Помощь с квестом {quest_id}",
                        "body": f"Я могу рассказать о {quest_id.replace('quest-', 'квесте ')} и дать советы по Detroit.",
                        "mechanics_links": [f"knowledge/canon/lore/timeline-author/quests/america/detroit/2020-2029/{quest_id}.yaml"],
                        "assets": []
                    }
                ],
                "actions": {
                    "give_hint": quest_id
                }
            }

            dialogues.append(quest_dialogue)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML for NPC data"""

    changesets = []

    for npc in npcs:
        changeset_id = f"npcs-{npc['id']}-{hashlib.md5(str(npc).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'detroit-npcs-generator',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.npc_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'id': f"canon-npc-{npc['id']}",
                                'version': '2.0.0',
                                'source_file': f"scripts/generate_detroit_npcs_dialogues.py",
                                'city': 'Detroit',
                                'period': '2020-2029'
                            }, ensure_ascii=False)}},
                            {'column': {'name': 'name', 'value': npc['name']}},
                            {'column': {'name': 'faction', 'value': npc['faction']}},
                            {'column': {'name': 'location', 'value': npc['location']}},
                            {'column': {'name': 'role', 'value': npc['role']}},
                            {'column': {'name': 'appearance', 'value': json.dumps(npc['appearance'], ensure_ascii=False)}},
                            {'column': {'name': 'stats', 'value': json.dumps({
                                'personality': npc['personality'],
                                'background': npc['background'],
                                'quest_connections': npc['quest_connections'],
                                'dialogue_topics': npc['dialogue_topics']
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

    print(f"Created Liquibase NPC file: {output_file}")
    print(f"Generated {len(changesets)} Detroit NPCs")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML for dialogue data"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'detroit-dialogues-generator',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.dialogue_nodes',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'node_data', 'value': json.dumps({
                                'sections': dialogue['sections']
                            }, ensure_ascii=False)}},
                            {'column': {'name': 'conditions', 'value': json.dumps(dialogue['conditions'], ensure_ascii=False)}},
                            {'column': {'name': 'actions', 'value': json.dumps(dialogue['actions'], ensure_ascii=False)}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'id': f"canon-dialogue-{dialogue['id']}",
                                'version': '2.0.0',
                                'npc_id': dialogue['npc_id'],
                                'type': dialogue['type'],
                                'source_file': f"scripts/generate_detroit_npcs_dialogues.py",
                                'city': 'Detroit',
                                'period': '2020-2029'
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

    print(f"Created Liquibase dialogues file: {output_file}")
    print(f"Generated {len(changesets)} Detroit dialogue nodes")

def main():
    """Main function"""
    print("Generating Detroit NPCs and Dialogues...")

    # Generate NPCs
    detroit_npcs = generate_detroit_npcs()
    print(f"Generated {len(detroit_npcs)} Detroit NPCs")

    # Generate dialogues
    detroit_dialogues = generate_detroit_dialogues(detroit_npcs)
    print(f"Generated {len(detroit_dialogues)} Detroit dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_detroit-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_detroit-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(detroit_npcs, npcs_output)
    create_liquibase_dialogues(detroit_dialogues, dialogues_output)

    print("Detroit NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
