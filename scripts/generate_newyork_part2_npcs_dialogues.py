#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for New York Part 2 Quests
Creates supporting NPC characters and dialogue nodes for New York city quests Part 2.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_newyork_part2_npcs():
    """Generate NPC characters for New York Part 2 quests"""

    newyork_npcs = [
        {
            "id": "newyork-broadway-producer-2020",
            "name": "Broadway Bella",
            "role": "Theater Producer",
            "faction": "Civilians",
            "location": "New York City, New York",
            "appearance": {
                "age": 42,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "petite",
                "hair": "long red",
                "eyes": "green",
                "clothing_style": "theatrical chic",
                "distinctive_features": "Always has theater tickets"
            },
            "personality": "dramatic, passionate about theater",
            "background": "Broadway producer who knows all the best shows and behind-the-scenes secrets",
            "quest_connections": ["quest-006-broadway-show"],
            "dialogue_topics": ["broadway-shows", "theater-history", "nyc-entertainment"]
        },
        {
            "id": "newyork-mafia-consigliere-2020",
            "name": "Vito Corleone Jr.",
            "role": "Family Advisor",
            "faction": "Mafia",
            "location": "New York City, New York",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "italian american",
                "height": "5'10\"",
                "build": "stocky",
                "hair": "gray slicked back",
                "eyes": "dark brown",
                "clothing_style": "expensive suit",
                "distinctive_features": "Gold pinky ring, speaks with accent"
            },
            "personality": "wise, intimidating when needed",
            "background": "High-ranking member of New York mafia family with deep connections",
            "quest_connections": ["quest-007-mafia-family"],
            "dialogue_topics": ["mafia-history", "family-business", "nyc-underground"]
        },
        {
            "id": "newyork-central-park-guard-2020",
            "name": "Park Ranger Pete",
            "role": "Park Ranger",
            "faction": "Government",
            "location": "New York City, New York",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'1\"",
                "build": "athletic",
                "hair": "short brown",
                "eyes": "blue",
                "clothing_style": "park ranger uniform",
                "distinctive_features": "Binoculars, knows every path in Central Park"
            },
            "personality": "helpful, nature-loving",
            "background": "Central Park ranger who knows all the park's secrets and hidden spots",
            "quest_connections": ["quest-008-central-park-night"],
            "dialogue_topics": ["central-park", "urban-nature", "nyc-parks"]
        },
        {
            "id": "newyork-empire-state-worker-2020",
            "name": "Elevator Eddie",
            "role": "Maintenance Worker",
            "faction": "Civilians",
            "location": "New York City, New York",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "hispanic",
                "height": "5'8\"",
                "build": "average",
                "hair": "black graying",
                "eyes": "brown",
                "clothing_style": "work coveralls",
                "distinctive_features": "Tool belt, knows Empire State secrets"
            },
            "personality": "reliable, knows shortcuts",
            "background": "Long-time maintenance worker at Empire State Building with access to restricted areas",
            "quest_connections": ["quest-009-empire-state-climb"],
            "dialogue_topics": ["empire-state", "building-maintenance", "nyc-landmarks"]
        },
        {
            "id": "newyork-bridge-engineer-2020",
            "name": "Brooklyn Bridge Bob",
            "role": "Structural Engineer",
            "faction": "Government",
            "location": "New York City, New York",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "5'11\"",
                "build": "lean",
                "hair": "brown",
                "eyes": "brown",
                "clothing_style": "engineer casual",
                "distinctive_features": "Hard hat, bridge blueprints"
            },
            "personality": "technical, safety-conscious",
            "background": "Bridge engineer who knows every bolt and cable of the Brooklyn Bridge",
            "quest_connections": ["quest-010-brooklyn-bridge-battle"],
            "dialogue_topics": ["brooklyn-bridge", "engineering", "nyc-infrastructure"]
        },
        {
            "id": "newyork-cyberpunk-hacker-2020",
            "name": "Neon Nova",
            "role": "Cybersecurity Consultant",
            "faction": "Corporation",
            "location": "New York City, New York",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "asian",
                "height": "5'4\"",
                "build": "slight",
                "hair": "neon blue",
                "eyes": "brown",
                "clothing_style": "cyberpunk",
                "distinctive_features": "Neural implants visible, multiple devices"
            },
            "personality": "brilliant, socially awkward",
            "background": "Elite hacker who navigates New York's digital underworld",
            "quest_connections": ["quest-011-new-york-cyberpunk-hacker"],
            "dialogue_topics": ["cybersecurity", "hacking", "digital-underground"]
        },
        {
            "id": "newyork-street-artist-2020",
            "name": "Graffiti Gus",
            "role": "Street Artist",
            "faction": "Underground",
            "location": "New York City, New York",
            "appearance": {
                "age": 32,
                "gender": "male",
                "ethnicity": "mixed",
                "height": "5'9\"",
                "build": "athletic",
                "hair": "dreadlocks",
                "eyes": "brown",
                "clothing_style": "urban streetwear",
                "distinctive_features": "Paint-stained clothes, sketchbook"
            },
            "personality": "creative, rebellious",
            "background": "Underground street artist who knows all the best spots for murals in NYC",
            "quest_connections": ["quest-012-new-york-street-art-revolution"],
            "dialogue_topics": ["street-art", "urban-culture", "nyc-underground-art"]
        },
        {
            "id": "newyork-food-truck-owner-2020",
            "name": "Halal Hassan",
            "role": "Food Vendor",
            "faction": "Civilians",
            "location": "New York City, New York",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "middle eastern",
                "height": "5'10\"",
                "build": "average",
                "hair": "black",
                "eyes": "brown",
                "clothing_style": "chef apron",
                "distinctive_features": "Always has food samples, knows best food spots"
            },
            "personality": "friendly, food-obsessed",
            "background": "Food truck owner who knows the best food scenes across New York City",
            "quest_connections": ["quest-013-new-york-food-truck-empire"],
            "dialogue_topics": ["street-food", "nyc-cuisine", "food-culture"]
        },
        {
            "id": "newyork-times-square-tourist-2020",
            "name": "Tourist Tina",
            "role": "City Visitor",
            "faction": "Civilians",
            "location": "New York City, New York",
            "appearance": {
                "age": 25,
                "gender": "female",
                "ethnicity": "caucasian",
                "height": "5'5\"",
                "build": "petite",
                "hair": "blonde",
                "eyes": "blue",
                "clothing_style": "casual tourist",
                "distinctive_features": "Camera, map of NYC"
            },
            "personality": "enthusiastic, camera-ready",
            "background": "Tourist exploring New York City, always taking photos of landmarks",
            "quest_connections": ["quest-003-times-square-chase"],
            "dialogue_topics": ["tourist-attractions", "nyc-photography", "city-exploration"]
        },
        {
            "id": "newyork-subway-worker-2020",
            "name": "Transit Tim",
            "role": "Transit Authority Worker",
            "faction": "Government",
            "location": "New York City, New York",
            "appearance": {
                "age": 48,
                "gender": "male",
                "ethnicity": "caucasian",
                "height": "6'0\"",
                "build": "stocky",
                "hair": "gray",
                "eyes": "blue",
                "clothing_style": "transit uniform",
                "distinctive_features": "Transit badge, knows all subway shortcuts"
            },
            "personality": "experienced, helpful",
            "background": "Long-time subway worker who knows every tunnel and shortcut in the NYC transit system",
            "quest_connections": ["quest-005-subway-survival"],
            "dialogue_topics": ["nyc-subway", "transit-system", "urban-navigation"]
        }
    ]

    return newyork_npcs

