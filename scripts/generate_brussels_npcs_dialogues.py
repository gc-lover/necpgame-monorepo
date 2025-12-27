#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Brussels Quests
Creates supporting NPC characters and dialogue nodes for Brussels city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_brussels_npcs():
    """Generate NPC characters for Brussels quests"""

    brussels_npcs = [
        {
            "id": "brussels-tour-guide-2020",
            "name": "Brussels Belle",
            "role": "City Tour Guide",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 29,
                "gender": "female",
                "ethnicity": "belgian",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "blonde",
                "eyes": "blue",
                "clothing_style": "casual european",
                "distinctive_features": "Speaks multiple languages fluently"
            },
            "personality": "welcoming, multilingual",
            "background": "Local tour guide passionate about Brussels history and culture",
            "quest_connections": ["quest-001-eu-capital", "quest-008-grand-place"],
            "dialogue_topics": ["eu-history", "brussels-landmarks", "belgian-culture"]
        },
        {
            "id": "brussels-chocolate-master-2020",
            "name": "Chocolate Charles",
            "role": "Chocolate Artisan",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "belgian",
                "height": "5'10\"",
                "build": "stocky",
                "hair": "brown mustache",
                "eyes": "brown",
                "clothing_style": "chef apron",
                "distinctive_features": "Chocolate stained apron"
            },
            "personality": "passionate about craftsmanship",
            "background": "Master chocolatier who knows the secrets of Belgian chocolate",
            "quest_connections": ["quest-003-belgian-chocolate"],
            "dialogue_topics": ["chocolate-making", "belgian-traditions", "sweet-treats"]
        },
        {
            "id": "brussels-beer-expert-2020",
            "name": "Brewmaster Bruno",
            "role": "Brewing Historian",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "belgian",
                "height": "6'0\"",
                "build": "robust",
                "hair": "gray beard",
                "eyes": "blue",
                "clothing_style": "traditional brewer",
                "distinctive_features": "Beer tasting glass collection"
            },
            "personality": "knowledgeable, proud of heritage",
            "background": "Beer expert who knows all about Belgian brewing traditions",
            "quest_connections": ["quest-004-belgian-beer"],
            "dialogue_topics": ["beer-history", "brewing-traditions", "belgian-ales"]
        },
        {
            "id": "brussels-atomium-guide-2020",
            "name": "Atomium Anna",
            "role": "Science Museum Guide",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "belgian",
                "height": "5'6\"",
                "build": "petite",
                "hair": "red curly",
                "eyes": "green",
                "clothing_style": "museum guide uniform",
                "distinctive_features": "Science themed accessories"
            },
            "personality": "enthusiastic about science",
            "background": "Museum guide at the Atomium, passionate about science and architecture",
            "quest_connections": ["quest-005-atomium"],
            "dialogue_topics": ["science-history", "atomium-architecture", "1958-worlds-fair"]
        },
        {
            "id": "brussels-waffle-chef-2020",
            "name": "Waffle Willem",
            "role": "Street Food Vendor",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "belgian",
                "height": "5'8\"",
                "build": "average",
                "hair": "black",
                "eyes": "brown",
                "clothing_style": "street vendor",
                "distinctive_features": "Always has waffle samples"
            },
            "personality": "friendly, food-loving",
            "background": "Waffle vendor who knows the history of Belgian waffles",
            "quest_connections": ["quest-006-waffles"],
            "dialogue_topics": ["street-food", "waffle-traditions", "belgian-cuisine"]
        },
        {
            "id": "brussels-frites-expert-2020",
            "name": "Frites Francois",
            "role": "Food Historian",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 41,
                "gender": "male",
                "ethnicity": "belgian",
                "height": "5'9\"",
                "build": "average",
                "hair": "balding",
                "eyes": "brown",
                "clothing_style": "casual chef",
                "distinctive_features": "Expert on fried foods"
            },
            "personality": "scholarly, passionate about food history",
            "background": "Food historian specializing in the origins of Belgian fries",
            "quest_connections": ["quest-007-frites-origin"],
            "dialogue_topics": ["food-history", "fries-origin", "belgian-street-food"]
        },
        {
            "id": "brussels-comic-artist-2020",
            "name": "Comic Clara",
            "role": "Comic Book Artist",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 33,
                "gender": "female",
                "ethnicity": "belgian",
                "height": "5'5\"",
                "build": "slight",
                "hair": "black pixie",
                "eyes": "brown",
                "clothing_style": "artistic",
                "distinctive_features": "Comic book tattoos"
            },
            "personality": "creative, enthusiastic",
            "background": "Comic book artist who knows Brussels' comic strip history",
            "quest_connections": ["quest-010-comic-strip-capital"],
            "dialogue_topics": ["comic-culture", "brussels-comics", "belgian-art"]
        },
        {
            "id": "brussels-eu-official-2020",
            "name": "Euro Elise",
            "role": "EU Administrator",
            "faction": "Government",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 40,
                "gender": "female",
                "ethnicity": "mixed european",
                "height": "5'8\"",
                "build": "professional",
                "hair": "blonde",
                "eyes": "blue",
                "clothing_style": "business suit",
                "distinctive_features": "Multiple language badges"
            },
            "personality": "diplomatic, knowledgeable",
            "background": "EU official who works in Brussels institutions",
            "quest_connections": ["quest-001-eu-capital"],
            "dialogue_topics": ["eu-politics", "european-union", "brussels-institutions"]
        },
        {
            "id": "brussels-manneken-guide-2020",
            "name": "Peeing Boy Pierre",
            "role": "Local Historian",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 47,
                "gender": "male",
                "ethnicity": "belgian",
                "height": "5'11\"",
                "build": "average",
                "hair": "gray",
                "eyes": "brown",
                "clothing_style": "tour guide",
                "distinctive_features": "Knows all Manneken Pis stories"
            },
            "personality": "humorous, storytelling",
            "background": "Local historian who knows all the legends about Manneken Pis",
            "quest_connections": ["quest-002-manneken-pis"],
            "dialogue_topics": ["local-legends", "manneken-stories", "brussels-folklore"]
        },
        {
            "id": "brussels-language-expert-2020",
            "name": "Bilingual Beatrice",
            "role": "Language Teacher",
            "faction": "Civilians",
            "location": "Brussels, Belgium",
            "appearance": {
                "age": 36,
                "gender": "female",
                "ethnicity": "belgian",
                "height": 34,
                "build": "athletic",
                "hair": "brown",
                "eyes": "green",
                "clothing_style": "casual professional",
                "distinctive_features": "Speaks French and Dutch fluently"
            },
            "personality": "patient, culturally aware",
            "background": "Language teacher who understands Brussels' bilingual tensions",
            "quest_connections": ["quest-009-bilingualism-tension"],
            "dialogue_topics": ["language-culture", "french-dutch", "cultural-identity"]
        }
    ]

    return brussels_npcs

