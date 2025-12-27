#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Asian and Middle Eastern Cities Quests
Creates supporting NPC characters and dialogue nodes for Asian and Middle Eastern cities quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_asian_middle_eastern_npcs():
    """Generate NPC characters for Asian and Middle Eastern cities quests"""

    asian_middle_eastern_npcs = [
        # Asian NPCs
        {
            "id": "tokyo-yakuza-boss-2020",
            "name": "Takeshi Tanaka",
            "role": "Yakuza Oyabun",
            "faction": "Yakuza",
            "location": "Tokyo, Japan",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "japanese",
                "height": "5'9\"",
                "build": "stocky",
                "hair": "gray traditional",
                "eyes": "dark brown",
                "clothing_style": "traditional yakuza suit",
                "distinctive_features": "Full body tattoos, missing pinky finger"
            },
            "personality": "stoic, honorable, ruthless",
            "background": "Powerful yakuza boss controlling Tokyo's underworld",
            "quest_connections": ["quest-002-yakuza-initiation", "quest-002-yakuza-corporate-initiation"],
            "dialogue_topics": ["yakuza-code", "tokyo-underworld", "corporate-corruption"]
        },
        {
            "id": "tokyo-otaku-merchant-2020",
            "name": "Hiroki Otaku",
            "role": "Anime Merchandise Dealer",
            "faction": "Civilians",
            "location": "Akihabara, Tokyo, Japan",
            "appearance": {
                "age": 28,
                "gender": "male",
                "ethnicity": "japanese",
                "height": "5'6\"",
                "build": "slender",
                "hair": "neon blue",
                "eyes": "brown behind glasses",
                "clothing_style": "anime cosplay",
                "distinctive_features": "Carries anime figurines everywhere"
            },
            "personality": "enthusiastic, knowledgeable about otaku culture",
            "background": "Anime expert and merchant in Akihabara district",
            "quest_connections": ["quest-003-akihabara-otaku", "quest-003-akihabara-otaku-network"],
            "dialogue_topics": ["anime-culture", "otaku-lifestyle", "japanese-pop-culture"]
        },
        {
            "id": "seoul-kpop-manager-2020",
            "name": "Ji-yeon Kim",
            "role": "K-Pop Talent Manager",
            "faction": "Entertainment Corp",
            "location": "Gangnam, Seoul, South Korea",
            "appearance": {
                "age": 32,
                "gender": "female",
                "ethnicity": "korean",
                "height": "5'5\"",
                "build": "petite",
                "hair": "perfectly styled black",
                "eyes": "almond-shaped brown",
                "clothing_style": "stylish corporate casual",
                "distinctive_features": "Always on phone, stylish accessories"
            },
            "personality": "ambitious, demanding, trend-conscious",
            "background": "High-powered K-pop manager working for major entertainment company",
            "quest_connections": ["quest-001-gangnam-style", "quest-003-k-pop-idol"],
            "dialogue_topics": ["kpop-industry", "entertainment-business", "seoul-nightlife"]
        },
        {
            "id": "shanghai-businessman-2020",
            "name": "Li Wei",
            "role": "Tech Entrepreneur",
            "faction": "Corporate",
            "location": "Pudong, Shanghai, China",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "chinese",
                "height": "5'10\"",
                "build": "fit",
                "hair": "black business cut",
                "eyes": "dark brown",
                "clothing_style": "expensive business suit",
                "distinctive_features": "Smart glasses, luxury watch"
            },
            "personality": "driven, tech-savvy, ambitious",
            "background": "Successful tech entrepreneur in Shanghai's booming startup scene",
            "quest_connections": ["quest-002-shanghai-tower", "quest-007-pudong-skyline"],
            "dialogue_topics": ["chinese-tech", "business-opportunities", "modern-china"]
        },
        {
            "id": "singapore-hawker-2020",
            "name": "Ahmad Hassan",
            "role": "Hawker Center Owner",
            "faction": "Civilians",
            "location": "Chinatown, Singapore",
            "appearance": {
                "age": 58,
                "gender": "male",
                "ethnicity": "malay",
                "height": "5'7\"",
                "build": "solid",
                "hair": "graying",
                "eyes": "warm brown",
                "clothing_style": "hawker apron",
                "distinctive_features": "Always cooking, friendly smile"
            },
            "personality": "warm, hospitable, proud of heritage",
            "background": "Traditional hawker who knows all about Singapore's food culture",
            "quest_connections": ["quest-004-hawker-centres", "quest-005-little-india-chinatown"],
            "dialogue_topics": ["singapore-food", "multiculturalism", "street-food"]
        },
        {
            "id": "hong-kong-triad-member-2020",
            "name": "Johnny Wong",
            "role": "Triad Enforcer",
            "faction": "Triads",
            "location": "Mong Kok, Hong Kong",
            "appearance": {
                "age": 35,
                "gender": "male",
                "ethnicity": "chinese",
                "height": "6'0\"",
                "build": "muscular",
                "hair": "short black",
                "eyes": "intense dark",
                "clothing_style": "street tough",
                "distinctive_features": "Dragon tattoo on neck, gold chains"
            },
            "personality": "intimidating, loyal, street-smart",
            "background": "Low-level triad member navigating Hong Kong's underworld",
            "quest_connections": ["quest-005-triad-initiation", "quest-008-mong-kok-density"],
            "dialogue_topics": ["hong-kong-underworld", "triad-life", "street-survival"]
        },
        {
            "id": "delhi-street-food-vendor-2020",
            "name": "Rajesh Kumar",
            "role": "Street Food Vendor",
            "faction": "Civilians",
            "location": "Old Delhi, India",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "indian",
                "height": "5'8\"",
                "build": "average",
                "hair": "black mustache",
                "eyes": "dark brown",
                "clothing_style": "traditional kurta",
                "distinctive_features": "Always cooking over open flame"
            },
            "personality": "friendly, passionate about food",
            "background": "Street food vendor who knows Delhi's culinary secrets",
            "quest_connections": ["quest-003-street-food-chaos", "quest-007-sacred-cows"],
            "dialogue_topics": ["indian-street-food", "delhi-culture", "bollywood"]
        },
        # Middle Eastern NPCs
        {
            "id": "moscow-data-smuggler-2020",
            "name": "Ivan Petrov",
            "role": "Data Smuggler",
            "faction": "Underground",
            "location": "Moscow, Russia",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "russian",
                "height": "6'1\"",
                "build": "lean",
                "hair": "short brown",
                "eyes": "blue",
                "clothing_style": "nondescript jacket",
                "distinctive_features": "Multiple data chips, nervous twitch"
            },
            "personality": "paranoid, resourceful, cynical",
            "background": "Data smuggler operating in Moscow's black market",
            "quest_connections": ["quest-006-data-smugglers", "quest-016-black-market-raid"],
            "dialogue_topics": ["russian-underground", "data-trafficking", "corporate-espionage"]
        },
        {
            "id": "moscow-corporate-spy-2020",
            "name": "Elena Volkov",
            "role": "Corporate Intelligence Agent",
            "faction": "Corporation",
            "location": "Moscow, Russia",
            "appearance": {
                "age": 31,
                "gender": "female",
                "ethnicity": "russian",
                "height": "5'7\"",
                "build": "athletic",
                "hair": "long blonde",
                "eyes": "green",
                "clothing_style": "business professional",
                "distinctive_features": "Sharp features, always observing"
            },
            "personality": "calculating, ambitious, mysterious",
            "background": "Corporate spy working for major Russian conglomerate",
            "quest_connections": ["quest-018-corpo-espionage", "quest-008-corporate-romance"],
            "dialogue_topics": ["corporate-intrigue", "russian-business", "espionage"]
        },
        {
            "id": "almaty-nomad-guide-2020",
            "name": "Aibek Kazakh",
            "role": "Steppe Nomad Guide",
            "faction": "Nomads",
            "location": "Almaty, Kazakhstan",
            "appearance": {
                "age": 40,
                "gender": "male",
                "ethnicity": "kazakh",
                "height": "5'11\"",
                "build": "strong",
                "hair": "black with gray",
                "eyes": "dark brown",
                "clothing_style": "traditional kazakh robe",
                "distinctive_features": "Eagle hunting glove, weathered face"
            },
            "personality": "wise, connected to nature, proud",
            "background": "Traditional Kazakh nomad who guides people through the steppe",
            "quest_connections": ["quest-004-nomad-culture", "quest-009-steppe-eagle-hunt"],
            "dialogue_topics": ["kazakh-culture", "nomadic-life", "eagle-hunting"]
        },
        {
            "id": "baku-oil-tycoon-2020",
            "name": "Rashid Aliyev",
            "role": "Oil Industry Executive",
            "faction": "Corporate",
            "location": "Baku, Azerbaijan",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "azerbaijani",
                "height": "5'10\"",
                "build": "portly",
                "hair": "gray",
                "eyes": "dark brown",
                "clothing_style": "expensive suit",
                "distinctive_features": "Gold rings, confident demeanor"
            },
            "personality": "charismatic, wealthy, influential",
            "background": "Oil tycoon who controls much of Azerbaijan's energy sector",
            "quest_connections": ["quest-005-oil-boom", "quest-006-formula-1-baku"],
            "dialogue_topics": ["oil-industry", "azerbaijani-culture", "wealth-power"]
        },
        {
            "id": "tashkent-plov-master-2020",
            "name": "Dilshod Karimov",
            "role": "Plov Master Chef",
            "faction": "Civilians",
            "location": "Tashkent, Uzbekistan",
            "appearance": {
                "age": 48,
                "gender": "male",
                "ethnicity": "uzbek",
                "height": "5'9\"",
                "build": "solid",
                "hair": "black mustache",
                "eyes": "warm brown",
                "clothing_style": "traditional uzbek",
                "distinctive_features": "Always cooking, apron stained with spices"
            },
            "personality": "passionate, generous, traditional",
            "background": "Master chef specializing in traditional Uzbek plov",
            "quest_connections": ["quest-002-plov-center", "quest-006-samsa-quest"],
            "dialogue_topics": ["uzbek-cuisine", "central-asian-culture", "traditional-cooking"]
        },
        {
            "id": "tbilisi-wine-maker-2020",
            "name": "Giorgi Abashidze",
            "role": "Wine Master",
            "faction": "Civilians",
            "location": "Tbilisi, Georgia",
            "appearance": {
                "age": 45,
                "gender": "male",
                "ethnicity": "georgian",
                "height": "6'0\"",
                "build": "robust",
                "hair": "dark curly",
                "eyes": "brown",
                "clothing_style": "casual local",
                "distinctive_features": "Wine stains on clothes, warm smile"
            },
            "personality": "friendly, knowledgeable, proud of heritage",
            "background": "Wine maker who knows Georgia's ancient wine-making traditions",
            "quest_connections": ["quest-003-wine-cradle", "quest-004-supra-feast"],
            "dialogue_topics": ["georgian-wine", "caucasian-culture", "feasting-traditions"]
        }
    ]

    return asian_middle_eastern_npcs