def generate_newyork_part2_dialogues(npcs):
    """Generate dialogue nodes for New York Part 2 NPCs"""

    dialogues = []

    for npc in npcs:
        # Base dialogue node
        base_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc['id'],
            "type": "greeting",
            "text": f"Hello! I'm {npc['name']}, {npc['role'].lower()} here in New York City.",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about",
                    "conditions": []
                },
                {
                    "text": "What do you know about New York?",
                    "next_dialogue_id": f"{npc['id']}-newyork",
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
                    "text": "Interesting! Tell me more about New York City",
                    "next_dialogue_id": f"{npc['id']}-newyork",
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

        # New York-specific dialogue
        newyork_dialogue = {
            "id": f"{npc['id']}-newyork",
            "npc_id": npc['id'],
            "type": "information",
            "text": f"New York City is incredible! {npc['personality']}",
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

        dialogues.extend([base_dialogue, about_dialogue, newyork_dialogue])

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPC import"""

    changesets = []

    for npc in npcs:
        changeset_id = f"npcs-newyork-part2-{npc['id']}-{hashlib.md5(str(npc).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'newyork-part2-npcs-import',
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
                                'city': 'New York City, New York, USA',
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
    print(f"Generated {len(changesets)} New York Part 2 NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogue import"""

    changesets = []

    for dialogue in dialogues:
        changeset_id = f"dialogues-newyork-part2-{dialogue['id']}-{hashlib.md5(str(dialogue).encode()).hexdigest()[:8]}"

        changeset = {
            'id': changeset_id,
            'author': 'newyork-part2-dialogues-import',
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
    print(f"Generated {len(changesets)} New York Part 2 dialogue nodes")

def main():
    """Main function"""
    print("Generating New York Part 2 NPCs and Dialogues...")

    # Generate NPCs
    newyork_npcs = generate_newyork_part2_npcs()
    print(f"Generated {len(newyork_npcs)} New York Part 2 NPCs")

    # Generate dialogues
    newyork_dialogues = generate_newyork_part2_dialogues(newyork_npcs)
    print(f"Generated {len(newyork_dialogues)} New York Part 2 dialogue nodes")

    # Output files
    npcs_output = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_newyork_part2-2020-2029_support.yaml')
    dialogues_output = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_newyork_part2-2020-2029_support.yaml')

    # Create Liquibase files
    create_liquibase_npcs(newyork_npcs, npcs_output)
    create_liquibase_dialogues(newyork_dialogues, dialogues_output)

    print("New York Part 2 NPCs and dialogues generation completed successfully!")

if __name__ == '__main__':
    main()
