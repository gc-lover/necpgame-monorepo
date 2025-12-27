#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Dallas Quests
Creates supporting NPC characters and dialogue nodes for Dallas city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_dallas_npcs():
    """Generate NPC characters for Dallas quests"""

    dallas_npcs = [
        {
            "id": "dallas-tour-guide-2020",
            "name": "Texas Rose",
            "role": "City Tour Guide",
            "faction": "Civilians",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'8\"",
                "build": "athletic",
                "hair": "long brown",
                "eyes": "green",
                "clothing_style": "casual tourist guide",
                "distinctive_features": "Texas accent, friendly smile"
            },
            "personality": "enthusiastic, knowledgeable about Dallas history",
            "background": "Local tour guide who knows all the best spots in Dallas",
            "quest_connections": ["quest-001-jfk-memorial", "quest-008-reunion-tower"],
            "dialogue_topics": ["dallas-history", "tourist-attractions", "local-culture"]
        },
        {
            "id": "dallas-cowboy-fan-2020",
            "name": "Billy Bob Thornton",
            "role": "Sports Enthusiast",
            "faction": "Civilians",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'1\"",
                "build": "stocky",
                "hair": "short brown",
                "eyes": "blue",
                "clothing_style": "cowboy hat and boots",
                "distinctive_features": "thick Texas accent, Cowboys jersey"
            },
            "personality": "passionate about football, friendly",
            "background": "Die-hard Dallas Cowboys fan who lives for game day",
            "quest_connections": ["quest-002-cowboys-stadium"],
            "dialogue_topics": ["football", "dallas-cowboys", "sports-culture"]
        },
        {
            "id": "dallas-bbq-master-2020",
            "name": "Smokey Joe",
            "role": "BBQ Pit Master",
            "faction": "Merchants",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "african-american",
                "height": "5'10\"",
                "build": "solid",
                "hair": "gray beard",
                "eyes": "brown",
                "clothing_style": "apron and chef hat",
                "distinctive_features": "smoky smell, BBQ sauce stains"
            },
            "personality": "proud, expert in Texas BBQ traditions",
            "background": "Third-generation BBQ master from East Dallas",
            "quest_connections": ["quest-003-bbq-texas-style"],
            "dialogue_topics": ["texas-bbq", "cooking-techniques", "food-traditions"]
        },
        {
            "id": "dallas-oil-engineer-2020",
            "name": "Dr. Petroleum",
            "role": "Oil Industry Engineer",
            "faction": "Corporations",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 48,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "average",
                "hair": "short gray",
                "eyes": "blue",
                "clothing_style": "business casual",
                "distinctive_features": "oil-stained hands, Texas Instruments watch"
            },
            "personality": "technical, proud of oil industry history",
            "background": "Petroleum engineer working for major Texas oil companies",
            "quest_connections": ["quest-004-oil-legacy"],
            "dialogue_topics": ["oil-industry", "texas-energy", "corporate-history"]
        },
        {
            "id": "dallas-rodeo-clown-2020",
            "name": "Crazy Tex",
            "role": "Rodeo Entertainer",
            "faction": "Entertainers",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "5'9\"",
                "build": "muscular",
                "hair": "bright red wig",
                "eyes": "brown",
                "clothing_style": "colorful rodeo clown costume",
                "distinctive_features": "painted face, exaggerated makeup"
            },
            "personality": "funny, energetic, loves entertaining crowds",
            "background": "Professional rodeo clown with years of experience",
            "quest_connections": ["quest-005-rodeo"],
            "dialogue_topics": ["rodeo-traditions", "western-culture", "entertainment"]
        },
        {
            "id": "dallas-chef-2020",
            "name": "Maria Gonzalez",
            "role": "Tex-Mex Chef",
            "faction": "Merchants",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 45,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'6\"",
                "build": "curvy",
                "hair": "long black",
                "eyes": "brown",
                "clothing_style": "chef uniform",
                "distinctive_features": "warm smile, cooking apron"
            },
            "personality": "passionate about food, welcoming",
            "background": "Award-winning Tex-Mex chef with family recipes",
            "quest_connections": ["quest-006-tex-mex-food"],
            "dialogue_topics": ["tex-mex-cuisine", "cooking", "cultural-fusion"]
        },
        {
            "id": "dallas-big-tex-2020",
            "name": "Big Tex",
            "role": "Fair Mascot",
            "faction": "Civilians",
            "location": "Dallas, Texas",
            "appearance": {
                "age": "symbolic",
                "gender": "male",
                "ethnicity": "symbolic",
                "height": "52 feet",
                "build": "giant cowboy",
                "hair": "none",
                "eyes": "none",
                "clothing_style": "giant cowboy outfit",
                "distinctive_features": "giant fiberglass statue, iconic hat"
            },
            "personality": "silent but iconic symbol of Texas pride",
            "background": "Giant cowboy statue, symbol of the State Fair of Texas",
            "quest_connections": ["quest-007-big-tex"],
            "dialogue_topics": ["texas-fair", "state-symbols", "texas-pride"]
        },
        {
            "id": "dallas-reunion-guide-2020",
            "name": "Sky Captain",
            "role": "Observation Deck Guide",
            "faction": "Tourism",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "fit",
                "hair": "short brown",
                "eyes": "blue",
                "clothing_style": "uniform",
                "distinctive_features": "name tag, professional demeanor"
            },
            "personality": "informative, safety-conscious",
            "background": "Guide at the Reunion Tower observation deck",
            "quest_connections": ["quest-008-reunion-tower"],
            "dialogue_topics": ["city-views", "architecture", "tourism"]
        },
        {
            "id": "dallas-stockyards-boss-2020",
            "name": "Cattle Baron",
            "role": "Livestock Trader",
            "faction": "Merchants",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 60,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "solid",
                "hair": "white mustache",
                "eyes": "sharp blue",
                "clothing_style": "western business",
                "distinctive_features": "cowboy boots, bolo tie"
            },
            "personality": "traditional, business-savvy",
            "background": "Long-time trader at Fort Worth Stockyards",
            "quest_connections": ["quest-009-fort-worth-stockyards"],
            "dialogue_topics": ["cattle-industry", "texas-history", "trading"]
        },
        {
            "id": "dallas-local-2020",
            "name": "Everything Bigger Fan",
            "role": "Local Resident",
            "faction": "Civilians",
            "location": "Dallas, Texas",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "curly brown",
                "eyes": "green",
                "clothing_style": "casual",
                "distinctive_features": "Texas pride tattoo, friendly"
            },
            "personality": "proud Texan, loves showing off local culture",
            "background": "Born and raised in Dallas, passionate about Texas culture",
            "quest_connections": ["quest-010-everything-bigger"],
            "dialogue_topics": ["texas-pride", "local-culture", "texas-slogans"]
        }
    ]

    return dallas_npcs

