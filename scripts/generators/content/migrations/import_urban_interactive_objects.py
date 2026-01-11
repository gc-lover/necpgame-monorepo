#!/usr/bin/env python3
"""
Import urban interactive objects to database
Generates and imports urban interactive objects based on content definitions
"""

import os
import sys
import psycopg2
import yaml
import json
import uuid
from pathlib import Path
from datetime import datetime
from typing import Dict, Any, List
import random

def get_random_position_in_night_city() -> Dict[str, float]:
    """Generate random position within Night City bounds"""
    # Approximate Night City (Los Angeles) coordinates
    # Longitude: -118.5 to -118.1 (roughly)
    # Latitude: 33.9 to 34.1 (roughly)
    lon = random.uniform(-118.5, -118.1)
    lat = random.uniform(33.9, 34.1)

    return {
        'longitude': round(lon, 6),
        'latitude': round(lat, 6)
    }

def generate_urban_interactive_objects(content_data: Dict[str, Any]) -> List[Dict[str, Any]]:
    """Generate specific interactive objects from content definitions"""
    objects = []

    # Night City zone ID (placeholder - should be real zone from database)
    night_city_zone_id = "550e8400-e29b-41d4-a716-446655440000"

    for interactive in content_data.get('interactives', []):
        name = interactive['name']
        display_name = interactive.get('display_name', name)
        category = interactive['category']

        # Map content categories to database object types
        object_type_mapping = {
            'information_access': 'street_terminal',
            'data_storage': 'security_terminal',
            'data_gateway': 'security_terminal',
            'power_control': 'security_terminal',
            'container': 'cargo_container',
            'door': 'access_door',
            'terminal': 'street_terminal',
            'vehicle': 'delivery_drone',
            'device': 'security_terminal',
            'decoration': 'ar_billboard'
        }

        object_type = object_type_mapping.get(category, 'street_terminal')

        # Generate 5-10 instances of each type
        instances_count = random.randint(5, 10)

        for i in range(instances_count):
            position = get_random_position_in_night_city()

            # Generate zone-specific data based on object type
            zone_specific_data = {}
            state = 'active'
            health = 100
            interaction_type = 'single_use'
            access_level = 'public'

            if object_type == 'street_terminal':
                zone_specific_data = {
                    'terminal_type': random.choice(['news', 'map', 'service']),
                    'hack_difficulty': random.randint(1, 3),
                    'alarm_risk': random.randint(10, 30)
                }
                interaction_type = 'reusable'
                access_level = 'public'

            elif object_type == 'ar_billboard':
                zone_specific_data = {
                    'advertisement_type': random.choice(['corporate', 'local', 'underground']),
                    'update_frequency': random.randint(300, 1800),  # seconds
                    'has_hidden_content': random.choice([True, False])
                }
                interaction_type = 'reusable'
                access_level = 'public'

            elif object_type == 'access_door':
                zone_specific_data = {
                    'security_level': random.randint(1, 5),
                    'access_methods': ['keycard', 'biometric', 'hack'],
                    'leads_to': random.choice(['apartment', 'office', 'storage', 'restricted_area'])
                }
                access_level = random.choice(['public', 'restricted', 'corporate'])
                state = random.choice(['active', 'locked', 'damaged'])

            elif object_type == 'delivery_drone':
                zone_specific_data = {
                    'cargo_type': random.choice(['food', 'medicine', 'electronics', 'contraband']),
                    'delivery_priority': random.randint(1, 5),
                    'flight_path': f"drone_path_{random.randint(1, 100)}"
                }
                interaction_type = 'timed'
                access_level = 'restricted'

            elif object_type == 'garbage_chute':
                zone_specific_data = {
                    'depth': random.randint(5, 50),
                    'toxicity_level': random.randint(1, 5),
                    'has_hidden_compartment': random.choice([True, False])
                }
                access_level = 'public'
                state = random.choice(['active', 'blocked', 'overflowing'])

            elif object_type == 'security_camera':
                zone_specific_data = {
                    'coverage_angle': random.randint(90, 360),
                    'detection_range': random.randint(20, 100),
                    'recording_quality': random.choice(['HD', '4K', 'thermal'])
                }
                interaction_type = 'reusable'
                access_level = 'restricted'

            # Randomize some properties
            if random.random() < 0.1:  # 10% chance
                state = random.choice(['damaged', 'hacked', 'inactive'])
                health = random.randint(10, 90)

            # Generate interaction effects and consequences
            interaction_effects = {}
            failure_consequences = {}

            if object_type in ['street_terminal', 'security_terminal']:
                interaction_effects = {
                    'data_access': True,
                    'temporary_buff': random.choice(['none', 'stealth_boost', 'hack_bonus'])
                }
                failure_consequences = {
                    'alarm_triggered': random.choice([True, False]),
                    'security_alert': random.choice([True, False])
                }

            obj = {
                'id': str(uuid.uuid4()),
                'object_type': object_type,
                'object_subtype': f"{name}_{i+1}",
                'zone_id': night_city_zone_id,
                'position': f"POINT({position['longitude']} {position['latitude']})",
                'rotation': round(random.uniform(0, 360), 1),
                'zone_name': 'Night City - Urban District',
                'state': state,
                'health': health,
                'max_health': 100,
                'interaction_type': interaction_type,
                'cooldown_seconds': random.randint(0, 300),
                'access_level': access_level,
                'zone_specific_data': json.dumps(zone_specific_data),
                'interaction_effects': json.dumps(interaction_effects),
                'failure_consequences': json.dumps(failure_consequences),
                'created_at': datetime.now().isoformat(),
                'updated_at': datetime.now().isoformat()
            }

            objects.append(obj)

    return objects

