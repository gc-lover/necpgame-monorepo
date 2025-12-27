#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Houston Quests
Creates supporting NPC characters and dialogue nodes for Houston city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_houston_npcs():
    """Generate NPC characters for Houston quests"""

    houston_npcs = [
        {
            "id": "houston-nasa-engineer-2020",
            "name": "Dr. Elena Vasquez",
            "role": "NASA Aerospace Engineer",
            "faction": "Corporations",
            "location": "Houston, Texas",
            "appearance": {
                "age": 38,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'6\"",
                "build": "athletic",
                "hair": "short black",
                "eyes": "brown",
                "clothing_style": "professional lab coat",
                "distinctive_features": "NASA badge, determined expression"
            },
            "personality": "brilliant, passionate about space exploration",
            "background": "NASA engineer working on next-generation spacecraft",
            "quest_connections": ["quest-001-nasa-space-center"],
            "dialogue_topics": ["space-exploration", "nasa-missions", "aerospace-tech"]
        },
        {
            "id": "houston-oil-worker-2020",
            "name": "Jack \"Black Gold\" Thompson",
            "role": "Oil Rig Worker",
            "faction": "Labor",
            "location": "Houston, Texas",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "muscular",
                "hair": "graying brown",
                "eyes": "blue",
                "clothing_style": "work boots and hard hat",
                "distinctive_features": "oil-stained hands, weathered face"
            },
            "personality": "tough, proud of working class heritage",
            "background": "Veteran oil rig worker from the Gulf of Mexico",
            "quest_connections": ["quest-002-oil-capital"],
            "dialogue_topics": ["oil-industry", "gulf-coast", "working-class"]
        },
        {
            "id": "houston-weather-survivor-2020",
            "name": "Tropical Storm Sally",
            "role": "Weather Survivor",
            "faction": "Civilians",
            "location": "Houston, Texas",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'8\"",
                "build": "fit",
                "hair": "long curly",
                "eyes": "hazel",
                "clothing_style": "practical outdoor wear",
                "distinctive_features": "sunny disposition despite weather"
            },
            "personality": "resilient, optimistic, weather-wise",
            "background": "Local who has survived multiple hurricanes",
            "quest_connections": ["quest-003-humidity-hell"],
            "dialogue_topics": ["weather-survival", "hurricane-stories", "climate-adaptation"]
        },
        {
            "id": "houston-rodeo-champion-2020",
            "name": "Buckaroo Bill",
            "role": "Rodeo Champion",
            "faction": "Entertainers",
            "location": "Houston, Texas",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'2\"",
                "build": "lean muscular",
                "hair": "long blonde",
                "eyes": "blue",
                "clothing_style": "western wear",
                "distinctive_features": "rodeo belt buckle, cowboy hat"
            },
            "personality": "confident, competitive, traditional",
            "background": "Professional rodeo rider with championship titles",
            "quest_connections": ["quest-004-rodeo-livestock-show"],
            "dialogue_topics": ["rodeo-life", "western-heritage", "competition"]
        },
        {
            "id": "houston-texmex-chef-2020",
            "name": "Rosa Maria Sanchez",
            "role": "Tex-Mex Fusion Chef",
            "faction": "Merchants",
            "location": "Houston, Texas",
            "appearance": {
                "age": 42,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'5\"",
                "build": "curvy",
                "hair": "dark brown",
                "eyes": "brown",
                "clothing_style": "chef apron",
                "distinctive_features": "warm smile, cooking passion"
            },
            "personality": "creative, hospitable, food enthusiast",
            "background": "Innovative chef blending Texan and Mexican cuisines",
            "quest_connections": ["quest-005-tex-mex-diversity"],
            "dialogue_topics": ["culinary-fusion", "tex-mex-culture", "food-innovation"]
        },
        {
            "id": "houston-astros-fan-2020",
            "name": "Orbit Ollie",
            "role": "Baseball Fanatic",
            "faction": "Civilians",
            "location": "Houston, Texas",
            "appearance": {
                "age": 32,
                "gender": "male",
                "ethnicity": "asian",
                "height": "5'9\"",
                "build": "average",
                "hair": "short black",
                "eyes": "brown",
                "clothing_style": "Astros jersey",
                "distinctive_features": "team spirit, enthusiastic"
            },
            "personality": "passionate, loyal, energetic",
            "background": "Dedicated Astros fan since childhood",
            "quest_connections": ["quest-006-astros-baseball"],
            "dialogue_topics": ["baseball", "astros-history", "sports-loyalty"]
        },
        {
            "id": "houston-hurricane-victim-2020",
            "name": "Captain Harvey",
            "role": "Storm Survivor",
            "faction": "Civilians",
            "location": "Houston, Texas",
            "appearance": {
                "age": 58,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "solid",
                "hair": "white",
                "eyes": "gray",
                "clothing_style": "practical work clothes",
                "distinctive_features": "weathered by storms, resilient gaze"
            },
            "personality": "stoic, experienced, community-minded",
            "background": "Survivor of Hurricane Harvey and other major storms",
            "quest_connections": ["quest-007-hurricane-harvey"],
            "dialogue_topics": ["storm-survival", "community-resilience", "climate-change"]
        },
        {
            "id": "houston-texans-cheerleader-2020",
            "name": "Lone Star Lucy",
            "role": "Football Cheerleader",
            "faction": "Entertainers",
            "location": "Houston, Texas",
            "appearance": {
                "age": 24,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "athletic",
                "hair": "blonde",
                "eyes": "blue",
                "clothing_style": "cheer uniform",
                "distinctive_features": "energetic, team spirit"
            },
            "personality": "cheerful, athletic, motivating",
            "background": "Professional cheerleader for the Houston Texans",
            "quest_connections": ["quest-008-texans-football"],
            "dialogue_topics": ["football", "cheerleading", "team-spirit"]
        },
        {
            "id": "houston-brunch-enthusiast-2020",
            "name": "Mimosa Mary",
            "role": "Brunch Culture Expert",
            "faction": "Civilians",
            "location": "Houston, Texas",
            "appearance": {
                "age": 30,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'7\"",
                "build": "slim",
                "hair": "strawberry blonde",
                "eyes": "green",
                "clothing_style": "stylish casual",
                "distinctive_features": "fashionable, brunch lover"
            },
            "personality": "social, foodie, trendsetter",
            "background": "Social media influencer focused on Houston brunch scene",
            "quest_connections": ["quest-009-tex-mex-breakfast"],
            "dialogue_topics": ["brunch-culture", "houston-foodie", "social-media"]
        },
        {
            "id": "houston-medical-innovator-2020",
            "name": "Dr. Future Health",
            "role": "Medical Center Director",
            "faction": "Corporations",
            "location": "Houston, Texas",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "indian",
                "height": "5'8\"",
                "build": "fit",
                "hair": "black with gray",
                "eyes": "dark brown",
                "clothing_style": "professional suit",
                "distinctive_features": "medical badge, authoritative presence"
            },
            "personality": "innovative, compassionate, visionary",
            "background": "Director of cutting-edge medical research at Texas Medical Center",
            "quest_connections": ["quest-010-medical-center"],
            "dialogue_topics": ["medical-innovation", "health-research", "texas-medicine"]
        }
    ]

    return houston_npcs

