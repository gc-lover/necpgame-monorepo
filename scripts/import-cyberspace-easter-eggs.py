#!/usr/bin/env python3
# Issue: #2262 - Import Cyberspace Easter Eggs from YAML to Database
# Enterprise-grade data import script for easter eggs content

import yaml
import psycopg2
import json
import uuid
from datetime import datetime
import sys
import os

def load_easter_eggs_from_yaml(file_path):
    """Load easter eggs from YAML file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            data = yaml.safe_load(file)

        if 'easter_eggs' not in data:
            print("ERROR: No 'easter_eggs' section found in YAML file")
            return []

        return data['easter_eggs']
    except Exception as e:
        print(f"ERROR: Failed to load YAML file {file_path}: {e}")
        return []

def create_database_connection():
    """Create database connection"""
    try:
        # Database connection parameters
        db_params = {
            'host': os.getenv('DB_HOST', 'localhost'),
            'port': os.getenv('DB_PORT', '5432'),
            'database': os.getenv('DB_NAME', 'necp_game'),
            'user': os.getenv('DB_USER', 'postgres'),
            'password': os.getenv('DB_PASSWORD', 'postgres')
        }

        conn = psycopg2.connect(**db_params)
        conn.autocommit = False  # Use transactions
        return conn
    except Exception as e:
        print(f"ERROR: Failed to connect to database: {e}")
        sys.exit(1)

def create_tables_if_not_exist(cursor):
    """Create necessary tables if they don't exist"""
    try:
        # Create easter_eggs table
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS easter_eggs (
                id VARCHAR(255) PRIMARY KEY,
                name VARCHAR(200) NOT NULL,
                category VARCHAR(50) NOT NULL,
                difficulty VARCHAR(20) NOT NULL,
                description TEXT,
                content TEXT,
                location JSONB,
                discovery_method JSONB,
                rewards JSONB,
                lore_connections JSONB,
                status VARCHAR(20) DEFAULT 'active',
                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
            )
        """)

        # Create indexes for performance
        cursor.execute("""
            CREATE INDEX IF NOT EXISTS idx_easter_eggs_category ON easter_eggs(category);
            CREATE INDEX IF NOT EXISTS idx_easter_eggs_difficulty ON easter_eggs(difficulty);
            CREATE INDEX IF NOT EXISTS idx_easter_eggs_status ON easter_eggs(status);
        """)

        # Create player progress table
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS player_easter_egg_progress (
                player_id VARCHAR(255) NOT NULL,
                easter_egg_id VARCHAR(255) NOT NULL,
                status VARCHAR(20) DEFAULT 'undiscovered',
                discovered_at TIMESTAMP WITH TIME ZONE,
                completed_at TIMESTAMP WITH TIME ZONE,
                rewards_claimed JSONB DEFAULT '[]'::jsonb,
                hint_level INTEGER DEFAULT 0,
                visit_count INTEGER DEFAULT 0,
                last_visited TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (player_id, easter_egg_id)
            )
        """)

        # Create discovery hints table
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS discovery_hints (
                id VARCHAR(255) PRIMARY KEY,
                easter_egg_id VARCHAR(255) NOT NULL,
                hint_level INTEGER NOT NULL,
                hint_text TEXT NOT NULL,
                hint_type VARCHAR(20) DEFAULT 'direct',
                cost INTEGER DEFAULT 0,
                is_enabled BOOLEAN DEFAULT true,
                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
            )
        """)

        print("[OK] Database tables created/verified")
    except Exception as e:
        print(f"ERROR: Failed to create tables: {e}")
        raise

def import_easter_egg(cursor, egg_data):
    """Import a single easter egg"""
    try:
        egg_id = egg_data.get('id', str(uuid.uuid4()))

        # Prepare data for insertion
        location_data = json.dumps(egg_data.get('location', {}))
        discovery_data = json.dumps(egg_data.get('discovery_method', {}))
        rewards_data = json.dumps(egg_data.get('rewards', []))
        lore_data = json.dumps(egg_data.get('lore_connections', []))

        # Insert easter egg
        cursor.execute("""
            INSERT INTO easter_eggs (
                id, name, category, difficulty, description, content,
                location, discovery_method, rewards, lore_connections,
                status, updated_at
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (id) DO UPDATE SET
                name = EXCLUDED.name,
                category = EXCLUDED.category,
                difficulty = EXCLUDED.difficulty,
                description = EXCLUDED.description,
                content = EXCLUDED.content,
                location = EXCLUDED.location,
                discovery_method = EXCLUDED.discovery_method,
                rewards = EXCLUDED.rewards,
                lore_connections = EXCLUDED.lore_connections,
                updated_at = CURRENT_TIMESTAMP
        """, (
            egg_id,
            egg_data.get('name', ''),
            egg_data.get('category', ''),
            egg_data.get('difficulty', ''),
            egg_data.get('description', ''),
            egg_data.get('content', ''),
            location_data,
            discovery_data,
            rewards_data,
            lore_data,
            'active',
            datetime.now()
        ))

        # Import hints if available
        if 'hints' in egg_data:
            for hint_level, hint_data in enumerate(egg_data['hints'], 1):
                hint_id = f"{egg_id}_hint_{hint_level}"
                cursor.execute("""
                    INSERT INTO discovery_hints (
                        id, easter_egg_id, hint_level, hint_text,
                        hint_type, cost, is_enabled
                    ) VALUES (%s, %s, %s, %s, %s, %s, %s)
                    ON CONFLICT (id) DO UPDATE SET
                        hint_text = EXCLUDED.hint_text,
                        hint_type = EXCLUDED.hint_type,
                        cost = EXCLUDED.cost,
                        is_enabled = EXCLUDED.is_enabled
                """, (
                    hint_id,
                    egg_id,
                    hint_level,
                    hint_data.get('text', ''),
                    hint_data.get('type', 'direct'),
                    hint_data.get('cost', 0),
                    hint_data.get('enabled', True)
                ))

        return egg_id

    except Exception as e:
        print(f"ERROR: Failed to import easter egg {egg_data.get('name', 'unknown')}: {e}")
        raise

def main():
    """Main import function"""
    print("[START] Starting Cyberspace Easter Eggs Import")
    print("=" * 50)

    # Path to YAML file
    yaml_file = "knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml"

    if not os.path.exists(yaml_file):
        print(f"ERROR: YAML file not found: {yaml_file}")
        sys.exit(1)

    # Load easter eggs from YAML
    print(f"[INFO] Loading easter eggs from {yaml_file}...")
    easter_eggs = load_easter_eggs_from_yaml(yaml_file)

    if not easter_eggs:
        print("ERROR: No easter eggs found in YAML file")
        sys.exit(1)

    print(f"[OK] Loaded {len(easter_eggs)} easter eggs")

    # Connect to database
    print("[DB] Connecting to database...")
    conn = create_database_connection()
    cursor = conn.cursor()

    try:
        # Create tables if needed
        create_tables_if_not_exist(cursor)

        # Import easter eggs
        print("[IMPORT] Importing easter eggs...")
        imported_count = 0

        for egg_data in easter_eggs:
            egg_id = import_easter_egg(cursor, egg_data)
            imported_count += 1
            print(f"  [OK] Imported: {egg_data.get('name', 'Unknown')} ({egg_id})")

        # Commit transaction
        conn.commit()
        print(f"\n[SUCCESS] Successfully imported {imported_count} easter eggs")
        print("[SUMMARY] Import Summary:")
        print(f"   - Total easter eggs: {len(easter_eggs)}")
        print(f"   • Successfully imported: {imported_count}")
        print("   • Database tables: Created/Updated"
        print("   • Indexes: Created for performance"
        print("\n[SUCCESS] Cyberspace Easter Eggs system ready for players!")

    except Exception as e:
        conn.rollback()
        print(f"[ERROR] Import failed: {e}")
        sys.exit(1)

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    main()
