#!/usr/bin/env python3
"""
Generate NPCs and Dialogues for Sao Paulo Part 2 Quests
Creates supporting NPC characters and dialogue nodes for Sao Paulo Part 2 city quests.
"""

import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
import hashlib

def generate_sao_paulo_part2_npcs():
    """Generate NPC characters for Sao Paulo Part 2 quests"""

    sao_paulo_part2_npcs = [
        {
            "id": "sao-paulo-carnival-director-2020",
            "name": "Maria Aparecida",
            "role": "Carnival Director",
            "faction": "Rio-São Paulo Carnival Association",
            "location": "São Paulo, Carnival Headquarters",
            "appearance": {
                "age": 45,
                "gender": "female",
                "ethnicity": "brazilian",
                "height": "5'6\"",
                "build": "curvy",
                "hair": "colorful braids, festive"
            },
            "personality": {
                "traits": ["enthusiastic", "creative", "passionate"],
                "background": "Former samba dancer who became carnival organizer",
                "motivations": ["preserve cultural traditions", "unite communities", "celebrate diversity"]
            },
            "stats": {
                "intelligence": 75,
                "charisma": 95,
                "empathy": 90,
                "morality": 80
            }
        },
        {
            "id": "sao-paulo-tech-founder-2020",
            "name": "Dr. Carlos Silva",
            "role": "Tech Startup Founder",
            "faction": "Silicon Valley of South America",
            "location": "São Paulo, Tech Innovation District",
            "appearance": {
                "age": 38,
                "gender": "male",
                "ethnicity": "brazilian",
                "height": "5'11\"",
                "build": "athletic",
                "hair": "short, professional"
            },
            "personality": {
                "traits": ["innovative", "ambitious", "visionary"],
                "background": "MIT-educated engineer who returned to Brazil to build local tech ecosystem",
                "motivations": ["develop Brazilian tech industry", "create jobs", "compete globally"]
            },
            "stats": {
                "intelligence": 95,
                "charisma": 85,
                "empathy": 70,
                "morality": 75
            }
        },
        {
            "id": "sao-paulo-football-president-2020",
            "name": "Roberto de Andrade",
            "role": "Corinthians President",
            "faction": "Sport Club Corinthians Paulista",
            "location": "São Paulo, Neo Química Arena",
            "appearance": {
                "age": 52,
                "gender": "male",
                "ethnicity": "brazilian",
                "height": "6'0\"",
                "build": "imposing",
                "hair": "gray, slicked back"
            },
            "personality": {
                "traits": ["loyal", "strategic", "competitive"],
                "background": "Former player who rose through club management ranks",
                "motivations": ["win championships", "grow fan base", "maintain club legacy"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 90,
                "empathy": 65,
                "morality": 70
            }
        },
        {
            "id": "sao-paulo-security-chief-2020",
            "name": "Major Ana Santos",
            "role": "Public Security Chief",
            "faction": "São Paulo Military Police",
            "location": "São Paulo, Police Headquarters",
            "appearance": {
                "age": 48,
                "gender": "female",
                "ethnicity": "brazilian",
                "height": "5'8\"",
                "build": "fit",
                "hair": "black, military cut"
            },
            "personality": {
                "traits": ["disciplined", "tough", "dedicated"],
                "background": "Military academy graduate with 25 years in law enforcement",
                "motivations": ["reduce crime", "protect citizens", "restore order"]
            },
            "stats": {
                "intelligence": 85,
                "charisma": 75,
                "empathy": 80,
                "morality": 90
            }
        },
        {
            "id": "sao-paulo-immigrant-leader-2020",
            "name": "Takeshi Nakamura",
            "role": "Japanese Community Leader",
            "faction": "Liberdade District Association",
            "location": "São Paulo, Liberdade District",
            "appearance": {
                "age": 55,
                "gender": "male",
                "ethnicity": "japanese-brazilian",
                "height": "5'7\"",
                "build": "slender",
                "hair": "black with gray"
            },
            "personality": {
                "traits": ["wise", "harmonious", "community-oriented"],
                "background": "Third-generation Japanese-Brazilian preserving cultural heritage",
                "motivations": ["maintain cultural identity", "promote integration", "support youth"]
            },
            "stats": {
                "intelligence": 80,
                "charisma": 70,
                "empathy": 95,
                "morality": 85
            }
        },
        {
            "id": "sao-paulo-corinthians-fan-2020",
            "name": "Roberto 'Timao' Costa",
            "role": "Die-hard Fan",
            "faction": "Gaviões da Fiel (Corinthians Ultras)",
            "location": "São Paulo, Various Locations",
            "appearance": {
                "age": 32,
                "gender": "male",
                "ethnicity": "brazilian",
                "height": "5'10\"",
                "build": "muscular",
                "hair": "shaved, tattoos visible"
            },
            "personality": {
                "traits": ["passionate", "loyal", "intense"],
                "background": "Lifelong Corinthians supporter, involved in fan activities",
                "motivations": ["support team", "create atmosphere", "defend club honor"]
            },
            "stats": {
                "intelligence": 70,
                "charisma": 85,
                "empathy": 75,
                "morality": 65
            }
        }
    ]

    return sao_paulo_part2_npcs

def generate_sao_paulo_part2_dialogues(npcs):
    """Generate dialogue nodes for Sao Paulo Part 2 NPCs"""

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
                "quest_context": f"sao_paulo_part2_quest_{(i % 6) + 6:03d}",
                "dialogue_type": "quest_related",
                "trigger_conditions": {
                    "quest_active": f"sao_paulo_part2_quest_{(i % 6) + 6:03d}",
                    "player_reputation": npc.get('faction', 'neutral'),
                    "time_of_day": "any"
                },
                "dialogue_flow": {
                    "opening_line": {
                        "text": f"*greets you with characteristic São Paulo energy* Oi! Sou {npc_name}. Como posso ajudar você hoje?",
                        "responses": [
                            {
                                "text": "Estou procurando trabalho.",
                                "next_node": f"{dialogue_id}_work",
                                "conditions": {"player_level": {"min": 1, "max": 20}}
                            },
                            {
                                "text": "Conte-me sobre São Paulo.",
                                "next_node": f"{dialogue_id}_city_info",
                                "conditions": {}
                            },
                            {
                                "text": "Até logo.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_work": {
                        "text": f"São Paulo sempre precisa de pessoas dispostas a ajudar. Como alguém que trabalha com {npc.get('role', 'a cidade').lower()}, posso precisar de assistência em assuntos importantes. Você está interessado?",
                        "responses": [
                            {
                                "text": "Sim, estou interessado.",
                                "next_node": "quest_offer",
                                "conditions": {},
                                "actions": ["offer_quest"]
                            },
                            {
                                "text": "Que tipo de trabalho?",
                                "next_node": f"{dialogue_id}_work_details",
                                "conditions": {}
                            },
                            {
                                "text": "Talvez depois.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    },
                    f"{dialogue_id}_city_info": {
                        "text": f"São Paulo é uma cidade incrível - uma mistura de culturas, inovação e energia. Como alguém que trabalha aqui, vejo o melhor da diversidade que faz desta cidade especial todos os dias.",
                        "responses": [
                            {
                                "text": "O que faz São Paulo única?",
                                "next_node": f"{dialogue_id}_unique",
                                "conditions": {}
                            },
                            {
                                "text": "Conte-me sobre a cultura brasileira.",
                                "next_node": f"{dialogue_id}_culture",
                                "conditions": {}
                            },
                            {
                                "text": "Preciso ir.",
                                "next_node": "end_conversation",
                                "conditions": {}
                            }
                        ]
                    }
                },
                "localization": {
                    "language": "pt-BR",
                    "region": "sao_paulo"
                },
                "created_at": datetime.now().isoformat(),
                "updated_at": datetime.now().isoformat()
            }
            dialogues.append(dialogue_node)

    return dialogues

def create_liquibase_npcs(npcs, output_file):
    """Create a Liquibase YAML file for NPC data"""

    changeset_id = f"data_npcs_sao_paulo_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'sao_paulo_npcs_generator',
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

    changeset_id = f"data_dialogues_sao_paulo_part2_support_{datetime.now().strftime('%Y%m%d%H%M%S')}"

    liquibase_data = {
        'databaseChangeLog': [
            {
                'changeSet': {
                    'id': changeset_id,
                    'author': 'sao_paulo_dialogues_generator',
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
    """Generate NPCs and Dialogues for Sao Paulo Part 2"""

    npcs = generate_sao_paulo_part2_npcs()
    dialogues = generate_sao_paulo_part2_dialogues(npcs)

    # Create output files
    npc_output = Path('infrastructure/liquibase/data/gameplay/npcs/data_npcs_sao_paulo_part2_support.yaml')
    dialogue_output = Path('infrastructure/liquibase/data/gameplay/dialogues/data_dialogues_sao_paulo_part2_support.yaml')

    create_liquibase_npcs(npcs, npc_output)
    create_liquibase_dialogues(dialogues, dialogue_output)

    print(f"Generated {len(npcs)} NPCs and {len(dialogues)} dialogue nodes for Sao Paulo Part 2")

if __name__ == '__main__':
    main()
