#!/usr/bin/env python3
"""
Batch import script for Warsaw 2020-2029 quests.
Imports quest YAML files from knowledge/canon/narrative/quests/ into database.
"""

import os
import yaml
import psycopg2
from psycopg2.extras import Json
import json
from datetime import datetime

def load_yaml_file(file_path):
    """Load and parse YAML file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            return yaml.safe_load(file)
    except Exception as e:
        print(f"[ERROR] Failed to load {file_path}: {e}")
        return None

def connect_to_database():
    """Establish database connection."""
    try:
        conn = psycopg2.connect(
            host=os.getenv('DB_HOST', 'localhost'),
            port=os.getenv('DB_PORT', '5432'),
            database=os.getenv('DB_NAME', 'gameplay'),
            user=os.getenv('DB_USER', 'gameplay_user'),
            password=os.getenv('DB_PASSWORD', 'gameplay_pass')
        )
        conn.autocommit = False
        return conn
    except Exception as e:
        print(f"[ERROR] Database connection failed: {e}")
        return None

def create_quest_table_if_not_exists(cursor):
    """Create quest definitions table if it doesn't exist."""
    try:
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS gameplay.quest_definitions (
                id VARCHAR(255) PRIMARY KEY,
                title VARCHAR(500) NOT NULL,
                description TEXT,
                city VARCHAR(255),
                country VARCHAR(255),
                time_period VARCHAR(50),
                category VARCHAR(100),
                difficulty VARCHAR(50),
                estimated_duration VARCHAR(50),
                tags JSONB,
                objectives JSONB,
                choice_points JSONB,
                ending_variations JSONB,
                narrative_elements JSONB,
                dialogue_samples JSONB,
                lore_connections JSONB,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            CREATE INDEX IF NOT EXISTS idx_quest_city ON gameplay.quest_definitions(city);
            CREATE INDEX IF NOT EXISTS idx_quest_country ON gameplay.quest_definitions(country);
            CREATE INDEX IF NOT EXISTS idx_quest_category ON gameplay.quest_definitions(category);
            CREATE INDEX IF NOT EXISTS idx_quest_difficulty ON gameplay.quest_definitions(difficulty);
        """)
        print("[SUCCESS] Quest definitions table ready")
        return True
    except Exception as e:
        print(f"[ERROR] Failed to create table: {e}")
        return False

def insert_quest(cursor, quest_data):
    """Insert quest data into database."""
    try:
        cursor.execute("""
            INSERT INTO gameplay.quest_definitions (
                id, title, description, city, country, time_period, category,
                difficulty, estimated_duration, tags, objectives, choice_points,
                ending_variations, narrative_elements, dialogue_samples, lore_connections
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (id) DO UPDATE SET
                title = EXCLUDED.title,
                description = EXCLUDED.description,
                city = EXCLUDED.city,
                country = EXCLUDED.country,
                time_period = EXCLUDED.time_period,
                category = EXCLUDED.category,
                difficulty = EXCLUDED.difficulty,
                estimated_duration = EXCLUDED.estimated_duration,
                tags = EXCLUDED.tags,
                objectives = EXCLUDED.objectives,
                choice_points = EXCLUDED.choice_points,
                ending_variations = EXCLUDED.ending_variations,
                narrative_elements = EXCLUDED.narrative_elements,
                dialogue_samples = EXCLUDED.dialogue_samples,
                lore_connections = EXCLUDED.lore_connections,
                updated_at = CURRENT_TIMESTAMP
        """, (
            quest_data['id'],
            quest_data['title'],
            quest_data['description'],
            quest_data['city'],
            quest_data['country'],
            quest_data['time_period'],
            quest_data['category'],
            quest_data['difficulty'],
            quest_data['estimated_duration'],
            Json(quest_data['tags']),
            Json(quest_data['objectives']),
            Json(quest_data.get('choice_points', [])),
            Json(quest_data.get('ending_variations', [])),
            Json(quest_data.get('narrative_elements', [])),
            Json(quest_data.get('dialogue_samples', [])),
            Json(quest_data.get('lore_connections', []))
        ))
        return True
    except Exception as e:
        print(f"[ERROR] Failed to insert quest {quest_data['id']}: {e}")
        return False

def main():
    """Main import function."""
    print("[INFO] Starting Warsaw quests import...")

    # Quest files to import
    quest_files = [
        "../../knowledge/canon/narrative/quests/warsaw-old-town-revival-2020-2029.yaml",
        "../../knowledge/canon/narrative/quests/warsaw-underground-resistance-2020-2029.yaml"
    ]

    imported_count = 0

    # Connect to database
    conn = connect_to_database()
    if not conn:
        print("[ERROR] Cannot proceed without database connection")
        return

    try:
        cursor = conn.cursor()

        # Ensure table exists
        if not create_quest_table_if_not_exists(cursor):
            print("[ERROR] Cannot proceed without quest table")
            return

        # Import each quest
        for quest_file in quest_files:
            if not os.path.exists(quest_file):
                print(f"[WARNING] Quest file not found: {quest_file}")
                continue

            print(f"[INFO] Processing {quest_file}...")
            quest_data = load_yaml_file(quest_file)

            if not quest_data:
                continue

            if insert_quest(cursor, quest_data):
                imported_count += 1
                print(f"[SUCCESS] Imported quest: {quest_data['id']}")
            else:
                print(f"[ERROR] Failed to import quest: {quest_data['id']}")

        # Commit transaction
        conn.commit()
        print(f"\n[SUCCESS] Successfully imported {imported_count} Warsaw quests")

        if imported_count > 0:
            print("[SUMMARY] Import Summary:")
            print("   - Warsaw Old Town Revival quest"            print("   - Warsaw Underground Resistance quest"            print(f"   - Total quests imported: {imported_count}")
            print("   - Database: gameplay.quest_definitions"
    except Exception as e:
        print(f"[ERROR] Import failed: {e}")
        if conn:
            conn.rollback()
    finally:
        if conn:
            conn.close()

if __name__ == "__main__":
    main()

