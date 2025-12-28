#!/usr/bin/env python3
"""
Check and create quest_definitions table if it doesn't exist
Issue: #2227 - Database migrations for quest definitions
"""

import os
import sys
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

def check_and_create_schema(conn):
    """Check and create gameplay schema if needed"""
    try:
        with conn.cursor() as cur:
            # Check if schema exists
            cur.execute("""
                SELECT COUNT(*) FROM information_schema.schemata
                WHERE schema_name = 'gameplay'
            """)
            count = cur.fetchone()[0]

            if count == 0:
                print("[CREATING] Creating gameplay schema...")
                cur.execute("CREATE SCHEMA IF NOT EXISTS gameplay")
                print("[SUCCESS] gameplay schema created")
            else:
                print("[OK] gameplay schema already exists")

        conn.commit()
    except Exception as e:
        conn.rollback()
        print(f"[ERROR] Failed to create schema: {e}")
        raise

def check_quest_definitions_table(conn):
    """Check if quest_definitions table exists and is properly structured"""
    try:
        with conn.cursor() as cur:
            # Check if table exists
            cur.execute("""
                SELECT COUNT(*) FROM information_schema.tables
                WHERE table_schema = 'gameplay'
                AND table_name = 'quest_definitions'
            """)
            count = cur.fetchone()[0]

            if count == 0:
                print("[CREATING] Creating quest_definitions table...")

                # Create the table
                create_table_sql = """
                CREATE TABLE IF NOT EXISTS gameplay.quest_definitions (
                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    quest_id VARCHAR(255) NOT NULL UNIQUE,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    category VARCHAR(100) NOT NULL DEFAULT 'main',
                    difficulty VARCHAR(50) NOT NULL DEFAULT 'normal' CHECK (difficulty IN ('easy', 'normal', 'hard', 'expert', 'legendary')),
                    level_requirement INTEGER NOT NULL DEFAULT 1 CHECK (level_requirement >= 1),
                    time_limit_minutes INTEGER,
                    is_repeatable BOOLEAN NOT NULL DEFAULT false,
                    max_completions INTEGER,
                    rewards JSONB,
                    objectives JSONB NOT NULL,
                    prerequisites JSONB,
                    location VARCHAR(255),
                    npc_giver VARCHAR(255),
                    npc_completer VARCHAR(255),
                    faction_requirements JSONB,
                    reputation_requirements JSONB,
                    item_requirements JSONB,
                    quest_chain_id VARCHAR(255),
                    quest_chain_order INTEGER,
                    is_active BOOLEAN NOT NULL DEFAULT true,
                    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """

                cur.execute(create_table_sql)
                print("[SUCCESS] quest_definitions table created")

                # Create indexes
                indexes_sql = """
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_category ON gameplay.quest_definitions(category);
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_difficulty ON gameplay.quest_definitions(difficulty);
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_location ON gameplay.quest_definitions(location);
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_level_req ON gameplay.quest_definitions(level_requirement);
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_active ON gameplay.quest_definitions(is_active) WHERE is_active = true;
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_quest_chain ON gameplay.quest_definitions(quest_chain_id, quest_chain_order);
                CREATE INDEX IF NOT EXISTS idx_quest_definitions_quest_id ON gameplay.quest_definitions(quest_id);
                """

                cur.execute(indexes_sql)
                print("[SUCCESS] Indexes created for quest_definitions table")

                conn.commit()
                return True

            else:
                print("[OK] quest_definitions table already exists")

                # Check table structure
                cur.execute("""
                    SELECT COUNT(*) FROM information_schema.columns
                    WHERE table_schema = 'gameplay'
                    AND table_name = 'quest_definitions'
                """)
                column_count = cur.fetchone()[0]
                print(f"[INFO] Table has {column_count} columns")

                return True

    except Exception as e:
        conn.rollback()
        print(f"[ERROR] Failed to check/create table: {e}")
        raise

def count_existing_quests(conn):
    """Count existing quests in the table"""
    try:
        with conn.cursor() as cur:
            cur.execute("SELECT COUNT(*) FROM gameplay.quest_definitions")
            count = cur.fetchone()[0]
            print(f"[INFO] Found {count} existing quests in quest_definitions table")
            return count
    except Exception as e:
        print(f"[WARNING] Could not count existing quests: {e}")
        return 0

def main():
    print("=" * 60)
    print("QUEST DEFINITIONS TABLE CHECKER/CREATOR")
    print("Issue: #2227 - Database migrations for quest definitions")
    print("=" * 60)

    try:
        # Connect to database
        print("[CONNECTING] Connecting to database...")
        conn = get_db_connection()
        print("[CONNECTED] Database connection established")

        # Check/create schema
        check_and_create_schema(conn)

        # Check/create table
        table_created = check_quest_definitions_table(conn)

        # Count existing quests
        quest_count = count_existing_quests(conn)

        print("\n" + "=" * 60)
        if table_created:
            print("[SUCCESS] quest_definitions table is ready for quest imports!")
            print(f"[INFO] Table contains {quest_count} existing quests")
        else:
            print("[OK] quest_definitions table was already properly configured")
            print(f"[INFO] Table contains {quest_count} existing quests")
        print("=" * 60)

    except Exception as e:
        print(f"[ERROR] Operation failed: {e}")
        sys.exit(1)
    finally:
        if 'conn' in locals():
            conn.close()
            print("[CLOSED] Database connection closed")

if __name__ == "__main__":
    main()
