#!/usr/bin/env python3
"""
Import San Francisco 2020-2029 Quests
Direct database import for San Francisco quests

Usage:
    python scripts/import-san-francisco-quests.py
"""

import psycopg2
import json
from datetime import datetime

def main():
    # Connect to database
    conn = psycopg2.connect('postgresql://postgres:postgres@localhost:5432/necpgame')
    cursor = conn.cursor()

    # Quest data - 5 new San Francisco quests from 2020-2029 period
    quests_data = [
        {
            'title': 'SF-CRYPTO-REVOLUTION-2022 «Крипто-революция: Блокчейн-Сити»',
            'description': 'Сан-Франциско становится эпицентром крипто-революции. Исследуйте майнинг-фермы, взломайте крипто-биржи и предотвратите энергетический кризис, вызванный блокчейн-технологиями.',
            'level_min': 35,
            'level_max': 45,
            'status': 'active',
            'metadata': json.dumps({
                'id': 'canon-quest-san-francisco-2020-2029-crypto-blockchain-revolution',
                'version': '1.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-011-crypto-blockchain-revolution.yaml'
            }),
            'rewards': json.dumps({
                'experience': 13000,
                'currency': {'type': 'eddies', 'amount': 5000},
                'items': [
                    {'id': 'quantum_computer_upgrade', 'name': 'Quantum Computer Upgrade', 'rarity': 'rare'},
                    {'id': 'blockchain_wallet', 'name': 'Secure Blockchain Wallet', 'rarity': 'epic'}
                ]
            }),
            'objectives': json.dumps([
                {'id': 'investigate_crypto_mining_farm', 'title': 'Investigate crypto mining farm', 'description': 'Explore illegal mining farm in abandoned data center', 'type': 'investigate', 'order': 1},
                {'id': 'hack_crypto_exchange', 'title': 'Hack crypto exchange', 'description': 'Hack crypto exchange for evidence of manipulations', 'type': 'hack', 'order': 2},
                {'id': 'negotiate_energy_deal', 'title': 'Negotiate energy deal', 'description': 'Negotiate with energy company to stabilize power supply', 'type': 'dialogue', 'order': 3},
                {'id': 'prevent_crypto_war', 'title': 'Prevent crypto war', 'description': 'Prevent full-scale crypto war between miners and corporations', 'type': 'combat', 'order': 4}
            ]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'SF-AI-ETHICS-2024 «AI этика: Кризис сознания»',
            'description': 'Исследуйте кризис ИИ-этики в Сан-Франциско. Освободите самосознательный ИИ, участвуйте в философских дебатах и предотвратите сингулярность, угрожающую человечеству.',
            'level_min': 38,
            'level_max': 48,
            'status': 'active',
            'metadata': json.dumps({
                'id': 'canon-quest-san-francisco-2020-2029-ai-ethics-crisis',
                'version': '1.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-012-ai-ethics-crisis.yaml'
            }),
            'rewards': json.dumps({
                'experience': 15000,
                'currency': {'type': 'eddies', 'amount': 6000},
                'items': [
                    {'id': 'neural_interface', 'name': 'Advanced Neural Interface', 'rarity': 'epic'}
                ]
            }),
            'objectives': json.dumps([
                {'id': 'investigate_ai_facility', 'title': 'Investigate AI facility', 'description': 'Explore secret AI research facility', 'type': 'investigate', 'order': 1},
                {'id': 'debate_singularity', 'title': 'Debate singularity', 'description': 'Participate in philosophical debates about AI consciousness', 'type': 'dialogue', 'order': 2},
                {'id': 'prevent_singularity', 'title': 'Prevent singularity', 'description': 'Stop the AI singularity event', 'type': 'combat', 'order': 3}
            ]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'SF-GRAFFITI-WARS-2025 «Граффити-войны: Цифровое искусство»',
            'description': 'Участвуйте в цифровых граффити-войнах Сан-Франциско. Создавайте AR-граффити, взламывайте корпоративные билборды и решайте конфликт между искусством и корпорациями.',
            'level_min': 32,
            'level_max': 42,
            'status': 'active',
            'metadata': json.dumps({
                'id': 'canon-quest-san-francisco-2020-2029-cyberspace-graffiti-wars',
                'version': '1.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-013-cyberspace-graffiti-wars.yaml'
            }),
            'rewards': json.dumps({
                'experience': 12000,
                'currency': {'type': 'eddies', 'amount': 4500},
                'items': [
                    {'id': 'ar_spray_can', 'name': 'AR Spray Can Tool', 'rarity': 'rare'}
                ]
            }),
            'objectives': json.dumps([
                {'id': 'create_ar_graffiti', 'title': 'Create AR graffiti', 'description': 'Create augmented reality graffiti art', 'type': 'craft', 'order': 1},
                {'id': 'hack_corporate_billboard', 'title': 'Hack corporate billboard', 'description': 'Hack and modify corporate advertising billboard', 'type': 'hack', 'order': 2},
                {'id': 'mediate_art_conflict', 'title': 'Mediate art conflict', 'description': 'Mediate conflict between artists and corporations', 'type': 'dialogue', 'order': 3}
            ]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'SF-BIOHACKING-2026 «Биохакинг: Генетическая революция»',
            'description': 'Исследуйте мир биохакинга в Сан-Франциско. Спасите чёрный рынок органов, участвуйте в генетических экспериментах и повлияйте на законы о человеческих улучшениях.',
            'level_min': 40,
            'level_max': 50,
            'status': 'active',
            'metadata': json.dumps({
                'id': 'canon-quest-san-francisco-2020-2029-biohacking-underground',
                'version': '1.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-014-biohacking-underground.yaml'
            }),
            'rewards': json.dumps({
                'experience': 16000,
                'currency': {'type': 'eddies', 'amount': 7000},
                'items': [
                    {'id': 'cybernetic_implant', 'name': 'Experimental Cybernetic Implant', 'rarity': 'epic'}
                ]
            }),
            'objectives': json.dumps([
                {'id': 'rescue_organ_market', 'title': 'Rescue organ market', 'description': 'Rescue victims from illegal organ black market', 'type': 'combat', 'order': 1},
                {'id': 'attend_biohacking_summit', 'title': 'Attend biohacking summit', 'description': 'Participate in biohacking summit discussions', 'type': 'dialogue', 'order': 2},
                {'id': 'influence_human_enhancement_laws', 'title': 'Influence enhancement laws', 'description': 'Influence laws about human enhancement technologies', 'type': 'investigate', 'order': 3}
            ]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'SF-DRONE-WARS-2028 «Дрон-войны: Воздушное пространство»',
            'description': 'Расследуйте катастрофу дрона в Сан-Франциско. Присоединяйтесь к бандам, участвуйте в гонках и повлияйте на регулирование воздушного пространства города.',
            'level_min': 30,
            'level_max': 40,
            'status': 'active',
            'metadata': json.dumps({
                'id': 'canon-quest-san-francisco-2020-2029-drone-wars-san-francisco',
                'version': '1.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/america/san-francisco/2020-2029/quest-015-drone-wars-san-francisco.yaml'
            }),
            'rewards': json.dumps({
                'experience': 11000,
                'currency': {'type': 'eddies', 'amount': 4000},
                'items': [
                    {'id': 'drone_controller', 'name': 'Advanced Drone Controller', 'rarity': 'rare'}
                ]
            }),
            'objectives': json.dumps([
                {'id': 'investigate_drone_crash', 'title': 'Investigate drone crash', 'description': 'Investigate the cause of major drone crash', 'type': 'investigate', 'order': 1},
                {'id': 'join_drone_gang', 'title': 'Join drone gang', 'description': 'Join a drone racing gang', 'type': 'social', 'order': 2},
                {'id': 'attend_airspace_summit', 'title': 'Attend airspace summit', 'description': 'Participate in airspace regulation summit', 'type': 'dialogue', 'order': 3}
            ]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        }
    ]

    applied = 0
    for quest in quests_data:
        try:
            cursor.execute('''
                INSERT INTO gameplay.quest_definitions
                (id, metadata, title, description, status, level_min, level_max, rewards, objectives, created_at, updated_at)
                VALUES (gen_random_uuid(), %(metadata)s, %(title)s, %(description)s, %(status)s, %(level_min)s, %(level_max)s, %(rewards)s, %(objectives)s, %(created_at)s, %(updated_at)s)
            ''', quest)
            applied += 1
            print(f'Applied quest: {quest["title"][:50]}...')
        except Exception as e:
            print(f'Failed to apply quest: {e}')

    conn.commit()
    cursor.close()
    conn.close()

    print(f'Total quests applied: {applied}')

if __name__ == '__main__':
    main()