def generate_dallas_dialogues(npcs):
    """Generate dialogue nodes for Dallas NPCs"""

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
                    "body": f"Приветствую в Dallas! Я {npc['name']}, {npc['role'].lower()}. Чем могу помочь?",
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
                        "body": f"Я могу рассказать о {quest_id.replace('quest-', 'квесте ')} и дать советы.",
                        "mechanics_links": [f"knowledge/canon/lore/timeline-author/quests/america/dallas/2020-2029/{quest_id}.yaml"],
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
            'author': 'dallas-npcs-generator',
            'changes': [
                {
                    'insert': {
                        'tableName': 'narrative.npc_definitions',
                        'columns': [
                            {'column': {'name': 'id', 'value': str(uuid.uuid4())}},
                            {'column': {'name': 'metadata', 'value': json.dumps({
                                'id': f"canon-npc-{npc['id']}",
                                'version': '2.0.0',
                                'source_file': f"scripts/generate_dallas_npcs_dialogues.py",
                                'city': 'Dallas',
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
    print(f"Generated {len(changesets)} Dallas NPCs")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML for dialogue data"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'dallas-dialogues-generator',
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
                                'source_file': f"scripts/generate_dallas_npcs_dialogues.py",
                                'city': 'Dallas',
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
    print(f"Generated {len(changesets)} Dallas dialogue nodes")

def main():
    """Main function"""
    print("Generating Dallas NPCs and Dialogues...")

    # Generate NPCs
    dallas_npcs = generate_dallas_npcs()
    print(f"Generated {len(dallas_npcs)} Dallas NPCs")

    # Generate dialogues
    dallas_dialogues = generate_dallas_dialogues(dallas_npcs)
    print(f"Generated {len(dallas_dialogues)} Dallas dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_dallas-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_dallas-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(dallas_npcs, npcs_output)
    create_liquibase_dialogues(dallas_dialogues, dialogues_output)

    print("Dallas NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