def generate_houston_dialogues(npcs):
    """Generate dialogue nodes for Houston NPCs"""

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
                    "body": f"Приветствую в Houston! Я {npc['name']}, {npc['role'].lower()}. Чем могу помочь?",
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
                        "mechanics_links": [f"knowledge/canon/lore/timeline-author/quests/america/houston/2020-2029/{quest_id}.yaml"],
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
                                'source_file': f"scripts/generate_houston_npcs_dialogues.py",
                                'city': 'Houston',
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
    print(f"Generated {len(changesets)} Houston NPCs")

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
                                'source_file': f"scripts/generate_houston_npcs_dialogues.py",
                                'city': 'Houston',
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
    print(f"Generated {len(changesets)} Houston dialogue nodes")

def main():
    """Main function"""
    print("Generating Houston NPCs and Dialogues...")

    # Generate NPCs
    houston_npcs = generate_houston_npcs()
    print(f"Generated {len(houston_npcs)} Houston NPCs")

    # Generate dialogues
    houston_dialogues = generate_houston_dialogues(houston_npcs)
    print(f"Generated {len(houston_dialogues)} Houston dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_houston-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_houston-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(houston_npcs, npcs_output)
    create_liquibase_dialogues(houston_dialogues, dialogues_output)

    print("Houston NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
