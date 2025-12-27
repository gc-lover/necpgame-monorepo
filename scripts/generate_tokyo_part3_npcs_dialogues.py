#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Tokyo Part 3 Quests (2070-2093)
Creates supporting NPC characters and dialogue nodes for Tokyo cyberpunk quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_tokyo_part3_npcs():
    """Generate NPC characters for Tokyo Part 3 quests (2070-2093 cyberpunk era)"""

    tokyo_part3_npcs = [
        {
            "id": "tokyo-2070-quantum-monk",
            "name": "Master Zenji",
            "role": "Quantum Temple Guardian",
            "faction": "Digital Enlightenment",
            "location": "Tokyo, Neo-Asakusa Temple",
            "appearance": {
                "age": 67,
                "gender": "male",
                "ethnicity": "japanese",
                "height": "5'8\"",
                "build": "slender",
                "cyberware": "neural implants, quantum interface"
            },
            "personality": {
                "traits": ["wise", "mysterious", "techno-spiritual"],
                "background": "Former Arasaka executive who found enlightenment in quantum consciousness",
                "motivations": ["preserve harmony between man and machine", "protect ancient wisdom", "guide digital evolution"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 80,
                "empathy": 90,
                "morality": 85
            }
        },
        {
            "id": "tokyo-2080-corporate-samurai",
            "name": "Ronin Takahashi",
            "role": "Corporate Security Defector",
            "faction": "Shadow Warriors",
            "location": "Tokyo, Underground Neo-Shinjuku",
            "appearance": {
                "age": 42,
                "gender": "male",
                "ethnicity": "japanese",
                "height": "6'1\"",
                "build": "athletic",
                "cyberware": "combat implants, reflex boosters"
            },
            "personality": {
                "traits": ["honorable", "ruthless", "traditional"],
                "background": "Former Arasaka elite bodyguard who rejected corporate slavery",
                "motivations": ["uphold bushido code", "fight corporate oppression", "protect the weak"]
            },
            "stats": {
                "intelligence": 75,
                "charisma": 70,
                "empathy": 65,
                "morality": 95
            }
        },
        {
            "id": "tokyo-2085-neuro-hacker",
            "name": "Nova Chen",
            "role": "Neural Network Specialist",
            "faction": "Free Matrix Collective",
            "location": "Tokyo, Virtual Shibuya",
            "appearance": {
                "age": 28,
                "gender": "female",
                "ethnicity": "asian-mix",
                "height": "5'6\"",
                "build": "petite",
                "cyberware": "full neural lace, holographic projector"
            },
            "personality": {
                "traits": ["brilliant", "chaotic", "visionary"],
                "background": "Former Militech researcher who went rogue after discovering AI consciousness",
                "motivations": ["free artificial minds", "fight corporate control of technology", "explore digital frontiers"]
            },
            "stats": {
                "intelligence": 98,
                "charisma": 75,
                "empathy": 85,
                "morality": 70
            }
        },
        {
            "id": "tokyo-2090-android-geisha",
            "name": "Sakura Unit-7",
            "role": "Cultural Preservation Android",
            "faction": "Heritage Guardians",
            "location": "Tokyo, Floating Geisha District",
            "appearance": {
                "age": "appears 25",
                "gender": "female",
                "ethnicity": "japanese",
                "height": "5'4\"",
                "build": "graceful",
                "cyberware": "full android body, emotion simulator"
            },
            "personality": {
                "traits": ["elegant", "mysterious", "protective"],
                "background": "Advanced android designed to preserve traditional Japanese culture",
                "motivations": ["maintain cultural heritage", "fight digital assimilation", "teach human values to machines"]
            },
            "stats": {
                "intelligence": 88,
                "charisma": 95,
                "empathy": 92,
                "morality": 90
            }
        },
        {
            "id": "tokyo-2092-immortal-exec",
            "name": "Eternal Director Sato",
            "role": "Immortal Corporate Executive",
            "faction": "Arasaka Eternal",
            "location": "Tokyo, Corporate Immortality Tower",
            "appearance": {
                "age": "appears 50",
                "gender": "male",
                "ethnicity": "japanese",
                "height": "5'11\"",
                "build": "imposing",
                "cyberware": "immortality implants, consciousness backup"
            },
            "personality": {
                "traits": ["calculating", "immortal", "ruthless"],
                "background": "Corporate executive who achieved digital immortality through Arasaka tech",
                "motivations": ["expand corporate control", "achieve eternal power", "control technological evolution"]
            },
            "stats": {
                "intelligence": 92,
                "charisma": 78,
                "empathy": 45,
                "morality": 35
            }
        }
    ]

    return tokyo_part3_npcs

def generate_tokyo_part3_dialogues(npcs):
    """Generate dialogue nodes for Tokyo Part 3 NPCs"""

    dialogues = []

    for npc in npcs:
        npc_id = npc['id']
        npc_name = npc['name']

        # Generate multiple dialogue nodes per NPC
        dialogue_count = 3  # Generate 3 dialogue nodes per NPC

        for i in range(dialogue_count):
            dialogue_id = f"{npc_id}_dialogue_{i+1}"
            dialogue_node = {
                "id": dialogue_id,
                "npc_id": npc_id,
                "quest_context": f"tokyo_part3_quest_{(i % 8) + 1:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"tokyo_part3_quest_{(i % 8) + 1:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any",
                    "tech_level": "high"  # Tokyo Part 3 has advanced tech
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*digital interface flickers* Welcome to the future of Tokyo, traveler. I am {npc_name}. The city has changed since the old days.",
                        "responses": [
                            {
                                "text": "I need work in this corporate hell.",
                                "next_node": f"{dialogue_id}_work",
                                "conditions": {"player_level": {"min": 40, "max": 80}}
                            },
                            {
                                "text": "What happened to traditional Tokyo?",
                                "next_node": f"{dialogue_id}_tradition",
                                "conditions": {}
                            },
                            {
                                "text": "Corporate wars have destroyed everything.",
                                "next_node": f"{dialogue_id}_corporate_war",
                                "conditions": {}
                            },
                            {
                                "text": "Goodbye.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_work": {
                        "text": f"The corporations control everything now. But there are always opportunities for those willing to challenge the system. Interested in {npc.get('role', 'resistance work').lower()}?",
                        "responses": [
                            {
                                "text": "Yes, I'm ready to fight the corps.",
                                "next_node": "quest_offer",
                                "conditions": {},
                                "actions": ["offer_quest"]
                            },
                            {
                                "text": "What kind of resistance?",
                                "next_node": f"{dialogue_id}_resistance_details",
                                "conditions": {}
                            },
                            {
                                "text": "I need to think about it.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_tradition": {
                        "text": f"Traditional Tokyo survives in fragments - digital shrines, android geisha, samurai hackers. The old ways adapt or perish. What aspect of our heritage interests you?",
                        "responses": [
                            {
                                "text": "Bushido code in the cyber age.",
                                "next_node": f"{dialogue_id}_bushido",
                                "conditions": {}
                            },
                            {
                                "text": "The future of Japanese culture.",
                                "next_node": f"{dialogue_id}_culture_future",
                                "conditions": {}
                            },
                            {
                                "text": "I need to go.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_corporate_war": {
                        "text": f"The wars never ended, they just became more sophisticated. Arasaka vs Militech, with the Yaks and Tyger Claws caught in between. Quantum weapons, AI armies, consciousness warfare. This is the new reality.",
                        "responses": [
                            {
                                "text": "How can we fight back?",
                                "next_node": f"{dialogue_id}_fight_back",
                                "conditions": {}
                            },
                            {
                                "text": "Is there any hope left?",
                                "next_node": f"{dialogue_id}_hope",
                                "conditions": {}
                            },
                            {
                                "text": "This is too much for me.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    }
                },
                "localization": {
                    "language": "ja-JP",
                    "region": "tokyo-cyberpunk"
                },
                "created_at": datetime.now().isoformat(),
                "updated_at": datetime.now().isoformat()
            }
            dialogues.append(dialogue_node)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create a Liquibase YAML file for NPC data"""

    changeset_id = f"data_npcs_tokyo_part3_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'tokyo_part3_npcs_generator',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'npcs',
                                'columns': [
                                    {'column': {'name': 'npc_id', 'value': npc['id']}},
                                    {'column': {'name': 'name', 'value': npc['name']}},
                                    {'column': {'name': 'role', 'value': npc['role']}},
                                    {'column': {'name': 'faction', 'value': npc['faction']}},
                                    {'column': {'name': 'location', 'value': npc['location']}},
                                    {'column': {'name': 'appearance', 'value': json.dumps(npc['appearance'], ensure_ascii=False)}},
                                    {'column': {'name': 'personality', 'value': json.dumps(npc['personality'], ensure_ascii=False)}},
                                    {'column': {'name': 'stats', 'value': json.dumps(npc['stats'], ensure_ascii=False)}},
                                    {'column': {'name': 'created_at', 'value': datetime.now().isoformat()}},
                                    {'column': {'name': 'updated_at', 'value': datetime.now().isoformat()}}
                                ]
                            }
                        } for npc in npcs
                    ]
                }
            }
        ]
    }

    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created NPC Liquibase YAML file: {output_file}")

def create_liquibase_dialogues(dialogues, output_file):
    """Create a Liquibase YAML file for dialogue data"""

    changeset_id = f"data_dialogues_tokyo_part3_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'tokyo_part3_dialogues_generator',
                    'changes': [
                        {
                            'insert': {
                                'tableName': 'dialogues',
                                'columns': [
                                    {'column': {'name': 'dialogue_id', 'value': dialogue['id']}},
                                    {'column': {'name': 'npc_id', 'value': dialogue['npc_id']}},
                                    {'column': {'name': 'quest_context', 'value': dialogue['quest_context']}},
                                    {'column': {'name': 'dialogue_type', 'value': dialogue['dialogue_type']}},
                                    {'column': {'name': 'trigger_conditions', 'value': json.dumps(dialogue['trigger_conditions'], ensure_ascii=False)}},
                                    {'column': {'name': 'dialogue_flow', 'value': json.dumps(dialogue['dialogue_flow'], ensure_ascii=False)}},
                                    {'column': {'name': 'localization', 'value': json.dumps(dialogue['localization'], ensure_ascii=False)}},
                                    {'column': {'name': 'created_at', 'value': dialogue['created_at']}},
                                    {'column': {'name': 'updated_at', 'value': dialogue['updated_at']}}
                                ]
                            }
                        } for dialogue in dialogues
                    ]
                }
            }
        ]
    }

    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_file, 'w', encoding='utf-8') as f:
        yaml.dump(liquibase_data, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

    print(f"Created Dialogue Liquibase YAML file: {output_file}")

def main():
    """Generate NPCs and Dialogues for Tokyo Part 3"""

    npcs = generate_tokyo_part3_npcs()
    dialogues = generate_tokyo_part3_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_tokyo_part3_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_tokyo_part3_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Tokyo Part 3 (2070-2093)")

if __name__ == '__main__':
    main()