def generate_brussels_dialogues(npcs):
    """Generate dialogue nodes for Brussels NPCs"""

    dialogues = []

    for npc in npcs:
        # Base dialogue node
        base_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc['id'],
            "type": "greeting",
            "text": f"Hello! I'm {npc['name']}, {npc['role'].lower()} here in Brussels.",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about",
                    "conditions": []
                },
                {
                    "text": "What do you know about Brussels?",
                    "next_dialogue_id": f"{npc['id']}-brussels",
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
                    "text": "Interesting! Tell me more about Brussels",
                    "next_dialogue_id": f"{npc['id']}-brussels",
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

        # Brussels-specific dialogue
        brussels_dialogue = {
            "id": f"{npc['id']}-brussels",
            "npc_id": npc['id'],
            "type": "information",
            "text": f"Brussels is a wonderful city! {npc['personality']}",
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

        dialogues.extend([base_dialogue, about_dialogue, brussels_dialogue])

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPC import"""

    changesets = []

    for npc in npcs:
        changeset_id = f"npcs-brussels-{npc['id']}-{hashlib.md5(str(npc).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'brussels-npcs-import',
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
                                'city': 'Brussels, Belgium',
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
    print(f"Generated {len(changesets)} Brussels NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogue import"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-brussels-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'brussels-dialogues-import',
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
    print(f"Generated {len(changesets)} Brussels dialogue nodes")

def main():
    """Main function"""
    print("Generating Brussels NPCs and Dialogues...")

    # Generate NPCs
    brussels_npcs = generate_brussels_npcs()
    print(f"Generated {len(brussels_npcs)} Brussels NPCs")

    # Generate dialogues
    brussels_dialogues = generate_brussels_dialogues(brussels_npcs)
    print(f"Generated {len(brussels_dialogues)} Brussels dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_brussels-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_brussels-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(brussels_npcs, npcs_output)
    create_liquibase_dialogues(brussels_dialogues, dialogues_output)

    print("Brussels NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
