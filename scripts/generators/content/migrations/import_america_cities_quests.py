#!/usr/bin/env python3
"""
Import America Cities quests to database
Issue: #2046 - Backend: Импорт 6 квестов America cities (Miami, Detroit, Mexico City) в базу данных
"""

import json
import os
import sys
import yaml
from pathlib import Path
from datetime import datetime
import psycopg2
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

QUEST_FILES = [
    # Miami
    '../../../knowledge/canon/narrative/quests/wynwood-walls-miami-2020-2029.yaml',
    '../../../knowledge/canon/narrative/quests/art-basel-miami-2020-2029.yaml',

    # Detroit
    '../../../knowledge/canon/narrative/quests/techno-homeland-detroit-2020-2029.yaml',
    '../../../knowledge/canon/narrative/quests/red-wings-revival-detroit-2020-2029.yaml',

    # Mexico City
    '../../../knowledge/canon/narrative/quests/zocalo-square-mexico-city-2020-2029.yaml',
    '../../../knowledge/canon/narrative/quests/teotihuacan-pyramids-mexico-city-2020-2029.yaml'
]

def get_db_connection():
    """Get database connection"""
    return psycopg2.connect(
        host=os.getenv('DB_HOST', 'localhost'),
        port=os.getenv('DB_PORT', '5432'),
        database=os.getenv('DB_NAME', 'necpgame'),
        user=os.getenv('DB_USER', 'postgres'),
        password=os.getenv('DB_PASSWORD', 'postgres')
    )

def parse_quest_file(file_path):
    """Parse quest YAML file and extract data for database"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)

        metadata = data.get('metadata', {})
        quest_def = data.get('quest_definition', {})

        # Extract basic quest info
        quest_id = metadata.get('id', f"quest-{Path(file_path).stem}")
        title = metadata.get('title', 'Unknown Quest')
        description = quest_def.get('description', '')

        # Extract quest definition data
        level_min = quest_def.get('level_min', 1)
        level_max = quest_def.get('level_max', 100)
        status = 'active'

        # Build rewards JSON
        rewards = quest_def.get('rewards', {})
        rewards_json = json.dumps(rewards, ensure_ascii=False, default=str)

        # Build objectives JSON
        objectives = quest_def.get('objectives', [])
        objectives_json = json.dumps(objectives, ensure_ascii=False, default=str)

        # Build metadata JSON
        metadata_json = json.dumps({
            'id': quest_id,
            'version': metadata.get('version', '1.0.0'),
            'source_file': str(file_path),
            'category': metadata.get('category', 'narrative'),
            'tags': metadata.get('tags', [])
        }, ensure_ascii=False)

        return {
            'id': quest_id,
            'title': title,
            'description': description,
            'status': status,
            'level_min': level_min,
            'level_max': level_max,
            'rewards': rewards_json,
            'objectives': objectives_json,
            'metadata': metadata_json
        }

    except Exception as e:
        print(f"[ERROR] Failed to parse quest file {file_path}: {e}")
        return None

def import_quest(conn, quest_data):
    """Import a single quest to database"""
    try:
        quest_id = quest_data['id']

        # Check if quest already exists
        with conn.cursor() as cursor:
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
                quest_data['id'], quest_data['metadata'], quest_data['title'],
                quest_data['description'], quest_data['status'], quest_data['level_min'],
                quest_data['level_max'], quest_data['rewards'], quest_data['objectives'],
                datetime.now(), datetime.now()
            ))

        conn.commit()
        print(f"[SUCCESS] Quest '{quest_id}' imported successfully")
        return True

    except Exception as e:
        print(f"[ERROR] Failed to import quest {quest_data['id']}: {e}")
        conn.rollback()
        return False

def main():
    print("=" * 80)
    print("AMERICA CITIES QUESTS IMPORT")
    print("Issue: #2046 - Backend: Импорт 6 квестов America cities (Miami, Detroit, Mexico City) в базу данных")
    print("=" * 80)

    success_count = 0
    total_quests = len(QUEST_FILES)

    try:
        # Connect to database
        print("[CONNECTING] Connecting to database...")
        conn = get_db_connection()
        print("[CONNECTED] Database connection established")

        # Check if quest_definitions table exists
        print("[VERIFYING] Checking quest_definitions table...")
        with conn.cursor() as cursor:
            cursor.execute("""
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = 'gameplay'
                AND table_name = 'quest_definitions'
            """)
            count = cursor.fetchone()[0]

            if count == 0:
                print("[ERROR] quest_definitions table does not exist! Run migrations first.")
                sys.exit(1)

        print("[INFO] quest_definitions table exists")

        # Import each quest
        for quest_file in QUEST_FILES:
            quest_file_path = Path(__file__).parent / quest_file

            if not quest_file_path.exists():
                print(f"[WARNING] Quest file not found: {quest_file_path}")
                continue

            print(f"[PARSING] {quest_file_path.name}")

            # Parse quest data
            quest_data = parse_quest_file(quest_file_path)
            if not quest_data:
                continue

            # Import quest
            if import_quest(conn, quest_data):
                success_count += 1

        # Final verification
        print(f"\n[RESULTS] Successfully imported {success_count}/{total_quests} quests")

        if success_count > 0:
            print("[VERIFICATION] Checking imported quests...")
            with conn.cursor() as cursor:
                cursor.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
                total_count = cursor.fetchone()[0]
                print(f"[INFO] Total quests in database: {total_count}")

        if success_count == total_quests:
            print("[COMPLETE] All America Cities quests imported successfully!")
        else:
            print(f"[WARNING] Only {success_count}/{total_quests} quests were imported")

    except Exception as e:
        print(f"[ERROR] Import failed: {e}")
        sys.exit(1)
    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

if __name__ == "__main__":
    main()