def generate_asian_middle_eastern_dialogues(npcs):
    """Generate dialogue nodes for Asian and Middle Eastern cities NPCs"""

    dialogues = []

    for npc in npcs:
        # Generate greeting dialogue
        greeting_dialogue = {
            "id": f"{npc['id']}-greeting",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "greeting",
            "text": f"Hello! I'm {npc['name']}. What brings you to {npc['location'].split(',')[0]}?",
            "responses": [
                {
                    "text": "Tell me about yourself",
                    "next_dialogue_id": f"{npc['id']}-about-me"
                },
                {
                    "text": "What do you know about this area?",
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
                    "text": "What do you know about this city?",
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
        location_topics = npc["dialogue_topics"]
        location_text = f"{npc['location'].split(',')[0]} is incredible! We're known for our {', '.join(location_topics[:2])} culture."

        location_dialogue = {
            "id": f"{npc['id']}-location-info",
            "npc_id": npc["id"],
            "quest_id": npc["quest_connections"][0] if npc["quest_connections"] else None,
            "dialogue_type": "information",
            "text": location_text,
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
        dialogues.append(location_dialogue)

        # Generate culture-info dialogue
        culture_topics = npc["dialogue_topics"]
        culture_text = f"Our culture is rich and diverse! You should experience our {', '.join(culture_topics[:2])} firsthand."

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

    changeset_id = f"npcs-asian-middle-eastern-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'asian-middle-eastern-cities-npcs-import',
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
    print(f"Generated {len(npcs)} Asian and Middle Eastern cities NPC definitions")

def create_liquibase_dialogues(dialogues, output_file):
    """Create Liquibase YAML file for dialogues"""

    changeset_id = f"dialogues-asian-middle-eastern-{uuid.uuid4().hex[:8]}"
    now = datetime.now().isoformat()

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'asian-middle-eastern-cities-dialogues-import',
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
    print(f"Generated {len(dialogues)} Asian and Middle Eastern cities dialogue nodes")

def main():
    """Main function to generate Asian and Middle Eastern cities NPCs and dialogues"""

    print("Generating Asian and Middle Eastern cities NPCs and Dialogues...")

    # Generate NPCs
    npcs = generate_asian_middle_eastern_npcs()
    print(f"Generated {len(npcs)} Asian and Middle Eastern cities NPCs")

    # Generate dialogues
    dialogues = generate_asian_middle_eastern_dialogues(npcs)
    print(f"Generated {len(dialogues)} Asian and Middle Eastern cities dialogue nodes")

    # Create output directories if they don't exist
    Path('infrastructure/liquibase/data/narrative/npcs').mkdir(parents=True, exist_ok=True)
    Path('infrastructure/liquibase/data/narrative/dialogues').mkdir(parents=True, exist_ok=True)

    # Create Liquibase files
    npcs_file = Path('infrastructure/liquibase/data/narrative/npcs/data_npcs_asian_middle_eastern_cities_2020_2093_support.yaml')
    dialogues_file = Path('infrastructure/liquibase/data/narrative/dialogues/data_dialogues_asian_middle_eastern_cities_2020_2093_support.yaml')

    create_liquibase_npcs(npcs, npcs_file)
    create_liquibase_dialogues(dialogues, dialogues_file)

    print("Asian and Middle Eastern cities NPCs and dialogues generation completed successfully!")

if __name__ == "__main__":
    main()
