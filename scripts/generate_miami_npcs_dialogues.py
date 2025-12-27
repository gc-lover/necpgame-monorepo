#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Miami Quests
Creates supporting NPC characters and dialogue nodes for Miami city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_miami_npcs():
    """Generate NPC characters for Houston quests"""

    miami_npcs = [
        {
            "id": "miami-beach-lifeguard-2020",
            "name": "Carlos \"Wave\" Martinez",
            "role": "Beach Lifeguard",
            "faction": "Civilians",
            "location": "Miami, Florida",
            "appearance": {
                "age": 28,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "6'1\"",
                "build": "muscular",
                "hair": "short brown",
                "eyes": "brown",
                "clothing_style": "red lifeguard trunks",
                "distinctive_features": "sunglasses, whistle, athletic build"
            },
            "personality": "responsible, outgoing, protective",
            "background": "Veteran lifeguard who knows every inch of South Beach",
            "quest_connections": ["quest-001-south-beach-neon"],
            "dialogue_topics": ["beach-safety", "ocean-conditions", "local-beaches"]
        },
        {
            "id": "miami-cuban-exile-2020",
            "name": "Rosa \"La Reina\" Delgado",
            "role": "Cuban Exile Community Leader",
            "faction": "Civilians",
            "location": "Miami, Florida",
            "appearance": {
                "age": 65,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'4\"",
                "build": "dignified",
                "hair": "gray with hints of black",
                "eyes": "dark brown",
                "clothing_style": "traditional Cuban dress",
                "distinctive_features": "gold cross necklace, strong presence"
            },
            "personality": "wise, passionate, community-oriented",
            "background": "Leader of the Cuban exile community in Little Havana",
            "quest_connections": ["quest-002-cuban-little-havana"],
            "dialogue_topics": ["cuban-history", "exile-stories", "cultural-preservation"]
        },
        {
            "id": "miami-drug-lord-2020",
            "name": "El Diablo Blanco",
            "role": "Underground Drug Dealer",
            "faction": "Criminal",
            "location": "Miami, Florida",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "6'0\"",
                "build": "intimidating",
                "hair": "shaved head",
                "eyes": "cold blue",
                "clothing_style": "expensive suit",
                "distinctive_features": "gold chains, dangerous aura"
            },
            "personality": "ruthless, calculating, charming",
            "background": "Kingpin of Miami's drug trade network",
            "quest_connections": ["quest-003-drug-run"],
            "dialogue_topics": ["underground-economy", "cartel-politics", "survival-skills"]
        },
        {
            "id": "miami-street-artist-2020",
            "name": "Color Bomb",
            "role": "Street Artist",
            "faction": "Artists",
            "location": "Miami, Florida",
            "appearance": {
                "age": 26,
                "gender": "non-binary",
                "ethnicity": "mixed",
                "height": "5'8\"",
                "build": "slender",
                "hair": "colorful mohawk",
                "eyes": "bright green",
                "clothing_style": "artistic street wear",
                "distinctive_features": "paint-stained hands, creative energy"
            },
            "personality": "creative, rebellious, expressive",
            "background": "Rising star of Wynwood Walls street art scene",
            "quest_connections": ["quest-004-wynwood-walls"],
            "dialogue_topics": ["street-art", "urban-expression", "art-movements"]
        },
        {
            "id": "miami-heat-fan-2020",
            "name": "South Beach Slammer",
            "role": "Basketball Fanatic",
            "faction": "Civilians",
            "location": "Miami, Florida",
            "appearance": {
                "age": 35,
                "gender": "male",
                "ethnicity": "african-american",
                "height": "6'3\"",
                "build": "athletic",
                "hair": "short dreads",
                "eyes": "brown",
                "clothing_style": "Miami Heat jersey",
                "distinctive_features": "basketball tattoo, energetic"
            },
            "personality": "passionate, loyal, competitive",
            "background": "Die-hard Miami Heat fan and former college player",
            "quest_connections": ["quest-005-miami-heat-game"],
            "dialogue_topics": ["basketball", "heat-legends", "sports-culture"]
        },
        {
            "id": "miami-everglades-guide-2020",
            "name": "Swamp Spirit",
            "role": "Everglades Guide",
            "faction": "Nature",
            "location": "Miami, Florida",
            "appearance": {
                "age": 45,
                "gender": "female",
                "ethnicity": "native american",
                "height": "5'7\"",
                "build": "fit",
                "hair": "long black braid",
                "eyes": "dark brown",
                "clothing_style": "practical outdoor gear",
                "distinctive_features": "nature wisdom, calming presence"
            },
            "personality": "wise, connected to nature, protective",
            "background": "Native guide with deep knowledge of Everglades ecosystem",
            "quest_connections": ["quest-006-everglades-gators"],
            "dialogue_topics": ["everglades-wildlife", "native-traditions", "environmental-protection"]
        },
        {
            "id": "miami-cuban-chef-2020",
            "name": "Papa Joe's Daughter",
            "role": "Cuban Sandwich Master",
            "faction": "Merchants",
            "location": "Miami, Florida",
            "appearance": {
                "age": 32,
                "gender": "female",
                "ethnicity": "hispanic",
                "height": "5'6\"",
                "build": "curvy",
                "hair": "dark curly",
                "eyes": "brown",
                "clothing_style": "chef uniform",
                "distinctive_features": "family pride, culinary passion"
            },
            "personality": "proud, skilled, family-oriented",
            "background": "Third-generation owner of legendary Cuban sandwich shop",
            "quest_connections": ["quest-007-cuban-sandwich"],
            "dialogue_topics": ["cuban-cuisine", "family-recipes", "cultural-heritage"]
        },
        {
            "id": "miami-speedboat-captain-2020",
            "name": "Thunder Wave",
            "role": "Speedboat Racer",
            "faction": "Racers",
            "location": "Miami, Florida",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "muscular",
                "hair": "wind-blown blonde",
                "eyes": "blue",
                "clothing_style": "racing gear",
                "distinctive_features": "speed tattoos, adventurous spirit"
            },
            "personality": "adventurous, competitive, thrill-seeking",
            "background": "Professional speedboat racer and ocean adventurer",
            "quest_connections": ["quest-008-speedboat-race"],
            "dialogue_topics": ["speedboat-racing", "ocean-adventures", "thrill-sports"]
        },
        {
            "id": "miami-art-collector-2020",
            "name": "Gallery Ghost",
            "role": "Art Basel Insider",
            "faction": "Elites",
            "location": "Miami, Florida",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'10\"",
                "build": "distinguished",
                "hair": "silver",
                "eyes": "piercing blue",
                "clothing_style": "expensive suit",
                "distinctive_features": "artistic air, sophisticated manner"
            },
            "personality": "cultured, mysterious, influential",
            "background": "Elite art collector with connections to Art Basel",
            "quest_connections": ["quest-009-art-basel"],
            "dialogue_topics": ["contemporary-art", "art-market", "cultural-events"]
        },
        {
            "id": "miami-hurricane-survivor-2020",
            "name": "Storm Watcher",
            "role": "Weather Researcher",
            "faction": "Scientists",
            "location": "Miami, Florida",
            "appearance": {
                "age": 48,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'9\"",
                "build": "solid",
                "hair": "short practical",
                "eyes": "determined brown",
                "clothing_style": "weather gear",
                "distinctive_features": "weather instruments, survival gear"
            },
            "personality": "scientific, prepared, community-focused",
            "background": "Meteorologist who studies and predicts hurricanes",
            "quest_connections": ["quest-010-hurricane-survival"],
            "dialogue_topics": ["hurricane-science", "weather-prediction", "disaster-preparedness"]
        }
    ]

    return miami_npcs

def generate_miami_dialogues(npcs):
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
                    "body": f"Приветствую в Miami! Я {npc['name']}, {npc['role'].lower()}. Чем могу помочь?",
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
                        "mechanics_links": [f"knowledge/canon/lore/timeline-author/quests/america/miami/2020-2029/{quest_id}.yaml"],
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
                                'city': 'Miami',
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
                                'city': 'Miami',
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
    miami_npcs = generate_miami_npcs()
    print(f"Generated {len(miami_npcs)} Miami NPCs")

    # Generate dialogues
    miami_dialogues = generate_miami_dialogues(miami_npcs)
    print(f"Generated {len(miami_dialogues)} Miami dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_miami-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_miami-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(miami_npcs, npcs_output)
    create_liquibase_dialogues(miami_dialogues, dialogues_output)

    print("Miami NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
