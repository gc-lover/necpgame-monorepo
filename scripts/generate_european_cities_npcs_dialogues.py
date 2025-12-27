#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for European Cities Quests
Creates supporting NPC characters and dialogue nodes for European cities quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_european_cities_npcs():
    """Generate NPC characters for European cities quests"""

    european_npcs = [
        # Amsterdam NPCs
        {
            "id": "amsterdam-tour-guide-2020",
            "name": "Canal Claire",
            "role": "City Tour Guide",
            "faction": "Civilians",
            "location": "Amsterdam, Netherlands",
            "appearance": {
                "age": 32,
                "gender": "female",
                "ethnicity": "dutch",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "blonde",
                "eyes": "blue",
                "clothing_style": "casual european",
                "distinctive_features": "Speaks multiple languages fluently"
            },
            "personality": "welcoming, knowledgeable about history",
            "background": "Local tour guide passionate about Amsterdam's canals and history",
            "quest_connections": ["quest-001-canal-cruise", "quest-002-anne-frank-house"],
            "dialogue_topics": ["canal-history", "dutch-culture", "amsterdam-landmarks"]
        },
        {
            "id": "amsterdam-coffee-shop-owner-2020",
            "name": "Green George",
            "role": "Coffee Shop Owner",
            "faction": "Civilians",
            "location": "Amsterdam, Netherlands",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "dutch",
                "height": "5'11\"",
                "build": "lean",
                "hair": "brown dreadlocks",
                "eyes": "brown",
                "clothing_style": "relaxed bohemian",
                "distinctive_features": "Peace symbol tattoo"
            },
            "personality": "laid-back, philosophical",
            "background": "Coffee shop owner with deep knowledge of Amsterdam's culture",
            "quest_connections": ["quest-001-coffee-shop-culture", "quest-005-coffeeshop-culture"],
            "dialogue_topics": ["coffee-culture", "dutch-tolerance", "amsterdam-underground"]
        },
        # Berlin NPCs
        {
            "id": "berlin-wall-guide-2020",
            "name": "Wall Wolfgang",
            "role": "Historical Guide",
            "faction": "Civilians",
            "location": "Berlin, Germany",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "german",
                "height": "6'0\"",
                "build": "fit",
                "hair": "graying",
                "eyes": "blue",
                "clothing_style": "practical outdoor",
                "distinctive_features": "Berlin Wall memorabilia collection"
            },
            "personality": "serious, passionate about history",
            "background": "Former East Berlin resident who witnessed the Wall fall",
            "quest_connections": ["quest-001-berlin-wall-memorial", "quest-004-berlin-wall-memorial"],
            "dialogue_topics": ["cold-war-history", "german-reunification", "berlin-wall"]
        },
        {
            "id": "berlin-techno-dj-2020",
            "name": "Techno Tina",
            "role": "Underground DJ",
            "faction": "Civilians",
            "location": "Berlin, Germany",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "mixed",
                "height": "5'6\"",
                "build": "petite",
                "hair": "neon pink",
                "eyes": "green",
                "clothing_style": "cyberpunk rave",
                "distinctive_features": "LED light tattoos"
            },
            "personality": "energetic, rebellious",
            "background": "Rising star in Berlin's legendary techno scene",
            "quest_connections": ["quest-002-berghain-techno", "quest-008-berghain-techno-legend"],
            "dialogue_topics": ["techno-culture", "berlin-clubs", "electronic-music"]
        },
        # Brussels NPCs (already exist, but including for completeness)
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
        # London NPCs
        {
            "id": "london-pub-owner-2020",
            "name": "Pub Pete",
            "role": "Traditional Pub Owner",
            "faction": "Civilians",
            "location": "London, UK",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "english",
                "height": "5'9\"",
                "build": "stocky",
                "hair": "balding gray",
                "eyes": "brown",
                "clothing_style": "traditional pub landlord",
                "distinctive_features": "Thick cockney accent"
            },
            "personality": "gruff but friendly",
            "background": "Fifth-generation pub owner who knows London's secrets",
            "quest_connections": ["quest-001-london-pub-culture"],
            "dialogue_topics": ["pub-culture", "london-history", "english-traditions"]
        },
        # Paris NPCs
        {
            "id": "paris-cafe-owner-2020",
            "name": "Cafe Claude",
            "role": "Bistro Owner",
            "faction": "Civilians",
            "location": "Paris, France",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "french",
                "height": "5'8\"",
                "build": "average",
                "hair": "black mustache",
                "eyes": "dark brown",
                "clothing_style": "classic french bistro",
                "distinctive_features": "Beret collection"
            },
            "personality": "romantic, cultured",
            "background": "Cafe owner who knows the soul of Paris",
            "quest_connections": ["quest-001-paris-cafe-culture"],
            "dialogue_topics": ["french-culture", "paris-life", "cafe-society"]
        },
        # Rome NPCs
        {
            "id": "rome-historian-2020",
            "name": "Colosseum Carlo",
            "role": "Ancient Rome Guide",
            "faction": "Civilians",
            "location": "Rome, Italy",
            "appearance": {
                "age": 50,
                "gender": "male",
                "ethnicity": "italian",
                "height": "5'10\"",
                "build": "fit",
                "hair": "salt and pepper",
                "eyes": "brown",
                "clothing_style": "casual professor",
                "distinctive_features": "Glasses and leather-bound notebook"
            },
            "personality": "enthusiastic, scholarly",
            "background": "Classical historian who brings ancient Rome to life",
            "quest_connections": ["quest-001-colosseum-tour"],
            "dialogue_topics": ["roman-empire", "ancient-history", "italian-culture"]
        },
        # Vienna NPCs
        {
            "id": "vienna-opera-singer-2020",
            "name": "Opera Olga",
            "role": "Classical Singer",
            "faction": "Civilians",
            "location": "Vienna, Austria",
            "appearance": {
                "age": 35,
                "gender": "female",
                "ethnicity": "austrian",
                "height": "5'8\"",
                "build": "slender",
                "hair": "dark elegant",
                "eyes": "green",
                "clothing_style": "elegant evening wear",
                "distinctive_features": "Perfect posture and graceful movements"
            },
            "personality": "refined, passionate about music",
            "background": "Rising opera star at the Vienna State Opera",
            "quest_connections": ["quest-001-vienna-opera"],
            "dialogue_topics": ["classical-music", "vienna-culture", "opera-traditions"]
        },
        # Prague NPCs
        {
            "id": "prague-beer-master-2020",
            "name": "Pilsner Pavel",
            "role": "Beer Master",
            "faction": "Civilians",
            "location": "Prague, Czech Republic",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "czech",
                "height": "6'1\"",
                "build": "robust",
                "hair": "blond beard",
                "eyes": "blue",
                "clothing_style": "traditional czech",
                "distinctive_features": "Beer tasting medal collection"
            },
            "personality": "proud of heritage",
            "background": "Master brewer who knows Prague's beer traditions",
            "quest_connections": ["quest-001-prague-beer-culture"],
            "dialogue_topics": ["beer-culture", "czech-traditions", "prague-history"]
        }
    ]

    return european_npcs