def import_objects_to_database(objects: List[Dict[str, Any]], conn_params: Dict[str, str]):
    """Import generated objects to database"""
    try:
        conn = psycopg2.connect(**conn_params)
        cursor = conn.cursor()

        # Create schema if not exists
        cursor.execute("CREATE SCHEMA IF NOT EXISTS interactive")

        # Insert objects
        insert_query = """
        INSERT INTO interactive.interactive_objects (
            id, object_type, object_subtype, zone_id, position, rotation, zone_name,
            state, health, max_health, interaction_type, cooldown_seconds,
            access_level, zone_specific_data, interaction_effects, failure_consequences,
            created_at, updated_at
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ON CONFLICT (id) DO NOTHING
        """

        inserted_count = 0
        for obj in objects:
            values = (
                obj['id'], obj['object_type'], obj['object_subtype'], obj['zone_id'],
                obj['position'], obj['rotation'], obj['zone_name'], obj['state'],
                obj['health'], obj['max_health'], obj['interaction_type'],
                obj['cooldown_seconds'], obj['access_level'], obj['zone_specific_data'],
                obj['interaction_effects'], obj['failure_consequences'],
                obj['created_at'], obj['updated_at']
            )

            cursor.execute(insert_query, values)
            inserted_count += 1

            if inserted_count % 50 == 0:
                print(f"[PROGRESS] Imported {inserted_count} objects...")

        conn.commit()
        print(f"[SUCCESS] Imported {inserted_count} urban interactive objects to database")

        cursor.close()
        conn.close()

        return True

    except Exception as e:
        print(f"[ERROR] Failed to import objects: {e}")
        if 'conn' in locals():
            conn.rollback()
            conn.close()
        return False

def main():
    print("[START] Importing urban interactive objects to database")

    # Load urban interactives content
    content_file = Path("knowledge/content/interactives/urban-interactives.yaml")
    if not content_file.exists():
        print(f"[ERROR] Content file not found: {content_file}")
        return False

    try:
        with open(content_file, 'r', encoding='utf-8') as f:
            content_data = yaml.safe_load(f)

        content_section = content_data.get('content', {})
        print(f"[INFO] Loaded content data with {len(content_section.get('interactives', []))} interactive types")

        # Generate objects
        objects = generate_urban_interactive_objects(content_section)
        print(f"[INFO] Generated {len(objects)} interactive objects")

        # Database connection
        conn_params = {
            'host': os.getenv('DB_HOST', 'localhost'),
            'port': os.getenv('DB_PORT', '5432'),
            'database': os.getenv('DB_NAME', 'necpgame'),
            'user': os.getenv('DB_USER', 'postgres'),
            'password': os.getenv('DB_PASSWORD', 'password')
        }

        # For testing - output generated objects
        if os.getenv('DRY_RUN', 'false').lower() == 'true':
            print("[DRY RUN] Generated objects:")
            print(json.dumps(objects[:5], indent=2, ensure_ascii=False))  # Show first 5
            print(f"[DRY RUN] Total objects generated: {len(objects)}")
            return True

        # Import to database
        success = import_objects_to_database(objects, conn_params)

        if success:
            print("[SUCCESS] Urban interactive objects import completed successfully")
            return True
        else:
            print("[ERROR] Urban interactive objects import failed")
            return False

    except Exception as e:
        print(f"[ERROR] Import process failed: {e}")
        return False

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)