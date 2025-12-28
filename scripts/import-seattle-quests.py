#!/usr/bin/env python3
"""
Import Seattle 2020-2029 Quests
Direct database import for Seattle quests

Usage:
    python scripts/import-seattle-quests.py
"""

import psycopg2
import json
from datetime import datetime

def main():
    # Connect to database
    conn = psycopg2.connect('postgresql://postgres:postgres@localhost:5432/necpgame')
    cursor = conn.cursor()

    # Quest data - 5 new Seattle quests from 2020-2029 period
    quests_data = [
        {
            'title': 'WQ-AMERICA-2022-001 «Происхождение Старбакс: Кофейная революция»',
            'description': 'Старбакс становится центром технологической революции в кофейной индустрии. Игрок помогает развивать умные кофейные технологии, персонализированное питание и новые модели доставки в дождливом Сиэтле.',
            'level_min': 14,
            'level_max': 20,
            'status': 'active',
            'metadata': json.dumps({'id': 'content-world-seattle-2022-001', 'version': '1.0.0', 'source_file': 'knowledge/content/quests/world/america/america-seattle-2020-2029.yaml'}),
            'rewards': json.dumps({'experience': 12000, 'currency': {'type': 'eddies', 'amount': 2400}}),
            'objectives': json.dumps([{'id': 'main_objective', 'title': 'Complete coffee tech revolution', 'description': 'Help develop smart coffee technologies in Seattle', 'type': 'main', 'order': 1}]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'WQ-AMERICA-2024-003 «Штаб-квартира Amazon: Город в городе»',
            'description': 'Штаб-квартира Amazon становится автономным городом внутри Сиэтла. Игрок расследует корпоративные интриги, борется с монополией и помогает сохранить баланс между технологическим прогрессом и человеческими ценностями.',
            'level_min': 18,
            'level_max': 25,
            'status': 'active',
            'metadata': json.dumps({'id': 'content-world-seattle-2024-003', 'version': '1.0.0', 'source_file': 'knowledge/content/quests/world/america/america-seattle-2020-2029.yaml'}),
            'rewards': json.dumps({'experience': 16000, 'currency': {'type': 'eddies', 'amount': 3200}}),
            'objectives': json.dumps([{'id': 'main_objective', 'title': 'Complete corporate investigation', 'description': 'Investigate Amazon corporate practices and find balance', 'type': 'main', 'order': 1}]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'WQ-AMERICA-2025-004 «Гора Рейнир: Природная мощь»',
            'description': 'Гора Рейнир становится источником возобновляемой энергии и туристическим центром. Игрок помогает развивать экологичные технологии, борется с климатическими изменениями и сохраняет природное наследие.',
            'level_min': 15,
            'level_max': 22,
            'status': 'active',
            'metadata': json.dumps({'id': 'content-world-seattle-2025-004', 'version': '1.0.0', 'source_file': 'knowledge/content/quests/world/america/america-seattle-2020-2029.yaml'}),
            'rewards': json.dumps({'experience': 13000, 'currency': {'type': 'eddies', 'amount': 2600}}),
            'objectives': json.dumps([{'id': 'main_objective', 'title': 'Develop renewable energy systems', 'description': 'Help harness Mount Rainier for sustainable energy', 'type': 'main', 'order': 1}]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'WQ-AMERICA-2027-006 «Завод Boeing: Авиационные инновации»',
            'description': 'Завод Boeing становится центром авиационных инноваций с интеграцией ИИ и автономных систем. Игрок помогает развивать новые технологии для коммерческой авиации и космических кораблей.',
            'level_min': 20,
            'level_max': 28,
            'status': 'active',
            'metadata': json.dumps({'id': 'content-world-seattle-2027-006', 'version': '1.0.0', 'source_file': 'knowledge/content/quests/world/america/america-seattle-2020-2029.yaml'}),
            'rewards': json.dumps({'experience': 17000, 'currency': {'type': 'eddies', 'amount': 3400}}),
            'objectives': json.dumps([{'id': 'main_objective', 'title': 'Develop aviation innovations', 'description': 'Help integrate AI and autonomous systems in Boeing manufacturing', 'type': 'main', 'order': 1}]),
            'created_at': datetime.now(),
            'updated_at': datetime.now()
        },
        {
            'title': 'WQ-AMERICA-2029-008 «Тех-бум и джентрификация: Цифровой раскол»',
            'description': 'Тех-бум Сиэтла достигает пика, вызывая массовую джентрификацию и цифровой раскол общества. Игрок расследует социальные последствия технологического прогресса и помогает найти баланс между инновациями и равенством.',
            'level_min': 22,
            'level_max': 30,
            'status': 'active',
            'metadata': json.dumps({'id': 'content-world-seattle-2029-008', 'version': '1.0.0', 'source_file': 'knowledge/content/quests/world/america/america-seattle-2020-2029.yaml'}),
            'rewards': json.dumps({'experience': 19000, 'currency': {'type': 'eddies', 'amount': 3800}}),
            'objectives': json.dumps([{'id': 'main_objective', 'title': 'Address digital divide', 'description': 'Investigate tech boom gentrification and social consequences', 'type': 'main', 'order': 1}]),
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