def generate_european_cities_dialogues(npcs):
    """Generate dialogue nodes for European cities NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hello! I'm {npc['name']}. How can I help you today?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What do you know about this place?",
                    "next_dialogue_id": f"{npc['id']}-location-info"
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
            "text": f"I'm {npc['role']} here in {npc['location'].split(',')[0]}. {npc['background']}",
            "responses": [
                {
                    "text": "What do you know about this area?",
                    "next_dialogue_id": f"{npc['id']}-location-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(about_me_dialogue)

        # Generate location-info dialogue
        location_info_dialogue = {
            "id": f"{npc['id']}-location-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": f"{npc['location'].split(',')[0]} has such rich history and culture. There are so many interesting places to explore!",
            "responses": [
                {
                    "text": "Tell me more about the local culture",
                    "next_dialogue_id": f"{npc['id']}-culture-info"
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(location_info_dialogue)

        # Generate culture-info dialogue
        culture_topics = npc["dialogue_topics"]
        culture_text = f"The culture here is fascinating! We're known for our {', '.join(culture_topics[:2])}."

        culture_dialogue = {
            "id": f"{npc['id']}-culture-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": culture_text,
            "responses": [
                {
                    "text": "Thank you for the information",
                    "next_dialogue_id": None,
                    "ends_conversation": True
                },
                {
                    "text": "Back to start",
                    "next_dialogue_id": f"{npc['id']}-greeting"
                }
            ]
        }
        dialogues.append(culture_dialogue)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create Liquibase YAML file for NPCs"""

    changeset_id = f"npcs-european-cities-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'european-cities-npcs-import',
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
    print(f"Generated {len(npcs)} European cities NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-european-cities-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'european-cities-dialogues-import',
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
    print(f"Generated {len(dialogues)} European cities dialogue nodes")

def main():
    """Main function to generate European cities NPCs and dialogues"""

    print("Generating European cities NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_european_cities_npcs()
    print(f"Generated {len(npcs)} European cities NPCs")

    # Generate dialogues
    dialogues = generate_european_cities_dialogues(npcs)
    print(f"Generated {len(dialogues)} European cities dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_european_cities_2020_2093_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_european_cities_2020_2093_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("European cities NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
