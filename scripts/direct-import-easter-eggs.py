#!/usr/bin/env python3
"""
Direct Import Cyberspace Easter Eggs from YAML to Database
Bypasses API and imports directly to PostgreSQL

Usage:
    python scripts/direct-import-easter-eggs.py
"""

import os
import sys
import yaml
import psycopg2
import psycopg2.extras
from pathlib import Path
from typing import Dict, List, Any
from dataclasses import dataclass

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

@dataclass
class EasterEgg:
    """Easter egg data structure"""
    id: str
    name: str
    description: str
    category: str
    difficulty: str
    discovery_methods: List[str]
    rewards: Dict[str, Any]
    hints: List[Dict[str, Any]]
    lore_connections: List[str]

def load_yaml_content() -> Dict[str, Any]:
    """Load easter eggs from YAML file"""
    yaml_path = Path("knowledge/canon/interactive-objects/cyberspace-easter-eggs.yaml")

    if not yaml_path.exists():
        print(f"ERROR: YAML file not found at {yaml_path}")
        return {}

    try:
        with open(yaml_path, 'r', encoding='utf-8') as f:
            data = yaml.safe_load(f)
        print(f"SUCCESS: Loaded YAML content from {yaml_path}")
        return data
    except Exception as e:
        print(f"ERROR: Failed to load YAML: {e}")
        return {}

def parse_easter_eggs(yaml_data: Dict[str, Any]) -> List[EasterEgg]:
    """Parse YAML data into EasterEgg objects"""
    easter_eggs = []

    # Try new structure first (easter_eggs array)
    eggs_data = yaml_data.get('easter_eggs', [])
    if eggs_data:
        print(f"Found {len(eggs_data)} easter eggs in easter_eggs array")
        for egg_data in eggs_data:
            egg = EasterEgg(
                id=egg_data.get('id', ''),
                name=egg_data.get('name', ''),
                description=egg_data.get('description', ''),
                category=egg_data.get('category', 'unknown'),
                difficulty=egg_data.get('difficulty', 'medium'),
                discovery_methods=egg_data.get('discovery_methods', []),
                rewards=egg_data.get('rewards', {}),
                hints=egg_data.get('hints', []),
                lore_connections=egg_data.get('lore_connections', [])
            )
            if egg.id and egg.name:
                easter_eggs.append(egg)
                print(f"Parsed easter egg: {egg.name} ({egg.category})")
    else:
        # Fallback to old structure (content.sections)
        sections = yaml_data.get('content', {}).get('sections', [])
        print(f"Found {len(sections)} sections in YAML (legacy format)")

        for section in sections:
            if 'metadata' not in section:
                continue

            metadata = section['metadata']
            egg_id = metadata.get('id', '')
            name = metadata.get('name', '')

            if not egg_id or not name:
                continue

            # Parse rewards
            rewards = metadata.get('rewards', {})
            if isinstance(rewards, str):
                rewards = {"description": rewards}

            # Parse hints
            hints = []
            if 'hints' in metadata:
                for hint_data in metadata['hints']:
                    hint = {
                        'level': hint_data.get('level', 1),
                        'text': hint_data.get('text', ''),
                        'cost': hint_data.get('cost', 0)
                    }
                    hints.append(hint)

            # Create EasterEgg object
            egg = EasterEgg(
                id=egg_id,
                name=name,
                description=section.get('body', ''),
                category=metadata.get('category', 'unknown'),
                difficulty=metadata.get('difficulty', 'medium'),
                discovery_methods=metadata.get('discovery_methods', []),
                rewards=rewards,
                hints=hints,
                lore_connections=metadata.get('lore_connections', [])
            )

            easter_eggs.append(egg)
            print(f"Parsed easter egg: {egg.name} ({egg.category})")

    return easter_eggs

def create_database_tables(conn):
    """Create necessary database tables"""
    try:
        with conn.cursor() as cur:
            # Create schema if not exists
            cur.execute("CREATE SCHEMA IF NOT EXISTS cyberspace;")

            # Create easter eggs table
            cur.execute("""
                CREATE TABLE IF NOT EXISTS cyberspace.easter_eggs (
                    id VARCHAR(100) PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    description TEXT,
                    category VARCHAR(50),
                    difficulty VARCHAR(20),
                    discovery_methods JSONB,
                    rewards JSONB,
                    hints JSONB,
                    lore_connections JSONB,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                );
            """)

            # Create indexes
            cur.execute("""
                CREATE INDEX IF NOT EXISTS idx_easter_eggs_category
                ON cyberspace.easter_eggs(category);
            """)

            cur.execute("""
                CREATE INDEX IF NOT EXISTS idx_easter_eggs_difficulty
                ON cyberspace.easter_eggs(difficulty);
            """)

            print("SUCCESS: Database tables created")
            conn.commit()

    except Exception as e:
        print(f"ERROR: Failed to create tables: {e}")
        conn.rollback()

def import_easter_eggs(conn, easter_eggs: List[EasterEgg]):
    """Import easter eggs into database"""
    try:
        with conn.cursor() as cur:
            for egg in easter_eggs:
                # Insert or update easter egg
                cur.execute("""
                    INSERT INTO cyberspace.easter_eggs
                    (id, name, description, category, difficulty, discovery_methods, rewards, hints, lore_connections)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                    ON CONFLICT (id) DO UPDATE SET
                        name = EXCLUDED.name,
                        description = EXCLUDED.description,
                        category = EXCLUDED.category,
                        difficulty = EXCLUDED.difficulty,
                        discovery_methods = EXCLUDED.discovery_methods,
                        rewards = EXCLUDED.rewards,
                        hints = EXCLUDED.hints,
                        lore_connections = EXCLUDED.lore_connections,
                        updated_at = CURRENT_TIMESTAMP;
                """, (
                    egg.id, egg.name, egg.description, egg.category, egg.difficulty,
                    psycopg2.extras.Json(egg.discovery_methods),
                    psycopg2.extras.Json(egg.rewards),
                    psycopg2.extras.Json(egg.hints),
                    psycopg2.extras.Json(egg.lore_connections)
                ))

                print(f"Imported easter egg: {egg.name}")

            conn.commit()
            print(f"SUCCESS: Imported {len(easter_eggs)} easter eggs")

    except Exception as e:
        print(f"ERROR: Failed to import easter eggs: {e}")
        conn.rollback()

def main():
    """Main import function"""
    print("=== Direct Cyberspace Easter Eggs Import Script ===")

    # Load YAML content
    yaml_data = load_yaml_content()
    if not yaml_data:
        return 1

    # Parse easter eggs
    easter_eggs = parse_easter_eggs(yaml_data)
    if not easter_eggs:
        print("ERROR: No easter eggs found to import")
        return 1

    print(f"Found {len(easter_eggs)} easter eggs to import")

    # Database connection
    db_config = {
        'host': 'localhost',
        'port': 5432,
        'database': 'necpgame',
        'user': 'postgres',
        'password': 'postgres'
    }

    try:
        conn = psycopg2.connect(**db_config)
        print("SUCCESS: Connected to database")

        # Create tables
        create_database_tables(conn)

        # Import data
        import_easter_eggs(conn, easter_eggs)

        conn.close()
        print("SUCCESS: Direct import completed successfully")

    except Exception as e:
        print(f"ERROR: Database connection failed: {e}")
        return 1

    return 0

if __name__ == '__main__':
    sys.exit(main())
