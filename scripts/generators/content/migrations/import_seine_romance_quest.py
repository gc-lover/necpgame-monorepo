#!/usr/bin/env python3
"""
Import Seine Romance quest to database
Issue: #140904671 - Quest Paris - Seine Romance - Import to DB
"""

import json
import os
import sys
from pathlib import Path
from datetime import datetime
import psycopg2
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

def get_db_connection():
    """Get database connection"""
    return psycopg2.connect(
        host=os.getenv('DB_HOST', 'localhost'),
        port=os.getenv('DB_PORT', '5432'),
        database=os.getenv('DB_NAME', 'necpgame'),
        user=os.getenv('DB_USER', 'postgres'),
        password=os.getenv('DB_PASSWORD', 'postgres')
    )

def import_quest():
    """Import Seine Romance quest directly to database"""
    try:
        conn = get_db_connection()
        print("[CONNECTED] Database connection established")

        with conn.cursor() as cursor:
            quest_id = 'canon-quest-paris-seine-romance'
            title = 'Париж 2020-2029 — Романтика Сены'
            description = 'Квест описывает приватный круиз по Сене во время заката, раскрывая атмосферу «Paris, je t\'\'aime» и ритуалы мостов.'
            status = 'active'
            level_min = 5
            level_max = 50

            # Build rewards JSON
            rewards_json = json.dumps({
                'experience': 1000,
                'money': {
                    'type': 'eddies',
                    'value': -200
                },
                'reputation': {
                    'romance': 25
                },
                'unlocks': {
                    'achievements': [{
                        'id': 'paris_romantic',
                        'name': 'Романтик Парижа'
                    }]
                }
            }, ensure_ascii=False)

            # Build objectives JSON
            objectives_json = json.dumps([
                {
                    'id': 'rent_boat',
                    'type': 'interaction',
                    'description': 'Арендовать bateau-mouche для двоих',
                    'required': True
                },
                {
                    'id': 'start_cruise',
                    'type': 'location',
                    'description': 'Стартовать маршрут из центра города',
                    'required': True
                },
                {
                    'id': 'pass_bridges',
                    'type': 'location',
                    'description': 'Проплыть под Pont Neuf, Pont Alexandre III и Pont des Arts',
                    'required': True
                },
                {
                    'id': 'see_eiffel',
                    'type': 'location',
                    'description': 'Встретить вид на Эйфелеву башню с воды',
                    'required': True
                },
                {
                    'id': 'kiss_under_bridge',
                    'type': 'interaction',
                    'description': 'Совериить поцелуй под мостом',
                    'required': True
                }
            ], ensure_ascii=False)

            # Build metadata JSON
            metadata_json = json.dumps({
                'id': quest_id,
                'version': '2.0.0',
                'source_file': 'knowledge/canon/lore/timeline-author/quests/europe/paris/2020-2029/quest-003-seine-romance.yaml'
            }, ensure_ascii=False)

            # Check if quest already exists
            cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE id = %s", (quest_id,))
            count = cursor.fetchone()[0]
            if count > 0:
                print(f"[SKIP] Quest '{quest_id}' already exists in database")
                return True

            # Insert quest
            query = """
                INSERT INTO gameplay.quest_definitions
                (id, metadata, title, description, status, level_min, level_max, rewards, objectives, created_at, updated_at)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """

            cursor.execute(query, (
                quest_id, metadata_json, title, description, status,
                level_min, level_max, rewards_json, objectives_json,
                datetime.now(), datetime.now()
            ))

            conn.commit()
            print("[SUCCESS] Quest imported successfully")

            # Verify insertion
            cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions WHERE id = %s", (quest_id,))
            count = cursor.fetchone()[0]
            if count > 0:
                print(f"[VERIFIED] Quest '{quest_id}' exists in database")
                return True
            else:
                print("[ERROR] Quest was not inserted!")
                return False

    except Exception as e:
        print(f"[ERROR] Failed to import quest: {e}")
        if 'conn' in locals():
            conn.rollback()
        return False
    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

def main():
    print("=" * 60)
    print("SEINE ROMANCE QUEST IMPORT")
    print("Issue: #140904671 - Quest Paris - Seine Romance - Import to DB")
    print("=" * 60)

    try:
        success = import_quest()
        if success:
            print("[COMPLETE] Seine Romance quest imported successfully!")
        else:
            print("[FAILED] Import failed!")
            sys.exit(1)

    except Exception as e:
        print(f"[ERROR] Import failed: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
